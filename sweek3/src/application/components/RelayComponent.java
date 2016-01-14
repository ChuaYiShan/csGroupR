package application.components;

import application.Component;
import application.ComponentType;

public class RelayComponent extends Component {
	
	private String componentName;
	private double voltage;
	
	public RelayComponent() {
		this.voltage = 0.0;
	}
	
	public RelayComponent(String id, double xVal, double yVal, ComponentType type) {
		super( id,  xVal,  yVal,  type);
		this.voltage = 0.0;
	}
	
	public void setName(String name) {
		componentName = name;
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
