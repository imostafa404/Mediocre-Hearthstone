package model.cards.spells;
import exceptions.InvalidTargetException;
import model.cards.*;
import model.cards.minions.Minion;
import model.heroes.Hero;

public class Pyroblast extends Spell implements MinionTargetSpell, HeroTargetSpell{
	public Pyroblast()
	{
		super("Pyroblast", 10, Rarity.EPIC);
	}

	public void performAction(Hero h) 
	{
		h.setCurrentHP(h.getCurrentHP() - 10);
	}

	public void performAction(Minion m) throws InvalidTargetException 
	{

		if(m.isDivine())
			m.setDivine(false);
		else
			m.setCurrentHP(m.getCurrentHP() - 10);
	}	
}
