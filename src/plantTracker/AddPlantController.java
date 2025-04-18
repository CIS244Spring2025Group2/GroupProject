package plantTracker;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPlantController implements Initializable {

	private String title;
	private String[] outsideOptions = { "Spring", "Summer", "Fall", "Winter" };
	private String[] sunshineOptions = { "Full sun", "Part sun", "Shade" };
	private String[] plantTypeOptions = { "Flowering Plant", "Fruiting Plant", "Vegetable", "Herb", "Decorative Plant",
			"Carnivorous Plant" };
	private Plant plant;
	private boolean canBeOutside;
	private String sunshineNeed;
	private Date date;

	@FXML
	private ComboBox<String> plantType;
	@FXML
	private TextField plantName;
	@FXML
	private TextField species;
	@FXML
	private Label datePlantedLabel;
	@FXML
	private DatePicker datePlanted;
	@FXML
	private Label outsideLabel;
	@FXML
	private ListView<String> outside;
	@FXML
	private ComboBox<String> sunshine;
	@FXML
	private Button save;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub

		plantType.getItems().addAll(FXCollections.observableArrayList(plantTypeOptions));

		sunshine.getItems().addAll(FXCollections.observableArrayList(sunshineOptions));

		outside.getItems().addAll(FXCollections.observableArrayList(outsideOptions));
		outside.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

	}

	public void save(ActionEvent event) throws IOException {
		Parent viewPlantsParent = FXMLLoader.load(getClass().getResource("ViewPlants.fxml"));
		Scene viewPlantsScene = new Scene(viewPlantsParent);

		// This line gets the Stage information
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

		window.setScene(viewPlantsScene);
		window.setTitle("View Plants");
		window.show();
	}
}
