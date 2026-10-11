public class BST {
    private static class Node {
        int key;
        Node left, right;

        Node(int key) {
            this.key = key;
        }
    }

    private Node root;

    public void insert(int key) {
        root = insert(root, key);
    }

    private Node insert(Node node, int key) {
        if (node == null) return new Node(key);

        if (key < node.key) {
            node.left = insert(node.left, key);
        } else if (key > node.key) {
            node.right = insert(node.right, key);
        }

        return node;
    }

    public boolean search(int key) {
        Node current = root;

        while (current != null) {
            if (key == current.key) return true;
            current = key < current.key
                    ? current.left : current.right;
        }

        return false;
    }

    public void delete(int key) {
        root = delete(root, key);
    }

    private Node delete(Node node, int key) {
        if (node == null) return null;

        if (key < node.key) {
            node.left = delete(node.left, key);
        } else if (key > node.key) {
            node.right = delete(node.right, key);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            Node successor = node.right;
            while (successor.left != null) {
                successor = successor.left;
            }

            node.key = successor.key;
            node.right = delete(node.right, successor.key);
        }

        return node;
    }
}
