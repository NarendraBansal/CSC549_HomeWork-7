import Parser.*;

public class Driver 
{

	public static void main(String[] args) 
	{
		
		Parser.parse("input.spyder");
		//Parser.display();
		Interpreter.SpyderInterpreter.interpret(Parser.getParsedStatements());

		Interpreter.SpyderInterpreter.displayResults();		
	}
}
