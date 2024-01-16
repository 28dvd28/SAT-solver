import java.util.List;

public class assignedLiteral {

    /**
     * This class implements an object that represents not only the literal itself but
     * also its truth value, and if this value comes after a decision or an implication,
     * saving in this case the clause that implied the value of this literal.
     *
     * All this feature are important during the CDCL procedure.
     *
     * The initialization of the literal require only indicating the literal itself, represented
     * as an integer and the truth values saved as Boolean, the after it is set if is a decided or
     * implied.
     */

    private final Integer name;
    private Boolean value;
    private List<Integer> ancestor;
    private Boolean decided;
    private Boolean implied;

    public assignedLiteral(Integer name, Boolean value){

        this.name = name;
        this.value = value;

    }

    public assignedLiteral setDecided(){
        this.decided = Boolean.TRUE;
        this.implied = Boolean.FALSE;

        return this;
    }

    public assignedLiteral setImplied(List<Integer> ancestor){
        this.decided = Boolean.FALSE;
        this.implied = Boolean.TRUE;
        this.ancestor = ancestor;

        return this;
    }

    public Integer getName(){ return this.name; }
    public Boolean getValue(){ return this.value; }
    public List<Integer> getAncestor(){ return this.ancestor; }
    public Boolean isDecided(){ return this.decided; }
    public Boolean isImplied(){ return this.implied; }

    public String toString(){
        return "[" + this.name + " | " + this.value + " |" + this.getAncestor() + "]";
    }


}