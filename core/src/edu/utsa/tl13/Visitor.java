package edu.utsa.tl13;

public interface Visitor {
	public void visit(Program p);
	public void visit(Declarations ds);
	public void visit(TypeRule t);
	public void visit(StatementSequence s);
	public void visit(Statement s);
	public void visit(Expression e);
	public void visit(ElseClause el);
	public void visit(SimpleExpression e);
	public void visit(Term t);
	public void visit(Factor t);
}
