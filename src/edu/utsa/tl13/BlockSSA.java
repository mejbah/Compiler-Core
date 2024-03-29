package edu.utsa.tl13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BlockSSA {
	String blockName;
	HashMap<String, PhiFunction> phiFunctions; // key is the orig_var // arraylist
	ArrayList<SSAInstruction>instructions;
	ArrayList<String> phiVars; // list of orig var name that has phi function
	Set<String> definedVars; // Set of vars defined in this blocks
	BlockSSA successor1;
	BlockSSA successor2;
	
	public BlockSSA( String blockName ) {
	   phiFunctions = new HashMap<String, PhiFunction>();
	   instructions = new ArrayList<SSAInstruction>();
	   phiVars = new ArrayList<String>();
	   definedVars= new HashSet<String>();
	   successor1 = null;
	   successor2 = null;
	   this.blockName = blockName;
	}
	
	public HashMap<String, PhiFunction> getPhiFunctions() {
		return phiFunctions;
	}
	
	public ArrayList<SSAInstruction> getInstructions() {
		return instructions;
	}
	
	public void addDefinedVar( String orig_var ) {
		definedVars.add(orig_var);
	}
	
	public boolean isDefined( String orig_var ) {
		if(definedVars.contains(orig_var)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public Set<String> getDefinedVars() {
		return definedVars;
	}

	public void addInstruction( SSAInstruction inst ) {
		this.instructions.add(inst);
	}
    
	public ArrayList<String> getPhiVars() {
		return phiVars;
	}

	public void setPhiVars(ArrayList<String> phiVars) {
		this.phiVars = phiVars;
	}

	public void addPhiFunction( String orig_var, int subscript, int dest_subscript ) {
		if( phiFunctions.get(orig_var) == null ) {
			// create new phi , dest will have subscript one greater than the source as it is the first one
			PhiFunction phi = new PhiFunction( orig_var, new VarWithNumbers(orig_var, dest_subscript) );
			phi.addSource(new VarWithNumbers(orig_var, subscript));
			phiFunctions.put(orig_var, phi);
			phiVars.add(orig_var);
		}
		else {
			phiFunctions.get(orig_var).addSource(new VarWithNumbers(orig_var, subscript));
		}
		
	}
 
	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public void setPhiFunctions(HashMap<String, PhiFunction> phiFunctions) {
		this.phiFunctions = phiFunctions;
	}

	public void setInstructions(ArrayList<SSAInstruction> instructions) {
		this.instructions = instructions;
	}

	public BlockSSA getSuccessor1() {
		return successor1;
	}

	public void setSuccessor1(BlockSSA successor1) {
		this.successor1 = successor1; 
	}

	public BlockSSA getSuccessor2() {
		return successor2;
	}

	public void setSuccessor2(BlockSSA successor2) {
		this.successor2 = successor2;
	}
	
	
	
}
