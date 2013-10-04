package edu.utsa.tl13;

public class Assignment extends Statement {
	Expression expr;
	String readInt;
	
	public Assignment( Expression expr ) {
		this.expr = expr;
		this.readInt = null;
	}
	
	public Assignment( String readInt ) {
		this.expr = null;
		this.readInt = readInt;
	}
	
	public Expression getExpr() {
		return expr;
	}

}
