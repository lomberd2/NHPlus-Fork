import de.hitec.nhplus.datastorage.ConnectionBuilder;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertFalse;


public class AssetTestByMo {

    @Test
    void assetsStillAvailable(){

       assertFalse(hasSensitiveDataInDB());
    }

    public boolean hasSensitiveDataInDB( ) {
        Connection connection = ConnectionBuilder.getConnection();

        try {
            return connection.createStatement().executeQuery("SELECT assets FROM patient").next();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
