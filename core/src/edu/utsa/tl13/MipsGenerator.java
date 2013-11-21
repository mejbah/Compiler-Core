package edu.utsa.tl13;

import java.util.HashMap;

public class MipsGenerator {
    private String sourceCodeText;
    private int offset;
    private HashMap<String, String> regOffset;
    
	public MipsGenerator() {
		regOffset = new HashMap<String, String>();
		sourceCodeText = new String();
		offset = 0;
		initialize();
	}
	
	private void initialize() {
		sourceCodeText = sourceCodeText + "\t.data\n" 
				+ "newline:\t .asciiz \"\\n\"\n"
				+ "\t.text\n"
				+ "\t.globl main\n"
				+ "main:\n"
				+ "\tli $fp, 0x7ffffffc\n\n";
/*		
		.data
		newline:	.asciiz "\n"
			.text
			.globl main
		main:
			li $fp, 0x7ffffffc
*/

	}
	
	public String getOffset( String register ) {
		String offset = regOffset.get(register);
		if(offset == null ) {
			String value = getNextOffset();
			regOffset.put(register, value );
			return value;
		}
		else {
			return offset;
		}
	}
	
	private String getNextOffset() {
		int currentOffset = this.offset;
		this.offset = this.offset - 4;
		return Integer.toString(currentOffset);
	}
	
	public String getSourceCodeText() {
		return sourceCodeText;
	}
	/*
	 * generate mips source lines for mips from an iloc instuction
	 */
	public String getMipsSourceInstructions( Instruction inst ) {
		String lines = null;
		String opcode = inst.getOpcode();
		
		if( opcode.equals(GlobalConstants.OPCODE_ADD)){
			/*
			 * # add r_SQRT, r3 => r4
				lw $t1, -4($fp)
				lw $t2, -20($fp)
				addu $t0, $t1, $t2
				sw $t0, -24($fp)
			 */
			lines = new String("\t# " + inst.getInstructionSourceText() + "\n"); 
			lines = lines +"\tlw $t1, " + getOffset(inst.getSrc1()) + "($fp)\n";
			lines = lines +"\tlw $t2, " + getOffset(inst.getSrc2()) + "($fp)\n";
			lines = lines +"\taddu $t0, $t1, $t2\n";  
			lines = lines + "\tsw $t0, " + getOffset(inst.getDest()) +"($fp)\n";
			lines = lines + "\n";
			
		}
		else if( opcode.equals(GlobalConstants.OPCODE_SUB)){
			lines = new String("\t# " + inst.getInstructionSourceText() + "\n"); 
			lines = lines +"\tlw $t1, " + getOffset(inst.getSrc1()) + "($fp)\n";
			lines = lines +"\tlw $t2, " + getOffset(inst.getSrc2()) + "($fp)\n";
			lines = lines +"\tsubu $t0, $t1, $t2\n";  
			lines = lines + "\tsw $t0, " + getOffset(inst.getDest()) +"($fp)\n";
			lines = lines + "\n";
		}
		else if( opcode.equals(GlobalConstants.OPCODE_MULT)){
			/*
			 * # mult r_SQRT, r_SQRT => r1
				lw $t1, -4($fp)
				lw $t2, -4($fp)
				mul $t0, $t1, $t2
				sw $t0, -12($fp)
			 */
			lines = new String("\t# " + inst.getInstructionSourceText() + "\n"); 
			lines = lines +"\tlw $t1, " + getOffset(inst.getSrc1()) + "($fp)\n";
			lines = lines +"\tlw $t2, " + getOffset(inst.getSrc2()) + "($fp)\n";
			lines = lines +"\tmul $t0, $t1, $t2\n";  
			lines = lines + "\tsw $t0, " + getOffset(inst.getDest()) +"($fp)\n";
			lines = lines + "\n";
		}
//		else if( opcode.equals(GlobalConstants.OPCODE_DIV)){
//			source = new String("div " + this.src1 +","+ this.src2 + " =&gt; " + this.dest );
//		}
//		else if( opcode.equals(GlobalConstants.OPCODE_MOD)){
//			source = new String("div " + this.src1 +","+ this.src2 + " =&gt; " + this.dest );
//		}
		else if( opcode.equals(GlobalConstants.OPCODE_LOADI)){
			lines = new String("\t# " + inst.getInstructionSourceText() + "\n"); 
			lines = lines +"\tli $t0, " + inst.getSrc1() + "\n";
			lines = lines + "\tsw $t0, " + getOffset(inst.getDest()) +"($fp)\n";
			lines = lines + "\n";
			
		}
//		else if( opcode.equals(GlobalConstants.OPCODE_LT)){
//			source = new String("cmp_LT " + this.src1 +","+ this.src2 +" =&gt; " + this.dest );
//		}
		else if( opcode.equals(GlobalConstants.OPCODE_LE)){
			/*
			 * # cmp_LE r1, r_N => r2
				lw $t1, -12($fp)
				lw $t2, 0($fp)
				sle $t0, $t1, $t2
				sw $t0, -16($fp)
			 */
			lines = new String("\t# " + inst.getInstructionSourceText() + "\n"); 
			lines = lines +"\tlw $t1, " + getOffset(inst.getSrc1()) + "($fp)\n";
			lines = lines +"\tlw $t2, " + getOffset(inst.getSrc2()) + "($fp)\n";
			lines = lines +"\tsle $t0, $t1, $t2\n";  
			lines = lines + "\tsw $t0, " + getOffset(inst.getDest()) +"($fp)\n";
			lines = lines + "\n";
		}
//		else if( opcode.equals(GlobalConstants.OPCODE_GT)){
//			source = new String("cmp_GT " + this.src1 +","+ this.src2 +" =&gt; " + this.dest );
//		}
//		else if( opcode.equals(GlobalConstants.OPCODE_GE)){
//			source = new String("cmp_GE " + this.src1 +","+ this.src2 +" =&gt; " + this.dest );
//		}
//		else if( opcode.equals(GlobalConstants.OPCODE_CMPEQ)){
//			source = new String("cmp_EQ " + this.src1 +","+ this.src2 +" =&gt; " + this.dest );
//		}
//		else if( opcode.equals(GlobalConstants.OPCODE_CMPNE)){
//			source = new String("cmp_NE " + this.src1 +","+ this.src2 +" =&gt; " + this.dest );
//		}
		else if( opcode.equals(GlobalConstants.OPCODE_CBR)){ // in case of cbr src2 to the 1st branch destination
			/*
			 * # cbr r2 -> B3, B4
				lw $t0, -16($fp)
				bne $t0, $zero, B3
				j B4
			 */
			lines = new String("\t# " + inst.getInstructionSourceText() + "\n"); 
			lines = lines +"\tlw $t0, " + getOffset(inst.getSrc1()) + "($fp)\n";
			lines = lines + "\tbne $t0, $zero, " + inst.getSrc2() + "\n";
			lines = lines + "\n";
			lines = lines + "\tj " + inst.getDest() + "\n";
			lines = lines + "\n";
		}
		else if( opcode.equals(GlobalConstants.OPCODE_JUMPI)){ // both source are null
			//j B2
			lines = new String("\t# " + inst.getInstructionSourceText() + "\n"); 
			lines = lines +"\tj " + inst.getDest() + "\n";
			
			lines = lines + "\n";
		}
		else if( opcode.equals(GlobalConstants.OPCODE_READINT)){ // both source are null
			/*
			 * 	li $v0, 5
				syscall 
				add $t0, $v0, $zero
			 */
			lines = new String("\t# " + inst.getInstructionSourceText() + "\n"); 
			lines = lines +"\tli $v0, " + "5" + "\n";
			lines = lines +"\tsyscall\n";
			lines = lines +"\tadd $t0, $v0, $zero\n";
			lines = lines + "\tsw $t0, " + getOffset(inst.getDest()) +"($fp)\n";
			lines = lines + "\n";
		}
		else if( opcode.equals(GlobalConstants.OPCODE_WRITEINT)){ // both source are null
			/*
			 * # writeInt r_SQRT 
	li $v0, 1
	lw $t1, -4($fp)
	add $a0, $t1, $zero
	syscall 
	li $v0, 4
	la $a0, newline
	syscall 
			 */
			lines = new String("\t# " + inst.getInstructionSourceText() + "\n"); 
			lines = lines +"\tli $v0, " + "1" + "\n";
			lines = lines +"\tlw $t1, " + getOffset(inst.getSrc1()) + "($fp)\n";
			lines = lines +"\tadd $a0, $t1, $zero\n";
			lines = lines +"\tsyscall\n";
			lines = lines +"\tli $v0, 4\n";
			lines = lines +"\tla $a0, newline\n";
			lines = lines +"\tsyscall\n";
			lines = lines + "\n";
		}
		else if( opcode.equals(GlobalConstants.OPCODE_I2I)){ // both source are null
			/*
			 * lw $t1, -8($fp)
				add $t0, $t1, $zero
				sw $t0, -4($fp)
			 */
			lines = new String("\t# " + inst.getInstructionSourceText() + "\n"); 
			lines = lines +"\tlw $t1, " + getOffset(inst.getSrc1()) + "($fp)\n";
			lines = lines +"\tadd $t0, $t1, $zero" + "\n";
			lines = lines + "\tsw $t0, " + getOffset(inst.getDest()) +"($fp)\n";
			lines = lines + "\n";
		}
		else if(opcode.equals(GlobalConstants.OPCODE_LABEL)) { // not an instruction but a label
			lines = new String(inst.getSrc1() +":" + "\n");
		}
		else if(opcode.equals(GlobalConstants.OPCODE_EXIT)) { // not an instruction but a label
		/*
		 * # exit  
	li $v0, 10
	syscall 
		 */
			lines = new String("\t#exit" + "\n");
			lines = lines + "\tli $v0, 10\n";
			lines = lines + "\tsyscall\n";
		}
		else {
			System.out.println("ERROR in MIPS Opcode for Instruction");
		}
		return lines;
		
	}
	
	public void addMipsForILOC( Instruction inst ) {
		sourceCodeText += getMipsSourceInstructions(inst);
	}
	

}
