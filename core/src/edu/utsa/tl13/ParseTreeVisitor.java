package edu.utsa.tl13;

public class ParseTreeVisitor implements Visitor {
	String textToWrite = "digraph parseTree {" + "\nordering=out; \nnode [shape = box, style = filled];\n";
	int nodeSerial = 0;
	int currentParentNo = 0;
	String labelText = "label=";
	String colorText = ",fillcolor=\"/x11/white\"";
	String shapeText = ",shape=box";
	
	public void visit(Program p){
		
        int parent = addNewNode(new String("program"));
        currentParentNo = parent;
        int child_1 = addNewNode( new String("PROGRAM"));
        addEdge(currentParentNo , child_1);
        int child_2 = addNewNode(new String("declarations"));
        addEdge(currentParentNo , child_2);
        
        Declarations ds = p.getDeclarations();
        currentParentNo = child_2;
        ds.accept(this);
        currentParentNo = parent;
        
        int child_3 = addNewNode(new String("BEGIN"));
        addEdge(currentParentNo, child_3);
        
        int child_4 = addNewNode(new String("statementSequence"));
        addEdge(currentParentNo, child_4);
        
        StatementSequence st = p.getStatements();
        currentParentNo = child_4;
        st.accept(this);
        currentParentNo = parent;
        
        int child_5 = addNewNode(new String("END"));
        addEdge(currentParentNo, child_5);
        
        textToWrite = textToWrite + "\n }";
        
	}
	
	public void visit( Declarations ds) {
		
		int parent = currentParentNo;
		int child_1, child_2, child_3, child_4, child_5, child_6;
		if( ds.getDeclarationList().isEmpty() ) { 
			//empty rule
			child_1 = addNewNode("&#949;");
			addEdge(parent, child_1);
		}
		else {
			int sub_parent = parent;
			for( DeclarationUnit d : ds.getDeclarationList()) { // is this if block really needed?
				if( d == null ) {
					child_1 = addNewNode("&#949;");
					addEdge(sub_parent, child_1);
					break;
				}
				child_1 = addNewNode("VAR");
				addEdge(sub_parent, child_1);
				child_2 = addNewNode("ident:"+ d.getIdent());
				addEdge(sub_parent, child_2);
				child_3 = addNewNode("AS");
				addEdge(sub_parent, child_3);
				child_4 = addNewNode("TYPE");
				addEdge(sub_parent, child_4);
				
				currentParentNo = child_4;
				TypeRule t = d.getType();
				t.accept(this);
				currentParentNo = sub_parent;
				
				child_5 = addNewNode("SC");
				addEdge(sub_parent, child_5);
				
				child_6 = addNewNode("declarations");
				addEdge(sub_parent, child_6);
				
				sub_parent = child_6;
				
			}
			// ends with empty declarations
			child_1 = addNewNode("&#949;");
			addEdge(sub_parent, child_1);
				
			
		}
	}
	
	public void visit( TypeRule t) {
		int child = addNewNode(t.getType());
		addEdge(currentParentNo, child);
	}
	
	public void visit( StatementSequence s ) {
		
		int parent = currentParentNo;
		int child_1, child_2, child_3;
		if( s.getStatements().isEmpty() ) { // is this if block really needed?
			//empty rule
			child_1 = addNewNode("&#949;");
			addEdge(parent, child_1);
		}
		else {
			int sub_parent = parent;
			for( Statement st : s.getStatements()) {
				if( st == null ) {
					child_1 = addNewNode("&#949;");
					addEdge(sub_parent, child_1);
					break;
				}
				
				child_1 = addNewNode("statement");
				addEdge(sub_parent, child_1);
				currentParentNo = child_1;
				st.accept(this);
				currentParentNo = sub_parent;
				
				child_2 = addNewNode("SC");
				addEdge(sub_parent, child_2);
				
				child_3 = addNewNode("statementSequence");
				addEdge(sub_parent, child_3);
				
				sub_parent = child_3;
				
			}
			// ends with empty stamentSeq
			child_1 = addNewNode("&#949;");
			addEdge(sub_parent, child_1);
				
			
		}
		
	}
	
	public void visit( Statement s ) {
		int parent = currentParentNo;
		int child_1;
		if( s instanceof IfStatement ) {
			child_1 = addNewNode("ifstatment");
			addEdge(parent, child_1);
			parent =  child_1;
			
			child_1 = addNewNode("IF");
			addEdge(parent, child_1);
			
			int child_2 = addNewNode("expression");
			addEdge(parent, child_2);
			currentParentNo =  child_2;
			((IfStatement) s).getExpression().accept(this);
			currentParentNo = parent;
			
			int child_3 = addNewNode("THEN");
			addEdge(parent, child_3);
			
			int child_4 = addNewNode("StatementSequence");
			addEdge(parent, child_4);
			currentParentNo = child_4;
			((IfStatement) s).getStatements().accept(this);
			currentParentNo = parent;
			
			int child_5 = addNewNode("elseClause");
			addEdge(parent, child_5);
			currentParentNo = child_5;
			ElseClause el = ((IfStatement) s).getElseClause();
			el.accept(this);
			currentParentNo = parent;
			
			int child_6 = addNewNode("END");
			addEdge(parent, child_6);
			
		}
	}
	public void visit( ElseClause el ) {
		int child_1;
		int parent = currentParentNo;
		if(el.getStatements() == null) {
			child_1 = addNewNode("&#949;");
			addEdge( parent , child_1);
				
		}
		else {
			child_1 = addNewNode("ELSE");
			addEdge( parent , child_1);
			
			int child_2 = addNewNode("StatementSequence");
			addEdge( parent , child_2);
			currentParentNo = child_2;
			el.getStatements().accept(this);
			currentParentNo = parent;
		}
		
		
	}
	public void visit( Expression e ) {
		
		
	}
	int addNewNode( String label ) {
		nodeSerial++;
		String node = "n" + nodeSerial + " ["  + labelText + "\"" + label + "\"" + colorText + shapeText + " ]" +"\n";
		textToWrite = textToWrite + node;
		
		return nodeSerial;
	}
	public String getTextToWrite() {
		return textToWrite;
	}
	
	void addEdge( int parentNodeNo, int childNodeNo ) {
		String edge = "n" + parentNodeNo + " -> n" + childNodeNo + "\n"; 
		textToWrite = textToWrite + edge;
	}

}
