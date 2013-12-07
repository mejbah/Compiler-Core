package edu.utsa.tl13;

public class IfStatementNode extends StatementNode implements Visitable {
	ExpressionNode expr;
	StatementListNode statements;
	StatementListNode elseStatements;
	
	boolean type_ok;
	String dataType;
	
	
	public IfStatementNode( ExpressionNode e, StatementListNode s, StatementListNode el) {
		expr = e;
		statements = s;
		elseStatements = el;
	}

	public IfStatementNode( ExpressionNode e, StatementListNode s) {
		expr = e;
		statements = s;
		elseStatements = null;
	}
	
	public ExpressionNode getExpr() {
		return expr;
	}

	public StatementListNode getStatements() {
		return statements;
	}

	public StatementListNode getElseStatements() {
		return elseStatements;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}
	
	public String getDataType() {
		return dataType;
	}
	
	public boolean typeOkay() {
		if( this.expr.typeOkay()) {
			if( this.expr.getDataType() == GlobalConstants.BOOL_TYPE ) {
				if(! this.statements.getStatementList().isEmpty())
					this.type_ok = this.statements.typeOkay();
				else {
					this.type_ok = true;
				}
				if(! this.elseStatements.getStatementList().isEmpty())
					this.type_ok = this.elseStatements.typeOkay();
				else {
					this.type_ok = true;
				}
			}
		}
		
		else {
			this.type_ok = false;
		}
		
		return this.type_ok;
	}

}
