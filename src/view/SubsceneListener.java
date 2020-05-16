package view;

import java.io.IOException;

import exceptions.FullHandException;

public interface SubsceneListener {

	void setPlayer2(String player2);
	void setPlayer1(String player1);
	void onGameStart() throws FullHandException, IOException, CloneNotSupportedException;


}
