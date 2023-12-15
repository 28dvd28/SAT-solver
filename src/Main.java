public class Main {


    public static void main(String[] args) {

        CNFProblem problem = new CNFProblem("Test_unsat.cnf");

        procedureCDCL procedure = new procedureCDCL(problem);
        String result = procedure.executeCDCL();

        System.out.println(result);

    }

}
