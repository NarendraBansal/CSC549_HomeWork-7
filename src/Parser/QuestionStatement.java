package Parser;

public class QuestionStatement extends Statement
{

	private Expression value;
	
	public QuestionStatement( Expression value)
	{
		super("Question Statement");
		
		this.value = value;
	}

	public String toString()
	{
		return super.toString() + "\n\t" + 
	 this.value;
	}
	
	public Expression getValueExpression() 
	{
		return value;
	}
	
	
}
