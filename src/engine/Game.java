package engine;
import exceptions.CannotAttackException;
import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughManaException;
import exceptions.NotSummonedException;
import exceptions.NotYourTurnException;
import exceptions.TauntBypassException;
import model.cards.*;
import model.cards.minions.Minion;
import model.heroes.*;

public class Game implements ActionValidator, HeroListener {
	private Hero firstHero;
	private Hero secondHero;
	private Hero currentHero;
	private Hero opponent;
	private GameListener listener;
	
	public Game(Hero p1, Hero p2) throws FullHandException, CloneNotSupportedException
	{
		this.firstHero = p1;
		this.firstHero.setListener(this);
		this.firstHero.setValidator(this);
		this.secondHero = p2;
		this.secondHero.setListener(this);
		this.secondHero.setValidator(this);
		int randomAssign = (int) (Math.random()*2);
		if(randomAssign == 0)
		{
			this.currentHero = p1;
			p1.setTotalManaCrystals(1);
			p1.setCurrentManaCrystals(1);
			this.opponent = p2;
		}
		else 
		{
			this.currentHero = p2;
			p2.setTotalManaCrystals(1);
			p2.setCurrentManaCrystals(1);
			this.opponent = p1;
		}
		this.currentHero.drawCard();
		this.currentHero.drawCard();
		this.currentHero.drawCard();
		this.opponent.drawCard();
		this.opponent.drawCard();
		this.opponent.drawCard();
		this.opponent.drawCard();
	}
	
	public void onHeroDeath() 
	{
		listener.onGameOver();
	}

	public void damageOpponent(int amount) 
	{
		this.opponent.setCurrentHP(this.opponent.getCurrentHP() - amount);
	}

	public void endTurn() throws FullHandException, CloneNotSupportedException 
	{
		if(this.firstHero == this.currentHero)
		{
			this.opponent = this.firstHero;
			this.currentHero = this.secondHero;
		}
		else
		{
			this.opponent = this.secondHero;
			this.currentHero = this.firstHero;
		}
		this.currentHero.setTotalManaCrystals(this.currentHero.getTotalManaCrystals() + 1);
		this.currentHero.setCurrentManaCrystals(this.currentHero.getTotalManaCrystals());
		this.currentHero.setHeroPowerUsed(false);
		for(int i = 0; i < this.currentHero.getField().size(); i++) 
		{
			this.currentHero.getField().get(i).setAttacked(false);
			this.currentHero.getField().get(i).setSleeping(false);
		}
		this.currentHero.drawCard();
	}

	public void validateTurn(Hero user) throws NotYourTurnException 
	{
		if (this.currentHero != user)
			throw new NotYourTurnException("It Is Not Your Turn!");
	}

	public void validateAttack(Minion attacker, Minion target) throws CannotAttackException, NotSummonedException, TauntBypassException, InvalidTargetException 
	{
		if (!this.currentHero.getField().contains(attacker))
			throw new NotSummonedException("You Have To Summon The Minion First!");
		if (this.currentHero.getField().contains(target) || this.currentHero.getHand().contains(target))
			throw new InvalidTargetException("This Minion Is An Ally!");
		if(!this.opponent.getField().contains(target))
			throw new NotSummonedException("This Minion is Not Summoned!");
		if(attacker.isSleeping())
			throw new CannotAttackException("This Minion Is Sleeping, Wait One Turn!");
		if(attacker.isAttacked())
			throw new CannotAttackException("This Minion Already Attacked Once This Turn!");
		if(attacker.getAttack() == 0)
			throw new CannotAttackException("This Minion Has Zero Attack!");
		for(int i = 0; i < opponent.getField().size() && !target.isTaunt(); i++)
		{
			if(opponent.getField().get(i).isTaunt())
				throw new TauntBypassException("A Taunt Minion Is In The Way!");
		}
	}

	public void validateAttack(Minion attacker, Hero target) throws CannotAttackException, NotSummonedException, TauntBypassException, InvalidTargetException 
	{
		if (!this.currentHero.getField().contains(attacker))
			throw new NotSummonedException("You Have To Summon The Minion First!");
		if (this.currentHero == target)
			throw new InvalidTargetException("You Cannot Attack Your Own Hero!");
		if(attacker.isSleeping())
			throw new CannotAttackException("This Minion Is Sleeping, Wait One Turn!");
		if(attacker.isAttacked())
			throw new CannotAttackException("This Minion Already Attacked Once This Turn!");
		if(attacker.getAttack() == 0)
			throw new CannotAttackException("This Minion Has Zero Attack!");
		for(int i = 0; i < opponent.getField().size(); i++)
		{
			if(opponent.getField().get(i).isTaunt())
				throw new TauntBypassException("A Taunt Minion Is In The Way!");
		}		
	}

	public void validateManaCost(Card card) throws NotEnoughManaException 
	{
		if(this.currentHero.getCurrentManaCrystals() < card.getManaCost())
			throw new NotEnoughManaException("You Dont Have enough Mana Crystals To Play This Card!");
	}

	public void validatePlayingMinion(Minion minion) throws FullFieldException 
	{
		if(this.currentHero.getField().size() == 7)
			throw new FullFieldException("Your Field Is Full!");
	}

	public void validateUsingHeroPower(Hero hero) throws NotEnoughManaException, HeroPowerAlreadyUsedException 
	{
		if(hero.isHeroPowerUsed())
			throw new HeroPowerAlreadyUsedException("Hero Power Already Used!");
		if(hero.getCurrentManaCrystals() < 2)
			throw new NotEnoughManaException("You Donnot Have Enough Mana To Use Your Hero Power!");
	}
	
	public Hero getCurrentHero() {
		return currentHero;
	}
	
	public Hero getOpponent() {
		return opponent;
	}
	
	public void setListener(GameListener listener) {
		this.listener = listener;
	}

}
