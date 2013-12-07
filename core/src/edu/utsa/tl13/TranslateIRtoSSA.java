package edu.utsa.tl13;

import java.util.HashMap;
import java.util.Stack;

public class TranslateIRtoSSA {
	private ILOC_block cfgRoot;
	private BlockSSA root;
//	HashMap<String, Integer> varNumber; // maps original var to current subscript
	HashMap<String, VarWithNumbers> varNumber;
	HashMap<String, BlockSSA> ssaBlocks; // maps block name to new ssa block
	
	
	private boolean isAssignment( Instruction inst ) {
		String opcode = inst.getOpcode();
		if( opcode.equals(GlobalConstants.OPCODE_CBR)
				| opcode.equals(GlobalConstants.OPCODE_JUMPI)
				| opcode.equals(GlobalConstants.OPCODE_WRITEINT) ) {
			return false;
		}
		return true;
		
	}
	public TranslateIRtoSSA( ILOC_block cfg ) {
		cfgRoot = cfg;
		root = null;
		varNumber = new HashMap<String, VarWithNumbers>();
		ssaBlocks = new HashMap<String, BlockSSA>();
	}
	/*
	 * add instructions is SSA form in a block
	 */
	private void addSSAInstruction( BlockSSA dest, Instruction inst ) {
		if(isAssignment(inst)) {
			VarWithNumbers newDest = null ;
			VarWithNumbers newSrc1 = null;
			VarWithNumbers newSrc2 = null;
			
			if( varNumber.get(inst.getDest()) == null ) {
				// new ir var
				newDest = new VarWithNumbers(inst.getDest(), 1); // first subscript number is 1
				varNumber.put(inst.getDest(), newDest);
			} 
			else {
				int subscript = varNumber.get(inst.getDest()).getNumber();
				subscript = subscript + 1;
				newDest = new VarWithNumbers(inst.getDest(), subscript ); // first subscript number is 1
				varNumber.put(inst.getDest(), newDest);
				
			}
			
			String src1 = inst.getSrc1();
			
			//******* for load src is immediate value*/////
			if(inst.getOpcode() == GlobalConstants.OPCODE_LOADI) {
				newSrc1 =  new VarWithNumbers(src1, 1); // TODO : special care for prinintng
				
			}
			else {
			
			
			//******* end special check for immidiate instruction loadI*////
			// check if we have a phi function for the variable in the block
			if( dest.getPhiFunctions().containsKey(src1)) {
				newSrc1 =  dest.getPhiFunctions().get(src1).getDestination();
			} 
			else {
				// defined in this block : will be in varNumber list
				newSrc1 = varNumber.get(src1);
			}
			
			if( inst.getSrc2() != null ) {
				String src2 = inst.getSrc2();
				
				// check if we have a phi function for the variable in the block
				if( dest.getPhiFunctions().containsKey(src2)) {
					newSrc2 =  dest.getPhiFunctions().get(src2).getDestination();
				} 
				else {
					// defined in this block : will be in varNumber list
					newSrc2 = varNumber.get(src2);
				}
				
			}
			} // end of checking if block  for loadI case
			dest.addInstruction(new SSAInstruction(inst.getOpcode(), newSrc1, newSrc2, newDest, false, null, null));
		}
		else { //jumpI cbr writeInt
			if( inst.getSrc1() == null ) { //jumpI : no src, destination is label
				dest.addInstruction(new SSAInstruction(inst.getOpcode(), null, null, null, true, inst.getDest(),null));
			}
			else if(inst.getSrc2() == null ){ // writeInt
				VarWithNumbers newSrc1 = null;
				String src1 = inst.getSrc1();
				// check if we have a phi function for the variable in the block
				if( dest.getPhiFunctions().containsKey(src1)) {
					newSrc1 =  dest.getPhiFunctions().get(src1).getDestination();
				} 
				else {
					// defined in this block : will be in varNumber list
					newSrc1 = varNumber.get(src1);
				}
				dest.addInstruction(new SSAInstruction(inst.getOpcode(), newSrc1, null, null, false, null, null));
			
			}
			else { //cbr
				VarWithNumbers newSrc1 = null;
				String src1 = inst.getSrc1();
				// check if we have a phi function for the variable in the block
				if( dest.getPhiFunctions().containsKey(src1)) {
					newSrc1 =  dest.getPhiFunctions().get(src1).getDestination();
				} 
				else {
					// defined in this block : will be in varNumber list
					newSrc1 = varNumber.get(src1);
				}
				
				dest.addInstruction(new SSAInstruction(inst.getOpcode(), newSrc1, null, null, true, inst.getSrc2(), inst.getDest()));
			}
		}
		
	}
	
	public void addPhiFunction( BlockSSA block, String origVar, VarWithNumbers srcVar ) {
		//check if already a phi function created for the block
		if(block.getPhiFunctions().get(origVar) == null) { // no phi exist
			//create new phi
			int subscript = varNumber.get(origVar).getNumber();
			subscript += 1;
			VarWithNumbers destVar = new VarWithNumbers(origVar, subscript);
			varNumber.put(origVar, destVar); // update global variable number subscript 
			
			block.addPhiFunction(origVar, srcVar.getNumber());
		}
		else {
			block.addPhiFunction(origVar, srcVar.getNumber());
		}
	}
	/**
	 * create non minimal SSA
	 */
	public void crudeSSA() {
		Stack<ILOC_block> block_stack = new Stack<ILOC_block>();
		
		//visit node
		// add all the instructions for node in new SSA block
		root = new BlockSSA( cfgRoot.getBlockName());
		BlockSSA child1 = null; 
		BlockSSA child2 = null;
		if( cfgRoot.getSuccessor1() != null) {
			
			child1 = new BlockSSA( cfgRoot.getSuccessor1().getBlockName());
			
			if(cfgRoot.getSuccessor2() != null) {
				child2 = new BlockSSA(cfgRoot.getSuccessor2().getBlockName());
				
			}
			
		}
		// buiding new tree for ssa blocks
		root.setSuccessor1(child1);
		root.setSuccessor2(child2);
		
		for( Instruction inst : cfgRoot.getInstructions()) {
			//add instruction in block
			addSSAInstruction(root, inst);
			// add phi function in successor
			if(isAssignment(inst)) {
				if(child1 != null ) {
					addPhiFunction(child1, inst.getDest(), varNumber.get(inst.getDest()));
					if(child2 != null) {
						addPhiFunction(child2, inst.getDest(), varNumber.get(inst.getDest()));
					}
				}
				
			}
		}
		
		//add the block in hashmap
		ssaBlocks.put(root.getBlockName(), root);
		if(child1 != null)
			ssaBlocks.put(child1.getBlockName(), child1);
		if(child2 != null)
			ssaBlocks.put(child2.getBlockName(), child2);
		
		cfgRoot.isVisited = true;
		System.out.println(cfgRoot.getBlockName());
		block_stack.push(cfgRoot);
		
		 
		while( !block_stack.isEmpty()) {
			ILOC_block b = block_stack.pop();
			
			ILOC_block s1 = b.getSuccessor1();
			ILOC_block s2 = b.getSuccessor2();
			 
			if( s1 != null) {
				if( !s1.isVisited ) {
					BlockSSA newblock = ssaBlocks.get(s1.getBlockName()); // get the ssa block for corresponding iloc block
					
					child1 = null; 
					child2 = null;
					if( s1.getSuccessor1() != null) {
						
						child1 = new BlockSSA( s1.getSuccessor1().getBlockName());
						
						if(s1.getSuccessor2() != null) {
							child2 = new BlockSSA(s1.getSuccessor2().getBlockName());
							
						}
						
					}
					// buiding new tree for ssa blocks
					newblock.setSuccessor1(child1);
					newblock.setSuccessor2(child2);
					
					for( Instruction inst : s1.getInstructions()) {
						//add instruction in block
						addSSAInstruction(newblock, inst);
						// add phi function in successor
						if(isAssignment(inst)) {
							if(child1 != null ) {
								addPhiFunction(child1, inst.getDest(), varNumber.get(inst.getDest()));
								if(child2 != null) {
									addPhiFunction(child2, inst.getDest(), varNumber.get(inst.getDest()));
								}
							}
							
						}
					}

					//add the block in hashmap
					ssaBlocks.put(newblock.getBlockName(), newblock);
					if(child1 != null)
						ssaBlocks.put(child1.getBlockName(), child1);
					if(child2 != null)
						ssaBlocks.put(child2.getBlockName(), child2);
					
					s1.isVisited = true;
					System.out.println(s1.getBlockName());
					block_stack.push(s1);
					
				}
			}
			
			
			if( s2 != null) {
				if( !s2.isVisited ) {
					BlockSSA newblock = ssaBlocks.get(s2.getBlockName()); // get the ssa block for corresponding iloc block
					
					child1 = null; 
					child2 = null;
					if( s2.getSuccessor1() != null) {
						
						child1 = new BlockSSA( s2.getSuccessor1().getBlockName());
						
						if(s2.getSuccessor2() != null) {
							child2 = new BlockSSA(s2.getSuccessor2().getBlockName());
							
						}
						
					}
					// buiding new tree for ssa blocks
					newblock.setSuccessor1(child1);
					newblock.setSuccessor2(child2);
					
					for( Instruction inst : s2.getInstructions()) {
						//add instruction in block
						addSSAInstruction(newblock, inst);
						// add phi function in successor
						if(isAssignment(inst)) {
							if(child1 != null ) {
								addPhiFunction(child1, inst.getDest(), varNumber.get(inst.getDest()));
								if(child2 != null) {
									addPhiFunction(child2, inst.getDest(), varNumber.get(inst.getDest()));
								}
							}
							
						}
					}

					//add the block in hashmap
					ssaBlocks.put(newblock.getBlockName(), newblock);
					if(child1 != null)
						ssaBlocks.put(child1.getBlockName(), child1);
					if(child2 != null)
						ssaBlocks.put(child2.getBlockName(), child2);

					s2.isVisited = true;
					System.out.println(s2.getBlockName());
					block_stack.push(s2);
					
				}
			} 
			
			
		}
		
		
	}
	
	
	
	public void printSSAInstructions( BlockSSA b ) {
		System.out.println("Block : " + b.getBlockName());
		for( String var : b.getPhiVars()) {
			PhiFunction p = b.getPhiFunctions().get(var);
			p.printText();
		}
		for (SSAInstruction inst : b.getInstructions()) {
		  System.out.println(inst.getSourceCode());
		}
		if(b.getSuccessor1() == null) {
			return;
		}
		else {
			printSSAInstructions(b.getSuccessor1());
			if( b.getSuccessor2() != null ) {
				printSSAInstructions(b.getSuccessor2());
			}
		}
		
	}
	
	public void debugPrint() {
		if(root != null)
			printSSAInstructions(root);
	}
//	
//	public void crudeSSA( ILOC_block block ) {
//		
//		BlockSSA newBlock = new BlockSSA();
//		
//		if(block.getSuccessor1() != null) { // has successor
//			
//			BlockSSA successor1 = new BlockSSA();
//			newBlock.setSuccessor1(successor1);
//			if( block.getSuccessor2() != null ) {
//				BlockSSA successor2 = new BlockSSA();
//				newBlock.setSuccessor2(successor2);
//			}
//			for( Instruction inst : block.getInstructions() ) {
//				if(isAssignment(inst)) {
//					//check dest var in hashmap for subscript
//					int destSubscript;
//					if( varNumber.get(inst.getDest()) == null ) {
//						// first occurance of var, so initial subscript is put
//						varNumber.put(inst.getDest(), 1);
//						destSubscript = 1;
//					}
//					else {
//						// else new assignment in successor
//						destSubscript = varNumber.get(inst.getDest());
//					}
//					//insert phi in successor block
//					newBlock.getSuccessor1().addPhiFunction( inst.getDest(), destSubscript );	
//				}
//				
//				//add instruction in new block :: add opearands/src from phifunctions
//			}
//					
//		}
//		
//		
//	}
	

}
