package python3;


import python3.pyerrors.NodeNotFoundException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.jpp.astnodes.ast.Attribute;
import org.jpp.astnodes.ast.Call;
import org.jpp.astnodes.ast.List;
import org.jpp.astnodes.ast.Name;
import org.jpp.astnodes.ast.Num;
import org.jpp.astnodes.ast.Subscript;
import org.jpp.astnodes.base.expr;
import org.jpp.heart.AstList;
import org.jpp.heart.PyLong;
import org.jpp.astnodes.ast.Index;
import python3.pyerrors.ExpressionNotFound;

public class MapPyExpressionsJDK {
    public static Expression mapExpression(expr pyexp, AST ast) throws ExpressionNotFound, NodeNotFoundException {
        if (pyexp instanceof Num){
            if (((Num) pyexp).getN() instanceof PyLong){
                return ast.newNumberLiteral(String.valueOf(((PyLong)((Num) pyexp).getN()).getValue()));
            }
            else {
                throw new ExpressionNotFound("Corresponding Expression is not Found "+pyexp.getClass());
            }

        }
        else if (pyexp instanceof Name){
            return ast.newSimpleName(((Name) pyexp).getId().toString());
        }
        else if (pyexp instanceof Call){
            MethodInvocation invocation = ast.newMethodInvocation();
            if (((Call) pyexp).getFunc() instanceof Name){
                SimpleName method_name = (SimpleName)mapExpression((expr) ((Call) pyexp).getFunc(), ast);
                invocation.setName(method_name);
                for (Object arg : (AstList) ((Call) pyexp).getArgs()) {
                    invocation.arguments().add(mapExpression((expr) arg,ast));
                }
                return invocation;
            }
            else if (((Call) pyexp).getFunc() instanceof Attribute){
                SimpleName method_name = ast.newSimpleName(((Attribute)((Call) pyexp).getFunc()).getAttr().toString());
                Expression expre = mapExpression( (expr) ((Attribute)((Call) pyexp).getFunc()).getValue(),ast);
                invocation.setName(method_name);
                invocation.setExpression(expre);
                for (Object arg : (AstList) ((Call) pyexp).getArgs()) {
                    invocation.arguments().add(mapExpression((expr) arg,ast));
                }
                return invocation;
            }
            else{
                throw new NodeNotFoundException("Node not found for Call Object");
            }
        }
        else if (pyexp instanceof Subscript){
            ArrayAccess arrayAccess = ast.newArrayAccess();
            arrayAccess.setArray(mapExpression((expr)((Subscript) pyexp).getValue(),ast));
            arrayAccess.setIndex(mapExpression((expr) ((Index)((Subscript) pyexp).getSlice()).getValue(),ast));
            return arrayAccess;
        }
        else if (pyexp instanceof List){
            ArrayInitializer arrayInitializer = ast.newArrayInitializer();
            for (Object elt : (AstList) ((List) pyexp).getElts()) {
                arrayInitializer.expressions().add(mapExpression((expr) elt,ast));
            }


            //TODO fille the data
            return arrayInitializer;
        }
        else if (pyexp instanceof Attribute){
            var expression = mapExpression((expr) ((Attribute) pyexp).getValue(), ast);
            var simpleName = ast.newSimpleName(((Attribute) pyexp).getAttr().toString());
            QualifiedName qualifiedName = ast.newQualifiedName((org.eclipse.jdt.core.dom.Name) expression, simpleName);
            return qualifiedName;
        }
        else {
            throw new ExpressionNotFound("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
        }
    }
}
