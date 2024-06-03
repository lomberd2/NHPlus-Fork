import de.hitec.nhplus.controller.MainWindowController;
import de.hitec.nhplus.model.User;
import de.hitec.nhplus.datastorage.CryptoUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MainWindowControllerTest {

    @Mock
    private BorderPane mainBorderPane;

    @InjectMocks
    private MainWindowController mainWindowController;

    @Mock
    private FXMLLoader loader;

    @BeforeEach
    public void setUp() {
        mainWindowController = new MainWindowController();
        mainWindowController.mainBorderPane = mainBorderPane;
    }

    @Test
    public void testHandleShowAllPatient() throws Exception {
        when(loader.load()).thenReturn(new BorderPane());
        mainWindowController.handleShowAllPatient(null);
        verify(mainBorderPane, times(1)).setCenter(any());
    }

    @Test
    public void testHandleShowAllCaregiver() throws Exception {
        when(loader.load()).thenReturn(new BorderPane());
        mainWindowController.handleShowAllCaregiver(null);
        verify(mainBorderPane, times(1)).setCenter(any());
    }

    @Test
    public void testCheckForAdmin() {
        User admin = new User(1, "admin", "Admin", "User", "hashedKey");
        when(CryptoUtils.getCurrentUser()).thenReturn(admin);
        mainWindowController.checkForAdmin();
        verify(mainBorderPane, times(1)).setTop(any());
    }
}
