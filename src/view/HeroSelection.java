package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import exceptions.FullHandException;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class HeroSelection extends MenuSubscene{
	private AnchorPane layout;
	private ImageView selected;
	private String selectedHero = "Mage";
	private String player1;
	private String player2;
	private Text title;
	private String mage = "/view/menuResources/HeroSelection/HearthMage.png";
	private String paladin = "/view/menuResources/HeroSelection/HearthPaladin.png";
	private String priest = "/view/menuResources/HeroSelection/HearthPriest.png";
	private String hunter = "/view/menuResources/HeroSelection/HearthHunter.png";
	private String warlock = "/view/menuResources/HeroSelection/HearthWarlock.png";
	private MediaPlayer mediaPlayer;
	
	public HeroSelection()
	{
		super();
		layout = (AnchorPane) this.getRoot();
		selected = new ImageView(mage);
		updateAvatar(selected);
		addButton("Confirm");
		addHeros();
		title = new Text();
		updateTitle("Player 1, Choose Your Hero!");
		layout.getChildren().add(title);
	}
	
	public void updateTitle(String head)
	{
		title.setText(head);
		try {
			title.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),30));
		} catch (FileNotFoundException e) {
			title.setFont(Font.font("Verdana",30));
		}
		title.setLayoutX(45*1.2);
		title.setLayoutY(73*1.17);
	}
	
	public void updateAvatar(ImageView selected) 
	{
		selected.setPreserveRatio(true);
		selected.setFitHeight(450);
		selected.setLayoutX(390*1.2);
		selected.setLayoutY(60*1.17);
		layout.getChildren().add(selected);
	}
	
	public void addHeros() 
	{
		Button h = createHeroButton("Mage");
		h.setLayoutX(40*1.2);
		h.setLayoutY(110*1.17);
		Button m = createHeroButton("Hunter");
		m.setLayoutX(40*1.2);
		m.setLayoutY(210*1.17);
		Button p = createHeroButton("Priest");
		p.setLayoutX(40*1.2);
		p.setLayoutY(310*1.17);
		Button w = createHeroButton("Warlock");
		w.setLayoutX(200*1.2);
		w.setLayoutY(160*1.17);
		Button pa = createHeroButton("Paladin");
		pa.setLayoutX(200*1.2);
		pa.setLayoutY(260*1.17);
		layout.getChildren().add(h);
		layout.getChildren().add(m);
		layout.getChildren().add(p);
		layout.getChildren().add(w);
		layout.getChildren().add(pa);
	}
	
	public Button createHeroButton(String hero)
    {
        MenuButton b = new MenuButton(hero);
        b.setStyle("-fx-text-stroke: black;-fx-background-size: 100% 100%; -fx-background-repeat: no-repeat;-fx-background-color: transparent; -fx-background-image: url('/view/menuResources/HeroSelection/hearthbanner.png')");
        b.setTextFill(Color.WHITE);
        try {
            b.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),23));
        } catch (FileNotFoundException e) {
            b.setFont(Font.font("Verdana",23));
        }
        b.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent e) 
            {
                selected.setEffect(null);
                selected = new ImageView("/view/menuResources/HeroSelection/Hearth" + hero+ ".png");
                selected.setEffect(new DropShadow());
                updateAvatar(selected);
                selectedHero = hero;
                String musicFile = hero+".mp3";
                Media sound = new Media(new File(musicFile).toURI().toString());
                mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.play();
            }
        });
        b.setPrefWidth(178);
        b.setPrefHeight(117);
        return b;
    }
	
	public void addButton(String command)
	{
		final MenuButton button = new MenuButton(command);
		button.setStyle("-fx-background-size: 100% 100%; -fx-background-repeat: no-repeat; -fx-background-color: transparent; -fx-background-image: url('/view/menuResources/hearthback.png')");
		try {
			button.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),23));
		} catch (FileNotFoundException e) {
			button.setFont(Font.font("Verdana",23));
		}
		button.setPrefWidth(135*1.2);
		button.setPrefHeight(65*1.17);
		button.setLayoutX(277*1.2);
		button.setLayoutY(460*1.17);
		button.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent e) 
            {
               if(button.getText().equals("Confirm"))
               {
            	   player1 = selectedHero;
            	   addButton("Play!");
            	   layout.getChildren().remove(button);
            	   updateTitle("Player 2, Choose Your Hero!");
            	   selectedHero = "Mage";
            	   selected = new ImageView(mage); 
            	   updateAvatar(selected);
            	   MenuButton backNew = addBack();
            	   backNew.setOnMouseClicked(new EventHandler<MouseEvent>()
                   {
                       public void handle(MouseEvent e) 
                       {
                    	   updateTitle("Player 1, Choose Your Hero!");
                    	   layout.getChildren().remove(button);
                    	   addButton("Confirm");
                    	   addBack();
                    	   selectedHero = "Mage";
                    	   selected = new ImageView(mage); 
                    	   updateAvatar(selected);
                       }
                   });
               }
               else
               {
            	   player2 = selectedHero;
            	   getListener().setPlayer1(player1);
            	   getListener().setPlayer2(player2);
            	   try {
					getListener().onGameStart();
				} catch (FullHandException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
               }
            }
        });
		layout.getChildren().add(button);
	}

	public String getPlayer1() {
		return player1;
	}

	public String getPlayer2() {
		return player2;
	}

}
