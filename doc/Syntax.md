### Concrete syntax

- program := ( declaration | statement )*
- block := LBRACE ( declaration | statement )* RBRACE
- type := KW\_INT | KW\_FLOAT | KW\_BOOL
- unassignedDeclaration := type IDENT
- assignmentDeclaration := type IDENT ASSIGN expression
- statement := assignmentStatement | ifStatement | whileStatement
- assignmentStatement := IDENT ASSIGN expression
- ifStatement := KW\_IF LPAREN expression RPAREN block
- whileStatement := KW\_WHILE LPAREN expression RPAREN block
- printStatement := KW\_PRINT LPAREN expression RPAREN
- expression := term ( termOperator term )*
- term := summand ( summandOperator summand )*
- summand := factor ( factorOperator factor )*
- termOperator := LT | LE | GT | GE | EQUAL | NOTEQUAL
- summandOperator := PLUS | MINUS | OR
- factorOperator := TIMES | DIV | MOD | AND

### Abstract syntax

- Program := ( Declaration | Statement )*
- Block := ( Declaration | Statement )*
- Type := INT | FLOAT | BOOL | STRING
- Declaration := UnassignedDeclaration | AssignmentDeclaration
- UnassignedDeclaration := Type IDENT
- AssignmentDeclaration := Type IDENT Expression
- Statement := AssignmentStatement | IfStatement | WhileStatement | PrintStatement
- AssignmentStatement := IDENT Expression
- IfStatement := Expression Block
- WhileStatement := Expression Block
- PrintStatement := Expression Block
- Expression := IdentExpression | InfoExpression | LiteralExpression | BinaryExpression
- LiteralExpression: IntLitExpression | FloatLitExpression | BoolLitExpression
- IdentExpression := IDENT
- InfoExpression := KW\_INFO
- IntLitExpression := INT\_LIT
- FloatLitExpression := FLOAT\_LIT
- BoolLitExpression := BOOL\_LIT
- BinaryExpression := Expression operator Expression
- operator := termOperator | summandOperator | factorOperator

### TypeChecker rules

- UnassignedDeclaration:
	+ Variable may not be declared more than once in the same scope
- AssignmentDeclaration:
	+ Variable may not be declared more than once in the same scope
	+ Declaration.type == Expression.type
- AssignmentStatement:
	+ Variable must have been declared in some enclosing scope
	+ Declaration.type == Expression.type
- IfStatement:
	+ Expression.type == BOOL
- WhileStatement:
	+ Expression.type == BOOL
- IdentExpression:
	+ Variable must have been declared in some enclosing scope
	+ IdentExpression.type = Declaration.type
- InfoExpression:
	+ InfoExpression.type = STRING
- IntLitExpression:
	+ IntLitExpression.type = INT
- FloatLitExpression:
	+ FloatLitExpression.type = FLOAT
- BoolLitExpression:
	+ BoolLitExpression.type = BOOL
- BinaryExpression:
	+ (INT | FLOAT) (MOD | PLUS | MINUS | TIMES | DIV) (INT | FLOAT) ==> FLOAT
	+ INT (summandOperator | factorOperator) INT ==> INT
	+ Type termOperator Type ==> BOOL
