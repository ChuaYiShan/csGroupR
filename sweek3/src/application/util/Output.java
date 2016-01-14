package application.util;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Output {

	private static Output instance = null;
	@FXML static Text output;

	public Output(Text output) {
		Output.output = output;
		output.setFill(Color.BLACK);
		instance = this;
	}
	public static Output getInstance() {
		return instance;
	}

	public void printOutput(String message) {
		output.setText(message);
	}

	public void clearOutput() {
		output.setText("");
	}
}