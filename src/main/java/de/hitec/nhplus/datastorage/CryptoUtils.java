package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.CryptoModel;
import de.hitec.nhplus.utils.DbUtils;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class CryptoUtils {
    private final static String algorithm = "AES";
    private final static String salt = "NHPlusSalt";
    protected static SecretKey key = null;

    protected static CryptoDao cryptoDao;
    protected static CryptoModel curCrypto;

    public static void init() {
        cryptoDao = DaoFactory.getDaoFactory().createCryptoDAO();
        loadCryptoModel();
    }

    private static void loadCryptoModel() {
        curCrypto = cryptoDao.getCryptoModel();
    }

    public static SecretKey getKeyFromPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return getKeyFromPassword(password, salt);
    }

    public static SecretKey getKeyFromPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 69420, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), algorithm);
    }

    public static String encrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return encrypt(input, key);
    }

    public static String encrypt(String input, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return decrypt(cipherText, key);
    }

    public static String decrypt(String cipherText, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainText);
    }

    public static SealedObject encryptObject(Serializable object) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException {
        return encryptObject(object, key);
    }

    public static SealedObject encryptObject(Serializable object, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return new SealedObject(object, cipher);
    }

    public static Serializable decryptObject(SealedObject sealedObject) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, ClassNotFoundException, BadPaddingException, IllegalBlockSizeException, IOException {
        return decryptObject(sealedObject, key);
    }

    public static Serializable decryptObject(SealedObject sealedObject, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, ClassNotFoundException, BadPaddingException, IllegalBlockSizeException, IOException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return (Serializable) sealedObject.getObject(cipher);
    }

    public static void login(String password) throws Exception  {
        Connection connection = ConnectionBuilder.getConnection();

        try {
            // Generate the secret key from the password
            key = getKeyFromPassword(password);

            // Retrieve the encrypted test string from the CryptoModel
            String encryptedTestString = curCrypto.getTestEncrypted();

            // Decrypt the test string to verify if the provided password is correct
            String decrypted = decrypt(encryptedTestString);

            // Check if the decrypted string matches the expected value
            if (!decrypted.equals("Hello World!")) {
                key = null; // Clear the key if the password is incorrect
                throw new Exception("Login failed, DB cannot be decrypted");
            }

            // Decrypt and restore all tables
            for (String table : getAllTablesForDecryption()) {
                System.out.println("Decrypting table: " + table);

                PreparedStatement selectStatement = connection.prepareStatement("SELECT id, encrypted_data FROM encrypted_" + table);
                ResultSet resultSet = selectStatement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String encryptedData = resultSet.getString("encrypted_data");
                    String decryptedData = decrypt(encryptedData);

                    System.out.println("Decrypted data: " + decryptedData);

                    // Assuming the decrypted data is a CSV string, split it into individual values
                    String[] values = decryptedData.split(",");

                    // Create the insert statement dynamically based on the column count
                    StringBuilder insertQuery = new StringBuilder("INSERT INTO " + table + " VALUES (");
                    for (int i = 0; i < values.length; i++) {
                        insertQuery.append("?");
                        if (i < values.length - 1) {
                            insertQuery.append(", ");
                        }
                    }
                    insertQuery.append(")");

                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery.toString());
                    for (int i = 0; i < values.length; i++) {
                        var curValue = values[i];

                        // replace sanitized , values
                        if (curValue.contains("[ÐæÑ]")) {
                            curValue = curValue.replace("[ÐæÑ]", ",");
                        }

                        insertStatement.setString(i + 1, curValue);
                    }

                    insertStatement.executeUpdate();
                }

                // Optionally, drop the encrypted table after decryption
                //connection.createStatement().execute("DROP TABLE encrypted_" + table);
            }
        } catch (Exception e) {
            e.printStackTrace();
            key = null; // Clear the key in case of any exception
            throw new RuntimeException("Login failed, DB cannot be decrypted", e);
        }
    }

    public static void setupDBEncryption(String password) throws Exception {
        if (isDBEncrypted()) {
            System.err.println("DB already encrypted");
            return;
        }
        Connection connection = ConnectionBuilder.getConnection();

        try {
            key = getKeyFromPassword(password, salt);
            String encrypted = encrypt("Hello World!");
            curCrypto.setTestEncrypted(encrypted);

            cryptoDao.updateCryptoModel(curCrypto);

            System.out.println(Arrays.toString(getAllTablesForEncryption()));

            for (String table : getAllTablesForEncryption()) {
                System.out.println("Encrypting table: " + table);
                connection.createStatement().execute("CREATE TABLE IF NOT EXISTS encrypted_" + table + " (id INTEGER PRIMARY KEY, encrypted_data BLOB)");

                PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM " + table);
                ResultSet resultSet = selectStatement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    StringBuilder rowData = new StringBuilder();

                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        if (i > 1) rowData.append(",");
                        String value = resultSet.getString(i);

                        // sanitize , values
                        if (value.contains(",")) {
                            value = value.replace(",", "[ÐæÑ]");
                        }

                        rowData.append(value);
                    }

                    String encryptedData = encrypt(rowData.toString());
                    PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO encrypted_" + table + " (id, encrypted_data) VALUES (?, ?)");
                    insertStatement.setInt(1, id);
                    insertStatement.setString(2, encryptedData);
                    insertStatement.executeUpdate();
                }
            }

            // Uncomment the next line to finalize the encryption process
            setDBEncrypted(true);
        } catch (Exception e) {
            e.printStackTrace();
            key = null;
            System.err.println("DB encryption setup failed");
        } finally {
            // Delete values from the original tables after encryption
            //for (String table : getAllTablesForEncryption()) {
            //    connection.createStatement().execute("DELETE FROM " + table);
            //}
        }


        // TODO remove on -release- just testing
        //throw new Exception("DB encryption setup failed");
    }

    public static boolean isDBEncrypted() {
        return curCrypto.getIsDBEncrypted();
    }

    public static void setDBEncrypted(boolean isDBEncrypted) {
        curCrypto.setIsDBEncrypted(isDBEncrypted);
        cryptoDao.updateCryptoModel(curCrypto);
    }

    /**
     * This method retrieves all the table names from the database that are eligible for encryption.
     * It filters out the 'crypto' table as it should not be encrypted.
     *
     * @return An array of table names that are eligible for encryption.
     */
    public static String[] getAllTablesForEncryption() {
        var tables = new ArrayList<String>();

        Collections.addAll(tables, Arrays.stream(DbUtils.getAllTables()).filter(s -> !s.equals("crypto") ).toArray(String[]::new));

        return tables.toArray(new String[0]);
    }

    public static String[] getAllTablesForDecryption() {
        var tables = new ArrayList<String>();

        Collections.addAll(tables, Arrays.stream(getAllTablesForEncryption()).filter(s -> !s.startsWith("encrypted") ).toArray(String[]::new));

        return tables.toArray(new String[0]);
    }
}
