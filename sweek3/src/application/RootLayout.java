package application;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import application.components.AmmeterComponent;
import application.components.BatteryComponent;
import application.components.ButtonComponent;
import application.components.LEDComponent;
import application.components.RelayComponent;
import application.components.ResistorComponent;
import application.components.SwitchComponent;
import application.components.VoltmeterComponent;
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
		
		Text ohms = new Text();
		Text ampere = new Text();
		ohms.setText("Ohms");
		ampere.setText("Ampere");
		
		myToolBar.getItems().addAll(
				openBtn,
				saveBtn,
				deleteBtn,
				clearBtn,
				runBtn,
				resistanceField,
				ohms,
				currentField,
				ampere
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
				ArrayList<String> values = runGetValues();
				resistanceField.setText(values.get(1));
				currentField.setText(values.get(2));
			}
		});
		
	}

	private boolean batteryAdded(){
		boolean battery = false;
		for (Node node : right_pane.getChildren()){
			if (node instanceof BatteryComponent) {
				battery = true;
			}
		}
		return battery;
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

		for(CircuitElement element: myArrayList)
		{
			if (!element.getType().equalsIgnoreCase("WIRE")) {
				Component newNode = new Component(element.getId(), element.getxCoord(), 
						element.getyCoord(), ComponentType.Ammeter);
				right_pane.getChildren().add(newNode);
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
	
	ArrayList<ArrayList<Component>> pathCollections = new ArrayList<ArrayList<Component>>();
	
	public ArrayList<String> runGetValues(){
		
		ArrayList<String> values = new ArrayList<String>();
		
		BatteryComponent battery = null;
		for (Node node : right_pane.getChildren()){
			if (node instanceof BatteryComponent) {
				battery = (BatteryComponent) node;
			}
		}
		
		if (battery != null) {
			
			pathCollections = new ArrayList<ArrayList<Component>>();
			runCircuit(battery, battery, new ArrayList<Component>(), -1);
			if (pathCollections.size() == 0) { return values; }
			
			double resistance = calculateResistance(pathCollections);
			values.add("" + (battery.getVoltage()));
			values.add("" + (resistance));
			values.add("" + (battery.getVoltage())/(resistance));
			
			// System.out.println("Total Voltage: " + battery.getVoltage());
			// System.out.println("Total Resistance: " + resistance);
			// System.out.println("Total Current: " + battery.getVoltage()/resistance);
		}
		
		return values;
		
	}

	public void runCircuit(Component starting, Component current, ArrayList<Component> path, int counter) {

		ArrayList<Component> newPath = new ArrayList<Component>();
		for (Component c : path) {
			newPath.add(c);
		}

		whilerun: while(true) {

			if (current == starting && newPath.size() > 0) {	
				System.out.println("Valid");
				//				for (Component c : newPath) {
				//					System.out.print(c.getType().toString() + " ");
				//				}
				//				System.out.println("");		
				pathCollections.add(newPath);
				return;
			}

			if (current.getTargetComponentList().size() == 0) {
				System.out.println("Invalid");
				//				for (Component c : newPath) {
				//					System.out.print(c.getType().toString() + " ");
				//				}
				//				System.out.println("");
				return;
			}

			if (current.getTargetComponentList().size() == 1) {
				//				System.out.println("Add " + current.getType().toString() + " to path");
				newPath.add(current);
				current = current.getTargetComponentList().get(0);
				continue;
			}

			if (current.getTargetComponentList().size() > 1) {
				newPath.add(current);
				for (int i = 0; i < current.getTargetComponentList().size(); i++) {
					if (counter > -1) {
						if (current.getTargetComponentList().size() > counter && 
								current.getTargetComponentList().get(counter).getTargetComponentList().size() > 0 ) {
							current = current.getTargetComponentList().get(counter);
						} else if (counter == 0) {
							current = current.getTargetComponentList().get(counter+1);
						} else {
							current = current.getTargetComponentList().get(counter-1);
						}		
						continue whilerun;
					} else {
						runCircuit(starting, current.getTargetComponentList().get(i), newPath, i);
					}		
				}
				return;
			}

		}
	}

	// == Calculation ==

	public double calculateResistance(ArrayList<ArrayList<Component>> pathCollections) {

		double circuitResistance = 0.0;
		int numberOfPaths = pathCollections.size();

		if ((numberOfPaths) == 0) { return 0.0; }

		// Drop Head
		for (ArrayList<Component> path : pathCollections) {
			path.remove(0);
		}

		while (true) {

			// Remove all same tail
			while (checkSameTail(pathCollections)) {

				ArrayList<Component> last = pathCollections.get(numberOfPaths-1);
				Component tail = last.get(last.size()-1);
				// Add Same Tail to Circuit Resistance
				if (tail instanceof ResistorComponent) {
					circuitResistance += ((ResistorComponent) tail).getResistance();
					// System.out.println("series Resistance: " + ((ResistorComponent) tail).getResistance());
				}
				removeTail(pathCollections);
			}

			// Do Parallel Resistance Calculation
			List<Double> pathResistance = new ArrayList<Double>();
			for (ArrayList<Component> path : pathCollections) {
				double parallelPathResistance = 0.0;	
				// remove series component along the path
				while(path.size()>0) {
					Component c = path.get(path.size()-1);
					if (c instanceof ResistorComponent) {
						parallelPathResistance += ((ResistorComponent) c).getResistance();
					}
					path.remove(path.size()-1);
					if (path.size()>0) {
						if (inMoreThanOnePath(pathCollections, path.get(path.size()-1))){
							break;
						}
					}
				}
				pathResistance.add(1/parallelPathResistance);
			}

			double pResistance = 0.0;
			for (double resistance : pathResistance) {
				pResistance += resistance;
			}

			// System.out.println("pResistance: " + 1/pResistance);
			circuitResistance +=  1/pResistance;

			if (!checkSameTail(pathCollections)) { break; }

		}
		return circuitResistance;

	}

	public boolean inMoreThanOnePath(ArrayList<ArrayList<Component>> pathCollections, Component c){
		int count = 0;
		for (ArrayList<Component> path : pathCollections) {
			if (path.contains(c)){
				count++;
			}
		}
		return count>1;
	}

	public void removeTail(ArrayList<ArrayList<Component>> pathCollections) {
		for (ArrayList<Component> path : pathCollections) {
			path.remove(path.size()-1);
		}
	}

	public boolean checkSameTail(ArrayList<ArrayList<Component>> pathCollections) {

		// Check Same Tail
		Component tail = null;
		boolean sameTail = true;
		for (ArrayList<Component> path : pathCollections) {
			if (path.size() == 0) { return false; }
			if (tail == null) { tail = path.get(path.size()-1); }
			if (path.get(path.size()-1) != tail) {
				sameTail = false;
				break;
			}
		}
		return sameTail;
	}

}
