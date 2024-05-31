package de.hitec.nhplus.controller;

import de.hitec.nhplus.datastorage.CryptoUtils;
import de.hitec.nhplus.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class UserCreationController {

    @FXML
    private Label titleLabel;

    @FXML
    private TextField idField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField firstnameField;
    @FXML
    private TextField surnameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button submitButton;
    @FXML
    private CheckBox shouldChangePassword;

    public UserCreationController() {
    }

    /**
     * Set the fields of the form to the values of the given user.
     * Also sets the title and submit button text accordingly.
     *
     * @param user the user to set the fields to
     */
    public void setUser(User user) {
        idField.setText(String.valueOf(user.getId()));
        usernameField.setText(user.getUsername());
        firstnameField.setText(user.getFirstname());
        surnameField.setText(user.getSurname());

        // enable optional fields
        shouldChangePassword.setDisable(false);

        // set default values and texts
        submitButton.setText("Update User");
        titleLabel.setText("Edit ".concat(user.getUsername()));

        passwordField.setPromptText("Leave empty to keep current password");
        // set default value for shouldChangePassword if input <> empty
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            shouldChangePassword.setSelected(!newValue.isEmpty());
        });

    }

    @FXML
    public void initialize() {
        titleLabel.setText("Create new User");

        // disable (hide) optional fields
        idField.setDisable(true);
        shouldChangePassword.setDisable(true);
    }


    @FXML
    private void handleCreateUser() {

        if (usernameField.getText().isEmpty() || firstnameField.getText().isEmpty() || surnameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            showAlert("Error", "Please fill out all fields");
            return;
        }

        User user = new User(0, usernameField.getText(), firstnameField.getText(), surnameField.getText(), passwordField.getText(), shouldChangePassword.isSelected());

        // save user to database
        User savedUser = CryptoUtils.createNewUser(user, passwordField.getText());

        if (savedUser != null) {
            showAlert("Success", "User created successfully");
        } else {
            showAlert("Error", "User could not be created");
        }

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
