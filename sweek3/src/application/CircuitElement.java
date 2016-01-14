package application;

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

		/*	if (type.compareTo("rO0ABX5yABhhcHBsaWNhdGlvbi5EcmFnSWNvblR5cGUAAAAAAAAAABIAAHhyAA5qYXZhLmxhbmcuRW51bQAAAAAAAAAAEgAAeHB0AANyZWQ=")==0) {
			this.aType = ComponentType.red;
		}
		else if (type.compareTo("rO0ABX5yABhhcHBsaWNhdGlvbi5EcmFnSWNvblR5cGUAAAAAAAAAABIAAHhyAA5qYXZhLmxhbmcuRW51bQAAAAAAAAAAEgAAeHB0AAVncmVlbg==")==0) {
			this.aType = ComponentType.green;
		}
		else if (type.compareTo("rO0ABX5yABhhcHBsaWNhdGlvbi5EcmFnSWNvblR5cGUAAAAAAAAAABIAAHhyAA5qYXZhLmxhbmcuRW51bQAAAAAAAAAAEgAAeHB0AARibHVl")==0){
			this.aType = ComponentType.blue;
		}
		else if (type.compareTo("rO0ABX5yABhhcHBsaWNhdGlvbi5EcmFnSWNvblR5cGUAAAAAAAAAABIAAHhyAA5qYXZhLmxhbmcuRW51bQAAAAAAAAAAEgAAeHB0AAVibGFjaw==")==0){
			this.aType= ComponentType.black;
		}
		else if (type.compareTo("rO0ABX5yABhhcHBsaWNhdGlvbi5EcmFnSWNvblR5cGUAAAAAAAAAABIAAHhyAA5qYXZhLmxhbmcuRW51bQAAAAAAAAAAEgAAeHB0AAZ5ZWxsb3c=")==0){
			this.aType= ComponentType.yellow;
		}
		else if (type.compareTo("rO0ABX5yABhhcHBsaWNhdGlvbi5EcmFnSWNvblR5cGUAAAAAAAAAABIAAHhyAA5qYXZhLmxhbmcuRW51bQAAAAAAAAAAEgAAeHB0AAZwdXJwbGU=")==0){
			this.aType= ComponentType.purple;
		}
		else if (type.compareTo("rO0ABX5yABhhcHBsaWNhdGlvbi5EcmFnSWNvblR5cGUAAAAAAAAAABIAAHhyAA5qYXZhLmxhbmcuRW51bQAAAAAAAAAAEgAAeHB0AARncmV5")==0){
			this.aType= ComponentType.grey;
		}
		else {
			this.aType = null;
		}
		 */	return aType;

	}
}
