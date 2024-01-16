import java.util.List;

public class assignedLiteral {

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