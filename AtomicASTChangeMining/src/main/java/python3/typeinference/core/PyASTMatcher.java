package python3.typeinference.core;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.internal.core.dom.util.DOMASTUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PyASTMatcher extends ASTMatcher {
    private static org.apache.log4j.Logger log = Logger.getLogger(PyASTMatcher.class);
    public boolean DEBUG;
    public boolean match(AnnotationTypeDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (DEBUG ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof AnnotationTypeDeclaration){
            node.setPyStartPosition(((AnnotationTypeDeclaration) other).getPyStartPosition());
            node.setPyLength(((AnnotationTypeDeclaration) other).getPyLength());
            node.setPyLine(((AnnotationTypeDeclaration) other).getPyLine());
            node.setPyColumnOffSet(((AnnotationTypeDeclaration) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(AnnotationTypeMemberDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof AnnotationTypeMemberDeclaration){
            node.setPyStartPosition(((AnnotationTypeMemberDeclaration) other).getPyStartPosition());
            node.setPyLength(((AnnotationTypeMemberDeclaration) other).getPyLength());
            node.setPyLine(((AnnotationTypeMemberDeclaration) other).getPyLine());
            node.setPyColumnOffSet(((AnnotationTypeMemberDeclaration) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(AnonymousClassDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof AnonymousClassDeclaration){
            node.setPyStartPosition(((AnonymousClassDeclaration) other).getPyStartPosition());
            node.setPyLength(((AnonymousClassDeclaration) other).getPyLength());
            node.setPyLine(((AnonymousClassDeclaration) other).getPyLine());
            node.setPyColumnOffSet(((AnonymousClassDeclaration) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ArrayAccess node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ArrayAccess){
            node.setPyStartPosition(((ArrayAccess) other).getPyStartPosition());
            node.setPyLength(((ArrayAccess) other).getPyLength());
            node.setPyLine(((ArrayAccess) other).getPyLine());
            node.setPyColumnOffSet(((ArrayAccess) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ArrayCreation node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ArrayCreation){
            node.setPyStartPosition(((ArrayCreation) other).getPyStartPosition());
            node.setPyLength(((ArrayCreation) other).getPyLength());
            node.setPyLine(((ArrayCreation) other).getPyLine());
            node.setPyColumnOffSet(((ArrayCreation) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ArrayInitializer node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ArrayInitializer){
            node.setPyStartPosition(((ArrayInitializer) other).getPyStartPosition());
            node.setPyLength(((ArrayInitializer) other).getPyLength());
            node.setPyLine(((ArrayInitializer) other).getPyLine());
            node.setPyColumnOffSet(((ArrayInitializer) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ArrayType node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ArrayType){
            node.setPyStartPosition(((ArrayType) other).getPyStartPosition());
            node.setPyLength(((ArrayType) other).getPyLength());
            node.setPyLine(((ArrayType) other).getPyLine());
            node.setPyColumnOffSet(((ArrayType) other).getPyColumnOffSet());
        }

        return match;
    }

    public boolean match(AssertStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof AssertStatement){
            node.setPyStartPosition(((AssertStatement) other).getPyStartPosition());
            node.setPyLength(((AssertStatement) other).getPyLength());
            node.setPyLine(((AssertStatement) other).getPyLine());
            node.setPyColumnOffSet(((AssertStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(Assignment node, Object other) {
        boolean match;

        if (!(other instanceof Assignment)) {
            match= false;
        } else {
            Assignment o = (Assignment)other;
            match= node.getOperator().toString().equals(o.getOperator().toString() ) && this.safeSubtreeMatch(node.getLeftHandSide(), o.getLeftHandSide()) && this.safeSubtreeMatch(node.getRightHandSide(), o.getRightHandSide());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match){
            node.setPyStartPosition(((Assignment) other).getPyStartPosition());
            node.setPyLength(((Assignment) other).getPyLength());
            node.setPyLine(((Assignment) other).getPyLine());
            node.setPyColumnOffSet(((Assignment) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(Block node, Object other) {
        boolean match = super.match(node, other);

        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof Block){
            node.setPyStartPosition(((Block) other).getPyStartPosition());
            node.setPyLength(((Block) other).getPyLength());
            node.setPyLine(((Block) other).getPyLine());
            node.setPyColumnOffSet(((Block) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(BlockComment node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());

        if (match && other instanceof BlockComment){
            node.setPyStartPosition(((BlockComment) other).getPyStartPosition());
            node.setPyLength(((BlockComment) other).getPyLength());
            node.setPyLine(((BlockComment) other).getPyLine());
            node.setPyColumnOffSet(((BlockComment) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(BooleanLiteral node, Object other) {
        boolean match = super.match(node, other);
        if (match && other instanceof BooleanLiteral){
            node.setPyStartPosition(((BooleanLiteral) other).getPyStartPosition());
            node.setPyLength(((BooleanLiteral) other).getPyLength());
            node.setPyLine(((BooleanLiteral) other).getPyLine());
            node.setPyColumnOffSet(((BooleanLiteral) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(BreakStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof BreakStatement){
            node.setPyStartPosition(((BreakStatement) other).getPyStartPosition());
            node.setPyLength(((BreakStatement) other).getPyLength());
            node.setPyLine(((BreakStatement) other).getPyLine());
            node.setPyColumnOffSet(((BreakStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(CastExpression node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof CastExpression){
            node.setPyStartPosition(((CastExpression) other).getPyStartPosition());
            node.setPyLength(((CastExpression) other).getPyLength());
            node.setPyLine(((CastExpression) other).getPyLine());
            node.setPyColumnOffSet(((CastExpression) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(CatchClause node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof CatchClause){
            node.setPyStartPosition(((CatchClause) other).getPyStartPosition());
            node.setPyLength(((CatchClause) other).getPyLength());
            node.setPyLine(((CatchClause) other).getPyLine());
            node.setPyColumnOffSet(((CatchClause) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(CharacterLiteral node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof CharacterLiteral){
            node.setPyStartPosition(((CharacterLiteral) other).getPyStartPosition());
            node.setPyLength(((CharacterLiteral) other).getPyLength());
            node.setPyLine(((CharacterLiteral) other).getPyLine());
            node.setPyColumnOffSet(((CharacterLiteral) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ClassInstanceCreation node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ClassInstanceCreation){
            node.setPyStartPosition(((ClassInstanceCreation) other).getPyStartPosition());
            node.setPyLength(((ClassInstanceCreation) other).getPyLength());
            node.setPyLine(((ClassInstanceCreation) other).getPyLine());
            node.setPyColumnOffSet(((ClassInstanceCreation) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(CompilationUnit node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof CompilationUnit){
            node.setPyStartPosition(((CompilationUnit) other).getPyStartPosition());
            node.setPyLength(((CompilationUnit) other).getPyLength());
            node.setPyLine(((CompilationUnit) other).getPyLine());
            node.setPyColumnOffSet(((CompilationUnit) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ConditionalExpression node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ConditionalExpression){
            node.setPyStartPosition(((ConditionalExpression) other).getPyStartPosition());
            node.setPyLength(((ConditionalExpression) other).getPyLength());
            node.setPyLine(((ConditionalExpression) other).getPyLine());
            node.setPyColumnOffSet(((ConditionalExpression) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ConstructorInvocation node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ConstructorInvocation){
            node.setPyStartPosition(((ConstructorInvocation) other).getPyStartPosition());
            node.setPyLength(((ConstructorInvocation) other).getPyLength());
            node.setPyLine(((ConstructorInvocation) other).getPyLine());
            node.setPyColumnOffSet(((ConstructorInvocation) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ContinueStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ContinueStatement){
            node.setPyStartPosition(((ContinueStatement) other).getPyStartPosition());
            node.setPyLength(((ContinueStatement) other).getPyLength());
            node.setPyLine(((ContinueStatement) other).getPyLine());
            node.setPyColumnOffSet(((ContinueStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(CreationReference node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof CreationReference){
            node.setPyStartPosition(((CreationReference) other).getPyStartPosition());
            node.setPyLength(((CreationReference) other).getPyLength());
            node.setPyLine(((CreationReference) other).getPyLine());
            node.setPyColumnOffSet(((CreationReference) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(Dimension node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof Dimension){
            node.setPyStartPosition(((Dimension) other).getPyStartPosition());
            node.setPyLength(((Dimension) other).getPyLength());
            node.setPyLine(((Dimension) other).getPyLine());
            node.setPyColumnOffSet(((Dimension) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(DoStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof DoStatement){
            node.setPyStartPosition(((DoStatement) other).getPyStartPosition());
            node.setPyLength(((DoStatement) other).getPyLength());
            node.setPyLine(((DoStatement) other).getPyLine());
            node.setPyColumnOffSet(((DoStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(EmptyStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof EmptyStatement){
            node.setPyStartPosition(((EmptyStatement) other).getPyStartPosition());
            node.setPyLength(((EmptyStatement) other).getPyLength());
            node.setPyLine(((EmptyStatement) other).getPyLine());
            node.setPyColumnOffSet(((EmptyStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(EnhancedForStatement node, Object other) {
        boolean match;
        if (!(other instanceof EnhancedForStatement)) {
            match = false;
        } else {
            EnhancedForStatement o = (EnhancedForStatement)other;
            List nodeM = new ArrayList();
            List nodeN = new ArrayList();
            if (!node.getParameter().getName().getIdentifier().equals("DummyTerminalNode")){
                nodeM.add(node.getParameter());
            }
            if (!o.getParameter().getName().getIdentifier().equals("DummyTerminalNode")){
                nodeN.add(node.getParameter());
            }
            for (Object parameter : node.Parameters()) {
                SingleVariableDeclaration para = (SingleVariableDeclaration)parameter;
                if (!(para.getName().getIdentifier().equals("DummyTerminalNode"))){
                    nodeM.add(para);
                }
            }
            for (Object parameter : o.Parameters()) {
                SingleVariableDeclaration para = (SingleVariableDeclaration)parameter;
                if (!(para.getName().getIdentifier().equals("DummyTerminalNode"))){
                    nodeN.add(para);
                }
            }
            match= this.safeSubtreeListMatch(nodeM, nodeN) && this.safeSubtreeMatch(node.getExpression(), o.getExpression()) && this.safeSubtreeMatch(node.getBody(), o.getBody());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof EnhancedForStatement){
            node.setPyStartPosition(((EnhancedForStatement) other).getPyStartPosition());
            node.setPyLength(((EnhancedForStatement) other).getPyLength());
            node.setPyLine(((EnhancedForStatement) other).getPyLine());
            node.setPyColumnOffSet(((EnhancedForStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(EnhancedForStatementWithElse node, Object other) {
        boolean match;
        if (other instanceof EnhancedForStatement){
            EnhancedForStatement o = (EnhancedForStatement)other;
            List nodeM = new ArrayList();
            List nodeN = new ArrayList();
            if (!node.getParameter().getName().getIdentifier().equals("DummyTerminalNode")){
                nodeM.add(node.getParameter());
            }
            if (!o.getParameter().getName().getIdentifier().equals("DummyTerminalNode")){
                nodeN.add(node.getParameter());
            }
            for (Object parameter : node.Parameters()) {
                SingleVariableDeclaration para = (SingleVariableDeclaration)parameter;
                if (!(para.getName().getIdentifier().equals("DummyTerminalNode"))){
                    nodeM.add(para);
                }
            }
            for (Object parameter : o.Parameters()) {
                SingleVariableDeclaration para = (SingleVariableDeclaration)parameter;
                if (!(para.getName().getIdentifier().equals("DummyTerminalNode"))){
                    nodeN.add(para);
                }
            }

            match =  this.safeSubtreeListMatch(nodeM, nodeN) && this.safeSubtreeMatch(node.getExpression(), o.getExpression()) && this.safeSubtreeMatch(node.getBody(), o.getBody()) && this.safeSubtreeMatch(node.getElseBody(), o.getElseBody());

        }
        else if (!(other instanceof EnhancedForStatementWithElse)) {
            match = false;
        } else {
            EnhancedForStatementWithElse o = (EnhancedForStatementWithElse)other;
            match =  this.safeSubtreeMatch(node.getParameter(), o.getParameter()) && this.safeSubtreeListMatch(node.Parameters(), o.Parameters()) && this.safeSubtreeMatch(node.getExpression(), o.getExpression()) && this.safeSubtreeMatch(node.getBody(), o.getBody()) && this.safeSubtreeMatch(node.getElseBody(), o.getElseBody());
        }

        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match){
            if (other instanceof EnhancedForStatement){
                node.setPyStartPosition(((EnhancedForStatement) other).getPyStartPosition());
                node.setPyLength(((EnhancedForStatement) other).getPyLength());
                node.setPyLine(((EnhancedForStatement) other).getPyLine());
                node.setPyColumnOffSet(((EnhancedForStatement) other).getPyColumnOffSet());
            }
            else{
                node.setPyStartPosition(((EnhancedForStatementWithElse) other).getPyStartPosition());
                node.setPyLength(((EnhancedForStatementWithElse) other).getPyLength());
                node.setPyLine(((EnhancedForStatementWithElse) other).getPyLine());
                node.setPyColumnOffSet(((EnhancedForStatementWithElse) other).getPyColumnOffSet());
            }
        }
        return match;
    }

    public boolean match(EnumConstantDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof EnumConstantDeclaration){
            node.setPyStartPosition(((EnumConstantDeclaration) other).getPyStartPosition());
            node.setPyLength(((EnumConstantDeclaration) other).getPyLength());
            node.setPyLine(((EnumConstantDeclaration) other).getPyLine());
            node.setPyColumnOffSet(((EnumConstantDeclaration) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(EnumDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof EnumDeclaration){
            node.setPyStartPosition(((EnumDeclaration) other).getPyStartPosition());
            node.setPyLength(((EnumDeclaration) other).getPyLength());
            node.setPyLine(((EnumDeclaration) other).getPyLine());
            node.setPyColumnOffSet(((EnumDeclaration) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ExportsDirective node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ExportsDirective){
            node.setPyStartPosition(((ExportsDirective) other).getPyStartPosition());
            node.setPyLength(((ExportsDirective) other).getPyLength());
            node.setPyLine(((ExportsDirective) other).getPyLine());
            node.setPyColumnOffSet(((ExportsDirective) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ExpressionMethodReference node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ExpressionMethodReference){
            node.setPyStartPosition(((ExpressionMethodReference) other).getPyStartPosition());
            node.setPyLength(((ExpressionMethodReference) other).getPyLength());
            node.setPyLine(((ExpressionMethodReference) other).getPyLine());
            node.setPyColumnOffSet(((ExpressionMethodReference) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ExpressionStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ExpressionStatement){
            node.setPyStartPosition(((ExpressionStatement) other).getPyStartPosition());
            node.setPyLength(((ExpressionStatement) other).getPyLength());
            node.setPyLine(((ExpressionStatement) other).getPyLine());
            node.setPyColumnOffSet(((ExpressionStatement) other).getPyColumnOffSet());

        }
        return match;
    }

    public boolean match(FieldAccess node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof FieldAccess){
            node.setPyStartPosition(((FieldAccess) other).getPyStartPosition());
            node.setPyLength(((FieldAccess) other).getPyLength());
            node.setPyLine(((FieldAccess) other).getPyLine());
            node.setPyColumnOffSet(((FieldAccess) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(FieldDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof FieldDeclaration){
            node.setPyStartPosition(((FieldDeclaration) other).getPyStartPosition());
            node.setPyLength(((FieldDeclaration) other).getPyLength());
            node.setPyLine(((FieldDeclaration) other).getPyLine());
            node.setPyColumnOffSet(((FieldDeclaration) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ForStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ForStatement){
            node.setPyStartPosition(((ForStatement) other).getPyStartPosition());
            node.setPyLength(((ForStatement) other).getPyLength());
            node.setPyLine(((ForStatement) other).getPyLine());
            node.setPyColumnOffSet(((ForStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(IfStatement node, Object other) {
        boolean match = super.match(node, other);

        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof IfStatement){
            node.setPyStartPosition(((IfStatement) other).getPyStartPosition());
            node.setPyLength(((IfStatement) other).getPyLength());
            node.setPyLine(((IfStatement) other).getPyLine());
            node.setPyColumnOffSet(((IfStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ImportDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ImportDeclaration){
            node.setPyStartPosition(((ImportDeclaration) other).getPyStartPosition());
            node.setPyLength(((ImportDeclaration) other).getPyLength());
            node.setPyLine(((ImportDeclaration) other).getPyLine());
            node.setPyColumnOffSet(((ImportDeclaration) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(InfixExpression node, Object other) {
        boolean match;
        if (!(other instanceof InfixExpression)) {
            match = false;
        } else {
            InfixExpression o = (InfixExpression)other;
            if (node.hasExtendedOperands() && o.hasExtendedOperands() && !this.safeSubtreeListMatch(node.extendedOperands(), o.extendedOperands())) {
                match = false;
            } else if (node.hasExtendedOperands() != o.hasExtendedOperands()) {
                if (node.hasExtendedOperands()){
                    Map<String, String> options = JavaCore.getOptions();
                    options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
                    options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
                    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
                    AST ast = new AST(options);

                    InfixExpression infix  = ast.newInfixExpression();
                    infix.setOperator(node.getOperator());
                    infix.setRightOperand((Expression) ASTNode.copySubtree(ast,(Expression) node.extendedOperands().get(node.extendedOperands().size()-1)));

                    InfixExpression left = ast.newInfixExpression();
                    left.setOperator(node.getOperator());
                    if (node.extendedOperands().size()==1){
                        left.setRightOperand((Expression) ASTNode.copySubtree(ast,node.getRightOperand()));
                        left.setLeftOperand((Expression) ASTNode.copySubtree(ast,node.getLeftOperand()));
                    }
                    else if (node.extendedOperands().size()==2){
                        left.setRightOperand((Expression) ASTNode.copySubtree(ast, (Expression)node.extendedOperands().get(0)));
                        InfixExpression left_left = ast.newInfixExpression();
                        left_left.setOperator(node.getOperator());
                        left_left.setLeftOperand((Expression) ASTNode.copySubtree(ast,node.getLeftOperand()));
                        left_left.setRightOperand((Expression) ASTNode.copySubtree(ast,node.getRightOperand()));
                        left.setLeftOperand(left_left);
                    }
                    else {
                        InfixExpression left_left = ast.newInfixExpression();
                        left_left.setOperator(node.getOperator());
                        left_left.setLeftOperand((Expression) ASTNode.copySubtree(ast,node.getLeftOperand()));
                        left_left.setRightOperand((Expression) ASTNode.copySubtree(ast,node.getRightOperand()));

                        for (int i = 0; i<node.extendedOperands().size()-2 ; i++){
                            InfixExpression temp = ast.newInfixExpression();
                            temp.setLeftOperand(left_left);
                            temp.setOperator(node.getOperator());
                            temp.setRightOperand((Expression) ASTNode.copySubtree(ast,(Expression) node.extendedOperands().get(i)));
                            left_left=temp;
                        }
                        left.setRightOperand((Expression) ASTNode.copySubtree(ast,(Expression) node.extendedOperands().get(node.extendedOperands().size()-2)));
                        left.setLeftOperand(left_left);
                    }
                    infix.setLeftOperand(left);
                    match = node.getOperator().toString().equals(o.getOperator().toString()) && this.safeSubtreeMatch(infix, o);

                }
                else{
                    match = false;
                }
            } else {
                match = node.getOperator().toString().equals(o.getOperator().toString()) && this.safeSubtreeMatch(node.getLeftOperand(), o.getLeftOperand()) && this.safeSubtreeMatch(node.getRightOperand(), o.getRightOperand());
            }
        }
        if (!match)
            System.out.printf("");
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match){
            node.setPyStartPosition(((InfixExpression) other).getPyStartPosition());
            node.setPyLength(((InfixExpression) other).getPyLength());
            node.setPyLine(((InfixExpression) other).getPyLine());
            node.setPyColumnOffSet(((InfixExpression) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(Initializer node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof Initializer){
            node.setPyStartPosition(((Initializer) other).getPyStartPosition());
            node.setPyLength(((Initializer) other).getPyLength());
            node.setPyLine(((Initializer) other).getPyLine());
            node.setPyColumnOffSet(((Initializer) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(InstanceofExpression node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof InstanceofExpression){
            node.setPyStartPosition(((InstanceofExpression) other).getPyStartPosition());
            node.setPyLength(((InstanceofExpression) other).getPyLength());
            node.setPyLine(((InstanceofExpression) other).getPyLine());
            node.setPyColumnOffSet(((InstanceofExpression) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(IntersectionType node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof IntersectionType){
            node.setPyStartPosition(((IntersectionType) other).getPyStartPosition());
            node.setPyLength(((IntersectionType) other).getPyLength());
            node.setPyLine(((IntersectionType) other).getPyLine());
            node.setPyColumnOffSet(((IntersectionType) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(Javadoc node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof Javadoc){
            node.setPyStartPosition(((Javadoc) other).getPyStartPosition());
            node.setPyLength(((Javadoc) other).getPyLength());
            node.setPyLine(((Javadoc) other).getPyLine());
            node.setPyColumnOffSet(((Javadoc) other).getPyColumnOffSet());
        }
        return match;
    }


    public boolean match(LabeledStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof LabeledStatement){
            node.setPyStartPosition(((LabeledStatement) other).getPyStartPosition());
            node.setPyLength(((LabeledStatement) other).getPyLength());
            node.setPyLine(((LabeledStatement) other).getPyLine());
            node.setPyColumnOffSet(((LabeledStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(LambdaExpression node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof LambdaExpression){
            node.setPyStartPosition(((LambdaExpression) other).getPyStartPosition());
            node.setPyLength(((LambdaExpression) other).getPyLength());
            node.setPyLine(((LambdaExpression) other).getPyLine());
            node.setPyColumnOffSet(((LambdaExpression) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(LineComment node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof LineComment){
            node.setPyStartPosition(((LineComment) other).getPyStartPosition());
            node.setPyLength(((LineComment) other).getPyLength());
            node.setPyLine(((LineComment) other).getPyLine());
            node.setPyColumnOffSet(((LineComment) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(MarkerAnnotation node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof MarkerAnnotation){
            node.setPyStartPosition(((MarkerAnnotation) other).getPyStartPosition());
            node.setPyLength(((MarkerAnnotation) other).getPyLength());
            node.setPyLine(((MarkerAnnotation) other).getPyLine());
            node.setPyColumnOffSet(((MarkerAnnotation) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(MemberRef node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof MemberRef){
            node.setPyStartPosition(((MemberRef) other).getPyStartPosition());
            node.setPyLength(((MemberRef) other).getPyLength());
            node.setPyLine(((MemberRef) other).getPyLine());
            node.setPyColumnOffSet(((MemberRef) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(MemberValuePair node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof MemberValuePair){
            node.setPyStartPosition(((MemberValuePair) other).getPyStartPosition());
            node.setPyLength(((MemberValuePair) other).getPyLength());
            node.setPyLine(((MemberValuePair) other).getPyLine());
            node.setPyColumnOffSet(((MemberValuePair) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(MethodRef node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof MethodRef){
            node.setPyStartPosition(((MethodRef) other).getPyStartPosition());
            node.setPyLength(((MethodRef) other).getPyLength());
            node.setPyLine(((MethodRef) other).getPyLine());
            node.setPyColumnOffSet(((MethodRef) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(MethodRefParameter node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof MethodRefParameter){
            node.setPyStartPosition(((MethodRefParameter) other).getPyStartPosition());
            node.setPyLength(((MethodRefParameter) other).getPyLength());
            node.setPyLine(((MethodRefParameter) other).getPyLine());
            node.setPyColumnOffSet(((MethodRefParameter) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(MethodDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof MethodDeclaration){
            node.setPyStartPosition(((MethodDeclaration) other).getPyStartPosition());
            node.setPyLength(((MethodDeclaration) other).getPyLength());
            node.setPyLine(((MethodDeclaration) other).getPyLine());
            node.setPyColumnOffSet(((MethodDeclaration) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(MethodInvocation node, Object other) {
        boolean match = super.match(node, other);
        if (!match) {
            System.out.println("");
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof MethodInvocation){
            node.setPyStartPosition(((MethodInvocation) other).getPyStartPosition());
            node.setPyLength(((MethodInvocation) other).getPyLength());
            node.setPyLine(((MethodInvocation) other).getPyLine());
            node.setPyColumnOffSet(((MethodInvocation) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(Modifier node, Object other) {
        boolean match;

        if (!(other instanceof Modifier)) {
            match =false;
        } else {
            Modifier o = (Modifier)other;
            match = node.getKeyword().toString().equals(o.getKeyword().toString());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match){
            node.setPyStartPosition(((Modifier) other).getPyStartPosition());
            node.setPyLength(((Modifier) other).getPyLength());
            node.setPyLine(((Modifier) other).getPyLine());
            node.setPyColumnOffSet(((Modifier) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ModuleDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ModuleDeclaration){
            node.setPyStartPosition(((ModuleDeclaration) other).getPyStartPosition());
            node.setPyLength(((ModuleDeclaration) other).getPyLength());
            node.setPyLine(((ModuleDeclaration) other).getPyLine());
            node.setPyColumnOffSet(((ModuleDeclaration) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ModuleModifier node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ModuleModifier){
            node.setPyStartPosition(((ModuleModifier) other).getPyStartPosition());
            node.setPyLength(((ModuleModifier) other).getPyLength());
            node.setPyLine(((ModuleModifier) other).getPyLine());
            node.setPyColumnOffSet(((ModuleModifier) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(NameQualifiedType node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof NameQualifiedType){
            node.setPyStartPosition(((NameQualifiedType) other).getPyStartPosition());
            node.setPyLength(((NameQualifiedType) other).getPyLength());
            node.setPyLine(((NameQualifiedType) other).getPyLine());
            node.setPyColumnOffSet(((NameQualifiedType) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(NormalAnnotation node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof NormalAnnotation){
            node.setPyStartPosition(((NormalAnnotation) other).getPyStartPosition());
            node.setPyLength(((NormalAnnotation) other).getPyLength());
            node.setPyLine(((NormalAnnotation) other).getPyLine());
            node.setPyColumnOffSet(((NormalAnnotation) other).getPyColumnOffSet());

        }
        return match;
    }

    public boolean match(NullLiteral node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof NullLiteral){
            node.setPyStartPosition(((NullLiteral) other).getPyStartPosition());
            node.setPyLength(((NullLiteral) other).getPyLength());
            node.setPyLine(((NullLiteral) other).getPyLine());
            node.setPyColumnOffSet(((NullLiteral) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(NumberLiteral node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof NumberLiteral){
            node.setPyStartPosition(((NumberLiteral) other).getPyStartPosition());
            node.setPyLength(((NumberLiteral) other).getPyLength());
            node.setPyLine(((NumberLiteral) other).getPyLine());
            node.setPyColumnOffSet(((NumberLiteral) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(OpensDirective node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof OpensDirective){
            node.setPyStartPosition(((OpensDirective) other).getPyStartPosition());
            node.setPyLength(((OpensDirective) other).getPyLength());
            node.setPyLine(((OpensDirective) other).getPyLine());
            node.setPyColumnOffSet(((OpensDirective) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PackageDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof PackageDeclaration){
            node.setPyStartPosition(((PackageDeclaration) other).getPyStartPosition());
            node.setPyLength(((PackageDeclaration) other).getPyLength());
            node.setPyColumnOffSet(((PackageDeclaration) other).getPyColumnOffSet());
            node.setPyLine(((PackageDeclaration) other).getPyLine());
        }
        return match;
    }

    public boolean match(ParameterizedType node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ParameterizedType){
            node.setPyStartPosition(((ParameterizedType) other).getPyStartPosition());
            node.setPyLength(((ParameterizedType) other).getPyLength());
            node.setPyLine(((ParameterizedType) other).getPyLine());
            node.setPyColumnOffSet(((ParameterizedType) other).getPyColumnOffSet());
        }

        return match;
    }

    public boolean match(ParenthesizedExpression node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof AnnotationTypeDeclaration){
            node.setPyStartPosition(((AnnotationTypeDeclaration) other).getPyStartPosition());
            node.setPyLength(((AnnotationTypeDeclaration) other).getPyLength());
            node.setPyLine(((AnnotationTypeDeclaration) other).getPyLine());
            node.setPyColumnOffSet(((AnnotationTypeDeclaration) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PostfixExpression node, Object other) {
        boolean match;
        if (!(other instanceof PostfixExpression)) {
            match = false;
        } else {
            PostfixExpression o = (PostfixExpression)other;
            match = node.getOperator().toString().equals(o.getOperator().toString()) && this.safeSubtreeMatch(node.getOperand(), o.getOperand());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match){
            node.setPyStartPosition(((PostfixExpression) other).getPyStartPosition());
            node.setPyLength(((PostfixExpression) other).getPyLength());
            node.setPyLine(((PostfixExpression) other).getPyLine());
            node.setPyColumnOffSet(((PostfixExpression) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PrefixExpression node, Object other) {
        boolean match;
        if (other instanceof NumberLiteral && other.toString().equals(node.toString())) {
            match = true;
        }
        else if (!(other instanceof PrefixExpression)) {
            match = false;
        }
        else {
            PrefixExpression o = (PrefixExpression)other;
            match = node.getOperator().toString().equals(o.getOperator().toString()) && this.safeSubtreeMatch(node.getOperand(), o.getOperand());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match){
            if (other instanceof org.eclipse.jdt.core.dom.NumberLiteral ){
                node.setPyStartPosition(((NumberLiteral) other).getPyStartPosition());
                node.setPyLength(((NumberLiteral) other).getPyLength());
                node.setPyLine(((NumberLiteral) other).getPyLine());
                node.setPyColumnOffSet(((NumberLiteral) other).getPyColumnOffSet());
            }
            else {
                node.setPyStartPosition(((PrefixExpression) other).getPyStartPosition());
                node.setPyLength(((PrefixExpression) other).getPyLength());
                node.setPyLine(((PrefixExpression) other).getPyLine());
                node.setPyColumnOffSet(((PrefixExpression) other).getPyColumnOffSet());
            }
        }
        return match;
    }

    public boolean match(PrimitiveType node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof PrimitiveType){
            node.setPyStartPosition(((PrimitiveType) other).getPyStartPosition());
            node.setPyLength(((PrimitiveType) other).getPyLength());
            node.setPyLine(((PrimitiveType) other).getPyLine());
            node.setPyColumnOffSet(((PrimitiveType) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ProvidesDirective node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ProvidesDirective){
            node.setPyStartPosition(((ProvidesDirective) other).getPyStartPosition());
            node.setPyLength(((ProvidesDirective) other).getPyLength());
            node.setPyLine(((ProvidesDirective) other).getPyLine());
            node.setPyColumnOffSet(((ProvidesDirective) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(QualifiedName node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof QualifiedName){
            node.setPyStartPosition(((QualifiedName) other).getPyStartPosition());
            node.setPyLength(((QualifiedName) other).getPyLength());
            node.setPyLine(((QualifiedName) other).getPyLine());
            node.setPyColumnOffSet(((QualifiedName) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ModuleQualifiedName node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ModuleQualifiedName){
            node.setPyStartPosition(((ModuleQualifiedName) other).getPyStartPosition());
            node.setPyLength(((ModuleQualifiedName) other).getPyLength());
            node.setPyLine(((ModuleQualifiedName) other).getPyLine());
            node.setPyColumnOffSet(((ModuleQualifiedName) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(QualifiedType node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof QualifiedType){
            node.setPyStartPosition(((QualifiedType) other).getPyStartPosition());
            node.setPyLength(((QualifiedType) other).getPyLength());
            node.setPyLine(((QualifiedType) other).getPyLine());
            node.setPyColumnOffSet(((QualifiedType) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(RecordDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof RecordDeclaration){
            node.setPyStartPosition(((RecordDeclaration) other).getPyStartPosition());
            node.setPyLength(((RecordDeclaration) other).getPyLength());
            node.setPyLine(((RecordDeclaration) other).getPyLine());
            node.setPyColumnOffSet(((RecordDeclaration) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(RequiresDirective node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof RequiresDirective){
            node.setPyStartPosition(((RequiresDirective) other).getPyStartPosition());
            node.setPyLength(((RequiresDirective) other).getPyLength());
            node.setPyLine(((RequiresDirective) other).getPyLine());
            node.setPyColumnOffSet(((RequiresDirective) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ReturnStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ReturnStatement){
            node.setPyStartPosition(((ReturnStatement) other).getPyStartPosition());
            node.setPyLength(((ReturnStatement) other).getPyLength());
            node.setPyLine(((ReturnStatement) other).getPyLine());
            node.setPyColumnOffSet(((ReturnStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SimpleName node, Object other) {
        boolean match = super.match(node, other);
        if (match!=true){
            System.out.println("");
        }
        if (DEBUG && !match && other instanceof SimpleName)
            log.info("Unmatched "+node.getClass() +" "+node.getIdentifier()+" : "+ ((SimpleName)other).getIdentifier());
        else if(DEBUG && !match)
            log.info("Unmatched "+node.getClass() +" "+node.getIdentifier());

        if (match && other instanceof SimpleName){
            node.setPyStartPosition(((SimpleName) other).getPyStartPosition());
            node.setPyLength(((SimpleName) other).getPyLength());
            node.setPyLine(((SimpleName) other).getPyLine());
            node.setPyColumnOffSet(((SimpleName) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SimpleType node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof SimpleType){
            node.setPyStartPosition(((SimpleType) other).getPyStartPosition());
            node.setPyLength(((SimpleType) other).getPyLength());
            node.setPyLine(((SimpleType) other).getPyLine());
            node.setPyColumnOffSet(((SimpleType) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SingleMemberAnnotation node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof SingleMemberAnnotation){
            node.setPyStartPosition(((SingleMemberAnnotation) other).getPyStartPosition());
            node.setPyLength(((SingleMemberAnnotation) other).getPyLength());
            node.setPyLine(((SingleMemberAnnotation) other).getPyLine());
            node.setPyColumnOffSet(((SingleMemberAnnotation) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SingleVariableDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof SingleVariableDeclaration){
            node.setPyStartPosition(((SingleVariableDeclaration) other).getPyStartPosition());
            node.setPyLength(((SingleVariableDeclaration) other).getPyLength());
            node.setPyLine(((SingleVariableDeclaration) other).getPyLine());
            node.setPyColumnOffSet(((SingleVariableDeclaration) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(StringLiteral node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof StringLiteral){
            node.setPyStartPosition(((StringLiteral) other).getPyStartPosition());
            node.setPyLength(((StringLiteral) other).getPyLength());
            node.setPyLine(((StringLiteral) other).getPyLine());
            node.setPyColumnOffSet(((StringLiteral) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SuperConstructorInvocation node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof SuperConstructorInvocation){
            node.setPyStartPosition(((SuperConstructorInvocation) other).getPyStartPosition());
            node.setPyLength(((SuperConstructorInvocation) other).getPyLength());
            node.setPyLine(((SuperConstructorInvocation) other).getPyLine());
            node.setPyColumnOffSet(((SuperConstructorInvocation) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SuperFieldAccess node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof SuperFieldAccess){
            node.setPyStartPosition(((SuperFieldAccess) other).getPyStartPosition());
            node.setPyLength(((SuperFieldAccess) other).getPyLength());
            node.setPyLine(((SuperFieldAccess) other).getPyLine());
            node.setPyColumnOffSet(((SuperFieldAccess) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SuperMethodInvocation node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof SuperMethodInvocation){
            node.setPyStartPosition(((SuperMethodInvocation) other).getPyStartPosition());
            node.setPyLength(((SuperMethodInvocation) other).getPyLength());
            node.setPyLine(((SuperMethodInvocation) other).getPyLine());
            node.setPyColumnOffSet(((SuperMethodInvocation) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SuperMethodReference node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof SuperMethodReference){
            node.setPyStartPosition(((SuperMethodReference) other).getPyStartPosition());
            node.setPyLength(((SuperMethodReference) other).getPyLength());
            node.setPyLine(((SuperMethodReference) other).getPyLine());
            node.setPyColumnOffSet(((SuperMethodReference) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SwitchCase node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof SwitchCase){
            node.setPyStartPosition(((SwitchCase) other).getPyStartPosition());
            node.setPyLength(((SwitchCase) other).getPyLength());
            node.setPyLine(((SwitchCase) other).getPyLine());
            node.setPyColumnOffSet(((SwitchCase) other).getPyColumnOffSet());
        }
        return match;
    }



    public boolean match(SwitchExpression node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof SwitchExpression){
            node.setPyStartPosition(((SwitchExpression) other).getPyStartPosition());
            node.setPyLength(((SwitchExpression) other).getPyLength());
            node.setPyLine(((SwitchExpression) other).getPyLine());
            node.setPyColumnOffSet(((SwitchExpression) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SwitchStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof SwitchStatement){
            node.setPyStartPosition(((SwitchStatement) other).getPyStartPosition());
            node.setPyLength(((SwitchStatement) other).getPyLength());
            node.setPyLine(((SwitchStatement) other).getPyLine());
            node.setPyColumnOffSet(((SwitchStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SynchronizedStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof SynchronizedStatement){
            node.setPyStartPosition(((SynchronizedStatement) other).getPyStartPosition());
            node.setPyLength(((SynchronizedStatement) other).getPyLength());
            node.setPyLine(((SynchronizedStatement) other).getPyLine());
            node.setPyColumnOffSet(((SynchronizedStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(TagElement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof TagElement){
            node.setPyStartPosition(((TagElement) other).getPyStartPosition());
            node.setPyLength(((TagElement) other).getPyLength());
            node.setPyLine(((TagElement) other).getPyLine());
            node.setPyColumnOffSet(((TagElement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(TextBlock node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof TextBlock){
            node.setPyStartPosition(((TextBlock) other).getPyStartPosition());
            node.setPyLength(((TextBlock) other).getPyLength());
            node.setPyLine(((TextBlock) other).getPyLine());
            node.setPyColumnOffSet(((TextBlock) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(TextElement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof TextElement){
            node.setPyStartPosition(((TextElement) other).getPyStartPosition());
            node.setPyLength(((TextElement) other).getPyLength());
            node.setPyLine(((TextElement) other).getPyLine());
            node.setPyColumnOffSet(((TextElement) other).getPyColumnOffSet());

        }
        return match;
    }

    public boolean match(ThisExpression node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ThisExpression){
            node.setPyStartPosition(((ThisExpression) other).getPyStartPosition());
            node.setPyLength(((ThisExpression) other).getPyLength());
            node.setPyLine(((ThisExpression) other).getPyLine());
            node.setPyColumnOffSet(((ThisExpression) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ThrowStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof ThrowStatement){
            node.setPyStartPosition(((ThrowStatement) other).getPyStartPosition());
            node.setPyLength(((ThrowStatement) other).getPyLength());
            node.setPyLine(((ThrowStatement) other).getPyLine());
            node.setPyColumnOffSet(((ThrowStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(TryStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof TryStatement){
            node.setPyStartPosition(((TryStatement) other).getPyStartPosition());
            node.setPyLength(((TryStatement) other).getPyLength());
            node.setPyLine(((TryStatement) other).getPyLine());
            node.setPyColumnOffSet(((TryStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(TypeDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof TypeDeclaration){
            node.setPyStartPosition(((TypeDeclaration) other).getPyStartPosition());
            node.setPyLength(((TypeDeclaration) other).getPyLength());
            node.setPyLine(((TypeDeclaration) other).getPyLine());
            node.setPyColumnOffSet(((TypeDeclaration) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(TypeDeclarationStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof TypeDeclarationStatement){
            node.setPyStartPosition(((TypeDeclarationStatement) other).getPyStartPosition());
            node.setPyLength(((TypeDeclarationStatement) other).getPyLength());
            node.setPyLine(((TypeDeclarationStatement) other).getPyLine());
            node.setPyColumnOffSet(((TypeDeclarationStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(TypeLiteral node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof TypeLiteral){
            node.setPyStartPosition(((TypeLiteral) other).getPyStartPosition());
            node.setPyLength(((TypeLiteral) other).getPyLength());
            node.setPyLine(((TypeLiteral) other).getPyLine());
            node.setPyColumnOffSet(((TypeLiteral) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(TypeMethodReference node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof TypeMethodReference){
            node.setPyStartPosition(((TypeMethodReference) other).getPyStartPosition());
            node.setPyLength(((TypeMethodReference) other).getPyLength());
            node.setPyLine(((TypeMethodReference) other).getPyLine());
            node.setPyColumnOffSet(((TypeMethodReference) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(TypeParameter node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof TypeParameter){
            node.setPyStartPosition(((TypeParameter) other).getPyStartPosition());
            node.setPyLength(((TypeParameter) other).getPyLength());
            node.setPyLine(((TypeParameter) other).getPyLine());
            node.setPyColumnOffSet(((TypeParameter) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(UnionType node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof UnionType){
            node.setPyStartPosition(((UnionType) other).getPyStartPosition());
            node.setPyLength(((UnionType) other).getPyLength());
            node.setPyLine(((UnionType) other).getPyLine());
            node.setPyColumnOffSet(((UnionType) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(UsesDirective node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof UsesDirective){
            node.setPyStartPosition(((UsesDirective) other).getPyStartPosition());
            node.setPyLength(((UsesDirective) other).getPyLength());
            node.setPyLine(((UsesDirective) other).getPyLine());
            node.setPyColumnOffSet(((UsesDirective) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(VariableDeclarationExpression node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof VariableDeclarationExpression){
            node.setPyStartPosition(((VariableDeclarationExpression) other).getPyStartPosition());
            node.setPyLength(((VariableDeclarationExpression) other).getPyLength());
            node.setPyLine(((VariableDeclarationExpression) other).getPyLine());
            node.setPyColumnOffSet(((VariableDeclarationExpression) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(VariableDeclarationFragment node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof VariableDeclarationFragment){
            node.setPyStartPosition(((VariableDeclarationFragment) other).getPyStartPosition());
            node.setPyLength(((VariableDeclarationFragment) other).getPyLength());
            node.setPyLine(((VariableDeclarationFragment) other).getPyLine());
            node.setPyColumnOffSet(((VariableDeclarationFragment) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(VariableDeclarationStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof VariableDeclarationStatement){
            node.setPyStartPosition(((VariableDeclarationStatement) other).getPyStartPosition());
            node.setPyLength(((VariableDeclarationStatement) other).getPyLength());
            node.setPyLine(((VariableDeclarationStatement) other).getPyLine());
            node.setPyColumnOffSet(((VariableDeclarationStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(WhileStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof WhileStatement){
            node.setPyStartPosition(((WhileStatement) other).getPyStartPosition());
            node.setPyLength(((WhileStatement) other).getPyLength());
            node.setPyLine(((WhileStatement) other).getPyLine());
            node.setPyColumnOffSet(((WhileStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(WildcardType node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof WildcardType){
            node.setPyStartPosition(((WildcardType) other).getPyStartPosition());
            node.setPyLength(((WildcardType) other).getPyLength());
            node.setPyLine(((WildcardType) other).getPyLine());
            node.setPyColumnOffSet(((WildcardType) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(YieldStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof YieldStatement){
            node.setPyStartPosition(((YieldStatement) other).getPyStartPosition());
            node.setPyLength(((YieldStatement) other).getPyLength());
            node.setPyLine(((YieldStatement) other).getPyLine());
            node.setPyColumnOffSet(((YieldStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PyInExpression node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof PyInExpression){
            node.setPyStartPosition(((PyInExpression) other).getPyStartPosition());
            node.setPyLength(((PyInExpression) other).getPyLength());
            node.setPyLine(((PyInExpression) other).getPyLine());
            node.setPyColumnOffSet(((PyInExpression) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PyTupleExpression node, Object other) {
        boolean match = super.match(node, other);

        if (DEBUG && !match )
            log.info("Unmatched "+node.getClass());
        if (match && other instanceof PyTupleExpression){
            node.setPyStartPosition(((PyTupleExpression) other).getPyStartPosition());
            node.setPyLength(((PyTupleExpression) other).getPyLength());
            node.setPyLine(((PyTupleExpression) other).getPyLine());
            node.setPyColumnOffSet(((PyTupleExpression) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PyGenerator node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof PyGenerator){
            node.setPyStartPosition(((PyGenerator) other).getPyStartPosition());
            node.setPyLength(((PyGenerator) other).getPyLength());
            node.setPyLine(((PyGenerator) other).getPyLine());
            node.setPyColumnOffSet(((PyGenerator) other).getPyColumnOffSet());
        }

        return match;
    }

    public boolean match(PyComparator node, Object other) {
        boolean match;
        if (!(other instanceof PyComparator)) {
            match = false;
        } else {
            PyComparator o = (PyComparator)other;
            List nodeM = new ArrayList();
            List nodeN = new ArrayList();

            for (Object parameter : node.getValueExpression()) {
                SingleVariableDeclaration para = (SingleVariableDeclaration)parameter;
                if (!(para.getName().getIdentifier().equals("DummyTerminalTypeNode"))){
                    nodeM.add(para);
                }
            }
            for (Object parameter : o.getValueExpression()) {
                SingleVariableDeclaration para = (SingleVariableDeclaration)parameter;
                if (!(para.getName().getIdentifier().equals("DummyTerminalNode"))){
                    nodeN.add(para);
                }
            }
            if (node.internalGetConditionalExpression() != null  &&  !(node.internalGetConditionalExpression() instanceof SimpleName && ((SimpleName) node.internalGetConditionalExpression()).getIdentifier().equals("DUMMY"))) {
                match = this.safeSubtreeMatch(node.getIteratorExpression(), o.getIteratorExpression()) && this.safeSubtreeMatch(node.getConditionalExpression(), o.getConditionalExpression()) && this.safeSubtreeListMatch(nodeM, nodeN);
            } else {
                match = this.safeSubtreeMatch(node.getIteratorExpression(), o.getIteratorExpression()) && this.safeSubtreeListMatch(nodeM, nodeN);
            }
        }

        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match){
            node.setPyStartPosition(((PyComparator) other).getPyStartPosition());
            node.setPyLength(((PyComparator) other).getPyLength());
            node.setPyLine(((PyComparator) other).getPyLine());
            node.setPyColumnOffSet(((PyComparator) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PyNotInExpression node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof PyNotInExpression){
            node.setPyStartPosition(((PyNotInExpression) other).getPyStartPosition());
            node.setPyLength(((PyNotInExpression) other).getPyLength());
            node.setPyLine(((PyNotInExpression) other).getPyLine());
            node.setPyColumnOffSet(((PyNotInExpression) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PyListComprehension node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof PyListComprehension){
            node.setPyStartPosition(((PyListComprehension) other).getPyStartPosition());
            node.setPyLength(((PyListComprehension) other).getPyLength());
            node.setPyLine(((PyListComprehension) other).getPyLine());
            node.setPyColumnOffSet(((PyListComprehension) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PyDictComprehension node, Object other) {
        boolean match ;
        if (other instanceof PySetComprehension){
            {
                PySetComprehension o = (PySetComprehension)other;
                match = this.safeSubtreeListMatch(node.getComparator(), o.getComparator()) && this.safeSubtreeMatch(node.getTarget1Expression(), o.getTargetExpression());
            }

        }
        else{
            match = super.match(node, other);
        }

        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof PyDictComprehension){
            node.setPyStartPosition(((PyDictComprehension) other).getPyStartPosition());
            node.setPyLength(((PyDictComprehension) other).getPyLength());
            node.setPyLine(((PyDictComprehension) other).getPyLine());
            node.setPyColumnOffSet(((PyDictComprehension) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PySetComprehension node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof PySetComprehension){
            node.setPyStartPosition(((PySetComprehension) other).getPyStartPosition());
            node.setPyLength(((PySetComprehension) other).getPyLength());
            node.setPyLine(((PySetComprehension) other).getPyLine());
            node.setPyColumnOffSet(((PySetComprehension) other).getPyColumnOffSet());
        }

        return match;
    }

    public boolean match(PyYieldReturnStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof PyYieldReturnStatement){
            node.setPyStartPosition(((PyYieldReturnStatement) other).getPyStartPosition());
            node.setPyLength(((PyYieldReturnStatement) other).getPyLength());
            node.setPyLine(((PyYieldReturnStatement) other).getPyLine());
            node.setPyColumnOffSet(((PyYieldReturnStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PyNonLocalStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof PyNonLocalStatement){
            node.setPyStartPosition(((PyNonLocalStatement) other).getPyStartPosition());
            node.setPyLength(((PyNonLocalStatement) other).getPyLength());
            node.setPyLine(((PyNonLocalStatement) other).getPyLine());
            node.setPyColumnOffSet(((PyNonLocalStatement) other).getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PyWithStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match && other instanceof PyWithStatement){
            node.setPyStartPosition(((PyWithStatement) other).getPyStartPosition());
            node.setPyLength(((PyWithStatement) other).getPyLength());
            node.setPyLine(((PyWithStatement) other).getPyLine());
            node.setPyColumnOffSet(((PyWithStatement) other).getPyColumnOffSet());
        }
        return match;
    }
}
