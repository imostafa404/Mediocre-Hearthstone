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

public class Warlock extends Hero{

	public Warlock() throws IOException, CloneNotSupportedException
	{
		super("Gul'dan");
	}
	
	public void useHeroPower() throws NotEnoughManaException, HeroPowerAlreadyUsedException, NotYourTurnException, FullHandException, FullFieldException, CloneNotSupportedException
	{
		super.useHeroPower();
		if(this.getHand().size() == 10)
			throw new FullHandException(this.drawCard());
		this.drawCard();
		this.setCurrentHP(this.getCurrentHP() - 2);
	}

	public void buildDeck() throws IOException , CloneNotSupportedException
	{
		ArrayList<Card> deck = this.getDeck();
		deck.addAll(getNeutralMinions(getAllNeutralMinions("neutral_minions.csv"),13));
		deck.add(new CurseOfWeakness());
		deck.add(new CurseOfWeakness());
		deck.add(new SiphonSoul());
		deck.add(new SiphonSoul());
		deck.add(new TwistingNether());
		deck.add(new TwistingNether());
		deck.add(new Minion("Wilfred Fizzlebang", 6, Rarity.LEGENDARY, 4, 4, false, false, false));
		for(int i = 0; i < deck.size(); i++)
		{
			if (deck.get(i) instanceof Minion)
				((Minion) deck.get(i)).setListener(this);
		}
		Collections.shuffle(deck);
	}
}
