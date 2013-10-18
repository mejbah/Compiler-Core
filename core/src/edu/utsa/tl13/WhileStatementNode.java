package edu.utsa.tl13;

public class WhileStatementNode extends StatementNode {

		ExpressionNode expr;
		StatementListNode statements;

		
		public WhileStatementNode( ExpressionNode e, StatementListNode s) {
			expr = e;
			statements = s;
			
			
		}


		public ExpressionNode getExpr() {
			return expr;
		}


		public StatementListNode getStatements() {
			return statements;
		}

		public void accept( Visitor visitor ) {
			visitor.visit(this);
		}



}
