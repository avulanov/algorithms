import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;

import java.util.HashMap;
import java.util.LinkedList;

public class WordNet {
    
    private Digraph graph = null;
    private HashMap<String, LinkedList<Integer>> dictionary = 
        new HashMap<String, LinkedList<Integer>>();
    private HashMap<Integer, String[]> index = new HashMap<Integer, String[]>();
    private SAP sap;
    
   // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw 
            new java.lang.NullPointerException();
        In synData = new In(synsets);
        int n = 0;
        // load synsets
        while (synData.hasNextLine()) {
            String[] tokens  = synData.readLine().split(",");
            int id = Integer.decode(tokens[0]);
            String[] nouns = tokens[1].split(" ");
            index.put(id, nouns);
            for (String noun : nouns) {
                LinkedList<Integer> idList = dictionary.get(noun);
                if (idList == null) {
                    idList = new LinkedList<Integer>();
                }
                idList.add(id);
                dictionary.put(noun, idList);
            }
            n += 1;
        }
        synData.close();
        graph = new Digraph(n);
        // load hypernyms
        In hypData = new In(hypernyms);
        while (hypData.hasNextLine()) {
            String[] tokens  = hypData.readLine().split(",");
            int id = Integer.decode(tokens[0]);
            for (int i = 1; i < tokens.length; i++) {
                int hypId = Integer.decode(tokens[i]);
                graph.addEdge(id, hypId);
            }
        }
        hypData.close();
        sap = new SAP(graph);        
    }

   // returns all WordNet nouns
    public Iterable<String> nouns() {
        return dictionary.keySet();
    }

   // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new java.lang.NullPointerException();
        return dictionary.get(word) != null;
    }

   // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw 
            new java.lang.NullPointerException();
        LinkedList<Integer> idsA = dictionary.get(nounA);
        LinkedList<Integer> idsB = dictionary.get(nounB);
        if (idsA == null || idsB == null) throw 
            new java.lang.IllegalArgumentException();
        // implement distance
        return sap.length(idsA, idsB);
    }

   // a synset (second field of synsets.txt) that 
   // is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw 
            new java.lang.NullPointerException();        
        LinkedList<Integer> idsA = dictionary.get(nounA);
        LinkedList<Integer> idsB = dictionary.get(nounB);
        if (idsA == null || idsB == null) throw 
            new java.lang.IllegalArgumentException();
        // implement distance
        int synsetId = sap.ancestor(idsA, idsB);
        String[] data = index.get(synsetId);
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            strBuf.append(data[i]);
            if (i < data.length - 1) {
                strBuf.append(" ");
            }
        }
        return strBuf.toString();
    }

   // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        System.out.println(wordNet.isNoun("change"));
        System.out.println(wordNet.sap("change", "freshener"));
        System.out.println(wordNet.distance("change", "freshener"));
    }
}