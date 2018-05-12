

import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class Bytecodes{
  

	public static void generateJavaBytecodes(Node root, SymbolTable symbolTable) throws IOException {

		String fileName = ((SimpleNode) root).jjtGetValue() + ".j";
	    File jFile = new File(fileName);
	    FileOutputStream jFileOS = new FileOutputStream(jFile);
	    PrintWriter writer = new PrintWriter(jFileOS);
	    
	    moduleJavaBytecodes(root, writer, symbolTable);
	  
	}

	private static void moduleJavaBytecodes(Node root, PrintWriter writer, SymbolTable symbolTable){

	    writer.println(".class public " + ((SimpleNode) root).jjtGetValue());
	    writer.println(".super java/lang/Object");

	    int numChildren = root.jjtGetNumChildren();

	    for(int i = 0; i < numChildren; i++) {

	        SimpleNode node = (SimpleNode) root.jjtGetChild(i);

	        int nodeType = node.getId();

	        switch (nodeType) {

	            case yal2jvmTreeConstants.JJTDECLARATION:
	                declarationJavaByteCodes(node, writer, symbolTable);
	                break;

	            case yal2jvmTreeConstants.JJTFUNCTION:
	                functionJavaBytecodes(node, writer, symbolTable);
	                break;

	            default:
	                break;

	        }
	    }   
	    clinitJavaBytecodes(writer);

	    writer.close();

	}



	private static void declarationJavaByteCodes(SimpleNode declarationNode, PrintWriter writer, SymbolTable symbolTable){

	    String declarationName = (String) declarationNode.jjtGetValue();

	    String dataType = typeToBytecodes(declarationNode.getDataType());
	    
	    writer.println(".field static " + declarationName + " " + dataType);

	}

	private static void functionJavaBytecodes(SimpleNode functionNode, PrintWriter writer, SymbolTable symbolTable){


	    // locals nº de argumentos da funcao + declaraçoes locais
	    // stack nº max entre 2, nº de args das fucntions Calls

	    String functionName = (String) functionNode.jjtGetValue();
	    ArrayList<SimpleNode.Type> argumentTypes = new ArrayList();

	    writer.print("\n.method public static ");

	    Node statementList = functionNode.jjtGetChild(0);
	    Node argumentList;

	    // Locals stack
	    // calculateLimitLocals(function);
	    // calculateLimitStack(function);
	    int limitLocals = 10, limitStack = 10;

	    ArrayList<String> register_variables = new ArrayList();
	    for(int i=0; i<limitLocals; i++) register_variables.add(null);

	    // Arguments
	        if(functionNode.jjtGetNumChildren() == 2) {

	            argumentList = statementList;
	            statementList = functionNode.jjtGetChild(1);

	            int numArguments = argumentList.jjtGetNumChildren();

	            for(int i = 0; i < numArguments; i++) {

	                SimpleNode argument = (SimpleNode) argumentList.jjtGetChild(i);

	                String argumentName = (String)argument.jjtGetValue();
	                register_variables.set(register_variables.indexOf(null), argumentName);

	                SimpleNode.Type argumentDataType = argument.getDataType();
	                argumentTypes.add(argumentDataType);
	            }

	        }

	        SymbolTable.Signature sign = new SymbolTable.Signature(argumentTypes, functionName);
	        writer.println(functionToBytecodes(symbolTable.functions.get(sign)));

	        writer.println(".limit locals " + limitLocals);
	        writer.println(".limit stack " + limitStack);

	    // StmtList
	        int numStatements = statementList.jjtGetNumChildren();
	        for(int i = 0; i < numStatements; i++) {

	            SimpleNode statement = (SimpleNode) statementList.jjtGetChild(i);

	            statementJavaBytecodes(statement, writer, register_variables, symbolTable, sign);
	        }

	        switch(symbolTable.functions.get(sign).returnType){
	            case INT:
	                writer.print("i");
	            break;
	            case ARRAY_INT:
	                writer.print("a");
	            break;
	                default:
	            break;
	        }
	        writer.println("return");
	        writer.println(".end method\n");
	}

	private static void statementJavaBytecodes(SimpleNode statementNode, PrintWriter writer, ArrayList<String> register_variables, SymbolTable symbolTable, SymbolTable.Signature sign){

	    SimpleNode statementChild = (SimpleNode) statementNode.jjtGetChild(0);
	    switch (statementChild.getId()) {
	        case yal2jvmTreeConstants.JJTASSIGN:

	            int register_index = register_variables.indexOf(null);
	            register_variables.set(register_index, (String) ((SimpleNode) statementChild.jjtGetChild(0)).jjtGetValue());
	            String lhsBytecode = "istore_" + register_index;

	            SimpleNode rhsNode = (SimpleNode) statementChild.jjtGetChild(1);

	            rhsJavaBytecodes(rhsNode, writer, register_variables, symbolTable, sign);

	            writer.println(lhsBytecode);
	            break;
	        case yal2jvmTreeConstants.JJTCALL:
	            SimpleNode callNode = (SimpleNode) statementNode.jjtGetChild(0);
	            functionCallJavaBytecodes(callNode, writer, register_variables, symbolTable, sign);
	        case yal2jvmTreeConstants.JJTIF:
	            SimpleNode ifNode = (SimpleNode) statementNode.jjtGetChild(0);
	            ifJavaBytecodes(ifNode, writer, register_variables, symbolTable, sign);
	        case yal2jvmTreeConstants.JJTWHILE:
	            SimpleNode whileNode = (SimpleNode) statementNode.jjtGetChild(0);
	            whileJavaBytecodes(whileNode, writer, register_variables, symbolTable, sign);
	        default:
	        break;
	    }
	}

	private static void rhsJavaBytecodes(SimpleNode rhsNode, PrintWriter writer, ArrayList<String> register_variables, SymbolTable symbolTable, SymbolTable.Signature sign){
	    SimpleNode rhs1stChild = (SimpleNode) rhsNode.jjtGetChild(0);

	    switch (rhs1stChild.getId()) {
	        case yal2jvmTreeConstants.JJTTERM:

	        termJavaBytecodes(rhs1stChild, writer, register_variables, symbolTable, sign);
	        break;

	        default:
	    // ARRAY SIZE DEF
	        break;
	    }

	    if(rhsNode.jjtGetNumChildren() == 2){
	        SimpleNode term2 = (SimpleNode) rhsNode.jjtGetChild(1);
	        termJavaBytecodes(term2, writer, register_variables, symbolTable, sign);
	        checkArithmeticJavaBytecodes(rhsNode, writer);
	    }
	}

	private static void checkArithmeticJavaBytecodes(SimpleNode node, PrintWriter writer){
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

	private static void termJavaBytecodes(SimpleNode termNode, PrintWriter writer, ArrayList<String> register_variables, SymbolTable symbolTable, SymbolTable.Signature sign){

	    if(termNode.jjtGetNumChildren() == 0){
	        if(termNode.getDataType() == SimpleNode.Type.INT){

	            if((Integer) termNode.jjtGetValue()>5)
	                writer.println("bipush " + (String) termNode.jjtGetValue());
	            else
	                writer.println("iconst_" + (String) termNode.jjtGetValue());
	        }
	        else{ // ID
	            int rIndex = register_variables.indexOf((String) termNode.jjtGetValue());
	            writer.println("iload_" + rIndex);
	        }
	    }
	    else{ //function call
	        SimpleNode callNode = (SimpleNode) termNode.jjtGetChild(0);

	        functionCallJavaBytecodes(callNode, writer, register_variables, symbolTable, sign);
	    }
	}

	private static void functionCallJavaBytecodes(SimpleNode callNode, PrintWriter writer, ArrayList<String> register_variables, SymbolTable symbolTable, SymbolTable.Signature sign){

	    System.out.println("callNode.id: " + callNode.getId());

	    String functionName = (String) callNode.getAssignId();
	    String moduleName = (String) callNode.getAssignIdModule();
	    if(moduleName == null) moduleName = symbolTable.moduleName;
	    
	    
	    SimpleNode argsListNode = (SimpleNode) callNode.jjtGetChild(0);
	    
	    ArrayList<SimpleNode.Type> argumentTypes = new ArrayList();
	    
	    ArrayList<SymbolTable.Pair<String, SimpleNode.Type>> assignFunctionParameters = callNode.getAssignFunctionParameters();
	    
	    System.out.println("functionName: " + moduleName + "/" + functionName);
	    for (int i = 0; i < argsListNode.jjtGetNumChildren(); i++) {
	        SimpleNode argNode = (SimpleNode) argsListNode.jjtGetChild(i);

	        String argName = assignFunctionParameters.get(i).key;
	        if(argName != null){

	            SimpleNode.Type type = symbolTable.globalDeclarations.get(argName);

	            if(type == null){
	                SymbolTable.Function function = symbolTable.functions.get(sign);
	                System.out.println("argName: " + argName);
	                type = function.localDeclarations.get(argName);
	                System.out.println("Type: " + type);
	            }
	            argumentTypes.add(type);
	            int rIndex = register_variables.indexOf((String) argNode.jjtGetValue());
	            writer.println("iload_" + rIndex);
	        }
	        else{
	            argumentTypes.add(assignFunctionParameters.get(i).value);
	            writer.println(loadIntegerToBytecodes(Integer.parseInt((String)((SimpleNode)argNode).jjtGetValue())));
	        }
	    }

	    for (SimpleNode.Type type : argumentTypes) {
	        System.out.println("argType: " + type);
	    }
	    SymbolTable.Signature funcCallSign = new SymbolTable.Signature(argumentTypes, functionName);
	    SymbolTable.Function function = symbolTable.functions.get(funcCallSign);
	    if(function == null){
	        System.out.println("NULL FUNCTION");
	    } 
	    else{

	        writer.println("invokestatic " + moduleName + "/" + functionToBytecodes(function) + "\n");
	    }

	}


	private static void ifJavaBytecodes(SimpleNode rhsNode, PrintWriter writer, ArrayList<String> register_variables, SymbolTable symbolTable, SymbolTable.Signature sign){
	   
	}

	private static void whileJavaBytecodes(SimpleNode rhsNode, PrintWriter writer, ArrayList<String> register_variables, SymbolTable symbolTable, SymbolTable.Signature sign){
	   
	}

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

	private static String functionToBytecodes(SymbolTable.Function function){
	    String result = function.signature.functionName + "(";

	    if (function.signature.functionName.equals("main")) result +=  "[Ljava/lang/String;";
	    else{
	        ArrayList<SimpleNode.Type> argumentTypes = function.signature.argumentTypes;
	        for (SimpleNode.Type type : argumentTypes) {
	            result += typeToBytecodes(type);
	        }
	    }

	    result += ")";

	    // Return type
	    result += typeToBytecodes(function.returnType);

	    return result;
	}

	private static String loadIntegerToBytecodes(Integer value){

	    if(value > 5) 
	        return "bipush " + value;
	    else 
	        return "iconst_" + value;   
	}

	private static void clinitJavaBytecodes(PrintWriter writer){
	    writer.println("method static public <clinit>()V");
	    writer.println(".limit stack 0");
	    writer.println(".limit locals 0");
	    writer.println("return");
	    writer.println(".end method ");

	}

}