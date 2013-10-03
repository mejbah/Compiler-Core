package edu.utsa.tl13;

import java.io.FileReader;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;


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

	System.out.println("Input file: " + inputFileName);
	System.out.println("Output file: " + parseOutName);

	FileReader tl12In = new FileReader(inputFileName);
	OutputStream parseOut = new FileOutputStream(parseOutName);
	
	//Parser parser = new Parser(tl12In, parseOut);
	//parser.program();
	
	// Remove code below:
	java.io.LineNumberReader lr = new java.io.LineNumberReader(tl12In);
	java.io.PrintStream ps = new java.io.PrintStream(parseOut);
	ps.println("[valid dot file produced by parser]");
	ps.println("Input contained: ");
	int num = lr.getLineNumber();
	String line = lr.readLine();
	while (line != null) {
	  ps.print(num + ": ");
	  for(String s : line.split("\\s+"))
		  ps.print("\"" + s + "\" - ");
	  ps.println();
	  num = lr.getLineNumber();
	  line = lr.readLine();
	}	
	lr.close();
	ps.close();
    }
}
