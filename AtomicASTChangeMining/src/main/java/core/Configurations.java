package core;

public class Configurations {
    public enum TYPE_SOURCE {
        INFER,
        FILE,
    }
    public static boolean IS_PYTHON = true;
    public static TYPE_SOURCE PYTHON_TYPE_SOURCE = TYPE_SOURCE.FILE;
    public static boolean IS_JAVA = false;
    //	public static String inputPath = "F:/github/NayaraBorges/Pousada";
    public static String inputPath = "/Users/malinda/Documents/Research_Topic_2/DATA_FOR_CPATMiner/";
    public static String outputPath = "/Users/malinda/Documents/Research_Topic_2/CPatMiner/AtomicASTChangeMining/OUTPUT/";
    public static String metaData = "/Users/malinda/Documents/Research_Topic_2/RepoMetaData/";
    static int THREAD_POOL_SIZE = 1;
    public static String PYTHON_VIRTUAL_ENVIRONMENT = "/Users/malinda/Documents/Research_Topic_2/VMS/pytestenv/bin/python3.7";
    public static String PYTHON_SCRIPT_DIRECTORY = "/Users/malinda/Documents/Research_Topic_2/CPatMiner/AtomicASTChangeMining/src/main/java/python3/typeinference/pythonscripts";
    public static String TYPE_REPOSITORY = "/Users/malinda/Documents/Research_Topic_2/PythonTypeInformation/";
}
