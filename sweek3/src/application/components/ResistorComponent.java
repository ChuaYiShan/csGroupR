package application.components;

import application.Component;

public class ResistorComponent extends Component {
	
	private String componentName;
	private double resistance;
	
	public ResistorComponent() {
		this.resistance = 0.0;
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
}
