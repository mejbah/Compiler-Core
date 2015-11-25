package edu.utsa.tl13;

import java.util.ArrayList;

public class ILOCsingleton {
	private static ILOCsingleton ILOC;
	private int register_no_cursor;
	private int block_no_cursor;
	private ArrayList<Instruction>ilocInstruction; // will contain the list of iloc
	private ILOCsingleton() {
		register_no_cursor = -1;
		block_no_cursor = 0;
		ilocInstruction = new ArrayList<Instruction>();
	}
	
	public static ILOCsingleton getInstance() {
		if( ILOC == null ) {
			ILOC = new ILOCsingleton();
		}
		return ILOC;
		
	}
	
	public ArrayList<Instruction> getIlocInstructionList() {
		return ilocInstruction;
	}
	
	String getNextVirtualRegister() {
		register_no_cursor++;
		return new String("r" + register_no_cursor);
		
	}
	
	String getNewBlock() {
		block_no_cursor++;
		return new String("B" + block_no_cursor);
	}
	

}
