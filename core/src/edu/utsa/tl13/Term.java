package edu.utsa.tl13;

public class Term implements Visitable{
	Factor factor;
	TermPart tPart;
	
	public Term( Factor f, TermPart part ) {
		factor = f;
		tPart = part;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}

	public Factor getFactor() {
		return factor;
	}

	public TermPart gettPart() {
		return tPart;
	}

}
