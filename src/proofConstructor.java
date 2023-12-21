import java.util.*;

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
        public Map.Entry<proofNode, Integer> contains(List<Integer> value, int iterations){

            Map<proofNode, Integer> returnValue = new HashMap<>();
            returnValue.put(this, iterations);

            if ( this.child.equals(value))
                return returnValue.entrySet().stream().toList().get(0);
            else if (this.parent1 == null && this.parent2 == null && !this.child.equals(value))
                return null;
            else {
                Map.Entry<proofNode,Integer> parent1Return = null;
                Map.Entry<proofNode, Integer> parent2Return = null;

                if (parent1 != null)
                    parent1Return = parent1.contains(value, iterations++);
                if (parent2 != null)
                    parent2Return = parent2.contains(value, iterations++);

                if (parent1Return != null)
                    return parent1Return;
                else if (parent2Return != null)
                    return parent2Return;
                else
                    return null;

            }
        }

        public String toString(){

            if ( this.parent1 == null && this.parent2 == null)
                return this.child.toString();
            else
                return "(" + this.parent1.toString() + "   " + this.parent2.toString() + ")  -> " + this.child.toString();

        }

    }

    private List<proofNode> proofList;
    private String returnString;

    public proofConstructor () {

        this.proofList = new ArrayList<>();
        this.returnString = "";

    }

    public void addProofStep (List<Integer> p1, List<Integer> p2, List<Integer> child) {

        returnString = returnString.concat( "(" + p1 + " " + p2 + ")" + " -> " + child + "\n" );

        if ( this.proofList.isEmpty() ) {
            this.proofList.add(new proofNode(new proofNode(p1), new proofNode(p2), child));
            return;
        }

        Map.Entry<proofNode, Integer> check1 = null;
        Map.Entry<proofNode, Integer> check2 = null;

        proofNode parent1;
        proofNode parent2;

        for (proofNode n : this.proofList){
            check1 = n.contains(p1, 0);
            if (check1 != null)
                break;
        }

        for (proofNode n : this.proofList){
            check2 = n.contains(p2, 0);
            if (check2 != null)
                break;
        }

        if (check1 == null)
            parent1 = new proofNode(p1);
        else if (check1.getKey() == null)
            parent1 = new proofNode(p1);
        else {
            parent1 = check1.getKey();
            if (check1.getValue().equals(0)) {

                Iterator<proofNode> iterator = this.proofList.iterator();
                while (iterator.hasNext()) {
                    proofNode current = iterator.next();
                    if (current.getChild().equals(check1.getKey().getChild())) {
                        this.proofList.remove(current);
                        break;
                    }
                }

            }
        }

        if (check2 == null)
            parent2 = new proofNode(p2);
        else if (check2.getKey() == null)
            parent2 = new proofNode(p2);
        else {
            parent2 = check2.getKey();
            if (check2.getValue().equals(0)){

                Iterator<proofNode> iterator = this.proofList.iterator();
                while (iterator.hasNext()){
                    proofNode current = iterator.next();
                    if ( current.getChild().equals(check2.getKey().getChild())){
                        this.proofList.remove(current);
                        break;
                    }
                }
            }
        }

        proofNode node = new proofNode( parent1, parent2, child);
        this.proofList.add(node);

    }

    @Override
    public String toString() {

       /* String returnString = "";
        for ( proofNode subtree: this.proofList)
            returnString = returnString.concat(subtree.toString()).concat("\n\n");
        return returnString;*/

        return this.returnString.toString();

    }

    public List<List<Integer>> getEndChild(){

        List<List<Integer>> returnList = new ArrayList<>();
        for (proofNode p : this.proofList)
            returnList.add(p.child);

        return returnList;
    }
}
