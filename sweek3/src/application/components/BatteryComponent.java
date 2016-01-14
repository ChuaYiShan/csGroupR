package application.components;

import java.util.Optional;

import application.Component;
import javafx.event.EventHandler;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;

public class BatteryComponent extends Component {
	
	private String componentName;
	private double voltage;
	
	public BatteryComponent() {
		this.voltage = 9.0;		
		this.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
		    @Override
		    public void handle(MouseEvent mouseEvent)
		    {            
		        if(mouseEvent.getClickCount() == 2)
		        {
		            System.out.println("Clicked");
		            TextInputDialog dialog = new TextInputDialog(String.valueOf(getVoltage())); //this will be different for every component 
		            dialog.setTitle("Edit Battery Voltage");
		            dialog.setHeaderText("Change the voltage of the battery.");
		            dialog.setContentText("Please enter the new value:");

		            Optional<String> result = dialog.showAndWait();
		            if (result.isPresent()){
		            	setVoltage(Double.parseDouble(result.get()));
		                System.out.println("Voltage of battery has been changed to " + result.get());
		                //replace old value with new value 
		            }

		        }
		    }
		});
	}
	
	public void setName(String name) {
		this.componentName = name;
		super.title_bar.setText(name);
	}
	
	public String getName(String name) {
		return componentName;
	}

	public double getVoltage() {
		return voltage;
	}

	public void setVoltage(double voltage) {
		this.voltage = voltage;
	}
	
	
}
