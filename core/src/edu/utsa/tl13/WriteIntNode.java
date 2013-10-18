package edu.utsa.tl13;

public class WriteIntNode extends StatementNode {
	
	ExpressionNode expr;
	
	public WriteIntNode(  ExpressionNode expr) {
		
		this.expr = expr;
	}

	public ExpressionNode getExpr() {
		return expr;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}

}
