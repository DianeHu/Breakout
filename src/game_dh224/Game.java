package game_dh224;

/* Written by Diane Hu, with help from UTA Delia, UTA Jordan, UTA Yuxiang, UTA Mike.
 * This game is an application version of Atari Breakout, dependent on JavaFX and java.util
 * libraries. Game relies on class Block and its extensions, class Bouncer, class Levels,
 * and class LabelMaker. When run, Game plays out a variation of breakout with three levels,
 * six block types, and several different powerups and cheat keys. See method comments for
 * demonstration of specific functionality. */

import java.util.ArrayList;

import java.util.ListIterator;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game extends Application {
	public static final String TITLE = "Game";
	public static final String BALL_IMAGE = "ball.gif";
	public static final String STANDARD_BLOCK = "Brick1.gif";
	public static final String SPEED_PLUS_BLOCK = "Brick2.gif";
	public static final String SPEED_MINUS_BLOCK = "Brick9.gif";
	public static final String BLACK_HOLE_BLOCK = "Brick4.gif";
	public static final String CREATION_BLOCK = "Brick5.gif";
	public static final String INSTANT_CLEAR_BLOCK = "Brick6.gif";
	public static final String PADDLE_IMAGE = "paddle.gif";
	public static final int IC_TIME_COUNT = 300;
	public static final int SIZE = 400;
	public static final int FRAMES_PER_SECOND = 60;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final int KEY_INPUT_SPEED = 40;
	public static final int BLOCK_WIDTH = 35;
	public static final int BLOCK_HEIGHT = 20;
	public static final int PADDLE_INC_THRESHOLD = 15;
	public static final int INSTANT_CLEAR_THRESHOLD = 25;
	public static final int BLOCK_OFFSET = 7;
	public static final int PADDLE_MAX_LENGTH = 120;
	public static final int BOUNCER_SIZEUP_THRESHOLD = 20;
	public static final int BOUNCER_MAX_SIZE = 25;
	public static final int BOUNCER_ORIGINAL_SIZE = 15;
	public static final int LABEL_X_OFFSET = 60;
	public static final int ORIGINAL_PADDLE_LENGTH = 70;
	public static final int ORIGINAL_PADDLE_HEIGHT = 10;
	public static final int INITIAL_PADDLE_X = SIZE / 2 - 40;
	public static final int INITIAL_PADDLE_Y = SIZE - 20;
	public static final int SPLASH_XLOC = SIZE / 2 - 150;
	public static final int SPLASH_YLOC = 40;
	public static final int SPLASH_WRAP = 300;
	public static final int LEVEL_DISPLAY_X = SIZE / 2 - 130;
	public static final int LEVEL_DISPLAY_Y = SIZE / 2;
	public static final Font STANDARD_FONT = new Font("Consolas", 12);
	public static final Font SMALL_FONT = new Font("Consolas", 9);
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	public static final double GROWTH_RATE = 1.1;
	public static final Paint BACKGROUND = Color.CADETBLUE;

	private LabelMaker pCountLabel;
	private LabelMaker lvlNumLabel;
	private LabelMaker livesCounter;
	private LabelMaker standardExplanation;
	private LabelMaker speedPlusExplanation;
	private LabelMaker speedMinusExplanation;
	private LabelMaker blackHoleExplanation;
	private LabelMaker creationExplanation;
	private LabelMaker instantClearExplanation;
	private Group root = new Group();
	private Group splashRoot;
	private Scene myScene;
	private Scene splashScene;
	private Rectangle paddle;
	private Rectangle backDrop = new Rectangle(SIZE, SIZE, Color.CADETBLUE);
	private Rectangle standard;
	private Rectangle speedUp;
	private Rectangle speedMinus;
	private Rectangle blackHole;
	private Rectangle instantClearRec;
	private Rectangle creation;
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
	private LabelMaker splashWelcome;
	private Text splashExplanation;
	private ListIterator<Block> myBlocksIterator = null;
	private int pointCount = 0;
	private int lvlCounter = 1;
	private int numLives = 3;
	private int timer = 0;
	private int paddleThresholdCount = 0;
	private int icThresholdCount = 0;
	private int bouncerSizeUpCount = 0;
	private int totalPoints = 0;
	private Random generator = new Random();

	
	/* start sets an initial scene with a splash screen.*/
	@Override
	public void start(Stage s) {
		this.s = s;
		Scene splashScene = setUpSplash(SIZE, SIZE, BACKGROUND);
		s.setScene(splashScene);
		s.setTitle(TITLE);
		s.show();

		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY));
		startAnimation(frame);
	}
	
	/* startGame begins the actual game, setting up a new Breakout game.*/
	private void startGame(Stage s) {
		this.s = s;
		root.getChildren().clear();
		Scene scene = setUpGame(SIZE, SIZE, BACKGROUND);
		s.setScene(scene);
		s.setTitle(TITLE);
		s.show();

		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
		startAnimation(frame);
	}
	
	/* startAnimation sets the timeline components of the game.*/
	private void startAnimation(KeyFrame frame) {
		Timeline animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}
	
	/* setUpGame creates the game levels, setting point, level, and life labels, initializing all images and scene objects, and
	 * setting up the level block configuration depending on what level the user is on. setUpGame calls Levels, which is simply
	 * contains helper methods that determine block locations algorithmically depending on level. The level calls also add all
	 * blocks initialized to an ArrayList of blocks.*/
	protected Scene setUpGame(int width, int height, Paint background) {
		pCountLabel = new LabelMaker(SIZE - LABEL_X_OFFSET, SIZE - 40, "Point: " + Integer.toString(pointCount), Color.BLACK, SMALL_FONT);
		lvlNumLabel = new LabelMaker(SIZE - LABEL_X_OFFSET, SIZE - 60, "Level: " + Integer.toString(lvlCounter), Color.BLACK, SMALL_FONT);
		livesCounter = new LabelMaker(SIZE - LABEL_X_OFFSET, SIZE - 80, "Lives: " + Integer.toString(numLives), Color.BLACK, SMALL_FONT);
		
		initImages();
		initBlockImages();
		
		root = new Group();
		root.getChildren().clear();
		myScene = new Scene(root, width, height, background);
		paddle = new Rectangle(INITIAL_PADDLE_X, INITIAL_PADDLE_Y, ORIGINAL_PADDLE_LENGTH, ORIGINAL_PADDLE_HEIGHT);
		paddle.setFill(new ImagePattern(paddleImage));
		myBouncer = new Bouncer(bouncerImage, width, height);
		myBouncers.add(myBouncer);
		root.getChildren().addAll(paddle, myBouncer.getView(), pCountLabel.getLabel(), lvlNumLabel.getLabel(), livesCounter.getLabel());

		switch (lvlCounter) {
		case (1):
			Levels.level1(root, standardBlockImage, myBlocks, BLOCK_OFFSET, SIZE, BLOCK_WIDTH, BLOCK_HEIGHT);
			break;
		case (2):
			Levels.level2(root, myBlockImages, myBlocks, BLOCK_OFFSET, SIZE, BLOCK_WIDTH, BLOCK_HEIGHT, paddle);
			break;
		case (3):
			Levels.level3(root, myBlockImages, myBlocks, BLOCK_OFFSET, SIZE, BLOCK_WIDTH, BLOCK_HEIGHT, paddle);
			break;
		}
		
		initICBlockPos();

		myScene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));
		myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
		myScene.setOnKeyReleased(e -> handleKeyRelease(e.getCode()));
		return myScene;
	}
	
	/* setUpSplash constructs the splash screen with basic instructions and visual guides as to what blocks do what.
	 * Full functionality is not revealed to the user through the explanatory messages; 
	 * the user must discover details on their own through gameplay. The splash screen is set to call the startGame method on
	 * pressing space, which actually begins the game.*/
	private Scene setUpSplash(int width, int height, Paint background) {
		initImages();
		initSplashImages();
		initSplashLabels();
		splashRoot = new Group();
		splashWelcome = new LabelMaker(SPLASH_XLOC, SPLASH_YLOC, "Welcome to Breakout! Press space to start.", Color.BLACK, STANDARD_FONT);
		
		splashExplanation = new Text(SPLASH_XLOC, SIZE - 150,
				"Your goal is to destroy all blocks using your ball and paddle, while trying to score as many points as you can. "
				+ "Some blocks will be worth more than others, and your points may reward you unexpectedly. "
				+ "If you're having trouble, test out your C, A, and S "
				+ "keys. Use your left and right arrows to control your paddle, and click to launch the ball. Good luck!");
		splashExplanation.setWrappingWidth(SPLASH_WRAP);
		splashExplanation.setFont(STANDARD_FONT);
		splashExplanation.setFill(Color.BLACK);
		
		splashRoot.getChildren().add(splashExplanation);
		splashRoot.getChildren().addAll(standard, speedUp, speedMinus, blackHole, instantClearRec, creation);
		splashRoot.getChildren().addAll(splashWelcome.getLabel(), standardExplanation.getLabel(),
				speedPlusExplanation.getLabel(), speedMinusExplanation.getLabel(),
				blackHoleExplanation.getLabel(), instantClearExplanation.getLabel(), creationExplanation.getLabel());
		
		splashScene = new Scene(splashRoot, width, height, background);
		splashScene.setOnKeyPressed(e -> handleSplashKeyPress(e.getCode()));
		return splashScene;
	}
	
	/* initICBlockPos adds all the block positions of the current level to two ArrayLists, such that they can be pulled from readily.*/
	private void initICBlockPos() {
		for(int i = 0; i < myBlocks.size(); i++) {
			possibleICBlockX.add(myBlocks.get(i).getX());
		}
		
		for(int i = 0; i < myBlocks.size(); i++) {
			possibleICBlockY.add(myBlocks.get(i).getY());
		}
	}

	/* initImages creates the images for all scene objects.*/
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
	
	/* initSplashImages specifically creates the images that are needed for display in the splash screen.*/
	private void initSplashImages() {
		standard = new Rectangle(SPLASH_XLOC, SPLASH_YLOC + BLOCK_HEIGHT + 2*BLOCK_OFFSET, BLOCK_WIDTH, BLOCK_HEIGHT);
		standard.setFill(new ImagePattern(standardBlockImage));
		
		speedUp = new Rectangle(SPLASH_XLOC, SPLASH_YLOC + 2*BLOCK_HEIGHT + 3*BLOCK_OFFSET, BLOCK_WIDTH, BLOCK_HEIGHT);
		speedUp.setFill(new ImagePattern(speedPlusBlockImage));
		
		speedMinus = new Rectangle(SPLASH_XLOC, SPLASH_YLOC + 3*BLOCK_HEIGHT + 4*BLOCK_OFFSET, BLOCK_WIDTH, BLOCK_HEIGHT);
		speedMinus.setFill(new ImagePattern(speedMinusBlockImage));
		
		blackHole = new Rectangle(SPLASH_XLOC, SPLASH_YLOC + 4*BLOCK_HEIGHT + 5*BLOCK_OFFSET, BLOCK_WIDTH, BLOCK_HEIGHT);
		blackHole.setFill(new ImagePattern(blackHoleBlockImage));
		
		instantClearRec = new Rectangle(SPLASH_XLOC, SPLASH_YLOC + 5*BLOCK_HEIGHT + 6*BLOCK_OFFSET, BLOCK_WIDTH, BLOCK_HEIGHT);
		instantClearRec.setFill(new ImagePattern(instantClearBlockImage));
		
		creation = new Rectangle(SPLASH_XLOC, SPLASH_YLOC + 6*BLOCK_HEIGHT + 7*BLOCK_OFFSET, BLOCK_WIDTH, BLOCK_HEIGHT);
		creation.setFill(new ImagePattern(creationBlockImage));
	}
	
	/* initSplashLabels creates the labels that will display explanations in the splash screen.*/
	private void initSplashLabels(){
		standardExplanation = new LabelMaker(SPLASH_XLOC + BLOCK_WIDTH + BLOCK_OFFSET, SPLASH_YLOC + BLOCK_HEIGHT + 2*BLOCK_OFFSET,
				"This is a standard block. Hit once to destroy.", Color.BLACK, SMALL_FONT);
		speedPlusExplanation = new LabelMaker(SPLASH_XLOC + BLOCK_WIDTH + BLOCK_OFFSET, SPLASH_YLOC + 2*BLOCK_HEIGHT + 3*BLOCK_OFFSET,
				"Hitting this block will speed you up.", Color.BLACK, SMALL_FONT);
		
		speedMinusExplanation = new LabelMaker(SPLASH_XLOC + BLOCK_WIDTH + BLOCK_OFFSET, SPLASH_YLOC + 3*BLOCK_HEIGHT + 4*BLOCK_OFFSET,
				"Hitting this block will slow you down.", Color.BLACK, SMALL_FONT);
		
		blackHoleExplanation = new LabelMaker(SPLASH_XLOC + BLOCK_WIDTH + BLOCK_OFFSET, SPLASH_YLOC + 4*BLOCK_HEIGHT + 5*BLOCK_OFFSET,
				"Don't hit these, they'll swallow you whole!", Color.BLACK, SMALL_FONT);
		
		instantClearExplanation = new LabelMaker(SPLASH_XLOC + BLOCK_WIDTH + BLOCK_OFFSET, SPLASH_YLOC + 5*BLOCK_HEIGHT + 6*BLOCK_OFFSET,
				"The presence of these are the present :)", Color.BLACK, SMALL_FONT);
		
		creationExplanation = new LabelMaker(SPLASH_XLOC + BLOCK_WIDTH + BLOCK_OFFSET, SPLASH_YLOC + 6*BLOCK_HEIGHT + 7*BLOCK_OFFSET,
				"These will leave you with an extra friend for a while!", Color.BLACK, SMALL_FONT);
	}
	
	/* initBlockImages adds all block images to an array of block images.*/
	private void initBlockImages() {
		myBlockImages.add(standardBlockImage);
		myBlockImages.add(speedPlusBlockImage);
		myBlockImages.add(speedMinusBlockImage);
		myBlockImages.add(blackHoleBlockImage);
		myBlockImages.add(creationBlockImage);
		myBlockImages.add(instantClearBlockImage);
	}

	/* step is called per elapsed time, and actually updates gameplay, including labels, positions, and tracking wins/losses. 
	 * A new list iterator is created for the block list every call. A timer is incremented for objects that need to be removed
	 * after a certain time period (e.g., powerup objects that are only supposed to last a few seconds.*/
	private void step(double elapsedTime) {
		timer++;
		myBouncer.moveWithPaddle(paddle);
		myBlocksIterator = myBlocks.listIterator();
		bounce();
		updateLabels();
		for (Bouncer b : myBouncers) {
			b.move(elapsedTime);
		}
		winLose();
	}

	/* bounce controls all collisions of objects in the screen. bounce adds all items that need to be removed from the screen to appropriate
	 * ArrayLists, and similarly for all items that need to be added. For example, instantClearBlocks are removed after a few seconds,
	 * and creationBlocks create new bouncers. bounce also checks if the user has earned enough points for a powerup. Overall, bounce 
	 * directs how each object in the screen bounces off another--bouncer, paddle, and block.*/
	private void bounce() {
		while (myBlocksIterator.hasNext()) {
			Block block = myBlocksIterator.next();
			for(Bouncer b : myBouncers) {
				//collects instantClearBlocks that need to be deleted if the time limit has expired
				if(block instanceof instantClearBlock && timer >= IC_TIME_COUNT) {
					blocksToBeDeleted.add(block);
					timer = 0;
				}
				if (b.getView().getBoundsInParent().intersects(block.getView().getBoundsInParent())) {
					//checks if any "abnormal" behaviors should be performed, i.e., anything that doesn't just affect speed/direction
					abnormalBlockCheck(block, b);
					block.bounceBlock(b);
					root.getChildren().remove(block.getView());
					updatePointCounts(block);
					//checks if the user has earned any power ups
					checkPowerUps();
					myBlocksIterator.remove();
				}
			}
			updateMyBouncers();
		}
		//checks if level should be instantly cleared, and if so, clears level
		checkInstantClear();

		for (Bouncer b : myBouncers) {
			b.bouncePaddle(paddle);
			b.bounceScreen(SIZE, SIZE);
		}
		updateMyBlocks();
	}

	/* checkInstantClear checks if an instant clear block has been hit. If so, the user moves on to the next level.*/
	private void checkInstantClear() {
		if(instantClear) {
			myBouncers.clear();
			myBlocks.clear();
			nextLevel();
			instantClear = false;
		}
	}
	/* updateMyBlocks adds and removes the appropriate blocks from the total arraylist of blocks, using the collect then add/remove
	 * method.*/
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
	
	/* updateMyBouncers adds and removes the appropriate bouncers from the total arraylist of bouncers, using the collect then add/remove
	 * method.*/
	private void updateMyBouncers() {
		for(Bouncer b : bouncersToBeAdded) {
			root.getChildren().add(b.getView());
		}
		for(Bouncer b : bouncersToBeDeleted) {
			root.getChildren().remove(b.getView());
		}
		myBouncers.addAll(bouncersToBeAdded);
		myBouncers.removeAll(bouncersToBeDeleted);
		clearBouncerMods();
	}

	/* checkPowerUps checks if the user has incremented enough points to obtain a powerup, and sets up the powerup if the user has.
	 * If the user has passed the threshold, then the threshold is reset and the powerup is initialized.*/
	private void checkPowerUps() {
		if(paddleThresholdCount >= PADDLE_INC_THRESHOLD && paddle.getWidth() < PADDLE_MAX_LENGTH) {
			paddleThresholdCount = paddleThresholdCount % PADDLE_INC_THRESHOLD;
			paddle.setWidth(paddle.getWidth() + 10);
		}
		if(icThresholdCount >= INSTANT_CLEAR_THRESHOLD) {
			icThresholdCount = icThresholdCount % INSTANT_CLEAR_THRESHOLD;
			Block icBlock = new instantClearBlock(instantClearBlockImage);
			//creates new bouncer at some random x and y position drawn from the starting block configuration
			int rand = generator.nextInt(possibleICBlockX.size());
			icBlock.setLoc(possibleICBlockX.get(rand), possibleICBlockY.get(rand));
			blocksToBeAdded.add(icBlock);
			timer = 0;
		}
		if(bouncerSizeUpCount >= BOUNCER_SIZEUP_THRESHOLD && myBouncer.getSize() < BOUNCER_MAX_SIZE) {
			bouncerSizeUpCount = BOUNCER_SIZEUP_THRESHOLD % bouncerSizeUpCount;
			myBouncer.incSize();
		}
	}

	/* updatePointCounts updates all counters that track points and powerup threshold counts.*/
	private void updatePointCounts(Block block) {
		pointCount += block.getVal();
		paddleThresholdCount += block.getVal();
		bouncerSizeUpCount += block.getVal();
		icThresholdCount += block.getVal();
		totalPoints += block.getVal();
	}

	/* abnormalBlockCheck controls the behavior of the game when a bouncer collides with any block that does not simply
	 * change the bouncer's speed/direction. Note that the blackHoleBlock behavior is differentiated between the original bouncer
	 * and any powerup bouncers obtained from a creationBlock--new bouncers merely die when they hit the blocks, but the true
	 * bouncer will lose lives and restart.*/
	private void abnormalBlockCheck(Block block, Bouncer b) {
		if (block instanceof blackHoleBlock && b == myBouncer) {
			restartBouncer(myBouncer);
			blackHoleBlocks.remove(block);
		} else if(block instanceof blackHoleBlock && b != myBouncer) {
			blackHoleBlocks.remove(block);
			root.getChildren().remove(b.getView());
			bouncersToBeDeleted.add(b);
		}
		//collects any new bouncers that have to be created due to colliding with creationBlock
		else if (block instanceof creationBlock) {
			Bouncer newBounce = myBouncer.creationBouncer(block.getX(), block.getY());
			bouncersToBeAdded.add(newBounce);
		}
		//flags if level should be instantly cleared
		if (block instanceof instantClearBlock) {
			instantClear = true;
		}
	}

	/* passLevel creates the text display after each level before the next.*/
	private Text passLevel(int level) {
		Text levelDisplay = new Text(LEVEL_DISPLAY_X, LEVEL_DISPLAY_Y, "You passed Level " + (level - 1) + "! Click to continue!");
		levelDisplay.setFont(STANDARD_FONT);
		levelDisplay.setFill(Color.BLACK);
		return levelDisplay;
	}
	
	/* defeat creates the screen shown to the user when they lose the game.*/
	private void defeat(int score) {
		Rectangle backDrop = new Rectangle(SIZE, SIZE, Color.CADETBLUE);
		Text defeat = new Text(LEVEL_DISPLAY_X, LEVEL_DISPLAY_Y, "I'm sorry, you lost! Your total score is: " + score);
		defeat.setFont(STANDARD_FONT);
		defeat.setFill(Color.BLACK);
		root.getChildren().addAll(backDrop, defeat);
		myBouncer.stopMoving();
	}
	
	/* victory creates the screen shown to the user when they win the game.*/
	private void victory(int score) {
		Rectangle backDrop = new Rectangle(SIZE, SIZE, Color.CADETBLUE);
		Text victory = new Text(LEVEL_DISPLAY_X - 5, LEVEL_DISPLAY_Y, "Congrats, you win! Your total score is: " + score);
		victory.setFont(STANDARD_FONT);
		victory.setFill(Color.BLACK);
		root.getChildren().addAll(backDrop, victory);
		myBouncer.stopMoving();
	}
	
	/* winLose tracks whether the user has won or not, judging based on bouncer position, the level the user is on, and whether or
	 * not they have cleared all blocks excluding blackHoleBlocks.*/
	private void winLose() {
		ArrayList<Block> needToWin = new ArrayList<>();
		needToWin.addAll(myBlocks);
		needToWin.removeAll(blackHoleBlocks);
		if (needToWin.size() == 0) {
			if (lvlCounter == 3) {
				victory(totalPoints);
			} else {
				myBouncer.stopMoving();
				nextLevel();
			}
		}
		for(Bouncer b : myBouncers) {
			//if not the original bouncer, simply collect bouncer to be deleted if they fall
			if(b != myBouncer && b.getY() > myScene.getHeight()) {
				bouncersToBeDeleted.add(b);
			}
			if (myBouncer.getY() > myScene.getHeight()) {
				if (needToWin.size() != 0 && numLives > 1) {
					restartBouncer(myBouncer);
				} else if (numLives <= 1) {
					defeat(totalPoints);
				}
			}
		}
		updateMyBouncers();
	}

	/* restartBouncer resets the bouncer to the original launching position, and sets it to track the paddle position until launched.
	 * If the user has no more lives to spare, then the defeat screen is automatically displayed*/
	private void restartBouncer(Bouncer myBouncer) {
		if(numLives > 1) {
			numLives--;
			myBouncer.setStayOnPaddle(true);
			myBouncer.setSize(BOUNCER_ORIGINAL_SIZE);
			myBouncer.setYDir(-1);
			myBouncer.setXDir(1);
			paddle.setX(INITIAL_PADDLE_X);
			paddle.setY(INITIAL_PADDLE_Y);
			myBouncer.moveWithPaddle(paddle);
		} else if (numLives == 1) {
			defeat(totalPoints);
		}
	}
	
	/* updateLabels updates the lives, points, and level labels.*/
	private void updateLabels() {
		lvlNumLabel.setText("Level: " + Integer.toString(lvlCounter));
		pCountLabel.setText("Points: " + Integer.toString(pointCount));
		livesCounter.setText("Lives: " + Integer.toString(numLives));
	}

	/* nextLevel sets up the next level from the level the user currently is on, unless the user has won, in which case the victory
	 * screen is displayed.*/
	private void nextLevel() {
		myBouncer.stopMoving();
		root.getChildren().clear();
		myBouncers.clear();
		myBlocks.clear();
		clearBouncerMods();
		clearBlockMods();
		blackHoleBlocks.clear();
		numLives = 3;
		lvlCounter++;
		if (lvlCounter <= 3) {
			totalPoints += pointCount;
			Scene nextScene = setUpGame(SIZE, SIZE, BACKGROUND);
			pointCount = 0;
			s.setScene(nextScene);
			levelUp = passLevel(lvlCounter);
			root.getChildren().addAll(backDrop, levelUp);
			isLevelDisplay = true;
		} else if (lvlCounter > 3) {
			victory(totalPoints);
		}
	}

	/* clearBlockMods clears lists tracking any blocks that have to be added or deleted.*/
	private void clearBlockMods() {
		blocksToBeAdded.clear();
		blocksToBeDeleted.clear();
	}

	/* clearBouncerMods clears lists tracking any bouncers that have to be added or deleted.*/
	private void clearBouncerMods() {
		bouncersToBeAdded.clear();
		bouncersToBeDeleted.clear();
	}

	/* handleKeyPress handles user key input once the game starts--how the paddle moves, and cheat keys.*/
	private void handleKeyPress(KeyCode code) {
		if (paddle.getX() < 0) {
			paddle.setX(SIZE - ORIGINAL_PADDLE_LENGTH);
		} else if (paddle.getX() > SIZE) {
			paddle.setX(0);
		} else if (code == KeyCode.RIGHT) {
			paddle.setX(paddle.getX() + KEY_INPUT_SPEED);
		} else if (code == KeyCode.LEFT) {
			paddle.setX(paddle.getX() - KEY_INPUT_SPEED);
		} else if (code == KeyCode.C && isLevelDisplay == false) {
			nextLevel();
		} else if (code == KeyCode.S) {
			myBouncer.slow();
		} else if (code == KeyCode.A) {
			myBouncer.accelerate();
		}
	}
	
	/* handleSplashKeyPress handles user key input on the splash screen--when space is pressed the actual game begins.*/
	private void handleSplashKeyPress(KeyCode code) {
		if(code == KeyCode.SPACE) {
			startGame(s);
		}
	}
	
	/* handleKeyRelease handles when the cheat code s is released, returning bouncer speed to what it was previously.*/
	private void handleKeyRelease(KeyCode code) {
		if(code == KeyCode.S) {
			myBouncer.returnSpeed();
		}
	}

	/* handleMouseInput handles the user's mouse input during the game, removing intermediary level screens and launching the bouncer.*/
	private void handleMouseInput(double x, double y) {
		if (x > 0 && x < SIZE && y > 0 && y < SIZE) {
			if(myBouncer.isSetOnPaddle() && isLevelDisplay == false) {
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