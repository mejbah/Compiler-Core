package edu.utsa.tl13;

import java.util.HashMap;

public class SymbolTable {
	private static SymbolTable instance=null; // singleton class instance
	
	private HashMap<String, String> symtab;
	
	private SymbolTable() {
		symtab = new HashMap<String, String>();
	}
	
	public static SymbolTable getInstance() {
		if( instance == null ) {
			instance = new SymbolTable();
		}
		return instance;
		
	}
	
	public void addSymbolInfo( String symbol, String type ) {
		symtab.put(symbol, type);
	}
	
	public boolean isRedeclrations( String symbol ) {
		return symtab.containsKey(symbol); 
	}
	
	public String getType( String symbol ) {
		return symtab.get(symbol);
	}
}
