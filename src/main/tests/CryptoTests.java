import de.hitec.nhplus.datastorage.CryptoUtils;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

public class CryptoTests {

    @Test
    void canEncrypt() {
        assert testCanEncrypt();
    }

    @Test
    void canDecrypt() {
        assert testCanDecrypt();
    }

    boolean testCanEncrypt() {
        String testString = "This is a test string";

        try {
            SecretKey key = CryptoUtils.getKeyFromPassword("password");
            String encrypted = CryptoUtils.encrypt(testString, key);
            return !encrypted.equals(testString);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean testCanDecrypt() {
        String testString = "This is a test string";

        try {
            SecretKey key = CryptoUtils.getKeyFromPassword("password");

            String encrypted = CryptoUtils.encrypt(testString, key);
            String decrypted = CryptoUtils.decrypt(encrypted, key);

            return decrypted.equals(testString);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
