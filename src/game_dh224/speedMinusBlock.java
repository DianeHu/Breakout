package game_dh224;

import javafx.scene.image.Image;

public class speedMinusBlock extends Block{
	
	public speedMinusBlock(Image image) {
		super(image);
	}
	
	@Override
	public int getVal() {
		return 2;
	}
	
	@Override
	public void bounceBlock (Bouncer myBouncer) {
		if((myBouncer.getX() + BOUNCER_SIZE / 2) >= this.getX() && myBouncer.getX() <= (this.getX() + BLOCK_WIDTH) && 
				myBouncer.getView().getBoundsInParent().intersects(this.getView().getBoundsInParent())) {
			myBouncer.yDirection *= -.5;
		} else if ((myBouncer.getY() + BOUNCER_SIZE / 2) >= this.getY() && myBouncer.getY() <= (this.getY() + BLOCK_HEIGHT) && 
				myBouncer.getView().getBoundsInParent().intersects(this.getView().getBoundsInParent())) {
			myBouncer.xDirection *= -.5;
		}
	}
}

