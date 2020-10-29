package python3;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.jpp.astnodes.ast.ImportFrom;
import python3.pyerrors.NodeNotFoundException;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.jpp.PyASTParser;
import org.jpp.astnodes.PythonTree;
import org.jpp.astnodes.ast.Import;
import org.jpp.astnodes.base.mod;
import python3.pyerrors.ExpressionNotFound;
import python3.typeinference.core.TypeASTNode;
import python3.typeinference.core.TypeStringToJDT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PythonASTUtil {
    static Logger logger = Logger.getLogger(PythonASTUtil.class);
    private Map<TypeASTNode, String> typeinformation;
    public PyCompilationUnit parseSource(String content, Map<TypeASTNode, String> typeinfo) {
        mod ast = PyASTParser.parsePython(content);
        logger.debug(ast.toStringTree());

        this.typeinformation=typeinfo;
        return createPyCompilationUnit(ast);
    }


    private PyCompilationUnit createPyCompilationUnit(mod ast){
        Map<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
        AST asn = new AST(options);
        PyCompilationUnit pyc = new PyCompilationUnit(asn);

        MapPyStatementsTOJDK pyStatementsTOJDK = new MapPyStatementsTOJDK(this.typeinformation);
        PyMap.totalCharGains=0;
        PyMap.currentMethodGain=0;
        HashMap<String, Name> import_nodes = getImportsAndAlias(ast, asn);
        logger.debug("Import and Alias Names : "+import_nodes);
        int startChar = 0;
        TypeDeclaration dummyClass = asn.newTypeDeclaration();
        Modifier.ModifierKeyword keyword= new Modifier.ModifierKeyword("public",1);
        Modifier modifier = asn.newModifier(keyword);
        dummyClass.setModifier(modifier);
        SimpleName simpleName = asn.newSimpleName("PyDummyClass");
        dummyClass.setName(simpleName);
        ArrayList<ASTNode> global_stmts = new ArrayList<ASTNode>();

        for (PythonTree ch : ast.getChildren()){
            logger.debug(ch.toString());
            try {
                ArrayList<?> nodes = pyStatementsTOJDK.getMappingPyNode(asn,ch,import_nodes,startChar, pyc);
                for (Object node : Objects.requireNonNull(nodes)) {
                    logger.debug(node.toString());
                    startChar+=node.toString().length();
                    logger.debug("Start Char : "+startChar);
                    if (node instanceof ImportDeclaration)
                    {
                        pyc.setImport((ImportDeclaration) node);
                    }
                    else if (node instanceof TypeDeclaration)
                    {
                        pyc.setTypes((TypeDeclaration) node);
                    }

//                    else if (node instanceof MethodDeclaration){
//                        pyc.setTypes(node);
//                    }
                    else if (node instanceof MethodDeclaration){
                        dummyClass.bodyDeclarations().add(node);
                    }
                    else if (node instanceof ExpressionStatement && ((ExpressionStatement) node).getExpression() instanceof Assignment && ((Assignment) ((ExpressionStatement) node).getExpression()).getLeftHandSide() instanceof SimpleName) {
                        VariableDeclarationFragment variableDeclarationFragment = asn.newVariableDeclarationFragment();
                        variableDeclarationFragment.setName(asn.newSimpleName(((SimpleName) ((Assignment) ((ExpressionStatement) node).getExpression()).getLeftHandSide()).getIdentifier()));
                        variableDeclarationFragment.setInitializer((Expression) ASTNode.copySubtree(asn, ((Assignment) ((ExpressionStatement) node).getExpression()).getRightHandSide()));
                        FieldDeclaration fieldDeclaration = asn.newFieldDeclaration(variableDeclarationFragment);
                        String typeString = typeinformation.get(new TypeASTNode((((Assignment) ((ExpressionStatement) node).getExpression()).getLeftHandSide()).getPyStartPosition(),
                                ((Assignment) ((ExpressionStatement) node).getExpression()).getLeftHandSide().getPyColumnOffSet(), ((SimpleName) ((Assignment) ((ExpressionStatement) node).getExpression()).getLeftHandSide()).getIdentifier(), null));
                        Type jdtType = TypeStringToJDT.getJDTType(asn, typeString, 0);
                        fieldDeclaration.setType(jdtType);
                        fieldDeclaration.modifiers().add(asn.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
                        fieldDeclaration.modifiers().add(asn.newModifier(Modifier.ModifierKeyword.STATIC_KEYWORD));
                        global_stmts.add(fieldDeclaration);
                        System.out.println(node);
                    }
                    else {
                        logger.fatal("Not implemented statement "+node+ node.toString());
                    }
                }
            } catch (NodeNotFoundException | ExpressionNotFound e) {
                e.printStackTrace();
            }

        }
        if (dummyClass.bodyDeclarations().size()>0){
            pyc.setTypes(dummyClass);
        }
        pyc.types().stream().filter(sc -> sc instanceof TypeDeclaration).forEach(x->((TypeDeclaration) x).bodyDeclarations()
                .addAll(global_stmts.stream().map(y->ASTNode.copySubtree(asn,y)).collect(Collectors.toList())));
        pyc.setSourceRange(ast.getCharStartIndex(),ast.getCharStopIndex()+PyMap.totalCharGains-ast.getCharStartIndex());

        return pyc;
    }

    public HashMap<String, Name> getImportsAndAlias(mod ast, AST asn) {
        MapPyStatementsTOJDK pyStatementsTOJDK = new MapPyStatementsTOJDK(this.typeinformation);
        HashMap<String,Name> import_nodes = new HashMap<>();
        for (PythonTree ch : ast.getChildren()){ //collect import statements to resolve alias names.
            if (ch instanceof Import || ch instanceof ImportFrom){
                try {
                    for (Object o : pyStatementsTOJDK.getMappingPyNode(asn, ch, null,0,null )) {
                        if (((ImportDeclaration)o).getasName() !=null){
                            import_nodes.put(((ImportDeclaration)o).getasName().getFullyQualifiedName(),((ImportDeclaration)o).getName());
                        }
                    }
                } catch (NodeNotFoundException | ExpressionNotFound e) {
                    logger.error(e);
                }
            }
        }
        return import_nodes;
    }


    public static List<Import> getImportStatements(PythonTree tree){
//        PyVisitor importVisitor = new PyVisitor();
//        try {
//            tree.accept(importVisitor);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        ArrayList<Import> imports = new ArrayList<Import>();
        for (PythonTree ch: tree.getChildren()){
            if (ch instanceof Import) {
                imports.add((Import)ch);
                AST asn = new AST();
                ImportDeclaration jdtimport = new ImportDeclaration(asn);
            }

        }
        return imports;
    }

}
