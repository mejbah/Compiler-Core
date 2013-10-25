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
		if(this.identType == GlobalConstants.IDENT ) {
			// left side of assignment has to be "ident" type
			if( this.expr != null ) {
			// assignment
				if(expr.typeOkay()) {
					// ident type == expr type
					String id_dataType = SymbolTable.getInstance().getType(this.identNode);
					if(id_dataType.equals(this.expr.getDataType())) {
						this.type_ok = true;
					}
					else {
						this.type_ok = false;
					}
				}
				else
					this.type_ok = false;
			}
			// readInt
			else {
				this.type_ok = true;
			}
			
			
		}
		else {
			this.type_ok = false;
		}
		return this.type_ok;
	}
	 
}
