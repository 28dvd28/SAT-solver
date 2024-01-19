import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {

    private List<String> wrongLabeled = new ArrayList<>();
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
            else if (command_input.startsWith("all")) {
                if ( command_input.equals("all") )
                    allFilesCheck("none");
                else{
                    String[] fileLabel = command_input.split(" ");
                    if (fileLabel.length > 2)
                        System.out.println(">>>Wrong number of parameters");
                    else if ( !(fileLabel[1].equals("sat") || fileLabel[1].equals("unsat")) )
                        System.out.println(">>>To indicate the label you must use 'sat' or 'unsat' strings");
                    else
                        allFilesCheck(fileLabel[1]);
                }
            }
            else if ( command_input.startsWith("file")  )
                if ( command_input.equals(("file")) )
                    System.out.println(">>>Expected a file name after the command");
                else {
                    String fileName = command_input.split(" ")[1];
                    Path filePath = Paths.get("src","Input", fileName);
                    if (Files.exists(filePath)) {
                        singleFileCheck(filePath);
                    }
                    else
                        System.out.println(">>>File not found" + filePath);
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
        System.out.println("    > all: to check the satisfiability of all the files in the input folder");
        System.out.println("    > all sat/unsat: to check the satisfiability of all the files in the input folder that are all sat or unsat");
        System.out.println("                     indicated as parameter. At the end it will output also a percentage of the wrong answer");
        System.out.println("                     indicating the files which output was wrong");
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
        executeProcedure(file, "none");
    }

    private static void allFilesCheck(String expectedLabelling) throws Exception {

        /**
         * execute the sat solver procedure over all the files inside the input folder
         */

        String directoryPath = "src/Input";
        File directory = new File(directoryPath);

        List<String> wrongLabeled = new ArrayList<>();

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    boolean isWrong = executeProcedure(file, expectedLabelling);
                    if (isWrong)
                        wrongLabeled.add(file.toString());
                }
            }
        }

        if ( expectedLabelling.equals("sat") || expectedLabelling.equals("unsat") ) {
            System.out.println("Wrong sat solver returning values: " + wrongLabeled.size());
            System.out.println("Over the files: ");
            if (!wrongLabeled.isEmpty())
                for (String s : wrongLabeled)
                    System.out.println("    " + s);
        }
    }

    private static boolean executeProcedure(File file, String expectedLabelling) throws Exception {

        /**
         * In this procedure first is checked if the file is in cnf form or not,
         * if it is then we can execute the CDCL, otherwise it is checked if it
         * is a txt file, if so it is translated from propositional logic to cnf
         * then is executed the CDCL
         */

        if (file.toString().endsWith(".cnf")) {

            boolean wrong = false;

            System.out.println("\nExecuting SAT SOLVER over file " + file);

            long start = System.currentTimeMillis();

            CNFProblem problem = new CNFProblem(file);

            procedureCDCL procedure = new procedureCDCL(problem);

            String result = procedure.executeCDCL();

            if (expectedLabelling.equals("sat") && result.startsWith("UNSAT"))
                wrong = true;
            if (expectedLabelling.equals("unsat") && result.startsWith("SAT"))
                wrong = true;

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

            System.out.println("Completed evaluation for: " + file + " in time: " + (stop-start) + "ms");
            return wrong;

        }
        else if ( file.toString().endsWith(".txt")){

            System.out.print("\nTransforming into cnf the file " + file);

            propositionalLogicToNormalForm transformer = new propositionalLogicToNormalForm(file.toString());
            File cnfFormFile = new File(transformer.outputFile);
            return executeProcedure(cnfFormFile, expectedLabelling);

        }
        else
            System.out.println(">>>File not valid, the file must be a .cnf file or a .txt file.");
            return false;
    }

}
