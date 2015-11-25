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
	
	public void visit(ProgramNode p);
	public void visit(StatementListNode s);
	public void visit(StatementNode s);
//	public void visit(IfStatementNode ifs);
//	public void visit(WhileStatementNode whls );
//	public void visit(AssignmentNode asgn);
//  public void visit(WriteIntNode wr);
	public void visit(ExpressionNode ex);
	public void visit(SimpleExpressionNode sex );
	public void visit(TermNode tn );
	public void visit(FactorNode f);
	public void visit(OperandNode op);
	
	
}
