package application;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import application.components.AmmeterComponent;
import application.components.BatteryComponent;
import application.components.ButtonComponent;
import application.components.LEDComponent;
import application.components.RelayComponent;
import application.components.ResistorComponent;
import application.components.SwitchComponent;
import application.components.VoltmeterComponent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Window;
import javafx.scene.control.Button;

public class RootLayout extends AnchorPane{

	@FXML SplitPane base_pane;
	@FXML AnchorPane right_pane;
	@FXML VBox left_pane;
	@FXML TextFlow output;
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

		myToolBar.getItems().addAll(
				openBtn,
				saveBtn,
				deleteBtn
				);

		new FileOperations().openFileButtonClick(myWindow, openBtn, this);
		new FileOperations().saveButtonClick(myWindow, saveBtn, right_pane);
		new FileOperations().deleteFileButtonClick(myWindow, deleteBtn);
	}
	
	public void clearNodes() {
		while (right_pane.getChildren().size()>1){
			if (right_pane.getChildren().get(0) instanceof TextFlow){
				continue;
			}
			right_pane.getChildren().remove(1);
		}
	}

	public void loadNodes(String fileName){
		
		ArrayList<CircuitElement> myArrayList = new FileParser().parse(fileName);

		for(CircuitElement element: myArrayList)
		{
			Component newNode = new Component(element.id, element.xCoord, 
					element.yCoord, ComponentType.Ammeter);
			
			System.out.println(newNode.getLayoutX());
			right_pane.getChildren().add(newNode);
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

				Output.getInstance().clearOuput();

				// Create node drag operation
				DragContainer container = (DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

				if (container != null) {

					if (container.getValue("scene_coords") != null) {

						Component component = null;
						ComponentType componentType = ComponentType.valueOf(container.getValue("type"));
						String componentName = componentType.toString();

						if (componentName.equals("Battery")){

							component = new BatteryComponent();
							((BatteryComponent) component).setName(componentName);

						} else if (componentName.equals("Button")) {

							component = new ButtonComponent();
							((ButtonComponent) component).setName(componentName);

						} else if (componentName.equals("LED")) {

							component = new LEDComponent();
							((LEDComponent) component).setName(componentName);

						} else if (componentName.equals("Relay")) {

							component = new RelayComponent();
							((RelayComponent) component).setName(componentName);

						} else if (componentName.equals("Resistor")) {

							component = new ResistorComponent();
							((ResistorComponent) component).setName(componentName);

						} else if (componentName.equals("Switch")) {

							component = new SwitchComponent();
							((SwitchComponent) component).setName(componentName);

						} else if (componentName.equals("Ammeter")) {

							component = new AmmeterComponent();
							((AmmeterComponent) component).setName(componentName);

						} else if (componentName.equals("Voltmeter")) {

							component = new VoltmeterComponent();
							((VoltmeterComponent) component).setName(componentName);

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

							if (n.getId() == null) {
								continue;
							}

							if (n.getId().equals(sourceId)) {
								source = (Component) n;
							}

							if (n.getId().equals(targetId)) {
								target = (Component) n;
							}

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

								//add our link at the top of the rendering order so it's rendered first
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

}
