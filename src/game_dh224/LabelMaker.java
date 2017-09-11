package game_dh224;

/* Written by Diane Hu. This class creates text labels, dependent on the JavaFX library.
 * It was written to allow for easy label creation in one line, rather than creating a label
 * then having to set text, fill, etc., subsequently.*/

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LabelMaker {
	
	private Label myLabel;
	
	/* The LabelMaker constructor takes an x position, y position, string of text,
	 * color fill, and font, and creates a text label. */
	public LabelMaker(double x, double y, String text, Color fill, Font font) {
		myLabel = new Label(text);
		myLabel.setLayoutX(x);
		myLabel.setLayoutY(y);
		myLabel.setTextFill(fill);
		myLabel.setFont(font);
	}
	
	/* getLabel returns the node of the label. */
	public Node getLabel() {
		return myLabel;
	}
	
	/* setText allows the user to set the text of a label outside of the constructor, meaning
	 * a user can easily update a label.*/
	public void setText(String string) {
		myLabel.setText(string);
	}
}