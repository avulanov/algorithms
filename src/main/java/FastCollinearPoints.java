import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] mySegments = null;
    private int num = 0;
    public FastCollinearPoints(Point[] points) throws 
        java.lang.NullPointerException, java.lang.IllegalArgumentException {
        // finds all line segments containing 4 or more points
        if (points == null) throw 
            new java.lang.NullPointerException("Null array");
        for (Point p : points) {
            if (p == null) throw 
                new java.lang.NullPointerException("Null entry");
        }
        int length = points.length;
        Point[] copy = new Point[length];
        for (int i = 0; i < length; i++) {
            copy[i] = points[i];
        }
        mySegments = new LineSegment[length + 2];
        for (int i = 0; i < length; i++) {
            Arrays.sort(copy);
            Point origin = points[i];
            Arrays.sort(copy, origin.slopeOrder());
            int j = 0;
            int adj = 1;
            boolean order = true;
            while (j < length - 1) {
                if (origin.slopeTo(copy[j]) == origin.slopeTo(copy[j + 1])) {
                    adj += 1;
                    if (!(origin.compareTo(copy[j]) < 0 && 
                          origin.compareTo(copy[j + 1]) < 0)) {
                        order = false;
                    }
                } else {
                    if (adj >= 3 && order) {
                        mySegments[num] = new LineSegment(origin, copy[j]);
                        num += 1;
                    }
                    adj = 1;
                    order = true;
                }
                j += 1;
                // process end of cycle
                if ((j == length - 1) && adj >= 3 && order) {
                    mySegments[num] = new LineSegment(origin, copy[j]);
                    num += 1;
                    adj = 1;
                    order = true;
                }
            }
        }
    }
    public int numberOfSegments() {
        // the number of line segments
        return num;
    }
    public LineSegment[] segments() {
        // the line segments
        int length = numberOfSegments();
        LineSegment[] segments = new LineSegment[length];
        for (int i = 0; i < length; i++) {
            segments[i] = mySegments[i];
        }
        return segments;
    }
}