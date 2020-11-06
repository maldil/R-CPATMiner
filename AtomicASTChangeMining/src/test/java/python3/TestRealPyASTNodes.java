package python3;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.testng.Assert;
import org.testng.annotations.Test;
import python3.typeinference.core.TypeASTNode;
import utils.JavaASTUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class TestRealPyASTNodes {
    private static org.apache.log4j.Logger log = Logger.getLogger(TestRealPyASTNodes.class);
    @Test
    public void file1(){
        String content = readFile("common.py");
        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file2(){
        String content = readFile("commonTest.py");
        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file3(){
        String content = readFile("exception.py");
        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file4(){
        String content = readFile("explorer.py");
        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    private CompilationUnit Convert(String content) {
        HashMap<TypeASTNode, String> typeASTNodeStringHashMap = new HashMap<>();
        PythonASTUtil pythonASTUtil = new PythonASTUtil();
        PyCompilationUnit pyCompilationUnit = pythonASTUtil.parseSource(content,typeASTNodeStringHashMap);
        return (CompilationUnit) JavaASTUtil.parseSource(pyCompilationUnit.toString());
    }

    public String readFile(String fileName) {
        Path resourceDirectory = Paths.get("src","test","resources","ASTConversion",fileName);
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
