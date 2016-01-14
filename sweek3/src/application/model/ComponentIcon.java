package application.model;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;

public class ComponentIcon extends AnchorPane{

	@FXML AnchorPane root_pane;

	private ComponentType type = null;

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


	public void setType (ComponentType type) {

		this.type = type;

		getStyleClass().clear();
		getStyleClass().add("dragicon");

		switch (type) {

		case Battery:
			getStyleClass().add("icon-battery");
			break;

		case Resistor:
			getStyleClass().add("icon-resistor");
			break;

		case Switch:
			getStyleClass().add("icon-switch");
			break;

		case LED:
			getStyleClass().add("icon-led");
			break;

		case Ammeter:
			getStyleClass().add("icon-ammeter");
			break;

		case Voltmeter:
			getStyleClass().add("icon-voltmeter");
			break;

		default:
			break;
		}
	}
	
	public ComponentType getType () { return type; }
}