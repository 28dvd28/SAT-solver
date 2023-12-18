import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CNFProblem {

    private int variableNumber;
    private int clausesNumber;
    private List<List<Integer>> clauses = new ArrayList<>();

    public CNFProblem(String fileName){

        String filePath = "src/Input_test/" + fileName;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {

                // Se la riga inizia con 'c', è un commento
                if (line.startsWith("c")) {
                    continue;
                }
                else if (line.startsWith("p cnf")) {
                    // La riga inizia con 'p cnf', indicando l'inizio delle specifiche CNF

                    String[] splitedLine = line.split(" ");
                    this.variableNumber = Integer.parseInt(splitedLine[2]);
                    this.clausesNumber = Integer.parseInt(splitedLine[3]);

                } else if (!line.startsWith("%")) {
                    // La riga non è un commento e non inizia con '%', quindi è una clausola CNF

                    String[] splitedLine = line.split(" ");
                    List<Integer> listedLine = new ArrayList<>();

                    for ( String s : splitedLine )
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

    }

    public void learnClause(List<Integer> newClause){

        if (!this.clauses.contains(newClause))
            this.clauses.add(newClause);

    }

    public void forgotClause(List<Integer> clause){
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

}
