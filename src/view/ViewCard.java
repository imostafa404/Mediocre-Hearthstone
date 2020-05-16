package view;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.effect.Blend;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.cards.Card;
import model.cards.minions.Minion;
import model.cards.spells.Spell;

public class ViewCard extends SubScene{
	private AnchorPane layout;
	private ImageView card;
	private Text manaText;
	private Text healthText;
	private Text attackText;
	public String name;
	private boolean divine;
	private boolean sleeping;
	private boolean taunt;
	private int mana;
	private int attack = -1;
	private int health = -1;
	private ImageView sheild;
	private ImageView tauntSheild;
	private ImageView asleep;
	private boolean isSpell;
	private Card modelCard;
	
	public ViewCard(Minion minion)
	{
		super(new AnchorPane(), 137, 200);
		modelCard = minion;
		layout = (AnchorPane) this.getRoot();
		layout.setBackground(Background.EMPTY);
		name = minion.getName();
		mana = minion.getManaCost();
		attack = minion.getAttack();
		health = minion.getCurrentHP();
		divine = minion.isDivine();
		taunt = minion.isTaunt();
		sleeping = minion.isSleeping();
		setManaText(mana);
		setHealthText(health);
		setAttackText(attack);
	}
	
	public ViewCard(Spell spell)
	{
		super(new AnchorPane(), 145, 200);
		modelCard = spell;
		layout = (AnchorPane) this.getRoot();
		layout.setBackground(Background.EMPTY);
		mana = spell.getManaCost();
		name = spell.getName();
		if(name == "Shadow Word: Death")
			name = "Shadow Word Death";
		isSpell = true;
		setManaText(mana);
	}
	
	public void setFieldSize()
    {
        if(manaText != null)
        {
            layout.getChildren().remove(card);
            layout.getChildren().remove(manaText);
            layout.getChildren().remove(attackText);
            layout.getChildren().remove(healthText);
            layout.getChildren().remove(sheild);
        }
        if(tauntSheild != null)
        	layout.getChildren().remove(tauntSheild);
        card = new ImageView(new Image("/view/fieldResources/Cards/"+ name + ".png",120,170,true, true));
        card.setLayoutX(0);
        card.setLayoutY(0);
        layout.getChildren().add(card);
        setTextSize(manaText, mana, 30);
    	if(mana < 10) 
    	{
            manaText.setLayoutX(8);
            manaText.setLayoutY(34);
        }
        else 
        {
            manaText.setLayoutX(5);
            manaText.setLayoutY(33);
        }    
    	if(attack != -1)
        {
            setTextSize(attackText, attack, 30);
            if(attack < 10) {
                attackText.setLayoutX(12);
                attackText.setLayoutY(165);
            }
            else {
                attackText.setLayoutX(5);
                attackText.setLayoutY(162);
            }
            layout.getChildren().add(attackText);
        }
        if (health != -1)
        {
            setTextSize(healthText, health, 30);
            if(health < 10) {
                healthText.setLayoutX(99);
                healthText.setLayoutY(165);
            }
            else {
                healthText.setLayoutX(91);
                healthText.setLayoutY(162);
            }
            layout.getChildren().add(healthText);
        }
        layout.getChildren().add(manaText);
        if(taunt)
        {
        	tauntSheild = new ImageView(new Image("/view/fieldResources/cardVariables/tauntsheild.png",160,156,true, true));
        	giveTauntSheild(4,8);
        }
        if(divine)
        {
            sheild = new ImageView(new Image("/view/fieldResources/cardVariables/hearthsheild.png",160,500,true, true));
            giveSheild(-20,-2);
        } 
        if(sleeping)
        {
            asleep = new ImageView(new Image("/view/fieldResources/cardVariables/sleeping.png",50,70,true, true));
            giveAsleep(75,-2);
        } 
    }
	
	public void setGlow(boolean selected)
	{
		if(selected)
		{
			DropShadow glow = new DropShadow();
		    glow.setColor(Color.YELLOW);
		    card.setEffect(glow);
		}
		else
		{
			card.setEffect(null);
		}
      
	}
	
	public void setHandSize()
	{
		if(manaText != null)
        {
            layout.getChildren().remove(card);
            layout.getChildren().remove(manaText);
            layout.getChildren().remove(attackText);
            layout.getChildren().remove(healthText);
            layout.getChildren().remove(sheild);
        }
		if(tauntSheild != null)
			layout.getChildren().remove(tauntSheild);
		if(card != null && card.getEffect() != null)
		{
			card = new ImageView(new Image("/view/fieldResources/Cards/"+ name + ".png",120,155,true, true));
			this.setGlow(true);
		}
		else
			card = new ImageView(new Image("/view/fieldResources/Cards/"+ name + ".png",120,155,true, true));
        card.setLayoutX(0);
        card.setLayoutY(0);
        layout.getChildren().add(card);
        setTextSize(manaText, mana, 30);
        if(isSpell)
        {
        	if(mana < 10)
                manaText.setLayoutX(7);
            else
                manaText.setLayoutX(0);
            manaText.setLayoutY(24);
            layout.getChildren().add(manaText);
        }
        else
        {
        	if(mana < 10)
                manaText.setLayoutX(8);
            else
                manaText.setLayoutX(3);
            manaText.setLayoutY(32);
            if(attack != -1)
            {
                setTextSize(attackText, attack, 30);
                if(attack < 10)
                    attackText.setLayoutX(15);
                else
                    attackText.setLayoutX(11);
                attackText.setLayoutY(192);
                layout.getChildren().add(attackText);
            }
            if(health != -1)
            {
                setTextSize(healthText, health, 30);
                if(health < 10)
                    healthText.setLayoutX(116);
                else
                    healthText.setLayoutX(111);
                healthText.setLayoutY(192);
                layout.getChildren().add(healthText);
            }
            layout.getChildren().add(manaText);
            if(taunt)
            {
            	tauntSheild = new ImageView(new Image("/view/fieldResources/cardVariables/tauntsheild.png",145,145,true, true));
            	giveTauntSheild(6,8);            
           	}
            if(divine)
            {
                sheild = new ImageView(new Image("/view/fieldResources/cardVariables/hearthsheild.png",140,300,true, true));
                giveSheild(-14,0);
            }
        }
    }
	
	public void setHoverSize()
	{
		if(manaText != null)
        {
            layout.getChildren().remove(card);
            layout.getChildren().remove(manaText);
            layout.getChildren().remove(attackText);
            layout.getChildren().remove(healthText);
            layout.getChildren().remove(sheild);
        }
		if(tauntSheild != null)
			layout.getChildren().remove(tauntSheild);
		if(card != null && card.getEffect() != null)
		{
			card = new ImageView(new Image("/view/fieldResources/Cards/"+ name + ".png",200,200,true, true));
			this.setGlow(true);
		}
		else
			card = new ImageView(new Image("/view/fieldResources/Cards/"+ name + ".png",200,200,true, true));        
		card.setLayoutX(0);
        card.setLayoutY(0);
        layout.getChildren().add(card);
        setTextSize(manaText, mana, 33);
        if(isSpell)
        {
        	if(mana < 10)
                manaText.setLayoutX(10);
            else
                manaText.setLayoutX(3);
            manaText.setLayoutY(28);
            layout.getChildren().add(manaText);
        }
        else
        {	
	    	if(mana < 10) 
	    	{
	            manaText.setLayoutX(12);
	            manaText.setLayoutY(39);
	        }
	        else 
	        {
	            manaText.setLayoutX(5);
	            manaText.setLayoutY(38);
	        }    
	    	if(attack != -1)
	        {
	            setTextSize(attackText, attack, 33);
	            if(attack < 10) {
	                attackText.setLayoutX(14);
	                attackText.setLayoutY(193);
	            }
	            else {
	                attackText.setLayoutX(4);
	                attackText.setLayoutY(192);
	            }
	            layout.getChildren().add(attackText);
	        }
	        if (health != -1)
	        {
	            setTextSize(healthText, health, 33);
	            if(health < 10) {
	                healthText.setLayoutX(116);
	                healthText.setLayoutY(195);
	            }
	            else {
	                healthText.setLayoutX(105);
	                healthText.setLayoutY(192);
	            }
	            layout.getChildren().add(healthText);
	        }
	        layout.getChildren().add(manaText);
	        if(taunt)
	        {
	        	tauntSheild = new ImageView(new Image("/view/fieldResources/cardVariables/tauntsheild.png",185,185,true, true));
	            giveTauntSheild(7,11);
	       	}
	        if(divine)
	        {
	            sheild = new ImageView(new Image("/view/fieldResources/cardVariables/hearthsheild.png",180,500,true, true));
	            giveSheild(-20,-2);
	        }
	        
        }
	}
	
	public void updateText()
	{
		layout.getChildren().remove(healthText);
		layout.getChildren().remove(attackText);
		setHealthText(((Minion) getModelCard()).getCurrentHP());
		setAttackText(((Minion) getModelCard()).getAttack());
		layout.getChildren().add(healthText);
		layout.getChildren().add(attackText);
		if(divine && ((Minion) getModelCard()).isDivine())
		{
			layout.getChildren().remove(sheild);
		}
	}
	
	public void giveSheild(int x, int y)
	{
		sheild.setLayoutX(x);
		sheild.setLayoutY(y);
		layout.getChildren().add(sheild);
	}
	
	public void giveTauntSheild(int x, int y)
	{
		tauntSheild.setLayoutX(x);
		tauntSheild.setLayoutY(y);
		layout.getChildren().add(tauntSheild);
	}
	
	public void giveAsleep(int x, int y)
	{
		asleep.setLayoutX(x);
		asleep.setLayoutY(y);
		layout.getChildren().add(asleep);
	}
	
	public Text setText(int x)
	{
		Text t = new Text(x + "");
		t.setFill(Color.WHITE);
		t.setStyle("-fx-stroke: black; -fx-stroke-width: 1");
		return t;
	}
	
	public static void setTextSize(Text t, int value, int size)
	{
		try {
        	if(value<10)
        		t.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),size));
        	else
        		t.setFont(Font.loadFont(new FileInputStream("src/view/menuResources/menuFont.ttf"),size-4));
        } catch (FileNotFoundException e) {
        	if(value<10)
        		t.setFont(Font.font("Verdana",size));
        	else
        		t.setFont(Font.font(size-4));
        };
	}
	
	public boolean upToDate()
	{
		Minion minion = (Minion) getModelCard();
		if(name != minion.getName() || attack != minion.getAttack() || health !=  minion.getCurrentHP() ||  mana != minion.getManaCost())
			return false;
		if(sleeping != minion.isSleeping() || divine != minion.isDivine() || taunt != minion.isTaunt())
			return false;
		return true;
			
	}
	
	public void setManaText(int m) 
	{
        manaText = setText(m);
    }

	public void setHealthText(int m) 
	{
        healthText = setText(m);
    }
	
	public void setAttackText(int m) 
	{
        attackText = setText(m);
    }

	public Card getModelCard() {
		return modelCard;
	}
	
	public boolean isDivine() {
		return divine;
	}


}
