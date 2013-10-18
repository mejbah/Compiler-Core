package edu.utsa.tl13;

public class FactorNode implements Visitable {
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

	public ExpressionNode getExpr() {
		return expr;
	}

	public String getId() {
		return id;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}
	
}
