package user;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ManageUsersController implements Initializable {

	@FXML
	private TextField searchBar;

	@FXML
	private Button home;

	@FXML
	private Button makeAdmin;

	@FXML
	private Button delete;

	private String selectedItem;
	private ObservableList<String> data = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

}