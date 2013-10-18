package edu.utsa.tl13;

public class ProgramNode {
	Declarations declarationList;
	StatementListNode statementList;
	public ProgramNode(Declarations declarationList,
			StatementListNode statementList) {
		
		this.declarationList = declarationList;
		this.statementList = statementList;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}

	public Declarations getDeclarationList() {
		return declarationList;
	}

	public StatementListNode getStatementList() {
		return statementList;
	}
	
	
	

}
