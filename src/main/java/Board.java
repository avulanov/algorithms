import java.util.Iterator;

public class Board {
    private int[][] blocks = null;
    private int j0 = 0;
    private int i0 = 0;
    public Board(int[][] blocks) {           
// construct a board from an N-by-N array of blocks
// (where blocks[i][j] = block in row i, column j)
        if (blocks == null) throw
            new java.lang.NullPointerException("Null input");
        if (blocks.length != blocks[0].length) throw
            new java.lang.IllegalArgumentException("Board not squared");
        this.blocks = new int[blocks.length][blocks[0].length];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                this.blocks[i][j] = blocks[i][j];
                if (blocks[i][j] == 0) {
                    i0 = i;
                    j0 = j;
                }
            }
        }
    }
    public int dimension() {
        // board dimension N
        return blocks.length;
    }
    public int hamming() {
        // number of blocks out of place
        int cost = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] != 0 && 
                    blocks[i][j] != i * dimension() + j + 1) {
                    cost += 1;
                }
            }
        }
        return cost;
    }
    public int manhattan() {
        // sum of Manhattan distances between blocks and goal
        int cost = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] != 0 && 
                    blocks[i][j] != i * dimension() + j + 1) {
                    int di = Math.abs(i - (blocks[i][j] - 1) / dimension());
                    int dj = Math.abs(j - (blocks[i][j] - 1) % dimension());
                    cost = cost + di + dj;
                }
            }
        }
        return cost;
    }
    public boolean isGoal() {
        // is this board the goal board?
        return manhattan() == 0;
    }
    public Board twin() {
        // a board that is obtained by exchanging any pair of blocks
        // if 2 x 2 return the same
        int dim = dimension();
        int[][] newBlocks = new int[dim][dim];
        boolean swap = false;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (!swap && i > 0 && 
                    blocks[i][j] != 0 && 
                    blocks[i-1][j] != 0) {
                    newBlocks[i-1][j] = blocks[i][j];
                    newBlocks[i][j] = blocks[i-1][j];
                    swap = true;
                } else {
                    newBlocks[i][j] = blocks[i][j];
                }
            }
        }
        return new Board(newBlocks);
    }
    public boolean equals(Object y) {
        // does this board equal y?
        if (y == null) return false;
        if (this == y) return true;
        if (!(y instanceof Board)) return false;
        Board that = (Board) y;
        if (that.dimension() != this.dimension()) return false;
        if (that.hamming() != this.hamming()) return false;
        if (that.manhattan() != this.manhattan()) return false;
        return true;
    }
    public Iterable<Board> neighbors() {
        // all neighboring boards        
        return new NeighborsIterable();
    }
    private class NeighborsIterable implements Iterable<Board> {
        public NeighborsIterator iterator() {
            return new NeighborsIterator();
        }
    }
    private class NeighborsIterator implements Iterator<Board> {
        private Board[] neighbors = new Board[4];
        private int n = 0;
        public NeighborsIterator() {
            int dim = dimension();
            for (int p = -1; p <= 1; p++) {
                for (int q = -1; q <= 1; q++) {
                    if (Math.abs(p) == Math.abs(q)) continue;
                    int in = i0 + p;
                    int jn = j0 + q;
                    if (in >= 0 && in < dim && jn >= 0 && jn < dim) {
                        int[][] newBlocks = new int[dim][dim];
                        for (int i = 0; i < dim; i++) {
                            for (int j = 0; j < dim; j++) {
                                newBlocks[i][j] = blocks[i][j];
                            }
                        }
                        newBlocks[i0][j0] = blocks[in][jn];
                        newBlocks[in][jn] = 0;
                        neighbors[n] = new Board(newBlocks);
                        n++;
                    }
                }
            }
        }
        public Board next() {
            n -= 1;
            if (n < 0) throw new java.util.NoSuchElementException();
            Board item = neighbors[n];
            return item;
        }
        public boolean hasNext() {
            return n > 0;
        }
        public void remove() {
        }
    }
    public String toString() {
        // string representation of this board (in the output format specified below)
        StringBuilder buf = new StringBuilder();
        int dim = dimension();
        buf.append(dimension());
        buf.append("\n");
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                buf.append(blocks[i][j]);
                buf.append(" ");
            }
            buf.append("\n");
        }
        return buf.toString();
    }
    public static void main(String[] args) {
    }
}