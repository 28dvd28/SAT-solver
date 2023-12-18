import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class searchMode {

    public static void pickBranchingVariable(procedureCDCL mainProcedure){

        for (Map.Entry<Integer, assignedLiteral> m : mainProcedure.assignedValue.entrySet()){
            if (m.getValue() == null){

                mainProcedure.assignedValue.put(m.getKey(), new assignedLiteral(m.getKey(), Boolean.TRUE).setDecided());
                mainProcedure.procedureStack.addDecidedLiteral(m.getKey(), Boolean.TRUE);
                break;

            }
        }

    }

    public static boolean allVariableAssigned(procedureCDCL mainProcedure){
        return !mainProcedure.assignedValue.containsValue(null);
    }

    public static String unitPropagation(procedureCDCL mainProcedure){

        if (mainProcedure.procedureStack.isEmpty())
            return levelZeroUnitPropagation(mainProcedure);
        else
            return afterDecisionsUnitPropagation(mainProcedure);

    }

    public static String levelZeroUnitPropagation(procedureCDCL mainProcedure){

        CNFProblem problem = mainProcedure.problem;

        List<List<Integer>> clauses = problem.getClauses();

        for (int i = 0; i < clauses.size()-1; i++) {

            if (clauses.get(i).size() == 1){

                for (int j = i + 1; j < clauses.size(); j++)
                    if (clausesAreOpposite(clauses.get(i), clauses.get(j), mainProcedure))
                        return "CONFLICT";

                Integer literal = clauses.get(i).get(0);
                Boolean value = Boolean.TRUE;
                if ( literal < 0 ) {
                    literal = Math.abs(literal);
                    value = Boolean.FALSE;
                }

                mainProcedure.procedureStack.addImpliedLiteral(literal, value, null);
                mainProcedure.assignedValue.put(literal, new assignedLiteral(literal, value).setImplied(null));
            }

            if (containsOppositeLiteral(clauses.get(i)))
                return "CONFLICT";

        }

        if ( !mainProcedure.procedureStack.isEmpty() )
            return afterDecisionsUnitPropagation(mainProcedure);
        else
            return "NOT-CONFLICT";

    }

    private static boolean containsOppositeLiteral(List<Integer> clause){

        for ( Integer l : clause)
            if (clause.contains(-1 * l))
                return true;

        return false;

    }

    public static boolean clausesAreOpposite(List<Integer> clause1, List<Integer> clause2, procedureCDCL mainProcedure){

        if ( clause1.size() == 1 && clause2.size() == 1 )
            return clause1.get(0) == -1 * clause2.get(0);
        return false;

    }

    public static String afterDecisionsUnitPropagation(procedureCDCL mainProcedure){

        List<List<Integer>> problem = mainProcedure.problem.getClauses();

        for ( int i = 0; i < problem.size(); i++ ){

            List<Integer> clause = problem.get(i);

            Boolean valueOfTheClause = Boolean.FALSE;
            List<Integer> noValueLiteral = new ArrayList<>();

            for (Integer literal : clause){

                Boolean literalValue = null;
                if (mainProcedure.assignedValue.get(Math.abs(literal)) != null)
                    if (literal < 0)
                        literalValue = !mainProcedure.assignedValue.get(Math.abs(literal)).getValue();
                    else
                        literalValue = mainProcedure.assignedValue.get(Math.abs(literal)).getValue();

                if (literalValue == null)
                    noValueLiteral.add(literal);
                else
                    valueOfTheClause = valueOfTheClause || literalValue;

            }

            if (noValueLiteral.isEmpty() && valueOfTheClause == Boolean.FALSE) {
                mainProcedure.conflictClause = new ArrayList<>(clause);
                return "CONFLICT";
            }
            if (noValueLiteral.size() == 1 && valueOfTheClause == Boolean.FALSE){

                Integer propagateLiteral = noValueLiteral.get(0);
                Boolean val = Boolean.TRUE;

                if ( propagateLiteral < 0){
                    propagateLiteral = Math.abs(propagateLiteral);
                    val = Boolean.FALSE;
                }
                mainProcedure.assignedValue.put(propagateLiteral, new assignedLiteral(propagateLiteral, val).setImplied(clause));
                mainProcedure.procedureStack.addImpliedLiteral(propagateLiteral, val, clause);
                i = 0;

            }

        }

        return "NOT-CONFLICT";

    }

    public static boolean problemIsTrue( procedureCDCL mainProcedure){

        boolean problemValue = true;
        int clauseIncompleted = 0;

        for ( List<Integer> clause : mainProcedure.problem.getClauses()) {

            boolean clauseValue = false;
            int literalAssigned = 0;
            for (Integer literal : clause) {

                boolean literalValue;
                if (mainProcedure.assignedValue.get(Math.abs(literal)) != null) {
                    literalValue = mainProcedure.assignedValue.get(Math.abs(literal)).getValue();
                    literalAssigned += 1;
                }
                else
                    continue;

                if (literal < 0)
                    literalValue = !literalValue;

                if (literalValue) {
                    clauseValue = true;
                    break;
                }

            }

            if (!clauseValue && literalAssigned == clause.size()) {
                problemValue = false;
                break;
            }

            if (literalAssigned != clause.size() && !clauseValue)
                clauseIncompleted += 1;

        }

        if ( clauseIncompleted > 0)
            return false;

        return problemValue;
    }
}
