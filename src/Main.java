import java.util.Map;

public class Main {


    public static void main(String[] args) {

        CNFProblem problem = new CNFProblem("Test_unsat.cnf");

        procedureCDCL procedure = new procedureCDCL(problem);

        String result = procedure.executeCDCL();
        System.out.println(result);

        if ( result.startsWith("SAT") ) {
            System.out.println("Values not assigned:");
            for (Map.Entry<Integer, assignedLiteral> noAssigned : procedure.assignedValue.entrySet()) {

                if (noAssigned.getValue() == null)
                    System.out.println("[NOT-ASSIGNED] " + noAssigned.getKey() + " -> true/false ");

            }
        }

    }

}
