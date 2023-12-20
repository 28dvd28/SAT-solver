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

        /*int j = 1;
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

        if(mainProcedure.procedureStack.size() - j == 0) {

            for ( Integer l : mainProcedure.conflictClause) {
                if (mainProcedure.assignedValue.get(Math.abs(l)).isImplied()) {

                    List<Integer> leftParent = mainProcedure.conflictClause;
                    List<Integer> rightParent = mainProcedure.assignedValue.get(Math.abs(l)).getAncestor();

                    mainProcedure.conflictClause = binaryResolution(leftParent, rightParent);
                    mainProcedure.proofConstructor.addProofStep(leftParent, rightParent, mainProcedure.conflictClause);
                    break;
                }
            }

            return 0;
        }*/
        int j;
        outerloop:
        for ( j = mainProcedure.procedureStack.size() - 1; j>=0; j--) {
            if (j == 0)
                break;
            for (assignedLiteral lit : mainProcedure.procedureStack.getLevelAt(j))
                if (lit.isDecided())
                    break outerloop;
        }

        System.out.println("J " + j);
        List<Integer> levelToConsider = mainProcedure.procedureStack.getLiteralAtLevel(j);

        if (j==0){
            getEmptyClause(mainProcedure);
            System.out.println("CONFICTCLAUSE: " + mainProcedure.conflictClause + " vdsvd");
            return j;
        }

        while (true){

            List<Integer> literalFalsified = new ArrayList<>();

            for ( assignedLiteral l : mainProcedure.assignedValue.values())
                if ( mainProcedure.conflictClause.contains(l.getName()) && l.getValue() == Boolean.FALSE)
                    literalFalsified.add(l.getName());
                else if ( mainProcedure.conflictClause.contains(-1 * l.getName()) && l.getValue() ==Boolean.TRUE)
                    literalFalsified.add(l.getName());

            literalFalsified.retainAll(levelToConsider);

            if ( literalFalsified.size() == 1 ){

                // learning phase
                if (!mainProcedure.problem.getClauses().contains(mainProcedure.conflictClause)) {
                    mainProcedure.learning.add(mainProcedure.conflictClause);
                    mainProcedure.problem.learnClause(mainProcedure.conflictClause);
                }

                return j;

            }

            for ( Integer l : mainProcedure.conflictClause) {
                if (mainProcedure.assignedValue.get(Math.abs(l)).isImplied()) {

                    List<Integer> leftParent = mainProcedure.conflictClause;
                    List<Integer> rightParent = mainProcedure.assignedValue.get(Math.abs(l)).getAncestor();

                    mainProcedure.conflictClause = binaryResolution(leftParent, rightParent);
                    mainProcedure.proofConstructor.addProofStep(leftParent, rightParent, mainProcedure.conflictClause);
                    break;
                }
            }


        }

    }

    public static void getEmptyClause(procedureCDCL mainProcedure){

        int c = 0;

        System.out.println("Conflict clause: " + mainProcedure.conflictClause);
        System.out.println("Stack: " + mainProcedure.procedureStack);

        while (!mainProcedure.conflictClause.isEmpty() && c != 50) {

            c++;

            System.out.println(mainProcedure.proofConstructor);

            for (Integer l : mainProcedure.conflictClause) {
                if (mainProcedure.assignedValue.get(Math.abs(l)).isImplied() ||
                    mainProcedure.assignedValue.get(Math.abs(l)).isConflictImplied()) {

                    List<Integer> leftParent = mainProcedure.conflictClause;
                    List<Integer> rightParent = mainProcedure.assignedValue.get(Math.abs(l)).getAncestor();

                    mainProcedure.conflictClause = binaryResolution(leftParent, rightParent);
                    mainProcedure.proofConstructor.addProofStep(leftParent, rightParent, mainProcedure.conflictClause);
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
