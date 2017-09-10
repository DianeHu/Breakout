package game_dh224;

import javafx.scene.image.Image;

public class speedMinusBlock extends Block{
	
	public speedMinusBlock(Image image) {
		super(image);
	}
	
	@Override
	public void bounceBlock (Bouncer myBouncer) {
		if((myBouncer.getX() + myBouncer.getSize()/2) > this.getX() && myBouncer.getX() < (this.getX() + BLOCK_WIDTH) && 
				myBouncer.getView().getBoundsInParent().intersects(this.getView().getBoundsInParent())) {
			myBouncer.yDirection *= -.5;
		} else if ((myBouncer.getY() + myBouncer.getSize()/2) > this.getY() && myBouncer.getY() < (this.getY() + BLOCK_HEIGHT) && 
				myBouncer.getView().getBoundsInParent().intersects(this.getView().getBoundsInParent())) {
			myBouncer.xDirection *= -.5;
		}
	}
}

