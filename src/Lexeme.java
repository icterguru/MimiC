/**
 * 
 * Lexeme class 
 * 
 * @author mokter
 *
 */
public class Lexeme implements Types

{
	String 	lextype;
	int 	ival;
	Double 	rval;
	String 	sval;
	Lexeme 	left, right; 

	/** For an empty Lexeme with only type value  
	 * 
	 */

	public Lexeme (String strType){
		lextype = strType;
	}

	/** For a single-character Lexeme  
	 * 
	 */
	public Lexeme (String strtype, int ch){
		lextype = strtype;
		ival = ch;	
	}


	/** For a multi-character Lexeme  
	 * 
	 */
	public Lexeme (String strtype, String str){
		lextype = strtype;
		sval = str;	
	}

	public void setLeft(Lexeme ltree)	{ 
		left = ltree; 
	}

	public void setRight(Lexeme rtree){ 
		right = rtree; 
	}

	public Lexeme left(){ 
		return left; 
	}

	public Lexeme right(){ 
		return right; 
	}

	public String lexType()	{ 
		return lextype; 
	}

	public void setType(String str)	{ 
		lextype = str; 
	}

	public int getIVal(){
		return ival;
	}


	public Double getRVal() {
		return rval;
	}

	public void setRVal(Double d) {
		rval = d;
	}


	public String getSVal()	{
		if (lextype.equals("INTEGER"))
			return Integer.toString(ival);

		return sval;
	}

	public int getNumValue(){
		if(sval != null)
			return Integer.parseInt(sval);

		return ival;
	}


	/**
	 * Overriding toString() method
	 * 
	 */

	public String toString(){
		if (lextype.equals("FUNC"))
			return (lextype);

		else if (lextype.equals("ID"))
			return (lextype + " " + sval);

		else if (lextype.equals("INT"))
			//return (lextype +  " " + ival);
			return (lextype +  " " + ival);

		else if (lextype.equals("STRING"))
			return (lextype +  " " + sval);

		else if (lextype.equals("KEYWORD"))
			return (sval);

		/**
		  else if(ival != 0)
		 
			return (lextype + (char) ival );
		 **/
		else 
			return (lextype);
	}

	public void print(){
		System.out.print(this.toString());
	}

	

} // End of Lexeme class
