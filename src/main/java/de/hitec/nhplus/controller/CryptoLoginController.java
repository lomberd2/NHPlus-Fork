package de.hitec.nhplus.controller;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.CryptoUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

public class CryptoLoginController {

    @FXML
    protected VBox MainVBox;

    @FXML
    protected PasswordField passwordBox;

    @FXML
    protected Button submitButton;

    @FXML
    protected Label labelTag;
    @FXML
    protected Label TitleLabel;

    @FXML
    protected void handleSubmitButton() {
        //System.out.println("Password: " + passwordBox.getText());

        try {

            if (CryptoUtils.isDBEncrypted()) {
                CryptoUtils.login(passwordBox.getText());
            } else {
                CryptoUtils.setupDBEncryption(passwordBox.getText());
            }

            Main.mainWindow();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());

            // Msg box
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();

            // Clear password field
            passwordBox.clear();
        }

    }

    protected void setupLoginTexts() {
        TitleLabel.setText("NHPlus - Crypto Login");
        labelTag.setText("Bitte geben Sie Ihr Passwort ein:");
        submitButton.setText("Login");
    }

    protected void setupSetupTexts() {
        labelTag.setText("Setzen Sie hier Ihr Passwort:");
        TitleLabel.setText("NHPlus - Crypto Setup");
        submitButton.setText("Setup DB Encryption");
    }

    @FXML
    public void initialize() {
        if (!CryptoUtils.isDBEncrypted()) {
            setupSetupTexts();
        } else {

            setupLoginTexts();
        }
    }
}
