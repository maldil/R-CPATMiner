package change;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Assert;
import org.testng.annotations.Test;
import python3.PyCompilationUnit;
import python3.PythonASTUtil;
import python3.typeinference.core.TypeASTNode;
import utils.JavaASTUtil;

import java.util.HashMap;

import static org.testng.Assert.*;

public class CFileTest {

    @Test
    public void checkReverseParsing(){
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
                "\t\tresult2 = np.sum.com.som.lom2(np.thh.array(M),axis=1)\n"+
                "\t\tresult3 = [[1,2,3],[3,2,1],[4,5,6]]\n"+
                "\t\tresult4 = result2^2\n";
        PythonASTUtil pythonASTUtil = new PythonASTUtil();
        HashMap<TypeASTNode, String> typeASTNodeStringHashMap = new HashMap<>();
        typeASTNodeStringHashMap.put(new TypeASTNode(6,2,"result",null),"numpy.ndarray" );
        typeASTNodeStringHashMap.put(new TypeASTNode(12,2,"result",null),"List[int]" );
        typeASTNodeStringHashMap.put(new TypeASTNode(24,2,"result1",null),"List[List[int]]" );
        typeASTNodeStringHashMap.put(new TypeASTNode(25,2,"result2",null),"numpy.ndarray.foo.dooo" );
        typeASTNodeStringHashMap.put(new TypeASTNode(26,2,"result3",null),"List[List[int]]" );
        PyCompilationUnit pyCompilationUnit = pythonASTUtil.parseSource(content,typeASTNodeStringHashMap);
        CompilationUnit compileUnit1= (CompilationUnit) JavaASTUtil.parseSource(pyCompilationUnit.toString());
        Assert.assertTrue(compileUnit1.getProblems().length==0);

    }

}