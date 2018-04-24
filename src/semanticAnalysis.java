import java.util.ArrayList;

class SymbolTable{

	static public enum Type={INT,ARRAY_INT};

	public ArrayList<Pair<String,Type>> globalDeclarations = new ArrayList<>();
	public ArrayList<Function> functions = new ArrayList<>();
	ArrayList<String> errors;

	static public class Pair<K,V>{
		public K key;
		public V value;

		@override
		public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        Pair that = (Pair) o;
	        return this.key == that.key;
    	}
	}

	static public class FunctionCall{
		public String functionName; //nome a procurar na tabela de funcoes
		public Type returnType; //tipo do objeto que esta a receber o retorno da funcao
		public ArrayList<Type> argumentTypes; //tipo dos argumentos passados na chamada
	}

	static public class Function{
		public String functionName; //nome da funcao
		public Type returnType; //tipo de retorno
		public ArrayList<Type> argumentTypes; //quantidade e tipo de argumentos
		public ArrayList<Pair<String,Type>> localDeclarations = new ArrayList<>(); //variaveis + argumentos
		public ArrayList<FunctionCall> functionCalls = new ArrayList<>();
	}
}

void analiseFunction(SimpleNode parent, Function f, ArrayList<String> errors){
	
	for(int i = 0; i < parent.jjtGetNumChildren(); i++){
		if(parent.jjtGetChild(i).getId() == yal2jvmTreeConstants.JJTASSIGN){
			checkAssign(parent.jjtGetChild(i), f);
			if(!checkDuplicates(functions,(SimpleNode)parent.jjtGetChild(i))){
				Function f = new Function(parent.jjtGetChild(i).getId());
				functions.add(f);
			}
			else{
				errors.add("Semantic error: node \"" + parent.jjtGetChild(i) + "\" is duplicate.");
			}
		}
		else if(parent.jjtGetChild(i).getId() == yal2jvmTreeConstants.JJTDECLARATION){
			if(!checkDuplicates(declarations,(SimpleNode)parent.jjtGetChild(i)))
				declarations.add((SimpleNode)parent.jjtGetChild(i));
			else{
				errors.add("Semantic error: node \"" + parent.jjtGetChild(i) + "\" is duplicate.");
			}
		}
	}
}


void checkFunctionCalls(){
	boolean found = false;
	for(FunctionCall fc : functionCalls){
		for (int i = 0; i < functions.size(); i++0)
			if (functions.get(i).functionName.equals(fc.functionName))
				found=true;
		if (!found)
			errors.add("Semantic error: there is no function \"" + fc.functionName + "\" !");
	}
}


void checkAssign(SimpleNode assign, Function f, ArrayList<String> errors){

		SimpleNode lhs = assign.jjtGetChild(0);
		Object var = lhs.jjtGetSecValue();
		//aqui não há semântica, só adiciona o valor da esquerda à table
		for (int i = 0; i < lhs.jjtGetNumChildren(); i++){
			SimpleNode lhsChild = lhs.jjtGetChild(i);
			if(lhsChild.getId() == yal2jvmTreeConstants.JJTLHSARRAYSIZE)
				if (!localDeclarations.contains(new Pair((String) var, ARRAY_INT)))
					localDeclarations.add(new Pair((String) var, ARRAY_INT));
			else if(lhsChild.getId() == yal2jvmTreeConstants.JJTINDEX)
				if (!localDeclarations.contains(new Pair((String) var, ARRAY_INT)))
					localDeclarations.add(new Pair((String) lhs, ARRAY_INT));
			else
				if (!localDeclarations.contains(new Pair((String) lhs, INT)))
					localDeclarations.add(new Pair((String) var, INT));
		}

		SimpleNode rhs = assign.jjtGetChild(1); 

		for (int i = 0; i < rhs.jjtGetNumChildren(); i++){
			SimpleNode rhsChild = rhs.jjtGetChild(i);
			if(rhsChild.getId() == yal2jvmTreeConstants.JJTTERM){

				for (int i = 0; i < rhsChild.jjtGetNumChildren(); i++){
					SimpleNode termChild = rhsChild.jjtGetChild(i);
					if ()
			}		
			if(rhsChild.getId() == yal2jvmTreeConstants.JJTARRAYSIZE)
				if (!localDeclarations.contains(new Pair((String) var, ARRAY_INT)))
					localDeclarations.add(new Pair((String) var, ARRAY_INT));

			else if(rhsChild.getId() == yal2jvmTreeConstants.JJTINDEX)
				if (!localDeclarations.contains(new Pair((String) var, ARRAY_INT)))
					localDeclarations.add(new Pair((String) lhs, ARRAY_INT));
			else
				if (!localDeclarations.contains(new Pair((String) lhs, INT)))
					localDeclarations.add(new Pair((String) lhs, INT));
		}
	}

void handleFunction(Function f, ArrayList<String> errors){

		for (int i = 0; i < f.jjtGetNumChildren(); i++){
			if(f.jjtGetChild(i).getId() == yal2jvmTreeConstants.RETORNODAFUNCAO) //we need to get function ret
				handleFunctionRet(f, errors);
			else if(f.jjtGetChild(i).getId() == yal2jvmTreeConstants.JJTVARLIST)
				handleFunctionParams(f, errors);
			else if(f.jjtGetChild(i).getId() == yal2jvmTreeConstants.JJTSTMTLST)
				handleFunctionBody(f, errors);
		}
	}

void handleFunctionBody(Function parent, ArrayList<String> errors){	

	SimpleNode functionStmts = (SimpleNode)parent.jjtGetChild(0);
	for(int i = 0; i < functionStmts.jjtGetNumChildren(); i++){
		SimpleNode stmt = (SimpleNode) functionStmts.jjtGetChild(i).jjtGetChild(i);
		if(stmt.getId() == yal2jvmTreeConstants.JJTIF)
			parent.addCondStatements(stmt);
		else if(stmt.getId() == yal2jvmTreeConstants.JJTWHILE)
			parent.addCondStatements(stmt);
		else if(stmt.getId() == yal2jvmTreeConstants.JJTASSIGN)
			parent.addVariables(stmt);
		else if(stmt.getId() == yal2jvmTreeConstants.JJTCALL)
			parent.addCalls(stmt);
	}
}

void handleFunctionParams(Function parent, ArrayList<String> errors){	

}

void handleFunctionRet(Function parent, ArrayList<String> errors){	

}

boolean checkDuplicates(ArrayList<SimpleNode> nodes, SimpleNode node){
	for(SimpleNode n : nodes){
		if(n.jjtGetValue().equals(node.jjtGetValue())){
			System.out.println("dup " + node.jjtGetValue());
			return true;
		}
	}
	return false;
}