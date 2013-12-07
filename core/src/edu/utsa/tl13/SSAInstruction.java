package edu.utsa.tl13;

public class SSAInstruction {
	private String opcode;
	private VarWithNumbers src1;
	private VarWithNumbers src2;
	private VarWithNumbers dest;
	private String sourceCode;
	boolean isbranch; // if it is a label for jump, src1 contains the label string
	private String branch1;
	private String branch2;
	
	public SSAInstruction(String opcode, VarWithNumbers src1,
			VarWithNumbers src2, VarWithNumbers dest, boolean isbranch,
			String branch1, String branch2) {
		super();
		this.opcode = opcode;
		this.src1 = src1;
		this.src2 = src2;
		this.dest = dest;
		this.isbranch = isbranch;
		this.branch1 = branch1;
		this.branch2 = branch2;
		this.sourceCode = null;
	}

	public String getOpcode() {
		return opcode;
	}

	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}

	public VarWithNumbers getSrc1() {
		return src1;
	}

	public void setSrc1(VarWithNumbers src1) {
		this.src1 = src1;
	}

	public VarWithNumbers getSrc2() {
		return src2;
	}

	public void setSrc2(VarWithNumbers src2) {
		this.src2 = src2;
	}

	public VarWithNumbers getDest() {
		return dest;
	}

	public void setDest(VarWithNumbers dest) {
		this.dest = dest;
	}

//	public String getSourceCode() {
//		return sourceCode;
//	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public boolean isIsbranch() {
		return isbranch;
	}

	public void setIsbranch(boolean isbranch) {
		this.isbranch = isbranch;
	}

	public String getBranch1() {
		return branch1;
	}

	public void setBranch1(String branch1) {
		this.branch1 = branch1;
	}

	public String getBranch2() {
		return branch2;
	}

	public void setBranch2(String branch2) {
		this.branch2 = branch2;
	}
	
	public String getSourceCode() {
		String source=null;
		
		if( opcode.equals(GlobalConstants.OPCODE_ADD)){
			source = new String("add " + VarText(this.src1) +","+ VarText(this.src2) + " =&gt; " + VarText(this.dest) );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_SUB)){
			source = new String("sub " + VarText(this.src1) +","+ VarText(this.src2) + " =&gt; " + VarText(this.dest) );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_MULT)){
			source = new String("mult " +  VarText(this.src1) +","+ VarText(this.src2) + " =&gt; " + VarText(this.dest) );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_DIV)){
			source = new String("div " +  VarText(this.src1) +","+ VarText(this.src2) + " =&gt; " + VarText(this.dest) );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_MOD)){
			source = new String("div " +  VarText(this.src1) +","+ VarText(this.src2) + " =&gt; " + VarText(this.dest) );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_LOADI)){// special because src is a number/immidiate value
			source = new String("loadI " + this.src1.getName() +" =&gt; " + VarText(this.dest) );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_LT)){
			source = new String("cmp_LT " + VarText(this.src1) +","+ VarText(this.src2) + " =&gt; " + VarText(this.dest) );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_LE)){
			source = new String("cmp_LE " + VarText(this.src1) +","+ VarText(this.src2) + " =&gt; " + VarText(this.dest) );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_GT)){
			source = new String("cmp_GT " + VarText(this.src1) +","+ VarText(this.src2) + " =&gt; " + VarText(this.dest) );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_GE)){
			source = new String("cmp_GE " + VarText(this.src1) +","+ VarText(this.src2) + " =&gt; " + VarText(this.dest) );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_CMPEQ)){
			source = new String("cmp_EQ " + VarText(this.src1) +","+ VarText(this.src2) + " =&gt; " + VarText(this.dest) );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_CMPNE)){
			source = new String("cmp_NE " + VarText(this.src1) +","+ VarText(this.src2) + " =&gt; " + VarText(this.dest) );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_CBR)){ // in case of cbr src2 to the 1st branch destination
			source = new String("cbr " + VarText(this.src1) + " -&gt; " + this.branch1 + "," + this.branch2);
		}
		else if( opcode.equals(GlobalConstants.OPCODE_JUMPI)){ // both source are null
			source = new String("jumpI" + " -&gt; " + this.branch1 );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_READINT)){ // both source are null
			source = new String("readInt" + " =&gt; " + VarText(this.dest) );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_WRITEINT)){ // both source are null
			source = new String("writeInt" + " " + VarText(this.src1) );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_I2I)){ // both source are null
			source = new String("i2i " + VarText(this.src1) +  " =&gt; " + VarText(this.dest) );
		}
//		else if(opcode.equals(GlobalConstants.OPCODE_LABEL)) { // not an instruction but a label
//			source = new String(this.src1 + ":");
//			this.isLabel = true;
//		}
		else if(opcode.equals(GlobalConstants.OPCODE_EXIT)) { // not an instruction but a label
			source = new String("exit");
		
		}
		else {
			System.out.println("ERROR in SSA for Instruction source code");
		}
		
	
		
		return source;
	}

	private String VarText( VarWithNumbers var ) {
		String varName = new String( var.getName());
		varName = varName + "." + var.getNumber();
		return varName;
	}
	
	
	

}
