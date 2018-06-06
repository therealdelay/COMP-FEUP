package yal;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;

/**
 * Class that handles the code generation into Java bytecodes, by writing into a .j file
 * Uses the SymbolTable and AST.
 * The generated files are saved in bin/generatedFiles.
 */
public class Bytecodes{

	private static SymbolTable symbolTable;
	private static PrintWriter writer;
	private static ArrayList<String> register_variables;
	private static SymbolTable.Signature sign;
	private static SymbolTable.Function currentFunction;
	private static int current_loop = 0;
	private static int stack_counter = 0;
	private static int stack_max = 0;

	/**
	 * Begins the generation into Java Bytecodes.
	 * @param root Root of the tree
	 * @param st SymbolTable
	 */
	public static void generateJavaBytecodes(Node root, SymbolTable st) throws IOException {
		symbolTable = st;
		String dirName = "generatedFiles";
		File dir = new File(dirName);
		if(!dir.exists())
			dir.mkdir();
		
		String fileName =((SimpleNode) root).jjtGetValue() + ".j";
		File jFile = new File(dirName + "/" + fileName);
	    FileOutputStream jFileOS = new FileOutputStream(jFile);
	    writer = new PrintWriter(jFileOS);
	    
	    moduleJavaBytecodes(root);
	  
	}

	/**
	 * Analyses the root of the tree, which holds the module on the yal file
	 * @param root Root of the tree
	 */
	private static void moduleJavaBytecodes(Node root){

	    writer.println(".class public " + ((SimpleNode) root).jjtGetValue());
	    writer.println(".super java/lang/Object");

	    int numChildren = root.jjtGetNumChildren();

	    for(int i = 0; i < numChildren; i++) {

	        SimpleNode node = (SimpleNode) root.jjtGetChild(i);

	        int nodeType = node.getId();

	        switch (nodeType) {

	            case yal2jvmTreeConstants.JJTDECLARATION:
	                declarationJavaByteCodes(node);
	                break;

	            case yal2jvmTreeConstants.JJTFUNCTION:
	                functionJavaBytecodes(node);
	                break;

	            default:
	                break;

	        }
	    }   
	    clinitJavaBytecodes();

	    writer.close();

	}

	/**
	 * Writes the global declarations into the file
	 * @param declarationNode Node containing the declarations
	 */
	private static void declarationJavaByteCodes(SimpleNode declarationNode){

	    String declarationName = (String) declarationNode.jjtGetValue();

		SimpleNode.Type type = declarationNode.getDataType();

		if(type == null) return;

		String dataType = typeToBytecodes(type);
	    
	    writer.println(".field static " + declarationName + " " + dataType);

	}

	/**
	 * Analyses the functions from the module
	 * @param declarationNode Node containing the functions
	 */
	private static void functionJavaBytecodes(SimpleNode functionNode){

	    String functionName = (String) functionNode.jjtGetValue();
	    ArrayList<SimpleNode.Type> argumentTypes = new ArrayList<SimpleNode.Type>();

	    writer.print("\n.method public static ");

	    Node statementList = functionNode.jjtGetChild(0);
		Node argumentList;
		
		stack_counter = 0;
		stack_max = 0;
		register_variables = new ArrayList<String>();
		register_variables.add(null);

		if(functionNode.jjtGetNumChildren() == 2) {

			argumentList = statementList;
			statementList = functionNode.jjtGetChild(1);

			int numArguments = argumentList.jjtGetNumChildren();

			for(int i = 0; i < numArguments; i++) {

				SimpleNode argument = (SimpleNode) argumentList.jjtGetChild(i);

				String argumentName = (String)argument.jjtGetValue();
				register_variables.set(register_variables.indexOf(null), argumentName);
				register_variables.add(null);

				SimpleNode.Type argumentDataType = argument.getDataType();
				argumentTypes.add(argumentDataType);
			}

		} 
		
		else if(functionName.equals("main")){

			String argumentName = "args";
			register_variables.set(register_variables.indexOf(null), argumentName);
			register_variables.add(null);

		}

		sign = new SymbolTable.Signature(argumentTypes, functionName);
		currentFunction = symbolTable.functions.get(sign);

		writer.println(functionNameToBytecodes(currentFunction));
		
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
		PrintWriter writerStandBy = writer;
		writer = new PrintWriter(outBuffer);
		
		statementListJavaBytecodes((SimpleNode) statementList);
		
		writer.close();
		writer = writerStandBy;

		writer.println(".limit locals " + register_variables.indexOf(null));
		writer.println(".limit stack " + stack_max);
		writer.println();
		writerStandBy.println(outBuffer);


		String returnVar = currentFunction.returnVariable;
		if (returnVar != null){
			push_stack();		
			writer.println(loadVariable(returnVar));
			pop_stack();
			switch(currentFunction.returnType){
				case INT:
					writer.print("i");
					break;
				case ARRAY_INT:
					writer.print("a");
					break;
				default:
					break;
			}	
		}		
		
		writer.println("return");
		writer.println(".end method\n");
	}

	/**
	 * Parses the statement of the node
	 * @param statementNode Node containing the statement
	 */
	private static void statementJavaBytecodes(SimpleNode statementNode){

	    SimpleNode statementChild = (SimpleNode) statementNode.jjtGetChild(0);
	    switch (statementChild.getId()) {
	        case yal2jvmTreeConstants.JJTASSIGN:
				SimpleNode lhsNode = (SimpleNode) statementChild.jjtGetChild(0);
	            SimpleNode rhsNode = (SimpleNode) statementChild.jjtGetChild(1);
				assignJavaBytecodes(lhsNode, rhsNode);
	            break;
	        case yal2jvmTreeConstants.JJTCALL:
	            SimpleNode callNode = (SimpleNode) statementNode.jjtGetChild(0);
	            functionCallJavaBytecodes(callNode);
				break;
	        case yal2jvmTreeConstants.JJTIF:
	            SimpleNode ifNode = (SimpleNode) statementNode.jjtGetChild(0);
	            ifJavaBytecodes(ifNode);
	        	break;
			case yal2jvmTreeConstants.JJTWHILE:
	            SimpleNode whileNode = (SimpleNode) statementNode.jjtGetChild(0);
	            whileJavaBytecodes(whileNode);
	        	break;
			default:
	        break;
	    }
	}

	/**
	 * Handles the right hand side of an expression
	 * @param rhsNode Node containing the rhs
	 */
	private static void rhsJavaBytecodes(SimpleNode rhsNode){
	    SimpleNode rhs1stChild = (SimpleNode) rhsNode.jjtGetChild(0);
	    switch (rhs1stChild.getId()) {
	        case yal2jvmTreeConstants.JJTTERM:

				termJavaBytecodes(rhs1stChild);
				break;

			case yal2jvmTreeConstants.JJTARRAYSIZE: 
				// ARRAY SIZE DEF
				arraySizeJavaBytecodes(rhs1stChild);
				break;

	        default:
	        break;
	    }

	    if(rhsNode.jjtGetNumChildren() == 2){
	        SimpleNode term2 = (SimpleNode) rhsNode.jjtGetChild(1);
	        termJavaBytecodes(term2);
	        checkArithmeticJavaBytecodes(rhsNode);
		}
	}

	/**
	 * Writes the Term instructions into the file
	 * @param termNode Node containing the Term
	 */
	private static void termJavaBytecodes(SimpleNode termNode){

		String value = (String) termNode.jjtGetValue();
		String signal = termNode.jjtGetOperation();

	    if(termNode.jjtGetNumChildren() == 0){
	        if(termNode.getDataType() == SimpleNode.Type.INT){
				String secValue = (String) termNode.jjtGetSecValue();
				if (secValue != null) {
					push_stack();
					writer.println(loadVariable(value));
					writer.println("arraylength");
				}
				else{
					push_stack();
					int signalFixer = 1;
					if(signal != null && signal.equals("-")){
						signalFixer = -1;
					}
					writer.println(loadInteger(signalFixer*Integer.parseInt(value)));
				}
	        }
			else{ 
				push_stack();
				String varName = (String) termNode.jjtGetValue();
				writer.println(loadVariable(varName));

	        }
	    }
	    else{ 
	        SimpleNode termChildNode = (SimpleNode) termNode.jjtGetChild(0);

			switch (termChildNode.getId()) {
				case yal2jvmTreeConstants.JJTCALL:				
					functionCallJavaBytecodes(termChildNode);
					break;
				case yal2jvmTreeConstants.JJTINDEX:				
					arrayAccessJavaBytecodes(termChildNode, value);
					break;
			
				default:
					break;
			}
	    }
	}

	/**
	 * Handles Array Sizes
	 * @param arraySizeNode Node containing the array
	 */
	private static void arraySizeJavaBytecodes(SimpleNode arraySizeNode){

		push_stack();
		if(arraySizeNode.jjtGetNumChildren() == 0){
			String value = (String) arraySizeNode.jjtGetValue();
			writer.println(loadInteger(Integer.parseInt(value)));
		}
		else{
			SimpleNode scalarAccessNode = (SimpleNode) arraySizeNode.jjtGetChild(0);

			String value = (String) scalarAccessNode.jjtGetValue();
			writer.println(loadVariable(value));
		}

		writer.println("newarray int");
	}

	/**
	 * Writes the Assign instructions into the file
	 * @param lhsNode Node containing the LHS of the Term
	 * @param rhsNode Node containing the RHS of the Term
	 */
	private static void assignJavaBytecodes(SimpleNode lhsNode, SimpleNode rhsNode){
		String lhsID = (String) lhsNode.jjtGetValue();
		boolean exists = false;
		int register_index = register_variables.indexOf(lhsID);
		if (register_index == -1) {
			if(symbolTable.globalDeclarations.containsKey(lhsID)){
				exists = true;
			}
		}
		else{
			exists = true;
		}

		if(!exists){
			register_index = register_variables.indexOf(null);
			register_variables.add(null);
			register_variables.set(register_index, lhsID);
		}

		
		SimpleNode.Type assignType = symbolTable.getType(lhsID, currentFunction);
		String lhsBytecode = storeVariable(register_index, assignType);

		if(lhsNode.jjtGetNumChildren() > 0){
			SimpleNode indexNode = (SimpleNode) lhsNode.jjtGetChild(0);

			push_stack();
			writer.println(loadVariable(lhsID));
			push_stack();
			String value = (String) indexNode.jjtGetValue();
		
			if(indexNode.getDataType() == SimpleNode.Type.INT){
				writer.println(loadInteger(Integer.parseInt(value)));
			}
			else{
				writer.println(loadVariable(value));
			}
			rhsJavaBytecodes(rhsNode);
			pop_stack();			
			pop_stack();			
			pop_stack();			
			writer.println("iastore");
		}
		else{
			rhsJavaBytecodes(rhsNode);			
			pop_stack();
			writer.println(lhsBytecode);
		}
		writer.println();
	}

	/**
	 * Writes Function Calls instructions into the file
	 * @param callNode Node containing the call
	 */
	private static void functionCallJavaBytecodes(SimpleNode callNode){
		
		String functionName = (String) callNode.getAssignId();
	    String moduleName = (String) callNode.getAssignIdModule();
	    if(moduleName == null) moduleName = symbolTable.moduleName;
		
		ArrayList<SimpleNode.Type> argumentTypes = new ArrayList<SimpleNode.Type>();
		if(callNode.jjtGetNumChildren() > 0){

			SimpleNode argsListNode = (SimpleNode) callNode.jjtGetChild(0);			
			
			ArrayList<SymbolTable.Pair<String, SimpleNode.Type>> assignFunctionParameters = callNode.getAssignFunctionParameters();
			
			for (int i = 0; i < argsListNode.jjtGetNumChildren(); i++) {
				SimpleNode argNode = (SimpleNode) argsListNode.jjtGetChild(i);
	
				String argName = assignFunctionParameters.get(i).key;
				if(argName != null){
	
					SimpleNode.Type type = symbolTable.globalDeclarations.get(argName);
	
					if(type == null){
						SymbolTable.Function function = symbolTable.functions.get(sign);
						type = symbolTable.getType(argName, function);
					}
					argumentTypes.add(type);
					String varName = (String) argNode.jjtGetValue();
					push_stack();
					writer.println(loadVariable(varName));
					
				}
				else{
					SimpleNode.Type argType = assignFunctionParameters.get(i).value;
					argumentTypes.add(argType);
					push_stack();
					switch (argType) {
						case INT:
							writer.println(loadInteger(Integer.parseInt((String) argNode.jjtGetValue())));
							break;
						case STRING:
							writer.println(loadString((String) argNode.jjtGetValue()));				
							break;
						default:
							break;
					}
				}
			}
			for (SimpleNode.Type type : argumentTypes) {
				pop_stack();
			}
		}

	    SymbolTable.Signature funcCallSign = new SymbolTable.Signature(argumentTypes, functionName);
		SymbolTable.Function function = symbolTable.functions.get(funcCallSign);

		if(function == null){
			SimpleNode.Type returnType = SimpleNode.Type.INT;
			if(callNode.jjtGetParent().getId() == yal2jvmTreeConstants.JJTSTMT){
				
				returnType = SimpleNode.Type.VOID;
			}
			else{
				push_stack();				
			}

			writer.println("invokestatic " + moduleName + "/" + functionNameToBytecodes(functionName, argumentTypes, returnType) +"\n");
	    } 
	    else{
				
			if(function.returnType != SimpleNode.Type.VOID){
				push_stack();
			}
			
	        writer.println("invokestatic " + moduleName + "/" + functionNameToBytecodes(function) + "\n");
	    }

	}

	/**
	 * Writes If instructions into the file
	 * @param ifNode Node containing the if
	 */
	private static void ifJavaBytecodes(SimpleNode ifNode){
		int loop = current_loop;
		
		current_loop++;
		
	    SimpleNode exprTestNode = (SimpleNode) ifNode.jjtGetChild(0);
		SimpleNode statementList = (SimpleNode) ifNode.jjtGetChild(1);

		exprTestJavaByteCodes(exprTestNode, loop);
		
		statementListJavaBytecodes(statementList);

		if (ifNode.jjtGetNumChildren() == 3)
			elseJavaByteCodes( (SimpleNode) ifNode.jjtGetChild(2), loop);
		
		else {
			writer.println("loop" + loop + "_end:");
			writer.println();
		}
	}

	/**
	 * Writes While instructions into the file
	 * @param whileNode Node containing the while
	 */
	private static void whileJavaBytecodes(SimpleNode whileNode){
		int loop = current_loop;		
		current_loop++;
		writer.println("loop" + loop + ":");
		writer.println();
		SimpleNode exprTestNode = (SimpleNode) whileNode.jjtGetChild(0);
		SimpleNode statementList = (SimpleNode) whileNode.jjtGetChild(1);
		exprTestJavaByteCodes(exprTestNode, loop);

		statementListJavaBytecodes(statementList);
		writer.println("goto loop" + loop);
		writer.println();
		writer.println("loop" + loop + "_end:");
		writer.println();	   
	}

	/**
	 * Handles Expressions instructions for the file
	 * @param exprTestNode Node containing the Expression
	 * @param loop loop id of the expression
	 */
	private static void exprTestJavaByteCodes(SimpleNode exprTestNode, int loop){
		SimpleNode lhs = (SimpleNode) exprTestNode.jjtGetChild(0);
		String operation =  exprTestNode.jjtGetOperation();
		SimpleNode rhs = (SimpleNode) exprTestNode.jjtGetChild(1);

		String left = (String) lhs.jjtGetValue();
		push_stack();
		writer.println(loadVariable(left));

		rhsJavaBytecodes(rhs);
		
		pop_stack();
		pop_stack();
		switch (operation) {
			case "==":
				writer.print("if_icmpne");
				break;
			case "!=":
				writer.print("if_icmpeq");
				break;
			case ">=":
				writer.print("if_icmplt");
				break;
			case ">":
				writer.print("if_icmple");
				break;
			case "<=":
				writer.print("if_icmpgt");
				break;
			case "<":
				writer.print("if_icmpge");
				break;
			default:
				break;  
		}
		writer.println(" loop" + loop + "_end");
		writer.println();
	}

	/**
	 * Writes Else instructions into the file
	 * @param elseNode Node containing the else
	 */
	private static void elseJavaByteCodes(SimpleNode elseNode, int loop){
			writer.println();
			writer.println("goto loop" + loop + "_next");
			writer.println("loop" + loop + "_end:");
	   		statementListJavaBytecodes(elseNode);
			writer.println();
			writer.println("loop" + loop + "_next:");
	}

	/**
	 * Handles StatementLists
	 * @param statementList Node containing the statementList
	 */
	private static void statementListJavaBytecodes(SimpleNode statementList){

		int numStatements = statementList.jjtGetNumChildren();
		for(int i = 0; i < numStatements; i++) {
			SimpleNode statement = (SimpleNode) statementList.jjtGetChild(i);
			statementJavaBytecodes(statement);
		}

	}

	/**
	 * Handles Array Accesses
	 * @param indexNode Node containing the indexNode
	 * @param arrayId String representing the id of the array on the register_variables
	 */
	private static void arrayAccessJavaBytecodes(SimpleNode indexNode, String arrayId){
		push_stack();
		writer.println(loadVariable(arrayId));
		push_stack();
		String value = (String) indexNode.jjtGetValue();

		if(indexNode.getDataType() == SimpleNode.Type.INT){
			writer.println(loadInteger(Integer.parseInt(value)));
		}
		else{
			writer.println(loadVariable(value));
		}
		pop_stack();
		writer.println("iaload");
	}

	/**
	 * Converts the SimpleNode.Type into a String
	 * @param type Type to be converted
	 */
	private static String typeToBytecodes(SimpleNode.Type type) {
	    switch (type) {
	        case INT:
	            return "I";
	        case ARRAY_INT:
	            return "[I";
	        case VOID:
	            return "V";
	        case STRING:
	            return "Ljava/lang/String;";
	        default:
	            return "";
	    }
	}

	/**
	 * Verifies the arithmetic expression and writes it into the file
	 * @param node Node with the expression
	 */
	private static void checkArithmeticJavaBytecodes(SimpleNode node){
		pop_stack();
		pop_stack();
		push_stack();
		switch ((String)node.jjtGetValue()) {
			case "*":
				writer.println("imul");
				break;
			case "/":
				writer.println("idiv");
				break;
			case "+":
				writer.println("iadd");
				break;
			case "-":
				writer.println("isub");
				break;
			case "<<":
				writer.println("ishl");
				break;
			case ">>":
				writer.println("ishr");
				break;
			case ">>>":
				writer.println("iushl");
				break;
			case "&":
				writer.println("iand");
				break;
			case "|":
				writer.println("ior");
				break;
			case "^":
				writer.println("ixor");
				break;
			default:
				break;  
		}
	}

	/**
	 * Gets the function declaration
	 * @param functionName Name of the function
	 * @param argumentTypes Argument Types of the function
	 * @param returnType Return Type of the function
	 * @param String representing the declaration of a function
	 */
	public static String functionNameToBytecodes(String functionName, ArrayList<SimpleNode.Type> argumentTypes, SimpleNode.Type returnType){
		String result = functionName + "(";
		
	    if (functionName.equals("main")) result +=  "[Ljava/lang/String;";
	    else{
			for (SimpleNode.Type type : argumentTypes) {
				result += typeToBytecodes(type);
	        }
	    }
		
	    result += ")";
		
	    result += typeToBytecodes(returnType);
		
	    return result;
	}
	
	/**
	 * Gets the function declaration
	 * @param Function Function information
	 */
	private static String functionNameToBytecodes(SymbolTable.Function function){
		return functionNameToBytecodes(function.signature.functionName, function.signature.argumentTypes, function.returnType);
	}
	

	/**
	 * Handles integer value and converts it to Jasmin instruction
	 * @param value Value to be converted
	 */
	private static String loadInteger(Integer value){
		if(value >= 0 && value <= 5)	
			return "iconst_" + value;
	    else if(value >= -128 && value <= 127)
	        return "bipush " + value;
		else if(value >= -32768 && value <= 32767)
			return "sipush " + value;
		else
			return "ldc " + value;
	}

	/**
	 * Converts a string to Jasmin instruction
	 * @param value Value to be converted
	 */
	private static String loadString(String value){
		return "ldc " + value;
	}

	/**
	 * Writes clinit code
	 */
	private static void clinitJavaBytecodes(){
	    writer.println("method static public <clinit>()V");
	    writer.println(".limit stack 0");
	    writer.println(".limit locals 0");
	    writer.println("return");
	    writer.println(".end method ");

	}

	/**
	 * Stores a variable
	 * @param register_index index of the variable in the register_variables array
	 * @param type Type of the variable
	 */
	private static String storeVariable(int register_index, SimpleNode.Type type){
		if(type == SimpleNode.Type.ARRAY_INT){
			return astore(register_index);
		}
		else {
			return istore(register_index);
		}
	}

	/**
	 * Stores an int
	 * @param register_index index of the int 
	 */
	private static String istore(int register_index){
		return "istore" + (register_index > 3 ? " " : "_") + register_index;
	}

	/**
	 * Stores an array
	 * @param register_index index of the int 
	 */
	private static String astore(int register_index){
		return "astore" + (register_index > 3 ? " " : "_") + register_index;
	}

	/**
	 * Loads a variable
	 * @param variableName varable name
	 */
	private static String loadVariable(String variableName){
		
		int rIndex = register_variables.indexOf(variableName);
		if(rIndex == -1){
			return loadGlobalVariable(variableName);
		}
		else{
			return loadLocalVariable(rIndex, symbolTable.getType(variableName, currentFunction));
		}
	}

	/**
	 * Loads a local variable
	 * @param register_index index of the int 
	 * @param type Type of the variable
	 */
	private static String loadLocalVariable(int register_index, SimpleNode.Type type){
		switch (type) {
			case INT:
				return "iload" + (register_index > 3 ? " " : "_") + register_index;
			case ARRAY_INT:
				return "aload" + (register_index > 3 ? " " : "_") + register_index;
		}
		return null;
	}

	/**
	 * Loads a global variable
	 * @param variableName varable name
	 */
	private static String loadGlobalVariable(String variableName){
		System.out.println(variableName);
		String result = "getstatic " + symbolTable.moduleName + "/" + variableName + " ";
		result += typeToBytecodes(symbolTable.globalDeclarations.get(variableName));

		return result;
	}

	/**
	 * Pushes the stack counter
	 */
	private static void push_stack(){
		stack_counter++;
		if(stack_counter > stack_max)
			stack_max = stack_counter;
	}

	/**
	 * Pops the stack counter
	 */
	private static void pop_stack(){
		stack_counter--;
	}

}