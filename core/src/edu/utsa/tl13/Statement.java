package edu.utsa.tl13;

public class Statement implements Visitable {
	//nothing to do here??
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}

}
