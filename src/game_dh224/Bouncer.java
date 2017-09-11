package game_dh224;

/* Written by Diane Hu, with help from UTA Delia and UTA Jordan. This Bouncer
 * class instantiates the ball object of a BreakOut game, which here is an ImageView at
 * its core. Class Bouncer depends on JavaFX and java.util libraries, and allows a user
 * to define a Bouncer outside of simply a circle object, taking images instead. */

import java.util.Random;


import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class Bouncer {
	public static final int BOUNCER_MIN_XSPEED = -200;
	public static final int BOUNCER_MAX_XSPEED = 200;
	public static final int BOUNCER_UPPER_YSPEED = -150;
	public static final int BOUNCER_LOWER_YSPEED = -200;
	public static final double BOUNCER_SPEED = 300;
	public static final int SCREEN_SIZE = 400;
	public static final int INITIAL_PADDLE_X = SCREEN_SIZE / 2 - 40;
	public static final int INITIAL_PADDLE_Y = SCREEN_SIZE - 20;
	public static final int BLOCK_WIDTH = 35;
	public static final int ORIGINAL_PADDLE_LENGTH = 70;
	public static final int ORIGINAL_PADDLE_HEIGHT = 10;
	public static final int BOUNCER_OFFSET = 3;
	
	private ImageView myView;
	private double xSpeed;
	private double ySpeed;
	private double returnXSpeed;
	private double returnYSpeed;
	private double xDirection;
	private double yDirection;
	private Random dice = new Random();
	private int bouncerMaxSpeed = 500;
	private int bouncerSize = 15;
	private int keepMoving;
	private boolean stayOnPaddle;
	
	/* The bouncer constructor takes an image, screenwidth, and screenheight,
	 * and sets a number of initial parameters. The constructor creates the
	 * bouncer object holding these initial default settings. */
	public Bouncer (Image image, int screenWidth, int screenHeight) {
		stayOnPaddle = true;
		keepMoving = 1;
		myView = new ImageView(image);
		myView.setFitWidth(bouncerSize);
		myView.setFitHeight(bouncerSize);
		myView.setX(INITIAL_PADDLE_X);
		myView.setY(screenHeight - 2*ORIGINAL_PADDLE_HEIGHT - bouncerSize - BOUNCER_OFFSET);
		xSpeed = 0;
		ySpeed = 0;
		xDirection = 1;
		yDirection = -1;
	}
	
	/* creationBouncer calls the Bouncer constructor to create a new bouncer, but changes
	 * a number of default settings such as x and y speed. The method was written to accommodate
	 * the existence of creationBlocks, which when hit launch a new bouncer from the block position at 
	 * a random x and y speed. */
	public Bouncer creationBouncer(double blockX, double blockY) {
		Bouncer newBouncer = new Bouncer(myView.getImage(), SCREEN_SIZE, SCREEN_SIZE);
		newBouncer.stayOnPaddle = false;
		newBouncer.setX(blockX);
		newBouncer.setY(blockY);
		newBouncer.xSpeed = getRandomInRange(BOUNCER_MIN_XSPEED, BOUNCER_MAX_XSPEED);
		newBouncer.ySpeed = getRandomInRange(BOUNCER_LOWER_YSPEED, BOUNCER_UPPER_YSPEED);
		return newBouncer;
	}
	
	/* move updates the location of the bouncer image, creating the appearance of movement.
	 * Bouncer movement can be changed depending on whether the bouncer is set to stop, or if
	 * direction or speed of movement are changed. */
	public void move(double elapsedTime) {
		myView.setX(myView.getX() + keepMoving * xDirection * xSpeed * elapsedTime);
		myView.setY(myView.getY() + keepMoving * yDirection * ySpeed * elapsedTime);
	}
	
	/* setX allows the user to set the bouncer's x position. */
	public void setX(double value) {
		myView.setX(value);
	}
	
	/* setY allows the user to set the bouncer's y position. */
	public void setY(double value) {
		myView.setY(value);
	}
	
	/* setXDir allows the user to set whether the bouncer is traveling in a positive
	 * or negative x direction (left or right). The value it takes should be positive or negative
	 * one, to flip the direction. */
	public void setXDir(int value) {
		xDirection = value;
	}
	
	/* setYDir allows the user to set whether the bouncer is traveling in a positive
	 * or negative y direction (up or down). The value it takes should be positive or negative
	 * one, to flip the direction. */
	public void setYDir(int value) {
		yDirection = value;
	}
	
	/* toggleXDir allows the user to flip the current x direction, instead of setting it
	 * manually. This allows the user to reverse the x direction of the bouncer, instead
	 * of simply setting the bouncer to go in a left or right direction. */
	public void toggleXDir(double value) {
		xDirection *= value;
	}
	
	/* toggleXDir allows the user to flip the current y direction, instead of setting it
	 * manually. This allows the user to reverse the y direction of the bouncer, instead
	 * of simply setting the bouncer to go in an up or down direction. */
	public void toggleYDir(double value) {
		yDirection *= value;
	}
	
	/* setStayOnPaddle sets the boolean determining whether or not the bouncer is supposed
	 * to stay on the paddle, for example, at launch position.*/
	public void setStayOnPaddle(boolean set) {
		stayOnPaddle = set;
	}
	
	/* isSetOnPaddle returns whether or not the bouncer is currently set to stay on the paddle. */
	public boolean isSetOnPaddle() {
		return stayOnPaddle;
	}
	
	/* setImage allows the user to set/reset the bouncer's image/appearance.*/
	public void setImage(Image image) {
		myView.setImage(image);
	}
	
	/* setSize allows the user to set the size of the bouncer. */
	public void setSize(int size) {
		bouncerSize = size;
		myView.setFitWidth(bouncerSize);
		myView.setFitHeight(bouncerSize);
	}
	
	/* slow slows down the bouncer to a constant speed, no matter what the previous x or y speeds
	 * were. slow also tracks what those previous speeds were.*/
	public void slow() {
		returnXSpeed = xSpeed;
		returnYSpeed = ySpeed;
		double constant = (Math.pow(BOUNCER_SPEED * .5, 2) / (Math.pow(xSpeed, 2) + Math.pow(ySpeed, 2)));;
		xSpeed = Math.pow(constant * Math.pow(xSpeed, 2), 0.5);
		ySpeed = Math.pow(constant * Math.pow(ySpeed, 2), 0.5);
	}
	
	/* returnSpeed returns the bouncer x and y speeds to what they were before they were slowed
	 * by the slow method.*/
	public void returnSpeed() {
		xSpeed = returnXSpeed;
		ySpeed = returnYSpeed;
	}
	
	/* accelerate speeds up the bouncer x and y speeds, if they are less than a maximum maxed out
	 * speed.*/
	public void accelerate() {
		if(xSpeed < bouncerMaxSpeed && ySpeed < bouncerMaxSpeed && keepMoving == 1) {
			xSpeed *= 1.2;
			ySpeed *= 1.2;
		}
	}
	
	/* stopMoving halts all bouncer movement by setting the variable keepMoving to 0.*/
	public void stopMoving() {
		keepMoving = 0;
	}
	
	/* incSize sizes up the bouncer by a magnitude of 1.2*/
	public void incSize() {
		bouncerSize *= 1.2;
		myView.setFitWidth(bouncerSize);
		myView.setFitHeight(bouncerSize);
	}
	
	/* launch launches the bouncer from the paddle in the direction of the mouse click, at a constant
	 * speed regardless of how far away the cursor was from the bouncer.*/
	public void launch(double x, double y) {
		stayOnPaddle = false;
		yDirection = -1;
		if(x < 0) 
			xDirection = -1;
		double constant = (Math.pow(BOUNCER_SPEED, 2) / (Math.pow(x, 2) + Math.pow(y, 2)));
		xSpeed = Math.pow(constant * Math.pow(x, 2), 0.5);
		ySpeed = Math.pow(constant * Math.pow(y, 2), 0.5);
	}
	
	/* bounceScreen tracks if the bouncer has hit any screen edges, and if so, bounces them
	 * appropriately.*/
	public void bounceScreen (double screenWidth, double screenHeight) {
		if(myView.getX() < 0 || myView.getX() + bouncerSize > screenWidth) {
			xDirection *= -1;
		}
		if(myView.getY() < 0) {
			yDirection *= -1;
		}
	}
	
	/* moveWithPaddle sets the bouncer to move with the paddle, i.e., to stay on the paddle as
	 * the user moves it left or right, before launching the bouncer.*/
	public void moveWithPaddle(Rectangle paddle) {
		if(stayOnPaddle) {
			this.setX(paddle.getX() + paddle.getWidth()/2 - 10);
			this.setY(SCREEN_SIZE - this.getSize() - 2*ORIGINAL_PADDLE_HEIGHT - BOUNCER_OFFSET);
		}
	}
	
	/* bouncePaddle checks if the bouncer has collided with the paddle, and if so, bounces the bouncer
	 * appropriately by toggling its yDirection.*/
	public void bouncePaddle (Rectangle paddle) {
		if(this.getView().getBoundsInParent().intersects(paddle.getBoundsInParent()) ||
				(this.getX() + this.getSize()) > paddle.getX() && this.getX() < paddle.getX() + ORIGINAL_PADDLE_LENGTH
						&& (this.getY() + this.getSize()) > paddle.getY()) {
			yDirection *= -1;
		}
	}
	
	/* getView returns the node of the bouncer.*/
	public Node getView() {
		return myView;
	}
	
	/* getX returns the x position of the bouncer.*/
	public double getX() {
		return myView.getX();
	}
	
	/* getY returns the y position of the bouncer.*/
	public double getY() {
		return myView.getY();
	}
	
	/* getSize returns the size of the bouncer.*/
	public double getSize() {
		return this.bouncerSize;
	}
	
	/* getRandomInRange returns a random value between the given parameters.*/
	private int getRandomInRange(int min, int max) {
		return min + dice.nextInt(max - min) + 1;
	}
}
