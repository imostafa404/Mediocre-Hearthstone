package model.cards.spells;
import java.util.ArrayList;
import model.cards.*;
import model.cards.minions.Minion;

public class HolyNova extends Spell implements AOESpell{
	public HolyNova()
	{
		super("Holy Nova", 5, Rarity.BASIC);
	}

	public void performAction(ArrayList<Minion> oppField, ArrayList<Minion> curField) 
	{
		int i = 0;
		while(i < oppField.size())
		{
			if(oppField.get(i).isDivine())
				oppField.get(i).setDivine(false);
			else
			{
				if(oppField.get(i).getCurrentHP() - 2 <= 0) 
					oppField.get(i).setCurrentHP(oppField.get(i).getCurrentHP() - 2);
				else
				{	
					oppField.get(i).setCurrentHP(oppField.get(i).getCurrentHP() - 2);
					i++;
				}
			}
			
		}
		for(int j = 0; j < curField.size(); j++)
		{
			curField.get(j).setCurrentHP(curField.get(j).getCurrentHP() + 2);
		}
	}	

}
