package exceptions;

public class NotEnoughManaException extends HearthstoneException{
	// occurs when a hero attempts to perform an action with insuffcient mana, e.g. playing a card or using hero power
	
	public NotEnoughManaException()
	{
		super();
	}
	
	public NotEnoughManaException(String s)
	{
		super(s);
	}
}
