package edu.utsa.tl13;

public class FactorNode implements Visitable {
	ExpressionNode expr;
	String id;
	boolean type_ok;
	String dataType;
	
	public FactorNode( ExpressionNode expr) {
		this.expr = expr;
		this.id = null;
		this.type_ok = true;
	
	}

	public FactorNode( String id ) {
		this.expr = null;
		this.id = id;
		this.type_ok = true;
	
	}

	public ExpressionNode getExpr() {
		return expr;
	}

	public String getId() {
		return id;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}
	
	
	public String getDataType() {
		return dataType;
	}

	public boolean typeOkay() {
		if( this.expr != null ) {
			this.dataType = expr.getDataType();
			return (this.type_ok && this.expr.typeOkay());
		}
		else { //look up symtab
			SymbolTable st = SymbolTable.getInstance();
			this.dataType = st.getType(this.id);
			this.type_ok = true;
			return this.type_ok;
		}
	}
}
