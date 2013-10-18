package edu.utsa.tl13;

public class DeclarationUnit {
	TypeRule type;
	String ident;
	String id_type; // added type field for AST
	
	public DeclarationUnit( TypeRule type, String ident ) {
		this.type = type;
		this.ident = ident;
		this.id_type = type.getType();
	}
	
	public TypeRule getType() {
		return type;
	}
	public String getIdent() {
		return ident;
	}
	public String getIdType() {
		return id_type;
	}
	
	
	

}
