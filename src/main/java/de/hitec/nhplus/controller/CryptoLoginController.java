package de.hitec.nhplus.controller;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.CryptoUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class CryptoLoginController {

    @FXML
    protected PasswordField passwordBox;

    @FXML
    protected Button submitButton;

    @FXML
    protected Label labelTag;

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
        }

    }

    @FXML
    public void initialize() {
        labelTag.setText("Enter your password");

        if (CryptoUtils.isDBEncrypted()) {
            labelTag.setText("Enter your password");
        } else {
            labelTag.setText("Set your password");
        }
    }
}
