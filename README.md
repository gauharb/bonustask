# BST vs. Hash Table

## 1. Introduction
This project compares the performance of a Binary Search Tree (BST) and a Hash Table. Both data structures support insertion, searching, and deletion.

## 2. Implementation
**Binary Search Tree (BST)**
The BST stores smaller keys in the left subtree and larger keys in the right subtree. It supports insertion, searching, and deletion. This implementation is not self-balancing.

**Hash Table**
The Hash Table uses separate chaining to handle collisions. It also resizes when the load factor exceeds 0.75.

## 3. Time Complexity
Let n be the number of stored elements.

| Operation | BST Best | BST Worst | Hash Table Expected | Hash Table Worst |
|---|---|---|---|---|
| Insertion | O(1) | O(n) | O(1) amortized | O(n) |
| Searching | O(1) | O(n) | O(1) | O(n) |
| Deletion | O(1) | O(n) | O(1) | O(n) |

The BST can become slow when elements are inserted in sorted order. Hash Table operations can become slow when many keys collide in the same bucket.

## 4. Experimental Results
The program measures insertion, searching, and deletion for 100, 1,000, and 10,000 elements.
The experiments include random input, sorted input for the BST, and a collision-heavy Hash Table.
The following table shows the results for 10,000 elements. Times are measured in milliseconds.

| Structure and Scenario | Insertion (ms) | Search (ms) | Deletion (ms) |
|---|---:|---:|---:|
| BST — Random | 2.184 | 1.221 | 1.720 |
| Hash Table — Random | 1.226 | 0.794 | 0.374 |
| BST — Sorted | 489.558 | 177.365 | 0.137 |
| Hash Table — Collisions | 147.196 | 132.532 | 94.843 |

The results are saved in `results/results.csv`.
For 10,000 random elements, the Hash Table was faster than the BST in searching and deletion. Sorted input significantly increased BST insertion and search times. Collisions also slowed down the Hash Table.
Execution times depend on the computer and Java runtime.

## 5. Conclusion
A Hash Table is useful when fast key-based access is required and key order is not important. A BST is useful when maintaining ordered data is important.
The experiments show that input order affects BST performance, while hash collisions affect Hash Table performance.
