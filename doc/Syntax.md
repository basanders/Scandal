### Concrete syntax

- program := ( declaration | statement )*
- block := LBRACE ( declaration | statement )* RBRACE
- declaration := unassignedDeclaration | assignmentDeclaration
- unassignedDeclaration := type IDENT
- assignmentDeclaration := type IDENT ASSIGN expression
- type := KW\_INT | KW\_FLOAT | KW\_BOOL | KW\_STRING | KW\_ARRAY
- statement := assignmentStatement | ifStatement | whileStatement | frameworkStatement
- assignmentStatement := IDENT ASSIGN expression
- ifStatement := KW\_IF LPAREN expression RPAREN block
- whileStatement := KW\_WHILE LPAREN expression RPAREN block
- frameworkStatement := printStatement | plotStatement
- printStatement := KW\_PRINT LPAREN expression RPAREN
- plotStatement := KW\_PLOT LPAREN expression COMMA expression COMMA expression RPAREN
- expression := term ( termOperator term )* | LPAREN expression RPAREN | waveFileExpression
- termOperator := LT | LE | GT | GE | EQUAL | NOTEQUAL
- term := summand ( summandOperator summand )*
- summandOperator := PLUS | MINUS | OR
- summand := factor ( factorOperator factor )*
- factorOperator := TIMES | DIV | MOD | AND
- factor := LPAREN expression RPAREN | identExpression | literalExpression | frameworkExpression
- identExpression := IDENT
- literalExpression := intLitExpression | floatLitExpression | boolLitExpression | stringLitExpression
- intLitExpression := INT\_LIT
- floatLitExpression := FLOAT\_LIT
- boolLitExpression := BOOL\_LIT
- stringLitExpression := STRING\_LIT
- frameworkExpression := infoExpression | readExpression
- infoExpression := KW\_INFO
- readExpression := KW\_READ LPAREN expression RPAREN

### Abstract syntax

- Program := ( Declaration | Statement )*
- Block := ( Declaration | Statement )*
- UnassignedDeclaration := Type IDENT
- AssignmentDeclaration := Type IDENT Expression
- Type := INT | FLOAT | BOOL | STRING | ARRAY
- Statement := AssignmentStatement | IfStatement | WhileStatement | FrameworkStatement
- AssignmentStatement := IDENT Expression
- IfStatement := Expression Block
- WhileStatement := Expression Block
- PrintStatement := Expression
- PlotStatement := Expression\_0 Expression\_1 Expression\_2
- Expression := BinaryExpression | IdentExpression | LiteralExpression | FrameworkExpression
- BinaryExpression := Expression\_0 ( termOperator | summandOperator | factorOperator ) Expression\_1
- ReadExpression := Expression

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
- PlotStatement:
	+ Expression\_0.type = STRING
	+ Expression\_1.type = ARRAY
	+ Expression\_2.type = INT
- BinaryExpression:
	+ (INT | FLOAT) (MOD | PLUS | MINUS | TIMES | DIV) (INT | FLOAT) ==> FLOAT
	+ INT (summandOperator | factorOperator) INT ==> INT
	+ Type termOperator Type ==> BOOL
- IdentExpression:
	+ Variable must have been declared in some enclosing scope
	+ IdentExpression.type = Declaration.type
- IntLitExpression:
	+ IntLitExpression.type = INT
- FloatLitExpression:
	+ FloatLitExpression.type = FLOAT
- BoolLitExpression:
	+ BoolLitExpression.type = BOOL
- StringLitExpression:
	+ StringLitExpression.type = STRING
- InfoExpression:
	+ InfoExpression.type = STRING
- ReadExpression:
	+ ReadExpression.type = ARRAY
	+ ReadExpression.expression.type = STRING
