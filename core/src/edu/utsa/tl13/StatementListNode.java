package edu.utsa.tl13;

import java.util.ArrayList;

public class StatementListNode implements Visitable{
	private ArrayList<StatementNode> statementList;
	boolean type_ok;
	
	public StatementListNode() {
		statementList = new ArrayList<StatementNode>();
	}
	public ArrayList<StatementNode> getStatementList() {
		return statementList;
	}
	public void setStatementList(ArrayList<StatementNode> statementList) {
		this.statementList = statementList;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit(this);
	}
	
	public boolean typeOkay() {
		this.type_ok = true;
		for( StatementNode s : statementList ) {
			if( s instanceof IfStatementNode ) {
				this.type_ok = ((IfStatementNode) s).typeOkay();
				if( this.type_ok == false ) break;
			}
			else if( s instanceof WhileStatementNode ) {
				this.type_ok = ((WhileStatementNode) s).typeOkay();
				if( this.type_ok == false ) break;
				
			}
			else if( s instanceof AssignmentNode ) {
				this.type_ok = ((AssignmentNode) s).typeOkay();
				if( this.type_ok == false ) break;
				
			}
			else if( s instanceof WriteIntNode ) {
				this.type_ok = ((WriteIntNode) s).typeOkay();
				if( this.type_ok == false ) break;
				
			}
			
				
				
		}
		return this.type_ok;
	}
	
	
	

}
