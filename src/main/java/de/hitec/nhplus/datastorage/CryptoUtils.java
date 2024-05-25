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
            key = getKeyFromPassword(password);

            // Unlock the database
            connection.prepareStatement("PRAGMA key = '" + Base64.getEncoder().encodeToString(key.getEncoded()) + "'").executeUpdate();

            String decrypted = decrypt(curCrypto.getTestEncrypted());

            if (!decrypted.equals("Hello World!")) {
                key = null;
                throw new Exception("Login failed, db cannot be decrypted");
            }
        } catch (Exception e) {
            key = null;
            throw new RuntimeException("Login failed, db cannot be decrypted");
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

            System.out.println( Arrays.toString(getAllTablesForEncryption()) );

            for (String table : getAllTablesForEncryption()) {
                System.out.println("Encrypting table: " + table);
                connection.createStatement().execute("CREATE TABLE encrypted_" + table + " (id INTEGER PRIMARY KEY, encrypted_data BLOB)");

                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + table );

                var resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    // Temporarily store the row
                    var row = new HashMap<String, String>();

                    // Encrypt all columns
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        String column = resultSet.getMetaData().getColumnName(i);
                        String value = resultSet.getString(i);
                        String encryptedValue = encrypt(value);

                        System.out.println("Column: " + column + " Value: " + value + " Encrypted: " + encryptedValue);

                        row.put(column, encryptedValue);
                    }

                    // Update the row with the encrypted values
                    String updateQuery = "UPDATE " + table + " SET ";

                    for (var entry : row.entrySet()) {
                        updateQuery += entry.getKey() + " = ?, ";
                    }

                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery.substring(0, updateQuery.length() - 2) + " WHERE id = " + resultSet.getInt("id"));


                    updateStatement.executeUpdate();

                }
            }

            // Dont save yet
            //setDBEncrypted(true);
        } catch (Exception e) {
            e.printStackTrace();
            key = null;
            System.err.println("DB encryption setup failed");
        }

        // TODO remove on -release- just testing
        throw new Exception("DB encryption setup failed");
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
}
