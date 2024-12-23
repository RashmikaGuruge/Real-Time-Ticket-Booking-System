package com.example.realtimeeventticketingsystem.controller;

import com.example.realtimeeventticketingsystem.model.DatabaseHandler;
import com.example.realtimeeventticketingsystem.model.Event;
import com.example.realtimeeventticketingsystem.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class AddEventController {

    @FXML private TextField eventNameField;
    @FXML private TextField eventDateField;
    @FXML private TextField eventLocationField;
    @FXML private TextField totalTicketsField;
    @FXML private TextField ticketPriceField;

    @FXML
    private void addEvent() {
        try {
            User currentUser = Navigation.getCurrentUser();
            if (currentUser == null || !"Vendor".equalsIgnoreCase(currentUser.getRole())) {
                showAlert("Error", "You must be logged in as a vendor to add an event.");
                return;
            }

            String name = eventNameField.getText();
            String date = eventDateField.getText();
            String location = eventLocationField.getText();
            String totalTicketsText = totalTicketsField.getText();
            String ticketPriceText = ticketPriceField.getText();


            if (name.isEmpty() || date.isEmpty() || location.isEmpty() || totalTicketsText.isEmpty() || ticketPriceText.isEmpty()) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            int totalTickets;
            double ticketPrice;
            try {
                totalTickets = Integer.parseInt(totalTicketsText);
                ticketPrice = Double.parseDouble(ticketPriceText);
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid numeric values for total tickets or ticket price.");
                return;
            }

            int vendorId = currentUser.getId();
            Event newEvent = new Event(name, date, location, totalTickets, ticketPrice, vendorId);
            if (DatabaseHandler.addEvent(newEvent)) {
                showAlert("Success", "Event added successfully!");
                clearFields();
            } else {
                showAlert("Error", "Failed to add the event.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An unexpected error occurred. Please try again.");
        }
    }

    private void clearFields() {
        eventNameField.clear();
        eventDateField.clear();
        eventLocationField.clear();
        totalTicketsField.clear();
        ticketPriceField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    private void goBack() {
        Navigation.goTo("VendorHomeView.fxml", "Vendor Home");
    }

}
