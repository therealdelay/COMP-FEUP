package yal;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import javax.lang.model.util.ElementScanner6;

import yal.SimpleNode;

/**
 * Class that creates a Symbol Table
 */
class SymbolTable {

	public String moduleName;

	public int semanticErrors = 0;

	public HashMap<String, SimpleNode.Type> globalDeclarations = new HashMap<>();
	public HashMap<Signature, Function> functions = new HashMap<>();

	public ArrayList<Signature> repeatedFunctions = new ArrayList<>();
	public ArrayList<Pair<String, SimpleNode.Type>> repeatedGlobalDeclarationsDiffType = new ArrayList<>();

	/**
	 * Class that represents a pair of generic variables.
	 */
	public static class Pair<K, V> {

		public K key;
		public V value;

		/**
		 * Constructs a Pair
		 * @param key Left side of the pair
		 * @param value Right side of the pair
		 */
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

	/**
	* Checks the function call and returns it
	* @param functionName Name of the function
	* @param moduleName Name of the module
	* @param functionCallParameters Parameters of the function call
	* @param processingFunction ProcessingFunction information
	* @return Returns a new FunctionCall
	*/
	public FunctionCall checkGoodFunctionCall(String functionName, String moduleName,
			ArrayList<Pair<String, SimpleNode.Type>> functionCallParameters, Function processingFunction) {

			for(Pair<String,SimpleNode.Type> par : functionCallParameters) {

			if(par.value == null) {

				par.value = getType(par.key, processingFunction);

			}

		}

		SymbolTable.Signature signature = new SymbolTable.Signature(functionName, functionCallParameters);

		if (moduleName != null) {

			return new FunctionCall(signature, moduleName, true, "OK", SimpleNode.Type.INT);

		}


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


		if (numberOfNotInitializedParameters > 0) {

			return new FunctionCall(signature, moduleName, false, errorFunctionCall, null);

		}

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

		else if(calledFunction.signature.functionName.equals("main")) {

			return new FunctionCall(signature, moduleName, false, "It's not possible to call main function", null);

		}

		return new FunctionCall(signature, moduleName, true, "OK", calledFunction.returnType);

	}

	/**
	 * Class that represents a FunctionCall
	 */
	public static class FunctionCall {

		public Signature signature;
		public String module;
		public String error;
		public boolean ok = true;

		public SimpleNode.Type funcionCallReturnType; // return type da função que chama, null no caso de ser uma chamada inválida

		/**
		 * Constructs a FunctionCall
		 * @param signature Signature of the function
		 * @param module Module name
		 * @param ok Boolean that indicates if functionCall is okay
		 * @param error Possible errors for the functionCall
		 * @param callReturnType Return Type
		 */
		public FunctionCall(Signature signature, String module, boolean ok, String error,
				SimpleNode.Type callReturnType) {
			this.signature = signature;
			this.module = module;
			this.ok = ok;
			this.error = error;
			this.funcionCallReturnType = callReturnType;
		}

		/**
		 * Sets error
		 * @param error Message containing the error
		 */
		public void setErrorCall(String error) {
			this.error = error;
			this.ok = false;
		}

	}

	/**
	 * Class that represents a Signature
	 */
	public static class Signature {

		public String functionName;
		public ArrayList<String> arguments = new ArrayList<>();
		public ArrayList<SimpleNode.Type> argumentTypes = new ArrayList<>();

		/**
		 * Constructs a Signature with an ArrayList of pairs representing the arguments
		 * @param functionName Name of the function
		 * @param arguments Arguments of the function
		 */
		public Signature(String functionName, ArrayList<Pair<String, SimpleNode.Type>> arguments) {

			this.functionName = functionName;

			for (Pair<String, SimpleNode.Type> pair : arguments) {

				this.arguments.add(pair.key);
				this.argumentTypes.add(pair.value);

			}

		}

		/**
		 * Constructs a Signature with an ArrayList of Types representing the arguments types
		 * @param argumentTypes Arguments types of the function
		 * @param functionName Name of the function
		 */
		public Signature(ArrayList<SimpleNode.Type> argumentTypes, String functionName) {

			this.functionName = functionName;

			for (SimpleNode.Type type : argumentTypes) {

				this.arguments.add("");
				this.argumentTypes.add(type);

			}

		}

		/**
		 * Constructs a Signature without arguments
		 * @param functionName Name of the function
		 */
		public Signature(String functionName) {
			this.functionName = functionName;
			this.arguments = new ArrayList<>();
		}

		/**
		 * Adds an argument type
		 * @param argName Argument name
		 * @param type Argument type
		 */
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

	/**
	 * Class that represents a Function
	 */
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
		public ArrayList<String> errors = new ArrayList<>();

		public ArrayList<Pair<String, String>> nullDeclarationsVariables = new ArrayList<>();
		public ArrayList<Pair<String, Signature>> nullDeclarationsFunctionCalls = new ArrayList<>();

		/**
		 * Constructs a Function
		 * @param signature Signature of the function
		 * @param type Return type
		 */
		public Function(Signature signature, SimpleNode.Type type) {
			this.signature = signature;
			this.returnType = type;
		}

		/**
		 * Returns the type of the function
		 * @param variableName Name of the variable
		 * @param globalDeclarations Global declarations(if the return is a global variable)
		 * @return Returns the type
		 */
		public SimpleNode.Type getType(String variableName, HashMap<String, SimpleNode.Type> globalDeclarations) {

			SimpleNode.Type type = this.localDeclarations.get(variableName);

			if (type == null) {

				return globalDeclarations.get(variableName);

			}

			return type;

		}

		/**
		 * Adds a local declaration
		 * @param key Key
		 * @param value Value of the variable
		 * @param globalDeclarations Global declarations
		 */
		public boolean addLocalDeclaration(String key, SimpleNode.Type value, HashMap<String, SimpleNode.Type> globalDeclarations) {

			boolean isGlobal = globalDeclarations.containsKey(key);

			if (isGlobal) {
				
				if(globalDeclarations.get(key) != value  && globalDeclarations.get(key) != null) {

					if(globalDeclarations.get(key) != SimpleNode.Type.ARRAY_INT && value != SimpleNode.Type.INT) {
						this.repeatedLocalDeclarationsDiffType.add(new Pair<>(key, value));
					}

				}
				return false;

			} 

			boolean alreadyLocal = this.localDeclarations.containsKey(key);
			boolean isParameter = this.signature.arguments.contains(key);
			
			if (!alreadyLocal && !isParameter) {
				localDeclarations.put(key, value);
				return true;
			}

			else {

				if(alreadyLocal) {

					if (this.localDeclarations.get(key) != value && localDeclarations.get(key) != null) {

						if(this.localDeclarations.get(key) != SimpleNode.Type.ARRAY_INT && value != SimpleNode.Type.INT)
							this.repeatedLocalDeclarationsDiffType.add(new Pair<>(key, value));

					}


				}

				else if(isParameter) {

					SimpleNode.Type type = this.signature.argumentTypes.get(this.signature.arguments.indexOf(key));

					if (type != value && type != null && type != SimpleNode.Type.ARRAY_INT && value != SimpleNode.Type.INT) {

						this.repeatedLocalDeclarationsDiffType.add(new Pair<>(key, value));

					}


				}

			}
						
			return false;
		}

		/**
		* Adds a function call 
		*/
		public void addFunctionCall(FunctionCall functionCall) {
			this.functionCalls.add(functionCall);
		}

		@Override
		public boolean equals(Object object) {

			Function f2 = (Function) object;

			return this.signature.equals(f2.signature);

		}

	}

	/**
	* Constructs a SymbolTable
	* @param moduleName Name of the module
	*/
	public SymbolTable(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	* Adds a global declaration
	* @param key key
	* @param value value
	*/
	public boolean addGlobalDeclaration(String key, SimpleNode.Type value) {

		boolean exists = this.globalDeclarations.containsKey(key);

		if (!exists) {

			this.globalDeclarations.put(key, value);
			return true;

		}

		else if (this.globalDeclarations.get(key) != value && this.globalDeclarations.get(key) != null) {

			if(this.globalDeclarations.get(key) != SimpleNode.Type.ARRAY_INT && value != SimpleNode.Type.INT)
				this.repeatedGlobalDeclarationsDiffType.add(new Pair<>(key, value));
		}

		return false;

	}

	/**
	* Adds a function
	* @param Function function to be added
	*/
	public boolean addFunction(Function function) {

		Function exists = this.functions.get(function.signature);

		if (exists == null) {

			this.functions.put(function.signature, function);
			return true;

		}

		this.repeatedFunctions.add(function.signature);
		return false;

	}

	/**
	* Returns the type of the function
	* @param variableName Name of the variable
	* @param function Function information
	* @return Returns the type
	*/
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
			this.semanticErrors++;

		}

		out.println("\nFunctions:");

		for (SymbolTable.Signature signature : this.functions.keySet()) {

			SymbolTable.Function function = this.functions.get(signature);

			out.println("Function " + function.signature.functionName + ":\n");

			if(function.returnVariable != null )
				out.println("\tReturn:\n " + "\t\t" + function.returnVariable + " of type: " + function.returnType);
			else
				out.println("\tReturn:\n\t\t void");

			
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
				else{
					function.errors.add("Semantic Error: Call NOT ok: " + functionCall.error);
				}

				for (int i = 0; i < functionCall.signature.arguments.size(); i++) {

					if (functionCall.ok) {
						SimpleNode.Type type = functionCall.signature.argumentTypes.get(i);
						out.println(
								"\t\t\tArgument Name " + functionCall.signature.arguments.get(i) + ", of type " + type);
					}
				}

				out.println("\t\tFunction return type: " + functionCall.funcionCallReturnType);

			}

			out.println("\tFunction errors:");

			for (Pair<String, SimpleNode.Type> repeatedVariable : function.repeatedLocalDeclarationsDiffType) {

				out.println("\t\tSemantic Error: (Repeated Variable) " + repeatedVariable.key + " with data type "
						+ repeatedVariable.value);
				this.semanticErrors++;

			}

			
			for(String error: function.errors) {

				out.println("\t\t" + error);
				this.semanticErrors++;

			}

			out.println();

		}

		for (SymbolTable.Signature signature : this.repeatedFunctions) {

			out.println("Semantic Error: (Repeated function) " + signature.functionName + ":");
			this.semanticErrors++;

			for (int i = 0; i < signature.arguments.size(); i++) {

				out.println("\tArgument " + signature.arguments.get(i) + ", of type " + signature.argumentTypes.get(i));

			}

		}

		out.close();

		return outStream.toString();

	}

}