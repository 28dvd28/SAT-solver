import java.util.*;

public class proofConstructor {


    /**
     * This class was created in order to make the construction of the proof easier. The proof is for the problems that
     * are unsatisfiable, and this is checked when after the explain process we get an empty clause. So the proof is just all the
     * subsumption during the explain process, saved one by one into a List of List of List of Integer.
     *
     * To explain better this complicated structure, you have to see that each step of the proof is a subsumption, constituted
     * by two parents clauses and a single child clauses. Each of this is a List of integer. Then this three clauses are
     * saved into another list where the first two elements are the parents and the last is the child of the subsumption.
     * So in this way we represent a subsumption and at the end we get all the subsumption combining them into a list.
     *
     * The reason why is used this structure instead combining them directly into a string is that not all subsumption are
     * important for the proof, so at the end all the irrelevant subsumption will be removed from the list. This is possible
     * just by checking if the child of a subsumption doesn't appear as a parent of others and if it is so,
     * then that subsumption is removed.
     */

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

    public int size(){
        return this.returnProof.size();
    }

}
