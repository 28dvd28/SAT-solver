import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class searchMode {

    public static void pickBranchingVariable(procedureCDCL mainProcedure){

        for (Map.Entry<Integer, Boolean> m : mainProcedure.assignedValue.entrySet()){
            if (m.getValue() == null){

                mainProcedure.assignedValue.put(m.getKey(), Boolean.TRUE);
                mainProcedure.procedureStack.addDecidedLiteral(m.getKey(), Boolean.TRUE);

            }
        }

    }

    public static boolean allVariableAssigned(procedureCDCL mainProcedure){
        return !mainProcedure.assignedValue.containsValue(null);
    }

    public static String unitPropagation(procedureCDCL mainProcedure){

        if (mainProcedure.procedureStack.size() == 0)
            return levelZeroUnitPropagation(mainProcedure);
        else
            return afterDecisionsUnitPropagation(mainProcedure);

    }

    public static String levelZeroUnitPropagation(procedureCDCL mainProcedure){

        CNFProblem problem = mainProcedure.problem;

        List<List<Integer>> clauses = problem.getClauses();

        for (int i = 0; i < clauses.size()-1; i++) {

            if (clauses.get(i).size() == 1){

                Integer literal = clauses.get(i).get(0);
                Boolean value = Boolean.TRUE;
                if ( literal < 0 ) {
                    literal = Math.abs(literal);
                    value = Boolean.FALSE;
                }

                mainProcedure.procedureStack.addImpliedLiteral(literal, value, null);
                mainProcedure.assignedValue.put(literal, value);
            }

            for (int j = i + 1; j < clauses.size()-1; j++){
                if (clausesAreOpposite(clauses.get(i), clauses.get(j), mainProcedure))
                    return "UNSAT";

            }
        }

        return afterDecisionsUnitPropagation(mainProcedure);

    }

    public static boolean clausesAreOpposite(List<Integer> clause1, List<Integer> clause2, procedureCDCL mainProcedure){

        int equalsValues = 0;
        int differentValues = 0;
        Integer equalLiteral = 0;

        for (Integer s1: clause1){
            for ( Integer s2: clause2){

                if ( s1.equals(s2) ) {
                    equalsValues++;
                    equalLiteral = s1;
                }
                if ( !s1.equals(-1 * s2) )
                    differentValues++;
                if ( !s2.equals(-1 * s1) )
                    differentValues++;

            }
        }

        if (equalsValues == 0 && differentValues == 0)
            return true;
        else{
            if (equalsValues == 1 && differentValues == 0){

                Boolean value = Boolean.TRUE;
                if ( equalLiteral < 0 ) {
                    equalLiteral = Math.abs(equalLiteral);
                    value = Boolean.FALSE;
                }

                mainProcedure.procedureStack.addImpliedLiteral(equalLiteral, value, null);
                mainProcedure.assignedValue.put(equalLiteral, value);

            }
        }

        return false;
    }

    public static String afterDecisionsUnitPropagation(procedureCDCL mainProcedure){

        List<List<Integer>> problem = mainProcedure.problem.getClauses();

        for ( int i = 0; i < problem.size(); i++ ){

            List<Integer> clause = problem.get(i);

            Boolean valueOfTheClause = Boolean.FALSE;
            List<Integer> noValueLiteral = new ArrayList<>();

            for (Integer literal : clause){

                Boolean literalValue;
                if (literal < 0)
                    literalValue = !mainProcedure.assignedValue.get(Math.abs(literal));
                else
                    literalValue = mainProcedure.assignedValue.get(Math.abs(literal));

                if (mainProcedure.assignedValue.get(Math.abs(literal)) == null)
                    noValueLiteral.add(Math.abs(literal));
                else
                    valueOfTheClause = valueOfTheClause || literalValue;

            }

            if (noValueLiteral.isEmpty() && valueOfTheClause == Boolean.FALSE) {
                mainProcedure.conflictClause = new ArrayList<>(clause);
                return "UNSAT";
            }
            if (noValueLiteral.size() == 1 && valueOfTheClause == Boolean.FALSE){

                Integer propagateLiteral = noValueLiteral.get(0);
                mainProcedure.assignedValue.put(propagateLiteral, Boolean.TRUE);
                mainProcedure.procedureStack.addImpliedLiteral(propagateLiteral, Boolean.TRUE, clause);
                i = 0;

            }

        }

        return "NOT-UNSAT";

    }
}
