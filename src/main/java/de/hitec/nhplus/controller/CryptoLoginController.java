package de.hitec.nhplus.controller;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.CryptoUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

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
        if (passwordBox.getText().isEmpty()) {
            // Msg box for empty password
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Sie müssen ein Passwort eingeben!");
            alert.showAndWait();
            return;
        }

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

    protected void setupSetup() {
        setupSetupTexts();

        /**
         * Login / Setup Information
         *
         * Herzlich Willkommen zu unserer Anwendung! Bitte beachten Sie die folgenden wichtigen Hinweise zur Sicherheit Ihres Kontos und Ihrer Daten.
         *
         * Wählen Sie ein sicheres Passwort
         *
         *     Verwenden Sie ein starkes Passwort, das aus mindestens 12 Zeichen besteht und eine Kombination aus Groß- und Kleinbuchstaben, Zahlen und Sonderzeichen enthält.
         *     Vermeiden Sie leicht zu erratende Passwörter wie "123456", "Passwort" oder persönliche Informationen wie Ihren Namen oder Ihr Geburtsdatum.
         *
         * Bewahren Sie Ihr Passwort sicher auf
         *
         *     Speichern Sie Ihr Passwort an einem sicheren Ort. Nutzen Sie am besten einen Passwort-Manager, um Ihre Zugangsdaten sicher zu verwalten.
         *     Teilen Sie Ihr Passwort niemals mit anderen Personen und geben Sie es nicht auf unsicheren Webseiten ein.
         *
         * Wichtiger Hinweis
         *
         * Bitte beachten Sie: Ihr Passwort ist der Schlüssel zu Ihren Daten. Aus Sicherheitsgründen haben wir keinen Zugriff auf Ihr Passwort und können es auch nicht wiederherstellen. Wenn Sie Ihr Passwort verlieren, haben Sie keine Möglichkeit mehr, auf Ihre Daten zuzugreifen.
         *
         * Bestätigen und Fortfahren
         *
         * Durch das Fortfahren bestätigen Sie, dass Sie die oben genannten Sicherheitsrichtlinien verstanden und akzeptiert haben. Schützen Sie Ihr Konto und Ihre Daten, indem Sie ein starkes Passwort wählen und sicher aufbewahren.
         *
         * Vielen Dank für Ihr Verständnis und Ihre Kooperation.
         */

        TextArea disclaimerArea = new TextArea();
        disclaimerArea.setText("Login / Setup Information\n\n" +
                "Herzlich Willkommen zu unserer Anwendung! Bitte beachten Sie die folgenden wichtigen Hinweise zur Sicherheit Ihres Kontos und Ihrer Daten.\n\n" +
                "Wählen Sie ein sicheres Passwort\n\n" +
                "•   Verwenden Sie ein starkes Passwort, das aus mindestens 12 Zeichen besteht und eine Kombination aus Groß- und Kleinbuchstaben, Zahlen und Sonderzeichen enthält.\n" +
                "•   Vermeiden Sie leicht zu erratende Passwörter wie \"123456\", \"Passwort\" oder persönliche Informationen wie Ihren Namen oder Ihr Geburtsdatum.\n\n" +
                "Bewahren Sie Ihr Passwort sicher auf\n\n" +
                "•   Speichern Sie Ihr Passwort an einem sicheren Ort. Nutzen Sie am besten einen Passwort-Manager, um Ihre Zugangsdaten sicher zu verwalten.\n" +
                "•   Teilen Sie Ihr Passwort niemals mit anderen Personen und geben Sie es nicht auf unsicheren Webseiten ein.\n\n" +
                "Wichtiger Hinweis\n\n" +
                "Bitte beachten Sie: Ihr Passwort ist der Schlüssel zu Ihren Daten. Aus Sicherheitsgründen haben wir keinen Zugriff auf Ihr Passwort und können es auch nicht wiederherstellen. Wenn Sie Ihr Passwort verlieren, haben Sie keine Möglichkeit mehr, auf Ihre Daten zuzugreifen.\n\n" +
                "Bestätigen und Fortfahren\n\n" +
                "Durch das Fortfahren bestätigen Sie, dass Sie die oben genannten Sicherheitsrichtlinien verstanden und akzeptiert haben. Schützen Sie Ihr Konto und Ihre Daten, indem Sie ein starkes Passwort wählen und sicher aufbewahren.\n\n" +
                "Vielen Dank für Ihr Verständnis und Ihre Kooperation."
        );
        disclaimerArea.setEditable(false);
        disclaimerArea.setWrapText(true);
        disclaimerArea.setPrefHeight(400);
        disclaimerArea.setPrefWidth(400);

        // Add disclaimer to MainVBox at index 1
        MainVBox.getChildren().add(0, disclaimerArea);
    }

    protected void setupSetupTexts() {
        labelTag.setText("Setzen Sie hier Ihr Passwort:");
        TitleLabel.setText("NHPlus - Crypto Setup");
        submitButton.setText("Setup DB Encryption");
    }

    @FXML
    public void initialize() {
        if (!CryptoUtils.isDBEncrypted()) {
            setupSetup();
            return;
        }

        setupLoginTexts();
    }
}
