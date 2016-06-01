import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;

public class PointSET {
    private SET<Point2D> set = new SET<Point2D>();
    public PointSET() {
        // construct an empty set of points 
    }
    public boolean isEmpty() {
        // is the set empty?
        return set.isEmpty();
    }
    public int size() {
        // number of points in the set 
        return set.size();
    }
    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) throw new NullPointerException();
        set.add(p);
    }
    public boolean contains(Point2D p) {
        // does the set contain point p? 
        if (p == null) throw new NullPointerException();
        return set.contains(p);
    }
    public void draw() {
        // draw all points to standard draw
        for (Point2D point : set) {
            StdDraw.point(point.x(), point.y());        
        }
    }
    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle 
        if (rect == null) throw new NullPointerException();
        LinkedList<Point2D> list = new LinkedList<Point2D>();
        for (Point2D point : set) {
            if (rect.contains(point)) {
                list.add(point);
            }
        }
        return list;
    }
    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new NullPointerException();
        Point2D nearestPoint = null;
        double minDist = Double.MAX_VALUE;
        for (Point2D point : set) {
            double distance = point.distanceTo(p);
            if (distance < minDist) {
                minDist = distance;
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }
    public static void main(String[] args) {
        // unit testing of the methods (optional) 
    }
}