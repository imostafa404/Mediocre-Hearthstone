package model.cards.minions;
import exceptions.*;
import model.cards.*;
import model.heroes.Hero;

public class Minion extends Card implements Cloneable{
	private int attack;
	private int maxHP;
	private int currentHP;
	private boolean taunt;
	private boolean divine;
	private boolean sleeping;
	private boolean attacked;
	private MinionListener listener;
	
	public Minion(String name,int manaCost,Rarity rarity, int attack,int maxHP,boolean taunt,boolean divine,boolean charge)
	 {
		 super(name, manaCost, rarity);
		 if(attack < 0)
			 this.attack = 0;
		 else 
			 this.attack = attack;
		 this.maxHP = maxHP;
		 this.currentHP = maxHP;
		 this.taunt = taunt;
		 this.divine = divine;
		 this.sleeping = !charge;
		 this.attacked = false;
	 }
	
	public void attack(Minion target)
	{
		if(this.isDivine() && target.getAttack() != 0)
			this.setDivine(false);
		else
			this.setCurrentHP(this.getCurrentHP() - target.getAttack());
		if(target.isDivine() && this.getAttack() != 0)
			target.setDivine(false);
		else
			target.setCurrentHP(target.getCurrentHP() - this.getAttack());
		this.setAttacked(true);
	}
	
	public void attack(Hero target) throws InvalidTargetException
	{
		if(this.getName().equals("Icehowl"))
			throw new InvalidTargetException("Icehowl Can Only Attack Minions And Not Heros!");
		target.setCurrentHP(target.getCurrentHP() - this.getAttack());
		this.setAttacked(true);
	}
	
	public Minion clone() throws CloneNotSupportedException
	{
		return (Minion) super.clone();
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		if(attack < 0)
			 this.attack = 0;
		else
			this.attack = attack;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	public int getCurrentHP() {
		return currentHP;
	}

	public void setCurrentHP(int currentHP) {
		if(currentHP > this.maxHP)
			this.currentHP = this.maxHP;
		else
			this.currentHP = currentHP;
		if(this.currentHP <= 0)
			listener.onMinionDeath(this);
		
	}

	public boolean isTaunt() {
		return taunt;
	}

	public void setTaunt(boolean taunt) {
		this.taunt = taunt;
	}

	public boolean isDivine() {
		return divine;
	}

	public void setDivine(boolean divine) {
		this.divine = divine;
	}

	public boolean isSleeping() {
		return sleeping;
	}

	public void setSleeping(boolean sleeping) {
		this.sleeping = sleeping;
	}

	public boolean isAttacked() {
		return attacked;
	}

	public void setAttacked(boolean attacked) {
		this.attacked = attacked;
	}
	
	public void setListener(MinionListener listener) {
		this.listener = listener;
	}


}
