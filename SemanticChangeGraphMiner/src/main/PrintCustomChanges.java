package main;

import groum.GROUMGraph;
import groum.GROUMNode;
import mining.Fragment;
import mining.Miner;
import mining.Pattern;
import utils.DirectoryHTML;
import utils.FileIO;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class PrintCustomChanges {
    private static final String PAIR_SEPARATOR = "~";
    public static void main(String[] args) {

        String content = FileIO.readStringFromFile("/Users/malinda/Documents/Research_Topic_2/SemanticChangeGraphMiner/SemanticChangeGraphMiner/selected-repos.csv");
        String changesPath = "/Users/malinda/Documents/Research_Topic_2/CPatMiner/AtomicASTChangeMining/OUTPUT/";
        Scanner sc = new Scanner(content);
        ArrayList<GROUMGraph> allGraphs = new ArrayList<>();
        while (sc.hasNextLine()) {

            String line = sc.nextLine();
            int index = line.indexOf(',');
            if (index == -1)
                index = line.length();
            final String name = line.substring(0, index);
            ArrayList<GROUMGraph> graphs = MineChangePatterns.readGraphs(changesPath, name);
            allGraphs.addAll(graphs);

        }
        System.out.println("Total graphs : "+allGraphs.size());
        mine(allGraphs);


    }

    private static boolean isRequiredMethod(GROUMNode node, GROUMNode pairedNode) {
        if (node!=null &&node.getDataName()!=null && node.getDataName().equals("numpy"))
            return true;
        if (pairedNode!=null&& pairedNode.getDataName()!=null &&pairedNode.getDataName().equals("numpy"))
            return true;
        if (pairedNode!=null&& pairedNode.getDataType()!=null &&pairedNode.getDataType().contains("numpy.ndarray")&&node!=null&& node.getDataType()!=null &&node.getDataType().contains("numpy.ndarray"))
            return false;
        if (pairedNode!=null&& pairedNode.getDataType()!=null &&pairedNode.getDataType().contains("numpy.ndarray"))
            return true;
        if (node!=null&& node.getDataType()!=null &&node.getDataType().contains("numpy.ndarray"))
            return true;
        return false;
    }

    private static void mine(ArrayList<GROUMGraph> graphs){
        HashMap<String, HashSet<GROUMNode[]>> nodesOfLabel = new HashMap<>();

        for (GROUMGraph groum : graphs) {
            for (GROUMNode node : groum.getNodes()) {
                if (node.getVersion() != 0 ) continue;
                GROUMNode mappedNode = node.getMappedNode();

                if (mappedNode == null) continue;
                if(!isRequiredMethod(node,mappedNode)) continue;
                String label = node.getLabel() + PAIR_SEPARATOR + mappedNode.getLabel();
                HashSet<GROUMNode[]> nodes = nodesOfLabel.get(label);
                if (nodes == null)
                    nodes = new HashSet<>();
                nodes.add(new GROUMNode[]{node, mappedNode});
                nodesOfLabel.put(label, nodes);
            }
        }

        System.out.println("Got all first pairs");
        HashMap<Integer, ArrayList<ArrayList<String>>> foundPatterns = new HashMap<>();
        int step = 4;
        File dir = new File("output/patterns" + "/OUTPUT-hybrid/ndarray/"  );
        for (String label : nodesOfLabel.keySet()) {
            HashSet<GROUMNode[]> pairs = nodesOfLabel.get(label);
            HashSet<Fragment> fragments = new HashSet<>();
            for (GROUMNode[] pair : pairs) {
                Fragment f = new Fragment(pair);
                fragments.add(f);
            }

            Pattern p = new Pattern(fragments, fragments.size());
            p.setId();
            Miner m = new Miner(step);
            m.setReposPath("/Users/malinda/Documents/Research_Topic_2/DATA_FOR_CPATMiner");
            ArrayList<ArrayList<String>> patterns = new ArrayList<>();
            m.printOutResults(dir, step, p, patterns, false);
            foundPatterns.put(step,patterns);

        }
        if (dir.exists()) {
            DirectoryHTML d = new DirectoryHTML();
            d.write(foundPatterns, dir);
        }
    }
}
