package python3.typeinference.antlr;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.DOTTreeGenerator;
import org.testng.Assert;

public class PyTypeParserTest {
    @org.testng.annotations.Test
    public void test1() throws RecognitionException {
        String src = "int";
        PyTypeLexer lexer = new PyTypeLexer(new ANTLRStringStream(src));
        PyTypeParser parser = new PyTypeParser(new CommonTokenStream(lexer));
        CommonTree tree = (CommonTree) parser.parse().getTree();
        Assert.assertEquals("int",tree.toString());
//        org.antlr.stringtemplate.StringTemplate stringTemplate = gen.toDOT(tree);
//        System.out.println(stringTemplate);

    }

    @org.testng.annotations.Test
    public void test2() throws RecognitionException {
        String src = "List[int]";
        PyTypeLexer lexer = new PyTypeLexer(new ANTLRStringStream(src));
        PyTypeParser parser = new PyTypeParser(new CommonTokenStream(lexer));
        CommonTree tree = (CommonTree) parser.parse().getTree();
        Assert.assertEquals("List",tree.toString());
        Assert.assertEquals("int",tree.getChild(0).toString());
    }

    @org.testng.annotations.Test
    public void test3() throws RecognitionException {
        String src = "List[int][int]";
        PyTypeLexer lexer = new PyTypeLexer(new ANTLRStringStream(src));
        PyTypeParser parser = new PyTypeParser(new CommonTokenStream(lexer));
        CommonTree tree = (CommonTree) parser.parse().getTree();
        Assert.assertEquals("List",tree.toString());
        Assert.assertEquals("int",tree.getChild(0).toString());
        Assert.assertEquals("int",tree.getChild(1).toString());
    }

    @org.testng.annotations.Test
    public void test4() throws RecognitionException {
        String src = "List[List[int]]";
        PyTypeLexer lexer = new PyTypeLexer(new ANTLRStringStream(src));
        PyTypeParser parser = new PyTypeParser(new CommonTokenStream(lexer));
        CommonTree tree = (CommonTree) parser.parse().getTree();

        Assert.assertEquals("List",tree.toString());
        Assert.assertEquals("List",tree.getChild(0).toString());
        Assert.assertEquals("int",tree.getChild(0).getChild(0).toString());
    }

    @org.testng.annotations.Test
    public void test5() throws RecognitionException {
        String src = "Any";
        PyTypeLexer lexer = new PyTypeLexer(new ANTLRStringStream(src));
        PyTypeParser parser = new PyTypeParser(new CommonTokenStream(lexer));
        CommonTree tree = (CommonTree) parser.parse().getTree();

        Assert.assertEquals("Any",tree.toString());
    }

    @org.testng.annotations.Test
    public void test6() throws RecognitionException {
        String src = "Union[int, Any]";
        PyTypeLexer lexer = new PyTypeLexer(new ANTLRStringStream(src));
        PyTypeParser parser = new PyTypeParser(new CommonTokenStream(lexer));
        CommonTree tree = (CommonTree) parser.parse().getTree();
        Assert.assertEquals("Union",tree.toString());
        Assert.assertEquals("int",tree.getChild(0).toString());
        Assert.assertEquals("Any",tree.getChild(1).toString());
//        DOTTreeGenerator gen = new DOTTreeGenerator();
//        org.antlr.stringtemplate.StringTemplate stringTemplate = gen.toDOT(tree);
//        System.out.println(stringTemplate);
    }

    @org.testng.annotations.Test
    public void test7() throws RecognitionException {
        String src = "List[Union[int, Any, str]]";
        PyTypeLexer lexer = new PyTypeLexer(new ANTLRStringStream(src));
        PyTypeParser parser = new PyTypeParser(new CommonTokenStream(lexer));
        CommonTree tree = (CommonTree) parser.parse().getTree();
        Assert.assertEquals("List",tree.toString());
        Assert.assertEquals("Union",tree.getChild(0).toString());
        Assert.assertEquals("int",tree.getChild(0).getChild(0).toString());
        Assert.assertEquals("Any",tree.getChild(0).getChild(1).toString());
        Assert.assertEquals("str",tree.getChild(0).getChild(2).toString());
//        DOTTreeGenerator gen = new DOTTreeGenerator();
//        org.antlr.stringtemplate.StringTemplate stringTemplate = gen.toDOT(tree);
//        System.out.println(stringTemplate);
    }

    @org.testng.annotations.Test
    public void test8() throws RecognitionException {
        String src = "List[abc.boo]";
        PyTypeLexer lexer = new PyTypeLexer(new ANTLRStringStream(src));
        PyTypeParser parser = new PyTypeParser(new CommonTokenStream(lexer));
        CommonTree tree = parser.parse().getTree();
        Assert.assertEquals(tree.toString(),"List");
        Assert.assertEquals(tree.getChild(0).toString(),"abc.boo");
    }

    @org.testng.annotations.Test
    public void test9() throws RecognitionException {
        String src = "List[Jhon]";
        PyTypeLexer lexer = new PyTypeLexer(new ANTLRStringStream(src));
        PyTypeParser parser = new PyTypeParser(new CommonTokenStream(lexer));
        CommonTree tree = (CommonTree) parser.parse().getTree();
        Assert.assertEquals("List",tree.toString());
        Assert.assertEquals(tree.getChild(0).toString(),"Jhon");
    }

    @org.testng.annotations.Test
    public void test10() throws RecognitionException {
        String src = "List[Jhon.Boo_12Zoo]";
        PyTypeLexer lexer = new PyTypeLexer(new ANTLRStringStream(src));
        PyTypeParser parser = new PyTypeParser(new CommonTokenStream(lexer));
        CommonTree tree = (CommonTree) parser.parse().getTree();
        Assert.assertEquals("List",tree.toString());
        Assert.assertEquals(tree.getChild(0).toString(),"Jhon.Boo_12Zoo");
    }

    @org.testng.annotations.Test
    public void test11() throws RecognitionException {
        String src = "List[Jhon.Boo_12Zoo]";
        PyTypeLexer lexer = new PyTypeLexer(new ANTLRStringStream(src));
        PyTypeParser parser = new PyTypeParser(new CommonTokenStream(lexer));
        CommonTree tree = (CommonTree) parser.parse().getTree();
        Assert.assertEquals("List",tree.toString());
        Assert.assertEquals(tree.getChild(0).toString(),"Jhon.Boo_12Zoo");
    }

    @org.testng.annotations.Test
    public void test12() throws RecognitionException {
        String src = "List[List[Jhon.Boo_12Zoo]]";
        PyTypeLexer lexer = new PyTypeLexer(new ANTLRStringStream(src));
        PyTypeParser parser = new PyTypeParser(new CommonTokenStream(lexer));
        CommonTree tree = (CommonTree) parser.parse().getTree();
        Assert.assertEquals("List",tree.toString());
        Assert.assertEquals(tree.getChild(0).toString(),"List");
        Assert.assertEquals(tree.getChild(0).getChild(0).toString(),"Jhon.Boo_12Zoo");
    }

}