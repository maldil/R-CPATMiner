package python3;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PyDictComprehension;
import org.eclipse.jdt.core.dom.PyErrorExpression;
import org.eclipse.jdt.core.dom.PyGenerator;
import org.eclipse.jdt.core.dom.PyInExpression;
import org.eclipse.jdt.core.dom.PyListComprehension;
import org.eclipse.jdt.core.dom.PyNotInExpression;
import org.eclipse.jdt.core.dom.PySetComprehension;
import org.eclipse.jdt.core.dom.PyTupleExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;
import org.jpp.PyASTParser;
import org.jpp.astnodes.ast.Assign;
import org.jpp.astnodes.ast.BoolOp;
import org.jpp.astnodes.ast.Compare;
import org.jpp.astnodes.ast.Dict;
import org.jpp.astnodes.ast.DictComp;
import org.jpp.astnodes.ast.ErrorExpr;
import org.jpp.astnodes.ast.ExtSlice;
import org.jpp.astnodes.ast.GeneratorExp;
import org.jpp.astnodes.ast.ListComp;
import org.jpp.astnodes.ast.Set;
import org.jpp.astnodes.ast.SetComp;
import org.jpp.astnodes.ast.Slice;
import org.jpp.astnodes.ast.Starred;
import org.jpp.astnodes.ast.Tuple;
import org.jpp.astnodes.ast.UnaryOp;
import org.jpp.astnodes.ast.boolopType;
import org.jpp.astnodes.ast.cmpopType;
import org.jpp.astnodes.ast.comprehension;
import org.jpp.astnodes.ast.keyword;
import org.jpp.astnodes.ast.operatorType;
import org.jpp.astnodes.ast.unaryopType;
import org.jpp.astnodes.base.mod;
import org.jpp.astnodes.base.slice;
import org.jpp.heart.Py;
import org.jpp.heart.PyFloat;
import org.jpp.heart.PyObject;
import python3.pyerrors.NodeNotFoundException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.ArrayAccess;

import org.jpp.astnodes.ast.Attribute;
import org.jpp.astnodes.ast.Call;
import org.jpp.astnodes.ast.List;
import org.jpp.astnodes.ast.Name;
import org.jpp.astnodes.ast.Num;
import org.jpp.astnodes.ast.Subscript;
import org.jpp.astnodes.ast.Str;
import org.jpp.astnodes.base.expr;
import org.jpp.heart.AstList;
import org.jpp.astnodes.ast.BinOp;
import org.jpp.astnodes.ast.IfExp;


import org.jpp.heart.PyLong;
import org.jpp.astnodes.ast.Index;
import python3.pyerrors.ExpressionNotFound;
import python3.typeinference.core.TypeASTNode;
import python3.typeinference.core.TypeApproximator;
import python3.typeinference.core.TypeStringToJDT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MapPyExpressionsJDK extends PyMap {
    static Logger logger = Logger.getLogger(MapPyExpressionsJDK.class);
    public static Expression mapExpression(expr pyexp, AST ast, HashMap<String, org.eclipse.jdt.core.dom.Name> import_nodes, int start_char_pos, Map<TypeASTNode, String> typeNodes, PyCompilationUnit pyc) throws ExpressionNotFound, NodeNotFoundException {
        if (pyexp instanceof Num){
            if (((Num) pyexp).getN() instanceof PyLong){
                NumberLiteral numberLiteral = ast.newNumberLiteral(String.valueOf(((PyLong) ((Num) pyexp).getN()).getValue()));
                numberLiteral.setSourceRange(start_char_pos,numberLiteral.toString().length());
                numberLiteral.setPyObject(pyexp);
                return numberLiteral;
            }
            else if (((Num) pyexp).getN() instanceof PyFloat ){
                NumberLiteral numberLiteral = ast.newNumberLiteral(String.valueOf(((PyFloat) ((Num) pyexp).getN()).getValue()));
                numberLiteral.setSourceRange(start_char_pos,numberLiteral.toString().length());
                numberLiteral.setPyObject(pyexp);
                return numberLiteral;
            }
            else {
                throw new ExpressionNotFound("Corresponding Expression is not Found "+pyexp.getClass());
            }

        }
        else if (pyexp instanceof Name){
            String id = mapPythonKeyWords(((Name) pyexp).getId().toString());
            SimpleName simpleName = ast.newSimpleName(id);
            simpleName.setSourceRange(pyexp.getCharStartIndex()+PyMap.totalCharGains,((Name) pyexp).getId().toString().length());
            simpleName.setPyObject(pyexp);
            return simpleName;
        }
        else if (pyexp instanceof Call){
            MethodInvocation invocation = ast.newMethodInvocation();
            if (((Call) pyexp).getFunc() instanceof Name){

                SimpleName method_name = (SimpleName)mapExpression((expr) ((Call) pyexp).getFunc(), ast, import_nodes,start_char_pos,typeNodes,pyc );
                invocation.setName(method_name);
                for (Object arg : (AstList) ((Call) pyexp).getArgs()) {  //TODO keyword arguments are not parsed by the JPyParser, Thus is a Bug
                    Expression expression = mapExpression((expr) arg, ast, import_nodes, start_char_pos + 1 + method_name.toString().length(), typeNodes, pyc);
                    if (expression instanceof PyErrorExpression){
                        return ast.newPyErrorExpression();
                    }
                    else{
                        invocation.arguments().add(expression);
                    }

                }
                for (Object karg: (AstList) ((Call) pyexp).getKeywords()){
                    Expression expression = mapExpression((expr) ((keyword) karg).getValue(),ast, import_nodes,start_char_pos+1+method_name.toString().length(),typeNodes, pyc);
                    if (expression instanceof PyErrorExpression){
                        return ast.newPyErrorExpression();
                    }
                    else{
                        invocation.arguments().add(expression);
                    }
                    logger.debug("Key ward is neglected : "+((keyword) karg).getArg());
                }
                invocation.setPyObject(pyexp);
                return invocation;
            }
            else if (((Call) pyexp).getFunc() instanceof Attribute){
                SimpleName method_name = ast.newSimpleName(((Attribute)((Call) pyexp).getFunc()).getAttr().toString());
                Expression expre;
                if (((Attribute)((Call) pyexp).getFunc()).getValue() instanceof Name && import_nodes.get(((Name)((Attribute)((Call) pyexp).getFunc()).getValue()).getId().toString())!=null){
                    org.eclipse.jdt.core.dom.Name name = import_nodes.get(((Name) ((Attribute) ((Call) pyexp).getFunc()).getValue()).getId().toString());
                    if (name.getFullyQualifiedName().contains("."))
                    {
//                        mod mod = PyASTParser.parsePython(name.getFullyQualifiedName());
//                        expre =  mapExpression((expr) mod.getChild(0).getChild(0),ast,import_nodes,0);
                        expre = ast.newName(name.getFullyQualifiedName().split("\\."));

                    }
                    else{
                        SimpleName simpleName = ast.newSimpleName(name.getFullyQualifiedName());
                        simpleName.setSourceRange(((Attribute) ((Call) pyexp).getFunc()).getCharStartIndex(),name.getFullyQualifiedName().length());
                        expre = simpleName;
                    }
                }
                else
                {
                    expre = mapExpression( (expr) ((Attribute)((Call) pyexp).getFunc()).getValue(),ast, import_nodes,0,typeNodes, pyc);
                }

                method_name.setSourceRange(pyexp.getCharStartIndex()+expre.toString().length()+PyMap.totalCharGains,method_name.toString().length());

                invocation.setName(method_name);
                invocation.setExpression(expre);
                for (Object arg : (AstList) ((Call) pyexp).getArgs()) {
                    invocation.arguments().add(mapExpression((expr) arg,ast, import_nodes,0,typeNodes,pyc ));
                }
                for (Object karg: (AstList) ((Call) pyexp).getKeywords()){
                    invocation.arguments().add(mapExpression((expr) ((keyword) karg).getValue(),ast, import_nodes,start_char_pos+1+method_name.toString().length(),typeNodes,pyc ));
                    logger.debug("Key ward is neglected : "+((keyword) karg).getArg());
                }
                invocation.setSourceRange(start_char_pos,invocation.toString().length());
                return invocation;
            }
            else{
                throw new NodeNotFoundException("Node not found for Call Object");
            }
        }
        else if (pyexp instanceof Subscript){
            ArrayAccess arrayAccess = ast.newArrayAccess();

            if (((Subscript) pyexp).getSlice() instanceof ExtSlice) {
                PyTupleExpression tupleExpression = ast.newPyTupleExpression();
                for (slice internalDim : ((ExtSlice) ((Subscript) pyexp).getSlice()).getInternalDims()) {
                    if (internalDim instanceof Slice) {
                        StringLiteral stringLiteral = ast.newStringLiteral();
                        stringLiteral.setLiteralValue("PythonSlice");
                        tupleExpression.expressions().add(stringLiteral);
                    } else {
                        tupleExpression.expressions().add(mapExpression((expr) ((Index) internalDim).getValue(), ast, import_nodes, 0, typeNodes, pyc));
                    }
                }
                arrayAccess.setArray(mapExpression((expr)((Subscript) pyexp).getValue(),ast, import_nodes,0,typeNodes,pyc  ));
                arrayAccess.setIndex(tupleExpression);
                return arrayAccess;
//                for (int j = 0;j<slice_length;j++) {
//
//
//                    slice slice = ((ExtSlice) ((Subscript) pyexp).getSlice()).getInternalDims().get(j);
//                    if (arrayAccess1==null){
//                        arrayAccess1=ast.newArrayAccess();
//                    }
//                    else{
//                        ArrayAccess arrayAccess2 = ast.newArrayAccess();
//                        arrayAccess2.setArray(arrayAccess1);
//                        arrayAccess1=arrayAccess2;
//                    }
//                    if (slice instanceof Slice){
//                        StringLiteral stringLiteral = ast.newStringLiteral();
//                        stringLiteral.setLiteralValue("PythonSlice");
//                        arrayAccess1.setIndex(stringLiteral);
//                    }
//                    else if (slice instanceof Index){
//                        arrayAccess1.setIndex(mapExpression((expr) ((Index)slice).getValue() ,ast, import_nodes,0,typeNodes, pyc));
//                    }
//                    if (j==0){
//                        arrayAccess1.setArray(mapExpression((expr)((Subscript) pyexp).getValue(),ast, import_nodes,0,typeNodes,pyc  ));
//                    }
//            }

//                Object o = ((AstList) ((ExtSlice) ((Subscript) pyexp).getSlice()).getDims()).get(slice_length - 1);
//                if (o instanceof Slice){
//                    StringLiteral stringLiteral = ast.newStringLiteral();
//                    stringLiteral.setLiteralValue("PythonSlice");
//                    arrayAccess1.setIndex(stringLiteral);
//                }
//                else if (o instanceof Index){
//                    arrayAccess1.setIndex(mapExpression((expr) ((Index)o).getValue() ,ast, import_nodes,0,typeNodes, pyc));
//                }
//

            }
            else{
                arrayAccess.setArray(mapExpression((expr)((Subscript) pyexp).getValue(),ast, import_nodes,0,typeNodes,pyc  ));
                if (((Subscript) pyexp).getSlice() instanceof Slice){
                    StringLiteral stringLiteral = ast.newStringLiteral();
                    stringLiteral.setLiteralValue("PythonSlice");
                    arrayAccess.setIndex(stringLiteral);
                }
                else{
                    arrayAccess.setIndex(mapExpression((expr) ((Index)((Subscript) pyexp).getSlice()).getValue(),ast, import_nodes,0,typeNodes, pyc));
                }

            }


            arrayAccess.setSourceRange(pyexp.getCharStartIndex()+PyMap.totalCharGains,pyexp.getCharStopIndex()-pyexp.getCharStartIndex());
            return arrayAccess;
        }
        else if (pyexp instanceof List){
            ArrayInitializer arrayInitializer = ast.newArrayInitializer();
            ((AstList) ((List) pyexp).getElts()).stream().filter(x->((expr)x).getParent()==null).forEach(x->((expr)x).setParent(pyexp));

            for (Object elt : (AstList) ((List) pyexp).getElts()) {
                Expression expression = mapExpression((expr) elt,ast, import_nodes,0,typeNodes,pyc  );
                if (expression instanceof PyErrorExpression){
                    return ast.newPyErrorExpression();
                }
                else{
                    arrayInitializer.expressions().add(expression);
                }
            }
            arrayInitializer.setSourceRange(pyexp.getCharStartIndex()+PyMap.totalCharGains,pyexp.getCharStopIndex()-pyexp.getCharStartIndex());

            if (!(pyexp.getParent() instanceof List)){
                ArrayCreation arrayCreation = ast.newArrayCreation();

                Type type = TypeApproximator.getSimpleTypeApproximation(ast, pyexp);
                if (type!=null){
                    arrayCreation.setType((ArrayType) type);
                }
                else{
                    if (pyexp.getParent() instanceof Assign){
                        if (((AstList)((Assign)pyexp.getParent()).getTargets()).size()>1){
                            logger.fatal("have to handle more than one assignments");
                        }
                        else if (((AstList)((Assign)pyexp.getParent()).getTargets()).get(0) instanceof Attribute){
                            Object o = ((AstList) ((Assign) pyexp.getParent()).getTargets()).get(0);
                            String typeString = typeNodes.get(new TypeASTNode(((Attribute) o).getLineno(),
                                    ((Attribute) o).getCol_offset()+((Attribute) o).getValue().toString().length()+1, ((Attribute) o).getAttr().toString(), null));

                            updateArrayTpe(ast, arrayCreation, typeString);
                        }
                        else if (((AstList)((Assign)pyexp.getParent()).getTargets()).get(0) instanceof Name){
                            String typeString = typeNodes.get(new TypeASTNode(((Name) ((AstList)((Assign)pyexp.getParent()).getTargets()).get(0)).getLineno(),
                                    ((Name) ((AstList)((Assign)pyexp.getParent()).getTargets()).get(0)).getCol_offset(), ((Name) ((AstList)((Assign)pyexp.getParent()).getTargets()).get(0)).getInternalId(), null));
                            updateArrayTpe(ast, arrayCreation, typeString);
                        }
                        else
                        {
                            Type jdtType1 = TypeStringToJDT.getJDTType(ast, "List[Any]", 0);
                            arrayCreation.setType((ArrayType) jdtType1);
                            logger.fatal("Created any type");
                            logger.fatal("Assigned target is not an instance of Name");
                        }
                    }//TODO write a component that can infer types of the arrays
                }
                arrayCreation.setInitializer(arrayInitializer);
                return arrayCreation;
            }
            else
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
                    expression = mapExpression((expr) mod.getChild(0).getChild(0),ast,import_nodes,0,typeNodes,pyc  );

                }
                else{
                    SimpleName simpleName = ast.newSimpleName(name.getFullyQualifiedName());
                    simpleName.setSourceRange(name.getStartPosition()+PyMap.totalCharGains,name.getLength());
                    expression = simpleName;
                }
            }
            else{
                expression = mapExpression((expr) ((Attribute) pyexp).getValue(), ast, import_nodes,0,typeNodes,pyc  );
            }
            SimpleName simpleName = ast.newSimpleName(mapPythonKeyWords(((Attribute) pyexp).getAttr().toString()));
            simpleName.setSourceRange(pyexp.getCharStartIndex()+PyMap.totalCharGains,((Attribute) pyexp).getAttr().toString().length());
            if (expression instanceof ArrayAccess || expression instanceof FieldAccess || expression instanceof MethodInvocation){
                FieldAccess fieldAccess = ast.newFieldAccess();
                fieldAccess.setExpression(expression);
                fieldAccess.setName(simpleName);
                return fieldAccess;
            }
            else{
                QualifiedName qualifiedName = ast.newQualifiedName((org.eclipse.jdt.core.dom.Name) expression, simpleName);
                qualifiedName.setSourceRange(pyexp.getCharStartIndex()+ PyMap.totalCharGains,pyexp.getCharStopIndex()-pyexp.getCharStartIndex());
                return qualifiedName;
            }
        }
        else if (pyexp instanceof Str){
            StringLiteral stringLiteral = ast.newStringLiteral();
            logger.debug(String.valueOf(((Str) pyexp).getInternalS()));
            stringLiteral.setLiteralValue(String.valueOf(((Str) pyexp).getInternalS()));
            return stringLiteral;
        }
        else if (pyexp instanceof BinOp){

            InfixExpression infixExpression = ast.newInfixExpression();
            Expression leftExpression =  mapExpression(((BinOp) pyexp).getInternalLeft(),ast,import_nodes,0,typeNodes,pyc  );
            Expression rightExpression = mapExpression(((BinOp) pyexp).getInternalRight(),ast,import_nodes,0,typeNodes,pyc  );
            infixExpression.setLeftOperand(leftExpression);
            infixExpression.setRightOperand(rightExpression);
            if (rightExpression instanceof PyErrorExpression || leftExpression instanceof PyErrorExpression){
                return ast.newPyErrorExpression();
            }

            if (((BinOp) pyexp).getInternalOp()== operatorType.Mult){
                infixExpression.setOperator(InfixExpression.Operator.TIMES);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.Add){
                infixExpression.setOperator(InfixExpression.Operator.PLUS);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.Sub) {
                infixExpression.setOperator(InfixExpression.Operator.MINUS);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.MatMult) {
                logger.error("Undefined MatMult operation found");
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.Div) {
                infixExpression.setOperator(InfixExpression.Operator.DIVIDE);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.Mod) {
                infixExpression.setOperator(InfixExpression.Operator.REMAINDER);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.Pow) {
                infixExpression.setOperator(InfixExpression.Operator.POW);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.LShift) {
                infixExpression.setOperator(InfixExpression.Operator.LEFT_SHIFT);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.RShift) {
                infixExpression.setOperator(InfixExpression.Operator.RIGHT_SHIFT_SIGNED);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.BitOr) {
                infixExpression.setOperator(InfixExpression.Operator.OR);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.BitXor) {
                infixExpression.setOperator(InfixExpression.Operator.XOR);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.BitAnd) {
                infixExpression.setOperator(InfixExpression.Operator.AND);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.FloorDiv) {
                infixExpression.setOperator(InfixExpression.Operator.FLOORDIV);
            }
            ParenthesizedExpression parenthesizedExpression = ast.newParenthesizedExpression();
            parenthesizedExpression.setExpression(infixExpression);
            return  parenthesizedExpression;
        }
        else if (pyexp instanceof IfExp){
            ConditionalExpression conditionalExpression = ast.newConditionalExpression();
            Expression thenExpression = mapExpression(((IfExp) pyexp).getInternalBody(), ast, import_nodes, 0, typeNodes, pyc);
            Expression elseExpression = mapExpression(((IfExp) pyexp).getInternalOrelse(), ast, import_nodes, 0, typeNodes, pyc);
            Expression expression = mapExpression(((IfExp) pyexp).getInternalTest(), ast, import_nodes, 0, typeNodes, pyc);
            if (!(thenExpression instanceof PyErrorExpression || elseExpression instanceof PyErrorExpression || expression instanceof PyErrorExpression)){
                conditionalExpression.setThenExpression(thenExpression);
                conditionalExpression.setElseExpression(elseExpression);
                conditionalExpression.setExpression(expression);

            }else {
                return ast.newPyErrorExpression();
            }
            return conditionalExpression;
        }
        else if (pyexp instanceof Compare){
            AstList comparators = (AstList) ((Compare) pyexp).getComparators();
            if (((AstList)((Compare) pyexp).getOps()).get(0).equals(cmpopType.In)){
                PyInExpression pyInExpression = ast.newPyInExpression();
                Expression leftExpression =  mapExpression(((Compare) pyexp).getInternalLeft(),ast,import_nodes,0,typeNodes,pyc  );
                Expression rightExpression = mapExpression((expr) (comparators).get(0), ast, import_nodes, 0, typeNodes, pyc);
                if (leftExpression instanceof PyErrorExpression || rightExpression instanceof PyErrorExpression){
                    return ast.newPyErrorExpression();
                }
                pyInExpression.setLeftOperand(leftExpression);
                pyInExpression.setRightOperand(rightExpression);
                return pyInExpression;
            }
            else if (((AstList)((Compare) pyexp).getOps()).get(0).equals(cmpopType.NotIn)){
                PyNotInExpression pyInExpression = ast.newPyNotInExpression();
                Expression leftExpression = mapExpression(((Compare) pyexp).getInternalLeft(),ast,import_nodes,0,typeNodes,pyc  );
                Expression rightExpression = mapExpression((expr) (comparators).get(0),ast,import_nodes,0,typeNodes,pyc  );
                if (leftExpression instanceof PyErrorExpression || rightExpression instanceof PyErrorExpression){
                    return ast.newPyErrorExpression();
                }
                pyInExpression.setLeftOperand(leftExpression);
                pyInExpression.setRightOperand(rightExpression);
                return pyInExpression;
            }
            InfixExpression infixExpression = ast.newInfixExpression();
            Expression leftExpression = mapExpression(((Compare) pyexp).getInternalLeft(),ast,import_nodes,0,typeNodes,pyc  );
            if (leftExpression instanceof PyErrorExpression){
                return ast.newPyErrorExpression();
            }
            infixExpression.setLeftOperand(leftExpression);

            InfixExpression expression0 = null;
            if ((comparators).size()>1){
                for (int y = 0; y < comparators.size(); y++ ){
                    if (y==0){
                        expression0 = ast.newInfixExpression();
                        Expression leftExpression1 =mapExpression((expr) comparators.get(0),ast,import_nodes,0,typeNodes,pyc);
                        Expression rightExpression1 =mapExpression((expr) comparators.get(1),ast,import_nodes,0,typeNodes,pyc);
                        if (leftExpression1 instanceof PyErrorExpression || rightExpression1 instanceof PyErrorExpression){
                            return ast.newPyErrorExpression();
                        }
                        expression0.setLeftOperand(leftExpression1);
                        expression0.setRightOperand(rightExpression1);
                        expression0.setOperator(setOperator(((Compare) pyexp).getInternalOps().get(y)));
                    }
                    else if (y>1){
                        InfixExpression expression = ast.newInfixExpression();
                        Expression leftExpression1= mapExpression((expr) (comparators).get(y),ast,import_nodes,0,typeNodes,pyc  );
                        if (leftExpression1 instanceof PyErrorExpression){
                            return ast.newPyErrorExpression();
                        }
                        expression.setLeftOperand(leftExpression1);
                        expression.setOperator(setOperator(((Compare) pyexp).getInternalOps().get(y)));
                        expression.setRightOperand(expression0);
                        expression0=expression;
                    }
                }
            }

            infixExpression.setOperator(setOperator(((Compare) pyexp).getInternalOps().get(0)));
            if (comparators.size()>1){
                infixExpression.setRightOperand(expression0);
            }
            else{
                Expression rightExpression =  mapExpression((expr) (comparators).get(0),ast,import_nodes,0,typeNodes,pyc  );
                if (rightExpression instanceof PyErrorExpression){
                    return ast.newPyErrorExpression();
                }
                infixExpression.setRightOperand(rightExpression);
            }
            ParenthesizedExpression parenthesizedExpression = ast.newParenthesizedExpression();
            parenthesizedExpression.setExpression(infixExpression);

            return parenthesizedExpression;
        }
        else if (pyexp instanceof BoolOp){
            ParenthesizedExpression paraexpression = ast.newParenthesizedExpression();
            InfixExpression infixExpression = ast.newInfixExpression();
            int values = ((AstList)((BoolOp) pyexp).getValues()).size();
            Expression rightExpression = mapExpression ((expr) ((AstList)((BoolOp) pyexp).getValues()).get(values-1),ast,import_nodes,0,typeNodes,pyc  );
            if (rightExpression instanceof PyErrorExpression){
                ast.newPyErrorExpression();
            }
            infixExpression.setRightOperand(rightExpression);
            InfixExpression temp = infixExpression;
            if (values==2){
                Expression right1Expression = mapExpression ((expr) ((AstList)((BoolOp) pyexp).getValues()).get(0),ast,import_nodes,0,typeNodes,pyc  );
                if (right1Expression instanceof PyErrorExpression){
                    return ast.newPyErrorExpression();
                }
                infixExpression.setLeftOperand(right1Expression);
            }
            else{
                for (int i = values-2; i >= 0; i--) {
                    if (i==0){
                        Expression leftTempExpression = mapExpression ((expr) ((AstList)((BoolOp) pyexp).getValues()).get(i),ast,import_nodes,0,typeNodes,pyc  );
                        if (leftTempExpression instanceof PyErrorExpression){
                            return ast.newPyErrorExpression();
                        }
                        temp.setLeftOperand(leftTempExpression);
                    }
                    else{
                        InfixExpression leftinfixExpression = ast.newInfixExpression();
                        Expression rightOperand = mapExpression ((expr) ((AstList)((BoolOp) pyexp).getValues()).get(i),ast,import_nodes,0,typeNodes,pyc  );
                        if (rightOperand instanceof PyErrorExpression){
                            return ast.newPyErrorExpression();
                        }
                        leftinfixExpression.setRightOperand(rightOperand);
                        if (((BoolOp) pyexp).getInternalOp().equals(boolopType.And)){
                            leftinfixExpression.setOperator(InfixExpression.Operator.CONDITIONAL_AND);
                        }
                        else if (((BoolOp) pyexp).getInternalOp().equals(boolopType.Or)){
                            leftinfixExpression.setOperator(InfixExpression.Operator.CONDITIONAL_OR);
                        }
                        else {
                            assert false;
                            logger.fatal("Unmapped operator found "+ ((BoolOp) pyexp).getOp());
                        }

                        temp.setLeftOperand(leftinfixExpression);

                        temp=leftinfixExpression;
                    }

                }
            }

            if (((BoolOp) pyexp).getInternalOp().equals(boolopType.And)){
                infixExpression.setOperator(InfixExpression.Operator.CONDITIONAL_AND);
            }
            else if (((BoolOp) pyexp).getInternalOp().equals(boolopType.Or)){
                infixExpression.setOperator(InfixExpression.Operator.CONDITIONAL_OR);
            }
            else {
                logger.fatal("Unmapped operator found "+ ((BoolOp) pyexp).getOp());
            }
            paraexpression.setExpression(infixExpression);
            return paraexpression;
        }
        else if (pyexp instanceof UnaryOp){
            PrefixExpression prefixExpression = ast.newPrefixExpression();
            Expression operand = mapExpression(((UnaryOp) pyexp).getInternalOperand(),ast,import_nodes,0,typeNodes,pyc  );
            if (operand instanceof PyErrorExpression){
                return ast.newPyErrorExpression();
            }
            prefixExpression.setOperand(operand);
            if (((UnaryOp) pyexp).getInternalOp().equals(unaryopType.Not)){
                prefixExpression.setOperator(PrefixExpression.Operator.NOT);
            }
            else if (((UnaryOp) pyexp).getInternalOp().equals(unaryopType.USub)){
                prefixExpression.setOperator(PrefixExpression.Operator.MINUS);
            }
            else if (((UnaryOp) pyexp).getInternalOp().equals(unaryopType.UAdd)){
                prefixExpression.setOperator(PrefixExpression.Operator.PLUS);
            }
            else{
                logger.fatal("Unhandle Unary operator");
            }
            return prefixExpression;
        }
        else if (pyexp instanceof Starred){
            Expression starsExpression = mapExpression((expr) ((Starred) pyexp).getValue(),ast,import_nodes,0,typeNodes,pyc  );
            if (starsExpression instanceof PyErrorExpression){
                return ast.newPyErrorExpression();
            }
            return starsExpression; //TODO handle stars, for now we neglect the stars
        }
        else if (pyexp instanceof Tuple){
            PyTupleExpression pyTupleExpression = ast.newPyTupleExpression();
            for (expr elt : ((Tuple) pyexp).getInternalElts()) {
                Expression  expression = mapExpression(elt,ast,import_nodes,0,typeNodes,pyc  );
                if (expression instanceof PyErrorExpression){
                    return ast.newPyErrorExpression();
                }
                pyTupleExpression.expressions().add(expression);
            }
            return pyTupleExpression;

        }
        else if (pyexp instanceof ListComp){
            PyListComprehension pyListComp = ast.newPyListComprehension();
            Expression target = mapExpression((expr) ((ListComp) pyexp).getElt(),ast,import_nodes,0,typeNodes,pyc  );
            if (target instanceof PyErrorExpression){return ast.newPyErrorExpression();}
            pyListComp.setTargetExpression(target);
            if (((AstList)((ListComp) pyexp).getGenerators()).size()==1 && ((AstList)((ListComp) pyexp).getGenerators()).get(0) instanceof comprehension){
                comprehension comp = (comprehension)((AstList)((ListComp) pyexp).getGenerators()).get(0);
                //      Expression valueexpression = mapExpression((expr) comp.getTarget(), ast, import_nodes, 0, typeNodes);

                if (comp.getTarget() instanceof Tuple){
                    SingleVariableDeclaration parameter_dummy = ast.newSingleVariableDeclaration();
                    parameter_dummy.setName(ast.newSimpleName( "DummyTerminalNode"));
                    parameter_dummy.setType(ast.newSimpleType(ast.newName("DummyTerminalTypeNode")));
                    pyListComp.getValueExpression().add(parameter_dummy);
                    for (Object elt : (AstList) ((Tuple) comp.getTarget()).getElts()) {
                        SingleVariableDeclaration lo_parameter = ast.newSingleVariableDeclaration();
                        lo_parameter.setName(ast.newSimpleName(((Name)elt).getId().toString()));
                        String typeString = typeNodes.get(new TypeASTNode(((Name)elt).getLineno(),
                                ((Name)elt).getCol_offset(), ((Name)elt).getId().toString(), null));
                        Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);
                        if (jdtType!=null){
                            lo_parameter.setType(jdtType);
                        }
                        else
                            logger.error("Type of for loop variable in generator is not updated");
                        pyListComp.getValueExpression().add(lo_parameter);
                    }

                }
                else if (comp.getTarget() instanceof Name){
                    SingleVariableDeclaration parameter_dummy = ast.newSingleVariableDeclaration();
                    parameter_dummy.setName(ast.newSimpleName( "DummyTerminalNode"));
                    parameter_dummy.setType(ast.newSimpleType(ast.newName("DummyTerminalTypeNode")));
                    pyListComp.getValueExpression().add(parameter_dummy);
                    SingleVariableDeclaration parameter = ast.newSingleVariableDeclaration();
                    parameter.setName(ast.newSimpleName(((Name)(comp.getTarget())).getId().toString()));
                    String typeString = typeNodes.get(new TypeASTNode(((Name)(comp.getTarget())).getLineno(),
                            ((Name)comp.getTarget()).getCol_offset(), ((Name)(comp.getTarget())).getId().toString(), null));

                    Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);
                    if (jdtType!=null){
                        parameter.setType(jdtType);
                    }
                    else
                        logger.error("Type of for loop variable is not updated");
                    pyListComp.getValueExpression().add(parameter);

                }
                else{
                    logger.error("The mapping for the corresponding for loop parameter is not found");
                }
                Expression iterator = mapExpression(comp.getInternalIter(),ast,import_nodes,0,typeNodes,pyc  );
                if (iterator instanceof PyErrorExpression) {return ast.newPyErrorExpression();}
                pyListComp.setIteratorExpression(iterator);
                if (((AstList)comp.getIfs()).size()==0){
                    return pyListComp;
                }
                else if (((AstList)comp.getIfs()).size()==1){
                    Expression conditionalExpression = mapExpression((expr) ((AstList)comp.getIfs()).get(0),ast,import_nodes,0,typeNodes,pyc  );
                    if (conditionalExpression instanceof PyErrorExpression){return ast.newPyErrorExpression();}
                    pyListComp.setConditionalExpression(conditionalExpression);
                    return pyListComp;
                }
                else {
                    logger.debug("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
                    throw new ExpressionNotFound("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
                }
            }
            else{
                logger.debug("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
                throw new ExpressionNotFound("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
            }

        }
        else if(pyexp instanceof GeneratorExp) {
            PyGenerator pyGenerator = ast.newPyGenerator();
            Expression targetExpression = mapExpression((expr) ((GeneratorExp) pyexp).getElt(),ast,import_nodes,0,typeNodes,pyc  );
            if (targetExpression instanceof PyErrorExpression){return ast.newPyErrorExpression();}
            pyGenerator.setTargetExpression(targetExpression);
            if (((AstList)((GeneratorExp) pyexp).getGenerators()).size()==1 && ((AstList)((GeneratorExp) pyexp).getGenerators()).get(0) instanceof comprehension){
                comprehension comp = (comprehension)((AstList)((GeneratorExp) pyexp).getGenerators()).get(0);
          //      Expression valueexpression = mapExpression((expr) comp.getTarget(), ast, import_nodes, 0, typeNodes);

                if (comp.getTarget() instanceof Tuple){
                    SingleVariableDeclaration parameter_dummy = ast.newSingleVariableDeclaration();
                    parameter_dummy.setName(ast.newSimpleName( "DummyTerminalNode"));
                    parameter_dummy.setType(ast.newSimpleType(ast.newName("DummyTerminalTypeNode")));
                    pyGenerator.getValueExpression().add(parameter_dummy);
                    for (Object elt : (AstList) ((Tuple) comp.getTarget()).getElts()) {
                        SingleVariableDeclaration lo_parameter = ast.newSingleVariableDeclaration();
                        lo_parameter.setName(ast.newSimpleName(((Name)elt).getId().toString()));
                        String typeString = typeNodes.get(new TypeASTNode(((Name)elt).getLineno(),
                                ((Name)elt).getCol_offset(), ((Name)elt).getId().toString(), null));
                        Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);
                        if (jdtType!=null){
                            lo_parameter.setType(jdtType);
                        }
                        else
                            logger.error("Type of for loop variable in generator is not updated");
                        pyGenerator.getValueExpression().add(lo_parameter);
                    }

                }
                else if (comp.getTarget() instanceof Name){
                    SingleVariableDeclaration parameter_dummy = ast.newSingleVariableDeclaration();
                    parameter_dummy.setName(ast.newSimpleName( "DummyTerminalNode"));
                    parameter_dummy.setType(ast.newSimpleType(ast.newName("DummyTerminalTypeNode")));
                    pyGenerator.getValueExpression().add(parameter_dummy);
                    SingleVariableDeclaration parameter = ast.newSingleVariableDeclaration();
                    parameter.setName(ast.newSimpleName(((Name)(comp.getTarget())).getId().toString()));
                    String typeString = typeNodes.get(new TypeASTNode(((Name)(comp.getTarget())).getLineno(),
                            ((Name)comp.getTarget()).getCol_offset(), ((Name)(comp.getTarget())).getId().toString(), null));

                    Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);
                    if (jdtType!=null){
                        parameter.setType(jdtType);
                    }
                    else
                        logger.error("Type of for loop variable is not updated");
                    pyGenerator.getValueExpression().add(parameter);

                }
                else{
                    logger.error("The mapping for the corresponding for loop parameter is not found");
                }
                Expression iteratorExpression = mapExpression(comp.getInternalIter(),ast,import_nodes,0,typeNodes,pyc  );
                if (iteratorExpression instanceof PyErrorExpression){return  ast.newPyErrorExpression();}
                pyGenerator.setIteratorExpression(iteratorExpression);
                if (((AstList)comp.getIfs()).size()==0){
                    return pyGenerator;
                }
                else if (((AstList)comp.getIfs()).size()==1){
                    Expression iteratorExpression1 = mapExpression((expr) ((AstList)comp.getIfs()).get(0),ast,import_nodes,0,typeNodes,pyc  );
                    if (iteratorExpression1 instanceof PyErrorExpression){ast.newPyErrorExpression();}
                    pyGenerator.setConditionalExpression(iteratorExpression1);
                    return pyGenerator;
                }
                else {
                    logger.debug("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
                    throw new ExpressionNotFound("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
                }
            }
            else{
                logger.debug("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
                throw new ExpressionNotFound("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
            }


        }
        else if (pyexp instanceof Dict){
            AstList keys = (AstList) (((Dict) pyexp).getKeys());
            AstList values = (AstList) (((Dict) pyexp).getValues());
            MethodInvocation methodInvocation = ast.newMethodInvocation();
            methodInvocation.setExpression(ast.newSimpleName("Map"));
            methodInvocation.setName(ast.newSimpleName("of"));
            methodInvocation.setPyObject(pyexp);
            for (int i=0;i<keys.size();i++){
                Expression keyExpression = mapExpression((expr) keys.get(i), ast, import_nodes, 0, typeNodes,pyc );
                Expression valueExpression = mapExpression((expr) values.get(i), ast, import_nodes, 0, typeNodes,pyc);
                if (keyExpression instanceof PyErrorExpression && valueExpression instanceof PyErrorExpression){return ast.newPyErrorExpression();}

                if (keyExpression instanceof org.eclipse.jdt.core.dom.Name && isClassImport(pyc,(org.eclipse.jdt.core.dom.Name)keyExpression)){
                    ClassInstanceCreation keyclassInstanceCreation = ast.newClassInstanceCreation();
                    keyclassInstanceCreation.setType(ast.newSimpleType((org.eclipse.jdt.core.dom.Name) keyExpression));
                    methodInvocation.arguments().add(keyclassInstanceCreation);
                }
                else{
                    methodInvocation.arguments().add(keyExpression);
                }
                if (valueExpression instanceof org.eclipse.jdt.core.dom.Name && isClassImport(pyc,(org.eclipse.jdt.core.dom.Name)valueExpression)){
                    ClassInstanceCreation valueclassInstanceCreation = ast.newClassInstanceCreation();
                    valueclassInstanceCreation.setType(ast.newSimpleType((org.eclipse.jdt.core.dom.Name) valueExpression));
                    methodInvocation.arguments().add(valueclassInstanceCreation);
                }
                else
                    {
                    methodInvocation.arguments().add(valueExpression);
                }
            }
            return methodInvocation;
        }
        else if (pyexp instanceof DictComp){

            PyDictComprehension pyListComp = ast.newPyDictComprehension();
            Expression target1 = mapExpression((expr) ((DictComp) pyexp).getKey(),ast,import_nodes,0,typeNodes,pyc  );
            Expression target2 = mapExpression((expr) ((DictComp) pyexp).getValue(),ast,import_nodes,0,typeNodes,pyc  );
            if (target1 instanceof PyErrorExpression || target2 instanceof PyErrorExpression){return ast.newPyErrorExpression();}
            pyListComp.setTarget1Expression(target1);
            pyListComp.setTarget2Expression(target2);
            if (((AstList)((DictComp) pyexp).getGenerators()).size()==1 && ((AstList)((DictComp) pyexp).getGenerators()).get(0) instanceof comprehension){
                comprehension comp = (comprehension)((AstList)((DictComp) pyexp).getGenerators()).get(0);
                //      Expression valueexpression = mapExpression((expr) comp.getTarget(), ast, import_nodes, 0, typeNodes);

                if (comp.getTarget() instanceof Tuple){
                    SingleVariableDeclaration parameter_dummy = ast.newSingleVariableDeclaration();
                    parameter_dummy.setName(ast.newSimpleName( "DummyTerminalNode"));
                    parameter_dummy.setType(ast.newSimpleType(ast.newName("DummyTerminalTypeNode")));
                    pyListComp.getValueExpression().add(parameter_dummy);
                    for (Object elt : (AstList) ((Tuple) comp.getTarget()).getElts()) {
                        SingleVariableDeclaration lo_parameter = ast.newSingleVariableDeclaration();
                        lo_parameter.setName(ast.newSimpleName(((Name)elt).getId().toString()));
                        String typeString = typeNodes.get(new TypeASTNode(((Name)elt).getLineno(),
                                ((Name)elt).getCol_offset(), ((Name)elt).getId().toString(), null));
                        Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);
                        if (jdtType!=null){
                            lo_parameter.setType(jdtType);
                        }
                        else
                            logger.error("Type of for loop variable in generator is not updated");
                        pyListComp.getValueExpression().add(lo_parameter);
                    }

                }
                else if (comp.getTarget() instanceof Name){
                    SingleVariableDeclaration parameter_dummy = ast.newSingleVariableDeclaration();
                    parameter_dummy.setName(ast.newSimpleName( "DummyTerminalNode"));
                    parameter_dummy.setType(ast.newSimpleType(ast.newName("DummyTerminalTypeNode")));
                    pyListComp.getValueExpression().add(parameter_dummy);
                    SingleVariableDeclaration parameter = ast.newSingleVariableDeclaration();
                    parameter.setName(ast.newSimpleName(((Name)(comp.getTarget())).getId().toString()));
                    String typeString = typeNodes.get(new TypeASTNode(((Name)(comp.getTarget())).getLineno(),
                            ((Name)comp.getTarget()).getCol_offset(), ((Name)(comp.getTarget())).getId().toString(), null));

                    Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);
                    if (jdtType!=null){
                        parameter.setType(jdtType);
                    }
                    else
                        logger.error("Type of for loop variable is not updated");
                    pyListComp.getValueExpression().add(parameter);

                }
                else{
                    logger.error("The mapping for the corresponding for loop parameter is not found");
                }
                Expression iteratorExpression = mapExpression(comp.getInternalIter(),ast,import_nodes,0,typeNodes,pyc  );
                if (iteratorExpression instanceof PyErrorExpression){return ast.newPyErrorExpression();}
                pyListComp.setIteratorExpression(iteratorExpression);
                if (((AstList)comp.getIfs()).size()==0){
                    return pyListComp;
                }
                else if (((AstList)comp.getIfs()).size()==1){
                    Expression conditionalExpression = mapExpression((expr) ((AstList)comp.getIfs()).get(0),ast,import_nodes,0,typeNodes,pyc  );
                    if (conditionalExpression instanceof PyErrorExpression){return ast.newPyErrorExpression();}
                    pyListComp.setConditionalExpression(conditionalExpression);
                    return pyListComp;
                }
                else {
                    logger.debug("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
                    throw new ExpressionNotFound("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
                }
            }
            else{
                assert false;
                logger.debug("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
                throw new ExpressionNotFound("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
            }
        }

        else if (pyexp instanceof ErrorExpr){
            return ast.newPyErrorExpression();
        }

        else if (pyexp instanceof SetComp){
            PySetComprehension setComprehension = ast.newPySetComprehension();
            Expression target = mapExpression((expr) ((SetComp) pyexp).getElt(),ast,import_nodes,0,typeNodes,pyc  );
            if (target instanceof PyErrorExpression){return ast.newPyErrorExpression();}
            setComprehension.setTargetExpression(target);
            if (((AstList)((SetComp) pyexp).getGenerators()).size()==1 && ((AstList)((SetComp) pyexp).getGenerators()).get(0) instanceof comprehension){
                comprehension comp = (comprehension)((AstList)((SetComp) pyexp).getGenerators()).get(0);
                //      Expression valueexpression = mapExpression((expr) comp.getTarget(), ast, import_nodes, 0, typeNodes);

                if (comp.getTarget() instanceof Tuple){
                    SingleVariableDeclaration parameter_dummy = ast.newSingleVariableDeclaration();
                    parameter_dummy.setName(ast.newSimpleName( "DummyTerminalNode"));
                    parameter_dummy.setType(ast.newSimpleType(ast.newName("DummyTerminalTypeNode")));
                    setComprehension.getValueExpression().add(parameter_dummy);
                    for (Object elt : (AstList) ((Tuple) comp.getTarget()).getElts()) {
                        SingleVariableDeclaration lo_parameter = ast.newSingleVariableDeclaration();
                        lo_parameter.setName(ast.newSimpleName(((Name)elt).getId().toString()));
                        String typeString = typeNodes.get(new TypeASTNode(((Name)elt).getLineno(),
                                ((Name)elt).getCol_offset(), ((Name)elt).getId().toString(), null));
                        Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);
                        if (jdtType!=null){
                            lo_parameter.setType(jdtType);
                        }
                        else
                            logger.error("Type of for loop variable in generator is not updated");
                        setComprehension.getValueExpression().add(lo_parameter);
                    }

                }
                else if (comp.getTarget() instanceof Name){
                    SingleVariableDeclaration parameter_dummy = ast.newSingleVariableDeclaration();
                    parameter_dummy.setName(ast.newSimpleName( "DummyTerminalNode"));
                    parameter_dummy.setType(ast.newSimpleType(ast.newName("DummyTerminalTypeNode")));
                    setComprehension.getValueExpression().add(parameter_dummy);
                    SingleVariableDeclaration parameter = ast.newSingleVariableDeclaration();
                    parameter.setName(ast.newSimpleName(((Name)(comp.getTarget())).getId().toString()));
                    String typeString = typeNodes.get(new TypeASTNode(((Name)(comp.getTarget())).getLineno(),
                            ((Name)comp.getTarget()).getCol_offset(), ((Name)(comp.getTarget())).getId().toString(), null));

                    Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);
                    if (jdtType!=null){
                        parameter.setType(jdtType);
                    }
                    else
                        logger.error("Type of for loop variable is not updated");
                    setComprehension.getValueExpression().add(parameter);

                }
                else{
                    logger.error("The mapping for the corresponding for loop parameter is not found");
                }
                Expression iterator = mapExpression(comp.getInternalIter(),ast,import_nodes,0,typeNodes,pyc  );
                if (iterator instanceof PyErrorExpression) {return ast.newPyErrorExpression();}
                setComprehension.setIteratorExpression(iterator);
                if (((AstList)comp.getIfs()).size()==0){
                    return setComprehension;
                }
                else if (((AstList)comp.getIfs()).size()==1){
                    Expression conditionalExpression = mapExpression((expr) ((AstList)comp.getIfs()).get(0),ast,import_nodes,0,typeNodes,pyc  );
                    if (conditionalExpression instanceof PyErrorExpression){return ast.newPyErrorExpression();}
                    setComprehension.setConditionalExpression(conditionalExpression);
                    return setComprehension;
                }
                else {
                    logger.debug("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
                    throw new ExpressionNotFound("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
                }
            }
            else{
                logger.debug("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
                throw new ExpressionNotFound("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
            }



        }
        else if (pyexp instanceof Set){
            MethodInvocation methodInvocation = ast.newMethodInvocation();
            methodInvocation.setName(ast.newSimpleName("set"));
            ((Set) pyexp).getInternalElts().stream().forEach(x->
            {
                try {
                    methodInvocation.arguments().add(mapExpression(x,ast,import_nodes,0,typeNodes,pyc));
                } catch (ExpressionNotFound expressionNotFound) {
                    expressionNotFound.printStackTrace();
                } catch (NodeNotFoundException e) {
                    e.printStackTrace();
                }
            });
            return methodInvocation;
        }


        else {
            logger.fatal("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
            assert false;
            throw new ExpressionNotFound("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
        }
    }

    private static InfixExpression.Operator setOperator(cmpopType pyexp) {
        if ( pyexp.equals(cmpopType.Eq)){
           return InfixExpression.Operator.EQUALS;
        }
        else if ( pyexp.equals(cmpopType.IsNot)){
            return InfixExpression.Operator.NOT_EQUALS;
        }
        else if (pyexp.equals(cmpopType.Is)){
            return InfixExpression.Operator.EQUALS;
        }
        else if (pyexp.equals(cmpopType.NotEq)){
            return InfixExpression.Operator.NOT_EQUALS;
        }
        else if (pyexp.equals(cmpopType.Lt)){
            return InfixExpression.Operator.LESS;
        }
        else if (pyexp.equals(cmpopType.LtE)){
            return InfixExpression.Operator.LESS_EQUALS;
        }
        else if (pyexp.equals(cmpopType.Gt)){
            return InfixExpression.Operator.GREATER;
        }
        else if (pyexp.equals(cmpopType.GtE)){
            return InfixExpression.Operator.GREATER_EQUALS;
        }
        else{
            logger.fatal("Unmapped operator found "+pyexp);
        }
        return null;
    }

    public static String mapPythonKeyWords(String keyword){
        if (keyword.equals("default")){
            return "def";
        }
        else if (keyword.equals("int")){
            return "Integer";
        }
        else if (keyword.equals("float")){
            return "Float";
        }
        else if (keyword.equals("str")){
            return "String";
        }
        else if (keyword.equals("super")){
            return "raise";
        }

        return keyword;


    }


    private static void updateArrayTpe(AST ast, ArrayCreation arrayCreation, String typeString) throws NodeNotFoundException {
        if (typeString !=null) {
            Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);
            if (jdtType!=null) {
                arrayCreation.setType((ArrayType) jdtType);
            }
            else
            {
                Type jdtType1 = TypeStringToJDT.getJDTType(ast, "List[Any]", 0);
                arrayCreation.setType((ArrayType) jdtType1);
                logger.fatal("Created any type");
            }
        }
        else{
            Type jdtType1 = TypeStringToJDT.getJDTType(ast, "List[Any]", 0);
            arrayCreation.setType((ArrayType) jdtType1);
            logger.fatal("Created any type");
        }
    }

    public static boolean isClassImport(PyCompilationUnit cmp, org.eclipse.jdt.core.dom.Name name){
        ArrayList<org.eclipse.jdt.core.dom.Name> importnames = (ArrayList<org.eclipse.jdt.core.dom.Name>) cmp.imports().stream().map(x->((ImportDeclaration)x).getName()).collect(Collectors.toList());
        for (org.eclipse.jdt.core.dom.Name importname : importnames) {
            if (importname.isSimpleName() && name.isSimpleName() ) {
                if (importname.getFullyQualifiedName().equals(name.getFullyQualifiedName())) return true;
            }
            else if (importname.isQualifiedName() && name.isSimpleName()){
                if (((QualifiedName)importname).getName().getIdentifier().equals(name.getFullyQualifiedName())) return true;
            }
            else if (importname.isQualifiedName() && name.isQualifiedName()){
                if (importname.getFullyQualifiedName().equals(name.getFullyQualifiedName())) return true;
                String[] split_import = importname.getFullyQualifiedName().split("\\.");
                String[] split_name = name.getFullyQualifiedName().split("\\.");

                for (int i=0;i<split_import.length;i++){
                    java.util.List<String> collect = Arrays.stream(split_import).skip(i).collect(Collectors.toList());
                    if ((split_import.length - i)<=split_name.length){
                        java.util.List<String> collect1 = Arrays.stream(split_name).limit(split_import.length - i).collect(Collectors.toList());
                        if (collect1.equals(collect)){
                            return true;
                        }
                    }

                }
            }
            else if (importname.isSimpleName() && name.isQualifiedName()){
                String[] split_name = name.getFullyQualifiedName().split("\\.");
                if (split_name[0].equals(importname.getFullyQualifiedName())) return true;
            }

        }
        return false;
    }


}
