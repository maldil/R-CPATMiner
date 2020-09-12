package python3;

import org.eclipse.jdt.core.dom.AST;
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

    @Test
    public void testGetImportStatements1()  {
        String content = "import numpy as np" + "\n" + "from tensorflow.boo import foo as fo" + "\n";
        mod ast = PyASTParser.parsePython(content);
        PythonASTUtil pythonASTUtil = new PythonASTUtil();
        HashMap<String, String> importsAndAlias = pythonASTUtil.getImportsAndAlias(ast, new AST());
        Assert.assertEquals(importsAndAlias.get("np"),"numpy");
        Assert.assertEquals(importsAndAlias.get("fo"),"tensorflow.boo.foo");
    }


    @Test
    public void testParseSource1() {
        String content = "import numpy as np\n" +
                "class Test ():\n" +
                "\tdef add_arrays(self,M):\n" +
                "\t\tresult = np.sum(np.array(M),axis=1)";
        mod ast = PyASTParser.parsePython(content);
        System.out.println(content);
        PythonASTUtil pythonASTUtil = new PythonASTUtil();
        HashMap<TypeASTNode, String> typeASTNodeStringHashMap = new HashMap<>();
        typeASTNodeStringHashMap.put(new TypeASTNode(4,2,"result",null),"Any" );
        PyCompilationUnit pyCompilationUnit = pythonASTUtil.parseSource(content,typeASTNodeStringHashMap);
        System.out.println(pyCompilationUnit.toString());
        Assert.assertEquals(pyCompilationUnit.toString(),"import numpy;\n" +
                "public class Test {\n" +
                "  void add_arrays(  Test self){\n" +
                "    Any result;\n" +
                "    result=numpy.sum(numpy.array(M));\n" +
                "  }\n" +
                "}\n");

    }
}