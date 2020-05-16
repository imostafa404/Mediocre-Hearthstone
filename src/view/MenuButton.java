package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MenuButton extends Button{
	private final String fontPath =  "src/view/menuResources/menuFont.ttf";
	private final String buttonPressedStyle = "-fx-background-color: transparent; -fx-background-image: url('/view/menuResources/buttonPressed.png');";
	private final String buttonStyle = "-fx-background-size: 100% 100%; -fx-background-repeat: no-repeat;-fx-background-color: transparent; -fx-background-image: url('/view/menuResources/hearthback.png');";
	private final String exitButton = "-fx-background-color: transparent; -fx-background-image: url('/view/menuResources/exitHovered.png');";
	private final String exitPressed = "-fx-background-color: transparent; -fx-background-image: url('/view/menuResources/exitPressed.png');";
	private MediaPlayer mediaPlayer;

	public MenuButton(String Label)
	{
		setText(Label);
		setButtonFont();
		setPrefWidth(230);
		setPrefHeight(76);
		setStyle(buttonStyle);
		initializeButtonListeners();
	}
	
	public void setButtonFont()
	{
		try {
			setFont(Font.loadFont(new FileInputStream(fontPath),23));
		} catch (FileNotFoundException e) {
			setFont(Font.font("Verdana",23));
		}
	}
	
	public void setButtonPressedStyle()
	{
		setEffect(new InnerShadow());
	}
	
	public void setButtonReleasedStyle()
	{
		setEffect(null);
	}
	
	public void initializeButtonListeners()
	{
		setOnMousePressed(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent e) 
			{
				if(e.getButton().equals(MouseButton.PRIMARY))
                {
                    setButtonPressedStyle();
                    if(getText().equals("Exit") || getText().equals("Play!") ||  getText().equals("Help") || getText().equals("Credits") || getText().equals("Play"))
                    {
	                    String musicFile = "clickButton.mp3";
	                    Media sound = new Media(new File(musicFile).toURI().toString());
	                    mediaPlayer = new MediaPlayer(sound);
	                    mediaPlayer.play();
                    }
                    else if(getText().equals("Back"))
                    {
                        String musicFile = "backButton.mp3";
                        Media sound = new Media(new File(musicFile).toURI().toString());
                        mediaPlayer = new MediaPlayer(sound);
                        mediaPlayer.play();
                    }
                    else if(getText().equals("End Turn!")) 
                    {
                        String musicFile = "endTurn.mp3";
                        Media sound = new Media(new File(musicFile).toURI().toString());
                        mediaPlayer = new MediaPlayer(sound);
                        mediaPlayer.play();
                    }
                    else 
                    {
	                    String musicFile = "normalButton.mp3";
	                    Media sound = new Media(new File(musicFile).toURI().toString());
	                    mediaPlayer = new MediaPlayer(sound);
	                    mediaPlayer.play();
                    }
            		mediaPlayer.setVolume(0.1);
                }
			}
		});
		
		setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent e) 
			{
				if(e.getButton().equals(MouseButton.PRIMARY))
				{
					if(getText().equals("Exit"))
					{
						Stage stage = (Stage) getScene().getWindow();
	                    stage.close();
					}
					else
						setButtonReleasedStyle();
				}
			}
		});
		
		setOnMouseEntered(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent e) 
			{
				setEffect(new DropShadow());
				String musicFile = "hoverButton.mp3";
		        Media media = new Media(new File(musicFile).toURI().toString());
		        mediaPlayer = new MediaPlayer(media);
		        mediaPlayer.setVolume(0.02);
		        mediaPlayer.play();
			}
		});
		
		setOnMouseExited(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent e) 
			{
				setEffect(null);
			}
		});
	}
}
