package game_dh224;

import javafx.scene.image.Image;

public class creationBlock extends Block{

	public creationBlock(Image image, int screenWidth, int screenHeight) {
		super(image, screenWidth, screenHeight);
	}
	
	@Override
	public int getVal() {
		return 3;
	}
}
