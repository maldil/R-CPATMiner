package python3;

import org.antlr.runtime.tree.CommonTree;
import org.apache.log4j.Logger;
import org.jpp.PyASTParser;
import org.jpp.astnodes.PythonTree;
import org.jpp.astnodes.ast.Expr;
import org.jpp.astnodes.base.mod;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class MapPyExpressionsJDK {
    static Logger logger = Logger.getLogger(MapPyExpressionsJDK.class);
    public static Expression mapExpression(expr pyexp, AST ast, HashMap<String, org.eclipse.jdt.core.dom.Name> import_nodes) throws ExpressionNotFound, NodeNotFoundException {
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
                SimpleName method_name = (SimpleName)mapExpression((expr) ((Call) pyexp).getFunc(), ast, import_nodes);
                invocation.setName(method_name);
                for (Object arg : (AstList) ((Call) pyexp).getArgs()) {
                    invocation.arguments().add(mapExpression((expr) arg,ast, import_nodes));
                }
                return invocation;
            }
            else if (((Call) pyexp).getFunc() instanceof Attribute){
                SimpleName method_name = ast.newSimpleName(((Attribute)((Call) pyexp).getFunc()).getAttr().toString());
                Expression expre;
                if (((Attribute)((Call) pyexp).getFunc()).getValue() instanceof Name && import_nodes.get(((Name)((Attribute)((Call) pyexp).getFunc()).getValue()).getId().toString())!=null){
                    org.eclipse.jdt.core.dom.Name name = import_nodes.get(((Name) ((Attribute) ((Call) pyexp).getFunc()).getValue()).getId().toString());
                    if (name.getFullyQualifiedName().contains("."))
                    {
                        mod mod = PyASTParser.parsePython(name.getFullyQualifiedName());
                        expre = mapExpression((expr) mod.getChild(0).getChild(0),ast,import_nodes);

                    }
                    else{
                        expre = ast.newSimpleName(name.getFullyQualifiedName());
                    }
                }
                else
                {
                    expre = mapExpression( (expr) ((Attribute)((Call) pyexp).getFunc()).getValue(),ast, import_nodes);
                }
                invocation.setName(method_name);
                invocation.setExpression(expre);
                for (Object arg : (AstList) ((Call) pyexp).getArgs()) {
                    invocation.arguments().add(mapExpression((expr) arg,ast, import_nodes));
                }
                return invocation;
            }
            else{
                throw new NodeNotFoundException("Node not found for Call Object");
            }
        }
        else if (pyexp instanceof Subscript){
            ArrayAccess arrayAccess = ast.newArrayAccess();
            arrayAccess.setArray(mapExpression((expr)((Subscript) pyexp).getValue(),ast, import_nodes));
            arrayAccess.setIndex(mapExpression((expr) ((Index)((Subscript) pyexp).getSlice()).getValue(),ast, import_nodes));
            return arrayAccess;
        }
        else if (pyexp instanceof List){
            ArrayInitializer arrayInitializer = ast.newArrayInitializer();
            for (Object elt : (AstList) ((List) pyexp).getElts()) {
                arrayInitializer.expressions().add(mapExpression((expr) elt,ast, import_nodes));
            }


            //TODO fille the data
            return arrayInitializer;
        }
        else if (pyexp instanceof Attribute){
            Expression expression;

            if (((Attribute) pyexp).getValue() instanceof Name && import_nodes.get(((Name)(((Attribute) pyexp).getValue())).getId().toString())!=null)
            {
                org.eclipse.jdt.core.dom.Name name = import_nodes.get(((Name) (((Attribute) pyexp).getValue())).getId().toString());
                if (name.getFullyQualifiedName().contains("."))
                {
                    mod mod = PyASTParser.parsePython(name.getFullyQualifiedName());
                    expression = mapExpression((expr) mod.getChild(0).getChild(0),ast,import_nodes);

                }
                else{
                    expression = ast.newSimpleName(name.getFullyQualifiedName());
                }
            }
            else{
                expression = mapExpression((expr) ((Attribute) pyexp).getValue(), ast, import_nodes);
            }
            SimpleName simpleName = ast.newSimpleName(((Attribute) pyexp).getAttr().toString());
            return ast.newQualifiedName((org.eclipse.jdt.core.dom.Name) expression, simpleName);
        }
        else {
            throw new ExpressionNotFound("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
        }
    }
}
