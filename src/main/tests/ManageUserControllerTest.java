import de.hitec.nhplus.controller.ManageUserController;
import de.hitec.nhplus.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ManageUserControllerTest {

    @Mock
    private TableView<User> tableView;

    @InjectMocks
    private ManageUserController manageUserController;

    @BeforeEach
    public void setUp() {
        manageUserController = new ManageUserController();
        manageUserController.tableView = tableView;
    }

    @Test
    public void testInitialize() {
        manageUserController.initialize();
        verify(tableView, times(1)).getItems().clear();
        verify(tableView, times(1)).getColumns().clear();
    }

    @Test
    public void testHandleDeleteUser() {
        User user = new User(1, "testuser", "Test", "User", "hashedKey");
        when(tableView.getSelectionModel().getSelectedItem()).thenReturn(user);
        manageUserController.handleDeleteUser();
        verify(tableView, times(1)).getSelectionModel().getSelectedItem();
    }
}
