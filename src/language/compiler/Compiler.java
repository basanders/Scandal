package language.compiler;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

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

	public byte[] compile(String fileName, Object args) throws Exception {
		Path path = FileSystems.getDefault().getPath(fileName);
		String code = new String(Files.readAllBytes(path));
		int extension = path.getFileName().toString().lastIndexOf('.');
		String className = path.getFileName().toString().substring(0, extension);
		Scanner scanner = new Scanner(code);
		scanner.scan();
		Program program = new Parser(scanner).parse();
		program.visit(new TypeChecker(), null);
		byte[] bytecode = (byte[]) program.visit(new BytecodeGenerator(), className);
		DynamicClassLoader loader = new DynamicClassLoader(Thread.currentThread().getContextClassLoader());
		Class<?> testClass = loader.define(className, bytecode);
		Constructor<?> constructor = testClass.getConstructor(args.getClass());
		((Runnable) constructor.newInstance(args)).run();
		return bytecode;
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
		String classFileName = name;
		OutputStream output = new FileOutputStream(classFileName);
		output.write(bytecode);
		output.close();
	}

}
