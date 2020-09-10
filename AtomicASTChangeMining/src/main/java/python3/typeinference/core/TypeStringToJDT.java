package python3.typeinference.core;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import python3.pyerrors.NodeNotFoundException;
import python3.typeinference.antlr.TypeInfo;
import python3.typeinference.antlr.TypeTree;

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
            else{
                logger.fatal("Corresponding Python node is not found : "+tree.getText());
                throw new NodeNotFoundException("Corresponding Python node is not found : "+tree.getText());
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
        else
        {
            logger.fatal("Corresponding Python node is not found : "+tree.getText());
            throw new NodeNotFoundException("Corresponding Python node is not found : "+tree.getText());
        }

    }

}
