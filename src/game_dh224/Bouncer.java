package game_dh224;

import java.util.Random;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class Bouncer {
	public static final int BOUNCER_SIZE = 15;
	public static final int BOUNCER_MIN_SPEED_LOWER = -200;
	public static final int BOUNCER_MIN_SPEED_UPPER = -150;
	public static final int BOUNCER_MAX_SPEED_LOWER = 150;
	public static final int BOUNCER_MAX_SPEED_UPPER = 200;
	
	private Random dice = new Random();
	private ImageView myView;
	private double xSpeed;
	private double ySpeed;
	public double xDirection;
	public double yDirection;
	public int keepMoving;
	public boolean stayOnPaddle;
	
	public Bouncer (Image image, int screenWidth, int screenHeight) {
		stayOnPaddle = true;
		keepMoving = 1;
		myView = new ImageView(image);
		myView.setFitWidth(BOUNCER_SIZE);
		myView.setFitHeight(BOUNCER_SIZE);
		myView.setX(screenHeight / 2);
		myView.setY(screenHeight - 37);
		xSpeed = 0;
		ySpeed = 0;
		xDirection = 1;
		yDirection = 1;
	}
	
	public void move(double elapsedTime) {
		myView.setX(myView.getX() + keepMoving * xDirection * xSpeed * elapsedTime);
		myView.setY(myView.getY() + keepMoving * yDirection * ySpeed * elapsedTime);
	}
	
	public void launch(double x, double y) {
		xSpeed = x;
		ySpeed = y;
	}
	
	public void bounceScreen (double screenWidth, double screenHeight) {
		if(myView.getX() < 0 || myView.getX() > screenWidth) {
			xDirection *= -1;
		}
		if(myView.getY() < 0) {
			yDirection *= -1;
		}
	}
	
	public void moveWithPaddle(Bouncer myBouncer, Rectangle paddle) {
		if(stayOnPaddle) {
			myBouncer.setX(paddle.getX() + 25);
		}
	}
	
	public void setX(double value) {
		myView.setX(value);
	}
	
	public void setY(double value) {
		myView.setY(value);
	}
	
	public void bouncePaddle (Bouncer myBouncer, Rectangle paddle) {
		if(myBouncer.getView().getBoundsInParent().intersects(paddle.getBoundsInParent())) {
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
	
	private int getRandomInRange(int min, int max) {
		return min + dice.nextInt(max - min) + 1;
	}
}
