package edu.utsa.tl13;

public class TypeRule implements Visitable{
	String type;
	public TypeRule( String t ) {
		type =  t;
	}
	
	public String getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return type;
	} 
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}
}
