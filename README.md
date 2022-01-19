
This is a fork of https://github.com/nguyenhoan/CPatMiner. We extended it to support Python. 

Clone the repo : git clone --recurse-submodules https://github.com/maldil/CPatMiner.git

# R-CPatMiner: A tool that mines fine-grained code change pattern in Python software systems


## R-CPatMiner consists of two tools that must be run one after the other. 
* AtomicASTChangeMining: extracts change graphs from commits and store them in the directory "OUTPUT" of the project's root directory.
* SemanticChangeGraphMiner: mines code change patterns from change graphs, extracted by running the tool AtomicASTChangeMining.

## Instructions on generating change graphs for commits. 
AtomicASTChangeMining builds change graphs that represent code changes.

main class: [MainChangeAnalyzer.java](https://github.com/maldil/R-CPATMiner/blob/master/AtomicASTChangeMining/src/main/java/core/MainChangeAnalyzer.java)

`AtomicASTChangeMining` requires two inputs. Namely,i.e., 1) the file `selected-repos.csv`, which lists all of the repositories that need to be analyzed, and 2) the Type repository, which lists the types of altered files in all of the commits of the projects listed in "selected-repos.csv". We have already inferred the type information of 1000 ML systems and stored it in this [repository](https://github.com/mlcodepatterns/PythonTypeInformation). This repository may be downloaded and the variable `Configurations.TYPE REPOSITORY` set to the downloaded location. If you want to analyze a project that isn't one of the 1000ML systems, you can infer the type information by following the guidelines in the type [repository](https://github.com/mlcodepatterns/PythonTypeInformation).

### Steps to generate change graphs 
  * To produce meta information required for change graph generation, run main function of `core.AnalyzeRepoMetadata`
  * To produce the change graphs, run `core.MainChangeAnalyzer`. This step might take some time depending on the number of projects you specified in `selected-repos.csv`.


## Instructions on Mining change patterns from change graphs
`SemanticChangeGraphMiner` mines the repetitive code changes. The tool needs to be run on the output of the previous step. 


changesPath
main class: [MineChangePatterns.java](https://github.com/maldil/R-CPATMiner/blob/master/SemanticChangeGraphMiner/src/main/MineChangePatterns.java)

The variable `reposPath` needs to be specified to the directory "output" generated in the previous step which contains the changed graphs. The file `selected-repos.csv` should be updated with all the projects that need to be analyzed. Execute the main class and the analysis might take some time depending on the number of projects in `selected-repos.csv`.

The resultant patterns will be created in a directory called "output" in the root directory.

## How to build CPATMiner
To have fully build CPATMiner, you have to build the two components. 
Before you can start building components, you must first build two dependancy libraries locally and add them to your local `maven` repository.
* [JPyParser](https://github.com/maldil/JPythonParser) 
  * Run mvn `mvn clean package` in the project's root directory. 
  * Install the binaries to your local maven repository using `mvn install:install-file -Dfile=./Your Path/target/JPyParser-1.0-SNAPSHOT.jar -DgroupId=org.mal.python -DartifactId=JPyParser -Dversion=1.0-SNAPSHOT.jar -Dpackaging=jar -DgeneratePom=true` 

* [EclipseJDT](https://github.com/maldil/JavaFyPy/tree/master/CustomizedEclipseJDT) 
  * Follow the instructions in the [repository](https://github.com/maldil/JavaFyPy/tree/master/CustomizedEclipseJDT)  to build the project.  
  * Install the binaries to your local maven repository using mvn install:install-file -Dfile= /You_Path/target/org.eclipse.jdt.core-3.24.0-SNAPSHOT.jar -DgroupId=org.eclipse.jdt -DartifactId=org.eclipse.jdt.core -Dversion=3.24.0-SNAPSHOT -Dpackaging=jar -DgeneratePom=true

Now you are ready to build the two components in CPATMiner.

* Bulding `AtomicASTChangeMining` -  run mvn `mvn clean package` in the AtomicASTChangeMining's root directory. 
* Bulding `SemanticChangeGraphMiner`  -  run mvn `mvn clean package` in the SemanticChangeGraphMiner's root directory. 


## Using the tools in Docker containers.
We have released Docker containers which make it easy to use the tools without building them.
Please follow the steps given below. 

**Step 1** - This [folder](https://drive.google.com/file/d/1mWy046yjHrywRUf_g_wklwiyGtb5Ggtn/view?usp=sharing) should be downloaded, unzipped, and saved to the $FOLDER PATH directory.  

**Step 2**- To download the required Docker container, execute following command
`docker pull malindadoo1/python-cpatminer:5.0`

**Step 3**- To start the container execute following command in your terminal
`docker run -v $FOLDER_PATH/ArtifactEvaluation:/user/local/cpatminer/ArtifactEvaluation -it malindadoo1/python-cpatminer:5.0 /bin/bash`

`$FOLDER_PATH` has to be the absolute folder path of the parent folder that you downloaded in step 1.
You will be entered to the interactive mode of the Docker container. Execute the command `ls` and check the folders `ArtifactEvaluation`,  `atomicminer`, and  `changeminer` are available in the current directory (`/user/local/cpatminer`).  
To check you successfully mount the downloaded folder into the container use `cd` and `ls` commands to go inside the folder `ArtifactEvaluation` and see whether all the folders that you have inside your downloaded folder are also available inside the `ArtifactEvaluation` folder in docker container.

Note-  Fine grained pattern generation consists of two steps, i.e.,(1) change graph generation, and (2) code change pattern generation. You can specify the selected-repos.csv file in the folder CPATMiner to specify the project for change graph generation. 


**Step 4** `Execute python3 test_container.py` If this command prints the message, “You've done an excellent job mounting the folders” appears after running this command, you've successfully finished step 3. You can go to the next step now. If not, make sure the variable $FOLDER PATH is set to the absolute path of the download folder's parent folder.


**Step 5** Execute `./test_cpatminer.sh` 
This shell script consists of the commands that need to 1) generate change graphs generation and 2) pattern generation. You can use vi test_cpatminer.sh to view the commands.

**Observation 1** - Change graphs should have been generated inside the folder `/ArtifactEvaluation/CPATMiner/OutPutChangeGraphs/maldil/MLEditsTest`

**Observation 2** - Code change patterns should have been created inside the folder, `$FOLDER_PATH/ArtifactEvaluation/CPATMiner/Patterns/OutPutChangeGraphs-hybrid/2` in your computer.

Open the `html` file directory.html using any of your browsers. You can see the change patterns that have been generated. 

A code change pattern is represented by a row in this generated table. The number of code change occurrences per pattern is represented by the column `NumberFound`. Click the hyperlinks in the `Details` column to view each case. It should take you to the page, which has all of the occurrences for the relevant pattern. To see the actual changes on GitHub, click on the `Link` hyperlinks.

**Step 6** Now, you saw how R-CPATMiner mines code change patterns. You can execute exit to terminate the container. You can still access all the generated files in the mounted folder.


## License
All software provided in this repository is subject to the [Apache License Version 2.0](LICENSE).
