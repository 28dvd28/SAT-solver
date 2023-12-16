import java.util.*;

public class procedureCDCL {

    CNFProblem problem;
    CDCLprocedureStack procedureStack;
    Map<String, Boolean> assignedValue;
    List<List<String>> actualProblem;
    List<String> learning;
    List<String> conflictClause;

    public procedureCDCL(CNFProblem problem){

        this.problem = problem;
        this.procedureStack = new CDCLprocedureStack();
        this.assignedValue = initializeVariables();

        this.learning = new ArrayList<>();
        this.conflictClause = new ArrayList<>();

    }

    private HashMap<String, Boolean> initializeVariables(){

        // This method was designed to create a map that identifies for each literal the value assigned to it, if it was assigned.
        // To apply the VSIDS heuristic, it has been created a map ordered so that the first values are the most frequent ones
        // so that at runtime, to make the decision just iterate over the map and the first not assigned is decided.
        // All the others after it will be less frequent than the one selected.

        Map<String, Integer> counterOccurence = new HashMap<>();

        for (int i = 1; i <= this.problem.getVariableNumber(); i++){
            counterOccurence.put(String.valueOf(i), 0);
        }

        for (String key : counterOccurence.keySet()){

            for (List<String> clause : this.problem.getClauses())
                if (clause.contains(key) || clause.contains("-" + key))
                    counterOccurence.put(key, counterOccurence.get(key) + 1);

        }

        List<Map.Entry<String, Integer>> ordingList = new ArrayList<>(counterOccurence.entrySet());
        ordingList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        LinkedHashMap <String, Boolean> ordinateMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : ordingList){
            ordinateMap.put(entry.getKey(), null);
        }

        return ordinateMap;

    }


    public String executeCDCL(){

        if (searchMode.unitPropagation(this).equals("CONFLICT")){
            return "UNSAT";
        }

        while ( ! searchMode.allVariableAssigned(this)){

            searchMode.pickBranchingVariable(this);
            if (searchMode.unitPropagation(this).equals("CONFLICT")){

                int decision_level = conflictAnalysis();

                if (decision_level < 0)
                        return "UNSAT";
                else
                    backTrack(decision_level);

            }

        }

        return "SAT" + " " + this.procedureStack;

    }


    private void backTrack(int decision_level){

        LinkedHashMap<String, Boolean> current_level;

        if ( this.procedureStack.size() > decision_level ){

            current_level= this.procedureStack.pop();

            for ( String literal : current_level.keySet()){

                if ( this.assignedValue.containsValue(literal))
                    this.assignedValue.put(literal, null);

            }

        }

        current_level= this.procedureStack.pop();
        Iterator<String> iterator = current_level.keySet().iterator();
        String firstElement = iterator.next();
        Boolean newVal = ! current_level.get(firstElement);

        for ( String literal : current_level.keySet()){

            if ( this.assignedValue.containsValue(literal))
                this.assignedValue.put(literal, null);

        }

        LinkedHashMap<String, Boolean> newCurrentLevel = new LinkedHashMap<>(){{ put ( firstElement , newVal); }};
        this.procedureStack.push(newCurrentLevel);

    }


    private int conflictAnalysis(){


    }

}
