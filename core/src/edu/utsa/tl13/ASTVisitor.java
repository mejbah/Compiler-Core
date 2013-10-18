package edu.utsa.tl13;

public class ASTVisitor implements Visitor {

	String textToWrite = "digraph parseTree {" + "\nordering=out; \nnode [shape = box, style = filled];\n";
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
			int child_1 = addNewNode("decl: " + d.getIdent());
			addEdge(parent , child_1);
			int type = addNewNode(d.getIdType());
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

	@Override
	public void visit(ProgramNode p) {
		int parent = addNewNode(new String("program"));
        
		if(!p.getDeclarationList().getDeclarationList().isEmpty()) {
			//declarations is not empty
			currentParentNo = parent;
	        int child_1 = addNewNode( new String("decl list"));
	        addEdge(currentParentNo , child_1);
	        
	        Declarations ds = p.getDeclarationList();
	        currentParentNo = child_1;
	        ds.accept(this);
	        currentParentNo = parent;
	        
			
		}
		
        int child_2 = addNewNode(new String("stmt list"));
        addEdge(currentParentNo , child_2);
        StatementListNode stmtList = p.getStatementList();
        currentParentNo = child_2;
        stmtList.accept(this);
		
	}

	@Override
	public void visit(StatementListNode s) {
		// TODO Auto-generated method stub
		
		for( StatementNode stmt : s.getStatementList() ) {
			stmt.accept(this);
		}
	}

	@Override
	public void visit(StatementNode s) {
		int parent = currentParentNo;
		
		if( s instanceof IfStatementNode ) {
			int child = addNewNode("If");
			addEdge(parent, child);
			
			currentParentNo = child;
			ExpressionNode expr = ((IfStatementNode) s).getExpr();
			expr.accept(this);
			
			currentParentNo = child;
			StatementListNode ifstmts = ((IfStatementNode) s).getStatements();
		    ifstmts.accept(this);
			
		    if(((IfStatementNode) s).getElseStatements() != null) {
		    	int child_1 = addNewNode("stmt list");
		    	addEdge(child , child_1);
		    	
		    	currentParentNo = child_1;
		    	StatementListNode elsestmts = ((IfStatementNode) s).getElseStatements();
		    	elsestmts.accept(this);
		    }
		}
		
		else if(s instanceof AssignmentNode ) {
			int child = addNewNode(" := ");
			addEdge(parent, child);
			
			int child_1 = addNewNode(((AssignmentNode) s).getIdentNode());
			addEdge(child, child_1);
			
			if( ((AssignmentNode) s).getReadInt() != null ) {
				int child_2 = addNewNode(((AssignmentNode) s).getReadInt());
				addEdge(child, child_2);
			}
			else {
				ExpressionNode expr = ((AssignmentNode) s).getExpr();
				currentParentNo = child;
				expr.accept(this);
			}
			
			
		}
		
		else if(s instanceof WhileStatementNode ) {
			int child = addNewNode(" While ");
			addEdge(parent, child);
			
			ExpressionNode expr = ((WhileStatementNode) s).getExpr();
			currentParentNo = child;
			expr.accept(this);
			
			
			if(! ((WhileStatementNode) s).getStatements().getStatementList().isEmpty() ) {
				
				StatementListNode stmts = ((WhileStatementNode) s).getStatements();
				int child_1 = addNewNode("stmt list");
		    	addEdge(child , child_1);
		    	
		    	currentParentNo = child_1;
				stmts.accept(this);
			}
			
		}
		
		else if( s instanceof WriteIntNode ) {
			int child = addNewNode(" WriteInt ");
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
			simplExpr.accept(this);
		}
		else {
			
			int child = addNewNode(ex.getOp().op); // add operand of expression as node
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
			
			int child = addNewNode(sex.getOp().op); // add operand of expression as node
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
			
			int child = addNewNode(tn.getOp().op); // add operand of expression as node
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
			int child = addNewNode(f.getId());
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
