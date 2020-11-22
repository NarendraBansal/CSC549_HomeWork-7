package Interpreter;

public class NameValuePair 
{
	private String name;
	private int value;
	
	public NameValuePair(String name, int value)
	{
		this.name = name;
		this.value = value;
	}
	
	public void display()
	{
		System.out.println("Name: " + this.name + " = " + this.value);
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getValue()
	{
		return this.value;
	}
	
	public void setValue(int newValue)
	{
		this.value = newValue;
	}
}
