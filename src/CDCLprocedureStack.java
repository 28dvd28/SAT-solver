import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CDCLprocedureStack {

    /**
     * This class implements the stack of the CDCL procedure. It is just a normal stack but each element
     * of the stack is a List of assigned literal.
     *
     * The implementation is very simple, with the method
     * addDecidedLiteral that add a decided literal generating a new level because in the CDCL every decision
     * make a new level in the stack. The method addImpliedLiteral just add a new implied literal to the last level.
     *
     * Useful method for the backtrack phase is the deleteLevel that simply remove the top level and the method getLevelAt
     * is for get all the literal at a given level, important during the checks made during the explain process.
     */

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

    public String toString(){

        String output = "";
        int i = 0;
        for ( ArrayList<assignedLiteral> level : this.procedureStack){

            output = output.concat(i + "}");

            for (assignedLiteral literal : level){

                if ( literal.isDecided())
                    output = output.concat("[DECIDED] " + literal.getName().toString() + " -> " + literal.getValue().toString() + "  \n");
                else if ( literal.isImplied())
                    output = output.concat("    [IMPLIED] " + literal.getName().toString() + " -> " + literal.getValue().toString() + "(" + literal.getAncestor() + ")" +  "  \n");
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
