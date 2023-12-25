import java.rmi.MarshalException;
import java.util.*;

public class conflictSolvingMode {

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


    public static int conflictAnalysis(procedureCDCL mainProcedure) {

        List<Integer> levelToConsider = mainProcedure.procedureStack.getLiteralAtLevel(mainProcedure.procedureStack.size() - 1);
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
            List<Integer> rightParent = fittestExplainClause(mainProcedure.conflictClause, mainProcedure.assignedValue);
            mainProcedure.conflictClause = binaryResolution(leftParent, rightParent);
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
