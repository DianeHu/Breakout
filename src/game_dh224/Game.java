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
	private ListIterator<Block> temp = null;
	private ArrayList<Block> myBlocks = new ArrayList<>();
	private ArrayList<Boolean> blockChecks = new ArrayList<>();
	private ArrayList<speedPlusBlock> mySPBlocks = new ArrayList<>();
	private ArrayList<blackHoleBlock> myBHBlocks = new ArrayList<>();
	private Stage s;
	private int lvlCounter = 1;
	private int numLives = 3;
	private Image standardBlockImage;
	private Image speedPlusBlockImage;
	private Image speedMinusBlockImage;
	private Image bouncerImage;
	private boolean needsSplash = true;


	@Override
	public void start(Stage s) {
		this.s = s;
		/*if(needsSplash) {
			splashScreen(s);
		}*/
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
	
	/*public void splashScreen(Stage s) {
		this.s = s;
		s.setScene(initialScreen);
	}*/

	protected Scene setUpGame(int width, int height, Paint background) {
		initNumLabel();
		initImages();
		initPointCounter();
		initLivesCounter();
		
		root = new Group();
		myScene = new Scene(root, width, height, background);
		
		paddle = new Rectangle(width / 2 - 40, height - 20, 70, 15);
		paddle.setFill(Color.CORNSILK);
		myBouncer = new Bouncer(bouncerImage, width, height);
		
		root.getChildren().add(paddle);
		root.getChildren().add(myBouncer.getView());
		root.getChildren().add(pCountLabel);
		root.getChildren().add(lvlNumLabel);
		root.getChildren().add(livesCounter);
		
		for (int i = 0; i < 3; i++) {
			Block currBlock = new StandardBlock(standardBlockImage, width, height);
			currBlock.setLoc(75 * i, 50 * i);
			myBlocks.add(currBlock);
			blockChecks.add(i, true);
			root.getChildren().add(currBlock.getView());
		}
		
		for (int i = 3; i < 5; i++) {
			Block currBlock = new speedPlusBlock(speedPlusBlockImage, width, height);
			currBlock.setLoc(75 * i, 50 * i);
			myBlocks.add(currBlock);
			blockChecks.add(i, true);
			root.getChildren().add(currBlock.getView());
		}
		
		/*for (int i = 3; i < 4; i++) {
			speedPlusBlock currBlock = new speedPlusBlock(speedPlusBlockImage, width, height);
			currBlock.setLoc(75 * i, 50 * i);
			myBlocks.add(currBlock);
			root.getChildren().add(currBlock.getView());
		}
		
		for (int i = 4; i < 5; i++) {
			speedMinusBlock currBlock = new speedMinusBlock(speedMinusBlockImage, width, height);
			currBlock.setLoc(75 * i, 50 * i);
			myBlocks.add(currBlock);
			root.getChildren().add(currBlock.getView());
		}*/
		
		myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
		return myScene;
	}
	
	public void initImages() {
		bouncerImage = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
		standardBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(STANDARD_BLOCK));
		speedPlusBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(SPEED_PLUS_BLOCK));
		speedMinusBlockImage = new Image(getClass().getClassLoader().getResourceAsStream(SPEED_MINUS_BLOCK));
	}
	
	public void initNumLabel() {
		lvlNumLabel.setLayoutX(SIZE - 50);
		lvlNumLabel.setLayoutY(SIZE - 100);
		lvlNumLabel.setText("Level: " + Integer.toString(lvlCounter));
		lvlNumLabel.setTextFill(Color.WHITE);
	}
	
	public void initPointCounter() {
		pCountLabel.setLayoutX(SIZE - 50);
		pCountLabel.setLayoutY(SIZE - 50);
		pCountLabel.setText("Point: " + Integer.toString(pointCount));
		pCountLabel.setTextFill(Color.WHITE);
	}
	
	public void initLivesCounter() {
		livesCounter.setLayoutX(SIZE - 50);
		livesCounter.setLayoutY(SIZE - 150);
		livesCounter.setText("Lives: " + Integer.toString(numLives));
		livesCounter.setTextFill(Color.WHITE);
	}

	public void step(double elapsedTime) {
		myBouncer.moveWithPaddle(myBouncer, paddle);
		temp = myBlocks.listIterator();
		bounce();
		myBouncer.move(elapsedTime);
		winLose();
	}
	
	private void bounce() {
		
		while(temp.hasNext()) {
			Block block = temp.next();
			if(myBouncer.getView().getBoundsInParent().intersects(block.getView().getBoundsInParent())) {
				block.bounceBlock(myBouncer);
				root.getChildren().remove(block.getView());
				pointCount += block.getVal();
				pCountLabel.setText("Points: " + Integer.toString(pointCount));
				temp.remove();
			}
		}
		
		/*for(int i = 0; i < myBlocks.size(); i++) {
			if (myBouncer.getView().getBoundsInParent().intersects(myBlocks.get(i).getView().getBoundsInParent()) && blockChecks.get(i)) {
				myBlocks.get(i).bounceBlock(myBouncer);
				blockChecks.set(i, false);
				root.getChildren().remove(myBlocks.get(i).getView());
				if(myBlocks.get(i) instanceof StandardBlock) {
					System.out.println("Collide");
					myBouncer.bounceStandardBlock(myBouncer, (StandardBlock) myBlocks.get(i)); 
					pointCount += 1;
					pCountLabel.setText("Points: " + Integer.toString(pointCount));
				}
				else if(myBlocks.get(i) instanceof speedPlusBlock) {
					System.out.println("Collide");
					myBouncer.bounceSpeedPlusBlock(myBouncer, (speedPlusBlock) myBlocks.get(i)); 
					pointCount += 1;
					pCountLabel.setText("Points: " + Integer.toString(pointCount));
				}
				else if(myBlocks.get(i) instanceof speedMinusBlock) {
					System.out.println("Collide");
					myBouncer.bounceSpeedMinusBlock(myBouncer, (speedMinusBlock) myBlocks.get(i)); 
					pointCount += 1;
					pCountLabel.setText("Points: " + Integer.toString(pointCount));
				}
				pointCount += myBlocks.get(i).getVal();
				pCountLabel.setText("Points: " + Integer.toString(pointCount));
			}
			/*if(pointCount % 3 == 1) {
				paddle.setWidth(paddle.getWidth() + 10);
			}*/
		
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
			lvlCounter++;
			nextLevel();
		}

		/*if(myBouncer.getY() > myScene.getHeight() - paddle.getHeight() && numLives != 0) {
			restartBouncer();
		}*/
		
		if (myBouncer.getY() > myScene.getHeight() - paddle.getHeight()) {
			Text defeat = new Text(myScene.getWidth() / 3 - 100, myScene.getHeight() / 3, "I'm sorry, you lost!");
			Font font = new Font(25);
			defeat.setFont(font);
			defeat.setFill(Color.WHITE);
			root.getChildren().add(defeat);
			myBouncer.keepMoving = 0;
			lvlCounter++;
			nextLevel();
		}
	}
	
	private void restartBouncer() {
		paddle.setX(SIZE / 2 - 40);
		paddle.setY(SIZE - 20);
		myBouncer.stayOnPaddle = true;
		myBouncer.moveWithPaddle(myBouncer, paddle);
		numLives--;
		livesCounter.setText("Lives: " + Integer.toString(numLives));
	}
	
	private void nextLevel() {
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
		if(x > 0 && x < SIZE && y > 0 && y < SIZE && needsSplash) {
			needsSplash = false;
			start(s);
		}
        if (x > 0 && x < SIZE && y > 0 && y < SIZE && !needsSplash) {
        	myBouncer.stayOnPaddle = false;
            myBouncer.launch(x - myBouncer.getX(), y - myBouncer.getY());
        }
    }

	public static void main(String[] args) {
		launch(args);
	}
}
