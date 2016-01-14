package application.components;

import java.util.ArrayList;

import application.Component;
import application.ComponentType;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class VoltmeterComponent extends Component {
	
	private String componentName;
	private VoltmeterComponent self;
	
	public VoltmeterComponent() {
		self = this;
		addButtonHandler();	
	}
	
	public VoltmeterComponent(String id, double xVal, double yVal, ComponentType type) {
		super( id,  xVal,  yVal,  type);
		self = this;
		addButtonHandler();	
	}
	
	public void addButtonHandler(){
		
		this.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
		    @Override
		    public void handle(MouseEvent mouseEvent)
		    {            
		        if(mouseEvent.getClickCount() == 2)
		        {
		        	getVoltage();
		        }
		    }
		});
		
	}
	
	public void getVoltage() {
    	ArrayList<Component> connected = new ArrayList<Component>();
    	
    	for (Component c : self.getConnectedComponentList()){
    		if (!connected.contains(c)){
    			connected.add(c);
    		}
    	}
    	
    	
    	double voltage = 0.0;
    	for(Component c : connected) {
    		if (c instanceof ResistorComponent) {
    			voltage += ((ResistorComponent) c).getVoltage();
    		}
    	}
		
        System.out.println(voltage);
	}
	
	public void setName(String name) {
		componentName = name;
		super.title_bar.setText(name);
	}
	
	public String getName(String name) {
		return componentName;
	}

}
