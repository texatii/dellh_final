package com.mycompany.testrunner;
import java.io.*;
import java.util.*;

//Part C: Person Class
class Person {
    int id;
    String name;
    String gender;
    Integer fatherId;
    Integer motherId;
    Integer spouseId;

    public Person(int id, String name, String gender, Integer fatherId, Integer motherId, Integer spouseId) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.fatherId = fatherId;
        this.motherId = motherId;
        this.spouseId = spouseId;
    }
}

public class Genealogy {
    //Part C: Data Structures
    private Map<Integer, Person> peopleById = new HashMap<>();
    private Map<String, Integer> nameToId = new HashMap<>();

    //Part C: 3.Parser
    public void loadFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                int id = Integer.parseInt(data[0].trim());
                String name = data[1].trim();
                String gender = data[2].trim();
                Integer fid = parseId(data[3]);
                Integer mid = parseId(data[4]);
                Integer sid = parseId(data[5]);

                Person p = new Person(id, name, gender, fid, mid, sid);
                peopleById.put(id, p);
                nameToId.put(name, id);
            }
        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    private Integer parseId(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return null; }
    }

    private Person get(int id) { return peopleById.get(id); }
    public Person getPersonByName(String name) { 
        if(!nameToId.containsKey(name)) return null;
        return peopleById.get(nameToId.get(name));
    }

    //Part D: 1.Father/Mother
    public boolean isFather(int idA, int idB) {
        Person child = get(idB);
        return child != null && child.fatherId != null && child.fatherId == idA;
    }
    public boolean isMother(int idA, int idB) {
        Person child = get(idB);
        return child != null && child.motherId != null && child.motherId == idA;
    }

    //Part D: 2.Child/Sibling
    public boolean isChild(int idA, int idB) { return isFather(idB, idA) || isMother(idB, idA); }

    public boolean isSibling(int idA, int idB) {
        if (idA == idB) return false;
        Person pA = get(idA); Person pB = get(idB);
        if (pA == null || pB == null) return false;
        boolean sameDad = pA.fatherId != null && Objects.equals(pA.fatherId, pB.fatherId);
        boolean sameMom = pA.motherId != null && Objects.equals(pA.motherId, pB.motherId);
        return sameDad || sameMom;
    }

    //Part D: 3.Grandparent
    public boolean isGrandparent(int idA, int idB) {
        Person pB = get(idB);
        if (pB == null) return false;
        if (pB.fatherId != null && (isFather(idA, pB.fatherId) || isMother(idA, pB.fatherId))) return true;
        if (pB.motherId != null && (isFather(idA, pB.motherId) || isMother(idA, pB.motherId))) return true;
        return false;
    }
    public boolean isGrandchild(int idA, int idB) { return isGrandparent(idB, idA); }

    //Part D: 4.First Cousin
    public boolean isFirstCousin(int idA, int idB) {
        Person pA = get(idA); Person pB = get(idB);
        if (pA == null || pB == null) return false;
        Integer[] parentsA = {pA.fatherId, pA.motherId};
        Integer[] parentsB = {pB.fatherId, pB.motherId};
        for (Integer pa : parentsA) {
            for (Integer pb : parentsB) {
                if (pa != null && pb != null && isSibling(pa, pb)) return true;
            }
        }
        return false;
    }

    //Part E: Complex Relations
    public boolean isSpouse(int idA, int idB) {
        Person pA = get(idA);
        return pA != null && pA.spouseId != null && pA.spouseId == idB;
    }

    public boolean isHalfSibling(int idA, int idB) {
        if (idA == idB) return false;
        Person pA = get(idA); Person pB = get(idB);
        if (pA == null || pB == null) return false;
        boolean sameDad = pA.fatherId != null && Objects.equals(pA.fatherId, pB.fatherId);
        boolean sameMom = pA.motherId != null && Objects.equals(pA.motherId, pB.motherId);
        return (sameDad || sameMom) && !(sameDad && sameMom);
    }

    //Part D: 5.Relation String and CLI
    public String relation(String nameA, String nameB) {
        if (!nameToId.containsKey(nameA) || !nameToId.containsKey(nameB)) return "Unknown person(s)";
        int idA = nameToId.get(nameA);
        int idB = nameToId.get(nameB);

        if (isSpouse(idA, idB)) return nameA + " is spouse of " + nameB;
        if (isFather(idA, idB)) return nameA + " is father of " + nameB;
        if (isMother(idA, idB)) return nameA + " is mother of " + nameB;
        if (isChild(idA, idB)) return nameA + " is child of " + nameB;
        if (isHalfSibling(idA, idB)) return nameA + " is half-sibling of " + nameB;
        if (isSibling(idA, idB)) return nameA + " is sibling of " + nameB;
        if (isGrandparent(idA, idB)) return nameA + " is grandparent of " + nameB;
        if (isGrandchild(idA, idB)) return nameA + " is grandchild of " + nameB;
        if (isFirstCousin(idA, idB)) return nameA + " is first cousin of " + nameB;

        return "Not related";
    }

    public static void main(String[] args) {
        Genealogy gen = new Genealogy();
        gen.loadFromFile("persons.csv");

        if (args.length == 2) {
            System.out.println(gen.relation(args[0], args[1]));
        } else {
            System.out.println("Usage: java Genealogy \"Name1\" \"Name2\"");
            System.out.println("Demo: " + gen.relation("Ιωάννης Καποδίστριας", "Αυγουστίνος Καποδίστριας"));
        }
    }
}