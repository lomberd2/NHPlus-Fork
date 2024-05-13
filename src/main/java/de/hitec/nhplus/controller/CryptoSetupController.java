package de.hitec.nhplus.controller;

import de.hitec.nhplus.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

public class CryptoSetupController {

    @FXML
    protected PasswordField passwordBox;

    @FXML
    protected Button submitButton;

    @FXML
    protected void handleSubmitButton() {
        System.out.println("Password: " + passwordBox.getText());
        Main.mainWindow();
    }
}
