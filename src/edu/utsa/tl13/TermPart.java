package edu.utsa.tl13;

public class TermPart {
	Factor factor;
	Token op2;
	
	public TermPart() {
		factor = null;
	}
	public TermPart( Factor f, Token op ) {
		factor = f;
		op2 = op;
	}
	public Factor getFactor() {
		return factor;
	}
	public Token getOp2() {
		return op2;
	}
	
	
	

}
