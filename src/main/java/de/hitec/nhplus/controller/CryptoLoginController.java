package de.hitec.nhplus.controller;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.CryptoUtils;
import javafx.fxml.FXML;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import javafx.scene.web.WebView;

public class CryptoLoginController {

    @FXML
    protected VBox MainVBox;

    private TextField usernameBox;
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

        if (CryptoUtils.isDBEncrypted() && usernameBox.getText().isEmpty()) {
            // Msg box for empty username
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Sie müssen einen Benutzernamen eingeben!");
            alert.showAndWait();
            return;
        }

        try {

            if (CryptoUtils.isDBEncrypted()) {

                CryptoUtils.loginAsUser(usernameBox.getText(), passwordBox.getText());
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

            if (usernameBox != null)
                usernameBox.clear();
        }

    }

    protected void setupLoginTexts() {
        TitleLabel.setText("NHPlus - Crypto Login");
        labelTag.setText("Bitte geben Sie Ihr Passwort ein:");
        submitButton.setText("Login");

        // add username input field and label
        usernameBox = new TextField();
        usernameBox.setPromptText("Benutzername");

        MainVBox.getChildren().add(0, usernameBox);
        Label usernameLabel = new Label("Username:");
        MainVBox.getChildren().add(0, usernameLabel);

    }

    protected void setupSetup() {
        setupSetupTexts();

        /**
         * Login / Setup Information
         *
         * Herzlich Willkommen zu unserer Anwendung! Bitte beachten Sie die folgenden wichtigen Hinweise zur Sicherheit Ihres Kontos und Ihrer Daten.
         *
         * "INFO ZU IHREM ACCOUNT\n\nMaster Account Name: ADMIN\nPasswort: <Welches Sie jetzt Festlegen>\n\n" +
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

        WebView disclaimerArea = new WebView();
        disclaimerArea.getEngine().loadContent(
                "<h1>Willkommen zu NHPlus!</h1>\n" +
                        "<p>Bitte beachten Sie die folgenden wichtigen Hinweise zur Sicherheit Ihres Kontos und Ihrer Daten.</p>\n" +
                        "<h2>INFO ZU IHREM ACCOUNT</h2>\n" +
                        "<p>Master <b>Account Name</b>: <u>ADMIN</u><br>Passwort: <u>Welches Sie jetzt Festlegen</u></p>\n" +
                        "<h2>Wählen Sie ein sicheres Passwort</h2>\n" +
                        "<ul>\n" +
                        "    <li>Verwenden Sie ein starkes Passwort, das aus mindestens 12 Zeichen besteht und eine Kombination aus Groß- und Kleinbuchstaben, Zahlen und Sonderzeichen enthält.</li>\n" +
                        "    <li>Vermeiden Sie leicht zu erratende Passwörter wie \"123456\", \"Passwort\" oder persönliche Informationen wie Ihren Namen oder Ihr Geburtsdatum.</li>\n" +
                        "</ul>\n" +
                        "<h2>Bewahren Sie Ihr Passwort sicher auf</h2>\n" +
                        "<ul>\n" +
                        "    <li>Speichern Sie Ihr Passwort an einem sicheren Ort. Nutzen Sie am besten einen Passwort-Manager, um Ihre Zugangsdaten sicher zu verwalten.</li>\n" +
                        "    <li>Teilen Sie Ihr Passwort niemals mit anderen Personen und geben Sie es nicht auf unsicheren Webseiten ein.</li>\n" +
                        "</ul>\n" +
                        "<h2>Wichtiger Hinweis</h2>\n" +
                        "<p>Bitte beachten Sie: Ihr Passwort ist der Schlüssel zu Ihren Daten. Aus Sicherheitsgründen haben wir keinen Zugriff auf Ihr Passwort und können es auch nicht wiederherstellen. Wenn Sie Ihr Passwort verlieren, haben Sie keine Möglichkeit mehr, auf Ihre Daten zuzugreifen.</p>\n" +
                        "<h2>Bestätigen und Fortfahren</h2>\n" +
                        "<p>Durch das Fortfahren bestätigen Sie, dass Sie die oben genannten Sicherheitsrichtlinien verstanden und akzeptiert haben. Schützen Sie Ihr Konto und Ihre Daten, indem Sie ein starkes Passwort wählen und sicher aufbewahren.</p>\n" +
                        "<p>Vielen Dank für Ihr Verständnis und Ihre Kooperation.</p>"
        );

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
