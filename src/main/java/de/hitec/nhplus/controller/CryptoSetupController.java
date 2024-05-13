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

            //String testString = "Hello World!";
            //var encrypted = Crypto.encrypt(testString, secretKey);

            String encrypted = "eIfg6P6aBzzk5yQmJxgi3Q=="; // "Hello World!"
            System.out.println(encrypted);

            var decrypted = Crypto.decrypt(encrypted, secretKey);

            System.out.println("Decrypted: " + decrypted);
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
