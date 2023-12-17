package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import models.UserProfile;
import utils.DBConnector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;

    @FXML
    private Label errorMessageLabel;

    private Connection connection;

    @FXML
    public void initialize() {
        connection = DBConnector.getConnection();
        loginButton.setOnAction(event -> handleLogin());
        signUpButton.setOnAction(event -> handleSignUp());
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                UserProfile userProfile = getUserProfile(userId);
                showUserProfile(userProfile);
            } else {
                showErrorMessage("Invalid username or password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleSignUp() {
        try {
            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.close();

            Stage signUpStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SignUp.fxml"));
            Parent root = loader.load();
            SignUpController signUpController = loader.getController();
            signUpStage.setScene(new Scene(root));
            signUpStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private UserProfile getUserProfile(int userId) throws SQLException {
        String query = "SELECT * FROM user_profiles WHERE user_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            UserProfile userProfile = new UserProfile();
            userProfile.setId(resultSet.getInt("id"));
            userProfile.setUserId(userId);
            userProfile.setFullName(resultSet.getString("full_name"));
            userProfile.setAge(resultSet.getInt("age"));
            userProfile.setAddress(resultSet.getString("address"));
            return userProfile;
        } else {
            return null;
        }
    }

    private void showUserProfile(UserProfile userProfile) {
        try {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();

            Stage userProfileStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserProfile.fxml"));
            Parent root = loader.load();
            UserProfileController userProfileController = loader.getController();
            userProfileController.setUserProfile(userProfile);
            userProfileStage.setScene(new Scene(root));
            userProfileStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showErrorMessage(String message) {
        errorMessageLabel.setText(message);
    }
}
