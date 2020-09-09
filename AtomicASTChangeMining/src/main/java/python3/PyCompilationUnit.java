package python3;


import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
//import org.jpp.astnodes.AnalyzingParser;

public class PyCompilationUnit extends CompilationUnit{
    PyCompilationUnit(AST ast){
        super(ast);
    }

}
