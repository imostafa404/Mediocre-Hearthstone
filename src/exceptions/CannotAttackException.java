package exceptions;

public class CannotAttackException extends HearthstoneException{
	//occurs when the current minion cannot be used to attack. This can occur if the minion is sleeping or has already been used to attack this turn.
	public CannotAttackException()
	{
		super();
	}
	
	public CannotAttackException(String s)
	{
		super(s);
	}
}
