package language.compiler;

import java.util.ArrayList;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import language.tree.*;
import language.tree.Node.Type;

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
		if (plotStatement.points.type == Type.FLOAT) mv.visitInsn(F2I);
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
	public Object visitWriteStatement(WriteStatement writeStatement, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/generators/AudioTask");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/generators/AudioTask", "<init>", "()V", false);
		writeStatement.expression.visit(this, arg);
		writeStatement.name.visit(this, arg);
		writeStatement.format.visit(this, arg);
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/generators/AudioTask", "export", "([FLjava/lang/String;I)V", false);
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
		if (speedExpression.speed.type == Type.INT) mv.visitInsn(I2F);
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/effects/Speed", "process", "([FF)[F", false);
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
		if (loopExpression.start.type == Type.FLOAT) mv.visitInsn(F2I);
		loopExpression.end.visit(this, arg);
		if (loopExpression.end.type == Type.FLOAT) mv.visitInsn(F2I);
		loopExpression.count.visit(this, arg);
		if (loopExpression.count.type == Type.FLOAT) mv.visitInsn(F2I);
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
		String type = "([FI";
		delayExpression.time.visit(this, arg);
		if (delayExpression.time.type == Type.FLOAT) mv.visitInsn(F2I);
		delayExpression.feedback.visit(this, arg);
		if (delayExpression.feedback.type == Type.INT) mv.visitInsn(I2F);
		if (delayExpression.feedback.type == Type.ARRAY) type += "[";
		type += "F";
		delayExpression.mix.visit(this, arg);
		if (delayExpression.mix.type == Type.INT) mv.visitInsn(I2F);
		if (delayExpression.mix.type == Type.ARRAY) type += "[";
		type += "F)[F";
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/effects/Delay", "process", type, false);
		return null;
	}
	
	@Override
	public Object visitOscillatorExpression(OscillatorExpression oscillatorExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/utilities/WavetableOscillatorUtility");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/utilities/WavetableOscillatorUtility", "<init>", "()V", false);
		oscillatorExpression.duration.visit(this, arg);
		if (oscillatorExpression.duration.type == Type.FLOAT) mv.visitInsn(F2I);
		String type = "(I";
		oscillatorExpression.amplitude.visit(this, arg);
		if (oscillatorExpression.amplitude.type == Type.INT) mv.visitInsn(I2F);
		if (oscillatorExpression.amplitude.type == Type.ARRAY) type += "[";
		type += "F";
		oscillatorExpression.frequency.visit(this, arg);
		if (oscillatorExpression.frequency.type == Type.INT) mv.visitInsn(I2F);
		if (oscillatorExpression.frequency.type == Type.ARRAY) type += "[";
		type += "F";
		oscillatorExpression.shape.visit(this, arg);
		type += "I)[F";
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/utilities/WavetableOscillatorUtility", "get", type, false);
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
		String type = "([F";
		if (gainExpression.gain.type == Type.INT) mv.visitInsn(I2F);
		if (gainExpression.gain.type == Type.ARRAY) type += "[";
		type += "F)[F";
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/effects/Gain", "process", type, false);
		return null;
	}
	
	@Override
	public Object visitBiquadExpression(BiquadExpression biquadExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/utilities/BiquadUtility");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/utilities/BiquadUtility", "<init>", "()V", false);
		biquadExpression.array.visit(this, arg);
		String type = "([F";
		biquadExpression.cutoff.visit(this, arg);
		if (biquadExpression.cutoff.type == Type.INT) mv.visitInsn(I2F);
		if (biquadExpression.cutoff.type == Type.ARRAY) type += "[";
		type += "F";
		biquadExpression.resonance.visit(this, arg);
		if (biquadExpression.resonance.type == Type.INT) mv.visitInsn(I2F);
		if (biquadExpression.resonance.type == Type.ARRAY) type += "[";
		type += "F";
		biquadExpression.method.visit(this, arg);
		type += "I)[F";
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/utilities/BiquadUtility", "process", type, false);
		return null;
	}
	
	@Override
	public Object visitTremoloExpression(TremoloExpression tremoloExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/utilities/RingModulatorUtility");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/utilities/RingModulatorUtility", "<init>", "()V", false);
		tremoloExpression.array.visit(this, arg);
		String type = "([F";
		tremoloExpression.depth.visit(this, arg);
		if (tremoloExpression.depth.type == Type.INT) mv.visitInsn(I2F);
		if (tremoloExpression.depth.type == Type.ARRAY) type += "[";
		type += "F";
		tremoloExpression.speed.visit(this, arg);
		if (tremoloExpression.speed.type == Type.INT) mv.visitInsn(I2F);
		if (tremoloExpression.speed.type == Type.ARRAY) type += "[";
		type += "F";
		tremoloExpression.shape.visit(this, arg);
		type += "I)[F";
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/utilities/RingModulatorUtility", "process", type, false);
		return null;
	}
	
	@Override
	public Object visitLineExpression(LineExpression lineExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/generators/BreakpointFunction");
		mv.visitInsn(DUP);
		lineExpression.size.visit(this, arg);
		if (lineExpression.size.type == Type.FLOAT) mv.visitInsn(F2I);
		lineExpression.breakpoints.visit(this, arg);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/generators/BreakpointFunction", "<init>", "(I[F)V", false);
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/generators/BreakpointFunction", "get", "()[F", false);		
		return null;
	}
	
	@Override
	public Object visitPanExpression(PanExpression panExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/generators/StereoPanner");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/generators/StereoPanner", "<init>", "()V", false);
		panExpression.array.visit(this, arg);
		String type = "([F";
		panExpression.position.visit(this, arg);
		if (panExpression.position.type == Type.INT) mv.visitInsn(I2F);
		if (panExpression.position.type == Type.ARRAY) type += "[";
		type += "F)[F";
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/generators/StereoPanner", "process", type, false);
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
	public Object visitMixExpression(MixExpression mixExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/generators/StereoMixer");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/generators/StereoMixer", "<init>", "()V", false);
		mv.visitLdcInsn(mixExpression.tracks.size());
		mv.visitTypeInsn(ANEWARRAY, "[F");
		for (int i = 0; i < mixExpression.tracks.size(); i++) {
			mv.visitInsn(DUP);
			mv.visitLdcInsn(i);
			mixExpression.tracks.get(i).visit(this, arg);
			mv.visitInsn(AASTORE);
		}
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/generators/StereoMixer", "render", "([[F)[F", false);
		return null;
	}

	@Override
	public Object visitFormatExpression(FormatExpression formatExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitLdcInsn(formatExpression.channels);
		return null;
	}
	
	@Override
	public Object visitWaveformExpression(WaveformExpression waveformExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitLdcInsn(waveformExpression.shape);
		return null;
	}
	
	@Override
	public Object visitFilterExpression(FilterExpression filterExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitLdcInsn(filterExpression.method);
		return null;
	}
	
	@Override
	public Object visitRecordExpression(RecordExpression recordExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/generators/AudioTask");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/generators/AudioTask", "<init>", "()V", false);
		recordExpression.duration.visit(this, arg);
		if (recordExpression.duration.type == Type.FLOAT) mv.visitInsn(F2I);
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/generators/AudioTask", "record", "(I)[F", false);
		return null;
	}
	
	@Override
	public Object visitTrackExpression(TrackExpression trackExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/generators/AudioTrack");
		mv.visitInsn(DUP);
		trackExpression.array.visit(this, arg);
		String type = "([FI";
		trackExpression.start.visit(this, arg);
		if (trackExpression.start.type == Type.FLOAT) mv.visitInsn(F2I);
		trackExpression.gain.visit(this, arg);
		if (trackExpression.gain.type == Type.INT) mv.visitInsn(I2F);
		if (trackExpression.gain.type == Type.ARRAY) type += "[";
		type += "F";
		trackExpression.pan.visit(this, arg);
		if (trackExpression.pan.type == Type.INT) mv.visitInsn(I2F);
		if (trackExpression.pan.type == Type.ARRAY) type += "[";
		type += "F)V";
		mv.visitMethodInsn(INVOKESPECIAL, "framework/generators/AudioTrack", "<init>", type, false);
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/generators/AudioTrack", "getShiftedVector", "()[F", false);
		return null;
	}
	
	@Override
	public Object visitUnaryExpression(UnaryExpression unaryExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		switch (unaryExpression.type) {
		case INT: {
			unaryExpression.e.visit(this, arg);
			mv.visitInsn(INEG);
		} break;
		case FLOAT: {
			unaryExpression.e.visit(this, arg);
			mv.visitInsn(FNEG);
		} break;
		default: {
			Label l1 = new Label();
			Label l2 = new Label();
			unaryExpression.e.visit(this, arg);
			mv.visitJumpInsn(IFEQ, l1);
			mv.visitInsn(ICONST_0);
			mv.visitJumpInsn(GOTO, l2);
			mv.visitLabel(l1);
			mv.visitFrame(Opcodes.F_APPEND, 2, new Object[] {"org/objectweb/asm/MethodVisitor", Opcodes.INTEGER}, 0, null);
			mv.visitInsn(ICONST_1);
			mv.visitLabel(l2);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {Opcodes.INTEGER});
		} break;
		}
		return null;
	}

	@Override
	public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		binaryExpression.e0.visit(this, mv);
		switch (binaryExpression.operator.kind) {
		case MOD: {
			if (binaryExpression.type == Type.FLOAT) {
				if (binaryExpression.e0.type == Type.INT) mv.visitInsn(I2F);
				binaryExpression.e1.visit(this, mv);
				if (binaryExpression.e1.type == Type.INT) mv.visitInsn(I2F);
				mv.visitInsn(FREM);
			}
			else {
				if (binaryExpression.e0.type == Type.FLOAT) mv.visitInsn(F2I);
				binaryExpression.e1.visit(this, mv);
				if (binaryExpression.e1.type == Type.FLOAT) mv.visitInsn(F2I);
				mv.visitInsn(IREM);
			}
		} break;
		case PLUS: {
			if (binaryExpression.type == Type.FLOAT) {
				if (binaryExpression.e0.type == Type.INT) mv.visitInsn(I2F);
				binaryExpression.e1.visit(this, mv);
				if (binaryExpression.e1.type == Type.INT) mv.visitInsn(I2F);
				mv.visitInsn(FADD);
			}
			else {
				if (binaryExpression.e0.type == Type.FLOAT) mv.visitInsn(F2I);
				binaryExpression.e1.visit(this, mv);
				if (binaryExpression.e1.type == Type.FLOAT) mv.visitInsn(F2I);
				mv.visitInsn(IADD);
			}
		} break;
		case MINUS: {
			if (binaryExpression.type == Type.FLOAT) {
				if (binaryExpression.e0.type == Type.INT) mv.visitInsn(I2F);
				binaryExpression.e1.visit(this, mv);
				if (binaryExpression.e1.type == Type.INT) mv.visitInsn(I2F);
				mv.visitInsn(FSUB);
			}
			else {
				if (binaryExpression.e0.type == Type.FLOAT) mv.visitInsn(F2I);
				binaryExpression.e1.visit(this, mv);
				if (binaryExpression.e1.type == Type.FLOAT) mv.visitInsn(F2I);
				mv.visitInsn(ISUB);
			}
		} break;
		case TIMES: {
			if (binaryExpression.type == Type.FLOAT) {
				if (binaryExpression.e0.type == Type.INT) mv.visitInsn(I2F);
				binaryExpression.e1.visit(this, mv);
				if (binaryExpression.e1.type == Type.INT) mv.visitInsn(I2F);
				mv.visitInsn(FMUL);
			}
			else {
				if (binaryExpression.e0.type == Type.FLOAT) mv.visitInsn(F2I);
				binaryExpression.e1.visit(this, mv);
				if (binaryExpression.e1.type == Type.FLOAT) mv.visitInsn(F2I);
				mv.visitInsn(IMUL);
			}
		} break;
		case DIV: {
			if (binaryExpression.type == Type.FLOAT) {
				if (binaryExpression.e0.type == Type.INT) mv.visitInsn(I2F);
				binaryExpression.e1.visit(this, mv);
				if (binaryExpression.e1.type == Type.INT) mv.visitInsn(I2F);
				mv.visitInsn(FDIV);
			}
			else {
				if (binaryExpression.e0.type == Type.FLOAT) mv.visitInsn(F2I);
				binaryExpression.e1.visit(this, mv);
				if (binaryExpression.e1.type == Type.FLOAT) mv.visitInsn(F2I);
				mv.visitInsn(IDIV);
			}
		} break;
		case AND: {
			binaryExpression.e1.visit(this, mv);
			mv.visitInsn(IAND);
		} break;
		case OR: {
			binaryExpression.e1.visit(this, mv);
			mv.visitInsn(IOR);
		} break;
		default: {
			binaryExpression.e1.visit(this, mv);
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
