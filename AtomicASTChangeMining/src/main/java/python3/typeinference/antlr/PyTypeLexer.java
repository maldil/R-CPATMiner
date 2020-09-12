package python3.typeinference.antlr;// $ANTLR 3.5.2 PyType.g 2020-09-10 21:44:11

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class PyTypeLexer extends Lexer {
	public static final int EOF=-1;
	public static final int ANY=4;
	public static final int CLOSE_SQ_BR=5;
	public static final int COMMA=6;
	public static final int INT=7;
	public static final int LIST=8;
	public static final int Nothing=9;
	public static final int OPEN_SQ_BR=10;
	public static final int STRING=11;
	public static final int UNION=12;
	public static final int WHITESPACE=13;

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

	// $ANTLR start "COMMA"
	public final void mCOMMA() throws RecognitionException {
		try {
			int _type = COMMA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:4:7: ( ',' )
			// PyType.g:4:9: ','
			{
			match(','); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMA"

	// $ANTLR start "INT"
	public final void mINT() throws RecognitionException {
		try {
			int _type = INT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:5:5: ( 'int' )
			// PyType.g:5:7: 'int'
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
			// PyType.g:6:6: ( 'List' )
			// PyType.g:6:8: 'List'
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

	// $ANTLR start "Nothing"
	public final void mNothing() throws RecognitionException {
		try {
			int _type = Nothing;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:7:9: ( 'nothing' )
			// PyType.g:7:11: 'nothing'
			{
			match("nothing"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Nothing"

	// $ANTLR start "OPEN_SQ_BR"
	public final void mOPEN_SQ_BR() throws RecognitionException {
		try {
			int _type = OPEN_SQ_BR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:8:12: ( '[' )
			// PyType.g:8:14: '['
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
			// PyType.g:9:8: ( 'str' )
			// PyType.g:9:10: 'str'
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

	// $ANTLR start "UNION"
	public final void mUNION() throws RecognitionException {
		try {
			int _type = UNION;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:10:7: ( 'Union' )
			// PyType.g:10:9: 'Union'
			{
			match("Union"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "UNION"

	// $ANTLR start "WHITESPACE"
	public final void mWHITESPACE() throws RecognitionException {
		try {
			int _type = WHITESPACE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:68:12: ( ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+ )
			// PyType.g:68:14: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+
			{
			// PyType.g:68:14: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+
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
		// PyType.g:1:8: ( ANY | CLOSE_SQ_BR | COMMA | INT | LIST | Nothing | OPEN_SQ_BR | STRING | UNION | WHITESPACE )
		int alt2=10;
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
		case ',':
			{
			alt2=3;
			}
			break;
		case 'i':
			{
			alt2=4;
			}
			break;
		case 'L':
			{
			alt2=5;
			}
			break;
		case 'n':
			{
			alt2=6;
			}
			break;
		case '[':
			{
			alt2=7;
			}
			break;
		case 's':
			{
			alt2=8;
			}
			break;
		case 'U':
			{
			alt2=9;
			}
			break;
		case '\t':
		case '\n':
		case '\f':
		case '\r':
		case ' ':
			{
			alt2=10;
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
				// PyType.g:1:26: COMMA
				{
				mCOMMA(); 

				}
				break;
			case 4 :
				// PyType.g:1:32: INT
				{
				mINT(); 

				}
				break;
			case 5 :
				// PyType.g:1:36: LIST
				{
				mLIST(); 

				}
				break;
			case 6 :
				// PyType.g:1:41: Nothing
				{
				mNothing(); 

				}
				break;
			case 7 :
				// PyType.g:1:49: OPEN_SQ_BR
				{
				mOPEN_SQ_BR(); 

				}
				break;
			case 8 :
				// PyType.g:1:60: STRING
				{
				mSTRING(); 

				}
				break;
			case 9 :
				// PyType.g:1:67: UNION
				{
				mUNION(); 

				}
				break;
			case 10 :
				// PyType.g:1:73: WHITESPACE
				{
				mWHITESPACE(); 

				}
				break;

		}
	}



}
