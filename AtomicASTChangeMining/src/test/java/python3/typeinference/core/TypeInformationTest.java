package python3.typeinference.core;

import org.antlr.runtime.RecognitionException;
import org.apache.log4j.Logger;

import org.testng.Assert;
import org.testng.annotations.Test;
import python3.typeinference.antlr.TypeTree;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class TypeInformationTest {
    private static org.apache.log4j.Logger log = Logger.getLogger(TypeInformationTest.class);
    @Test
    public void testGetTypeInformation() {
        ClassLoader classLoader = getClass().getClassLoader();
        File absoluteFile = new File(Objects.requireNonNull(classLoader.getResource("TestPyType")).getFile()+"/main.py").getAbsoluteFile();
        TypeInformation typeInformation = new TypeInformation();
        String project_path = Objects.requireNonNull(classLoader.getResource("TestPyType")).getFile();
        String file_name = "main.py";

        Map<TypeASTNode, String> information = typeInformation.getTypeInformation(absoluteFile.toString(),project_path,file_name);
        Assert.assertEquals(information.get(new TypeASTNode(7, 8, "M")),"List[List[int]]");
        Assert.assertEquals(information.get(new TypeASTNode(8, 8, "result")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(9, 15, "result")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(12, 8, "result")),"List[List[int]]");
        Assert.assertEquals(information.get(new TypeASTNode(13, 12, "m")),"List[int]");
        Assert.assertEquals(information.get(new TypeASTNode(13, 17, "M")),"List[List[int]]");
        Assert.assertEquals(information.get(new TypeASTNode(18, 15, "result")),"List[List[int]]");
        Assert.assertEquals(information.get(new TypeASTNode(21, 8, "B")),"List[List[int]]");
        Assert.assertEquals(information.get(new TypeASTNode(22, 8, "final_result")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(25, 8, "aa")),"package2.example_file.A");
        Assert.assertEquals(information.get(new TypeASTNode(26, 8, "bb")),"numpy.ndarray");
        Assert.assertEquals(information.get(new TypeASTNode(27, 8, "cc")),"numpy.ndarray");
        Assert.assertEquals(information.get(new TypeASTNode(28, 8, "dd")),"numpy.ndarray");
        Assert.assertEquals(information.get(new TypeASTNode(29, 8, "x1")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(30, 8, "x2")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(31, 8, "x3")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(35, 8, "mnist")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(35, 16, "mnist")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(8, 16, "add")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(8, 51, "A")),"List[int]");
        Assert.assertEquals(information.get(new TypeASTNode(14, 12, "list1")),"List[int]");
        Assert.assertEquals(information.get(new TypeASTNode(15, 16, "x")),"int");
        Assert.assertEquals(information.get(new TypeASTNode(15, 21, "m")),"List[int]");
        Assert.assertEquals(information.get(new TypeASTNode(21, 12, "add_one")),"Callable[[Any], Any]");
        Assert.assertEquals(information.get(new TypeASTNode(22, 23, "add_arrays")),"Callable[[Any], Any]");
        Assert.assertEquals(information.get(new TypeASTNode(25, 13, "get_a_class")),"Callable[[], package2.example_file.A]");
        Assert.assertEquals(information.get(new TypeASTNode(26, 13, "boo")),"Callable[[], numpy.ndarray]");
        Assert.assertEquals(information.get(new TypeASTNode(27, 13, "array")),"Callable[..., numpy.ndarray]");
        Assert.assertEquals(information.get(new TypeASTNode(28, 13, "array")),"Callable[..., numpy.ndarray]");
        Assert.assertEquals(information.get(new TypeASTNode(29, 13, "reshape")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(30, 13, "arange")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(31, 13, "add")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(31, 20, "x1")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(31, 24, "x2")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(32, 24, "constant")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(35, 16, "datasets")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(37, 47, "load_data")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(38, 8, "x_train")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(38, 17, "x_test")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(8, 16, "np")),"module");
        Assert.assertEquals(information.get(new TypeASTNode(8, 23, "sum")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(17, 12, "append")),"Callable");
        Assert.assertEquals(information.get(new TypeASTNode(17, 26, "list1")),"List[int]");
        Assert.assertEquals(information.get(new TypeASTNode(21, 12, "self")),"Test");
        Assert.assertEquals(information.get(new TypeASTNode(22, 23, "self")),"Test");
        Assert.assertEquals(information.get(new TypeASTNode(25, 13, "example_file")),"module");
        Assert.assertEquals(information.get(new TypeASTNode(26, 13, "aa")),"package2.example_file.A");
        Assert.assertEquals(information.get(new TypeASTNode(27, 13, "np")),"module");
        Assert.assertEquals(information.get(new TypeASTNode(28, 13, "np")),"module");
        Assert.assertEquals(information.get(new TypeASTNode(30, 13, "np")),"module");
        Assert.assertEquals(information.get(new TypeASTNode(31, 13, "np")),"module");
        Assert.assertEquals(information.get(new TypeASTNode(32, 24, "tf")),"module");
        Assert.assertEquals(information.get(new TypeASTNode(34, 52, "float16")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(35, 16, "keras")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(37, 9, "x_train")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(37, 18, "y_train")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(37, 29, "x_test")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(37, 37, "y_test")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(37, 47, "mnist")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(38, 26, "x_train")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(38, 43, "x_test")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(8, 23, "np")),"module");
        Assert.assertEquals(information.get(new TypeASTNode(8, 30, "array")),"Callable[..., numpy.ndarray]");
        Assert.assertEquals(information.get(new TypeASTNode(8, 39, "M")),"List[List[int]]");
        Assert.assertEquals(information.get(new TypeASTNode(16, 16, "append")),"Callable");
        Assert.assertEquals(information.get(new TypeASTNode(17, 12, "result")),"List[List[int]]");
        Assert.assertEquals(information.get(new TypeASTNode(29, 13, "arange")),"Any");
        Assert.assertEquals(information.get(new TypeASTNode(34, 52, "tf")),"module");
        Assert.assertEquals(information.get(new TypeASTNode(35, 16, "tf")),"module");
        Assert.assertEquals(information.get(new TypeASTNode(8, 30, "np")),"module");
        Assert.assertEquals(information.get(new TypeASTNode(16, 16, "list1")),"List[int]");
        Assert.assertEquals(information.get(new TypeASTNode(16, 29, "x")),"int");
        Assert.assertEquals(information.get(new TypeASTNode(29, 13, "np")),"module");

    }

    @Test
    public void testGetTypeTreeInformation() throws RecognitionException {
        ClassLoader classLoader = getClass().getClassLoader();
        File absoluteFile = new File(Objects.requireNonNull(classLoader.getResource("TestPyType")).getFile()+"/main.py").getAbsoluteFile();
        TypeInformation typeInformation = new TypeInformation();
        String project_path = Objects.requireNonNull(classLoader.getResource("TestPyType")).getFile();
        String file_name = "main.py";
        ArrayList<TypeASTNode> typeASTNodeList = new ArrayList<>();
        typeASTNodeList.add(new TypeASTNode(7, 8, "M"));
        typeASTNodeList.add(new TypeASTNode(8, 8, "result"));
        typeASTNodeList.add(new TypeASTNode(9, 15, "result"));
        typeASTNodeList.add(new TypeASTNode(12, 8, "result"));
        typeASTNodeList.add(new TypeASTNode(13, 12, "m"));
        typeASTNodeList.add(new TypeASTNode(18, 15, "result"));
        typeASTNodeList.add(new TypeASTNode(21, 8, "B"));
        typeASTNodeList.add(new TypeASTNode(25, 8, "aa"));
        typeASTNodeList.add(new TypeASTNode(26, 8, "bb"));
        typeASTNodeList.add(new TypeASTNode(27, 8, "cc"));
        typeASTNodeList.add(new TypeASTNode(22, 8, "final_result"));
        typeASTNodeList.add(new TypeASTNode(28, 8, "dd"));
        Map<TypeASTNode, TypeTree> typeTreeInformation = typeInformation.getTypeTreeInformation(absoluteFile.toString(), project_path, file_name, typeASTNodeList);
    }
}