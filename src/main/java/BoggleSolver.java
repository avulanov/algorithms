import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.TrieSET;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;
    
public class BoggleSolver
{
    private BoggleTrie dictionary;
    private class BoggleTrie {
        class Node {
            private boolean isWord;
            private Node[] next;
            public Node() {
                isWord = false;
                next = new Node[26];
            }
        }
        private Node root;
        public BoggleTrie() {
            root = new Node();
        }
        public void add(String str) {
            add(root, str, 0);
        }
        private void add(Node node, String str, int pos) {
            int x = str.charAt(pos) - 'A';
            if (node.next[x] == null) {
                node.next[x] = new Node();
            }
            if (pos == str.length() - 1) {
                node.next[x].isWord = true;
                return;
            }
            add(node.next[x], str, pos + 1);
        }
        public boolean contains(String str) {
            return contains(root, str, 0);
        }
        private boolean contains(Node node, String str, int pos) {
            int x = str.charAt(pos) - 'A';
            if (node.next[x] == null) {
                return false;
            }
            if (pos == str.length() - 1) {
                return node.next[x].isWord;
            }
            return contains(node.next[x], str, pos + 1);
        }
        public boolean matchPrefix(String prefix) {
            if (prefix.length() < 1) return true;
            return matchPrefix(root, prefix, 0);
        }
        private boolean matchPrefix(Node node, String str, int pos) {
            int x = str.charAt(pos) - 'A';
            if (node.next[x] == null) {
                return false;
            }
            if (pos == str.length() - 1) {
                return true;
            }
            return matchPrefix(node.next[x], str, pos + 1);
        }
    }
    private class BoggleGraph {
        private BoggleBoard board;
        private int numVertices;
        public BoggleGraph(BoggleBoard board) {
            this.board = board;
            numVertices = board.rows() * board.cols();
        }
        public int v() {
            return numVertices;
        }
        public char getLetter(int vid) {
            return board.getLetter(vid / board.cols(), vid % board.cols());
        }
        public Iterable<Integer> adjList(int vid) {
            LinkedList<Integer> list = new LinkedList<Integer>();
            int rows = board.rows();
            int cols = board.cols();
            int vi = vid / cols;
            int vj = vid % cols;
            for (int i = vi - 1; i <= vi + 1; i++) {
                for (int j = vj - 1; j <= vj + 1; j++) {
                    if (i >= 0 && j >= 0 && i < rows && j < cols) {
                        int adjId = i * cols + j;
                        if (adjId != vid) {
                            list.add(adjId);
                        }                   
                    }
                }
            }
            return list;
        }
    }
    private class BoggleDFS {
        private boolean[] visited;
        private BoggleGraph graph;
        private BoggleTrie dictionary;
        public BoggleDFS(BoggleGraph graph, BoggleTrie dictionary) {
            this.graph = graph;
            this.dictionary = dictionary;
        }
        public TrieSET find() {
            visited = new boolean[graph.v()];
            TrieSET result = new TrieSET();
            for (int i = 0; i < graph.v(); i++) {
                dfs(i, new StringBuffer(), result);
            }
            //visited = null;
            return result;
        }
        private void dfs(int vid, StringBuffer buff, TrieSET result) {
            if (visited[vid] || !dictionary.matchPrefix(buff.toString())) return;
            visited[vid] = true;
            buff.append(graph.getLetter(vid));
            if (buff.charAt(buff.length() - 1) == 'Q') {
                buff.append('U');
            }
            for (int id : graph.adjList(vid)) {
                dfs(id, buff, result);
            }
            if (dictionary.contains(buff.toString())) {
                //StdOut.println(buff);
                result.add(buff.toString());
            }
            buff.deleteCharAt(buff.length() - 1);
            if (buff.length() > 0 && buff.charAt(buff.length() - 1) == 'Q') {
                buff.deleteCharAt(buff.length() - 1);
            }
            visited[vid] = false;
            return;
        }
    }
    // Initializes the data structure using the 
    // given array of strings as the dictionary.
    // (You can assume each word in the dictionary
    // contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dictionary = new BoggleTrie();
        for (String word : dictionary) {
            if (word.length() > 2) this.dictionary.add(new String(word));
        }
    }

    // Returns the set of all valid 
    // words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        BoggleGraph graph = new BoggleGraph(board);
        BoggleDFS dfs = new BoggleDFS(graph, dictionary);
        return dfs.find();
    }

    // Returns the score of the given word if it 
    // is in the dictionary, zero otherwise.
    // (You can assume the word 
    // contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int score = 0;
        int length = word.length();
        if (length < 3) return 0;
        else if (length < 5) return 1;
        else if (length == 5) return 2;
        else if (length == 6) return 3;
        else if (length == 7) return 5;
        else return 11;
    }
    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        int words = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
            words += 1;
        }
        StdOut.println("Score = " + score);
        StdOut.println("Words: " + words);
        StdOut.println(board);
    }
}
