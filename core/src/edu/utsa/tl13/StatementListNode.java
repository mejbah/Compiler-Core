package edu.utsa.tl13;

import java.util.ArrayList;

public class StatementListNode {
	private ArrayList<StatementNode> statementList;
	public StatementListNode() {
		statementList = new ArrayList<StatementNode>();
	}
	public ArrayList<StatementNode> getStatementList() {
		return statementList;
	}
	public void setStatementList(ArrayList<StatementNode> statementList) {
		this.statementList = statementList;
	}
	
	
	

}