package language.compiler;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;

import language.tree.Program;

public class Compiler {

	public class DynamicClassLoader extends ClassLoader {
		public DynamicClassLoader(ClassLoader parent) {
			super(parent);
		}

		public Class<?> define(String name, byte[] bytecode) {
			return super.defineClass(name, bytecode, 0, bytecode.length);
		}
	}

	public Runnable getInstance(String name, byte[] bytecode, Object args) throws Exception {
		DynamicClassLoader loader = new DynamicClassLoader(Thread.currentThread().getContextClassLoader());
		Class<?> testClass = loader.define(name, bytecode);
		Constructor<?> constructor = testClass.getConstructor(args.getClass());
		return (Runnable) constructor.newInstance(args);
	}

	public void compileAndRun(String name, String code, String[] args) throws Exception {
		Scanner scanner = new Scanner(code);
		scanner.scan();
		Program program = new Parser(scanner).parse();
		program.visit(new TypeChecker(), name);
		byte[] bytecode = (byte[]) program.visit(new BytecodeGenerator(), name);
		print(bytecode);
		Runnable instance = getInstance(name, bytecode, args);
		instance.run();
	}

	public String decompile(byte[] bytecode) {
		ClassReader cr = new ClassReader(bytecode);
		StringWriter out = new StringWriter();
		cr.accept(new TraceClassVisitor(new PrintWriter(out)), ClassReader.SKIP_DEBUG);
		return out.toString();
	}

	public void print(byte[] bytecode) {
		ClassReader cr = new ClassReader(bytecode);
		PrintStream out = System.out;
		cr.accept(new TraceClassVisitor(new PrintWriter(out)), ClassReader.SKIP_DEBUG);
	}

	public void save(String name, byte[] bytecode) throws Exception {
		String classFileName = "bin/" + name + ".class";
		OutputStream output = new FileOutputStream(classFileName);
		output.write(bytecode);
		output.close();
	}

}
