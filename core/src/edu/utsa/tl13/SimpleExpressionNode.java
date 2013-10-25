package edu.utsa.tl13;

public class SimpleExpressionNode implements Visitable {
	TermNode lhs;
	TermNode rhs;
	OperandNode op;
	String dataType;
	boolean type_ok;

	public SimpleExpressionNode(TermNode lhs, TermNode rhs, OperandNode op) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.op = op;
	}



	public SimpleExpressionNode(TermNode lhs) {
		this.lhs = lhs;
		this.rhs = null;
		this.op = null;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}


	public String getDataType() {
		return dataType;
	}
	
	public TermNode getLhs() {
		return lhs;
	}



	public TermNode getRhs() {
		return rhs;
	}



	public OperandNode getOp() {
		return op;
	}
	
	public boolean typeOkay() {
		if( this.rhs == null) {
			if( this.lhs.typeOkay() ) {
				this.dataType = this.lhs.getDataType();
				this.type_ok = true;
			}
			else {
				this.type_ok = false;
			}
			return this.type_ok;
		}
		else {
			if(this.lhs.typeOkay() && this.rhs.typeOkay()) {
				if(lhs.getDataType().equals(GlobalConstants.INT_TYPE) && rhs.getDataType().equals(GlobalConstants.INT_TYPE)){
					this.dataType = GlobalConstants.INT_TYPE;
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
