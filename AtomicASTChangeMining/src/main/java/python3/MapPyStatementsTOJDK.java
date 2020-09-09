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

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class MapPyStatementsTOJDK {
    static Logger logger = Logger.getLogger(MapPyStatementsTOJDK.class);
    private Map<TypeASTNode, String> typeNodes;

    public MapPyStatementsTOJDK(Map<TypeASTNode, String> typeNodes) {
        this.typeNodes = typeNodes;
    }

    public ArrayList<?> getMappingPyNode(AST asn , PythonTree node) throws NodeNotFoundException, ExpressionNotFound {
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
            ArrayList<TypeDeclaration> list_class = new ArrayList<>();
            classdec.setName(asn.newSimpleName(((ClassDef) node).getName().toString()));
            Modifier.ModifierKeyword keyword= new Modifier.ModifierKeyword("public",1);
            classdec.setModifier(asn.newModifier(keyword));

            if (((AstList)((ClassDef) node).getBases()).size()>0){
                classdec.setSuperclassType(asn.newSimpleType(asn.newName(((Name)((AstList)((ClassDef) node).getBases()).get(0)).getInternalId()))); //TODO handle multiple inheritance
            }

            for (Object ch : (AstList)((ClassDef) node).getBody()){
                for (Object o : getMappingPyNode(asn, (PythonTree) ch)) {
                    classdec.bodyDeclarations().add(o);
                }

            }

            list_class.add(classdec);
            return list_class;
        }
        else if (node instanceof FunctionDef){
            Set<TypeDecNeeds> variableNeedsDeclaration=null;
            try {
                variableNeedsDeclaration=getVariabelNeedsDecleration(node);
            } catch (Exception e) {
                logger.error(e);
            }
            MethodDeclaration methoddec = asn.newMethodDeclaration();
            ArrayList<MethodDeclaration> list_method = new ArrayList<>();
            methoddec.setName(asn.newSimpleName(((FunctionDef) node).getName().toString())); //TODO set the field isConstructor
            for (Object arg : (AstList) ((arguments) ((FunctionDef) node).getArgs()).getArgs()) {
                SingleVariableDeclaration parameter = asn.newSingleVariableDeclaration();
                parameter.setName(asn.newSimpleName(((arg)arg).getArg().toString())); //TODO change type of the parameter
                methoddec.parameters().add(parameter);
            }
            for (Object ch : (AstList)((FunctionDef) node).getBody()){
                for (Object o : getMappingPyNode(asn, (PythonTree) ch)) {
                    if (methoddec.getBody() ==null){
                        methoddec.setBody(asn.newBlock());
                    }
                    methoddec.getBody().statements().add(o);
                }
            }
//
            list_method.add(methoddec);
            return list_method;
        }
        else if (node instanceof Assign){
            Assignment assign = asn.newAssignment();
            ArrayList<ExpressionStatement> list_assign = new ArrayList<>();
            assign.setOperator(new Assignment.Operator("="));
            assign.setLeftHandSide(asn.newSimpleName(((Name)((AstList)((Assign) node).getTargets()).get(0)).getId().toString()));
            assign.setRightHandSide(MapPyExpressionsJDK.mapExpression((expr)((Assign) node).getValue(),asn));
            list_assign.add(asn.newExpressionStatement(assign));
            return list_assign;

        }
        else if (node instanceof For){
            EnhancedForStatement forstmt = asn.newEnhancedForStatement();
            SingleVariableDeclaration parameter = asn.newSingleVariableDeclaration();
            ArrayList<EnhancedForStatement> list_for = new ArrayList<>();
            parameter.setName(asn.newSimpleName(((Name)((expr)((For) node).getTarget())).getId().toString()));
            forstmt.setParameter(parameter);
            forstmt.setExpression(MapPyExpressionsJDK.mapExpression((expr)((For) node).getIter(),asn));
            ((For) node).getBody() ;

            for (Object ch : (AstList)((For) node).getBody()){
                for (Object o : getMappingPyNode(asn, (PythonTree) ch)) {
                    if (forstmt.getBody() ==null){
                        forstmt.setBody(asn.newBlock());
                    }
                    ((Block)forstmt.getBody()).statements().add(o);
                }
            }
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

            assign.setLeftHandSide( MapPyExpressionsJDK.mapExpression((expr) ((AugAssign) node).getTarget(),asn));
            assign.setRightHandSide(MapPyExpressionsJDK.mapExpression((expr)((AugAssign) node).getValue(),asn));
            list_assign.add(asn.newExpressionStatement(assign));
            return list_assign;
        }
        else if (node instanceof Return){
            ReturnStatement statement = asn.newReturnStatement();
            ArrayList<ReturnStatement> list_assign = new ArrayList<>();
            statement.setExpression(MapPyExpressionsJDK.mapExpression((expr) ((Return) node).getValue(),asn));
            list_assign.add(statement);
            return list_assign;
        }
        else if (node instanceof Expr){
            Expression exp = MapPyExpressionsJDK.mapExpression((expr)((Expr) node).getValue(),asn);
            ArrayList<ExpressionStatement> list_assign = new ArrayList<>();
            ExpressionStatement expstmt= asn.newExpressionStatement(exp);

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
