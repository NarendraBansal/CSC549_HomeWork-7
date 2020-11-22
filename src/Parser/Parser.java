package Parser;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import Interpreter.NameValuePair;
import Interpreter.SpyderInterpreter;

public class Parser 
{
	private static ArrayList<Statement> theListOfStatements = new ArrayList<Statement>();
	
	public static ArrayList<Statement> getParsedStatements()
	{
		return Parser.theListOfStatements;
	}
	
	public static void display()
	{
		for(Statement s : theListOfStatements)
		{
			System.out.println(s);
		}
	}
	
	static ResolveExpression parseResolve(String name)
	{
		//parse this string into language objects
		//turn remember syntax into a ResolveStatement
		ResolveExpression rs = new ResolveExpression(name);
		return rs;
	}
	
	private static boolean isMathOp(String s)
	{
		return "+-*/%".indexOf(s.trim()) > -1;
	}
	
	private static int getDoMathExpressionEndBucket(int startPos, String[] theParts)
	{
		//do-math do-math a + 7 + do-math b + 4
		int opCount = 0;
		while(startPos < theParts.length)
		{
			if(theParts[startPos].equals("do-math"))
			{
				opCount++;
			}
			else if(Parser.isMathOp(theParts[startPos]))
			{
				opCount--;
				if(opCount == 0)
				{
					return startPos-1; //add startPos to the end of the string
				}
			}
			startPos++;
		}
		return startPos;
		
	}
	
	static TestExpression parseTest(String expression)
	{
		String[] theParts = expression.split("\\s+");
		Expression left;
		int pos = 1;
		String temp = "";
		TestExpression theResult=null;
		if(theParts[0].equals("test"))
		{
			theResult = new TestExpression(Parser.parseExpression(theParts[1]), theParts[2], Parser.parseExpression(theParts[3]) );
		}
		 
		return theResult;
	}
	
	static DoMathExpression parseDoMath(String expression)
	{
		//do-math do-math a + 7 + do-math b + 4 - doesn't work for this YET!
		//do-math expression op expression
		//make the above work for HW
		
		//do-math a + 7 - will work for this
		// (resolve expression a) + (int_lit expression 7)
		//right now we are assuming only a single level of do-math
		String[] theParts = expression.split("\\s+");
		Expression left;
		int pos = 1;
		String temp = "";
		if(theParts[pos].equals("do-math"))
		{
			//we need to handle the left expression as a do-math expression
			//left side contains at least 1 do-math expression
			//capture the substring from the current point until we reach the appropriate
			//operator
			pos = Parser.getDoMathExpressionEndBucket(0, theParts);
			//pos is the position in theParts where the do math is complete for the left side
			
			for(int i = 1; i <= pos; i++)
			{
				temp += theParts[i] + " ";
			}
			left = Parser.parseDoMath(temp.trim()); 
		}
		else
		{
			//it is either a resolve or literal expression
			left = Parser.parseExpression(theParts[pos]);
		}
		
		String math_op = theParts[pos+1];
		
		//everything from pos+2 forward is the right half of our do-math expression
	    temp = "";
		for(int i = pos+2; i < theParts.length; i++)
		{
			temp += theParts[i] + " ";
		}
		Expression right = Parser.parseExpression(temp.trim());
	
		//create and return an instance of DoMathExpression
		DoMathExpression theResult = new DoMathExpression(left, math_op, right);
		return theResult;
	}
	
	static LiteralExpression parseLiteral(String value)
	{
		//We ONLY have a single LiteralType - int literal
		return new Int_LiteralExpression(Integer.parseInt(value));
	}
	
	static RememberStatement parseRemember(String type, String name, Expression valueExpression)
	{
		//parse this string into language objects
		//turn remember syntax into a RememberStatement
		RememberStatement rs = new RememberStatement(type, name, valueExpression);
		return rs;
	}
	
	static QuestionStatement parseQuestion( Expression valueExpression)
	{
		//parse this string into language objects
		//turn remember syntax into a RememberStatement
		QuestionStatement qs = new QuestionStatement(valueExpression);
		return qs;
	}
	
	public static void parse(String filename)
	{
		try
		{
			Scanner input = new Scanner(new File(System.getProperty("user.dir") + 
					"/src/" + filename));
			//builds a single string that has the contents of the file
			String fileContents = "";
			while(input.hasNext())
			{
				fileContents += input.nextLine().trim();
			}
			
			String[] theProgramLines = fileContents.split(";");
			for(int i = 0; i < theProgramLines.length; i++)
			{
				parseStatement(theProgramLines[i]);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("File Not Found!!!");
		}
	}
	
	static Expression parseExpression(String expression)
	{
		//determine which kind of expression this is, and parse it
		//right now we only have a single kind of expression (ResolveExpression)
		//Possible expressions types:
		// do-math, resolve, literal

		String[] theParts = expression.split("\\s+");
		if(theParts[0].equals("do-math"))
		{
			//must be a do-math expression
			return Parser.parseDoMath(expression);
		}
		else if(theParts[0].equals("test"))
		{
			//must be a do-math expression
			return Parser.parseTest(expression);
		}
		else if(Character.isDigit(theParts[0].charAt(0))) //does the value start with a number
		{
			//must a literal expression
			return Parser.parseLiteral(expression);
		}
		else
		{
			//must be a var name
			return Parser.parseResolve(expression);
		}
	}
	
	//parses the top level statements within our language
	static void parseStatement(String s)
	{
		//split the string on white space (1 or more spaces)
		String[] theParts = s.split("\\s+");
		// remember int b = do-math 5 + a;
		//s = "remember int a = 5"
		//parts = {"remember", "int", "a", "=", "5"}
		//s = "resolve a"
		//parts = {"resolve", "a"}
		
		if(theParts[0].equals("remember"))
		{
			int posOfEqualSign = s.indexOf('=');
			String everythingAfterTheEqualSign = s.substring(posOfEqualSign+1).trim();
	
			//parse a remember statement with type, name, and value
			theListOfStatements.add(Parser.parseRemember(theParts[1], 
					theParts[2], Parser.parseExpression(everythingAfterTheEqualSign)));
		}
		
		if(theParts[0].equals("question"))
		{
			//int posOfEqualSign = s.indexOf('=');

			//parse a remember statement with type, name, and value
			
			String exp =theParts[1]+" "+theParts[2]+" "+theParts[3]+" "+theParts[4] ;
			
			theListOfStatements.add(Parser.parseQuestion(Parser.parseExpression(exp)));
			
			
			int indexofDo= s.indexOf("do");
			int indexofOth= s.indexOf("otherwise");
			
			Interpreter.SpyderInterpreter.interpret(Parser.getParsedStatements());
			ArrayList<NameValuePair> li= SpyderInterpreter.theEnv.theVariables;
			
			boolean flag=false;
			
			 for(NameValuePair nv:li) {
				 
				 if(exp.equals(nv.getName())) {					 
					 if (nv.getValue()==-1) {
						 //String ifStatememtPass =s.substring(indexofDo+3,indexofOth);
						 //String elseStatementPass =s.substring(indexofOth+10);
						flag=true;
					 }
					 else {
						 flag=false;
					 }
				 }
			 }
			
			
		   String ifStatememtPass =s.substring(indexofDo+3,indexofOth);
		   String elseStatementPass =s.substring(indexofOth+10);
			
			if(flag==true) {
				parseStatement(ifStatememtPass);
			}
			else {
			parseStatement(elseStatementPass);
			}

		}
	}
	
	
}
