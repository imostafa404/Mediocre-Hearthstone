package model.cards.minions;
import model.cards.*;

public class Icehowl extends Minion{
	public Icehowl()
	{
		super("Icehowl", 9, Rarity.LEGENDARY, 10, 10, false, false, true);
		//This minion can only attack other minions and not heros
	}
}
