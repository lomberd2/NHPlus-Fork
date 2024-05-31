package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.CryptoModel;
import de.hitec.nhplus.model.User;
import de.hitec.nhplus.utils.DbUtils;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

public class CryptoUtils {
    private final static String algorithm = "AES";
    private final static String salt = "NHPlus";
    protected static SecretKey key = null;

    private static boolean isLoggedIn = false;
    private static User currentUser = null;

    protected static CryptoDao cryptoDao;
    protected static CryptoModel curCrypto;

    /**
     * Initializes the CryptoUtils by creating a CryptoDAO and loading the CryptoModel.
     */
    public static void init() {
        cryptoDao = DaoFactory.getDaoFactory().createCryptoDAO();
        loadCryptoModel();
    }

    /**
     * Loads the CryptoModel from the CryptoDAO.
     */
    private static void loadCryptoModel() {
        curCrypto = cryptoDao.getCryptoModel();
    }

    public static SecretKey getKeyFromPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return getKeyFromPassword(password, salt);
    }

    /**
     * This method generates a SecretKey from a given password and salt.
     * It uses the PBKDF2WithHmacSHA256 algorithm to generate a key specification from the password and salt.
     * The key specification is then used to generate a SecretKey.
     *
     * @param password The password to be used for generating the SecretKey.
     * @param salt     The salt to be used for generating the SecretKey.
     * @return The generated SecretKey.
     * @throws NoSuchAlgorithmException If the specified algorithm is not available.
     * @throws InvalidKeySpecException  If the specified key specification is inappropriate.
     */
    public static SecretKey getKeyFromPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 69420, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), algorithm);
    }

    /**
     * This method generates a SecretKey from a given password and salt.
     * It uses the PBKDF2WithHmacSHA256 algorithm to generate a key specification from the password and salt.
     * The key specification is then used to generate a SecretKey.
     *
     * @param password The password to be used for generating the SecretKey.
     * @param salt     The salt to be used for generating the SecretKey.
     * @return The generated SecretKey.
     * @throws NoSuchAlgorithmException If the specified algorithm is not available.
     * @throws InvalidKeySpecException  If the specified key specification is inappropriate.
     */
    public static SecretKey getKeyFromPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 69420, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), algorithm);
    }

    public static String encrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return encrypt(input, key);
    }

    /**
     * Encrypts a given input string using a given SecretKey.
     *
     * @param input The string to be encrypted.
     * @param key   The SecretKey to be used for encryption.
     * @return The encrypted string.
     * @throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException If there is an error during encryption.
     */
    public static String encrypt(String input, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return decrypt(cipherText, key);
    }

    /**
     * Decrypts a given cipher text using a given SecretKey.
     *
     * @param cipherText The cipher text to be decrypted.
     * @param key        The SecretKey to be used for decryption.
     * @return The decrypted string.
     * @throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException If there is an error during decryption.
     */
    public static String decrypt(String cipherText, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainText);
    }

    public static SealedObject encryptObject(Serializable object) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException {
        return encryptObject(object, key);
    }

    /**
     * Encrypts a given Serializable object using a given SecretKey.
     *
     * @param object The Serializable object to be encrypted.
     * @param key    The SecretKey to be used for encryption.
     * @return The encrypted object as a SealedObject.
     * @throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException If there is an error during encryption.
     */
    public static SealedObject encryptObject(Serializable object, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return new SealedObject(object, cipher);
    }

    public static Serializable decryptObject(SealedObject sealedObject) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, ClassNotFoundException, BadPaddingException, IllegalBlockSizeException, IOException {
        return decryptObject(sealedObject, key);
    }

    /**
     * Decrypts a given SealedObject using a given SecretKey.
     *
     * @param sealedObject The SealedObject to be decrypted.
     * @param key          The SecretKey to be used for decryption.
     * @return The decrypted object as a Serializable.
     * @throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, ClassNotFoundException, BadPaddingException, IllegalBlockSizeException, IOException If there is an error during decryption.
     */
    public static Serializable decryptObject(SealedObject sealedObject, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, ClassNotFoundException, BadPaddingException, IllegalBlockSizeException, IOException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return (Serializable) sealedObject.getObject(cipher);
    }

    /**
     * Logs in the user by decrypting the test string with the provided password.
     * If the decrypted string matches the expected value, the database is and can be decrypted.
     *
     * @param password The password provided by the user.
     * @throws Exception If the login fails or the database cannot be decrypted.
     */
    public static void login(String password) throws Exception {
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

            if (!decryptDB()) {
                throw new Exception("Login failed, DB cannot be decrypted");
            }

            isLoggedIn = true;

        } catch (Exception e) {
            //e.printStackTrace();
            key = null; // Clear the key in case of any exception
            throw new Exception("Login failed, DB cannot be decrypted");
        }
    }

    /**
     * Sets up the database encryption by encrypting the test string with the provided password.
     * If the encryption is successful, the database is encrypted.
     *
     * @param password The master password provided by the user.
     * @throws Exception If the setup fails or the database cannot be encrypted.
     */
    public static void setupDBEncryption(String password) throws Exception {
        if (isDBEncrypted()) {
            System.err.println("DB already encrypted");
            return;
        }

        try {
            // Generate the secret key from the password
            key = getKeyFromPassword(password, salt);
            String encrypted = encrypt("Hello World!");

            curCrypto.setTestEncrypted(encrypted);
            cryptoDao.updateCryptoModel(curCrypto);

            //encryptDB();

            // create master user
            //User masterUser = new User(0, "admin", "admin", "admin", encrypted, false);
            createMasterUser(password);

            setDBEncrypted(true);
            isLoggedIn = true;
        } catch (Exception e) {
            //e.printStackTrace();
            key = null;
            System.err.println("DB encryption setup failed");
        }
    }

    /**
     * Creates the master user aka admin user with the provided password.
     *
     * @param password The password for the master user.
     */
    public static void createMasterUser(String password) {
        if (isLoggedIn)
            return;

        Connection connection = ConnectionBuilder.getConnection();

        try {
            // Generate the secret key from the password
            key = getKeyFromPassword(password, salt);

            // create master user
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO user (username, firstname, surname, hashedMasterPwKey) VALUES ('admin', 'admin', 'admin', ?)");
            insertStatement.setString(1, encrypt(password));
            insertStatement.executeUpdate();

            isLoggedIn = true;
            currentUser = new User(0, "admin", "admin", "admin", encrypt(password), false);
            currentUser.login(password);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Login failed, DB cannot be decrypted");
        }
    }

    /**
     * Logs in as a specific user by decrypting the test string with the provided password.
     * If the decrypted string matches the expected value, the database is decrypted.
     *
     * @param user     The username of the user.
     * @param password The password provided by the user.
     * @throws IllegalStateException If the login fails or the database cannot be decrypted.
     */
    public static void loginAsUser(String user, String password) {
        if (isLoggedIn || !isDBEncrypted())
            return;

        try {
            // Get User From DB
            Connection connection = ConnectionBuilder.getConnection();
            PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM user WHERE username = ?");
            selectStatement.setString(1, user.toLowerCase().trim());
            ResultSet resultSet = selectStatement.executeQuery();

            if (!resultSet.next()) {
                System.err.println("Login failed, user not found");
                throw new IllegalStateException("Login failed, user not found");
            }

            currentUser = User.fromResultSet(resultSet);
            assert currentUser != null;
            currentUser.login(password);

            if (user.equals("admin")) {
                login(password);
                return;
            }

            String masterPw = currentUser.getDecryptedMasterPw();

            if (currentUser.getNeedsToChangePw())
                promptToChangePassword(currentUser, masterPw);

            login(masterPw);
        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Login failed, DB cannot be decrypted");
            throw new IllegalStateException("Login failed, DB cannot be decrypted");
        }

    }

    /**
     * This method prompts the user to change their password.
     * It checks if the user exists and if the database is encrypted.
     * If these conditions are met, it shows a dialog for the user to enter a new password.
     *
     * @param user The user who needs to change their password.
     * @param masterPw The master password of the db.
     * @return true if the operation is successful, false otherwise.
     */
    public static boolean promptToChangePassword(User user, String masterPw) {
        Connection connection = ConnectionBuilder.getConnection();
        if (!hasUser(connection, user) || !isDBEncrypted()) {
            return false;
        }

        // show and wait for user to enter new password
        DialogPane dialogPane = new DialogPane();

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter new password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm new password");

        // Add password fields
        VBox content = new VBox();
        HBox passwordBox = new HBox();
        passwordBox.setSpacing(10);

        passwordBox.getChildren().add(passwordField);
        passwordBox.getChildren().add(confirmPasswordField);

        TextArea errorText = new TextArea();
        errorText.setEditable(false);
        errorText.setWrapText(true);
        errorText.setPrefHeight(50);

        // Add details and check for password requirements
        errorText.setText("Password must be at least 8 characters long and contain at least 1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character.");

        // content
        content.getChildren().add(passwordBox);
        content.getChildren().add(errorText);

        dialogPane.setContent(content);
        dialogPane.getButtonTypes().add(ButtonType.APPLY);
        dialogPane.getButtonTypes().add(ButtonType.CANCEL);

        // Disable apply button until both fields are filled and match
        dialogPane.lookupButton(ButtonType.APPLY).setDisable(true);

        passwordListener(dialogPane, passwordField, confirmPasswordField, errorText);
        passwordListener(dialogPane, confirmPasswordField, passwordField, errorText);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Enter new password for user: " + user.getUsername());
        dialog.setDialogPane(dialogPane);

        if (dialog.showAndWait().isEmpty() || dialog.getResult() == null || dialog.getResult() == ButtonType.CANCEL) {
            throw new IllegalStateException("Password change cancelled");
        }

        String newPassword = passwordField.getText();

        String results = setNewPassword(user, masterPw, newPassword);
        if (!results.equals("Password updated")) {
            System.out.println("Error: " + results);
            return false;
        }

        // Check if user "needs to change password" and update if necessary
        if (user.getNeedsToChangePw()) {
            disableUserHasToChangePw(user);
            user.setNeedsToChangePw(false);
        }
        return true;
    }

    /**
     * This method adds a listener to the password fields in the password change dialog.
     * It checks if the new password and the confirmation password are the same and meet the password requirements.
     * If the conditions are met, it enables the Apply button in the dialog.
     * If the conditions are not met, it shows an error message in the dialog.
     *
     * @param dialogPane           The DialogPane that contains the password fields and the Apply button.
     * @param passwordField        The PasswordField for the new password.
     * @param confirmPasswordField The PasswordField for the confirmation password.
     * @param errorText            The TextArea that displays the error message.
     */
    private static void passwordListener(DialogPane dialogPane, PasswordField passwordField, PasswordField confirmPasswordField, TextArea errorText) {
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isValid = !newValue.isEmpty() && newValue.equals(confirmPasswordField.getText());

            isValid = isValid && checkPassword(newValue);

            dialogPane.lookupButton(ButtonType.APPLY).setDisable(!isValid);
            errorText.setVisible(!isValid && !newValue.isEmpty());
        });
    }

    /**
     * This method checks if a password meets the requirements.
     * The requirements are: at least 8 characters, at least 1 uppercase letter,
     * at least 1 lowercase letter, at least 1 number, and at least 1 special character.
     *
     * @param password The password to be checked.
     * @return true if the password meets the requirements, false otherwise.
     */
    public static boolean checkPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else {
                hasSpecial = true;
            }
        }

        return hasUppercase && hasLowercase && hasNumber && hasSpecial;
    }

    /**
     * Returns the current logged-in user.
     *
     * @return The current User object.
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Creates a new user with the provided password.
     *
     * @param newUser  The new User object to be created.
     * @param password The password for the new user.
     * @return The created User object.
     */
    public static User createNewUser(User newUser, String password) {
        if (!isLoggedIn || !isDBEncrypted())
            return null;

        // Current must be the master user
        if (currentUser == null || !currentUser.getUsername().equals("admin"))
            return null;

        String masterPassword = currentUser.getEncryptedMasterPw();

        try {
            SecretKey userKey = getKeyFromPassword(password);

            // Get User From DB
            Connection connection = ConnectionBuilder.getConnection();

            // check if user exists
            if (hasUser(connection, newUser)) {
                System.err.println("User already exists");
                return null;
            }

            String hashedMasterPwKey = encrypt(masterPassword, userKey);

            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO user (username, firstname, surname, hashedMasterPwKey) VALUES (?, ?, ?, ?)");
            insertStatement.setString(1, newUser.getUsername());
            insertStatement.setString(2, newUser.getFirstname());
            insertStatement.setString(3, newUser.getSurname());
            insertStatement.setString(4, hashedMasterPwKey);
            insertStatement.executeUpdate();

            return newUser;
        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deletes a user from the database.
     *
     * @param userToDelete The user to be deleted.
     * @return true if the user is successfully deleted, false otherwise.
     */
    public static boolean deleteUser(User userToDelete) {
        if (!isLoggedIn || !isDBEncrypted())
            return false;

        // Current must be the master user
        if (currentUser == null || !currentUser.getUsername().equals("admin"))
            return false;

        try {
            // check if user exists
            Connection connection = ConnectionBuilder.getConnection();

            if (!hasUser(connection, userToDelete)) {
                System.err.println("User not found");
                return false;
            }

            PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM user WHERE username = ?");
            deleteStatement.setString(1, userToDelete.getUsername());
            deleteStatement.executeUpdate();

            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if a user exists in the database.
     *
     * @param connection The database connection.
     * @param user       The user to be checked.
     * @return true if the user exists, false otherwise.
     */
    public static boolean hasUser(Connection connection, User user) {
        try {
            PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM user WHERE username = ?");
            selectStatement.setString(1, user.getUsername());
            ResultSet resultSet = selectStatement.executeQuery();

            if (!resultSet.next()) {
                return false;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Updates the details of a user in the database.
     * The method checks if the user is logged in and if the database is encrypted.
     * If these conditions are met, it updates the user's username, firstname, surname, password, and the flag indicating if the user needs to change their password.
     *
     * @param user The User object to be updated.
     * @param username The new username for the user.
     * @param firstname The new firstname for the user.
     * @param surname The new surname for the user.
     * @param password The new password for the user.
     * @param needsToChangePw The flag indicating if the user needs to change their password.
     */
    public static void updateUser(User user, String username, String firstname, String surname, String password, boolean needsToChangePw) {
        if (!isLoggedIn || !isDBEncrypted())
            return;

        // Current must be the master user
        if (currentUser == null || !currentUser.getUsername().equals("admin"))
            return;

        try {
            // Get User From DB
            Connection connection = ConnectionBuilder.getConnection();

            if (!hasUser(connection, user)) {
                System.err.println("User not found");
                return;
            }

            // check for changes in names
            if (!user.getUsername().equals(username) || !user.getFirstname().equals(firstname) || !user.getSurname().equals(surname)) {
                PreparedStatement updateStatement = connection.prepareStatement("UPDATE user SET username = ?, firstname = ?, surname = ? WHERE username = ?");
                updateStatement.setString(1, username);
                updateStatement.setString(2, firstname);
                updateStatement.setString(3, surname);
                updateStatement.setString(4, user.getUsername());
                updateStatement.executeUpdate();
            }

            // check for password change
            if (!password.isEmpty()) {
                String masterPw = currentUser.getDecryptedMasterPw();
                SecretKey newUserKey = getKeyFromPassword(password);
                String newHashedMasterPwKey = encrypt(masterPw, newUserKey);

                PreparedStatement updateStatement = connection.prepareStatement("UPDATE user SET hashedMasterPwKey = ? WHERE username = ?");
                updateStatement.setString(1, newHashedMasterPwKey);
                updateStatement.setString(2, user.getUsername());
                updateStatement.executeUpdate();
            }

            // check for needsToChangePw change
            if (user.getNeedsToChangePw() != needsToChangePw) {
                PreparedStatement updateStatement = connection.prepareStatement("UPDATE user SET needsToChangePw = ? WHERE username = ?");
                updateStatement.setBoolean(1, needsToChangePw);
                updateStatement.setString(2, user.getUsername());
                updateStatement.executeUpdate();
            }

        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }

    }

    /**
     * Sets a new password for a user.
     *
     * @param user        The user who wants to change their password.
     * @param newPassword The new password of the user.
     * @return A string message indicating the result of the operation.
     */
    public static String setNewPassword(User user, String masterPw, String newPassword) {
        if (!isDBEncrypted())
            return "DB encryption not finalized";

        try {
            // has user
            Connection connection = ConnectionBuilder.getConnection();

            if (!hasUser(connection, user)) {
                return "User not found";
            }

            SecretKey newUserKey = getKeyFromPassword(newPassword);
            String newHashedMasterPwKey = encrypt(masterPw, newUserKey);

            PreparedStatement updateStatement = connection.prepareStatement("UPDATE user SET hashedMasterPwKey = ? WHERE username = ?");
            updateStatement.setString(1, newHashedMasterPwKey);
            updateStatement.setString(2, user.getUsername());
            updateStatement.executeUpdate();

            return "Password updated";
        } catch (Exception e) {
            //e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Disables the 'needsToChangePw' flag for a user in the database.
     * This method is used to indicate that a user no longer needs to change their password at their next login.
     *
     * @param user The User object whose 'needsToChangePw' flag is to be updated.
     */
    private static void disableUserHasToChangePw(User user) {
        Connection connection = ConnectionBuilder.getConnection();
        user.setNeedsToChangePw(false);

        try {
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE user SET needsToChangePw = ? WHERE username = ?");
            updateStatement.setBoolean(1, false);
            updateStatement.setString(2, user.getUsername());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Error: " + e.getMessage());
        }
    }

    /**
     * Encrypts the database by creating encrypted versions of all eligible tables and storing the encrypted data.
     *
     * @return true if the encryption is successful, false otherwise.
     */
    public static boolean encryptDB() {
        Connection connection = ConnectionBuilder.getConnection();

        try {

            for (String table : getAllTablesForEncryption()) {
                if (table.startsWith("sqlite_"))
                    continue;
                if (table.startsWith("encrypted_"))
                    continue;
                // User table should not be encrypted
                if (table.equals("user"))
                    continue;

                System.out.println("Encrypting table: " + table);
                connection.createStatement().execute("CREATE TABLE IF NOT EXISTS encrypted_" + table + " (id INTEGER PRIMARY KEY, encrypted_data BLOB)");

                // clear encrypted table
                connection.createStatement().execute("DELETE FROM encrypted_" + table);

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

        } catch (Exception e) {
            //e.printStackTrace();
            key = null;
            System.err.println("DB encryption failed");
            return false;
        }

        return true;
    }

    /**
     * Decrypts the database by decrypting the data in the encrypted tables and restoring it to the original tables.
     *
     * @return true if the decryption is successful, false otherwise.
     */
    public static boolean decryptDB() {
        if (!isDBEncrypted()) {
            System.err.println("DB encryption not finalized");
            return false;
        }
        Connection connection = ConnectionBuilder.getConnection();

        try {

            // Decrypt and restore all tables
            for (String table : getAllTablesForDecryption()) {
                System.out.println("Decrypting table: " + table);

                PreparedStatement selectStatement = connection.prepareStatement("SELECT id, encrypted_data FROM encrypted_" + table);
                ResultSet resultSet = selectStatement.executeQuery();

                while (resultSet.next()) {
                    String encryptedData = resultSet.getString("encrypted_data");
                    String decryptedData = decrypt(encryptedData);

                    //System.out.println("Decrypted data: " + decryptedData);

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
            //e.printStackTrace();
            key = null;
            System.err.println("DB decryption failed");
            return false;
        }

        return true;
    }

    /**
     * Clears all encrypted tables in the database.
     *
     * @return true if the operation is successful, false otherwise.
     */
    public static boolean clearEncryptedTables() {
        Connection connection = ConnectionBuilder.getConnection();

        try {
            for (String table : getAllTablesForEncryption()) {
                if (table.startsWith("encrypted_"))
                    connection.createStatement().execute("DELETE FROM " + table);

            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Logs out the user by encrypting the database and clearing the original tables.
     *
     * @return true if the logout is successful, false otherwise.
     */
    public static boolean logout() {
        if (!CryptoUtils.isDBEncrypted())
            throw new IllegalStateException("Cannot logout while the DB encryption is not finalized");

        if (!isLoggedIn)
            return false;

        Connection connection = ConnectionBuilder.getConnection();

        // encrypt and store all tables
        clearEncryptedTables();

        if (encryptDB())
            System.out.println("DB encrypted successfully");

        // Delete all values from the original tables after decryption
        for (String table : getAllTablesForEncryption()) {
            if (table.startsWith("sqlite_"))
                continue;
            if (table.startsWith("encrypted_"))
                continue;
            try {
                connection.createStatement().execute("DELETE FROM " + table);
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage() + " - " + table);
            }
        }

        return true;
    }

    /**
     * Checks if the database encryption is finalized.
     *
     * @return true if the database is encrypted, false otherwise.
     */
    public static boolean isDBEncrypted() {
        return curCrypto.getIsDBEncrypted();
    }

    /**
     * Sets the database encryption status.
     *
     * @param isDBEncrypted The new status of the database encryption.
     */
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

        Collections.addAll(tables, Arrays.stream(DbUtils.getAllTables()).filter(s -> !s.equals("crypto")).toArray(String[]::new));

        return tables.toArray(new String[0]);
    }

    /**
     * Retrieves all the table names from the database that are eligible for decryption.
     *
     * @return An array of table names that are eligible for decryption.
     */
    public static String[] getAllTablesForDecryption() {
        var tables = new ArrayList<String>();

        Collections.addAll(tables, Arrays.stream(getAllTablesForEncryption()).filter(s -> !s.startsWith("encrypted")).toArray(String[]::new));

        return tables.toArray(new String[0]);
    }

}
