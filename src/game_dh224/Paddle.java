package game_dh224;

import javafx.scene.shape.Rectangle;

public class Paddle {
	public static final int PADDLE_WIDTH = 75;
	public static final int PADDLE_HEIGHT = 50;
	
	private Rectangle paddle;
	
	public Paddle (double xc, double yc) {
		setLoc(xc, yc);
		paddle.setWidth(PADDLE_WIDTH);
		paddle.setHeight(PADDLE_HEIGHT);
	}
	
	public void setLoc(double x, double y) {
		paddle.setX(x);
		paddle.setY(y);
	}
}
