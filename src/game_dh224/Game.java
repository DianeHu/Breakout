package game_dh224;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
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
	public static final String STANDARD_BLOCK = "Brick1.gif";
	public static final String SPEED_PLUS_BLOCK = "Brick2.gif";
	public static final String SPEED_MINUS_BLOCK = "Brick3.gif";
	public static final String BLACK_HOLE_BLOCK = "Brick4.gif";
	public static final String CREATION_BLOCK = "Brick5.gif";
	public static final String INSTANT_CLEAR_BLOCK = "Brick6.gif";
	public static final String PADDLE_IMAGE = "paddle.gif";
	public static final int SIZE = 400;
	public static final Paint BACKGROUND = Color.DARKSLATEGREY;
	public static final int FRAMES_PER_SECOND = 60;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	public static final int KEY_INPUT_SPEED = 40;
	public static final double GROWTH_RATE = 1.1;
	public static final int BLOCK_WIDTH = 40;
	public static final int BLOCK_HEIGHT = 20;
	// public Scene initialScreen = new Scene(root, SIZE, SIZE, Color.AZURE);

	private Label pCountLabel = new Label();
	private Label lvlNumLabel = new Label();
	private Label livesCounter = new Label();
	private int pointCount = 0;
	private Group root = new Group();
	private Scene myScene;
	private Rectangle paddle;
	private Bouncer myBouncer;
	private ArrayList<Bouncer> myBouncers = new ArrayList<>();
	private ListIterator<Block> myBlocksIterator = null;
	private ArrayList<Block> myBlocks = new ArrayList<>();
	private ListIterator<Bouncer> myBouncersIterator = null;
	private ArrayList<Block> blackHoleBlocks = new ArrayList<>();
	private ArrayList<Bouncer> toBeAdded = new ArrayList<>();
	private Stage s = null;
	private int lvlCounter = 1;
	private int numLives = 3;
	private Image standardBlockImage;
	private Image speedPlusBlockImage;
	private Image speedMinusBlockImage;
	private Image creationBlockImage;
	private Image blackHoleBlockImage;
	private Image paddleImage;
	private Image bouncerImage;
	private Image instantClearBlockImage;
	private boolean needsSplash = true;
	private boolean sHeld = false;
	private int numNewBouncers = 0;
	private StackPane stack;
	private Rectangle backDrop;
	private Text splashWelcome;
	private Text splashExplanation;
	private int offset = 7;

	@Override
	public void start(Stage s) {

		this.s = s;
		root.getChildren().clear();
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
		initNumLabel();
		initImages();
		initPointCounter();
		initLivesCounter();

		// stack = loseScreen(stack, SIZE, SIZE);

		root = new Group();
		root.getChildren().clear();
		myScene = new Scene(root, width, height, background);

		paddle = new Rectangle(width / 2 - 40, height - 20, 70, 10);
		paddle.setFill(new ImagePattern(paddleImage));
		myBouncer = new Bouncer(bouncerImage, width, height);
		myBouncers.add(myBouncer);
		splashScreen();
		root.getChildren().add(paddle);
		// root.getChildren().add(stack);
		root.getChildren().add(myBouncer.getView());
		root.getChildren().add(pCountLabel);
		root.getChildren().add(lvlNumLabel);
		root.getChildren().add(livesCounter);

		switch (lvlCounter) {
		case (1):
			level1(SIZE, BLOCK_WIDTH, BLOCK_HEIGHT);
			break;
		case (2):
			level2(SIZE, BLOCK_WIDTH, BLOCK_HEIGHT);
			break;
		case (3):
			level3(SIZE, BLOCK_WIDTH, BLOCK_HEIGHT);
			break;
		}

		if (lvlCounter == 1) {
			root.getChildren().add(backDrop);
			root.getChildren().add(splashWelcome);
			root.getChildren().add(splashExplanation);
		}
		myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
		return myScene;
	}

	private void level1(int width, int blockWidth, int blockHeight) {
		for (int i = 1; i < (width / blockWidth - 3); i++) {
			for (int j = 1; j < (width / blockHeight - 15); j++) {
				Block currBlock = new Block(standardBlockImage);
				currBlock.setLoc(blockWidth * i + offset * i, blockHeight * j + offset * j);
				myBlocks.add(currBlock);
				root.getChildren().add(currBlock.getView());
			}
		}
	}

	private void level2(int width, int blockWidth, int blockHeight) {
		for (int i = 1; i < (width / blockWidth - 2); i++) {
			for (int j = 1; j < (width / blockHeight - 10); j++) {
				if(i%2 != 0 && j%2 != 0) {
					Block currBlock = new Block(standardBlockImage);
					currBlock.setLoc(blockWidth * i + offset * i, blockHeight * j + offset * j);
					myBlocks.add(currBlock);
					root.getChildren().add(currBlock.getView());
				} else if (i%2 == 0 && j%4 == 0) {
					Block currBlock = new speedPlusBlock(speedPlusBlockImage);
					currBlock.setLoc(blockWidth * i + offset * i, blockHeight * j + offset * j);
					myBlocks.add(currBlock);
					root.getChildren().add(currBlock.getView());
				} else if (i%2 == 0 && j%2 == 0) {
					Block currBlock = new speedMinusBlock(speedMinusBlockImage);
					currBlock.setLoc(blockWidth * i + offset * i, blockHeight * j + offset * j);
					myBlocks.add(currBlock);
					root.getChildren().add(currBlock.getView());
				}
			}
		}
		paddle.setWidth(paddle.getWidth() - 10);
		paddle.setX(paddle.getX() + 5);
	}
	
	private void level3(int width, int blockWidth, int blockHeight) {
		for (int i = 1; i < (width / blockWidth - 3); i += 2) {
			for (int j = 2; j < (width / blockHeight - 15); j += 2) {
				Block currBlock = new Block(standardBlockImage);
				currBlock.setLoc(blockWidth * i + offset * i, blockHeight * j + offset * j);
				myBlocks.add(currBlock);
				root.getChildren().add(currBlock.getView());
			}
		}
	}

	public void addBouncer(Bouncer bounce) {
		root.getChildren().add(bounce.getView());
	}

	public void splashScreen() {
		backDrop = new Rectangle(0, 0, SIZE, SIZE);
		backDrop.setFill(Color.BLUEVIOLET);
		splashWelcome = new Text(SIZE / 2 - 150, SIZE / 3, "Welcome to Breakout! Click anywhere to start.");
		splashExplanation = new Text(SIZE / 2 - 150, 2 * SIZE / 3,
				"Your goal is to destroy all blocks--however, beware! Hitting certain "
						+ "blocks may have unforseen consequences! Good luck!");
		splashExplanation.setWrappingWidth(300);
		Font font = new Font(15);
		splashWelcome.setFont(font);
		splashWelcome.setFill(Color.WHITE);
		splashExplanation.setFont(font);
		splashExplanation.setFill(Color.WHITE);
	}

	/*
	 * public StackPane loseScreen(StackPane stack, int screenWidth, int
	 * screenHeight) { Rectangle lose = new Rectangle(30, 30, screenWidth,
	 * screenHeight); lose.setFill(Color.BLUEVIOLET); Text defeat = new
	 * Text(screenWidth / 3 - 100, screenHeight / 3, "I'm sorry, you lost!"); Font
	 * font = new Font(25); defeat.setFont(font); defeat.setFill(Color.WHITE);
	 * stack.getChildren().addAll(lose, defeat);
	 * System.out.println(stack.getChildren()); return stack; }
	 */

	private void initImages() {
		bouncerImage = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
		standardBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(STANDARD_BLOCK));
		speedPlusBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(SPEED_PLUS_BLOCK));
		speedMinusBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(SPEED_MINUS_BLOCK));
		blackHoleBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(BLACK_HOLE_BLOCK));
		creationBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(CREATION_BLOCK));
		instantClearBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(INSTANT_CLEAR_BLOCK));
		paddleImage = new Image(getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE));
	}

	private void initNumLabel() {
		lvlNumLabel.setLayoutX(SIZE - 60);
		lvlNumLabel.setLayoutY(SIZE - 60);
		lvlNumLabel.setText("Level: " + Integer.toString(lvlCounter));
		lvlNumLabel.setTextFill(Color.WHITE);
	}

	private void initPointCounter() {
		pCountLabel.setLayoutX(SIZE - 60);
		pCountLabel.setLayoutY(SIZE - 40);
		pCountLabel.setText("Point: " + Integer.toString(pointCount));
		pCountLabel.setTextFill(Color.WHITE);
	}

	private void initLivesCounter() {
		livesCounter.setLayoutX(SIZE - 60);
		livesCounter.setLayoutY(SIZE - 80);
		livesCounter.setText("Lives: " + Integer.toString(numLives));
		livesCounter.setTextFill(Color.WHITE);
	}

	private void step(double elapsedTime) {
		myBouncer.moveWithPaddle(myBouncer, paddle);
		myBlocksIterator = myBlocks.listIterator();
		myBouncersIterator = myBouncers.listIterator();
		bounce();
		for (Bouncer b : myBouncers) {
			b.move(elapsedTime);
		}
		winLose();
	}

	private void bounce() {
		while (myBlocksIterator.hasNext()) {
			Block block = myBlocksIterator.next();
			for(Bouncer b : myBouncers) {
				if (b.getView().getBoundsInParent().intersects(block.getView().getBoundsInParent())) {
					if (block instanceof blackHoleBlock) {
						restartBouncer(myBouncer);
						blackHoleBlocks.remove(block);
					} else if (block instanceof creationBlock) {
						Bouncer newBounce = myBouncer.creationBouncer(block.getX(), block.getY());
						toBeAdded.add(newBounce);
					}
					if (block instanceof instantClearBlock) {
						myBlocksIterator.remove();
						nextLevel();
					}
					block.bounceBlock(myBouncer);
					// root.getChildren().remove(block);
					root.getChildren().remove(block.getView());
					pointCount += block.getVal();
					pCountLabel.setText("Points: " + Integer.toString(pointCount));
					myBlocksIterator.remove();
				}
			}
			for(Bouncer b : toBeAdded) {
				addBouncer(b);
			}
			myBouncers.addAll(toBeAdded);
		}

		for (Bouncer b : myBouncers) {
			b.bouncePaddle(b, paddle);
			b.bounceScreen(SIZE, SIZE);
		}

		/*while (myBlocksIterator.hasNext()) {
			Block block = myBlocksIterator.next();
			collide(myBouncer, block);
		}

		for (Bouncer b : myBouncers) {
			b.bouncePaddle(b, paddle);
			b.bounceScreen(SIZE, SIZE);
		}*/
	}

	private void collide(Bouncer b, Block block) {
		if (b.getView().getBoundsInParent().intersects(block.getView().getBoundsInParent())) {
			if (block instanceof blackHoleBlock) {
				restartBouncer(myBouncer);
				blackHoleBlocks.remove(block);
			} else if (block instanceof creationBlock) {
				Bouncer newBounce = myBouncer.creationBouncer(block.getX(), block.getY());
				toBeAdded.add(newBounce);
				addBouncer(newBounce);
			}
			if (block instanceof instantClearBlock) {
				myBlocksIterator.remove();
				nextLevel();
			}
			block.bounceBlock(myBouncer);
			// root.getChildren().remove(block);
			root.getChildren().remove(block.getView());
			pointCount += block.getVal();
			pCountLabel.setText("Points: " + Integer.toString(pointCount));
			myBlocksIterator.remove();
		}
		myBouncers.addAll(toBeAdded);
	}

	private void winLose() {
		ArrayList<Block> needToWin = new ArrayList<>();
		needToWin.addAll(myBlocks);
		needToWin.removeAll(blackHoleBlocks);
		if (needToWin.size() == 0) {
			if (lvlCounter == 3) {
				Text victory = new Text(myScene.getWidth() / 3 - 100, myScene.getHeight() / 3, "Congrats, you win!");
				Font font = new Font(25);
				victory.setFont(font);
				victory.setFill(Color.WHITE);
				root.getChildren().add(victory);
				myBouncer.keepMoving = 0;
			} else {
				myBouncer.keepMoving = 0;
				nextLevel();
			}
		}

		if (myBouncer.getY() > myScene.getHeight()) {
			if (needToWin.size() != 0 && numLives != 0) {
				restartBouncer(myBouncer);
			} else if (numLives == 0) {
				Text defeat = new Text(myScene.getWidth() / 3 - 100, myScene.getHeight() / 3, "I'm sorry, you lost!");
				Font font = new Font(25);
				defeat.setFont(font);
				defeat.setFill(Color.WHITE);
				root.getChildren().add(defeat);
				myBouncer.keepMoving = 0;
			}
		}

	}

	private void restartBouncer(Bouncer myBouncer) {
		numLives--;
		myBouncer.stayOnPaddle = true;
		myBouncer.yDirection = -1;
		myBouncer.xDirection = 1;
		paddle.setX(SIZE / 2 - 40);
		paddle.setY(SIZE - 20);
		myBouncer.moveWithPaddle(myBouncer, paddle);
		livesCounter.setText("Lives: " + Integer.toString(numLives));
	}

	private void nextLevel() {
		myBouncer.keepMoving = 0;
		myBlocks.clear();
		blackHoleBlocks.clear();
		numLives = 3;
		lvlCounter++;
		if (lvlCounter <= 3) {
			Scene nextScene = setUpGame(SIZE, SIZE, BACKGROUND);
			lvlNumLabel.setText("Level: " + Integer.toString(lvlCounter));
			pointCount = 0;
			pCountLabel.setText("Points: " + Integer.toString(pointCount));
			s.setScene(nextScene);
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

		if (code == KeyCode.C) {
			myBouncer.keepMoving = 0;
			if (lvlCounter < 3) {
				nextLevel();
			}
		} else if (code == KeyCode.S) {
			sHeld = true;
			if (sHeld) {
				myBouncer.slow(myBouncer);
			}
		} else if (code == KeyCode.A) {
			myBouncer.accelerate(myBouncer);
		}
		sHeld = false;
	}

	private void handleMouseInput(double x, double y) {
		if (x > 0 && x < SIZE && y > 0 && y < SIZE) {
			if (lvlCounter == 1) {
				root.getChildren().removeAll(backDrop, splashWelcome, splashExplanation);
			}
			myBouncer.stayOnPaddle = false;
			myBouncer.launch(x - myBouncer.getX(), y - myBouncer.getY());
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
