package edu.utsa.tl13;

public class StatementNode implements Visitable {
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}
	

}
