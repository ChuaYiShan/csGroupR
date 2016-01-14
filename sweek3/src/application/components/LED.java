package application.components;

import application.model.Component;
import application.model.ComponentType;

public class LED extends Component {

	private boolean on;

	public LED() {
		this.on = false;
	}

	public LED(String id, double xVal, double yVal, ComponentType type) {
		super( id,  xVal,  yVal,  type);
		this.on = false;
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}
}