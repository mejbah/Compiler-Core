package edu.utsa.tl13;

public class AssignmentNode extends StatementNode implements Visitable {
	String identNode;
	ExpressionNode expr;
	String readInt;
	
	public AssignmentNode( String identNode, ExpressionNode expr) {
		this.identNode = identNode;
		this.expr = expr;
		this.readInt = null;
	}

	public AssignmentNode( String identNode, String readInt) {
		this.identNode = identNode;
		this.expr = null;
		this.readInt = readInt;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}

	public String getIdentNode() {
		return identNode;
	}

	public ExpressionNode getExpr() {
		return expr;
	}

	public String getReadInt() {
		return readInt;
	}
	 
}
