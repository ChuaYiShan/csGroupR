package application;

import application.components.AmmeterComponent;
import application.components.BatteryComponent;
import application.components.ButtonComponent;
import application.components.LEDComponent;
import application.components.RelayComponent;
import application.components.ResistorComponent;
import application.components.SwitchComponent;
import application.components.VoltmeterComponent;

public class CircuitElement {
	
	String id;
	Double xCoord;
	Double yCoord;
	String type;
	private ComponentType aType;

	public CircuitElement(){
	}

	public void setId (String id){
		this.id = id;
	}

	public void setxCoord (String xCoord){
		this.xCoord = Double.parseDouble(xCoord);
	}

	public void setyCoord (String yCoord){
		this.yCoord = Double.parseDouble(yCoord);
	}

	public void setType (String type){
		this.type = type;
	}

	public ComponentType getType ()
	{
			if (type.compareTo("rO0ABX5yABlhcHBsaWNhdGlvbi5Db21wb25lbnRUeXBlAAAAAAAAAAASAAB4cgAOamF2YS5sYW5nLkVudW0AAAAAAAAAABIAAHhwdAAHQmF0dGVyeQ==")==0) {
			this.aType = ComponentType.Battery;
		}
		else if (type.compareTo("rO0ABX5yABlhcHBsaWNhdGlvbi5Db21wb25lbnRUeXBlAAAAAAAAAAASAAB4cgAOamF2YS5sYW5nLkVudW0AAAAAAAAAABIAAHhwdAAGQnV0dG9u")==0) {
			this.aType = ComponentType.Button;
		}
		else if (type.compareTo("rO0ABX5yABlhcHBsaWNhdGlvbi5Db21wb25lbnRUeXBlAAAAAAAAAAASAAB4cgAOamF2YS5sYW5nLkVudW0AAAAAAAAAABIAAHhwdAADTEVE")==0){
			this.aType = ComponentType.LED;
		}
		else if (type.compareTo("rO0ABX5yABlhcHBsaWNhdGlvbi5Db21wb25lbnRUeXBlAAAAAAAAAAASAAB4cgAOamF2YS5sYW5nLkVudW0AAAAAAAAAABIAAHhwdAAFUmVsYXk=")==0){
			this.aType= ComponentType.Relay;
		}
		else if (type.compareTo("rO0ABX5yABlhcHBsaWNhdGlvbi5Db21wb25lbnRUeXBlAAAAAAAAAAASAAB4cgAOamF2YS5sYW5nLkVudW0AAAAAAAAAABIAAHhwdAAIUmVzaXN0b3I=")==0){
			this.aType= ComponentType.Resistor;
		}
		else if (type.compareTo("rO0ABX5yABlhcHBsaWNhdGlvbi5Db21wb25lbnRUeXBlAAAAAAAAAAASAAB4cgAOamF2YS5sYW5nLkVudW0AAAAAAAAAABIAAHhwdAAGU3dpdGNo")==0){
			this.aType= ComponentType.Switch;
		}
		else if (type.compareTo("rO0ABX5yABlhcHBsaWNhdGlvbi5Db21wb25lbnRUeXBlAAAAAAAAAAASAAB4cgAOamF2YS5sYW5nLkVudW0AAAAAAAAAABIAAHhwdAAHQW1tZXRlcg==")==0){
			this.aType= ComponentType.Ammeter;
		}
		else if (type.compareTo("rO0ABX5yABlhcHBsaWNhdGlvbi5Db21wb25lbnRUeXBlAAAAAAAAAAASAAB4cgAOamF2YS5sYW5nLkVudW0AAAAAAAAAABIAAHhwdAAJVm9sdG1ldGVy")==0){
			this.aType= ComponentType.Voltmeter;
		}
		else {
			this.aType = null;
		}
		 	return aType;
	}
	
	
	
}
