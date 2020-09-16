package python3.typeinference.core;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.jpp.PyASTParser;
import org.jpp.astnodes.base.expr;
import org.jpp.astnodes.base.mod;
import python3.MapPyExpressionsJDK;
import python3.pyerrors.ExpressionNotFound;
import python3.pyerrors.NodeNotFoundException;
import python3.typeinference.antlr.TypeInfo;
import python3.typeinference.antlr.TypeTree;

import java.util.HashMap;

public class TypeStringToJDT {
    static Logger logger = Logger.getLogger(TypeStringToJDT.class);
    public static VariableDeclarationStatement mapTypeStringToTypeTree(AST ast,TypeDecNeeds needs,String typeString) throws NodeNotFoundException {
        VariableDeclarationFragment variableDeclarationFragment = ast.newVariableDeclarationFragment();
        variableDeclarationFragment.setName(ast.newSimpleName(needs.getName()));
        VariableDeclarationStatement variableDeclarationStatement = ast.newVariableDeclarationStatement(variableDeclarationFragment);
        variableDeclarationStatement.setType(getJDTType(ast,typeString));

        return variableDeclarationStatement;
    }

    private static Type getJDTType(AST ast,String typeString) throws NodeNotFoundException {
        TypeInfo typeInfo = new TypeInfo();
        TypeTree typeTree=null;
        try {
            typeTree = typeInfo.getTypeTree(typeString);
            if (typeTree.isError()){
                logger.fatal("Error when parsing Type String :"+typeString);
            }
            return convertToJDTType(ast,typeTree.getTree());
        } catch (RecognitionException e) {
            logger.fatal("Type tree formation error");
            logger.error(e);
            return null;
        }

    }

    private static Type convertToJDTType(AST ast, CommonTree typeTree) throws NodeNotFoundException {
        CommonTree tree = typeTree;

        if (tree.getChildren()==null){
            if (tree.getText().equals("int")){
                return ast.newPrimitiveType(PrimitiveType.INT);
            }
            else if (tree.getText().equals("Any")){
                return ast.newSimpleType(ast.newName("Any"));
            }
            else if (tree.getText().equals("nothing")){
                return ast.newSimpleType(ast.newName("Any"));
            }
            else if (tree.getText().contains(".")){
                try {
                    mod mod = PyASTParser.parsePython(tree.getText());
                    Expression expression = MapPyExpressionsJDK.mapExpression((expr) mod.getChild(0).getChild(0), ast, new HashMap<String, org.eclipse.jdt.core.dom.Name>());
                    return ast.newSimpleType((Name) expression);

                } catch (ExpressionNotFound expressionNotFound) {
                    expressionNotFound.printStackTrace();
                    logger.fatal("Error when converting types");
                    logger.error(expressionNotFound);
                }
                return ast.newSimpleType(ast.newName("Any"));
            }

            else{
                logger.debug("SimpleType was assigned to  : "+tree.getText());
                return ast.newSimpleType(ast.newName(tree.getText()));
            }

        }
        else if(tree.getChildren().size()==1){
            if (tree.getText().equals("List")){
                return ast.newArrayType(convertToJDTType(ast, (CommonTree) tree.getChild(0)));

            }
            else {
                logger.fatal("Corresponding Python node is not found : " + tree.getText());
                throw new NodeNotFoundException("Corresponding Python node is not found : " + tree.getText());
            }
        }
        else if (tree.getText().equals("Union")){
            UnionType unionType = ast.newUnionType();
            for (Object child : tree.getChildren()) {
                unionType.types().add(convertToJDTType(ast, (CommonTree) child));
            }
            return unionType;
        }
        else
        {
            logger.fatal("Corresponding Python node is not found : "+tree.getText());
            throw new NodeNotFoundException("Corresponding Python node is not found : "+tree.getText());
        }

    }

}
