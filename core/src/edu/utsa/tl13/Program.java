package edu.utsa.tl13;

public class Program implements Visitable {
	Declarations declarations;
	StatementSequence statements;
	
	public Program( Declarations d, StatementSequence s ) {
		declarations = d;
		statements = s;
	}
	
    public void accept(Visitor visitor) {
    	visitor.visit(this);
    }
    
    public Declarations getDeclarations() {
    	return declarations;
    }
    public StatementSequence getStatements() {
    	return statements;
    }
	
}