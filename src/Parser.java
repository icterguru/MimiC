

public class Parser implements Types 
{

	Lexer lexer;
	Lexeme oldlexeme;
	Lexeme currentlexeme;
	
	public Parser() {
		lexer = new Lexer();
		
	
	}

	
	/**
	 * Move to the next lexeme in the lexical stream.
	 */
	private Lexeme advance() {
		oldlexeme = currentlexeme;
		
		currentlexeme = lexer.lex();
		return oldlexeme;
	}


	/**
	 * If the current lexeme is of the given type, advance to the next lexeme.
	 * Otherwise, report an error and exit.
	 */
	public void match(String type) {
		matchNoAdvance(type);
		advance();
	}

	
	/**
	 * Check if the current lexeme is of a given type.
	 */
	private boolean check(String type) {
		if (currentlexeme.lextype.equals(type))
			return true;
		else
			return false;
	}

	/**
	 * Insist that the current lexeme is of the given type. Otherwise, report an
	 * error and exit.
	 */
	public void matchNoAdvance(String type) {
		if (!check(type)) {
			System.err.println("Parser expected a " + type + ".");
			System.exit(0);
		}
	}

	/**
	 * Recursive descent function corresponding to our arithmetic expression
	 * grammar rule.
	 * 
	 * expression: primary | primary operator expression
	 */
		
	
	public Lexeme expression() {
		Lexeme tree = primary();
		if (operatorPending()) {
			Lexeme temp = operator();
			temp.setLeft(tree);
			temp.setRight(expression());
			tree = temp;
		}
		return tree;
	}

	/**
	 * operator: PLUS | MINUS | TIMES | DIVIDES | MODULO | EQ | GREATERTHAN
	 * | LESSTHAN | GREATEREQUAL | LESSEQUAL | AND | OR
	 */
	private Lexeme operator() {
		if (check("PLUS"))
			return match("PLUS");
		if (check("MINUS"))
			return match("MINUS");
		if (check("TIMES"))
			return match("TIMES");
		if (check("DIVIDES"))
			return match("DIVIDES");
		if (check("MODULO"))
			return match("MODULO");
		if (check("EQ"))
			return match("EQ");
		if (check("GREATERTHAN"))
			return match("GREATERTHAN");
		if (check("LESSTHAN"))
			return match("LESSTHAN");
		if (check("GREATEREQUAL"))
			return match("GREATEREQUAL");
		if (check("LESSEQUAL"))
			return match("LESSEQUAL");
		if (check("AND"))
			return match("AND");
		if (check("OR"))
			return match("OR");
		return match("INVALID-OPERATOR");
	}

	/**
	 * Check if the current lexeme is an operator.
	 */
	private boolean operatorPending() {
		return (check("PLUS") || check("TIMES") || check("MINUS"))
				|| check("DIVIDES") || check("EQ") || check("MODULO")
				|| check("GREATERTHAN") || check("LESSTHAN")
				|| check("GREATEREQUAL") || check("LESSEQUAL")
				|| check("AND") || check("OR");
	}

	/**
	 * An expression must eventually reduce to a primary with no consumption of
	 * tokens.
	 * 
	 * primary : NUMBER | VARIABLE | STRING 
	 * | VARIABLE OPAREN optionalArgumentList CPAREN 
	 * | OPAREN expression CPAREN 
	 * | OPAREN pairExpression CPAREN 
	 * | OPAREN dictExpression CPAREN
	 * @return 
	 */
	private Lexeme primary() {
		Lexeme tree = null;
		if (check("NUMBER")) {
			tree = match("NUMBER");
		} else if (check("STRING")) {
			tree = match("STRING");
		} else if (check("VARIABLE")) {
			tree = match("VARIABLE");		
			// function call
			if (check("OPAREN")) {
				Lexeme temp = new Lexeme("FUNC-CALL");
				match("OPAREN");
				temp.setLeft(tree);
				temp.setRight(optionalArgumentList());
				match("CPAREN");
				return temp;
			} else if (check("ASSIGN")) {
				Lexeme temp = match("ASSIGN");
				temp.setLeft(tree);
				temp.setRight(expression());
				return temp;
			}
		} else if (check("OBRACKET")) {
			match("OBRACKET");
			tree = new Lexeme("ARRAY");
			tree.setRight(optionalArgumentList());
			match("CBRACKET");
		}
		else {
			tree = match("OPAREN");
			if (expressionPending()) {
				tree.setLeft(null);
				tree.setRight(expression());
			}
			else if (check("PAIR"))
				pairExpression();
			else if (check("DICT")) 
				dictExpression();
			match("CPAREN");
		}
		return tree;
	}

	/**
	 * dictExpression: DICT keyValList
	 */
	private void dictExpression() {
		match("DICT");
		keyValList();
	}

	/**
	 * keyValList: keyValPair keyValList | keyValPair | *empty*
	 */
	private void keyValList() {
		if (check("OPAREN")) {
			match("OPAREN");
			keyValPair();
			match("CPAREN");
			keyValList();
		}
	}

	/**
	 * keyValPair: key COLON expression
	 */
	private void keyValPair() {
		key();
		match("COLON");
		expression();
	}

	/**
	 * key: NUMBER | STRING
	 */
	private void key() {
		if (check("NUMBER"))
			match("NUMBER");
		if (check("STRING"))
			match("STRING");
	}

	/**
	 * pairExpression: expression COLON expression
	 */
	private void pairExpression() {
		match("PAIR");
		if (expressionPending())
			expression();
		if (expressionPending())
			expression();
	}

	/**
	 * optionalArgumentList: expression COMMA | expression | expression
	 * optionalArgumentList | *empty*
	 */
	private Lexeme optionalArgumentList() {	
		Lexeme tree = new Lexeme("ARG-LIST");
		if (!(check("CPAREN") || check("CBRACKET"))) {
			tree.setLeft(expression());
			if (check("COMMA"))
				match("COMMA");
			tree.setRight(optionalArgumentList());
		}
		return tree;
	}

	/**
	 * statement : ifStatement | whileStatement | expression SEMI 
	 * | printStatement | printlnStatement
	 */
	private Lexeme statement() {
		Lexeme tree = null;
		if (check("IF")) {
			tree = ifStatement();
		} else if (check("WHILE")) {
			tree = whileStatement();
		} else if(check("PRINT")) {
			tree = printStatment();
		} else if(check("PRINTLN")) {
			tree = printlnStatment();
		}
		else {
			tree = expression();
			match("SEMICOLON");
		} 
		return tree;
	}

	/**
	 * printlnStatement: PRINTLN OPAREN expression CPAREN SEMI
	 */
	private Lexeme printlnStatment() {
		Lexeme tree = match("PRINTLN");
		match("OPAREN");
		tree.setRight(expression());
		match("CPAREN");
		match("SEMICOLON");
		return tree;
	}

	/**
	 * printStatement: PRINT OPAREN expression CPAREN SEMI
	 */
	private Lexeme printStatment() {
		Lexeme tree = match("PRINT");
		match("OPAREN");
		tree.setRight(expression());
		match("CPAREN");
		match("SEMICOLON");
		return tree;
	}

	/**
	 * whilestatement: WHILE OPAREN expression CPAREN block
	 */
	private Lexeme whileStatement() {
		Lexeme tree = match("WHILE");
		match("OPAREN");
		tree.setLeft(expression());
		match("CPAREN");
		tree.setRight(block());
		return tree;
	}

	/**
	 * ifstatement: IF OPAREN expression CPAREN block optElse
	 */
	private Lexeme ifStatement() {
		Lexeme tree = match("IF");
		match("OPAREN");
		tree.setLeft(new Lexeme("GLUE"));
		tree.left().setLeft(expression());
		match("CPAREN");
		tree.setRight(block());
		tree.left().setRight(optElse());
		return tree;
	}

	/**
	 * optElse: ELSE block | *empty*
	 */
	private Lexeme optElse() {
		Lexeme tree = null;
		if (check("ELSE")) {
			tree = match("ELSE");
			tree.setRight(block());
		}
		return tree;
	}

	/**
	 * block: OPAREN optStatementList CPAREN
	 */
	private Lexeme block() {
		Lexeme tree = new Lexeme("BLOCK");
		match("OPAREN");
		tree.setRight(optStatementList());
		match("CPAREN");
		return tree;
	}

	/**
	 * Details the first rule of our program. It is our start rule. We may
	 * change this to unit test individual recursive descent functions.
	 * 
	 * program: function program | statement program | *empty*
	 */
	public void program()
	{
		statementList();
	}
	
	public void statementList(){
		statement();
		if(statementListPending())
			statementList();
	}
	
	public boolean statementListPending(){
		return expressionPending() || ifStatementPending() ||
				whileStatementPending() || definitionPending();
		
	}
	
	
	public boolean definitionPending(){
		return varDeclarationPending() || funcDefinitionPending();
	}
	
	public void varDeclarationPending(){
		return check(VAR);
	}

	public void functionDefinitionPending(){
		return check(FUNC);
	}
	
	public boolean ifStatementPending(){
		if (check(KEYWORD))
			if (currentLexeme.lextype.equals("if"))
				return true;
	}
	
	public void whileStatementPending(){
		return check(WHILE);
	}
	
/**	
 * 
	private Lexeme program() {
		Lexeme tree = new Lexeme("PROGRAM");
		if (check("FUNCTION")) {
			tree.setLeft(function());
			tree.setRight(program());
		} else if (statementPending()) {
			tree.setLeft(statement());
			tree.setRight(program());
		}
		return tree;
	}

**/
	private boolean statementPending() {
		return expressionPending() || check("IF") || check("WHILE") || 
				check("PRINTLN") || check("PRINT");
	}

	private boolean expressionPending() {
		return primaryPending();
	}

	private boolean primaryPending() {
		return check("NUMBER") || check("VARIABLE") || check("OPAREN")
				|| check("STRING");
	}

	/**
	 * function: FUNCTION VARIABLE OPAREN optionalParameters CPAREN block
	 */
	private Lexeme function() {
		Lexeme tree = match("FUNCTION");
		tree.setRight(new Lexeme("GLUE"));
		tree.setLeft(match("VARIABLE"));
		match("OPAREN");
		tree.right().setLeft(optParametersList());
		match("CPAREN");
		tree.right().setRight(block());
		return tree;
	}

	/**
	 * optParametersList: VARIABLE | VARIABLE COMMA optParametersList | *empty*
	 */
	private Lexeme optParametersList() {
		Lexeme tree = new Lexeme("PARAM-LIST");
		if (check("VARIABLE")) {
			tree.setLeft(match("VARIABLE"));
			if (check("COMMA")) {
				match("COMMA");
				tree.setRight(optParametersList());
			}
		}
		return tree;
	}

	/**
	 * optStatementList: statement | statement optStatementList | *empty*
	 */
	private Lexeme optStatementList() {
		Lexeme tree = new Lexeme("STATEMENT-LIST");
		if (statementPending()) {
			tree.setLeft(statement());
			tree.setRight(optStatementList());
		}
		return tree;
	}

	/**
	 * Set the first lexeme and begin parsing the source code.
	 * We may change the start rule to test units. Returns a Lexeme
	 * that is the root of the parse tree for our program.
	 */
	/**
	public Lexeme parse() {
		System.out.println("-- PARSER OUTPUT: PROGRAM PARSE TREE --");
		currentlexeme = lexer.lex();
		Lexeme root = program();
		match("END");
		root.preorderTraversal(root);
		System.out.println("");
		return root;
	}
**/
	
	public void parse() {
		System.out.println("-- PARSER OUTPUT: PROGRAM PARSE TREE --");
		advance();
		program();
		match(ENDofINPUT);
	
	}
}
