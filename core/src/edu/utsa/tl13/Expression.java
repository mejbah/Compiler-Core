package edu.utsa.tl13;

public class Expression implements Visitable {
	SimpleExpression sExpr1;
	ExpressionPart partExpr;
	
	public Expression( SimpleExpression s1, ExpressionPart part ) {
		sExpr1 = s1;
		partExpr = part;		
		
	}
	
	public SimpleExpression getSimpleExpression() {
		return sExpr1;
	}
	
	public ExpressionPart getExprPart() {
		return partExpr;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}
}