package python3.typeinference.antlr;// $ANTLR 3.5.2 PyType.g 2020-09-13 15:37:52

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class PyTypeLexer extends Lexer {
	public static final int EOF=-1;
	public static final int CLOSE_SQ_BR=4;
	public static final int COMMA=5;
	public static final int DIGIT=6;
	public static final int DOT=7;
	public static final int HYPHEN=8;
	public static final int LETTER=9;
	public static final int LIST=10;
	public static final int NUMBER=11;
	public static final int Nothing=12;
	public static final int OPEN_SQ_BR=13;
	public static final int SimpleType=14;
	public static final int UNDERSCORE=15;
	public static final int UNION=16;
	public static final int WHITESPACE=17;

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

	// $ANTLR start "CLOSE_SQ_BR"
	public final void mCLOSE_SQ_BR() throws RecognitionException {
		try {
			int _type = CLOSE_SQ_BR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:2:13: ( ']' )
			// PyType.g:2:15: ']'
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
			// PyType.g:3:7: ( ',' )
			// PyType.g:3:9: ','
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

	// $ANTLR start "DOT"
	public final void mDOT() throws RecognitionException {
		try {
			int _type = DOT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:4:5: ( '\\.' )
			// PyType.g:4:7: '\\.'
			{
			match('.'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DOT"

	// $ANTLR start "HYPHEN"
	public final void mHYPHEN() throws RecognitionException {
		try {
			int _type = HYPHEN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:5:8: ( '-' )
			// PyType.g:5:10: '-'
			{
			match('-'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "HYPHEN"

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

	// $ANTLR start "UNDERSCORE"
	public final void mUNDERSCORE() throws RecognitionException {
		try {
			int _type = UNDERSCORE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:9:12: ( '_' )
			// PyType.g:9:14: '_'
			{
			match('_'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "UNDERSCORE"

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

	// $ANTLR start "DIGIT"
	public final void mDIGIT() throws RecognitionException {
		try {
			int _type = DIGIT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:55:8: ( '0' .. '9' )
			// PyType.g:
			{
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DIGIT"

	// $ANTLR start "LETTER"
	public final void mLETTER() throws RecognitionException {
		try {
			int _type = LETTER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:57:8: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )
			// PyType.g:
			{
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LETTER"

	// $ANTLR start "NUMBER"
	public final void mNUMBER() throws RecognitionException {
		try {
			int _type = NUMBER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:59:8: ( ( DIGIT )+ )
			// PyType.g:59:10: ( DIGIT )+
			{
			// PyType.g:59:10: ( DIGIT )+
			int cnt1=0;
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( ((LA1_0 >= '0' && LA1_0 <= '9')) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// PyType.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
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

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NUMBER"

	// $ANTLR start "SimpleType"
	public final void mSimpleType() throws RecognitionException {
		try {
			int _type = SimpleType;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:61:12: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '.' )* ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' ) )
			// PyType.g:61:14: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '.' )* ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// PyType.g:61:37: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '.' )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( ((LA2_0 >= '0' && LA2_0 <= '9')||(LA2_0 >= 'A' && LA2_0 <= 'Z')||LA2_0=='_'||(LA2_0 >= 'a' && LA2_0 <= 'z')) ) {
					int LA2_1 = input.LA(2);
					if ( (LA2_1=='.'||(LA2_1 >= '0' && LA2_1 <= '9')||(LA2_1 >= 'A' && LA2_1 <= 'Z')||LA2_1=='_'||(LA2_1 >= 'a' && LA2_1 <= 'z')) ) {
						alt2=1;
					}

				}
				else if ( (LA2_0=='.') ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// PyType.g:
					{
					if ( input.LA(1)=='.'||(input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
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
					break loop2;
				}
			}

			if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SimpleType"

	// $ANTLR start "WHITESPACE"
	public final void mWHITESPACE() throws RecognitionException {
		try {
			int _type = WHITESPACE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:73:12: ( ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+ )
			// PyType.g:73:14: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+
			{
			// PyType.g:73:14: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+
			int cnt3=0;
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( ((LA3_0 >= '\t' && LA3_0 <= '\n')||(LA3_0 >= '\f' && LA3_0 <= '\r')||LA3_0==' ') ) {
					alt3=1;
				}

				switch (alt3) {
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
					if ( cnt3 >= 1 ) break loop3;
					EarlyExitException eee = new EarlyExitException(3, input);
					throw eee;
				}
				cnt3++;
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
		// PyType.g:1:8: ( CLOSE_SQ_BR | COMMA | DOT | HYPHEN | LIST | Nothing | OPEN_SQ_BR | UNDERSCORE | UNION | DIGIT | LETTER | NUMBER | SimpleType | WHITESPACE )
		int alt4=14;
		switch ( input.LA(1) ) {
		case ']':
			{
			alt4=1;
			}
			break;
		case ',':
			{
			alt4=2;
			}
			break;
		case '.':
			{
			alt4=3;
			}
			break;
		case '-':
			{
			alt4=4;
			}
			break;
		case 'L':
			{
			switch ( input.LA(2) ) {
			case 'i':
				{
				int LA4_13 = input.LA(3);
				if ( (LA4_13=='s') ) {
					int LA4_21 = input.LA(4);
					if ( (LA4_21=='t') ) {
						int LA4_24 = input.LA(5);
						if ( (LA4_24=='.'||(LA4_24 >= '0' && LA4_24 <= '9')||(LA4_24 >= 'A' && LA4_24 <= 'Z')||LA4_24=='_'||(LA4_24 >= 'a' && LA4_24 <= 'z')) ) {
							alt4=13;
						}

						else {
							alt4=5;
						}

					}

					else {
						alt4=13;
					}

				}

				else {
					alt4=13;
				}

				}
				break;
			case '.':
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
			case 'G':
			case 'H':
			case 'I':
			case 'J':
			case 'K':
			case 'L':
			case 'M':
			case 'N':
			case 'O':
			case 'P':
			case 'Q':
			case 'R':
			case 'S':
			case 'T':
			case 'U':
			case 'V':
			case 'W':
			case 'X':
			case 'Y':
			case 'Z':
			case '_':
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
			case 'g':
			case 'h':
			case 'j':
			case 'k':
			case 'l':
			case 'm':
			case 'n':
			case 'o':
			case 'p':
			case 'q':
			case 'r':
			case 's':
			case 't':
			case 'u':
			case 'v':
			case 'w':
			case 'x':
			case 'y':
			case 'z':
				{
				alt4=13;
				}
				break;
			default:
				alt4=11;
			}
			}
			break;
		case 'n':
			{
			switch ( input.LA(2) ) {
			case 'o':
				{
				int LA4_16 = input.LA(3);
				if ( (LA4_16=='t') ) {
					int LA4_22 = input.LA(4);
					if ( (LA4_22=='h') ) {
						int LA4_25 = input.LA(5);
						if ( (LA4_25=='i') ) {
							int LA4_28 = input.LA(6);
							if ( (LA4_28=='n') ) {
								int LA4_30 = input.LA(7);
								if ( (LA4_30=='g') ) {
									int LA4_32 = input.LA(8);
									if ( (LA4_32=='.'||(LA4_32 >= '0' && LA4_32 <= '9')||(LA4_32 >= 'A' && LA4_32 <= 'Z')||LA4_32=='_'||(LA4_32 >= 'a' && LA4_32 <= 'z')) ) {
										alt4=13;
									}

									else {
										alt4=6;
									}

								}

								else {
									alt4=13;
								}

							}

							else {
								alt4=13;
							}

						}

						else {
							alt4=13;
						}

					}

					else {
						alt4=13;
					}

				}

				else {
					alt4=13;
				}

				}
				break;
			case '.':
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
			case 'G':
			case 'H':
			case 'I':
			case 'J':
			case 'K':
			case 'L':
			case 'M':
			case 'N':
			case 'O':
			case 'P':
			case 'Q':
			case 'R':
			case 'S':
			case 'T':
			case 'U':
			case 'V':
			case 'W':
			case 'X':
			case 'Y':
			case 'Z':
			case '_':
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
			case 'g':
			case 'h':
			case 'i':
			case 'j':
			case 'k':
			case 'l':
			case 'm':
			case 'n':
			case 'p':
			case 'q':
			case 'r':
			case 's':
			case 't':
			case 'u':
			case 'v':
			case 'w':
			case 'x':
			case 'y':
			case 'z':
				{
				alt4=13;
				}
				break;
			default:
				alt4=11;
			}
			}
			break;
		case '[':
			{
			alt4=7;
			}
			break;
		case '_':
			{
			int LA4_8 = input.LA(2);
			if ( (LA4_8=='.'||(LA4_8 >= '0' && LA4_8 <= '9')||(LA4_8 >= 'A' && LA4_8 <= 'Z')||LA4_8=='_'||(LA4_8 >= 'a' && LA4_8 <= 'z')) ) {
				alt4=13;
			}

			else {
				alt4=8;
			}

			}
			break;
		case 'U':
			{
			switch ( input.LA(2) ) {
			case 'n':
				{
				int LA4_18 = input.LA(3);
				if ( (LA4_18=='i') ) {
					int LA4_23 = input.LA(4);
					if ( (LA4_23=='o') ) {
						int LA4_26 = input.LA(5);
						if ( (LA4_26=='n') ) {
							int LA4_29 = input.LA(6);
							if ( (LA4_29=='.'||(LA4_29 >= '0' && LA4_29 <= '9')||(LA4_29 >= 'A' && LA4_29 <= 'Z')||LA4_29=='_'||(LA4_29 >= 'a' && LA4_29 <= 'z')) ) {
								alt4=13;
							}

							else {
								alt4=9;
							}

						}

						else {
							alt4=13;
						}

					}

					else {
						alt4=13;
					}

				}

				else {
					alt4=13;
				}

				}
				break;
			case '.':
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
			case 'G':
			case 'H':
			case 'I':
			case 'J':
			case 'K':
			case 'L':
			case 'M':
			case 'N':
			case 'O':
			case 'P':
			case 'Q':
			case 'R':
			case 'S':
			case 'T':
			case 'U':
			case 'V':
			case 'W':
			case 'X':
			case 'Y':
			case 'Z':
			case '_':
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
			case 'g':
			case 'h':
			case 'i':
			case 'j':
			case 'k':
			case 'l':
			case 'm':
			case 'o':
			case 'p':
			case 'q':
			case 'r':
			case 's':
			case 't':
			case 'u':
			case 'v':
			case 'w':
			case 'x':
			case 'y':
			case 'z':
				{
				alt4=13;
				}
				break;
			default:
				alt4=11;
			}
			}
			break;
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			{
			int LA4_10 = input.LA(2);
			if ( ((LA4_10 >= '0' && LA4_10 <= '9')) ) {
				alt4=12;
			}

			else {
				alt4=10;
			}

			}
			break;
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F':
		case 'G':
		case 'H':
		case 'I':
		case 'J':
		case 'K':
		case 'M':
		case 'N':
		case 'O':
		case 'P':
		case 'Q':
		case 'R':
		case 'S':
		case 'T':
		case 'V':
		case 'W':
		case 'X':
		case 'Y':
		case 'Z':
		case 'a':
		case 'b':
		case 'c':
		case 'd':
		case 'e':
		case 'f':
		case 'g':
		case 'h':
		case 'i':
		case 'j':
		case 'k':
		case 'l':
		case 'm':
		case 'o':
		case 'p':
		case 'q':
		case 'r':
		case 's':
		case 't':
		case 'u':
		case 'v':
		case 'w':
		case 'x':
		case 'y':
		case 'z':
			{
			int LA4_11 = input.LA(2);
			if ( (LA4_11=='.'||(LA4_11 >= '0' && LA4_11 <= '9')||(LA4_11 >= 'A' && LA4_11 <= 'Z')||LA4_11=='_'||(LA4_11 >= 'a' && LA4_11 <= 'z')) ) {
				alt4=13;
			}

			else {
				alt4=11;
			}

			}
			break;
		case '\t':
		case '\n':
		case '\f':
		case '\r':
		case ' ':
			{
			alt4=14;
			}
			break;
		default:
			NoViableAltException nvae =
				new NoViableAltException("", 4, 0, input);
			throw nvae;
		}
		switch (alt4) {
			case 1 :
				// PyType.g:1:10: CLOSE_SQ_BR
				{
				mCLOSE_SQ_BR(); 

				}
				break;
			case 2 :
				// PyType.g:1:22: COMMA
				{
				mCOMMA(); 

				}
				break;
			case 3 :
				// PyType.g:1:28: DOT
				{
				mDOT(); 

				}
				break;
			case 4 :
				// PyType.g:1:32: HYPHEN
				{
				mHYPHEN(); 

				}
				break;
			case 5 :
				// PyType.g:1:39: LIST
				{
				mLIST(); 

				}
				break;
			case 6 :
				// PyType.g:1:44: Nothing
				{
				mNothing(); 

				}
				break;
			case 7 :
				// PyType.g:1:52: OPEN_SQ_BR
				{
				mOPEN_SQ_BR(); 

				}
				break;
			case 8 :
				// PyType.g:1:63: UNDERSCORE
				{
				mUNDERSCORE(); 

				}
				break;
			case 9 :
				// PyType.g:1:74: UNION
				{
				mUNION(); 

				}
				break;
			case 10 :
				// PyType.g:1:80: DIGIT
				{
				mDIGIT(); 

				}
				break;
			case 11 :
				// PyType.g:1:86: LETTER
				{
				mLETTER(); 

				}
				break;
			case 12 :
				// PyType.g:1:93: NUMBER
				{
				mNUMBER(); 

				}
				break;
			case 13 :
				// PyType.g:1:100: SimpleType
				{
				mSimpleType(); 

				}
				break;
			case 14 :
				// PyType.g:1:111: WHITESPACE
				{
				mWHITESPACE(); 

				}
				break;

		}
	}



}
