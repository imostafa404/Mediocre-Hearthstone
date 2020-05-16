package view;

import java.io.IOException;

import exceptions.FullHandException;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import model.cards.Card;
import model.cards.minions.Minion;

public interface ViewListener {

	void onGameStart(String player1, String player2) throws IOException, CloneNotSupportedException, FullHandException;

	void playMinion(ViewCard card);

	void endTurn();

	void attackMinion(ViewCard card);

	void castSpell(ViewCard card);

	void useHeroPower();

	void attackOpponent(boolean target);

	void resetSelected();

	MediaPlayer getMediaPlayer();


	
}
