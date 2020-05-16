package exceptions;

public class TauntBypassException extends HearthstoneException{
	//occurs when a hero attempts to perform an action outside their turn.
	
	public TauntBypassException() 
	{
		super();
	}
	
	public TauntBypassException(String s)
	{
		super(s);
	}
}
