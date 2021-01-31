// $ANTLR 3.5.2 PyType.g 2021-01-20 15:55:10
package python3.typeinference.antlr;
import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings("all")
public class PyTypeParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "CLOSE_SQ_BR", "COMMA", "DICT", 
		"DIGIT", "DOT", "EMPTY_TUPLE", "HYPHEN", "LETTER", "LIST", "NUMBER", "OPEN_SQ_BR", 
		"OPTIONAL", "SET", "SimpleType", "THREE_DOTS", "TUPLE", "TYPE", "UNDERSCORE", 
		"UNION", "WHITESPACE"
	};
	public static final int EOF=-1;
	public static final int CLOSE_SQ_BR=4;
	public static final int COMMA=5;
	public static final int DICT=6;
	public static final int DIGIT=7;
	public static final int DOT=8;
	public static final int EMPTY_TUPLE=9;
	public static final int HYPHEN=10;
	public static final int LETTER=11;
	public static final int LIST=12;
	public static final int NUMBER=13;
	public static final int OPEN_SQ_BR=14;
	public static final int OPTIONAL=15;
	public static final int SET=16;
	public static final int SimpleType=17;
	public static final int THREE_DOTS=18;
	public static final int TUPLE=19;
	public static final int TYPE=20;
	public static final int UNDERSCORE=21;
	public static final int UNION=22;
	public static final int WHITESPACE=23;

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
	// PyType.g:45:1: parse : expr ;
	public final PyTypeParser.parse_return parse() throws RecognitionException {
		PyTypeParser.parse_return retval = new PyTypeParser.parse_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		ParserRuleReturnScope expr1 =null;


		try {
			// PyType.g:46:5: ( expr )
			// PyType.g:46:8: expr
			{
			root_0 = (CommonTree)adaptor.nil();


			pushFollow(FOLLOW_expr_in_parse217);
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


	public static class expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "expr"
	// PyType.g:49:1: expr : ( list_expr | EMPTY_TUPLE | THREE_DOTS | set_expr | SimpleType | union_expr | dict_expr | tuple_expr | optional_expr | type_expr | generic_expr );
	public final PyTypeParser.expr_return expr() throws RecognitionException {
		PyTypeParser.expr_return retval = new PyTypeParser.expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token EMPTY_TUPLE3=null;
		Token THREE_DOTS4=null;
		Token SimpleType6=null;
		ParserRuleReturnScope list_expr2 =null;
		ParserRuleReturnScope set_expr5 =null;
		ParserRuleReturnScope union_expr7 =null;
		ParserRuleReturnScope dict_expr8 =null;
		ParserRuleReturnScope tuple_expr9 =null;
		ParserRuleReturnScope optional_expr10 =null;
		ParserRuleReturnScope type_expr11 =null;
		ParserRuleReturnScope generic_expr12 =null;

		CommonTree EMPTY_TUPLE3_tree=null;
		CommonTree THREE_DOTS4_tree=null;
		CommonTree SimpleType6_tree=null;

		try {
			// PyType.g:49:6: ( list_expr | EMPTY_TUPLE | THREE_DOTS | set_expr | SimpleType | union_expr | dict_expr | tuple_expr | optional_expr | type_expr | generic_expr )
			int alt1=11;
			switch ( input.LA(1) ) {
			case LIST:
				{
				alt1=1;
				}
				break;
			case EMPTY_TUPLE:
				{
				alt1=2;
				}
				break;
			case THREE_DOTS:
				{
				alt1=3;
				}
				break;
			case SET:
				{
				alt1=4;
				}
				break;
			case SimpleType:
				{
				int LA1_5 = input.LA(2);
				if ( (LA1_5==EOF||(LA1_5 >= CLOSE_SQ_BR && LA1_5 <= COMMA)) ) {
					alt1=5;
				}
				else if ( (LA1_5==OPEN_SQ_BR) ) {
					alt1=11;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 1, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case UNION:
				{
				alt1=6;
				}
				break;
			case DICT:
				{
				alt1=7;
				}
				break;
			case TUPLE:
				{
				alt1=8;
				}
				break;
			case OPTIONAL:
				{
				alt1=9;
				}
				break;
			case TYPE:
				{
				alt1=10;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 1, 0, input);
				throw nvae;
			}
			switch (alt1) {
				case 1 :
					// PyType.g:49:8: list_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_list_expr_in_expr230);
					list_expr2=list_expr();
					state._fsp--;

					adaptor.addChild(root_0, list_expr2.getTree());

					}
					break;
				case 2 :
					// PyType.g:50:7: EMPTY_TUPLE
					{
					root_0 = (CommonTree)adaptor.nil();


					EMPTY_TUPLE3=(Token)match(input,EMPTY_TUPLE,FOLLOW_EMPTY_TUPLE_in_expr238); 
					EMPTY_TUPLE3_tree = (CommonTree)adaptor.create(EMPTY_TUPLE3);
					adaptor.addChild(root_0, EMPTY_TUPLE3_tree);

					}
					break;
				case 3 :
					// PyType.g:51:7: THREE_DOTS
					{
					root_0 = (CommonTree)adaptor.nil();


					THREE_DOTS4=(Token)match(input,THREE_DOTS,FOLLOW_THREE_DOTS_in_expr246); 
					THREE_DOTS4_tree = (CommonTree)adaptor.create(THREE_DOTS4);
					adaptor.addChild(root_0, THREE_DOTS4_tree);

					}
					break;
				case 4 :
					// PyType.g:52:7: set_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_set_expr_in_expr254);
					set_expr5=set_expr();
					state._fsp--;

					adaptor.addChild(root_0, set_expr5.getTree());

					}
					break;
				case 5 :
					// PyType.g:53:7: SimpleType
					{
					root_0 = (CommonTree)adaptor.nil();


					SimpleType6=(Token)match(input,SimpleType,FOLLOW_SimpleType_in_expr262); 
					SimpleType6_tree = (CommonTree)adaptor.create(SimpleType6);
					adaptor.addChild(root_0, SimpleType6_tree);

					}
					break;
				case 6 :
					// PyType.g:54:7: union_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_union_expr_in_expr270);
					union_expr7=union_expr();
					state._fsp--;

					adaptor.addChild(root_0, union_expr7.getTree());

					}
					break;
				case 7 :
					// PyType.g:55:7: dict_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_dict_expr_in_expr278);
					dict_expr8=dict_expr();
					state._fsp--;

					adaptor.addChild(root_0, dict_expr8.getTree());

					}
					break;
				case 8 :
					// PyType.g:56:7: tuple_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_tuple_expr_in_expr286);
					tuple_expr9=tuple_expr();
					state._fsp--;

					adaptor.addChild(root_0, tuple_expr9.getTree());

					}
					break;
				case 9 :
					// PyType.g:57:7: optional_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_optional_expr_in_expr294);
					optional_expr10=optional_expr();
					state._fsp--;

					adaptor.addChild(root_0, optional_expr10.getTree());

					}
					break;
				case 10 :
					// PyType.g:58:7: type_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_type_expr_in_expr302);
					type_expr11=type_expr();
					state._fsp--;

					adaptor.addChild(root_0, type_expr11.getTree());

					}
					break;
				case 11 :
					// PyType.g:59:7: generic_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_generic_expr_in_expr310);
					generic_expr12=generic_expr();
					state._fsp--;

					adaptor.addChild(root_0, generic_expr12.getTree());

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


	public static class list_element_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "list_element"
	// PyType.g:61:1: list_element : OPEN_SQ_BR expr CLOSE_SQ_BR -> expr ;
	public final PyTypeParser.list_element_return list_element() throws RecognitionException {
		PyTypeParser.list_element_return retval = new PyTypeParser.list_element_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPEN_SQ_BR13=null;
		Token CLOSE_SQ_BR15=null;
		ParserRuleReturnScope expr14 =null;

		CommonTree OPEN_SQ_BR13_tree=null;
		CommonTree CLOSE_SQ_BR15_tree=null;
		RewriteRuleTokenStream stream_CLOSE_SQ_BR=new RewriteRuleTokenStream(adaptor,"token CLOSE_SQ_BR");
		RewriteRuleTokenStream stream_OPEN_SQ_BR=new RewriteRuleTokenStream(adaptor,"token OPEN_SQ_BR");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// PyType.g:62:5: ( OPEN_SQ_BR expr CLOSE_SQ_BR -> expr )
			// PyType.g:62:7: OPEN_SQ_BR expr CLOSE_SQ_BR
			{
			OPEN_SQ_BR13=(Token)match(input,OPEN_SQ_BR,FOLLOW_OPEN_SQ_BR_in_list_element322);  
			stream_OPEN_SQ_BR.add(OPEN_SQ_BR13);

			pushFollow(FOLLOW_expr_in_list_element324);
			expr14=expr();
			state._fsp--;

			stream_expr.add(expr14.getTree());
			CLOSE_SQ_BR15=(Token)match(input,CLOSE_SQ_BR,FOLLOW_CLOSE_SQ_BR_in_list_element326);  
			stream_CLOSE_SQ_BR.add(CLOSE_SQ_BR15);

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
			// 62:35: -> expr
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
	// PyType.g:65:1: union_element : OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ ;
	public final PyTypeParser.union_element_return union_element() throws RecognitionException {
		PyTypeParser.union_element_return retval = new PyTypeParser.union_element_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPEN_SQ_BR16=null;
		Token COMMA18=null;
		Token CLOSE_SQ_BR20=null;
		ParserRuleReturnScope expr17 =null;
		ParserRuleReturnScope expr19 =null;

		CommonTree OPEN_SQ_BR16_tree=null;
		CommonTree COMMA18_tree=null;
		CommonTree CLOSE_SQ_BR20_tree=null;
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_CLOSE_SQ_BR=new RewriteRuleTokenStream(adaptor,"token CLOSE_SQ_BR");
		RewriteRuleTokenStream stream_OPEN_SQ_BR=new RewriteRuleTokenStream(adaptor,"token OPEN_SQ_BR");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// PyType.g:66:5: ( OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ )
			// PyType.g:66:7: OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR
			{
			OPEN_SQ_BR16=(Token)match(input,OPEN_SQ_BR,FOLLOW_OPEN_SQ_BR_in_union_element347);  
			stream_OPEN_SQ_BR.add(OPEN_SQ_BR16);

			pushFollow(FOLLOW_expr_in_union_element349);
			expr17=expr();
			state._fsp--;

			stream_expr.add(expr17.getTree());
			// PyType.g:66:23: ( COMMA expr )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( (LA2_0==COMMA) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// PyType.g:66:24: COMMA expr
					{
					COMMA18=(Token)match(input,COMMA,FOLLOW_COMMA_in_union_element352);  
					stream_COMMA.add(COMMA18);

					pushFollow(FOLLOW_expr_in_union_element354);
					expr19=expr();
					state._fsp--;

					stream_expr.add(expr19.getTree());
					}
					break;

				default :
					break loop2;
				}
			}

			CLOSE_SQ_BR20=(Token)match(input,CLOSE_SQ_BR,FOLLOW_CLOSE_SQ_BR_in_union_element359);  
			stream_CLOSE_SQ_BR.add(CLOSE_SQ_BR20);

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
			// 66:50: -> ( expr )+
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


	public static class dict_element_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "dict_element"
	// PyType.g:68:1: dict_element : OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ ;
	public final PyTypeParser.dict_element_return dict_element() throws RecognitionException {
		PyTypeParser.dict_element_return retval = new PyTypeParser.dict_element_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPEN_SQ_BR21=null;
		Token COMMA23=null;
		Token CLOSE_SQ_BR25=null;
		ParserRuleReturnScope expr22 =null;
		ParserRuleReturnScope expr24 =null;

		CommonTree OPEN_SQ_BR21_tree=null;
		CommonTree COMMA23_tree=null;
		CommonTree CLOSE_SQ_BR25_tree=null;
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_CLOSE_SQ_BR=new RewriteRuleTokenStream(adaptor,"token CLOSE_SQ_BR");
		RewriteRuleTokenStream stream_OPEN_SQ_BR=new RewriteRuleTokenStream(adaptor,"token OPEN_SQ_BR");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// PyType.g:69:5: ( OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ )
			// PyType.g:69:7: OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR
			{
			OPEN_SQ_BR21=(Token)match(input,OPEN_SQ_BR,FOLLOW_OPEN_SQ_BR_in_dict_element376);  
			stream_OPEN_SQ_BR.add(OPEN_SQ_BR21);

			pushFollow(FOLLOW_expr_in_dict_element378);
			expr22=expr();
			state._fsp--;

			stream_expr.add(expr22.getTree());
			// PyType.g:69:23: ( COMMA expr )*
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( (LA3_0==COMMA) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// PyType.g:69:24: COMMA expr
					{
					COMMA23=(Token)match(input,COMMA,FOLLOW_COMMA_in_dict_element381);  
					stream_COMMA.add(COMMA23);

					pushFollow(FOLLOW_expr_in_dict_element383);
					expr24=expr();
					state._fsp--;

					stream_expr.add(expr24.getTree());
					}
					break;

				default :
					break loop3;
				}
			}

			CLOSE_SQ_BR25=(Token)match(input,CLOSE_SQ_BR,FOLLOW_CLOSE_SQ_BR_in_dict_element388);  
			stream_CLOSE_SQ_BR.add(CLOSE_SQ_BR25);

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
			// 69:50: -> ( expr )+
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
	// $ANTLR end "dict_element"


	public static class tuple_element_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "tuple_element"
	// PyType.g:71:1: tuple_element : OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ ;
	public final PyTypeParser.tuple_element_return tuple_element() throws RecognitionException {
		PyTypeParser.tuple_element_return retval = new PyTypeParser.tuple_element_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPEN_SQ_BR26=null;
		Token COMMA28=null;
		Token CLOSE_SQ_BR30=null;
		ParserRuleReturnScope expr27 =null;
		ParserRuleReturnScope expr29 =null;

		CommonTree OPEN_SQ_BR26_tree=null;
		CommonTree COMMA28_tree=null;
		CommonTree CLOSE_SQ_BR30_tree=null;
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_CLOSE_SQ_BR=new RewriteRuleTokenStream(adaptor,"token CLOSE_SQ_BR");
		RewriteRuleTokenStream stream_OPEN_SQ_BR=new RewriteRuleTokenStream(adaptor,"token OPEN_SQ_BR");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// PyType.g:72:5: ( OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ )
			// PyType.g:72:7: OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR
			{
			OPEN_SQ_BR26=(Token)match(input,OPEN_SQ_BR,FOLLOW_OPEN_SQ_BR_in_tuple_element405);  
			stream_OPEN_SQ_BR.add(OPEN_SQ_BR26);

			pushFollow(FOLLOW_expr_in_tuple_element407);
			expr27=expr();
			state._fsp--;

			stream_expr.add(expr27.getTree());
			// PyType.g:72:23: ( COMMA expr )*
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( (LA4_0==COMMA) ) {
					alt4=1;
				}

				switch (alt4) {
				case 1 :
					// PyType.g:72:24: COMMA expr
					{
					COMMA28=(Token)match(input,COMMA,FOLLOW_COMMA_in_tuple_element410);  
					stream_COMMA.add(COMMA28);

					pushFollow(FOLLOW_expr_in_tuple_element412);
					expr29=expr();
					state._fsp--;

					stream_expr.add(expr29.getTree());
					}
					break;

				default :
					break loop4;
				}
			}

			CLOSE_SQ_BR30=(Token)match(input,CLOSE_SQ_BR,FOLLOW_CLOSE_SQ_BR_in_tuple_element417);  
			stream_CLOSE_SQ_BR.add(CLOSE_SQ_BR30);

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
			// 72:50: -> ( expr )+
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
	// $ANTLR end "tuple_element"


	public static class optional_element_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "optional_element"
	// PyType.g:74:1: optional_element : OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ ;
	public final PyTypeParser.optional_element_return optional_element() throws RecognitionException {
		PyTypeParser.optional_element_return retval = new PyTypeParser.optional_element_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPEN_SQ_BR31=null;
		Token COMMA33=null;
		Token CLOSE_SQ_BR35=null;
		ParserRuleReturnScope expr32 =null;
		ParserRuleReturnScope expr34 =null;

		CommonTree OPEN_SQ_BR31_tree=null;
		CommonTree COMMA33_tree=null;
		CommonTree CLOSE_SQ_BR35_tree=null;
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_CLOSE_SQ_BR=new RewriteRuleTokenStream(adaptor,"token CLOSE_SQ_BR");
		RewriteRuleTokenStream stream_OPEN_SQ_BR=new RewriteRuleTokenStream(adaptor,"token OPEN_SQ_BR");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// PyType.g:75:5: ( OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ )
			// PyType.g:75:7: OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR
			{
			OPEN_SQ_BR31=(Token)match(input,OPEN_SQ_BR,FOLLOW_OPEN_SQ_BR_in_optional_element434);  
			stream_OPEN_SQ_BR.add(OPEN_SQ_BR31);

			pushFollow(FOLLOW_expr_in_optional_element436);
			expr32=expr();
			state._fsp--;

			stream_expr.add(expr32.getTree());
			// PyType.g:75:23: ( COMMA expr )*
			loop5:
			while (true) {
				int alt5=2;
				int LA5_0 = input.LA(1);
				if ( (LA5_0==COMMA) ) {
					alt5=1;
				}

				switch (alt5) {
				case 1 :
					// PyType.g:75:24: COMMA expr
					{
					COMMA33=(Token)match(input,COMMA,FOLLOW_COMMA_in_optional_element439);  
					stream_COMMA.add(COMMA33);

					pushFollow(FOLLOW_expr_in_optional_element441);
					expr34=expr();
					state._fsp--;

					stream_expr.add(expr34.getTree());
					}
					break;

				default :
					break loop5;
				}
			}

			CLOSE_SQ_BR35=(Token)match(input,CLOSE_SQ_BR,FOLLOW_CLOSE_SQ_BR_in_optional_element446);  
			stream_CLOSE_SQ_BR.add(CLOSE_SQ_BR35);

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
			// 75:50: -> ( expr )+
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
	// $ANTLR end "optional_element"


	public static class type_element_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "type_element"
	// PyType.g:77:1: type_element : OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ ;
	public final PyTypeParser.type_element_return type_element() throws RecognitionException {
		PyTypeParser.type_element_return retval = new PyTypeParser.type_element_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPEN_SQ_BR36=null;
		Token COMMA38=null;
		Token CLOSE_SQ_BR40=null;
		ParserRuleReturnScope expr37 =null;
		ParserRuleReturnScope expr39 =null;

		CommonTree OPEN_SQ_BR36_tree=null;
		CommonTree COMMA38_tree=null;
		CommonTree CLOSE_SQ_BR40_tree=null;
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_CLOSE_SQ_BR=new RewriteRuleTokenStream(adaptor,"token CLOSE_SQ_BR");
		RewriteRuleTokenStream stream_OPEN_SQ_BR=new RewriteRuleTokenStream(adaptor,"token OPEN_SQ_BR");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// PyType.g:78:5: ( OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ )
			// PyType.g:78:7: OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR
			{
			OPEN_SQ_BR36=(Token)match(input,OPEN_SQ_BR,FOLLOW_OPEN_SQ_BR_in_type_element463);  
			stream_OPEN_SQ_BR.add(OPEN_SQ_BR36);

			pushFollow(FOLLOW_expr_in_type_element465);
			expr37=expr();
			state._fsp--;

			stream_expr.add(expr37.getTree());
			// PyType.g:78:23: ( COMMA expr )*
			loop6:
			while (true) {
				int alt6=2;
				int LA6_0 = input.LA(1);
				if ( (LA6_0==COMMA) ) {
					alt6=1;
				}

				switch (alt6) {
				case 1 :
					// PyType.g:78:24: COMMA expr
					{
					COMMA38=(Token)match(input,COMMA,FOLLOW_COMMA_in_type_element468);  
					stream_COMMA.add(COMMA38);

					pushFollow(FOLLOW_expr_in_type_element470);
					expr39=expr();
					state._fsp--;

					stream_expr.add(expr39.getTree());
					}
					break;

				default :
					break loop6;
				}
			}

			CLOSE_SQ_BR40=(Token)match(input,CLOSE_SQ_BR,FOLLOW_CLOSE_SQ_BR_in_type_element475);  
			stream_CLOSE_SQ_BR.add(CLOSE_SQ_BR40);

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
			// 78:50: -> ( expr )+
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
	// $ANTLR end "type_element"


	public static class generic_element_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "generic_element"
	// PyType.g:80:1: generic_element : OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ ;
	public final PyTypeParser.generic_element_return generic_element() throws RecognitionException {
		PyTypeParser.generic_element_return retval = new PyTypeParser.generic_element_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPEN_SQ_BR41=null;
		Token COMMA43=null;
		Token CLOSE_SQ_BR45=null;
		ParserRuleReturnScope expr42 =null;
		ParserRuleReturnScope expr44 =null;

		CommonTree OPEN_SQ_BR41_tree=null;
		CommonTree COMMA43_tree=null;
		CommonTree CLOSE_SQ_BR45_tree=null;
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_CLOSE_SQ_BR=new RewriteRuleTokenStream(adaptor,"token CLOSE_SQ_BR");
		RewriteRuleTokenStream stream_OPEN_SQ_BR=new RewriteRuleTokenStream(adaptor,"token OPEN_SQ_BR");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// PyType.g:81:5: ( OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ )
			// PyType.g:81:7: OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR
			{
			OPEN_SQ_BR41=(Token)match(input,OPEN_SQ_BR,FOLLOW_OPEN_SQ_BR_in_generic_element492);  
			stream_OPEN_SQ_BR.add(OPEN_SQ_BR41);

			pushFollow(FOLLOW_expr_in_generic_element494);
			expr42=expr();
			state._fsp--;

			stream_expr.add(expr42.getTree());
			// PyType.g:81:23: ( COMMA expr )*
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( (LA7_0==COMMA) ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// PyType.g:81:24: COMMA expr
					{
					COMMA43=(Token)match(input,COMMA,FOLLOW_COMMA_in_generic_element497);  
					stream_COMMA.add(COMMA43);

					pushFollow(FOLLOW_expr_in_generic_element499);
					expr44=expr();
					state._fsp--;

					stream_expr.add(expr44.getTree());
					}
					break;

				default :
					break loop7;
				}
			}

			CLOSE_SQ_BR45=(Token)match(input,CLOSE_SQ_BR,FOLLOW_CLOSE_SQ_BR_in_generic_element504);  
			stream_CLOSE_SQ_BR.add(CLOSE_SQ_BR45);

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
			// 81:50: -> ( expr )+
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
	// $ANTLR end "generic_element"


	public static class list_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "list_expr"
	// PyType.g:83:1: list_expr : LIST ^ ( list_element )* ;
	public final PyTypeParser.list_expr_return list_expr() throws RecognitionException {
		PyTypeParser.list_expr_return retval = new PyTypeParser.list_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token LIST46=null;
		ParserRuleReturnScope list_element47 =null;

		CommonTree LIST46_tree=null;

		try {
			// PyType.g:84:5: ( LIST ^ ( list_element )* )
			// PyType.g:84:7: LIST ^ ( list_element )*
			{
			root_0 = (CommonTree)adaptor.nil();


			LIST46=(Token)match(input,LIST,FOLLOW_LIST_in_list_expr521); 
			LIST46_tree = (CommonTree)adaptor.create(LIST46);
			root_0 = (CommonTree)adaptor.becomeRoot(LIST46_tree, root_0);

			// PyType.g:84:13: ( list_element )*
			loop8:
			while (true) {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( (LA8_0==OPEN_SQ_BR) ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					// PyType.g:84:13: list_element
					{
					pushFollow(FOLLOW_list_element_in_list_expr524);
					list_element47=list_element();
					state._fsp--;

					adaptor.addChild(root_0, list_element47.getTree());

					}
					break;

				default :
					break loop8;
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


	public static class set_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "set_expr"
	// PyType.g:86:1: set_expr : SET ^ ( list_element )* ;
	public final PyTypeParser.set_expr_return set_expr() throws RecognitionException {
		PyTypeParser.set_expr_return retval = new PyTypeParser.set_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token SET48=null;
		ParserRuleReturnScope list_element49 =null;

		CommonTree SET48_tree=null;

		try {
			// PyType.g:87:5: ( SET ^ ( list_element )* )
			// PyType.g:87:7: SET ^ ( list_element )*
			{
			root_0 = (CommonTree)adaptor.nil();


			SET48=(Token)match(input,SET,FOLLOW_SET_in_set_expr537); 
			SET48_tree = (CommonTree)adaptor.create(SET48);
			root_0 = (CommonTree)adaptor.becomeRoot(SET48_tree, root_0);

			// PyType.g:87:12: ( list_element )*
			loop9:
			while (true) {
				int alt9=2;
				int LA9_0 = input.LA(1);
				if ( (LA9_0==OPEN_SQ_BR) ) {
					alt9=1;
				}

				switch (alt9) {
				case 1 :
					// PyType.g:87:12: list_element
					{
					pushFollow(FOLLOW_list_element_in_set_expr540);
					list_element49=list_element();
					state._fsp--;

					adaptor.addChild(root_0, list_element49.getTree());

					}
					break;

				default :
					break loop9;
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
	// $ANTLR end "set_expr"


	public static class union_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "union_expr"
	// PyType.g:89:1: union_expr : UNION ^ union_element ;
	public final PyTypeParser.union_expr_return union_expr() throws RecognitionException {
		PyTypeParser.union_expr_return retval = new PyTypeParser.union_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token UNION50=null;
		ParserRuleReturnScope union_element51 =null;

		CommonTree UNION50_tree=null;

		try {
			// PyType.g:90:5: ( UNION ^ union_element )
			// PyType.g:90:7: UNION ^ union_element
			{
			root_0 = (CommonTree)adaptor.nil();


			UNION50=(Token)match(input,UNION,FOLLOW_UNION_in_union_expr553); 
			UNION50_tree = (CommonTree)adaptor.create(UNION50);
			root_0 = (CommonTree)adaptor.becomeRoot(UNION50_tree, root_0);

			pushFollow(FOLLOW_union_element_in_union_expr556);
			union_element51=union_element();
			state._fsp--;

			adaptor.addChild(root_0, union_element51.getTree());

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


	public static class dict_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "dict_expr"
	// PyType.g:92:1: dict_expr : DICT ^ dict_element ;
	public final PyTypeParser.dict_expr_return dict_expr() throws RecognitionException {
		PyTypeParser.dict_expr_return retval = new PyTypeParser.dict_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token DICT52=null;
		ParserRuleReturnScope dict_element53 =null;

		CommonTree DICT52_tree=null;

		try {
			// PyType.g:93:5: ( DICT ^ dict_element )
			// PyType.g:93:7: DICT ^ dict_element
			{
			root_0 = (CommonTree)adaptor.nil();


			DICT52=(Token)match(input,DICT,FOLLOW_DICT_in_dict_expr568); 
			DICT52_tree = (CommonTree)adaptor.create(DICT52);
			root_0 = (CommonTree)adaptor.becomeRoot(DICT52_tree, root_0);

			pushFollow(FOLLOW_dict_element_in_dict_expr571);
			dict_element53=dict_element();
			state._fsp--;

			adaptor.addChild(root_0, dict_element53.getTree());

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
	// $ANTLR end "dict_expr"


	public static class tuple_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "tuple_expr"
	// PyType.g:95:1: tuple_expr : TUPLE ^ tuple_element ;
	public final PyTypeParser.tuple_expr_return tuple_expr() throws RecognitionException {
		PyTypeParser.tuple_expr_return retval = new PyTypeParser.tuple_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token TUPLE54=null;
		ParserRuleReturnScope tuple_element55 =null;

		CommonTree TUPLE54_tree=null;

		try {
			// PyType.g:96:5: ( TUPLE ^ tuple_element )
			// PyType.g:96:7: TUPLE ^ tuple_element
			{
			root_0 = (CommonTree)adaptor.nil();


			TUPLE54=(Token)match(input,TUPLE,FOLLOW_TUPLE_in_tuple_expr583); 
			TUPLE54_tree = (CommonTree)adaptor.create(TUPLE54);
			root_0 = (CommonTree)adaptor.becomeRoot(TUPLE54_tree, root_0);

			pushFollow(FOLLOW_tuple_element_in_tuple_expr586);
			tuple_element55=tuple_element();
			state._fsp--;

			adaptor.addChild(root_0, tuple_element55.getTree());

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
	// $ANTLR end "tuple_expr"


	public static class optional_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "optional_expr"
	// PyType.g:98:1: optional_expr : OPTIONAL ^ optional_element ;
	public final PyTypeParser.optional_expr_return optional_expr() throws RecognitionException {
		PyTypeParser.optional_expr_return retval = new PyTypeParser.optional_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPTIONAL56=null;
		ParserRuleReturnScope optional_element57 =null;

		CommonTree OPTIONAL56_tree=null;

		try {
			// PyType.g:99:5: ( OPTIONAL ^ optional_element )
			// PyType.g:99:7: OPTIONAL ^ optional_element
			{
			root_0 = (CommonTree)adaptor.nil();


			OPTIONAL56=(Token)match(input,OPTIONAL,FOLLOW_OPTIONAL_in_optional_expr600); 
			OPTIONAL56_tree = (CommonTree)adaptor.create(OPTIONAL56);
			root_0 = (CommonTree)adaptor.becomeRoot(OPTIONAL56_tree, root_0);

			pushFollow(FOLLOW_optional_element_in_optional_expr603);
			optional_element57=optional_element();
			state._fsp--;

			adaptor.addChild(root_0, optional_element57.getTree());

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
	// $ANTLR end "optional_expr"


	public static class type_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "type_expr"
	// PyType.g:101:1: type_expr : TYPE ^ type_element ;
	public final PyTypeParser.type_expr_return type_expr() throws RecognitionException {
		PyTypeParser.type_expr_return retval = new PyTypeParser.type_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token TYPE58=null;
		ParserRuleReturnScope type_element59 =null;

		CommonTree TYPE58_tree=null;

		try {
			// PyType.g:102:5: ( TYPE ^ type_element )
			// PyType.g:102:7: TYPE ^ type_element
			{
			root_0 = (CommonTree)adaptor.nil();


			TYPE58=(Token)match(input,TYPE,FOLLOW_TYPE_in_type_expr615); 
			TYPE58_tree = (CommonTree)adaptor.create(TYPE58);
			root_0 = (CommonTree)adaptor.becomeRoot(TYPE58_tree, root_0);

			pushFollow(FOLLOW_type_element_in_type_expr618);
			type_element59=type_element();
			state._fsp--;

			adaptor.addChild(root_0, type_element59.getTree());

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
	// $ANTLR end "type_expr"


	public static class generic_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "generic_expr"
	// PyType.g:104:1: generic_expr : SimpleType ^ generic_element ;
	public final PyTypeParser.generic_expr_return generic_expr() throws RecognitionException {
		PyTypeParser.generic_expr_return retval = new PyTypeParser.generic_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token SimpleType60=null;
		ParserRuleReturnScope generic_element61 =null;

		CommonTree SimpleType60_tree=null;

		try {
			// PyType.g:105:5: ( SimpleType ^ generic_element )
			// PyType.g:105:7: SimpleType ^ generic_element
			{
			root_0 = (CommonTree)adaptor.nil();


			SimpleType60=(Token)match(input,SimpleType,FOLLOW_SimpleType_in_generic_expr630); 
			SimpleType60_tree = (CommonTree)adaptor.create(SimpleType60);
			root_0 = (CommonTree)adaptor.becomeRoot(SimpleType60_tree, root_0);

			pushFollow(FOLLOW_generic_element_in_generic_expr633);
			generic_element61=generic_element();
			state._fsp--;

			adaptor.addChild(root_0, generic_element61.getTree());

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
	// $ANTLR end "generic_expr"

	// Delegated rules



	public static final BitSet FOLLOW_expr_in_parse217 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_list_expr_in_expr230 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_EMPTY_TUPLE_in_expr238 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_THREE_DOTS_in_expr246 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_expr_in_expr254 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SimpleType_in_expr262 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_union_expr_in_expr270 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dict_expr_in_expr278 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_tuple_expr_in_expr286 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_optional_expr_in_expr294 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_expr_in_expr302 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_generic_expr_in_expr310 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPEN_SQ_BR_in_list_element322 = new BitSet(new long[]{0x00000000005F9240L});
	public static final BitSet FOLLOW_expr_in_list_element324 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_CLOSE_SQ_BR_in_list_element326 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPEN_SQ_BR_in_union_element347 = new BitSet(new long[]{0x00000000005F9240L});
	public static final BitSet FOLLOW_expr_in_union_element349 = new BitSet(new long[]{0x0000000000000030L});
	public static final BitSet FOLLOW_COMMA_in_union_element352 = new BitSet(new long[]{0x00000000005F9240L});
	public static final BitSet FOLLOW_expr_in_union_element354 = new BitSet(new long[]{0x0000000000000030L});
	public static final BitSet FOLLOW_CLOSE_SQ_BR_in_union_element359 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPEN_SQ_BR_in_dict_element376 = new BitSet(new long[]{0x00000000005F9240L});
	public static final BitSet FOLLOW_expr_in_dict_element378 = new BitSet(new long[]{0x0000000000000030L});
	public static final BitSet FOLLOW_COMMA_in_dict_element381 = new BitSet(new long[]{0x00000000005F9240L});
	public static final BitSet FOLLOW_expr_in_dict_element383 = new BitSet(new long[]{0x0000000000000030L});
	public static final BitSet FOLLOW_CLOSE_SQ_BR_in_dict_element388 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPEN_SQ_BR_in_tuple_element405 = new BitSet(new long[]{0x00000000005F9240L});
	public static final BitSet FOLLOW_expr_in_tuple_element407 = new BitSet(new long[]{0x0000000000000030L});
	public static final BitSet FOLLOW_COMMA_in_tuple_element410 = new BitSet(new long[]{0x00000000005F9240L});
	public static final BitSet FOLLOW_expr_in_tuple_element412 = new BitSet(new long[]{0x0000000000000030L});
	public static final BitSet FOLLOW_CLOSE_SQ_BR_in_tuple_element417 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPEN_SQ_BR_in_optional_element434 = new BitSet(new long[]{0x00000000005F9240L});
	public static final BitSet FOLLOW_expr_in_optional_element436 = new BitSet(new long[]{0x0000000000000030L});
	public static final BitSet FOLLOW_COMMA_in_optional_element439 = new BitSet(new long[]{0x00000000005F9240L});
	public static final BitSet FOLLOW_expr_in_optional_element441 = new BitSet(new long[]{0x0000000000000030L});
	public static final BitSet FOLLOW_CLOSE_SQ_BR_in_optional_element446 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPEN_SQ_BR_in_type_element463 = new BitSet(new long[]{0x00000000005F9240L});
	public static final BitSet FOLLOW_expr_in_type_element465 = new BitSet(new long[]{0x0000000000000030L});
	public static final BitSet FOLLOW_COMMA_in_type_element468 = new BitSet(new long[]{0x00000000005F9240L});
	public static final BitSet FOLLOW_expr_in_type_element470 = new BitSet(new long[]{0x0000000000000030L});
	public static final BitSet FOLLOW_CLOSE_SQ_BR_in_type_element475 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPEN_SQ_BR_in_generic_element492 = new BitSet(new long[]{0x00000000005F9240L});
	public static final BitSet FOLLOW_expr_in_generic_element494 = new BitSet(new long[]{0x0000000000000030L});
	public static final BitSet FOLLOW_COMMA_in_generic_element497 = new BitSet(new long[]{0x00000000005F9240L});
	public static final BitSet FOLLOW_expr_in_generic_element499 = new BitSet(new long[]{0x0000000000000030L});
	public static final BitSet FOLLOW_CLOSE_SQ_BR_in_generic_element504 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LIST_in_list_expr521 = new BitSet(new long[]{0x0000000000004002L});
	public static final BitSet FOLLOW_list_element_in_list_expr524 = new BitSet(new long[]{0x0000000000004002L});
	public static final BitSet FOLLOW_SET_in_set_expr537 = new BitSet(new long[]{0x0000000000004002L});
	public static final BitSet FOLLOW_list_element_in_set_expr540 = new BitSet(new long[]{0x0000000000004002L});
	public static final BitSet FOLLOW_UNION_in_union_expr553 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_union_element_in_union_expr556 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DICT_in_dict_expr568 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_dict_element_in_dict_expr571 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TUPLE_in_tuple_expr583 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_tuple_element_in_tuple_expr586 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPTIONAL_in_optional_expr600 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_optional_element_in_optional_expr603 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TYPE_in_type_expr615 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_type_element_in_type_expr618 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SimpleType_in_generic_expr630 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_generic_element_in_generic_expr633 = new BitSet(new long[]{0x0000000000000002L});
}
