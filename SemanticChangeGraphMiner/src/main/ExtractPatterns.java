package main;

import change.ChangeGraph;
import groum.GROUMGraph;
import groum.GROUMNode;
import mining.Fragment;
import mining.Miner;
import mining.Pattern;
import utils.FileIO;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExtractPatterns {
    private static AtomicInteger numOfCommits = new AtomicInteger(0), numOfGraphs = new AtomicInteger(0);
    private static String changesPath ="";
    public static void main(String[] args) {
        analyse_numpy();

    }

    public static void analyse_numpy(){
        ArrayList<GROUMGraph> graphs = collect_to_numpy();
        HashSet<Fragment> numpy_fragments= new HashSet<>();
        for (GROUMGraph graph : graphs) {
            GROUMNode first_frag;
            boolean consider=false;
            boolean has_deleted=false;
            for (GROUMNode node : graph.getNodes()) {
                if (node!=null && node.getVersion()==1 &&node.getDataType()!=null &&node.getDataType().startsWith("numpy.ndarray")){
                    consider=true;
                    break;
                }
                if (node.getVersion()==0){
                    has_deleted=true;
                }
            }
            Optional<GROUMNode> first = graph.getNodes().stream().findFirst();
            if (has_deleted&&consider&&first.isPresent()){
                first_frag = first.get();
                List<GROUMNode> collect = graph.getNodes().stream().skip(1).collect(Collectors.toList());
                Fragment g_frag = new Fragment(new Fragment(first_frag)  , (ArrayList<GROUMNode>) collect);
                numpy_fragments.add(g_frag);
            }
        }
        Pattern p = new Pattern(numpy_fragments, numpy_fragments.size());
        Miner mine = new Miner(2);
        mine.setReposPath("/Users/malinda/Documents/Research_Topic_2/DATA_FOR_CPATMiner");
        File dir = new File("output/patterns" + "/numpy/");
        ArrayList<ArrayList<String>> patterns = new ArrayList<>();
        mine.printOutResults(dir,0,p,patterns,false);

    }

    public static ArrayList<GROUMGraph> collect_to_numpy(){
        String content = FileIO.readStringFromFile("/Users/malinda/Documents/Research_Topic_2/SemanticChangeGraphMiner/SemanticChangeGraphMiner/selected-repos.csv");
        changesPath = "/Users/malinda/Documents/Research_Topic_2/CPatMiner/AtomicASTChangeMining/OUTPUT/";
        assert content != null;
        Scanner sc = new Scanner(content);

        ArrayList<GROUMGraph> allGraphs = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            int index = line.indexOf(',');
            if (index == -1)
                index = line.length();
            final String name = line.substring(0, index);
            allGraphs.addAll(readGraphs(changesPath, name)) ;

        }
        sc.close();
        return allGraphs;
    }


    private static ArrayList<GROUMGraph> readGraphs(String changesPath, String projectName) {
        ArrayList<GROUMGraph> graphs = new ArrayList<>();
        File dir = new File(changesPath + "/" + projectName);
        System.out.println(changesPath + "/" + projectName);
        if (!dir.exists())
            return graphs;
        File[] files = dir.listFiles();
        if (files == null)
            return graphs;
        for (int i = 0; i < files.length; i++) {
            File sub = files[i];
            if (!sub.getName().endsWith(".dat")) continue;
            numOfCommits.incrementAndGet();
            //System.out.println("Commit " + i + ": " + file.getName());
            @SuppressWarnings("unchecked")
            HashMap<String, HashMap<String, ChangeGraph>> fileChangeGraphs = (HashMap<String, HashMap<String, ChangeGraph>>) FileIO.readObjectFromFile(sub.getAbsolutePath());
            for (String fp : fileChangeGraphs.keySet()) {
                //System.out.println(fp);
                HashMap<String, ChangeGraph> cgs = fileChangeGraphs.get(fp);
                for (String method : cgs.keySet()) {
                    //System.out.println(method);
                    int index = sub.getName().indexOf('.');
                    if (index < 0) {
                        index = sub.getName().length();
                    }
                    String name = FileIO.getSimpleFileName(sub.getName().substring(0, index)) + "," + fp + "," + method;
                    ChangeGraph cg = cgs.get(method);
                    if (cg.getNodes().size() <= 1) continue;
                    numOfGraphs.incrementAndGet();
                    GROUMGraph g = new GROUMGraph(cg, name);
                    // FIXME
                    g.pruneDoubleEdges();
                    g.setProject(projectName);
                    graphs.add(g);
                }
            }
        }
        return graphs;
    }
}
