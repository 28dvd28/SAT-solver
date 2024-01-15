package propLogicRecognizer;// Generated from C:/Users/cordi/Desktop/Cartella personale/Compiti scolastici/Materiale universit�/Magistrale/Primo Anno/Planning and Automated Reasoning/SAT-solver/src/propLogic.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link propLogicParser}.
 */
public interface propLogicListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code Not}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNot(propLogicParser.NotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Not}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNot(propLogicParser.NotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Variable}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterVariable(propLogicParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Variable}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitVariable(propLogicParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Or}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterOr(propLogicParser.OrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Or}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitOr(propLogicParser.OrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Implies}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterImplies(propLogicParser.ImpliesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Implies}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitImplies(propLogicParser.ImpliesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code And}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAnd(propLogicParser.AndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code And}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAnd(propLogicParser.AndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Iff}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIff(propLogicParser.IffContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Iff}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIff(propLogicParser.IffContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Parentheses}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParentheses(propLogicParser.ParenthesesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Parentheses}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParentheses(propLogicParser.ParenthesesContext ctx);
}