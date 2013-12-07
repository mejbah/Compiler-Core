package edu.utsa.tl13;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CfgSSAForm {
	
	private String textToWrite;
	
	private int nodeSerial; // for block
	HashMap<String, Integer> blockNodeNo;
	public CfgSSAForm() {
		textToWrite = "digraph graphviz { \n" + 
	    		"node [shape = none];\n" + 
	    		"edge [tailport = s];\n" +
	    		"entry\n" +
	    		"subgraph cluster {\n" +
	    		"color=\"/x11/white\"\n";
		
		nodeSerial = 0;
		blockNodeNo = new HashMap<String, Integer>();
		
	}
	/*
	 * create new node in graph. Must be followed by a closeTextCurrentNode() fuction call
	 */
	private void createNewNode( String blockName, int nodeNo ) {
			
		textToWrite = textToWrite + "\nn"+ nodeNo + " [label=<<table border=\"0\">";
		
		String blockText = "<tr><td border=\"1\" colspan=\"3\">" + blockName + "</td></tr>";
		textToWrite = textToWrite + blockText;
	}
	
	private void addInsructionText(String s ) {
		textToWrite = textToWrite + "<tr><td align=\"left\">"+ s+"</td></tr>";
	}
	
	private void closeTextCurrentNode() {
		String closingText = "</table>>,fillcolor=\"/x11/white\",shape=box]\n";
		textToWrite = textToWrite + closingText;
		
//		nodeSerial++;
	}
	
	public String getTextToWrite() {
		
		String endText = "\n}\nentry -> n0\nn" + (nodeSerial-1) + "-> exit" +  "\n }";
		//TODO:add exit instruction
//		addInsructionText("exit");
//		closeTextCurrentNode();
		
		textToWrite = textToWrite + endText;
		return textToWrite;
	}
	
	/**
	 * 
	 * @param parentNodeNo : nodeserial for parent
	 * @param childNodeNo : nodeserial for child
	 */
	private void addEdge( int parentNodeNo, int childNodeNo ) {
		String edge = "n" + parentNodeNo + " -> n" + childNodeNo + "\n"; 
		textToWrite = textToWrite + edge;
	}
	
	
	private void printSSAInstructions( BlockSSA b, Set<String> blocksVisited ) {
		if( ! blocksVisited.contains(b.getBlockName())) {
		blocksVisited.add(b.getBlockName());	
		
		if(blockNodeNo.get(b.getBlockName()) == null) {
			blockNodeNo.put(b.getBlockName(), nodeSerial);
			nodeSerial++;
		}
		
		//System.out.println("Block : " + b.getBlockName());
		
		createNewNode(b.getBlockName(), blockNodeNo.get(b.getBlockName()));
		
		for( String var : b.getPhiVars()) {
			PhiFunction p = b.getPhiFunctions().get(var);
			addInsructionText(p.printText());
			//System.out.println(p.printText());
		}
		for (SSAInstruction inst : b.getInstructions()) {
			addInsructionText(inst.getSourceCode());
			//System.out.println(inst.getSourceCode());
		}
		
		closeTextCurrentNode(); // nodeSerial increased here
		if(b.getSuccessor1() == null) {
			return;
		}
		else {
			if(blockNodeNo.get(b.getSuccessor1().getBlockName()) == null) {
				blockNodeNo.put(b.getSuccessor1().getBlockName(), nodeSerial);
				nodeSerial++;
			}
			addEdge(blockNodeNo.get(b.getBlockName()), blockNodeNo.get(b.getSuccessor1().getBlockName()));
			printSSAInstructions(b.getSuccessor1(), blocksVisited);
			if( b.getSuccessor2() != null ) {
				if(blockNodeNo.get(b.getSuccessor2().getBlockName()) == null) {
					blockNodeNo.put(b.getSuccessor2().getBlockName(), nodeSerial);
					nodeSerial++;
				}
				addEdge(blockNodeNo.get(b.getBlockName()), blockNodeNo.get(b.getSuccessor2().getBlockName()));
				printSSAInstructions(b.getSuccessor2(), blocksVisited);
			}
		}
		
		} 
		
	}
	
	public void graphVizText( BlockSSA root ) {
		
		Set<String> blockNames = new HashSet<String>();
		if(root != null)
			printSSAInstructions(root, blockNames);
	}
}
