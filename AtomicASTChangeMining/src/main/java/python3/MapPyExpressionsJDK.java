package python3;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PyGenerator;
import org.eclipse.jdt.core.dom.PyInExpression;
import org.eclipse.jdt.core.dom.PyTupleExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.jpp.PyASTParser;
import org.jpp.astnodes.ast.Assign;
import org.jpp.astnodes.ast.BoolOp;
import org.jpp.astnodes.ast.Compare;
import org.jpp.astnodes.ast.Delete;
import org.jpp.astnodes.ast.For;
import org.jpp.astnodes.ast.GeneratorExp;
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
import python3.typeinference.core.TypeStringToJDT;

import java.util.HashMap;
import java.util.Map;

public class MapPyExpressionsJDK extends PyMap {
    static Logger logger = Logger.getLogger(MapPyExpressionsJDK.class);
    public static Expression mapExpression(expr pyexp, AST ast, HashMap<String, org.eclipse.jdt.core.dom.Name> import_nodes , int start_char_pos, Map<TypeASTNode, String> typeNodes) throws ExpressionNotFound, NodeNotFoundException {
        if (pyexp instanceof Num){
            if (((Num) pyexp).getN() instanceof PyLong){
                NumberLiteral numberLiteral = ast.newNumberLiteral(String.valueOf(((PyLong) ((Num) pyexp).getN()).getValue()));
                numberLiteral.setSourceRange(start_char_pos,numberLiteral.toString().length());
                return numberLiteral;
            }
            else if (((Num) pyexp).getN() instanceof PyFloat ){
                NumberLiteral numberLiteral = ast.newNumberLiteral(String.valueOf(((PyFloat) ((Num) pyexp).getN()).getValue()));
                numberLiteral.setSourceRange(start_char_pos,numberLiteral.toString().length());
                return numberLiteral;
            }
            else {
                throw new ExpressionNotFound("Corresponding Expression is not Found "+pyexp.getClass());
            }

        }
        else if (pyexp instanceof Name){
            String id = ((Name) pyexp).getId().toString();
            if (id.equals("int")){
                id="integer";
            }
            SimpleName simpleName = ast.newSimpleName(id);
            simpleName.setSourceRange(pyexp.getCharStartIndex()+PyMap.totalCharGains,((Name) pyexp).getId().toString().length());
            return simpleName;
        }
        else if (pyexp instanceof Call){
            MethodInvocation invocation = ast.newMethodInvocation();
            if (((Call) pyexp).getFunc() instanceof Name){
//                if (((Name) ((Call) pyexp).getFunc()).getId().toString().toString().equals("isinstance")){
//                    InstanceofExpression instanceofExpression = ast.newInstanceofExpression();
//                    instanceofExpression.setLeftOperand(MapPyExpressionsJDK.mapExpression((expr) ((AstList)((Call) pyexp).getArgs()).get(0),ast, import_nodes,0,typeNodes));
//                    instanceofExpression.setRightOperand(MapPyExpressionsJDK.mapExpression((expr) ((AstList)((Call) pyexp).getArgs()).get(1),ast, import_nodes,0,typeNodes));
//                    return instanceofExpression;
//                }
                SimpleName method_name = (SimpleName)mapExpression((expr) ((Call) pyexp).getFunc(), ast, import_nodes,start_char_pos,typeNodes);
                invocation.setName(method_name);
                for (Object arg : (AstList) ((Call) pyexp).getArgs()) {  //TODO keyword arguments are not parsed by the JPyParser, Thus is a Bug
                    invocation.arguments().add(mapExpression((expr) arg,ast, import_nodes,start_char_pos+1+method_name.toString().length(),typeNodes));
                }
                for (Object karg: (AstList) ((Call) pyexp).getKeywords()){
                    invocation.arguments().add(mapExpression((expr) ((keyword) karg).getValue(),ast, import_nodes,start_char_pos+1+method_name.toString().length(),typeNodes));
                    logger.debug("Key ward is neglected : "+((keyword) karg).getArg());
                }

                invocation.setSourceRange(start_char_pos,invocation.toString().length());
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
                        expre = ast.newName(name.getFullyQualifiedName().split("."));

                    }
                    else{
                        SimpleName simpleName = ast.newSimpleName(name.getFullyQualifiedName());
                        simpleName.setSourceRange(((Attribute) ((Call) pyexp).getFunc()).getCharStartIndex(),name.getFullyQualifiedName().length());
                        expre = simpleName;
                    }
                }
                else
                {
                    expre = mapExpression( (expr) ((Attribute)((Call) pyexp).getFunc()).getValue(),ast, import_nodes,0,typeNodes);
                }

                method_name.setSourceRange(pyexp.getCharStartIndex()+expre.toString().length()+PyMap.totalCharGains,method_name.toString().length());

                invocation.setName(method_name);
                invocation.setExpression(expre);
                for (Object arg : (AstList) ((Call) pyexp).getArgs()) {
                    invocation.arguments().add(mapExpression((expr) arg,ast, import_nodes,0,typeNodes));
                }
                for (Object karg: (AstList) ((Call) pyexp).getKeywords()){
                    invocation.arguments().add(mapExpression((expr) ((keyword) karg).getValue(),ast, import_nodes,start_char_pos+1+method_name.toString().length(),typeNodes));
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
            arrayAccess.setArray(mapExpression((expr)((Subscript) pyexp).getValue(),ast, import_nodes,0,typeNodes));
            arrayAccess.setIndex(mapExpression((expr) ((Index)((Subscript) pyexp).getSlice()).getValue(),ast, import_nodes,0,typeNodes));
            arrayAccess.setSourceRange(pyexp.getCharStartIndex()+PyMap.totalCharGains,pyexp.getCharStopIndex()-pyexp.getCharStartIndex());
            return arrayAccess;
        }
        else if (pyexp instanceof List){
            ArrayInitializer arrayInitializer = ast.newArrayInitializer();
            for (Object elt : (AstList) ((List) pyexp).getElts()) {
                arrayInitializer.expressions().add(mapExpression((expr) elt,ast, import_nodes,0,typeNodes));
            }
            arrayInitializer.setSourceRange(pyexp.getCharStartIndex()+PyMap.totalCharGains,pyexp.getCharStopIndex()-pyexp.getCharStartIndex());

            //TODO fille the data
            if (!(pyexp.getParent() instanceof List)){
                ArrayCreation arrayCreation = ast.newArrayCreation();
                if (pyexp.getParent() instanceof Assign){
                    if (((AstList)((Assign)pyexp.getParent()).getTargets()).size()>1){
                        logger.fatal("have to handle more than one assignments");
                    }
                    if (((AstList)((Assign)pyexp.getParent()).getTargets()).get(0) instanceof Name){
                        String typeString = typeNodes.get(new TypeASTNode(((Name) ((AstList)((Assign)pyexp.getParent()).getTargets()).get(0)).getLineno(),
                                ((Name) ((AstList)((Assign)pyexp.getParent()).getTargets()).get(0)).getCol_offset(), ((Name) ((AstList)((Assign)pyexp.getParent()).getTargets()).get(0)).getInternalId(), null));
                        if (typeString!=null) {
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
                    else
                    {
                        Type jdtType1 = TypeStringToJDT.getJDTType(ast, "List[Any]", 0);
                        arrayCreation.setType((ArrayType) jdtType1);
                        logger.fatal("Created any type");
                        logger.fatal("Assigned target is not an instance of Name");
                    }

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
                    expression = mapExpression((expr) mod.getChild(0).getChild(0),ast,import_nodes,0,typeNodes);

                }
                else{
                    SimpleName simpleName = ast.newSimpleName(name.getFullyQualifiedName());
                    simpleName.setSourceRange(name.getStartPosition()+PyMap.totalCharGains,name.getLength());
                    expression = simpleName;
                }
            }
            else{
                expression = mapExpression((expr) ((Attribute) pyexp).getValue(), ast, import_nodes,0,typeNodes);
            }
            SimpleName simpleName = ast.newSimpleName(((Attribute) pyexp).getAttr().toString());
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
            infixExpression.setLeftOperand(mapExpression(((BinOp) pyexp).getInternalLeft(),ast,import_nodes,0,typeNodes));
            infixExpression.setRightOperand(mapExpression(((BinOp) pyexp).getInternalRight(),ast,import_nodes,0,typeNodes));
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
            return infixExpression;
        }
        else if (pyexp instanceof IfExp){
            ConditionalExpression conditionalExpression = ast.newConditionalExpression();

            conditionalExpression.setThenExpression(mapExpression(((IfExp) pyexp).getInternalBody(),ast,import_nodes,0,typeNodes));
            conditionalExpression.setElseExpression(mapExpression(((IfExp) pyexp).getInternalOrelse(),ast,import_nodes,0,typeNodes));
            conditionalExpression.setExpression(mapExpression(((IfExp) pyexp).getInternalTest(),ast,import_nodes,0,typeNodes));
            return conditionalExpression;
        }
        else if (pyexp instanceof Compare){
            if (((AstList)((Compare) pyexp).getOps()).get(0).equals(cmpopType.In)){
                PyInExpression pyInExpression = ast.newPyInExpression();
                pyInExpression.setLeftOperand(mapExpression(((Compare) pyexp).getInternalLeft(),ast,import_nodes,0,typeNodes));
                pyInExpression.setRightOperand(mapExpression((expr) ((AstList)((Compare) pyexp).getComparators()).get(0),ast,import_nodes,0,typeNodes));
                return pyInExpression;
            }
            InfixExpression infixExpression = ast.newInfixExpression();
            infixExpression.setLeftOperand(mapExpression(((Compare) pyexp).getInternalLeft(),ast,import_nodes,0,typeNodes));
            if (((AstList)((Compare) pyexp).getComparators()).size()>1){
                logger.fatal("Size of the Comparators is more than one");
            }
            if (((AstList)((Compare) pyexp).getOps()).size()>1){
                logger.fatal("Size of the operators is more than one");
            }
            if ( ((AstList)((Compare) pyexp).getOps()).get(0).equals(cmpopType.Eq)){
               infixExpression.setOperator(InfixExpression.Operator.EQUALS);
            }
            else if ( ((AstList)((Compare) pyexp).getOps()).get(0).equals(cmpopType.IsNot)){
                infixExpression.setOperator(InfixExpression.Operator.NOT_EQUALS);
            }
            else if (((AstList)((Compare) pyexp).getOps()).get(0).equals(cmpopType.Is)){
                infixExpression.setOperator(InfixExpression.Operator.EQUALS);
            }
            else if (((AstList)((Compare) pyexp).getOps()).get(0).equals(cmpopType.NotEq)){
                infixExpression.setOperator(InfixExpression.Operator.NOT_EQUALS);
            }
            else if (((AstList)((Compare) pyexp).getOps()).get(0).equals(cmpopType.Lt)){
                infixExpression.setOperator(InfixExpression.Operator.LESS);
            }
            else if (((AstList)((Compare) pyexp).getOps()).get(0).equals(cmpopType.LtE)){
                infixExpression.setOperator(InfixExpression.Operator.LESS_EQUALS);
            }
            else if (((AstList)((Compare) pyexp).getOps()).get(0).equals(cmpopType.Gt)){
                infixExpression.setOperator(InfixExpression.Operator.GREATER);
            }
            else if (((AstList)((Compare) pyexp).getOps()).get(0).equals(cmpopType.GtE)){
                infixExpression.setOperator(InfixExpression.Operator.GREATER_EQUALS);
            }
            else if (((AstList)((Compare) pyexp).getOps()).get(0).equals(cmpopType.GtE)){
                infixExpression.setOperator(InfixExpression.Operator.GREATER_EQUALS);
            }
            else{
                logger.fatal("Unmapped operator found "+((AstList)((Compare) pyexp).getOps()).get(0));
            }


            infixExpression.setRightOperand(mapExpression((expr) ((AstList)((Compare) pyexp).getComparators()).get(0),ast,import_nodes,0,typeNodes));
            ParenthesizedExpression parenthesizedExpression = ast.newParenthesizedExpression();
            parenthesizedExpression.setExpression(infixExpression);

            return parenthesizedExpression;
        }
        else if (pyexp instanceof BoolOp){
            InfixExpression infixExpression = ast.newInfixExpression();
            int values = ((AstList)((BoolOp) pyexp).getValues()).size();
            infixExpression.setRightOperand(mapExpression ((expr) ((AstList)((BoolOp) pyexp).getValues()).get(values-1),ast,import_nodes,0,typeNodes));
            InfixExpression temp = infixExpression;
            if (values==2){
                infixExpression.setLeftOperand(mapExpression ((expr) ((AstList)((BoolOp) pyexp).getValues()).get(0),ast,import_nodes,0,typeNodes));
            }
            else{
                for (int i = values-2; i >= 0; i--) {
                    if (i==0){
                        temp.setLeftOperand(mapExpression ((expr) ((AstList)((BoolOp) pyexp).getValues()).get(i),ast,import_nodes,0,typeNodes));
                    }
                    else{
                        InfixExpression leftinfixExpression = ast.newInfixExpression();
                        leftinfixExpression.setRightOperand(mapExpression ((expr) ((AstList)((BoolOp) pyexp).getValues()).get(i),ast,import_nodes,0,typeNodes));
                        if (((BoolOp) pyexp).getInternalOp().equals(boolopType.And)){
                            leftinfixExpression.setOperator(InfixExpression.Operator.CONDITIONAL_AND);
                        }
                        else if (((BoolOp) pyexp).getInternalOp().equals(boolopType.Or)){
                            leftinfixExpression.setOperator(InfixExpression.Operator.CONDITIONAL_OR);
                        }
                        else {
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

            return infixExpression;
        }
        else if (pyexp instanceof UnaryOp){
            PrefixExpression prefixExpression = ast.newPrefixExpression();
            prefixExpression.setOperand(mapExpression(((UnaryOp) pyexp).getInternalOperand(),ast,import_nodes,0,typeNodes));
            if (((UnaryOp) pyexp).getInternalOp().equals(unaryopType.Not)){
                prefixExpression.setOperator(PrefixExpression.Operator.NOT);
            }
            else{
                logger.fatal("Unhandle Unary operator");
            }
            return prefixExpression;
        }
        else if (pyexp instanceof Starred){
         return mapExpression((expr) ((Starred) pyexp).getValue(),ast,import_nodes,0,typeNodes); //TODO handle stars, for now we neglect the stars


        }
        else if (pyexp instanceof Tuple){
            PyTupleExpression pyTupleExpression = ast.newPyTupleExpression();
            for (expr elt : ((Tuple) pyexp).getInternalElts()) {
                pyTupleExpression.expressions().add(mapExpression(elt,ast,import_nodes,0,typeNodes));
            }
            return pyTupleExpression;

        }
        else if(pyexp instanceof GeneratorExp) {
            PyGenerator pyGenerator = ast.newPyGenerator();
            pyGenerator.setTargetExpression(mapExpression((expr) ((GeneratorExp) pyexp).getElt(),ast,import_nodes,0,typeNodes));
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
                pyGenerator.setIteratorExpression(mapExpression(comp.getInternalIter(),ast,import_nodes,0,typeNodes));
                if (((AstList)comp.getIfs()).size()==1){
                    pyGenerator.setConditionalExpression(mapExpression((expr) ((AstList)comp.getIfs()).get(0),ast,import_nodes,0,typeNodes));
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
        else {
            logger.debug("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
            throw new ExpressionNotFound("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
        }
    }


}
