package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import controller.Controller;
import javafx.event.EventHandler;
import javafx.scene.SubScene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.heroes.Hero;

public class VictoryScreen extends SubScene{
	private AnchorPane layout;
	private MenuButton button;
	private ViewListener controller;
	
	public VictoryScreen(Hero winningHero, ViewListener controller2) {
        super(new AnchorPane(), 1200,700);
        layout = (AnchorPane) this.getRoot();
        ImageView victoryScreen = null;
        switch(winningHero.getName())
		{
			case "Jaina Proudmoore": victoryScreen = new ImageView(new Image("/view/fieldResources/VictoryScreens/mageVictoryScreen.png" ,800,800,true, true)); break;
			case "Rexxar": victoryScreen = new ImageView(new Image("/view/fieldResources/VictoryScreens/hunterVictoryScreen.png" ,800,800,true, true)); break;
			case "Uther Lightbringer": victoryScreen = new ImageView(new Image("/view/fieldResources/VictoryScreens/paladinVictoryScreen.png" ,800,800,true, true)); break;
			case "Anduin Wrynn":victoryScreen = new ImageView(new Image("/view/fieldResources/VictoryScreens/priestVictoryScreen.png" ,800,800,true, true)); break;
			case "Gul'dan":victoryScreen = new ImageView(new Image("/view/fieldResources/VictoryScreens/warlockVictoryScreen.png" ,800,800,true, true)); break;
		}
        DropShadow glow = new DropShadow();
	    glow.setColor(Color.GOLD);
	    victoryScreen.setEffect(glow);
		victoryScreen.setLayoutX(180);
		victoryScreen.setLayoutY(-105);
        this.controller = controller2;
        layout.setBackground(Background.EMPTY);
        button = new MenuButton("Back To Menu");
        button.setPrefWidth(230);
        button.setPrefHeight(70);
        layout.getChildren().add(victoryScreen);
        layout.getChildren().add(button);
        button.setLayoutX(479);
        button.setLayoutY(530);
        button.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent e) 
            {
                try {
                	((Controller) controller).getView().getView().close();
					((Controller) controller).start(new Stage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }
        });
    }
}
