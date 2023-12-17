package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.UserProfile;
import utils.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Label fullNameLabel;

    @FXML
    private Label ageLabel;

    @FXML
    private Label addressLabel;

    private Connection connection;

    @FXML
    public void initialize() {
        connection = DBConnector.getConnection();
    }

    public void setUserProfile(UserProfile userProfile) {
        try {
            String query = "SELECT username, full_name, age, address FROM users INNER JOIN user_profiles ON users.id = user_profiles.user_id WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userProfile.getUserId());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String fullName = resultSet.getString("full_name");
                int age = resultSet.getInt("age");
                String address = resultSet.getString("address");

                usernameLabel.setText(username);
                fullNameLabel.setText(fullName);
                ageLabel.setText(String.valueOf(age));
                addressLabel.setText(address);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
