package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.heroes.Hero;

public class FatiguePopup extends SubScene{
	private AnchorPane layout;
	private Text fatigueDamage;
	private ImageView background;
	
	public FatiguePopup(Hero hero, int damage)
	{
		super(new AnchorPane(),1200,700);
		layout = (AnchorPane) this.getRoot();
        layout.setBackground(Background.EMPTY);
		fatigueDamage = new Text(Integer.toString(-damage));
		fatigueDamage.setFill(Color.DARKRED);
		fatigueDamage.setStroke(Color.BLACK);
		 try {
			 	fatigueDamage.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),40));
	        } catch (FileNotFoundException e1) {
	        	fatigueDamage.setFont(Font.font("Verdana",30));
	        }
		background = new ImageView(new Image("/view/fieldResources/fatigue.png",158,247, true, true));
		background.setLayoutX(522);
		background.setLayoutY(240);
		layout.getChildren().add(background);
		if(Integer.parseInt(fatigueDamage.getText())==0) {
		fatigueDamage.setLayoutX(592);
		}
		else {
			fatigueDamage.setLayoutX(582);
		}
		fatigueDamage.setLayoutY(425);

		layout.getChildren().add(fatigueDamage);
	}
	

}
