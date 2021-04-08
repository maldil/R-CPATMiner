package change;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import com.ibm.icu.impl.Assert;
import core.Configurations;
import gr.uom.java.xmi.UMLClass;
import gr.uom.java.xmi.UMLOperation;
import gr.uom.java.xmi.decomposition.UMLOperationBodyMapper;
import gr.uom.java.xmi.diff.AddParameterRefactoring;
import gr.uom.java.xmi.diff.ExtractClassRefactoring;
import gr.uom.java.xmi.diff.ExtractSuperclassRefactoring;
import gr.uom.java.xmi.diff.MoveAndRenameClassRefactoring;
import gr.uom.java.xmi.diff.MoveClassRefactoring;
import gr.uom.java.xmi.diff.MoveOperationRefactoring;
import gr.uom.java.xmi.diff.PullUpOperationRefactoring;
import gr.uom.java.xmi.diff.PushDownOperationRefactoring;
import gr.uom.java.xmi.diff.RemoveParameterRefactoring;
import gr.uom.java.xmi.diff.RenameClassRefactoring;
import gr.uom.java.xmi.diff.RenameOperationRefactoring;
import gr.uom.java.xmi.diff.RenameVariableRefactoring;
import gr.uom.java.xmi.diff.ReorderParameterRefactoring;
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
import refactoringminer.api.Refactoring;
import refactoringminer.api.RefactoringType;
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

	private HashSet<CFile> addedFile = new HashSet<CFile>();

	private HashSet<CFile> deletedFile = new HashSet<CFile>();

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

	public boolean analyzeGit(List<Refactoring> refs) {
		if (!buildGitModifiedFiles())   // update mappedfilesM and mappedfilesN with source codes.
			return false;
		if (Config.countChangeFileOnly)
			return true;
		if (Configurations.WITH_REFACTORING)
			updateRefactoringChangesofClasses(refs);
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
									continue;
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
									continue;
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
									continue;
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
					}else if (Configurations.IS_PYTHON && diff.getChangeType() == ChangeType.ADD){
						TypeInformation typeM = new TypeInformation();
						Map<TypeASTNode, String> typeInfo =typeM.getTypeInformationFromJsonFile
								(Configurations.TYPE_REPOSITORY,new File(Configurations.inputPath).toURI().relativize(new File(url).toURI()).getPath(),gitCommit.getName(),"N",diff.getNewPath().replace('/', '_'));
						CFile file;
						if (typeInfo!=null){
							String content = null;
							ObjectLoader ldr = null;
							try {
								ldr = repository.open(diff.getOldId().toObjectId(),
										Constants.OBJ_BLOB);
								content = new String(ldr.getCachedBytes());
							} catch (IOException e) {
								logger.debug(e.getMessage());
								continue;
							}
							file = new CFile(this, diff.getOldPath(),
									content, this.url, typeInfo);
							file.setCType(Type.Added);
							addedFile.add(file);
						}
					}
					else if (Configurations.IS_PYTHON && diff.getChangeType() == ChangeType.DELETE){
						TypeInformation typeM = new TypeInformation();
						Map<TypeASTNode, String> typeInfo =typeM.getTypeInformationFromJsonFile
								(Configurations.TYPE_REPOSITORY,new File(Configurations.inputPath).toURI().relativize(new File(url).toURI()).getPath(),gitCommit.getName(),"O",diff.getNewPath().replace('/', '_'));
						CFile file;
						if (typeInfo!=null){
							String content = null;
							ObjectLoader ldr = null;
							try {
								ldr = repository.open(diff.getOldId().toObjectId(),
										Constants.OBJ_BLOB);
								content = new String(ldr.getCachedBytes());
							} catch (IOException e) {
								logger.debug(e.getMessage());
								continue;
							}
							file = new CFile(this, diff.getOldPath(),
									content, this.url, typeInfo);
							file.setCType(Type.Deleted);
							deletedFile.add(file);
						}

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


	private void updateRefactoringChangesofClasses(List<Refactoring> refs) {
		for (Refactoring ref : refs) {
			if (ref.getRefactoringType()==RefactoringType.RENAME_CLASS ){
				RenameClassRefactoring mapper = ((RenameClassRefactoring) ref);
				UMLClass operationBefore = mapper.getOriginalClass();
				UMLClass operationAfter = mapper.getRenamedClass();
				CClass classBefore = getClass(mappedFilesM,operationBefore);
				CClass classAfter = getClass(mappedFilesN,operationAfter);
				if (classBefore!=null&&classAfter!=null){
					CClass.setMap(classBefore,classAfter);
					mappedClassesM.add(classBefore);
					mappedClassesN.add(classAfter);
				}
			}
			else if(ref.getRefactoringType()==RefactoringType.MOVE_CLASS ){
				MoveClassRefactoring mapper = ((MoveClassRefactoring) ref);
				UMLClass operationBefore = mapper.getOriginalClass();
				UMLClass operationAfter = mapper.getMovedClass();
				CClass classBefore = getClass(mappedFilesM,operationBefore);
				if (classBefore==null)
					classBefore = getClass(deletedFile,operationBefore);

				CClass classAfter = getClass(mappedFilesN,operationAfter);
				if (classAfter==null)
					classAfter = getClass(addedFile,operationAfter);

				if (classBefore!=null&&classAfter!=null){
					CClass.setMap(classBefore,classAfter);
					mappedClassesM.add(classBefore);
					mappedClassesN.add(classAfter);
				}
			}
			else if(ref.getRefactoringType()==RefactoringType.MOVE_RENAME_CLASS ){
				MoveAndRenameClassRefactoring mapper = ((MoveAndRenameClassRefactoring) ref);
				UMLClass operationBefore = mapper.getOriginalClass();
				UMLClass operationAfter = mapper.getRenamedClass();
				CClass classBefore = getClass(mappedFilesM,operationBefore);
				if (classBefore==null)
					classBefore = getClass(deletedFile,operationBefore);

				CClass classAfter = getClass(mappedFilesN,operationAfter);
				if (classAfter==null)
					classAfter = getClass(addedFile,operationAfter);

				if (classBefore!=null&&classAfter!=null){
					CClass.setMap(classBefore,classAfter);
					mappedClassesM.add(classBefore);
					mappedClassesN.add(classAfter);
				}
			}
			else if(ref.getRefactoringType()==RefactoringType.EXTRACT_CLASS ){
				ExtractClassRefactoring mapper = ((ExtractClassRefactoring) ref);
				UMLClass operationBefore = mapper.getOriginalClass();
				UMLClass operationAfter = mapper.getExtractedClass();
				CClass classBefore = getClass(mappedFilesM,operationBefore);
				if (classBefore==null)
					classBefore = getClass(deletedFile,operationBefore);

				CClass classAfter = getClass(mappedFilesN,operationAfter);
				if (classAfter==null)
					classAfter = getClass(addedFile,operationAfter);

				if (classBefore!=null&&classAfter!=null){
					CClass.setMap(classBefore,classAfter);
					mappedClassesM.add(classBefore);
					mappedClassesN.add(classAfter);
				}
			}
			else if(ref.getRefactoringType()==RefactoringType.EXTRACT_SUBCLASS ){
				System.out.println();
				ExtractSuperclassRefactoring mapper = ((ExtractSuperclassRefactoring) ref);


			}
			else if (ref.getRefactoringType()==RefactoringType.EXTRACT_SUPERCLASS){
//				System.out.println();
//				ExtractSuperclassRefactoring mapper = ((ExtractSuperclassRefactoring) ref);
//				UMLClass operationBefore = mapper.get();
//				UMLClass operationAfter = mapper.getExtractedClass();
//				CClass classBefore = getClass(mappedFilesM,operationBefore);
//				if (classBefore==null)
//					classBefore = getClass(deletedFile,operationBefore);
//
//				CClass classAfter = getClass(mappedFilesN,operationAfter);
//				if (classAfter==null)
//					classAfter = getClass(addedFile,operationAfter);
//
//				if (classBefore!=null&&classAfter!=null){
//					CClass.setMap(classBefore,classAfter);
//					mappedClassesM.add(classBefore);
//					mappedClassesN.add(classAfter);
//				}

			}


		}

	}

	public void updateRefactoringChanges(List<Refactoring> refs) {
		for (Refactoring ref : refs) {
			if(ref.getRefactoringType()== RefactoringType.RENAME_METHOD)
			{
				UMLOperationBodyMapper mapper = ((RenameOperationRefactoring) ref).getBodyMapper();
				String methodNameBefore = mapper.getOperation1().getName();
				int parametersBefore = mapper.getOperation1().getParameters().size();
				String packageNameBefore = Arrays.stream(mapper.getOperation1().getClassName().split("\\.")).
						limit(mapper.getOperation1().getClassName().split("\\.").length-1).collect(Collectors.joining("."));
				String classNameBefore = mapper.getOperation1().getClassName().split("\\.")[mapper.getOperation1().getClassName().split("\\.").length-1];


				String methodNameAfter = mapper.getOperation2().getName();
				int parameterAfter = mapper.getOperation2().getParameters().size();
				String packageNameAfter = Arrays.stream(mapper.getOperation2().getClassName().split("\\.")).
						limit(mapper.getOperation2().getClassName().split("\\.").length-1).collect(Collectors.joining("."));
				String classNameAfter = mapper.getOperation2().getClassName().split("\\.")[mapper.getOperation2().getClassName().split("\\.").length-1];


				CMethod methodBefore = getMethod(mappedFilesM, packageNameBefore,classNameBefore,methodNameBefore,parametersBefore);
				CMethod methodAfter = getMethod(mappedFilesN, packageNameAfter,classNameAfter,methodNameAfter,parameterAfter );

				if (methodAfter!=null && methodBefore!=null){
					CMethod.setMap(methodBefore,methodAfter);
					mappedMethodsM.add(methodBefore);
					mappedMethodsN.add(methodAfter);
				}
			}
			else if (ref.getRefactoringType()== RefactoringType.MOVE_OPERATION){  //Move method
				MoveOperationRefactoring mapper = ((MoveOperationRefactoring) ref);
				UMLOperation operationBefore = mapper.getOriginalOperation();
				UMLOperation operationAfter = mapper.getMovedOperation();
				CMethod methodBefore = getMethod(mappedFilesM,operationBefore);
				CMethod methodAfter = getMethod(mappedFilesN,operationAfter);
				if (methodAfter!=null && methodBefore!=null){
					CMethod.setMap(methodBefore,methodAfter);
					mappedMethodsM.add(methodBefore);
					mappedMethodsN.add(methodAfter);
				}

			}

			else if (ref.getRefactoringType()== RefactoringType.PULL_UP_OPERATION){
				PullUpOperationRefactoring mapper = ((PullUpOperationRefactoring) ref);
				UMLOperation operationBefore = mapper.getOriginalOperation();
				UMLOperation operationAfter = mapper.getMovedOperation();
				CMethod methodBefore = getMethod(mappedFilesM,operationBefore);
				CMethod methodAfter = getMethod(mappedFilesN,operationAfter);
				if (methodAfter!=null && methodBefore!=null){
					CMethod.setMap(methodBefore,methodAfter);
					mappedMethodsM.add(methodBefore);
					mappedMethodsN.add(methodAfter);
				}
			}

			else if (ref.getRefactoringType()== RefactoringType.PUSH_DOWN_OPERATION){
				PushDownOperationRefactoring mapper = ((PushDownOperationRefactoring) ref);
				UMLOperation operationBefore = mapper.getOriginalOperation();
				UMLOperation operationAfter = mapper.getMovedOperation();
				CMethod methodBefore = getMethod(mappedFilesM,operationBefore);
				CMethod methodAfter = getMethod(mappedFilesN,operationAfter);
				if (methodAfter!=null && methodBefore!=null){
					CMethod.setMap(methodBefore,methodAfter);
					mappedMethodsM.add(methodBefore);
					mappedMethodsN.add(methodAfter);
				}
			}

			else if (ref.getRefactoringType()==RefactoringType.ADD_PARAMETER){
				AddParameterRefactoring mapper = ((AddParameterRefactoring) ref);
				UMLOperation operationBefore = mapper.getOperationBefore();
				UMLOperation operationAfter = mapper.getOperationAfter();
				CMethod methodBefore = getMethod(mappedFilesM,operationBefore);
				CMethod methodAfter = getMethod(mappedFilesN,operationAfter);
				if (methodAfter!=null && methodBefore!=null){
					CMethod.setMap(methodBefore,methodAfter);
					mappedMethodsM.add(methodBefore);
					mappedMethodsN.add(methodAfter);
				}
			}

			else if (ref.getRefactoringType()==RefactoringType.REORDER_PARAMETER){
				ReorderParameterRefactoring mapper = ((ReorderParameterRefactoring) ref);
				UMLOperation operationBefore = mapper.getOperationBefore();
				UMLOperation operationAfter = mapper.getOperationAfter();
				CMethod methodBefore = getMethod(mappedFilesM,operationBefore);
				CMethod methodAfter = getMethod(mappedFilesN,operationAfter);
				if (methodAfter!=null && methodBefore!=null){
					CMethod.setMap(methodBefore,methodAfter);
					mappedMethodsM.add(methodBefore);
					mappedMethodsN.add(methodAfter);
				}
			}

			else if (ref.getRefactoringType()==RefactoringType.REMOVE_PARAMETER ){
				RemoveParameterRefactoring mapper = ((RemoveParameterRefactoring) ref);
				UMLOperation operationBefore = mapper.getOperationBefore();
				UMLOperation operationAfter = mapper.getOperationAfter();
				CMethod methodBefore = getMethod(mappedFilesM,operationBefore);
				CMethod methodAfter = getMethod(mappedFilesN,operationAfter);
				if (methodAfter!=null && methodBefore!=null){
					CMethod.setMap(methodBefore,methodAfter);
					mappedMethodsM.add(methodBefore);
					mappedMethodsN.add(methodAfter);
				}
			}

			else if (ref.getRefactoringType()==RefactoringType.PARAMETERIZE_VARIABLE){
				RenameVariableRefactoring mapper = ((RenameVariableRefactoring) ref);
				UMLOperation operationBefore = mapper.getOperationBefore();
				UMLOperation operationAfter = mapper.getOperationAfter();
				CMethod methodBefore = getMethod(mappedFilesM,operationBefore);
				CMethod methodAfter = getMethod(mappedFilesN,operationAfter);
				if (methodAfter!=null && methodBefore!=null){
					CMethod.setMap(methodBefore,methodAfter);
					mappedMethodsM.add(methodBefore);
					mappedMethodsN.add(methodAfter);
				}
			}
			else if (ref.getRefactoringType()==RefactoringType.MOVE_AND_RENAME_OPERATION){
				MoveOperationRefactoring mapper =  ((MoveOperationRefactoring)ref);
				UMLOperation operationBefore = mapper.getOriginalOperation();
				UMLOperation operationAfter = mapper.getMovedOperation();
				CMethod methodBefore = getMethod(mappedFilesM,operationBefore);
				CMethod methodAfter = getMethod(mappedFilesN,operationAfter);
				if (methodAfter!=null && methodBefore!=null){
					CMethod.setMap(methodBefore,methodAfter);
					mappedMethodsM.add(methodBefore);
					mappedMethodsN.add(methodAfter);
				}

			}







			deriveChanges();
		}

	}

	private CClass getClass(HashSet<CFile> files, UMLClass operation){
		String packageName = operation.getPackageName();
		String className =  operation.getName();
		CClass cClass=null;
		for (CFile file : files) {
			String filePackage =  file.getPath().replace('/','.').substring(0, file.getPath().length() - 3);
			if (filePackage.equals(packageName.substring(4, packageName.length()))){
				for (CClass aClass : file.getClasses()) {
					if (aClass.getName().equals(className)|| !aClass.getName().contains("PyDummyClass")){
						cClass=aClass;
					}
				}
			}
		}
		return cClass;
	}

	private CMethod getMethod(HashSet<CFile> files, UMLOperation operation){
		String methodName =  operation.getName();
		int parameters = operation.getParameters().size();
		String packageName = Arrays.stream(operation.getClassName().split("\\.")).
				limit(operation.getClassName().split("\\.").length-1).collect(Collectors.joining("."));
		String className = operation.getClassName().split("\\.")[operation.getClassName().split("\\.").length-1];
		return getMethod(files,packageName,className,methodName,parameters);
	}




	private CMethod getMethod(HashSet<CFile> files , String packageName, String className,String methodName,int numberParameters){
		CMethod cMethod=null;
		for (CFile file : files) {
			String filePackage =  file.getPath().replace('/','.').substring(0, file.getPath().length() - 3);
			if (filePackage.equals(packageName.substring(4, packageName.length()))){
				for (CClass aClass : file.getClasses()) {
					if (aClass.getName().equals(className)|| aClass.getName().contains("PyDummyClass"))
					{
						for (CMethod method : aClass.getMethods()) {
							if (method.getSimpleName().equals(methodName) && method.getNumOfParameters()==numberParameters-1){
								cMethod=method;
								break;
							}
						}

					}
				}
			}

		}
		return cMethod;
	}
}
