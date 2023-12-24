import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CNFProblem {

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

        if (!this.clauses.contains(newClause))
            this.clauses.add(newClause);

    }

    public void forgotClause(List<Integer> clause){

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
    public void updateTwoWatchedLiteral(List<Integer[]> newWatchedliterals){ this.twoWatchedLiteral = newWatchedliterals; }

    public void subsumption(){

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
