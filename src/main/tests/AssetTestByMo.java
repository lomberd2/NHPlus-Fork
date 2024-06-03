import de.hitec.nhplus.datastorage.ConnectionBuilder;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AssetTestByMo {

    // Annotation indicating that this method is a test case
    @Test
    void assetsStillAvailable() {
        // Check if the hasSensitiveDataInDB() method returns true or false
        assertFalse(hasSensitiveDataInDB());
    }

    // Method that checks if sensitive data is still in the database
    public boolean hasSensitiveDataInDB() {
        // Establish a DB connection using the ConnectionBuilder
        Connection connection = ConnectionBuilder.getConnection();

        try {
            // Execute a SQL query to check if there is data in the 'assets' column of the 'patient' table
            return connection.createStatement().executeQuery("SELECT assets FROM patient").next();
        } catch (Exception e) {
            // Print the error message in case of an exception and return false
            System.out.println("There are no sensitive assets in the DB");
            return false;
        }
    }
}
