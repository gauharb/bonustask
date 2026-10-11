
import java.io.File;
import java.io.PrintWriter;
import java.util.Random;

public class Benchmark {

    private static final int[] SIZES = {100, 1000, 10000};
    private static final int REPEATS = 5;
    private static volatile long sink;

    public static void main(String[] args) throws Exception {
        File resultsDir = new File("results");
        if (!resultsDir.exists() && !resultsDir.mkdirs()) {
            throw new IllegalStateException("Cannot create results directory");
        }

        try (PrintWriter out =
                     new PrintWriter(new File(resultsDir, "results.csv"))) {
            out.println("Structure,Input,Elements,Insert_ms,Search_ms,Delete_ms");

            for (int n : SIZES) {
                runBST(out, n, false);
                runBST(out, n, true);
                runHashTable(out, n, false);
                runHashTable(out, n, true);
            }
        }

        System.out.println("Benchmark completed. Results: results/results.csv");
        System.out.println("Checksum: " + sink);
    }

    private static void runBST(PrintWriter out, int n, boolean sorted) {
        long insertTotal = 0;
        long searchTotal = 0;
        long deleteTotal = 0;

        for (int r = 0; r < REPEATS; r++) {
            int[] keys = makeKeys(n, sorted, 100 + r);
            BST tree = new BST();

            long start = System.nanoTime();
            for (int key : keys) tree.insert(key);
            insertTotal += System.nanoTime() - start;

            start = System.nanoTime();
            for (int key : keys) {
                if (tree.search(key)) sink++;
            }
            searchTotal += System.nanoTime() - start;

            start = System.nanoTime();
            for (int key : keys) tree.delete(key);
            deleteTotal += System.nanoTime() - start;
        }

        writeRow(out, "BST", sorted ? "Sorted" : "Random", n,
                insertTotal, searchTotal, deleteTotal);
    }

    private static void runHashTable(PrintWriter out, int n,
                                     boolean collisions) {
        long insertTotal = 0;
        long searchTotal = 0;
        long deleteTotal = 0;

        for (int r = 0; r < REPEATS; r++) {
            int[] keys = makeKeys(n, false, 200 + r);

            HashTable table = collisions
                    ? new HashTable(8, key -> 1)
                    : new HashTable();

            long start = System.nanoTime();
            for (int key : keys) table.insert(key, key * 2);
            insertTotal += System.nanoTime() - start;

            start = System.nanoTime();
            for (int key : keys) {
                Integer value = table.search(key);
                if (value != null) sink += value;
            }
            searchTotal += System.nanoTime() - start;

            start = System.nanoTime();
            for (int key : keys) table.delete(key);
            deleteTotal += System.nanoTime() - start;
        }

        writeRow(out, "HashTable",
                collisions ? "Collisions" : "Random",
                n, insertTotal, searchTotal, deleteTotal);
    }

    private static int[] makeKeys(int n, boolean sorted, int seed) {
        int[] keys = new int[n];
        for (int i = 0; i < n; i++) keys[i] = i;

        if (!sorted) {
            Random random = new Random(seed);
            for (int i = n - 1; i > 0; i--) {
                int j = random.nextInt(i + 1);
                int temp = keys[i];
                keys[i] = keys[j];
                keys[j] = temp;
            }
        }

        return keys;
    }

    private static void writeRow(PrintWriter out, String structure,
                                 String input, int n,
                                 long insert, long search, long delete) {
        double divisor = REPEATS * 1_000_000.0;

        out.printf("%s,%s,%d,%.3f,%.3f,%.3f%n",
                structure, input, n,
                insert / divisor, search / divisor, delete / divisor);
    }
}
