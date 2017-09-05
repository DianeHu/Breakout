package game_dh224;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Bouncer {
	public static final int BOUNCER_BASE_SPEED = 50;
	public static final int BOUNCER_SIZE = 15;
	
	private ImageView myView;
	private Point2D myVelocity;
	
	public Bouncer (Image image, int screenWidth, int screenHeight) {
		myView = new ImageView(image);
		myView.setFitWidth(BOUNCER_SIZE);
		myView.setFitHeight(BOUNCER_SIZE);
		myView.setX(screenWidth / 2);
		myView.setY(0);
		myVelocity = new Point2D(0, BOUNCER_BASE_SPEED);
	}
	
	public void move(double elapsedTime) {
		myView.setX(myView.getX() + myVelocity.getX() * elapsedTime);
		myView.setY(myView.getY() + myVelocity.getY() * elapsedTime);
	}
	
	public void bounceScreen (double screenWidth, double screenHeight) {
		if(myView.getX() < 0 || myView.getX() > myView.getBoundsInLocal().getWidth()) {
			myVelocity = new Point2D(-myVelocity.getX(), myVelocity.getY());
		}
		if(myView.getY() < 0 || myView.getY() > screenHeight - myView.getBoundsInLocal().getHeight()) {
			myVelocity = new Point2D(myVelocity.getX(), -myVelocity.getY());
		}
	}
	
	public void bouncePaddle (Bouncer b, Rectangle paddle) {
		if(b.getView().getBoundsInParent().intersects(paddle.getBoundsInParent())) {
			b.myVelocity = new Point2D(-b.myVelocity.getY(), -b.myVelocity.getX());
		}
	}
	
	public void bounceBlock (Bouncer b, Block block) {
		if(b.getView().getBoundsInParent().intersects(block.getView().getBoundsInParent())) {
			b.myVelocity = new Point2D(-b.myVelocity.getY(), -b.myVelocity.getX());
		}
	}
	
	public Node getView() {
		return myView;
	}
	
	public double getX() {
		return myView.getX();
	}
	
	public double getY() {
		return myView.getY();
	}
}
