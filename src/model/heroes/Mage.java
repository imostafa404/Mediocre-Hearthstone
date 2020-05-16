package model.heroes;
import model.cards.*;
import model.cards.spells.*;
import model.cards.minions.*;
import java.util.*;

import engine.Game;
import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.NotEnoughManaException;
import exceptions.NotYourTurnException;

import java.io.*;

public class Mage extends Hero{

	public Mage() throws IOException, CloneNotSupportedException
	{
		super("Jaina Proudmoore");
	}
	
	public void useHeroPower(Hero target) throws NotEnoughManaException, HeroPowerAlreadyUsedException, NotYourTurnException, FullHandException, FullFieldException, CloneNotSupportedException
	{
		super.useHeroPower();
		this.getListener().damageOpponent(1);
	}
	
	public void useHeroPower(Minion target) throws NotEnoughManaException, HeroPowerAlreadyUsedException, NotYourTurnException, FullHandException, FullFieldException, CloneNotSupportedException
	{
		super.useHeroPower();
		if(target.isDivine())
			target.setDivine(false);
		else
			target.setCurrentHP(target.getCurrentHP() - 1);
	}
	
	public void buildDeck() throws IOException ,CloneNotSupportedException
	{
		ArrayList<Card> deck = this.getDeck();
		deck.addAll(getNeutralMinions(getAllNeutralMinions("neutral_minions.csv"),13));
	    deck.add(new Polymorph());
	    deck.add(new Polymorph());
	    deck.add(new Flamestrike());
	    deck.add(new Flamestrike());
	    deck.add(new Pyroblast());
	    deck.add(new Pyroblast());
	    deck.add(new Minion("Kalycgos", 10, Rarity.LEGENDARY, 4, 12, false, false, false));
	    for(int i = 0; i < deck.size(); i++)
		{
	    	if (deck.get(i) instanceof Minion)
				((Minion) deck.get(i)).setListener(this);
		}
		Collections.shuffle(deck);
	}
}
