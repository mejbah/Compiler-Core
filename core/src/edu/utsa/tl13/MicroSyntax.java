package edu.utsa.tl13;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MicroSyntax {
	public static HashMap<String,String> keyWords;
	public static HashMap<String,String> opSymbols;
	
	public static void initLexicalElements() {
		keyWords = new HashMap<String, String>();
		opSymbols = new HashMap<String, String>();
		
		keyWords.put("if", "IF");
		keyWords.put("then", "THEN");
		keyWords.put("else", "ELSE");
		keyWords.put("begin", "BEGIN");
		keyWords.put("end", "END");
		keyWords.put("while", "WHILE");
		keyWords.put("do", "DO");
		keyWords.put("program", "PROGRAM");
		keyWords.put("var", "VAR");
		keyWords.put("as", "AS");
		keyWords.put("int", "INT");
		keyWords.put("bool", "BOOL");
		//TODO: add/put  all the keywords in hashmap
		opSymbols.put("(", "LP");
		opSymbols.put(")", "RP");
		opSymbols.put(":=", "ASGN");
		opSymbols.put(";", "SC");
		opSymbols.put("*", "OP2");
		opSymbols.put("div", "OP2");
		opSymbols.put("mod", "OP2");
		opSymbols.put("+", "OP3");
		opSymbols.put("-", "OP3");
		opSymbols.put("=", "OP4");
		opSymbols.put("!=", "OP4");
		opSymbols.put(">", "OP4");
		opSymbols.put(">=", "OP4");
		opSymbols.put("<", "OP4");
		opSymbols.put("<=", "OP4");
		opSymbols.put("writeInt", "WRITEINT");
		opSymbols.put("readInt", "readInt");
	}
	
	public static Token isKeyWord( String word ) {
		String token = keyWords.get(word);
		if( token != null )
			return new Token( word, token);
		else 
			return null;
	}
	
	public static Token isOpSymbol( String word ) {
		String token = opSymbols.get(word);
		if( token != null )
			return new Token( word, token);
		else 
			return null;
	}
	
	public static Token isNumLit( String word ) {
		Pattern regex = Pattern.compile("[1-9][0-9]*|0");
		Matcher matcher = regex.matcher(word);
		if(matcher.matches()) {
			return new Token(word, "num");
		}
		else
			return null;
	}
	
	
	public static Token isBoolLit( String word ) {
		Pattern regex = Pattern.compile("true|false");
		Matcher matcher = regex.matcher(word);
		if(matcher.matches()) {
			return new Token(word, "boollit");
		}
		else
			return null;
	}
	
	public static Token isIdentLit( String word ) {
		Pattern regex = Pattern.compile("[A-Z][A-Z0-9]*");
		Matcher matcher = regex.matcher(word);
		if(matcher.matches()) {
			return new Token(word, "ident");
		}
		else
			return null;
	}
	
	public static Token getValidToken( String word ) {
	
		Token token = isKeyWord(word);
		if( token != null )
			return token;
		token = isOpSymbol(word);
		if( token != null )
			return token;
		token = isNumLit(word);
		if( token != null )
			return token;
		
		token = isIdentLit(word);
		if( token != null )
			return token;
		
		token = isBoolLit(word);
		if( token != null )
			return token;
		
		return null;
	}
}


