package python3.typeinference.antlr;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.CommonTree;
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
    public void testError1() throws RecognitionException {
        String src = "Jhon";
        PyTypeLexer lexer = new PyTypeLexer(new ANTLRStringStream(src));
        PyTypeParser parser = new PyTypeParser(new CommonTokenStream(lexer));
        CommonTree tree = (CommonTree) parser.parse().getTree();
        Assert.assertTrue(tree instanceof CommonErrorNode);

    }

    @org.testng.annotations.Test
    public void testError2() throws RecognitionException {
        String src = "List[Jhon]";
        PyTypeLexer lexer = new PyTypeLexer(new ANTLRStringStream(src));
        PyTypeParser parser = new PyTypeParser(new CommonTokenStream(lexer));
        CommonTree tree = (CommonTree) parser.parse().getTree();
        Assert.assertEquals("List",tree.toString());
        Assert.assertTrue(tree.getChild(0) instanceof CommonErrorNode);

    }
}