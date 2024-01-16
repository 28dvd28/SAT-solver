package propLogicRecognizer;// Generated from C:/Users/cordi/Desktop/Cartella personale/Compiti scolastici/Materiale universit�/Magistrale/Primo Anno/Planning and Automated Reasoning/SAT-solver/src/propLogic.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link propLogicParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface propLogicVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link propLogicParser#main}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMain(propLogicParser.MainContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Not}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot(propLogicParser.NotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Variable}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(propLogicParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Or}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr(propLogicParser.OrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Implies}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplies(propLogicParser.ImpliesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code And}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd(propLogicParser.AndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Iff}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIff(propLogicParser.IffContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Parentheses}
	 * labeled alternative in {@link propLogicParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParentheses(propLogicParser.ParenthesesContext ctx);
}