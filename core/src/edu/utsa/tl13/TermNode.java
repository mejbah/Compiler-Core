package edu.utsa.tl13;

public class TermNode implements Visitable {
	FactorNode lhs;
	FactorNode rhs;
	OperandNode op;
	String dataType;
	Boolean type_ok;
	
	public TermNode( FactorNode lhs, FactorNode rhs, OperandNode op ) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.op = op;
		this.dataType = null;	
	}
	
	public TermNode( FactorNode lhs ) {
		this.lhs =lhs;
		this.rhs = null;
		this.op = null;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}

	public FactorNode getLhs() {
		return lhs;
	}

	public FactorNode getRhs() {
		return rhs;
	}

	public OperandNode getOp() {
		return op;
	}
	
	public String getDataType() {
		return dataType;
	}
	
	public boolean typeOkay() {
		if( this.rhs == null ) {
			if( this.lhs.typeOkay()) {
				this.dataType =  this.lhs.getDataType();
				this.type_ok = true;
			}
			else {
				this.type_ok = false;
			}
		}
		else {
			if(this.lhs.typeOkay() && this.rhs.typeOkay()) {
				if(lhs.getDataType().equals(GlobalConstants.INT_TYPE) && rhs.getDataType().equals(GlobalConstants.INT_TYPE)){
					this.dataType = GlobalConstants.INT_TYPE;
					this.type_ok = true;
				
				}
				else {
					this.type_ok =  false;
				}
			}
			else {
				this.type_ok = false;
			}
			
			
				
		}
		return this.type_ok;
	}
	
	
	
	

}
