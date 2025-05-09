module Group2Project {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.web;
	requires javafx.base;
	requires javafx.graphics;
	requires java.sql;
	requires java.desktop;

	exports plantTracker.controller to javafx.graphics, javafx.fxml, javafx.base;
	exports util to javafx.graphics, javafx.fxml, javafx.base;
	exports user to javafx.graphics, javafx.fxml, javafx.base;

	opens user to javafx.graphics, javafx.fxml;
	opens plantTracker.controller to javafx.graphics, javafx.fxml;
	opens util to javafx.graphics, javafx.fxml;
}