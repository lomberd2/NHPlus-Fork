package de.hitec.nhplus.controller;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.CryptoUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

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

            //System.out.println("Secret key: \n" + Arrays.toString(secretKey.getEncoded()));

            //String testString = "Hello World!";
            //var encrypted = Crypto.encrypt(testString, secretKey);

            String encrypted = "eIfg6P6aBzzk5yQmJxgi3Q=="; // "Hello World!"
            System.out.println(encrypted);

            //var decrypted = Crypto.decrypt(encrypted, secretKey);

            //System.out.println("Decrypted: " + decrypted);

            Main.mainWindow();
        } catch (Exception e) {
            e.printStackTrace();

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
