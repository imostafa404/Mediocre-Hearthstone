package view;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class MenuSubscene extends SubScene{
	AnchorPane layout;
    TranslateTransition transition = new TranslateTransition();
	private final String backgroundPath = "/view/menuResources/subsceneBackground.png";
	private SubsceneListener listener;

	
	public MenuSubscene() {
		super(new AnchorPane(), 1200, 700);
		prefHeight(470);
		prefWidth(720);
		setLayoutX(177);
		setLayoutY(-608);
		BackgroundImage background = new BackgroundImage(new Image(backgroundPath, 840,585,false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
		layout = (AnchorPane) this.getRoot();
		layout.setBackground(new Background(background));
		addBack();
}
	
	public MenuButton addBack()
	{
		MenuButton back = new MenuButton("Back");
		back.setStyle("-fx-background-size: 100% 100%; -fx-background-repeat: no-repeat; -fx-background-color: transparent; -fx-background-image: url('/view/menuResources/hearthback.png')");
		try {
			back.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),23));
		} catch (FileNotFoundException e) {
			back.setFont(Font.font("Verdana",23));
		}
		back.setPrefWidth(120);
		back.setPrefHeight(52);
		back.setLayoutX(20);
		back.setLayoutY(515);
		back.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent e) 
            {
                transition.setToY(-10);
                transition.setDuration(Duration.millis(800));
                transition.play();
            }
        });
		layout.getChildren().add(back);
		return back;
	}
	
	public void moveSubscene() {
        transition.setDuration(Duration.millis(1100));
        transition.setNode(this);
        transition.setToY(668);
        transition.play();

    }

	public void setListener(SubsceneListener listener) {
		this.listener = listener;
	}

	public SubsceneListener getListener() {
		return listener;
	}


}
