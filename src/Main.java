import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Main {

    public static void main(String[] args) throws Exception {

        /**
         * The main consist simply in the implementation of a simple stdin/stdout interface
         * where different commands will execute the functions in which the execution starts.
         * The sat solver itself is implemented by the class procedureCDCL, so the function
         * implemented here just take the input, initialize the procedureCDCL object and
         * then save the result inside a file
         * **/

        Scanner scanner = new Scanner(System.in);

        String command_input = "";
        boolean begin = true;
        while ( !command_input.equals("quit") ) {

            System.out.println("\n=======================================");
            System.out.println("==========     SAT SOLVER     =========");
            System.out.println("=======================================\n");
            if (begin) {
                helpGuide();
                begin = false;
            }
            System.out.print(">>>");
            command_input = scanner.nextLine();

            if (command_input.equals("help"))
                helpGuide();
            else if (command_input.equals("all"))
                allFilesCheck();
            else if ( command_input.startsWith("file")  ) {
                if (command_input.equals(("file")) || command_input.equals(("file ")))
                    System.out.println(">>>Expected a file name after the command");
                else if (command_input.startsWith("file ")) {
                    String fileName = command_input.split(" ")[1];
                    Path filePath = Paths.get("src", "Input", fileName);
                    if (Files.exists(filePath)) {
                        singleFileCheck(filePath);
                    } else
                        System.out.println(">>>File not found" + filePath);
                }
            }
            else if ( command_input.equals("quit") )
                continue;
            else if ( command_input.equals("clear"))
                clearTerminal();
            else
                System.out.println(">>>Command not found, use help command to show the guide");


        }

    }


    private static void helpGuide(){

        /**
         * Simply output the help guide
         */

        System.out.println("Commands ");
        System.out.println("    > all: to check the satisfiability of all the files in the input folder.");
        System.out.println("           At the end insert 'y' to see a short preview of the sat solver execution");
        System.out.println("           or insert 'n' to go back to the main sat solver menu");
        System.out.println("    > file 'filename': to check the satisfiability of only the single file indicated");
        System.out.println("    > help: to show again this guide");
        System.out.println("    > clear: to clear the terminal");
        System.out.println("    > quit: to close the sat solver interface\n");
    }


    private static void clearTerminal() {

        /**
         * A function that allow just to clear the terminal
         */

        try {
            ProcessBuilder processBuilder;

            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                processBuilder = new ProcessBuilder("cmd", "/c", "cls");
            } else {
                processBuilder = new ProcessBuilder("clear");
            }

            Process process = processBuilder.inheritIO().start();

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }


    private static void singleFileCheck(Path filePath) throws Exception {

        /**
         * execute the procedure over only the single file given in input
         */

        File file = filePath.toFile();
        executeProcedure(file);

    }


    private static void allFilesCheck() throws Exception {

        /**
         * execute the sat solver procedure over all the files inside the input folder
         */

        Map<String, String> labeling = new LinkedHashMap<>();

        String directoryPath = "src/Input";
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    String isSat = executeProcedure(file);
                    labeling.put(file.toString(), isSat);
                }
            }
        }
        int satCount = 0;
        int unsatCount = 0;
        for ( String val : labeling.values())
            if (val.equals("SAT"))
                satCount++;
            else if ( val.equals("UNSAT"))
                unsatCount++;

        System.out.println("\nCompleted execution over " + labeling.size() + " files.\nResults: " + satCount + " SAT || " + unsatCount + " UNSAT");
        System.out.println("Printing results? [y/n]");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(">");
            String command_input = scanner.nextLine();
            if(command_input.equals("y")) {
                for (Map.Entry<String, String> val : labeling.entrySet())
                    System.out.println(val.getKey() + " -> " + val.getValue());
                break;
            }
            else if (command_input.equals("n"))
                break;
            else
                System.out.println("Command not valid, insert 'y' for printing results, insert 'n' to exit the execution");
        }

    }

    private static String executeProcedure(File file) throws Exception {

        /**
         * In this procedure first is checked if the file is in cnf form or not,
         * if it is then we can execute the CDCL, otherwise it is checked if it
         * is a txt file, if so it is translated from propositional logic to cnf
         * then is executed the CDCL
         */

        if (file.toString().endsWith(".cnf")) {

            String sat = "";

            System.out.println("\nExecuting SAT SOLVER over file " + file);

            long start = System.currentTimeMillis();

            CNFProblem problem = new CNFProblem(file);

            procedureCDCL procedure = new procedureCDCL(problem);

            String result = procedure.executeCDCL();

            if ( result.startsWith("UNSAT") )
                sat = "UNSAT";
            if ( result.startsWith("SAT") )
                sat = "SAT";

            if (result.startsWith("SAT")) {
                result = result.concat("\nModel:\n");
                for (int i = 1; i <= procedure.problem.getVariableNumber(); i++) {
                    if (procedure.assignedValue.get(i) == null)
                        result = result.concat(i + " -> true\n");
                    else
                        result = result.concat(i + " -> " + procedure.assignedValue.get(i).getValue() + "\n");
                }

            } else {

                if (procedure.proofConstructor.size() < 500) {
                    result = result.concat("\nThe proof for unsat is:\n");
                    result = result.concat(procedure.proofConstructor.toString());
                }else
                    result = result.concat("\nThe proof is too big, so it goes beyond the human understanding\n");
            }

            String outputFile = "src/Output/" + file.toString().substring(10, file.toString().length() - 4) + ".txt";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                writer.write(result);
            } catch (IOException e) {
                e.printStackTrace();
            }

            long stop = System.currentTimeMillis();


            System.out.println("===========================[ EVALUATION COMPLETED: PROBLEM STATS ]===========================");
            System.out.println("    Variables: " + procedure.problem.getVariableNumber());
            System.out.println("    Clauses:   " + procedure.problem.getClausesNumber());
            System.out.println("======================================[ SEARCH STATS ]=======================================");
            System.out.println("    Decisions: " + procedure.decisions_made);
            System.out.println("    Conflicts: " +procedure.conflict_number);
            System.out.println("    Time:      " + (stop-start) + "ms");
            System.out.println("    Result:    " + sat);
            if ( sat.equals("UNSAT") )
                if (procedure.proofConstructor.size()<500)
                    System.out.println("    Proof size: " + procedure.proofConstructor.size());
                else
                    System.out.println("    Proof size: over 500");
            System.out.println("=============================================================================================\n");
            return sat;

        }
        else if ( file.toString().endsWith(".txt")){

            System.out.print("\nTransforming into cnf the file " + file);

            propositionalLogicToNormalForm transformer = new propositionalLogicToNormalForm(file.toString());
            File cnfFormFile = new File(transformer.outputFile);
            return executeProcedure(cnfFormFile);

        }
        else
            System.out.println(">>>File not valid, the file must be a .cnf file or a .txt file.");
            return "Error";
    }

}
