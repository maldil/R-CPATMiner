package python3;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class PrintTotatlASTs {
    private static org.apache.log4j.Logger log = Logger.getLogger(TestTensorFlow.class);
    @Test
    public void printResults (){
        final File path =  new File("./node_map1.bin");
        if(path.exists()){
            try {
                FileInputStream is = new FileInputStream("./node_map1.bin");
                ObjectInputStream ois = new ObjectInputStream(is);
                HashMap<String, Integer> nodes = (HashMap<String, Integer>) ois.readObject();
                log.warn(nodes);
                int numberOfNodes = nodes.values().stream().mapToInt(i -> i).sum();
                log.warn("Total Number of  Nodes  : "+ numberOfNodes );
                ois.close();
                is.close();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
