package edu.utsa.tl13;

public class AssignmentNode extends StatementNode implements Visitable {
	String identNode;
	ExpressionNode expr;
	String readInt;
	String identType;
	boolean type_ok;
	
	public AssignmentNode( String identNode, String identType,ExpressionNode expr) {
		this.identNode = identNode;
		this.expr = expr;
		this.readInt = null;
		this.identType = identType;
	}

	public AssignmentNode( String identNode, String identType, String readInt) {
		this.identNode = identNode;
		this.expr = null;
		this.readInt = readInt;
		this.identType = identType;
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
	
	public boolean typeOkay() {
		if( (this.identType == GlobalConstants.BOOL_TYPE) || (this.identType == GlobalConstants.INT_TYPE) ) {
			// left has to var type
			if( this.expr != null ) {
				this.type_ok = expr.typeOkay();
			}
			else {
				this.type_ok = true;
			}
			
			return type_ok;
		}
		else
			return false;
	}
	 
}
