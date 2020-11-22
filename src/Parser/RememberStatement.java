package Parser;

public class RememberStatement extends Statement
{
	private String name;
	private String type;
	private Expression value;
	
	public RememberStatement(String type, String name, Expression value)
	{
		super("Remember Statement");
		this.name = name;
		this.type = type;
		this.value = value;
	}

	public String toString()
	{
		return super.toString() + "\n\t" + 
	this.type + " " + this.name + " = " + this.value;
	}
	
	public String getName() 
	{
		return name;
	}

	public String getType() 
	{
		return type;
	}

	public Expression getValueExpression() 
	{
		return value;
	}
	
	
}
