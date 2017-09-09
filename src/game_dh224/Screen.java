package game_dh224;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Screen {
	
	private StackPane stack;
	
	public Screen(Color background, int screenWidth, int screenHeight) {
		Rectangle colorDrop = new Rectangle(screenWidth, screenHeight);
		colorDrop.setFill(background);
		stack.getChildren().add(colorDrop);
	}
	
	public StackPane loseScreen(int screenWidth, int screenHeight) {
		Rectangle lose = new Rectangle(0, 0, screenWidth, screenHeight);
		lose.setFill(Color.BLUEVIOLET);
		Text defeat = new Text(screenWidth / 3 - 100, screenHeight / 3, "I'm sorry, you lost!");
		Font font = new Font(25);
		defeat.setFont(font);
		defeat.setFill(Color.WHITE);
		stack.getChildren().addAll(defeat, lose);
		return stack;
	}
}