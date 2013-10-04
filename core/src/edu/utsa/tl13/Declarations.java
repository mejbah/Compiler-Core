package edu.utsa.tl13;

import java.util.ArrayList;

/**
 * 
 * @author mejbah
 *
 */

public class Declarations {
	
	public ArrayList<DeclarationUnit> declarations;
	public Declarations() {
		declarations = new ArrayList<DeclarationUnit>();
	}
	
	public void addDeclaration( DeclarationUnit d ) {
		declarations.add(d);
	}
	
	
	
	
}
