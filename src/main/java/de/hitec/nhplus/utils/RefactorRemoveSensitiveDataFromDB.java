package de.hitec.nhplus.utils;

import de.hitec.nhplus.datastorage.ConnectionBuilder;

import java.sql.Connection;

public class RefactorRemoveSensitiveDataFromDB {

    public static void start() {
        Connection connection = ConnectionBuilder.getConnection();
        if (hasSensitiveDataInDB(connection)) {
            removeSensitiveDataFromDB(connection);
        }
    }

    /**
     * This method checks if the database still contains sensitive data. Checks if patient.assets still exists on table.
     * @param connection Connection to the database.
     * @return true if the database still contains sensitive data, false if not.
     */
    public static boolean hasSensitiveDataInDB(Connection connection) {
        try {
            return connection.createStatement().executeQuery("SELECT assets FROM patient").next();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * This method removes sensitive data from the database. Removes the column patient.assets from the table patient.
     * @param connection Connection to the database.
     */
    public static void removeSensitiveDataFromDB(Connection connection) {
        try {
            connection.createStatement().execute("ALTER TABLE patient DROP COLUMN assets");
            System.out.println("Sensitive data removed from database.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) { RefactorRemoveSensitiveDataFromDB.start(); }
}
