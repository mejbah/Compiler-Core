package edu.utsa.tl13;

import java.util.ArrayList;
import java.util.ListIterator;

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
/*
 * need to traverse list in reverse order
 * : stored statements in list recursively in reverse order			
 */
			ArrayList<Statement>statementList = s.getStatements(); 
			ListIterator<Statement> li = statementList.listIterator( statementList.size());
			while( li.hasPrevious() ) {
				Statement st = li.previous();
//			for( Statement st : s.getStatements()) {
//				if( st == null ) {
//					child_1 = addNewNode("&#949;");
//					addEdge(sub_parent, child_1);
//					break;
//				}
				
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
		else if( s instanceof WhileStatement ) {
			child_1 = addNewNode("whilestatment");
			addEdge(parent, child_1);
			parent =  child_1;
			
			child_1 = addNewNode("WHILE");
			addEdge(parent, child_1);
			
			int child_2 = addNewNode("expression");
			addEdge(parent, child_2);
			currentParentNo =  child_2;
			((WhileStatement)s).getExpression().accept(this);
			currentParentNo = parent;
			
			int child_3 = addNewNode("DO");
			addEdge(parent, child_3);
			
			int child_4 = addNewNode("StatementSequence");
			addEdge(parent, child_4);
			currentParentNo = child_4;
			((WhileStatement) s).getStatements().accept(this);
			currentParentNo = parent;
						
			int child_5 = addNewNode("END");
			addEdge(parent, child_5);
			
		}
		else if( s instanceof Assignment ) {
			child_1 = addNewNode(((Assignment) s).getId().getType());
			addEdge(parent, child_1);			
			int child_2 = addNewNode("ASGN");
			addEdge(parent, child_2);
			
			if( ((Assignment) s).getExpr() != null) {
				int child_3 = addNewNode("expression");
				addEdge(parent, child_3);
				currentParentNo =  child_3;
				((Assignment)s).getExpr().accept(this);
				currentParentNo = parent;
			}
			else {
				if( ((Assignment) s).getReadInt() != null ) {
					int child_3 = addNewNode("readInt");
					addEdge(parent, child_3);
				}
			
			}
			int id_node = addNewNode(((Assignment) s).getId().getWord());
			addEdge(child_1, id_node);
			
		}
		else if( s instanceof WriteInt ) {
			child_1 = addNewNode("WriteInt");
			addEdge(parent, child_1);
			int child_2 = addNewNode("expression");
			addEdge(parent, child_2);
			currentParentNo = child_2;
			((WriteInt) s).getExpr().accept(this);
			currentParentNo = parent;
			
		}
		else {
			System.err.println("Statement of no valid type!!");
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
		
		int parent = currentParentNo;
		int child_1 = addNewNode("SimpleExpression");
		addEdge(parent, child_1);
		currentParentNo = child_1;
		e.getSimpleExpression().accept(this);
		currentParentNo = parent;
		ExpressionPart exprPart;

		if( (exprPart = e.getExprPart()) != null ) {
			Token t;
			if( (t = exprPart.getOp()) !=null) {
				int child_2 = addNewNode( t.getWord() );
				addEdge(parent, child_2);
			
				int child_3 = addNewNode("SimpleExpression");
				addEdge(parent, child_3);
				currentParentNo = child_3;
				exprPart.getSimpleExpression().accept(this);
				currentParentNo = parent;
			}
		}
		
		
	}
	
	public void visit( SimpleExpression sExpr) {
		int parent = currentParentNo;
		int child_1 = addNewNode("Term");
		addEdge(parent, child_1);
		currentParentNo = child_1;
		sExpr.getTerm().accept(this);
		currentParentNo = parent;
		
		SimpleExpressionPart sExprPart;
		if( (sExprPart = sExpr.getSExprPart()) != null ) {
			Token t;
			if( (t = sExprPart.getOp()) != null ) {
				int child_2 = addNewNode( t.getWord());
				addEdge(parent, child_2);
			
				int child_3 = addNewNode("Term");
				addEdge(parent, child_3);
				currentParentNo = child_3;
				sExprPart.getTerm().accept(this);
				currentParentNo = parent;
			}
		}
		
	}
	
	public void visit( Term t ) {
		int parent = currentParentNo;
		int child_1 = addNewNode("Factor");
		addEdge(parent, child_1);
		currentParentNo = child_1;
		t.getFactor().accept(this);
		currentParentNo = parent;
		
		TermPart termPart;
		if( (termPart = t.gettPart()) != null ) {
			Token op;
			if( (op = termPart.getOp2()) != null ) {
				int child_2 = addNewNode( op.getWord());
				addEdge(parent, child_2);
			
				int child_3 = addNewNode("Factor");
				addEdge(parent, child_3);
				currentParentNo = child_3;
				termPart.getFactor().accept(this);
				currentParentNo = parent;
			}
		}
	}
	
	public void visit( Factor f ) {
		int parent = currentParentNo;
		Expression e;
		Token t;
		int child_1;
		if( ( e = f.getExpression()) != null ) {
			child_1 = addNewNode("LP");
			addEdge(parent, child_1);
			int child_2 = addNewNode("expression");
			addEdge(parent, child_2);
			currentParentNo = child_2;
			e.accept(this);
			currentParentNo = parent;
			int child_3 = addNewNode("RP");
			addEdge(parent, child_3);
		}
		else {
			if( (t = f.getToken()) != null) {
				child_1 = addNewNode( t.getType());
				addEdge(parent, child_1);
				parent = child_1;
				
				child_1 = addNewNode( t.getWord());
				addEdge(parent, child_1);
				
			}
			
		}

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
