package de.hitec.nhplus;

import de.hitec.nhplus.datastorage.ConnectionBuilder;

import de.hitec.nhplus.datastorage.CryptoUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import static de.hitec.nhplus.datastorage.CryptoUtils.getAllTablesForEncryption;

public class Main extends Application {

    protected static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        //mainWindow();
        cryptoSetup();
    }

    public static void mainWindow() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/MainWindowView.fxml"));
        Main.loadScene(loader, "NHPlus");
    }

    public static void cryptoSetup() {
        CryptoUtils.init();

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/CryptoSetupView.fxml"));
        Main.loadScene(loader, "NHPlus - Crypto Setup");
    }

    public static void loadScene(FXMLLoader loader, String title) {
        try {
            BorderPane pane = loader.load();

            Scene scene = new Scene(pane);
            Main.primaryStage.setTitle(title);
            Main.primaryStage.setScene(scene);
            Main.primaryStage.setResizable(false);
            Main.primaryStage.show();

            Main.primaryStage.setOnCloseRequest(event -> {
                CryptoUtils.logout();

                ConnectionBuilder.closeConnection();
                Platform.exit();
                System.exit(0);
            });
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}