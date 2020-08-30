package python3;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.jpp.PyASTParser;
import org.jpp.astnodes.PythonTree;
import org.jpp.astnodes.ast.Import;
import org.jpp.astnodes.base.mod;
import python3.PyErrors.ExpressionNotFound;
import python3.PyErrors.NodeNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PythonASTUtil {
    public static PyCompilationUnit parseSource(String content) {
        mod ast = PyASTParser.parsePython(content);
        System.out.println(ast.toStringTree());
        return createPyCompilationUnit(ast);
    }


    private static PyCompilationUnit createPyCompilationUnit(mod ast){
        Map options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
        AST asn = new AST(options);
        PyCompilationUnit pyc = new PyCompilationUnit(asn);

        for (PythonTree ch : ast.getChildren()){
            System.out.printf(ch.toString());
            try {
                ArrayList<?> nodes = MapPyStatementsTOJDK.getMappingPyNode(asn,ch);
                for (Object node : Objects.requireNonNull(nodes)) {
                    if (node instanceof ImportDeclaration)
                    {
                        pyc.setImport((ImportDeclaration) node);
                    }
                    else if (node instanceof TypeDeclaration)
                    {
                        pyc.setTypes((TypeDeclaration) node);
                    }
                }
            } catch (NodeNotFoundException | ExpressionNotFound e) {
                e.printStackTrace();
            }

        }
        return pyc;
    }


    public static List<Import> getImportStatements(PythonTree tree){
//        PyImportVisitor importVisitor = new PyImportVisitor();
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
