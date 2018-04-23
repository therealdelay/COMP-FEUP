/*import java.util.ArrayList;

class SymbolTable{

	static public enum Type={INT,ARRAY_INT};

	public ArrayList<Pair<String,Type>> globalDeclarations = new ArrayList<>();
	public ArrayList<Function> functions = new ArrayList<>();


	static public class Pair<K,V>{
		public K key;
		public V value;
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
}*/