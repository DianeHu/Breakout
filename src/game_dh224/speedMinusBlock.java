package game_dh224;

/* Written by Diane Hu. speedMinusBlock is an extension class of Block that overrides
 * certain methods in Block to create a new type of block that behaves differently from the standard
 * block. Dependent on the Block class, and JavaFX libraries.*/

import javafx.scene.image.Image;

public class speedMinusBlock extends Block{
	
	public speedMinusBlock(Image image) {
		super(image);
	}
	
	/* bounceBlock overrides the superclass bounceBlock to not only bounce the bouncer
	 * when it hits the block, but also to slow it by a magnitude of .7*/
	@Override
	public void bounceBlock (Bouncer myBouncer) {
		if(isCollidingTopBottom(myBouncer)) {
			myBouncer.toggleYDir(-.7);
		} else if (isCollidingOnSides(myBouncer)) {
			myBouncer.toggleXDir(-.7);
		}
	}
}