package edu.utsa.tl13;

import java.util.ArrayList;

public class ILOC_block {
	ArrayList<Instruction> instructions;
	String blockName;
	ILOC_block successor1;
	ILOC_block successor2;
	
	public ILOC_block(ILOC_block successor1, ILOC_block successor2) {
		super();
		instructions = new ArrayList<Instruction>();
		this.successor1 = successor1;
		this.successor2 = successor2;
		blockName = ILOCsingleton.getInstance().getNewBlock();
	}
	
	
	public ILOC_block() {
		blockName = ILOCsingleton.getInstance().getNewBlock();
		
	}


	void addInstruction( Instruction inst ) {
		this.instructions.add(inst);
	}
	
	Instruction getInstruction( int index ) {
		return instructions.get(index);
	}
	
	String getILOCforInstruction( int index) {
		return instructions.get(index).getSourceCode();
	}
	
	String getBlockName() {
		return blockName;
	}
	
	ILOC_block getSuccessor1() {
		return successor1;
	}
	
	ILOC_block getSuccessor2() {
		return successor2;
	}
	
	

}
