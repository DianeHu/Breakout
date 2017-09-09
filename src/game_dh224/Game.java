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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
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
	public static final String STANDARD_BLOCK = "Brick1.gif";
	public static final String SPEED_PLUS_BLOCK = "Brick2.gif";
	public static final String SPEED_MINUS_BLOCK = "Brick3.gif";
	public static final String BLACK_HOLE_BLOCK = "Brick4.gif";
	public static final String CREATION_BLOCK = "Brick5.gif";
	public static final int SIZE = 400;
	public static final Paint BACKGROUND = Color.DARKSLATEGREY;
	public static final int FRAMES_PER_SECOND = 60;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	public static final int KEY_INPUT_SPEED = 40;
	public static final double GROWTH_RATE = 1.1;
	//public Scene initialScreen = new Scene(root, SIZE, SIZE, Color.AZURE);
	
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
	private ArrayList<Bouncer> toAddBouncers = new ArrayList<>();
	private Stage s = null;
	private int lvlCounter = 1;
	private int numLives = 3;
	private Image standardBlockImage;
	private Image speedPlusBlockImage;
	private Image speedMinusBlockImage;
	private Image creationBlockImage;
	private Image blackHoleBlockImage;
	private Image bouncerImage;
	private boolean needsSplash = true;

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
		
		root = new Group();
		root.getChildren().clear();
		myScene = new Scene(root, width, height, background);
		
		paddle = new Rectangle(width / 2 - 40, height - 20, 70, 15);
		paddle.setFill(Color.CORNSILK);
		myBouncer = new Bouncer(bouncerImage, width, height);
		myBouncers.add(myBouncer);
		
		root.getChildren().add(paddle);
		root.getChildren().add(myBouncer.getView());
		root.getChildren().add(pCountLabel);
		root.getChildren().add(lvlNumLabel);
		root.getChildren().add(livesCounter);
		
		for (int i = 0; i < 2; i++) {
			Block currBlock = new Block(standardBlockImage, width, height);
			currBlock.setLoc(75*i, 50*i);
			myBlocks.add(currBlock);
			root.getChildren().add(currBlock.getView());
		}
		
		for (int i = 2; i < 3; i++) {
			Block currBlock = new creationBlock(creationBlockImage, width, height);
			currBlock.setLoc(75*i, 50*i);
			myBlocks.add(currBlock);
			root.getChildren().add(currBlock.getView());
		}
		
		for (int i = 3; i < 4; i++) {
			Block currBlock = new speedPlusBlock(speedPlusBlockImage, width, height);
			currBlock.setLoc(75 * i, 50 * i);
			myBlocks.add(currBlock);
			root.getChildren().add(currBlock.getView());
		}
		
		for (int i = 4; i < 5; i++) {
			Block currBlock = new blackHoleBlock(blackHoleBlockImage, width, height);
			currBlock.setLoc(75 * i, 50 * i);
			myBlocks.add(currBlock);
			root.getChildren().add(currBlock.getView());
		}
		
		myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
		return myScene;
	}
	
	public void addBouncer(Bouncer bounce) {
		root.getChildren().add(bounce.getView());
		myBouncers.add(bounce);
	}
	
	private void initImages() {
		bouncerImage = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
		standardBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(STANDARD_BLOCK));
		speedPlusBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(SPEED_PLUS_BLOCK));
		speedMinusBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(SPEED_MINUS_BLOCK));
		blackHoleBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(BLACK_HOLE_BLOCK));
		creationBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(CREATION_BLOCK));
	}
	
	private void initNumLabel() {
		lvlNumLabel.setLayoutX(SIZE - 50);
		lvlNumLabel.setLayoutY(SIZE - 100);
		lvlNumLabel.setText("Level: " + Integer.toString(lvlCounter));
		lvlNumLabel.setTextFill(Color.WHITE);
	}
	
	private void initPointCounter() {
		pCountLabel.setLayoutX(SIZE - 50);
		pCountLabel.setLayoutY(SIZE - 50);
		pCountLabel.setText("Point: " + Integer.toString(pointCount));
		pCountLabel.setTextFill(Color.WHITE);
	}
	
	private void initLivesCounter() {
		livesCounter.setLayoutX(SIZE - 50);
		livesCounter.setLayoutY(SIZE - 150);
		livesCounter.setText("Lives: " + Integer.toString(numLives));
		livesCounter.setTextFill(Color.WHITE);
	}

	private void step(double elapsedTime) {
		myBouncer.moveWithPaddle(myBouncer, paddle);
		myBlocksIterator = myBlocks.listIterator();
		bounce();
		for(Bouncer b : myBouncers) {
			b.move(elapsedTime);
		}
		winLose();
	}
	
	private void bounce() {
		
		while(myBlocksIterator.hasNext()) {
			Block block = myBlocksIterator.next();
			if(myBouncer.getView().getBoundsInParent().intersects(block.getView().getBoundsInParent())) {
				if(block instanceof blackHoleBlock) {
					restartBouncer(myBouncer);
				}
				if(block instanceof creationBlock) {
					addBouncer(myBouncer.creationBouncer(block.getX(), block.getY()));
				}
				block.bounceBlock(myBouncer);
				//root.getChildren().remove(block);
				root.getChildren().remove(block.getView());
				pointCount += block.getVal();
				pCountLabel.setText("Points: " + Integer.toString(pointCount));
				myBlocksIterator.remove();
			}
		}
		
		myBouncer.bouncePaddle(myBouncer, paddle);
		myBouncer.bounceScreen(SIZE, SIZE);
	}
	
	private void winLose() {
		if (myBlocks.size() == 0) {
			if(lvlCounter == 3) {
				Text victory = new Text(myScene.getWidth() / 3 - 100, myScene.getHeight() / 3,
						"Congrats, you win!");
				Font font = new Font(25);
				victory.setFont(font);
				victory.setFill(Color.WHITE);
				root.getChildren().add(victory);
				myBouncer.keepMoving = 0;
			} else {
				myBouncer.keepMoving = 0;
				lvlCounter++;
				nextLevel();
			}
		}
		
		if(myBouncer.getY() > myScene.getHeight()) {
			if(myBlocks.size() != 0 && numLives != 0) {
				restartBouncer(myBouncer);
			} else if(numLives == 0 || lvlCounter == 3){
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
		paddle.setX(SIZE / 2 - 40);
		paddle.setY(SIZE - 20);
		myBouncer.moveWithPaddle(myBouncer, paddle);
		livesCounter.setText("Lives: " + Integer.toString(numLives));
	}
	
	private void nextLevel() {
		myBlocks.clear();
		numLives = 3;
		if(lvlCounter <= 3) {
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
	}
	
	private void handleMouseInput (double x, double y) {
        if (x > 0 && x < SIZE && y > 0 && y < SIZE) {
        	myBouncer.stayOnPaddle = false;
            myBouncer.launch(x, y);
        }
    }

	public static void main(String[] args) {
		launch(args);
	}
}
