/**
 * This class holds the common keywords that will be used for special purpose
 * 
 * @author mokter
 *
 */

public class Keywords {
	private static final String keywords[] = { "char", "int", "real", "string", "if", "else", "func", "true", "false", "eq", "ne", "ge", "le",  "and", "or", "not",
		"print", "println", "while" };
	
	/**
	 * Checks if an used token is found in the list of keywords 
	 */
	public static boolean ifKeyword(String str){
		int i = 0;
		while(i< keywords.length)
		{
			if (keywords[i].equals(str))
				return true;
			i++;	
		}
		
		return false;
		
	}
}
