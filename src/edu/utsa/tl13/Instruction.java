package edu.utsa.tl13;

public class Instruction {
	
	private String opcode;
	private String src1;
	private String src2;
	private String dest;
	private String sourceCode;
	boolean isLabel; // if it is a label for jump, src1 contains the label string
	
	public Instruction(String opcode, String src1, String src2, String dest) {
		super();
		this.opcode = opcode;
		this.src1 = src1;
		this.src2 = src2;
		this.dest = dest;
		this.isLabel = false;
		this.sourceCode = getSourceCode(opcode);
	}

	public String getOpcode() {
		return opcode;
	}

	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}

	public String getSrc1() {
		return src1;
	}

	public void setSrc1(String src1) {
		this.src1 = src1;
	}

	public String getSrc2() {
		return src2;
	}

	public void setSrc2(String src2) {
		this.src2 = src2;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public String getSourceCode() {
		//add instruction in ILOC instruction list : bad bad / hacking
		ILOCsingleton.getInstance().getIlocInstructionList().add(this);
		return sourceCode;
	}
	public String getInstructionSourceText() {
		//adding another function for debugging
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	
	public String getSourceCode( String opcode ) {
		String source=null;
		
		if( opcode.equals(GlobalConstants.OPCODE_ADD)){
			source = new String("add " + this.src1 +","+ this.src2 + " =&gt; " + this.dest );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_SUB)){
			source = new String("sub " + this.src1 +","+ this.src2 + " =&gt; " + this.dest );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_MULT)){
			source = new String("mult " + this.src1 +","+ this.src2 + " =&gt; " + this.dest );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_DIV)){
			source = new String("div " + this.src1 +","+ this.src2 + " =&gt; " + this.dest );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_MOD)){
			source = new String("div " + this.src1 +","+ this.src2 + " =&gt; " + this.dest );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_LOADI)){
			source = new String("loadI " + this.src1 +" =&gt; " + this.dest );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_LT)){
			source = new String("cmp_LT " + this.src1 +","+ this.src2 +" =&gt; " + this.dest );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_LE)){
			source = new String("cmp_LE " + this.src1 +","+ this.src2 +" =&gt; " + this.dest );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_GT)){
			source = new String("cmp_GT " + this.src1 +","+ this.src2 +" =&gt; " + this.dest );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_GE)){
			source = new String("cmp_GE " + this.src1 +","+ this.src2 +" =&gt; " + this.dest );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_CMPEQ)){
			source = new String("cmp_EQ " + this.src1 +","+ this.src2 +" =&gt; " + this.dest );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_CMPNE)){
			source = new String("cmp_NE " + this.src1 +","+ this.src2 +" =&gt; " + this.dest );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_CBR)){ // in case of cbr src2 to the 1st branch destination
			source = new String("cbr " + this.src1 + " -&gt; " + this.src2 + "," + this.dest);
		}
		else if( opcode.equals(GlobalConstants.OPCODE_JUMPI)){ // both source are null
			source = new String("jumpI" + " -&gt; " + this.dest );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_READINT)){ // both source are null
			source = new String("readInt" + " =&gt; " + this.dest );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_WRITEINT)){ // both source are null
			source = new String("writeInt" + " " + this.src1 );
		}
		else if( opcode.equals(GlobalConstants.OPCODE_I2I)){ // both source are null
			source = new String("i2i " + this.src1 +  " =&gt; " + this.dest );
		}
		else if(opcode.equals(GlobalConstants.OPCODE_LABEL)) { // not an instruction but a label
			source = new String(this.src1 + ":");
			this.isLabel = true;
		}
		else if(opcode.equals(GlobalConstants.OPCODE_EXIT)) { // not an instruction but a label
			source = new String("exit");
		
		}
		else {
			System.out.println("ERROR in ILOC Opcode for Instruction");
		}
		
	
		
		return source;
	}

	
	
}
