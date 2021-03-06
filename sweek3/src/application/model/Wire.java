package application.model;

import java.io.IOException;
import java.util.UUID;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

public class Wire extends AnchorPane {

	@FXML Line start_line;
	@FXML Line end_line;
	@FXML Line second_line;
	@FXML Line third_line;
	@FXML Line fourth_line;
	@FXML Line fifth_line;
	@FXML Line sixth_line;

	private Component source;
	private Component target;

	public Wire() {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Wire.fxml"));

		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);

		try { 
			fxmlLoader.load();   
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		// provide a universally unique identifier for this object
		setId(UUID.randomUUID().toString());
	}

	public Wire(String wireId) {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Wire.fxml"));

		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);

		try { 
			fxmlLoader.load();   
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		// provide a universally unique identifier for this object
		setId(wireId);
	}


	@FXML
	private void initialize() {}

	
	public Component getSource() {
		return source;
	}

	public void setSource(Component source) {
		this.source = source;
	}

	public Component getTarget() {
		return target;
	}

	public void setTarget(Component target) {
		this.target = target;
	}
	
	public void setStart(Point2D startPoint) {
		start_line.setStartX(startPoint.getX());
		start_line.setStartY(startPoint.getY());		
	}

	public void setEnd(Point2D endPoint) {
		start_line.setEndX(endPoint.getX());
		start_line.setEndY(endPoint.getY());	
	}	

	public void bindEnds(Component source, Component target) {

		this.setSource(source);
		this.setTarget(target);

		if (source.getType() == ComponentType.Voltmeter ) {

			if (source.getTargetComponentList().size() < 1) {

				start_line.startXProperty().bind(Bindings.add(source.layoutXProperty(), (source.getPrefWidth())));
				start_line.startYProperty().bind(Bindings.add(source.layoutYProperty(), (source.getPrefWidth()/2.0)+9));

				end_line.startXProperty().bind(Bindings.add(target.layoutXProperty(), (target.getPrefWidth())));		
				end_line.startYProperty().bind(Bindings.add(target.layoutYProperty(), (target.getPrefWidth() / 2.0)+10));

				start_line.endXProperty().bind(Bindings.add(start_line.startXProperty(),40));
				start_line.endYProperty().bind(Bindings.add(start_line.startYProperty(),0));

				second_line.startXProperty().bind(Bindings.add(start_line.endXProperty(),0));
				second_line.startYProperty().bind(Bindings.add(start_line.endYProperty(),0));

				second_line.endXProperty().bind(Bindings.add(second_line.startXProperty(),0));
				second_line.endYProperty().bind(Bindings.add(end_line.startYProperty(),0));

				end_line.endXProperty().bind(Bindings.add(second_line.endXProperty(),0));
				end_line.endYProperty().bind(Bindings.add(end_line.startYProperty(),0));


			} else if (source.getTargetComponentList().size() < 2) {

				start_line.startXProperty().bind(Bindings.add(source.layoutXProperty(),0));
				start_line.startYProperty().bind(Bindings.add(source.layoutYProperty(), (source.getPrefWidth()/2.0)+9));

				end_line.startXProperty().bind(Bindings.add(target.layoutXProperty(), -(target.getPrefWidth() / 2.0)));		
				end_line.startYProperty().bind(Bindings.add(target.layoutYProperty(), (target.getPrefWidth() / 2.0)+10));

				end_line.endXProperty().bind(Bindings.add(end_line.startXProperty(),40));
				end_line.endYProperty().bind(Bindings.add(end_line.startYProperty(),0));

				start_line.endXProperty().bind(Bindings.add(end_line.endXProperty(),-40));
				start_line.endYProperty().bind(Bindings.add(start_line.startYProperty(),0));

				second_line.startXProperty().bind(Bindings.add(start_line.endXProperty(),0));
				second_line.startYProperty().bind(Bindings.add(start_line.endYProperty(),0));

				second_line.endXProperty().bind(Bindings.add(second_line.startXProperty(),0));
				second_line.endYProperty().bind(Bindings.add(end_line.endYProperty(),0));

			}

		} else if (target.layoutXProperty().get() > source.layoutXProperty().get()){

			// target on right side of source

			start_line.startXProperty().bind(Bindings.add(source.layoutXProperty(), (source.getPrefWidth())));
			start_line.startYProperty().bind(Bindings.add(source.layoutYProperty(), (source.getPrefWidth() / 2.0)+10));

			end_line.endXProperty().bind(Bindings.add(target.layoutXProperty(),0));		
			end_line.endYProperty().bind(Bindings.add(target.layoutYProperty(), (target.getPrefWidth() / 2.0)+10));

			start_line.endXProperty().bind(Bindings.add(start_line.startXProperty(),40));
			start_line.endYProperty().bind(Bindings.add(start_line.startYProperty(),0));

			second_line.startXProperty().bind(Bindings.add(start_line.endXProperty(),0));
			second_line.startYProperty().bind(Bindings.add(start_line.endYProperty(),0));

			second_line.endXProperty().bind(Bindings.add(second_line.startXProperty(),0));
			second_line.endYProperty().bind(Bindings.add(end_line.endYProperty(),0));

			end_line.startXProperty().bind(Bindings.add(second_line.endXProperty(),0));
			end_line.startYProperty().bind(Bindings.add(second_line.endYProperty(),0));


		} else {

			// target on left side of source	

			start_line.startXProperty().bind(Bindings.add(source.layoutXProperty(), source.getPrefWidth()));
			start_line.startYProperty().bind(Bindings.add(source.layoutYProperty(), (source.getPrefWidth() / 2.0)+10));

			start_line.endXProperty().bind(Bindings.add(start_line.startXProperty(),40));
			start_line.endYProperty().bind(Bindings.add(start_line.startYProperty(),0));

			second_line.startXProperty().bind(Bindings.add(start_line.endXProperty(),0));
			second_line.startYProperty().bind(Bindings.add(start_line.endYProperty(),0));

			second_line.endXProperty().bind(Bindings.add(second_line.startXProperty(),0));
			second_line.endYProperty().bind((Bindings.add(target.layoutYProperty(), target.getPrefWidth()/2+10)));

			third_line.startXProperty().bind(Bindings.add(second_line.endXProperty(),0));
			third_line.startYProperty().bind(Bindings.add(second_line.endYProperty(),0));

			third_line.endXProperty().bind(Bindings.add(third_line.startXProperty(),40));
			third_line.endYProperty().bind(Bindings.add(third_line.startYProperty(),0));

			fourth_line.startXProperty().bind(Bindings.add(third_line.endXProperty(),0));
			fourth_line.startYProperty().bind(Bindings.add(third_line.endYProperty(),0));

			fourth_line.endXProperty().bind(Bindings.add(fourth_line.startXProperty(),0));
			fourth_line.endYProperty().bind(Bindings.add(target.layoutYProperty(),target.getPrefWidth()*3));

			fifth_line.startXProperty().bind(Bindings.add(fourth_line.endXProperty(),0));
			fifth_line.startYProperty().bind(Bindings.add(fourth_line.endYProperty(),0));

			fifth_line.endXProperty().bind(Bindings.add(target.layoutXProperty(),-40));
			fifth_line.endYProperty().bind(Bindings.add(fifth_line.startYProperty(),0));

			sixth_line.startXProperty().bind(Bindings.add(fifth_line.endXProperty(),0));
			sixth_line.startYProperty().bind(Bindings.add(fifth_line.endYProperty(),0));

			sixth_line.endXProperty().bind(Bindings.add(sixth_line.startXProperty(),0));
			sixth_line.endYProperty().bind(Bindings.add(target.layoutYProperty(), (target.getPrefWidth()/2)+10));

			end_line.startXProperty().bind(Bindings.add(sixth_line.endXProperty(),0));
			end_line.startYProperty().bind(Bindings.add(sixth_line.endYProperty(),0));

			end_line.endXProperty().bind(Bindings.add(target.layoutXProperty(),0));
			end_line.endYProperty().bind(Bindings.add(end_line.startYProperty(),0));				

		}

		source.addTargetComponent(target);
		target.addSourceComponent(source);

		source.registerWire(getId());
		target.registerWire(getId());

		source.registerComponent(target);
		target.registerComponent(source);
	}

}