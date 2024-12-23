package com.example.realtimeeventticketingsystem.controller;

import com.example.realtimeeventticketingsystem.model.DatabaseHandler;
import com.example.realtimeeventticketingsystem.model.Event;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class BookTicketsController {

    @FXML private Label ticketPriceLabel;
    @FXML private Label eventNameLabel;
    @FXML private Label eventDateLabel;
    @FXML private Label locationLabel;
    @FXML private Label availableTicketsLabel;
    @FXML private TextField quantityField;
    @FXML private Label totalPriceLabel;

    private double ticketPrice;

    public void initialize() {
        int eventId = Navigation.getSelectedEventId();
        loadEventDetails(eventId);

        quantityField.textProperty().addListener((observable, oldValue, newValue) -> calculateTotalPrice(newValue));
    }

    private void loadEventDetails(int eventId) {
        Event event = DatabaseHandler.getEventById(eventId);
        if (event != null) {
            eventNameLabel.setText(event.getEventName());
            eventDateLabel.setText(event.getEventDate());
            locationLabel.setText(event.getLocation());
            availableTicketsLabel.setText(String.valueOf(event.getTotalTickets()));
            ticketPrice = event.getTicketPrice(); // Store the ticket price
            ticketPriceLabel.setText(String.format("%.2f", ticketPrice));
            totalPriceLabel.setText("0.00"); // Initialize total price to 0
        } else {
            showAlert("Error", "Failed to load event details.");
        }
    }

    private void calculateTotalPrice(String newValue) {
        try {
            int quantity = Integer.parseInt(newValue);
            double totalPrice = quantity * ticketPrice;
            totalPriceLabel.setText(String.format("%.2f", totalPrice));
        } catch (NumberFormatException e) {
            totalPriceLabel.setText("0.00");
        }
    }

    @FXML
    private void bookTickets() {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            int eventId = Navigation.getSelectedEventId();
            int customerId = Navigation.getCurrentUserId();

            boolean success = DatabaseHandler.bookTickets(eventId, customerId, quantity);
            if (success) {
                showAlert("Success", "Tickets booked successfully.");
                Navigation.goTo("CustomerHomeView.fxml", "Customer Home");
            } else {
                showAlert("Error", "Booking failed. Please check available tickets.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid ticket quantity.");
        }
    }

    @FXML
    private void goBack() {
        Navigation.goTo("CustomerHomeView.fxml", "Customer Home");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
