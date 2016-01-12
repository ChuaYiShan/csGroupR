package application.components;

import application.Component;

public class SwitchComponent extends Component {
	
	private String componentName;
	private boolean on;
	
	public SwitchComponent() {
		this.on = false;
	}
	
	public void setName(String name) {
		componentName = name;
		super.title_bar.setText(name);
	}
	
	public String getName(String name) {
		return componentName;
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}
}
