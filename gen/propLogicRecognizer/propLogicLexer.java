// Generated from C:/Users/cordi/Desktop/Cartella personale/Compiti scolastici/Materiale universit�/Magistrale/Primo Anno/Planning and Automated Reasoning/SAT-solver/src/propLogicRecognizer/propLogic.g4 by ANTLR 4.13.1
package propLogicRecognizer;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class propLogicLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, LPAREN=2, RPAREN=3, AND=4, OR=5, NOT=6, IMPLIES=7, IFF=8, VAR=9;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"WS", "LPAREN", "RPAREN", "AND", "OR", "NOT", "IMPLIES", "IFF", "VAR"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'('", "')'", "'AND'", "'OR'", "'NOT'", "'->'", "'<->'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WS", "LPAREN", "RPAREN", "AND", "OR", "NOT", "IMPLIES", "IFF", 
			"VAR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public propLogicLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "propLogic.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\t7\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0001\u0000\u0004\u0000\u0015\b\u0000\u000b"+
		"\u0000\f\u0000\u0016\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001"+
		"\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\b\u0001\b\u0005\b3\b\b\n\b\f\b6\t\b\u0000\u0000"+
		"\t\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006"+
		"\r\u0007\u000f\b\u0011\t\u0001\u0000\u0003\u0003\u0000\t\n\r\r  \u0001"+
		"\u000019\u0001\u0000098\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003"+
		"\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007"+
		"\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001"+
		"\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000"+
		"\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0001\u0014\u0001\u0000"+
		"\u0000\u0000\u0003\u001a\u0001\u0000\u0000\u0000\u0005\u001c\u0001\u0000"+
		"\u0000\u0000\u0007\u001e\u0001\u0000\u0000\u0000\t\"\u0001\u0000\u0000"+
		"\u0000\u000b%\u0001\u0000\u0000\u0000\r)\u0001\u0000\u0000\u0000\u000f"+
		",\u0001\u0000\u0000\u0000\u00110\u0001\u0000\u0000\u0000\u0013\u0015\u0007"+
		"\u0000\u0000\u0000\u0014\u0013\u0001\u0000\u0000\u0000\u0015\u0016\u0001"+
		"\u0000\u0000\u0000\u0016\u0014\u0001\u0000\u0000\u0000\u0016\u0017\u0001"+
		"\u0000\u0000\u0000\u0017\u0018\u0001\u0000\u0000\u0000\u0018\u0019\u0006"+
		"\u0000\u0000\u0000\u0019\u0002\u0001\u0000\u0000\u0000\u001a\u001b\u0005"+
		"(\u0000\u0000\u001b\u0004\u0001\u0000\u0000\u0000\u001c\u001d\u0005)\u0000"+
		"\u0000\u001d\u0006\u0001\u0000\u0000\u0000\u001e\u001f\u0005A\u0000\u0000"+
		"\u001f \u0005N\u0000\u0000 !\u0005D\u0000\u0000!\b\u0001\u0000\u0000\u0000"+
		"\"#\u0005O\u0000\u0000#$\u0005R\u0000\u0000$\n\u0001\u0000\u0000\u0000"+
		"%&\u0005N\u0000\u0000&\'\u0005O\u0000\u0000\'(\u0005T\u0000\u0000(\f\u0001"+
		"\u0000\u0000\u0000)*\u0005-\u0000\u0000*+\u0005>\u0000\u0000+\u000e\u0001"+
		"\u0000\u0000\u0000,-\u0005<\u0000\u0000-.\u0005-\u0000\u0000./\u0005>"+
		"\u0000\u0000/\u0010\u0001\u0000\u0000\u000004\u0007\u0001\u0000\u0000"+
		"13\u0007\u0002\u0000\u000021\u0001\u0000\u0000\u000036\u0001\u0000\u0000"+
		"\u000042\u0001\u0000\u0000\u000045\u0001\u0000\u0000\u00005\u0012\u0001"+
		"\u0000\u0000\u000064\u0001\u0000\u0000\u0000\u0003\u0000\u00164\u0001"+
		"\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}