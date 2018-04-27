import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

class SymbolTable {

	public String moduleName;

	public HashMap<String,SimpleNode.Type> globalDeclarations = new HashMap<>();
	public HashMap<Signature,Function> functions = new HashMap<>();

	public ArrayList<Signature> repeatedFunctions = new ArrayList<>();
	public ArrayList<Pair<String,SimpleNode.Type>> repeatedGlobalDeclarationsDiffType = new ArrayList<>();

	public static class Pair<K,V> {
		
		public K key;
		public V value;

		public Pair(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public boolean equals(Object object) {

			Pair p2 = (Pair) object;
			return p2.key.equals(this.key) && p2.value.equals(this.value);

		} 

		@Override
		public int hashCode() {
			return Objects.hash(this.value);
		}
	
	}

	public static class FunctionCall {
		
		public Signature signature;
		public String module;

		public FunctionCall(Signature signature, String module) {
			this.signature = signature;
			this.module = module;
		}
	
	}

	public static class Signature {
		
		public String functionName; //nome da funcao
		public ArrayList<String> arguments = new ArrayList<>();
		public ArrayList<SimpleNode.Type> argumentTypes = new ArrayList<>();

		public Signature(String functionName, ArrayList<Pair<String,SimpleNode.Type>> arguments) {

			this.functionName = functionName;

			for(Pair<String,SimpleNode.Type> pair : arguments) {

				this.arguments.add(pair.key);
				this.argumentTypes.add(pair.value);

			}

		}

		public Signature(String functionName) {
			this.functionName = functionName;
			this.arguments = new ArrayList<>();
		}

		public void addArgumentType(String argName, SimpleNode.Type type) {

			this.arguments.add(argName);
			this.argumentTypes.add(type);
		}

		@Override
		public boolean equals(Object object) {

			Signature s2 = (Signature) object;

			if(!this.functionName.equals(s2.functionName))
				return false;
			if(this.arguments.size() != s2.arguments.size())
				return false;
			for(int i = 0; i < this.arguments.size(); i++)
				if(this.argumentTypes.get(i) != s2.argumentTypes.get(i))
					return false;

			return true;


		}

		@Override
		public int hashCode() {

			return Objects.hash(this.functionName,this.argumentTypes);
		}

	}


	public static class Function {

		public Signature signature; 
		public HashMap<String,SimpleNode.Type> localDeclarations = new HashMap<>();
		public ArrayList<Pair<String,SimpleNode.Type>> repeatedLocalDeclarationsDiffType = new ArrayList<>();
		public ArrayList<FunctionCall> functionCalls = new ArrayList<>();
		public SimpleNode.Type returnType; //tipo de retorno

		public ArrayList<Pair<String,String>> nullDeclarationsVariables = new ArrayList<>();
		public ArrayList<Pair<String,Signature>> nullDeclarationsFunctionCalls = new ArrayList<>();


		public Function(Signature signature, SimpleNode.Type type) {
			this.signature = signature;
			this.returnType = type;
		}

		public boolean addLocalDeclaration(String key, SimpleNode.Type value, String localVariable, Signature functionCall) {

			SimpleNode.Type exists = this.localDeclarations.get(key);

			if(exists == null) {

				this.localDeclarations.put(key, value);

				if(value == null) {

					if(localVariable != null)
						this.nullDeclarationsVariables.add(new Pair(key,localVariable));
					else
						this.nullDeclarationsFunctionCalls.add(new Pair(key,functionCall));

				}
					

				return true;

			}

			if(exists != value) {

				this.repeatedLocalDeclarationsDiffType.add(new Pair(key,value));

			}

			
			return false;
		
		}

		public void addFunctionCall(Signature signature, String module) {
			this.functionCalls.add(new FunctionCall(signature, module));
		}

		@Override
		public boolean equals(Object object) {

			Function f2 = (Function) object;

			return this.signature.equals(f2.signature);

		}
	
	}

	public SymbolTable(String moduleName) {
		this.moduleName = moduleName;
	}


	public boolean addGlobalDeclaration(String key, SimpleNode.Type value) {

		SimpleNode.Type exists = this.globalDeclarations.get(key);

		if(exists == null) {

			this.globalDeclarations.put(key, value);
			return true;

		}

		if(exists != value) {

			this.repeatedGlobalDeclarationsDiffType.add(new Pair(key,value));

		}
			
		return false;

	}

	public boolean addFunction(Function function) {

		Function exists = this.functions.get(function.signature);
		
		if(exists == null) {

			this.functions.put(function.signature, function);
			return true;

		}

		this.repeatedFunctions.add(function.signature);
		return false;

	}

	@Override
	public String toString() {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		PrintWriter out = new PrintWriter(outStream);

		out.println("Module " + this.moduleName + ":\n");
		out.println("Global Variables:");

		for(String globalDeclaration : this.globalDeclarations.keySet()) {

			SimpleNode.Type type = this.globalDeclarations.get(globalDeclaration);

			out.println(globalDeclaration + ", type: " + type);
		}

		for(SymbolTable.Pair<String,SimpleNode.Type> repeatedDeclaration : this.repeatedGlobalDeclarationsDiffType) {

			out.println("(Repeated Variable) " + repeatedDeclaration.key + " with type: " + repeatedDeclaration.value);

		}

		out.println("\nFunctions:");

		for(SymbolTable.Signature signature : this.functions.keySet()) {

			SymbolTable.Function function = this.functions.get(signature);

			out.println(function.signature.functionName + ":");

			out.println("Return type: " + function.returnType);

			out.println("Function arguments:");
			
			for(int i = 0; i < function.signature.arguments.size(); i++) {

				out.println("\tArgument " + function.signature.arguments.get(i) + ", of type " + function.signature.argumentTypes.get(i));

			}

			out.println("Function local variables:");

			for(String variable : function.localDeclarations.keySet()) {

				SimpleNode.Type type = function.localDeclarations.get(variable);
				out.println(variable + " with data type: " + type);

			}


			for(Pair<String,SimpleNode.Type> repeatedVariable : function.repeatedLocalDeclarationsDiffType) {

				out.println("(Repeated Variable) " + repeatedVariable.key + " with data type " + repeatedVariable.value);

			}

			out.println("Function function calls:");

			for(FunctionCall functionCall : function.functionCalls) {

				out.println(functionCall.signature.functionName);

				for(int i = 0; i < functionCall.signature.arguments.size(); i++) {

					out.println("\tArgument " + functionCall.signature.arguments.get(i) + ", of type " + functionCall.signature.argumentTypes.get(i));
	
				}

			}

			out.println("Null declarations for variables");

			for(Pair<String,String> nullDeclarationVatiable : function.nullDeclarationsVariables) {

				out.println("Left: " + nullDeclarationVatiable.key + " Right: " + nullDeclarationVatiable.value);

			}

			out.println("Null declarations for function calls");

			for(Pair<String,Signature> nullDeclarationsFunctionCall : function.nullDeclarationsFunctionCalls) {

				out.println("Left: " + nullDeclarationsFunctionCall.key + " Right: " + nullDeclarationsFunctionCall.value.functionName);

				out.println("With Parameters: ");

				for(int i = 0; i < nullDeclarationsFunctionCall.value.arguments.size(); i++) {

					out.println("\tArgument " + nullDeclarationsFunctionCall.value.arguments.get(i) + ", of type " + nullDeclarationsFunctionCall.value.argumentTypes.get(i));
	
				}


			}


		}

		for(SymbolTable.Signature signature : this.repeatedFunctions) {

			out.println("(Repeated function) " + signature.functionName + ":");

			for(int i = 0; i < signature.arguments.size(); i++) {

				out.println("\tArgument " + signature.arguments.get(i) + ", of type " + signature.argumentTypes.get(i));

			}

		}

		out.close();

		return outStream.toString();


	}


	

}