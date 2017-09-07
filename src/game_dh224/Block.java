package game_dh224;

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
	}
	
	public void setLoc(double x, double y) {
		myView.setX(x);
		myView.setY(y);
	}
	
	public Node getView() {
		return myView;
	}
	
	public int getVal() {
		return 1;
	}
	
	public void bounceBlock (Bouncer myBouncer) {
		if(myBouncer.getView().getBoundsInParent().intersects(myView.getBoundsInParent())) {
			myBouncer.yDirection *= -1;
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









