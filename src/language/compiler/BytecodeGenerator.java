package language.compiler;

import java.util.ArrayList;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

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
import language.tree.InfoExpression;
import language.tree.IntLitExpression;
import language.tree.Node.Type;
import language.tree.NodeVisitor;
import language.tree.PrintStatement;
import language.tree.Program;
import language.tree.Statement;
import language.tree.StringLitExpression;
import language.tree.UnassignedDeclaration;
import language.tree.WaveFileExpression;
import language.tree.WhileStatement;

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
		if (expr.type == Type.STRING || expr.type == Type.ARRAY) {
			mv.visitVarInsn(ASTORE, declaration.slotNumber);
		}
		else if (expr.type == Type.FLOAT) mv.visitVarInsn(FSTORE, declaration.slotNumber);
		else mv.visitVarInsn(ISTORE, declaration.slotNumber);
		return null;
	}

	@Override
	public Object visitAssignmentStatement(AssignmentStatement assignmentStatement, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		Expression expr = assignmentStatement.expression;
		expr.visit(this, mv);
		if (expr.type == Type.STRING || expr.type == Type.ARRAY) {
			mv.visitVarInsn(ASTORE, assignmentStatement.declaration.slotNumber);
		}
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
		String type = printStatement.expression.getJvmType();
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		printStatement.expression.visit(this, mv);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(" + type + ")V", false);
		return null;
	}

	@Override
	public Object visitIdentExpression(IdentExpression identExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		if (identExpression.type == Type.STRING || identExpression.type == Type.ARRAY) {
			mv.visitVarInsn(ALOAD, identExpression.declaration.slotNumber);
		}
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
	public Object visitWaveFileExpression(WaveFileExpression waveFileExpression, Object arg) throws Exception {
		MethodVisitor mv = (MethodVisitor) arg;
		mv.visitTypeInsn(NEW, "framework/generators/WaveFile");
		mv.visitInsn(DUP);
		waveFileExpression.expression.visit(this, arg);
		mv.visitMethodInsn(INVOKESPECIAL, "framework/generators/WaveFile", "<init>", "(Ljava/lang/String;)V", false);
		mv.visitMethodInsn(INVOKEVIRTUAL, "framework/generators/WaveFile", "getMonoSum", "()[F", false);
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
