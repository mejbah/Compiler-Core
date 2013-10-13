package edu.utsa.tl13;

import java.util.ArrayList;

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
		
		//System.out.println(visitor.getTextToWrite());
		return visitor.getTextToWrite();
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

