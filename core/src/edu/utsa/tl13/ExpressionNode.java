package edu.utsa.tl13;

public class ExpressionNode implements Visitable {
	SimpleExpressionNode lhs;
	SimpleExpressionNode rhs;
	OperandNode op;
	String dataType;
	boolean type_ok;

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

	public SimpleExpressionNode getLhs() {
		return lhs;
	}

	public SimpleExpressionNode getRhs() {
		return rhs;
	}

	public OperandNode getOp() {
		return op;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}
	
	public String getDataType() {
		return dataType;
	}
	
	public boolean typeOkay() {
		if( this.rhs == null) {
			if(this.lhs.typeOkay()) {
				this.type_ok = true;
				this.dataType = this.lhs.getDataType();
			}
			else {
				this.type_ok = false;
			}
			return this.type_ok;
		}
		else {
			if(this.lhs.typeOkay() && this.rhs.typeOkay()) {
				if(lhs.getDataType().equals(GlobalConstants.INT_TYPE) && rhs.getDataType().equals(GlobalConstants.INT_TYPE)){
					this.dataType = GlobalConstants.BOOL_TYPE;
					this.type_ok = true;
				}
				
			}
			else {
				this.type_ok = false;
			}
			
			return this.type_ok;
		}
	}

	
	

}
