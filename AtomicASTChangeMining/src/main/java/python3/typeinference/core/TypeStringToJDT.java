package python3.typeinference.core;

import com.google.gson.internal.$Gson$Preconditions;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.jpp.PyASTParser;
import org.jpp.astnodes.base.expr;
import org.jpp.astnodes.base.mod;
import python3.MapPyExpressionsJDK;
import python3.PyMap;
import python3.pyerrors.ExpressionNotFound;
import python3.pyerrors.NodeNotFoundException;
import python3.typeinference.antlr.TypeInfo;
import python3.typeinference.antlr.TypeTree;

import java.util.HashMap;

public class TypeStringToJDT extends PyMap{
    static Logger logger = Logger.getLogger(TypeStringToJDT.class);
    public static VariableDeclarationStatement mapTypeStringToTypeTree(AST ast, TypeDecNeeds needs, String typeString, int startPosition) throws NodeNotFoundException {
        VariableDeclarationFragment variableDeclarationFragment = ast.newVariableDeclarationFragment();
        VariableDeclarationStatement variableDeclarationStatement = ast.newVariableDeclarationStatement(variableDeclarationFragment);

        Type jdtType = getJDTType(ast, typeString, startPosition);
        assert jdtType != null;
        jdtType.setSourceRange(startPosition,jdtType.toString().length());
        variableDeclarationStatement.setType(jdtType);

        SimpleName simpleName = ast.newSimpleName(needs.getName());
        simpleName.setSourceRange(startPosition + jdtType.toString().length()+1,simpleName.toString().length());
        variableDeclarationFragment.setName(simpleName);

        variableDeclarationFragment.setSourceRange(startPosition+ jdtType.toString().length()+1,variableDeclarationFragment.toString().length());

        return variableDeclarationStatement;
    }

    public static Type getJDTType(AST ast, String typeString, int startPosition) throws NodeNotFoundException {
        TypeInfo typeInfo = new TypeInfo();
        TypeTree typeTree=null;
        try {
            typeTree = typeInfo.getTypeTree(typeString);
            if (typeTree.isError()){
                logger.fatal("Error when parsing Type String :"+typeString);
            }
            return convertToJDTType(ast,typeTree.getTree(),startPosition);
        } catch (RecognitionException e) {
            logger.fatal("Type tree formation error");
            logger.error(e);
            return null;
        }

    }

    private static Type convertToJDTType(AST ast, CommonTree typeTree,int startPosition) throws NodeNotFoundException {
        CommonTree tree = typeTree;

        if (tree.getChildren()==null){
            if (tree.getText().equals("int")){
                PrimitiveType primitiveType = ast.newPrimitiveType(PrimitiveType.INT);
                primitiveType.setSourceRange(startPosition,primitiveType.toString().length());
                return primitiveType;
            }
            else if (tree.getText().equals("Any")){
                SimpleType any = ast.newSimpleType(ast.newName("Any"));
                any.setSourceRange(startPosition,3);
                return any;
            }
            else if (tree.getText().equals("nothing")){
                SimpleType any = ast.newSimpleType(ast.newName("Any"));
                any.setSourceRange(startPosition,3);
                return any;
            }
            else if (tree.getText().contains(".")){

//                    mod mod = PyASTParser.parsePython(tree.getText());
//                    Expression expression = MapPyExpressionsJDK.mapExpression((expr) mod.getChild(0).getChild(0), ast, new HashMap<>());
//                    SimpleType simpleType = ast.newSimpleType((Name) expression);


                    QualifiedName qualifiedName = (QualifiedName) ast.newName(tree.getText().split("\\."));
                    qualifiedName.setSourceRange(startPosition,qualifiedName.toString().length());
                    SimpleType simpleType = ast.newSimpleType(qualifiedName);
                    simpleType.setSourceRange(startPosition,simpleType.toString().length());
                    return simpleType;

//                Name any = ast.newName("Any");
//                any.setSourceRange(startPosition,4);
//                return ast.newSimpleType(any);
            }

            else{
                logger.debug("SimpleType was assigned to  : "+tree.getText());
                return ast.newSimpleType(ast.newName(tree.getText()));
            }

        }
        else if(tree.getChildren().size()==1){
            if (tree.getText().equals("List")){
                ArrayType arrayType = ast.newArrayType(convertToJDTType(ast, (CommonTree) tree.getChild(0), startPosition+5));
                arrayType.setSourceRange(startPosition,arrayType.toString().length());
                return arrayType;

            }
            else {
                logger.fatal("Corresponding Python node is not found : " + tree.getText());
                throw new NodeNotFoundException("Corresponding Python node is not found : " + tree.getText());
            }
        }
        else if (tree.getText().equals("Union")){
            UnionType unionType = ast.newUnionType();
            for (Object child : tree.getChildren()) {
                unionType.types().add(convertToJDTType(ast, (CommonTree) child,startPosition+6));
            }
            unionType.setSourceRange(startPosition,unionType.toString().length());
            return unionType;
        }
        else
        {
            logger.fatal("Corresponding Python node is not found : "+tree.getText());
            throw new NodeNotFoundException("Corresponding Python node is not found : "+tree.getText());
        }

    }


}
