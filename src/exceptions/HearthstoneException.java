package exceptions;

public abstract class HearthstoneException extends Exception{

	public HearthstoneException()
	{
		super();
	}
	
	public HearthstoneException(String s)
	{
		super(s);
	}
}
