grammar PyType;

options {
  output=AST;
  ASTLabelType=CommonTree;
}

tokens {
    INT    = 'int' ;
    STRING   = 'str' ;
    ANY    = 'Any' ;
    OPEN_SQ_BR = '[';
    CLOSE_SQ_BR = ']';
    LIST = 'List';
    UNION = 'Union';
    COMMA = ',';
    Nothing = 'nothing';

}

@members {
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

expr
    : INT
    | STRING
    | ANY
    | Nothing
    | list_expr
    | union_expr;

/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+    { $channel = HIDDEN; } ;

