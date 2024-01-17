import java.rmi.MarshalException;
import java.util.*;

public class conflictSolvingMode {

    /**
     * Like the searchMode class, also the conflictSolvingMode class is a static class created just to make the
     * organization of the project and the code more readable. The conflict solving mode class contains all the methods
     * for the resolution of a conflict, so the explain procedure that get to an assertion clause or to an empty clause,
     * so to an UNSAT output. If an assertion clause is reached it will return the level to backtrack.
     *
     * It is also implemented the backtrack algorithm which given the level to reach it will clear all the procedure stack
     * till the level indicated, adding also the assertion literal as implied in that level.
     *
     * Look to the code to get a better explanation for each method and its implementation
     */


    /**
     * As described before, backtrack method takes in input a level to reach, and then it will delete each level on the stack,
     * deleting also all the assignment to each literal into the assignment map.
     *
     * Firstly it gets what value should the assertion literal assume, so if it gets a literal lower than zero, he will
     * change it into a positive one but the truth value that it will get will be false, otherwise true.
     * Then level after level, it will clear the procedure stack, getting the literals to that levels and putting
     * the assignment to that literals to a null values into the assigedValue map.
     */
    public static void backTrack(procedureCDCL mainProcedure,int levelToReach){

        Integer literalToChange;
        Boolean literalToChangeValue;
        ArrayList<assignedLiteral> topLevel;

        if (mainProcedure.assertionLiteral < 0) {
            literalToChange = Math.abs(mainProcedure.assertionLiteral);
            literalToChangeValue = !mainProcedure.assignedValue.get(literalToChange).getValue();
        }else{
            literalToChange = Math.abs(mainProcedure.assertionLiteral);
            literalToChangeValue = !mainProcedure.assignedValue.get(literalToChange).getValue();
        }

        while (true){

            topLevel = mainProcedure.procedureStack.deleteLevel();

            if(mainProcedure.procedureStack.size() == levelToReach + 1){

                for( assignedLiteral l : topLevel )
                    mainProcedure.assignedValue.put(l.getName(), null);

                mainProcedure.procedureStack.addImpliedLiteral(literalToChange, literalToChangeValue, mainProcedure.conflictClause);
                mainProcedure.assignedValue.put(literalToChange, new assignedLiteral(literalToChange, literalToChangeValue).setImplied(mainProcedure.conflictClause));
                return;

            }else{

                for ( assignedLiteral l : topLevel )
                    mainProcedure.assignedValue.put(l.getName(), null);

            }

        }

    }


    /**
     *
     */
    public static int conflictAnalysis(procedureCDCL mainProcedure) {

        List<Integer> levelToConsider = mainProcedure.procedureStack.getLiteralAtLevel(mainProcedure.procedureStack.size() - 1);

        Map<Integer, assignedLiteral> topLevelAssignement = new LinkedHashMap<>();
        for ( assignedLiteral al : mainProcedure.procedureStack.getTopLevel() )
            topLevelAssignement.put(al.getName(), al);


        while (true) {

            if (mainProcedure.conflictClause.isEmpty())
                return -1;

            List<Integer> literalFalsified = new ArrayList<>();

            for (Integer l : mainProcedure.conflictClause)
                if (levelToConsider.contains(Math.abs(l)))
                    literalFalsified.add(l);

            if (literalFalsified.size() == 1) {

                mainProcedure.assertionLiteral = literalFalsified.get(0);

                // learning phase
                if (!mainProcedure.problem.getClauses().contains(mainProcedure.conflictClause)) {

                    // forgetting
                    if ( mainProcedure.learning.size() > mainProcedure.problem.getClausesNumber() ){

                        mainProcedure.learning.sort((list1, list2) -> Integer.compare(list2.size(), list1.size()));
                        List<Integer> clauseToForget = mainProcedure.learning.get(0);
                        mainProcedure.learning.remove(clauseToForget);
                        mainProcedure.problem.forgotClause(clauseToForget);

                    }

                    mainProcedure.learning.add(mainProcedure.conflictClause);
                    mainProcedure.problem.learnClause(mainProcedure.conflictClause);

                    //nella learn fase aggiungo anche il corrispettivo two watched
                    List<Integer[]> watchedLiterals = mainProcedure.problem.getTwoWatchedLiteral();
                    if (mainProcedure.conflictClause.size() == 1)
                        watchedLiterals.add(new Integer[]{mainProcedure.conflictClause.get(0)});
                    if (mainProcedure.conflictClause.size() >= 2)
                        watchedLiterals.add(new Integer[]{mainProcedure.conflictClause.get(0), mainProcedure.conflictClause.get(1)});
                }


                List<Integer> i_levelLiteral = new ArrayList<>();
                int definedLiteral;
                for (int i = 0; i < mainProcedure.procedureStack.size(); i++) {

                    definedLiteral = 0;

                    i_levelLiteral.addAll(mainProcedure.procedureStack.getLiteralAtLevel(i));

                    for (Integer falseLiteral : mainProcedure.conflictClause) {

                        if (i_levelLiteral.contains(Math.abs(falseLiteral)))
                            definedLiteral++;

                    }

                    if (definedLiteral == mainProcedure.conflictClause.size() - 1)
                        return i;

                }


            }

            List<Integer> leftParent = mainProcedure.conflictClause;
            List<Integer> rightParent = fittestExplainClause(mainProcedure.conflictClause, topLevelAssignement);
            mainProcedure.conflictClause = binaryResolution(leftParent, rightParent);

            /** The proof generation is automatically turned off when is getting too big for a human to read it */
            if (mainProcedure.proofConstructor.size() <= 500)
                mainProcedure.proofConstructor.addProofStep(leftParent, rightParent, mainProcedure.conflictClause);

        }

    }


    private static List<Integer> fittestExplainClause(List<Integer> confictClause, Map<Integer, assignedLiteral> assignedLiterals){

        List<Integer> current;
        List<Integer> returnClause = new ArrayList<>();

        int currentSimilarity;
        int returnClauseSimilarity = 0;

        for (Integer l : confictClause) {

            currentSimilarity = 0;

            if (assignedLiterals.get(Math.abs(l)) == null)
                continue;

            if (assignedLiterals.get(Math.abs(l)).isImplied())
                current = assignedLiterals.get(Math.abs(l)).getAncestor();
            else
                continue;

            for (Integer literal : current)
                if (!confictClause.contains(literal))
                    currentSimilarity -= 1;

            if (current.size() < confictClause.size())
                currentSimilarity -= confictClause.size() - current.size();

            if (returnClause.isEmpty()) {
                returnClause = new ArrayList<>(current);
                returnClauseSimilarity = currentSimilarity;
            }
            else {
                if (currentSimilarity > returnClauseSimilarity) {
                    returnClauseSimilarity = currentSimilarity;
                    returnClause = new ArrayList<>(current);
                }

            }

        }

        return returnClause;

    }

    private static List<Integer> binaryResolution(List<Integer> leftC, List<Integer> rightC){

        List<Integer> leftClause = new ArrayList<>(leftC);
        List<Integer> rightClause = new ArrayList<>(rightC);

        leftClause.sort(null);
        rightClause.sort(null);

        ArrayList<Integer> leftLiteralToRemove = new ArrayList<>();
        ArrayList<Integer> rightLiteralToRemove = new ArrayList<>();

        for (Integer literal : leftClause)
            if (rightClause.contains( -1 * literal )){

                leftLiteralToRemove.add(literal);
                rightLiteralToRemove.add(-1 * literal);

            }

        leftClause.removeAll(leftLiteralToRemove);
        rightClause.removeAll(rightLiteralToRemove);

        Set<Integer> union = new HashSet<>(leftClause);
        union.addAll(rightClause);

        return new ArrayList<>(union);

    }

}
