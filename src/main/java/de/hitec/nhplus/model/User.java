package de.hitec.nhplus.model;

import de.hitec.nhplus.datastorage.CryptoUtils;

import javax.crypto.SecretKey;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private long id;
    private String username;
    private String firstname;
    private String surname;
    private String hashedMasterPwKey;
    private boolean needsToChangePw;

    // temp values (should not be stored in the database or serialized)
    private String password;
    private SecretKey secretKey;

    public User(long id, String username, String firstname, String surname, String hashedMasterPwKey) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.surname = surname;
        this.hashedMasterPwKey = hashedMasterPwKey;
    }

    public User(long id, String username, String firstname, String surname, String hashedMasterPwKey, boolean needsToChangePw) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.surname = surname;
        this.hashedMasterPwKey = hashedMasterPwKey;
        this.needsToChangePw = needsToChangePw;
    }

    // Getter und Setter Methoden
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getHashedMasterPwKey() {
        return hashedMasterPwKey;
    }

    public void setHashedMasterPwKey(String hashedMasterPwKey) {
        this.hashedMasterPwKey = hashedMasterPwKey;
    }

    public boolean getNeedsToChangePw() {
        return needsToChangePw;
    }

    public void setNeedsToChangePw(boolean needsToChangePw) {
        this.needsToChangePw = needsToChangePw;
    }


    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public void login(String password) throws Exception {
        this.password = password;
        try {
            this.secretKey = CryptoUtils.getKeyFromPassword(password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // check if password is correct
       CryptoUtils.decrypt(hashedMasterPwKey, secretKey);
    }

    public String getEncryptedMasterPw() {
        if (username.equals("admin")) {
            return password;
        }

        try {
            SecretKey secretKey = CryptoUtils.getKeyFromPassword(password);
            return CryptoUtils.encrypt(hashedMasterPwKey, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDecryptedMasterPw() {
        if (username.equals("admin")) {
            return password;
        }

        try {
            SecretKey secretKey = CryptoUtils.getKeyFromPassword(password);
            return CryptoUtils.decrypt(hashedMasterPwKey, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getPassword() {
        return password;
    }

    public static User fromResultSet(ResultSet resultSet) {
        try {
            long id = resultSet.getLong("id");
            String username = resultSet.getString("username");
            String firstname = resultSet.getString("firstname");
            String surname = resultSet.getString("surname");
            String hashedMasterPwKey = resultSet.getString("hashedMasterPwKey");
            boolean needsToChangePw = resultSet.getBoolean("needsToChangePw");

            return new User(id, username, firstname, surname, hashedMasterPwKey, needsToChangePw);
        } catch (SQLException exception) {
            exception.printStackTrace();

            return null;
        }
    }
}
