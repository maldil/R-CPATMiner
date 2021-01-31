package core;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import change.ChangeAnalyzer;
import org.apache.log4j.Logger;
import python3.typeinference.antlr.TypeInfo;
import utils.FileIO;
import utils.NotifyingBlockingThreadPoolExecutor;
import org.apache.commons.lang.SystemUtils;

public class MainChangeAnalyzer {
	static Logger logger = Logger.getLogger(MainChangeAnalyzer.class);
	private static final Callable<Boolean> blockingTimeoutCallback = new Callable<Boolean>() {
		@Override
		public Boolean call() throws Exception {
			return true; // keep waiting
		}
	};
	private static NotifyingBlockingThreadPoolExecutor pool = new NotifyingBlockingThreadPoolExecutor(Configurations.THREAD_POOL_SIZE, Configurations.THREAD_POOL_SIZE, 15, TimeUnit.SECONDS, 200, TimeUnit.MILLISECONDS, blockingTimeoutCallback);

	//	public static String inputPath = "C:/repos/repos-junit", outputPath = "C:/change graphs/repos-junit";
//	public static String inputPath = "F:/testrepos/repos-test", outputPath = "F:/change graphs/repos-test";
//	public static String inputPath = "F:/github/testchange", outputPath = "D:/temp/testchanges";
//	public static String inputPath = "/Users/michaelhilton/Development/Research/GraphMinerData/junit";
//	public static String outputPath = "/Users/michaelhilton/Development/Research/GraphMinerData/output";
//	public static String inputPath = "G:/github/repos-bare", outputPath = "T:/change graphs/repos-99";
	
	public static void main(String[] args) {
		logger.debug("++++++Starting the process AtomicASTChangeMining++++++++");
		String content = null;

		if (SystemUtils.IS_OS_WINDOWS) {
			Configurations.THREAD_POOL_SIZE = 8;
			pool = new NotifyingBlockingThreadPoolExecutor(Configurations.THREAD_POOL_SIZE, Configurations.THREAD_POOL_SIZE, 15, TimeUnit.SECONDS, 200, TimeUnit.MILLISECONDS, blockingTimeoutCallback);
//			inputPath = "F:/github/repos-junit"; outputPath = "T:/change graphs/repos-junit";
//			content = FileIO.readStringFromFile("G:/github/repos-junit.csv");
			Configurations.inputPath = "F:/github/repos-IntelliJ"; Configurations.outputPath = "T:/change graphs/repos-IntelliJ";
			content = FileIO.readStringFromFile("G:/github/repos-IntelliJ.csv");
//			inputPath = "T:/github/repos-selected"; outputPath = "T:/change graphs/repos-selected";
//			inputPath = "E:/github/repos-5stars-50commits"; outputPath = "T:/change graphs/repos-5stars-50commits-fresh";
//			content = FileIO.readStringFromFile("E:/github/repos-5stars-50commits-fresh-remain.csv");
		} else if (SystemUtils.IS_OS_MAC) {
			Configurations.THREAD_POOL_SIZE = 1;
			Configurations.inputPath = "/Users/malinda/Documents/Research_Topic_2/DATA_FOR_CPATMiner/";
			Configurations.outputPath = "/Users/malinda/Documents/Research_Topic_2/CPatMiner/AtomicASTChangeMining/OUTPUT/";

			pool = new NotifyingBlockingThreadPoolExecutor(Configurations.THREAD_POOL_SIZE, Configurations.THREAD_POOL_SIZE, 15, TimeUnit.SECONDS, 200, TimeUnit.MILLISECONDS, blockingTimeoutCallback);
			content = FileIO.readStringFromFile("/Users/malinda/Documents/Research_Topic_2/CPatMiner/AtomicASTChangeMining/selected-repos.csv");
		} else if (SystemUtils.IS_OS_LINUX) {
			Configurations.THREAD_POOL_SIZE = 24;
			pool = new NotifyingBlockingThreadPoolExecutor(Configurations.THREAD_POOL_SIZE, Configurations.THREAD_POOL_SIZE, 15, TimeUnit.SECONDS, 200, TimeUnit.MILLISECONDS, blockingTimeoutCallback);
			Configurations.inputPath = "/home/hoan/github/repositories/java-stars-100";
			Configurations.outputPath = "/home/hoan/github/change graphs/repos-99";
			content = FileIO.readStringFromFile("/home/hoan/github/selected-repos.csv");
		}
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("-i")) {
					Configurations.inputPath = args[i+1];
				}
				if (args[i].equals("-o")) {
					Configurations.outputPath = args[i+1];
				}
			}
		}
		Scanner sc = new Scanner(content);
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			int index = line.indexOf(',');
			if (index < 0)
				index = line.length();
			String name = line.substring(0, index);
			File dir = new File(Configurations.inputPath + "/" + name);
			logger.info("Started analysing : "+dir+" , Project name : "+name);
			analyze(dir, name);
		}
		sc.close();
		try {
			pool.await(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (final InterruptedException e) { }
	}

	private static void analyze(final File dir, final String name) {
		if (!dir.isDirectory())
			return;
		File git = new File(dir, ".git");
		if (git.exists()) {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					long startProjectTime = System.currentTimeMillis();
					String url = dir.getAbsolutePath();
					ChangeAnalyzer ca = new ChangeAnalyzer(name, -1, url);
					ca.buildGitConnector();
					ca.analyzeGit();
			    	long endProjectTime = System.currentTimeMillis();
			    	ca.getCproject().setRunningTime(endProjectTime - startProjectTime);
			    	ca.closeGitConnector();
					System.out.println("Done " + name + " in " + (endProjectTime - startProjectTime) / 1000 + "s");
					logger.info("Done " + name + " in " + (endProjectTime - startProjectTime) / 1000 + "s");
				}
			});
		}
	}

}
