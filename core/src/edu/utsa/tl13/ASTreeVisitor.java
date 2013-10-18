package edu.utsa.tl13;

public class ASTreeVisitor implements Visitor {
	
	String textToWrite = "digraph parseTree {" + "\nordering=out; \nnode [shape = box, style = filled];\n";
	int nodeSerial = 0;
	int currentParentNo = 0;
	String labelText = "label=";
	String colorText = ",fillcolor=\"/x11/white\"";
	String shapeText = ",shape=box";
	@Override
	public void visit(Program p) {
		// TODO Auto-generated method stub
		
		
	} 

	@Override
	public void visit(Declarations ds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TypeRule t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(StatementSequence s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Statement s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Expression e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ElseClause el) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SimpleExpression e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Term t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Factor t) {
		// TODO Auto-generated method stub
		
	}
}
