package edu.utsa.tl13;

public class ASTVisitor implements Visitor {

	String textToWrite = "digraph asTree {" + "\nordering=out; \nnode [shape = box, style = filled];\n";
	int nodeSerial = 0;
	int currentParentNo = 0;
	String labelText = "label=";
	String colorText = ",fillcolor=\"/x11/white\"";
	String shapeText = ",shape=box";
	
	int addNewNode( String label ) {
		nodeSerial++;
		String node = "n" + nodeSerial + " ["  + labelText + "\"" + label + "\"" + colorText + shapeText + " ]" +"\n";
		textToWrite = textToWrite + node;
		
		return nodeSerial;
	}
	/**
	 * 
	 * @param label : String containing label text 
	 * @param color : String containing color info ( Select one from Global Constants)
	 * @return
	 */
	int addNewNode( String label, String color ) {
		nodeSerial++;
		String node = "n" + nodeSerial + " ["  + labelText + "\"" + label + "\"" + color + shapeText + " ]" +"\n";
		textToWrite = textToWrite + node;
		
		return nodeSerial;
	}
	
	public String getTextToWrite() {
		textToWrite = textToWrite + "\n }";
		return textToWrite;
	}
	
	void addEdge( int parentNodeNo, int childNodeNo ) {
		String edge = "n" + parentNodeNo + " -> n" + childNodeNo + "\n"; 
		textToWrite = textToWrite + edge;
	}
	
	@Override
	public void visit(Program p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Declarations ds) {
		int parent = currentParentNo;
		for( DeclarationUnit d : ds.getDeclarationList()) {
			int child_1 = addNewNode("decl: " + d.getIdent(), GlobalConstants.COLOR_TEXT_IDENT);
			addEdge(parent , child_1);
			int type = addNewNode(d.getIdType(), GlobalConstants.COLOR_TEXT_TYPE);
			addEdge(child_1, type);
		}
		
	}

	@Override
	public void visit(TypeRule t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(StatementSequence s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Statement s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Expression e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ElseClause el) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SimpleExpression e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Term t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Factor t) {
		// TODO Auto-generated method stub
		
	}

	String getNodeColor( boolean type_okay ) {
		return type_okay  ? GlobalConstants.COLOR_TEXT_NO_ERROR : GlobalConstants.COLOR_TEXT_ERROR;
	}
	
	@Override
	public void visit(ProgramNode p) {
		
		int parent = addNewNode(new String("program"),  getNodeColor(p.type_ok));
        
		if(!p.getDeclarationList().getDeclarationList().isEmpty()) {
			//declarations is not empty
			currentParentNo = parent;
			Declarations ds = p.getDeclarationList();
	        
			int child_1 = addNewNode( new String("decl list"), getNodeColor(!ds.hasError));
	        addEdge(currentParentNo , child_1);
	        
	        
	        currentParentNo = child_1;
	        ds.accept(this);
	        currentParentNo = parent;
	        
			
		}
		StatementListNode stmtList = p.getStatementList();
		
        int child_2 = addNewNode(new String("stmt list"), getNodeColor(stmtList.type_ok));
        addEdge(currentParentNo , child_2);
        
        currentParentNo = child_2;
        stmtList.accept(this);
		
	}

	@Override
	public void visit(StatementListNode s) {
		
		for( StatementNode stmt : s.getStatementList() ) {
			stmt.accept(this);
		}
	}

	@Override
	public void visit(StatementNode s) {
		int parent = currentParentNo;
		
		if( s instanceof IfStatementNode ) {
			int child = addNewNode("If", getNodeColor(((IfStatementNode) s).type_ok));
			addEdge(parent, child);
			
			currentParentNo = child;
			ExpressionNode expr = ((IfStatementNode) s).getExpr();
			expr.accept(this);
			
			currentParentNo = child;
			StatementListNode ifstmts = ((IfStatementNode) s).getStatements();
		    ifstmts.accept(this);
			
		    if(((IfStatementNode) s).getElseStatements() != null) {
		    	StatementListNode elsestmts = ((IfStatementNode) s).getElseStatements();
		    	
		    	int child_1 = addNewNode("stmt list", getNodeColor(elsestmts.type_ok));
		    	
		    	addEdge(child , child_1);
		    	
		    	currentParentNo = child_1;
		    	
		    	elsestmts.accept(this);
		    }
		}
		
		else if(s instanceof AssignmentNode ) {
			
			
			if( ((AssignmentNode) s).getReadInt() != null ) {
				int child = addNewNode(" := readInt ", getNodeColor(((AssignmentNode) s).type_ok));
				addEdge(parent, child);
				
				int child_1 = addNewNode(((AssignmentNode) s).getIdentNode(), GlobalConstants.COLOR_TEXT_IDENT);
				addEdge(child, child_1);
			
			}
			else {
				int child = addNewNode(" := ", getNodeColor(((AssignmentNode) s).type_ok));
				addEdge(parent, child);
				
				int child_1 = addNewNode(((AssignmentNode) s).getIdentNode(), GlobalConstants.COLOR_TEXT_IDENT);
				addEdge(child, child_1);
				
				ExpressionNode expr = ((AssignmentNode) s).getExpr();
				currentParentNo = child;
				expr.accept(this);
			}
			
			
		}
		
		else if(s instanceof WhileStatementNode ) {
			int child = addNewNode(" While ", getNodeColor(((WhileStatementNode) s).type_ok));
			addEdge(parent, child);
			
			ExpressionNode expr = ((WhileStatementNode) s).getExpr();
			currentParentNo = child;
			expr.accept(this);
			
			
			if(! ((WhileStatementNode) s).getStatements().getStatementList().isEmpty() ) {
				
				StatementListNode stmts = ((WhileStatementNode) s).getStatements();
				int child_1 = addNewNode("stmt list", getNodeColor(stmts.type_ok));
		    	addEdge(child , child_1);
		    	
		    	currentParentNo = child_1;
				stmts.accept(this);
			}
			
		}
		
		else if( s instanceof WriteIntNode ) {
			int child = addNewNode(" WriteInt ", getNodeColor(((WriteIntNode) s).type_ok));
			addEdge(parent, child);
			
			ExpressionNode expr = ((WriteIntNode) s).getExpr();
			currentParentNo = child;
			expr.accept(this);
		}
		else {
			
		}
		
		currentParentNo = parent;
		
	}


	@Override
	public void visit(ExpressionNode ex) {
		int parent = currentParentNo;
		
		if( ex.getRhs() == null ) {
			SimpleExpressionNode simplExpr = ex.getLhs();
			currentParentNo = parent;
			simplExpr.accept(this);
			parent = currentParentNo;
		}
		else {
			
			int child = addNewNode(ex.getOp().op, getNodeColor(ex.type_ok)); // add operand of expression as node
			addEdge(parent, child);
			
			currentParentNo = child;
			SimpleExpressionNode simplExpr1 = ex.getLhs();
			simplExpr1.accept(this);
			
			currentParentNo = child;
			SimpleExpressionNode simplExpr2 = ex.getRhs();
			simplExpr2.accept(this);
			
		}
		
		currentParentNo = parent;
		
		
	}

	@Override
	public void visit(SimpleExpressionNode sex) {
		int parent = currentParentNo;
		
		if( sex.getRhs() == null ) {
			TermNode lhs = sex.getLhs();
			
			lhs.accept(this);
			
		}
		else {
			
			int child = addNewNode(sex.getOp().op, getNodeColor(sex.type_ok)); // add operand of expression as node
			addEdge(parent, child);
			
			currentParentNo = child;
			TermNode lhs = sex.getLhs();
			lhs.accept(this);
			
			currentParentNo = child;
			TermNode rhs = sex.getRhs();
			rhs.accept(this);
			
		}

		currentParentNo = parent;
	}

	@Override
	public void visit(TermNode tn) {
		
		int parent = currentParentNo;
		
		if( tn.getRhs() == null ) {
		
			FactorNode lhs = tn.getLhs();
			lhs.accept(this);
		
		}
		else {
			
			int child = addNewNode(tn.getOp().op, getNodeColor(tn.type_ok)); // add operand of expression as node
			addEdge(parent, child);
			
			currentParentNo = child;
			FactorNode lhs = tn.getLhs();
			lhs.accept(this);
			
			currentParentNo = child;
			FactorNode rhs = tn.getRhs();
			rhs.accept(this);
			
		}

		currentParentNo = parent;
	}

	@Override
	public void visit(FactorNode f) {
		int parent = currentParentNo;
		
		if( f.getExpr() == null ) {
			int child = addNewNode(f.getId(), GlobalConstants.COLOR_TEXT_IDENT);
			addEdge(parent, child);
		}
		else {
			ExpressionNode expr = f.getExpr();
			expr.accept(this);
		}
		
		currentParentNo = parent;
		
	}

	@Override
	public void visit(OperandNode op) {
		// TODO Auto-generated method stub
		
	}
	
	

}
