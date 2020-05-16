package exceptions;

public class NotSummonedException extends HearthstoneException{
	//occurs when trying to attack with a minion that is not yet summoned to the field (i.e. still in hand).

	public NotSummonedException()
	{
		super();
	}
	
	public NotSummonedException(String s)
	{
		super(s);
	}
}
