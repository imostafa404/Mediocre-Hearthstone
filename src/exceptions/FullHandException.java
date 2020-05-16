package exceptions;
import model.cards.*;

public class FullHandException extends HearthstoneException{
	//occurs when trying to add a card to an already full hand.
	private Card burned;
	
	public FullHandException(Card b)
	{
		super();
		this.burned = b;
	}
	
	public FullHandException(String s, Card b)
	{
		super(s);
		this.burned = b;
	}

	public Card getBurned() {
		return burned;
	}
}
