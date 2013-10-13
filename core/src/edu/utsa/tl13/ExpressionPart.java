package edu.utsa.tl13;

public class ExpressionPart {
	SimpleExpression expr;
	Token op4;
	
	public ExpressionPart( SimpleExpression sExpr, Token op ) {
		expr = sExpr;
		op4 = op;
		
	}
	public ExpressionPart() {
		expr = null;
		op4 = null;
	}
	
	public Token getOp() {
		return op4;
	}
	
	public SimpleExpression getSimpleExpression() {
		return expr;
	}
}
