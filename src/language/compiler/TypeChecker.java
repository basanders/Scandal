package language.compiler;

import static language.tree.Node.Type.BOOL;
import static language.tree.Node.Type.FLOAT;
import static language.tree.Node.Type.INT;

import language.tree.AssignmentDeclaration;
import language.tree.AssignmentStatement;
import language.tree.BinaryExpression;
import language.tree.Block;
import language.tree.BoolLitExpression;
import language.tree.Declaration;
import language.tree.Expression;
import language.tree.FloatLitExpression;
import language.tree.IdentExpression;
import language.tree.IfStatement;
import language.tree.IntLitExpression;
import language.tree.Node.Type;
import language.tree.NodeVisitor;
import language.tree.Program;
import language.tree.Statement;
import language.tree.UnassignedDeclaration;
import language.tree.WhileStatement;

public class TypeChecker implements NodeVisitor {

	SymbolTable symtab = new SymbolTable();

	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		for (Declaration declaration : program.declarations) declaration.visit(this, null);
		for (Statement statement : program.statements) statement.visit(this, null);
		return null;
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		symtab.enterScope();
		for (Declaration declaration : block.declarations) declaration.visit(this, null);
		for (Statement statement : block.statements) statement.visit(this, null);
		symtab.leaveScope();
		return null;
	}

	@Override
	public Object visitUnassignedDeclaration(UnassignedDeclaration declaration, Object arg) throws Exception {
		Declaration testResult = symtab.topOfStackLookup(declaration.identToken.text);
		if (testResult != null) throw new Exception("Illegal redeclaration");
		symtab.insert(declaration.identToken.text, declaration);
		return null;
	}

	@Override
	public Object visitAssignmentDeclaration(AssignmentDeclaration declaration, Object arg) throws Exception {
		Declaration testResult = symtab.topOfStackLookup(declaration.identToken.text);
		if (testResult != null) throw new Exception("Illegal redeclaration");
		Type expressionType = (Type) declaration.expression.visit(this, null);
		if (expressionType != declaration.type) throw new Exception("Type mismatch");
		symtab.insert(declaration.identToken.text, declaration);
		return null;
	}

	@Override
	public Object visitAssignmentStatement(AssignmentStatement assignmentStatement, Object arg) throws Exception {
		Declaration declaration = symtab.lookup(assignmentStatement.firstToken.text);
		if (declaration == null) throw new Exception("Variable must have been declared in some enclosing scope");
		assignmentStatement.declaration = declaration;
		Type expressionType = (Type) assignmentStatement.expression.visit(this, null);
		if (expressionType != declaration.type) throw new Exception("Type mismatch");
		return null;
	}

	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception {
		Expression expression = ifStatement.expression;
		expression.visit(this, null);
		if (expression.type != BOOL) throw new Exception("Invalid IfStatement");
		ifStatement.block.visit(this, null);
		return null;
	}

	@Override
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg) throws Exception {
		Expression expression = whileStatement.expression;
		expression.visit(this, null);
		if (expression.type != BOOL) throw new Exception("Invalid WhileStatement");
		whileStatement.block.visit(this, null);
		return null;
	}

	@Override
	public Object visitIdentExpression(IdentExpression identExpression, Object arg) throws Exception {
		Token ident = identExpression.firstToken;
		Declaration declaration = symtab.lookup(ident.text);
		if (declaration == null) throw new Exception("Variable must have been declared in some enclosing scope");
		identExpression.declaration = declaration;
		return identExpression.type = declaration.type;
	}

	@Override
	public Object visitIntLitExpression(IntLitExpression intLitExpression, Object arg) throws Exception {
		return intLitExpression.type;
	}

	@Override
	public Object visitFloatLitExpression(FloatLitExpression floatLitExpression, Object arg) throws Exception {
		return floatLitExpression.type;
	}

	@Override
	public Object visitBoolLitExpression(BoolLitExpression boolLitExpression, Object arg) throws Exception {
		return boolLitExpression.type;
	}

	@Override
	public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) throws Exception {
		Expression e0 = binaryExpression.e0;
		Expression e1 = binaryExpression.e1;
		Token op = binaryExpression.operator;
		e0.visit(this, null);
		e1.visit(this, null);		
		if (e0.type != BOOL && e1.type != BOOL) {
			switch(op.kind) {
			case MOD:
			case PLUS:
			case MINUS:
			case TIMES:
			case DIV: {
				if (e0.type == INT && e1.type == INT) binaryExpression.type = INT;
				else binaryExpression.type = FLOAT;
				break;
			}
			default: break;
			}
		}
		if (e0.type != FLOAT && e1.type != FLOAT) {
			switch(op.kind) {
			case AND:
			case OR: {
				binaryExpression.type = BOOL;
			} break;
			default: break;
			}
		}
		//if (e0.type == e1.type || e0.type != BOOL && e1.type != BOOL) {}
		switch(op.kind) {
		case LT:
		case LE:
		case GT:
		case GE:
		case EQUAL:
		case NOTEQUAL: {
			binaryExpression.type = BOOL;
		} break;
		default: break;
		}
		if (binaryExpression.type == null) {
			throw new Exception("Invalid BinaryExpression");
		}
		return binaryExpression.type;
	}

}
