import java.util.ArrayList;
import java.util.List;

public class proofConstructor {

    private static class proofNode {

        private final proofNode parent1;
        private final proofNode parent2;
        private final List<Integer> child;

        public proofNode(List<Integer> child){

            this.parent1 = null;
            this.parent2 = null;
            this.child = child;

        }

        public proofNode(proofNode parent1, proofNode parent2, List<Integer> child){

            this.parent1 = parent1;
            this.parent2 = parent2;
            this.child = child;

        }

        public List<Integer> getChild(){ return this.child; }
        public proofNode contains(List<Integer> value){

            if ( this.child.equals(value))
                return this;
            else if (this.parent1 == null && this.parent2 == null && !this.child.equals(value))
                return null;
            else {
                if (parent1 != null && parent2 == null)
                    return this.parent1.contains(value);
                else if (parent1 == null && parent2 != null)
                    return this.parent2.contains(value);
                else if (parent1 != null) {

                    proofNode firstRet = parent1.contains(value);
                    return (firstRet == null)? parent2.contains(value) : firstRet;

                }
            }
            return null;
        }

    }

    private List<proofNode> proofList;

    public proofConstructor () {
        this.proofList = new ArrayList<>();
    }

    public void addProofStep (List<Integer> p1, List<Integer> p2, List<Integer> child) {

        proofNode parent1 = null;
        proofNode parent2 = null;

        for (proofNode n : this.proofList){
            parent1 = n.contains(p1);
            break;
        }

        for (proofNode n : this.proofList){
            parent2 = n.contains(p2);
            break;
        }

        if (parent1 == null)
            parent1 = new proofNode(p1);
        if (parent2 == null)
            parent2 = new proofNode(p2);

        this.proofList.remove(parent1);
        this.proofList.remove(parent2);

        proofNode node = new proofNode( parent1, parent2, child);
        this.proofList.add(node);

    }

    @Override
    public String toString() {



    }
}


