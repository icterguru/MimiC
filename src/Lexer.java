/**
 * ************************* Lexer.java file ***********************
 *  
 * Lexical analysis of the MimiC language development
 * 
 *   
 * 
 * @author Mokter
 *
 */


// www.tutorials/jenkov.com/java-io/pushbackreader.html

public class Lexer implements Types    
{

 Input input = new Input();

	public Lexeme lex(){
		int ich ;
		input.skipWhitespace();
		ich  =  input.getCharacter();

		if(ich == -1 || ich >= 65535)
			
			return new Lexeme("ENDofINPUT");

		switch ((char) ich){
		// for single-character token	
		case '(':
			return new Lexeme (OPAREN);

		case ')':
			return new Lexeme (CPAREN);

		case '{':
			return new Lexeme (OBRACE);

		case '}':
			return new Lexeme (CBRACE);

		case '[':
			return new Lexeme (OBRACKET);

		case ']':
			return new Lexeme (CBRACKET);

		case '=':
			return new Lexeme (ASSIGN);

		case '+':
			return new Lexeme (PLUS);

		case '-':
			return new Lexeme (MINUS);

		case '*':
			return new Lexeme (TIMES);

		case '/':
			return new Lexeme (DIVIDES);
		
		case '%':
			return new Lexeme (MODULO);
	
		case '<':
			return new Lexeme (LT);

		case '>':
			return new Lexeme (GT);

		case '&':
			return new Lexeme(AND);
			
		case '|':
			return new Lexeme(OR);
			
		case '~':
			return new Lexeme(NOT);
			
		case ',':
			return new Lexeme (COMMA);

		case ';':
			return new Lexeme (SEMICOLON);

		case ':':
			return new Lexeme (COLON);
			
		case '.':
			return new Lexeme (DOT);
				
		case '#': {
			input.ifComments();
			return lex();
		}

		default: 
			// multi-character tokens for on members, variables/keywords/Strings

			if(ifAlpha(ich))
			{
				input.pushback((char) ich);
				return getVariable();
			}

			else if(ifDigit(ich))
			{
				input.pushback((char) ich);
				return getInteger();
			}

			else if(ich == '\"')
			{
				return getString();
			}


		}

		return new Lexeme (UNKNOWN);
	}


	private boolean ifAlpha(int ch) {
		// TODO Auto-generated method stub
		if(ch >= 65 && ch <= 122)
			return true;
		else
			return false;
	}


	private boolean ifDigit(int ch) {
		// TODO Auto-generated method stub
		if (ch >= 48 && ch <= 57)
			return true;
		else 
			return false;
	}

	/**
	 * For getting a String  
	 * 	 
	 */

	private Lexeme getString() {
		// TODO Auto-generated method stub
		String token = "\"";
		int ch = input.getCharacter();

		while (ch != '\"'){
			token = token + (char) ch;
			ch= input.getCharacter();
		}
		token = token + "\"";
		return new Lexeme (STRING, token);
	}

	/**
	 * For getting a number   
	 * 	 
	 */

	private Lexeme getInteger() {
		// TODO Auto-generated method stub


		int ch =   input. getCharacter();
		String token = "";
		while(ifDigit(ch)){
			token = token + (char) ch;
			ch = input.getCharacter();
		}

		input.pushback((char) ch);

		return new Lexeme (INT, Integer.parseInt(token));
	}


	/**
	 * For getting a variable- a combination of digits and letters (optional) but    
	 * 	 must starts with a digit
	 */

	private Lexeme getVariable() {
		// TODO Auto-generated method stub

		String token = "";
		int ch = input.getCharacter();

		while(ifAlpha(ch) || ifDigit(ch))
		{
			token = token + (char) ch;
			ch = input.getCharacter(); 

		}
		input.pushback((char) ch);

		if (Keywords.ifKeyword(token))
		{
			//System.out.println("Keyword testing...");
			return new Lexeme (KEYWORD, token);
		}

		return new Lexeme (ID, token);
	}



} // end of Lexer class

