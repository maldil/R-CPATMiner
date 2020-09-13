// $ANTLR 3.5.2 PyType.g 2020-09-12 20:48:51

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
	public static final int QualifiedType=11;
	public static final int STRING=12;
	public static final int SimpleType=13;
	public static final int UNION=14;
	public static final int WHITESPACE=15;

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

	// $ANTLR start "SimpleType"
	public final void mSimpleType() throws RecognitionException {
		try {
			int _type = SimpleType;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:58:5: ( ( 'A' .. 'Z' | 'a' .. 'z' )+ )
			// PyType.g:58:7: ( 'A' .. 'Z' | 'a' .. 'z' )+
			{
			// PyType.g:58:7: ( 'A' .. 'Z' | 'a' .. 'z' )+
			int cnt1=0;
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( ((LA1_0 >= 'A' && LA1_0 <= 'Z')||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// PyType.g:
					{
					if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
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
	// $ANTLR end "SimpleType"

	// $ANTLR start "QualifiedType"
	public final void mQualifiedType() throws RecognitionException {
		try {
			int _type = QualifiedType;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:61:15: ( SimpleType | SimpleType '.' ( SimpleType )* )
			int alt3=2;
			alt3 = dfa3.predict(input);
			switch (alt3) {
				case 1 :
					// PyType.g:61:17: SimpleType
					{
					mSimpleType(); 

					}
					break;
				case 2 :
					// PyType.g:61:30: SimpleType '.' ( SimpleType )*
					{
					mSimpleType(); 

					match('.'); 
					// PyType.g:61:43: ( SimpleType )*
					loop2:
					while (true) {
						int alt2=2;
						int LA2_0 = input.LA(1);
						if ( ((LA2_0 >= 'A' && LA2_0 <= 'Z')||(LA2_0 >= 'a' && LA2_0 <= 'z')) ) {
							alt2=1;
						}

						switch (alt2) {
						case 1 :
							// PyType.g:61:43: SimpleType
							{
							mSimpleType(); 

							}
							break;

						default :
							break loop2;
						}
					}

					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "QualifiedType"

	// $ANTLR start "WHITESPACE"
	public final void mWHITESPACE() throws RecognitionException {
		try {
			int _type = WHITESPACE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:77:12: ( ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+ )
			// PyType.g:77:14: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+
			{
			// PyType.g:77:14: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+
			int cnt4=0;
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( ((LA4_0 >= '\t' && LA4_0 <= '\n')||(LA4_0 >= '\f' && LA4_0 <= '\r')||LA4_0==' ') ) {
					alt4=1;
				}

				switch (alt4) {
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
					if ( cnt4 >= 1 ) break loop4;
					EarlyExitException eee = new EarlyExitException(4, input);
					throw eee;
				}
				cnt4++;
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
		// PyType.g:1:8: ( ANY | CLOSE_SQ_BR | COMMA | INT | LIST | Nothing | OPEN_SQ_BR | STRING | UNION | SimpleType | QualifiedType | WHITESPACE )
		int alt5=12;
		alt5 = dfa5.predict(input);
		switch (alt5) {
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
				// PyType.g:1:73: SimpleType
				{
				mSimpleType(); 

				}
				break;
			case 11 :
				// PyType.g:1:84: QualifiedType
				{
				mQualifiedType(); 

				}
				break;
			case 12 :
				// PyType.g:1:98: WHITESPACE
				{
				mWHITESPACE(); 

				}
				break;

		}
	}


	protected DFA3 dfa3 = new DFA3(this);
	protected DFA5 dfa5 = new DFA5(this);
	static final String DFA3_eotS =
		"\1\uffff\1\2\2\uffff";
	static final String DFA3_eofS =
		"\4\uffff";
	static final String DFA3_minS =
		"\1\101\1\56\2\uffff";
	static final String DFA3_maxS =
		"\2\172\2\uffff";
	static final String DFA3_acceptS =
		"\2\uffff\1\1\1\2";
	static final String DFA3_specialS =
		"\4\uffff}>";
	static final String[] DFA3_transitionS = {
			"\32\1\6\uffff\32\1",
			"\1\3\22\uffff\32\1\6\uffff\32\1",
			"",
			""
	};

	static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
	static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
	static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
	static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
	static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
	static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
	static final short[][] DFA3_transition;

	static {
		int numStates = DFA3_transitionS.length;
		DFA3_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
		}
	}

	protected class DFA3 extends DFA {

		public DFA3(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 3;
			this.eot = DFA3_eot;
			this.eof = DFA3_eof;
			this.min = DFA3_min;
			this.max = DFA3_max;
			this.accept = DFA3_accept;
			this.special = DFA3_special;
			this.transition = DFA3_transition;
		}
		@Override
		public String getDescription() {
			return "61:1: QualifiedType : ( SimpleType | SimpleType '.' ( SimpleType )* );";
		}
	}

	static final String DFA5_eotS =
		"\1\uffff\1\15\2\uffff\3\15\1\uffff\3\15\1\uffff\1\15\2\uffff\5\15\1\32"+
		"\1\33\2\15\1\36\1\15\2\uffff\1\40\1\15\1\uffff\1\15\1\uffff\1\15\1\44"+
		"\1\15\1\uffff\1\46\1\uffff";
	static final String DFA5_eofS =
		"\47\uffff";
	static final String DFA5_minS =
		"\1\11\1\56\2\uffff\3\56\1\uffff\3\56\1\uffff\1\56\2\uffff\13\56\2\uffff"+
		"\2\56\1\uffff\1\56\1\uffff\3\56\1\uffff\1\56\1\uffff";
	static final String DFA5_maxS =
		"\2\172\2\uffff\3\172\1\uffff\3\172\1\uffff\1\172\2\uffff\13\172\2\uffff"+
		"\2\172\1\uffff\1\172\1\uffff\3\172\1\uffff\1\172\1\uffff";
	static final String DFA5_acceptS =
		"\2\uffff\1\2\1\3\3\uffff\1\7\3\uffff\1\14\1\uffff\1\12\1\13\13\uffff\1"+
		"\1\1\4\2\uffff\1\10\1\uffff\1\5\3\uffff\1\11\1\uffff\1\6";
	static final String DFA5_specialS =
		"\47\uffff}>";
	static final String[] DFA5_transitionS = {
			"\2\13\1\uffff\2\13\22\uffff\1\13\13\uffff\1\3\24\uffff\1\1\12\12\1\5"+
			"\10\12\1\11\5\12\1\7\1\uffff\1\2\3\uffff\10\12\1\4\4\12\1\6\4\12\1\10"+
			"\7\12",
			"\1\16\22\uffff\32\12\6\uffff\15\12\1\14\14\12",
			"",
			"",
			"\1\16\22\uffff\32\12\6\uffff\15\12\1\17\14\12",
			"\1\16\22\uffff\32\12\6\uffff\10\12\1\20\21\12",
			"\1\16\22\uffff\32\12\6\uffff\16\12\1\21\13\12",
			"",
			"\1\16\22\uffff\32\12\6\uffff\23\12\1\22\6\12",
			"\1\16\22\uffff\32\12\6\uffff\15\12\1\23\14\12",
			"\1\16\22\uffff\32\12\6\uffff\32\12",
			"",
			"\1\16\22\uffff\32\12\6\uffff\30\12\1\24\1\12",
			"",
			"",
			"\1\16\22\uffff\32\12\6\uffff\23\12\1\25\6\12",
			"\1\16\22\uffff\32\12\6\uffff\22\12\1\26\7\12",
			"\1\16\22\uffff\32\12\6\uffff\23\12\1\27\6\12",
			"\1\16\22\uffff\32\12\6\uffff\21\12\1\30\10\12",
			"\1\16\22\uffff\32\12\6\uffff\10\12\1\31\21\12",
			"\1\16\22\uffff\32\12\6\uffff\32\12",
			"\1\16\22\uffff\32\12\6\uffff\32\12",
			"\1\16\22\uffff\32\12\6\uffff\23\12\1\34\6\12",
			"\1\16\22\uffff\32\12\6\uffff\7\12\1\35\22\12",
			"\1\16\22\uffff\32\12\6\uffff\32\12",
			"\1\16\22\uffff\32\12\6\uffff\16\12\1\37\13\12",
			"",
			"",
			"\1\16\22\uffff\32\12\6\uffff\32\12",
			"\1\16\22\uffff\32\12\6\uffff\10\12\1\41\21\12",
			"",
			"\1\16\22\uffff\32\12\6\uffff\15\12\1\42\14\12",
			"",
			"\1\16\22\uffff\32\12\6\uffff\15\12\1\43\14\12",
			"\1\16\22\uffff\32\12\6\uffff\32\12",
			"\1\16\22\uffff\32\12\6\uffff\6\12\1\45\23\12",
			"",
			"\1\16\22\uffff\32\12\6\uffff\32\12",
			""
	};

	static final short[] DFA5_eot = DFA.unpackEncodedString(DFA5_eotS);
	static final short[] DFA5_eof = DFA.unpackEncodedString(DFA5_eofS);
	static final char[] DFA5_min = DFA.unpackEncodedStringToUnsignedChars(DFA5_minS);
	static final char[] DFA5_max = DFA.unpackEncodedStringToUnsignedChars(DFA5_maxS);
	static final short[] DFA5_accept = DFA.unpackEncodedString(DFA5_acceptS);
	static final short[] DFA5_special = DFA.unpackEncodedString(DFA5_specialS);
	static final short[][] DFA5_transition;

	static {
		int numStates = DFA5_transitionS.length;
		DFA5_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA5_transition[i] = DFA.unpackEncodedString(DFA5_transitionS[i]);
		}
	}

	protected class DFA5 extends DFA {

		public DFA5(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 5;
			this.eot = DFA5_eot;
			this.eof = DFA5_eof;
			this.min = DFA5_min;
			this.max = DFA5_max;
			this.accept = DFA5_accept;
			this.special = DFA5_special;
			this.transition = DFA5_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( ANY | CLOSE_SQ_BR | COMMA | INT | LIST | Nothing | OPEN_SQ_BR | STRING | UNION | SimpleType | QualifiedType | WHITESPACE );";
		}
	}

}
