package edu.utsa.tl13;

public class AssignmentNode extends StatementNode {
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
}
