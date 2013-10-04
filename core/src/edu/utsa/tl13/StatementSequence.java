package edu.utsa.tl13;

import java.util.ArrayList;

public class StatementSequence {
	
	public ArrayList<Statement> stmt;
	public StatementSequence() {
		stmt = new ArrayList<Statement>();
	}
		

	
	public void addStatement( Statement statement ) {
		stmt.add(statement);
	}

}
