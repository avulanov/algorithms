import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.BinaryStdIn;

public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading 
    // from standard input and writing to standard output
    public static void encode() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int length = csa.length();
        for (int i = 0; i < length; i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < length; i++) {
            BinaryStdOut.write(s.charAt((csa.index(i) + length - 1) % length));
        }
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler decoding, reading 
    // from standard input and writing to standard output
    public static void decode() {
        int start = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int length = s.length();
        char[] t = new char[length];
        int[] counts = new int[256];
        int[] index = new int[length];
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            t[i] = c;
            index[i] = counts[c];
            counts[c] += 1;
        }
        int[] offset = new int[256];
        int cum = 0;
        for (int i = 1; i < 256; i++) {
            cum = cum + counts[i - 1];
            offset[i] = cum;
        }
        int[] next = new int[length];
        // decode
        int prev = start;
        int cc = length - 1;
        while (cc >= 0) {
            next[cc] = prev;
            char c = t[prev];
            prev = offset[c] + index[prev];
            cc -= 1;
        }
        // print
        for (int i = 0; i < length; i++) {
            BinaryStdOut.write(t[next[i]]);
        }
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        String par = args[0];
        if (par.equals("-")) {
            encode();
        }
        if (par.equals("+")) {
            decode();
        }
    }
}