package game_dh224;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

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
	public static final int IC_TIME_COUNT = 300;
	public static final String TITLE = "Game";
	public static final String BALL_IMAGE = "ball.gif";
	public static final String MEW = "Mew.gif";
	public static final String STANDARD_BLOCK = "Brick1.gif";
	public static final String SPEED_PLUS_BLOCK = "Brick2.gif";
	public static final String SPEED_MINUS_BLOCK = "Brick3.gif";
	public static final String BLACK_HOLE_BLOCK = "Brick4.gif";
	public static final String CREATION_BLOCK = "Brick5.gif";
	public static final String INSTANT_CLEAR_BLOCK = "Brick6.gif";
	public static final String PADDLE_IMAGE = "paddle.gif";
	public static final int SIZE = 400;
	public static final int FRAMES_PER_SECOND = 60;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	public static final int KEY_INPUT_SPEED = 40;
	public static final double GROWTH_RATE = 1.1;
	public static final int BLOCK_WIDTH = 35;
	public static final int BLOCK_HEIGHT = 20;
	public static final Paint BACKGROUND = Color.CADETBLUE;
	public static final int PADDLE_INC_THRESHOLD = 10;
	public static final int INSTANT_CLEAR_THRESHOLD = 20;
	public static final int OFFSET = 7;
	public static final int PADDLE_MAX_LENGTH = 120;
	public static final int BOUNCER_SIZEUP_THRESHOLD = 15;
	public static final int BOUNCER_MAX_SIZE = 25;
	public static final int BOUNCER_ORIGINAL_SIZE = 15;

	private LabelMaker pCountLabel;
	private LabelMaker lvlNumLabel;
	private LabelMaker livesCounter;
	private Group root = new Group();
	private Group splashRoot;
	private Scene myScene;
	private Scene splashScene;
	private Rectangle paddle;
	private Rectangle backDrop = new Rectangle(SIZE, SIZE, Color.CADETBLUE);
	private boolean isLevelDisplay = false;
	private boolean instantClear = false;
	private Text levelUp;
	private Bouncer myBouncer;
	private ArrayList<Bouncer> myBouncers = new ArrayList<>();
	private ArrayList<Block> myBlocks = new ArrayList<>();
	private ArrayList<Block> blackHoleBlocks = new ArrayList<>();
	private ArrayList<Bouncer> bouncersToBeAdded = new ArrayList<>();
	private ArrayList<Image> myBlockImages = new ArrayList<>();
	private ArrayList<Bouncer> bouncersToBeDeleted = new ArrayList<>();
	private ArrayList<Block> blocksToBeAdded = new ArrayList<>();
	private ArrayList<Block> blocksToBeDeleted = new ArrayList<>();
	private ArrayList<Double> possibleICBlockX = new ArrayList<>();
	private ArrayList<Double> possibleICBlockY = new ArrayList<>();
	private Stage s = null;
	private Image standardBlockImage; 
	private Image speedPlusBlockImage;
	private Image speedMinusBlockImage;
	private Image creationBlockImage;
	private Image blackHoleBlockImage;
	private Image paddleImage;
	private Image bouncerImage;
	private Image instantClearBlockImage;
	private Image mewImage;
	private Text splashWelcome;
	private Text splashExplanation;
	private ListIterator<Block> myBlocksIterator = null;
	private int pointCount = 0;
	private int lvlCounter = 1;
	private int numLives = 3;
	private int timer = 0;
	private int paddleThresholdCount = 0;
	private int icThresholdCount = 0;
	private int bouncerSizeUpCount = 0;
	private Random generator = new Random();

	@Override
	public void start(Stage s) {
		this.s = s;
		Scene splashScene = setUpSplash(SIZE, SIZE, BACKGROUND);
		s.setScene(splashScene);
		s.setTitle(TITLE);
		s.show();

		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY));
		Timeline animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}
	
	private void startGame(Stage s) {
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
		lvlNumLabel = new LabelMaker(SIZE - 60, SIZE - 60, "Level: " + Integer.toString(lvlCounter), Color.WHITE);
		pCountLabel = new LabelMaker(SIZE - 60, SIZE - 40, "Point: " + Integer.toString(pointCount), Color.WHITE);
		livesCounter = new LabelMaker(SIZE - 60, SIZE - 80, "Lives: " + Integer.toString(numLives), Color.WHITE);
		
		initImages();
		initBlockImages();

		root = new Group();
		root.getChildren().clear();
		myScene = new Scene(root, width, height, background);
		paddle = new Rectangle(width / 2 - 40, height - 20, 70, 10);
		paddle.setFill(new ImagePattern(paddleImage));
		myBouncer = new Bouncer(bouncerImage, width, height);
		myBouncers.add(myBouncer);
		root.getChildren().add(paddle);
		root.getChildren().add(myBouncer.getView());
		root.getChildren().add(pCountLabel.getLabel());
		root.getChildren().add(lvlNumLabel.getLabel());
		root.getChildren().add(livesCounter.getLabel());

		switch (lvlCounter) {
		case (1):
			Levels.level1(root, standardBlockImage, myBlocks, OFFSET, SIZE, BLOCK_WIDTH, BLOCK_HEIGHT);
			break;
		case (2):
			Levels.level2(root, myBlockImages, myBlocks, OFFSET, SIZE, BLOCK_WIDTH, BLOCK_HEIGHT, paddle);
			break;
		case (3):
			Levels.level3(root, myBlockImages, myBlocks, OFFSET, SIZE, BLOCK_WIDTH, BLOCK_HEIGHT, paddle);
			break;
		}
		
		initICBlockPos();

		myScene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));
		myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
		myScene.setOnKeyReleased(e -> handleKeyRelease(e.getCode()));
		return myScene;
	}
	
	private Scene setUpSplash(int width, int height, Paint background) {
		splashRoot = new Group();
		splashScene = new Scene(splashRoot, width, height, background);
		
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
		splashRoot.getChildren().addAll(splashWelcome, splashExplanation);

		splashScene.setOnKeyPressed(e -> handleSplashKeyPress(e.getCode()));
		return splashScene;
	}
	
	private void initICBlockPos() {
		for(int i = 0; i < myBlocks.size(); i++) {
			possibleICBlockX.add(myBlocks.get(i).getX());
		}
		
		for(int i = 0; i < myBlocks.size(); i++) {
			possibleICBlockY.add(myBlocks.get(i).getY());
		}
	}

	private void initImages() {
		bouncerImage = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
		standardBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(STANDARD_BLOCK));
		speedPlusBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(SPEED_PLUS_BLOCK));
		speedMinusBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(SPEED_MINUS_BLOCK));
		blackHoleBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(BLACK_HOLE_BLOCK));
		creationBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(CREATION_BLOCK));
		instantClearBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(INSTANT_CLEAR_BLOCK));
		paddleImage = new Image(getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE));
		mewImage = new Image(getClass().getClassLoader().getResourceAsStream(MEW));
	}
	
	private void initBlockImages() {
		myBlockImages.add(standardBlockImage);
		myBlockImages.add(speedPlusBlockImage);
		myBlockImages.add(speedMinusBlockImage);
		myBlockImages.add(blackHoleBlockImage);
		myBlockImages.add(creationBlockImage);
		myBlockImages.add(instantClearBlockImage);
	}

	private void step(double elapsedTime) {
		timer++;
		myBouncer.moveWithPaddle(myBouncer, paddle);
		myBlocksIterator = myBlocks.listIterator();
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
				if(block instanceof instantClearBlock && timer >= IC_TIME_COUNT) {
					blocksToBeDeleted.add(block);
					timer = 0;
				}
				if (b.getView().getBoundsInParent().intersects(block.getView().getBoundsInParent())) {
					abnormalBlockCheck(block, b);
					block.bounceBlock(b);
					root.getChildren().remove(block.getView());
					updatePointCounts(block);
					checkPowerUps();
					pCountLabel.setText("Points: " + Integer.toString(pointCount));
					myBlocksIterator.remove();
				}
			}
			updateMyBouncers();
		}
		if(instantClear) {
			myBouncers.clear();
			myBlocks.clear();
			nextLevel();
			instantClear = false;
		}

		for (Bouncer b : myBouncers) {
			b.bouncePaddle(b, paddle);
			b.bounceScreen(SIZE, SIZE);
		}
		updateMyBlocks();
	}

	private void updateMyBlocks() {
		for(Block block : blocksToBeAdded) {
			root.getChildren().add(block.getView());
		}
		for(Block block : blocksToBeDeleted) {
			root.getChildren().remove(block.getView());
		}
		myBlocks.addAll(blocksToBeAdded);
		myBlocks.removeAll(blocksToBeDeleted);
		clearBlockMods();
	}

	private void updateMyBouncers() {
		for(Bouncer b : bouncersToBeAdded) {
			root.getChildren().add(b.getView());
		}
		myBouncers.addAll(bouncersToBeAdded);
		myBouncers.removeAll(bouncersToBeDeleted);
		clearBouncerMods();
	}

	private void checkPowerUps() {
		if(paddleThresholdCount >= PADDLE_INC_THRESHOLD && paddle.getWidth() < PADDLE_MAX_LENGTH) {
			paddleThresholdCount = paddleThresholdCount % PADDLE_INC_THRESHOLD;
			paddle.setWidth(paddle.getWidth() + 10);
		}
		if(icThresholdCount >= INSTANT_CLEAR_THRESHOLD) {
			icThresholdCount = INSTANT_CLEAR_THRESHOLD % icThresholdCount;
			Block icBlock = new instantClearBlock(instantClearBlockImage);
			int rand = generator.nextInt(possibleICBlockX.size());
			icBlock.setLoc(possibleICBlockX.get(rand), possibleICBlockY.get(rand));
			//icBlock.setLoc(blockReference.getX(), blockReference.getY());
			blocksToBeAdded.add(icBlock);
			timer = 0;
		}
		if(bouncerSizeUpCount >= BOUNCER_SIZEUP_THRESHOLD && myBouncer.getSize() < BOUNCER_MAX_SIZE) {
			bouncerSizeUpCount = BOUNCER_SIZEUP_THRESHOLD % bouncerSizeUpCount;
			myBouncer.incSize();
		}
	}

	private void updatePointCounts(Block block) {
		pointCount += block.getVal();
		paddleThresholdCount += block.getVal();
		bouncerSizeUpCount += block.getVal();
		icThresholdCount += block.getVal();
	}

	private void abnormalBlockCheck(Block block, Bouncer b) {
		if (block instanceof blackHoleBlock && b == myBouncer) {
			restartBouncer(myBouncer);
			blackHoleBlocks.remove(block);
		} else if(block instanceof blackHoleBlock && b != myBouncer) {
			blackHoleBlocks.remove(block);
			root.getChildren().remove(b.getView());
			bouncersToBeDeleted.add(b);
		}
		else if (block instanceof creationBlock) {
			Bouncer newBounce = myBouncer.creationBouncer(block.getX(), block.getY());
			bouncersToBeAdded.add(newBounce);
		}
		if (block instanceof instantClearBlock) {
			instantClear = true;
		}
	}

	private void winLose() {
		ArrayList<Block> needToWin = new ArrayList<>();
		needToWin.addAll(myBlocks);
		needToWin.removeAll(blackHoleBlocks);
		if (needToWin.size() == 0) {
			if (lvlCounter == 3) {
				Screen.victory(root, myBouncer);
			} else {
				myBouncer.keepMoving = 0;
				nextLevel();
			}
		}
		if (myBouncer.getY() > myScene.getHeight()) {
			if (needToWin.size() != 0 && numLives > 1) {
				restartBouncer(myBouncer);
			} else if (numLives <= 1) {
				Screen.defeat(root, myBouncer);
			}
		}
	}

	private void restartBouncer(Bouncer myBouncer) {
		if(numLives > 1) {
			numLives--;
			myBouncer.stayOnPaddle = true;
			myBouncer.setSize(BOUNCER_ORIGINAL_SIZE);
			myBouncer.yDirection = -1;
			myBouncer.xDirection = 1;
			paddle.setX(SIZE / 2 - 40);
			paddle.setY(SIZE - 20);
			myBouncer.moveWithPaddle(myBouncer, paddle);
			livesCounter.setText("Lives: " + Integer.toString(numLives));
		} else if (numLives == 1) {
			Screen.defeat(root, myBouncer);
		}
	}

	private void nextLevel() {
		myBouncer.keepMoving = 0;
		root.getChildren().clear();
		myBouncers.clear();
		clearBouncerMods();
		myBlocks.clear();
		clearBlockMods();
		blackHoleBlocks.clear();
		numLives = 3;
		lvlCounter++;
		if (lvlCounter <= 3) {
			Scene nextScene = setUpGame(SIZE, SIZE, BACKGROUND);
			lvlNumLabel.setText("Level: " + Integer.toString(lvlCounter));
			pointCount = 0;
			pCountLabel.setText("Points: " + Integer.toString(pointCount));
			s.setScene(nextScene);
			levelUp = Screen.passLevel(lvlCounter);
			root.getChildren().addAll(backDrop, levelUp);
			isLevelDisplay = true;
		} else if (lvlCounter > 3) {
			Screen.victory(root, myBouncer);
		}
	}

	private void clearBlockMods() {
		blocksToBeAdded.clear();
		blocksToBeDeleted.clear();
	}

	private void clearBouncerMods() {
		bouncersToBeAdded.clear();
		bouncersToBeDeleted.clear();
	}

	private void handleKeyPress(KeyCode code) {
		if (paddle.getX() < 0) {
			paddle.setX(SIZE - 75);
		} else if (paddle.getX() > SIZE) {
			paddle.setX(0);
		} else if (code == KeyCode.RIGHT) {
			paddle.setX(paddle.getX() + KEY_INPUT_SPEED);
		} else if (code == KeyCode.LEFT) {
			paddle.setX(paddle.getX() - KEY_INPUT_SPEED);
		} else if (code == KeyCode.C && isLevelDisplay == false) {
			nextLevel();
		} else if (code == KeyCode.S) {
			myBouncer.slow(myBouncer);
		} else if (code == KeyCode.A) {
			myBouncer.accelerate(myBouncer);
		}
	}
	
	private void handleSplashKeyPress(KeyCode code) {
		if(code == KeyCode.SPACE) {
			startGame(s);
		}
	}
	
	private void handleKeyRelease(KeyCode code) {
		if(code == KeyCode.S) {
			myBouncer.returnSpeed(myBouncer);
		}
	}

	private void handleMouseInput(double x, double y) {
		if (x > 0 && x < SIZE && y > 0 && y < SIZE) {
			if(myBouncer.stayOnPaddle && isLevelDisplay == false) {
				myBouncer.launch(x - myBouncer.getX(), y - myBouncer.getY());
			} else if (isLevelDisplay) {
				root.getChildren().removeAll(backDrop, levelUp);
				isLevelDisplay = false;
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
