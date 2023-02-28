package bruteforce;

import java.util.*;
import java.io.*;
public class Main{
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
        while (fileInput.hasNextLine()) {
            String line = fileInput.nextLine();
            if (line.startsWith("c")) {
                continue;
            } else if (line.startsWith("p")) {
                String[] parts = line.split(" ");
                numVars = Integer.parseInt(parts[2]);
            } else {
                String[] parts = line.split(" ");
                ArrayList<Integer> clause = new ArrayList<>();
                for (String part : parts) {
                    if (!part.equals("0")) {
                        clause.add(Integer.parseInt(part));
                    }
                }
                clauses.add(clause);
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
        System.out.println("Runtime: " + (endTime - startTime) + " milliseconds");

    }

    //  recursion
    public static boolean generateAssignments(ArrayList<ArrayList<Integer>> clauses, int varIndex, int[] assignment) {
        if (varIndex == assignment.length) {
            return evaluateFormula(clauses, assignment);
        }
        assignment[varIndex] = 0;
        if (generateAssignments(clauses, varIndex + 1, assignment)) {
            return true;
        }
        assignment[varIndex] = 1;
        return generateAssignments(clauses, varIndex + 1, assignment);
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