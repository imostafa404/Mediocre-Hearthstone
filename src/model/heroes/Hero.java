package model.heroes;
import model.cards.*;
import model.cards.minions.*;
import model.cards.spells.*;
import java.util.*;
import exceptions.*;
import engine.ActionValidator;

import java.io.*;

public abstract class Hero implements MinionListener{
	private String name; 
	private int currentHP; 
	private boolean heroPowerUsed; 
	private int totalManaCrystals; 
	private int currentManaCrystals; 
	private ArrayList<Card> deck;
	private ArrayList<Minion> field;
	private ArrayList<Card> hand; 
	private  int fatigueDamage;
	private HeroListener listener;
	private ActionValidator validator;
	
	public abstract void buildDeck() throws IOException, CloneNotSupportedException;
	
	public Hero(String name) throws IOException, CloneNotSupportedException
	{
		this.name = name;
		this.currentHP = 30;
		this.heroPowerUsed = false;
		this.totalManaCrystals = 0;
		this.currentManaCrystals = 0;
		this.deck = new ArrayList<Card>();
		this.buildDeck();
		this.field = new ArrayList<Minion>();
		this.hand = new ArrayList<Card>();
		this.fatigueDamage = 0;
	}
	
	public void useHeroPower() throws NotEnoughManaException, HeroPowerAlreadyUsedException, NotYourTurnException, FullHandException, FullFieldException, CloneNotSupportedException
	{
		validator.validateTurn(this);
		validator.validateUsingHeroPower(this);
		this.setCurrentManaCrystals(this.getCurrentManaCrystals() - 2);
		this.heroPowerUsed = true;
	}
	
	public void playMinion(Minion m)  throws NotYourTurnException, NotEnoughManaException, FullFieldException
	{
		validator.validateTurn(this);
		validator.validateManaCost(m);
		validator.validatePlayingMinion(m);
		this.setCurrentManaCrystals(this.getCurrentManaCrystals() -  ((Card) m).getManaCost());
		this.hand.remove(m);
		this.field.add(m);
	}
	
	public void attackWithMinion(Minion attacker, Minion target) throws CannotAttackException, NotYourTurnException, TauntBypassException, InvalidTargetException, NotSummonedException
	{
		validator.validateTurn(this);
		validator.validateAttack(attacker, target);
		attacker.attack(target);
	}
	
	public void attackWithMinion(Minion attacker, Hero target) throws CannotAttackException, NotYourTurnException, TauntBypassException, InvalidTargetException, NotSummonedException
	{
		validator.validateTurn(this);
		validator.validateAttack(attacker, target);
		attacker.attack(target);
	}
	
	public void mageCheck(Spell s)
	{
		if(this instanceof Mage)
			for(int i = 0; i < this.field.size(); i++)
			{
				if(this.field.get(i).getName().equals("Kalycgos")) //for later: do this in real time? every time a minion is played -> check if it was kalycgos -> pass on hand and dec. spell mana cost (will have to check when he dies too -> every attack method?) 
					s.setManaCost(s.getManaCost() - 4);
			}		
	}
	
	public void castSpell(FieldSpell s) throws NotYourTurnException, NotEnoughManaException
	{
		validator.validateTurn(this);
		mageCheck((Spell)s);
		validator.validateManaCost((Spell) s);
		this.setCurrentManaCrystals(this.getCurrentManaCrystals() - ((Spell) s).getManaCost());
		s.performAction(this.field);
		this.hand.remove(s);
	}
	
	public void castSpell(AOESpell s, ArrayList<Minion >oppField) throws NotYourTurnException, NotEnoughManaException
	{
		validator.validateTurn(this);
		mageCheck((Spell)s);
		validator.validateManaCost((Spell) s);
		this.setCurrentManaCrystals(this.getCurrentManaCrystals() - ((Spell) s).getManaCost());
		s.performAction(oppField, this.field);
		this.hand.remove(s);
	}
	
	public void castSpell(MinionTargetSpell s, Minion m) throws NotYourTurnException, NotEnoughManaException, InvalidTargetException
	{
		validator.validateTurn(this);
		mageCheck((Spell)s);
		validator.validateManaCost((Spell) s);
		this.setCurrentManaCrystals(this.getCurrentManaCrystals() - ((Spell) s).getManaCost());
		s.performAction(m);
		this.hand.remove(s);
	}
	
	public void castSpell(HeroTargetSpell s, Hero h) throws NotYourTurnException, NotEnoughManaException
	{
		validator.validateTurn(this);
		mageCheck((Spell)s);
		validator.validateManaCost((Spell) s);
		this.setCurrentManaCrystals(this.getCurrentManaCrystals() - ((Spell) s).getManaCost());
		s.performAction(h);
		this.hand.remove(s);
	}
	
	public void castSpell(LeechingSpell s, Minion m) throws NotYourTurnException, NotEnoughManaException
	{
		validator.validateTurn(this);
		mageCheck((Spell)s);
		validator.validateManaCost((Spell) s);
		this.setCurrentManaCrystals(this.getCurrentManaCrystals() - ((Spell) s).getManaCost());
		this.currentHP += s.performAction(m);
		this.hand.remove(s);
	}
	
	public void endTurn() throws FullHandException, CloneNotSupportedException
	{
		listener.endTurn();
	}
	
	 public Card drawCard() throws FullHandException, CloneNotSupportedException 
	 {
		 if(this.deck.size() == 0)
			 this.setCurrentHP(this.getCurrentHP() - this.fatigueDamage++);
		 else
		 {
			 boolean chromaggus = false;
			 boolean wilfred = false;
			 Card c = this.deck.remove(0);
			 if(this.hand.size() == 10)
				 throw new FullHandException("Your Hand Is Full!",c);
			 for(int i = 0; i < this.field.size() && this.hand.size() < 9; i++)
			 {
				 if(this.field.get(i).getName().equals("Chromaggus"))
					 chromaggus  = true;
			 }
			 
			 for(int j = 0; j < this.field.size() ; j ++)
			 {
				 if(this.field.get(j).getName().equals("Wilfred Fizzlebang"))
					 wilfred = true;
			 }
			 
			 if(wilfred && this instanceof Warlock && c instanceof Minion)
				 c.setManaCost(0);
			 if(chromaggus)
				 this.hand.add(c.clone());
			 this.hand.add(c);
			 if(this.deck.size() == 0)
				 this.fatigueDamage++;
			 return c; 
		 }
		 return null;
	 }
	
	public void onMinionDeath(Minion m)
	{
		this.field.remove(m);
	}
	
	public String getName() {
		return name;
	}
	public int getCurrentHP() {
		return currentHP;
	}
	public void setCurrentHP(int currentHP) {
		if(currentHP > 30)
			this.currentHP = 30;
		else
			this.currentHP = currentHP;
		if(this.currentHP <= 0)
			listener.onHeroDeath();
	}
	public boolean isHeroPowerUsed() {
		return heroPowerUsed;
	}
	public void setHeroPowerUsed(boolean heroPowerUsed) {
		this.heroPowerUsed = heroPowerUsed;
	}
	public int getTotalManaCrystals() {
		return totalManaCrystals;
	}
	public void setTotalManaCrystals(int totalManaCrystals) {
		if(totalManaCrystals > 10)
			this.totalManaCrystals = 10;
		else if(totalManaCrystals < 0)
			this.totalManaCrystals = 0;
		else	
			this.totalManaCrystals = totalManaCrystals;
	}
	public int getCurrentManaCrystals() {
		return currentManaCrystals;
	}
	public void setCurrentManaCrystals(int currentManaCrystals) {
		if(currentManaCrystals > 10)
			this.currentManaCrystals = 10;
		else if (currentManaCrystals < 0)
			this.currentManaCrystals = 0;
		else
			this.currentManaCrystals = currentManaCrystals;
	}
	public ArrayList<Card> getDeck() {
		return deck;
	}

	public ArrayList<Minion> getField() {
		return field;
	}
	
	public ArrayList<Card> getHand() {
		return hand;
	}
	
	public int getFatigueDamage(){
		return fatigueDamage;
	}
	
	public void setListener(HeroListener listener) {
		this.listener = listener;
	}
	
	public HeroListener getListener(){
		return this.listener;
	}
	
	public void setValidator(ActionValidator validator) {
		this.validator = validator;
	}
	
	public final static ArrayList<Minion> getAllNeutralMinions(String filepath) throws IOException
	{
		ArrayList<Minion> neutralMinions = new ArrayList<Minion>();
		String currentLine = "";
		FileReader fileReader= new FileReader(filepath);
		BufferedReader br = new BufferedReader(fileReader);
		String[] minionList = null;
		while ((currentLine = br.readLine()) != null) 
		{
			minionList = currentLine.split(",");
			for(int i = 0; i < minionList.length;)
			{
				String name = minionList[i++];
				Icehowl h;
				if(name.equals("Icehowl")) 
				{
					h = new Icehowl();
					neutralMinions.add(h);
					i+=7;
				}
				else 
				{
					int manaCost = Integer.parseInt(minionList[i++]);
					Rarity rarity = null;
					switch(minionList[i++])
					{
					case "b": rarity = Rarity.BASIC;break;
					case "c": rarity = Rarity.COMMON;break;
					case "r": rarity = Rarity.RARE;break;
					case "e": rarity = Rarity.EPIC;break;
					case "l": rarity = Rarity.LEGENDARY;break;
					}
					int attack = Integer.parseInt(minionList[i++]);
					int maxHP = Integer.parseInt(minionList[i++]);
					boolean taunt = Boolean.parseBoolean(minionList[i++]);
					boolean divine = Boolean.parseBoolean(minionList[i++]);
					boolean charge = Boolean.parseBoolean(minionList[i++]);
					Minion m = new Minion(name, manaCost, rarity, attack, maxHP, taunt, divine, charge);
					neutralMinions.add(m);
				}
			}
		}
		
		return neutralMinions;
	}
	
	public final static ArrayList<Minion> getNeutralMinions(ArrayList<Minion> minions, int count) throws CloneNotSupportedException 
	{
		ArrayList<Minion> neutralMinions = new ArrayList<Minion>(); 
		ArrayList<Integer> noOfCards = new ArrayList<Integer>();
		for(int i = 0; i<minions.size(); i++)
		{
			if(minions.get(i).getRarity() == Rarity.LEGENDARY)
				noOfCards.add(1);
			else
				noOfCards.add(0);
		}
		while(count > 0)
		{
			int random = (int) (Math.random() * (minions.size()));
			if(noOfCards.get(random)< 2)
			{
				neutralMinions.add(minions.get(random).clone());
				noOfCards.set(random, noOfCards.get(random) + 1 );
				count--;
			}
		}
		return neutralMinions;
	}
	
	
	
}
