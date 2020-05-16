package view;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import exceptions.FullHandException;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


public class HearthstoneView implements SubsceneListener{
	private AnchorPane layout;
	private Scene scene;
	private Stage view;
	private ArrayList<MenuButton> menuButtons;
	private HeroSelection heroSelection;
	private String player1;
	private String player2;
	private ViewListener controller;
	private MediaPlayer mediaPlayer;
	private Credits credits;
	private HelpMenu helpMenu;
	private MediaPlayer announcer;
	
	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	public void setPlayer2(String player2) {
		this.player2 = player2;
	}

	public HearthstoneView()
	{
		layout = new AnchorPane();
		scene = new Scene(layout,1200,700);
		Image cursor = new Image("/view/hearthleagueabominationcursor.png");
	    scene.setCursor(new ImageCursor(cursor));
		menuButtons = new ArrayList<MenuButton>();
		view = new Stage();
		view.getIcons().add(new Image("/view/menuResources/hearthstonelogo.png"));
		view.setResizable(false);
		view.setScene(scene);
		addLogo("/view/menuResources/hearthlogo.png", 270, -70);
		createPlayButton();
		createHelpButton();
		createCreditsButton();
		createExitButton();
		createBackground();
		createSubscene();
		heroSelection.setListener(this);
		String musicFile = "titleMusic.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        setMuteButton();
        announcerStart();
	}
	public void announcerStart() {
		int random = (int)(Math.random() * 5 + 1);
		String musicFile = "welcome"+random+".mp3";
        Media clip = new Media(new File(musicFile).toURI().toString());
        announcer = new MediaPlayer(clip);
        announcer.play();
	}

	public void onGameStart() throws FullHandException, IOException, CloneNotSupportedException
	{
		controller.onGameStart( player1,  player2);
	}
	
	public void createSubscene()
    {
        heroSelection = new HeroSelection();
        layout.getChildren().add(heroSelection);
        helpMenu = new HelpMenu();
        layout.getChildren().add(helpMenu);
        credits = new Credits();
        layout.getChildren().add(credits);
    }
	
	public void addLogo(String url, int x, int y) 
    {
        ImageView logo = new ImageView (new Image(url, 700,450,true,true));
        logo.setEffect(new Glow(0.2));
        logo.setLayoutX(x);
        logo.setLayoutY(y);
        logo.prefHeight(500);
        logo.prefWidth(500);
        layout.getChildren().add(logo);
    }
	
	public void createMenuButtons(MenuButton b)
    {
        b.setLayoutX(485);
        b.setLayoutY(240 + menuButtons.size() * 90);
        menuButtons.add(b);
        layout.getChildren().add(b);
    }
	
	public void createPlayButton()
	{
		MenuButton b = new MenuButton("Play");
		b.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) 
			{
				heroSelection.moveSubscene();
			}
		});
		createMenuButtons(b);		
	}
	
	public void createHelpButton()
	{
		MenuButton b = new MenuButton("Help");
        b.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent arg0) 
            {
                helpMenu.moveSubscene();
            }
        });
        createMenuButtons(b);
	}
	
	public void createCreditsButton()
    {
        MenuButton b = new MenuButton("Credits");
        b.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent arg0) 
            {
                credits.moveSubscene();
            }
        });
        createMenuButtons(b);

    }
	
	public void createExitButton()
	{
		MenuButton b = new MenuButton("Exit");
		createMenuButtons(b);
	}
	
	public void createBackground()
	{
		Image image = new Image("view/menuResources/menuBackground.jpg",1200,700,false,true);
		BackgroundImage background = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
		layout.setBackground(new Background(background));
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
					mediaPlayer.pause();
				}
				else {
					mute.setVisible(true);
					unmute.setVisible(false);
					mediaPlayer.play();
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
	
	public Stage getView() {
		return view;
	}

	public String getPlayer2() {
		return player2;
	}

	public String getPlayer1() {
		return player1;
	}

	public ViewListener getController() {
		return controller;
	}

	public void setController(ViewListener controller) {
		this.controller = controller;
	}
	
	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
}
