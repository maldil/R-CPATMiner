package python3;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PyErrorExpression;
import org.eclipse.jdt.core.dom.PyInExpression;
import org.eclipse.jdt.core.dom.PyNonLocalStatement;
import org.eclipse.jdt.core.dom.PyTupleExpression;
import org.eclipse.jdt.core.dom.PyWithStatement;
import org.eclipse.jdt.core.dom.PyYieldReturnStatement;
import org.eclipse.jdt.core.dom.QualifiedName;
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
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.internal.compiler.ast.YieldReturnStatement;
import org.jpp.astnodes.PythonErrorNode;
import org.jpp.astnodes.PythonTree;
import org.jpp.astnodes.ast.Assert;
import org.jpp.astnodes.ast.Assign;
import org.jpp.astnodes.ast.AsyncFunctionDef;
import org.jpp.astnodes.ast.AsyncWith;
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
import org.jpp.astnodes.ast.Nonlocal;
import org.jpp.astnodes.ast.Pass;
import org.jpp.astnodes.ast.Raise;
import org.jpp.astnodes.ast.Return;
import org.jpp.astnodes.ast.Str;
import org.jpp.astnodes.ast.TryExcept;
import org.jpp.astnodes.ast.TryFinally;
import org.jpp.astnodes.ast.Tuple;
import org.jpp.astnodes.ast.While;
import org.jpp.astnodes.ast.With;
import org.jpp.astnodes.ast.Yield;
import org.jpp.astnodes.ast.alias;
import org.jpp.astnodes.ast.arg;
import org.jpp.astnodes.ast.arguments;
import org.jpp.astnodes.ast.operatorType;
import org.jpp.astnodes.ast.withitem;
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
import java.util.Arrays;
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
                    import_dec.setasName( asn.newName(MapPyExpressionsJDK.mapPythonKeyWords(asname.asString())));
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
                String[] import_name;
                if (((ImportFrom) node).getModule().toString().equals("")){
                    import_name = ((alias) pyObject).getName().toString().split("\\.");
                }
                else{
                    import_name = (String[]) ArrayUtils.addAll(module,((alias) pyObject).getName().toString().split("\\."));
                }
                List<String> import_name_clean = Arrays.stream(import_name).filter(x-> !x.equals("*")).collect(Collectors.toList());
                import_name_clean = import_name_clean.stream().map(x->MapPyExpressionsJDK.mapPythonKeyWords(x)).collect(Collectors.toList());
                String[] cleaned_import_names = new String[import_name_clean.size()];
                import_name_clean.toArray(cleaned_import_names);



                org.eclipse.jdt.core.dom.Name name = asn.newName(cleaned_import_names);
                import_dec.setName(name);
                PyObject asname = ((alias) pyObject).getAsname();
                if (!(asname instanceof PyNone)){
                    import_dec.setasName(asn.newName(MapPyExpressionsJDK.mapPythonKeyWords(asname.asString())  ));
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
                if (className instanceof MethodInvocation){
                    SimpleType simpleType = asn.newSimpleType((org.eclipse.jdt.core.dom.Name)
                            ASTNode.copySubtree(asn,((MethodInvocation) className).getName()));
                    classdec.setSuperclassType(simpleType);
                }
                else if (className instanceof ArrayAccess){
                    classdec.setSuperclassType(asn.newSimpleType((org.eclipse.jdt.core.dom.Name)
                            ASTNode.copySubtree(asn,((ArrayAccess) className).getArray())));
                }
                else {

                    SimpleType simpleType = asn.newSimpleType((org.eclipse.jdt.core.dom.Name) className);
                    classdec.setSuperclassType(simpleType); //TODO handle multiple inheritance
                }
            }
            else
                start_char_pos+=2+2;//{,\n , space, space

            MethodDeclaration methodDeclaration = null;
            int number_of_dummy_methods = 0;
            for (Object ch : (AstList)((ClassDef) node).getBody()){
                for (Object o : getMappingPyNode(asn, (PythonTree) ch,import_nodes,start_char_pos,pyc )) {

                    if (o instanceof BodyDeclaration){
                        if (methodDeclaration!=null){
                            classdec.bodyDeclarations().add(methodDeclaration);
                            methodDeclaration=null;

                        }
                        classdec.bodyDeclarations().add(o);
                        start_char_pos+=o.toString().length()+2+2 +2*(o.toString().lines().count()-1) ; //space,space,two spaces in parameters
                    }
                    else if (o instanceof ImportDeclaration){
                        pyc.imports().add(o);
                    }
                    else {
                        if (methodDeclaration==null){
                            number_of_dummy_methods++;
                            methodDeclaration = asn.newMethodDeclaration();
                            methodDeclaration.setBody(asn.newBlock());
                            methodDeclaration.setName(asn.newSimpleName(((ClassDef) node).getName().toString()+"_"+number_of_dummy_methods));
                        }
                        if (!((o instanceof ExpressionStatement && ((ExpressionStatement) o).getExpression() instanceof StringLiteral)||
                                o instanceof EmptyStatement ||
                                ( o instanceof ExpressionStatement && ((ExpressionStatement) o).getExpression() instanceof SimpleName) ||
                                ( o instanceof ExpressionStatement && ((ExpressionStatement) o).getExpression() instanceof ArrayAccess) ||
                                ( o instanceof ExpressionStatement && ((ExpressionStatement) o).getExpression() instanceof QualifiedName)||
                                ( o instanceof ExpressionStatement && ((ExpressionStatement) o).getExpression() instanceof PyTupleExpression) ||
                                ( o instanceof ExpressionStatement && ((ExpressionStatement) o).getExpression() instanceof PyInExpression)
                        )){
                            methodDeclaration.getBody().statements().add(o);
                        }
                    }

                }

            }
            if (methodDeclaration!=null){
                classdec.bodyDeclarations().add(methodDeclaration);
            }
            classdec.setSourceRange(startChar,classdec.toString().length());
            list_class.add(classdec);
            return list_class;
        }
        else if (node instanceof FunctionDef){
            int start_char_pos =startChar;

            MethodDeclaration methoddec = getMethodDeclaration(asn, node, import_nodes, startChar, pyc, start_char_pos);

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

                SingleVariableDeclaration parameter = updateForLoopLocalVariables(asn, (Name) ((For) node).getTarget());
                forstmt.setParameter(parameter_dummy);
                forstmt.Parameters().add(parameter);
            }
            else if (((For) node).getTarget() instanceof Tuple){
                SingleVariableDeclaration parameter_dummy = asn.newSingleVariableDeclaration();
                parameter_dummy.setName(asn.newSimpleName( "DummyTerminalNode"));
                parameter_dummy.setType(asn.newSimpleType(asn.newName("DummyTerminalTypeNode")));
                forstmt.setParameter(parameter_dummy);
                for (Object elt : (AstList) ((Tuple) ((For) node).getTarget()).getElts()) {
                    if (elt instanceof Tuple){
                        for (Object elt1 : (AstList) ((Tuple) elt).getElts()) {
                            if (elt1 instanceof Tuple){
                                for (Object elt2 : (AstList) ((Tuple) elt1).getElts()) {
                                    SingleVariableDeclaration lo_parameter = updateForLoopLocalVariables(asn, (Name) elt2);
                                    forstmt.Parameters().add(lo_parameter);
                                }
                            }
                            else{
                                SingleVariableDeclaration lo_parameter = updateForLoopLocalVariables(asn, (Name) elt1);
                                forstmt.Parameters().add(lo_parameter);
                            }
                        }
                    }
                    else{
                        SingleVariableDeclaration lo_parameter = updateForLoopLocalVariables(asn, (Name) elt);
                        forstmt.Parameters().add(lo_parameter);
                    }

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
                    if (o instanceof MethodDeclaration) {
                        TypeDeclaration typeDec = asn.newTypeDeclaration();
                        typeDec.setName((SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName()));
                        typeDec.bodyDeclarations().add(o);
                        TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                        ((Block)forstmt.getBody()).statements().add(dummyClass);
                    }
                    else if (o instanceof TypeDeclaration ){
                        TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                        ((Block)forstmt.getBody()).statements().add(dummyClass);
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                        continue;
                    }
                    else if (o instanceof ImportDeclaration ){
                        pyc.imports().add(o);
                    }
                    else {
                        ((Block)forstmt.getBody()).statements().add(o);
                    }
                }
                forstmt.getBody().setSourceRange(start_of_for_loop, node.toString().length());
            }
            for (Object ch : (AstList)((For) node).getOrelse()){
                for (Object o : getMappingPyNode(asn, (PythonTree) ch,import_nodes,0, pyc)) {
                    if (forstmt.getElseBody() ==null){
                        forstmt.setElseBody(asn.newBlock());
                    }
                    if (o instanceof MethodDeclaration) {
                        TypeDeclaration typeDec = asn.newTypeDeclaration();
                        typeDec.setName((SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName()));
                        typeDec.bodyDeclarations().add(o);
                        TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                        ((Block)forstmt.getElseBody()).statements().add(dummyClass);
                    }
                    else if (o instanceof TypeDeclaration ){
                        TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                        ((Block)forstmt.getElseBody()).statements().add(dummyClass);
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                        continue;
                    }
                    else {
                        ((Block)forstmt.getElseBody()).statements().add(o);
                    }
                }
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
            else if (((AugAssign) node).getInternalOp() == operatorType.Pow){
                assign.setOperator(Assignment.Operator.POWER_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp() == operatorType.FloorDiv){//TODO solve issues/9
                assign.setOperator(Assignment.Operator.DIVIDE_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp() == operatorType.Mod){//TODO solve issues/9
                assign.setOperator(Assignment.Operator.REMAINDER_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp() == operatorType.BitXor){//TODO solve issues/9
                assign.setOperator(Assignment.Operator.BIT_XOR_ASSIGN);
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
            else if (((Expr) node).getValue() instanceof Yield){
                PyYieldReturnStatement yieldReturnStatement = asn.newPyYieldReturnStatement();
                if ( ((Yield) ((Expr) node).getValue()).getInternalValue()==null){
//                    yieldReturnStatement.setExpression(asn.newSimpleName("PyCpatDummy"));
                }
                else{
                    yieldReturnStatement.setExpression(MapPyExpressionsJDK.mapExpression(((Yield) ((Expr) node).getValue()).getInternalValue(),asn,import_nodes,0,typeNodes,pyc));

                }
                ArrayList<PyYieldReturnStatement> list_assign = new ArrayList<>();
                list_assign.add(yieldReturnStatement);
                return list_assign ;
            }

            Expression exp  = MapPyExpressionsJDK.mapExpression((expr)((Expr) node).getValue(),asn, import_nodes,0,typeNodes,pyc );



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
                                if (o instanceof MethodDeclaration) {
                                    TypeDeclaration typeDec = asn.newTypeDeclaration();
                                    typeDec.setName((SimpleName) ASTNode.copySubtree(asn,((MethodDeclaration)o).getName()));
                                    typeDec.bodyDeclarations().add(o);
                                    TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                                    ((Block)ifStatement.getThenStatement()).statements().add(dummyClass);
                                }
                                else if (o instanceof TypeDeclaration ){
                                    TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                                    ((Block)ifStatement.getThenStatement()).statements().add(dummyClass);
                                }
                                else if (o instanceof ImportDeclaration){
                                    pyc.imports().add(o);
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                                    continue;
                                }
                                else{
                                    ((Block)ifStatement.getThenStatement()).statements().add(o);
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
                                    for (Object o : getMappingPyNode(asn,
                                            (PythonTree) ob, import_nodes, 0, pyc)) {
                                        if (o instanceof MethodDeclaration) {
                                            TypeDeclaration typeDec = asn.newTypeDeclaration();
                                            typeDec.setName((SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName()));
                                            typeDec.bodyDeclarations().add(o);
                                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                                            ((Block)ifStatement.getElseStatement()).statements().add(dummyClass);
                                        }
                                        else if (o instanceof TypeDeclaration ){
                                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                                            ((Block)ifStatement.getElseStatement()).statements().add(dummyClass);
                                        }
                                        else if (o instanceof ImportDeclaration){
                                            pyc.imports().add(o);
                                        }
                                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                                            continue;
                                        }
                                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                                            continue;
                                        }
                                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                                            continue;
                                        }
                                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                                            continue;
                                        }
                                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                                            continue;
                                        }
                                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                                            continue;
                                        }
                                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                                            continue;
                                        }
                                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                                            continue;
                                        }
                                        else{
                                            ((Block)ifStatement.getElseStatement()).statements().add(o);
                                        }
                                    }
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
            if ( ((Raise) node).getExc()==null){
                ClassInstanceCreation classInstanceCreation = asn.newClassInstanceCreation();
                classInstanceCreation.setType(asn.newSimpleType(asn.newSimpleName("PythonException")));
                throwStatement.setExpression(classInstanceCreation);
            }else{
                Expression expression = MapPyExpressionsJDK.mapExpression((expr) ((Raise) node).getExc(), asn, import_nodes, 0, typeNodes, pyc);
                throwStatement.setExpression(expression);
            }

//
//            ClassInstanceCreation classInstanceCreation = asn.newClassInstanceCreation();
//            if (((Raise) node).getExc() instanceof Call){
//                Expression throwName = MapPyExpressionsJDK.mapExpression((expr) ((Call) ((Raise) node).getExc()).getFunc(), asn, import_nodes, 0, typeNodes, pyc);
//                if (throwName instanceof MethodInvocation){
//                    classInstanceCreation.setType(asn.newSimpleType((org.eclipse.jdt.core.dom.Name) ASTNode.copySubtree(asn,((MethodInvocation) throwName).getName())));
//                    ((MethodInvocation)throwName).arguments().stream().forEach(x->classInstanceCreation.arguments().add(ASTNode.copySubtree(asn, (ASTNode) x)));
//                }
//                else{
//
//                    classInstanceCreation.setType(asn.newSimpleType((org.eclipse.jdt.core.dom.Name) throwName));
//                    ((AstList)((Call) ((Raise) node).getExc()).getArgs()).stream().forEach(o->
//                    {
//                        try {
//                            Expression expression = MapPyExpressionsJDK.mapExpression((expr) o, asn, import_nodes, 0, typeNodes, pyc);
//                            classInstanceCreation.arguments().add(expression);
//                        } catch (ExpressionNotFound expressionNotFound) {
//                            expressionNotFound.printStackTrace();
//                        } catch (NodeNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                    });
//                }
//            }
//            else{
//                classInstanceCreation.setType(asn.newSimpleType(asn.newSimpleName("PythonException")));
//                logger.info("PythonException was assigned to empty raise statment");
//            }
//            throwStatement.setExpression(classInstanceCreation);
            ArrayList<ThrowStatement> list_assign = new ArrayList<>();
            list_assign.add(throwStatement);
            return list_assign;
        }
        else if (node instanceof TryExcept){
            TryStatement tryStatement = asn.newTryStatement();
            Block block = asn.newBlock();
            ((AstList)((TryExcept) node).getBody()).stream().forEach(x-> {
                try {
                    for (Object o : getMappingPyNode(asn, (PythonTree) x, import_nodes, 0, pyc)) {
                        if (o instanceof MethodDeclaration) {
                            TypeDeclaration typeDec = asn.newTypeDeclaration();
                            typeDec.setName((SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName()));
                            typeDec.bodyDeclarations().add(o);
                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                            block.statements().add(dummyClass);
                        }
                        else if (o instanceof TypeDeclaration ){
                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                            block.statements().add(dummyClass);
                        }
                        else if (o instanceof ImportDeclaration ) {
                            pyc.imports().add(o);
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                            continue;
                        }
                        else{
                            block.statements().add(o);
                        }
                    }
//                    ArrayList<?> mappingPyNode = getMappingPyNode(asn, (PythonTree) x, import_nodes, 0, pyc);
//                    block.statements().addAll(mappingPyNode.stream().filter(Predicate.not(z->z instanceof ImportDeclaration)).collect(Collectors.toList()));
//                    pyc.imports().addAll(mappingPyNode.stream().filter(z->z instanceof ImportDeclaration).collect(Collectors.toList()));

                } catch (NodeNotFoundException e) {
                    e.printStackTrace();
                } catch (ExpressionNotFound expressionNotFound) {
                    expressionNotFound.printStackTrace();
                }
            });

            ((AstList)((TryExcept) node).getHandlers()).stream().forEach(x-> {
                try {
                    for (Object o : getMappingPyNode(asn, (PythonTree) x, import_nodes, 0, pyc)) {
                        if (o instanceof MethodDeclaration) {
                            TypeDeclaration typeDec = asn.newTypeDeclaration();
                            typeDec.setName((SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName()));
                            typeDec.bodyDeclarations().add(o);
                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                            tryStatement.catchClauses().add(dummyClass);
                        }
                        else if (o instanceof TypeDeclaration ){
                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                            tryStatement.catchClauses().add(dummyClass);
                        }
                        else if (o instanceof ImportDeclaration ) {
                            pyc.imports().add(o);
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                            continue;
                        }
                        else{
                            tryStatement.catchClauses().add(o);
                        }
                    }

                } catch (NodeNotFoundException e) {
                    e.printStackTrace();
                } catch (ExpressionNotFound expressionNotFound) {
                    expressionNotFound.printStackTrace();
                }
            });
            if (((TryExcept) node).getInternalOrelse().size()>0){
                tryStatement.setElse(asn.newBlock());
            }

            (((TryExcept) node).getInternalOrelse()).stream().forEach(x->
                    {
                        try {
                            for (Object o : getMappingPyNode(asn, x, import_nodes, 0, pyc)) {
                                if (o instanceof MethodDeclaration) {
                                    TypeDeclaration typeDec = asn.newTypeDeclaration();
                                    typeDec.setName((SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName()));
                                    typeDec.bodyDeclarations().add(o);
                                    TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                                    tryStatement.getElse().statements().add(dummyClass);
                                }
                                else if (o instanceof TypeDeclaration ){
                                    TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                                    tryStatement.getElse().statements().add(dummyClass);
                                }
                                else if (o instanceof ImportDeclaration ) {
                                    pyc.imports().add(o);
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                                    continue;
                                }
                                else{
                                    tryStatement.getElse().statements().add(o);
                                }
                            }
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
                    getMappingPyNode(asn, (PythonTree) x, import_nodes, 0, pyc).stream().filter
                            (Predicate.not(z->z instanceof ImportDeclaration)).collect(Collectors.toList()).forEach(state->{
                        if (state instanceof MethodDeclaration) {
                            TypeDeclaration typeDec = asn.newTypeDeclaration();
                            typeDec.setName((SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) state).getName()));
                            typeDec.bodyDeclarations().add(state);
                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                            block.statements().add(dummyClass);
                        }
                        else if (state instanceof TypeDeclaration ){
                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) state);
                            block.statements().add(dummyClass);
                        }
                        else if (state instanceof ImportDeclaration ) {
                            pyc.imports().add(state);
                        }
                        else if (state instanceof ExpressionStatement && ((ExpressionStatement)state).getExpression() instanceof QualifiedName){ }
                        else if (state instanceof ExpressionStatement && ((ExpressionStatement)state).getExpression() instanceof SimpleName){ }
                        else if (state instanceof ExpressionStatement && ((ExpressionStatement)state).getExpression() instanceof PyTupleExpression){ }
                        else if (state instanceof ExpressionStatement && ((ExpressionStatement)state).getExpression() instanceof ParenthesizedExpression){ }
                        else if (state instanceof ExpressionStatement && ((ExpressionStatement)state).getExpression() instanceof ArrayAccess){ }
                        else if (state instanceof ExpressionStatement && ((ExpressionStatement)state).getExpression() instanceof StringLiteral){ }
                        else{
                            block.statements().add(state);
                        }
                    });
                } catch (NodeNotFoundException e) {
                    e.printStackTrace();
                } catch (ExpressionNotFound expressionNotFound) {
                    expressionNotFound.printStackTrace();
                }
            });
            SingleVariableDeclaration singleVarDec = asn.newSingleVariableDeclaration();
            if (((ExceptHandler) node).getExceptType()!=null){
                Expression expression = MapPyExpressionsJDK.mapExpression((expr) ((ExceptHandler) node).getExceptType(), asn, import_nodes, 0, typeNodes, pyc);
                if (expression instanceof  org.eclipse.jdt.core.dom.PyTupleExpression){
                    UnionType unionType = asn.newUnionType();
                    ((PyTupleExpression)expression).expressions().stream().forEach(x->unionType.types().add(asn.newSimpleType((org.eclipse.jdt.core.dom.Name) ASTNode.copySubtree(asn, (ASTNode) x))));
                    singleVarDec.setType(unionType);
                }
                else{
                    singleVarDec.setType(asn.newSimpleType((org.eclipse.jdt.core.dom.Name) expression));
                }
            }
            else{
                SimpleType type = asn.newSimpleType(asn.newSimpleName("PyCpatDummy"));
                singleVarDec.setType(type);
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
            PyWithStatement pyWithStatement = getPyWithStatement(asn, (With) node, import_nodes, pyc);
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
            for (Object ob : body) {
                for (Object stmt : getMappingPyNode(asn, (PythonTree) ob, import_nodes, 0, pyc)) {
                    if (stmt instanceof MethodDeclaration) {
                        TypeDeclaration typeDec = asn.newTypeDeclaration();
                        typeDec.setName((SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) stmt).getName()));
                        typeDec.bodyDeclarations().add(stmt);
                        TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                        ((Block)statement.getBody()).statements().add(dummyClass);
                    }
                    else if (stmt instanceof TypeDeclaration ){
                        TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) stmt);
                        ((Block)statement.getBody()).statements().add(dummyClass);
                    }
                    else if (stmt instanceof ImportDeclaration ) {
                        pyc.imports().add(stmt);
                    }
                    else if (stmt instanceof ExpressionStatement && ((ExpressionStatement)stmt).getExpression() instanceof QualifiedName){
                        continue;
                    }
                    else if (stmt instanceof ExpressionStatement && ((ExpressionStatement)stmt).getExpression() instanceof SimpleName){
                        continue;
                    }
                    else if (stmt instanceof ExpressionStatement && ((ExpressionStatement)stmt).getExpression() instanceof PyTupleExpression){
                        continue;
                    }
                    else if (stmt instanceof ExpressionStatement && ((ExpressionStatement)stmt).getExpression() instanceof ParenthesizedExpression){
                        continue;
                    }
                    else {
                        ((Block)statement.getBody()).statements().add(stmt);
                    }
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
            Expression expression1 = MapPyExpressionsJDK.mapExpression((expr) ((Assert) node).getTest(), asn, import_nodes, 0, typeNodes, pyc);
            if (!(expression1 instanceof PyErrorExpression)){
                assertStatement.setExpression(expression1);
            }
            else
            {
                assertStatement.setExpression(asn.newSimpleName("PyCpatDummy"));
            }
            if (((Assert) node).getInternalMsg()!=null){
                Expression expression = MapPyExpressionsJDK.mapExpression(((Assert) node).getInternalMsg(), asn, import_nodes, 0, typeNodes, pyc);
                if (!(expression instanceof PyErrorExpression))
                    assertStatement.setMessage(expression);

            }
            ArrayList<AssertStatement> list_assign = new ArrayList<>();
            list_assign.add(assertStatement);
            return list_assign;
        }
        else if (node instanceof TryFinally){
            TryStatement tryStatement = asn.newTryStatement();
            if (((TryFinally) node).getInternalBody().size()==1 &&
                    getMappingPyNode(asn,((TryFinally) node).getInternalBody().get(0), import_nodes, 0, pyc).get(0) instanceof TryStatement){
                TryStatement trystmt = (TryStatement)getMappingPyNode(asn,((TryFinally) node).getInternalBody().get(0), import_nodes, 0, pyc).get(0);
                for (Object clause : trystmt.catchClauses()) {
                    tryStatement.catchClauses().add(ASTNode.copySubtree(asn, (ASTNode) clause));
                }
                tryStatement.setBody((Block) ASTNode.copySubtree(asn,  trystmt.getBody()));
                tryStatement.setElse((Block) ASTNode.copySubtree(asn,  trystmt.getElse()));
            }
            else{
                Block block = asn.newBlock();
                ((AstList)((TryFinally) node).getBody()).stream().forEach(x-> {
                    try {
                        for (Object o : getMappingPyNode(asn, (PythonTree) x, import_nodes, 0, pyc)) {
                            if (o instanceof MethodDeclaration) {
                                TypeDeclaration typeDec = asn.newTypeDeclaration();
                                typeDec.setName((SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName()));
                                typeDec.bodyDeclarations().add(o);
                                TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                                block.statements().add(dummyClass);
                            }
                            else if (o instanceof TypeDeclaration ){
                                TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                                block.statements().add(dummyClass);
                            }
                            else if (o instanceof ImportDeclaration ) {
                                pyc.imports().add(o);
                            }
                            else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                                continue;
                            }
                            else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                                continue;
                            }
                            else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                                continue;
                            }
                            else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                                continue;
                            }
                            else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                                continue;
                            }
                            else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                                continue;
                            }
                            else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                                continue;
                            }
                            else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                                continue;
                            }
                            else {
                                block.statements().add(o);
                            }
                        }
                    } catch (NodeNotFoundException e) {
                        e.printStackTrace();
                    } catch (ExpressionNotFound expressionNotFound) {
                        expressionNotFound.printStackTrace();
                    }
                });
                tryStatement.setBody(block);
            }

            Block final_block = asn.newBlock();
            (((TryFinally) node).getInternalFinalbody()).stream().forEach(x-> {

                try {
                    for (Object o : getMappingPyNode(asn, x, import_nodes, 0, pyc)) {
                        if (o instanceof MethodDeclaration) {
                            TypeDeclaration typeDec = asn.newTypeDeclaration();
                            typeDec.setName((SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName()));
                            typeDec.bodyDeclarations().add(o);
                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                            final_block.statements().add(dummyClass);
                        }
                        else if (o instanceof TypeDeclaration ){
                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                            final_block.statements().add(dummyClass);
                        }
                        else if (o instanceof ImportDeclaration ) {
                            pyc.imports().add(o);
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                            continue;
                        }
                        else{
                            final_block.statements().add(o);
                        }
                    }
                } catch (NodeNotFoundException e) {
                    e.printStackTrace();
                } catch (ExpressionNotFound expressionNotFound) {
                    expressionNotFound.printStackTrace();
                }
            });
            tryStatement.setFinally(final_block);


//            if (((TryFinally) node).getInternalBody().size()>1){
//                logger.fatal("Size of body of try final is larger than one");
//                assert false;
//            }
//            if (((TryFinally) node).getInternalBody().get(0) instanceof TryExcept){
//                ArrayList<?> mappingPyNode = getMappingPyNode(asn, ((TryFinally) node).getInternalBody().get(0), import_nodes, 0, pyc);
//                TryStatement except =(TryStatement)mappingPyNode.get(0);
//
//                Block finalBlock = asn.newBlock();
//                ((TryFinally) node).getInternalFinalbody().stream().forEach(x-> {
//                    try {
//                        finalBlock.statements().addAll(getMappingPyNode(asn,x,import_nodes,0,pyc));
//                    } catch (NodeNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (ExpressionNotFound expressionNotFound) {
//                        expressionNotFound.printStackTrace();
//                    }
//                });
//                except.setFinally(finalBlock);
//                ArrayList<TryStatement> list_assign = new ArrayList<>();
//                list_assign.add(except);
//                return list_assign;
//            }
//            else{
//                logger.fatal("Type of the try final body is not try except");
//                assert false;
//                return new ArrayList<>();
//            }
            ArrayList<TryStatement> list_assign = new ArrayList<>();
            list_assign.add(tryStatement);
            return list_assign;

        }

        else if (node instanceof Nonlocal){
            ArrayList<PyNonLocalStatement> list_assign = new ArrayList<>();
            for (Name nameNode : ((Nonlocal) node).getInternalNameNodes()) {
                PyNonLocalStatement nonlocal = asn.newNonLocal();
                nonlocal.setExpression(MapPyExpressionsJDK.mapExpression(nameNode,asn,import_nodes,0,typeNodes,pyc));
                list_assign.add(nonlocal);
            }
            return list_assign;
        }
        else if (node instanceof AsyncFunctionDef){
            MethodDeclaration methoddec = getMethodDeclaration(asn, node, import_nodes, startChar, pyc, 0);
            ArrayList<MethodDeclaration> list_method = new ArrayList<>();
            list_method.add(methoddec);
            return list_method;
        }
        else if (node instanceof AsyncWith){
            PyWithStatement pyWithStatement = getPyWithStatement(asn,  (AsyncWith)node, import_nodes, pyc);
            ArrayList<PyWithStatement> list_assign = new ArrayList<>();
            list_assign.add(pyWithStatement);
            return list_assign;
        }
        else {

            logger.fatal("Corresponding Python node is not found : found "+node.getClass()+" Class "+node.toString());
            assert false;
            return new ArrayList<>();
        }

    }


    private PyWithStatement getPyWithStatement(AST asn, stmt node, HashMap<String, org.eclipse.jdt.core.dom.Name> import_nodes, PyCompilationUnit pyc) throws ExpressionNotFound, NodeNotFoundException {
        PyWithStatement pyWithStatement = asn.newPyWithStatement();

        List<withitem> internalItems = node instanceof With ? ((With) node).getInternalItems(): ((AsyncWith) node).getInternalItems();

        if (internalItems.size() > 1) {
            logger.error("With item contain more than one item in the header");
        }
        expr internalContext_expr = internalItems.get(0).getInternalContext_expr();
        Expression expression = MapPyExpressionsJDK.mapExpression(internalContext_expr, asn, import_nodes, 0, typeNodes, pyc);
        if (expression instanceof PyErrorExpression){
            expression =  asn.newSimpleName("PyCpatDummy");
        }

        pyWithStatement.setExpression(expression);
        expr optional_vars = internalItems.get(0).getInternalOptional_vars();

        Block block = asn.newBlock();
        if (optional_vars != null && optional_vars instanceof Name) {
            String typeString = this.typeNodes.get(new TypeASTNode(((Name) optional_vars).getLineno(), ((Name) optional_vars).getCol_offset(), ((Name) optional_vars).getInternalId(), null));
            VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, ((Name) optional_vars).getInternalId(), typeString, 0);
            block.statements().add(varDecStat);
        }
        List<stmt> internalBody = node instanceof With ? ((With) node).getInternalBody():((AsyncWith) node).getInternalBody();
        internalBody.stream().forEach(x -> {
            try {
                for (Object o : getMappingPyNode(asn, x, import_nodes, 0, pyc)) {

                    if (o instanceof MethodDeclaration) {
                        TypeDeclaration typeDec = asn.newTypeDeclaration();
                        typeDec.setName((SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName()));
                        typeDec.bodyDeclarations().add(o);
                        TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                        block.statements().add(dummyClass);
                    }
                    else if (o instanceof TypeDeclaration ){
                        TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                        block.statements().add(dummyClass);
                    }
                    else if (o instanceof ImportDeclaration ) {
                        pyc.imports().add(o);
                    }
                    else if (o instanceof PythonErrorNode){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                        continue;
                    }
                    else{
                        block.statements().add(o);
                    }
                }
            } catch (NodeNotFoundException e) {
                e.printStackTrace();
            } catch (ExpressionNotFound expressionNotFound) {
                expressionNotFound.printStackTrace();
            }
        });
        pyWithStatement.setBody(block);
        return pyWithStatement;
    }


    private MethodDeclaration getMethodDeclaration(AST asn, PythonTree node, HashMap<String, org.eclipse.jdt.core.dom.Name> import_nodes, int startChar, PyCompilationUnit pyc, int start_char_pos) throws NodeNotFoundException, ExpressionNotFound {
        Set<TypeDecNeeds> variableNeedsDeclaration=null;
        try {
            variableNeedsDeclaration=getVariabelNeedsDecleration(node);
        } catch (Exception e) {
            logger.error(e);
        }
        MethodDeclaration methoddec = asn.newMethodDeclaration();
        String name = node instanceof FunctionDef ? ((FunctionDef) node).getInternalName() : ((AsyncFunctionDef) node).getInternalName();

        SimpleName methodName = asn.newSimpleName(MapPyExpressionsJDK.mapPythonKeyWords(name));
        start_char_pos +=5; //length of void + two spaces
        int method_name_l = methodName.toString().length();
        methodName.setSourceRange(start_char_pos,method_name_l);
        methoddec.setName(methodName);
        start_char_pos +=1+method_name_l; //(
        //We assign only the parameter self. Type of other parameters are assigned
        int numb_args=0;
        arguments args = (arguments) (node instanceof FunctionDef ? ((FunctionDef) node).getArgs() : ((AsyncFunctionDef) node).getArgs());

        for (Object arg : (AstList) args.getArgs()) {
            if (((arg) arg).getArg().toString().equals("self") && node.getParent() instanceof ClassDef){
                numb_args+=1;
                if (numb_args==1)
                    start_char_pos +=2; //two spaces
                else
                    start_char_pos +=3;
                SingleVariableDeclaration parameter = asn.newSingleVariableDeclaration();

                SimpleType simpleType = asn.newSimpleType(asn.newName(((ClassDef) node.getParent()).getInternalName()));
                int type_length = simpleType.toString().length();
                simpleType.setSourceRange(start_char_pos,type_length);

                SimpleName simpleName = asn.newSimpleName(((arg) arg).getArg().toString());
                simpleName.setSourceRange(start_char_pos +type_length+1,simpleName.toString().length());
                parameter.setName(simpleName);
//TODO uncomment above
                parameter.setType(simpleType);
                parameter.setSourceRange(start_char_pos,parameter.toString().length());
                methoddec.parameters().add(parameter);
                start_char_pos +=type_length+4+1;// "self", space, type length,  comma
            }

        }
        start_char_pos +=3; // ),{ newline


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
        AstList body = node instanceof FunctionDef? (AstList)((FunctionDef) node).getBody(): (AstList)((AsyncFunctionDef) node).getBody();
        for (Object ch : body){
            start_char_pos +=2*number_of_par;
            if (ch instanceof Expr && ((Expr) ch).getValue() instanceof Str){
                continue;  //remove comments
            }

            for (Object o : getMappingPyNode(asn, (PythonTree) ch, import_nodes, start_char_pos, pyc)) {
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
                    else if (o instanceof TypeDeclaration ){
                        TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                        methoddec.getBody().statements().add(dummyClass);
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                        continue;
                    }

                    else {
                        methoddec.getBody().statements().add(o);
                    }
                }

                start_char_pos +=length;
            }
        }
        methoddec.setSourceRange(startChar, methoddec.toString().length());
        return methoddec;
    }

    private SingleVariableDeclaration updateForLoopLocalVariables(AST asn, Name elt) throws NodeNotFoundException {
        SingleVariableDeclaration lo_parameter = asn.newSingleVariableDeclaration();
        lo_parameter.setName(asn.newSimpleName(MapPyExpressionsJDK.mapPythonKeyWords(elt.getId().toString())));
        String typeString = this.typeNodes.get(new TypeASTNode(elt.getLineno(),
                elt.getCol_offset(), elt.getId().toString(), null));

        Type jdtType = TypeStringToJDT.getJDTType(asn, typeString, 0);
        if (jdtType != null) {
            lo_parameter.setType(jdtType);
        } else
            logger.error("Type of for loop variable is not updated");
        return lo_parameter;
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