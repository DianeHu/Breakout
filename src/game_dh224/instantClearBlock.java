package game_dh224;

/* Written by Diane Hu. speedPlusBlock is an extension class of Block that overrides
 * certain methods in Block to create a new type of block that behaves differently from the standard
 * block. Dependent on the Block class, and JavaFX libraries. Note that no bounceBlock methods are
 * overridden in this method, since an instantClearBlock clears the level immediately, bounce or no bounce.
 * The classic bounce behavior of the superclass is sufficient, since the behavior will never be called
 * anyways.*/

import javafx.scene.image.Image;

public class instantClearBlock extends Block{
	
	public instantClearBlock(Image image) {
		super(image);
	}
	
	/* getVal overrides the getVal method of superclass Block to return 2, meaning a user
	 * should gain 3 points from hitting a speedPlusBlock, instead of the standard 1.*/
	@Override
	public int getVal() {
		return 3;
	}
}