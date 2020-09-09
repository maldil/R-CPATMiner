// $ANTLR 3.5.2 PyType.g 2020-09-01 20:40:04
package python3.typeinference.antlr;
import org.antlr.runtime.*;

@SuppressWarnings("all")
public class PyTypeLexer extends Lexer {
	public static final int EOF=-1;
	public static final int ANY=4;
	public static final int CLOSE_SQ_BR=5;
	public static final int INT=6;
	public static final int LIST=7;
	public static final int OPEN_SQ_BR=8;
	public static final int STRING=9;
	public static final int WHITESPACE=10;

	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public PyTypeLexer() {} 
	public PyTypeLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public PyTypeLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "PyType.g"; }

	// $ANTLR start "ANY"
	public final void mANY() throws RecognitionException {
		try {
			int _type = ANY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:2:5: ( 'Any' )
			// PyType.g:2:7: 'Any'
			{
			match("Any"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ANY"

	// $ANTLR start "CLOSE_SQ_BR"
	public final void mCLOSE_SQ_BR() throws RecognitionException {
		try {
			int _type = CLOSE_SQ_BR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:3:13: ( ']' )
			// PyType.g:3:15: ']'
			{
			match(']'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CLOSE_SQ_BR"

	// $ANTLR start "INT"
	public final void mINT() throws RecognitionException {
		try {
			int _type = INT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:4:5: ( 'int' )
			// PyType.g:4:7: 'int'
			{
			match("int"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INT"

	// $ANTLR start "LIST"
	public final void mLIST() throws RecognitionException {
		try {
			int _type = LIST;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:5:6: ( 'List' )
			// PyType.g:5:8: 'List'
			{
			match("List"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LIST"

	// $ANTLR start "OPEN_SQ_BR"
	public final void mOPEN_SQ_BR() throws RecognitionException {
		try {
			int _type = OPEN_SQ_BR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:6:12: ( '[' )
			// PyType.g:6:14: '['
			{
			match('['); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OPEN_SQ_BR"

	// $ANTLR start "STRING"
	public final void mSTRING() throws RecognitionException {
		try {
			int _type = STRING;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:7:8: ( 'str' )
			// PyType.g:7:10: 'str'
			{
			match("str"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STRING"

	// $ANTLR start "WHITESPACE"
	public final void mWHITESPACE() throws RecognitionException {
		try {
			int _type = WHITESPACE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:65:12: ( ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+ )
			// PyType.g:65:14: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+
			{
			// PyType.g:65:14: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+
			int cnt1=0;
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( ((LA1_0 >= '\t' && LA1_0 <= '\n')||(LA1_0 >= '\f' && LA1_0 <= '\r')||LA1_0==' ') ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// PyType.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt1 >= 1 ) break loop1;
					EarlyExitException eee = new EarlyExitException(1, input);
					throw eee;
				}
				cnt1++;
			}

			 _channel = HIDDEN; 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WHITESPACE"

	@Override
	public void mTokens() throws RecognitionException {
		// PyType.g:1:8: ( ANY | CLOSE_SQ_BR | INT | LIST | OPEN_SQ_BR | STRING | WHITESPACE )
		int alt2=7;
		switch ( input.LA(1) ) {
		case 'A':
			{
			alt2=1;
			}
			break;
		case ']':
			{
			alt2=2;
			}
			break;
		case 'i':
			{
			alt2=3;
			}
			break;
		case 'L':
			{
			alt2=4;
			}
			break;
		case '[':
			{
			alt2=5;
			}
			break;
		case 's':
			{
			alt2=6;
			}
			break;
		case '\t':
		case '\n':
		case '\f':
		case '\r':
		case ' ':
			{
			alt2=7;
			}
			break;
		default:
			NoViableAltException nvae =
				new NoViableAltException("", 2, 0, input);
			throw nvae;
		}
		switch (alt2) {
			case 1 :
				// PyType.g:1:10: ANY
				{
				mANY(); 

				}
				break;
			case 2 :
				// PyType.g:1:14: CLOSE_SQ_BR
				{
				mCLOSE_SQ_BR(); 

				}
				break;
			case 3 :
				// PyType.g:1:26: INT
				{
				mINT(); 

				}
				break;
			case 4 :
				// PyType.g:1:30: LIST
				{
				mLIST(); 

				}
				break;
			case 5 :
				// PyType.g:1:35: OPEN_SQ_BR
				{
				mOPEN_SQ_BR(); 

				}
				break;
			case 6 :
				// PyType.g:1:46: STRING
				{
				mSTRING(); 

				}
				break;
			case 7 :
				// PyType.g:1:53: WHITESPACE
				{
				mWHITESPACE(); 

				}
				break;

		}
	}



}
