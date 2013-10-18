package edu.utsa.tl13;

public class TermNode implements Visitable {
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
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}

	public FactorNode getLhs() {
		return lhs;
	}

	public FactorNode getRhs() {
		return rhs;
	}

	public OperandNode getOp() {
		return op;
	}
	
	
	
	

}
