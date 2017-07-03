package language.tree;

public interface NodeVisitor {

	Object visitProgram(Program program, Object argument) throws Exception;

	Object visitBlock(Block block, Object argument) throws Exception;

	Object visitUnassignedDeclaration(UnassignedDeclaration dec, Object arg) throws Exception;

	Object visitAssignmentDeclaration(AssignmentDeclaration dec, Object arg) throws Exception;

	Object visitAssignmentStatement(AssignmentStatement assignStatement, Object argument) throws Exception;

	Object visitIfStatement(IfStatement ifStatement, Object argument) throws Exception;

	Object visitWhileStatement(WhileStatement whileStatement, Object argument) throws Exception;
	
	Object visitPrintStatement(PrintStatement printStatement, Object argument) throws Exception;
	
	Object visitPlotStatement(PlotStatement plotStatement, Object argument) throws Exception;
	
	Object visitPlayStatement(PlayStatement playStatement, Object argument) throws Exception;

	Object visitIdentExpression(IdentExpression identExpression, Object argument) throws Exception;
	
	Object visitInfoExpression(InfoExpression infoExpression, Object argument) throws Exception;

	Object visitIntLitExpression(IntLitExpression intLitExpression, Object argument) throws Exception;

	Object visitFloatLitExpression(FloatLitExpression floatLitExpression, Object argument) throws Exception;

	Object visitBoolLitExpression(BoolLitExpression boolLitExpression, Object argument) throws Exception;
	
	Object visitStringLitExpression(StringLitExpression stringLitExpression, Object argument) throws Exception;
	
	Object visitReadExpression(ReadExpression readExpression, Object argument) throws Exception;
	
	Object visitReverseExpression(ReverseExpression reverseExpression, Object argument) throws Exception;
	
	Object visitSpeedExpression(SpeedExpression speedExpression, Object argument) throws Exception;
	
	Object visitLoopExpression(LoopExpression loopExpression, Object argument) throws Exception;
	
	Object visitDelayExpression(DelayExpression delayExpression, Object argument) throws Exception;
	
	Object visitSpliceExpression(SpliceExpression spliceExpression, Object argument) throws Exception;
	
	Object visitFormatExpression(FormatExpression formatExpression, Object argument) throws Exception;

	Object visitBinaryExpression(BinaryExpression binaryExpression, Object argument) throws Exception;

}
