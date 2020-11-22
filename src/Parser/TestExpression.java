package Parser;

public class TestExpression extends Expression 
{
	private Expression left;
	private Expression right;
	private String op;
	
	public TestExpression(Expression left, String op, Expression right)
	{
		super("Test Expression");
		this.left = left;
		this.right = right;
		this.op = op;
	}
	
	public String toString()
	{
		return super.toString() + "\n\t" + this.left.toString() + " "
				+ this.op + " " + this.right.toString();
	}

	public Expression getLeft() {
		return left;
	}

	public Expression getRight() {
		return right;
	}

	public String getOp() {
		return op;
	}

	
	
}
