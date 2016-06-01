import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.AcyclicSP;
    
public class SeamCarver {
    private Picture picture;
    public SeamCarver(Picture picture) {
        // create a seam carver object based on the given picture
        this.picture = new Picture(picture);
    }
    public Picture picture() {
        // current picture
        return picture;
    }
    public int width() {
        // width of current picture
        return picture.width();
    }
    public int height() {
        // height of current picture
        return picture.height();
    }
    public double energy(int x, int y) {
        // energy of pixel at column x and row y
        if (x > width() - 1 || y > height() - 1 || x < 0 || y < 0) 
            throw new java.lang.IndexOutOfBoundsException();
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) 
            return 1000;
        double Rx = picture.get(x + 1, y).getRed() - 
            picture.get(x - 1, y).getRed();
        double Gx = picture.get(x + 1, y).getGreen() - 
            picture.get(x - 1, y).getGreen();
        double Bx = picture.get(x + 1, y).getBlue() - 
            picture.get(x - 1, y).getBlue();
        double Ry = picture.get(x, y + 1).getRed() - 
            picture.get(x, y - 1).getRed();
        double Gy = picture.get(x, y + 1).getGreen() - 
            picture.get(x, y - 1).getGreen();
        double By = picture.get(x, y + 1).getBlue() - 
            picture.get(x, y - 1).getBlue();
        return Math.sqrt(Rx * Rx + Gx * Gx + Bx * Bx + 
                         Ry * Ry + Gy * Gy + By * By);
    }
    public int[] findHorizontalSeam() {
        // sequence of indices for horizontal seam
        int width = height();
        int height = width();
        int vertices = width * height + 2;
        int top = vertices - 2;
        int bottom = vertices - 1;
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(vertices);
        // add top
        for (int i = 0; i < width; i++) {
            int s = i;
            graph.addEdge(new DirectedEdge(top, s, energy(0, i)));
        }
        // add intermediate
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height - 1; j++) {
                int current = j * width + i;
                int s = current + width;
                graph.addEdge(new DirectedEdge(current, s, energy(j + 1, i)));
                // left or right case
                if (i > 0) {
                    int sw = current + width - 1;
                    graph.addEdge(new DirectedEdge(current,
                                                   sw, energy(j + 1, i - 1)));
                }
                if (i < width - 1) {
                    int se = current + width + 1;
                    graph.addEdge(new DirectedEdge(current,
                                                   se, energy(j + 1, i + 1)));
                }
            }
        }
        // add bottom
        for (int i = 0; i < width; i++) {
            int current = (height - 1) * width + i;
            graph.addEdge(new DirectedEdge(current, bottom, 0));
        }        
        AcyclicSP sp = new AcyclicSP(graph, top);
        int[] path = new int[height];
        int i = 0;
        for (DirectedEdge e : sp.pathTo(bottom)) {
            if (i < height) { 
                path[i] = e.to() % width;
            }
            i += 1;
        }
        return path;
    }
    public   int[] findVerticalSeam() {
        // sequence of indices for vertical seam
        int width = width();
        int height = height();
        int vertices = width * height + 2;
        int top = vertices - 2;
        int bottom = vertices - 1;
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(vertices);
        // add top
        for (int i = 0; i < width; i++) {
            int s = i;
            graph.addEdge(new DirectedEdge(top, s, energy(i, 0)));
        }
        // add intermediate
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height - 1; j++) {
                int current = j * width + i;
                int s = current + width;
                graph.addEdge(new DirectedEdge(current, s, energy(i, j + 1)));
                // left or right case
                if (i > 0) {
                    int sw = current + width - 1;
                    graph.addEdge(new DirectedEdge(current,
                                                   sw, energy(i - 1, j + 1)));
                }
                if (i < width - 1) {
                    int se = current + width + 1;
                    graph.addEdge(new DirectedEdge(current,
                                                   se, energy(i + 1, j + 1)));
                }
            }
        }
        // add bottom
        for (int i = 0; i < width; i++) {
            int current = (height - 1) * width + i;
            graph.addEdge(new DirectedEdge(current, bottom, 0));
        }        
        AcyclicSP sp = new AcyclicSP(graph, top);
        int[] path = new int[height];
        int i = 0;
        for (DirectedEdge e : sp.pathTo(bottom)) {
            if (i < height) { 
                path[i] = e.to() % width;
            }
            i += 1;
        }
        return path;
    }
    public void removeHorizontalSeam(int[] seam) {
        // remove horizontal seam from current picture
        if (seam == null) throw new java.lang.NullPointerException();
        if (seam.length != width() || height() <= 1) 
            throw new java.lang.IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
            if (i > 0) {
                if (Math.abs(seam[i - 1] - seam[i]) > 1) 
                    throw new java.lang.IllegalArgumentException();
            }
            if (seam[i] < 0 || seam[i] > height() - 1)
                throw new java.lang.IllegalArgumentException();
        }        
        int width = width();
        int height = height();
        Picture newPicture = new Picture(width, height - 1);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int removeY = seam[x];
                if (y < removeY) {
                    newPicture.set(x, y, picture.get(x, y));
                }
                if (y > removeY) {
                    newPicture.set(x, y - 1, picture.get(x, y));
                }
            }
        }
        picture = newPicture;
    }
    public void removeVerticalSeam(int[] seam) {
        // remove vertical seam from current picture
        if (seam == null) throw new java.lang.NullPointerException();
        if (seam.length != height() || width() <= 1) 
            throw new java.lang.IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
            if (i > 0) {
                if (Math.abs(seam[i - 1] - seam[i]) > 1) 
                    throw new java.lang.IllegalArgumentException();
            }
            if (seam[i] < 0 || seam[i] > width() - 1)
                throw new java.lang.IllegalArgumentException();
        }        
        int width = width();
        int height = height();
        Picture newPicture = new Picture(width - 1, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int removeX = seam[y];
                if (x < removeX) {
                    newPicture.set(x, y, picture.get(x, y));
                }
                if (x > removeX) {
                    newPicture.set(x - 1, y, picture.get(x, y));
                }
            }
        }
        picture = newPicture;        
    }
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver seam = new SeamCarver(picture);
        System.out.println(seam.energy(0, 1));
        System.out.println(seam.energy(1, 1));
        System.out.println(seam.energy(1, 2));
        System.out.println(seam.energy(2, 3));
        int[] path = seam.findHorizontalSeam();
        for (int i : path) {
            System.out.println(i);
        }
        seam.removeHorizontalSeam(path);
        seam.findVerticalSeam();
    }
}