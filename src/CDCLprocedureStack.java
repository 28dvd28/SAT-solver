import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class CDCLprocedureStack {

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

        return this.procedureStack.pop();

    }

    public ArrayList<assignedLiteral> getTopLevel(){

        return this.procedureStack.peek();

    }

    public int size(){

        return this.procedureStack.size();

    }


}
