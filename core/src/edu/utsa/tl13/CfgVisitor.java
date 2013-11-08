package edu.utsa.tl13;

import java.util.ArrayList;

public class CfgVisitor implements Visitor {
	private String textToWrite = "digraph graphviz { \n" + 
    		"node [shape = none];\n" + 
    		"edge [tailport = s];\n" +
    		"entry\n" +
    		"subgraph cluster {\n" +
    		"color=\"/x11/white\"\n";
//	int currentParentNo = 0;
	int nodeSerial = 0;
	String returnValueRegister = null;
	public void createNewNode( String blockName, ArrayList<String>instructions ) {
		
		textToWrite = textToWrite + "\nn"+ nodeSerial + " [label=<<table border=\"0\">";
		
		String blockText = "<tr><td border=\"1\" colspan=\"3\">" + blockName + "</td></tr>";
		textToWrite = textToWrite + blockText;
		for( String s : instructions ) {
			//textToWrite = textToWrite + "<tr><td align=\"left\">"+ s+"</td>";
			addInsructionText(s);
		}
		
	}
	// closeTextCurrentNode must call before createNewNode where nodeSerial increments
	public void createNewNode( String blockName ) {
		
		textToWrite = textToWrite + "\nn"+ nodeSerial + " [label=<<table border=\"0\">";
		
		String blockText = "<tr><td border=\"1\" colspan=\"3\">" + blockName + "</td></tr>";
		textToWrite = textToWrite + blockText;
		
	
		
	}
	
	public void addInsructionText(String s ) {
		textToWrite = textToWrite + "<tr><td align=\"left\">"+ s+"</td></tr>";
	}
	
	public void closeTextCurrentNode() {
		String closingText = "</table>>,fillcolor=\"/x11/white\",shape=box]\n";
		textToWrite = textToWrite + closingText;
		nodeSerial++;
	}
	
	public String getTextToWrite() {
		
		String endText = "\n}\nentry -> n0\nn" + nodeSerial + "-> exit" +  "\n }";
		//TODO:add exit instruction
		addInsructionText("exit");
		closeTextCurrentNode();
		
		textToWrite = textToWrite + endText;
		return textToWrite;
	}
	
	void addEdge( int parentNodeNo, int childNodeNo ) {
		String edge = "n" + parentNodeNo + " -> n" + childNodeNo + "\n"; 
		textToWrite = textToWrite + edge;
	}
	
	
	@Override
	public void visit(Declarations ds) {
		
		if(!ds.getDeclarationList().isEmpty()) {
		//create the first block
			ILOC_block b = new ILOC_block();
		
			ArrayList<String>instructions = new ArrayList<String>();
			for( DeclarationUnit d : ds.getDeclarationList()) {
			
				if(d.getIdType() == GlobalConstants.INT_TYPE) {
					//load 0 in id_register
					String id_register = "r_" + d.getIdent();
					instructions.add((new Instruction(GlobalConstants.OPCODE_LOADI, "0",  "" , id_register )).getSourceCode());
				}
				else if(d.getIdType() == GlobalConstants.BOOL_TYPE){
					String id_register = "r_" + d.getIdent();
					instructions.add((new Instruction(GlobalConstants.OPCODE_LOADI, "0",  "" , id_register )).getSourceCode());
					
				}
			}
			// create node;
			createNewNode(b.getBlockName(), instructions);
		}
		
	}

	@Override
	public void visit(Program p) {
		
		
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
		p.getDeclarationList().accept(this);
		StatementListNode stmtList = p.getStatementList();
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
		if( s instanceof AssignmentNode ) {
			if( ((AssignmentNode) s).getReadInt() != null ) {
				String opcode = GlobalConstants.OPCODE_READINT;
				String dest = new String( "r_" + ((AssignmentNode) s).getIdentNode());

				addInsructionText((new Instruction(opcode, null, null, dest)).getSourceCode());
				
			}
			else {
				String opcode = GlobalConstants.OPCODE_I2I;
				ExpressionNode expr = ((AssignmentNode) s).getExpr();
				expr.accept(this);
				String dest = new String( "r_" + ((AssignmentNode) s).getIdentNode());
				String src = returnValueRegister;
				addInsructionText((new Instruction(opcode, src, null, dest)).getSourceCode());
				
				
			}
	   
		}
		else if( s instanceof IfStatementNode ) {
			// jmp to a new block
			String opcode = GlobalConstants.OPCODE_JUMPI;
			String blockName = ILOCsingleton.getInstance().getNewBlock();
			addInsructionText(new Instruction(opcode, null, null, blockName).getSourceCode());
			
			int parentNodeNo = nodeSerial; // store current node no for creating edge
			closeTextCurrentNode();
			addEdge(parentNodeNo, nodeSerial);
			
			// create new block for If expression
			createNewNode(blockName);
			
			//evaluate expr
			((IfStatementNode) s).getExpr().accept(this);
			String src = returnValueRegister;
			// cbr to L1(if true) or L2 : cbr src l1,l2
			opcode = GlobalConstants.OPCODE_CBR;
			
			String ifblock = ILOCsingleton.getInstance().getNewBlock();
			String elseblock = ILOCsingleton.getInstance().getNewBlock();
			
			addInsructionText((new Instruction(opcode, src, ifblock, elseblock)).getSourceCode());
			
			parentNodeNo = nodeSerial; // store current node no for creating edge
			closeTextCurrentNode();
			
			addEdge(parentNodeNo, nodeSerial);
			//create new block for If statments
			createNewNode(ifblock);
			
			int child1 = nodeSerial; //node no for if block
			((IfStatementNode) s).getStatements().accept(this);
			
			//jmp to exit block instruction
			
			// get the exit block for if-else block
			opcode = GlobalConstants.OPCODE_JUMPI;
			String exitblock = ILOCsingleton.getInstance().getNewBlock();
			addInsructionText(new Instruction(opcode, null, null, exitblock).getSourceCode());
		    closeTextCurrentNode();
		    
		    
		    addEdge(parentNodeNo, nodeSerial);
		    //create new block for else statments
			createNewNode(elseblock);
			int child2 = nodeSerial; //node no for else block
			((IfStatementNode) s).getElseStatements().accept(this);
			//jmp to exit block
			addInsructionText(new Instruction(opcode, null, null, exitblock).getSourceCode());
			closeTextCurrentNode();
			
			
			// add edge to exit block
			addEdge(child1, nodeSerial);
			addEdge(child2, nodeSerial);
			
			createNewNode(exitblock);
			
						
		}
		if( s instanceof WhileStatementNode ) {
			// jmp to a new block
			String opcode = GlobalConstants.OPCODE_JUMPI;
			String blockName = ILOCsingleton.getInstance().getNewBlock();
			addInsructionText(new Instruction(opcode, null, null, blockName).getSourceCode());
			
			int parentNodeNo = nodeSerial; // store current node no for creating edge
			closeTextCurrentNode();
			addEdge(parentNodeNo, nodeSerial);
			
			// create new block for If expression
			createNewNode(blockName);
			
			//evaluate expr
			((WhileStatementNode) s).getExpr().accept(this);
			String src = returnValueRegister;
			// cbr to L1(if true) or L2 : cbr src l1,l2
			opcode = GlobalConstants.OPCODE_CBR;
			
			String loopblock = ILOCsingleton.getInstance().getNewBlock();
			String exitblock = ILOCsingleton.getInstance().getNewBlock();
			
			addInsructionText((new Instruction(opcode, src, loopblock, exitblock)).getSourceCode());
			
			parentNodeNo = nodeSerial; // store current node no for creating edge
			closeTextCurrentNode();
			// add edge from e to L1 : loopblock
			addEdge(parentNodeNo, nodeSerial);
			int child_1 = nodeSerial;
			createNewNode(loopblock);
			
			// add while statements
			((WhileStatementNode) s).getStatements().accept(this);
			// add jmp to blockname : expression block for while
			opcode = GlobalConstants.OPCODE_JUMPI;
			addInsructionText((new Instruction(opcode, null, null, blockName)).getSourceCode());
			
			
			closeTextCurrentNode();
			// add edge to expr block of while
			addEdge(child_1, parentNodeNo);
			// add edge from e to L2 : exitblock
			addEdge(parentNodeNo, nodeSerial);
			//int child_2 = nodeSerial;
			createNewNode(exitblock);
			
		}
		
		if( s instanceof WriteIntNode ) {
			String opcode = GlobalConstants.OPCODE_WRITEINT;
			((WriteIntNode) s).getExpr().accept(this);
			String src = returnValueRegister;
			
			addInsructionText((new Instruction(opcode, src, null, null)).getSourceCode());
		}
		
		
	}
	
	String getArithOpcode( String op ) {
		String opcode = null;
		if( op.equals("*")) {
			opcode = GlobalConstants.OPCODE_MULT;
		}
		else if( op.equals("div")) {
			opcode = GlobalConstants.OPCODE_DIV;
		}
		else if( op.equals("+")) {
			opcode = GlobalConstants.OPCODE_ADD;
		}
		else if( op.equals("-")) {
			opcode = GlobalConstants.OPCODE_SUB;
		}
		else if( op.equals("mod")) {
			opcode = GlobalConstants.OPCODE_MOD;
		}
		else if( op.equals("<")) {
			opcode = GlobalConstants.OPCODE_LT;
		}
		else if( op.equals("<=")) {
			opcode = GlobalConstants.OPCODE_LE;
		}
		else if( op.equals(">")) {
			opcode = GlobalConstants.OPCODE_GT;
		}
		else if( op.equals(">=")) {
			opcode = GlobalConstants.OPCODE_GE;
		}
		else if( op.equals("=")) {
			opcode = GlobalConstants.OPCODE_CMPEQ;
		}
		else if( op.equals("!=")) {
			opcode = GlobalConstants.OPCODE_CMPNE;
		}
		else {
			System.out.println("op not recognized in func :: getArithOpcode");
		}
		return opcode;
	}
	@Override
	public void visit(ExpressionNode ex) {
		if( ex.getRhs() == null ) { // evaluate simpleexpression
			ex.getLhs().accept(this);
		}
		else {
			String opcode = getArithOpcode( ex.getOp().getOp());
			ex.getLhs().accept(this);
			String src1 = returnValueRegister;
			ex.getRhs().accept(this);
			String src2 = returnValueRegister;
			String dest = ILOCsingleton.getInstance().getNextVirtualRegister();
			
			addInsructionText((new Instruction(opcode, src1, src2, dest)).getSourceCode());
			returnValueRegister = dest;
		}
		
	}

	@Override
	public void visit(SimpleExpressionNode sex) {
		if( sex.getRhs() == null ) {
			sex.getLhs().accept(this);
		}
		else {
			String opcode = getArithOpcode( sex.getOp().getOp());
			sex.getLhs().accept(this);
			String src1 = returnValueRegister;
			sex.getRhs().accept(this);
			String src2 = returnValueRegister;
			String dest = ILOCsingleton.getInstance().getNextVirtualRegister();

			addInsructionText((new Instruction(opcode, src1, src2, dest)).getSourceCode());
			returnValueRegister = dest;
		}
		
	}

	@Override
	public void visit(TermNode tn) {
		if( tn.getRhs() == null ) {
			tn.getLhs().accept(this);
		}
		else {
			String opcode = getArithOpcode( tn.getOp().getOp());
			tn.getLhs().accept(this);
			String src1 = returnValueRegister;
			tn.getRhs().accept(this);
			String src2 = returnValueRegister;
			String dest = ILOCsingleton.getInstance().getNextVirtualRegister();

			addInsructionText((new Instruction(opcode, src1, src2, dest)).getSourceCode());
			returnValueRegister = dest;
		}
		
	}

	@Override
	public void visit(FactorNode f) {
		if(f.getExpr() == null) { // got a num or ident or boollit
			 if(f.getId_Type().equals("num")) {
				 // add instruction for loadI
				 String opcode = GlobalConstants.OPCODE_LOADI;
				 String dest = ILOCsingleton.getInstance().getNextVirtualRegister();
				 Instruction ld = new Instruction(opcode, f.getId(), null , dest);
				 
				 addInsructionText(ld.getSourceCode());
				 returnValueRegister = dest;
			 }
			 else if(f.getId_Type().equals("ident")) {
				 // add instruction for loadI
//				 String opcode = GlobalConstants.OPCODE_LOADI;
//				 String dest = ILOCsingleton.getInstance().getNextVirtualRegister();
				 String ident_reg = new String("r_" + f.getId());
//				 Instruction ld = new Instruction(opcode, src, null , dest);
//				 
//				 addInsructionText(ld.getSourceCode());
				 returnValueRegister = ident_reg;
			 }
			 else if(f.getId_Type().equals("boollit")) {
				 String opcode = GlobalConstants.OPCODE_LOADI;
				 String dest = ILOCsingleton.getInstance().getNextVirtualRegister();
				 String src;
				 if(f.getId().equals("true")) {
					 src = "1";
				 }
				 else {
					 src = "0";
				 }
				 
				 Instruction ld = new Instruction(opcode, src, null , dest);
				 
				 addInsructionText(ld.getSourceCode());
				 returnValueRegister = dest;
			 }
			 else { // term = (expr)
				 f.getExpr().accept(this);
			 }
			 
		 }
		
	}

	@Override
	public void visit(OperandNode op) {
		// TODO Auto-generated method stub
		
	}

}