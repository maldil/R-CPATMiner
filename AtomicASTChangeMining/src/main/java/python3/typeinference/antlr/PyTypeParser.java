// $ANTLR 3.5.2 PyType.g 2020-09-01 20:40:04
package python3.typeinference.antlr;
import org.antlr.runtime.*;

import org.antlr.runtime.tree.*;


@SuppressWarnings("all")
public class PyTypeParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "ANY", "CLOSE_SQ_BR", "INT", "LIST", 
		"OPEN_SQ_BR", "STRING", "WHITESPACE"
	};
	public static final int EOF=-1;
	public static final int ANY=4;
	public static final int CLOSE_SQ_BR=5;
	public static final int INT=6;
	public static final int LIST=7;
	public static final int OPEN_SQ_BR=8;
	public static final int STRING=9;
	public static final int WHITESPACE=10;

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
	        PyTypeLexer lex = new PyTypeLexer(new ANTLRFileStream(args[0]));
	        CommonTokenStream tokens = new CommonTokenStream(lex);

	        PyTypeParser parser = new PyTypeParser(tokens);

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
	// PyType.g:35:1: parse : expr ;
	public final parse_return parse() throws RecognitionException {
		parse_return retval = new parse_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		ParserRuleReturnScope expr1 =null;


		try {
			// PyType.g:36:5: ( expr )
			// PyType.g:36:8: expr
			{
			root_0 = (CommonTree)adaptor.nil();


			pushFollow(FOLLOW_expr_in_parse124);
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
	// PyType.g:39:1: list_element : OPEN_SQ_BR expr CLOSE_SQ_BR -> expr ;
	public final list_element_return list_element() throws RecognitionException {
		list_element_return retval = new list_element_return();
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
			// PyType.g:40:5: ( OPEN_SQ_BR expr CLOSE_SQ_BR -> expr )
			// PyType.g:40:7: OPEN_SQ_BR expr CLOSE_SQ_BR
			{
			OPEN_SQ_BR2=(Token)match(input,OPEN_SQ_BR,FOLLOW_OPEN_SQ_BR_in_list_element141);  
			stream_OPEN_SQ_BR.add(OPEN_SQ_BR2);

			pushFollow(FOLLOW_expr_in_list_element143);
			expr3=expr();
			state._fsp--;

			stream_expr.add(expr3.getTree());
			CLOSE_SQ_BR4=(Token)match(input,CLOSE_SQ_BR,FOLLOW_CLOSE_SQ_BR_in_list_element145);  
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
			// 40:35: -> expr
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


	public static class list_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "list_expr"
	// PyType.g:45:1: list_expr : LIST ^ ( list_element )* ;
	public final list_expr_return list_expr() throws RecognitionException {
		list_expr_return retval = new list_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token LIST5=null;
		ParserRuleReturnScope list_element6 =null;

		CommonTree LIST5_tree=null;

		try {
			// PyType.g:46:5: ( LIST ^ ( list_element )* )
			// PyType.g:46:7: LIST ^ ( list_element )*
			{
			root_0 = (CommonTree)adaptor.nil();


			LIST5=(Token)match(input,LIST,FOLLOW_LIST_in_list_expr168); 
			LIST5_tree = (CommonTree)adaptor.create(LIST5);
			root_0 = (CommonTree)adaptor.becomeRoot(LIST5_tree, root_0);

			// PyType.g:46:13: ( list_element )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==OPEN_SQ_BR) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// PyType.g:46:13: list_element
					{
					pushFollow(FOLLOW_list_element_in_list_expr171);
					list_element6=list_element();
					state._fsp--;

					adaptor.addChild(root_0, list_element6.getTree());

					}
					break;

				default :
					break loop1;
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


	public static class expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "expr"
	// PyType.g:52:1: expr : ( INT | STRING | ANY | list_expr );
	public final expr_return expr() throws RecognitionException {
		expr_return retval = new expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token INT7=null;
		Token STRING8=null;
		Token ANY9=null;
		ParserRuleReturnScope list_expr10 =null;

		CommonTree INT7_tree=null;
		CommonTree STRING8_tree=null;
		CommonTree ANY9_tree=null;

		try {
			// PyType.g:53:5: ( INT | STRING | ANY | list_expr )
			int alt2=4;
			switch ( input.LA(1) ) {
			case INT:
				{
				alt2=1;
				}
				break;
			case STRING:
				{
				alt2=2;
				}
				break;
			case ANY:
				{
				alt2=3;
				}
				break;
			case LIST:
				{
				alt2=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 2, 0, input);
				throw nvae;
			}
			switch (alt2) {
				case 1 :
					// PyType.g:53:7: INT
					{
					root_0 = (CommonTree)adaptor.nil();


					INT7=(Token)match(input,INT,FOLLOW_INT_in_expr188); 
					INT7_tree = (CommonTree)adaptor.create(INT7);
					adaptor.addChild(root_0, INT7_tree);

					}
					break;
				case 2 :
					// PyType.g:54:7: STRING
					{
					root_0 = (CommonTree)adaptor.nil();


					STRING8=(Token)match(input,STRING,FOLLOW_STRING_in_expr196); 
					STRING8_tree = (CommonTree)adaptor.create(STRING8);
					adaptor.addChild(root_0, STRING8_tree);

					}
					break;
				case 3 :
					// PyType.g:55:7: ANY
					{
					root_0 = (CommonTree)adaptor.nil();


					ANY9=(Token)match(input,ANY,FOLLOW_ANY_in_expr204); 
					ANY9_tree = (CommonTree)adaptor.create(ANY9);
					adaptor.addChild(root_0, ANY9_tree);

					}
					break;
				case 4 :
					// PyType.g:56:7: list_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_list_expr_in_expr212);
					list_expr10=list_expr();
					state._fsp--;

					adaptor.addChild(root_0, list_expr10.getTree());

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



	public static final BitSet FOLLOW_expr_in_parse124 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPEN_SQ_BR_in_list_element141 = new BitSet(new long[]{0x00000000000002D0L});
	public static final BitSet FOLLOW_expr_in_list_element143 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_CLOSE_SQ_BR_in_list_element145 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LIST_in_list_expr168 = new BitSet(new long[]{0x0000000000000102L});
	public static final BitSet FOLLOW_list_element_in_list_expr171 = new BitSet(new long[]{0x0000000000000102L});
	public static final BitSet FOLLOW_INT_in_expr188 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_in_expr196 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ANY_in_expr204 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_list_expr_in_expr212 = new BitSet(new long[]{0x0000000000000002L});
}
