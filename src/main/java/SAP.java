import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.LinkedList;

public class SAP {
    
    private Digraph graph = null;
    
    private class Result {
        private int length = -1;
        private int ancestor = -1;
        public Result(int length, int ancestor) {
            this.length = length;
            this.ancestor = ancestor;
        }
    }

   // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new java.lang.NullPointerException();
        graph = G;
    }
    
    private boolean validId(int v) {
        return v >= 0 && v < graph.V();
    }

   // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (!validId(v) || !validId(w)) throw 
            new java.lang.IndexOutOfBoundsException();
        LinkedList<Integer> vList = new LinkedList<Integer>();
        vList.add(v);
        LinkedList<Integer> wList = new LinkedList<Integer>();
        wList.add(w);
        return lca(vList, wList).length;
    }

    // a common ancestor of v and w that 
    // participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (!validId(v) || !validId(w)) throw 
            new java.lang.IndexOutOfBoundsException();        
        LinkedList<Integer> vList = new LinkedList<Integer>();
        vList.add(v);
        LinkedList<Integer> wList = new LinkedList<Integer>();
        wList.add(w);
        return lca(vList, wList).ancestor;
    }
    
    private Result lca(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new java.lang.NullPointerException();
        // check for the same ids
        for (int i : v) {
            for (int j : w) {
                if (i == j) return new Result(0, i);
            }
        }
        int[] label = new int[graph.V()];
        int[] dist = new int[graph.V()];
        Queue<Integer> queue = new Queue<Integer>();
        for (int i : v) {
            if (!validId(i)) throw new java.lang.IndexOutOfBoundsException();
            queue.enqueue(i);
            label[i] = -1;
        }
        for (int j : w) {
            if (!validId(j)) throw new java.lang.IndexOutOfBoundsException();
            queue.enqueue(j);
            label[j] = +1;
        }
        while (!queue.isEmpty()) {
            int current = queue.dequeue();
            for (int next : graph.adj(current)) {
                if (label[next] == 0) {
                    queue.enqueue(next);
                    label[next] = label[current];
                    dist[next] = dist[current] + 1;
                } else if (label[next] != label[current]) {
                    return new Result(dist[next] + dist[current] + 1, next);
                }
            }
        }
        return new Result(-1, -1);    
    }

   // length of shortest ancestral path between any 
   // vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return lca(v, w).length;
    }

   // a common ancestor that participates 
   // in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return lca(v, w).ancestor;
    }

   // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }        
    }
}
