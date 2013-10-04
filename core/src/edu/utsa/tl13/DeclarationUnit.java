package edu.utsa.tl13;

public class DeclarationUnit {
	TypeRule type;
	String ident;
	public DeclarationUnit( TypeRule type, String ident ) {
		this.type = type;
		this.ident = ident;
	}
	
	public TypeRule getType() {
		return type;
	}
	public String getIdent() {
		return ident;
	}
	
	
	

}
