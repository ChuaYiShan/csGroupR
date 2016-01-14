package application.components;

import application.model.Component;
import application.model.ComponentType;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Switch extends Component {

	private boolean on;

	public Switch() {	
		this.on = false;	
		addButtonHandler();
	}


	public Switch(String id, double xVal, double yVal, ComponentType type) {
		super( id,  xVal,  yVal,  type);
		this.on = false;
		addButtonHandler();

	}

	public void addButtonHandler(){	
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {    

				if(mouseEvent.getClickCount() == 2) {

					getStyleClass().clear();
					getStyleClass().add("dragicon");

					if (on) {
						getStyleClass().add("icon-switch");
					} else {
						getStyleClass().add("icon-switchon");
					}

					on = !on;
				}
			}
		});
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}

}