package com.example.realtimeeventticketingsystem.controller;

import com.example.realtimeeventticketingsystem.model.User;
import com.example.realtimeeventticketingsystem.model.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    public void login() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and password cannot be empty.");
            return;
        }

        try {
            User user = DatabaseHandler.authenticateUser(username, password);

            if (user != null) {
                Navigation.setCurrentUser(user);

                if ("Vendor".equals(user.getRole())) {
                    Navigation.goTo("VendorHomeView.fxml", "Vendor Home");
                } else {
                    Navigation.goTo("CustomerHomeView.fxml", "Customer Home");
                }
            } else {
                showAlert("Error", "Invalid login credentials.");
                lockLoginButton();
            }
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred. Please try again.");
            logError("Login error for user: " + username, e);
        }
    }


    @FXML
    public void goToRegister() {
        try {
            Navigation.goTo("RegisterView.fxml", "Register");
        } catch (Exception e) {
            showAlert("Navigation Error", "Failed to load the registration view.");
            logError("Navigation error to RegisterView.fxml", e);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    private void lockLoginButton() {
        loginButton.setDisable(true);
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            } finally {
                loginButton.setDisable(false);
            }
        }).start();
    }

    private void logError(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
    }
}
