package com.example.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HelloController {


    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private void initialize() {
        // Initialize the UI elements here
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        // Handle login button click here
    }

    @FXML
    private void handleSignupButton(ActionEvent event) {
        // Handle signup button click here
    }

    public void setStage(Stage primaryStage) {
    }
}
