package application.components;

import application.Component;

public class AmmeterComponent extends Component {
	
	private String componentName;
	private double current;
	
	public AmmeterComponent() {
		this.current = 0.0;
	}
	
	public void setName(String name) {
		componentName = name;
		super.title_bar.setText(name);
	}
	
	public String getName(String name) {
		return componentName;
	}

	public double getCurrent() {
		return current;
	}

	public void setCurrent(double current) {
		this.current = current;
	}
}
