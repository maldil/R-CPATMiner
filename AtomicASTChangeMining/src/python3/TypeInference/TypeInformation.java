package python3.TypeInference;

import main.Configurations;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Map;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;


public class TypeInformation {
    static Logger logger = Logger.getLogger(TypeInformation.class);
    public Map<TypeASTNode,String> getTypeInformation(String filePath){
        Map<TypeASTNode,String> type_string = new HashMap<>();
        try {

            Process process = Runtime.getRuntime().exec(Configurations.PYTHON_VIRTUAL_ENVIRONMENT+ " "+System.getProperty("user.dir")+"/src/python3/TypeInference/PyScripts/mainPyTypetest.py "+filePath);
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            BufferedReader stdINput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s = stdINput.readLine();
            Gson gson = new Gson();
            TypeASTNode[] astArray = gson.fromJson(s, TypeASTNode[].class);

            for(TypeASTNode node : astArray) {
                logger.debug(node.toString());
//                System.out.println(node.toString());
                if (type_string.get(node) !=null){
                    logger.error(node + "Node exists");
                }
                type_string.put(node, node.getType());
            }
            while ((s = stdError.readLine()) != null) {
                System.err.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return type_string;
    }
}
