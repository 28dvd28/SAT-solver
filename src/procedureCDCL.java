import java.util.*;

public class procedureCDCL {

    CNFProblem problem;
    CDCLprocedureStack procedureStack;
    Map<Integer, assignedLiteral> assignedValue;
    List<List<Integer>> learning;
    List<Integer> conflictClause;
    proofConstructor proofConstructor;
    Integer assertionLiteral;

    public procedureCDCL(CNFProblem problem){

        this.problem = problem;
        this.procedureStack = new CDCLprocedureStack();
        this.assignedValue = initializeVariables();

        this.learning = new ArrayList<>();
        this.conflictClause = new ArrayList<>();
        this.proofConstructor = new proofConstructor();
        this.assertionLiteral = 0;

    }

    private HashMap<Integer, assignedLiteral> initializeVariables(){

        // This method was designed to create a map that identifies for each literal the value assigned to it, if it was assigned.
        // To apply the VSIDS heuristic, it has been created a map ordered so that the first values are the most frequent ones
        // so that at runtime, to make the decision just iterate over the map and the first not assigned is decided.
        // All the others after it will be less frequent than the one selected.

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

        return "SAT" + "\nModel:\n" + this.procedureStack;

    }

}
