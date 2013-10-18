package edu.utsa.tl13;

import java.util.ArrayList;
import java.util.ListIterator;

public class Parser {
	ArrayList<Token> tokens;
	int symbol_cursor_pos;
	
	public Parser( ArrayList<Token> tokens ) {
		this.tokens = new ArrayList<Token>(tokens);
		symbol_cursor_pos = 0;
		
	}
	
	boolean checkNextToken( String token_type ) {
		if( tokens.get(symbol_cursor_pos).getType().equals(token_type)) {
			symbol_cursor_pos++;
			return true;
		}
		else
			return false;
		
	}
	
	
	boolean checkNextTokenNoUpdate( String token_type ) {
		if( tokens.get(symbol_cursor_pos).getType().equals(token_type)) {
		
			return true;
		}
		else
			return false;
		
	}
	
	Token getCurrentToken() {
		if(symbol_cursor_pos > 0)
			return tokens.get(symbol_cursor_pos - 1);
		else
			return tokens.get(symbol_cursor_pos);
	}
	
	Token getToken( int pos ) {
		return tokens.get(pos);
	}
	/*
	 * returns text string describing parse tree
	 */
	public String parse() {
		Program p;
		if( (p = program()) == null) {
			System.err.println(" Program error");
			return null;
		}
		
		ParseTreeVisitor visitor = new ParseTreeVisitor();
		p.accept(visitor);
		
		// root is the root of AST
		ProgramNode root = createAST(p);
		
		ASTVisitor visitAST = new ASTVisitor();
		root.accept(visitAST);
		
		//System.out.println(visitor.getTextToWrite());
		//return visitor.getTextToWrite();
		return visitAST.getTextToWrite();
	}
	
	/**
	 * Receives parse tree root node program and constructs AST from Parse Tree
	 * @return root of AST
	 */
	ProgramNode createAST( Program  p) {
		
		Declarations declarationList = new Declarations();	
		ArrayList<DeclarationUnit>declarations = p.getDeclarations().getDeclarationList(); 
		ListIterator<DeclarationUnit>li = declarations.listIterator( declarations.size());
 
		while( li.hasPrevious() ) {
			DeclarationUnit d = li.previous();
			declarationList.addDeclaration(d);
		}
		
		StatementListNode statementList = getStatementList(p.getStatements());
		
		return new ProgramNode(declarationList, statementList);
			
	}
	
	StatementListNode getStatementList( StatementSequence statements) {
		
		StatementListNode statementList = new StatementListNode();
		ArrayList<Statement>stmt = statements.getStatements(); 
		ListIterator<Statement>liStmt = stmt.listIterator( stmt.size());
 
		while( liStmt.hasPrevious() ) {
			Statement s = liStmt.previous();
			if( s instanceof IfStatement ) {
				//statementList.addStatement( new IfStatement(, st, el));
				ExpressionNode expressionNode = getExpressionNode( ((IfStatement) s).getExpression()) ; // traverse expression subtree
				StatementListNode ifStatements= getStatementList(((IfStatement) s).getStatements());
				if( ((IfStatement) s).getElseClause() != null) {
					StatementListNode elseStatementSequence = getStatementList(((IfStatement) s).getElseClause().getStatements());
				
					statementList.getStatementList().add(new IfStatementNode(expressionNode, ifStatements, elseStatementSequence));
				}
				else {
					statementList.getStatementList().add(new IfStatementNode(expressionNode, ifStatements));
				}
				return statementList;
			}
			else if( s instanceof WhileStatement ) {
				ExpressionNode expressionNode = getExpressionNode(((WhileStatement) s).getExpression());
				StatementListNode whileStatements = getStatementList(((WhileStatement) s).getStatements());
				
				statementList.getStatementList().add(new WhileStatementNode(expressionNode, whileStatements));
				return statementList;
			}
			else if( s instanceof Assignment) {
				String identNode = ((Assignment) s).getId().getWord();
				if( ((Assignment) s).getReadInt() == null) { // not a readInt assignment
					ExpressionNode expressionNode = getExpressionNode(((Assignment) s).getExpr());
				
					statementList.getStatementList().add(new AssignmentNode(identNode, expressionNode));
				}
				else {
					String readInt = ((Assignment) s).getReadInt();
					statementList.getStatementList().add(new AssignmentNode(readInt, identNode));
					
				}
				
			}
			else if( s instanceof WriteInt ) {
				ExpressionNode expressionNode = getExpressionNode(((WriteInt) s).getExpr());
				
				statementList.getStatementList().add(new WriteIntNode(expressionNode));
			}
			else
				return null;
			
				
			
		}
		return statementList;

	}
	/**
	 * 
	 * @param expr
	 * @return Expression tree for AST from corresponding Parse Tree
	 */
	
	ExpressionNode getExpressionNode( Expression expr ) {
		
		if( expr.getExprPart().getSimpleExpression() == null) {
			return new ExpressionNode( getSimpleExprNode(expr.getSimpleExpression()));
		}
		else {
			OperandNode op = new OperandNode(expr.getExprPart().getOp().getWord(), expr.getExprPart().getOp().getType());
			return new ExpressionNode( getSimpleExprNode(expr.getSimpleExpression()), getSimpleExprNode(expr.getExprPart().getSimpleExpression()), op);
		}
		
	}
	
	SimpleExpressionNode getSimpleExprNode( SimpleExpression sExpr ) {
		if( sExpr.getSExprPart().getTerm() == null) {
			return new SimpleExpressionNode( getTermNode(sExpr.getTerm()));
		}
		else {
			OperandNode op = new OperandNode(sExpr.getSExprPart().getOp().getWord(), sExpr.getSExprPart().getOp().getType());
			return new SimpleExpressionNode(getTermNode(sExpr.getTerm()), getTermNode(sExpr.getSExprPart().getTerm()), op);
		}
	}
	
	TermNode getTermNode( Term t ) {
		if( t.gettPart().getFactor() == null ) {
			return new TermNode(getFactorNode( t.getFactor()));
		}
		else {
			OperandNode op = new OperandNode(t.gettPart().getOp2().getWord(),t.gettPart().getOp2().getType() );
			return new TermNode( getFactorNode(t.getFactor()), getFactorNode(t.gettPart().getFactor()), op); 
		}
	}
	
	FactorNode getFactorNode( Factor f ) {
		if( f.getExpression() == null ) {
			return new FactorNode( f.getToken().getWord());
		}
		else {
			return new FactorNode(getExpressionNode(f.getExpression()));
		}
	}
	
	
	Program program() {
		if( checkNextToken("PROGRAM") ) {
			Declarations d = declarations();
			if(d != null && checkNextToken("BEGIN")) {
				StatementSequence s = statement_sequence();
				if( s != null && checkNextToken("END")) {
					return new Program( d, s);
				}
				else
				{
					System.err.println(" Syntax Error: in program() " + getCurrentToken());
					return null;
				}
			}
			else
			{
				System.err.println(" Syntax Error: in program() " + getCurrentToken());
				return null;
			}
			
		}
		else
		{
			System.err.println(" Syntax Error : in program() "  + getCurrentToken() );
			return null;
		}
		
	}
	
	Declarations declarations() {
		//first rule
		Declarations dcls;
		String ident;
		if( checkNextToken("VAR") && checkNextToken("ident")) {
			ident = getCurrentToken().getWord();
			if (checkNextToken("AS")){ 
				TypeRule type = type_rule();
				if( type != null && checkNextToken("SC") && (dcls = declarations()) != null) {
					dcls.addDeclaration(new DeclarationUnit(type, ident));
					return dcls;
				
				}
				else {
					System.err.println(" Syntax Error : in declarations() "  + getCurrentToken() );
					return null;
				}
			}
			else {
				System.err.println(" Syntax Error : in declarations() "  + getCurrentToken() );
				return null;
			}
		}
		else if( checkNextTokenNoUpdate("BEGIN")) {
			// empty rule
			return new Declarations();
		}
		else {
			System.err.println(" Syntax Error : in declarations() "  + getCurrentToken() );
			return null;
		}
	}
	
	TypeRule type_rule() {
		if(checkNextToken("INT")) {
			return new TypeRule("INT");
		}
		else if(checkNextToken("BOOL")) {
			return new TypeRule("BOOL");
		}
		else {
			System.err.println(" Syntax Error : in type_rule() "  + getCurrentToken() );
			return null;
		}
	}
	
	StatementSequence statement_sequence() {
		StatementSequence statements;
		Statement s;
		// empty rule
	    if( checkNextTokenNoUpdate("END") || checkNextTokenNoUpdate("ELSE")) {
			return new StatementSequence();
		}
		else if( (s = statement()) != null && checkNextToken("SC") && (statements = statement_sequence())!=null) {
			statements.addStatement(s);
			return statements;
		}
		else {
			System.err.println(" Syntax Error : in statement_sequence() "  + getCurrentToken().getWord() );
			return null;
		}
		
		
	}
	
	Statement statement() {
		if( checkNextTokenNoUpdate("WRITEINT")) {
			return writeInt();
		}
		else if ( checkNextTokenNoUpdate("ident")) {
			return assignment();
		}
		else if ( checkNextTokenNoUpdate("IF")) {
			return if_statement();
		}
		else if ( checkNextTokenNoUpdate("WHILE")) {
			return while_statement();
		}
		else {
			System.err.println(" Syntax Error : in statement() "  + getCurrentToken() );
			return null;
		}
	}
	
	WhileStatement while_statement() {
		StatementSequence s;
		Expression e;
		if( checkNextToken("WHILE") && (e = expression()) != null && checkNextToken("DO") && ( s = statement_sequence())!= null && checkNextToken("END") )  {
			return new WhileStatement(e, s);
		}
		else {
			System.err.println(" Syntax Error : in statement() "  + getCurrentToken() );
			return null;
		}
			
		
	}
	
	IfStatement if_statement() {
		Expression e;
		StatementSequence s;
		ElseClause elcl;
		if( checkNextToken("IF") && (e = expression()) != null && checkNextToken("THEN") && ( s = statement_sequence())!= null && ( elcl = else_clause()) != null
				&& checkNextToken("END")) {
			return new IfStatement(e, s, elcl);
		}
		else {
			System.err.println(" Syntax Error : in statement() "  + getCurrentToken() );
			return null;
		}
		
	}
	
	ElseClause else_clause() {
		StatementSequence st;
		if( checkNextToken("ELSE") && (st = statement_sequence()) != null ){
			
				return new ElseClause(st);
			
		}
		//empty rule
		else if( checkNextTokenNoUpdate("END")) {
			 return new ElseClause();
		}
		else {
			
			System.err.println(" Syntax Error : in statement() "  + getCurrentToken() );
			return null;
		}
	}
	
	Assignment assignment() {
		Token id;
		if( checkNextToken("ident") && checkNextToken("ASGN")) {
			id = getToken(symbol_cursor_pos - 2); // get token for ident
			
			if(checkNextToken("READINT")) {
				return new Assignment(new String("readInt"), id);
			}
			else {
				Expression e = expression();
				if( e != null)
					return new Assignment(e, id);
				else {
					System.err.println(" Syntax Error : in assignment() "  + getCurrentToken() );
					return null;
				}	
				
			}
			
		}
		else {
			System.err.println(" Syntax Error : in statement() "  + getCurrentToken() );
			return null;
		}
		
	}
	
	WriteInt writeInt() {
		Expression expr;
		if( checkNextToken("WRITEINT") && (expr = expression()) != null) {
			return new WriteInt(expr);
		}
		else {
			System.err.println(" Syntax Error : in writeInt() "  + getCurrentToken() );
			return null;
		}
		
	}
	
	Expression expression() {
		SimpleExpression sExpr = simple_expression();
		if( sExpr != null ) {
			ExpressionPart part = expression_part();
			return new Expression(sExpr, part);
		}
		else {
			System.err.println(" Syntax Error : in expression() "  + getCurrentToken() );
			return null;
		}
	}
	
	ExpressionPart expression_part() {
		SimpleExpression sExpr;
		// empty rule
		if(checkNextTokenNoUpdate("THEN") | checkNextTokenNoUpdate("DO") | checkNextTokenNoUpdate("SC") | checkNextTokenNoUpdate("RP")) {
			return new ExpressionPart();
		}
		else if( checkNextToken("OP4")) {
			Token op4 =  getToken(symbol_cursor_pos - 1); // as symbol pos increased in checkNextToken method
			if((sExpr = simple_expression())!=null) {
				return new ExpressionPart(sExpr, op4);
			}
			else {
				System.err.println(" Syntax Error : in expression_part() "  + getCurrentToken() );
				return null;
			}
			
		}
		else {
			System.err.println(" Syntax Error : in expression_part() "  + getCurrentToken() );
			return null;
		}
		
		
	}
	
	SimpleExpression simple_expression() {
		Term t = term();
		if( t != null ) {
			SimpleExpressionPart sPart = simple_expression_part();
			return new SimpleExpression(t, sPart);
		}
		else {
			System.err.println(" Syntax Error : in simple_expression() "  + getCurrentToken() );
			return null;
		}
		
	}
	
	SimpleExpressionPart simple_expression_part() {
		// empty rule
		Term term;
		if(checkNextTokenNoUpdate("THEN") | checkNextTokenNoUpdate("DO") | checkNextTokenNoUpdate("SC")
				| checkNextTokenNoUpdate("OP4") | checkNextTokenNoUpdate("RP")) {
			return new SimpleExpressionPart();
		}
		else if( checkNextToken("OP3")) {
			Token op3 =  getToken(symbol_cursor_pos - 1); // as symbol pos increased in checkNextToken method
			if((term = term())!= null) {
				return new SimpleExpressionPart(term, op3);
			}
			else {
				System.err.println(" Syntax Error : in simple_expression_part() "  + getCurrentToken() );
				return null;
			}
		}
		else {
			System.err.println(" Syntax Error : in simple_expression_part() "  + getCurrentToken() );
			return null;
		}
		
	}
	
	Term term() {
		Factor f = factor();
		if( f != null ) {
			TermPart tPart = term_part();
			return new Term(f, tPart);
		}
		else {
			System.err.println(" Syntax Error : term() "  + getCurrentToken() );
			return null;
		}
		
		 
	}
	TermPart term_part() {
		// empty rule
		Factor f;
		if(checkNextTokenNoUpdate("THEN") | checkNextTokenNoUpdate("DO") | checkNextTokenNoUpdate("SC")
				| checkNextTokenNoUpdate("OP3") | checkNextTokenNoUpdate("OP4") | checkNextTokenNoUpdate("RP")) {
			return new TermPart();
		}
		else if( checkNextToken("OP2") ) {
			Token op =  getToken(symbol_cursor_pos - 1); // as symbol pos increased in checkNextToken method 
			if ((f = factor())!= null) {
				return new TermPart(f , op);
			}
			else {
				System.err.println(" Syntax Error : in term_part() "  + getCurrentToken() );
				return null;
			}
			
		}
		else {
			System.err.println(" Syntax Error : in term_part() "  + getCurrentToken() );
			return null;
		}
		
	}
	
	Factor factor() {
		if( checkNextToken("ident") | checkNextToken("num") | checkNextToken("boollit")) {
			Token facor_token =  getToken(symbol_cursor_pos - 1); // as symbol pos increased in checkNextToken method
			return new Factor( facor_token );
		}
		else if( checkNextToken("LP")) {
			Expression expr = expression();
			if( (expr != null) && checkNextToken("RP"))
				return new Factor(expr);
			else {
				System.err.println(" Syntax Error : in factor() "  + getCurrentToken() );
				return null;
			}
		}
		else {
			System.err.println(" Syntax Error : in factor() "  + getCurrentToken() );
			return null;
		}
	}
	
}

