package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import application.components.Ammeter;
import application.components.Battery;
import application.components.LED;
import application.components.Resistor;
import application.components.Switch;
import application.components.Voltmeter;
import application.filemanagement.CircuitElement;
import application.filemanagement.FileOperations;
import application.filemanagement.FileParser;
import application.model.Component;
import application.model.ComponentIcon;
import application.model.ComponentType;
import application.model.Wire;
import application.util.Output;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Window;

public class RootLayout extends AnchorPane{

	@FXML SplitPane base_pane;
	@FXML AnchorPane right_pane;
	@FXML VBox left_pane;
	@FXML Text output;
	@FXML Window myWindow;
	@FXML ToolBar myToolBar;

	private ComponentIcon translucentIcon = null;

	private EventHandler<DragEvent> iconDragOverRoot = null;
	private EventHandler<DragEvent> iconDragDropped = null;
	private EventHandler<DragEvent> iconDragOverRightPane = null;

	public RootLayout() {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/RootLayout.fxml"));

		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);

		try { 
			fxmlLoader.load();    
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	@FXML
	private void initialize() {

		// Add one icon that will be used for the drag-drop process
		// This is added as a child to the root anchorpane so it can be visible
		// on both sides of the split pane.
		translucentIcon = new ComponentIcon();

		translucentIcon.setVisible(false);
		translucentIcon.setOpacity(0.65);
		getChildren().add(translucentIcon);

		// populate left pane with multiple component icons
		for (int i = 0; i < ComponentType.values().length; i++) {

			ComponentIcon componentIcon = new ComponentIcon();

			addDragDetection(componentIcon);

			componentIcon.setType(ComponentType.values()[i]);
			left_pane.getChildren().add(componentIcon);
		}

		new Output(output);

		buildDragHandlers();

		Button openBtn = new Button("Open File");
		Button saveBtn = new Button("Save File");
		Button deleteBtn = new Button("Delete File");
		Button clearBtn = new Button("Clear");
		Button runBtn = new Button("Run");

		TextField resistanceField = new TextField();
		resistanceField.setDisable(true);
		resistanceField.setPrefWidth(70);
		TextField currentField = new TextField();
		currentField.setDisable(true);
		currentField.setPrefWidth(70);

		Text ohmsLabel = new Text();
		Text ampereLabel= new Text();
		ohmsLabel.setText("Ohms");
		ampereLabel.setText("Ampere");

		myToolBar.getItems().addAll(
				openBtn,
				saveBtn,
				deleteBtn,
				clearBtn,
				runBtn,
				resistanceField,
				ohmsLabel,
				currentField,
				ampereLabel
				);

		new FileOperations().openFileButtonClick(myWindow, openBtn, this);
		new FileOperations().saveButtonClick(myWindow, saveBtn, right_pane);
		new FileOperations().deleteFileButtonClick(myWindow, deleteBtn);

		clearBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				clearNodes();
			}
		});

		runBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ArrayList<String> values = Calculator.runGetValues(right_pane);
				if(values.size() > 0){
					output.setText("");
					resistanceField.setText(values.get(1));
					currentField.setText(values.get(2));
				} else {
					output.setText("Invalid circuit");
					resistanceField.clear();
					resistanceField.clear();
				}
			}
		});

	}

	public void clearNodes() {
		ArrayList<Node> list =  new ArrayList<Node>();
		for (Node n : right_pane.getChildren()) {
			list.add(n);
		}
		for (Node n : list) {
			if (n instanceof Text) { continue; }
			right_pane.getChildren().remove(n);
		}
		Output.getInstance().clearOutput();
	}

	public void loadNodes(String fileName){

		ArrayList<CircuitElement> myArrayList = new FileParser().parse(fileName);

		for(CircuitElement element: myArrayList) {
			
			if (!element.getType().equalsIgnoreCase("WIRE")) {

				Component component = null;
				String type = element.getType();

				if (type.equals("Ammeter")) {
					component = new Ammeter(element.getId(), element.getxCoord(), element.getyCoord(), ComponentType.Ammeter);
					((Ammeter) component).setName(type);
				}
				if (type.equals("Battery")) {
					component = new Battery(element.getId(), element.getxCoord(), element.getyCoord(), ComponentType.Battery);
					((Battery) component).setName(type);
				}
				if (type.equals("LED")) {
					component = new LED(element.getId(), element.getxCoord(), element.getyCoord(), ComponentType.LED); 
					((LED) component).setName(type);
				}
				if (type.equals("Resistor")) {
					component = new Resistor(element.getId(), element.getxCoord(), element.getyCoord(), ComponentType.Resistor);
					((Resistor) component).setName(type);
				}
				if (type.equals("Switch")) {
					component = new Switch(element.getId(), element.getxCoord(), element.getyCoord(), ComponentType.Switch);
					((Switch) component).setName(type);
				}
				if (type.equals("Voltmeter")) {
					component = new Voltmeter(element.getId(), element.getxCoord(), element.getyCoord(), ComponentType.Voltmeter);
					((Voltmeter) component).setName(type);
				}
				
				right_pane.getChildren().add(component);

			}

		}

		for(CircuitElement element: myArrayList)
		{
			if (element.getType().equalsIgnoreCase("WIRE")) {
				Component source = findComponent(element.getSource());
				Component target = findComponent(element.getTarget());
				Wire wire = new Wire(element.getId());
				right_pane.getChildren().add(0,wire);
				wire.bindEnds(source, target);
				wire.setVisible(true);		
			}
		}
	
	}
	
	private void addDragDetection(ComponentIcon componentIcon) {

		componentIcon.setOnDragDetected (new EventHandler <MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {

				// set drag event handlers on their respective objects
				base_pane.setOnDragOver(iconDragOverRoot);
				right_pane.setOnDragOver(iconDragOverRightPane);
				right_pane.setOnDragDropped(iconDragDropped);

				// get a reference to the clicked DragIcon object
				ComponentIcon componentIcon = (ComponentIcon) event.getSource();

				if (batteryAdded() && componentIcon.getType() == ComponentType.Battery) { 
					event.consume();
					return;
				}

				// begin drag ops
				translucentIcon.setType(componentIcon.getType());
				translucentIcon.relocateToPoint(new Point2D (event.getSceneX(), event.getSceneY()));

				ClipboardContent content = new ClipboardContent();
				DragContainer container = new DragContainer();

				container.addData ("type", translucentIcon.getType().toString());
				content.put(DragContainer.AddNode, container);

				translucentIcon.startDragAndDrop (TransferMode.ANY).setContent(content);
				translucentIcon.setVisible(true);
				translucentIcon.setMouseTransparent(true);
				event.consume();					
			}
		});
	}	

	private void buildDragHandlers() {

		//drag over transition to move widget form left pane to right pane
		iconDragOverRoot = new EventHandler <DragEvent>() {

			@Override
			public void handle(DragEvent event) {

				Point2D p = right_pane.sceneToLocal(event.getSceneX(), event.getSceneY());

				// turn on transfer mode and track in the right-pane's context 
				// if (and only if) the mouse cursor falls within the right pane's bounds.
				if (!right_pane.boundsInLocalProperty().get().contains(p)) {	
					event.acceptTransferModes(TransferMode.ANY);
					translucentIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
					return;
				}

				event.consume();
			}
		};

		iconDragOverRightPane = new EventHandler <DragEvent> () {

			@Override
			public void handle(DragEvent event) {

				event.acceptTransferModes(TransferMode.ANY);

				// convert the mouse coordinates to scene coordinates,
				// then convert back to coordinates that are relative to 
				// the parent of translucentIcon.  Since translucentIcon is a child of the root
				// pane, coordinates must be in the root pane's coordinate system to work
				// properly.
				translucentIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
				event.consume();
			}
		};

		iconDragDropped = new EventHandler <DragEvent> () {

			@Override
			public void handle(DragEvent event) {

				DragContainer container = (DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

				container.addData("scene_coords", new Point2D(event.getSceneX(), event.getSceneY()));

				ClipboardContent content = new ClipboardContent();
				content.put(DragContainer.AddNode, container);

				event.getDragboard().setContent(content);
				event.setDropCompleted(true);
			}
		};

		this.setOnDragDone (new EventHandler <DragEvent> (){

			@Override
			public void handle (DragEvent event) {

				right_pane.removeEventHandler(DragEvent.DRAG_OVER, iconDragOverRightPane);
				right_pane.removeEventHandler(DragEvent.DRAG_DROPPED, iconDragDropped);
				base_pane.removeEventHandler(DragEvent.DRAG_OVER, iconDragOverRoot);

				translucentIcon.setVisible(false);

				Output.getInstance().clearOutput();

				// Create node drag operation
				DragContainer container = (DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

				if (container != null) {

					if (container.getValue("scene_coords") != null) {

						Component component = null;
						ComponentType componentType = ComponentType.valueOf(container.getValue("type"));
						String componentName = componentType.toString();

						if (componentName.equals("Battery")){

							component = new Battery();
							((Battery) component).setName(componentName);

						} else if (componentName.equals("LED")) {

							component = new LED();
							((LED) component).setName(componentName);

						} else if (componentName.equals("Resistor")) {

							component = new Resistor();
							((Resistor) component).setName(componentName);

						} else if (componentName.equals("Switch")) {

							component = new Switch();
							((Switch) component).setName(componentName);

						} else if (componentName.equals("Ammeter")) {

							component = new Ammeter();
							((Ammeter) component).setName(componentName);

						} else if (componentName.equals("Voltmeter")) {

							component = new Voltmeter();
							((Voltmeter) component).setName(componentName);

						}

						component.setType(componentType);
						
						right_pane.getChildren().add(component);
						Point2D cursorPoint = container.getValue("scene_coords");
						component.relocateToPoint(new Point2D(cursorPoint.getX() - 32, cursorPoint.getY() - 32));

					}
				}

				// AddWire drag operation
				container = (DragContainer) event.getDragboard().getContent(DragContainer.AddWire);

				if (container != null) {

					// bind the ends of our wire to the nodes whose id's are stored in the drag container
					String sourceId = container.getValue("source");
					String targetId = container.getValue("target");

					if (sourceId != null && targetId != null) {

						Component source = null;
						Component target = null;

						for (Node n: right_pane.getChildren()) {
							if (n.getId() == null) { continue; }
							if (n.getId().equals(sourceId)) { source = (Component) n; }
							if (n.getId().equals(targetId)) { target = (Component) n; }
						}

						if (target == source && source.getType() != ComponentType.Voltmeter) {
							Output.getInstance().printOutput("Try linking to another component.");
							return;
						}

						if (target.getType() == ComponentType.Voltmeter) {
							Output.getInstance().printOutput("Use Voltmeter to link to component instead.");
							return;
						}

						if (source == null || target == null) {
							Output.getInstance().printOutput("No target component.");
							return;
						}

						if (Collections.frequency(source.getConnectedComponentList(), target) < 2) {

							if (!source.getTargetComponentList().contains(target) || source.getType() == ComponentType.Voltmeter) {

								// System.out.println(container.getData());
								Wire wire = new Wire();
								// add our link at the top of the rendering order so it's rendered first
								right_pane.getChildren().add(0,wire);
								wire.bindEnds(source, target);
								
								Output.getInstance().printOutput("Wire link " + source.getType().toString() + " with " + target.getType().toString());	

							} else {

								Output.getInstance().printOutput("Source already connected to Target, try connecting Target to Source.");
							}

						} else {

							Output.getInstance().printOutput(source.getType().toString() + " already connected to " + target.getType().toString() + " twice.");

						}
					}

				}

				event.consume();
			}
		});		
	}
	
	private Component findComponent(String id){
		Component component = null;
		for (Node node : right_pane.getChildren()){
			if (node instanceof Component) {
				Component c = (Component) node;
				if (c.getId().equals(id) ) {
					return c;
				}
			}
		}
		return component;
	}
	
	private boolean batteryAdded(){
		boolean battery = false;
		for (Node node : right_pane.getChildren()){
			if (node instanceof Battery) {
				battery = true;
			}
		}
		return battery;
	}

}
