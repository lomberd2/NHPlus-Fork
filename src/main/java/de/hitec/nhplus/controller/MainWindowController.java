package de.hitec.nhplus.controller;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.CryptoUtils;
import de.hitec.nhplus.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainWindowController {

    @FXML
    private BorderPane mainBorderPane;

    private User user;

    @FXML
    private void handleShowAllPatient(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/AllPatientView.fxml"));
        try {
            checkForAdmin();
            mainBorderPane.setCenter(loader.load());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void handleShowAllTreatments(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/AllTreatmentView.fxml"));
        try {
            checkForAdmin();
            mainBorderPane.setCenter(loader.load());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void checkForAdmin() {
        if (user == null) {
            // if admin add admin context menu
            User currentUser = CryptoUtils.getCurrentUser();
            if (currentUser != null && currentUser.getUsername().equals("admin")) {
                // add context menu

                var menuBar = new javafx.scene.control.MenuBar();
                var menu = new javafx.scene.control.Menu("Admin");
                var manageUserItem = new javafx.scene.control.MenuItem("Manage User");
                var menuItem = new javafx.scene.control.MenuItem("Create User");
                menuItem.setOnAction(e -> Main.showAdminUserCreation());
                manageUserItem.setOnAction(e -> Main.showManageUser());

                this.mainBorderPane.setTop(menuBar);

                menu.getItems().add(manageUserItem);
                menu.getItems().add(menuItem);

                menuBar.getMenus().add(menu);

                this.user = currentUser;
            }
        }
    }
}
