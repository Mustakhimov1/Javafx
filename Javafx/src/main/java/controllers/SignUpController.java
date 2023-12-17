package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import models.UserProfile;
import utils.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField addressField;

    @FXML
    private Button signUpButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label errorMessageLabel;

    private Connection connection;

    @FXML
    public void initialize() {
        connection = DBConnector.getConnection();
        signUpButton.setOnAction(event -> handleSignUp());
        cancelButton.setOnAction(event -> handleCancel());
    }

    private void handleSignUp() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String fullName = fullNameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String address = addressField.getText();

        if (!password.equals(confirmPassword)) {
            showErrorMessage("Passwords do not match");
            return;
        }

        try {
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted == 1) {
                int userId = getUserId(username);
                UserProfile userProfile = new UserProfile();
                userProfile.setUserId(userId);
                userProfile.setFullName(fullName);
                userProfile.setAge(age);
                userProfile.setAddress(address);
                insertUserProfile(userProfile);
                showSuccessMessage();
            } else {
                showErrorMessage("Error creating user");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private int getUserId(String username) throws SQLException {
        String query = "SELECT id FROM users WHERE username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt("id");
        } else {
            return -1;
        }
    }

    private void insertUserProfile(UserProfile userProfile) throws SQLException {
        String query = "INSERT INTO user_profiles (user_id, full_name, age, address) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userProfile.getUserId());
        statement.setString(2, userProfile.getFullName());
        statement.setInt(3, userProfile.getAge());
        statement.setString(4, userProfile.getAddress());
        statement.executeUpdate();
    }

    private void showSuccessMessage() {
        errorMessageLabel.setStyle("-fx-text-fill: green");
        errorMessageLabel.setText("User created successfully");
    }

    private void showErrorMessage(String message) {
        errorMessageLabel.setStyle("-fx-text-fill: red");
        errorMessageLabel.setText(message);
    }
}
