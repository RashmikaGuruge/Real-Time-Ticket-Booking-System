package com.example.realtimeeventticketingsystem.controller;

import com.example.realtimeeventticketingsystem.model.User;
import com.example.realtimeeventticketingsystem.model.DatabaseHandler;
import com.example.realtimeeventticketingsystem.model.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ProfileController {

    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private ListView<String> myEvents;

    private int userId;

    public void setUserId(int userId) {
        this.userId = userId;
        loadProfile();
    }

    private void loadProfile() {
        User currentUser = DatabaseHandler.getCurrentUser(userId);
        if (currentUser != null) {
            System.out.println("Loaded User: " + currentUser.getUsername());
            usernameLabel.setText(currentUser.getUsername());
            emailLabel.setText(currentUser.getEmail());
            loadUserEvents(userId);
        } else {
            showAlert("Error", "Failed to load profile.");
        }
    }

    private void loadUserEvents(int vendorId) {
        List<Event> events = DatabaseHandler.getEventsByVendor(vendorId);

        if (events != null && !events.isEmpty()) {
            ObservableList<String> eventList = FXCollections.observableArrayList();
            for (Event event : events) {
                eventList.add(event.getEventName() + " - " + event.getEventDate() + " at " + event.getLocation());
            }
            myEvents.setItems(eventList);
        } else {
            System.out.println("No events found for vendor with ID: " + vendorId);
            showAlert("Information", "No events found for this user.");
        }
    }

    @FXML
    private void goBack() {
        Navigation.goTo("VendorHomeView.fxml", "Vendor Home");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
