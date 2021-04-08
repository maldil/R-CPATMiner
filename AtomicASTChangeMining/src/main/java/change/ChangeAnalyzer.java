package change;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

import core.Configurations;
import graphics.DotGraph;

import io.vavr.control.Try;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.CommitTimeRevFilter;
import org.eclipse.jgit.revwalk.filter.RevFilter;

import python3.typeinference.core.TypeInformation;
import refactoringminer.api.Refactoring;
import refactoringminer.python.GetPythonRefactoring;
import repository.SVNConnector;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;

import repository.GitConnector;
import utils.Config;
import utils.FileIO;

import static core.Configurations.WITH_REFACTORING;
import static io.vavr.API.Try;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;

public class ChangeAnalyzer {
	private String projectName;
	private int projectId;
	private String url;
	private long startRevision = -1, endRevision = -1;
	private int numOfRevisions = -1, numOfCodeRevisions = -1, numOfExtractedRevisions = -1;
	private SVNConnector svnConn;
	private GitConnector gitConn;
	private int typeErroredFails=0;
	GetPythonRefactoring refactoring=null ;
	private HashMap<Long, SVNLogEntry> logEntries;
	private ArrayList<RevisionAnalyzer> revisionAnalyzers = new ArrayList<RevisionAnalyzer>();
	private CProject cproject;
//	private PrintStream ps;
	static Logger logger = Logger.getLogger(ChangeAnalyzer.class);
	private int sucessRate;

	public int getSuccessRate(){
		return sucessRate;
	}

	public ChangeAnalyzer(String projectName, int projectId, String svnUrl,
			long start, long end) {
		this.projectName = projectName;
		this.projectId = projectId;
		this.url = svnUrl;
		this.startRevision = start;
		this.endRevision = end;


	}

	public ChangeAnalyzer(String projectName, int projectId, String svnUrl) {
		this.projectName = projectName;
		this.projectId = projectId;
		this.url = svnUrl;
//		System.out.println(this.url);
//		try {
//			File dir = new File(MainChangeAnalyzer.outputPath + "/"
//					+ projectName);
//			if (!dir.exists())
//				dir.mkdirs();
//			ps = new PrintStream(new FileOutputStream(dir.getAbsolutePath() + "/stats.txt"));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}

	}

	public String getProjectName() {
		return projectName;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public SVNConnector getSvnConn() {
		return this.svnConn;
	}

	public long getStartRevision() {
		return startRevision;
	}

	public long getEndRevision() {
		return endRevision;
	}

	public int getNumOfRevisions() {
		if (numOfRevisions == -1)
			numOfRevisions= this.gitConn.getNumberOfCommits(null);
		return numOfRevisions;
	}

	public int getNumOfCodeRevisions() {
		return numOfCodeRevisions;
	}

	public void incrementNumOfCodeRevisions() {
		this.numOfCodeRevisions++;
	}

	public SVNLogEntry getLogEntry(long revision) {
		return this.logEntries.get(revision);
	}

	public ArrayList<RevisionAnalyzer> getRevisionAnalyzers() {
		return revisionAnalyzers;
	}

	public CProject getCproject() {
		return cproject;
	}

	public void buildSvnConnector() {
		svnConn = new SVNConnector(url, "guest", "guest");
		svnConn.connect();
		if (this.startRevision == -1) {
			this.startRevision = 1;
			svnConn.setLatestRevision();
			this.endRevision = svnConn.getLatestRevision();
		}
	}

	public void buildGitConnector() {
		logger.debug("Building git connector for : "+url);
		this.gitConn = new GitConnector(url + "/.git");
		this.gitConn.connect();
		logger.debug("Connected");

	}

	public void closeGitConnector() {
		this.gitConn.close();
	}

	public void buildLogEntries() {
		this.logEntries = new HashMap<Long, SVNLogEntry>();
		long start = this.startRevision;
		while (start <= this.endRevision) {
			long end = start + 99;
			if (end > this.endRevision) {
				end = this.endRevision;
			}
			buildLogEntries(start, end);
			start = end + 1;
		}
	}

	public void buildLogEntries(long startRevision, long endRevision) {
		Collection<?> logEntries = null;
		try {
			logEntries = svnConn.getRepository().log(new String[] { "" }, null,
					startRevision, endRevision, true, true);
		} catch (SVNException svne) {
			System.out.println("Error while collecting log information for '"
					+ url + "': " + svne.getMessage());
			return;
			// System.exit(1);
		}
		for (Iterator<?> entries = logEntries.iterator(); entries.hasNext();) {
			SVNLogEntry logEntry = (SVNLogEntry) entries.next();
			this.logEntries.put(logEntry.getRevision(), logEntry);
		}
	}

	public void buildLogAndAnalyze() {
		this.cproject = new CProject(projectId, projectName);
		this.cproject.numOfAllRevisions = this.endRevision - this.startRevision
				+ 1;
		this.cproject.revisions = new ArrayList<CRevision>();

		this.logEntries = new HashMap<Long, SVNLogEntry>();
		long start = this.startRevision;
		while (start <= this.endRevision) {
			long end = start + 99;
			if (end > this.endRevision) {
				end = this.endRevision;
			}
			buildLogEntries(start, end);
			analyze(start, end);
			start = end + 1;
		}
	}

	public void analyze() {
		analyze(this.startRevision, this.endRevision);
	}

	public static List<RevCommit> getCommits(Git git, RevSort order) {
		List<RevCommit> commits = new ArrayList<>(Try.of(() -> {
			RevWalk walk = new RevWalk(git.getRepository());
			walk.markStart(walk.parseCommit(git.getRepository().resolve(Constants.HEAD)));
			walk.sort(order);
			walk.setRevFilter(RevFilter.NO_MERGES);
			return walk;
		})
				.map(walk -> {
					Iterator<RevCommit> iter = walk.iterator();
					List<RevCommit> l = new ArrayList<>();
					while (iter.hasNext()) {
						l.add(iter.next());
					}
					walk.dispose();
					return l;
				})
				.onSuccess(l -> System.out.println(l.size() + " number of commits found for " + git.getRepository().getDirectory().getParentFile().getName()))
				.onFailure(Throwable::printStackTrace)
				.getOrElse(new ArrayList<>()));
		Collections.reverse(commits);
		return commits;
	}



	public void analyzeGit() {
		this.cproject = new CProject(projectId, projectName);
		this.cproject.revisions = new ArrayList<>();
		File dir = new File(Configurations.outputPath + "/" + projectName);

		if (WITH_REFACTORING) {
			try {
				refactoring = new GetPythonRefactoring(Configurations.inputPath, Configurations.TYPE_REPOSITORY, projectName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Iterable<RevCommit> commits = null;
		try {
//			ObjectId head = this.gitConn.getRepository().resolve(Constants.HEAD);
//			commits = this.gitConn.getGit().log().add(head).call();
			commits = getCommits(this.gitConn.getGit(),RevSort.REVERSE);
			assert StreamSupport.stream(commits.spliterator(), false).count()>0;

		} catch (RuntimeException e) {
			logger.error(e.getMessage());
		}
		if (commits == null)
			return;
		this.numOfCodeRevisions = 0;
		this.numOfRevisions = 0;
		this.numOfExtractedRevisions = 0;
//		for (RevCommit commit : commits) {
//			long time = commit.getCommitTime();
//			Date date = new Date(time * 1000);
//		    String sdate = new SimpleDateFormat("yyyy-MM-dd").format(date);
//		    if (sdate.compareTo("2017-02-01") < 0)
//		    	return;
//		    break;
//		}
		int num_commits =0;
		for (final RevCommit commit : commits) {    //Iterate each commit
			logger.info(commit.toString());

			if (numOfExtractedRevisions >= Config.MAX_EXTRACTED_COMMITS)
				break;

			File file = new File(dir.getAbsolutePath() + "/" + commit.getName() + ".dat");
			if (file.exists()) {
				numOfExtractedRevisions++;
				continue;
			}
			analyzeGit(commit);     //Start Analysing git commit
			num_commits+=1;
			logger.info(typeErroredFails+" commits did not analysed due to type errors  "+projectName);
			logger.info(sucessRate+" commits analysed  "+projectName);
		}
		this.cproject.numOfAllRevisions = this.numOfRevisions;
		logger.debug(num_commits+" commits processed");
	}

	private void analyzeGit(RevCommit commit) {
		int imageID = 0;
		this.numOfRevisions++;
		if (this.numOfRevisions % 1000 == 0)
			logger.info("Analyzing revision: " + this.numOfRevisions + " " + commit.getName() + " from " + projectName);

		List<Refactoring> refs = null;

		if (refactoring!=null && refs!=null && refs.size()>0)
			System.out.println();

		RevisionAnalyzer ra = new RevisionAnalyzer(this, commit,this.url);


		boolean analyzed =false;
		if (refactoring!=null && WITH_REFACTORING) {
			try {
				refs = this.refactoring.getPythonRefactoringInCommit(commit.getName());
				analyzed =ra.analyzeGit(refs);
				ra.updateRefactoringChanges(refs);
			} catch (Exception e) {

			}
		}
		else{
			analyzed = ra.analyzeGit(refs);
		}

		if (ra.isTypeError()) typeErroredFails++;

		if (ra.getSuceessRate()) sucessRate++;

		if (analyzed) {
			HashMap<String, HashMap<String, ChangeGraph>> changeGraphs = new HashMap<>();
			for (CMethod e : ra.getMappedMethodsM()) {
				//System.out.println("Method: " + e.getQualName() + " - " + e.getMappedEntity().getQualName());
				ChangeGraph cg = e.getChangeGraph(this.gitConn.getRepository(), commit);
//				ps.println(commit.getName()
//						+ ":" + e.getCFile().getPath()
//						+ ":" + e.getCClass().getName() + "." + e.getSimpleName() + "(" + e.getNumOfParameters()+ ")" + e.getParameterTypes()
//						+ ":" + cg.summarize());

				int[] csizes = cg.getChangeSizes();

				if (csizes[0] > 0 && csizes[1] > 0
						&& (csizes[0] + csizes[1]) >= 2
						&& csizes[0] <= 100 && csizes[1] <= 100 
						&& (cg.hasMethods()||cg.hasArrays()||cg.hasForLoops())) {
					// DEBUG
//					DotGraph dg = new DotGraph(cg);
//					String dirPath = "./OUTPUT/DEBUG/";
//					dg.toDotFile(new File(dirPath  +commit.name()+"___"+imageID+".dot"));
//					dg.toGraphics(dirPath  +commit.name()+"___"+imageID, "png");
					imageID+=1;
					HashMap<String, ChangeGraph> cgs = changeGraphs.get(e.getCFile().getPath());
					if (cgs == null) {
						cgs = new HashMap<>();
						changeGraphs.put(e.getCFile().getPath(), cgs);
					}
					String className = e.getCClass().getName().contains("PyDummyClass")? "": e.getCClass().getName();
					String methodName = e.getSimpleName().contains("PyDummyMethod") ? "":e.getSimpleName();
					String paraName = e.getParameterTypes();

					if (Configurations.IS_PYTHON){
						cgs.put(className+"," + methodName + "," + paraName + "," + e.startPyLine, cg);
					}
					else{
						cgs.put(className+"," + methodName + "," + paraName + "," + e.startLine, cg);
					}

				}
				else{
					logger.info("PDG was not created - CSIZES monitor : "+ Arrays.toString(csizes) + "Has methods "+cg.hasMethods());
				}
				e.cleanForStats();
			}
			if (!changeGraphs.isEmpty()) {
				File dir = new File(Configurations.outputPath + "/"
						+ projectName);
				if (!dir.exists())
					dir.mkdirs();
				FileIO.writeObjectToFile(changeGraphs, dir.getAbsolutePath()
						+ "/" + commit.getName() + ".dat", false);
				numOfExtractedRevisions++;
			}
		}
	}

	private void analyze(long startRevision, long endRevision) {
		for (long r = startRevision; r <= endRevision; r++) {
			if (this.logEntries.containsKey(r)) {
				System.out.println("Analyzing revision: " + r);
				SVNLogEntry logEntry = this.logEntries.get(r);
				if (logEntry != null && logEntry.getDate() != null) {
					RevisionAnalyzer ra = new RevisionAnalyzer(this, r);
					// this.revisionAnalyzers.add(ra);
					boolean analyzed = ra.analyze();
					if (analyzed) {
						if (ra.crevision != null) {
							this.cproject.revisions.add(ra.crevision);
						}
						/*
						 * HashSet<CMethod> methodsM = ra.getMappedMethodsM(),
						 * methodsN = ra.getMappedMethodsN(); HashSet<CField>
						 * fieldsM = ra.getMappedFieldsM(), fieldsN =
						 * ra.getMappedFieldsN(); HashSet<CInitializer> initsM =
						 * ra.getMappedInitsM(), initsN = ra.getMappedInitsN();
						 */
						if (!ra.getMappedMethodsM().isEmpty())
							ra.crevision.methods = new ArrayList<CMethod>();
						for (CMethod e : ra.getMappedMethodsM()) {
							// System.out.println("Method: " + e.getQualName() +
							// " - " + e.getMappedEntity().getQualName());
							e.getChangeGraph(null, null);
							ra.crevision.methods.add(e);
							/*
							 * String diff = e.printTree();
							 * System.out.println(diff);
							 */
							e.cleanForStats();
						}
						/*
						 * for (CField e : fieldsM) {
						 * System.out.println("Field: " + e.getQualName());
						 * String diff = e.printTree();
						 * System.out.println(diff); }
						 */
						/*
						 * if (!ra.getMappedInitsM().isEmpty())
						 * ra.crevision.inits = new ArrayList<CInitializer>();
						 * for (CInitializer e : ra.getMappedInitsM()) {
						 * //System.out.println("Init: " + e.getQualName());
						 * e.getAtomicTreeChanges(); ra.crevision.inits.add(e);
						 * e.cleanForStats(); }
						 */
					}
				}
			}
		}
	}

	public String getSourceCode(String changedPath, long revision) {
		return this.svnConn.getFile(changedPath, revision);
	}

	public GitConnector getGitConn() {
		return this.gitConn;
	}
}
