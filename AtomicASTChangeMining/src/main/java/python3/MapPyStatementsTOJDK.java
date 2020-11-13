package python3;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PyErrorExpression;
import org.eclipse.jdt.core.dom.PyTupleExpression;
import org.eclipse.jdt.core.dom.PyWithStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.jpp.astnodes.PythonTree;
import org.jpp.astnodes.ast.Assert;
import org.jpp.astnodes.ast.Assign;
import org.jpp.astnodes.ast.Attribute;
import org.jpp.astnodes.ast.AugAssign;
import org.jpp.astnodes.ast.Break;
import org.jpp.astnodes.ast.Call;
import org.jpp.astnodes.ast.ClassDef;
import org.jpp.astnodes.ast.Continue;
import org.jpp.astnodes.ast.Delete;
import org.jpp.astnodes.ast.ErrorStmt;
import org.jpp.astnodes.ast.ExceptHandler;
import org.jpp.astnodes.ast.Expr;
import org.jpp.astnodes.ast.For;
import org.jpp.astnodes.ast.FunctionDef;
import org.jpp.astnodes.ast.Global;
import org.jpp.astnodes.ast.If;
import org.jpp.astnodes.ast.Import;
import org.jpp.astnodes.ast.ImportFrom;
import org.jpp.astnodes.ast.Name;
import org.jpp.astnodes.ast.Pass;
import org.jpp.astnodes.ast.Raise;
import org.jpp.astnodes.ast.Return;
import org.jpp.astnodes.ast.Str;
import org.jpp.astnodes.ast.TryExcept;
import org.jpp.astnodes.ast.Tuple;
import org.jpp.astnodes.ast.While;
import org.jpp.astnodes.ast.With;
import org.jpp.astnodes.ast.alias;
import org.jpp.astnodes.ast.arg;
import org.jpp.astnodes.ast.arguments;
import org.jpp.astnodes.ast.operatorType;
import org.jpp.astnodes.base.expr;
import org.jpp.astnodes.base.stmt;
import org.jpp.heart.AstList;
import org.jpp.heart.PyNone;
import org.jpp.heart.PyObject;
import org.jpp.heart.PyUnicode;
import org.jpp.modules.itertools.groupby;
import python3.pyerrors.ExpressionNotFound;
import python3.pyerrors.NodeNotFoundException;
import python3.pyvisitors.PyVisitor;
import python3.typeinference.core.TypeASTNode;
import python3.typeinference.core.TypeDecNeeds;
import python3.typeinference.core.TypeStringToJDT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapPyStatementsTOJDK extends PyMap{
    static Logger logger = Logger.getLogger(MapPyStatementsTOJDK.class);
    private Map<TypeASTNode, String> typeNodes;
    private int augmentedChars=0;

    public MapPyStatementsTOJDK(Map<TypeASTNode, String> typeNodes) {
        this.typeNodes = typeNodes;
    }

    public ArrayList<?> getMappingPyNode(AST asn, PythonTree node, HashMap<String, org.eclipse.jdt.core.dom.Name> import_nodes, int startChar, PyCompilationUnit pyc) throws NodeNotFoundException, ExpressionNotFound {
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
                Expression className = MapPyExpressionsJDK.mapExpression((expr)
                        ((AstList) ((ClassDef) node).getBases()).get(0),asn,import_nodes,0,typeNodes,pyc);
                if (className instanceof PyErrorExpression){return new ArrayList<>();}  //If python can not parse the class name correctly there is no point of converting this further

                SimpleType simpleType = asn.newSimpleType((org.eclipse.jdt.core.dom.Name) className);
                classdec.setSuperclassType(simpleType); //TODO handle multiple inheritance

            }
            else
                start_char_pos+=2+2;//{,\n , space, space

            for (Object ch : (AstList)((ClassDef) node).getBody()){
                for (Object o : getMappingPyNode(asn, (PythonTree) ch,import_nodes,start_char_pos,pyc )) {
                    if (o instanceof BodyDeclaration){
                        classdec.bodyDeclarations().add(o);
                        start_char_pos+=o.toString().length()+2+2 +2*(o.toString().lines().count()-1) ; //space,space,two spaces in parameters
                    }
                    else {
                        if (!((o instanceof ExpressionStatement && ((ExpressionStatement) o).getExpression() instanceof StringLiteral)|| o instanceof EmptyStatement )){
                            logger.warn("The node is not node added :"+o.toString());
                        }
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

                Map<String, List<TypeDecNeeds>> collect = variableNeedsDeclaration.stream().collect(Collectors.groupingBy(TypeDecNeeds::getName));
                for (Map.Entry<String, List<TypeDecNeeds>> entry : collect.entrySet()){
                    if (entry.getValue().size()==1){
                        TypeDecNeeds typeDecNeeds = entry.getValue().get(0);
                        String typeString = this.typeNodes.get(new TypeASTNode(typeDecNeeds.getRow(), typeDecNeeds.getCol_offset(), typeDecNeeds.getName(), null));
                        VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, typeDecNeeds, typeString,0);
                        if (methoddec.getBody() == null) {
                            methoddec.setBody(asn.newBlock());
                        }
                        varDecStat.setSourceRange(start_char_pos,varDecStat.toString().length());
                        methoddec.getBody().statements().add(varDecStat);
                    }
                    else{
                        Set<String> hash_Set= new HashSet<>();
                        for (TypeDecNeeds typeDecNeeds : entry.getValue()) {
                            String typeString = this.typeNodes.get(new TypeASTNode(typeDecNeeds.getRow(), typeDecNeeds.getCol_offset(), typeDecNeeds.getName(), null));
                            hash_Set.add(typeString);
                        }
                        if (hash_Set.size()==1){
                            VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, entry.getKey(), hash_Set.iterator().next(),0);
                            if (methoddec.getBody() == null) {
                                methoddec.setBody(asn.newBlock());
                            }
                            methoddec.getBody().statements().add(varDecStat);
                        }
                        else{
                            List<String> collect1 = hash_Set.stream().filter(Predicate.not(x -> x.equals("Any") || x.equals("PyTypeError"))).collect(Collectors.toList());
                            if (collect1.size()==0){
                                if (hash_Set.contains("Any")){
                                    VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, entry.getKey(), "Any",0);
                                    if (methoddec.getBody() == null) {
                                        methoddec.setBody(asn.newBlock());
                                    }
                                    methoddec.getBody().statements().add(varDecStat);
                                }
                                else if (hash_Set.contains("PyTypeError")){
                                    VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, entry.getKey(), "PyTypeError",0);
                                    if (methoddec.getBody() == null) {
                                        methoddec.setBody(asn.newBlock());
                                    }
                                    methoddec.getBody().statements().add(varDecStat);
                                }
                            }
                            else if (collect1.size()==1){
                                VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, entry.getKey(), collect1.get(0),0);
                                if (methoddec.getBody() == null) {
                                    methoddec.setBody(asn.newBlock());
                                }
                                methoddec.getBody().statements().add(varDecStat);
                            }
                            else{
                                StringBuilder unionTypeStr = new StringBuilder("Union[");
                                int type_length =0;
                                for (String type:collect1){
                                    type_length+=1;
                                    if (type_length==collect1.size()) {
                                        unionTypeStr.append(type);
                                    }
                                    else {
                                        unionTypeStr.append(type).append(",");
                                    }
                                }
                                unionTypeStr.append("]");
                                VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, entry.getKey(), unionTypeStr.toString(),0);
                                if (methoddec.getBody() == null) {
                                    methoddec.setBody(asn.newBlock());
                                }
                                methoddec.getBody().statements().add(varDecStat);
                            }
                        }
                    }
                }

//                for (TypeDecNeeds typeDecNeeds : variableNeedsDeclaration) {
//                    String typeString = this.typeNodes.get(new TypeASTNode(typeDecNeeds.getRow(), typeDecNeeds.getCol_offset(), typeDecNeeds.getName(), null));
//                    start_char_pos+=2*number_of_par;// two spaces
//                    VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, typeDecNeeds, typeString,start_char_pos);
//                    if (methoddec.getBody() == null) {
//                        methoddec.setBody(asn.newBlock());
//                    }
//                    varDecStat.setSourceRange(start_char_pos,varDecStat.toString().length());
//                    start_char_pos+=varDecStat.toString().length();
//                    methoddec.getBody().statements().add(varDecStat);
//
//                }
            }


             //two spaces

            for (Object ch : (AstList)((FunctionDef) node).getBody()){
                start_char_pos+=2*number_of_par;
                if (ch instanceof Expr && ((Expr) ch).getValue() instanceof Str){
                    continue;  //remove comments
                }

                for (Object o : getMappingPyNode(asn, (PythonTree) ch,import_nodes,start_char_pos, pyc)) {
                    if (methoddec.getBody() ==null){
                        methoddec.setBody(asn.newBlock());
                    }
                    int length = o.toString().length();
                    ((ASTNode)o).setSourceRange(start_char_pos,length);
                    if (o instanceof ImportDeclaration){
                        pyc.imports().add(o);
                    }
                    else{
                        if (o instanceof MethodDeclaration) {
                            TypeDeclaration typeDec = asn.newTypeDeclaration();
                            typeDec.setName((SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName()));
                            typeDec.bodyDeclarations().add(o);
                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                            methoddec.getBody().statements().add(dummyClass);
                        }
                        else{
                            methoddec.getBody().statements().add(o);
                        }
                    }
                    start_char_pos+=length;
                }
            }
            methoddec.setSourceRange(startChar, methoddec.toString().length());

            ArrayList<MethodDeclaration> list_method = new ArrayList<>();
            list_method.add(methoddec);
            return list_method;
        }
        else if (node instanceof Assign){
                        ArrayList<ExpressionStatement> list_assign = new ArrayList<>();
            for (expr target : ((Assign) node).getInternalTargets()) {
                Assignment assign = asn.newAssignment();
                assign.setOperator(new Assignment.Operator("="));
                Expression leftExpression = MapPyExpressionsJDK.mapExpression(target, asn, import_nodes, 0, typeNodes,pyc );
                assign.setLeftHandSide(leftExpression);
                Expression rightHandExpression = MapPyExpressionsJDK.mapExpression((expr) ((Assign) node).getValue(), asn, import_nodes, 0, typeNodes, pyc);
                assign.setRightHandSide(rightHandExpression);
                ExpressionStatement statement = asn.newExpressionStatement(assign);
                if (!(leftExpression instanceof PyErrorExpression || rightHandExpression instanceof PyErrorExpression )){
                    list_assign.add(statement);
                }
            }
            return list_assign;
        }
        else if (node instanceof For){
            //TODO change the grammer to add else statement
            int start_char_pos =startChar;
            EnhancedForStatement forstmt = asn.newEnhancedForStatement();

            ArrayList<EnhancedForStatement> list_for = new ArrayList<>();
            if (((For) node).getTarget() instanceof Name){
                SingleVariableDeclaration parameter_dummy = asn.newSingleVariableDeclaration();
                parameter_dummy.setName(asn.newSimpleName( "DummyTerminalNode"));
                parameter_dummy.setType(asn.newSimpleType(asn.newName("DummyTerminalTypeNode")));


                SingleVariableDeclaration parameter = asn.newSingleVariableDeclaration();
                parameter.setName(asn.newSimpleName(((Name)(((For) node).getTarget())).getId().toString()));
                String typeString = this.typeNodes.get(new TypeASTNode(((Name)(((For) node).getTarget())).getLineno(),
                        ((Name)(((For) node).getTarget())).getCol_offset(), ((Name)(((For) node).getTarget())).getId().toString(), null));

                Type jdtType = TypeStringToJDT.getJDTType(asn, typeString, 0);
                if (jdtType!=null){
                    parameter.setType(jdtType);
                }
                else
                    logger.error("Type of for loop variable is not updated");
                forstmt.setParameter(parameter_dummy);
                forstmt.Parameters().add(parameter);


            }
            else if (((For) node).getTarget() instanceof Tuple){
                SingleVariableDeclaration parameter_dummy = asn.newSingleVariableDeclaration();
                parameter_dummy.setName(asn.newSimpleName( "DummyTerminalNode"));
                parameter_dummy.setType(asn.newSimpleType(asn.newName("DummyTerminalTypeNode")));
                forstmt.setParameter(parameter_dummy);
                for (Object elt : (AstList) ((Tuple) ((For) node).getTarget()).getElts()) {
                    SingleVariableDeclaration lo_parameter = asn.newSingleVariableDeclaration();
                    lo_parameter.setName(asn.newSimpleName(((Name)elt).getId().toString()));
                    String typeString = this.typeNodes.get(new TypeASTNode(((Name)elt).getLineno(),
                            ((Name)elt).getCol_offset(), ((Name)elt).getId().toString(), null));

                    Type jdtType = TypeStringToJDT.getJDTType(asn, typeString, 0);
                    if (jdtType!=null){
                        lo_parameter.setType(jdtType);
                    }
                    else
                        logger.error("Type of for loop variable is not updated");
                    forstmt.Parameters().add(lo_parameter);

                }
            }
            else{
                logger.error("The mapping for the corresponding for loop parameter is not found");
            }
            start_char_pos+=3+2+2+2; //for , space, ( , 4space
//
//            int para_int = parameter.toString().length();
//            parameter.setSourceRange(start_char_pos,para_int);
//            start_char_pos+=para_int+1+1+1; //space, : , space



            Expression expression = MapPyExpressionsJDK.mapExpression((expr)((For) node).getIter(),asn,import_nodes,start_char_pos,typeNodes,pyc );
            if (expression instanceof PyErrorExpression){return new ArrayList<>();}
            forstmt.setExpression(expression);
            int number_of_parents = get_Number_Of_Parent_Statements(node);
            start_char_pos += 4; //), space,{,new line
            int start_of_for_loop = start_char_pos+number_of_parents*2;
            for (Object ch : (AstList)((For) node).getBody()){
                for (Object o : getMappingPyNode(asn, (PythonTree) ch,import_nodes,start_char_pos+=number_of_parents*2, pyc)) {
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
            if (((AugAssign) node).getInternalOp()== operatorType.Div){
                assign.setOperator(Assignment.Operator.DIVIDE_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp()== operatorType.Add){
                assign.setOperator(Assignment.Operator.PLUS_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp()== operatorType.Sub){
                assign.setOperator(Assignment.Operator.MINUS_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp() == operatorType.BitAnd){
                assign.setOperator(Assignment.Operator.BIT_AND_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp() == operatorType.BitOr){
                assign.setOperator(Assignment.Operator.BIT_OR_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp() == operatorType.Mult){
                assign.setOperator(Assignment.Operator.TIMES_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp() == operatorType.LShift){
                assign.setOperator(Assignment.Operator.LEFT_SHIFT_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp() == operatorType.RShift){
                assign.setOperator(Assignment.Operator.RIGHT_SHIFT_SIGNED_ASSIGN);
            }
            else {
                assert  false;
                throw new NodeNotFoundException("Operator is not implemented :"+((AugAssign) node).getInternalOp().name());
            }
            Expression leftExpression =  MapPyExpressionsJDK.mapExpression((expr) ((AugAssign) node).getTarget(),asn, import_nodes,0,typeNodes,pyc );
            Expression rightExpression =  MapPyExpressionsJDK.mapExpression((expr)((AugAssign) node).getValue(),asn, import_nodes,0,typeNodes,pyc );
            if (leftExpression instanceof PyErrorExpression || rightExpression instanceof PyErrorExpression){return new ArrayList<>();}
            assign.setLeftHandSide(leftExpression);
            assign.setRightHandSide(rightExpression);

            ExpressionStatement statement = asn.newExpressionStatement(assign);

            list_assign.add(statement);
            return list_assign;
        }
        else if (node instanceof Return){
            ReturnStatement statement = asn.newReturnStatement();
            ArrayList<ReturnStatement> list_assign = new ArrayList<>();
            if ( ((Return) node).getValue()!=null){
                statement.setExpression(MapPyExpressionsJDK.mapExpression((expr) ((Return) node).getValue(),asn, import_nodes,0,typeNodes,pyc ));
            }
            statement.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,node.getCharStopIndex()-node.getCharStartIndex());
            list_assign.add(statement);
            return list_assign;
        }
        else if (node instanceof Expr){
            if (((Expr) node).getValue() instanceof Call){ //Add super invocation
                Call value = (Call)((Expr) node).getValue();
//                if (value.getFunc() instanceof Name && ((Name) value.getFunc()).getId().equals("isinstance")){
//                    InstanceofExpression instanceofExpression = asn.newInstanceofExpression();
//                    instanceofExpression.setLeftOperand(MapPyExpressionsJDK.mapExpression((expr) ((AstList) value.getArgs()).get(0),asn, import_nodes,0,typeNodes));
//                    instanceofExpression.setLeftOperand(MapPyExpressionsJDK.mapExpression((expr) ((AstList) value.getArgs()).get(1),asn, import_nodes,0,typeNodes));
//                    ArrayList<InstanceofExpression> list_assign = new ArrayList<>();
//                    list_assign.add(instanceofExpression);
//                    return list_assign ;
//                }
                if (value.getFunc() instanceof Name && ((Name) value.getFunc()).getId().equals("super")){
                    SuperConstructorInvocation constructorInvocation = asn.newSuperConstructorInvocation();
                    for (Object arg : (AstList) value.getArgs()) {
                        constructorInvocation.arguments().add(MapPyExpressionsJDK.mapExpression((expr) arg,asn, import_nodes,0,typeNodes,pyc ));
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
                        constructorInvocation.arguments().add(MapPyExpressionsJDK.mapExpression((expr) arg,asn, import_nodes,0,typeNodes,pyc ));
                    }
                    ArrayList<SuperConstructorInvocation> list_assign = new ArrayList<>();
                    list_assign.add(constructorInvocation);
                    return list_assign ;
                }
            }
            Expression exp = MapPyExpressionsJDK.mapExpression((expr)((Expr) node).getValue(),asn, import_nodes,0,typeNodes,pyc );
            ArrayList<ExpressionStatement> list_assign = new ArrayList<>();
            ExpressionStatement expstmt= asn.newExpressionStatement(exp);
            expstmt.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,node.getCharStopIndex()-node.getCharStartIndex());
            list_assign.add(expstmt);
            return list_assign;

        }
        else if (node instanceof If){
            IfStatement ifStatement = asn.newIfStatement();
            Expression expression = MapPyExpressionsJDK.mapExpression((expr) ((If) node).getTest(), asn, import_nodes, 0, typeNodes, pyc);
            if (expression instanceof PyErrorExpression){return new ArrayList<>();}
            ifStatement.setExpression(expression);

            ((AstList) ((If) node).getBody()).stream().forEach(
                    ob-> {
                        try {
                            for (Object o : getMappingPyNode(asn, (PythonTree) ob, import_nodes, 0, pyc)) {
                                if (!(o instanceof MethodDeclaration || o instanceof ImportDeclaration)){
                                    ((Block)ifStatement.getThenStatement()).statements().add(o);
                                }
                                else if (o instanceof MethodDeclaration) {
                                    TypeDeclaration typeDec = asn.newTypeDeclaration();
                                    typeDec.setName((SimpleName) ASTNode.copySubtree(asn,((MethodDeclaration)o).getName()));
                                    typeDec.bodyDeclarations().add(o);
                                    TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                                    ((Block)ifStatement.getThenStatement()).statements().add(dummyClass);
                                }
                                else if (o instanceof ImportDeclaration){
                                    pyc.imports().add(o);
                                }


                            }

                        } catch (NodeNotFoundException e) {
                            e.printStackTrace();
                        } catch (ExpressionNotFound expressionNotFound) {
                            expressionNotFound.printStackTrace();
                        }
                    });
            if (((AstList)((If) node).getOrelse()).size()>0){
                if (((AstList)((If) node).getOrelse()).size()==1 && ((AstList)((If) node).getOrelse()).get(0) instanceof If ){
                    ifStatement.setElseStatement((Statement) getMappingPyNode
                            (asn, (PythonTree) ((AstList)((If) node).getOrelse()).get(0),import_nodes,0, pyc).get(0));
                }
                else{
                    ((AstList) ((If) node).getOrelse()).stream().forEach(
                            ob-> {
                                try {
                                    if (ifStatement.getElseStatement()==null){
                                        ifStatement.setElseStatement(asn.newBlock());
                                    }
                                    ((Block)ifStatement.getElseStatement()).statements().addAll(getMappingPyNode(asn,
                                            (PythonTree) ob,import_nodes,0,pyc ));
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
                SimpleType simpleType = asn.newSimpleType((org.eclipse.jdt.core.dom.Name) MapPyExpressionsJDK.mapExpression((expr) ((Call) ((Raise) node).getExc()).getFunc(), asn, import_nodes, 0, typeNodes,pyc ));
                classInstanceCreation.setType(simpleType);
                ((AstList)((Call) ((Raise) node).getExc()).getArgs()).stream().forEach(o->
                {
                    try {
                        Expression expression = MapPyExpressionsJDK.mapExpression((expr) o, asn, import_nodes, 0, typeNodes, pyc);
                        classInstanceCreation.arguments().add(expression);
                    } catch (ExpressionNotFound expressionNotFound) {
                        expressionNotFound.printStackTrace();
                    } catch (NodeNotFoundException e) {
                        e.printStackTrace();
                    }
                });

            }
            else{
                classInstanceCreation.setType(asn.newSimpleType(asn.newSimpleName("PythonException")));
                logger.info("PythonException was assigned to empty raise statment");
            }
            throwStatement.setExpression(classInstanceCreation);
            ArrayList<ThrowStatement> list_assign = new ArrayList<>();
            list_assign.add(throwStatement);
            return list_assign;
        }
        else if (node instanceof TryExcept){
            TryStatement tryStatement = asn.newTryStatement();
            Block block = asn.newBlock();
            ((AstList)((TryExcept) node).getBody()).stream().forEach(x-> {
                try {
                    ArrayList<?> mappingPyNode = getMappingPyNode(asn, (PythonTree) x, import_nodes, 0, pyc);
                    block.statements().addAll(mappingPyNode.stream().filter(Predicate.not(z->z instanceof ImportDeclaration)).collect(Collectors.toList()));
                    pyc.imports().addAll(mappingPyNode.stream().filter(z->z instanceof ImportDeclaration).collect(Collectors.toList()));

                } catch (NodeNotFoundException e) {
                    e.printStackTrace();
                } catch (ExpressionNotFound expressionNotFound) {
                    expressionNotFound.printStackTrace();
                }
            });

            ((AstList)((TryExcept) node).getHandlers()).stream().forEach(x-> {
                try {
                    tryStatement.catchClauses().addAll(getMappingPyNode(asn, (PythonTree) x, import_nodes, 0, pyc));
                } catch (NodeNotFoundException e) {
                    e.printStackTrace();
                } catch (ExpressionNotFound expressionNotFound) {
                    expressionNotFound.printStackTrace();
                }
            });
            if (((TryExcept) node).getInternalOrelse().size()>0){
                tryStatement.setFinally(asn.newBlock());
            }

            (((TryExcept) node).getInternalOrelse()).stream().forEach(x->
                    {
                        try {
                            tryStatement.getFinally().statements().addAll(getMappingPyNode(asn,  x, import_nodes, 0, pyc));
                        } catch (NodeNotFoundException e) {
                            e.printStackTrace();
                        } catch (ExpressionNotFound expressionNotFound) {
                            expressionNotFound.printStackTrace();
                        }
                    }
            );

            tryStatement.setBody(block);


            ArrayList<TryStatement> list_assign = new ArrayList<>();
            list_assign.add(tryStatement);
            return list_assign;
        }
        else if (node instanceof ExceptHandler){
            ArrayList<CatchClause> list_assign = new ArrayList<>();
            CatchClause catchClause = asn.newCatchClause();
            Block block = asn.newBlock();
            ((AstList)((ExceptHandler) node).getBody()).stream().forEach(x-> {
                try {
                    block.statements().addAll(getMappingPyNode(asn, (PythonTree) x, import_nodes, 0, pyc).stream().filter(Predicate.not(z->z instanceof ImportDeclaration)).collect(Collectors.toList()));
                    pyc.imports().addAll(getMappingPyNode(asn, (PythonTree) x, import_nodes, 0, pyc).stream().filter(z->z instanceof ImportDeclaration).collect(Collectors.toList()));

                } catch (NodeNotFoundException e) {
                    e.printStackTrace();
                } catch (ExpressionNotFound expressionNotFound) {
                    expressionNotFound.printStackTrace();
                }
            });
            SingleVariableDeclaration singleVarDec = asn.newSingleVariableDeclaration();
            Expression expression = MapPyExpressionsJDK.mapExpression((expr) ((ExceptHandler) node).getExceptType(), asn, import_nodes, 0, typeNodes, pyc);
            if (expression instanceof  org.eclipse.jdt.core.dom.PyTupleExpression){
                singleVarDec.setType(asn.newSimpleType(asn.newSimpleName(((SimpleName) ((PyTupleExpression) expression).expressions().get(0)).getIdentifier())));//TODO change grammer to support multiple exceptions in catch claus
            }
            else{
                singleVarDec.setType(asn.newSimpleType((org.eclipse.jdt.core.dom.Name) expression));
            }


            if (((ExceptHandler) node).getName()!=null){
                if (((ExceptHandler) node).getName() instanceof PyUnicode){
                    SimpleName simpleName = asn.newSimpleName(((PyUnicode) ((ExceptHandler) node).getName()).getString());
                    singleVarDec.setName(simpleName);
                }
                else if (((ExceptHandler) node).getName() instanceof PyNone){
                    SimpleName simpleName = asn.newSimpleName("PyCpatDummy");
                    singleVarDec.setName(simpleName);
                }
                else{
                    Expression name = MapPyExpressionsJDK.mapExpression((expr) ((ExceptHandler) node).getName(), asn, import_nodes, 0, typeNodes, pyc);
                    singleVarDec.setName((SimpleName) name);
                }

            }

            catchClause.setException(singleVarDec);
            catchClause.setBody(block);
            list_assign.add(catchClause);
            return list_assign;
        }
        else if (node instanceof Delete){
            MethodInvocation methodInvocation = asn.newMethodInvocation();
            methodInvocation.setName(asn.newSimpleName("del"));
            for (Object arg : (AstList) ((Delete) node).getTargets()) {  //TODO keyword arguments are not parsed by the JPyParser, Thus is a Bug
                methodInvocation.arguments().add(MapPyExpressionsJDK.mapExpression((expr) arg,asn, import_nodes,0,typeNodes,pyc ));
            }

            ArrayList<ExpressionStatement> list_assign = new ArrayList<>();
            list_assign.add(asn.newExpressionStatement(methodInvocation));
            return list_assign;
        }
        else if (node instanceof With) {
            PyWithStatement pyWithStatement = asn.newPyWithStatement();
            if (((With) node).getInternalItems().size() > 1) {
                logger.error("With item contain more than one item in the header");
            }
            expr internalContext_expr = ((With) node).getInternalItems().get(0).getInternalContext_expr();
            Expression expression = MapPyExpressionsJDK.mapExpression(internalContext_expr, asn, import_nodes, 0, typeNodes,pyc );
            pyWithStatement.setExpression(expression);
            expr optional_vars = ((With) node).getInternalItems().get(0).getInternalOptional_vars();

            Block block = asn.newBlock();
            if (optional_vars != null && optional_vars instanceof Name) {
                String typeString = this.typeNodes.get(new TypeASTNode(((Name) optional_vars).getLineno(), ((Name) optional_vars).getCol_offset(), ((Name) optional_vars).getInternalId(), null));
                VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, ((Name) optional_vars).getInternalId(), typeString, 0);
                block.statements().add(varDecStat);
            }
            ((With) node).getInternalBody().stream().forEach(x -> {
                    try {
                        block.statements().addAll(getMappingPyNode(asn, x, import_nodes, 0, pyc));
                    } catch (NodeNotFoundException e) {
                        e.printStackTrace();
                    } catch (ExpressionNotFound expressionNotFound) {
                        expressionNotFound.printStackTrace();
                    }
                });
            pyWithStatement.setBody(block);
            ArrayList<PyWithStatement> list_assign = new ArrayList<>();
            list_assign.add(pyWithStatement);
            return list_assign;
            }
        else if (node instanceof Break){
            BreakStatement breakStatement = asn.newBreakStatement();

            ArrayList<BreakStatement> list_assign = new ArrayList<>();
            list_assign.add(breakStatement);
            return list_assign;
        }
        else if (node instanceof Continue){
            ContinueStatement continueStatement = asn.newContinueStatement();
            ArrayList<ContinueStatement> list_assign = new ArrayList<>();
            list_assign.add(continueStatement);
            return list_assign;
        }
        else if (node instanceof Pass){
            EmptyStatement statement = asn.newEmptyStatement();
            ArrayList<EmptyStatement> list_assign = new ArrayList<>();
            list_assign.add(statement);
            return list_assign;
        }
        else if (node instanceof While){
            WhileStatement statement = asn.newWhileStatement();
            statement.setExpression(MapPyExpressionsJDK.mapExpression((expr) ((While) node).getTest(),asn,import_nodes,0,typeNodes,pyc));
            AstList body = (AstList) ((While) node).getBody();
            statement.setBody(asn.newBlock());
            for (Object o : body) {
                for (Object stmt : getMappingPyNode(asn, (PythonTree) o, import_nodes, 0, pyc)) {
                    ((Block)statement.getBody()).statements().add(stmt);
                }
            }
            ArrayList<WhileStatement> list_assign = new ArrayList<>();
            list_assign.add(statement);
            return list_assign;

        }
        else if (node instanceof Global){
            for (Name name :((Global) node).getInternalNameNodes()){


                VariableDeclarationFragment declarationFragment = asn.newVariableDeclarationFragment();
                declarationFragment.setPyObject(name);
                declarationFragment.setName(asn.newSimpleName(name.getInternalId()));
                FieldDeclaration declaration = asn.newFieldDeclaration(declarationFragment);

                String typeString = this.typeNodes.get(new TypeASTNode(name.getLineno(),
                        name.getCol_offset(), name.getInternalId(), null));

                Type jdtType = TypeStringToJDT.getJDTType(asn, typeString, 0);
                if (jdtType!=null){
                    declaration.setType(jdtType);
                }
                pyc.setGlobal_variables(declaration);
            }
            return new ArrayList<>();
        }
        else if (node instanceof ErrorStmt)
        {
            return new ArrayList<>();
        }
        else if (node instanceof Assert){
            AssertStatement assertStatement = asn.newAssertStatement();

            assertStatement.setExpression(MapPyExpressionsJDK.mapExpression((expr) ((Assert) node).getTest(),asn,import_nodes,0,typeNodes,pyc));
            ArrayList<AssertStatement> list_assign = new ArrayList<>();
            list_assign.add(assertStatement);
            return list_assign;
        }
        else {

            logger.fatal("Corresponding Python node is not found : found "+node.getClass()+" Class "+node.toString());
            assert false;
            return new ArrayList<>();
        }

    }

    private Set<TypeDecNeeds> getVariabelNeedsDecleration (PythonTree node) throws Exception {
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
