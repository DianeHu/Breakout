package game_dh224;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Screen extends Game{
	
	private static final int SIZE = 400;
	private static Font font = new Font(15);
	
	public static void victory(Group root, Bouncer myBouncer) {
		Rectangle backDrop = new Rectangle(SIZE, SIZE, Color.BLACK);
		Text victory = new Text(SIZE / 2 - 70, SIZE / 2, "Congrats, you win!");
		victory.setFont(font);
		victory.setFill(Color.WHITE);
		root.getChildren().addAll(backDrop, victory);
		myBouncer.keepMoving = 0;
	}
	
	public static void defeat(Group root, Bouncer myBouncer) {
		Rectangle backDrop = new Rectangle(SIZE, SIZE, Color.BLACK);
		Text defeat = new Text(SIZE / 2 - 70, SIZE / 2, "I'm sorry, you lost!");
		defeat.setFont(font);
		defeat.setFill(Color.WHITE);
		root.getChildren().addAll(backDrop, defeat);
		myBouncer.keepMoving = 0;
	}
	
	public static Text passLevel(int level) {
		Text levelDisplay = new Text(SIZE / 2 - 125, SIZE / 2, "You passed Level " + (level - 1) + "! Click to continue!");
		levelDisplay.setFont(font);
		levelDisplay.setFill(Color.WHITE);
		return levelDisplay;
	}
}