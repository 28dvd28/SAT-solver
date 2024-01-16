import java.beans.Introspector;
import java.util.*;

class searchMode {

    /**
     * In this class are implemented the method of the searchMode of the CDCL procedure. There are no variable and
     * all the method are static because the creation of this class, like for conflictSolvingMode class, are just for making
     * the structure and the code more clear, avoiding putting all in the same class with the risk of making all more complicated
     *
     * All the method implemented will be explained one by one in the code.
     */

    /**
     * The pickBranchingVariable method simply decide a new literal from the ones that still have no assigned value.
     * Since the procedure assigned value contains the literal already ordered, simply iterate over the assignedValue
     * list till a not assigned literal is reached, then by default the decision assign as truth value TRUE, adding this
     * into the assignedValue list and also to the procedure stack
     */
    public static void pickBranchingVariable(procedureCDCL mainProcedure){

        for (Map.Entry<Integer, assignedLiteral> m : mainProcedure.assignedValue.entrySet()){
            if (m.getValue() == null){

                mainProcedure.assignedValue.put(m.getKey(), new assignedLiteral(m.getKey(), Boolean.TRUE).setDecided());
                mainProcedure.procedureStack.addDecidedLiteral(m.getKey(), Boolean.TRUE);
                break;

            }
        }

    }



    /**
     * The unit propagation is sliced in two parts. One is executed on the level zero, deriving all the literals which
     * truth value can be derived from the problem as it is. If the procedureStack is not empty, then is executed the
     * propagation considering the values of the procedure stack
     */
    public static String unitPropagation(procedureCDCL mainProcedure){

        if (mainProcedure.procedureStack.isEmpty())
            return levelZeroUnitPropagation(mainProcedure);
        else
            return afterDecisionsUnitPropagation(mainProcedure);

    }



    /**
     * The level zero propagation check on all the problem clauses and find if there is some which length is 1, so it is
     * just a literal, so it must be TRUE, adding this information inside the procedureStack and into the assignedValue
     * list.
     *
     * Then after all this process, if there is something on the procedure stack, it is executed the method that implements
     * the propagation with the values on the stack.
     */
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

        }

        if ( !mainProcedure.procedureStack.isEmpty() )
            return afterDecisionsUnitPropagation(mainProcedure);
        else
            return "NOT-CONFLICT";

    }



    /**
     * This method implements the propagation decision using the two watched literals technique.
     *
     * So it iterates all over the clauses in the problems, and for each clause make this checks:
     *      -for each literal in the clause save its value (TRUE, FALSE, null) into a list
     *      -check if is a clause with only one literal and if it is FALSE then return conflict,
     *       if it is TRUE do nothing, otherwise if null make an implication
     *      -if the length of the clauses is more than one then it is applied the two watched literals technique.
     *       For each step check in the code to see a better explanation of the implementation of this technique.
     */
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

                    /**
                     * -If both are not false (so both are null or TRUE) then break
                     * -If one is false and the other is null, and is the only one than add the correspondent literal as an implied literal,
                     *  otherwise continue to the next clause
                     * -If both are false check if there is one other literal which value is true or null and change the two watched ones
                     *  otherwise if there exists non, the end of the code is reached and the CONFLICT string is returned.
                     */
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

        /** When i get here no other action can be made and a return a non conflict output */
        mainProcedure.problem.updateTwoWatchedLiteral(watchedLiterals);
        return "NOT-CONFLICT";

    }



    /**
     * A method to just make the implication of a literal easier, adding it to the procedure stack and to
     * the assigned values list.
     */
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



    /**
     * This method is used inside the main algorithm in the procedureCDCL class and return TRUE if the problem
     * value is true, FALSE otherwise. So false in this case is not indicating that the problem is unsatisfiable.
     * In the procedure algorithm if this get TRUE means that the problem is satisfiable and return the model.
     */
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
