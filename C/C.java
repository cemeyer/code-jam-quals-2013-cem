import java.math.BigInteger;
import java.io.*;
import java.util.*;

public class C {
    public static boolean debug = false ;
    public static boolean time = false ;

    public static void main(String[] args) {
        Scanner s = new Scanner(new BufferedInputStream(System.in));

        long T = new Scanner(s.nextLine()).nextLong();
        long st = System.currentTimeMillis();

        for (long i = 0; i < T; i++) {
            String[] ABs = s.nextLine().split(" ");
            String strA = ABs[0];
            String strB = ABs[1];

            BigInteger A = new BigInteger(strA);
            BigInteger B = new BigInteger(strB);

            if (debug)
                System.out.println("XXX " + (1+i) + " start==== (" + A + ", " + B + ")");
            System.out.println("Case #" + (1+i) + ": " + answer(strA, strB, A, B));

            if (i%100 == 0 && time) {
                long now = System.currentTimeMillis();
                long diff = now - st;

                System.err.println("" + (1000.0 * i / diff ) + " t/sec");
            }
        }
    }

    public static String answer(String sA, String ssB, BigInteger A, BigInteger B) {
        BigInteger rA = root(sA, A, true);
        BigInteger rB = root(ssB, B, false);
        BigInteger ans = BigInteger.ZERO;

        if (debug) {
            System.out.println(rA);
            System.out.println(rB);
        }

        char[] si = rA.toString().toCharArray();
        char[] sB = rB.toString().toCharArray();

        char[][] siP = new char[1][];
        siP[0] = si;

        boolean brk = false;
        for (; !brk; incr10(siP)) {
            if (Arrays.equals(siP[0], sB))
                brk = true;

            if (!palindrome(siP[0]))
                continue;

            BigInteger i = new BigInteger(new String(siP[0]));

            BigInteger iS = i.multiply(i);
            if (!palindrome(iS.toString().toCharArray()))
                continue;

            ans = ans.add(BigInteger.ONE);
        }

        return ans.toString();
    }

    public static void incr10(char[][] siP) {
        char c = 1;
        char[] si = siP[0];

        for (int i = si.length-1; i >= 0; i--) {
            char d = (char)(si[i] + c);
            if (d > '9') {
                si[i] = '0';
                c = 1;
            } else {
                si[i] = d;
                c = 0;
                break;
            }
        }

        if (c > 0) {
            String longer = "1";
            for (int i = 0; i < si.length; i++)
                longer += "0";
            siP[0] = longer.toCharArray();
        }
    }

    public static BigInteger root(String s, BigInteger i, boolean ceil) {
        int D = s.length();
        int n;
        int l;

        if (D % 2 == 0) {
            n = (D-2)/2;
            l = 6;
        } else {
            n = (D-1)/2;
            l = 2;
        }

        String approx = "" + l;
        for (int j = 0; j < n; j++)
            approx += "0";

        BigInteger res = new BigInteger(approx);
        BigInteger two = new BigInteger("2");
        BigInteger prev = null;
        while (true) {
            BigInteger next = res.add(i.divide(res)).divide(two);

            // break cycles
            if (next.compareTo(res) == 0 || (prev != null && next.compareTo(prev) == 0))
                break;

            prev = res;
            res = next;
        }

        // find ceil/floor from res...
        BigInteger rs = res.multiply(res);
        if (ceil) {

            // while r^2 > i, r--
            while (rs.compareTo(i) > 0) {
                res = res.subtract(BigInteger.ONE);
                rs = res.multiply(res);
            }

            // r^2 <= i
            //
            // while r^2 < i, r++
            while (rs.compareTo(i) < 0) {
                res = res.add(BigInteger.ONE);
                rs = res.multiply(res);
            }

        } else {

            // while r^2 < i, r++
            while (rs.compareTo(i) < 0) {
                res = res.add(BigInteger.ONE);
                rs = res.multiply(res);
            }

            // r^2 >= i
            //
            // while r^2 > i, r--
            while (rs.compareTo(i) > 0) {
                res = res.subtract(BigInteger.ONE);
                rs = res.multiply(res);
            }
        }

        return res;
    } 

    static boolean palindrome(char[] s) {
        for (int i = 0; i < s.length/2; i++)
            if (s[i] != s[s.length - 1 - i])
                return false;
        return true;
    }

}
