package exceptions;

public class NotYourTurnException extends HearthstoneException{
	//occurs when a hero attempts to perform an action outside their turn.

	public NotYourTurnException()
	{
		super();
	}
	
	public NotYourTurnException(String s)
	{
		super(s);
	}
}
