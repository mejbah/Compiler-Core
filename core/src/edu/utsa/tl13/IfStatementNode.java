package edu.utsa.tl13;

public class IfStatementNode extends StatementNode implements Visitable {
	ExpressionNode expr;
	StatementListNode statements;
	StatementListNode elseStatements;
	
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

}
