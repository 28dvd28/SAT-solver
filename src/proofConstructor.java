import java.util.*;

public class proofConstructor {
    private List<List<List<Integer>>> returnProof;

    public proofConstructor () {

        this.returnProof = new ArrayList<>();

    }

    public void addProofStep (List<Integer> p1, List<Integer> p2, List<Integer> child) {

        List<List<Integer>> newProofPass = new ArrayList<>();
        newProofPass.add(p1);
        newProofPass.add(p2);
        newProofPass.add(child);

        if ( ! returnProof.contains(newProofPass))
                returnProof.add(newProofPass);

    }

    @Override
    public String toString() {

        List<List<List<Integer>>> passToRemove;

        do{

            passToRemove = new ArrayList<>();

            externalCycle:
            for ( int i = 0; i < this.returnProof.size()-1; i++ ) {
                for (int j = i + 1; j < this.returnProof.size(); j++)
                    if (this.returnProof.get(j).get(0).equals(this.returnProof.get(i).get(2)) ||
                            this.returnProof.get(j).get(1).equals(this.returnProof.get(i).get(2))) {
                        continue externalCycle;
                    }
                passToRemove.add(this.returnProof.get(i));
            }

            this.returnProof.removeAll(passToRemove);

        }while(!passToRemove.isEmpty());

        String returnString = "";

        for (List<List<Integer>> passProof : this.returnProof){

            returnString = returnString.concat("(" + passProof.get(0) + " " + passProof.get(1) + ")" + " -> " + passProof.get(2)) + "\n";

        }

        return returnString;


    }

}
