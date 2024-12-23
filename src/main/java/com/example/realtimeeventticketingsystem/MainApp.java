package com.example.realtimeeventticketingsystem;


import com.example.realtimeeventticketingsystem.controller.Navigation;
import com.example.realtimeeventticketingsystem.model.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize database and tables
        DatabaseHandler.setupTables();

        // Set up the primary stage for navigation and load the login screen
        Navigation.setStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));

        primaryStage.setTitle("Ticket Booking System");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);  // This starts the JavaFX application lifecycle
    }
}

