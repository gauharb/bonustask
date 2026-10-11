import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Tests {

    public static void main(String[] args) {
        testBST();
        testHashTable();
        testHashTableCollisions();
        testRandomOperations();

        System.out.println("All tests passed!");
    }

    private static void testBST() {
        BST bst = new BST();

        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        bst.insert(3);
        bst.insert(7);

        check(bst.search(10), "BST root search");
        check(bst.search(3), "BST left search");
        check(bst.search(15), "BST right search");
        check(!bst.search(99), "BST missing key");

        bst.delete(3);
        check(!bst.search(3), "BST delete leaf");

        bst.delete(5);
        check(!bst.search(5), "BST delete node with one child");

        bst.delete(10);
        check(!bst.search(10), "BST delete node with two children");

        check(bst.search(7), "BST remaining key");
        check(bst.search(15), "BST remaining right key");
    }

    private static void testHashTable() {
        HashTable table = new HashTable();

        table.insert(10, 100);
        table.insert(20, 200);
        table.insert(30, 300);

        check(table.search(10) == 100, "HashTable search");
        check(table.search(20) == 200, "HashTable second search");
        check(table.search(99) == null, "HashTable missing key");
        check(table.contains(30), "HashTable contains");
        check(!table.contains(99), "HashTable missing contains");

        table.insert(10, 999);
        check(table.search(10) == 999, "HashTable update existing key");
        check(table.size() == 3, "HashTable update size");

        check(table.delete(20), "HashTable delete existing key");
        check(!table.delete(20), "HashTable delete missing key");
        check(table.search(20) == null, "HashTable deleted key");
        check(table.size() == 2, "HashTable size after delete");

        for (int i = 0; i < 1000; i++) {
            table.insert(i, i * 10);
        }

        for (int i = 0; i < 1000; i++) {
            check(table.search(i) == i * 10,
                    "HashTable after resizing");
        }
    }

    private static void testHashTableCollisions() {
        HashTable table = new HashTable(4, key -> 1);

        for (int i = 0; i < 50; i++) {
            table.insert(i, i + 100);
        }

        for (int i = 0; i < 50; i++) {
            check(table.search(i) == i + 100,
                    "HashTable collision search");
        }

        check(table.longestChain() == 50,
                "HashTable longest chain");

        for (int i = 0; i < 25; i++) {
            check(table.delete(i), "HashTable collision deletion");
        }

        check(table.size() == 25, "HashTable collision size");
        check(table.longestChain() == 25,
                "HashTable chain after deletion");
    }

    private static void testRandomOperations() {
        BST bst = new BST();
        HashTable table = new HashTable();
        Map<Integer, Integer> reference = new HashMap<>();
        Random random = new Random(42);

        for (int i = 0; i < 20000; i++) {
            int key = random.nextInt(500);
            int operation = random.nextInt(3);

            if (operation == 0) {
                int value = random.nextInt(10000);
                bst.insert(key);
                table.insert(key, value);
                reference.put(key, value);

            } else if (operation == 1) {
                bst.delete(key);
                boolean expected = reference.containsKey(key);
                boolean actual = table.delete(key);

                check(actual == expected,
                        "HashTable randomized delete");

                reference.remove(key);

            } else {
                check(bst.search(key) == reference.containsKey(key),
                        "BST randomized search");

                Integer expected = reference.get(key);
                Integer actual = table.search(key);

                check(
                        expected == null
                                ? actual == null
                                : expected.equals(actual),
                        "HashTable randomized search"
                );
            }

            check(table.size() == reference.size(),
                    "HashTable randomized size");
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("Test failed: " + message);
        }
    }
}
