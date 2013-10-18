package edu.utsa.tl13;

public class OperandNode implements Visitable {
	String op;
	String type;
	
	public OperandNode( String  op, String type ) {
		this.op = op;
		this.type = type;
	
	}
	
	public String getOp() {
		return op;
	}

	public String getType() {
		return type;
	}

	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}

}
