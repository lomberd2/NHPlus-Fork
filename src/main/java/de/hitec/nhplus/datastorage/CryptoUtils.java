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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

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

    public static void login(String password) {
        try {
            key = getKeyFromPassword(password);

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

    public static void setupDBEncryption(String password) {
        if (isDBEncrypted()) {
            System.err.println("DB already encrypted");
            return;
        }

        try {
            key = getKeyFromPassword(password, salt);
            String encrypted = encrypt("Hello World!");
            curCrypto.setTestEncrypted(encrypted);

            cryptoDao.updateCryptoModel(curCrypto);

            System.out.println( Arrays.toString(getAllTablesForEncryption()) );

            // TODO remove on -release- just testing
            throw new RuntimeException("DB encryption setup failed");
            // Dont save yet
            //setDBEncrypted(true);
        } catch (Exception e) {
            key = null;
            System.err.println("DB encryption setup failed");
        }
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
