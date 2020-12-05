package python3;

import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GenerateTestCode {

    @Test
    public void genTestCode(){
        String path= "sklearn/cross_decomposition/";
        Path resourceDirectory = Paths.get("/Users/malinda/Documents/Research_Topic_2/CPatMiner/AtomicASTChangeMining/src/test/resources/ASTConversion/",path);
        ArrayList<Path> arrayList = new ArrayList<>();
        listf(resourceDirectory,arrayList,resourceDirectory,path);
        int start_int = 464;
        ArrayList<String> file_names  = new ArrayList<>();
        for (Path path1 : arrayList) {
            start_int++;
            System.out.println("@Test\n" +
                    "public void file"+start_int+"(){");
            System.out.println("    String content = readFile(\""+path1+"\");");
            System.out.println("    CompilationUnit converted = convert(content);");
            System.out.println("    Assert.assertEquals(converted.getProblems().length,0);");
            System.out.println("    }");
            file_names.add("file"+start_int+"();");

        }


        for (String file_name : file_names) {
            System.out.println(file_name);
        }


        System.out.printf(arrayList.toString());
    }

    public void listf(Path directoryName, List<Path> files, Path resourceDirectory,String path) {
        File directory = new File(directoryName.toString());

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if(fList != null)
            for (File file : fList) {
                if (file.isFile() && file.toString().endsWith(".py")) {
                    files.add(Path.of(path + resourceDirectory.relativize(Path.of(file.getAbsolutePath()))));
                } else if (file.isDirectory()) {
                    listf(Path.of(file.getAbsolutePath()), files,resourceDirectory,path);
                }
            }
    }
}
