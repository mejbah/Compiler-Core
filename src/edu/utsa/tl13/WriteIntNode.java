package edu.utsa.tl13;

public class WriteIntNode extends StatementNode {
	
	ExpressionNode expr;
	boolean type_ok;
	
	public WriteIntNode(  ExpressionNode expr) {
		
		this.expr = expr;
	}

	public ExpressionNode getExpr() {
		return expr;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}
	
	public boolean typeOkay() {
		if( expr.typeOkay() && (expr.getDataType() == GlobalConstants.INT_TYPE)) {
			this.type_ok = true;
		}
		else {
			this.type_ok = false;
		}
		return this.type_ok;
	}

}
