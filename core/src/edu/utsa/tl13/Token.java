package edu.utsa.tl13;

public class Token {
	private String token_word;
	private String token_type;
	
	public Token(String word, String type ) {
		token_word = word;
		token_type = type;
	}
	
	//TODO: write set methods
	
	public String getWord() {
		return token_word;
	}
	
	public String getType() {
		return token_type;
	}
	@Override
	public String toString() {
		return token_type;
	}

}