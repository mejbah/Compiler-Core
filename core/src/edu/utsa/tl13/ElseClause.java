package edu.utsa.tl13;

public class ElseClause implements Visitable {
	StatementSequence statements;

	public ElseClause( StatementSequence st ) {
		statements = st;
	}
	public ElseClause( ) {
		statements = null;
	}
	
	public StatementSequence getStatements() {
		return statements;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}
}
