package application.components;

import application.Component;

public class BatteryComponent extends Component {
	
	private String componentName;
	private double voltage;
	
	public BatteryComponent() {
		this.voltage = 0.0;		
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