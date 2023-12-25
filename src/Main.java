import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        String directoryPath = "src/Input";
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if ( files != null) {
                for (File file : files) {
                    if (file.toString().endsWith(".cnf")) {

                        CNFProblem problem = new CNFProblem(file);

                        procedureCDCL procedure = new procedureCDCL(problem);

                        String result = procedure.executeCDCL();

                        if (result.startsWith("SAT")) {
                            result = result.concat("\nModel:\n");
                            for (int i = 1; i<= procedure.problem.getVariableNumber(); i++){
                                if (procedure.assignedValue.get(i) == null)
                                    result = result.concat(i + " -> true\n");
                                else
                                    result = result.concat(i + " -> " + procedure.assignedValue.get(i).getValue() + "\n");
                            }

                        } else {
                            result = result.concat("\nThe proof for unsat is:\n");
                            result = result.concat(procedure.proofConstructor.toString());
                        }

                        String outputFile = "src/Output/" + file.toString().substring(10, file.toString().length()-4) + ".txt";

                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                            writer.write(result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Completed evaluation for file: " + file.toString());

                    }
                }
            }
        }
    }
}
