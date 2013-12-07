package edu.utsa.tl13;

import java.util.ArrayList;

public class ILOC_block {
	ArrayList<Instruction> instructions;
	String blockName;
	ILOC_block successor1;
	ILOC_block successor2;
	public boolean isVisited;
	
	public ILOC_block(ILOC_block successor1, ILOC_block successor2) {
		super();
		instructions = new ArrayList<Instruction>();
		this.successor1 = successor1;
		this.successor2 = successor2;
		blockName = ILOCsingleton.getInstance().getNewBlock();
	}
	
	
	public ILOC_block() {
		instructions = new ArrayList<Instruction>();
		blockName = ILOCsingleton.getInstance().getNewBlock();
		this.successor1 = null;
		this.successor2 = null;
		isVisited = false;
		
	}
	
	public void addSuccessor( ILOC_block b ) {
		if(successor1 == null) {
			successor1 = b;
		}
		else {
			successor2 = b;
		}
		
	}
	
	void addBlockInstruction( Instruction instruction ) {
		this.instructions.add(instruction);
	}
	public ArrayList<Instruction> getInstructions() {
		return instructions;
	}


	public void setInstructions(ArrayList<Instruction> instructions) {
		this.instructions = instructions;
	}


	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}


	public void setSuccessor1(ILOC_block successor1) {
		this.successor1 = successor1;
	}


	public void setSuccessor2(ILOC_block successor2) {
		this.successor2 = successor2;
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
