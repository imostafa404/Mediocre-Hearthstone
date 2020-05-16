package model.cards.spells;
import exceptions.*;
import model.cards.minions.*;

public interface MinionTargetSpell {
	public void performAction(Minion m) throws InvalidTargetException;
}
