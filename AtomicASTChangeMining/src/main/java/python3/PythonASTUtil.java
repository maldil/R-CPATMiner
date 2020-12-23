package python3;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PyInExpression;
import org.eclipse.jdt.core.dom.PyTupleExpression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.jpp.astnodes.Visitor;
import org.jpp.astnodes.ast.ClassDef;
import org.jpp.astnodes.ast.FunctionDef;
import org.jpp.astnodes.ast.ImportFrom;
import org.jpp.astnodes.base.expr;
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
import python3.typeinference.core.TypeApproximator;
import python3.typeinference.core.TypeStringToJDT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PythonASTUtil {
    static Logger logger = Logger.getLogger(PythonASTUtil.class);
    private Map<TypeASTNode, String> typeinformation=new HashMap<>();;
    public PyCompilationUnit parseSource(String content, Map<TypeASTNode, String> typeinfo) {
        mod ast = PyASTParser.parsePython(content);
        logger.debug(ast.toStringTree());

        this.typeinformation=typeinfo;
        return createPyCompilationUnit(ast);
    }


    public PyCompilationUnit createPyCompilationUnit(mod ast){
        Map<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
        AST asn = new AST(options);
        PyASTVisitor pyNodeCounter = new PyASTVisitor();  //This is to count the number of classes and methods,
                                                            // if it is zero only one Dummy class will be created
        try {
            pyNodeCounter.visit(ast);           //This could be a performance overhead
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        SimpleName simpleName = asn.newSimpleName("PyDummyClass1");
        dummyClass.setName(simpleName);
        ArrayList<ASTNode> global_stmts = new ArrayList<ASTNode>();
        Object currentCursor = null;
        TypeDeclaration currentHolder = null;
        TypeDeclaration otherCurrentHolder = null;
        int number_of_dummy_classes = 1;
        int number_of_dummy_methods = 1;
        for (PythonTree ch : ast.getChildren()){
            logger.debug(ch.toString());
            try {
                ArrayList<?> nodes = pyStatementsTOJDK.getMappingPyNode(asn,ch,import_nodes,startChar, pyc);
                int nodeNumber=0;

                for (Object node : Objects.requireNonNull(nodes)) {

                    logger.debug(node.toString());
                    startChar+=node.toString().length();
                    logger.debug("Start Char : "+startChar);
                    if (node instanceof ImportDeclaration)
                    {
                        if (currentHolder!=null && (currentCursor instanceof MethodDeclaration || currentCursor ==null)){
                            pyc.setTypes(currentHolder);
                            currentHolder=null;
                        }

                        pyc.setImport((ImportDeclaration) node);

                    }
                    else if (node instanceof TypeDeclaration)
                    {
                        if (currentHolder!=null && (currentCursor instanceof MethodDeclaration|| currentCursor==null)){
                            pyc.setTypes(currentHolder);
                            currentHolder=null;
                        }
                        if (otherCurrentHolder!=null){
                            pyc.setTypes(otherCurrentHolder);
                            otherCurrentHolder=null;

                        }

                        pyc.setTypes((TypeDeclaration) node);

                    }
//                    else if (node instanceof MethodDeclaration){
//                        pyc.setTypes(node);
//                    }
                    else if (node instanceof MethodDeclaration){
                        if (currentHolder !=null){
                            currentHolder.bodyDeclarations().add(node);
                            //pyc.setTypes(dummyClass);
                        }
                        else {
                            currentHolder=asn.newTypeDeclaration();
                            currentHolder.setModifier(asn.newModifier(new Modifier.ModifierKeyword("public",1)));
                            currentHolder.setName(asn.newSimpleName("PyDummyClass"+number_of_dummy_classes));
                            currentHolder.bodyDeclarations().add(node);
                            number_of_dummy_classes+=1;
                        }

                        if (otherCurrentHolder!=null){
                            pyc.setTypes(otherCurrentHolder);
                            otherCurrentHolder=null;

                        }
                    }
                    else if (node instanceof ExpressionStatement && ((ExpressionStatement) node).getExpression() instanceof
                            Assignment && ((Assignment) ((ExpressionStatement) node).getExpression()).getLeftHandSide() instanceof SimpleName && (pyNodeCounter.classDef+ pyNodeCounter.funcDef!=0)) {
                        VariableDeclarationFragment variableDeclarationFragment = asn.newVariableDeclarationFragment();
                        variableDeclarationFragment.setName(asn.newSimpleName(((SimpleName) ((Assignment) ((ExpressionStatement) node).getExpression()).getLeftHandSide()).getIdentifier()));
                        Expression rightHandSide = ((Assignment) ((ExpressionStatement) node).getExpression()).getRightHandSide();
                        Expression initilizer = (Expression) ASTNode.copySubtree(asn,rightHandSide );
                        variableDeclarationFragment.setInitializer(initilizer);
                        FieldDeclaration fieldDeclaration = asn.newFieldDeclaration(variableDeclarationFragment);
                        Type type = TypeApproximator.getSimpleTypeApproximation(asn, (expr)rightHandSide.getPyObject());
                        if (type==null){
                            String typeString = typeinformation.get(new TypeASTNode((((Assignment) ((ExpressionStatement) node).getExpression()).getLeftHandSide()).getPyStartPosition(),
                                    ((Assignment) ((ExpressionStatement) node).getExpression()).getLeftHandSide().getPyColumnOffSet(), ((SimpleName) ((Assignment)
                                    ((ExpressionStatement) node).getExpression()).getLeftHandSide()).getIdentifier(), null));
                            Type jdtType = TypeStringToJDT.getJDTType(asn, typeString, 0);
                            fieldDeclaration.setType(jdtType);
                        }
                        else{
                            fieldDeclaration.setType(type);
                        }
                        fieldDeclaration.modifiers().add(asn.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
                        fieldDeclaration.modifiers().add(asn.newModifier(Modifier.ModifierKeyword.STATIC_KEYWORD));
                        global_stmts.add(fieldDeclaration);
                        if (otherCurrentHolder!=null){
                            pyc.setTypes(otherCurrentHolder);
                            otherCurrentHolder=null;

                        }

                    }
                    else {
                        if (currentHolder!=null && (currentCursor instanceof MethodDeclaration|| currentCursor==null)){
                            pyc.setTypes(currentHolder);
                            currentHolder=null;
                        }
                        if (!(node instanceof ExpressionStatement && (((ExpressionStatement) node).getExpression() instanceof StringLiteral ||
                                ((ExpressionStatement) node).getExpression() instanceof SimpleName  ||
                                ((ExpressionStatement) node).getExpression() instanceof ArrayAccess ||
                                ((ExpressionStatement) node).getExpression() instanceof FieldAccess ||
                                ((ExpressionStatement) node).getExpression() instanceof PyTupleExpression ||
                                ((ExpressionStatement) node).getExpression() instanceof PyInExpression))
                        ){
                            if (otherCurrentHolder==null){
                                otherCurrentHolder=asn.newTypeDeclaration();
                                otherCurrentHolder.setModifier(asn.newModifier(new Modifier.ModifierKeyword("public",1)));
                                otherCurrentHolder.setName(asn.newSimpleName("PyDummyClass"+number_of_dummy_classes));
                                MethodDeclaration methoddec = asn.newMethodDeclaration();
                                methoddec.setName(asn.newSimpleName(("PyDummyMethod"+number_of_dummy_methods)));
                                methoddec.setBody(asn.newBlock());
                                methoddec.getBody().statements().add(node);
                                otherCurrentHolder.bodyDeclarations().add(methoddec);
                                number_of_dummy_classes+=1;
                                number_of_dummy_methods+=1;
                            }
                            else{
                                ((MethodDeclaration)otherCurrentHolder.bodyDeclarations().get(0)).getBody().statements().add(node);
                            }
                        }
//                        logger.fatal("Not implemented statement "+node+ node.toString());
                    }
                    currentCursor = node;
                }
            } catch (NodeNotFoundException | ExpressionNotFound e) {
                e.printStackTrace();
            }

        }
        if (currentHolder!=null && currentHolder.bodyDeclarations().size()>0){
            pyc.setTypes(currentHolder);
        }
        if (otherCurrentHolder!=null && otherCurrentHolder.bodyDeclarations().size()>0){
            pyc.setTypes(otherCurrentHolder);
        }

        pyc.types().stream().filter(sc -> sc instanceof TypeDeclaration).forEach(x->((TypeDeclaration) x).bodyDeclarations()
                .addAll(global_stmts.stream().map(y->ASTNode.copySubtree(asn,y)).collect(Collectors.toList())));

        pyc.types().stream().filter(sc -> sc instanceof TypeDeclaration).forEach(x->((TypeDeclaration) x).bodyDeclarations()
                .addAll(pyc.getGlobal_variables().stream().map(y->ASTNode.copySubtree(asn,y)).collect(Collectors.toList())));



        pyc.setSourceRange(ast.getCharStartIndex(),ast.getCharStopIndex()+PyMap.totalCharGains-ast.getCharStartIndex());

        return pyc;
    }

    class PyASTVisitor extends Visitor {
        private int classDef= 0;
        private int funcDef= 0;
        @Override
        public Object visitClassDef(ClassDef node) throws Exception {
            classDef+=1;
            return super.visitClassDef(node);
        }
        @Override
        public Object visitFunctionDef(FunctionDef node) throws Exception {
            classDef+=1;
            return super.visitFunctionDef(node);
        }
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
