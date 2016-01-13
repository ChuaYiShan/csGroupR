package application.components;

import application.Component;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class ButtonComponent extends Component {
	
	private String componentName;
	private boolean on;
	
	public ButtonComponent() {
		this.on = false;
		
		this.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
		    @Override
		    public void handle(MouseEvent mouseEvent)
		    {            
		        if(mouseEvent.getClickCount() == 2)
		        {
		    	
		    		getStyleClass().clear();
		    		getStyleClass().add("dragicon");
		    		
		    		if (on) {
		    			getStyleClass().add("icon-button");
		    		} else {
		    			getStyleClass().add("icon-buttonon");
		    		}
		    		
		    		on = !on;
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

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}
}
