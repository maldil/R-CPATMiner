package python3;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.*;
import org.testng.Assert;
import python3.typeinference.core.TypeInformationTest;

import javax.swing.*;
import java.util.jar.JarOutputStream;

import static org.hamcrest.core.AnyOf.anyOf;

public class LineCounterTestASTVisitor extends ASTVisitor {

    private static org.apache.log4j.Logger log = Logger.getLogger(LineCounterTestASTVisitor.class);
    @Override
    public void preVisit(ASTNode node) {
        if (node instanceof SimpleName || node instanceof QualifiedName || node instanceof  PrimitiveType){
            log.debug("Name : "+node.toString()+" "+ASTNode.nodeClassForType(node.getNodeType())+" Start Position : "+node.getStartPosition()+" Length : "+node.getLength());
        }
        else
            log.debug(ASTNode.nodeClassForType(node.getNodeType())+" Start Position : "+node.getStartPosition()+" Length : "+node.getLength());
//        Assert.assertNotEquals(node.getStartPosition(),-1);
        super.preVisit(node);
    }

    @Override
    public boolean visit(AnnotationTypeDeclaration node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(AnnotationTypeMemberDeclaration node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(AnonymousClassDeclaration node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ArrayAccess node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ArrayCreation node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ArrayInitializer node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ArrayType node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(AssertStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(Assignment node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(Block node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(BlockComment node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(BooleanLiteral node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(BreakStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(CastExpression node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(CatchClause node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(CharacterLiteral node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ClassInstanceCreation node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(CompilationUnit node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ConditionalExpression node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ConstructorInvocation node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ContinueStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(CreationReference node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(Dimension node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(DoStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(EmptyStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(EnhancedForStatement node) {
        if (node.getParent().getParent() instanceof MethodDeclaration && ((MethodDeclaration) node.getParent().getParent()).getName().getIdentifier().equals("add_arrays1")) {
            Assert.assertEquals(node.getStartPosition(), ("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    ").length());
            Assert.assertEquals(node.getParameter().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    ").length());

            Assert.assertEquals(node.getExpression().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : ").length());
        }
        else if (node.getParent().getParent() instanceof MethodDeclaration && ((MethodDeclaration) node.getParent().getParent()).getName().getIdentifier().equals("add_arrays2")) {
            Assert.assertEquals(node.getStartPosition(), ("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    int[] result;\n" +
                    "    result=aaa.boo.sum.com.som.lom2(aaa.boo.thh.array(M));\n" +
                    "    ").length());
            Assert.assertEquals(node.getParameter().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    int[] result;\n" +
                    "    result=aaa.boo.sum.com.som.lom2(aaa.boo.thh.array(M));\n" +
                    "    for (    ").length());
            Assert.assertEquals(node.getExpression().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    int[] result;\n" +
                    "    result=aaa.boo.sum.com.som.lom2(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : ").length());
        }
        return super.visit(node);
    }

    @Override
    public boolean visit(EnumConstantDeclaration node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(EnumDeclaration node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ExportsDirective node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ExpressionMethodReference node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ExpressionStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(FieldAccess node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(FieldDeclaration node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ForStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(IfStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ImportDeclaration node) {
        Assert.assertEquals(node.toString().length(),node.getLength());
        log.debug(node.toString());
        if (node.toString().equals("import numpy.boo;"+"\n")){
            Assert.assertEquals(node.getStartPosition(),0);
        }
//        else if (node.toString().equals("import tensorflow.matrix;"+"\n"))
//        {
//            Assert.assertEquals(node.getStartPosition(),("import numpy.boo;"+"\n").length()+("import aaa.boo;"+"\n").length()+("import zooo;"+"\n").length());
//        }
        else if (node.toString().equals("import aaa.boo;"+"\n")){
            Assert.assertEquals(node.getStartPosition(),18);
        }
        else if (node.toString().equals("import zooo;"+"\n")){
            Assert.assertEquals(node.getStartPosition(),("import numpy.boo;"+"\n").length()+("import aaa.boo;"+"\n").length());
        }
//        else {
//            log.error(node.toString());
//            Assert.assertTrue(false);
//        }
        return super.visit(node);
    }

    @Override
    public boolean visit(InfixExpression node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(Initializer node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(InstanceofExpression node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(IntersectionType node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(Javadoc node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(LabeledStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(LambdaExpression node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(LineComment node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(MarkerAnnotation node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(MemberRef node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(MemberValuePair node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(MethodRef node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(MethodRefParameter node) {


        return super.visit(node);
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        Assert.assertEquals(node.getLength(),node.toString().length());
        if (node.getName().getIdentifier().equals("add_arrays1")){
            Assert.assertEquals(node.getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  ").length());
            Assert.assertEquals(node.getName().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void ").length());

            Assert.assertEquals(((SingleVariableDeclaration)node.parameters().get(0)).getType().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  ").length());
            Assert.assertEquals(((SingleVariableDeclaration)node.parameters().get(0)).getName().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 ").length());
        }
        else if (node.getName().getIdentifier().equals("add_arrays2")){
            Assert.assertEquals(node.getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  ").length());
            Assert.assertEquals(node.getName().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void ").length());
            Assert.assertEquals(((SingleVariableDeclaration)node.parameters().get(0)).getType().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  ").length());
            Assert.assertEquals(((SingleVariableDeclaration)node.parameters().get(0)).getName().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 ").length());
        }
        else if (node.getName().getIdentifier().equals("add_arrays3")){
            Assert.assertEquals(node.getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    int[] result;\n" +
                    "    result=aaa.boo.sum.com.som.lom2(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test3 {\n" +
                    "  ").length());
            Assert.assertEquals(node.getName().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    int[] result;\n" +
                    "    result=aaa.boo.sum.com.som.lom2(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test3 {\n" +
                    "  void ").length());
            Assert.assertEquals(((SingleVariableDeclaration)node.parameters().get(0)).getType().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    int[] result;\n" +
                    "    result=aaa.boo.sum.com.som.lom2(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test3 {\n" +
                    "  void add_arrays3(  ").length());
            Assert.assertEquals(((SingleVariableDeclaration)node.parameters().get(0)).getName().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    int[] result;\n" +
                    "    result=aaa.boo.sum.com.som.lom2(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test3 {\n" +
                    "  void add_arrays3(  Test3 ").length());
        }
        else if (node.getName().getIdentifier().equals("add_arrays4")){
            Assert.assertEquals(node.getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    int[] result;\n" +
                    "    result=aaa.boo.sum.com.som.lom2(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test3 {\n" +
                    "  void add_arrays3(  Test3 self){\n" +
                    "    print(\"nice\");\n" +
                    "  }\n" +
                    "  ").length());

            Assert.assertEquals(((SingleVariableDeclaration)node.parameters().get(0)).getType().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    int[] result;\n" +
                    "    result=aaa.boo.sum.com.som.lom2(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test3 {\n" +
                    "  void add_arrays3(  Test3 self){\n" +
                    "    print(\"nice\");\n" +
                    "  }\n" +
                    "  void add_arrays4(  ").length());
            Assert.assertEquals(((SingleVariableDeclaration)node.parameters().get(0)).getName().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    int[] result;\n" +
                    "    result=aaa.boo.sum.com.som.lom2(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test3 {\n" +
                    "  void add_arrays3(  Test3 self){\n" +
                    "    print(\"nice\");\n" +
                    "  }\n" +
                    "  void add_arrays4(  Test3 ").length());
        }
        else if (node.getName().getIdentifier().equals("add_arrays5")){
            Assert.assertEquals(node.getName().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    int[] result;\n" +
                    "    result=aaa.boo.sum.com.som.lom2(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test3 {\n" +
                    "  void add_arrays3(  Test3 self){\n" +
                    "    print(\"nice\");\n" +
                    "  }\n" +
                    "  void add_arrays4(  Test3 self){\n" +
                    "    print(\"nice\");\n" +
                    "  }\n" +
                    "  void ").length());
            Assert.assertEquals(node.getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    int[] result;\n" +
                    "    result=aaa.boo.sum.com.som.lom2(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test3 {\n" +
                    "  void add_arrays3(  Test3 self){\n" +
                    "    print(\"nice\");\n" +
                    "  }\n" +
                    "  void add_arrays4(  Test3 self){\n" +
                    "    print(\"nice\");\n" +
                    "  }\n" +
                    "  ").length());


            Assert.assertEquals(((SingleVariableDeclaration)node.parameters().get(0)).getType().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    int[] result;\n" +
                    "    result=aaa.boo.sum.com.som.lom2(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test3 {\n" +
                    "  void add_arrays3(  Test3 self){\n" +
                    "    print(\"nice\");\n" +
                    "  }\n" +
                    "  void add_arrays4(  Test3 self){\n" +
                    "    print(\"nice\");\n" +
                    "  }\n" +
                    "  void add_arrays5(  ").length());
            Assert.assertEquals(((SingleVariableDeclaration)node.parameters().get(0)).getName().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    int[] result;\n" +
                    "    result=aaa.boo.sum.com.som.lom2(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test3 {\n" +
                    "  void add_arrays3(  Test3 self){\n" +
                    "    print(\"nice\");\n" +
                    "  }\n" +
                    "  void add_arrays4(  Test3 self){\n" +
                    "    print(\"nice\");\n" +
                    "  }\n" +
                    "  void add_arrays5(  Test3 ").length());
        }


        return super.visit(node);
    }

    @Override
    public boolean visit(MethodInvocation node) {

        return super.visit(node);
    }

    @Override
    public boolean visit(Modifier node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ModuleDeclaration node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ModuleModifier node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(NameQualifiedType node) {
        log.debug(node.getNodeType()+" : "+node.getStartPosition());
        return super.visit(node);
    }

    @Override
    public boolean visit(NormalAnnotation node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(NullLiteral node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(NumberLiteral node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(OpensDirective node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(PackageDeclaration node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ParameterizedType node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ParenthesizedExpression node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(PostfixExpression node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(PrefixExpression node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ProvidesDirective node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(PrimitiveType node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(QualifiedName node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(QualifiedType node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(RequiresDirective node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(RecordDeclaration node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ReturnStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(SimpleName node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(SimpleType node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(SingleMemberAnnotation node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(SingleVariableDeclaration node) {
        System.out.println("Node "+node.toString());
        if (node.getName().getIdentifier().equals("self") && node.getParent() instanceof MethodDeclaration &&
                ((MethodDeclaration) node.getParent()).getName().getIdentifier().equals("add_arrays1")){
            Assert.assertEquals(node.getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  ").length());

        }
        else if (node.getName().getIdentifier().equals("self") && node.getParent() instanceof MethodDeclaration &&
                ((MethodDeclaration) node.getParent()).getName().getIdentifier().equals("add_arrays2")){
            Assert.assertEquals(node.getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  ").length());
        }
        else if (node.getName().getIdentifier().equals("self") && node.getParent() instanceof MethodDeclaration &&
                ((MethodDeclaration) node.getParent()).getName().getIdentifier().equals("add_arrays3")){
            Assert.assertEquals(node.getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    int[] result;\n" +
                    "    result=aaa.boo.sum.com.som.lom2(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test3 {\n" +
                    "  void add_arrays3(  ").length());

        }
        else if (node.getName().getIdentifier().equals("self") && node.getParent() instanceof MethodDeclaration &&
                ((MethodDeclaration) node.getParent()).getName().getIdentifier().equals("add_arrays4")) {
            Assert.assertEquals(node.getStartPosition(), ("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    int[] result;\n" +
                    "    result=aaa.boo.sum.com.som.lom2(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test3 {\n" +
                    "  void add_arrays3(  Test3 self){\n" +
                    "    print(\"nice\");\n" +
                    "  }\n" +
                    "  void add_arrays4(  ").length());
        }
        else if (node.getName().getIdentifier().equals("self") && node.getParent() instanceof MethodDeclaration &&
                ((MethodDeclaration) node.getParent()).getName().getIdentifier().equals("add_arrays5")) {
            Assert.assertEquals(node.getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    int[] result;\n" +
                    "    result=aaa.boo.sum.com.som.lom2(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test3 {\n" +
                    "  void add_arrays3(  Test3 self){\n" +
                    "    print(\"nice\");\n" +
                    "  }\n" +
                    "  void add_arrays4(  Test3 self){\n" +
                    "    print(\"nice\");\n" +
                    "  }\n" +
                    "  void add_arrays5(  ").length());

        }
        else if (node.getType().toString().equals("numpy.ndarray") && node.getParent() instanceof MethodDeclaration &&
                ((MethodDeclaration) node.getParent()).getName().getIdentifier().equals("add_arrays1")){
            System.out.println("hit");
            Assert.assertEquals(node.getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    ").length());

        }

        return super.visit(node);
    }

    @Override
    public boolean visit(StringLiteral node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(SuperConstructorInvocation node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(SuperFieldAccess node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(SuperMethodInvocation node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(SuperMethodReference node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(SwitchCase node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(SwitchExpression node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(SwitchStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(SynchronizedStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(TagElement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(TextBlock node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(TextElement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ThisExpression node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ThrowStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(TryStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        Assert.assertEquals(node.getLength(),node.toString().length());
        if (node.getName().toString().equals("Test1")){
            log.debug(node.getStartPosition());
            Assert.assertEquals(node.getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n").length());
            Assert.assertEquals(node.getName().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class ").length());



        }
        else if (node.getName().toString().equals("Test2"))
        {
            Assert.assertEquals(node.getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n").length());
            Assert.assertEquals(node.getName().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays2(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class ").length());
            Assert.assertEquals(node.getSuperclassType().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays3(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends ").length());

        }
        else if (node.getName().toString().equals("Test3")) {
            Assert.assertEquals(node.getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    int[] result;\n" +
                    "    result=aaa.boo.sum.com.som.lom2(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n").length());

            Assert.assertEquals(node.getName().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    int[] result;\n" +
                    "    result=aaa.boo.sum.com.som.lom2(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class ").length());
        }


        log.debug("class");
        log.debug(node.toString());
        return super.visit(node);
    }

    @Override
    public boolean visit(TypeDeclarationStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(TypeLiteral node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(TypeMethodReference node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(TypeParameter node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(UnionType node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(UsesDirective node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(VariableDeclarationExpression node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(VariableDeclarationStatement node) {
        if (node.getType().toString().equals("numpy.ndarray") && node.getParent().getParent() instanceof MethodDeclaration
                && ((MethodDeclaration) node.getParent().getParent()).getName().getIdentifier().equals("add_arrays1")){
            Assert.assertEquals(node.getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    ").length());
            Assert.assertEquals(node.getType().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    ").length());

            Assert.assertEquals(((SimpleType)node.getType()).getName().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    ").length());
//            Assert.assertEquals(((QualifiedName)((SimpleType)node.getType()).getName()).getQualifier().getStartPosition(),("import numpy.boo;\n" +
//                    "import aaa.boo;\n" +
//                    "import zooo;\n" +
//                    "import tensorflow.matrix;\n" +
//                    "public class Test1 {\n" +
//                    "  void add_arrays1(  Test1 self){\n" +
//                    "    ").length());
//            Assert.assertEquals(((QualifiedName)((SimpleType)node.getType()).getName()).getName().getStartPosition(),("import numpy.boo;\n" +
//                    "import aaa.boo;\n" +
//                    "import zooo;\n" +
//                    "import tensorflow.matrix;\n" +
//                    "public class Test1 {\n" +
//                    "  void add_arrays1(  Test1 self){\n" +
//                    "    numpy.").length());
        }
        else if (node.getType().toString().equals("int[]") && node.getParent().getParent() instanceof MethodDeclaration
                && ((MethodDeclaration) node.getParent().getParent()).getName().getIdentifier().equals("add_arrays2")){
            Assert.assertEquals(node.getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    ").length());

            Assert.assertEquals(node.getType().getStartPosition(),("import numpy.boo;\n" +
                    "import aaa.boo;\n" +
                    "import zooo;\n" +
                    "import tensorflow.matrix;\n" +
                    "public class Test1 {\n" +
                    "  void add_arrays1(  Test1 self){\n" +
                    "    numpy.ndarray result;\n" +
                    "    result=aaa.boo.sum.com.som.lom(aaa.boo.thh.array(M));\n" +
                    "    for (    int x : range(10)) {\n" +
                    "      voo+=1;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "public class Test2 extends Boo {\n" +
                    "  void add_arrays2(  Test2 self){\n" +
                    "    ").length());
        }
        else if (node.getType().toString().equals("numpy.ndarray.foo.dooo") && node.getParent().getParent() instanceof MethodDeclaration
                && ((MethodDeclaration) node.getParent().getParent()).getName().getIdentifier().equals("add_arrays6")){
            Assert.assertTrue(node.getStartPosition()==715 || node.getStartPosition()==694 );
            Assert.assertTrue(node.getType().getStartPosition()==715 || node.getType().getStartPosition()==694);
            Assert.assertTrue(((SimpleType)node.getType()).getName().getStartPosition()==715||((SimpleType)node.getType()).getName().getStartPosition()==694 );
//            Assert.assertTrue(((QualifiedName)((SimpleType)node.getType()).getName()).getQualifier().getStartPosition()==715||((QualifiedName)((SimpleType)node.getType()).getName()).getQualifier().getStartPosition()==694);
//            Assert.assertTrue(((QualifiedName)((SimpleType)node.getType()).getName()).getName().getStartPosition()==733||((QualifiedName)((SimpleType)node.getType()).getName()).getName().getStartPosition()==712);

        }
        else if (node.getType().toString().equals("int[][]") && node.getParent().getParent() instanceof MethodDeclaration
                && ((MethodDeclaration) node.getParent().getParent()).getName().getIdentifier().equals("add_arrays6")){
            Assert.assertTrue(node.getStartPosition()==694||node.getStartPosition()==730);
       //     Assert.assertTrue(((ArrayType)node.getType()).getElementType().getStartPosition()==694||((ArrayType)node.getType()).getElementType().getStartPosition()==730);
        }

        return super.visit(node);
    }

    @Override
    public boolean visit(VariableDeclarationFragment node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(WhileStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(WildcardType node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(YieldStatement node) {
        return super.visit(node);
    }
}
