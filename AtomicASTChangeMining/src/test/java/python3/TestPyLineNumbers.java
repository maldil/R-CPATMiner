package python3;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.testng.annotations.Test;
import python3.typeinference.core.PyASTMatcher;
import utils.JavaASTUtil;

import java.util.HashMap;

import static org.testng.Assert.assertEquals;

public class TestPyLineNumbers {
    @Test
    public void testCharPosition2(){
        String content = "import numpy.boo as np\n" +
                "from tensorflow import matrix as xp\n" +
                "class Test ():\n" +
                "\tdef add_arrays(self,M):\n" +
                "\t\tresult = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "\t\tfor x in range(10):\n" +
                "\t\t\tvoo+=1\n";
        PythonASTUtil astutil = new PythonASTUtil();
        PyCompilationUnit unit = astutil.parseSource(content, new HashMap<>());
        assertEquals  (((TypeDeclaration) unit.types().get(0)).getMethods()[0].getPyLine(),4);
        assertEquals  (((TypeDeclaration) unit.types().get(0)).getPyLine(),3);
    }

    @Test
    public void testCharPosition3(){
        String content = "import numpy.boo as np\n" +
                "from tensorflow import matrix as xp\n" +
                "result = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "for x in range(10):\n" +
                "\t\tvoo+=1\n";
        PythonASTUtil astutil = new PythonASTUtil();
        PyCompilationUnit unit = astutil.parseSource(content, new HashMap<>());
        assertEquals  (((TypeDeclaration) unit.types().get(0)).getMethods()[0].getPyLine(),3);
        assertEquals  (((TypeDeclaration) unit.types().get(0)).getPyLine(),3);
    }

    @Test
    public void testCharPosition4(){
        String content = "import numpy as np\n" +
                "from tensorflow import matrix as xp\n" +
                "def add_arrays1(M):\n" +
                "    result = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "    for x in range(10):\n" +
                "        voo+=1\n" +
                "def add_arrays2(M):\n" +
                "    result = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "    for x in range(10):\n" +
                "        joo+=1";
        PythonASTUtil astutil = new PythonASTUtil();
        CompilationUnit pyCompileUnit = astutil.parseSource(content,new HashMap<>());
        CompilationUnit compileUnit= (CompilationUnit) JavaASTUtil.parseSource(pyCompileUnit.toString());
        pyCompileUnit.subtreeMatch(new PyASTMatcher(),compileUnit);  //update Python LOCs and lengths

        assertEquals  (((TypeDeclaration) compileUnit.types().get(0)).getMethods()[0].getPyLine(),3);
        assertEquals  (((TypeDeclaration) compileUnit.types().get(0)).getMethods()[1].getPyLine(),7);
        assertEquals  (((TypeDeclaration) compileUnit.types().get(0)).getPyLine(),3);
    }

    @Test
    public void testCharPosition8(){
        String content = "import numpy as np\n" +
                "from tensorflow import matrix as xp\n" +
                "# This class\n" +
                "#     is to test the comments\n" +
                "def add_arrays1(M):\n" +
                "    result = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "    for x in range(10):\n" +
                "        voo+=1\n" +
                "# This class\n" +
                "#     is to test the comments\n" +
                "def add_arrays2(M):\n" +
                "    result = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "    for x in range(10):\n" +
                "        joo+=1";
        PythonASTUtil astutil = new PythonASTUtil();
        CompilationUnit pyCompileUnit = astutil.parseSource(content,new HashMap<>());
        CompilationUnit compileUnit= (CompilationUnit)JavaASTUtil.parseSource(pyCompileUnit.toString());
        pyCompileUnit.subtreeMatch(new PyASTMatcher(),compileUnit);  //update Python LOCs and lengths

        assertEquals  (((TypeDeclaration) compileUnit.types().get(0)).getMethods()[0].getPyLine(),5);
        assertEquals  (((TypeDeclaration) compileUnit.types().get(0)).getMethods()[1].getPyLine(),11);
        assertEquals  (((TypeDeclaration) compileUnit.types().get(0)).getPyLine(),5);
    }
    @Test
    public void testCharPosition5(){
        String content = "import numpy as np\n" +
                "from tensorflow import matrix as xp\n" +
                "result = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "for x in range(10):\n" +
                "    voo+=1\n" +
                "def add_arrays1(M):\n" +
                "    result = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "    for x in range(10):\n" +
                "        voo+=1\n" +
                "def add_arrays2(M):\n" +
                "    result = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "    for x in range(10):\n" +
                "        joo+=1";
        PythonASTUtil astutil = new PythonASTUtil();
        CompilationUnit pyCompileUnit = astutil.parseSource(content,new HashMap<>());
        CompilationUnit unit= (CompilationUnit)JavaASTUtil.parseSource(pyCompileUnit.toString());
        pyCompileUnit.subtreeMatch(new PyASTMatcher(),unit);  //update Python LOCs and lengths
        assertEquals  (((TypeDeclaration) unit.types().get(0)).getMethods()[0].getPyLine(),3);
        assertEquals  (((TypeDeclaration) unit.types().get(1)).getMethods()[0].getPyLine(),6);
        assertEquals  (((TypeDeclaration) unit.types().get(1)).getMethods()[1].getPyLine(),10);
        assertEquals  (((TypeDeclaration) unit.types().get(0)).getPyLine(),3);
        assertEquals  (((TypeDeclaration) unit.types().get(1)).getPyLine(),6);
    }

    @Test
    public void testCharPosition6(){
        String content = "import numpy as np\n" +
                "from tensorflow import matrix as xp\n" +
                "result = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "for x in range(10):\n" +
                "    voo+=1\n" +
                "def add_arrays1(M):\n" +
                "    result = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "    for x in range(10):\n" +
                "        voo+=1\n" +
                "def add_arrays2(M):\n" +
                "    result = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "    for x in range(10):\n" +
                "        joo+=1\n" +
                "result = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "for x in range(10):\n" +
                "    voo+=1\n";
        PythonASTUtil astutil = new PythonASTUtil();
        CompilationUnit pyCompileUnit = astutil.parseSource(content,new HashMap<>());
        CompilationUnit unit= (CompilationUnit)JavaASTUtil.parseSource(pyCompileUnit.toString());
        pyCompileUnit.subtreeMatch(new PyASTMatcher(),unit);  //update Python LOCs and lengths
        assertEquals  (((TypeDeclaration) unit.types().get(0)).getMethods()[0].getPyLine(),3);
        assertEquals  (((TypeDeclaration) unit.types().get(1)).getMethods()[0].getPyLine(),6);
        assertEquals  (((TypeDeclaration) unit.types().get(1)).getMethods()[1].getPyLine(),10);
        assertEquals  (((TypeDeclaration) unit.types().get(2)).getMethods()[0].getPyLine(),14);
        assertEquals  (((TypeDeclaration) unit.types().get(0)).getPyLine(),3);
        assertEquals  (((TypeDeclaration) unit.types().get(1)).getPyLine(),6);
        assertEquals  (((TypeDeclaration) unit.types().get(2)).getPyLine(),14);
    }

    @Test
    public void testCharPosition7(){
        String content = "import numpy as np\n" +
                "from tensorflow import matrix as xp\n" +
                "result = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "for x in range(10):\n" +
                "    voo+=1\n" +
                "def add_arrays1(M):\n" +
                "    result = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "    for x in range(10):\n" +
                "        voo+=1\n" +
                "result = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "for x in range(10):\n" +
                "    voo+=1\n" +
                "def add_arrays2(M):\n" +
                "    result = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "    for x in range(10):\n" +
                "        joo+=1\n" +
                "result = np.sum.com.som.lom(np.thh.array(M),axis=1)\n" +
                "for x in range(10):\n" +
                "    voo+=1\n";
        PythonASTUtil astutil = new PythonASTUtil();
        CompilationUnit pyCompileUnit = astutil.parseSource(content,new HashMap<>());
        CompilationUnit unit= (CompilationUnit)JavaASTUtil.parseSource(pyCompileUnit.toString());
        pyCompileUnit.subtreeMatch(new PyASTMatcher(),unit);  //update Python LOCs and lengths
        assertEquals  (((TypeDeclaration) unit.types().get(0)).getMethods()[0].getPyLine(),3);
        assertEquals  (((TypeDeclaration) unit.types().get(1)).getMethods()[0].getPyLine(),6);
        assertEquals  (((TypeDeclaration) unit.types().get(2)).getMethods()[0].getPyLine(),10);
        assertEquals  (((TypeDeclaration) unit.types().get(3)).getMethods()[0].getPyLine(),13);
        assertEquals  (((TypeDeclaration) unit.types().get(4)).getMethods()[0].getPyLine(),17);
        assertEquals  (((TypeDeclaration) unit.types().get(0)).getPyLine(),3);
    }
}
