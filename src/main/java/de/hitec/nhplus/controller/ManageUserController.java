package de.hitec.nhplus.controller;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.ConnectionBuilder;
import de.hitec.nhplus.datastorage.CryptoUtils;
import de.hitec.nhplus.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Window;

import java.lang.reflect.Field;
import java.sql.Connection;

public class ManageUserController {
    @FXML
    private TableView<User> tableView;
    @FXML
    private TextField idField;

    @FXML
    private HBox selectedUserBox;


    @FXML
    private void initialize() {
        // disable (hide) optional fields
        selectedUserBox.setVisible(false);

        initTable();
    }

    private void initTable() {
        tableView.getItems().clear();
        tableView.getColumns().clear();

        Connection connection = ConnectionBuilder.getConnection();
        if (connection == null) {
            return;
        }

        // Set up table columns
        for (Field field : User.class.getDeclaredFields()) {
            if (field.getName().equals("password") || field.getName().equals("secretKey") )
                continue;

            TableColumn<User, String> column = new TableColumn<>(field.getName());
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            tableView.getColumns().add(column);
        }

        // Load data from database
        try {

            connection.prepareStatement("SELECT * FROM user");
            var resultSet = connection.createStatement().executeQuery("SELECT * FROM user");

            while (resultSet.next()) {
                User user = User.fromResultSet(resultSet);
                tableView.getItems().add(user);
            }

        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }

        // Init interaction handlers
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                idField.setText(String.valueOf(newValue.getId()));
                selectedUserBox.setVisible(true);
            } else {
                selectedUserBox.setVisible(false);
            }
        });

        // Double click to edit user
        tableView.setOnMouseClicked(event -> {
            if (tableView.getSelectionModel().getSelectedItem() == null)
                return;

            if (event.getClickCount() == 2 && lastClickedUser == tableView.getSelectionModel().getSelectedItem())
                handleEditUser();
            else
                lastClickedUser = tableView.getSelectionModel().getSelectedItem();

        });
    }

    protected User lastClickedUser;

    @FXML
    private void handleDeleteUser() {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null)
            return;

        CryptoUtils.deleteUser(selectedUser);
    }

    @FXML
    private void handleEditUser() {
        // Get selected user and pass it to the user creation controller
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        Main.showAdminUserCreation(selectedUser);
    }

    @FXML
    private void handleCreateUser() {
        Main.showAdminUserCreation();
    }

    @FXML
    private void handleClose() {
        Window window = this.selectedUserBox.getParent().getScene().getWindow();
        window.fireEvent(new javafx.stage.WindowEvent(window, javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST));
    }

}
