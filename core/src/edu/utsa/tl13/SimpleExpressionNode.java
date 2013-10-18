package edu.utsa.tl13;

public class SimpleExpressionNode implements Visitable {
	TermNode lhs;
	TermNode rhs;
	OperandNode op;
	
	

	public SimpleExpressionNode(TermNode lhs, TermNode rhs, OperandNode op) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.op = op;
	}



	public SimpleExpressionNode(TermNode lhs) {
		this.lhs = lhs;
		this.rhs = null;
		this.op = null;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}



	public TermNode getLhs() {
		return lhs;
	}



	public TermNode getRhs() {
		return rhs;
	}



	public OperandNode getOp() {
		return op;
	}
	
	
}
