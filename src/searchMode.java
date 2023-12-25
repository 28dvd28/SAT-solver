import java.beans.Introspector;
import java.util.*;

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

        for (int i = 0; i < clauses.size(); i++) {

            if (clauses.get(i).size() == 1){

                if (clauses.get(i).get(0) < 0 ){
                    mainProcedure.procedureStack.addImpliedLiteral(Math.abs(clauses.get(i).get(0)), Boolean.FALSE, clauses.get(i));
                    mainProcedure.assignedValue.put(Math.abs(clauses.get(i).get(0)), new assignedLiteral(Math.abs(clauses.get(i).get(0)), Boolean.FALSE).setImplied(clauses.get(i)));
                }
                else{
                    mainProcedure.procedureStack.addImpliedLiteral(clauses.get(i).get(0), Boolean.TRUE, clauses.get(i));
                    mainProcedure.assignedValue.put(clauses.get(i).get(0), new assignedLiteral(Math.abs(clauses.get(i).get(0)), Boolean.TRUE).setImplied(clauses.get(i)));
                }

            }

            /*if (containsOppositeLiteral(clauses.get(i)))
                return "CONFLICT";*/

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
        List<Integer[]> watchedLiterals = mainProcedure.problem.getTwoWatchedLiteral();

        for ( int i = 0; i < problem.size(); i++ ){

            List<Integer> clause = problem.get(i);
            Map<Integer, Boolean> values = new HashMap<>();

            for (Integer lit : clause){

                if ( mainProcedure.assignedValue.get(Math.abs(lit)) == null )
                    values.put(lit, null);
                else{
                    if ( lit > 0)
                        values.put(lit, mainProcedure.assignedValue.get(lit).getValue());

                    else
                        values.put(lit, !mainProcedure.assignedValue.get(Math.abs(lit)).getValue());
                }

            }

            if ( clause.size() == 1){
                if( values.get(clause.get(0)) == null) {
                    setImplication(clause.get(0), clause, mainProcedure);
                    i = -1;
                    continue;
                }
                else{
                    if ( values.get(clause.get(0)).equals(Boolean.TRUE) )
                        continue;
                    else{
                        mainProcedure.conflictClause = clause;
                        mainProcedure.problem.updateTwoWatchedLiteral(watchedLiterals);
                        return "CONFLICT";
                    }

                }
            }
            else{

                TWL_scheme:
                while(true) {

                    Integer[] currentWatchedLiterals = watchedLiterals.get(i);

                    if (!Boolean.FALSE.equals(values.get(currentWatchedLiterals[0])) || !Boolean.FALSE.equals(values.get(currentWatchedLiterals[1])))
                        break;
                    else if (Boolean.FALSE.equals(values.get(currentWatchedLiterals[0])) && values.get(currentWatchedLiterals[1]) == null) {

                        for (Map.Entry<Integer, Boolean> x : values.entrySet()) {

                            if (x.getKey().equals(currentWatchedLiterals[1]))
                                continue;

                            if (x.getValue() == Boolean.TRUE || x.getValue() == null) {
                                currentWatchedLiterals[0] = x.getKey();
                                watchedLiterals.set(i, currentWatchedLiterals);
                                continue TWL_scheme;
                            }

                        }

                        setImplication(currentWatchedLiterals[1], clause, mainProcedure);
                        i = -1;
                        break;
                    }
                    else if (Boolean.FALSE.equals(values.get(currentWatchedLiterals[1])) && values.get(currentWatchedLiterals[0]) == null) {

                        for (Map.Entry<Integer, Boolean> x : values.entrySet()) {

                            if (x.getKey().equals(currentWatchedLiterals[0]))
                                continue;

                            if (x.getValue() == Boolean.TRUE || x.getValue() == null) {
                                currentWatchedLiterals[1] = x.getKey();
                                watchedLiterals.set(i, currentWatchedLiterals);
                                continue TWL_scheme;
                            }

                        }
                        setImplication(currentWatchedLiterals[0], clause, mainProcedure);
                        i = -1;
                        break;
                    }
                    else if (Boolean.FALSE.equals(values.get(currentWatchedLiterals[0])) && Boolean.FALSE.equals(values.get(currentWatchedLiterals[1]))){

                        for (Map.Entry<Integer, Boolean> x : values.entrySet()) {

                            if (x.getKey().equals(currentWatchedLiterals[1]))
                                continue;

                            if (x.getValue() == Boolean.TRUE || x.getValue() == null) {
                                currentWatchedLiterals[0] = x.getKey();
                                watchedLiterals.set(i, currentWatchedLiterals);

                                for (Map.Entry<Integer, Boolean> y : values.entrySet()) {
                                    if (y.getKey().equals(currentWatchedLiterals[0]))
                                        continue;
                                    if (y.getValue() == Boolean.TRUE || y.getValue() == null) {
                                        currentWatchedLiterals[1] = y.getKey();
                                        watchedLiterals.set(i, currentWatchedLiterals);
                                        continue TWL_scheme;
                                    }
                                }

                                if (values.get(currentWatchedLiterals[0]) == null) {
                                    setImplication(currentWatchedLiterals[0], clause, mainProcedure);
                                    i = -1;
                                    break TWL_scheme;
                                }
                                else
                                    continue TWL_scheme;


                            }
                        }

                        mainProcedure.conflictClause = clause;
                        mainProcedure.problem.updateTwoWatchedLiteral(watchedLiterals);
                        return "CONFLICT";

                    }

                }
            }


        }

        mainProcedure.problem.updateTwoWatchedLiteral(watchedLiterals);
        return "NOT-CONFLICT";

    }

    private static void setImplication(Integer literal, List<Integer> ancestor, procedureCDCL mainProcedure){

        Boolean value;

        if (literal < 0) {
            literal = Math.abs(literal);
            value = Boolean.FALSE;
        }else {
            value = Boolean.TRUE;
        }

        mainProcedure.procedureStack.addImpliedLiteral(literal, value, ancestor);
        mainProcedure.assignedValue.put(literal, new assignedLiteral(literal, value).setImplied(ancestor));

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
