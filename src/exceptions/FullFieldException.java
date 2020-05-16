package exceptions;

public class FullFieldException extends HearthstoneException{
	//occurs when trying to add a minion card to an already full field.
	
	public FullFieldException()
	{
		super();
	}
	
	public FullFieldException(String s)
	{
		super(s);
	}
}
