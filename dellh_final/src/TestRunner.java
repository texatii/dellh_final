// Part F: unit tests
public class TestRunner {

    // A simple assertion helper
    public static void assertTrue(boolean condition, String testName) {
        if (condition) System.out.println("[PASS] " + testName);
        else System.out.println("[FAIL] " + testName);
    }

    public static void assertEquals(Object expected, Object actual, String testName) {
        if (expected.equals(actual)) System.out.println("[PASS] " + testName);
        else System.out.println("[FAIL] " + testName + " (Expected " + expected + ",got" + actual + ")");
    }

    public static void main(String[] args) {
        System.out.println("Running Unit Tests");

        // 1: test relationship
        Genealogy gen = new Genealogy();
        gen.loadFromFile("persons.csv");

        // C.4: Loading Checks
        Person p = gen.getPersonByName("Ιωάννης Καποδίστριας");
        assertTrue(p != null, "Load Ioannis Kapodistrias");
        if(p != null) assertEquals(3, p.id, "Check ID matches Name");

        // Test D.1: Parents
        assertTrue(gen.isFather(1, 3), "isFather (1 -> 3)");

        // Test D.2: Siblings
        assertTrue(gen.isSibling(3, 4), "isSibling (3 <-> 4)");

        // Test D.3: Grandparents
        assertTrue(gen.isGrandparent(3, 8), "isGrandparent (3 -> 8)");

        // Test E: Spouse
        assertTrue(gen.isSpouse(1, 2), "isSpouse (1 <-> 2)");

        // 2. TEST AVL TREE
        System.out.println("\n Testing AVL Tree ");
        AVLTree tree = new AVLTree();
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);

        assertTrue(tree.root.key == 20, "AVL Left Rotation (Root became 20)");

        tree.insert(10);
        assertTrue(tree.root.left.count == 2, "Duplicate Count Handling");
        System.out.println("Tests Completed");
    }
}
