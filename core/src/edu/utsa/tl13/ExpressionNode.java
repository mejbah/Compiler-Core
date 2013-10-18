package edu.utsa.tl13;

public class ExpressionNode {
	SimpleExpressionNode lhs;
	SimpleExpressionNode rhs;
	OperandNode op;
	
	public ExpressionNode( SimpleExpressionNode lhs ) {
		this.lhs = lhs;
		this.rhs = null;
		this.op = null;
	}

	public ExpressionNode(SimpleExpressionNode lhs, SimpleExpressionNode rhs,
			OperandNode op) {
		super();
		this.lhs = lhs;
		this.rhs = rhs;
		this.op = op;
	}

}
