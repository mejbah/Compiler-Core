package edu.utsa.tl13;

public class WhileStatement extends Statement{
	Expression expr;
	StatementSequence st;
	
	public WhileStatement( Expression e, StatementSequence s ) {
		expr = e;
		st = s;
		
	}
	
	Expression getExpression() {
		return expr;
	}
	
	StatementSequence getStatements() {
		return st;
	}

}
