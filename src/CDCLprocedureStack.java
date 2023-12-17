import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class CDCLprocedureStack {

    public static class assignedLiteral {

        private final Integer name;
        private Boolean value;
        private List<Integer> ancestor;
        private Boolean decided;
        private Boolean implied;
        private Boolean conflictImplied;

        public assignedLiteral(Integer name, Boolean value){

            this.name = name;
            this.value = value;

        }

        public void setDecided(){
            this.decided = Boolean.TRUE;
            this.implied = Boolean.FALSE;
            this.conflictImplied = Boolean.FALSE;
        }

        public void setImplied(List<Integer> ancestor){
            this.decided = Boolean.FALSE;
            this.implied = Boolean.TRUE;
            this.conflictImplied = Boolean.FALSE;
            this.ancestor = ancestor;
        }

        public void setConflictImplied(List<Integer> ancestor) {
            this.decided = Boolean.FALSE;
            this.implied = Boolean.FALSE;
            this.conflictImplied = Boolean.TRUE;

            this.value = value;
            this.ancestor = ancestor;
        }

        public Integer getName(){ return this.name; }
        public Boolean getValue(){ return this.value; }
        public List<Integer> getAncestor(){ return this.ancestor; }
        public Boolean isDecided(){ return this.decided; }
        public Boolean isImplied(){ return this.implied; }
        public Boolean isConflictImplied(){ return this.conflictImplied; }


    }

    private Stack<ArrayList<assignedLiteral>> procedureStack;

    public CDCLprocedureStack(){

        // chiedere se il livello 0 è il primo deciso oppure se è l'1 e quindi lo 0 sarà vuoto
        // o conterrà solo i letterali implicati prima di compiere una decisione

        this.procedureStack = new Stack<>();
    }

    public void addDecidedLiteral(Integer name, Boolean value) {

        assignedLiteral literalDecided = new assignedLiteral(name, value);
        literalDecided.setDecided();
        ArrayList<assignedLiteral> newLevel = new ArrayList<>();
        newLevel.add(literalDecided);

        this.procedureStack.push(newLevel);
    }

    public void addImpliedLiteral(Integer name, Boolean value, List<Integer> ancestor){

        assignedLiteral literalImplied = new assignedLiteral(name, value);
        literalImplied.setImplied(ancestor);
        ArrayList<assignedLiteral> currentLevel = this.procedureStack.pop();
        currentLevel.add(literalImplied);

        this.procedureStack.push(currentLevel);
    }

    public void addConflictImpliedLiteral(Integer name, Boolean value, List<Integer> ancestor){

        assignedLiteral literalConflictImplied = new assignedLiteral(name, value);
        literalConflictImplied.setConflictImplied(ancestor);
        ArrayList<assignedLiteral> newLevel = new ArrayList<>();
        newLevel.add(literalConflictImplied);

        this.procedureStack.push(newLevel);
    }

    public ArrayList<assignedLiteral> deleteLevel(){

        ArrayList<assignedLiteral> currentLevel = this.procedureStack.pop();
        return currentLevel;

    }

    public int size(){ return this.procedureStack.size(); }


}
