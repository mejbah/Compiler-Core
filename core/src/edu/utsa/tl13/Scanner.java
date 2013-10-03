package edu.utsa.tl13;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Scanner {
	private ArrayList<Token> tokenList;
	private String inputFileName;
	
	public Scanner( String fileName ) {
		inputFileName = fileName;
		tokenList = new ArrayList<Token>();
	}
	
	public void  startScanner() {
		// populate MicroSyntax table
		MicroSyntax.initLexicalElements();
		// Read lines from input file
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
			String line = null;
			while((line = reader.readLine()) != null ) {
				updateTokenList( line );
			}
			reader.close();
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	void updateTokenList( String line ) {
		//TODO : comment % replace with whitespace 
		line = line.trim(); // remove leading whitespace
		String delims = "[ ]+"; // tokens are separated by one or more space
		String[] words = line.split(delims);
		 
		 for( int i=0; i<words.length; i++)
		 {
			 //check valid tokens
			Token token = MicroSyntax.getValidToken(words[i]);
			if(token != null) {
				tokenList.add(token);
			}
			else {
				// add error message
				System.err.println("Invalid symbol!");
			}
			
		
		 }
		
	}

	ArrayList<Token> getTokenList() {
		return tokenList;
	}
	

}