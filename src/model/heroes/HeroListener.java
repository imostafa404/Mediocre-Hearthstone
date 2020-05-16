package model.heroes;
import engine.*;
import exceptions.*;

public interface HeroListener {
	public void onHeroDeath();
	public void damageOpponent(int amount);
	public void endTurn() throws FullHandException, CloneNotSupportedException;
	//1.Switch hero turn 2.current and total mana updated for current 3.reset hero power 4. minnion attackeset and wake 5.draw a card 
}
