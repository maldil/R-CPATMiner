package python3;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PyDictComprehension;
import org.eclipse.jdt.core.dom.PyListComprehension;
import org.eclipse.jdt.core.dom.PyTupleExpression;
import org.eclipse.jdt.core.dom.PyWithStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.jpp.astnodes.ast.Return;

import java.util.HashMap;
import java.util.Map;

public class JDTASTVisitor extends ASTVisitor {
    private static org.apache.log4j.Logger log = Logger.getLogger(JDTASTVisitor.class);
    private HashMap<String,Integer> jdtNodeMap = new HashMap<>();
    @Override
    public boolean visit(AssertStatement node) {
        increment(jdtNodeMap,"Java_AssertStatement");
        return super.visit(node);
    }

    @Override
    public boolean visit(EnhancedForStatement node) {
        increment(jdtNodeMap,"Java_EnhancedForStatement");
        return super.visit(node);
    }

    @Override
    public boolean visit(IfStatement node) {
        increment(jdtNodeMap,"Java_IfStatement");
        return super.visit(node);
    }

    @Override
    public boolean visit(ImportDeclaration node) {
        increment(jdtNodeMap,"Java_ImportDeclaration");
        return super.visit(node);
    }

    @Override
    public boolean visit(Assignment node) {
        increment(jdtNodeMap,"Java_Assignment");
        return super.visit(node);
    }

    @Override
    public boolean visit(PyListComprehension node) {
        increment(jdtNodeMap,"Java_PyListComprehension");
        return super.visit(node);
    }
    @Override
    public boolean visit(MethodInvocation node) {
        increment(jdtNodeMap,"Java_MethodInvocation");
        return super.visit(node);
    }

    @Override
    public boolean visit(PyWithStatement node) {
        increment(jdtNodeMap,"Java_PyWithStatement");
        return super.visit(node);
    }

    @Override
    public boolean visit(PyDictComprehension node) {
        increment(jdtNodeMap,"Java_PyDictComprehensiont");
        return super.visit(node);
    }

    @Override
    public boolean visit(PyTupleExpression node) {
        increment(jdtNodeMap,"Java_PyTupleExpression");
        return super.visit(node);
    }

    @Override
    public boolean visit(ThrowStatement node) {
        increment(jdtNodeMap,"Java_ThrowStatement");
        return super.visit(node);
    }

    @Override
    public boolean visit(ReturnStatement node) {
        increment(jdtNodeMap,"Java_Return");
        return super.visit(node);
    }

    @Override
    public boolean visit(TryStatement node) {
        increment(jdtNodeMap,"Java_TryStatement");
        return super.visit(node);
    }

    @Override
    public boolean visit(ContinueStatement node) {
        increment(jdtNodeMap,"Java_ContinueStatement");
        return super.visit(node);
    }

    @Override
    public boolean visit(WhileStatement node) {
        increment(jdtNodeMap,"Java_WhileStatement");
        return super.visit(node);
    }

    @Override
    public boolean visit(LambdaExpression node) {
        increment(jdtNodeMap,"Java_LambdaExpression");
        return super.visit(node);
    }



    public int getStatFor(String nodename){
        return jdtNodeMap.get(nodename) == null? 0:jdtNodeMap.get(nodename);
    }

    public static<K> void increment(Map<K,Integer> map, K key) {
        Integer count = map.getOrDefault(key, 0);
        map.put(key, count + 1);
    }
}