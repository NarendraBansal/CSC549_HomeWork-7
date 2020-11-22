package Parser;

public class ResolveExpression extends Expression 
{
	private String name;
	
	public ResolveExpression(String name)
	{
		super("Resolve Expression");
		this.name = name;
	}
	
	public String toString()
	{
		return super.toString() + "\n\t" + this.name;
	}

	public String getName() 
	{
		return name;
	}	
}
