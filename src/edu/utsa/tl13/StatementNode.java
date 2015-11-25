package edu.utsa.tl13;

public class StatementNode implements Visitable {
	
//	boolean type_ok;
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}
/*	
	public boolean typeOkay() {
		
	}
*/	

}
