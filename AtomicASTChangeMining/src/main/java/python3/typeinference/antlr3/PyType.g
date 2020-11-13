grammar PyType;

options {
  output=AST;
  ASTLabelType=CommonTree;
}

tokens {
    OPEN_SQ_BR = '[';
    CLOSE_SQ_BR = ']';
    LIST = 'List';
    UNION = 'Union';
    DICT = 'Dict';
    COMMA = ',';
    Nothing = 'nothing';
    HYPHEN = '-' ;
    UNDERSCORE = '_' ;
    DOT = '\.';
}

@members {
    public static void main(String[] args) throws Exception {
        python3.typeinference.antlr.python3.typeinference.antlr.PyTypeLexer lex = new python3.typeinference.antlr.python3.typeinference.antlr.PyTypeLexer(new ANTLRFileStream(args[0]));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        python3.typeinference.antlr.python3.typeinference.antlr.PyTypeParser parser = new python3.typeinference.antlr.python3.typeinference.antlr.PyTypeParser(tokens);

        try {
            parser.expr();
        } catch (RecognitionException e)  {
            e.printStackTrace();
        }
    }
}

/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/
parse
    :  expr
    ;

list_element
    : OPEN_SQ_BR expr CLOSE_SQ_BR -> expr
    ;

union_element
    : OPEN_SQ_BR (expr COMMA)+ CLOSE_SQ_BR -> expr+;

list_expr
    : LIST^ list_element*;

union_expr
    : UNION^ union_element;

dict_expr
    : DICT^ union_element;

DIGIT  : '0'..'9' ;

LETTER : 'a'..'z' |'A'..'Z'|'0'..'9'|'_' ;

NUMBER : DIGIT+ ;

SimpleType : ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'.')*('a'..'z'|'A'..'Z'|'_'|'0'..'9')
           ;


expr : list_expr
    | SimpleType
    | union_expr
    | dict_expr;

/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+    { $channel = HIDDEN; } ;

