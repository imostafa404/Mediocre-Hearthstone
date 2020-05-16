package view;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.swing.GroupLayout.Alignment;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Credits extends MenuSubscene{
	private AnchorPane layout;
    private ImageView help;
    private final String backgroundPath = "/view/menuResources/subsceneBackground.png";

    public Credits()
    {
        super();
        layout = (AnchorPane) this.getRoot();
        ImageView badrr = createBackground();
        ImageView mostafaa = createBackground();
        ImageView yasserr = createBackground();
        Text badr = createCreditNames("Badr\nAlSayed");
        Text mostafa = createCreditNames("Mostafa\nElSheikh");
        Text yasser = createCreditNames("Mohammed\nYasser");
        badrr.setLayoutX(65);
        badrr.setLayoutY(240);
        mostafaa.setLayoutX(300);
        mostafaa.setLayoutY(240);
        yasserr.setLayoutX(535);
        yasserr.setLayoutY(240);
        badr.setLayoutX(125);
        badr.setLayoutY(300);
        mostafa.setLayoutX(352);
        mostafa.setLayoutY(300);
        yasser.setLayoutX(570);
        yasser.setLayoutY(300);
		layout.getChildren().add(badrr);
		layout.getChildren().add(mostafaa);
		layout.getChildren().add(yasserr);
		layout.getChildren().add(badr);
		layout.getChildren().add(mostafa);
		layout.getChildren().add(yasser);

    }
    
    public Text createCreditNames(String Dev)
	{
		Text t = new Text(Dev);
		t.setTextAlignment(TextAlignment.CENTER);
		t.setFill(Color.ANTIQUEWHITE);
		t.setStroke(Color.BLACK);
		try {
			t.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),27));
		} catch (FileNotFoundException e) {
			t.setFont(Font.font("Verdana",23));
		}
		return t;
	}
    
    public ImageView createBackground()
    {
    	ImageView background = new ImageView(new Image("/view/menuResources/HeroSelection/hearthbanner.png", 220, 165, true, true));
    	background.setOnMouseEntered(new EventHandler<MouseEvent>()
    	{

			public void handle(MouseEvent arg0) {
				background.setEffect(new DropShadow());
			}
    				
    	});
    	background.setOnMouseExited(new EventHandler<MouseEvent>()
    	{
			public void handle(MouseEvent arg0) {
				background.setEffect(null);
			}
    				
    	});
    	return background;
    }
}
