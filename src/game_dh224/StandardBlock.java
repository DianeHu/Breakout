package game_dh224;

import javafx.scene.image.Image;

public class StandardBlock extends Block{

	public StandardBlock(Image image, int screenWidth, int screenHeight) {
		super(image, screenWidth, screenHeight);
	}
	
	@Override
	public int getVal() {
		return 1;
	}
	
	@Override
	public void bounceBlock (Bouncer myBouncer) {
		if(myBouncer.getView().getBoundsInParent().intersects(this.getView().getBoundsInParent())) {
			myBouncer.yDirection *= -1;
		}
	}
}