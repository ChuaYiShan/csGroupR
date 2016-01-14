package application.components;

import application.model.Component;
import application.model.ComponentType;
import application.util.Output;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Ammeter extends Component {

	private double current;	

	public Ammeter() {
		this.current = 0.0;
		addButtonHandler();
	}

	public Ammeter(String id, double xVal, double yVal, ComponentType type) {
		super( id,  xVal,  yVal,  type);
		this.current = 0.0;
		addButtonHandler();	
	}

	public void addButtonHandler(){	
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {            
				if(mouseEvent.getClickCount() == 2) {
					Output.getInstance().printOutput("Current Measured: " + getCurrent() + " ampere");
				}
			}
		});
	}

	public void setCurrent(double circuitCurrent){

		Component currentComponent = this;
		Component next = null;
		if (currentComponent.getTargetComponentList().size() == 1) {
			next = currentComponent.getTargetComponentList().get(0);
		}
		if (currentComponent.getTargetComponentList().size() > 1) {
			if (currentComponent.getSourceComponentList().size() == 1) {
				this.current = circuitCurrent;
				return;
			}
		}

		while (next.getSourceComponentList().size() <= 1){
			// System.out.println(next);
			if (next instanceof Battery) { 
				break; 
			}
			currentComponent = next;
			if (currentComponent instanceof Resistor) {
				// System.out.println(((Resistor) currentComponent).getVoltage());
				this.current = ((Resistor) currentComponent).getVoltage() / 
						((Resistor) currentComponent).getResistance(); 
			}
			if (currentComponent.getTargetComponentList().size() == 1) {
				next = currentComponent.getTargetComponentList().get(0);
			}
		}

		if (next instanceof Battery) { 

			if (currentComponent.getSourceComponentList().size() > 1){
				this.current = circuitCurrent;
			} else {
				Resistor resistor = (Resistor) currentComponent.getSourceComponentList().get(0);
				this.current = resistor.getVoltage() / resistor.getResistance();  
			}

		}

	}
	
	public double getCurrent() {
		return this.current;
	}

}