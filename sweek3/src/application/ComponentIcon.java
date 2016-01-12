package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;

public class ComponentIcon extends AnchorPane{
	
	@FXML AnchorPane root_pane;

	private ComponentType mType = null;
	
	public ComponentIcon() {
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ComponentIcon.fxml"));
		
		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);
		
		try { 
			fxmlLoader.load();
        
		} catch (IOException exception) {
		    throw new RuntimeException(exception);
		}
	}
	
	@FXML
	private void initialize() {}
	
	public void relocateToPoint (Point2D p) {

		// relocates the object to a point that has been converted to
		// scene coordinates
		Point2D localCoords = getParent().sceneToLocal(p);
		
		relocate ( 
				(int) (localCoords.getX() - (getBoundsInLocal().getWidth() / 2)),
				(int) (localCoords.getY() - (getBoundsInLocal().getHeight() / 2))
			);
	}
	
	public ComponentType getType () { return mType; }
	
	public void setType (ComponentType type) {
		
		mType = type;
		
		getStyleClass().clear();
		getStyleClass().add("dragicon");
		
		//added because the cubic curve will persist into other icons
		if (this.getChildren().size() > 0)
			getChildren().clear();
		
		switch (mType) {
		
			case Battery:
				getStyleClass().add("icon-blue");
			break;
	
			case Button:
				getStyleClass().add("icon-red");			
			break;
	
			case Relay:
				getStyleClass().add("icon-green");
			break;
	
			case Resistor:
				getStyleClass().add("icon-grey");
			break;
	
			case Switch:
				getStyleClass().add("icon-purple");
			break;
	
			case LED:
				getStyleClass().add("icon-yellow");
			break;
			
			default:
			break;
		}
	}
}
