package edu.utsa.tl13;

public class IfStatement extends Statement{
	Expression expr;
	StatementSequence statements;
	ElseClause elseClause;
	
	
	public IfStatement( Expression e, StatementSequence st, ElseClause el ) {
		expr = e;
		statements = st;
		elseClause = el;
		
		
	}
	
	public Expression getExpression() {
		return expr;
	}
	
	public StatementSequence getStatements() {
		return statements;
	}
	
	public ElseClause getElseClause() {
		return elseClause;
	}

}
