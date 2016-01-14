package application;

public class CircuitElement {
	
	private String id;
	
	private double xCoord, yCoord;
	String type;
	
	private String source, target;
	
	private ComponentType aType;

	public CircuitElement(){
	}
	
	public String getId(){
		return this.id;
	}

	public void setId (String id){
		this.id = id;
	}
	
	public double getxCoord() {
		return xCoord;
	}

	public double getyCoord() {
		return yCoord;
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

	public ComponentType getType()
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
	
	public String getStringType() {
		return this.type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
