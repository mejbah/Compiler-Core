package edu.utsa.tl13;

import java.util.ArrayList;

/**
 * 
 * @author mejbah
 *
 */

public class Declarations implements Visitable {
	
	public ArrayList<DeclarationUnit> declarations;
	boolean hasError;
	public Declarations() {
		declarations = new ArrayList<DeclarationUnit>();
		hasError = false;
	}
	
	public void addDeclaration( DeclarationUnit d ) {
		declarations.add(d);
//		SymbolTable st = SymbolTable.getInstance();
//		if(st.isRedeclrations(d.getIdent()) ) {
//			hasError = true;
//		}
//		st.addSymbolInfo(d.getIdent(), d.getIdType());
	}
	
	public ArrayList<DeclarationUnit> getDeclarationList() {
		return declarations;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}
	
	public boolean typeOkay() {
		
		for( DeclarationUnit d : this.declarations ) {
			SymbolTable st = SymbolTable.getInstance();
			if(st.isRedeclrations(d.getIdent()) ) {
				hasError = true;
			}
			st.addSymbolInfo(d.getIdent(), d.getIdType());
		}
		if( hasError == false ) {
			return true;
			
		}
		else return false;
	}
}
