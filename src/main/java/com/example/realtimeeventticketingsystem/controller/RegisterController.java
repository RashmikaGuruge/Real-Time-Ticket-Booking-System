package com.example.realtimeeventticketingsystem.controller;

import com.example.realtimeeventticketingsystem.model.DatabaseHandler;
import com.example.realtimeeventticketingsystem.model.User;
import com.example.realtimeeventticketingsystem.util.SecurityUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class RegisterController {
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private ComboBox<String> roleComboBox;

    public void register() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String role = roleComboBox.getValue();

        if (role != null && !username.isEmpty() && !password.isEmpty()) {
            String hashedPassword = SecurityUtil.hashPassword(password);

            User newUser = new User(username, hashedPassword, email, role);

            DatabaseHandler.registerUser(newUser);

            Navigation.goTo("LoginView.fxml", "Login");
        }
    }

    @FXML
    public void goToLogin() {
        Navigation.goTo("LoginView.fxml", "Login");
    }

}
