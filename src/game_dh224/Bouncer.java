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
	public static final int BOUNCER_MIN_SPEED = -200;
	public static final int BOUNCER_MAX_SPEED = 200;
	public static final double BOUNCER_SPEED = 300;
	public static final int SCREEN_SIZE = 400;
	
	private ImageView myView;
	private double xSpeed;
	private double ySpeed;
	private Random dice = new Random();
	public double xDirection;
	public double yDirection;
	private double previousXDir;
	private double previousYDir;
	public int keepMoving;
	public boolean stayOnPaddle;
	
	public Bouncer (Image image, int screenWidth, int screenHeight) {
		stayOnPaddle = true;
		keepMoving = 1;
		myView = new ImageView(image);
		myView.setFitWidth(BOUNCER_SIZE);
		myView.setFitHeight(BOUNCER_SIZE);
		myView.setX(screenWidth / 2 - 37);
		myView.setY(screenHeight - 37);
		xSpeed = 0;
		ySpeed = 0;
		xDirection = 1;
		yDirection = -1;
	}
	
	public Bouncer creationBouncer(double blockX, double blockY) {
		Bouncer newBouncer = new Bouncer(myView.getImage(), SCREEN_SIZE, SCREEN_SIZE);
		newBouncer.stayOnPaddle = false;
		newBouncer.setX(blockX);
		newBouncer.setY(blockY);
		newBouncer.xSpeed = getRandomInRange(BOUNCER_MIN_SPEED, BOUNCER_MAX_SPEED);
		newBouncer.ySpeed = getRandomInRange(BOUNCER_MIN_SPEED, BOUNCER_MAX_SPEED);
		return newBouncer;
	}
	
	public void move(double elapsedTime) {
		myView.setX(myView.getX() + keepMoving * xDirection * xSpeed * elapsedTime);
		myView.setY(myView.getY() + keepMoving * yDirection * ySpeed * elapsedTime);
	}
	
	public void slow(Bouncer b) {
		double constant = (Math.pow(100, 2) / (Math.pow(b.xSpeed, 2) + Math.pow(b.ySpeed, 2)));
		previousXDir = b.xSpeed;
		previousYDir = b.ySpeed;
		b.xSpeed /= 2;
		b.ySpeed /= 2;
		//b.xSpeed = (b.xSpeed < 0) ? (-1 * Math.pow(constant * Math.pow(b.xSpeed, 2), 0.5)) : Math.pow(constant * Math.pow(b.xSpeed, 2), 0.5);
		//b.ySpeed = (b.ySpeed < 0) ? (-1 * Math.pow(constant * Math.pow(b.ySpeed, 2), 0.5)) : Math.pow(constant * Math.pow(b.ySpeed, 2), 0.5);
	}
	
	public void accelerate(Bouncer b) {
		b.xSpeed *= 1.05;
		b.ySpeed *= 1.05;
	}
	
	public void returnSpeed(Bouncer b) {
		b.xSpeed = previousXDir;
		b.ySpeed = previousYDir;
	}
	
	public void launch(double x, double y) {
		yDirection = -1;
		double xDist = x;
		if(x < 0) 
			xDirection = -1;
		double yDist = y;
		double constant = (Math.pow(BOUNCER_SPEED, 2) / (Math.pow(xDist, 2) + Math.pow(yDist, 2)));
		xSpeed = Math.pow(constant * Math.pow(xDist, 2), 0.5);
		ySpeed = Math.pow(constant * Math.pow(yDist, 2), 0.5);
		
		System.out.println("My xDirection is" + xDirection + "My yDirection is" + yDirection);
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
			myBouncer.setX(paddle.getX() + paddle.getWidth()/2 - 10);
			myBouncer.setY(SCREEN_SIZE - 36);
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
	
	public void dropBouncer () {
		this.xSpeed = 0;
		this.ySpeed = 250;
		this.yDirection = 1;
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
