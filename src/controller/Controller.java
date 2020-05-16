package controller;
import java.io.File;
import java.io.IOException;
import engine.Game;
import engine.GameListener;
import exceptions.*;
import javafx.application.Application;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import model.cards.Card;
import model.cards.minions.Minion;
import model.cards.spells.*;
import model.heroes.*;
import view.*;

public class Controller extends Application implements GameListener, ViewListener{
	private HearthstoneView view;
	private Game model;
	private BattleScene battleScene;
	private boolean deckNotEmpty;
	private Object selected;
	private MediaPlayer mediaPlayer;
	private MediaPlayer exceptions;
	public void start(Stage primaryStage) throws Exception 
	{
		view = new HearthstoneView();
		view.setController(this);
		primaryStage = view.getView();
		primaryStage.setTitle("Hearthstone But Better");
		primaryStage.show();
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	public void onGameOver() 
	{
		System.out.println(model.getCurrentHero().getName());
		if(model.getCurrentHero().getCurrentHP() <= 0)
			battleScene.onGameOver(model.getOpponent());
		else
			battleScene.onGameOver(model.getCurrentHero());

	}
	
	public void endTurn()
	{
		
		try {
			resetSelected();
			int fatigueDamage = model.getOpponent().getFatigueDamage();
			model.endTurn();
			if(!model.getCurrentHero().getDeck().isEmpty())
				deckNotEmpty = true;
			else 
				deckNotEmpty = false;
			if(deckNotEmpty)
			{
				battleScene.onEndTurn(true);
			}
			else
			{
				if(model.getCurrentHero().getCurrentHP() > 0 && model.getOpponent().getCurrentHP() > 0 && fatigueDamage > 0) 
				{
					battleScene.showFatigueDamage(fatigueDamage);
				}
				battleScene.onEndTurn(false);
				battleScene.updateHeroValues();
			}
		} catch (FullHandException e) {
			battleScene.onEndTurn(false);
			battleScene.createPopup(e);
			resetSelected();

		} catch (CloneNotSupportedException e) {
			battleScene.createPopup(e);
			resetSelected();
		}
	}
	
	public void attackOpponent(boolean who)
	{
		String musicFile = model.getCurrentHero().getName()+"Exception.mp3";
        Media media = new Media(new File(musicFile).toURI().toString());
        String spellmusic = "spellSound.mp3";
    	Media spellSound = new Media(new File(spellmusic).toURI().toString());
    	MediaPlayer attackingSound = new MediaPlayer(spellSound);
        exceptions = new MediaPlayer(media);
		try 
		{
			Hero current = model.getCurrentHero();
			Hero opponent = model.getOpponent();
			Hero target =  who? (opponent):current;
			if(selected instanceof ViewCard)
			{
				Card selectedCard = ((ViewCard) selected).getModelCard();
				((ViewCard) selected).setGlow(false);
				if(selectedCard instanceof Minion) {
					current.attackWithMinion((Minion) selectedCard, target);
			    	String attackmusic = "attackingMinion.mp3";
			    	Media attacking = new Media(new File(attackmusic).toURI().toString());
			    	 attackingSound = new MediaPlayer(attacking);
					attackingSound.play();
				}
				if(selectedCard instanceof HeroTargetSpell)
				{
					current.castSpell((HeroTargetSpell) selectedCard, target);
					battleScene.onCastSpell((ViewCard) selected);
					attackingSound.play();
				}
			}
			else if (selected instanceof Mage)
			{
				((Mage) current).useHeroPower(target);
				battleScene.setHeroPowerGlow(false);
				battleScene.setHeroPowerUsed();
				attackingSound.play();
			}

			selected = null;
			battleScene.updateHeroValues();
		} catch (CannotAttackException e) {
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		} catch (NotYourTurnException e) {
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		} catch (TauntBypassException e) {
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		} catch (InvalidTargetException e) {
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		} catch (NotSummonedException e) {
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		} catch (NotEnoughManaException e) {
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		}catch (HeroPowerAlreadyUsedException e) {
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		} catch (FullHandException e) {
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		} catch (FullFieldException e) {
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		} catch (CloneNotSupportedException e) {
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		}
	}

	public void playMinion(ViewCard card)
	{
		String musicFile = model.getCurrentHero().getName()+"Exception.mp3";
        Media media = new Media(new File(musicFile).toURI().toString());
        exceptions = new MediaPlayer(media);
		try {
			model.getCurrentHero().playMinion((Minion) card.getModelCard());
			battleScene.onPlayCard(card);
		}
		catch (NotYourTurnException e) {
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		} catch (NotEnoughManaException e) {
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		} catch (FullFieldException e) {
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		}
	}
	
	public void useHeroPower() {
		String musicFile = model.getCurrentHero().getName()+"Exception.mp3";
        Media media = new Media(new File(musicFile).toURI().toString());
        exceptions = new MediaPlayer(media);
        String spellmusic = "spellSound.mp3";
    	Media spellSound = new Media(new File(spellmusic).toURI().toString());
    	MediaPlayer attackingSound = new MediaPlayer(spellSound);
		try {
			Hero hero = model.getCurrentHero();
			if(selected == null)
			{
				if(hero instanceof Mage || hero instanceof Priest)
				{
					selected = model.getCurrentHero();
					battleScene.setHeroPowerGlow(true);
				}
				else if (hero instanceof Hunter)
				{
					((Hunter) model.getCurrentHero()).useHeroPower(model.getOpponent());
					battleScene.updateHeroValues();
					battleScene.setHeroPowerUsed();
					attackingSound.play();
				}
				else
				{
					selected = null;
					model.getCurrentHero().useHeroPower();
					battleScene.redrawField();
					battleScene.updateHeroValues();
					battleScene.setHeroPowerUsed();
					attackingSound.play();
					if(hero instanceof Warlock)
					{
						Card drew = model.getCurrentHero().getHand().get(model.getCurrentHero().getHand().size() -1);
						battleScene.onCardDraw(drew);
					}
					else
						battleScene.redrawHand();
	

				}
				battleScene.updateHeroValues();
			}
			else if (selected == hero)
			{
				resetSelected();
				battleScene.setHeroPowerGlow(false);
			}
			
		} catch (NotEnoughManaException e) {
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		} catch (HeroPowerAlreadyUsedException e) {
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		} catch (NotYourTurnException e) {
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		} catch (FullHandException e) {
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		} catch (FullFieldException e) {
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		} catch (CloneNotSupportedException e) {
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		}
	}
	
	public void attackMinion(ViewCard viewCard)
	{
		String musicFile = model.getCurrentHero().getName()+"Exception.mp3";
        Media media = new Media(new File(musicFile).toURI().toString());
        exceptions = new MediaPlayer(media);
    	String attack = "attackingMinion.mp3";
    	Media attackSound= new Media(new File(attack).toURI().toString());
    	String spellmusic = "spellSound.mp3";
    	Media spellSound = new Media(new File(spellmusic).toURI().toString());
		MediaPlayer attackingSound = new MediaPlayer(spellSound);
		try 
		{
			Card card = viewCard.getModelCard();
			if(selected == null && model.getCurrentHero().getField().contains(card))
			{
				selected = viewCard;
				viewCard.setGlow(true);
			}
			else if (selected == viewCard)
			{
				resetSelected();
			}
			else if (selected != null)
			{
				if(selected instanceof ViewCard && !(((ViewCard) selected).getModelCard() instanceof Spell))
				{
					((ViewCard) selected).setGlow(false);
					selected = ((ViewCard) selected).getModelCard();
				}
				if(selected instanceof Mage)
				{
					((Mage) model.getCurrentHero()).useHeroPower((Minion) card);
					battleScene.setHeroPowerGlow(false);
					battleScene.updateHeroValues();
					battleScene.setHeroPowerUsed();
					attackingSound.play();
				}
				else if(selected instanceof Priest)
				{
					((Priest) model.getCurrentHero()).useHeroPower((Minion) card);
					battleScene.setHeroPowerGlow(false);
					battleScene.updateHeroValues();
					battleScene.setHeroPowerUsed();
			        attackingSound.play();
				}
				else if(selected instanceof Minion && card instanceof Minion)
				{
					model.getCurrentHero().attackWithMinion((Minion) selected, (Minion) card);
					
			        attackingSound = new MediaPlayer(attackSound);
			        attackingSound.play();
			        }
				else if (((ViewCard) selected).getModelCard() instanceof Spell && card instanceof Minion)
				{
					Card spell = ((ViewCard) selected).getModelCard();
					if(spell instanceof MinionTargetSpell)
					{
						model.getCurrentHero().castSpell((MinionTargetSpell) spell, (Minion) card);
						battleScene.onCastSpell((ViewCard) selected);
				        attackingSound.play();
					}
					else
					{
						model.getCurrentHero().castSpell((LeechingSpell) spell, (Minion) card);
						battleScene.onCastSpell((ViewCard) selected);
				        attackingSound.play();
					}
				}
				selected = null;
			}
			battleScene.redrawField();
		}
		catch(NotYourTurnException e)
		{
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		}
		catch(NotEnoughManaException e)
		{
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		}
		catch(InvalidTargetException e)
		{
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		}
		catch(CannotAttackException e)
		{
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		}
		catch(TauntBypassException e)
		{
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		}
		catch(NotSummonedException e)
		{
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		}
		catch(CloneNotSupportedException e)
		{
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		}
		catch(FullFieldException e)
		{
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		}
		catch(FullHandException e)
		{
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		}
		catch(HeroPowerAlreadyUsedException e)
		{
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		}
	}

	public void castSpell(ViewCard card)
	{
		String musicFile = model.getCurrentHero().getName()+"Exception.mp3";
        Media media = new Media(new File(musicFile).toURI().toString());
        String spellmusic = "spellSound.mp3";
    	Media spellSound = new Media(new File(spellmusic).toURI().toString());
    	MediaPlayer attackingSound = new MediaPlayer(spellSound);
        exceptions = new MediaPlayer(media);
		Card spell = card.getModelCard();
		try
		{
			if(selected == card)
			{
				resetSelected();
			}
			else if(spell instanceof FieldSpell)
			{
				model.getCurrentHero().castSpell((FieldSpell) spell);
				resetSelected();
				battleScene.onCastSpell(card);
				attackingSound.play();
			}
			else if(spell instanceof AOESpell)
			{
				model.getCurrentHero().castSpell((AOESpell) spell, model.getOpponent().getField());
				resetSelected();
				battleScene.onCastSpell(card);
				attackingSound.play();
			}
			else
			{
				selected = card;
				card.setGlow(true);
			}
			battleScene.redrawField();
		}
		catch(NotYourTurnException e)
		{
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		}
		catch(NotEnoughManaException e)
		{
			battleScene.createPopup(e);
			resetSelected();
			exceptions.play();
		}
	}
	
	public void resetSelected()
	{
		if(selected != null)
		{
			if(selected instanceof Hero)
				battleScene.setHeroPowerGlow(false);
			else if (selected instanceof ViewCard)
				((ViewCard) selected).setGlow(false);	
			selected = null;
		}
	}
	
	public void onGameStart(String pl1, String pl2) throws IOException, CloneNotSupportedException, FullHandException {
		Hero p1 = null; 
		Hero p2 = null;
		switch(pl1)
		{
			case "Mage": p1 = new Mage(); break;
			case "Hunter": p1 = new Hunter(); break;
			case "Priest": p1 = new Priest(); break;
			case "Paladin": p1 = new Paladin(); break;
			case "Warlock": p1 = new Warlock(); break;
		}
		
		switch(pl2)
		{
			case "Mage": p2 = new Mage(); break;
			case "Hunter": p2 = new Hunter(); break;
			case "Priest": p2 = new Priest(); break;
			case "Paladin": p2 = new Paladin(); break;
			case "Warlock": p2 = new Warlock(); break;
		}
		model = new Game(p1,p2);
		model.setListener(this);
		Hero current = model.getCurrentHero();
		Hero opponent = model.getOpponent();
		battleScene= new BattleScene(current,opponent); 
		battleScene.setController(this);
        Image cursor = new Image("/view/hearthleagueabominationcursor.png");
        battleScene.setCursor(new ImageCursor(cursor));
		view.getView().setScene(((Scene) battleScene));
		view.getMediaPlayer().stop();
	}
	
	public HearthstoneView getView(){
		return view;
	}

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
	
}
