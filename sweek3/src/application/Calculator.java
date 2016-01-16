package application;

import java.util.ArrayList;
import java.util.List;

import application.components.Ammeter;
import application.components.Battery;
import application.components.Resistor;
import application.model.Component;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class Calculator {
	
	static ArrayList<ArrayList<Component>> pathCollections = new ArrayList<ArrayList<Component>>();

	public static ArrayList<String> runGetValues(AnchorPane right_pane) {

		ArrayList<String> values = new ArrayList<String>();

		Battery battery = null;
		for (Node node : right_pane.getChildren()){
			if (node instanceof Battery) {
				battery = (Battery) node;
			}
		}

		if (battery != null) {

			pathCollections = new ArrayList<ArrayList<Component>>();
			createPathes(battery, battery, new ArrayList<Component>(), -1);
			if (pathCollections.size() == 0) { return values; }

			double circuitResistance = calculateResistance(pathCollections);
			if (circuitResistance == 0.0) { return values; }
			double circuitCurrent = (battery.getVoltage())/(circuitResistance);
			
			values.add("" + (battery.getVoltage()));
			values.add("" + (circuitResistance));
			values.add("" + (circuitCurrent));
			
			pathCollections = new ArrayList<ArrayList<Component>>();
			createPathes(battery, battery, new ArrayList<Component>(), -1);
			setVoltageForComponents(battery.getVoltage(), circuitResistance, circuitCurrent);
			setAmmeter(right_pane, circuitCurrent);
		
		}

		return values;

	}

	public static void setAmmeter(AnchorPane right_pane, double circuitCurrent){	
		for (Node n : right_pane.getChildren()){
			if (n instanceof Resistor) { continue; }
			if (n instanceof Ammeter){
				((Ammeter) n).setCurrent(circuitCurrent);
			}
		}
	}
	
	public static void setVoltageForComponents(double voltage, double resistance, double current){
		
		int numberOfPaths = pathCollections.size();

		if ((numberOfPaths) == 0) { return; }

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
				if (tail instanceof Resistor) {
					Resistor resistor = (Resistor) tail;
					double resistorVoltage = current*resistor.getResistance();
					resistor.setVoltage(resistorVoltage);
					voltage -= resistorVoltage;
					// System.out.println("series Resistance: " + ((ResistorComponent) tail).getResistance());
				}
				removeTail(pathCollections);		
			}

			// Do Parallel Resistance Calculation
			List<Double> pathResistance = new ArrayList<Double>();
			ArrayList<ArrayList<Resistor>> parallelPathes = new ArrayList<ArrayList<Resistor>>();
			for (ArrayList<Component> path : pathCollections) {
				double parallelPathResistance = 0.0;	
				// remove series component along the path
				ArrayList<Resistor> resistorPath = new ArrayList<Resistor>();
				while(path.size()>0) {
					Component c = path.get(path.size()-1);
					if (c instanceof Resistor) {
						parallelPathResistance += ((Resistor) c).getResistance();
						resistorPath.add((Resistor) c);
					}
					path.remove(path.size()-1);
					if (path.size()>0) {
						if (inMoreThanOnePath(pathCollections, path.get(path.size()-1))){
							break;
						}
					}
				}
				
				if(parallelPathResistance > 0) {
					parallelPathes.add(resistorPath);
					pathResistance.add(parallelPathResistance);
				}
			}

			double totalParallelResistance = 0.0;
			for (double res : pathResistance) {
				totalParallelResistance += res;
			}
			
			for (int i = 0; i < pathResistance.size(); i++) {
				double ratio = pathResistance.get(i)/totalParallelResistance;
				ArrayList<Resistor> resistorPath = parallelPathes.get(i);
				for (int j = 0; j < resistorPath.size(); j++){
					resistorPath.get(j).setVoltage(resistorPath.get(j).getResistance()*ratio*current);
				}
			}

			if (!checkSameTail(pathCollections)) { break; }

		}
		
	}

	public static void createPathes(Component starting, Component current, ArrayList<Component> path, int counter) {

		ArrayList<Component> newPath = new ArrayList<Component>();
		for (Component c : path) {
			newPath.add(c);
		}

		whilerun: while(true) {

			if (current == starting && newPath.size() > 0) {	
				// System.out.println("Valid");
				pathCollections.add(newPath);
				return;
			}

			if (current.getTargetComponentList().size() == 0) {
				// System.out.println("Invalid");
				return;
			}

			if (current.getTargetComponentList().size() == 1) {
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
						createPathes(starting, current.getTargetComponentList().get(i), newPath, i);
					}		
				}
				return;
			}
		}
	}

	// == Calculation ==

	public static double calculateResistance(ArrayList<ArrayList<Component>> pathCollections) {

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
				if (tail instanceof Resistor) {
					circuitResistance += ((Resistor) tail).getResistance();
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
					if (c instanceof Resistor) {
						parallelPathResistance += ((Resistor) c).getResistance();
					}
					path.remove(path.size()-1);
					if (path.size()>0) {
						if (inMoreThanOnePath(pathCollections, path.get(path.size()-1))){
							break;
						}
					}
				}
				if (parallelPathResistance > 0) {
					pathResistance.add(1/parallelPathResistance);
				}

			}

			double pResistance = 0.0;
			for (double resistance : pathResistance) {
				pResistance += resistance;
			}
			if (pResistance > 0) {
				// System.out.println("pResistance: " + 1/pResistance);
				circuitResistance +=  1/pResistance;
			}

			if (!checkSameTail(pathCollections)) { break; }

		}
		return circuitResistance;

	}

	public static boolean inMoreThanOnePath(ArrayList<ArrayList<Component>> pathCollections, Component c){
		int count = 0;
		for (ArrayList<Component> path : pathCollections) {
			if (path.contains(c)){
				count++;
			}
		}
		return count>1;
	}

	public static void removeTail(ArrayList<ArrayList<Component>> pathCollections) {
		for (ArrayList<Component> path : pathCollections) {
			path.remove(path.size()-1);
		}
	}

	public static boolean checkSameTail(ArrayList<ArrayList<Component>> pathCollections) {
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
