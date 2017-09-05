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

public class Game extends Application {
	public static final String TITLE = "Game";
	public static final String BALL_IMAGE = "ball.gif";
	public static final String BLOCK_IMAGE = "Brick1.gif";
	public static final int SIZE = 400;
	public static final Paint BACKGROUND = Color.DARKSLATEGREY;
	public static final int FRAMES_PER_SECOND = 60;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	public static final int KEY_INPUT_SPEED = 5;
	public static final double GROWTH_RATE = 1.1;
	public static final int NUM_BLOCKS = 500;
	public Group root;

	private Scene myScene;
	private Rectangle Block;
	private Rectangle paddle;
	private Bouncer b;
	private List<Bouncer> myBouncers = new ArrayList<>();
	private List<Block> myBlocks = new ArrayList<>();
	private Group Blocks;

	@Override
	public void start(Stage s) {
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

	private Scene setUpGame(int width, int height, Paint background) {
		Group root = new Group();
		myScene = new Scene(root, width, height, background);
		paddle = new Rectangle(width / 2 - 40, height / 2 - 50, 70, 20);
		Image bouncerImage = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
		Image blockImage = new Image(getClass().getClassLoader().getResourceAsStream(BLOCK_IMAGE));
		b = new Bouncer(bouncerImage, width, height);

		paddle.setFill(Color.CORNSILK);
		Blocks = new Group();

		root.getChildren().add(paddle);
		root.getChildren().add(Blocks);
		root.getChildren().add(b.getView());

		for (int i = 0; i < 10; i++) {
			Block currBlock = new Block(blockImage, width, height);
			Blocks.getChildren().add(currBlock.getView());
			myBlocks.add(currBlock);
		}

		myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		return myScene;
	}

	private void step(double elapsedTime) {
		collide();
		b.move(elapsedTime);

	}

	public void collide() {
		for (int i = 0; i < Blocks.getChildren().size(); i++) {
			if (b.getView().getBoundsInParent().intersects(Blocks.getChildren().get(i).getBoundsInParent())) {
				Blocks.getChildren().remove(i);

				if (Blocks.getChildren().size() == 0) {
					Text victory = new Text(myScene.getWidth() / 3 - 100, myScene.getHeight() / 3,
							"Congrats, you win!");
					Font font = new Font(50);
					victory.setFont(font);
					victory.setFill(Color.WHITE);
					root.getChildren().add(victory);
				}
			}
		}

		b.bouncePaddle(b, paddle);
		b.bounceScreen(SIZE, SIZE);

		for (Block block : myBlocks) {
			b.bounceBlock(b, block);
		}

		if (b.getY() > myScene.getHeight()) {
			Text defeat = new Text(myScene.getWidth() / 3 - 100, myScene.getHeight() / 3, "I'm sorry, you lost!");
			Font font = new Font(50);
			defeat.setFont(font);
			defeat.setFill(Color.WHITE);
			root.getChildren().add(defeat);
		}

	}

	private void handleKeyInput(KeyCode code) {
		if (code == KeyCode.RIGHT) {
			paddle.setX(paddle.getX() + KEY_INPUT_SPEED);
		} else if (code == KeyCode.LEFT) {
			paddle.setX(paddle.getX() - KEY_INPUT_SPEED);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
