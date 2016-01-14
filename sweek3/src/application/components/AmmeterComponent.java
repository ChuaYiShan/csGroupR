package application.components;

import application.Component;
import application.ComponentType;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class AmmeterComponent extends Component {
	
	private String componentName;
	private double current;	
	
	public AmmeterComponent() {
		this.current = 0.0;
		this.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
		    @Override
		    public void handle(MouseEvent mouseEvent)
		    {            
		        if(mouseEvent.getClickCount() == 2)
		        {	
		        	System.out.println(getCurrent());
		        }
		    }
		});
	}
	
	public AmmeterComponent(String id, double xVal, double yVal, ComponentType type) {
		super( id,  xVal,  yVal,  type);
		this.current = 0.0;
		
		this.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
		    @Override
		    public void handle(MouseEvent mouseEvent)
		    {            
		        if(mouseEvent.getClickCount() == 2)
		        {	
		        	System.out.println(getCurrent());
		        }
		    }
		});
		
		
	}
	
	public double getCurrent() {
		return this.current;
	}
	
	public void setCurrent(double circuitCurrent){
		
		Component currentComponent = this;
		Component next = null;
		if (currentComponent.getTargetComponentList().size() == 1) {
			next = currentComponent.getTargetComponentList().get(0);
		}
		if (currentComponent.getTargetComponentList().size() > 1) {
			if (currentComponent.getSourceComponentList().size() == 1) {
				this.current = circuitCurrent;
				return;
			}
		}
		
		while (next.getSourceComponentList().size() <= 1){
			System.out.println(next);
			if (next instanceof BatteryComponent) { 
				break; 
			}
			currentComponent = next;
			if (currentComponent instanceof ResistorComponent) {
				System.out.println(((ResistorComponent) currentComponent).getVoltage());
				this.current = ((ResistorComponent) currentComponent).getVoltage() / 
						((ResistorComponent) currentComponent).getResistance(); 
			}
			if (currentComponent.getTargetComponentList().size() == 1) {
				next = currentComponent.getTargetComponentList().get(0);
			}
		}
		
		if (next instanceof BatteryComponent) { 
			
			if (currentComponent.getSourceComponentList().size() > 1){
				this.current = circuitCurrent;
			} else {
				ResistorComponent resistor = (ResistorComponent) currentComponent.getSourceComponentList().get(0);
				this.current = resistor.getVoltage() / resistor.getResistance();  
			}

		}
		
	}

	
	public void setName(String name) {
		componentName = name;
		super.title_bar.setText(name);
	}
	
	public String getName(String name) {
		return componentName;
	}
}
