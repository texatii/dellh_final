import java.util.*;

// --- PART A: 1.1 Node Class ---
class Node {
    int key;
    int count;      // Handles duplicates
    int height;     // For AVL balancing
    Node left, right;

    public Node(int key) {
        this.key = key;
        this.count = 1;
        this.height = 1;
        this.left = null;
        this.right = null;
    }
}

// --- PART A & B: AVL Tree Implementation ---
public class AVLTree {
    Node root;

    // Helper: Get height safe against null
    private int height(Node N) {
        if (N == null) return 0;
        return N.height;
    }

    // Helper: Get balance factor
    private int getBalance(Node N) {
        if (N == null) return 0;
        return height(N.left) - height(N.right);
    }

    // Helper: Update height
    private void updateHeight(Node N) {
        if (N != null) {
            N.height = Math.max(height(N.left), height(N.right)) + 1;
        }
    }

    // --- PART B: 2.1 Rotations ---
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left = T2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        y.left = x;
        x.right = T2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    // --- PART A: 1.2 Insert ---
    public void insert(int key) {
        root = insertRec(root, key);
    }

    private Node insertRec(Node node, int key) {
        if (node == null) return new Node(key);

        if (key < node.key)
            node.left = insertRec(node.left, key);
        else if (key > node.key)
            node.right = insertRec(node.right, key);
        else {
            node.count++; // Handle duplicate
            return node;
        }

        updateHeight(node);
        int balance = getBalance(node);

        // Balance Checks
        if (balance > 1 && key < node.left.key) return rightRotate(node);
        if (balance < -1 && key > node.right.key) return leftRotate(node);
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }

    // --- PART A: 1.3 Delete ---
    public void delete(int key) {
        root = deleteRec(root, key);
    }

    private Node deleteRec(Node root, int key) {
        if (root == null) return root;

        if (key < root.key) root.left = deleteRec(root.left, key);
        else if (key > root.key) root.right = deleteRec(root.right, key);
        else {
            // Node Found
            if (root.count > 1) {
                root.count--; // Just decrease count
                return root;
            }
            // Real Deletion
            if ((root.left == null) || (root.right == null)) {
                Node temp = (root.left != null) ? root.left : root.right;
                if (temp == null) {
                    temp = root;
                    root = null;
                } else root = temp;
            } else {
                Node temp = minValueNode(root.right);
                root.key = temp.key;
                root.count = temp.count;
                temp.count = 1; // Force delete of successor
                root.right = deleteRec(root.right, temp.key);
            }
        }

        if (root == null) return root;

        updateHeight(root);
        int balance = getBalance(root);

        // Re-balance after delete
        if (balance > 1 && getBalance(root.left) >= 0) return rightRotate(root);
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        if (balance < -1 && getBalance(root.right) <= 0) return leftRotate(root);
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }
        return root;
    }

    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null) current = current.left;
        return current;
    }

    // --- PART B: 2.2 ChangeKey ---
    public void changeKey(int oldKey, int newKey) {
        Node target = find(root, oldKey);
        if (target != null) {
            int savedCount = target.count;
            for(int i=0; i<savedCount; i++) delete(oldKey);
            for(int i=0; i<savedCount; i++) insert(newKey);
        }
    }

    private Node find(Node node, int key) {
        if (node == null || node.key == key) return node;
        if (key < node.key) return find(node.left, key);
        return find(node.right, key);
    }

    // --- PART A: 1.4 Traversals ---
    public void inorder() { System.out.print("Inorder: "); inorderRec(root); System.out.println(); }
    private void inorderRec(Node node) {
        if (node != null) {
            inorderRec(node.left);
            System.out.print("(" + node.key + ":" + node.count + ") ");
            inorderRec(node.right);
        }
    }

    public void preorder() { System.out.print("Preorder: "); preorderRec(root); System.out.println(); }
    private void preorderRec(Node node) {
        if (node != null) {
            System.out.print("(" + node.key + ":" + node.count + ") ");
            preorderRec(node.left);
            preorderRec(node.right);
        }
    }

    public void postorder() { System.out.print("Postorder: "); postorderRec(root); System.out.println(); }
    private void postorderRec(Node node) {
        if (node != null) {
            postorderRec(node.left);
            postorderRec(node.right);
            System.out.print("(" + node.key + ":" + node.count + ") ");
        }
    }

    // Main Demo Method
    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
        // ID: 1234 -> Inputs: 1, 2, 3, 4, 1, 2 (Total 6 inputs)
        int[] inputs = {1, 2, 3, 4, 1, 2};
        for(int k : inputs) tree.insert(k);

        tree.inorder();
        tree.preorder();

        System.out.println("Changing key 1 to 99...");
        tree.changeKey(1, 99);
        tree.inorder();
    }
}