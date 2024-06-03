import de.hitec.nhplus.controller.UserCreationController;
import de.hitec.nhplus.model.User;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserCreationControllerTest {

    @Mock
    private TextField idField;
    @Mock
    private TextField usernameField;
    @Mock
    private TextField firstnameField;
    @Mock
    private TextField surnameField;
    @Mock
    private PasswordField passwordField;
    @Mock
    private Button submitButton;
    @Mock
    private CheckBox shouldChangePassword;
    @Mock
    private Label titleLabel;

    @InjectMocks
    private UserCreationController userCreationController;

    @BeforeEach
    public void setUp() {
        userCreationController = new UserCreationController();
        userCreationController.idField = idField;
        userCreationController.usernameField = usernameField;
        userCreationController.firstnameField = firstnameField;
        userCreationController.surnameField = surnameField;
        userCreationController.passwordField = passwordField;
        userCreationController.submitButton = submitButton;
        userCreationController.shouldChangePassword = shouldChangePassword;
        userCreationController.titleLabel = titleLabel;
    }

    @Test
    public void testInitialize() {
        userCreationController.initialize();
        verify(titleLabel).setText("Create new User");
        verify(idField).setDisable(true);
        verify(shouldChangePassword).setDisable(true);
    }

    @Test
    public void testSetUser() {
        User user = new User(1, "testuser", "Test", "User", "hashedKey");
        userCreationController.setUser(user);
        verify(usernameField).setText("testuser");
        verify(firstnameField).setText("Test");
        verify(surnameField).setText("User");
        verify(submitButton).setText("Update User");
        verify(titleLabel).setText("Edit testuser");
    }

    @Test
    public void testHandleCreateUser() {
        when(usernameField.getText()).thenReturn("testuser");
        when(firstnameField.getText()).thenReturn("Test");
        when(surnameField.getText()).thenReturn("User");
        when(passwordField.getText()).thenReturn("password");

        userCreationController.handleCreateUser();
        verify(usernameField, times(1)).getText();
    }
}
