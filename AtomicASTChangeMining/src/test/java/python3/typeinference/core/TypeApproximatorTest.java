package python3.typeinference.core;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Type;
import org.jpp.PyASTParser;
import org.jpp.astnodes.ast.Expr;
import org.jpp.astnodes.base.expr;
import org.jpp.astnodes.base.mod;
import org.jpp.heart.AstList;
import org.testng.Assert;
import org.testng.annotations.Test;
import python3.LineCounterTestASTVisitor;

import java.util.Map;
import java.util.Objects;

import static org.testng.Assert.*;

public class TypeApproximatorTest {
    private static org.apache.log4j.Logger log = Logger.getLogger(TypeApproximatorTest.class);
    @Test
    public void testGetSimpleTypeApproximation1() {
        String test = "['pdf', 'txt', 'inp', 'csv']";
        mod ast = PyASTParser.parsePython(test);
        Map<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
        AST asn = new AST(options);
        Type type = TypeApproximator.getSimpleTypeApproximation(asn, (expr) ((Expr)ast.getChildren().get(0)).getValue());
        Assert.assertEquals(Objects.requireNonNull(type).toString(),"String[]");
        log.info(type);
    }

    @Test
    public void testGetSimpleTypeApproximation2() {
        String test = "[1, 2, 3, 4]";
        mod ast = PyASTParser.parsePython(test);
        Map<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
        AST asn = new AST(options);
        Type type = TypeApproximator.getSimpleTypeApproximation(asn, (expr) ((Expr)ast.getChildren().get(0)).getValue());
        Assert.assertEquals(Objects.requireNonNull(type).toString(),"int[]");
        log.info(type);
    }

    @Test
    public void testGetSimpleTypeApproximation3() {
        String test = "[[2,6], [5,4], [3,4], [1,2]]";
        mod ast = PyASTParser.parsePython(test);
        Map<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
        AST asn = new AST(options);
        Type type = TypeApproximator.getSimpleTypeApproximation(asn, (expr) ((Expr)ast.getChildren().get(0)).getValue());
        Assert.assertEquals(Objects.requireNonNull(type).toString(),"int[][]");
        log.info(type);
    }

    @Test
    public void testGetSimpleTypeApproximation4() {
        String test = "[['malinda','dilhara'], ['my','mom']]";
        mod ast = PyASTParser.parsePython(test);
        Map<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
        AST asn = new AST(options);
        Type type = TypeApproximator.getSimpleTypeApproximation(asn, (expr) ((Expr)ast.getChildren().get(0)).getValue());
        log.info(type);
        Assert.assertEquals(Objects.requireNonNull(type).toString(),"String[][]");
    }

    @Test
    public void testGetSimpleTypeApproximation5() {
        String test = "[1,2, 'my','mom']";
        mod ast = PyASTParser.parsePython(test);
        Map<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
        AST asn = new AST(options);
        Type type = TypeApproximator.getSimpleTypeApproximation(asn, (expr) ((Expr)ast.getChildren().get(0)).getValue());
        Assert.assertEquals(Objects.requireNonNull(type).toString(),"String|int[]");
        log.info(type);
    }

    @Test
    public void testGetSimpleTypeApproximation6() {
        String test = "[1.0, 2.3, 3.8, 4.2]";
        mod ast = PyASTParser.parsePython(test);
        Map<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
        AST asn = new AST(options);
        Type type = TypeApproximator.getSimpleTypeApproximation(asn, (expr) ((Expr)ast.getChildren().get(0)).getValue());
        Assert.assertEquals(Objects.requireNonNull(type).toString(),"float[]");
        log.info(type);
    }
    @Test
    public void testGetSimpleTypeApproximation7() {
        String test = "[1]";
        mod ast = PyASTParser.parsePython(test);
        Map<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
        AST asn = new AST(options);
        Type type = TypeApproximator.getSimpleTypeApproximation(asn, (expr) ((Expr)ast.getChildren().get(0)).getValue());
        Assert.assertEquals(Objects.requireNonNull(type).toString(),"int[]");
        log.info(type);
    }

}