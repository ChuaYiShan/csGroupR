package application.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import application.DragContainer;
import application.util.Output;
import application.util.Point2dSerial;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;

public class Component extends AnchorPane {

	@FXML private AnchorPane root_pane;
	@FXML private AnchorPane left_link_handle;
	@FXML private AnchorPane right_link_handle;
	@FXML protected Label title_bar;
	@FXML private Label close_button;

	private EventHandler <MouseEvent> mWireHandleDragDetected;
	private EventHandler <DragEvent> mWireHandleDragDropped;
	private EventHandler <DragEvent> mContextWireDragOver;
	private EventHandler <DragEvent> mContextWireDragDropped;

	private EventHandler <DragEvent> mContextDragOver;
	private EventHandler <DragEvent> mContextDragDropped;

	private ComponentType componentType = null;

	private Point2D mDragOffset = new Point2D (0.0, 0.0);

	private final Component self;

	private Wire wire = null;
	private AnchorPane right_pane = null;

	private final List<String> linkedWireIDs = new ArrayList<String>();
	private final List<Component> connectedComponentList = new ArrayList<Component>();
	private final List<Component> targetComponentList = new ArrayList<Component>();
	private final List<Component> sourceComponentList = new ArrayList<Component>();

	private String componentName;

	public Component() {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Component.fxml"));
		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);

		self = this;

		try { 
			fxmlLoader.load();        
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		// provide a universally unique identifier for this object
		self.setId(UUID.randomUUID().toString());

	}

	public Component(String id, double xVal, double yVal, ComponentType type)
	{	
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Component.fxml"));
		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);

		self = this;

		try { 
			fxmlLoader.load();        
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		self.setId(id);
		self.setVisible(true);
		self.setType(type);
		self.setLayoutX(xVal);
		self.setLayoutY(yVal);
	}


	@FXML
	private void initialize() {

		buildNodeDragHandlers();
		buildWireDragHandlers();

		left_link_handle.setOnDragDetected(mWireHandleDragDetected);
		right_link_handle.setOnDragDetected(mWireHandleDragDetected);

		left_link_handle.setOnDragDropped(mWireHandleDragDropped);
		right_link_handle.setOnDragDropped(mWireHandleDragDropped);

		wire = new Wire();
		wire.setVisible(false);

		parentProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				right_pane = (AnchorPane) getParent();
			}
		});
	}

	public void setName(String name) {
		componentName = name;
		this.title_bar.setText(name);
	}

	public String getName(String name) {
		return componentName;
	}

	public void registerComponent(Component component){
		connectedComponentList.add(component);
	}

	public void unregisterComponent(Component component){
		connectedComponentList.remove(component);
	}

	public List<Component> getConnectedComponentList() {
		return this.connectedComponentList;
	}

	public void addTargetComponent(Component component){
		targetComponentList.add(component);
	}

	public void deleteTargetComponent(Component component){
		targetComponentList.remove(component);
	}

	public List<Component> getTargetComponentList() {
		return this.targetComponentList;
	}

	public void addSourceComponent(Component component){
		sourceComponentList.add(component);
	}

	public void deleteSourceComponent(Component component){
		sourceComponentList.remove(component);
	}

	public List<Component> getSourceComponentList() {
		return this.sourceComponentList;
	}

	public void registerWire(String linkId) {
		linkedWireIDs.add(linkId);
	}

	public List<String> getWiresList() {
		return this.linkedWireIDs;
	}

	public void setType (ComponentType type) {

		this.componentType = type;
		getStyleClass().clear();
		getStyleClass().add("dragicon");

		switch (componentType) {

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

	public ComponentType getType () { 
		return this.componentType; 
	}

	public void relocateToPoint (Point2D p) {

		// relocates the object to a point that has been converted to
		// scene coordinates
		Point2D localCoords = getParent().sceneToLocal(p);
		relocate( 
				(int) (localCoords.getX() - mDragOffset.getX()),
				(int) (localCoords.getY() - mDragOffset.getY())
				);
	}

	public void buildNodeDragHandlers() {

		// drag over to handle node dragging in the right pane view
		mContextDragOver = new EventHandler <DragEvent>() {

			@Override
			public void handle(DragEvent event) {		
				event.acceptTransferModes(TransferMode.ANY);				
				relocateToPoint(new Point2dSerial(event.getSceneX(), event.getSceneY()));
				event.consume();
			}
		};

		// drag drop for node dragging
		mContextDragDropped = new EventHandler <DragEvent> () {

			@Override
			public void handle(DragEvent event) {
				getParent().setOnDragOver(null);
				getParent().setOnDragDropped(null);
				event.setDropCompleted(true);
				event.consume();
			}
		};

		// close button click
		close_button.setOnMouseClicked(new EventHandler <MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {

				Output.getInstance().clearOutput();
				Output.getInstance().printOutput(self.getType().toString() + " removed.");

				AnchorPane parent  = (AnchorPane) self.getParent();
				parent.getChildren().remove(self);

				// iterate each wire id connected to this node
				// find it's corresponding component in the right-hand
				// AnchorPane and delete it.
				for (ListIterator<String> iterId = linkedWireIDs.listIterator(); iterId.hasNext();) {

					String id = iterId.next();

					for (ListIterator <Node> iterNode = parent.getChildren().listIterator(); iterNode.hasNext();) {

						Node node = iterNode.next();

						if (node.getId() == null)
							continue;

						if (node.getId().equals(id))
							iterNode.remove();
					}

					iterId.remove();
				}

				for(Component component : connectedComponentList) {
					component.unregisterComponent(self);
					component.deleteTargetComponent(self);
					self.deleteSourceComponent(component);
				}

			}

		});

		// drag detection for node dragging
		title_bar.setOnDragDetected ( new EventHandler <MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {

				getParent().setOnDragOver(null);
				getParent().setOnDragDropped(null);

				getParent().setOnDragOver (mContextDragOver);
				getParent().setOnDragDropped (mContextDragDropped);

				// begin drag ops
				mDragOffset = new Point2D(event.getX(), event.getY());
				relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));

				ClipboardContent content = new ClipboardContent();
				DragContainer container = new DragContainer();

				container.addData("type", componentType.toString());
				content.put(DragContainer.AddNode, container);

				startDragAndDrop(TransferMode.ANY).setContent(content);                

				event.consume();					
			}

		});		
	}

	private void buildWireDragHandlers() {

		mWireHandleDragDetected = new EventHandler <MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {

				getParent().setOnDragOver(null);
				getParent().setOnDragDropped(null);

				getParent().setOnDragOver(mContextWireDragOver);
				getParent().setOnDragDropped(mContextWireDragDropped);

				// Set up user-draggable wire
				right_pane.getChildren().add(0,wire);					
				wire.setVisible(false);

				// Centre of component
				Point2D p = new Point2D(getLayoutX() + (getWidth() / 2.0),getLayoutY() + (getHeight() / 2.0));
				wire.setStart(p);					

				// Drag content code
				ClipboardContent content = new ClipboardContent();
				DragContainer container = new DragContainer();

				// pass the UUID of the source node for later lookup
				container.addData("source", getId());
				content.put(DragContainer.AddWire, container);

				startDragAndDrop(TransferMode.ANY).setContent(content);	
				event.consume();
			}
		};

		mWireHandleDragDropped = new EventHandler <DragEvent> () {

			@Override
			public void handle(DragEvent event) {

				getParent().setOnDragOver(null);
				getParent().setOnDragDropped(null);

				// get the drag data.  If it's null, abort.  
				// This isn't the drag event we're looking for.
				DragContainer container = (DragContainer) event.getDragboard().getContent(DragContainer.AddWire);
				if (container == null) { return; }

				// hide the draggable Wire and remove it from the right-hand AnchorPane's children
				wire.setVisible(false);
				right_pane.getChildren().remove(0);

				ClipboardContent content = new ClipboardContent();

				// pass the UUID of the target node for later lookup
				container.addData("target", getId());
				content.put(DragContainer.AddWire, container);

				event.getDragboard().setContent(content);
				event.setDropCompleted(true);
				event.consume();				
			}
		};

		mContextWireDragOver = new EventHandler <DragEvent> () {

			@Override
			public void handle(DragEvent event) {

				event.acceptTransferModes(TransferMode.ANY);

				// Relocate end of user-draggable wire
				if (!wire.isVisible()) {
					wire.setVisible(true);
				}
				wire.setEnd(new Point2D(event.getX(), event.getY()));

				event.consume();
			}
		};

		// drop event for link creation
		mContextWireDragDropped = new EventHandler <DragEvent> () {

			@Override
			public void handle(DragEvent event) {

				getParent().setOnDragOver(null);
				getParent().setOnDragDropped(null);

				// hide the draggable Wire and remove it from the right-hand AnchorPane's children
				wire.setVisible(false);
				right_pane.getChildren().remove(0);

				event.setDropCompleted(true);
				event.consume();
			}
		};

	}
}