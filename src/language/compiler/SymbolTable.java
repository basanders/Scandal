package language.compiler;

import java.util.Hashtable;
import java.util.Stack;

import language.tree.Declaration;

public class SymbolTable {

	private final Stack<Hashtable<String, Declaration>> table = new Stack<Hashtable<String, Declaration>>();
	public int scopeNumber = 0;

	public SymbolTable() {
		// Initialize the zeroth scope
		table.push(new Hashtable<String, Declaration>());
	}

	public void enterScope() {
		table.push(new Hashtable<String, Declaration>());
		scopeNumber++;
	}

	public void leaveScope() {
		if (!table.empty()) {
			table.pop();
			scopeNumber--;
		}
	}

	public void insert(String ident, Declaration Declaration){
		if (!table.empty()) table.peek().put(ident, Declaration);
	}

	public Declaration lookup(String ident) {
		// Last in, first out
		for (int i = table.size() - 1; i >= 0; i--) {
			Declaration info = table.elementAt(i).get(ident);
			if (info != null) return info;
		}
		return null;
	}

	public Declaration topOfStackLookup(String ident) {
		if (!table.empty()) return table.peek().get(ident);
		return null;
	}

}
