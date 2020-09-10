package python3.typeinference.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.google.gson.Gson;
import python3.typeinference.antlr.TypeInfo;
import python3.typeinference.antlr.TypeTree;
import org.antlr.runtime.RecognitionException;
import org.apache.log4j.Logger;


public class TypeInformation {
    static Logger logger = Logger.getLogger(TypeInformation.class);

    public Map<TypeASTNode, TypeTree> getTypeTreeInformation(String filePath, String projectPath, String fileName, ArrayList<TypeASTNode> requiredNodes) throws RecognitionException {
        TypeInfo typeInfo = new TypeInfo();
        Map<TypeASTNode, TypeTree> typeASTNodeTypeTreeMap = new HashMap<>();
        Map<TypeASTNode, String> typeInformation = getTypeInformation(filePath, projectPath, fileName);
        for (TypeASTNode requiredNode : requiredNodes) {
            logger.debug("Finding type information of : "+requiredNode.toString());
            String typeString = typeInformation.get(requiredNode);
            typeASTNodeTypeTreeMap.put(requiredNode,typeInfo.getTypeTree(typeString));
        }
        return typeASTNodeTypeTreeMap;
    }

    public Map<TypeASTNode,String> getTypeInformation(String filePath,String project_path,String file_name){
        Map<TypeASTNode,String> type_string = new HashMap<>();
        try {
            logger.debug("Generating pytype files : File path:"+filePath+" Project path : "+project_path+" String file name : "+file_name);
            Process process = Runtime.getRuntime().exec(Configurations.PYTHON_VIRTUAL_ENVIRONMENT+ " "+
                    System.getProperty("user.dir")+"/src/main/java/python3/typeinference/pythonscripts/mainPyTypetest.py " +
                    ""+filePath+" "+project_path+" "+file_name+"/");
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            BufferedReader stdINput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean error = false;
            while ((line = stdError.readLine()) != null) {
                logger.error(line);
                System.err.println(line);
                error=true;
            }

            String typeString = getTypeString(stdINput);

            Gson gson = new Gson();
            TypeASTNode[] astArray = gson.fromJson(typeString, TypeASTNode[].class);

            for(TypeASTNode node : astArray) {
                logger.debug(node.toString());
//                System.out.println(node.toString());
                if (type_string.get(node) !=null){
                    logger.error(node + "Node exists");
                }
                String type = node.getType();
                node.setType(null);
                type_string.put(node, type);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return type_string;
    }

    private String getTypeString(BufferedReader stdINput) throws IOException {
        String s ;
        String typeString = "";
        while ((s = stdINput.readLine()) != null) {
            logger.info(s);
            typeString=s;
        }
        return typeString;
    }
}
