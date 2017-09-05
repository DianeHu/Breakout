package game_dh224;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class Bouncer {
	public static final int BOUNCER_SIZE = 15;
	
	private ImageView myView;
	private double xSpeed;
	private double ySpeed;
	private double xDirection;
	private double yDirection;
	public int keepMoving;
	
	public Bouncer (Image image, int screenWidth, int screenHeight) {
		keepMoving = 1;
		myView = new ImageView(image);
		myView.setFitWidth(BOUNCER_SIZE);
		myView.setFitHeight(BOUNCER_SIZE);
		myView.setX(screenWidth / 2);
		myView.setY(screenWidth / 2);
		xSpeed = 150;
		ySpeed = -75;
		xDirection = 1;
		yDirection = 1;
	}
	
	public void move(double elapsedTime) {
		myView.setX(myView.getX() + keepMoving * xDirection * xSpeed * elapsedTime);
		myView.setY(myView.getY() + keepMoving * yDirection * ySpeed * elapsedTime);
	}
	
	public void bounceScreen (double screenWidth, double screenHeight) {
		if(myView.getX() < 0 || myView.getX() > screenWidth) {
			xDirection *= -1;
		}
		if(myView.getY() < 0) {
			yDirection *= -1;
		}
	}
	
	public void setX(double value) {
		myView.setX(value);
	}
	
	public void setY(double value) {
		myView.setY(value);
	}
	
	public void bouncePaddle (Bouncer b, Rectangle paddle) {
		if(b.getView().getBoundsInParent().intersects(paddle.getBoundsInParent())) {
			yDirection *= -1;
		}
	}
	
	public void bounceBlock (Bouncer b, Block block) {
		if(b.getView().getBoundsInParent().intersects(block.getView().getBoundsInParent())) {
			yDirection *= -1;
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
