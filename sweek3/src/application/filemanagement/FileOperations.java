package application.filemanagement;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.RootLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class FileOperations {

	public void openFileButtonClick(Window aWindow, Button btn, RootLayout root){

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
				fileChooser.getExtensionFilters().add(extFilter);

				File file = fileChooser.showOpenDialog(aWindow);
				if (file == null) { return; }

				String path = file.getAbsolutePath();

				root.clearNodes();
				root.loadNodes(path);

				//				try {
				//					readFile(path);
				//				} catch (IOException e) {
				//					e.printStackTrace();
				//				}
			}
		});
	}

	private void readFile(String pathName) throws IOException{
		InputStream input = new BufferedInputStream(new FileInputStream(pathName));
		byte[] buffer = new byte[8192];
		try {
			for (int length = 0; (length = input.read(buffer)) != -1;) {
				System.out.write(buffer, 0, length);
			}
		} finally {
			input.close();
		}
	}

	public void saveButtonClick(Window aWindow, Button btn, AnchorPane right_pane){

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
				fileChooser.getExtensionFilters().add(extFilter);

				File file = fileChooser.showSaveDialog(aWindow);

				if (file != null){

					BufferedWriter writer = null;
					try {
						writer = new BufferedWriter( new FileWriter(file));
						writer.write(new FileCreate().savefile(right_pane));
					}
					catch ( IOException ex) {
						Logger.getLogger(RootLayout.class.getName()).log(Level.SEVERE, null, ex);
					}
					finally {
						try {
							if ( writer != null) { writer.close( ); }
						}
						catch ( IOException ex) {
							Logger.getLogger(RootLayout.class.getName()).log(Level.SEVERE, null, ex);
						}
					}
				}
			}
		});
	}

	public void deleteFileButtonClick(Window aWindow, Button btn){

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
				fileChooser.getExtensionFilters().add(extFilter);

				File file = fileChooser.showOpenDialog(aWindow);

				if (file != null){
					Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
					alert.setTitle("Confirmation Dialog");
					alert.setHeaderText("Warning!");
					alert.setContentText("Are you sure you want to delete this file?");

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK) {
						file.delete();
					}  
				} 
			}
		});
	}

}