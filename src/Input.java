import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;


/**
 * Gets the source characters one by one form the scanner and returns the corresponding Lexeme
 * 
 */


public class Input {


	private static PushbackReader pbr;

	public static void main (String[] args){

		/**
		 * Scan the input source and produce a list of lexemes
		 */

		try {
			pbr = new PushbackReader(new FileReader(args[0]));
			System.out.println("List of the lexemes: ");

			Lexer lexer = new Lexer();

			Lexeme token; 
			token = lexer.lex();

			while (token.lexType() != "ENDofINPUT")
			{
				System.out.println(token);
				token = lexer.lex();
			}
			System.out.println("ENDofINPUT");
		} 

		catch (FileNotFoundException e) {
			System.out.println("Cannot find file " + args[0]);
			e.printStackTrace();
		}


	}  //end of main


	/**
	 * Gets the next char from the character stream 
	 */

	public int getCharacter(){
		int ch;	
		try{
			//System.out.println("Test reading...");
			ch= pbr.read();
			return ch;
		} catch (IOException ioe){
			System.out.println("The source file cannot be read ");
		}
		return -1;
	}


	/**
	 * Skips whitespace from the source file
	 */
	public void skipWhitespace() {
		int ch;
		try {
			ch = pbr.read();

			//while (ifWhitespace(ch))
			
			while (ch == 9 || ch == 10 || ch == 13 || ch == 32)
			{
				ch = pbr.read();
			}
			pushback((char) ch);

		}

		catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * skipComments skips all read-in characters in the same line (until it finds a new line character '\n')
	*/
	public void ifComments() {
		try {
			int ch = pbr.read();
			while(ch != '\n') {
				ch = pbr.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Pushbacks the character into the character stream
	 */
	public void pushback(char ch) {
		try {
			pbr.unread(ch);
		} 

		catch (IOException e) {
			e.printStackTrace();
		}
	}	

	
}
