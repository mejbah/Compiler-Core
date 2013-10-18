package edu.utsa.tl13;

public class SimpleExpressionNode{
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
	
}
