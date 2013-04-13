import java.math.BigInteger;
import java.io.*;
import java.util.*;

class Chest {
    int num;
    int open;
    HashSet<Integer> keys;

    public Chest(int n, Scanner s) {
        num = n;
        open = s.nextInt();
        int nkeys = s.nextInt();

        keys = new HashSet<Integer>();

        for (int i = 0; i < nkeys; i++)
            keys.add(s.nextInt());
    }
}

public class D {
    public static boolean debug = false ;
    public static boolean time = true ;

    public static Random r = new Random(0xb7c9c4029dc57c9dL); // i rolled a 2**64 die

    public static long eval_states = 0;
    public static long st;

    public static void main(String[] args) {
        Scanner s = new Scanner(new BufferedInputStream(System.in));

        long T = new Scanner(s.nextLine()).nextLong();
        st = System.currentTimeMillis();

        zobristInit();

        for (long i = 0; i < T; i++) {
            Scanner KNs = new Scanner(s.nextLine());

            int K = KNs.nextInt();
            int N = KNs.nextInt();

            HashMap<Integer,Integer> keys = new HashMap<Integer,Integer>();

            Scanner Ks = new Scanner(s.nextLine());
            for (int j = 0; j < K; j++)
                putKey(keys, Ks.nextInt());

            Chest[] allchest = new Chest[N];
            for (int j = 0; j < N; j++) {
                allchest[j] = new Chest(1+j, new Scanner(s.nextLine()));
            }

            zobristInitOnce(allchest);

            System.out.println("Case #" + (1+i) + ": " + answer(K,N,keys,allchest));

            if (i%5 == 0 && time) {
                long now = System.currentTimeMillis();
                long diff = now - st;

                System.err.println("" + (1000.0 * i / diff ) + " t/sec");
            }
        }
    }

    public static String answer(int K, int N, HashMap<Integer,Integer> startingkeys, Chest[] closed) {
/*        HashMap<Integer, HashSet<Chest>> key_to_chests = new HashMap<Integer, HashSet<Chest>>();*/
        int[] sequence = new int[N];
/*
        for (Chest c : chests) {
            if (!key_to_chests.containsKey(c.open))
                key_to_chests.put(c.open, new HashSet<Chest>());
            key_to_chests.get(c.open).put(c);
        }
*/
        boolean success = recurse(sequence, 0, closed, startingkeys/*, key_to_chests*/);

        if (!success)
            return "IMPOSSIBLE";

        String res = "";
        for (int i = 0; i < N; i++) {
            res += sequence[i];
            if (i != N-1)
                res += " ";
        }

        return res;
    }

    public static boolean haveKey(HashMap<Integer,Integer> keys, int k) {
        if (keys.containsKey(k) && keys.get(k) > 0)
            return true;
        return false;
    }

    public static void takeKey(HashMap<Integer,Integer> keys, int k) {
        if (!keys.containsKey(k) || keys.get(k) <= 0)
            throw new AssertionError("awda");
        keys.put(k, keys.get(k)-1);
    }

    public static void putKey(HashMap<Integer,Integer> keys, int k) {
        if (!keys.containsKey(k)) {
            keys.put(k, 1);
            return;
        }
        keys.put(k, keys.get(k) + 1);
    }

    static HashMap<String,Long> zobristKeys;
    public static void zobristInit() {
        zobristKeys = new HashMap<String,Long>();
        for (int k = 1; k <= 200; k++)
            for (int i = 0; i < 400; i++)
                zobristKeys.put("" + k + ":" + i, r.nextLong());
    }

    // per-trial init
    static HashMap<Chest,Long> zobristChests;
    static HashSet<Long> zobristDeadEnds;
    public static void zobristInitOnce(Chest[] cs) {
        zobristDeadEnds = new HashSet<Long>();
        zobristChests = new HashMap<Chest,Long>();
        for (Chest c : cs)
            zobristChests.put(c, r.nextLong());
    }

    public static long zobristHash(Chest[] closed, HashMap<Integer,Integer> keys) {
        long hash = 0;
        for (Chest c : closed)
            if (c != null)
                hash ^= zobristChests.get(c);
        for (int keytype : keys.keySet())
            for (int i = 0; i < keys.get(keytype); i++)
                hash ^= zobristKeys.get("" + keytype + ":" + i);
        return hash;
    }

    public static void zobristSetDead(long h) {
        zobristDeadEnds.add(h);
    }

    // is this a dead end by zobrist hashing state?
    public static boolean zobristIsDead(long h) {
        return zobristDeadEnds.contains(h);
    }


    // We are solving for position 'pos' in 'seq';
    // each index i in 'closed' represents chest (i+1) = closed iff closed[i] != null
    // 'kc' is mapping from keys to set of chests they open
    public static boolean recurse(int[] seq, int pos, Chest[] closed, HashMap<Integer,Integer> keys/*, HashMap<Integer,HashSet<Chest>> kc*/) {
        if (pos >= seq.length)
            return true;

        eval_states++;
        if (eval_states % 500000 == 0 && time) {
            long now = System.currentTimeMillis();
            long diff = now - st;

            System.err.println("" + (1000.0 * eval_states / diff ) + " states/sec");
        }

        long zobhash = zobristHash(closed, keys);

        if (zobristIsDead(zobhash))
            return false;

        dumpState(seq, pos, closed, keys);

        for (int i = 0; i < closed.length; i++) {
            if (closed[i] != null) {
                Chest attempt = closed[i];

                if (!haveKey(keys, attempt.open))
                    continue;

                closed[i] = null;
                seq[pos] = attempt.num;
                takeKey(keys, attempt.open);

                for (int k : attempt.keys)
                    putKey(keys, k);

                boolean succ = recurse(seq, pos+1, closed, keys/*, kc*/);

                if (succ)
                    return succ;

                for (int k : attempt.keys)
                    takeKey(keys, k);

                putKey(keys, attempt.open);
                closed[i] = attempt;
            }
        }

        zobristSetDead(zobhash);

        return false;
    }

    public static void dumpState(int[] seq, int pos, Chest[] closed, HashMap<Integer,Integer> keys) {
        if (!debug)
            return;

        String s = "prev moves: {";
        for (int i = 0; i < pos; i++)
            s += "" + (seq[i]) + " ";
        s += "} pos=" + pos + " closed={";
        for (int i = 0; i < closed.length; i++) {
            if (closed[i] != null)
                s += "" + (closed[i].num) + " ";
        }
        s += "} keys={";
        for (int k : keys.keySet()) {
            if (haveKey(keys, k))
                s += "#" + k + ":" + keys.get(k) + " ";
        }
        s += "}";

        System.err.println(s);
    }
}
