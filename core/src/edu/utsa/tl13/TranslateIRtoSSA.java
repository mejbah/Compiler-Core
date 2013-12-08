package edu.utsa.tl13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
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
//	/*
//	 * add instructions is SSA form in a block
//	 */
//	private void addSSAInstruction( BlockSSA dest, Instruction inst ) {
//		if(isAssignment(inst)) {
//			VarWithNumbers newDest = null ;
//			VarWithNumbers newSrc1 = null;
//			VarWithNumbers newSrc2 = null;
//			
//			if( varNumber.get(inst.getDest()) == null ) {
//				// new ir var
//				newDest = new VarWithNumbers(inst.getDest(), 1); // first subscript number is 1
//				varNumber.put(inst.getDest(), newDest);
//			} 
//			else {
//				int subscript = varNumber.get(inst.getDest()).getNumber();
//				subscript = subscript + 1;
//				newDest = new VarWithNumbers(inst.getDest(), subscript ); // first subscript number is 1
//				varNumber.put(inst.getDest(), newDest);
//				
//			}
//			
//			String src1 = inst.getSrc1();
//			
//			//******* for load src is immediate value*/////
//			if(inst.getOpcode() == GlobalConstants.OPCODE_LOADI) {
//				newSrc1 =  new VarWithNumbers(src1, 1); // TODO : special care for prinintng
//				
//			}
//			else {
//			
//			
//			//******* end special check for immidiate instruction loadI*////
//			// check if we have a phi function for the variable in the block
//			if( dest.getPhiFunctions().containsKey(src1)) {
//				newSrc1 =  dest.getPhiFunctions().get(src1).getDestination();
//			} 
//			else {
//				// defined in this block : will be in varNumber list
//				newSrc1 = varNumber.get(src1);
//			}
//			
//			if( inst.getSrc2() != null ) {
//				String src2 = inst.getSrc2();
//				
//				// check if we have a phi function for the variable in the block
//				if( dest.getPhiFunctions().containsKey(src2)) {
//					newSrc2 =  dest.getPhiFunctions().get(src2).getDestination();
//				} 
//				else {
//					// defined in this block : will be in varNumber list
//					newSrc2 = varNumber.get(src2);
//				}
//				
//			}
//			} // end of checking if block  for loadI case
//			dest.addInstruction(new SSAInstruction(inst.getOpcode(), newSrc1, newSrc2, newDest, false, null, null));
//		}
//		else { //jumpI cbr writeInt
//			if( inst.getSrc1() == null ) { //jumpI : no src, destination is label
//				dest.addInstruction(new SSAInstruction(inst.getOpcode(), null, null, null, true, inst.getDest(),null));
//			}
//			else if(inst.getSrc2() == null ){ // writeInt
//				VarWithNumbers newSrc1 = null;
//				String src1 = inst.getSrc1();
//				// check if we have a phi function for the variable in the block
//				if( dest.getPhiFunctions().containsKey(src1)) {
//					newSrc1 =  dest.getPhiFunctions().get(src1).getDestination();
//				} 
//				else {
//					// defined in this block : will be in varNumber list
//					newSrc1 = varNumber.get(src1);
//				}
//				dest.addInstruction(new SSAInstruction(inst.getOpcode(), newSrc1, null, null, false, null, null));
//			
//			}
//			else { //cbr
//				VarWithNumbers newSrc1 = null;
//				String src1 = inst.getSrc1();
//				// check if we have a phi function for the variable in the block
//				if( dest.getPhiFunctions().containsKey(src1)) {
//					newSrc1 =  dest.getPhiFunctions().get(src1).getDestination();
//				} 
//				else {
//					// defined in this block : will be in varNumber list
//					newSrc1 = varNumber.get(src1);
//				}
//				
//				dest.addInstruction(new SSAInstruction(inst.getOpcode(), newSrc1, null, null, true, inst.getSrc2(), inst.getDest()));
//			}
//		}
//		
//	}
	
	/* new edited function for above : delete following and uncomment above function if not working */
	/*
	 * add instructions is SSA form in a block
	 */
	private void addSSAInstruction( BlockSSA dest, Instruction inst ) {
		if(isAssignment(inst)) {
			VarWithNumbers newDest = null ;
			VarWithNumbers newSrc1 = null;
			VarWithNumbers newSrc2 = null;
			
			
			String src1 = inst.getSrc1();
			
			//******* for load src is immediate value*/////
			if(inst.getOpcode() == GlobalConstants.OPCODE_LOADI) {
				newSrc1 =  new VarWithNumbers(src1, 1); // TODO : special care for prinintng
				
			}
			else {
			
			
			//******* end special check for immidiate instruction loadI*////
			//check if the source is defined in this block
			if( dest.isDefined(src1)) {
				newSrc1 = varNumber.get(src1);
			}
			else {
			//else if should be defined in phi functions
				if( dest.getPhiFunctions().containsKey(src1)) {
					newSrc1 =  dest.getPhiFunctions().get(src1).getDestination();
				}
				else {
					System.out.println("### Not found src ###");
				}
			}
			
			
			if( inst.getSrc2() != null ) {
				String src2 = inst.getSrc2();
				
				//check if the source is defined in this block
				if( dest.isDefined(src2)) {
					newSrc2 = varNumber.get(src2);
				}
				else {
				//else if should be defined in phi functions
					if( dest.getPhiFunctions().containsKey(src2)) {
						newSrc2 =  dest.getPhiFunctions().get(src2).getDestination();
					}
					else {
						System.out.println("### Not found src ###");
					}
				}
				
			}
			} // end of checking if block  for loadI case
			// update destination
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

			dest.addInstruction(new SSAInstruction(inst.getOpcode(), newSrc1, newSrc2, newDest, false, null, null));
			
			/** add set of variable assigned in this block **/
			dest.addDefinedVar(inst.getDest());
			
		}
		else { //jumpI cbr writeInt
			if( inst.getSrc1() == null ) { //jumpI : no src, destination is label
				dest.addInstruction(new SSAInstruction(inst.getOpcode(), null, null, null, true, inst.getDest(),null));
			}
			else if(inst.getSrc2() == null ){ // writeInt
				VarWithNumbers newSrc1 = null;
				String src1 = inst.getSrc1();
				//check if the source is defined in this block
				if( dest.isDefined(src1)) {
					newSrc1 = varNumber.get(src1);
				}
				else {
				//else if should be defined in phi functions
					if( dest.getPhiFunctions().containsKey(src1)) {
						newSrc1 =  dest.getPhiFunctions().get(src1).getDestination();
					}
					else {
						System.out.println("### Not found src ###");
					}
				}
				dest.addInstruction(new SSAInstruction(inst.getOpcode(), newSrc1, null, null, false, null, null));
			
			}
			else { //cbr
				VarWithNumbers newSrc1 = null;
				String src1 = inst.getSrc1();
				//check if the source is defined in this block
				if( dest.isDefined(src1)) {
					newSrc1 = varNumber.get(src1);
				}
				else {
				//else if should be defined in phi functions
					if( dest.getPhiFunctions().containsKey(src1)) {
						newSrc1 =  dest.getPhiFunctions().get(src1).getDestination();
					}
					else {
						System.out.println("### Not found src ###");
					}
				}
				
				dest.addInstruction(new SSAInstruction(inst.getOpcode(), newSrc1, null, null, true, inst.getSrc2(), inst.getDest()));
			}
		}
		
	}

	
	
	/* end edit new function */
	public void addPhiFunction( BlockSSA block, String origVar, VarWithNumbers srcVar ) {
		//check if already a phi function created for the block
		if(block.getPhiFunctions().get(origVar) == null) { // no phi exist
			//create new phi
			int subscript = varNumber.get(origVar).getNumber();
			subscript += 1;
			VarWithNumbers destVar = new VarWithNumbers(origVar, subscript);
			varNumber.put(origVar, destVar); // update global variable number subscript: wrong 
			
			block.addPhiFunction(origVar, srcVar.getNumber(), destVar.getNumber());
		}
		else {
			// 3rd parameter dest var subscript not used if phi exist already
			block.addPhiFunction(origVar, srcVar.getNumber(), 0 );
		}
	}
	
	private BlockSSA getSSAblock( String name ) {
		if( ssaBlocks.containsKey(name)) {
			return ssaBlocks.get(name);
		}
		else {
			BlockSSA b = new BlockSSA( name );
			ssaBlocks.put(name, b);
			return b;
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
		
		Set<String> varForPhi = new HashSet<String>();
		for( Instruction inst : cfgRoot.getInstructions()) {
			//add instruction in block
			addSSAInstruction(root, inst);
			
			// add phi function in successor
			if(isAssignment(inst)) {
				varForPhi.add(inst.getDest());
			}
			
		}
		// add phi function in successor
		for( String s : varForPhi ){
			//store the src varwithnumber here as subscript may be updated in one child will 
			//affect other child
			 
			VarWithNumbers srcForPhi = varNumber.get(s);
			
			if(child1 != null) {
//				addPhiFunction(child1, s, varNumber.get(s));
				addPhiFunction(child1, s, srcForPhi);
				if(child2 != null) {
//					addPhiFunction(child2, s, varNumber.get(s));
					addPhiFunction(child2, s, srcForPhi);
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
					// TODO: check existing SSA block before creating new ??
						child1 = getSSAblock(s1.getSuccessor1().getBlockName());
						//child1 = new BlockSSA( s1.getSuccessor1().getBlockName());
						
						if(s1.getSuccessor2() != null) {
							child2 = getSSAblock(s1.getSuccessor2().getBlockName());
//							child2 = new BlockSSA(s1.getSuccessor2().getBlockName());
							
						}
						
					}
					// buiding new tree for ssa blocks
					newblock.setSuccessor1(child1);
					newblock.setSuccessor2(child2);
					
					Set<String> phiVars = new HashSet<String>();
					/* new edit : first add the current block phi assignment instruction */
					for( String var : newblock.getPhiVars()) {
						
						phiVars.add(var);
					}
					/* end new edit */
					for( Instruction inst : s1.getInstructions()) {
						//add instruction in block
						addSSAInstruction(newblock, inst);
						
						// add phi function in successor
						if(isAssignment(inst)) {
							phiVars.add(inst.getDest());
						}
					}
					
					// add phi function in successor
					for( String s : phiVars ) {
						//store the src varwithnumber here as subscript may be updated in one child will 
						//affect other child
						VarWithNumbers srcForPhi = varNumber.get(s); 
						
						if( ! newblock.isDefined(s) ) {
							// not defined just passing my phi to successor
							PhiFunction phi = newblock.getPhiFunctions().get(s);
							srcForPhi = phi.getDestination();
						}
						
						
						if(child1 != null) {
//							addPhiFunction(child1, s, varNumber.get(s));
							addPhiFunction(child1, s, srcForPhi);
							if(child2 != null) {
//								addPhiFunction(child2, s, varNumber.get(s));
								addPhiFunction(child2, s, srcForPhi);
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
						child1 = getSSAblock(s2.getSuccessor1().getBlockName());
					//	child1 = new BlockSSA( s2.getSuccessor1().getBlockName());
						
						if(s2.getSuccessor2() != null) {
							child2 = getSSAblock(s2.getSuccessor2().getBlockName());
//							child2 = new BlockSSA(s2.getSuccessor2().getBlockName());
							
						}
						
					}
					// buiding new tree for ssa blocks
					newblock.setSuccessor1(child1);
					newblock.setSuccessor2(child2);
					
					Set<String> phiVars = new HashSet<String>();
					for( Instruction inst : s2.getInstructions()) {
						//add instruction in block
						addSSAInstruction(newblock, inst);
						// add phi function in successor
						if(isAssignment(inst)) {
							phiVars.add(inst.getDest());
						}
					}

					// add phi function in successor
					for( String s : phiVars ) {
						//store the src varwithnumber here as subscript may be updated in one child will 
						//affect other child
						VarWithNumbers srcForPhi = varNumber.get(s);
						
						if( ! newblock.isDefined(s) ) {
							// not defined just passing my phi to successor
							PhiFunction phi = newblock.getPhiFunctions().get(s);
							srcForPhi = phi.getDestination();
						}
						
						if(child1 != null) {
//							addPhiFunction(child1, s, varNumber.get(s));
							addPhiFunction(child1, s, srcForPhi);
							if(child2 != null) {
//								addPhiFunction(child2, s, varNumber.get(s));
								addPhiFunction(child2, s, srcForPhi);
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
	
	/**
	 * call after crudeSSA() for minimal  phi function
	 */
	public void minimizationPhase() {
		
		Set<String> blockNames = new HashSet<String>();
		if(root != null)
			minimizeFirstPhase(root, blockNames);
				
	}
	
	public void minimizeFirstPhase( BlockSSA b, Set<String> blocksVisited ){
		if( ! blocksVisited.contains(b.getBlockName())) {
			blocksVisited.add(b.getBlockName());
			ArrayList<String> deleteList = new ArrayList<>();
			for( String var : b.getPhiVars() ) {
				PhiFunction phi = b.getPhiFunctions().get(var);
				if(phi.getSources().size() == 1 ) { //only one source
					replaceVar(b, phi.getDestination(), phi.getSources().get(0));
					//delete the phi
					deleteList.add(var);
				}
			
			}
		
			for( String var : deleteList ) {
				b.getPhiVars().remove(var);
			}
			
			if(b.getSuccessor1() == null) {
				return;
			}
			else {
				minimizeFirstPhase(b.getSuccessor1(), blocksVisited);
				if( b.getSuccessor2() != null ) {
					minimizeFirstPhase(b.getSuccessor2(), blocksVisited);
				}
			}
		}
		

	}
	
	
	private void replaceVar( BlockSSA block, VarWithNumbers oldVar, VarWithNumbers newVar ) {
		// edit all instruction of current block : update src with same name as phi destVar
		for( SSAInstruction inst : block.getInstructions()) {
			if( inst.getSrc1() != null ) {
				if(isEqualVar(oldVar, inst.getSrc1())) {
					inst.setSrc1(newVar);
				}
			}
			if( inst.getSrc2() != null ) {
				if(isEqualVar(oldVar, inst.getSrc2())) {
					inst.setSrc2(newVar);
				}
			}
			
		}
		// edit phi for successor block
		if( block.getSuccessor1() != null) {
			BlockSSA child1 = block.getSuccessor1();
			PhiFunction phi = child1.getPhiFunctions().get(oldVar.getName());
			if( phi != null ) {

				if(phi.getSources().remove(oldVar)) {
					phi.getSources().add(newVar);
				}
			}
			child1.getPhiFunctions().put(oldVar.getName(), phi);
			
			if( block.getSuccessor2() != null ) {
				BlockSSA child2 = block.getSuccessor2();
				phi = child2.getPhiFunctions().get(oldVar.getName());
				if( phi != null ) {

					if(phi.getSources().remove(oldVar)) {
						phi.getSources().add(newVar);
					}
				}
				child2.getPhiFunctions().put(oldVar.getName(), phi);
				
			}
		}
		
		
	}
	
	private boolean isEqualVar( VarWithNumbers v1, VarWithNumbers v2) {
		if( v1.getName().equals(v2.getName()) ) {
			if( v1.getNumber() == v2.getNumber()) {
				return true;
			}
		}
		
		return false;
	}
	
	public void printSSAInstructions( BlockSSA b, Set<String> blocksVisited ) {
		if( ! blocksVisited.contains(b.getBlockName())) {
		blocksVisited.add(b.getBlockName());	
		
		System.out.println("Block : " + b.getBlockName());
		for( String var : b.getPhiVars()) {
			PhiFunction p = b.getPhiFunctions().get(var);
			System.out.println(p.printText());
		}
		for (SSAInstruction inst : b.getInstructions()) {
		  System.out.println(inst.getSourceCode());
		}
		if(b.getSuccessor1() == null) {
			return;
		}
		else {
			printSSAInstructions(b.getSuccessor1(), blocksVisited);
			if( b.getSuccessor2() != null ) {
				printSSAInstructions(b.getSuccessor2(), blocksVisited);
			}
		}
		
		} 
		
	}
	
	public void debugPrint() {
		
		Set<String> blockNames = new HashSet<String>();
		if(root != null)
			printSSAInstructions(root, blockNames);
	}
	/*
	 * text for the graphviz file
	 */
	public String graphVizString() {
		CfgSSAForm graph = new CfgSSAForm();
		if(root != null) {
			graph.graphVizText(root);
		}
		return graph.getTextToWrite();
		
	}
	

}
