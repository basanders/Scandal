package language.tree;

import language.tree.function.BiquadExpression;
import language.tree.function.DelayExpression;
import language.tree.function.FilterExpression;
import language.tree.function.FormatExpression;
import language.tree.function.GainExpression;
import language.tree.function.InfoExpression;
import language.tree.function.LineExpression;
import language.tree.function.LoopExpression;
import language.tree.function.PlayStatement;
import language.tree.function.PlotStatement;
import language.tree.function.PrintStatement;
import language.tree.function.ReadExpression;
import language.tree.function.ReverseExpression;
import language.tree.function.SpeedExpression;
import language.tree.function.SpliceExpression;

public interface NodeVisitor {

	Object visitProgram(Program program, Object arg) throws Exception;

	Object visitBlock(Block block, Object arg) throws Exception;

	Object visitUnassignedDeclaration(UnassignedDeclaration dec, Object arg) throws Exception;

	Object visitAssignmentDeclaration(AssignmentDeclaration dec, Object arg) throws Exception;

	Object visitAssignmentStatement(AssignmentStatement assignStatement, Object arg) throws Exception;

	Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception;

	Object visitWhileStatement(WhileStatement whileStatement, Object arg) throws Exception;

	Object visitPrintStatement(PrintStatement printStatement, Object arg) throws Exception;

	Object visitPlotStatement(PlotStatement plotStatement, Object arg) throws Exception;

	Object visitPlayStatement(PlayStatement playStatement, Object arg) throws Exception;

	Object visitIdentExpression(IdentExpression identExpression, Object arg) throws Exception;

	Object visitInfoExpression(InfoExpression infoExpression, Object arg) throws Exception;

	Object visitIntLitExpression(IntLitExpression intLitExpression, Object arg) throws Exception;

	Object visitFloatLitExpression(FloatLitExpression floatLitExpression, Object arg) throws Exception;

	Object visitBoolLitExpression(BoolLitExpression boolLitExpression, Object arg) throws Exception;

	Object visitStringLitExpression(StringLitExpression stringLitExpression, Object arg) throws Exception;

	Object visitArrayLitExpression(ArrayLitExpression arrayLitExpression, Object arg) throws Exception;

	Object visitReadExpression(ReadExpression readExpression, Object arg) throws Exception;

	Object visitReverseExpression(ReverseExpression reverseExpression, Object arg) throws Exception;

	Object visitSpeedExpression(SpeedExpression speedExpression, Object arg) throws Exception;

	Object visitLoopExpression(LoopExpression loopExpression, Object arg) throws Exception;

	Object visitDelayExpression(DelayExpression delayExpression, Object arg) throws Exception;

	Object visitSpliceExpression(SpliceExpression spliceExpression, Object arg) throws Exception;

	Object visitGainExpression(GainExpression gainExpression, Object arg) throws Exception;
	
	Object visitLineExpression(LineExpression lineExpression, Object arg) throws Exception;

	Object visitFormatExpression(FormatExpression formatExpression, Object arg) throws Exception;
	
	Object visitFilterExpression(FilterExpression filterExpression, Object arg) throws Exception;
	
	Object visitBiquadExpression(BiquadExpression biquadExpression, Object arg) throws Exception;

	Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) throws Exception;

}
