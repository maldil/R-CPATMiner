package change;

import graph.PDGBuildingContext;
import graph.PDGGraph;
import graphics.DotGraph;
import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.jpp.PyASTParser;
import org.jpp.astnodes.base.mod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import python3.JDTASTVisitor;
import python3.PyCompilationUnit;
import python3.PythonASTUtil;
import python3.typeinference.PyASTVisitor;
import utils.JavaASTUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.testng.Assert.*;

public class CMethodTest {
    //Draw PDG for a code
    Repository repository;
    String url =  "/Users/malinda/Documents/Research_Topic_2/DATA_FOR_CPATMiner/PDG/";
    @Test
    public void testGetChangeGraph1() {
        String content = "def for_loop():\n" +
                "    foo = (boo[line]  for line in page if line>0 for goo in line)";
        drawPGD(content);
    }

    @Test
    public void testGetChangeGraph2() {
        String content = "def for_loop():\n" +
                "    foo = (boo[line]  for line in page if line>0 for goo in line if goo>0 )";
        drawPGD(content);
    }

    @Test
    public void testGetChangeGraph3() {
        String content = "def for_loop():\n" +
                "    foo = (line for line in page if DUMMY for goo in line)";
        drawPGD(content);
    }

    @Test
    public void testGetChangeGraph4() {
        String content = "def for_loop():\n" +
                "    foo,coo,zoo = boo()";
        drawPGD(content);
    }



    @Test
    public void testGetChangeGraph5() {
        String content = "def for_loop():\n" +
                "    koo[1] = boo()";
        drawPGD(content);
    }

    @Test
    public void testGetChangeGraph6() {
        String content = "def for_loop():\n" +
                "    koo[1] = [(1,2,3,4),(5,7,3,4)]";
        drawPGD(content);
    }

    @Test
    public void testGetChangeGraph7() {
        String content = "def for_loop():\n" +
                "    koo[1] +=1";
        drawPGD(content);
    }

    @Test
    public void testGetChangeGraph8() {
        String content = "def for_loop():\n" +
                "    foo = {line for line in page if line>0}";
        drawPGD(content);
    }

    @Test
    public void testGetChangeGraph9() {
        String content = "def for_loop():\n" +
                "    foo = [line for line in page if line>0 for goo in line]";
        drawPGD(content);
    }

    @Test
    public void testGetChangeGraph10() {
        String content = "def for_loop():\n" +
                "    foo = {\"one\":1,\"two\":2}";
        drawPGD(content);
    }

    @Test
    public void testGetChangeGraph11() {
        String content = "def for_loop():\n" +
                "    foo = {line:page[goo] for line in page  for goo in line}";
        drawPGD(content);
    }



    @Test
    public void testGetChangeGraph12() {
        String content = "def inner():\n" +
                "    nonlocal x\n" +
                "    x = 2\n" +
                "    print(\"inner:\", x)";
        drawPGD(content);
    }

    @Test
    public void testGetChangeGraph13() {
        String content = "def simpleGeneratorFun(): \n" +
                "    yield 1\n" +
                "    yield 2\n" +
                "    yield 3\n" +
                "  ";
        drawPGD(content);
    }

    @Test
    public void testGetChangeGraph14() {
        String content = "def for_loop():\n" +
                "    x **= 3";
        drawPGD(content);
    }

    @Test
    public void testGetChangeGraph15() {
        String content = "def for_loop():\n" +
                "    x = 5** 3";
        drawPGD(content);
    }

    @Test
    public void testGetChangeGraph16() {
        String content = "def for_loop():\n" +
                "    x = 5** 3";
        drawPGD(content);
    }

    @Test
    public void testGetChangeGraph17() {
        String content = "def for_loop():\n" +
                "    koo[1] = [(),()]";
        drawPGD(content);
    }

    @Test
    public void testGetChangeGraph18() {
        String content = "def for_loop():\n" +
                "    foo = {line for line in page  for goo in line}";
        drawPGD(content);
    }

    @Test
    public void testGetChangeGraph19() {
        String content = "for item in container:\n" +
                "        process(item)\n" +
                "else:\n" +
                "    not_found_in_container()";
        drawPGD(content);
    }




    private void drawPGD(String code){
        CompilationUnit cuUnit = convert(code);
        MethodDeclaration declaration = getMethodDeclaration(cuUnit);
        PDGBuildingContext context = new PDGBuildingContext(repository, null, "", false, cuUnit.imports());
        PDGGraph pdg1 = new PDGGraph(declaration, context);
        pdg1.buildChangeGraph(0);
        DotGraph dg = new DotGraph(pdg1);
        String dirPath = "./OUTPUT/DEBUG/";
        dg.toDotFile(new File(dirPath  +"file___"+".dot"));
    }

    private MethodDeclaration getMethodDeclaration(CompilationUnit cuUnit){
        return (MethodDeclaration)((TypeDeclaration)cuUnit.types().get(0)).bodyDeclarations().get(0);
    }

    @BeforeClass
    private boolean getRepository(){
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {

            repository = builder.setGitDir(new File(url)).readEnvironment() // scan
                    // environment
                    // GIT_*
                    // variables
                    .findGitDir() // scan up the file system tree
                    .build();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
        try {
            if (repository.getBranch() == null)
                return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    protected CompilationUnit convert(String content) {
//        String content = readFile(fileName);
        mod ast = PyASTParser.parsePython(content);
        PythonASTUtil pythonASTUtil = new PythonASTUtil();
        PyCompilationUnit pyCompilationUnit = pythonASTUtil.createPyCompilationUnit(ast);
        PyASTVisitor astParser = new PyASTVisitor();
        try {
            astParser.visit(ast);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CompilationUnit compilationUnit = (CompilationUnit) JavaASTUtil.parseSource(pyCompilationUnit.toString());
        return compilationUnit;
    }

    public String readFile(String fileName) {
        Path resourceDirectory = Paths.get(url, fileName);
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