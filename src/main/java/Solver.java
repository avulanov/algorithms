import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

public class Solver {
    private class Node implements Comparable<Node> {
        private Board board = null;
        private int moves = 0;
        private Node prev = null;
        private int score = 0;
        public Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.score = board.manhattan() + moves;
        }
        public Board getBoard() { return board; }
        public int getMoves() { return moves; }
        public Node getPrev() { return prev; }
        public int getScore() { return score; }
        public int compareTo(Node that) {
            if (this == that) return 0;
            return Integer.compare(this.getScore(), that.getScore());            
        }
    }
    private MinPQ<Node> queue = new MinPQ<Node>();
    private MinPQ<Node> twinQueue = new MinPQ<Node>();
    private int totalMoves = -1;
    private Node solution = null;
    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        if (initial == null) throw new java.lang.NullPointerException("Null");
        queue.insert(new Node(initial, 0, null));
        twinQueue.insert(new Node(initial.twin(), 0, null));
        while (true) {
            if (queue.isEmpty() && twinQueue.isEmpty()) {
                break;
            }
            if (queue.isEmpty()) {
                break;
            } else {
                Node currentNode = queue.delMin();
                Board currentBoard = currentNode.getBoard();            
                if (currentBoard.isGoal()) {
                    solution = currentNode;
                    totalMoves = currentNode.getMoves();
                    break;
                } else {
                    for (Board neighbor : currentBoard.neighbors()) {
                        if (currentNode.getPrev() == null 
                                || !neighbor
                                .equals(currentNode.getPrev().getBoard())) {
                            queue.insert(new Node(neighbor,
                                                  currentNode.getMoves() + 1,
                                                  currentNode));
                        }
                    }
                }
            }
            if (!twinQueue.isEmpty()) {
                Node twinNode = twinQueue.delMin();
                Board twinBoard = twinNode.getBoard();            
                if (twinBoard.isGoal()) {
                    break;
                } else {
                    for (Board neighbor : twinBoard.neighbors()) {
                        if (twinNode.getPrev() == null 
                                || !neighbor
                                .equals(twinNode.getPrev().getBoard())) {
                            twinQueue.insert(new Node(neighbor,
                                                      twinNode.getMoves() + 1,
                                                      twinNode));
                        }
                    }
                }
            }            
        }
    }
    public boolean isSolvable() {
        // is the initial board solvable?
        return totalMoves > -1;
    }
    public int moves() {
        // min number of moves to solve initial board; -1 if unsolvable
        return totalMoves;
    }
    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if unsolvable
        if (!isSolvable()) return null;
        return new SolutionIterable();
    }
    private class SolutionIterable implements Iterable<Board> {
        public Iterator<Board> iterator() {
            return new SolutionIterator();
        }
    }
    private class SolutionIterator implements Iterator<Board> {
        private Stack<Board> stack = new Stack<Board>();
        public SolutionIterator() {
            Node currentNode = solution;
            do {
                stack.push(currentNode.getBoard());
                currentNode = currentNode.getPrev();
            } while (currentNode != null);
        }
        public Board next() {
            return stack.pop();
        }
        public boolean hasNext() {
            return !stack.isEmpty();
        }
        public void remove() {        
        }
    }
    public static void main(String[] args) {
    // solve a slider puzzle (given below)
    // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }        
    }
}