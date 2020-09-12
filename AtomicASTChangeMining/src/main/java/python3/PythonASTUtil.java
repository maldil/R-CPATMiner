package python3;

import com.google.common.annotations.VisibleForTesting;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.jpp.astnodes.ast.ImportFrom;
import org.jpp.astnodes.base.stmt;
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
import python3.typeinference.core.TypeInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        Map options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
        AST asn = new AST(options);
        PyCompilationUnit pyc = new PyCompilationUnit(asn);
        MapPyStatementsTOJDK pyStatementsTOJDK = new MapPyStatementsTOJDK(this.typeinformation);
        HashMap<String, String> import_nodes = getImportsAndAlias(ast, asn);
        logger.debug("Import and Alias Names : "+import_nodes);
        for (PythonTree ch : ast.getChildren()){
            logger.debug(ch.toString());
            try {
                ArrayList<?> nodes = pyStatementsTOJDK.getMappingPyNode(asn,ch,import_nodes);
                for (Object node : Objects.requireNonNull(nodes)) {
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
                    else {
                        logger.fatal("Not implemented statement "+node+ node.toString());
                    }
                }
            } catch (NodeNotFoundException | ExpressionNotFound e) {
                e.printStackTrace();
            }

        }
        return pyc;
    }

    public HashMap<String, String> getImportsAndAlias(mod ast, AST asn) {
        MapPyStatementsTOJDK pyStatementsTOJDK = new MapPyStatementsTOJDK(this.typeinformation);
        HashMap<String,String> import_nodes = new HashMap<>();
        for (PythonTree ch : ast.getChildren()){ //collect import statements to resolve alias names.
            if (ch instanceof Import || ch instanceof ImportFrom){
                try {
                    for (Object o : pyStatementsTOJDK.getMappingPyNode(asn, ch, null)) {
                        if (((ImportDeclaration)o).getasName() !=null){
                            import_nodes.put(((ImportDeclaration)o).getasName().getFullyQualifiedName(),((ImportDeclaration)o).getName().getFullyQualifiedName());
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
