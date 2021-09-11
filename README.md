
This is a fork of https://github.com/nguyenhoan/CPatMiner. We extended it to support Python. 

Clone the repo : git clone --recurse-submodules https://github.com/maldil/CPatMiner.git

# CPatMiner: A tool that mines fine-grained code change pattern in Python software systems


## Packages
AtomicASTChangeMining: extracts change graphs from commits.

SemanticChangeGraphMiner: mines change templates from change graphs.

## Extracting change graphs from commits

main class: https://github.com/nguyenhoan/CPatMiner/blob/master/AtomicASTChangeMining/src/main/MainChangeAnalyzer.java

arguments:

-i input_repos_root_path: each sub folder is a git repo

-o output_path: where the graphs are stored

## Mining change templates from change graphs

main class: https://github.com/nguyenhoan/CPatMiner/blob/master/SemanticChangeGraphMiner/src/main/MineChangePatterns.java

reposPath = input_repos_root_path: a sub folder is a git repo

changesPath = change_graph_path: output_path of the extraction step

file list.csv under reposPath: a text file containing the names of repos to be processed, one repo name on each line. ls reposPath > list.csv to create this file if you want to process all repos

output: in a directory patterns/input_repos_root_path-hybrid under the working directory

note: the directory https://github.com/nguyenhoan/CPatMiner/tree/master/SemanticChangeGraphMiner/src/resources has to be in a directory named src under the working directory.


## License
All software provided in this repository is subject to the [Apache License Version 2.0](LICENSE).
