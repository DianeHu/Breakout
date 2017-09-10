package game_dh224;

import java.util.Random;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class Bouncer {
	public static final int BOUNCER_MIN_SPEED = -200;
	public static final int BOUNCER_MAX_SPEED = 200;
	public static final double BOUNCER_SPEED = 300;
	public static final int SCREEN_SIZE = 400;
	
	private ImageView myView;
	private int bouncerSize = 15;
	private double xSpeed;
	private double ySpeed;
	private double returnXSpeed;
	private double returnYSpeed;
	private Random dice = new Random();
	public double xDirection;
	public double yDirection;
	public int keepMoving;
	public boolean stayOnPaddle;
	private int bouncerMaxSpeed = 500;
	private boolean fireBall = false;
	
	public Bouncer (Image image, int screenWidth, int screenHeight) {
		stayOnPaddle = true;
		keepMoving = 1;
		myView = new ImageView(image);
		myView.setFitWidth(bouncerSize);
		myView.setFitHeight(bouncerSize);
		myView.setX(screenWidth / 2 - 38);
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
	
	public void setImage(Image image) {
		myView.setImage(image);
	}
	
	public boolean isFireBall() {
		return fireBall;
	}
	public void slow(Bouncer b) {
		b.returnXSpeed = b.xSpeed;
		b.returnYSpeed = b.ySpeed;
		double constant = (Math.pow(BOUNCER_SPEED * .5, 2) / (Math.pow(b.xSpeed, 2) + Math.pow(b.ySpeed, 2)));;
		b.xSpeed = Math.pow(constant * Math.pow(xSpeed, 2), 0.5);
		b.ySpeed = Math.pow(constant * Math.pow(ySpeed, 2), 0.5);
	}
	
	public void returnSpeed(Bouncer b) {
		b.xSpeed = b.returnXSpeed;
		b.ySpeed = b.returnYSpeed;
	}
	
	public void accelerate(Bouncer b) {
		if(b.xSpeed < bouncerMaxSpeed && b.ySpeed < bouncerMaxSpeed && b.keepMoving == 1) {
			b.xSpeed *= 1.2;
			b.ySpeed *= 1.2;
		}
	}
	
	public void incSize() {
		bouncerSize *= 1.2;
		myView.setFitWidth(bouncerSize);
		myView.setFitHeight(bouncerSize);
	}
	
	public void setSize(int size) {
		bouncerSize = size;
		myView.setFitWidth(bouncerSize);
		myView.setFitHeight(bouncerSize);
	}
	
	public void launch(double x, double y) {
		stayOnPaddle = false;
		yDirection = -1;
		if(x < 0) 
			xDirection = -1;
		double constant = (Math.pow(BOUNCER_SPEED, 2) / (Math.pow(x, 2) + Math.pow(y, 2)));
		xSpeed = Math.pow(constant * Math.pow(x, 2), 0.5);
		ySpeed = Math.pow(constant * Math.pow(y, 2), 0.5);
	}
	
	public void bounceScreen (double screenWidth, double screenHeight) {
		if(myView.getX() < 0 || myView.getX() + bouncerSize > screenWidth) {
			xDirection *= -1;
		}
		if(myView.getY() + bouncerSize < 0) {
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
	
	public Node getView() {
		return myView;
	}
	
	public double getX() {
		return myView.getX();
	}
	
	public double getY() {
		return myView.getY();
	}
	
	public double getSize() {
		return this.bouncerSize;
	}
	
	private int getRandomInRange(int min, int max) {
		return min + dice.nextInt(max - min) + 1;
	}
}
