package org.example.lms;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.List;

public class HelloController {
    @FXML
    private ListView<String> tableListView;

    // Method triggered by the button press
    @FXML
    public void loadTables() {
        List<String> tables = DAO.getTableNames();

        // If no tables are found, show an alert
        if (tables.isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No Tables Found");
            alert.setHeaderText(null);
            alert.setContentText("No tables found in the database.");
            alert.showAndWait();
        } else {
            tableListView.getItems().clear();  // Clear previous items
            tableListView.getItems().addAll(tables);  // Add new table names
        }
    }
}
