package edu.utsa.tl13;

public class FactorNode implements Visitable {
	ExpressionNode expr;
	String id;
	String id_type; // num or boollit or ident
	boolean type_ok;
	String dataType;
	
	public FactorNode( ExpressionNode expr) {
		this.expr = expr;
		this.id = null;
		//this.type_ok = true;
	
	}

	public FactorNode( String id, String id_type ) {
		this.expr = null;
		this.id = id;
		//this.type_ok = true;
		this.id_type = id_type;
	
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
	
	public String getId_Type() {
		return id_type;
	}

	public boolean typeOkay() {
		if( this.expr != null ) {
			this.dataType = expr.getDataType();
			return (this.type_ok && this.expr.typeOkay());
		}
		else { 
			//num
			if( this.id_type.equals("num")) {
				this.dataType = GlobalConstants.INT_TYPE;
				this.type_ok = true;
			}
			// boollit
			else if( this.id_type.equals("boollit")) {
				this.dataType = GlobalConstants.BOOL_TYPE;
				this.type_ok = true;
			}
			// ident
			else {
				//look up symtab
				SymbolTable st = SymbolTable.getInstance();
				this.dataType = st.getType(this.id);
				this.type_ok = true;
				
			}
			return this.type_ok;
		}
	}
}
