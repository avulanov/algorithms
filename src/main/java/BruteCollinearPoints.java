import java.util.Arrays;

public class BruteCollinearPoints {
    private int num = 0;
    private LineSegment[] mySegments = null;
    public BruteCollinearPoints(Point[] points) throws 
        java.lang.NullPointerException, java.lang.IllegalArgumentException {
        // finds all line segments containing 4 points
        if (points == null) throw 
            new java.lang.NullPointerException("Null array");
        for (Point p : points) {
            if (p == null) throw 
                new java.lang.NullPointerException("Null entry");
        }
        Arrays.sort(points);
        int length = points.length;
        mySegments = new LineSegment[length];
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                if (points[i].compareTo(points[j]) == 0) throw 
                    new java.lang.IllegalArgumentException("Repeated point");
                for (int k = j + 1; k < length; k++) {
                    for (int m = k + 1; m < length; m++) {
                        double slopeIJ = points[i].slopeTo(points[j]);
                        double slopeJK = points[j].slopeTo(points[k]);
                        double slopeKM = points[k].slopeTo(points[m]);
                        if (slopeIJ == slopeJK && slopeJK == slopeKM) {
                            mySegments[num] = 
                                new LineSegment(points[i], points[m]);
                            num++;
                        }
                    }
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