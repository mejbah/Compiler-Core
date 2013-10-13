package edu.utsa.tl13;

public class Factor implements Visitable {
	Expression expression;
	Token token;
	public Factor() {
		expression = null;
	}
	public Factor( Expression e ) {
		expression = e;
		token = null;
	}
	
	public Factor( Token t ) {
		expression = null;
		token = t;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}
	public Expression getExpression() {
		return expression;
	}
	public Token getToken() {
		return token;
	}
}
