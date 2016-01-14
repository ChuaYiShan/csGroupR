package application.components;

import java.util.ArrayList;

import application.model.Component;
import application.model.ComponentType;
import application.util.Output;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Voltmeter extends Component {

	private Voltmeter self;
	
	public Voltmeter() {
		self = this;
		addButtonHandler();	
	}
	
	public Voltmeter(String id, double xVal, double yVal, ComponentType type) {
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
		        	Output.getInstance().printOutput("Voltage Measured: " + getVoltage() + " volts");
		        }
		    }
		});
	}
	
	public double getVoltage() {
    	ArrayList<Component> connected = new ArrayList<Component>();	
    	for (Component c : self.getConnectedComponentList()){
    		if (!connected.contains(c)){
    			connected.add(c);
    		}
    	}
    	double voltage = 0.0;
    	for(Component c : connected) {
    		if (c instanceof Resistor) {
    			voltage += ((Resistor) c).getVoltage();
    		}
    	}
    	return voltage;
	}

}