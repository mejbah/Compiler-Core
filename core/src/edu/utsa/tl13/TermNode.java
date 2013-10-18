package edu.utsa.tl13;

public class TermNode {
	FactorNode lhs;
	FactorNode rhs;
	OperandNode op;
	
	public TermNode( FactorNode lhs, FactorNode rhs, OperandNode op ) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.op = op;
	
	}
	
	public TermNode( FactorNode lhs ) {
		this.lhs =lhs;
		this.rhs = null;
		this.op = null;
	}

}
