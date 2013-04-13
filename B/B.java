import java.io.*;
import java.util.*;

public class B {
    public static boolean debug = false;
    public static void main(String[] args) {
        Scanner s = new Scanner(new BufferedInputStream(System.in));

        long T = new Scanner(s.nextLine()).nextLong();
        for (long i = 0; i < T; i++) {
            Scanner NMs = new Scanner(s.nextLine());
            int N = NMs.nextInt();
            int M = NMs.nextInt();
            int[][] a = new int[N][];

            for (int j = 0; j < N; j++) {
                Scanner ls = new Scanner(s.nextLine());

                a[j] = new int[M];

                for (int k = 0; k < M; k++) {
                    a[j][k] = ls.nextInt();
                }
            }

            if (debug)
                System.out.println("XXX " + (1+i) + " start====");
            dumpMat(a);
            System.out.println("Case #" + (1+i) + ": " + answer(N,M,a));
            if (debug)
                System.out.println("XXX " + (1+i) + " end====");
        }
    }

    public static void dumpMat(int[][] m) {
        if (!debug)
            return;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++)
                System.out.print("" + m[i][j] + " ");
            System.out.println();
        }
    }

    // returns "YES" or "NO"
    public static String answer(int N, int M, int[][] hts) {
        /*
        if (N == 1 || M == 1)
            return "YES";
            */

        int[] downs = new int[N];
        int[] across = new int[M];

        for (int i = 0; i < N; i++)
            downs[i] = -1;

        for (int i = 0; i < M; i++)
            across[i] = -1;

        for (int d = 0; d < N; d++) {
            for (int a = 0; a < M; a++) {
                downs[d] = Math.max(downs[d], hts[d][a]);
                across[a] = Math.max(across[a], hts[d][a]);
            }
        }

        for (int d = 0; d < N; d++) {
            for (int a = 0; a < M; a++) {
                if (hts[d][a] < Math.min(downs[d], across[a]))
                    return "NO";
            }
        }

        return "YES";
    }

}
