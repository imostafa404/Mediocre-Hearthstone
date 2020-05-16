package view;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class HelpMenu extends MenuSubscene {
    private AnchorPane layout;
    private String pageOnePath = "/view/menuResources/help/helpMenuPage1.png";
    private String pageTwoPath = "/view/menuResources/help/helpMenuPage2.png";

    public HelpMenu()
    {
        super();
        layout = (AnchorPane) this.getRoot();
        ImageView pageOne = new ImageView(new Image(pageOnePath,840,585,false, true));
        ImageView pageTwo = new ImageView(new Image(pageTwoPath,840,585,false, true));
        layout.getChildren().add(pageOne);
        layout.getChildren().add(pageTwo);
        pageTwo.setVisible(false);
        MenuButton pageTwoButton = new MenuButton("Page 2");
        pageTwoButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
			public void handle(MouseEvent arg0) 
			{
				pageOne.setVisible(false);
				pageTwo.setVisible(true);
			}	
        });
        MenuButton pageOneButton = new MenuButton("Page 1");
        pageOneButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
			public void handle(MouseEvent arg0) {
				pageOne.setVisible(true);
				pageTwo.setVisible(false);
			}	
        });
        pageOneButton.setPrefWidth(120);
        pageOneButton.setPrefHeight(52);
        pageTwoButton.setPrefWidth(120);
        pageTwoButton.setPrefHeight(52);
        pageOneButton.setLayoutX(500);
        pageOneButton.setLayoutY(515);
        pageTwoButton.setLayoutX(630);
        pageTwoButton.setLayoutY(515);
        layout.getChildren().add(pageOneButton);
        layout.getChildren().add(pageTwoButton);
    }
}