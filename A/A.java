import java.io.*;
import java.util.*;

public class A {
    public static void main(String[] args) {
        Scanner s = new Scanner(new BufferedInputStream(System.in));
        long T = new Scanner(s.nextLine()).nextLong();
        for (long i = 0; i < T; i++) {
            char[] board = new char[16];
            for (int j = 0; j < 4; j++) {
                String l = s.nextLine();
                board[j*4 + 0] = l.charAt(0);
                board[j*4 + 1] = l.charAt(1);
                board[j*4 + 2] = l.charAt(2);
                board[j*4 + 3] = l.charAt(3);
            }
            s.nextLine(); // skip empty

            System.out.println("Case #" + (1 + i) + ": " + status(board));
        }
    }

    public static String status(char[] board) {
        String s;
        for (int i = 0; i < 4; i++) {
            // across
            s = stride(board, 0, i, 1, 0);
            if (s != null)
                return s;

            // down
            s = stride(board, i, 0, 0, 1);
            if (s != null)
                return s;
        }

        // diags
        s = stride(board, 0, 0, 1, 1);
        if (s != null)
            return s;

        s = stride(board, 3, 0, -1, 1);
        if (s != null)
            return s;

        if (any_empty(board))
            return "Game has not completed";

        return "Draw";
    }

    public static boolean any_empty(char[] board) {
        for (char c : board)
            if (c == '.')
                return true;
        return false;
    }

    public static String stride(char[] board, int x, int y, int dx, int dy) {
        char who = '?';
        int ymul = 4;

        for (int i = 0; i < 4; i++) {
            int p = y*ymul + x + i*dy*ymul + i*dx;

            if (who == '?' && board[p] != 'T' && board[p] != '.')
                who = board[p];

            if (board[p] != who && board[p] != 'T')
                return null;
        }

        return "" + who + " won";
    }
}
