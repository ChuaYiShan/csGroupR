package application;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Output {
	
	   private static Output instance = null;
	   @FXML static TextFlow output;
	   
	   public Output(TextFlow output) {
		   Output.output = output;
		   instance = this;
	   }
	   public static Output getInstance() {
	      return instance;
	   }
	   
		public void printOutput(String message) {
			Text text = new Text(message);
			text.setFill(Color.BLACK);
			output.getChildren().addAll(text);
		}
		
		public void clearOuput() {
			output.getChildren().remove(0,output.getChildren().size());
		}
	}
