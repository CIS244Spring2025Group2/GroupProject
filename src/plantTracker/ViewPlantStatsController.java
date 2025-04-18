package plantTracker;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ViewPlantStatsController implements Initializable {

    @FXML
    private TextField date;

    @FXML
    private Button edit;
    
    @FXML
    private Text editStatus;

    @FXML
    private Button home;

    @FXML
    private TextField name;

    @FXML
    private TextField outdoor;

    @FXML
    private Button plantList;

    @FXML
    private Button reminders;

    @FXML
    private Button save;

    @FXML
    private TextField species;

    @FXML
    private TextField sunlight;

    @FXML
    private TextField type;

    @FXML
    private TextField typeSpecific;

    @FXML
    private Label typeSpecificLabel;
    
    @FXML
	private Label sceneLabel;
    
	@FXML
	private HBox bottomBar;
    
    @FXML
    private void enableEdit(ActionEvent e) {
    	setEditMode(true);
    	editStatus.setText("Plant attributes are now open for edit");
    }
    
    @FXML
    private void saveEdit(ActionEvent e) {
    	setEditMode(false);
    	// Add way to update changes in database
    	editStatus.setText("Textfields above are now closed for edit and changes are saved.");
    }
    
    @FXML
    private void toPlantTracker(ActionEvent e) {
    	switchScene(e, "PlantTracker.fxml", "Plant Tracker");
    }
    
    @FXML
    private void toPlantList(ActionEvent e) {
    	switchScene(e, "ViewPlants.fxml", "Plant List");
    }
    
    @FXML
    private void toReminders(ActionEvent e) {
    	switchScene(e, "ViewReminders.fxml", "Reminders");
    }
    
    private void setEditMode(boolean editable) {
    	name.setEditable(editable);
    	type.setEditable(editable);
    	date.setEditable(editable);
    	species.setEditable(editable);
    	outdoor.setEditable(editable);
    	sunlight.setEditable(editable);
    	typeSpecific.setEditable(editable);
    }
    
    public void switchScene(ActionEvent event, String fxmlFile, String title) {
		try {
			Parent loader = FXMLLoader.load(getClass().getResource(fxmlFile));
			Scene newScene = new Scene(loader);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(newScene);
			stage.setTitle(title);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Allows for bottom-bar to always to be the same height as label at top
		bottomBar.prefHeightProperty().bind(sceneLabel.heightProperty());
		
	}

}
