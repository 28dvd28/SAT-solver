import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class searchMode {

    public static void pickBranchingVariable(procedureCDCL mainProcedure){

        for (Map.Entry<String, Boolean> m : mainProcedure.assignedValue.entrySet()){
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

        List<List<String>> clauses = problem.getClauses();

        for (int i = 0; i < clauses.size()-1; i++) {

            if (clauses.get(i).size() == 1){

                String literal = clauses.get(i).get(0);
                Boolean value = Boolean.TRUE;
                if ( literal.startsWith("-")) {
                    literal = literal.substring(1);
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

    public static boolean clausesAreOpposite(List<String> clause1, List<String> clause2, procedureCDCL mainProcedure){

        int equalsValues = 0;
        int differentValues = 0;
        String equalLiteral = "";

        for (String s1: clause1){
            for ( String s2: clause2){

                if ( s1.equals(s2) ) {
                    equalsValues++;
                    equalLiteral = s1;
                }
                if ( !s1.equals("-" + s2) )
                    differentValues++;
                if ( !s2.equals("-" + s1) )
                    differentValues++;

            }
        }

        if (equalsValues == 0 && differentValues == 0)
            return true;
        else{
            if (equalsValues == 1 && differentValues == 0){

                Boolean value = Boolean.TRUE;
                if ( equalLiteral.startsWith("-")) {
                    equalLiteral = equalLiteral.substring(1);
                    value = Boolean.FALSE;
                }

                mainProcedure.procedureStack.addImpliedLiteral(equalLiteral, value, null);
                mainProcedure.assignedValue.put(equalLiteral, value);

            }
        }

        return false;
    }

    public static String afterDecisionsUnitPropagation(procedureCDCL mainProcedure){

        List<List<String>> problem = mainProcedure.problem.getClauses();

        for ( int i = 0; i < problem.size(); i++ ){

            List<String> clause = problem.get(i);

            Boolean valueOfTheClause = Boolean.FALSE;
            List<String> noValueLiteral = new ArrayList<>();

            for (String literal : clause){

                String l = literal;
                Boolean literalValue;
                if (l.startsWith("-")) {
                    l = literal.substring(1);
                    literalValue = !mainProcedure.assignedValue.get(literal);
                }
                else{
                    literalValue = mainProcedure.assignedValue.get(literal);
                }

                if (mainProcedure.assignedValue.get(literal) == null)
                    noValueLiteral.add(literal);
                else
                    valueOfTheClause = valueOfTheClause || literalValue;

            }

            if (noValueLiteral.isEmpty() && valueOfTheClause == Boolean.FALSE) {
                mainProcedure.conflictClause = new ArrayList<>(clause);
                return "UNSAT";
            }
            if (noValueLiteral.size() == 1 && valueOfTheClause == Boolean.FALSE){

                String propagateLiteral = noValueLiteral.get(0);
                mainProcedure.assignedValue.put(propagateLiteral, Boolean.TRUE);
                mainProcedure.procedureStack.addImpliedLiteral(propagateLiteral, Boolean.TRUE, clause);
                i = 0;

            }

        }

        return "NOT-UNSAT";

    }
}
