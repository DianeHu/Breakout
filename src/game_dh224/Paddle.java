package game_dh224;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle {
	public static final int PADDLE_WIDTH = 75;
	public static final int PADDLE_HEIGHT = 15;
	public static final Color COLOR = Color.CORNSILK;
	
	private Rectangle paddle;
	
	public Paddle (double xc, double yc) {
		setLoc(xc, yc);
		paddle.setWidth(PADDLE_WIDTH);
		paddle.setHeight(PADDLE_HEIGHT);
		paddle.setFill(COLOR);
	}
	
	public void setLoc(double x, double y) {
		paddle.setX(x);
		paddle.setY(y);
	}
	
	public double getX() {
		return paddle.getX();
	}
	
	public double getY() {
		return paddle.getY();
	}
	
	public Rectangle getRec() {
		return paddle;
	}
	
	public void setX(double xVal) {
		paddle.setX(xVal);
	}
	
	public void setY(double yVal) {
		paddle.setY(yVal);
	}
}
