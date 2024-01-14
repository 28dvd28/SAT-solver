import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        helpGuide();

        String command_input = "";
        while ( !command_input.equals("quit") ) {
            System.out.print(">>>");
            command_input = scanner.nextLine();

            if (command_input.equals("help"))
                helpGuide();
            else if (command_input.equals("all"))
                allFilesCheck();
            else if ( command_input.startsWith("file")  )
                if ( command_input.equals(("file")) )
                    System.out.println(">>>Expected a file name after the command");
                else {
                    String fileName = command_input.split(" ")[1];
                    Path filePath = Paths.get("src","Input", fileName);
                    if (Files.exists(filePath)) {
                        System.out.println("\nExecuting SAT SOLVER over file " + filePath);
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
        System.out.println("=======================================");
        System.out.println("==========     SAT SOLVER     =========");
        System.out.println("=======================================\n");
        System.out.println("Commands ");
        System.out.println("    > all: to check the satisfiability of all the files in the input folder");
        System.out.println("    > file 'filename': to check the satisfiability of only the single file indicated");
        System.out.println("    > help: to show again this guide");
        System.out.println("    > clear: to clear the terminal");
        System.out.println("    > quit: to close the sat solver interface\n");
    }

    private static void clearTerminal() {
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

    private static void singleFileCheck(Path filePath) {

        File file = filePath.toFile();
        executeProcedure(file);
    }

    private static void allFilesCheck() {

        String directoryPath = "src/Input";
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    executeProcedure(file);
                }
            }
        }
    }

    private static void executeProcedure(File file){

        if (file.toString().endsWith(".cnf")) {

            CNFProblem problem = new CNFProblem(file);

            procedureCDCL procedure = new procedureCDCL(problem);

            String result = procedure.executeCDCL();

            if (result.startsWith("SAT")) {
                result = result.concat("\nModel:\n");
                for (int i = 1; i <= procedure.problem.getVariableNumber(); i++) {
                    if (procedure.assignedValue.get(i) == null)
                        result = result.concat(i + " -> true\n");
                    else
                        result = result.concat(i + " -> " + procedure.assignedValue.get(i).getValue() + "\n");
                }

            } else {
                result = result.concat("\nThe proof for unsat is:\n");
                result = result.concat(procedure.proofConstructor.toString());
            }

            String outputFile = "src/Output/" + file.toString().substring(10, file.toString().length() - 4) + ".txt";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                writer.write(result);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Completed evaluation for file: " + file.toString());

        }
    }

}
