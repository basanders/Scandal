package language.compiler;

import java.util.ArrayList;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import language.tree.*;
import language.tree.Node.Type;
import language.tree.function.*;

public class BytecodeGenerator implements NodeVisitor, Opcodes {

	private int slotCount = 1;

	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		String className = (String) arg;
		String classDesc = "L" + className + ";";
		cw.visit(52, ACC_PUBLIC + ACC_SUPER, className, null, "java/lang/Object", new String[]{"java/lang/Runnable"});
		cw.visitSource(className, null);
		addConstructor(cw, classDesc);
		addMain(cw, className, classDesc);
		addRun(cw, classDesc, program);
		cw.visitEnd();
		return cw.toByteArray();
	}

	private void addConstructor(ClassWriter cw, String classDesc) {
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "([Ljava/lang/String;)V", null, null);
		mv.visitCode();
		Label constructorStart = new Label();
		mv.visitLabel(constructorStart);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
		mv.visitInsn(RETURN);
		Label constructorEnd = new Label();
		mv.visitLabel(constructorEnd);
		mv.visitLocalVariable("this", classDesc, null, constructorStart, constructorEnd, 0);
		mv.visitLocalVariable("args", "[Ljava/lang/String;", null, constructorStart, constructorEnd, 1);
		mv.visitMaxs(0, 0); // use COMPUTE_FRAMES
		mv.visitEnd();
	}

	private void addMain(ClassWriter cw, String className, String classDesc) {
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		mv.visitCode();
		Label mainStart = new Label();
		mv.visitLabel(mainStart);
		mv.visitTypeInsn(NEW, className);
		mv.visitInsn(DUP);	
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, className, "<init>", "([Ljava/lang/String;)V", false);
		mv.visitMethodInsn(INVOKEVIRTUAL, className, "run", "()V", false);
		mv.visitInsn(RETURN);
		Label mainEnd = new Label();
		mv.visitLabel(mainEnd);
		mv.visitLocalVariable("args", "[Ljava/lang/String;", null, mainStart, mainEnd, 0);
		mv.visitLocalVariable("instance", classDesc, null, mainStart, mainEnd, 1);
		mv.visitMaxs(0, 0); // use COMPUTE_FRAMES
		mv.visitEnd();
	}

	private void addRun(ClassWriter cw, String classDesc, Program program) throws Exception {
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "run", "()V", null, null);
		mv.visitCode();
		Label runStart = new Label();
		Label runEnd = new Label();
		mv.visitLabel(runStart);
		ArrayList<Declaration> declarations = program.declarations;
		ArrayList<Statement> statements = program.statements;		
		for (Declaration declaration : declarations) declaration.visit(this, new Object[]{mv, runStart, runEnd});
		for (Statement statement : statements) statement.visit(this, mv);
		mv.visitInsn(RETURN);
		mv.visitLabel(runEnd);
		mv.visitLocalVariable("this", classDesc, null, runStart, runEnd, 0);
		mv.visitMaxs(0, 0); // use COMPUTE_FRAMES
		mv.visitEnd();
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		ArrayList<Declaration> declarations = block.declarations;
		ArrayList<Statement> statements = block.statements;
		Label blockStart = new Label();
		Label blockEnd = new Label();
		mv.visitLabel(blockStart);
		mv.visitLabel(blockEnd);
		for (Declaration declaration : declarations) declaration.visit(this, new Object[]{mv, blockStart, blockEnd});
		for (Statement statement : statements) statement.visit(this, arg);
		return null;
	}

	@Override
	public Object visitUnassignedDeclaration(UnassignedDeclaration declaration, Object arg) throws Exception {
		Object[] args = (Object[]) arg;
		MethodVisitor mv = (MethodVisitor) args[0];
		Label start = (Label) args[1];
		Label end = (Label) args[2];
		String ident = declaration.firstToken.text;
		String type = declaration.jvmType;
		mv.visitLocalVariable(ident, type, null, start, end, slotCount);
		declaration.slotNumber = slotCount++;
		return null;
	}

	@Override
	public Object visitAssignmentDeclaration(AssignmentDeclaration declaration, Object arg) throws Exception {
		Object[] args = (Object[]) arg;
		MethodVisitor mv = (MethodVisitor) args[0];
		Label start = (Label) args[1];
		Label end = (Label) args[2];
		String ident = declaration.firstToken.text;
		String type = declaration.jvmType;
		mv.visitLocalVariable(ident, type, null, start, end, slotCount);
		declaration.slotNumber = slotCount++;
		Expression expr = ((AssignmentDeclaration) declaration).expression;
		expr.visit(this, mv);
		if (expr.type == Type.STRING || expr.type == Type.ARRAY)
			mv.visitVarInsn(ASTORE, declaration.slotNumber);
		else if (expr.type == Type.FLOAT) mv.visitVarInsn(FSTORE, declaration.slotNumber);
		else mv.visitVarInsn(ISTORE, declaration.slotNumber);
		return null;
	}

	@Override
	public Object visitAssignmentStatement(AssignmentStatement assignmentStatement, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		Expression expr = assignmentStatement.expression;
		expr.visit(this, mv);
		if (expr.type == Type.STRING || expr.type == Type.ARRAY)
			mv.visitVarInsn(ASTORE, assignmentStatement.declaration.slotNumber);
		else if (expr.type == Type.FLOAT) mv.visitVarInsn(FSTORE, assignmentStatement.declaration.slotNumber);
		else mv.visitVarInsn(ISTORE, assignmentStatement.declaration.slotNumber);
		return null;
	}

	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		Label label = new Label();
		ifStatement.expression.visit(this, mv);
		mv.visitInsn(ICONST_1);
		mv.visitJumpInsn(IF_ICMPNE, label);
		ifStatement.block.visit(this, mv);
		mv.visitLabel(label);
		return null;
	}

	@Override
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		Label l1 = new Label();
		Label l2 = new Label();
		mv.visitLabel(l1);
		whileStatement.expression.visit(this, mv);
		mv.visitInsn(ICONST_1);
		mv.visitJumpInsn(IF_ICMPNE, l2);
		whileStatement.block.visit(this, mv);
		mv.visitJumpInsn(GOTO, l1);
		mv.visitLabel(l2);
		return null;
	}

	@Override
	public Object visitPrintStatement(PrintStatement printStatement, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		String type = printStatement.expression.getJvmType();
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		printStatement.expression.visit(this, mv);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(" + type + ")V", false);
		return null;
	}

	@Override
	public Object visitPlotStatement(PlotStatement plotStatement, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/utilities/PlotUtility");
		plotStatement.expression.visit(this, arg);
		plotStatement.array.visit(this, arg);
		plotStatement.points.visit(this, arg);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/utilities/PlotUtility", "<init>", "(Ljava/lang/String;[FI)V", false);
		return null;
	}

	@Override
	public Object visitPlayStatement(PlayStatement playStatement, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/generators/AudioTask");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/generators/AudioTask", "<init>", "()V", false);
		playStatement.expression.visit(this, arg);
		playStatement.format.visit(this, arg);
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/generators/AudioTask", "play", "([FI)V", false);	
		return null;
	}

	@Override
	public Object visitIdentExpression(IdentExpression identExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		if (identExpression.type == Type.STRING || identExpression.type == Type.ARRAY)
			mv.visitVarInsn(ALOAD, identExpression.declaration.slotNumber);
		else if (identExpression.type == Type.FLOAT) mv.visitVarInsn(FLOAD, identExpression.declaration.slotNumber);
		else mv.visitVarInsn(ILOAD, identExpression.declaration.slotNumber);
		return null;
	}

	@Override
	public Object visitInfoExpression(InfoExpression infoExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitLdcInsn(infoExpression.value);
		return null;
	}

	@Override
	public Object visitIntLitExpression(IntLitExpression intLitExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitLdcInsn(intLitExpression.value);
		return null;
	}

	@Override
	public Object visitFloatLitExpression(FloatLitExpression floatLitExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitLdcInsn(floatLitExpression.value);
		return null;
	}

	@Override
	public Object visitBoolLitExpression(BoolLitExpression boolLitExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		if (boolLitExpression.value) mv.visitInsn(ICONST_1);
		else mv.visitInsn(ICONST_0);
		return null;
	}

	@Override
	public Object visitStringLitExpression(StringLitExpression stringLitExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitLdcInsn(stringLitExpression.firstToken.text);
		return null;
	}
	
	@Override
	public Object visitArrayLitExpression(ArrayLitExpression arrayLitExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitLdcInsn(arrayLitExpression.floats.size());
		mv.visitIntInsn(NEWARRAY, T_FLOAT);
		for (int i = 0; i < arrayLitExpression.floats.size(); i++) {
			mv.visitInsn(DUP);
			mv.visitLdcInsn(i);
			mv.visitLdcInsn(arrayLitExpression.floats.get(i));
			mv.visitInsn(FASTORE);
		}
		return null;
	}

	@Override
	public Object visitReadExpression(ReadExpression readExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/generators/WaveFile");
		mv.visitInsn(DUP);
		readExpression.fileName.visit(this, arg);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/generators/WaveFile", "<init>", "(Ljava/lang/String;)V", false);
		readExpression.format.visit(this, arg);
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/generators/WaveFile", "get", "(I)[F", false);
		return null;
	}

	@Override
	public Object visitReverseExpression(ReverseExpression reverseExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/effects/Reverse");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/effects/Reverse", "<init>", "()V", false);
		reverseExpression.array.visit(this, arg);
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/effects/Reverse", "process", "([F)[F", false);
		return null;
	}
	
	@Override
	public Object visitSpeedExpression(SpeedExpression speedExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/effects/Speed");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/effects/Speed", "<init>", "()V", false);
		speedExpression.array.visit(this, arg);
		speedExpression.speed.visit(this, arg);
		String type = speedExpression.speed.getJvmType();
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/effects/Speed", "process", "([F" + type + ")[F", false);
		return null;
	}

	@Override
	public Object visitLoopExpression(LoopExpression loopExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/effects/Loop");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/effects/Loop", "<init>", "()V", false);
		loopExpression.array.visit(this, arg);
		loopExpression.start.visit(this, arg);
		loopExpression.end.visit(this, arg);
		loopExpression.count.visit(this, arg);
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/effects/Loop", "process", "([FIII)[F", false);
		return null;
	}

	@Override
	public Object visitDelayExpression(DelayExpression delayExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/effects/Delay");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/effects/Delay", "<init>", "()V", false);
		delayExpression.array.visit(this, arg);
		delayExpression.time.visit(this, arg);
		String type = delayExpression.time.getJvmType();
		delayExpression.feedback.visit(this, arg);
		type += delayExpression.feedback.getJvmType();
		delayExpression.mix.visit(this, arg);
		type += delayExpression.mix.getJvmType();
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/effects/Delay", "process", "([F" + type + ")[F", false);
		return null;
	}
	
	@Override
	public Object visitGainExpression(GainExpression gainExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/effects/Gain");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/effects/Gain", "<init>", "()V", false);
		gainExpression.array.visit(this, arg);
		gainExpression.gain.visit(this, arg);
		String type = gainExpression.gain.getJvmType();
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/effects/Gain", "process", "([F" + type + ")[F", false);
		return null;
	}
	
	@Override
	public Object visitBiquadExpression(BiquadExpression biquadExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/effects/BiquadConvenience");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/effects/BiquadConvenience", "<init>", "()V", false);
		biquadExpression.array.visit(this, arg);
		biquadExpression.cutoff.visit(this, arg);
		String type = biquadExpression.cutoff.getJvmType();
		biquadExpression.resonance.visit(this, arg);
		type += biquadExpression.resonance.getJvmType();
		biquadExpression.method.visit(this, arg);
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/effects/BiquadConvenience", "process", "([F" + type + "I)[F", false);
		return null;
	}
	
	@Override
	public Object visitLineExpression(LineExpression lineExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/generators/BreakpointFunction");
		mv.visitInsn(DUP);
		lineExpression.size.visit(this, arg);
		lineExpression.breakpoints.visit(this, arg);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/generators/BreakpointFunction", "<init>", "(I[F)V", false);
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/generators/BreakpointFunction", "get", "()[F", false);		
		return null;
	}

	@Override
	public Object visitSpliceExpression(SpliceExpression spliceExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/effects/Splice");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/effects/Splice", "<init>", "()V", false);
		mv.visitLdcInsn(spliceExpression.expressions.size());
		mv.visitTypeInsn(ANEWARRAY, "[F");
		for (int i = 0; i < spliceExpression.expressions.size(); i++) {
			mv.visitInsn(DUP);
			mv.visitLdcInsn(i);
			spliceExpression.expressions.get(i).visit(this, arg);
			mv.visitInsn(AASTORE);
		}
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/effects/Splice", "process", "([[F)[F", false);
		return null;
	}

	@Override
	public Object visitFormatExpression(FormatExpression formatExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitLdcInsn(formatExpression.channels);
		return null;
	}
	
	@Override
	public Object visitFilterExpression(FilterExpression filterExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitLdcInsn(filterExpression.method);
		return null;
	}

	@Override
	public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		binaryExpression.e0.visit(this, mv);
		binaryExpression.e1.visit(this, mv);
		switch (binaryExpression.operator.kind) {
		case MOD: {
			if (binaryExpression.type == Type.FLOAT) mv.visitInsn(FREM);
			else mv.visitInsn(IREM);
		} break;
		case PLUS: {
			if (binaryExpression.type == Type.FLOAT) mv.visitInsn(FADD);
			else mv.visitInsn(IADD);
		} break;
		case MINUS: {
			if (binaryExpression.type == Type.FLOAT) mv.visitInsn(FSUB);
			else mv.visitInsn(ISUB);
		} break;
		case TIMES: {
			if (binaryExpression.type == Type.FLOAT) mv.visitInsn(FMUL);
			else mv.visitInsn(IMUL);
		} break;
		case DIV: {
			if (binaryExpression.type == Type.FLOAT) mv.visitInsn(FDIV);
			else mv.visitInsn(IDIV);
		} break;
		case AND: {
			mv.visitInsn(IAND);
		} break;
		case OR: {
			mv.visitInsn(IOR);
		} break;
		default: {
			Label l1 = new Label();
			switch (binaryExpression.operator.kind) {
			case LT: {
				mv.visitJumpInsn(IF_ICMPGE, l1);
			} break;
			case LE: {
				mv.visitJumpInsn(IF_ICMPGT, l1);
			} break;
			case GT: {
				mv.visitJumpInsn(IF_ICMPLE, l1);
			} break;
			case GE: {
				mv.visitJumpInsn(IF_ICMPLT, l1);
			} break;
			case EQUAL: {
				mv.visitJumpInsn(IF_ICMPNE, l1);
			} break;
			case NOTEQUAL: {
				mv.visitJumpInsn(IF_ICMPEQ, l1);
			} break;
			default: break;
			}
			mv.visitInsn(ICONST_1);
			Label l2 = new Label();
			mv.visitJumpInsn(GOTO, l2);
			mv.visitLabel(l1);
			mv.visitInsn(ICONST_0);
			mv.visitLabel(l2);
		} break;
		}
		return null;
	}

}
