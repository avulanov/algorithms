import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;

public class KdTree {
    private Node root;
    private int size = 0;
    public KdTree() {
        // construct an empty set of points 
    }
    private class Node {
        private double key;
        private Point2D value;
        private Node left, right;
        public Node(double key, Point2D value) {
            this.key = key;
            this.value = value;
        }
    }
    private Node put(Node node, double key, Point2D value, int level) {
        // comparisons etc.
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        // pick the right key for the next level
        boolean isX = level % 2 == 0;
        double nextLevelKey = isX ? value.y() : value.x();
        // comparisons cntd.
        int cmp = Double.compare(key, node.key);
        int l = level + 1;
        if (cmp < 0) {
            node.left = put(node.left, nextLevelKey, value, l);
        }
        if (cmp > 0) {
            node.right = put(node.right, nextLevelKey, value, l);
        }
        // do nothing if value equal and add to the left if only one
        if (cmp == 0) {
            if (!value.equals(node.value)) {
                node.left = put(node.left, nextLevelKey, value, l);
            }
        }
        return node;
    }
    public boolean isEmpty() {
        // is the set empty?
        return size == 0;
    }
    public int size() {
        // number of points in the set 
        return size;
    }
    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) throw new NullPointerException();
        root = put(root, p.x(), p, 0);
    }
    public boolean contains(Point2D p) {
        // does the set contain point p? 
        if (p == null) throw new NullPointerException();
        return contains(root, p.x(), p, 0);
    }
    private boolean contains(Node node, double key, Point2D value, int level) {
        // comparisons etc.
        if (node == null) return false;
        // pick the right key for the next level
        boolean isX = level % 2 == 0;
        double nextLevelKey = isX ? value.y() : value.x();
        // comparisons cntd.
        int cmp = Double.compare(key, node.key);
        int l = level + 1;
        boolean found = false;
        if (cmp < 0) {
            found = contains(node.left, nextLevelKey, value, l);
        }
        if (cmp > 0) {
            found = contains(node.right, nextLevelKey, value, l);
        }
        if (cmp == 0) {
            if (value.equals(node.value)) {
                return true;
            } else {
                found = contains(node.left, nextLevelKey, value, l);
            }
        }
        return found;
    }
    
    public void draw() {
        // draw all points to standard draw 
        traverse(root);
    }
    private void traverse(Node node) {
        if (node == null) return;
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.point(node.value.x(), node.value.y());
        traverse(node.left);
        traverse(node.right);
    }
    public Iterable<Point2D> range(RectHV rect) throws NullPointerException {
        // all points that are inside the rectangle 
        if (rect == null || root == null) throw new NullPointerException();
        LinkedList<Point2D> list = new LinkedList<Point2D>();
        range(root, rect, 0, list);
        return list;
    }
    private void range(Node node, RectHV rect, int level, LinkedList<Point2D> list) {
        if (node == null) return;
        int l = level + 1;
        boolean isX = level % 2 == 0;
        double minKey = isX ? rect.xmin() : rect.ymin();
        double maxKey = isX ? rect.xmax() : rect.ymax();
        // rectangle contains the point
        if (rect.contains(node.value)) {
            list.add(node.value);
            range(node.left, rect, l, list);
            range(node.right, rect, l, list);
        } else {
            // rectangle "key" to check at that level
            // intersects
            if (node.key > minKey && node.key < maxKey) {
                range(node.left, rect, l, list);
                range(node.right, rect, l, list);
            }
            // lower/left
            if (node.key > minKey && node.key >= maxKey) {
                range(node.left, rect, l, list);
            }
            // higher/right
            if (node.key <= minKey && node.key < maxKey) {
                range(node.right, rect, l, list);
            }
        }
    }
    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null || root == null) throw new NullPointerException();
        return nearest(root, p, 0, new Champion(root.value, Double.MAX_VALUE)).point;
    }
    private class Champion {
        private Point2D point;
        private double dist;
        public Champion(Point2D point, double dist) {
            this.point = point;
            this.dist = dist;
        }
    }
    private Champion nearest(Node node, Point2D p, int level, Champion champion) {
        if (node == null) return champion;
        int l = level + 1;
        boolean isX = level % 2 == 0;
        double key = isX ? p.x() : p.y();
        // check how close is the current point
        double currDist = p.distanceSquaredTo(node.value);
        Champion nextChampion = champion;
        if (currDist < nextChampion.dist) {
            nextChampion = new Champion(node.value, currDist);
        }
        boolean goLeft = (key <= node.key);
        if (goLeft) {
            nextChampion = nearest(node.left, p, l, nextChampion);
            double estDist = node.key - key;
            if (nextChampion.dist > estDist) {
                nextChampion = nearest(node.right, p, l, nextChampion);
            }
        } else {
            nextChampion = nearest(node.right, p, l, nextChampion);
            double estDist = node.key - key;
            if (nextChampion.dist > estDist) {
                nextChampion = nearest(node.left, p, l, nextChampion);
            }
        }
        return nextChampion;
    }
    public static void main(String[] args) {
        // unit testing of the methods (optional) 
        KdTree ps = new KdTree();
        System.out.println(ps.size());
        ps.insert(new Point2D(0.7, 0.2));
        ps.insert(new Point2D(0.5, 0.4));
        ps.insert(new Point2D(0.2, 0.3));
        ps.insert(new Point2D(0.4, 0.7));
        ps.insert(new Point2D(0.9, 0.6));
        System.out.println(ps.size());
        System.out.println(ps.contains(new Point2D(0.5, 0.2)));
        System.out.println(ps.nearest(new Point2D(1.1, 1.0)));
    }
}