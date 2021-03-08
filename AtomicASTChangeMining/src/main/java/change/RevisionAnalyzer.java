package change;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.ibm.icu.impl.Assert;
import core.Configurations;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.filter.PathSuffixFilter;
import org.eclipse.jgit.util.io.NullOutputStream;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import python3.typeinference.core.TypeASTNode;
import python3.typeinference.core.TypeInformation;
import utils.Config;
import change.ChangeEntity.Type;

import static core.Configurations.PYTHON_TYPE_SOURCE;
import static python3.typeinference.core.Configurations.TYPE_REPOSITORY;

public class RevisionAnalyzer {

	private ChangeAnalyzer changeAnalyzer;
	private long revision;
	private RevCommit gitCommit;
	private String url;
	private boolean typeError;
	static Logger logger = Logger.getLogger(RevisionAnalyzer.class);
	private static Log typeana= LogFactory.getLog("ExternalAppLogger");
//	static Logger typeLogger = Logger.getLogger("typeerror");

	private HashSet<CFile> mappedFilesM = new HashSet<CFile>(),
			mappedFilesN = new HashSet<CFile>();
	private HashSet<CClass> classesM = new HashSet<CClass>(),
			classesN = new HashSet<CClass>();
	private HashSet<CClass> mappedClassesM = new HashSet<CClass>(),
			mappedClassesN = new HashSet<CClass>();
	private HashSet<CMethod> methodsM = new HashSet<CMethod>(),
			methodsN = new HashSet<CMethod>();
	private HashSet<CMethod> mappedMethodsM = new HashSet<CMethod>(),
			mappedMethodsN = new HashSet<CMethod>();
	private HashSet<CField> fieldsM = new HashSet<CField>(),
			fieldsN = new HashSet<CField>();
	private HashSet<CField> mappedFieldsM = new HashSet<CField>(),
			mappedFieldsN = new HashSet<CField>();
	private HashSet<CInitializer> initsM = new HashSet<CInitializer>(),
			initsN = new HashSet<CInitializer>();
	private HashSet<CInitializer> mappedInitsM = new HashSet<CInitializer>(),
			mappedInitsN = new HashSet<CInitializer>();
	CRevision crevision;
	private boolean success;

	public boolean getSuceessRate(){
		return success;
	}


	public RevisionAnalyzer(ChangeAnalyzer changeAnalyzer, long revision) {
		this.changeAnalyzer = changeAnalyzer;
		this.revision = revision;
	}

	public RevisionAnalyzer(ChangeAnalyzer changeAnalyzer, RevCommit commit,String svnUrl) {
		this.changeAnalyzer = changeAnalyzer;
		this.gitCommit = commit;
		this.revision = commit.getCommitTime();
		this.url = svnUrl;
	}

	public ChangeAnalyzer getChangeAnalyzer() {
		return changeAnalyzer;
	}

	public long getRevision() {
		return revision;
	}

	public HashSet<CFile> getMappedFilesM() {
		return mappedFilesM;
	}

	public HashSet<CFile> getMappedFilesN() {
		return mappedFilesN;
	}

	public HashSet<CClass> getMappedClassesM() {
		return mappedClassesM;
	}

	public HashSet<CClass> getMappedClassesN() {
		return mappedClassesN;
	}

	public HashSet<CMethod> getMappedMethodsM() {
		return mappedMethodsM;
	}

	public HashSet<CMethod> getMappedMethodsN() {
		return mappedMethodsN;
	}

	public HashSet<CField> getMappedFieldsM() {
		return mappedFieldsM;
	}

	public HashSet<CField> getMappedFieldsN() {
		return mappedFieldsN;
	}

	public HashSet<CInitializer> getMappedInitsM() {
		return mappedInitsM;
	}

	public HashSet<CInitializer> getMappedInitsN() {
		return mappedInitsN;
	}

	public boolean analyze() {
		if (!buildModifiedFiles())
			return false;
		if (Config.countChangeFileOnly)
			return true;
		if (!map())
			return false;

		deriveChanges();
		/*
		 * try { printChanges(new PrintStream(new
		 * FileOutputStream("output/changes.txt"))); } catch
		 * (FileNotFoundException e) { e.printStackTrace(); }
		 */
		return true;
	}

	public boolean analyzeGit() {
		if (!buildGitModifiedFiles())   // update mappedfilesM and mappedfilesN with source codes.
			return false;
		if (Config.countChangeFileOnly)
			return true;
		if (!map())
		logger.info("methodsM: "+methodsM);//removed methods
		logger.info("methodsN: "+methodsN); //Added methods
		deriveChanges();
		logger.info("mappedMethodsM: "+mappedMethodsM); //All coexisting methods in removed code
		logger.info("mappedMethodsN: "+mappedMethodsN); //All coexisting methods in added code

		if (this.mappedMethodsM.size() > 100)
			return false;
		return true;
	}

	private boolean buildModifiedFiles() {
		SVNLogEntry logEntry = this.changeAnalyzer.getLogEntry(revision);
		HashSet<String> changedPaths = new HashSet<String>(logEntry
				.getChangedPaths().keySet());
		HashMap<String, String> copiedPaths = new HashMap<>();
		HashMap<String, Long> copiedRevisions = new HashMap<>();
		for (String changedPath : changedPaths) {
			SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry
					.getChangedPaths().get(changedPath);
			String path = entryPath.getPath();
			if (entryPath.getCopyPath() != null) {
				copiedPaths.put(path, entryPath.getCopyPath());
				copiedRevisions.put(path, entryPath.getCopyRevision());
			}
		}
		HashSet<String> javaChangedPaths = new HashSet<>();
		for (String path : changedPaths) {
			if (path.endsWith(".java")) {
				SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry
						.getChangedPaths().get(path);
				if (entryPath.getType() == SVNLogEntryPath.TYPE_MODIFIED/*
																		 * &&
																		 * entryPath
																		 * .
																		 * getKind
																		 * () ==
																		 * SVNNodeKind
																		 * .FILE
																		 */) {
					String oldPath = path;
					long oldRevision = logEntry.getRevision() - 1;
					if (copiedPaths.containsKey(path)) {
						oldPath = copiedPaths.get(path);
					} else {
						String prefix = "";
						for (String copiedPath : copiedPaths.keySet()) {
							if (path.startsWith(copiedPath)) {
								if (copiedPath.length() > prefix.length()) {
									prefix = copiedPath;
								}
							}
						}
						if (!prefix.isEmpty()) {
							oldPath = copiedPaths.get(prefix)
									+ path.substring(prefix.length());
							oldRevision = copiedRevisions.get(prefix);
						}
					}
					javaChangedPaths.add(path);
					copiedPaths.put(path, oldPath);
					copiedRevisions.put(path, oldRevision);
				}
			}
		}
		if (javaChangedPaths.size() > 50)
			return false;
		if (!javaChangedPaths.isEmpty()) {
			this.crevision = new CRevision();
			this.crevision.id = this.revision;
			this.crevision.numOfFiles = javaChangedPaths.size();
			this.crevision.files = new ArrayList<CSourceFile>();
		}
		if (Config.countChangeFileOnly)
			return true;
		for (String changedPath : javaChangedPaths) {
			String contentM = getSourceCode(copiedPaths.get(changedPath),
					copiedRevisions.get(changedPath));
			String contentN = getSourceCode(changedPath, revision);

			if (contentM.isEmpty() || contentN.isEmpty()){
				continue;
			}

			if (contentM == null)
				continue;

			if (contentN == null)
				continue;

			CFile fileM = new CFile(this, copiedPaths.get(changedPath),
					contentM,this.url,new HashMap<>());
			CFile fileN = new CFile(this, changedPath, contentN,this.url,new HashMap<>());
			this.mappedFilesM.add(fileM);
//			this.crevision.files.add(new CSourceFile(changedPath, fileM
//					.getSourceFile().getLines().size()));
			this.mappedFilesN.add(fileN);
			fileM.setCType(Type.Modified);
			fileN.setCType(Type.Modified);
			fileM.setMappedFile(fileN);
			fileN.setMappedFile(fileM);
		}
		return true;
	}

	private boolean buildGitModifiedFiles() {
		if (gitCommit.getParentCount() == 1) {
			RevWalk rw = new RevWalk(this.changeAnalyzer.getGitConn()
					.getRepository());
			RevCommit parent = null;
			try {
				parent = rw.parseCommit(gitCommit.getParent(0).getId());
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			if (parent == null) {
				rw.close();
				return false;
			}
			Repository repository = this.changeAnalyzer.getGitConn()
					.getRepository();
			DiffFormatter df = new DiffFormatter(NullOutputStream.INSTANCE);
			df.setRepository(repository);
			df.setDiffComparator(RawTextComparator.DEFAULT);
			df.setDetectRenames(true);
			if (Configurations.IS_PYTHON && !Configurations.IS_JAVA) {
				df.setPathFilter(PathSuffixFilter.create(".py"));
			}
			else if (Configurations.IS_JAVA && !Configurations.IS_PYTHON){
				df.setPathFilter(PathSuffixFilter.create(".java"));
			}
			else {
				Assert.assrt("Please correctly specify the processing language in Configurations",false);
			}
			List<DiffEntry> diffs = null;
			try {
				diffs = df.scan(parent.getTree(), gitCommit.getTree());
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			if (diffs == null) {
				rw.close();
				df.close();
				return false;
			}
			if (diffs.size() > 50) {
				rw.close();
				df.close();
				return false;
			}
			if (!diffs.isEmpty()) {
				this.crevision = new CRevision();
				this.crevision.id = this.revision;
				this.crevision.numOfFiles = diffs.size();
				this.crevision.files = new ArrayList<CSourceFile>();
			}
			if (Config.countChangeFileOnly) {
				rw.close();
				df.close();
				return true;
			}

			if (!diffs.isEmpty()) {
				this.changeAnalyzer.incrementNumOfCodeRevisions();
				for (DiffEntry diff : diffs) {
					if (diff.getChangeType() == ChangeType.MODIFY
							&& diff.getOldMode().getObjectType() == Constants.OBJ_BLOB
							&& diff.getNewMode().getObjectType() == Constants.OBJ_BLOB) {
						// System.out.println(diff.getChangeType() + ": " +
						// diff.getOldPath() + " --> " + diff.getNewPath());
						ObjectLoader ldr = null;
						String oldContent = null, newContent = null;
						try {
							ldr = repository.open(diff.getOldId().toObjectId(),
									Constants.OBJ_BLOB);
							oldContent = new String(ldr.getCachedBytes());
						} catch (IOException e) {
							logger.debug(e.getMessage());
							continue;
						}
						try {
							ldr = repository.open(diff.getNewId().toObjectId(),
									Constants.OBJ_BLOB);
							newContent = new String(ldr.getCachedBytes());
						} catch (IOException e) {
							logger.debug(e.getMessage());
							continue;
						}
						if (oldContent.isEmpty() || newContent.isEmpty()){
							continue;
						}
						CFile fileN;
						CFile fileM;
						if (Configurations.IS_PYTHON ){
							if (PYTHON_TYPE_SOURCE == Configurations.TYPE_SOURCE.FILE){
								if (!Files.exists(Path.of(Configurations.TYPE_REPOSITORY +new File(Configurations.inputPath).toURI().relativize(new File(url).toURI()).getPath() + gitCommit.getName() +   "O" +"/"))){
//									assert false :Configurations.TYPE_REPOSITORY  +new File(Configurations.inputPath).toURI().relativize(new File(url).toURI()).getPath() + gitCommit.getName() +   "O" +"/";
								}
								TypeInformation typeM = new TypeInformation();
								Map<TypeASTNode, String> typeInfoM =typeM.getTypeInformationFromJsonFile
										(Configurations.TYPE_REPOSITORY,new File(Configurations.inputPath).toURI().relativize(new File(url).toURI()).getPath(),gitCommit.getName(),"O",diff.getOldPath().replace('/', '_'));
								TypeInformation typeN = new TypeInformation();
								Map<TypeASTNode, String> typeInfoN = typeN.getTypeInformationFromJsonFile
										(Configurations.TYPE_REPOSITORY,new File(Configurations.inputPath).toURI().relativize(new File(url).toURI()).getPath(),gitCommit.getName(),"N",diff.getNewPath().replace('/', '_'));


								if (typeInfoM == null || typeInfoN==null) {
									typeError = true;
									return false;
								}
								else {
									success = true;
								}
								fileM = new CFile(this, diff.getOldPath(),
										oldContent, this.url, typeInfoM);


								fileN = new CFile(this, diff.getNewPath(),
										newContent, this.url, typeInfoN);
							}
							else {
								logger.debug("checked out : " + this.gitCommit.getName());
								try {
									typeana.info(this.gitCommit.getName());
									this.changeAnalyzer.getGitConn().getGit().checkout().setAllPaths(true).call(); //TODO change this to checkout a commit only once.
									this.changeAnalyzer.getGitConn().getGit().checkout().setName(this.gitCommit.getName()).call();
								} catch (GitAPIException e) {
									logger.error(e);
								}
								TypeInformation typeInformation1 = new TypeInformation();
								Map<TypeASTNode, String> typeInfor1 = typeInformation1.getTypeInformation(this.url + "/" + diff.getNewPath(), this.url, diff.getNewPath().replace('/', '.'));
								if (typeInfor1 == null) {
									typeError = true;
									return false;
								}
								fileN = new CFile(this, diff.getNewPath(),
										newContent, this.url, typeInfor1);
								logger.debug("checked out : " + parent.getName());
								try {
									typeana.info(this.gitCommit.getName());
									this.changeAnalyzer.getGitConn().getGit().checkout().setAllPaths(true).call();
									this.changeAnalyzer.getGitConn().getGit().checkout().setName(parent.getName()).call();
								} catch (GitAPIException e) {
									logger.error(e);
								}
								TypeInformation typeInformation2 = new TypeInformation();
								Map<TypeASTNode, String> typeInfo2 = typeInformation2.getTypeInformation(this.url + "/" + diff.getOldPath(), this.url, diff.getOldPath().replace('/', '.'));
								if (typeInfo2 == null) {
									typeError = true;
									return false;
								}
								fileM = new CFile(this, diff.getOldPath(),
										oldContent, this.url, typeInfo2);
							}

						}
						else {
							fileM = new CFile(this, diff.getOldPath(),
									oldContent,this.url,null);
							fileN = new CFile(this, diff.getNewPath(),
									newContent,this.url,null);
						}





						this.mappedFilesM.add(fileM);
//						this.crevision.files.add(new CSourceFile(diff
//								.getNewPath(), fileM.getSourceFile().getLines()
//								.size()));
						this.mappedFilesN.add(fileN);
						fileM.setCType(Type.Modified);
						fileN.setCType(Type.Modified);
						fileM.setMappedFile(fileN);
						fileN.setMappedFile(fileM);
					}
				}
			}
			rw.close();
			df.close();
		}
		return true;
	}

	private boolean map() {
		mapClasses();
		mapMethods();
		// mapFields();
		// mapEnumConstants();
		return true;
	}

	private void mapClasses() {
		// diff classes in modified files
		for (CFile fileM : mappedFilesM) {
			CFile fileN = fileM.getMappedFile();
			fileM.computeSimilarity(fileN);
			for (CClass cc : fileM.getClasses()) {
				if (cc.getMappedClass() != null) {
					mappedClassesM.add(cc);
					mappedClassesN.add(cc.getMappedClass());
					Stack<CClass> stkClasses = new Stack<CClass>();
					stkClasses.push(cc);
					while (!stkClasses.isEmpty()) {
						CClass stkClass = stkClasses.pop();
						stkClass.computeSimilarity(stkClass.getMappedClass(),
								false);
						for (CClass icc : stkClass.getInnerClasses(false)) {
							if (icc.getMappedClass() != null) {
								mappedClassesM.add(icc);
								mappedClassesN.add(icc.getMappedClass());
								stkClasses.push(icc);
							}
						}
					}
				} else
					classesM.add(cc);
			}
			for (CClass cc : fileN.getClasses()) {
				if (cc.getMappedClass() != null)
					mappedClassesN.add(cc);
				else
					classesN.add(cc);
				for (CClass icc : cc.getInnerClasses(true)) {
					if (icc.getMappedClass() != null)
						mappedClassesN.add(icc);
					else
						classesN.add(icc);
				}
			}
		}

		// map any classes
		CClass.mapAll(classesM, classesN, mappedClassesM, mappedClassesN);

		// done diffing classes
		clearClassBodyMapping();
		for (CClass cc : new HashSet<CClass>(mappedClassesM)) {
			cc.computeSimilarity(cc.getMappedClass(), true);
			for (CMethod cm : cc.getMethods()) {
				if (cm.getMappedMethod() != null)
					mappedMethodsM.add(cm);
				else
					methodsM.add(cm);
			}
			for (CField cf : cc.getFields()) {
				if (cf.getMappedField() != null)
					mappedFieldsM.add(cf);
				else
					fieldsM.add(cf);
			}
			for (CInitializer ci : cc.getInitializers()) {
				if (ci.getMappedInitializer() != null)
					mappedInitsM.add(ci);
				else
					initsM.add(ci);
			}
		}
		for (CClass cc : new HashSet<CClass>(mappedClassesN)) {
			for (CMethod cm : cc.getMethods()) {
				if (cm.getMappedMethod() != null)
					mappedMethodsN.add(cm);
				else
					methodsN.add(cm);
			}
			for (CField cf : cc.getFields()) {
				if (cf.getMappedField() != null)
					mappedFieldsN.add(cf);
				else
					fieldsN.add(cf);
			}
			for (CInitializer ci : cc.getInitializers()) {
				if (ci.getMappedInitializer() != null)
					mappedInitsN.add(ci);
				else
					initsN.add(ci);
			}
		}
		for (CClass cc : classesM) {
			for (CMethod cm : cc.getMethods()) {
				methodsM.add(cm);
			}
			for (CField cf : cc.getFields()) {
				fieldsM.add(cf);
			}
			for (CInitializer ci : cc.getInitializers()) {
				initsM.add(ci);
			}
		}
		for (CClass cc : classesN) {
			for (CMethod cm : cc.getMethods()) {
				methodsN.add(cm);
			}
			for (CField cf : cc.getFields()) {
				fieldsN.add(cf);
			}
			for (CInitializer ci : cc.getInitializers()) {
				initsN.add(ci);
			}
		}
	}

	private void clearClassBodyMapping() {
		for (CClass cc : this.classesM) {
			cc.clearBodyMapping();
		}
		for (CClass cc : this.classesN) {
			cc.clearBodyMapping();
		}
		for (CClass cc : this.mappedClassesM) {
			cc.clearBodyMapping();
		}
		for (CClass cc : this.mappedClassesN) {
			cc.clearBodyMapping();
		}
	}

	private void mapMethods() {
		CMethod.mapAll(methodsM, methodsN, mappedMethodsM, mappedMethodsN,
				false);
	}

	private void deriveChanges() {
		// deriveFieldChanges();
		deriveMethodChanges();
		//deriveInitChanges();
		// deriveEnumConstantChanges();
		// deriveClassChanges();
	}

	private void deriveMethodChanges() {
		for (CMethod cmM : new HashSet<CMethod>(mappedMethodsM)) {
			cmM.deriveChanges();			//iterate each methods that wasn't deleted or inserted.
			if (cmM.getCType() == Type.Unchanged) {
				mappedMethodsM.remove(cmM);
				mappedMethodsN.remove(cmM.getMappedMethod());
			}
		}
	}

	@SuppressWarnings("unused")
	private void deriveInitChanges() {
		for (CInitializer ciM : new HashSet<CInitializer>(mappedInitsM)) {
			ciM.deriveChanges();
			if (ciM.getCType() == Type.Unchanged) {
				mappedInitsM.remove(ciM);
				mappedInitsN.remove(ciM.getMappedInitializer());
			}
		}
	}

	@SuppressWarnings("unused")
	private void deriveClassChanges() {
		for (CClass cc : classesM) {
			cc.setCType(Type.Deleted);
		}
		for (CClass cc : classesN) {
			cc.setCType(Type.Added);
		}
		for (CClass ccM : new HashSet<CClass>(mappedClassesM)) {
			ccM.deriveChanges();
			if (ccM.getCType() == Type.Unchanged) {
				mappedClassesM.remove(ccM);
				mappedClassesN.remove(ccM.getMappedClass());
			}
		}
	}

	public void printChanges(PrintStream ps) {
		ps.println("Revision: " + this.revision);
		ps.println("Old system");
		printChanges(ps, mappedFilesM);
		ps.println("New system");
		printChanges(ps, mappedFilesN);
	}

	private void printChanges(PrintStream ps, HashSet<CFile> files) {
		for (CFile cf : files) {
			cf.printChanges(ps);
		}
	}

	private String getSourceCode(String changedPath, long revision) {
		return this.changeAnalyzer.getSourceCode(changedPath, revision);
	}

	public boolean isTypeError() {
		return typeError;
	}
}
