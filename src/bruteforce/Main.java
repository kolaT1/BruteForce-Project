package bruteforce;

import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the name of the file: ");
        String filename = input.nextLine();

        File file = new File(filename);
        if (!file.exists()) {
            System.out.println(" file does not exist.");
            System.exit(1);
        }

        int numVars = 0;
        ArrayList<ArrayList<Integer>> clauses = new ArrayList<>();
        Scanner fileInput = new Scanner(file);
        ArrayList<Integer> currentClause = new ArrayList<>();
        while (fileInput.hasNextLine()) {
            String line = fileInput.nextLine();
            if (line.trim().isEmpty() || line.startsWith("c")) {
                continue;
            } else if (line.startsWith("p")) {
                String[] parts = line.split(" ");
                numVars = Integer.parseInt(parts[2]);
            } else {
                String[] parts = line.split(" ");
                for (String part : parts) {
                    if (part.trim().isEmpty()) {
                        continue;
                    }
                    int literal = Integer.parseInt(part);
                    if (literal == 0) {
                        clauses.add(currentClause);
                        currentClause = new ArrayList<>();
                    } else {
                        currentClause.add(literal);
                    }
                }
            }
        }
        fileInput.close();

        // Generate assignments
        int[] assignment = new int[numVars];
        boolean satisfiable = generateAssignments(clauses, 0, assignment);
        if (satisfiable) {
            System.out.println("satisfiable");
        } else {
            System.out.println("unsatisfiable");
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Runtime: " + (endTime - startTime) / (1000 * 60) + " mins");
    }

    public static boolean generateAssignments(ArrayList<ArrayList<Integer>> clauses, int varIndex, int[] assignment) {
        if (varIndex == assignment.length) {
            return evaluateFormula(clauses, assignment);
        }
        for (int i = 0; i < 2; i++) {
            assignment[varIndex] = i;
            if (generateAssignments(clauses, varIndex + 1, assignment)) {
                return true;
            }
        }
        return false;
    }

    public static boolean evaluateFormula(ArrayList<ArrayList<Integer>> clauses, int[] assignment) {
        for (ArrayList<Integer> clause : clauses) {
            boolean satisfied = false;
            for (int literal : clause) {
                int varIndex = Math.abs(literal) - 1;
                int varValue = assignment[varIndex];
                if (literal < 0) {
                    varValue = 1 - varValue;
                }
                if (varValue == 1) {
                    satisfied = true;
                    break;
                }
            }
            if (!satisfied) {
                return false;
            }
        }
        return true;
    }
}
