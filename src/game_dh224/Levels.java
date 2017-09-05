package game_dh224;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class Levels extends Game{
	
	public static final int SIZE = 400;
	public static final Paint BACKGROUND = Color.DARKSLATEGREY;
	public static int lvlCounter = 1;
	private Scene nextScene;
	

	public Scene newStage(Stage s) {
		return nextScene = setUpGame(SIZE, SIZE, BACKGROUND);
	}
	
	public void incrementLvl() {
		lvlCounter += 1;
	}
}
