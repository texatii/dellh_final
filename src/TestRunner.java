// --- PART F: Unit Tests Runner ---
public class TestRunner {

    // A simple assertion helper
    public static void assertTrue(boolean condition, String testName) {
        if (condition) System.out.println("[PASS] " + testName);
        else System.out.println("[FAIL] " + testName);
    }

    public static void assertEquals(Object expected, Object actual, String testName) {
        if (expected.equals(actual)) System.out.println("[PASS] " + testName);
        else System.out.println("[FAIL] " + testName + " (Expected " + expected + ", got " + actual + ")");
    }

    public static void main(String[] args) {
        System.out.println("--- Running Unit Tests ---");

        // 1. TEST GENEALOGY
        Genealogy gen = new Genealogy();
        gen.loadFromFile("persons.csv");

        // Test C.4: Loading Checks
        Person p = gen.getPersonByName("Ιωάννης Καποδίστριας");
        assertTrue(p != null, "Load Ioannis Kapodistrias");
        if(p != null) assertEquals(3, p.id, "Check ID matches Name");

        // Test D.1: Parents
        // Augoustinos (1) is father of Ioannis (3)
        assertTrue(gen.isFather(1, 3), "isFather (1 -> 3)");

        // Test D.2: Siblings
        // Ioannis (3) and Eleni (4) share parents (1, 2)
        assertTrue(gen.isSibling(3, 4), "isSibling (3 <-> 4)");

        // Test D.3: Grandparents
        // Augoustinos (1) is grandfather of Dimitris (8) (via 3->6->8 ? No, 3 is uncle? Let's check CSV)
        // CSV: 8 is son of 6 & 7. 6 is son of 3. 3 is son of 1.
        // So 1 -> 3 -> 6 -> 8. 1 is Great-Grandparent.
        // Let's check immediate Grandparent: Ioannis (3) -> Alexandros (6) -> Dimitris (8)
        assertTrue(gen.isGrandparent(3, 8), "isGrandparent (3 -> 8)");

        // Test E: Spouse
        // Augoustinos (1) and Anastasia (2)
        assertTrue(gen.isSpouse(1, 2), "isSpouse (1 <-> 2)");

        // 2. TEST AVL TREE
        System.out.println("\n--- Testing AVL Tree ---");
        AVLTree tree = new AVLTree();
        tree.insert(10);
        tree.insert(20);
        tree.insert(30); // Should rotate. Root should be 20.

        assertTrue(tree.root.key == 20, "AVL Left Rotation (Root became 20)");

        tree.insert(10); // Duplicate
        assertTrue(tree.root.left.count == 2, "Duplicate Count Handling");

        System.out.println("--- Tests Completed ---");
    }
}