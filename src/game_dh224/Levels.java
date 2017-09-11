package game_dh224;

/* Written by Diane Hu. Levels is a class that relies on java.util and JavaFX libraries. Class Levels
 * basically contains helper methods for the main Game class to set up block configurations for every level of the
 * game. The methods in this class are public and static because of the necessity of referencing in game,
 * and also because for any given game defined in this context, any level can only have one possible block
 * configuration. In other words, there is one configuration per level, and only one.*/

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Levels {
	
	private static final int ORIGINAL_PADDLE_LENGTH = 70;
	
	/* level1 sets up the block configuration for level one, where the level just contains a rows of standard blocks.
	 * The method takes a root to add blacks to, an image to set the block to, an ArrayList to add the blocks too, an
	 * offset amount for the area between blocks, a blockwidth, and a height.*/
	public static void level1(Group root, Image image, ArrayList<Block> myBlocks, int offset, int width, int blockWidth, int blockHeight) {
		for (int i = 1; i < (width / blockWidth - 2); i++) {
			for (int j = 1; j < (width / blockHeight - 15); j++) {
				Block currBlock = new Block(image);
				setBlock(myBlocks, currBlock, root, blockWidth, blockHeight, i, j, offset);
			}
		}
	}
	
	/* level2 sets up the block configuration for level two, where the blocks are in a lattice pattern
	 * with different types of blocks. The method takes the same parameters as level1, except that since now
	 * there are different types of blocks, it takes an ArrayList of images instead. Additionally, at this
	 * level, the paddle length decreases, and therefore the method call asks for a paddle to decrement length
	 * for as well.*/
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
		paddle.setWidth(ORIGINAL_PADDLE_LENGTH - 10);
		paddle.setX(paddle.getX() + 5);
	}
	
	/* level3 sets up the block configuration for level three, where an outside perimeter of blackHoleBlocks
	 * is lined with "safe" standard blocks, and the inside is configured in a different lattice pattern. The method
	 * takes the same parameters as level2.*/
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
		paddle.setWidth(ORIGINAL_PADDLE_LENGTH - 20);
		paddle.setX(paddle.getX() + 5);
	}
	
	/* setBlock takes an ArrayList of blocks and a single block to add to that array, with a given position. The
	 * method also takes a root to add the single block to.*/
	private static void setBlock(ArrayList<Block> myBlocks, Block currBlock, Group root, int blockWidth, int blockHeight, int i, int j, int offset) {
		currBlock.setLoc(blockWidth * i + offset * i, blockHeight * j + offset * j);
		myBlocks.add(currBlock);
		root.getChildren().add(currBlock.getView());
	}
}