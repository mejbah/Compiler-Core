package edu.utsa.tl13;

import java.util.ArrayList;

/**
 * 
 * @author mejbah
 *
 */

public class Declarations implements Visitable {
	
	public ArrayList<DeclarationUnit> declarations;
	public Declarations() {
		declarations = new ArrayList<DeclarationUnit>();
	}
	
	public void addDeclaration( DeclarationUnit d ) {
		declarations.add(d);
	}
	
	public ArrayList<DeclarationUnit> getDeclarationList() {
		return declarations;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}
	
	
}
