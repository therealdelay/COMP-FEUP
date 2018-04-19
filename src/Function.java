import java.util.*;

public class Function extends SimpleNode{

	protected ArrayList<SimpleNode> params;
	protected ArrayList<SimpleNode> variables;
	protected ArrayList<SimpleNode> cond_statements;
	protected ArrayList<SimpleNode> calls;
	protected SimpleNode ret;

	public Function(int id){
		super(id);
		this.params = new ArrayList<SimpleNode>();
		this.variables = new ArrayList<SimpleNode>();
		this.cond_statements = new ArrayList<SimpleNode>();
		this.calls = new ArrayList<SimpleNode>();
	}

	ArrayList<SimpleNode> getParams(){
		return this.params;
	}

	ArrayList<SimpleNode> getVariables(){
		return this.variables;
	}

	ArrayList<SimpleNode> getCondStatements(){
		return this.cond_statements;
	}

	ArrayList<SimpleNode> getCalls(){
		return this.calls;
	}

	SimpleNode getReturn(){
		return this.ret;
	}

	void addParams(SimpleNode param){
		this.params.add(param);
	}

	void addVariables(SimpleNode variable){
		this.variables.add(variable);
	}

	void addCondStatements(SimpleNode cond_statement){
		this.cond_statements.add(cond_statement);
	}

	void addCalls(SimpleNode call){
		this.calls.add(call);
	}

	void addReturn(SimpleNode ret){
		this.ret = ret;
	}

}
