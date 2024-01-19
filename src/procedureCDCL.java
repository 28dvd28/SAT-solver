import java.util.*;

public class procedureCDCL {

    /**
     * Class that implements the CDCL procedure.
     *
     * A little description over the variables in this class:
     *      problem: contains the CNF problem with the clauses and the two watched literal
     *      procedureStack: is an object that implements the CDCL stack
     *      assignedValue: is a map that bind a literals (Integer) to an assigned value. An assigned value tells if it is decided or implied.
     *                     If it is implied contains also the clause of its implication. If there is no assignment the value is null
     *      learning: is the list of all learned clauses
     *      conflictClause: when it is get a conflict clause it is saved in this list
     *      assertionLiteral: contains the literal that makes the conflict clause an assertion clause
     *      proofConstructor: an object that compute and save the proof in case it is an UNSAT problem
     */

    CNFProblem problem;
    List<List<Integer>> originalProblem;
    CDCLprocedureStack procedureStack;
    Map<Integer, assignedLiteral> assignedValue;
    Map<Integer, Integer> counterVSIDS;
    List<List<Integer>> learning;
    List<Integer> conflictClause;
    proofConstructor proofConstructor;
    Integer assertionLiteral;

    public procedureCDCL(CNFProblem problem){

        this.problem = problem;
        this.originalProblem = problem.getClauses();
        this.procedureStack = new CDCLprocedureStack();
        this.assignedValue = initializeVariables();

        counterVSIDS = new HashMap<>();
        for (Integer key : assignedValue.keySet()) {
            counterVSIDS.put(key, 0);
            counterVSIDS.put(-1*key, 0);
        }

        this.learning = new ArrayList<>();
        this.conflictClause = new ArrayList<>();
        this.proofConstructor = new proofConstructor();
        this.assertionLiteral = 0;

    }

    private LinkedHashMap<Integer, assignedLiteral> initializeVariables(){

        /**
         * This method was designed to create a map that identifies for each literal the value assigned to it, if it was assigned.
         * To apply the VSIDS heuristic, it has been created a map ordered so that the first values are the most frequent ones
         * so that at runtime, to make the decision just iterate over the map and the first not assigned is decided.
         * All the others after it will be less frequent than the one selected.
         */

        Map<Integer, Integer> counterOccurence = new HashMap<>();

        for (int i = 1; i <= this.problem.getVariableNumber(); i++){
            counterOccurence.put(i, 0);
        }

        for (Integer key : counterOccurence.keySet()){

            for (List<Integer> clause : this.problem.getClauses())
                if (clause.contains(key) || clause.contains(-1 * key))
                    counterOccurence.put(key, counterOccurence.get(key) + 1);

        }

        List<Map.Entry<Integer, Integer>> ordingList = new ArrayList<>(counterOccurence.entrySet());
        ordingList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        LinkedHashMap <Integer, assignedLiteral> ordinateMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> entry : ordingList){
            ordinateMap.put(entry.getKey(), null);
        }

        return ordinateMap;

    }


    public String executeCDCL(){

        /**
         *
         * The method that implements the core of the SAT solver. In order to get a more readable code
         * All the functions used in the following lines are implemented into two different static classes,
         * in order to use them without initialization.
         * The two classes are:
         *      -searchMode -> decision and propagation are implemented in this class
         *      -conflictSolvingMode -> explain and backtrack are implemented here
         *
         */

        if (searchMode.unitPropagation(this).equals("CONFLICT")){
            conflictSolvingMode.conflictAnalysis(this);
            return "UNSAT";
        }

        boolean afterBacktrackCycle = false;

        while ( !searchMode.problemIsTrue(this) ){

            if (!afterBacktrackCycle) {
                searchMode.pickBranchingVariable(this);
            }else{
                afterBacktrackCycle = false;
            }

            if (searchMode.unitPropagation(this).equals("CONFLICT")){

                searchMode.conflictCountingVSIDS(this);

                int decision_level = conflictSolvingMode.conflictAnalysis(this);

                if (decision_level < 0) {
                    return "UNSAT";
                }
                else {
                    conflictSolvingMode.backTrack(this, decision_level);
                    afterBacktrackCycle = true;
                }

            }

        }

        return "SAT";

    }

}
