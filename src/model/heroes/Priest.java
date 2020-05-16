package model.heroes;
import model.cards.*;
import model.cards.spells.*;
import model.cards.minions.*;
import java.util.*;

import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.NotEnoughManaException;
import exceptions.NotYourTurnException;

import java.io.*;

public class Priest extends Hero{

	public Priest() throws IOException, CloneNotSupportedException
	{
		super("Anduin Wrynn");
	}
	
	public void useHeroPower(Hero target) throws NotEnoughManaException, HeroPowerAlreadyUsedException, NotYourTurnException, FullHandException, FullFieldException, CloneNotSupportedException
	{
		super.useHeroPower();
		int healed = 2;
		for(int i = 0; i < this.getField().size(); i ++)
		{
			if(this.getField().get(i).getName().equals("Prophet Velen"))
			{
				healed = 8;
				break;
			}
		}
		target.setCurrentHP(target.getCurrentHP() + healed);
	}
	
	public void useHeroPower(Minion target) throws NotEnoughManaException, HeroPowerAlreadyUsedException, NotYourTurnException, FullHandException, FullFieldException, CloneNotSupportedException
	{
		super.useHeroPower();
		int healed = 2;
		for(int i = 0; i < this.getField().size(); i ++)
		{
			if(this.getField().get(i).getName().equals("Prophet Velen"))
			{
				healed = 8;
				break;
			}
		}
		target.setCurrentHP(target.getCurrentHP() + healed);
	}
	
	public void buildDeck() throws IOException, CloneNotSupportedException
	{
		ArrayList<Card> deck = this.getDeck();
		deck.addAll(getNeutralMinions(getAllNeutralMinions("neutral_minions.csv"),13));
	    deck.add(new DivineSpirit());
	    deck.add(new DivineSpirit());
	    deck.add(new HolyNova());
	    deck.add(new HolyNova());
	    deck.add(new ShadowWordDeath());
	    deck.add(new ShadowWordDeath());
	    deck.add(new Minion("Prophet Velen", 7, Rarity.LEGENDARY, 7, 7, false, false, false));
	    for(int i = 0; i < deck.size(); i++)
		{
	    	if (deck.get(i) instanceof Minion)
				((Minion) deck.get(i)).setListener(this);
		}
		Collections.shuffle(deck);
	}
	
}
