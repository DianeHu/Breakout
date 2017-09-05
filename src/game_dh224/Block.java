package game_dh224;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Block {
	public static final int BLOCK_WIDTH = 75;
	public static final int BLOCK_HEIGHT = 50;
	
	private ImageView myView;
	
	public Block (Image image, int screenWidth, int screenHeight) {
		myView = new ImageView(image);
		myView.setFitWidth(BLOCK_WIDTH);
		myView.setFitHeight(BLOCK_HEIGHT);
		//still need to set x and y locations
	}
	
	public Node getView() {
		return myView;
	}
	
	public class StandardBlock extends Block{

		public StandardBlock(Image image, int screenWidth, int screenHeight) {
			super(image, screenWidth, screenHeight);
		}
	}
	
	public class speedPlusBlock extends Block{
		
		public speedPlusBlock(Image image, int screenWidth, int screenHeight) {
			super(image, screenWidth, screenHeight);
		}
	}
	
	public class speedMinusBlock extends Block{
		
		public speedMinusBlock(Image image, int screenWidth, int screenHeight) {
			super(image, screenWidth, screenHeight);
		}
	}
	
	public class blackHoleBlock extends Block{
		
		public blackHoleBlock(Image image, int screenWidth, int screenHeight) {
			super(image, screenWidth, screenHeight);
		}
	}
	
	public class creationBlock extends Block{
		
		public creationBlock(Image image, int screenWidth, int screenHeight) {
			super(image, screenWidth, screenHeight);
		}
	}
	
	public class instantClearBlock extends Block{
		
		public instantClearBlock(Image image, int screenWidth, int screenHeight) {
			super(image, screenWidth, screenHeight);
		}
	}
}









