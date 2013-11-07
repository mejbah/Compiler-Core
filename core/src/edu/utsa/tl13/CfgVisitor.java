package edu.utsa.tl13;

import java.util.ArrayList;

public class CfgVisitor implements Visitor {
	private String textToWrite = "digraph graphviz { \n" + 
    		"node [shape = none];\n" + 
    		"edge [tailport = s];\n" +
    		"entry\n" +
    		"subgraph cluster {\n" +
    		"color=\"/x11/white\"\n";
	int currentParentNo = 0;
	int nodeSerial = 0;
	public void createNewNode( String blockName, ArrayList<String>instructions ) {
		textToWrite = textToWrite + "n"+ nodeSerial + " [label=<<table border=\"0\">";
		String closingText = "</table>>,fillcolor=\"/x11/white\",shape=box]\n";
		
		String blockText = "<tr><td border=\"1\" colspan=\"3\">" + blockName + "</td></tr>";
		textToWrite = textToWrite + blockText;
		for( String s : instructions ) {
			textToWrite = textToWrite + "<tr><td align=\"left\">"+ s+"</td>";
		}
		
		textToWrite = textToWrite + closingText;
		nodeSerial++;
	}
	
	public String getTextToWrite() {
		
		String endText = "}\nentry -> n0\nn" + currentParentNo + "-> exit" +  "\n }";
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
					String id_register = "r" + d.getIdent();
					instructions.add((new Instruction(GlobalConstants.OPCODE_LOADI, "0",  "" , id_register )).getSourceCode());
				}
				else if(d.getIdType() == GlobalConstants.BOOL_TYPE){
					String id_register = "r" + d.getIdent();
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
		
	}

	@Override
	public void visit(StatementListNode s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(StatementNode s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ExpressionNode ex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SimpleExpressionNode sex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TermNode tn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(FactorNode f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(OperandNode op) {
		// TODO Auto-generated method stub
		
	}

}