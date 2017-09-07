package game_dh224;

import javafx.scene.image.Image;

public class speedMinusBlock extends Block{
	
	public speedMinusBlock(Image image, int screenWidth, int screenHeight) {
		super(image, screenWidth, screenHeight);
	}
	
	@Override
	public int getVal() {
		return 2;
	}
	
	@Override
	public void bounceBlock (Bouncer myBouncer) {
		if(myBouncer.getView().getBoundsInParent().intersects(this.getView().getBoundsInParent())) {
			myBouncer.yDirection *= -.5;
		}
	}
}

