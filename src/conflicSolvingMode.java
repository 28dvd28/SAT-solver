import java.util.*;

public class conflicSolvingMode {

    public static void backTrack(){


    }


    public static int conflictAnalysis(procedureCDCL mainProcedure){



    }

    private List<Integer> binaryResolution(List<Integer> leftClause, List<Integer> rightClause){

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
