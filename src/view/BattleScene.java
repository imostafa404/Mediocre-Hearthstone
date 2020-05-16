package view;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import controller.Controller;
import exceptions.FullHandException;
import exceptions.HearthstoneException;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.cards.Card;
import model.cards.Rarity;
import model.cards.minions.Minion;
import model.heroes.Hero;
import model.cards.spells.*;

public class BattleScene extends Scene{
	private AnchorPane superLayout;
	private AnchorPane layout;
	private AnchorPane errorMsgLayout;
	private String backPath = "/view/fieldResources/smallercolorfulbackground.png";
	private String  mageIcon = "/view/fieldResources/HeroIcons/HearthMage.png";
	private String  hunterIcon = "/view/fieldResources/HeroIcons/HearthHunter.png";
	private String  warlockIcon = "/view/fieldResources/HeroIcons/HearthWarlock.png";
	private String  priestIcon = "/view/fieldResources/HeroIcons/HearthPriest.png";
	private String  paladinIcon = "/view/fieldResources/HeroIcons/HearthPaladin.png";
	private String  magePower = "/view/fieldResources/heroPowers/mageheropower.png";
	private String  hunterPower = "/view/fieldResources/heroPowers/hunterheropower.png";
	private String  warlockPower = "/view/fieldResources/heroPowers/warlockheropower.png";
	private String  priestPower = "/view/fieldResources/heroPowers/priestheropower.png";
	private String  paladinPower = "/view/fieldResources/heroPowers/paladinheropower.png";
	private ImageView currentHero;
	private ImageView opponentHero;
	private ImageView currentHeroPower;
	private ImageView opponentHeroPower;
	private Text currHP;
	private Text oppHP;
	private Text currManaCurr;
	private Text currManaTotal;
	private Text oppMana;
	private int currentRemainingCards;
	private int opponentRemainingCards;
	private ArrayList<ViewCard> currHand;
	private ArrayList<ViewCard> oppHand;
	private ArrayList<ImageView> cardBacks;
	private ArrayList<ViewCard> currField;
	private ArrayList<ViewCard> oppField;
	private ViewListener controller;
	private Text currRemaining;
	private Text oppRemaining;
	private Hero current;
	private Hero opponent;
	private ExceptionPopup popup;
	private int order;
	private ImageView heroPowerUsed;
	private MediaPlayer mediaplayer;
	private MediaPlayer gameMusic;

	public BattleScene(Hero current, Hero opponent)
	{	
		super(new AnchorPane(),1200,700);
		this.current = current;
		this.opponent = opponent;
		superLayout = (AnchorPane) super.getRoot();
		errorMsgLayout = new AnchorPane();
		layout = new AnchorPane();
		layout.setMinWidth(1200);
        announcerCall();
		Image image = new Image(backPath,1200,700,false,true);
		BackgroundImage background = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
		layout.setBackground(new Background(background));
		superLayout.setBackground(new Background(background));
		setHeros(current, opponent);
		currentRemainingCards = current.getDeck().size();
		opponentRemainingCards = opponent.getDeck().size();
		setEndTurnButton();
		setCardBack();
		currHand = new ArrayList<ViewCard>();
		oppHand = new ArrayList<ViewCard>();
		cardBacks = new ArrayList<ImageView>();
		currField = new ArrayList<ViewCard>();
		oppField = new ArrayList<ViewCard>();
		for(int i = 0; i < current.getHand().size(); i ++)
		{
			onCardDraw(current.getHand().get(i));
		}
		fixEnemyCards();
		drawEnemyCards();
        superLayout.getChildren().addAll(layout);
        layout.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getTarget() instanceof AnchorPane) {
                    controller.resetSelected();
                    }
            }
        });
        setMuteButton();
		String musicFile = "gameMusic.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        gameMusic = new MediaPlayer(sound);
        gameMusic.setVolume(0.1);
        gameMusic.setCycleCount(MediaPlayer.INDEFINITE);
        gameMusic.play();
	}
	public void announcerCall() {
		String musicFile = current.getName()+".mp3";
        Media player1 = new Media(new File(musicFile).toURI().toString());
        musicFile = "Versus.mp3";
        Media versusound = new Media(new File(musicFile).toURI().toString());
        musicFile = opponent.getName()+".mp3";
        Media player2 = new Media(new File(musicFile).toURI().toString());
        ObservableList<Media> mediaList = FXCollections.observableArrayList();
        mediaList.addAll(player1, versusound, player2);
        playMediaTracks(mediaList);
	}
	
	private void playMediaTracks(ObservableList<Media> mediaList) {
        if (mediaList.size() == 0)
            return;

        mediaplayer = new MediaPlayer(mediaList.remove(0));
        mediaplayer.play();

        mediaplayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                playMediaTracks(mediaList);
            }
        });
    }
	
	public void onGameOver(Hero winningHero)
	{
		VictoryScreen victoryScreen = new VictoryScreen(winningHero, controller);
		ColorAdjust monochrome = new ColorAdjust();
        monochrome.setSaturation(-0.6);
        layout.setEffect(monochrome);
        errorMsgLayout.getChildren().clear();
        errorMsgLayout.getChildren().add(victoryScreen);
        superLayout.getChildren().addAll(errorMsgLayout);
        errorMsgLayout.setLayoutX(0);
        String musicFile = "victory.mp3";
        Media victory = new Media(new File(musicFile).toURI().toString());
        mediaplayer = new MediaPlayer(victory);
        gameMusic.stop();
        mediaplayer.play();
	}
	
	public void onCardDraw(Card m)
    {
        ViewCard newCard;
        if( m instanceof Minion)
        {
            newCard = new ViewCard((Minion) m);
        }
        else 
        {
            newCard = new ViewCard((Spell) m);
        }
        newCard.setHandSize();
        currHand.add(newCard);
        redrawHand();
        updateCardBack();
        newCard.setViewOrder(-order);
        order++;
        int cardOrder = (int) newCard.getViewOrder();
        newCard.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent e) 
            {
                if( m instanceof Minion)
                {
                    controller.playMinion(newCard);
                }
                else 
                {
                    controller.castSpell(newCard);
                }
            }            
        });
        newCard.setOnMouseEntered(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent arg0) 
            {
                newCard.setLayoutY(newCard.getLayoutY() - 92);
                newCard.setHoverSize();
                newCard.toFront();
                newCard.setViewOrder(-100);
                
            }            
        });
        newCard.setOnMouseExited(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent arg0) 
            {
                newCard.setLayoutY(newCard.getLayoutY() + 92);
                newCard.setHandSize();
                newCard.setViewOrder(cardOrder);
            }        
         }); 
    }
	
	public void onPlayCard(ViewCard card)
	{
		String musicFile = "playingCard.mp3";
        Media media = new Media(new File(musicFile).toURI().toString());
        mediaplayer = new MediaPlayer(media);
        mediaplayer.play();
		currHand.remove(card);
		layout.getChildren().remove(card);
		Card modelCard = card.getModelCard();
		ViewCard newCard;
		if(modelCard instanceof Minion)
			newCard = new ViewCard((Minion) modelCard);
		else
			newCard = new ViewCard((Spell) modelCard);
    	currField.add(newCard);
    	newCard.setFieldSize();
    	newCard.setEffect(new DropShadow());
    	redrawHand();
    	redrawField();
    	updateHeroValues();
    	newCard.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent e) 
            {
            	controller.attackMinion(newCard);
        		updateHeroValues();
            }            
        });
  
	}
	
	public void onCastSpell(ViewCard card)
	{
		layout.getChildren().remove(card);
		currHand.remove(card);
		updateHeroValues();
		redrawHand();
	}
	
	public void onEndTurn(boolean validDraw)
	{
		setHeros(opponent, current);
		for(ViewCard anyCard: currHand)
		{
			layout.getChildren().remove(anyCard);
		}
		ArrayList<ViewCard> temp = currHand;
		currHand = oppHand;
		oppHand = temp;
		temp = currField;
		currField = oppField;
		oppField = temp;
		drawEnemyCards();
		if(validDraw)
		{
			Card card = current.getHand().get(current.getHand().size()-1);
			onCardDraw(card);
		}
		else
		{
	    	redrawHand();
			updateCardBack();
		}
		redrawField();
	}
	
	public void showFatigueDamage(int damage) 
	{
		String musicFile = "fatigue.mp3";
        Media media = new Media(new File(musicFile).toURI().toString());
        mediaplayer = new MediaPlayer(media);
        mediaplayer.play();
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        errorMsgLayout.setPrefWidth(900);
        pause.setOnFinished(event ->{
        	layout.setEffect(null);
            superLayout.getChildren().removeAll(errorMsgLayout);
            errorMsgLayout.getChildren().clear();
        });
        	FatiguePopup fatigue = new FatiguePopup(current, damage);
    		ColorAdjust monochrome = new ColorAdjust();
            monochrome.setSaturation(-0.6);
            layout.setEffect(monochrome);
            errorMsgLayout.getChildren().clear();
            errorMsgLayout.getChildren().add(fatigue);
            errorMsgLayout.setLayoutX(0);
            superLayout.getChildren().addAll(errorMsgLayout);
            pause.play();
        }
	

	public void redrawField()
	{
		for(int i = 0; i < oppField.size(); i ++) 
		{
			layout.getChildren().remove(oppField.get(i));
		}
		for(int i = 0; i < currField.size(); i ++)
		{
			layout.getChildren().remove(currField.get(i));
		}
		for(int i = 0; i < oppField.size(); i ++)
		{
			if(((Minion) oppField.get(i).getModelCard()).getCurrentHP() <= 0)
			{
				oppField.remove(oppField.get(i));
				i--;
			}
			else if(!oppField.get(i).upToDate())
			{
				ViewCard temp = new ViewCard((Minion) oppField.get(i).getModelCard());
				temp.setOnMouseClicked(new EventHandler<MouseEvent>()
		        {
		            public void handle(MouseEvent e) 
		            {
		            	controller.attackMinion(temp);
		            }
		        });
				temp.setFieldSize();
				temp.setEffect(new DropShadow());
				oppField.set(i,temp);
			}
		}
		for(int i = 0; i < currField.size(); i ++)
		{
			if(((Minion) currField.get(i).getModelCard()).getCurrentHP() <= 0)
			{
				currField.remove(currField.get(i));
				i--;
			}
			else if(!currField.get(i).upToDate())
			{
				ViewCard temp = new ViewCard( (Minion) currField.get(i).getModelCard());
				temp.setOnMouseClicked(new EventHandler<MouseEvent>()
		        {
		            public void handle(MouseEvent e) 
		            {
		            	controller.attackMinion(temp);
		            }
		        });
				temp.setFieldSize();
				temp.setEffect(new DropShadow());
				currField.set(i,temp);
			}
		}
		if(current.getField().size() > currField.size())
		{
			ViewCard newCard = new ViewCard(current.getField().get(current.getField().size() - 1));
			newCard.setOnMouseClicked(new EventHandler<MouseEvent>()
	        {
	            public void handle(MouseEvent e) 
	            {
	            	controller.attackMinion(newCard);
	            }            
	        });
			newCard.setFieldSize();
			newCard.setEffect(new DropShadow());
			currField.add(newCard);
		}
		int left = 1;
        int right = 0;
        int fieldMiddle = 0;
        for(int i = 0; i < currField.size(); i ++)
        {    if(fieldMiddle %2 == 0) {
            
            currField.get(i).setLayoutX(405 + left * 120);
            currField.get(i).setLayoutY(350);
            layout.getChildren().add(currField.get(i));
            left++;
        }
        else {
            currField.get(i).setLayoutX(405 - right * 120);
            currField.get(i).setLayoutY(350);
            layout.getChildren().add(currField.get(i));
            right++;
        }
        fieldMiddle++;
        }
        fieldMiddle = 0;
        left = 1;
        right = 0;
        for(int i = 0; i < oppField.size(); i ++)
        {    if(fieldMiddle %2 == 0) {
            
            oppField.get(i).setLayoutX(405 + left * 120);
            oppField.get(i).setLayoutY(165);
            layout.getChildren().add(oppField.get(i));
            left++;
        }
        else {
            oppField.get(i).setLayoutX(405 - right * 120);
            oppField.get(i).setLayoutY(165);
            layout.getChildren().add(oppField.get(i));
            right++;
        }
        fieldMiddle++;
        }
	}
	
	public void redrawHand()
	{
		for(ViewCard card: currHand)
		{
			layout.getChildren().remove(card);
		}
		int startingPosition = 600 - (currHand.size() * 38);
		for(int i = 0; i < currHand.size() ; i ++)
		{
			currHand.get(i).setLayoutX(startingPosition + i * 78);			
			currHand.get(i).setLayoutY(590);
			if(currHand.get(i).getModelCard().getManaCost()<=current.getCurrentManaCrystals())
	        {
	            DropShadow glow = new DropShadow();
	            glow.setColor(Color.GREENYELLOW);
	            glow.setSpread(0.4);
	            currHand.get(i).setEffect(glow);
	        }
	        else
	            currHand.get(i).setEffect(null);
			layout.getChildren().add(currHand.get(i));
		}
		
	}
	
	public void drawEnemyCards()
	{
		for(ImageView back: cardBacks)
		{
			layout.getChildren().remove(back);
		}
		cardBacks = new ArrayList<ImageView>();
		for(int i = 0; i < oppHand.size() && i < 10; i++)
		{
			ImageView cardBack = new ImageView(new Image("/view/fieldResources/cardVariables/fullcardback.png" ,200,100,true, true));
			cardBack.setLayoutX(850 - i * 70);
			cardBack.setLayoutY(52);
			cardBacks.add(cardBack);
			cardBack.setEffect(new DropShadow());
			layout.getChildren().add(cardBack);
		}
	}
	
	public void setEndTurnButton() 
	{
        MenuButton endButton = new MenuButton("End Turn!");
        try {
            endButton.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),15));
        } catch (FileNotFoundException e) {
            endButton.setFont(Font.font("Verdana",20));
        }
        endButton.setLayoutX(1040);
        endButton.setLayoutY(327);
        endButton.setPrefWidth(110);
        endButton.setPrefHeight(57);
        endButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent e) 
            {
            	controller.endTurn();
            }
        });
        endButton.setEffect(new DropShadow());
        layout.getChildren().add(endButton);
	}
	
	public void setCardBack() 
	{
        ImageView oppDeck = new ImageView(new Image("/view/fieldResources/cardVariables/hearthcardback.png",400,170,true, true));
        ImageView currDeck = new ImageView(new Image("/view/fieldResources/cardVariables/hearthcardback.png",300,130,true, true));
        currRemaining = new Text(currentRemainingCards + "");
        currRemaining.setFill(Color.ANTIQUEWHITE);
        currRemaining.setStroke(Color.BLACK);
        
        try {
			currRemaining.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),40));
		} catch (FileNotFoundException e) {
			currRemaining.setFont(Font.font("Verdana",20));
		}
        oppRemaining = new Text(opponentRemainingCards + "");
        oppRemaining.setFill(Color.ANTIQUEWHITE);
        oppRemaining.setStroke(Color.BLACK);
        try {
        	oppRemaining.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),30));
		} catch (FileNotFoundException e) {
			oppRemaining.setFont(Font.font("Verdana",20));
		}
        currRemaining.setLayoutX(1069);
        currRemaining.setLayoutY(584);
        oppRemaining.setLayoutX(80);
        oppRemaining.setLayoutY(126);
        oppDeck.setLayoutX(1030);
        oppDeck.setLayoutY(486);
        currDeck.setLayoutX(50);
        currDeck.setLayoutY(50);
        currDeck.setEffect(new DropShadow());
        oppDeck.setEffect(new DropShadow());
        layout.getChildren().add(currDeck);
        layout.getChildren().add(oppDeck);
        layout.getChildren().add(oppRemaining);
        layout.getChildren().add(currRemaining);
    }
    
	public void updateCardBack() 
	{
        currentRemainingCards = current.getDeck().size();
        opponentRemainingCards = opponent.getDeck().size();
        if(currentRemainingCards < 10) 
            currRemaining.setLayoutX(1080); 
        else
            currRemaining.setLayoutX(1069); 
        if(opponentRemainingCards < 10)
            oppRemaining.setLayoutX(90);
        else
        	oppRemaining.setLayoutX(80); 
        currRemaining.setText(currentRemainingCards + "");
        oppRemaining.setText(opponentRemainingCards + "");
    }
	
	public void fixEnemyCards() // plz find better solution
    {
        for(int i = 0; i < opponent.getHand().size(); i ++)
        {
            
            ViewCard newCard;
            Card m = opponent.getHand().get(i);
            
            if( m instanceof Minion)
            {
                newCard = new ViewCard((Minion) m);
                newCard.setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    public void handle(MouseEvent e) 
                    {
                        controller.playMinion(newCard);
                    }            
                });
            }
            else 
            {
                newCard = new ViewCard((Spell) m);
                newCard.setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    public void handle(MouseEvent e) 
                    {
                        controller.castSpell(newCard);
                    }            
                });
            }
            newCard.setViewOrder(-order);
            order++;
            int cardOrder = (int) newCard.getViewOrder();
            newCard.setOnMouseEntered(new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent arg0) 
                {
                    newCard.setLayoutY(newCard.getLayoutY() - 92);
                    newCard.setHoverSize();
                    newCard.toFront();
                    newCard.setViewOrder(-100);
                }            
            });
            newCard.setOnMouseExited(new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent arg0) 
                {
                    newCard.setLayoutY(newCard.getLayoutY() + 92);
                    newCard.setHandSize();
                    newCard.setViewOrder(cardOrder);
                }        
             }); 
            newCard.setHandSize();
            oppHand.add(newCard);
        }
    }
	
	public void setHeros(Hero current, Hero opponent)
	{
		if(currentHero != null)
		{
			layout.getChildren().remove(currentHero);
			layout.getChildren().remove(opponentHero);
			layout.getChildren().remove(currentHeroPower);
			layout.getChildren().remove(opponentHeroPower);
		}
		if(heroPowerUsed != null)
			layout.getChildren().remove(heroPowerUsed);
		this.current = current;
		this.opponent = opponent;		
		switch(current.getName())
		{
			case "Jaina Proudmoore": currentHero = new ImageView(new Image(mageIcon,400,250,true, true)); 
			currentHeroPower = new ImageView(new Image(magePower,80,80,true, true)); break;
			case "Rexxar": currentHero = new ImageView(new Image(hunterIcon,400,250,true, true));
			currentHeroPower = new ImageView(new Image(hunterPower,80,80,true, true)); break;
			case "Uther Lightbringer": currentHero = new ImageView(new Image(paladinIcon,400,250,true, true));
			currentHeroPower = new ImageView(new Image(paladinPower,80,80,true, true)); break;
			case "Anduin Wrynn": currentHero = new ImageView(new Image(priestIcon,400,250,true, true));
			currentHeroPower = new ImageView(new Image(priestPower,80,80,true, true)); break;
			case "Gul'dan":currentHero = new ImageView(new Image(warlockIcon,400,250,true, true));
			currentHeroPower = new ImageView(new Image(warlockPower,80,80,true, true)); break;
		}
		switch(opponent.getName())
		{
			case "Jaina Proudmoore": opponentHero = new ImageView(new Image(mageIcon,400,220,true, true)); 
			opponentHeroPower = new ImageView(new Image(magePower,80,80,true, true)); break;
			case "Rexxar": opponentHero = new ImageView(new Image(hunterIcon,400,220,true, true));
			opponentHeroPower = new ImageView(new Image(hunterPower,80,80,true, true)); break;
			case "Uther Lightbringer": opponentHero = new ImageView(new Image(paladinIcon,400,220,true, true));
			opponentHeroPower = new ImageView(new Image(paladinPower,80,80,true, true)); break;
			case "Anduin Wrynn": opponentHero = new ImageView(new Image(priestIcon,400,220,true, true));
			opponentHeroPower = new ImageView(new Image(priestPower,80,80,true, true)); break;
			case "Gul'dan":opponentHero = new ImageView(new Image(warlockIcon,400,220,true, true));
			opponentHeroPower = new ImageView(new Image(warlockPower,80,80,true, true)); break;
		}
//		currentHeroPower.setOnMouseEnter(new EventHandler<MouseEvent>()
//		{
//			public void handle(MouseEvent e) 
//			{
//				controller.useHeroPower();
//			}
//		});
//		currentHeroPower.setOnMouseExit(new EventHandler<MouseEvent>()
//		{
//			public void handle(MouseEvent e) 
//			{
//				controller.useHeroPower();
//			}
//		});
		currentHeroPower.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent e) 
			{
				controller.useHeroPower();
			}
		});
		opponentHero.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent e)
			{
				controller.attackOpponent(true);
			}
		});
		currentHero.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent e)
			{
				controller.attackOpponent(false);
			}
		});
		currentHero.setLayoutX(20);
		currentHero.setLayoutY(470);
		currentHeroPower.setLayoutX(177);
		currentHeroPower.setLayoutY(517);
		opponentHero.setLayoutX(1000);
		opponentHero.setLayoutY(48);
		opponentHeroPower.setLayoutX(930);
		opponentHeroPower.setLayoutY(65);
		currentHero.setEffect(new DropShadow());
		currentHeroPower.setEffect(new DropShadow());
		opponentHero.setEffect(new DropShadow());
		opponentHeroPower.setEffect(new DropShadow());
		layout.getChildren().add(currentHeroPower);
		layout.getChildren().add(currentHero);
		layout.getChildren().add(opponentHeroPower);
		layout.getChildren().add(opponentHero);
		updateHeroValues();
	}

	public void updateHeroValues() 
	{
		if(currHP != null)
		{
			layout.getChildren().remove(currHP);
			layout.getChildren().remove(oppHP);
			layout.getChildren().remove(currManaCurr);
			layout.getChildren().remove(currManaTotal);
			layout.getChildren().remove(oppMana);
		}
		currHP = new Text(Integer.toString(current.getCurrentHP()));
		oppHP = new Text(Integer.toString(opponent.getCurrentHP()));
		currManaCurr = new Text(Integer.toString(current.getCurrentManaCrystals()));
		currManaTotal = new Text(Integer.toString(current.getTotalManaCrystals()));
		Text currManaSlash = new Text("/");
		oppMana = new Text(Integer.toString(opponent.getTotalManaCrystals()));
		currHP.setFill(Color.WHITE);
		currHP.setStyle("-fx-stroke: black; -fx-stroke-width: 1");
		oppHP.setFill(Color.WHITE);
		oppHP.setStyle("-fx-stroke: black; -fx-stroke-width: 1");
		currManaCurr.setFill(Color.WHITE);
		currManaCurr.setStyle("-fx-stroke: black; -fx-stroke-width: 1");
		currManaTotal.setFill(Color.WHITE);
		currManaTotal.setStyle("-fx-stroke: black; -fx-stroke-width: 0.8");
		oppMana.setFill(Color.WHITE);
		oppMana.setStyle("-fx-stroke: black; -fx-stroke-width: 1");
		currManaSlash.setFill(Color.WHITE);
		currManaSlash.setStyle("-fx-stroke: black; -fx-stroke-width: 1");

		try
		{
			if(current.getCurrentHP()<10)
				currHP.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),34));
        	else
        		currHP.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),26));
			
			if(opponent.getCurrentHP()<10)
				oppHP.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),32));
        	else
        		oppHP.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),26));
			
			if(current.getCurrentManaCrystals()==10)
				currManaCurr.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),18));
        	else
        		currManaCurr.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),26));
			if(opponent.getTotalManaCrystals()==10)
				oppMana.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),28));
        	else
        		oppMana.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),30));
			if(current.getTotalManaCrystals()==10)
				currManaTotal.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),15));
        	else
        		currManaTotal.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),19));
			if(current.getTotalManaCrystals()==10)
				currManaSlash.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),17));
        	else
    			currManaSlash.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),22));
			

		}
		catch (FileNotFoundException e) {
			currHP.setFont(Font.font("Verdana",30));
			oppHP.setFont(Font.font("Verdana",30));
			currManaCurr.setFont(Font.font("Verdana",30));
			currManaTotal.setFont(Font.font("Verdana",30));
			oppMana.setFont(Font.font("Verdana",30));
        };
        if(current.getCurrentHP()<10)
            currHP.setLayoutX(169);
        else
            currHP.setLayoutX(164);
		currHP.setLayoutY(647);
		
		if(current.getCurrentManaCrystals()==10)
			currManaCurr.setLayoutX(22);
	    else
	    	currManaCurr.setLayoutX(28);
		if(current.getCurrentManaCrystals()==10)
			currManaCurr.setLayoutY(640);
        else
        	currManaCurr.setLayoutY(645);
		if(opponent.getCurrentHP()<10)
            oppHP.setLayoutX(1130);
        else
            oppHP.setLayoutX(1125);
		oppHP.setLayoutY(205);
		if(opponent.getTotalManaCrystals()<10)
	            oppMana.setLayoutX(1012);
	    	else
	            oppMana.setLayoutX(1005);
		if(current.getTotalManaCrystals()==10)
		{
				currManaTotal.setLayoutX(43);
				currManaTotal.setStyle("-fx-stroke: black; -fx-stroke-width: 0.7");
				currManaTotal.setLayoutY(645);
		}
		else {
		    currManaTotal.setLayoutX(47);
			currManaTotal.setLayoutY(646);
			}
		if(current.getTotalManaCrystals()==10) {
			if(current.getCurrentManaCrystals()<10) {
		    	currManaCurr.setLayoutX(26);
			}
			currManaSlash.setLayoutX(40);
			currManaSlash.setLayoutY(643);
		}
    	else {
    		currManaSlash.setLayoutX(41);
			currManaSlash.setLayoutY(645);
    	}
		oppMana.setLayoutY(205);
		layout.getChildren().add(currHP);
		layout.getChildren().add(oppHP);
		layout.getChildren().add(currManaCurr);
		layout.getChildren().add(currManaTotal);
		layout.getChildren().add(currManaSlash);
		layout.getChildren().add(oppMana);
	}
	
	public void setHeroPowerGlow(boolean selected)
	{
		if(selected)
		{
			DropShadow glow = new DropShadow();
		    glow.setColor(Color.YELLOW);
		    currentHeroPower.setEffect(glow);
		}
		else
		{
			currentHeroPower.setEffect(null);
		}
	}
	
	public void createPopup(Exception exception) 
	{
        String message = exception.getMessage();
        ColorAdjust monochrome = new ColorAdjust();
        monochrome.setSaturation(-0.6);
        layout.setEffect(monochrome);
        popup = new ExceptionPopup(message, this);
        errorMsgLayout.setPrefWidth(900);
        errorMsgLayout.getChildren().clear();
        errorMsgLayout.getChildren().add(popup);
        if(exception instanceof FullHandException) {
        	String musicFile = "cardFire.mp3";
            Media sound = new Media(new File(musicFile).toURI().toString());
            gameMusic = new MediaPlayer(sound);
            gameMusic.play();
            Card c = ((FullHandException) exception).getBurned();
            ImageView flame = new ImageView (new Image("/view/fieldResources/flame.gif", 165,165,true,true));
            ViewCard newCard;
            if(c instanceof Minion)
            {
                newCard = new ViewCard((Minion) c);
            }
            else 
            {
                newCard = new ViewCard((Spell) c);
            }
            newCard.setHoverSize();
            newCard.setLayoutX(300);
            newCard.setLayoutY(230);
            popup.setLayoutX(-650);
            errorMsgLayout.setLayoutX(500);
            errorMsgLayout.setLayoutY(0);
            flame.setLayoutX(287);
            flame.setLayoutY(263);
            flame.setOpacity(0.6);
            errorMsgLayout.getChildren().add(newCard);
            errorMsgLayout.getChildren().add(flame);
       		}
        	else 
        	{
	        errorMsgLayout.setLayoutX(0);
	        errorMsgLayout.setLayoutY(0);
        	}
        superLayout.getChildren().addAll(errorMsgLayout);
	}

    public void removePopup() 
    {
    	superLayout.getChildren().removeAll(errorMsgLayout);	
    	layout.setEffect(null);
    }
    
    public void setHeroPowerUsed()
    {
    	currentHeroPower.setVisible(false);
    	heroPowerUsed = new ImageView(new Image("/view/fieldResources/heroPowers/heroPowerUsed.png",85,85,true, true));
    	heroPowerUsed.setLayoutX(173);
    	heroPowerUsed.setLayoutY(517);
    	heroPowerUsed.setEffect(new DropShadow());
    	layout.getChildren().add(heroPowerUsed);
    	redrawHand();
    }

    public void setMuteButton() 
    {
		MenuButton b = new MenuButton("");
		String buttonStyle = "-fx-background-size: 100% 100%; -fx-background-repeat: no-repeat;-fx-background-color: transparent; -fx-background-image: url('/view/menuResources/musicButton.png');";
		b.setStyle(buttonStyle);
		ImageView mute = new ImageView (new Image("/view/fieldResources/playing.png", 15,20,true,true));
        ImageView unmute = new ImageView (new Image("/view/fieldResources/mute.png", 15,20,true,true));
        mute.setDisable(true);
        unmute.setDisable(true);
		b.setLayoutX(15);
		b.setLayoutY(9);
		b.setPrefWidth(44);
        b.setPrefHeight(30);
        mute.setLayoutX(30);	
        mute.setLayoutY(23);
        unmute.setLayoutX(29);
        unmute.setLayoutY(23);
        unmute.setVisible(false);
        b.setOpacity(0.3);
	    mute.setOpacity(0.3);
	    unmute.setOpacity(0.3);
	    layout.getChildren().add(b);
	    layout.getChildren().add(mute);
	    layout.getChildren().add(unmute);
	    b.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

	        public void handle(MouseEvent event) {
	        	if(mute.isVisible()) {
					unmute.setVisible(true);
					mute.setVisible(false);
					gameMusic.pause();
				}
				else {
					mute.setVisible(true);
					unmute.setVisible(false);
					gameMusic.play();
				}
	        }
	   });
	    b.setOnMouseEntered(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent e) 
			{
				 b.setOpacity(1);
			     mute.setOpacity(1);
			     unmute.setOpacity(1);
			}
		});
	    b.setOnMouseExited(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent e) 
			{
				 b.setOpacity(0.3);
			     mute.setOpacity(0.3);
			     unmute.setOpacity(0.3);
			}
		});
	}
    
	public void setController(ViewListener controller) {
		this.controller = controller;
	}
	
	public ArrayList<ViewCard> getCurrHand() {
		return currHand;
	}

	public ArrayList<ViewCard> getOppHand() {
		return oppHand;
	}

	public ArrayList<ViewCard> getCurrField() {
		return currField;
	}

	public ArrayList<ViewCard> getOppField() {
		return oppField;
	}
}
