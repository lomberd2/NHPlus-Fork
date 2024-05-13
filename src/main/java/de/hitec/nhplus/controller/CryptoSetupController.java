package de.hitec.nhplus.controller;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.Crypto;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.util.Arrays;

public class CryptoSetupController {

    @FXML
    protected PasswordField passwordBox;

    @FXML
    protected Button submitButton;

    @FXML
    protected Label labelTag;

    @FXML
    protected void handleSubmitButton() {
        System.out.println("Password: " + passwordBox.getText());

        try {
            var secretKey = Crypto.getKeyFromPassword(passwordBox.getText(), "salt");
            System.out.println("Secret key: \n" + Arrays.toString(secretKey.getEncoded()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Main.mainWindow();
    }

    @FXML
    public void initialize() {
        labelTag.setText("Enter your password");
    }
}
