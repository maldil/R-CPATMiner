package python3.typeinference.antlr;

import org.antlr.runtime.RecognitionException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TypeInfoTest {

    @Test
    public void testGetTypeTree1() throws RecognitionException {
        String typeString = "List[int]";
        TypeInfo typeInfo = new TypeInfo();
        TypeTree typeTree = typeInfo.getTypeTree(typeString);
        Assert.assertEquals("List",typeTree.getTree().toString());
        Assert.assertEquals("int",typeTree.getTree().getChild(0).toString());
        Assert.assertFalse(typeTree.isError());
    }
    @Test
    public void testGetTypeTree2() throws RecognitionException {
        String typeString = "List[jhon]";
        TypeInfo typeInfo = new TypeInfo();
        TypeTree typeTree = typeInfo.getTypeTree(typeString);
        Assert.assertEquals("List",typeTree.getTree().toString());
    }

    @Test
    public void testGetTypeTree3() throws RecognitionException {
        String typeString = "jhon";
        TypeInfo typeInfo = new TypeInfo();
        TypeTree typeTree = typeInfo.getTypeTree(typeString);

    }

    @Test
    public void testGetTypeTree4() throws RecognitionException {
        String typeString = "List[List[int]]]";
        TypeInfo typeInfo = new TypeInfo();
        TypeTree typeTree = typeInfo.getTypeTree(typeString);
    }

    @Test
    public void testGetTypeTree5() throws RecognitionException {
        String typeString = "List[List[int]][int]";
        TypeInfo typeInfo = new TypeInfo();
        TypeTree typeTree = typeInfo.getTypeTree(typeString);
    }
}