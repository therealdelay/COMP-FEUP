import java.io.BufferedWriter;

public class GenCode {
    
    public void gen(SymbolTable symbolTable) throws IOException {
        String str = "Hello";
        BufferedWriter writer = new BufferedWriter(new FileWriter(symbolTable.moduleName + ".j"));
        
        writer.write(".class public " + symbolTable.moduleName + "\n");

        for (Signature sign : symbolTable.functions) {
            Function function = symbolTable.functions.get(sign);

            writer.write(".method public static ");
            writer.write(sign.functionName + "(");

            if (sign.functionName.equals("main")) {

                writer.write("[Ljava/lang/String;");
            } else {
                String arguments = "";

                for (Pair<String, SimpleNode.Type> pair : sign.arguments) {
                    arguments += typeToStr(pair.value);
                }
                writer.write(arguments);
            }

            writer.write(")" + typeToStr(sign.returnType) + "\n");

        }

        writer.write(str);

        writer.close();
    }

    public String typeToStr(SimpleNode.Type type) {
        switch (type) {
        case INT:
            return "I";
            break;
        case ARRAY_INT:
            return "[I";
            break;
        case VOID:
            return "V";
            break;
        case STRING:
            return "Ljava/lang/String;";
            break;
        default:
            break;
        }
        return "";
    }

    // locals nº de argumentos da funcao + declaraçoes locais
    // stack nº max entre 2, nº de args das fucntions Calls
}