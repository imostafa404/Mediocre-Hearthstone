package model.cards.spells;
import java.util.ArrayList;
import model.cards.*;
import model.cards.minions.Minion;

public class MultiShot extends Spell implements AOESpell{
	public MultiShot()
	{
		super("Multi-Shot", 4, Rarity.BASIC);
	}

	public void performAction(ArrayList<Minion> oppField, ArrayList<Minion> curField) 
	{
		if(oppField.size() == 1)
			if(oppField.get(0).isDivine())
				oppField.get(0).setDivine(false);
			else
				oppField.get(0).setCurrentHP(oppField.get(0).getCurrentHP() - 3);
		else if ((oppField.size() != 0))
		{
			int attacked = -1;
			int i = 0;
			while(i<2)
			{
				int j = ((int) (Math.random()*oppField.size()));
				if(j!= attacked)
				{
					if(oppField.get(j).isDivine())
						oppField.get(j).setDivine(false);
					else
						oppField.get(j).setCurrentHP(oppField.get(j).getCurrentHP() - 3);
					attacked = j;
					i++;
				}
			}
		}
	}
}
