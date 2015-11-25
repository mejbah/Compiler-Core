package edu.utsa.tl13;

public class ProgramNode {
	Declarations declarationList;
	StatementListNode statementList;

	boolean type_ok;
	
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
	
	public boolean typeOkay() {
		if(! this.declarationList.getDeclarationList().isEmpty()) {
			boolean decl_type = this.declarationList.typeOkay();
			boolean st_type = true;
			if(! this.statementList.getStatementList().isEmpty()) {
				st_type = this.getStatementList().typeOkay();
				
			}
			this.type_ok =  decl_type && st_type;
		}
		else {
			this.type_ok = true;
		}
		
		return this.type_ok;
	}
	

}
