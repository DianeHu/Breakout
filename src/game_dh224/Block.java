package game_dh224;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Block {
	public static final int BLOCK_WIDTH = 30;
	public static final int BLOCK_HEIGHT = 20;
	
	private ImageView myView;
	
	public Block (Image image) {
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
	
	public double getX() {
		return myView.getX();
	}
	
	public double getY() {
		return myView.getY();
	}
	
	public void bounceBlock (Bouncer myBouncer) {
		if(myBouncer.getX() > myView.getX() && myBouncer.getX() < (myView.getX() + BLOCK_WIDTH) && 
				myBouncer.getView().getBoundsInParent().intersects(myView.getBoundsInParent())) {
			myBouncer.yDirection *= -1;
		} else if (myBouncer.getY() > myView.getY() && myBouncer.getY() < (myView.getY() + BLOCK_HEIGHT) && 
				myBouncer.getView().getBoundsInParent().intersects(myView.getBoundsInParent())) {
			myBouncer.xDirection *= -1;
		}
		/*if(myBouncer.getView().getBoundsInParent().intersects(myView.getBoundsInParent())) {
			myBouncer.yDirection *= -1;
		}*/
	}
	
	public class creationBlock extends Block{
		
		public creationBlock(Image image, int screenWidth, int screenHeight) {
			super(image);
		}
	}
	
	public class instantClearBlock extends Block{
		
		public instantClearBlock(Image image, int screenWidth, int screenHeight) {
			super(image);
		}
	}

}









