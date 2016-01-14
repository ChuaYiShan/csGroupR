package application.filemanagement;

import application.model.ComponentType;

public class CircuitElement {

	private String id;
	private double xCoord, yCoord;
	private String type;
	private String source, target;
	private ComponentType cType;

	public CircuitElement(){}

	public void setId (String id){
		this.id = id;
	}

	public String getId(){
		return this.id;
	}

	public void setxCoord (String xCoord) {
		this.xCoord = Double.parseDouble(xCoord);
	}

	public void setyCoord(String yCoord) {
		this.yCoord = Double.parseDouble(yCoord);
	}

	public double getxCoord() {
		return xCoord;
	}

	public double getyCoord() {
		return yCoord;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return source;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTarget() {
		return target;
	}

}