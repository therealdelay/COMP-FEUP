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
	
}
