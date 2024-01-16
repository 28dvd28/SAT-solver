import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CNFProblem {

    /**
     * This class when initialized starts reading the .cnf file given as parameter and save the clauses in input.
     * The clauses are saved into a list of lists of integers where each integer is a literal and all the lists are the
     * clauses of the problem. The 0 is eliminated because is obvious that all the list are into a conjunction
     *
     * It is also initialized the two watched literal technique for the propagation in which it is generated a list
     * where in each position are saved the two literals firstly watched of the correspondent clause.
     *
     * There are other methods which description can be read in some comments in the following lines.
     *
     * In general this class will be used during the execution of the all procedure because it will save the
     * clauses of the problem to be resolved, at which will be added also the new clauses, and also the two watched literals
     * that will update during the execution
     */

    private int variableNumber;
    private int clausesNumber;
    private List<List<Integer>> clauses = new ArrayList<>();
    private List<Integer[]> twoWatchedLiteral;

    public CNFProblem(File fileName){

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = br.readLine()) != null) {

                // Se la riga inizia con 'c', è un commento
                if (line.startsWith("c")) {
                    continue;
                }
                else if (line.startsWith("p cnf")) {
                    // La riga inizia con 'p cnf', indicando l'inizio delle specifiche CNF

                    String[] splitedLine = line.split("[\s\t]+");
                    this.variableNumber = Integer.parseInt(splitedLine[2]);
                    this.clausesNumber = Integer.parseInt(splitedLine[3]);

                } else if (!line.startsWith("%")) {
                    // La riga non è un commento e non inizia con '%', quindi è una clausola CNF

                    String[] splitedLine = line.split("[\s\t]+");
                    List<Integer> listedLine = new ArrayList<>();

                    for ( String s : splitedLine )
                        if (!s.isEmpty())
                            listedLine.add(Integer.parseInt(s));

                    listedLine.removeIf(s -> s == 0);

                    if ( !listedLine.isEmpty() ) {
                        this.clauses.add(listedLine);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        subsumption();

        twoWatchedLiteral = new ArrayList<>();

        Integer [] twoLiteral;
        for( List<Integer> c : this.clauses) {
            twoLiteral = new Integer[2];
            if (c.size() == 1)
                twoLiteral[0] = c.get(0);
            else{
                twoLiteral[0] = c.get(0);
                twoLiteral[1] = c.get(1);
            }
            twoWatchedLiteral.add(twoLiteral);
        }

    }

    public void learnClause(List<Integer> newClause){

        /**
         * method implemented for adding new clause to the problem,
         * process that constitute the learning phase. The two watched literals will
         * be updated separately
         */

        if (!this.clauses.contains(newClause))
            this.clauses.add(newClause);

    }

    public void forgotClause(List<Integer> clause){

        /**
         * remove a clause from the problem and its correspondent two watched literals,
         * necessary during the forgetting process
         */

        int index = this.clauses.indexOf(clause);
        this.twoWatchedLiteral.remove(index);
        this.clauses.remove(clause);
    }

    public int getVariableNumber(){
        return this.variableNumber;
    }

    public int getClausesNumber(){
        return this.clausesNumber;
    }

    public List<List<Integer>> getClauses(){
        return  this.clauses;
    }

    public List<Integer[]> getTwoWatchedLiteral(){ return this.twoWatchedLiteral; }
    public void updateTwoWatchedLiteral(List<Integer[]> newWatchedliterals){

        /**
         *
         * Function with which the two watched literals list will be updated,
         * adding new elements or modifying the ones already present
         */

        this.twoWatchedLiteral = newWatchedliterals;
    }

    public void subsumption(){

        /**
         * Implementation of the subsumption, where are made an iteration all over
         * the clauses. Once finding ones that contains another, this is saved and at the end is removed
         * from the problem clause, so the problem is definitely smaller
         */

        List<List<Integer>> clauseToRemove = new ArrayList<>();
        List<List<Integer>> copy;

        for (List<Integer> c1 : this.clauses) {

            copy = new ArrayList<>(this.clauses);
            copy.remove(c1);

            for (List<Integer> c2 : copy)
                if (new HashSet<>(c2).containsAll(c1))
                    clauseToRemove.add(c2);
        }

        this.clauses.removeAll(clauseToRemove);

    }

}
