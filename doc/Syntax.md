## Concrete syntax

- program := (declaration | statement)\*
- block := LBRACE ( declaration | statement )\* RBRACE
- declaration := unassignedDeclaration | assignmentDeclaration
- unassignedDeclaration := type IDENT
- assignmentDeclaration := type IDENT ASSIGN expression
- type := KW\_INT | KW\_FLOAT | KW\_BOOL | KW\_STRING | KW\_ARRAY | KW\_FORMAT
- statement := assignmentStatement | ifStatement | whileStatement | frameworkStatement
- assignmentStatement := IDENT ASSIGN expression
- ifStatement := KW\_IF expression block
- whileStatement := KW\_WHILE expression block
- frameworkStatement := printStatement | plotStatement | playStatement
- printStatement := KW\_PRINT LPAREN expression RPAREN
- plotStatement := KW\_PLOT LPAREN expression COMMA expression COMMA expression RPAREN
- playStatement := KW\_PLAY LPAREN expression COMMA expression RPAREN
- expression := term (termOperator term)\*
- termOperator := LT | LE | GT | GE | EQUAL | NOTEQUAL
- term := summand (summandOperator summand)\*
- summandOperator := PLUS | MINUS | OR
- summand := factor (factorOperator factor)\*
- factorOperator := TIMES | DIV | MOD | AND
- factor := LPAREN expression RPAREN | identExpression | literalExpression | frameworkExpression
- identExpression := IDENT
- literalExpression := intLitExpression | floatLitExpression | boolLitExpression | stringLitExpression | arrayLitExpression
- intLitExpression := INT\_LIT
- floatLitExpression := FLOAT\_LIT
- boolLitExpression := KW\_TRUE | KW\_FALSE
- stringLitExpression := STRING\_LIT
- arrayLitExpression := LBRACKET ((FLOAT\_LIT | INT\_LIT) (COMMA (FLOAT\_LIT | INT\_LIT))\*)\* RBRACKET
- frameworkExpression := infoExpression | readExpression | formatExpression | gainExpression | lineExpression
- frameworkExpression := reverseExpression | speedExpression | spliceExpression | loopExpression | delayExpression
- frameworkExpression := filterExpression | biquadExpression
- infoExpression := KW\_INFO
- readExpression := KW\_READ LPAREN expression COMMA expression RPAREN
- formatExpression := KW\_MONO | KW\_STEREO
- reverseExpression := KW\_REVERSE LPAREN expression RPAREN
- speedExpression := KW\_SPEED LPAREN expression COMMA expression RPAREN
- loopExpression := KW\_LOOP LPAREN expression COMMA expression COMMA expression COMMA expression RPAREN
- delayExpression := KW\_DELAY LPAREN expression COMMA expression COMMA expression COMMA expression RPAREN
- spliceExpression := KW\_SPLICE LPAREN expression (COMMA expression)\* RPAREN
- gainExpression := KW\_GAIN LPAREN expression COMMA expression RPAREN
- lineExpression := KW\_LINE LPAREN expression COMMA expression RPAREN
- filterExpression := KW\_ALLPASS | KW\_LOWPASS | KW\_HIPASS | KW\_BANDPASS
- filterExpression := KW\_BANDSTOP | KW\_LOWSHELF | KW\_HISHELF | KW\_PEAKING
- biquadExpression := KW\_BIQUAD LPAREN expression COMMA expression COMMA expression COMMA expression RPAREN

## Abstract syntax

- Program := (Declaration | Statement)\*
- Block := (Declaration | Statement)\*
- Declaration := UnassignedDeclaration | AssignmentDeclaration
- UnassignedDeclaration := Type IDENT
- AssignmentDeclaration := Type IDENT Expression
- Type := INT | FLOAT | BOOL | STRING | ARRAY | FORMAT
- Statement := AssignmentStatement | IfStatement | WhileStatement | FrameworkStatement
- AssignmentStatement := IDENT Expression
- IfStatement := Expression Block
- WhileStatement := Expression Block
- FrameworkStatement := PrintStatement | PlotStatement | PlayStatement
- PrintStatement := Expression
- PlotStatement := Expression\_0 Expression\_1 Expression\_2
- PlayStatement := Expression\_0 Expression\_1
- Expression := BinaryExpression | IdentExpression | LiteralExpression | FrameworkExpression
- BinaryExpression := Expression\_0 (termOperator | summandOperator | factorOperator) Expression\_1
- ReadExpression := Expression\_0 Expression\_1
- ReverseExpression := Expression
- SpeedExpression := Expression\_0 Expression\_1
- LoopExpression := Expression\_0 Expression\_1 Expression\_2 Expression\_3
- DelayExpression := Expression\_0 Expression\_1 Expression\_2 Expression\_3
- SpliceExpression := Expression+
- GainExpression := Expression\_0 Expression\_1
- LineExpression := Expression\_0 Expression\_1
- BiquadExpression := Expression\_0 Expression\_1 Expression\_2 Expression\_3

## TypeChecker rules

- UnassignedDeclaration:
	+ Variable may not be declared more than once in the same scope
- AssignmentDeclaration:
	+ Variable may not be declared more than once in the same scope
	+ Type = Expression.type
- AssignmentStatement:
	+ Variable must have been declared in some enclosing scope
	+ Declaration.Type = Expression.type
- IfStatement:
	+ Expression.type = BOOL
- WhileStatement:
	+ Expression.type = BOOL
- PrintStatement:
	+ Expression.type != ARRAY | FORMAT | FILTER
- PlotStatement:
	+ Expression\_0.type = STRING
	+ Expression\_1.type = ARRAY
	+ Expression\_2.type = INT
- PlayStatement:
	+ Expression\_0.type = ARRAY
	+ Expression\_1.type = FORMAT
- BinaryExpression:
	+ (INT | FLOAT) (MOD | PLUS | MINUS | TIMES | DIV) (FLOAT | INT) ==> FLOAT
	+ INT (summandOperator | factorOperator) INT ==> INT
	+ (INT | BOOL) (AND | OR) (BOOL | INT) ==> BOOL
	+ !(ARRAY | STRING) termOperator !(STRING | ARRAY) ==> BOOL
- IdentExpression:
	+ Variable must have been declared in some enclosing scope
	+ Type = Declaration.type
- IntLitExpression:
	+ Type = INT
- FloatLitExpression:
	+ Type = FLOAT
- BoolLitExpression:
	+ Type = BOOL
- StringLitExpression:
	+ Type = STRING
- ArrayLitExpression:
	+ Type = ARRAY
- InfoExpression:
	+ Type = STRING
- FormatExpression:
	+ Type = FORMAT
- FilterExpression:
	+ Type = FILTER
- ReadExpression:
	+ Type = ARRAY
	+ Expression\_0.type = STRING
	+ Expression\_1.type = FORMAT
- ReverseExpression:
	+ Type = ARRAY
	+ Expression.type = ARRAY
- SpeedExpression:
	+ Type = ARRAY
	+ Expression\_0.type = ARRAY
	+ Expression\_1.type = INT | FLOAT
- LoopExpression:
	+ Type = ARRAY
	+ Expression\_0.type = ARRAY
	+ Expression\_1.type = INT
	+ Expression\_2.type = INT
	+ Expression\_3.type = INT
- DelayExpression:
	+ Type = ARRAY
	+ Expression\_0.type = ARRAY
	+ Expression\_1.type = INT | FLOAT
	+ Expression\_2.type = INT | FLOAT | ARRAY
	+ Expression\_3.type = INT | FLOAT | ARRAY
- SpliceExpression:
	+ Type = ARRAY
	+ Expression.type = ARRAY
- GainExpression:
	+ Type = ARRAY
	+ Expression\_0.type = ARRAY
	+ Expression\_1.type = INT | FLOAT | ARRAY
- LineExpression:
	+ Type = ARRAY
	+ Expression\_0.type = INT
	+ Expression\_1.type = ARRAY
- BiquadExpression:
	+ Type = ARRAY
	+ Expression\_0.type = ARRAY
	+ Expression\_1.type = INT | FLOAT | ARRAY
	+ Expression\_2.type = INT | FLOAT | ARRAY
	+ Expression\_3.type = FILTER
