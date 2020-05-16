package exceptions;

public class InvalidTargetException extends HearthstoneException{
	//occurs when trying to do an action with a wrong target (Hint: think about Icehowl).

	public InvalidTargetException()
	{
		super();
	}
	
	public InvalidTargetException(String s)
	{
		super(s);
	}
}
