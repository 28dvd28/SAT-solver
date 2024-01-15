import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import propLogicRecognizer.*;

public class propositionalLogicToNormalForm {

    public static void main(String[] args) throws Exception {
        // Leggi il contenuto del file di testo
        CharStream input = CharStreams.fromFileName("tuoFile.txt");

        // Crea un lexer usando la grammatica generata
        propLogicLexer lexer = new propLogicLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        propLogicParser parser = new propLogicParser(tokens);

        ParseTree tree = parser.expression();

        Normalizer normalizer = new Normalizer();
        String cnfForm = normalizer.visit(tree);
        System.out.println(cnfForm);


    }

    public static class Normalizer implements propLogicVisitor<String> {
        @Override
        public String visitNot(propLogicParser.NotContext ctx) {
            return "-" + ctx.expression();
        }

        @Override
        public String visitVariable(propLogicParser.VariableContext ctx) {
            return ctx.getText();
        }

        @Override
        public String visitOr(propLogicParser.OrContext ctx) {
            return null;
        }

        @Override
        public String visitImplies(propLogicParser.ImpliesContext ctx) {
            return null;
        }

        @Override
        public String visitAnd(propLogicParser.AndContext ctx) {
            return null;
        }

        @Override
        public String visitIff(propLogicParser.IffContext ctx) {
            return null;
        }

        @Override
        public String visitParentheses(propLogicParser.ParenthesesContext ctx) {
            return null;
        }

        @Override
        public String visit(ParseTree parseTree) {
            return null;
        }

        @Override
        public String visitChildren(RuleNode ruleNode) {
            return null;
        }

        @Override
        public String visitTerminal(TerminalNode terminalNode) {
            return null;
        }

        @Override
        public String visitErrorNode(ErrorNode errorNode) {
            return null;
        }
    }

}
