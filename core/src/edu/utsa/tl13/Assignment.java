package edu.utsa.tl13;

public class Assignment extends Statement {
	Expression expr;
	String readInt;
	Token id;
	
	public Assignment( Expression expr, Token id ) {
		this.expr = expr;
		this.readInt = null;
		this.id = id;
	}
	
	public Assignment( String readInt, Token id ) {
		this.expr = null;
		this.readInt = readInt;
		this.id = id;
	}
	
	public Expression getExpr() {
		return expr;
	}

	public String getReadInt() {
		return readInt;
	}

	public Token getId() {
		return id;
	}
	
	

}
