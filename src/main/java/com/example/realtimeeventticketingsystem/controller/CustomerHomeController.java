package com.example.realtimeeventticketingsystem.controller;

import com.example.realtimeeventticketingsystem.model.Event;
import com.example.realtimeeventticketingsystem.model.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CustomerHomeController {

    @FXML private ListView<String> eventListView;

    public void initialize() {
        if (eventListView == null) {
            System.out.println("eventListView is null. Check FXML file.");
        } else {
            loadEvents();

            eventListView.setOnMouseClicked(this::handleEventSelection);
        }
    }

    private void loadEvents() {
        try {
            List<Event> events = DatabaseHandler.getAllEvents();
            for (Event event : events) {
                eventListView.getItems().add(event.getEventName() + " - " + event.getEventDate());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load events. Please check the console for details.");
        }
    }

    private void handleEventSelection(MouseEvent event) {
        if (event.getClickCount() == 2) {
            String selectedItem = eventListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                int selectedIndex = eventListView.getSelectionModel().getSelectedIndex();

                Navigation.setSelectedEventId(DatabaseHandler.getAllEvents().get(selectedIndex).getId());

                Navigation.goTo("BookTicketsView.fxml", "Book Tickets");
            }
        }
    }

    @FXML
    private void logOut(ActionEvent event) {
        try {
            DatabaseHandler.setCurrentVendorId(0);

            Parent loginPage = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
            Scene loginScene = new Scene(loginPage);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to navigate to the login screen.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
