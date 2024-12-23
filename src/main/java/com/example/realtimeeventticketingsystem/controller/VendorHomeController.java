package com.example.realtimeeventticketingsystem.controller;

import com.example.realtimeeventticketingsystem.model.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class VendorHomeController {

    public void initialize() {

    }

    @FXML
    private void addEvent() {
        try {
            Navigation.goTo("AddEventView.fxml", "Add Event");
        } catch (Exception e) {
            showAlert("Navigation Error", "Unable to open the Add Event page.");
        }
    }

    @FXML
    private void viewProfile() {
        try {
            Navigation.goTo("ProfileView.fxml", "View Profile");
        } catch (Exception e) {
            showAlert("Navigation Error", "Unable to open the Profile page.");
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
