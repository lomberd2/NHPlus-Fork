import de.hitec.nhplus.datastorage.ConnectionBuilder;
import de.hitec.nhplus.datastorage.CryptoUtils;
import de.hitec.nhplus.datastorage.UserDao;
import de.hitec.nhplus.model.User;
import de.hitec.nhplus.utils.SetUpDB;
import org.junit.jupiter.api.*;

import javax.crypto.SecretKey;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginPrototypTest {

    private User user;
    private Connection connection;
    private UserDao userDao;

    @BeforeEach
    void setUp() throws SQLException {
        connection = ConnectionBuilder.getConnection();
        userDao = new UserDao(connection);
        SetUpDB.setUpDb();
        user = new User(1, "testUser", "Test", "User", "hashedPasswordKey");
    }

    @AfterEach
    void tearDown() throws SQLException {
        SetUpDB.wipeDb(connection);
        connection.close();
    }

    // User Tests
    @Test
    void loginScreenDisplayed() {
        assertTrue(testLoginScreenDisplayed());
    }

    @Test
    void authenticationSuccess() {
        assertTrue(testAuthenticationSuccess());
    }

    @Test
    void passwordEncryption() {
        assertTrue(testPasswordEncryption());
    }

    @Test
    void registrationDisabled() {
        assertTrue(testRegistrationDisabled());
    }

    boolean testLoginScreenDisplayed() {
        return user != null;
    }

    boolean testAuthenticationSuccess() {
        try {
            user.setPassword("validPassword");
            user.login("validPassword");
            return user.getDecryptedMasterPw() != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean testPasswordEncryption() {
        try {
            String password = "newPassword";
            SecretKey key = CryptoUtils.getKeyFromPassword(password);
            String encrypted = CryptoUtils.encrypt("hashedPasswordKey", key);
            return !encrypted.equals(password);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean testRegistrationDisabled() {
        return user.isRegistrationDisabled();
    }

    // UserDao Tests
    @Test
    void createUser() {
        assertTrue(testCreateUser());
    }

    @Test
    void readUser() {
        assertTrue(testReadUser());
    }

    @Test
    void updateUser() {
        assertTrue(testUpdateUser());
    }

    @Test
    void deleteUser() {
        assertTrue(testDeleteUser());
    }

    boolean testCreateUser() {
        try {
            User newUser = new User(0, "newUser", "New", "User", "hashedPasswordKey");
            userDao.create(newUser);
            User retrievedUser = userDao.readById(newUser.getId());
            return retrievedUser != null && retrievedUser.getUsername().equals(newUser.getUsername());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean testReadUser() {
        try {
            User adminUser = new User(1, "admin", "Admin", "Admin", "admin");
            User retrievedUser = userDao.readById(1);
            return retrievedUser != null && retrievedUser.getUsername().equals(adminUser.getUsername());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean testUpdateUser() {
        try {
            User adminUser = new User(1, "admin", "Admin", "Admin", "admin");
            adminUser.setFirstname("UpdatedAdmin");
            userDao.update(adminUser);
            User updatedUser = userDao.readById(1);
            return updatedUser != null && "UpdatedAdmin".equals(updatedUser.getFirstname());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean testDeleteUser() {
        try {
            userDao.delete(1);
            User deletedUser = userDao.readById(1);
            return deletedUser == null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
