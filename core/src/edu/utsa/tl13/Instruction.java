package edu.utsa.tl13;

public class Instruction {
	
	private String opcode;
	private String src1;
	private String src2;
	private String dest;
	private String sourceCode;
	
	public Instruction(String opcode, String src1, String src2, String dest) {
		super();
		this.opcode = opcode;
		this.src1 = src1;
		this.src2 = src2;
		this.dest = dest;
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
		else if( opcode.equals(GlobalConstants.OPCODE_LOADI)){
			source = new String("load " + this.src1 +" =&gt; " + this.dest );
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
		else if( opcode.equals(GlobalConstants.OPCODE_CBR)){ // in case of cbr src2 to the 1st branch destination
			source = new String("cbr " + this.src1 + " -> " + this.src2 + "," + this.dest);
		}
		else if( opcode.equals(GlobalConstants.OPCODE_JUMPI)){ // both source are null
			source = new String("jumpI" + " -> " + this.dest );
		}
		else {
			System.out.println("ERROR in ILOC Opcode for Instruction");
		}
		
	
		
		return source;
	}

	
	
}
