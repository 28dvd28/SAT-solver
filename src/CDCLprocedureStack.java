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
        this.procedureStack.push(new ArrayList<>());
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

    public ArrayList<Integer> getLiteralAtLevel(int index){

        List<assignedLiteral> level = this.procedureStack.get(index);
        ArrayList<Integer> definedLiteral = new ArrayList<>();

        for (assignedLiteral literal : level)
            definedLiteral.add(literal.getName());

        return definedLiteral;

    }

    public ArrayList<assignedLiteral> getLevelAt(int index){

        return this.procedureStack.elementAt(index);

    }

    public String toString(){

        String output = "";
        int i = 0;
        for ( ArrayList<assignedLiteral> level : this.procedureStack){

            output = output.concat(i + "}");

            for (assignedLiteral literal : level){

                if ( literal.isDecided())
                    output = output.concat("[DECIDED] " + literal.getName().toString() + " -> " + literal.getValue().toString() + "  ");
                else if ( literal.isImplied())
                    output = output.concat("[IMPLIED] " + literal.getName().toString() + " -> " + literal.getValue().toString() + "(" + literal.getAncestor() + ")" +  "  ");
                else if ( literal.isConflictImplied())
                    output = output.concat("[CONFLICT-IMPLIED] " + literal.getName().toString() + " -> " + literal.getValue().toString() + "(" + literal.getAncestor() + ")" + "  ");

            }

            output = output.concat("\n");
            i++;

        }

        return output;

    }

    public boolean isEmpty(){

        if ( this.procedureStack.size() == 1 && this.procedureStack.peek().isEmpty())
            return true;
        else
            return this.procedureStack.isEmpty();

    }




}
