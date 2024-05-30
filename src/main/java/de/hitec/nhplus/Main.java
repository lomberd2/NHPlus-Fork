package de.hitec.nhplus;

import de.hitec.nhplus.controller.UserCreationController;
import de.hitec.nhplus.datastorage.ConnectionBuilder;

import de.hitec.nhplus.datastorage.CryptoUtils;
import de.hitec.nhplus.model.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    protected static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        //mainWindow();
        cryptoSetupLogin();

        //showManageUser();
    }

    public static void mainWindow() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/MainWindowView.fxml"));
        Main.loadScene(loader, "NHPlus");
    }

    public static void cryptoSetupLogin() {
        CryptoUtils.init();

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/CryptoLoginView.fxml"));
        Main.loadScene(loader, "NHPlus - Crypto Setup / Login");
    }

    public static void showAdminUserCreation() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/UserCreationView.fxml"));
        loadSceneAsPopup(loader, "NHPlus - Create Admin User");
    }

    public static void showAdminUserCreation( User user ) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/UserCreationView.fxml"));
        loadSceneAsPopup(loader, "NHPlus - Create Admin User");

        UserCreationController controller = loader.getController();
        controller.setUser(user);
    }

    public static void showManageUser() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/ManageUserView.fxml"));
        loadSceneAsPopup(loader, "NHPlus - Manage Users");
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

    public static void loadSceneAsPopup(FXMLLoader loader, String title) {
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage dialog = new Stage();
            dialog.setTitle(title);
            dialog.setScene(scene);
            dialog.setResizable(false);
            dialog.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}