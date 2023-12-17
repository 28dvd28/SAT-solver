import java.util.*;

public class conflictSolvingMode {

    public static void backTrack(procedureCDCL mainProcedure,int levelToReach){

        Integer literalToChange;
        Boolean literalToChangeValue;
        ArrayList<assignedLiteral> topLevel;

        while (true){

            topLevel = mainProcedure.procedureStack.getTopLevel();

            if(mainProcedure.procedureStack.size() == levelToReach + 1){

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

        ArrayList<assignedLiteral> currentLevelAssigments = mainProcedure.procedureStack.getTopLevel();

        while (true){

            List<Integer> literalFalsified = new ArrayList<>();

            for ( assignedLiteral l : currentLevelAssigments)
                if ( mainProcedure.conflictClause.contains(l.getName()) && l.getValue() == Boolean.FALSE)
                    literalFalsified.add(l.getName());
                else if ( mainProcedure.conflictClause.contains(-1 * l.getName()) && l.getValue() ==Boolean.TRUE)
                    literalFalsified.add(l.getName());

            if ( literalFalsified.size() == 1 ){

                mainProcedure.learning.add(mainProcedure.conflictClause); // learning phase
                for (int i = 0; i < mainProcedure.procedureStack.size(); i++){

                    if (mainProcedure.procedureStack.getLiteralAtLevel(i).contains(literalFalsified.get(0)))
                        return i;
                }
            }

            for ( Integer l : mainProcedure.conflictClause)
                if ( mainProcedure.assignedValue.get(l).isImplied() ) {
                    mainProcedure.conflictClause = binaryResolution(mainProcedure.conflictClause, mainProcedure.assignedValue.get(l).getAncestor());
                    break;
                }


        }

    }

    private static List<Integer> binaryResolution(List<Integer> leftClause, List<Integer> rightClause){

        leftClause.sort(null);
        rightClause.sort(null);

        for (Integer literal : leftClause)
            if (rightClause.contains( -1 * literal )){

                leftClause.removeIf(l -> Objects.equals(l, literal));
                rightClause.removeIf(l -> Objects.equals(l, -1 * literal));

            }

        Set<Integer> union = new HashSet<>(leftClause);
        union.addAll(rightClause);

        return new ArrayList<>(union);

    }

}
