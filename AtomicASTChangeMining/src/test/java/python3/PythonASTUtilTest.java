package python3;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Name;
import org.jpp.PyASTParser;
import org.jpp.astnodes.base.mod;
import org.testng.Assert;
import org.testng.annotations.Test;
import python3.typeinference.core.TypeASTNode;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

public class PythonASTUtilTest {
    private static org.apache.log4j.Logger log = Logger.getLogger(LineCounterTestASTVisitor.class);
    @Test
    public void testGetImportStatements1()  {
        String content = "import numpy as np" + "\n" + "from tensorflow.boo import foo as fo" + "\n";
        mod ast = PyASTParser.parsePython(content);
        PythonASTUtil pythonASTUtil = new PythonASTUtil();
        HashMap<String, Name> importsAndAlias = pythonASTUtil.getImportsAndAlias(ast, new AST());
        Assert.assertEquals(importsAndAlias.get("np").getFullyQualifiedName(),"numpy");
        Assert.assertEquals(importsAndAlias.get("fo").getFullyQualifiedName(),"tensorflow.boo.foo");
    }


    @Test
    public void testParseSource1() {
        String content = "import numpy as np\n" +
                "class Test ():\n" +
                "\tdef add_arrays(self,M):\n" +
                "\t\tresult = np.sum(np.array(M),axis=1)";
        PythonASTUtil pythonASTUtil = new PythonASTUtil();
        HashMap<TypeASTNode, String> typeASTNodeStringHashMap = new HashMap<>();
        typeASTNodeStringHashMap.put(new TypeASTNode(4,2,"result",null),"Any" );
        PyCompilationUnit pyCompilationUnit = pythonASTUtil.parseSource(content,typeASTNodeStringHashMap);
        Assert.assertEquals(pyCompilationUnit.toString(),"import numpy;\n" +
                "public class Test {\n" +
                "  void add_arrays(  Test self){\n" +
                "    Any result;\n" +
                "    result=numpy.sum(numpy.array(M),1);\n" +
                "  }\n" +
                "}\n");

    }

    @Test
    public void testParseSource2() {
        String content = "import numpy as np\n" +
                "class Test ():\n" +
                "\tdef add_arrays(self,M):\n" +
                "\t\txxx= aaa.bbb.ccc.ddd\n" +
                "\t\tresult = np.sum.com.som.lom(np.array(M),axis=1)\n";
        PythonASTUtil pythonASTUtil = new PythonASTUtil();
        HashMap<TypeASTNode, String> typeASTNodeStringHashMap = new HashMap<>();
        typeASTNodeStringHashMap.put(new TypeASTNode(4,2,"result",null),"Any" );
        PyCompilationUnit pyCompilationUnit = pythonASTUtil.parseSource(content,typeASTNodeStringHashMap);
        Assert.assertEquals(pyCompilationUnit.toString(),"import numpy;\n" +
                "public class Test {\n" +
                "  void add_arrays(  Test self){\n" +
                "    xxx=aaa.bbb.ccc.ddd;\n" +
                "    result=numpy.sum.com.som.lom(numpy.array(M));\n" +
                "  }\n" +
                "}\n");

    }

    @Test
    public void testParseSource3() {
        String content = "import numpy.boo as np\n" +
                "class Test ():\n" +
                "\tdef add_arrays(self,M):\n" +
                "\t\tresult = np.sum.com.som.lom(np.thh.array(M),axis=1)";
        PythonASTUtil pythonASTUtil = new PythonASTUtil();
        HashMap<TypeASTNode, String> typeASTNodeStringHashMap = new HashMap<>();
        typeASTNodeStringHashMap.put(new TypeASTNode(4,2,"result",null),"Any" );
        PyCompilationUnit pyCompilationUnit = pythonASTUtil.parseSource(content,typeASTNodeStringHashMap);
        Assert.assertEquals(pyCompilationUnit.toString(),"import numpy.boo;\n" +
                "public class Test {\n" +
                "  void add_arrays(  Test self){\n" +
                "    Any result;\n" +
                "    result=numpy.boo.sum.com.som.lom(numpy.boo.thh.array(M));\n" +
                "  }\n" +
                "}\n");
    }

    @Test
    public void testParseSource4() { //Type conversion test
        String content = "import numpy.boo as np\n" +
                "class Test ():\n" +
                "\tdef add_arrays(self,M):\n" +
                "\t\tresult = np.sum.com.som.lom(np.thh.array(M),axis=1)";
        PythonASTUtil pythonASTUtil = new PythonASTUtil();
        HashMap<TypeASTNode, String> typeASTNodeStringHashMap = new HashMap<>();
        typeASTNodeStringHashMap.put(new TypeASTNode(4,2,"result",null),"List[int]" );
        PyCompilationUnit pyCompilationUnit = pythonASTUtil.parseSource(content,typeASTNodeStringHashMap);
        Assert.assertEquals(pyCompilationUnit.toString(),"import numpy.boo;\n" +
                "public class Test {\n" +
                "  void add_arrays(  Test self){\n" +
                "    int[] result;\n" +
                "    result=numpy.boo.sum.com.som.lom(numpy.boo.thh.array(M));\n" +
                "  }\n" +
                "}\n");

    }

    @Test
    public void testParseSource5() { //Type conversion test
        String content = "import numpy.boo as np\n" +
                "class Test ():\n" +
                "\tdef add_arrays(self,M):\n" +
                "\t\tresult = np.sum.com.som.lom(np.thh.array(M),axis=1)";
        PythonASTUtil pythonASTUtil = new PythonASTUtil();
        HashMap<TypeASTNode, String> typeASTNodeStringHashMap = new HashMap<>();
        typeASTNodeStringHashMap.put(new TypeASTNode(4,2,"result",null),"numpy.ndarray" );
        PyCompilationUnit pyCompilationUnit = pythonASTUtil.parseSource(content,typeASTNodeStringHashMap);
        Assert.assertEquals(pyCompilationUnit.toString(),"import numpy.boo;\n" +
                "public class Test {\n" +
                "  void add_arrays(  Test self){\n" +
                "    numpy.ndarray result;\n" +
                "    result=numpy.boo.sum.com.som.lom(numpy.boo.thh.array(M));\n" +
                "  }\n" +
                "}\n");
    }

    @Test
    public void testCharPosition1(){
        String content = "import numpy.boo as np\n" +
                "from tensorflow import matrix as xp\n" +
                "class Test ():\n" +
                "\tdef add_arrays(self,M):\n" +
                "\t\tresult = np.sum.com.som.lom(np.thh.array(M),axis=1)";

        PythonASTUtil pythonASTUtil = new PythonASTUtil();
        HashMap<TypeASTNode, String> typeASTNodeStringHashMap = new HashMap<>();
        typeASTNodeStringHashMap.put(new TypeASTNode(5,2,"result",null),"numpy.ndarray" );
        PyCompilationUnit pyCompilationUnit = pythonASTUtil.parseSource(content,typeASTNodeStringHashMap);

        LineCounterTestASTVisitor visitor = new LineCounterTestASTVisitor();
        pyCompilationUnit.accept(visitor);
        log.debug("\n"+pyCompilationUnit.toString());
    }

    @Test
    public void testCharPosition2(){
        String content = "import numpy.boo as np\n" +
                "from tensorflow import matrix as xp\n" +
                "class Test ():\n" +
                "\tdef add_arrays(self,M):\n" +
                "\t\tresult = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "\t\tfor x in range(10):\n" +
                "\t\t\tvoo+=1\n";

        PythonASTUtil pythonASTUtil = new PythonASTUtil();
        HashMap<TypeASTNode, String> typeASTNodeStringHashMap = new HashMap<>();
        typeASTNodeStringHashMap.put(new TypeASTNode(5,2,"result",null),"numpy.ndarray" );
        PyCompilationUnit pyCompilationUnit = pythonASTUtil.parseSource(content,typeASTNodeStringHashMap);

        LineCounterTestASTVisitor visitor = new LineCounterTestASTVisitor();
        pyCompilationUnit.accept(visitor);
        log.debug("\n"+pyCompilationUnit.toString());
    }

    @Test
    public void testCharPosition3(){
        String content = "import numpy.boo as np\n" +
                "import aaa.boo as np,zooo as z\n" +
                "from tensorflow import matrix as xp\n" +
                "class Test1 ():\n" +
                "\tdef add_arrays1(self,M):\n" +
                "\t\tresult = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "\t\tfor x in range(10):\n" +
                "\t\t\tvoo+=1\n" +
                "\n" +
                "class Test2 (Boo):\n" +
                "\tdef add_arrays2(self,M):\n" +
                "\t\tresult = np.sum.com.som.lom2(np.thh.array(M),axis=1)\n" +
                "\t\tfor x in range(10):\n" +
                "\t\t\tvoo+=1\n"+
                "class Test3 ():\n" +
                "\tdef add_arrays3(self,M):\n" +
                "\t\tprint('nice')\n" +
                "\tdef add_arrays4(self,M):\n" +
                "\t\tprint('nice')\n"+
                "\tdef add_arrays5(self,M):\n" +
                "\t\tprint('nice')\n"+
                "\tdef add_arrays6():\n" +
                "\t\tprint('nice')\n"+
                "\t\tresult1 = np.sum.com.som.lom2(np.thh.array(M),axis=1)\n" +
                "\t\tresult2 = np.sum.com.som.lom2(np.thh.array(M),axis=1)\n";

        PythonASTUtil pythonASTUtil = new PythonASTUtil();
        HashMap<TypeASTNode, String> typeASTNodeStringHashMap = new HashMap<>();
        typeASTNodeStringHashMap.put(new TypeASTNode(6,2,"result",null),"numpy.ndarray" );
        typeASTNodeStringHashMap.put(new TypeASTNode(12,2,"result",null),"List[int]" );
        typeASTNodeStringHashMap.put(new TypeASTNode(24,2,"result1",null),"List[List[int]]" );
        typeASTNodeStringHashMap.put(new TypeASTNode(25,2,"result2",null),"numpy.ndarray.foo.dooo" );
        PyCompilationUnit pyCompilationUnit = pythonASTUtil.parseSource(content,typeASTNodeStringHashMap);
        log.debug(pyCompilationUnit.toString());
        LineCounterTestASTVisitor visitor = new LineCounterTestASTVisitor();
        pyCompilationUnit.accept(visitor);
        log.debug("\n"+pyCompilationUnit.toString());
    }


}