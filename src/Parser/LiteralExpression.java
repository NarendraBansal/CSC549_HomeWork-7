package Parser;

public abstract class LiteralExpression extends Expression
{
	protected String value;
	
	public LiteralExpression(int value)
	{
		super("Int_Literal Expression");
		this.value = "" + value;
	}
	
	public String toString()
	{
		return super.toString() + "\n\t" + this.value;
	}
}
