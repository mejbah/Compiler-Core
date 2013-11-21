package edu.utsa.tl13;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class Compiler {
    public static void main (String[] args) throws IOException {
        String inputFileName = args[0];
        int baseNameOffset = inputFileName.length() - 5;

        String baseName;
        if (inputFileName.substring(baseNameOffset).equals(".tl13"))
            baseName = inputFileName.substring(0,baseNameOffset);
        else
            throw new RuntimeException("inputFileName does not end in .tl13");

        String parseOutName = baseName + ".pt.dot";
        String astOutName = baseName + ".ast.dot";
        String cfgOutName = baseName + ".iloc.cfg.dot";
        String mipsOutName = baseName + ".s";
	System.out.println("Input file: " + inputFileName);
	System.out.println("Output file: " + parseOutName);
	System.out.println("Output file: " + astOutName);
	System.out.println("Output file: " + cfgOutName);
	System.out.println("Output file: " + mipsOutName);
	Scanner scanner = new Scanner(inputFileName);
	scanner.startScanner();
/*
	for( Token t : scanner.getTokenList() ) {
		System.out.println(t);
	}

*/
	Parser parser = new Parser(scanner.getTokenList());
	String parseTree = parser.parse();
	String asTree = parser.getTextForAST();
	String cfgTree = parser.getTextForCFG();
	
//	for( Instruction inst : ILOCsingleton.getInstance().getIlocInstructionList()) {
//    	System.out.println(inst.getInstructionSourceText());
//    }
	//System.out.println(ILOCsingleton.getInstance().getIlocInstructionList().size());
	//System.out.println(astOutName);
	
	
	/*** generate mips code***/
	MipsGenerator mips = new MipsGenerator();
//	System.out.println(mips.getSourceCodeText());
//	Instruction inst;
//	for( int i=0; i<20; i++){
//		inst = ILOCsingleton.getInstance().getIlocInstructionList().get(i);
//		mips.addMipsForILOC(inst);
//    }
	for( Instruction inst : ILOCsingleton.getInstance().getIlocInstructionList()) {
		mips.addMipsForILOC(inst);
	}
	mips.addMipsForILOC(new Instruction(GlobalConstants.OPCODE_EXIT, null, null, null));
	//System.out.println(mips.getSourceCodeText());
	String mipsText = mips.getSourceCodeText();
	/****end*********/
	
	
	
	// add code for writing output file
	
	OutputStream astOut = new FileOutputStream(astOutName);
	java.io.PrintStream psAst = new java.io.PrintStream(astOut);
	psAst.print(asTree);
	astOut.close();
	psAst.close();
	
	OutputStream parseOut = new FileOutputStream(parseOutName);
	java.io.PrintStream ps = new java.io.PrintStream(parseOut);
	ps.print(parseTree);
	parseOut.close();
	ps.close();
	
	OutputStream cfgOut = new FileOutputStream(cfgOutName);
	java.io.PrintStream psCfg = new java.io.PrintStream(cfgOut);
	psCfg.print(cfgTree);
	cfgOut.close();
	psCfg.close();
	
	OutputStream mipsOut = new FileOutputStream(mipsOutName);
	java.io.PrintStream psMips = new java.io.PrintStream(mipsOut);
	psMips.print(mipsText);
	mipsOut.close();
	psMips.close();
 }
    
}