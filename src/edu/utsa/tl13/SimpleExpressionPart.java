package edu.utsa.tl13;

public class SimpleExpressionPart {
	Term term;
	Token op3;
	
	public Token getOp() {
		return op3;
	}

	public SimpleExpressionPart() {
		term = null;
	}
	
	public SimpleExpressionPart( Term t, Token op ) {
		term = t;
		op3 = op;
	}

	public Term getTerm() {
		return term;
	}
}
