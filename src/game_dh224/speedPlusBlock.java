package game_dh224;

/* Written by Diane Hu. speedPlusBlock is an extension class of Block that overrides
 * certain methods in Block to create a new type of block that behaves differently from the standard
 * block. Dependent on the Block class, and JavaFX libraries.*/

import javafx.scene.image.Image;

public class speedPlusBlock extends Block{
	
	public speedPlusBlock(Image image) {
		super(image);
	}
	
	/* getVal overrides the getVal method of superclass Block to return 2, meaning a user
	 * should gain 2 points from hitting a speedPlusBlock, instead of the standard 1.*/
	@Override
	public int getVal() {
		return 2;
	}
	
	/* bounceBlock overrides the superclass bounceBlock to not only bounce a bouncer when
	 * it collides with a speedPlusBlock, but also to increase its speed by a magnitude of 1.3*/
	@Override
	public void bounceBlock (Bouncer myBouncer) {
		if(isCollidingTopBottom(myBouncer)) {
			myBouncer.toggleYDir(-1.3);
		} else if (isCollidingOnSides(myBouncer)) {
			myBouncer.toggleXDir(-1.3);
		}
	}
}