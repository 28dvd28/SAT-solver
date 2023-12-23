import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {


    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        CNFProblem problem = new CNFProblem("Test_unsat.cnf");

        procedureCDCL procedure = new procedureCDCL(problem);

        String result = procedure.executeCDCL();
        long endTime = System.currentTimeMillis();
        System.out.println(result);

        if ( result.startsWith("SAT") ) {
            String notAssignOutput = "";
            for (Map.Entry<Integer, assignedLiteral> noAssigned : procedure.assignedValue.entrySet()) {

                if (noAssigned.getValue() == null)
                    notAssignOutput = notAssignOutput.concat(" [NOT-ASSIGNED] " + noAssigned.getKey() + " -> true/false\n");

            }

            if (!notAssignOutput.isEmpty())
                System.out.println("Values not assigned:\n" + notAssignOutput);

        }
        else{
            System.out.println("The proof for unsat is: ");
            System.out.println(procedure.proofConstructor.toString());

        }

        System.out.println("Durata in millisecondi: " + (endTime - startTime));

    }

}
