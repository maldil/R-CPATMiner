package treed;

import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.testng.annotations.Test;
import python3.PythonASTUtil;
import utils.FileIO;
import utils.JavaASTUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.testng.Assert.*;

public class TreedMapperTest {
    @Test
    public void testTreeDif1(){
        PythonASTUtil astUtil = new PythonASTUtil();
        CompilationUnit pyCompileUnit = astUtil.parseSource(readFile("./src/test/resources/TreeDiff/test1old.py"),new HashMap<>());
        ASTNode astM= JavaASTUtil.parseSource(pyCompileUnit.toString());

        PythonASTUtil astUtil1 = new PythonASTUtil();
        CompilationUnit pyCompileUnit1 =astUtil1.parseSource(readFile("./src/test/resources/TreeDiff/test1new.py"),new HashMap<>());
        ASTNode astN= JavaASTUtil.parseSource(pyCompileUnit1.toString());

        TreedMapper tm = new TreedMapper(astM, astN);
        tm.map(false);
        tm.printChanges();
        System.out.println("Number of changed AST nodes: " + tm.getNumOfChanges());
    }

    public String readFile(String fileName) {
        Path resourceDirectory = Paths.get( fileName);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(String.valueOf(resourceDirectory));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String everything = null;
        try {
            everything = IOUtils.toString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return everything;
    }

}