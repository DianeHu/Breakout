package game_dh224;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GamePrevious extends Application {
	public static final String TITLE = "Game";
	public static final String BALL_IMAGE = "ball.gif";
	public static final String BLOCK_IMAGE = "Brick1.gif";
	public static final int SIZE = 400;
	public static final Paint BACKGROUND = Color.DARKSLATEGREY;
	public static final int FRAMES_PER_SECOND = 60;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	public static final int KEY_INPUT_SPEED = 40;
	public static final double GROWTH_RATE = 1.1;
	public static final int NUM_BLOCKS = 500;

	private Scene myScene;
	private Rectangle paddle;
	private Bouncer myBouncer;
	private ArrayList<Bouncer> myBouncers = new ArrayList<>();
	private ArrayList<Block> myBlocks = new ArrayList<>();
	private Group Blocks;
	private Group root = new Group();
	private Stage s;
	private int lvlCounter = 1;

	@Override
	public void start(Stage s) {
		this.s = s;
		Scene scene = setUpGame(SIZE, SIZE, BACKGROUND);
		s.setScene(scene);
		s.setTitle(TITLE);
		s.show();

		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
		Timeline animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}

	protected Scene setUpGame(int width, int height, Paint background) {
		root = new Group();
		myScene = new Scene(root, width, height, background);
		paddle = new Rectangle(width / 2 - 40, height - 30, 70, 15);
		Image bouncerImage = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
		Image blockImage = new Image(getClass().getClassLoader().getResourceAsStream(BLOCK_IMAGE));
		myBouncer = new Bouncer(bouncerImage, width, height);

		paddle.setFill(Color.CORNSILK);
		Blocks = new Group();

		root.getChildren().add(paddle);
		root.getChildren().add(Blocks);
		root.getChildren().add(myBouncer.getView());

		for (int i = 0; i < 3; i++) {
			Block currBlock = new Block(blockImage, width, height);
			currBlock.setLoc(75 * i, 50 * i);
			myBlocks.add(currBlock);
			root.getChildren().add(currBlock.getView());
		}

		myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		return myScene;
	}

	public void step(double elapsedTime) {
		bounce();
		myBouncer.move(elapsedTime);
		winLose();
	}
	
	private void bounce() {
		for (int i = 0; i < myBlocks.size(); i++) {
			if (myBouncer.getView().getBoundsInParent().intersects(myBlocks.get(i).getView().getBoundsInParent())) {
				root.getChildren().remove(myBlocks.get(i).getView());
				myBouncer.bounceBlock(myBouncer, myBlocks.get(i));
				myBlocks.remove(i);
			}
		}
		myBouncer.bouncePaddle(myBouncer, paddle);
		myBouncer.bounceScreen(SIZE, SIZE);
	}
	
	private void winLose() {
		if (myBlocks.size() == 0) {
			Text victory = new Text(myScene.getWidth() / 3 - 100, myScene.getHeight() / 3,
					"Congrats, you win!");
			Font font = new Font(25);
			victory.setFont(font);
			victory.setFill(Color.WHITE);
			root.getChildren().add(victory);
			myBouncer.keepMoving = 0;
			if(lvlCounter < 3) {
				lvlCounter++;
				Scene nextScene = setUpGame(SIZE, SIZE, BACKGROUND);
				s.setScene(nextScene);
			}
		}
		
		if (myBouncer.getY() > myScene.getHeight()) {
			Text defeat = new Text(myScene.getWidth() / 3 - 100, myScene.getHeight() / 3, "I'm sorry, you lost!");
			Font font = new Font(25);
			defeat.setFont(font);
			defeat.setFill(Color.WHITE);
			root.getChildren().add(defeat);
			myBouncer.keepMoving = 0;
			if(lvlCounter < 3) {
				lvlCounter++;
				Scene nextScene = setUpGame(SIZE, SIZE, BACKGROUND);
				s.setScene(nextScene);
			}
		}
	}
	
	private void handleKeyInput(KeyCode code) {
		if (paddle.getX() < 0) {
			paddle.setX(SIZE - 75);
		} else if (paddle.getX() > SIZE) {
			paddle.setX(0);
		} else if (code == KeyCode.RIGHT) {
			paddle.setX(paddle.getX() + KEY_INPUT_SPEED);
		} else if (code == KeyCode.LEFT) {
			paddle.setX(paddle.getX() - KEY_INPUT_SPEED);
		}
	}
	
	private void handleMouseInput (double x, double y) {
		/*if(x > 0 && x < SIZE && y > 0 && y < SIZE && needsSplash) {
			needsSplash = false;
			start(s);
		}*/
        if (x > 0 && x < SIZE && y > 0 && y < SIZE) {
        	myBouncer.stayOnPaddle = false;
            myBouncer.launch(x - myBouncer.getX(), y - myBouncer.getY());
        }
    }

	public static void main(String[] args) {
		launch(args);
	}
}
