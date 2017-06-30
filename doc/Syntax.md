### Concrete syntax

- program := ( declaration | statement )*
- block := LBRACE ( declaration | statement )* RBRACE
- type := KW\_INT | KW\_FLOAT | KW\_BOOL | KW\_STRING | KW\_ARRAY
- unassignedDeclaration := type IDENT
- assignmentDeclaration := type IDENT ASSIGN expression
- statement := assignmentStatement | ifStatement | whileStatement | printStatement
- assignmentStatement := IDENT ASSIGN expression
- ifStatement := KW\_IF LPAREN expression RPAREN block
- whileStatement := KW\_WHILE LPAREN expression RPAREN block
- printStatement := KW\_PRINT LPAREN expression RPAREN
- expression := term ( termOperator term )* | LPAREN expression RPAREN | waveFileExpression
- term := summand ( summandOperator summand )*
- summand := factor ( factorOperator factor )*
- termOperator := LT | LE | GT | GE | EQUAL | NOTEQUAL
- summandOperator := PLUS | MINUS | OR
- factorOperator := TIMES | DIV | MOD | AND
- waveFileExpression := KW\_WAVE LPAREN expression RPAREN

### Abstract syntax

- Program := ( Declaration | Statement )*
- Block := ( Declaration | Statement )*
- Type := INT | FLOAT | BOOL | STRING | ARRAY
- Declaration := UnassignedDeclaration | AssignmentDeclaration
- UnassignedDeclaration := Type IDENT
- AssignmentDeclaration := Type IDENT Expression
- Statement := AssignmentStatement | IfStatement | WhileStatement | PrintStatement
- AssignmentStatement := IDENT Expression
- IfStatement := Expression Block
- WhileStatement := Expression Block
- PrintStatement := Expression
- Expression := IdentExpression | InfoExpression | WaveFileExpression | LiteralExpression | BinaryExpression
- LiteralExpression: IntLitExpression | FloatLitExpression | BoolLitExpression | StringLitExpression
- IdentExpression := IDENT
- InfoExpression := KW\_INFO
- WaveFileExpression := KW\_WAVE Expression
- IntLitExpression := INT\_LIT
- FloatLitExpression := FLOAT\_LIT
- BoolLitExpression := BOOL\_LIT
- StringLitExpression := STRING\_LIT
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
- PrintStatement:
	+ Expression.type != ARRAY
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
- StringLitExpression:
	+ StringLitExpression.type = STRING
- WaveFileExpression:
	+ WaveFileExpression.type = ARRAY
	+ WaveFileExpression.expression.type = STRING
- BinaryExpression:
	+ (INT | FLOAT) (MOD | PLUS | MINUS | TIMES | DIV) (INT | FLOAT) ==> FLOAT
	+ INT (summandOperator | factorOperator) INT ==> INT
	+ Type termOperator Type ==> BOOL
