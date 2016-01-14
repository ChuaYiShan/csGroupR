package application.components;

import java.util.Optional;

import application.model.Component;
import application.model.ComponentType;
import javafx.event.EventHandler;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;

public class Resistor extends Component {

	private double resistance;
	private double voltage;

	public Resistor() {
		this.resistance = 100.0;
		this.voltage = 0.0;
		addButtonHandler();
	}

	public Resistor(String id, double xVal, double yVal, ComponentType type) {
		super( id,  xVal,  yVal,  type);
		this.resistance = 100.0;
		this.voltage = 0.0;
		addButtonHandler();
	}

	public void addButtonHandler(){	
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {    
				if(mouseEvent.getClickCount() == 2) {
					TextInputDialog dialog = new TextInputDialog(String.valueOf(getResistance())); 
					dialog.setTitle("Edit Resistor Value");
					dialog.setHeaderText("Change the resistance of the resistor.");
					dialog.setContentText("Please enter the new value:");

					Optional<String> result = dialog.showAndWait();
					if (result.isPresent()){
						setResistance(Double.parseDouble(result.get()));
					}
				}
			}
		});
	}

	public double getResistance() {
		return resistance;
	}

	public void setResistance(double resistance) {
		this.resistance = resistance;
	}

	public double getVoltage() {
		return voltage;
	}

	public void setVoltage(double voltage) {
		this.voltage = voltage;
	}
}