package application.components;

import java.util.Optional;

import application.Component;
import application.ComponentType;
import javafx.event.EventHandler;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;

public class ResistorComponent extends Component {
	
	private String componentName;
	private double resistance;
	private double voltage;
	
	public ResistorComponent() {
		this.resistance = 100.0;
		this.voltage = 0.0;
		this.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
		    @Override
		    public void handle(MouseEvent mouseEvent)
		    {            
		        if(mouseEvent.getClickCount() == 2)
		        {
		    		
		            System.out.println("Clicked");
		            TextInputDialog dialog = new TextInputDialog(String.valueOf(getResistance())); 
		            dialog.setTitle("Edit Resistor Value");
		            dialog.setHeaderText("Change the resistance of the resistor.");
		            dialog.setContentText("Please enter the new value:");

		            // Traditional way to get the response value.
		            Optional<String> result = dialog.showAndWait();
		            if (result.isPresent()){
		            	setResistance(Double.parseDouble(result.get()));
		                System.out.println("Resistance has been changed to " + result.get());
		            }

		        }
		    }
		});
	}
	
	public ResistorComponent(String id, double xVal, double yVal, ComponentType type) {
		super( id,  xVal,  yVal,  type);
		
		this.resistance = 100.0;
		this.voltage = 0.0;
		this.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
		    @Override
		    public void handle(MouseEvent mouseEvent)
		    {            
		        if(mouseEvent.getClickCount() == 2)
		        {
		            TextInputDialog dialog = new TextInputDialog(String.valueOf(getResistance())); 
		            dialog.setTitle("Edit Resistor Value");
		            dialog.setHeaderText("Change the resistance of the resistor.");
		            dialog.setContentText("Please enter the new value:");

		            // Traditional way to get the response value.
		            Optional<String> result = dialog.showAndWait();
		            if (result.isPresent()){
		            	setResistance(Double.parseDouble(result.get()));
		                System.out.println("Resistance has been changed to " + result.get());
		            }

		        }
		    }
		});
	}
	
	public void setName(String name) {
		componentName = name;
		super.title_bar.setText(name);
	}
	
	public String getName(String name) {
		return componentName;
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
