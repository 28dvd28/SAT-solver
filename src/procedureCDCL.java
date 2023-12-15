import java.util.*;

public class procedureCDCL {

    private CNFProblem problem;
    private Stack<LinkedHashMap<String, Boolean>> procedureStack;
    private Map<String, Boolean> assignedValue;
    private List<List<String>> actualProblem;
    private List<String> learning;
    private List<String> conflictClause;

    public procedureCDCL(CNFProblem problem){

        this.problem = problem;
        this.procedureStack = new Stack<>();
        this.assignedValue = new HashMap<>();

        for (int i = 1; i <= this.problem.getVariableNumber(); i++){
            this.assignedValue.put(String.valueOf(i), null);
        }

        this.actualProblem = problem.getClauses();
        this.learning = new ArrayList<>();
        this.conflictClause = new ArrayList<>();

    }

    public String executeCDCL(){

        /*if (unitPropagation() == "CONFLICT"){
            return "UNSAT";
        }*/

        while ( ! allVariableAssigned()){

            pickBranchingVariable();
            if (Objects.equals(unitPropagation(), "CONFLICT")){

                int decision_level = conflictAnalysis();

                if (decision_level < 0)
                        return "UNSAT";
                else
                    backTrack(decision_level);

            }

        }

        return "SAT" + " " + this.procedureStack;

    }

    private void pickBranchingVariable(){

        for (Map.Entry<String, Boolean> m : this.assignedValue.entrySet()){
            if (m.getValue() == null){

                this.assignedValue.put(m.getKey(), Boolean.TRUE);
                this.procedureStack.push(new LinkedHashMap<>(){{ put(m.getKey(), Boolean.TRUE); }});

            }
        }

    }

    private boolean allVariableAssigned(){
        return !this.assignedValue.containsValue(null);
    }

    private String unitPropagation(){

        LinkedHashMap<String, Boolean> currentLevel = this.procedureStack.pop();

        for (List<String> clause : this.actualProblem){

            List<String> copyClause = new ArrayList<>(clause);
            boolean valueOfTheClose = false;

            for (Map.Entry<String, Boolean> m : this.assignedValue.entrySet()){

                if ( valueOfTheClose )
                    break;

                if (copyClause.contains(m.getKey())) {
                    if ( m.getValue() == Boolean.TRUE )
                        valueOfTheClose = true;
                    else
                        copyClause.remove(m.getKey());

                }
                else if ( copyClause.contains("-" + m.getKey()) ) {
                    if ( m.getValue() == Boolean.FALSE )
                        valueOfTheClose = true;
                    else
                        copyClause.remove("-" + m.getKey());
                }

            }

            if ( valueOfTheClose )
                continue;
            else {

                if (copyClause.size() == 1) {
                    String literal = copyClause.get(0);
                    Boolean value;

                    if (literal.startsWith("-")) {
                        value = Boolean.FALSE;
                        literal = literal.substring(1);
                    } else {
                        value = Boolean.TRUE;
                    }

                    if (this.assignedValue.containsValue(literal)) {
                        this.assignedValue.put(literal, value);
                        currentLevel.put(literal, value);
                    }
                }
                else if (copyClause.isEmpty()) {
                    this.conflictClause = new ArrayList<>(clause);
                    return "CONFLICT";
                }
            }

        }

        this.procedureStack.push(currentLevel);
        return "NO-CONFLICT";
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
