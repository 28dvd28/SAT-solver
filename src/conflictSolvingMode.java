import java.util.*;

public class conflictSolvingMode {

    public static void backTrack(procedureCDCL mainProcedure,int levelToReach){

        Integer literalToChange;
        Boolean literalToChangeValue;
        ArrayList<assignedLiteral> topLevel;

        while (true){

            topLevel = mainProcedure.procedureStack.deleteLevel();

            if(mainProcedure.procedureStack.size() == levelToReach){

                for ( assignedLiteral l : topLevel )
                    if ( l.isImplied() )
                        mainProcedure.assignedValue.put(l.getName(), null);
                    else if ( l.isDecided() ){

                        literalToChange = l.getName();
                        literalToChangeValue = !l.getValue();
                        mainProcedure.assignedValue.put(l.getName(), new assignedLiteral(literalToChange, literalToChangeValue).setConflictImplied(mainProcedure.conflictClause));
                        mainProcedure.procedureStack.addConflictImpliedLiteral(literalToChange, literalToChangeValue, mainProcedure.conflictClause);

                    }

                break;
            }else{

                for ( assignedLiteral l : topLevel )
                    mainProcedure.assignedValue.put(l.getName(), null);

            }

        }

    }


    public static int conflictAnalysis(procedureCDCL mainProcedure){

        ArrayList<assignedLiteral> currentLevelAssigments;

        int j = 1;
        boolean changed;
        do {

            changed = false;
            currentLevelAssigments = mainProcedure.procedureStack.getLevelAt(mainProcedure.procedureStack.size() - j);
            for (assignedLiteral l : currentLevelAssigments)
                if (l.isConflictImplied()) {
                    j += 1;
                    changed = true;
                    break;
            }
        }while(changed);

        if(mainProcedure.procedureStack.size() - j == 0)
            return 0;


        currentLevelAssigments = mainProcedure.procedureStack.getTopLevel();
        while (true){

            List<Integer> literalFalsified = new ArrayList<>();

            for ( assignedLiteral l : currentLevelAssigments)
                if ( mainProcedure.conflictClause.contains(l.getName()) && l.getValue() == Boolean.FALSE)
                    literalFalsified.add(l.getName());
                else if ( mainProcedure.conflictClause.contains(-1 * l.getName()) && l.getValue() ==Boolean.TRUE)
                    literalFalsified.add(l.getName());

            if ( literalFalsified.size() == 1 ){

                // learning phase
                mainProcedure.learning.add(mainProcedure.conflictClause);
                mainProcedure.problem.learnClause(mainProcedure.conflictClause);

                for (int i = mainProcedure.procedureStack.size() - 1; i >= 0; i--){

                    if ( mainProcedure.procedureStack.getLevelAt(i).get(0).isDecided())
                        return i;

                }
            }

            for ( Integer l : mainProcedure.conflictClause) {
                if (mainProcedure.assignedValue.get(Math.abs(l)).isImplied()) {
                    mainProcedure.conflictClause = binaryResolution(mainProcedure.conflictClause, mainProcedure.assignedValue.get(Math.abs(l)).getAncestor());
                    break;
                }
            }


        }

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
