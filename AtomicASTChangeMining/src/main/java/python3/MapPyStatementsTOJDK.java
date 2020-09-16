package python3;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Expression;
import org.jpp.astnodes.PythonTree;
import org.jpp.astnodes.ast.*;
import org.jpp.astnodes.ast.Name;
import org.jpp.astnodes.base.expr;
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
import java.util.Set;

public class MapPyStatementsTOJDK extends PyMap{
    static Logger logger = Logger.getLogger(MapPyStatementsTOJDK.class);
    private Map<TypeASTNode, String> typeNodes;
    private int augmentedChars=0;

    public MapPyStatementsTOJDK(Map<TypeASTNode, String> typeNodes) {
        this.typeNodes = typeNodes;
    }

    public ArrayList<?> getMappingPyNode(AST asn , PythonTree node, HashMap<String, org.eclipse.jdt.core.dom.Name> import_nodes) throws NodeNotFoundException, ExpressionNotFound {
        if (node instanceof Import){
            ArrayList<ImportDeclaration> list_imports = new ArrayList<>();
            for (PyObject pyObject : ((Import) node).getNames().asIterable()) {
                ImportDeclaration import_dec =  asn.newImportDeclaration();
                String[] import_name = ((alias) pyObject).getName().toString().split("\\.");
                import_dec.setName(asn.newName(import_name));
                PyObject asname = ((alias) pyObject).getAsname();
                if (!(asname instanceof PyNone)){
                    import_dec.setasName(asn.newName(asname.asString()));
                }
                list_imports.add(import_dec);
            }
            return list_imports;
        }
        else if (node instanceof ImportFrom){
            ArrayList<ImportDeclaration> list_imports = new ArrayList<>();
            String[] module = ((ImportFrom) node).getModule().toString().split("\\.");
            for (PyObject pyObject : ((ImportFrom) node).getNames().asIterable()) {
                ImportDeclaration import_dec =  asn.newImportDeclaration();
                String[] import_name = (String[]) ArrayUtils.addAll(module,((alias) pyObject).getName().toString().split("\\."));

                import_dec.setName(asn.newName(import_name));
                PyObject asname = ((alias) pyObject).getAsname();
                if (!(asname instanceof PyNone)){
                    import_dec.setasName(asn.newName(asname.asString()));
                }
                list_imports.add(import_dec);
            }

            return list_imports;
        }
        else if (node instanceof ClassDef){
            TypeDeclaration classdec = asn.newTypeDeclaration();
            int startCharPos = node.getCharStartIndex()+PyMap.totalCharGains;

            Modifier.ModifierKeyword keyword= new Modifier.ModifierKeyword("public",1);
            Modifier modifier = asn.newModifier(keyword);
            modifier.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,keyword.toString().length());
            PyMap.totalCharGains+=keyword.toString().length()+1;
            classdec.setModifier(modifier);



            ArrayList<TypeDeclaration> list_class = new ArrayList<>();
            SimpleName simpleName = asn.newSimpleName(((ClassDef) node).getName().toString());
            simpleName.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,((ClassDef) node).getName().toString().length());
            PyMap.totalCharGains+=((ClassDef) node).getName().toString().length();
            classdec.setName(simpleName);



            if (((AstList)((ClassDef) node).getBases()).size()>0){
                SimpleType simpleType = asn.newSimpleType(asn.newName(((Name) ((AstList) ((ClassDef) node).getBases()).get(0)).getInternalId()));
                int length_of_node = ((Name) ((AstList) ((ClassDef) node).getBases()).get(0)).getInternalId().length()+1;
                simpleType.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,length_of_node);
                PyMap.totalCharGains+=length_of_node;
                classdec.setSuperclassType(simpleType); //TODO handle multiple inheritance

            }

            for (Object ch : (AstList)((ClassDef) node).getBody()){
                for (Object o : getMappingPyNode(asn, (PythonTree) ch,import_nodes)) {
                    classdec.bodyDeclarations().add(o);
                }

            }
            classdec.setSourceRange(startCharPos,node.getCharStopIndex()-node.getCharStartIndex()+startCharPos-PyMap.totalCharGains);
            list_class.add(classdec);
            return list_class;
        }
        else if (node instanceof FunctionDef){
            PyMap.currentMethodGain=0;
            Set<TypeDecNeeds> variableNeedsDeclaration=null;
            try {
                variableNeedsDeclaration=getVariabelNeedsDecleration(node);
            } catch (Exception e) {
                logger.error(e);
            }
            MethodDeclaration methoddec = asn.newMethodDeclaration();
            if (variableNeedsDeclaration != null) {
                for (TypeDecNeeds typeDecNeeds : variableNeedsDeclaration) {
                    String typeString = this.typeNodes.get(new TypeASTNode(typeDecNeeds.getRow(), typeDecNeeds.getCol_offset(), typeDecNeeds.getName(), null));
                    if (typeString!=null) {
                        VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, typeDecNeeds, typeString);
                        if (methoddec.getBody() == null) {
                            methoddec.setBody(asn.newBlock());
                            methoddec.getBody().setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,node.getCharStopIndex()-node.getCharStopIndex());
                        }
                        varDecStat.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,varDecStat.toString().length());
                        PyMap.currentMethodGain+=varDecStat.toString().length();
                        PyMap.totalCharGains+=varDecStat.toString().length();
                        methoddec.getBody().statements().add(varDecStat);
                    }
                }
            }

            ArrayList<MethodDeclaration> list_method = new ArrayList<>();
            methoddec.setName(asn.newSimpleName(((FunctionDef) node).getName().toString())); //We assign only the parameter self. Type of other parameters are assigned
            for (Object arg : (AstList) ((arguments) ((FunctionDef) node).getArgs()).getArgs()) {
                if (((arg) arg).getArg().toString().equals("self") && node.getParent() instanceof ClassDef){
                    SingleVariableDeclaration parameter = asn.newSingleVariableDeclaration();
                    SimpleName simpleName = asn.newSimpleName(((arg) arg).getArg().toString());
                    simpleName.setSourceRange(((arg) arg).getCharStartIndex()+PyMap.totalCharGains,((arg) arg).getArg().toString().length());
                    parameter.setName(simpleName);
                    SimpleType simpleType = asn.newSimpleType(asn.newName(((ClassDef) node.getParent()).getInternalName()));
                    simpleType.setSourceRange(simpleName.getStartPosition()+1,((ClassDef) node.getParent()).getInternalName().length());

                    parameter.setType(simpleType);
                    parameter.setSourceRange(((arg) arg).getCharStartIndex()+PyMap.totalCharGains,((arg) arg).getCharStopIndex()-((arg) arg).getCharStartIndex()+
                            ((ClassDef) node.getParent()).getInternalName().length()+1);
                    PyMap.currentMethodGain += ((ClassDef) node.getParent()).getInternalName().length()+1; //Type of 'self' is the current class, we add charactor size of class name
                    PyMap.totalCharGains += ((ClassDef) node.getParent()).getInternalName().length()+1;
                    methoddec.parameters().add(parameter);
                }

            }
            for (Object ch : (AstList)((FunctionDef) node).getBody()){
                for (Object o : getMappingPyNode(asn, (PythonTree) ch,import_nodes)) {
                    if (methoddec.getBody() ==null){
                        methoddec.setBody(asn.newBlock());
                        methoddec.getBody().setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,node.getCharStopIndex()-node.getCharStopIndex());
                    }
                    methoddec.getBody().statements().add(o);
                }
            }
            methoddec.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains, node.getCharStopIndex()-node.getCharStartIndex()+PyMap.currentMethodGain);
            PyMap.totalCharGains+=PyMap.currentMethodGain;
//
            list_method.add(methoddec);
            return list_method;
        }
        else if (node instanceof Assign){
            Assignment assign = asn.newAssignment();
            assign.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,node.getCharStopIndex()-node.getCharStartIndex());
            ArrayList<ExpressionStatement> list_assign = new ArrayList<>();
            assign.setOperator(new Assignment.Operator("="));
            assert(((AstList)((Assign) node).getTargets()).size()>1);
            assign.setLeftHandSide(MapPyExpressionsJDK.mapExpression((expr) ((AstList)((Assign) node).getTargets()).get(0),asn, import_nodes));
            assign.setRightHandSide(MapPyExpressionsJDK.mapExpression((expr)((Assign) node).getValue(),asn, import_nodes));
            assign.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,node.getCharStopIndex()-node.getCharStartIndex());
            ExpressionStatement statement = asn.newExpressionStatement(assign);

            statement.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,node.getCharStopIndex()-node.getCharStartIndex()+1);
            PyMap.totalCharGains+=1;
            list_assign.add(statement);
            return list_assign;

        }
        else if (node instanceof For){
            EnhancedForStatement forstmt = asn.newEnhancedForStatement();
            SingleVariableDeclaration parameter = asn.newSingleVariableDeclaration();
            ArrayList<EnhancedForStatement> list_for = new ArrayList<>();
            parameter.setName(asn.newSimpleName(((Name)((expr)((For) node).getTarget())).getId().toString()));
            forstmt.setParameter(parameter);
            forstmt.setExpression(MapPyExpressionsJDK.mapExpression((expr)((For) node).getIter(),asn,import_nodes));
            ((For) node).getBody() ;

            for (Object ch : (AstList)((For) node).getBody()){
                for (Object o : getMappingPyNode(asn, (PythonTree) ch,import_nodes)) {
                    if (forstmt.getBody() ==null){
                        forstmt.setBody(asn.newBlock());
                    }
                    ((Block)forstmt.getBody()).statements().add(o);
                }
            }
            forstmt.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,node.getCharStopIndex()-node.getCharStartIndex());
            list_for.add(forstmt);
            return list_for;

        }
        else if (node instanceof AugAssign){
            Assignment assign = asn.newAssignment();
            ArrayList<ExpressionStatement> list_assign = new ArrayList<>();
            if (((AugAssign) node).getInternalOp().name()=="Add"){
                assign.setOperator(new Assignment.Operator("+="));
            }
            else {
                throw new NodeNotFoundException("Operator is not implemented :"+((AugAssign) node).getInternalOp().name());
            }

            assign.setLeftHandSide( MapPyExpressionsJDK.mapExpression((expr) ((AugAssign) node).getTarget(),asn, import_nodes));
            assign.setRightHandSide(MapPyExpressionsJDK.mapExpression((expr)((AugAssign) node).getValue(),asn, import_nodes));
            assign.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,node.getCharStopIndex()-node.getCharStopIndex());
            ExpressionStatement statement = asn.newExpressionStatement(assign);

            statement.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,node.getCharStopIndex()-node.getCharStopIndex()+1);
            PyMap.totalCharGains+=1;
            list_assign.add(statement);
            return list_assign;
        }
        else if (node instanceof Return){
            ReturnStatement statement = asn.newReturnStatement();
            ArrayList<ReturnStatement> list_assign = new ArrayList<>();
            statement.setExpression(MapPyExpressionsJDK.mapExpression((expr) ((Return) node).getValue(),asn, import_nodes));

            statement.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,node.getCharStopIndex()-node.getCharStartIndex());
            list_assign.add(statement);
            return list_assign;
        }
        else if (node instanceof Expr){
            Expression exp = MapPyExpressionsJDK.mapExpression((expr)((Expr) node).getValue(),asn, import_nodes);
            ArrayList<ExpressionStatement> list_assign = new ArrayList<>();
            ExpressionStatement expstmt= asn.newExpressionStatement(exp);
            expstmt.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,node.getCharStopIndex()-node.getCharStartIndex());
            list_assign.add(expstmt);
            return list_assign;

        }
        else {
            throw new NodeNotFoundException("Corresponding Python node is not found : found "+node.getClass());
        }

    }

    private Set<TypeDecNeeds> getVariabelNeedsDecleration(PythonTree node) throws Exception {
        PyVisitor pyVisitor = new PyVisitor();
        pyVisitor.visit(node);
        return pyVisitor.getTypeDecNeeds();

    }
}
