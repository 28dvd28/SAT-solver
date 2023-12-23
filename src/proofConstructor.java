import java.util.*;

public class proofConstructor {
    private String returnString;

    public proofConstructor () {

        this.returnString = "";

    }

    public void addProofStep (List<Integer> p1, List<Integer> p2, List<Integer> child) {

        returnString = returnString.concat( "(" + p1 + " " + p2 + ")" + " -> " + child + "\n" );

    }

    @Override
    public String toString() {
        return this.returnString;
    }

}
