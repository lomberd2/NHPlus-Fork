import de.hitec.nhplus.datastorage.ConnectionBuilder;
import de.hitec.nhplus.datastorage.CryptoUtils;
import de.hitec.nhplus.datastorage.UserDao;
import de.hitec.nhplus.model.User;
import de.hitec.nhplus.utils.SetUpDB;
import org.junit.jupiter.api.*;

import javax.crypto.SecretKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginPrototypTest {

    private static User user;
    private static Connection connection;

    @BeforeAll
    static void setUp() {
        connection = ConnectionBuilder.getConnection();
        SetUpDB.setUpDb();
        user = new User(1, "testUser", "Test", "User", "hashedPasswordKey");
    }

    @AfterAll
    static void tearDown() throws SQLException {
        SetUpDB.wipeDb(connection);
        connection.close();
    }

    // UserDao Tests
    @Test
    @Order(1)
    void createUser() {
        assertTrue(testCreateUser());
    }

    @Test
    @Order(2)
    void readUser() {
        assertTrue(testReadUser());
    }

    @Test
    @Order(3)
    void updateUser() {
        assertTrue(testUpdateUser());
    }

    @Test
    @Order(4)
    void deleteUser() {
        assertTrue(testDeleteUser());
    }

    boolean testCreateUser() {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO user (id, username, firstname, surname, hashedMasterPwKey) VALUES (?, ?, ?, ?, ?)");
            statement.setInt(1, (int) user.getId());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getFirstname());
            statement.setString(4, user.getSurname());
            statement.setString(5, user.getHashedMasterPwKey());
            statement.executeUpdate();

            User createdUser = null;

            ResultSet executeQuery = connection.createStatement().executeQuery("SELECT * FROM user WHERE id = 1");
            if (executeQuery.next()) {
                createdUser = new User(executeQuery.getInt("id"), executeQuery.getString("username"), executeQuery.getString("firstname"), executeQuery.getString("surname"), executeQuery.getString("hashedMasterPwKey"));
            }

            return createdUser != null && createdUser.getUsername().equals(user.getUsername());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean testReadUser() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user WHERE id = 1");
            ResultSet resultSet = statement.executeQuery();
            User readUser = null;

            if (resultSet.next()) {
                readUser = new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("firstname"), resultSet.getString("surname"), resultSet.getString("hashedMasterPwKey"));
            }

            return readUser != null && readUser.getUsername().equals(user.getUsername());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean testUpdateUser() {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE user SET username = ? WHERE id = 1");
            statement.setString(1, "newUsername");
            statement.executeUpdate();

            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM user WHERE id = 1");
            User updatedUser = null;

            if (resultSet.next()) {
                updatedUser = new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("firstname"), resultSet.getString("surname"), resultSet.getString("hashedMasterPwKey"));
            }

            return updatedUser != null && updatedUser.getUsername().equals("newUsername");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean testDeleteUser() {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM user WHERE id = 1");
            statement.executeUpdate();

            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM user WHERE id = 1");
            return !resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
