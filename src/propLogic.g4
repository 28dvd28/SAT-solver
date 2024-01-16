grammar propLogic;

main : expression EOF;

expression
    : LPAREN expression RPAREN               # Parentheses
    | NOT expression                        # Not
    | expression AND expression             # And
    | expression OR expression              # Or
    | expression IMPLIES expression         # Implies
    | expression IFF expression             # Iff
    | VAR                                   # Variable
    ;

// Ignore white spaces and new line
WS  : [ \t\r\n]+ -> skip ;

LPAREN  : '(' ;
RPAREN  : ')' ;
AND     : 'AND' ;
OR      : 'OR' ;
NOT     : 'NOT' ;
IMPLIES : '->' ;
IFF     : '<->';

VAR : LETTER ALPHANUMERIC* ;
fragment LETTER : [a-zA-Z] ;
fragment ALPHANUMERIC : [a-zA-Z0-9] ;
