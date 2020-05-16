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

public class Hunter extends Hero{
	
	public Hunter() throws IOException, CloneNotSupportedException
	{
		super("Rexxar");
	}
	
	public void useHeroPower(Hero target) throws NotEnoughManaException, HeroPowerAlreadyUsedException, NotYourTurnException, FullHandException, FullFieldException, CloneNotSupportedException
	{
		super.useHeroPower();
		this.getListener().damageOpponent(2);
	}
	
	public void buildDeck() throws IOException, CloneNotSupportedException
	{
		ArrayList<Card> deck = this.getDeck();
		deck.addAll(getNeutralMinions(getAllNeutralMinions("neutral_minions.csv"),15));
		deck.add(new KillCommand());
		deck.add(new KillCommand());
		deck.add(new MultiShot());
		deck.add(new MultiShot());
		deck.add(new Minion("King Krush", 9, Rarity.LEGENDARY, 8, 8, false, false, true));
		for(int i = 0; i < deck.size(); i++)
		{
			if (deck.get(i) instanceof Minion)
				((Minion) deck.get(i)).setListener(this);
		}
		Collections.shuffle(deck);


	}

}
