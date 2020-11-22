package Parser;

public class Int_LiteralExpression extends LiteralExpression
{	
	public Int_LiteralExpression(int value)
	{
		super(value);
	}
	
	public String toString()
	{
		return super.toString() + "\n\t" + this.value;
	}

	public int getValue() 
	{
		return Integer.parseInt(this.value);
	}
}