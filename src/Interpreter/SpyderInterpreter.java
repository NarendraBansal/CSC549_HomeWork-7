package Interpreter;

import java.util.ArrayList;

import Parser.*;

public class SpyderInterpreter 
{
	public static VariableEnvironment theEnv = new VariableEnvironment();
	public static ArrayList<String> theOutput = new ArrayList<String>();
	
	public static void displayResults()
	{
		SpyderInterpreter.theEnv.display();
		for(String s : SpyderInterpreter.theOutput)
		{
			System.out.println(s);
		}
	}
	
	public static void interpret(ArrayList<Statement> theStatements)
	{
		for(Statement s : theStatements)
		{
			if(s instanceof RememberStatement)
			{
				//interpret a remember statement
				SpyderInterpreter.interpretRememberStatement((RememberStatement)s);
			}
			if(s instanceof QuestionStatement)
			{
				//interpret a remember statement
				SpyderInterpreter.interpretQuestionStatement((QuestionStatement)s);
			}
		}
	}
	
	public static void interpret(QuestionStatement s)
	{
		
			if(s instanceof QuestionStatement)
			{
				//interpret a remember statement
				SpyderInterpreter.interpretQuestionStatement((QuestionStatement)s);
			}
		
	}
	
	//determines if a String contains all digits (numbers)
	private static boolean isInteger(String s)
	{
		for(int i = 0; i < s.length(); i++)
		{
			if(!Character.isDigit(s.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}
	
	private static int interpretLiteralExpression(LiteralExpression le)
	{
		if(le instanceof Int_LiteralExpression)
		{
			return ((Int_LiteralExpression) le).getValue();
		}
		throw new RuntimeException("Not a valid literal type...");
	}
	
	private static int interpretDoMathExpression(DoMathExpression dme)
	{
		Expression left = dme.getLeft();
		int leftValue = SpyderInterpreter.getExpressionValue(left);
		Expression right = dme.getRight();
		int rightValue = SpyderInterpreter.getExpressionValue(right);
		String math_op = dme.getOp();
		
		if(math_op.equals("+"))
		{
			return leftValue + rightValue;
		}
		else if(math_op.equals("-"))
		{
			return leftValue - rightValue;
		}
		else if(math_op.equals("*"))
		{
			return leftValue * rightValue;
		}
		else if(math_op.equals("/"))
		{
			return leftValue / rightValue;
		}
		else if(math_op.equals("%"))
		{
			return leftValue % rightValue;
		}
		throw new RuntimeException("Not a valid math operator: " + math_op);
	}
	
	
	private static int  interpretTestExpression(TestExpression dme)
	{
		Expression left = dme.getLeft();
		int leftValue = SpyderInterpreter.getExpressionValue(left);
		Expression right = dme.getRight();
		int rightValue = SpyderInterpreter.getExpressionValue(right);
		String math_op = dme.getOp();

		if(math_op.equals(">"))
		{
			boolean boo = leftValue > rightValue;
			if(boo==true) {
				return -1;
			}
			else {
				return -2;
			}
			 
		}
		else if(math_op.equals("<"))
		{
			boolean boo = leftValue < rightValue;
			if(boo==true) {
				return -1;
			}
			else {
				return -2;
			}
		}
		
		throw new RuntimeException("Not a valid boolean operator: " + math_op);
	}
	
	private static int interpretResolveExpression(ResolveExpression rs)
	{
		
		//only look up the variable in the env if it is not a LITERAL
		//Literal Types: int
		//this try/catch attempts to convert a string to an int and if it fails it
		//looks the string up as a variable name
		try
		{
			//tries to treat it as a int literal
			return Integer.parseInt(rs.getName());	
		}
		catch(Exception e)
		{
			try
			{
				//if not a literal, look it up in our environment
				return SpyderInterpreter.theEnv.getValue(rs.getName());
			}
			catch(Exception e2)
			{
				throw new RuntimeException("Variable " + rs.getName() + " NOT FOUND!");
			}
		}
	}
	
	private static int getExpressionValue(Expression e)
	{
		if(e instanceof ResolveExpression)
		{
			return SpyderInterpreter.interpretResolveExpression((ResolveExpression)e);
		}
		else if(e instanceof LiteralExpression)
		{
			return SpyderInterpreter.interpretLiteralExpression((LiteralExpression) e);
		}
		else if(e instanceof DoMathExpression)
		{
			return SpyderInterpreter.interpretDoMathExpression((DoMathExpression) e);
		}
		
		throw new RuntimeException("Not a known expression type: " + e.getExpressionType());
	}
	
	private static int getTestExpressionValue(Expression e)
	{
		if(e instanceof ResolveExpression)
		{
			return SpyderInterpreter.interpretResolveExpression((ResolveExpression)e);
		}
		else if(e instanceof LiteralExpression)
		{
			return SpyderInterpreter.interpretLiteralExpression((LiteralExpression) e);
		}
		if(e instanceof TestExpression)
		{
			return SpyderInterpreter.interpretTestExpression((TestExpression) e);
		}
		
		throw new RuntimeException("Not a known expression type: " + e.getExpressionType());
	}
	
	private static void interpretRememberStatement(RememberStatement rs)
	{
		//we need to resolve this expression before we can actually remember anything
		Expression valueExpression = rs.getValueExpression();
		int answer = SpyderInterpreter.getExpressionValue(valueExpression);
		
		SpyderInterpreter.theEnv.addVariable(rs.getName(), answer);
		
		ArrayList<String> lis=SpyderInterpreter.theOutput;
		
		boolean exists=false;
		for(String s:lis) {
			String token="<HIDDEN> Added " + rs.getName();
			if(s.contains(token)){
				exists=true;
			}
		}
		
		if(exists==false) {
			SpyderInterpreter.theOutput.add("<HIDDEN> Added " + rs.getName() + " = " + answer + " to the variable environment.");
		}
		
		
	}
	
	private static boolean interpretQuestionStatement(QuestionStatement ts)
	{
		//we need to resolve this expression before we can actually remember anything
		Expression valueExpression = ts.getValueExpression();
		int answer = SpyderInterpreter.getTestExpressionValue(valueExpression);
		
		if(answer==-1) {
			TestExpression te = (TestExpression)valueExpression;
			
		
			
			String lex="";
			 
			 if(te.getLeft() instanceof ResolveExpression) {
				 ResolveExpression le=(ResolveExpression)te.getLeft();
				 lex=le.getName();
			 }
			 else if (te.getLeft() instanceof Int_LiteralExpression) {
				 Int_LiteralExpression le=(Int_LiteralExpression)te.getLeft();
				 lex=le.getValue()+"";
			}
			 
			 String rex="";
			 
			 if(te.getRight() instanceof ResolveExpression) {
				 ResolveExpression re=(ResolveExpression)te.getRight();
				 rex=re.getName();
			 }
			 else if (te.getRight() instanceof Int_LiteralExpression) {
				 Int_LiteralExpression re=(Int_LiteralExpression)te.getRight();
				 rex=re.getValue()+"";
			}			
			
			SpyderInterpreter.theEnv.addVariable("test "+lex+" "+te.getOp()+" "+rex, -1);
			return true;
		}
		else {
			TestExpression te = (TestExpression)valueExpression;
			return false;
		}
	}
}
