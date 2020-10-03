package python3;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Expression;
import org.jpp.astnodes.PythonTree;
import org.jpp.astnodes.ast.*;
import org.jpp.astnodes.ast.Name;
import org.jpp.astnodes.base.expr;
import org.jpp.astnodes.base.stmt;
import org.jpp.heart.AstList;
import org.jpp.heart.PyNone;
import org.jpp.heart.PyObject;
import python3.pyerrors.ExpressionNotFound;
import python3.pyerrors.NodeNotFoundException;
import python3.pyvisitors.PyVisitor;
import python3.typeinference.core.TypeASTNode;
import python3.typeinference.core.TypeDecNeeds;
import python3.typeinference.core.TypeStringToJDT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MapPyStatementsTOJDK extends PyMap{
    static Logger logger = Logger.getLogger(MapPyStatementsTOJDK.class);
    private Map<TypeASTNode, String> typeNodes;
    private int augmentedChars=0;

    public MapPyStatementsTOJDK(Map<TypeASTNode, String> typeNodes) {
        this.typeNodes = typeNodes;
    }

    public ArrayList<?> getMappingPyNode(AST asn , PythonTree node, HashMap<String, org.eclipse.jdt.core.dom.Name> import_nodes,int startChar) throws NodeNotFoundException, ExpressionNotFound {
        if (node instanceof Import){
            int start_import = startChar;
            ArrayList<ImportDeclaration> list_imports = new ArrayList<>();
            for (PyObject pyObject : ((Import) node).getNames().asIterable()) {
                ImportDeclaration import_dec =  asn.newImportDeclaration();
                String[] import_name = ((alias) pyObject).getName().toString().split("\\.");
                org.eclipse.jdt.core.dom.Name name = asn.newName(import_name);
                name.setSourceRange(startChar+7,name.toString().length());
                import_dec.setName(name);
                PyObject asname = ((alias) pyObject).getAsname();
                if (!(asname instanceof PyNone)){
                    import_dec.setasName(asn.newName(asname.asString()));
                }
                logger.debug(((alias) pyObject).getCharStartIndex());
                int import_length = import_dec.toString().length();
                import_dec.setSourceRange(start_import,import_length);
                start_import+=import_length;
                list_imports.add(import_dec);
            }
            return list_imports;
        }
        else if (node instanceof ImportFrom){
            ArrayList<ImportDeclaration> list_imports = new ArrayList<>();
            String[] module = ((ImportFrom) node).getModule().toString().split("\\.");
            int start_import = startChar;
            for (PyObject pyObject : ((ImportFrom) node).getNames().asIterable()) {
                ImportDeclaration import_dec =  asn.newImportDeclaration();
                String[] import_name = (String[]) ArrayUtils.addAll(module,((alias) pyObject).getName().toString().split("\\."));
                org.eclipse.jdt.core.dom.Name name = asn.newName(import_name);
                name.setSourceRange(start_import+7,name.toString().length());
                import_dec.setName(name);
                PyObject asname = ((alias) pyObject).getAsname();
                if (!(asname instanceof PyNone)){
                    import_dec.setasName(asn.newName(asname.asString()));
                }
                int im_length = import_dec.toString().length();
                import_dec.setSourceRange(startChar,im_length);
                startChar+=im_length;
                list_imports.add(import_dec);
            }

            return list_imports;
        }
        else if (node instanceof ClassDef){
            int start_char_pos =startChar;
            TypeDeclaration classdec = asn.newTypeDeclaration();

            Modifier.ModifierKeyword keyword= new Modifier.ModifierKeyword("public",1);
            Modifier modifier = asn.newModifier(keyword);
            int modifier_length = keyword.toString().length();
            modifier.setSourceRange(start_char_pos,modifier_length);
            start_char_pos+=modifier_length;
            classdec.setModifier(modifier);



            ArrayList<TypeDeclaration> list_class = new ArrayList<>();
            SimpleName simpleName = asn.newSimpleName(((ClassDef) node).getName().toString());
            int cls_length = simpleName.toString().length();
            simpleName.setSourceRange(start_char_pos+7,cls_length);

            classdec.setName(simpleName);
            start_char_pos+=(8+cls_length); //length of "class"(5) + 2 + length of class name + space



            if (((AstList)((ClassDef) node).getBases()).size()>0){
                SimpleType simpleType = asn.newSimpleType(asn.newName(((Name) ((AstList) ((ClassDef) node).getBases()).get(0)).getInternalId()));
                int length_of_node = ((Name) ((AstList) ((ClassDef) node).getBases()).get(0)).getInternalId().length()+1;
                simpleType.setSourceRange(start_char_pos+=8,length_of_node);
                PyMap.totalCharGains+=length_of_node;
                classdec.setSuperclassType(simpleType); //TODO handle multiple inheritance
                start_char_pos+=length_of_node+2+2;
            }
            else
                start_char_pos+=2+2;//{,\n , space, space

            for (Object ch : (AstList)((ClassDef) node).getBody()){
                for (Object o : getMappingPyNode(asn, (PythonTree) ch,import_nodes,start_char_pos)) {
                    if (o instanceof BodyDeclaration){
                        classdec.bodyDeclarations().add(o);
                        System.out.println(o.toString());
                        start_char_pos+=o.toString().length()+2+2 +2*(o.toString().lines().count()-1) ; //space,space,two spaces in parameters
                    }
                    else {
                        logger.warn("The node is not node added :"+o.toString());
                    }

                }

            }
            classdec.setSourceRange(startChar,classdec.toString().length());
            list_class.add(classdec);
            return list_class;
        }
        else if (node instanceof FunctionDef){
            int start_char_pos =startChar;

            Set<TypeDecNeeds> variableNeedsDeclaration=null;
            try {
                variableNeedsDeclaration=getVariabelNeedsDecleration(node);
            } catch (Exception e) {
                logger.error(e);
            }
            MethodDeclaration methoddec = asn.newMethodDeclaration();
            SimpleName methodName = asn.newSimpleName(((FunctionDef) node).getName().toString());
            start_char_pos+=5; //length of void + two spaces
            int method_name_l = methodName.toString().length();
            methodName.setSourceRange(start_char_pos,method_name_l);
            methoddec.setName(methodName);
            start_char_pos+=1+method_name_l; //(
            //We assign only the parameter self. Type of other parameters are assigned
            int numb_args=0;
            for (Object arg : (AstList) ((arguments) ((FunctionDef) node).getArgs()).getArgs()) {
                if (((arg) arg).getArg().toString().equals("self") && node.getParent() instanceof ClassDef){
                    numb_args+=1;
                    if (numb_args==1)
                        start_char_pos+=2; //two spaces
                    else
                        start_char_pos+=3;
                    SingleVariableDeclaration parameter = asn.newSingleVariableDeclaration();

                    SimpleType simpleType = asn.newSimpleType(asn.newName(((ClassDef) node.getParent()).getInternalName()));
                    int type_length = simpleType.toString().length();
                    simpleType.setSourceRange(start_char_pos,type_length);

                    SimpleName simpleName = asn.newSimpleName(((arg) arg).getArg().toString());
                    simpleName.setSourceRange(start_char_pos+type_length+1,simpleName.toString().length());
                    parameter.setName(simpleName);
//TODO uncomment above
                    parameter.setType(simpleType);
                    parameter.setSourceRange(start_char_pos,parameter.toString().length());
                    methoddec.parameters().add(parameter);
                    start_char_pos+=type_length+4+1;// "self", space, type length,  comma
                }

            }
            start_char_pos+=3; // ),{ newline


            int number_of_par =  get_Number_Of_Parent_Statements(node);

            if (variableNeedsDeclaration != null) {
                for (TypeDecNeeds typeDecNeeds : variableNeedsDeclaration) {
                    String typeString = this.typeNodes.get(new TypeASTNode(typeDecNeeds.getRow(), typeDecNeeds.getCol_offset(), typeDecNeeds.getName(), null));
                    if (typeString!=null) {
                        start_char_pos+=2*number_of_par;// two spaces
                        VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, typeDecNeeds, typeString,start_char_pos);
                        if (methoddec.getBody() == null) {
                            methoddec.setBody(asn.newBlock());
                        }
                        varDecStat.setSourceRange(start_char_pos,varDecStat.toString().length());
                        start_char_pos+=varDecStat.toString().length();
                        methoddec.getBody().statements().add(varDecStat);
                    }
                }
            }


             //two spaces

            for (Object ch : (AstList)((FunctionDef) node).getBody()){
                start_char_pos+=2*number_of_par;
                for (Object o : getMappingPyNode(asn, (PythonTree) ch,import_nodes,start_char_pos)) {
                    if (methoddec.getBody() ==null){
                        methoddec.setBody(asn.newBlock());
                    }
                    int length = o.toString().length();
                    ((ASTNode)o).setSourceRange(start_char_pos,length);
                    methoddec.getBody().statements().add(o);
                    start_char_pos+=length;
                }
            }
            methoddec.setSourceRange(startChar, methoddec.toString().length());

            ArrayList<MethodDeclaration> list_method = new ArrayList<>();
            list_method.add(methoddec);
            return list_method;
        }
        else if (node instanceof Assign){
            Assignment assign = asn.newAssignment();
            assign.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,node.getCharStopIndex()-node.getCharStartIndex());
            ArrayList<ExpressionStatement> list_assign = new ArrayList<>();
            assign.setOperator(new Assignment.Operator("="));
            logger.debug(((AstList)((Assign) node).getTargets()));
            assert(((AstList)((Assign) node).getTargets()).size()<=1);
            assign.setLeftHandSide(MapPyExpressionsJDK.mapExpression((expr) ((AstList)((Assign) node).getTargets()).get(0),asn, import_nodes,0,typeNodes));
            assign.setRightHandSide(MapPyExpressionsJDK.mapExpression((expr)((Assign) node).getValue(),asn, import_nodes,0,typeNodes));
            assign.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,node.getCharStopIndex()-node.getCharStartIndex());
            ExpressionStatement statement = asn.newExpressionStatement(assign);

            statement.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,node.getCharStopIndex()-node.getCharStartIndex()+1);
            PyMap.totalCharGains+=1;
            list_assign.add(statement);
            return list_assign;

        }
        else if (node instanceof For){
            int start_char_pos =startChar;
            EnhancedForStatement forstmt = asn.newEnhancedForStatement();
            SingleVariableDeclaration parameter = asn.newSingleVariableDeclaration();
            ArrayList<EnhancedForStatement> list_for = new ArrayList<>();
            parameter.setName(asn.newSimpleName(((Name)(((For) node).getTarget())).getId().toString()));

            String typeString = this.typeNodes.get(new TypeASTNode(((Name)(((For) node).getTarget())).getLineno(),
                    ((Name)(((For) node).getTarget())).getCol_offset(), ((Name)(((For) node).getTarget())).getId().toString(), null));
            if (typeString!=null){
                Type jdtType = TypeStringToJDT.getJDTType(asn, typeString, 0);
                if (jdtType!=null){
                    parameter.setType(jdtType);
                }
                else
                    logger.error("Type of for loop variable is not updated");
            }
            else
                logger.error("Type of for loop variable is not updated");


            start_char_pos+=3+2+2+2; //for , space, ( , 4space

            int para_int = parameter.toString().length();
            parameter.setSourceRange(start_char_pos,para_int);
            start_char_pos+=para_int+1+1+1; //space, : , space

            forstmt.setParameter(parameter);
            forstmt.setExpression(MapPyExpressionsJDK.mapExpression((expr)((For) node).getIter(),asn,import_nodes,start_char_pos,typeNodes));
            int number_of_parents = get_Number_Of_Parent_Statements(node);
            start_char_pos += 4; //), space,{,new line
            int start_of_for_loop = start_char_pos+number_of_parents*2;
            for (Object ch : (AstList)((For) node).getBody()){
                for (Object o : getMappingPyNode(asn, (PythonTree) ch,import_nodes,start_char_pos+=number_of_parents*2)) {
                    if (forstmt.getBody() ==null){
                        forstmt.setBody(asn.newBlock());
                    }
                    ((Block)forstmt.getBody()).statements().add(o);
                }
                forstmt.getBody().setSourceRange(start_of_for_loop, node.toString().length());
            }
            forstmt.setSourceRange(startChar,node.toString().length());
            list_for.add(forstmt);
            return list_for;

        }
        else if (node instanceof AugAssign){
            Assignment assign = asn.newAssignment();
            ArrayList<ExpressionStatement> list_assign = new ArrayList<>();
            if (((AugAssign) node).getInternalOp().name().equals("Add")){
                assign.setOperator(new Assignment.Operator("+="));
            }
            else {
                throw new NodeNotFoundException("Operator is not implemented :"+((AugAssign) node).getInternalOp().name());
            }

            assign.setLeftHandSide( MapPyExpressionsJDK.mapExpression((expr) ((AugAssign) node).getTarget(),asn, import_nodes,0,typeNodes));
            assign.setRightHandSide(MapPyExpressionsJDK.mapExpression((expr)((AugAssign) node).getValue(),asn, import_nodes,0,typeNodes));

            ExpressionStatement statement = asn.newExpressionStatement(assign);

            list_assign.add(statement);
            return list_assign;
        }
        else if (node instanceof Return){
            ReturnStatement statement = asn.newReturnStatement();
            ArrayList<ReturnStatement> list_assign = new ArrayList<>();
            statement.setExpression(MapPyExpressionsJDK.mapExpression((expr) ((Return) node).getValue(),asn, import_nodes,0,typeNodes));

            statement.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,node.getCharStopIndex()-node.getCharStartIndex());
            list_assign.add(statement);
            return list_assign;
        }
        else if (node instanceof Expr){
            if (((Expr) node).getValue() instanceof Call){ //Add super invocation
                Call value = (Call)((Expr) node).getValue();
                if (value.getFunc() instanceof Name && ((Name) value.getFunc()).getId().equals("super")){
                    SuperConstructorInvocation constructorInvocation = asn.newSuperConstructorInvocation();
                    for (Object arg : (AstList) value.getArgs()) {
                        constructorInvocation.arguments().add(MapPyExpressionsJDK.mapExpression((expr) arg,asn, import_nodes,0,typeNodes));
                    }
                    ArrayList<SuperConstructorInvocation> list_assign = new ArrayList<>();
                    list_assign.add(constructorInvocation);
                    return list_assign ;
                }
                else if (value.getFunc() instanceof Attribute
                        && ((Attribute) value.getFunc()).getValue() instanceof Call
                        && ((Call) ((Attribute) value.getFunc()).getValue()).getFunc() instanceof Name
                        && ((Name) ((Call) ((Attribute) value.getFunc()).getValue()).getFunc()).getId().toString().equals("super")
                        && ((Attribute) value.getFunc()).getAttr().toString().equals("__init__")){

                    SuperConstructorInvocation constructorInvocation = asn.newSuperConstructorInvocation();
                    for (Object arg : (AstList) ((Call) ((Attribute) value.getFunc()).getValue()).getArgs()) {
                        constructorInvocation.arguments().add(MapPyExpressionsJDK.mapExpression((expr) arg,asn, import_nodes,0,typeNodes));
                    }
                    ArrayList<SuperConstructorInvocation> list_assign = new ArrayList<>();
                    list_assign.add(constructorInvocation);
                    return list_assign ;

                }


            }
            Expression exp = MapPyExpressionsJDK.mapExpression((expr)((Expr) node).getValue(),asn, import_nodes,0,typeNodes);
            ArrayList<ExpressionStatement> list_assign = new ArrayList<>();
            ExpressionStatement expstmt= asn.newExpressionStatement(exp);
            expstmt.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,node.getCharStopIndex()-node.getCharStartIndex());
            list_assign.add(expstmt);


            return list_assign;

        }
        else if (node instanceof If){
            IfStatement ifStatement = asn.newIfStatement();
            ifStatement.setExpression(MapPyExpressionsJDK.mapExpression((expr) ((If) node).getTest(),asn,import_nodes,0,typeNodes));
            ((AstList) ((If) node).getBody()).stream().forEach(
                    ob-> {
                        try {
                            ((Block)ifStatement.getThenStatement()).statements().addAll(getMappingPyNode(asn, (PythonTree) ob,import_nodes,0));
                        } catch (NodeNotFoundException e) {
                            e.printStackTrace();
                        } catch (ExpressionNotFound expressionNotFound) {
                            expressionNotFound.printStackTrace();
                        }
                    });
            if (((AstList)((If) node).getOrelse()).size()>0){
                if (((AstList)((If) node).getOrelse()).size()==1 && ((AstList)((If) node).getOrelse()).get(0) instanceof If ){
                    ifStatement.setElseStatement((Statement) getMappingPyNode
                            (asn, (PythonTree) ((AstList)((If) node).getOrelse()).get(0),import_nodes,0).get(0));
                }
                else{
                    ((AstList) ((If) node).getOrelse()).stream().forEach(
                            ob-> {
                                try {
                                    if (ifStatement.getElseStatement()==null){
                                        ifStatement.setElseStatement(asn.newBlock());
                                    }
                                    ((Block)ifStatement.getElseStatement()).statements().addAll(getMappingPyNode(asn,
                                            (PythonTree) ob,import_nodes,0));
                                } catch (NodeNotFoundException e) {
                                    e.printStackTrace();
                                } catch (ExpressionNotFound expressionNotFound) {
                                    expressionNotFound.printStackTrace();
                                }
                            });
                }
            }


            ArrayList<IfStatement> list_assign = new ArrayList<>();
            list_assign.add(ifStatement);
            return list_assign;
        }
        else if (node instanceof Raise){
            ThrowStatement throwStatement = asn.newThrowStatement();
            ClassInstanceCreation classInstanceCreation = asn.newClassInstanceCreation();
            if (((Raise) node).getExc() instanceof Call){
                SimpleType simpleType = asn.newSimpleType((org.eclipse.jdt.core.dom.Name) MapPyExpressionsJDK.mapExpression((expr) ((Call) ((Raise) node).getExc()).getFunc(), asn, import_nodes, 0, typeNodes));
                classInstanceCreation.setType(simpleType);
                ((AstList)((Call) ((Raise) node).getExc()).getArgs()).stream().forEach(o->
                {
                    try {
                        Expression expression = MapPyExpressionsJDK.mapExpression((expr) o, asn, import_nodes, 0, typeNodes);
                        classInstanceCreation.arguments().add(expression);
                    } catch (ExpressionNotFound expressionNotFound) {
                        expressionNotFound.printStackTrace();
                    } catch (NodeNotFoundException e) {
                        e.printStackTrace();
                    }
                });
            }
            else{
                logger.fatal("Throw new expression was not created correctly");
            }
            throwStatement.setExpression(classInstanceCreation);
            ArrayList<ThrowStatement> list_assign = new ArrayList<>();
            list_assign.add(throwStatement);
            return list_assign;
        }

        else {
            logger.fatal("Corresponding Python node is not found : found "+node.getClass()+" Class "+node.toString());
            return new ArrayList<>();
        }

    }

    private Set<TypeDecNeeds> getVariabelNeedsDecleration(PythonTree node) throws Exception {
        PyVisitor pyVisitor = new PyVisitor();
        pyVisitor.visit(node);
        return pyVisitor.getTypeDecNeeds();
    }

    private int get_Number_Of_Parent_Statements(PythonTree node){
        PythonTree parent = node.getParent();

        int number_of_parents = (parent==null) ? 0 : 1;
        while (parent!=null){
            if (parent instanceof stmt){
                number_of_parents+=1;
            }
            parent=parent.getParent();
        }
        return number_of_parents;
    }

}
