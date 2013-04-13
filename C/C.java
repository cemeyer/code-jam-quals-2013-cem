import java.math.BigInteger;
import java.io.*;
import java.util.*;

public class C {
    public static boolean debug = false ;

    public static void main(String[] args) {
        Scanner s = new Scanner(new BufferedInputStream(System.in));

        long T = new Scanner(s.nextLine()).nextLong();

        for (long i = 0; i < T; i++) {
            String[] ABs = s.nextLine().split(" ");
            String strA = ABs[0];
            String strB = ABs[1];

            BigInteger A = new BigInteger(strA);
            BigInteger B = new BigInteger(strB);

            if (debug)
                System.out.println("XXX " + (1+i) + " start==== (" + A + ", " + B + ")");
            System.out.println("Case #" + (1+i) + ": " + answer(strA, strB, A, B));
        }
    }

    public static String answer(String sA, String sB, BigInteger A, BigInteger B) {
        BigInteger rA = root(sA, A, true);
        BigInteger rB = root(sB, B, false);
        BigInteger ans = BigInteger.ZERO;

        if (debug) {
            System.out.println(rA);
            System.out.println(rB);
        }

        for (BigInteger i = rA; i.compareTo(rB) <= 0; i = i.add(BigInteger.ONE)) {
            if (!palindrome(i))
                continue;

            BigInteger iS = i.multiply(i);
            if (!palindrome(iS))
                continue;

            ans = ans.add(BigInteger.ONE);
        }

        return ans.toString();
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

    static boolean palindrome(BigInteger iii) {
        String s = iii.toString();
        for (int i = 0; i < s.length()/2; i++)
            if (s.charAt(i) != s.charAt(s.length() - 1 - i))
                return false;
        return true;
    }

}
