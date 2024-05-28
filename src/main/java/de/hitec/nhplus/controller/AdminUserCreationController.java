package de.hitec.nhplus.controller;

import de.hitec.nhplus.datastorage.CryptoUtils;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class AdminUserCreationController {

    // FXML-Annotation binden die UI-Elemente an Variablen
    @FXML
    private TextField nameField;

    @FXML
    private TextField descriptionField;

    @FXML
    private PasswordField passwordField;

    // Methode zum Erstellen eines neuen Benutzers
    @FXML
    private void handleCreateUser() {
        String name = nameField.getText();
        String description = descriptionField.getText();
        String password = passwordField.getText();

        // Generieren von Salt und gehashtem Passwort
        try {
            byte[] salt = CryptoUtils.generateSalt();
            String hashedPassword = CryptoUtils.hashPassword(password, salt);
            String hashedMasterPwKey = CryptoUtils.hashMasterPassword(password, salt); // Assuming you have a method for this

            // Erstellen eines neuen User-Objekts und Speichern in der Datenbank
            User user = new User(name, description, hashedPassword, hashedMasterPwKey);
            DaoFactory.getDaoFactory().createUserDao().create(user);

            showAlert("Success", "User created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while creating the user.");
        }
    }
    
    // Methode zum anzeigen des Alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
