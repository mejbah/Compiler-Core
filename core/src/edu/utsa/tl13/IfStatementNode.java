package edu.utsa.tl13;

public class IfStatementNode extends StatementNode{
	ExpressionNode expr;
	StatementListNode statements;
	StatementListNode elseStatements;
	
	public IfStatementNode( ExpressionNode e, StatementListNode s, StatementListNode el) {
		expr = e;
		statements = s;
		elseStatements = el;
	}

}
