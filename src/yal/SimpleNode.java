package yal;
/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 7.0 */
/* JavaCCOptions:MULTI=false,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
import java.util.*;

public class SimpleNode implements Node {

  public static enum Type {
    INT, ARRAY_INT, VOID, STRING
  };

  protected Node parent;
  protected Node[] children;
  protected int id;
  protected Object value;
  protected Object secvalue;
  protected String operation;
  protected Type dataType;
  protected String assignIdModule;
  protected String assignId;
  protected int current_loop;
  protected ArrayList<SymbolTable.Pair<String, Type>> assignFunctionParameters = new ArrayList<>();
  protected boolean isFunctionCall = false;
  protected yal2jvm parser;

  public SimpleNode(int i) {
    id = i;
  }

  public SimpleNode(yal2jvm p, int i) {
    this(i);
    parser = p;
  }

  public void jjtOpen() {
  }

  public void jjtClose() {
  }

  public void jjtSetParent(Node n) {
    parent = n;
  }

  public Node jjtGetParent() {
    return parent;
  }

  public void jjtAddChild(Node n, int i) {
    if (children == null) {
      children = new Node[i + 1];
    } else if (i >= children.length) {
      Node c[] = new Node[i + 1];
      System.arraycopy(children, 0, c, 0, children.length);
      children = c;
    }
    children[i] = n;
    children[i].jjtSetParent(this);

    SimpleNode newChild = (SimpleNode) children[i];

    if (newChild.getId() == yal2jvmTreeConstants.JJTRHS) {

      this.dataType = newChild.dataType;

      if (newChild.jjtGetNumChildren() > 0 && newChild.jjtGetChild(0).getId() == yal2jvmTreeConstants.JJTTERM) {

      
        SimpleNode term = (SimpleNode) newChild.children[0];

        this.dataType = term.getDataType();
        this.assignId = term.getAssignId();

        if (term.jjtGetNumChildren() > 0) {

          SimpleNode call = (SimpleNode) term.children[0];

          this.assignId = call.assignId;
          this.assignIdModule = call.assignIdModule;
          this.isFunctionCall = true;

          if (call.jjtGetNumChildren() > 0 && call.jjtGetChild(0).getId() == yal2jvmTreeConstants.JJTARGUMENTLIST) {

            SimpleNode argumentList = (SimpleNode) call.jjtGetChild(0);

            for (int j = 0; j < argumentList.jjtGetNumChildren(); j++) {

              SimpleNode arg = (SimpleNode) argumentList.jjtGetChild(j);
              SymbolTable.Pair<String, Type> argument = arg.assignFunctionParameters.get(0);
              this.addAssignFunctionParameter(argument.key, argument.value);

            }

          }

        }

      }

    }

    if(this.getId() == yal2jvmTreeConstants.JJTCALL) {

      if(this.jjtGetNumChildren() > 0 && this.jjtGetChild(0).getId() == yal2jvmTreeConstants.JJTARGUMENTLIST) {

        SimpleNode arguments = (SimpleNode) this.jjtGetChild(0);

        for(int l = 0; l < arguments.jjtGetNumChildren(); l++) {

          SimpleNode arg = (SimpleNode) arguments.jjtGetChild(l);
          SymbolTable.Pair<String, Type> argument = arg.assignFunctionParameters.get(0);
          this.addAssignFunctionParameter(argument.key, argument.value);

        }


      }

    }

    if(newChild.getId() == yal2jvmTreeConstants.JJTTERM && this.getId() == yal2jvmTreeConstants.JJTRHS){
      
      this.jjtSetType(newChild.getDataType());
    }

  }

  public Node jjtGetChild(int i) {
    return children[i];
  }

  public int jjtGetNumChildren() {
    return (children == null) ? 0 : children.length;
  }

  public void jjtSetValue(Object value) {
    this.value = value;
  }

  public Object jjtGetValue() {
    return value;
  }

  public void jjtSetSecValue(Object value) {
    this.secvalue = value;
  }

  public Object jjtGetSecValue() {
    return secvalue;
  }

  /* You can override these two methods in subclasses of SimpleNode to
     customize the way the node appears when the tree is dumped.  If
     your output uses more than one line you should override
     toString(String), otherwise overriding toString() is probably all
     you need to do. */

  public String toString() {
    return yal2jvmTreeConstants.jjtNodeName[id] + (value != null ? (" " + value) : "")
        + (secvalue != null ? (" " + secvalue) : "");
  }

  public String toString(String prefix) {
    return prefix + toString();
  }

  /* Override this method if you want to customize how the node dumps
     out its children. */

  public void dump(String prefix) {
    System.out.println(toString(prefix));
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        SimpleNode n = (SimpleNode) children[i];
        if (n != null) {
          n.dump(prefix + " ");
        }
      }
    }
  }

  public int getId() {
    return id;
  }

  public void jjtSetType(Type type) {
    this.dataType = type;
  }

   public void jjtSetOperation(String operation) {
    this.operation = operation;
  }

  public String jjtGetOperation(){
    return this.operation;
  }

  public void jjtSetIntType() {
    this.dataType = Type.INT;
  }

  public void jjtSetArrayType() {
    this.dataType = Type.ARRAY_INT;
  }

  public Type getDataType() {
    return this.dataType;
  }

  public void jjtSetAssignId(String assignId) {
    this.assignId = assignId;
  }

  public String getAssignId() {
    return this.assignId;
  }

  public ArrayList<SymbolTable.Pair<String, Type>> getAssignFunctionParameters() {
    return this.assignFunctionParameters;
  }

  public void addAssignFunctionParameter(String variable, Type parameter) {

    this.assignFunctionParameters.add(new SymbolTable.Pair<>(variable, parameter));
  }

  public void setAssignIdModule(String module) {
    this.assignIdModule = module;
  }

  public String getAssignIdModule() {
    return this.assignIdModule;
  }

  SimpleNode getAncestor(int level) {

    SimpleNode simpleNode = this;

    while (level > 0) {
      simpleNode = (SimpleNode) simpleNode.jjtGetParent();
      level--;
    }

    return simpleNode;

  }

}

/* JavaCC - OriginalChecksum=a18553b8ef6880d94e885b7367412b11 (do not edit this line) */
