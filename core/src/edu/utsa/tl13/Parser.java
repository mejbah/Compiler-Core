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
	
	public void parse() {
		if(program() == null) {
			System.err.println("Program error");
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
					System.err.println("Syntax Error: in program() " + getCurrentToken());
					return null;
				}
			}
			else
			{
				System.err.println("Syntax Error: in program() " + getCurrentToken());
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
		if( checkNextToken("VAR") && checkNextToken("ident") && checkNextToken("AS")) {
			TypeRule type = type_rule();
			if( type != null && checkNextToken("SC") && (dcls = declarations()) != null) {
				dcls.addDeclaration(new DeclarationUnit(type));
				return dcls;
				
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
		
	    if( checkNextTokenNoUpdate("END") || checkNextTokenNoUpdate("ELSE")) {
			return new StatementSequence();
		}
		else if( (s = statement()) != null && checkNextToken("SC") && (statements = statement_sequence())!=null) {
			statements.addStatement(s);
			return statements;
		}
		else {
			System.err.println(" Syntax Error : in stetement_sequence() "  + getCurrentToken() );
			return null;
		}
		
		
	}
	
	Statement statement() {
		if( checkNextTokenNoUpdate("WRITEINT")) {
			return writeInt();
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
		else if( checkNextToken("OP4") && (sExpr = simple_expression())!=null) {
			return new ExpressionPart(sExpr);
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
		else if( checkNextToken("OP3") && (term = term())!= null) {
			return new SimpleExpressionPart(term);
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
		else if( checkNextToken("OP2") && (f = factor())!= null) {
			return new TermPart(f);
		}
		else {
			System.err.println(" Syntax Error : in factor_part() "  + getCurrentToken() );
			return null;
		}
		
	}
	
	Factor factor() {
		if( checkNextToken("ident") | checkNextToken("num") | checkNextToken("boollit")) {
			return new Factor();
		}
		else if( checkNextToken("LP")) {
			Expression expr = expression();
			return new Factor(expr);
		}
		else {
			System.err.println(" Syntax Error : in factor() "  + getCurrentToken() );
			return null;
		}
	}
	
}

