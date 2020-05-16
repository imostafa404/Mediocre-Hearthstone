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

public class Paladin extends Hero{
	
	public Paladin() throws IOException, CloneNotSupportedException
	{
		super("Uther Lightbringer");	
	}
	
	public void useHeroPower() throws NotEnoughManaException, HeroPowerAlreadyUsedException, NotYourTurnException, FullHandException, FullFieldException, CloneNotSupportedException
	{
		super.useHeroPower();
		if(this.getField().size() == 7) // use Validator? no getValidator()
			throw new FullFieldException("Your Field Is Full!");
		Minion recruit = new Minion("Silver Hand Recruit", 1, Rarity.BASIC, 1, 1, false, false, false);
		recruit.setListener(this);
		this.getField().add(recruit);
	}
	
	public void buildDeck() throws IOException , CloneNotSupportedException
	{
		ArrayList<Card> deck = getDeck();
		deck.addAll(getNeutralMinions(getAllNeutralMinions("neutral_minions.csv"),15));
	    deck.add(new SealOfChampions());
	    deck.add(new SealOfChampions());
	    deck.add(new LevelUp());
	    deck.add(new LevelUp());
	    deck.add(new Minion("Tirion Fordring", 4, Rarity.LEGENDARY, 6, 6, true, true, false));
	    for(int i = 0; i < deck.size(); i++)
		{
	    	if (deck.get(i) instanceof Minion)
				((Minion) deck.get(i)).setListener(this);
		}
		Collections.shuffle(deck);
	}
	
}
