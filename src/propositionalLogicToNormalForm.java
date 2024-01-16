import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import propLogicRecognizer.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class propositionalLogicToNormalForm {

    public String outputFile;

    public propositionalLogicToNormalForm(String filePath) throws Exception {

        // Leggi il contenuto del file di testo
        CharStream input = CharStreams.fromFileName(filePath);

        // Crea un lexer usando la grammatica generata
        propLogicLexer lexer = new propLogicLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        propLogicParser parser = new propLogicParser(tokens);

        ParseTree tree = parser.expression();

        Normalizer normalizer = new Normalizer();
        Integer cnfForm = normalizer.visit(tree);

        StringBuilder problemInCnf = new StringBuilder("p cnf " + (normalizer.newVar - 1) + " " + normalizer.problemCNF.size() + "\n" );
        problemInCnf.append(cnfForm.toString()).append(" 0\n");
        for ( List<Integer> clause : normalizer.problemCNF){
            for ( Integer literal : clause)
                problemInCnf.append(literal.toString()).append(" ");
            problemInCnf.append("0\n");
        }

        this.outputFile = filePath.replace(".txt", ".cnf");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(String.valueOf(problemInCnf));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static class Normalizer extends propLogicBaseVisitor<Integer> {

        public Integer newVar = 1;
        public Map<String, Integer> variablesAssociation = new HashMap<>();
        public List<List<Integer>> problemCNF = new ArrayList<>();


        @Override
        public Integer visitMain(propLogicParser.MainContext ctx) {

            return visit(ctx.expression());
        }

        @Override
        public Integer visitVariable(propLogicParser.VariableContext ctx) {

            Integer representative;
            if ( this.variablesAssociation.containsKey(ctx.getText()) )
                representative = this.variablesAssociation.get(ctx.getText() );
            else {
                representative = newVar;
                this.variablesAssociation.put(ctx.getText(), representative);
                newVar += 1;
            }

            return representative;
        }

        @Override
        public Integer visitNot(propLogicParser.NotContext ctx) {

            // new variable for representation of the current NOT
            Integer repP = newVar;
            newVar += 1;

            // get representative of the expression negated by the NOT
            Integer repF1 = visit(ctx.expression());

            // the transformation in CNF leads to this three clauses that are put in and
            Integer[] firstClause = {-1 * repP, -1 * repF1};
            Integer[] secondClause = {repP, repF1};

            // add the two clauses to the problem clauses in CNF
            this.problemCNF.add(Arrays.asList(firstClause));
            this.problemCNF.add(Arrays.asList(secondClause));

            // I will return the representative of my NOT that could be necessary to a call happened before
            return repP;

        }

        @Override
        public Integer visitAnd(propLogicParser.AndContext ctx) {

            // new variable for representation of the current AND
            Integer repP = newVar;
            newVar += 1;

            // get representative of the expressions of the AND
            Integer repF1 = visit(ctx.expression(0));
            Integer repF2 = visit(ctx.expression(1));

            // the transformation in CNF leads to this three clauses that are put in and
            Integer[] firstClause = {-1 * repP, repF1};
            Integer[] secondClause = {-1 * repP, repF2};
            Integer[] thirdClause = {-1 * repF1, -1 * repF2, repP};

            // add the three clauses to the problem clauses in CNF
            this.problemCNF.add(Arrays.asList(firstClause));
            this.problemCNF.add(Arrays.asList(secondClause));
            this.problemCNF.add(Arrays.asList(thirdClause));

            // I will return the representative of my AND that could be necessary to a call happened before
            return repP;

        }

        @Override
        public Integer visitOr(propLogicParser.OrContext ctx) {

            // new variable for representation of the current OR
            Integer repP = newVar;
            newVar += 1;

            // get representative of the expression of the OR
            Integer repF1 = visit(ctx.expression(0));
            Integer repF2 = visit(ctx.expression(1));

            // the transformation in CNF leads to this three clauses that are put in and
            Integer[] firstClause   =   {-1 * repP,  repF1, repF2};
            Integer[] secondClause  =   {-1 * repF1, repP};
            Integer[] thirdClause   =   {-1 * repF2, repP};

            // add the three clauses to the problem clauses in CNF
            this.problemCNF.add(Arrays.asList(firstClause));
            this.problemCNF.add(Arrays.asList(secondClause));
            this.problemCNF.add(Arrays.asList(thirdClause));

            // I will return the representative of my OR that could be necessary to a call happened before
            return repP;

        }

        @Override
        public Integer visitImplies(propLogicParser.ImpliesContext ctx) {

            // new variable for representation of the current IMPLICATION
            Integer repP = newVar;
            newVar += 1;

            // get representative of the expression negated by the IMPLICATION
            Integer repF1 = visit(ctx.expression(0));
            Integer repF2 = visit(ctx.expression(1));

            // the transformation in CNF leads to this three clauses that are put in and
            Integer[] firstClause   =   {-1 * repP, -1 * repF1, repF2};
            Integer[] secondClause  =   {repF1, repP};
            Integer[] thirdClause   =   {-1 * repF2, repP};

            // add the three clauses to the problem clauses in CNF
            this.problemCNF.add(Arrays.asList(firstClause));
            this.problemCNF.add(Arrays.asList(secondClause));
            this.problemCNF.add(Arrays.asList(thirdClause));

            // I will return the representative of my not that could be necessary to a call happened before
            return repP;

        }



        @Override
        public Integer visitIff(propLogicParser.IffContext ctx) {

            // new variable for representation of the current IFF
            Integer repP = newVar;
            newVar += 1;

            // get representative of the expression negated by the IFF
            Integer repF1 = visit(ctx.expression(0));
            Integer repF2 = visit(ctx.expression(1));

            // the transformation in CNF leads to this four clauses that are put in and
            Integer[] firstClause   =   { -1 * repP, -1 * repF1, repF2};
            Integer[] secondClause  =   { -1 * repP, -1 * repF2, repF1};
            Integer[] thirdClause   =   { repF1, repF2, repP};
            Integer[] fourthClause  =   { -1 * repF2, -1 * repF2, repP};

            // add the four clauses to the problem clauses in CNF
            this.problemCNF.add(Arrays.asList(firstClause));
            this.problemCNF.add(Arrays.asList(secondClause));
            this.problemCNF.add(Arrays.asList(thirdClause));
            this.problemCNF.add(Arrays.asList(fourthClause));

            // I will return the representative of my not that could be necessary to a call happened before
            return repP;

        }

        @Override
        public Integer visitParentheses(propLogicParser.ParenthesesContext ctx) {
            // just return the representative of the expression inside the parenthesis
            return visit(ctx.expression());
        }

    }

}
