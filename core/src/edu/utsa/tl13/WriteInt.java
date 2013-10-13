package edu.utsa.tl13;

public class WriteInt extends Statement {
	Expression expr;
	
	public WriteInt( Expression expression ) {
		expr = expression;
	}
	public Expression getExpr() {
		return expr;
	}
	

}
