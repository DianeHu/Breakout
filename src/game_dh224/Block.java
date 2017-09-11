package game_dh224;

/* Written by Diane Hu, with advice from UTA Delia. This class describes a Block object,
 * which at its core is an ImageView. Block class depends on JavaFX libraries, allowing users
 * to define blocks in a BreakOut style game easily with extension subclasses.*/

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Block {
	public static final int BLOCK_WIDTH = 35;
	public static final int BLOCK_HEIGHT = 20;
	
	private ImageView myView;
	
	/* The Block constructor takes an image and sets it to given width and height parameters. */
	public Block (Image image) {
		myView = new ImageView(image);
		myView.setFitWidth(BLOCK_WIDTH);
		myView.setFitHeight(BLOCK_HEIGHT);
	}
	
	/* setLoc allows the user to set the x and y coordinate positions of the block. */
	public void setLoc(double x, double y) {
		myView.setX(x);
		myView.setY(y);
	}
	
	/* getView returns the node of the block */
	public Node getView() {
		return myView;
	}
	
	/* getVal returns the default "worth" of the block when hit, i.e., how many points
	 * the user earns by hitting a block. */
	public int getVal() {
		return 1;
	}
	
	/* getX returns the x position of the block. */
	public double getX() {
		return myView.getX();
	}
	
	/* getY returns the y position of the block. */
	public double getY() {
		return myView.getY();
	}
	
	/* bounceBlock bounces a given bouncer of the block, depending on whether the bouncer
	 * hit the top or bottom, or left and right sides of the block. */
	public void bounceBlock (Bouncer myBouncer) {
		if(isCollidingTopBottom(myBouncer)) {
			myBouncer.toggleYDir(-1);
		} else if (isCollidingOnSides(myBouncer)) {
			myBouncer.toggleXDir(-1);
		}
	}
	
	/* isCollidingTopBottom is a boolean returning whether or not a given bouncer intersects
	 * the block on the top or bottom sides. */
	public boolean isCollidingTopBottom(Bouncer myBouncer) {
		return (myBouncer.getX() + myBouncer.getSize()/2) > myView.getX() && myBouncer.getX() < (myView.getX() + BLOCK_WIDTH) && 
				myBouncer.getView().getBoundsInParent().intersects(myView.getBoundsInParent());
	}
	
	/* isCollidingOnSides is a boolean returning whether or not a given bouncer intersects
	 * the block on the left and right sides. */
	public boolean isCollidingOnSides(Bouncer myBouncer) {
		return (myBouncer.getY() + myBouncer.getSize()/2) > myView.getY() && myBouncer.getY() < (myView.getY() + BLOCK_HEIGHT) && 
				myBouncer.getView().getBoundsInParent().intersects(myView.getBoundsInParent());
	}
}