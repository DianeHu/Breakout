package game_dh224;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class LabelMaker {
	
	private Label myLabel;
	
	public LabelMaker(int x, int y, String text, Color fill) {
		myLabel = new Label(text);
		myLabel.setLayoutX(x);
		myLabel.setLayoutY(y);
		myLabel.setTextFill(fill);
	}
	
	public Node getLabel() {
		return myLabel;
	}

	public void setText(String string) {
		myLabel.setText(string);
	}
}
