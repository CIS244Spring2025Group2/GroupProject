module Group2Project {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.web;
	requires javafx.base;
	requires javafx.graphics;
	requires java.sql;
	requires java.desktop;

	opens plantTracker to javafx.graphics, javafx.fxml, javafx.base;
	opens user to javafx.graphics, javafx.fxml;
}