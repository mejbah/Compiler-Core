package edu.utsa.tl13;

import java.util.ArrayList;

public class StatementSequence implements Visitable {
	
	public ArrayList<Statement> stmt;
	public StatementSequence() {
		stmt = new ArrayList<Statement>();
	}
		

	
	public void addStatement( Statement statement ) {
		stmt.add(statement);
	}
	
	public ArrayList<Statement> getStatements() {
		return stmt;
	}
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}

}
