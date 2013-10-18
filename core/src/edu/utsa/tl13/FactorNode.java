package edu.utsa.tl13;

public class FactorNode {
	ExpressionNode expr;
	String id;
	
	public FactorNode( ExpressionNode expr) {
		this.expr = expr;
		this.id = null;
	
	}

	public FactorNode( String id ) {
		this.expr = null;
		this.id = id;
	
	}
}
