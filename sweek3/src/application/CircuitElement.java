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

	public String getType()
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
		 */	
		
		return type;

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
