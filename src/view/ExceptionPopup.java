package view;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.event.EventHandler;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ExceptionPopup extends SubScene{
	private AnchorPane layout;
	private String backPath = "/view/fieldResources/popup.png";
	private Text errorMessage;
	private BattleScene battleScene;
	
	public ExceptionPopup(String message, BattleScene scene) {
        super(new AnchorPane(), 1200,700);
        layout = (AnchorPane) this.getRoot();
        battleScene = scene;
        layout.setBackground(Background.EMPTY);
        ImageView popup = new ImageView(new Image(backPath, 543,165,false, true));
        popup.setLayoutX(322);
        popup.setLayoutY(250);
        errorMessage = new Text(message);
        Color c = Color.web("#19343D");
        errorMessage.setFill(c);

        errorMessage.setLayoutX(350);
        if (message.length() < 31)
        	errorMessage.setLayoutY(335);
        else
        	errorMessage.setLayoutY(319);
        errorMessage.setWrappingWidth(500);
        errorMessage.setTextAlignment(TextAlignment.CENTER);
        try {
            errorMessage.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),30));
        } catch (FileNotFoundException e1) {
            errorMessage.setFont(Font.font("Verdana",30));
        }
        MenuButton ok = new MenuButton("I Understand");
        ok.setPrefWidth(200);
        ok.setPrefHeight(70);
        layout.getChildren().add(popup);
        layout.getChildren().add(errorMessage);
        layout.getChildren().add(ok);
        ok.setLayoutX(487);
        ok.setLayoutY(380);
        ok.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent e) 
            {
                battleScene.removePopup();
            }
        });
    }
}