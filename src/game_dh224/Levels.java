package game_dh224;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Levels extends Game{
	
	private static int originalPaddleLength = 70;
	
	public static void level1(Group root, Image image, ArrayList<Block> myBlocks, int offset, int width, int blockWidth, int blockHeight) {
		for (int i = 1; i < (width / blockWidth - 2); i++) {
			for (int j = 1; j < (width / blockHeight - 15); j++) {
				Block currBlock = new Block(image);
				setBlock(myBlocks, currBlock, root, blockWidth, blockHeight, i, j, offset);
			}
		}
	}
	
	public static void level2(Group root, ArrayList<Image> myImages, ArrayList<Block> myBlocks, int offset, int width, int blockWidth, int blockHeight,
			Rectangle paddle) {
		for (int i = 1; i < (width / blockWidth - 2); i++) {
			for (int j = 1; j < (width / blockHeight - 10); j++) {
				if(i%2 != 0 && j%2 != 0) {
					Block currBlock = new Block(myImages.get(0));
					setBlock(myBlocks, currBlock, root, blockWidth, blockHeight, i, j, offset);
				} else if (i%2 == 0 && j%4 == 0) {
					Block currBlock = new speedPlusBlock(myImages.get(1));
					setBlock(myBlocks, currBlock, root, blockWidth, blockHeight, i, j, offset);
				} else if (i%2 == 0 && j%2 == 0) {
					Block currBlock = new speedMinusBlock(myImages.get(2));
					setBlock(myBlocks, currBlock, root, blockWidth, blockHeight, i, j, offset);
				}
			}
		}
		paddle.setWidth(originalPaddleLength - 10);
		paddle.setX(paddle.getX() + 5);
	}
	
	public static void level3(Group root, ArrayList<Image> myImages, ArrayList<Block> myBlocks, int offset, int width, int blockWidth, int blockHeight, 
			Rectangle paddle) {
		for (int i = 0; i < (width / blockWidth - 2); i++) {
			for (int j = 0; j < (width / blockHeight - 8); j++) {
				if(((i == 0 || i == width/blockWidth - 3) && j%2 != 0) || (j == 0 && i%2 != 0)) {
					Block currBlock = new blackHoleBlock(myImages.get(3));
					setBlock(myBlocks, currBlock, root, blockWidth, blockHeight, i, j, offset);
				} else if((i == 1 || j == 1 || i == width/blockWidth - 4) && (i!=0 && j!=0)){
					Block currBlock = new Block(myImages.get(0));
					setBlock(myBlocks, currBlock, root, blockWidth, blockHeight, i, j, offset);
				} else if((i%2 == 0 && j%4 == 0) && (i>1 && j>1 && i < width/blockWidth - 4)) {
					Block currBlock = new speedPlusBlock(myImages.get(1));
					setBlock(myBlocks, currBlock, root, blockWidth, blockHeight, i, j, offset);
				} else if(i%2 == 0 && j%2 == 0 && (i>1 && j>1 && i < width/blockWidth - 4)) {
					Block currBlock = new speedMinusBlock(myImages.get(2));
					setBlock(myBlocks, currBlock, root, blockWidth, blockHeight, i, j, offset);
				} else if(i%4 == 0 && j%3!= 0 && (i>1 && j>1 && i < width/blockWidth - 4 && j < width/blockHeight - 9)) {
					Block currBlock = new creationBlock(myImages.get(4));
					setBlock(myBlocks, currBlock, root, blockWidth, blockHeight, i, j, offset);
				}
			}
		}
		paddle.setWidth(originalPaddleLength - 20);
		paddle.setX(paddle.getX() + 5);
	}
	
	public static void setBlock(ArrayList<Block> myBlocks, Block currBlock, Group root, int blockWidth, int blockHeight, int i, int j, int offset) {
		currBlock.setLoc(blockWidth * i + offset * i, blockHeight * j + offset * j);
		myBlocks.add(currBlock);
		root.getChildren().add(currBlock.getView());
	}
}