package com.example.realtimeeventticketingsystem.controller;

import com.example.realtimeeventticketingsystem.model.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navigation {
    private static Stage stage;
    private static User currentUser;
    private static int selectedEventId;

    public static void setStage(Stage stage) {
        Navigation.stage = stage;
    }

    public static void goTo(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigation.class.getResource("/view/" + fxml));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

            Object controller = loader.getController();
            if (controller instanceof ProfileController && currentUser != null) {
                ((ProfileController) controller).setUserId(currentUser.getId());
            }
        } catch (Exception e) {
            System.err.println("Failed to load FXML file: " + fxml);
            e.printStackTrace();
        }
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setSelectedEventId(int eventId) {
        selectedEventId = eventId;
    }

    public static int getSelectedEventId() {
        return selectedEventId;
    }

    public static int getCurrentUserId() {
        if (currentUser != null) {
            return currentUser.getId();
        } else {
            throw new IllegalStateException("No user is currently logged in.");
        }
    }

    public static String getUserRole() {
        if (currentUser != null) {
            return currentUser.getRole();
        } else {
            throw new IllegalStateException("No user is currently logged in.");
        }
    }
}

