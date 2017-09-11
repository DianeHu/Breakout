package game_dh224;

/* Written by Diane Hu. speedPlusBlock is an extension class of Block that overrides
 * certain methods in Block to create a new type of block that behaves differently from the standard
 * block. Dependent on the Block class, and JavaFX libraries. Note that no bounceBlock methods are
 * overridden in this method, since a creationBlock does not bounce any bouncer differently--it only creates a new one.
 * The classic bounce behavior of the superclass is sufficient*/

import javafx.scene.image.Image;

public class creationBlock extends Block{

	public creationBlock(Image image) {
		super(image);
	}
	
	/* getVal overrides the getVal method of superclass Block to return 2, meaning a user
	 * should gain 3 points from hitting a speedPlusBlock, instead of the standard 1.*/
	@Override
	public int getVal() {
		return 3;
	}
}