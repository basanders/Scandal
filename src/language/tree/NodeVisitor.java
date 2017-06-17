package language.tree;

public interface NodeVisitor {

	Object visitProgram(Program program, Object argument) throws Exception;

	Object visitBlock(Block block, Object argument) throws Exception;

	Object visitUnassignedDeclaration(UnassignedDeclaration dec, Object arg) throws Exception;

	Object visitAssignmentDeclaration(AssignmentDeclaration dec, Object arg) throws Exception;

	Object visitAssignmentStatement(AssignmentStatement assignStatement, Object argument) throws Exception;

	Object visitIfStatement(IfStatement ifStatement, Object argument) throws Exception;

	Object visitWhileStatement(WhileStatement whileStatement, Object argument) throws Exception;

	Object visitIdentExpression(IdentExpression identExpression, Object argument) throws Exception;

	Object visitIntLitExpression(IntLitExpression intLitExpression, Object argument) throws Exception;

	Object visitFloatLitExpression(FloatLitExpression floatLitExpression, Object argument) throws Exception;

	Object visitBoolLitExpression(BoolLitExpression boolLitExpression, Object argument) throws Exception;

	Object visitBinaryExpression(BinaryExpression binaryExpression, Object argument) throws Exception;

}
