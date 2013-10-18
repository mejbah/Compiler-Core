package edu.utsa.tl13;

public class WhileStatementNode extends StatementNode {

		ExpressionNode expr;
		StatementListNode statements;

		
		public WhileStatementNode( ExpressionNode e, StatementListNode s) {
			expr = e;
			statements = s;
			
		}




}
