package edu.utsa.tl13;

public class SimpleExpression implements Visitable {
	Term term;
	SimpleExpressionPart sPart;
	
	public SimpleExpression( Term t, SimpleExpressionPart part ) {
		term = t;
		sPart = part;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}

	public Term getTerm() {
		return term;
	}
	
	public SimpleExpressionPart getSExprPart() {
		return sPart;
	}
}
