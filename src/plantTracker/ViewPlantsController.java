package plantTracker;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewPlantsController implements Initializable {
	
	@FXML 
	private Label sceneLabel;
	
	@FXML 
	private HBox bottomBar;
	
	@FXML
	private VBox plantList;
	
	@FXML
	private TextField searchBar;
	
	@FXML
	private void toStats(MouseEvent e) {
		switchScene(e, "ViewPlantStats.fxml", "Plant Stats");
	}

	@FXML
	public void handleAddPlant(MouseEvent event) {
		switchScene(event, "AddPlant.fxml", "Add Plant");
	}

	@FXML
	public void handlePlantTracker(MouseEvent event) {
		switchScene(event, "PlantTracker.fxml", "Plant Tracker"); 
	}
	
	@FXML
	private void handleViewReminders(MouseEvent event) {
		switchScene(event, "ViewReminders.fxml", "View Reminders"); 
	}

	public void switchScene(MouseEvent event, String fxmlFile, String title) {
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
		
		//TODO: Fetch plant names from database
		ObservableList<String> data = FXCollections.observableArrayList();
		data.add("Potato Patch 1");
		data.add("Flytrap");
		data.add("Flytrap's Brother");
		data.add("Potato Patch 2");
		data.add("Pitcher Plant");
		data.add("Potat");
		data.add("Cacto");
		data.add("Name");
		data.add("Name 2");
		data.add("FJIoew 2");
        
        //Real-time search functionality
        FilteredList<String> filteredData = new FilteredList<>(data, s -> true);
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(item -> {
                //If search bar is empty, display all items
                if (newValue.isEmpty()) {
                    return true;
                }
                //Filter items that contain the search query
                return item.toLowerCase().contains(newValue.toLowerCase());
            });
        });
        
        //Adds list to GUI
        ListView<String> listView = new ListView<>(filteredData);
        plantList.getChildren().add(listView);
        //Have listView grow with VBox it is in
        listView.prefHeightProperty().bind(plantList.heightProperty());
        
        //Scene switch functionality for elements in list
        listView.setOnMouseClicked(e -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                toStats(e);
            }
            System.out.println(selectedItem);
        });
        
      //Stops search-bar from automatically being highlighted when scene is switched
      searchBar.setFocusTraversable(false);
	}
}
