package exceptions;

public class HeroPowerAlreadyUsedException extends HearthstoneException{
	// occurs when a hero attempts to use their hero power more than once per turn.
	
	public HeroPowerAlreadyUsedException()
	{
		super();
	}
	
	public HeroPowerAlreadyUsedException(String s)
	{
		super(s);
	}
}
