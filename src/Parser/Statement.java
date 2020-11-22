package Parser;

public abstract class Statement
{
	protected String statementType;
	
	public Statement(String statementType)
	{
		this.statementType = statementType;
	}
	
	public String toString()
	{
		return "Statement: " + this.statementType;
	}
}
