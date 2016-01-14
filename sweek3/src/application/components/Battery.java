package application.components;

import java.util.Optional;

import application.model.Component;
import application.model.ComponentType;
import javafx.event.EventHandler;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;

public class Battery extends Component {

	private double voltage;

	public Battery() {
		this.voltage = 9.0;		
		addButtonHandler();
	}

	public Battery(String id, double xVal, double yVal, ComponentType type) {
		super( id,  xVal,  yVal,  type);
		this.voltage = 9.0;		
		addButtonHandler();
	}

	public void addButtonHandler(){	
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {            
				if(mouseEvent.getClickCount() == 2) {
					TextInputDialog dialog = new TextInputDialog(String.valueOf(getVoltage()));
					dialog.setTitle("Edit Battery Voltage");
					dialog.setHeaderText("Change the voltage of the battery.");
					dialog.setContentText("Please enter the new value:");

					Optional<String> result = dialog.showAndWait();
					if (result.isPresent()){
						setVoltage(Double.parseDouble(result.get()));
					}
				}
			}
		});
	}

	public double getVoltage() {
		return voltage;
	}

	public void setVoltage(double voltage) {
		this.voltage = voltage;
	}
	
}