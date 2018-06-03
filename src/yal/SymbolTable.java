package yal;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import javax.lang.model.util.ElementScanner6;

// import SimpleNode.Type;

class SymbolTable {

	public String moduleName;

	public HashMap<String, SimpleNode.Type> globalDeclarations = new HashMap<>();
	public HashMap<Signature, Function> functions = new HashMap<>();

	public ArrayList<Signature> repeatedFunctions = new ArrayList<>();
	public ArrayList<Pair<String, SimpleNode.Type>> repeatedGlobalDeclarationsDiffType = new ArrayList<>();

	public static class Pair<K, V> {

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

	public FunctionCall checkGoodFunctionCall(String functionName, String moduleName,
			ArrayList<Pair<String, SimpleNode.Type>> functionCallParameters, Function processingFunction) {

			for(Pair<String,SimpleNode.Type> par : functionCallParameters) {

			if(par.value == null) {

				par.value = getType(par.key, processingFunction);

			}

		}

		SymbolTable.Signature signature = new SymbolTable.Signature(functionName, functionCallParameters);
		
		//if function is from another module OK

		if (moduleName != null) {

			return new FunctionCall(signature, moduleName, true, "OK", SimpleNode.Type.INT);

		}

		//Check if all parameters are initialized

		String errorFunctionCall = "";
		int numberOfNotInitializedParameters = 0;

		for (SymbolTable.Pair<String, SimpleNode.Type> argument : functionCallParameters) {

			if (argument.value == null) {

				argument.value = this.getType(argument.key, processingFunction);
				if (argument.value == null) {
					errorFunctionCall += "Parameter " + argument.key + " not initialized";
					numberOfNotInitializedParameters++;
				}

			}

		}

		//Parameters not initialized

		if (numberOfNotInitializedParameters > 0) {

			return new FunctionCall(signature, moduleName, false, errorFunctionCall, null);

		}

		// check if function exists
		Function calledFunction = this.functions.get(signature);

		
		if ((calledFunction == null) || (calledFunction.functionIsOk == false)) {

			String error = functionName + "(";

			for (int i = 0; i < signature.argumentTypes.size(); i++) {

				error += signature.argumentTypes.get(i);

				if (i < signature.argumentTypes.size() - 1)
					error += ", ";

			}

			error += ") does not exist or is invalid!";

			return new FunctionCall(signature, moduleName, false, error, null);

		}

		return new FunctionCall(signature, moduleName, true, "OK", calledFunction.returnType);

	}

	public static class FunctionCall {

		public Signature signature;
		public String module;
		public String error;
		public boolean ok = true;

		public SimpleNode.Type funcionCallReturnType; // return type da função que chama, null no caso de ser uma chamada inválida

		public FunctionCall(Signature signature, String module, boolean ok, String error,
				SimpleNode.Type callReturnType) {
			this.signature = signature;
			this.module = module;
			this.ok = ok;
			this.error = error;
			this.funcionCallReturnType = callReturnType;
		}

		public void setErrorCall(String error) {
			this.error = error;
			this.ok = false;
		}

	}

	public static class Signature {

		public String functionName; //nome da funcao
		public ArrayList<String> arguments = new ArrayList<>();
		public ArrayList<SimpleNode.Type> argumentTypes = new ArrayList<>();

		public Signature(String functionName, ArrayList<Pair<String, SimpleNode.Type>> arguments) {

			this.functionName = functionName;

			for (Pair<String, SimpleNode.Type> pair : arguments) {

				this.arguments.add(pair.key);
				this.argumentTypes.add(pair.value);

			}

		}

		public Signature(ArrayList<SimpleNode.Type> argumentTypes, String functionName) {

			this.functionName = functionName;

			for (SimpleNode.Type type : argumentTypes) {

				this.arguments.add("");
				this.argumentTypes.add(type);

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

			if (!this.functionName.equals(s2.functionName))
				return false;
			if (this.arguments.size() != s2.arguments.size())
				return false;
			for (int i = 0; i < this.arguments.size(); i++)
				if (this.argumentTypes.get(i) != s2.argumentTypes.get(i))
					return false;

			return true;

		}

		@Override
		public int hashCode() {

			return Objects.hash(this.functionName, this.argumentTypes);
		}

	}

	public static class Function {

		public Signature signature;
		public HashMap<String, SimpleNode.Type> localDeclarations = new HashMap<>();
		public ArrayList<Pair<String, SimpleNode.Type>> repeatedLocalDeclarationsDiffType = new ArrayList<>();
		public ArrayList<FunctionCall> functionCalls = new ArrayList<>();
		public SimpleNode.Type returnType; //tipo de retorno
		public String returnVariable = null;
		public String returnVariableError = null;
		public String argumentsError = null;
		public boolean functionIsOk = true;

		public ArrayList<Pair<String, String>> nullDeclarationsVariables = new ArrayList<>();
		public ArrayList<Pair<String, Signature>> nullDeclarationsFunctionCalls = new ArrayList<>();

		public Function(Signature signature, SimpleNode.Type type) {
			this.signature = signature;
			this.returnType = type;
		}

		public SimpleNode.Type getType(String variableName, HashMap<String, SimpleNode.Type> globalDeclarations) {

			SimpleNode.Type type = this.localDeclarations.get(variableName);

			if (type == null) {

				return globalDeclarations.get(variableName);

			}

			return type;

		}

		public boolean addLocalDeclaration(String key, SimpleNode.Type value,
				HashMap<String, SimpleNode.Type> globalDeclarations) {

			SimpleNode.Type isGlobal = globalDeclarations.get(key);

			if (isGlobal != null) { // lhs é variável global
				if (value != isGlobal)
					this.repeatedLocalDeclarationsDiffType.add(new Pair(key, value));
				return true;
			} else { //lhs é variável local
				SimpleNode.Type alreadyLocal = this.localDeclarations.get(key);
				if (alreadyLocal == null) {
					localDeclarations.put(key, value);
					return true;
				}
				if (alreadyLocal != value) {
					this.repeatedLocalDeclarationsDiffType.add(new Pair(key, value));
					return false;
				}
				return true;
			}
		}

		public void addFunctionCall(FunctionCall functionCall) {
			this.functionCalls.add(functionCall);
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

		if (exists == null) {

			this.globalDeclarations.put(key, value);
			return true;

		}

		if (exists != value) {
			this.repeatedGlobalDeclarationsDiffType.add(new Pair(key, value));
		}

		return false;

	}

	public boolean addFunction(Function function) {

		Function exists = this.functions.get(function.signature);

		if (exists == null) {

			this.functions.put(function.signature, function);
			return true;

		}

		this.repeatedFunctions.add(function.signature);
		return false;

	}

	public SimpleNode.Type getType(String variableName, Function function) {

		SimpleNode.Type result = this.globalDeclarations.get(variableName);
		
		if (result == null) {

			int index = function.signature.arguments.indexOf(variableName);

			if(index == -1)
				result = function.localDeclarations.get(variableName);
			else
				result = function.signature.argumentTypes.get(index);

		}
			
			

		return result;

	}

	@Override
	public String toString() {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		PrintWriter out = new PrintWriter(outStream);

		out.println("Module " + this.moduleName + "\n");
		out.println("Global Variables:");

		for (String globalDeclaration : this.globalDeclarations.keySet()) {

			SimpleNode.Type type = this.globalDeclarations.get(globalDeclaration);

			out.println("\t" + globalDeclaration + ", type: " + type);
		}

		for (SymbolTable.Pair<String, SimpleNode.Type> repeatedDeclaration : this.repeatedGlobalDeclarationsDiffType) {

			out.println("Semantic Error: (Repeated Variable) " + repeatedDeclaration.key + " with type: "
					+ repeatedDeclaration.value);

		}

		out.println("\nFunctions:");

		for (SymbolTable.Signature signature : this.functions.keySet()) {

			SymbolTable.Function function = this.functions.get(signature);

			out.println("Function " + function.signature.functionName + ":");

			out.println("\tReturn type: " + function.returnType);

			if (function.returnVariableError != null)
				out.println("\t\t" + function.returnVariableError);

			if (function.argumentsError != null)
				out.println("\t\t" + function.argumentsError);

			out.println();
			out.println("\tFunction arguments:");

			for (int i = 0; i < function.signature.arguments.size(); i++) {

				out.println("\t\tArgument " + function.signature.arguments.get(i) + ", of type "
						+ function.signature.argumentTypes.get(i));

			}

			out.println();
			out.println("\tFunction local variables:");

			for (String variable : function.localDeclarations.keySet()) {

				SimpleNode.Type type = function.localDeclarations.get(variable);
				out.println("\t\t" + variable + " with data type: " + type);

			}

			for (Pair<String, SimpleNode.Type> repeatedVariable : function.repeatedLocalDeclarationsDiffType) {

				out.println("\t\tSemantic Error: (Repeated Variable) " + repeatedVariable.key + " with data type "
						+ repeatedVariable.value);

			}

			out.println();
			out.println("\tFunction function calls:");

			boolean first = true;
			for (FunctionCall functionCall : function.functionCalls) {
				if (!first)
					out.println();
				first = false;
				out.println("\t\t" + functionCall.signature.functionName);

				out.print("\t\tModule: ");
				if (functionCall.module != null)
					out.println(functionCall.module);
				else
					out.println(this.moduleName);

				if (functionCall.ok)
					out.println("\t\tCall is ok");
				else
					out.println("\t\tSemantic Error: Call NOT ok: " + functionCall.error);

				for (int i = 0; i < functionCall.signature.arguments.size(); i++) {

					if (functionCall.ok) {
						SimpleNode.Type type = functionCall.signature.argumentTypes.get(i);
						out.println(
								"\t\t\tArgument Name " + functionCall.signature.arguments.get(i) + ", of type " + type);
					}
				}

				out.println("\t\tFunction return type: " + functionCall.funcionCallReturnType);

			}
			out.println();

		}

		for (SymbolTable.Signature signature : this.repeatedFunctions) {

			out.println("Semantic Error: (Repeated function) " + signature.functionName + ":");

			for (int i = 0; i < signature.arguments.size(); i++) {

				out.println("\tArgument " + signature.arguments.get(i) + ", of type " + signature.argumentTypes.get(i));

			}

		}

		out.close();

		return outStream.toString();

	}

}