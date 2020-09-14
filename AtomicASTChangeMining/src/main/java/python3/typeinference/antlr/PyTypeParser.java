package python3.typeinference.antlr;// $ANTLR 3.5.2 PyType.g 2020-09-13 15:37:52

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings("all")
public class PyTypeParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "CLOSE_SQ_BR", "COMMA", "DIGIT", 
		"DOT", "HYPHEN", "LETTER", "LIST", "NUMBER", "Nothing", "OPEN_SQ_BR", 
		"SimpleType", "UNDERSCORE", "UNION", "WHITESPACE"
	};
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
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public PyTypeParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public PyTypeParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	protected TreeAdaptor adaptor = new CommonTreeAdaptor();

	public void setTreeAdaptor(TreeAdaptor adaptor) {
		this.adaptor = adaptor;
	}
	public TreeAdaptor getTreeAdaptor() {
		return adaptor;
	}
	@Override public String[] getTokenNames() { return PyTypeParser.tokenNames; }
	@Override public String getGrammarFileName() { return "PyType.g"; }


	    public static void main(String[] args) throws Exception {
	        python3.typeinference.antlr.PyTypeLexer lex = new python3.typeinference.antlr.PyTypeLexer(new ANTLRFileStream(args[0]));
	        CommonTokenStream tokens = new CommonTokenStream(lex);

	        python3.typeinference.antlr.PyTypeParser parser = new python3.typeinference.antlr.PyTypeParser(tokens);

	        try {
	            parser.expr();
	        } catch (RecognitionException e)  {
	            e.printStackTrace();
	        }
	    }


	public static class parse_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "parse"
	// PyType.g:38:1: parse : expr ;
	public final PyTypeParser.parse_return parse() throws RecognitionException {
		PyTypeParser.parse_return retval = new PyTypeParser.parse_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		ParserRuleReturnScope expr1 =null;


		try {
			// PyType.g:39:5: ( expr )
			// PyType.g:39:8: expr
			{
			root_0 = (CommonTree)adaptor.nil();


			pushFollow(FOLLOW_expr_in_parse148);
			expr1=expr();
			state._fsp--;

			adaptor.addChild(root_0, expr1.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "parse"


	public static class list_element_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "list_element"
	// PyType.g:42:1: list_element : OPEN_SQ_BR expr CLOSE_SQ_BR -> expr ;
	public final PyTypeParser.list_element_return list_element() throws RecognitionException {
		PyTypeParser.list_element_return retval = new PyTypeParser.list_element_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPEN_SQ_BR2=null;
		Token CLOSE_SQ_BR4=null;
		ParserRuleReturnScope expr3 =null;

		CommonTree OPEN_SQ_BR2_tree=null;
		CommonTree CLOSE_SQ_BR4_tree=null;
		RewriteRuleTokenStream stream_CLOSE_SQ_BR=new RewriteRuleTokenStream(adaptor,"token CLOSE_SQ_BR");
		RewriteRuleTokenStream stream_OPEN_SQ_BR=new RewriteRuleTokenStream(adaptor,"token OPEN_SQ_BR");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// PyType.g:43:5: ( OPEN_SQ_BR expr CLOSE_SQ_BR -> expr )
			// PyType.g:43:7: OPEN_SQ_BR expr CLOSE_SQ_BR
			{
			OPEN_SQ_BR2=(Token)match(input,OPEN_SQ_BR,FOLLOW_OPEN_SQ_BR_in_list_element165);  
			stream_OPEN_SQ_BR.add(OPEN_SQ_BR2);

			pushFollow(FOLLOW_expr_in_list_element167);
			expr3=expr();
			state._fsp--;

			stream_expr.add(expr3.getTree());
			CLOSE_SQ_BR4=(Token)match(input,CLOSE_SQ_BR,FOLLOW_CLOSE_SQ_BR_in_list_element169);  
			stream_CLOSE_SQ_BR.add(CLOSE_SQ_BR4);

			// AST REWRITE
			// elements: expr
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 43:35: -> expr
			{
				adaptor.addChild(root_0, stream_expr.nextTree());
			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "list_element"


	public static class union_element_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "union_element"
	// PyType.g:46:1: union_element : OPEN_SQ_BR ( expr COMMA )+ CLOSE_SQ_BR -> ( expr )+ ;
	public final PyTypeParser.union_element_return union_element() throws RecognitionException {
		PyTypeParser.union_element_return retval = new PyTypeParser.union_element_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPEN_SQ_BR5=null;
		Token COMMA7=null;
		Token CLOSE_SQ_BR8=null;
		ParserRuleReturnScope expr6 =null;

		CommonTree OPEN_SQ_BR5_tree=null;
		CommonTree COMMA7_tree=null;
		CommonTree CLOSE_SQ_BR8_tree=null;
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_CLOSE_SQ_BR=new RewriteRuleTokenStream(adaptor,"token CLOSE_SQ_BR");
		RewriteRuleTokenStream stream_OPEN_SQ_BR=new RewriteRuleTokenStream(adaptor,"token OPEN_SQ_BR");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// PyType.g:47:5: ( OPEN_SQ_BR ( expr COMMA )+ CLOSE_SQ_BR -> ( expr )+ )
			// PyType.g:47:7: OPEN_SQ_BR ( expr COMMA )+ CLOSE_SQ_BR
			{
			OPEN_SQ_BR5=(Token)match(input,OPEN_SQ_BR,FOLLOW_OPEN_SQ_BR_in_union_element190);  
			stream_OPEN_SQ_BR.add(OPEN_SQ_BR5);

			// PyType.g:47:18: ( expr COMMA )+
			int cnt1=0;
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==LIST||LA1_0==SimpleType||LA1_0==UNION) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// PyType.g:47:19: expr COMMA
					{
					pushFollow(FOLLOW_expr_in_union_element193);
					expr6=expr();
					state._fsp--;

					stream_expr.add(expr6.getTree());
					COMMA7=(Token)match(input,COMMA,FOLLOW_COMMA_in_union_element195);  
					stream_COMMA.add(COMMA7);

					}
					break;

				default :
					if ( cnt1 >= 1 ) break loop1;
					EarlyExitException eee = new EarlyExitException(1, input);
					throw eee;
				}
				cnt1++;
			}

			CLOSE_SQ_BR8=(Token)match(input,CLOSE_SQ_BR,FOLLOW_CLOSE_SQ_BR_in_union_element199);  
			stream_CLOSE_SQ_BR.add(CLOSE_SQ_BR8);

			// AST REWRITE
			// elements: expr
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 47:44: -> ( expr )+
			{
				if ( !(stream_expr.hasNext()) ) {
					throw new RewriteEarlyExitException();
				}
				while ( stream_expr.hasNext() ) {
					adaptor.addChild(root_0, stream_expr.nextTree());
				}
				stream_expr.reset();

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "union_element"


	public static class list_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "list_expr"
	// PyType.g:49:1: list_expr : LIST ^ ( list_element )* ;
	public final PyTypeParser.list_expr_return list_expr() throws RecognitionException {
		PyTypeParser.list_expr_return retval = new PyTypeParser.list_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token LIST9=null;
		ParserRuleReturnScope list_element10 =null;

		CommonTree LIST9_tree=null;

		try {
			// PyType.g:50:5: ( LIST ^ ( list_element )* )
			// PyType.g:50:7: LIST ^ ( list_element )*
			{
			root_0 = (CommonTree)adaptor.nil();


			LIST9=(Token)match(input,LIST,FOLLOW_LIST_in_list_expr216); 
			LIST9_tree = (CommonTree)adaptor.create(LIST9);
			root_0 = (CommonTree)adaptor.becomeRoot(LIST9_tree, root_0);

			// PyType.g:50:13: ( list_element )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( (LA2_0==OPEN_SQ_BR) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// PyType.g:50:13: list_element
					{
					pushFollow(FOLLOW_list_element_in_list_expr219);
					list_element10=list_element();
					state._fsp--;

					adaptor.addChild(root_0, list_element10.getTree());

					}
					break;

				default :
					break loop2;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "list_expr"


	public static class union_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "union_expr"
	// PyType.g:52:1: union_expr : UNION ^ union_element ;
	public final PyTypeParser.union_expr_return union_expr() throws RecognitionException {
		PyTypeParser.union_expr_return retval = new PyTypeParser.union_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token UNION11=null;
		ParserRuleReturnScope union_element12 =null;

		CommonTree UNION11_tree=null;

		try {
			// PyType.g:53:5: ( UNION ^ union_element )
			// PyType.g:53:7: UNION ^ union_element
			{
			root_0 = (CommonTree)adaptor.nil();


			UNION11=(Token)match(input,UNION,FOLLOW_UNION_in_union_expr232); 
			UNION11_tree = (CommonTree)adaptor.create(UNION11);
			root_0 = (CommonTree)adaptor.becomeRoot(UNION11_tree, root_0);

			pushFollow(FOLLOW_union_element_in_union_expr235);
			union_element12=union_element();
			state._fsp--;

			adaptor.addChild(root_0, union_element12.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "union_expr"


	public static class expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "expr"
	// PyType.g:65:1: expr : ( list_expr | SimpleType | union_expr );
	public final PyTypeParser.expr_return expr() throws RecognitionException {
		PyTypeParser.expr_return retval = new PyTypeParser.expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token SimpleType14=null;
		ParserRuleReturnScope list_expr13 =null;
		ParserRuleReturnScope union_expr15 =null;

		CommonTree SimpleType14_tree=null;

		try {
			// PyType.g:65:6: ( list_expr | SimpleType | union_expr )
			int alt3=3;
			switch ( input.LA(1) ) {
			case LIST:
				{
				alt3=1;
				}
				break;
			case SimpleType:
				{
				alt3=2;
				}
				break;
			case UNION:
				{
				alt3=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 3, 0, input);
				throw nvae;
			}
			switch (alt3) {
				case 1 :
					// PyType.g:65:8: list_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_list_expr_in_expr351);
					list_expr13=list_expr();
					state._fsp--;

					adaptor.addChild(root_0, list_expr13.getTree());

					}
					break;
				case 2 :
					// PyType.g:66:7: SimpleType
					{
					root_0 = (CommonTree)adaptor.nil();


					SimpleType14=(Token)match(input,SimpleType,FOLLOW_SimpleType_in_expr359); 
					SimpleType14_tree = (CommonTree)adaptor.create(SimpleType14);
					adaptor.addChild(root_0, SimpleType14_tree);

					}
					break;
				case 3 :
					// PyType.g:67:7: union_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_union_expr_in_expr367);
					union_expr15=union_expr();
					state._fsp--;

					adaptor.addChild(root_0, union_expr15.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "expr"

	// Delegated rules



	public static final BitSet FOLLOW_expr_in_parse148 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPEN_SQ_BR_in_list_element165 = new BitSet(new long[]{0x0000000000014400L});
	public static final BitSet FOLLOW_expr_in_list_element167 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_CLOSE_SQ_BR_in_list_element169 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPEN_SQ_BR_in_union_element190 = new BitSet(new long[]{0x0000000000014400L});
	public static final BitSet FOLLOW_expr_in_union_element193 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_COMMA_in_union_element195 = new BitSet(new long[]{0x0000000000014410L});
	public static final BitSet FOLLOW_CLOSE_SQ_BR_in_union_element199 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LIST_in_list_expr216 = new BitSet(new long[]{0x0000000000002002L});
	public static final BitSet FOLLOW_list_element_in_list_expr219 = new BitSet(new long[]{0x0000000000002002L});
	public static final BitSet FOLLOW_UNION_in_union_expr232 = new BitSet(new long[]{0x0000000000002000L});
	public static final BitSet FOLLOW_union_element_in_union_expr235 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_list_expr_in_expr351 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SimpleType_in_expr359 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_union_expr_in_expr367 = new BitSet(new long[]{0x0000000000000002L});
}
