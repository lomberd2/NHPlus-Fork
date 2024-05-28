package de.hitec.nhplus.datastorage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SetupDB {

    // SQL-Statements zur Erstellung und Löschung der Benutzertabelle
    private static final String CREATE_USER_TABLE_SQL = """
        CREATE TABLE IF NOT EXISTS user (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name VARCHAR(255) NOT NULL,
            description VARCHAR(255),
            hashed_pw VARCHAR(255) NOT NULL,
            hashed_master_pw_key VARCHAR(255) NOT NULL
        );
    """;

    private static final String DROP_USER_TABLE_SQL = "DROP TABLE IF EXISTS user;";

    // Methode zur Erstellung der Datenbankstruktur
    public static void setupDatabase(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(CREATE_USER_TABLE_SQL);
        }
    }

    // Methode zum Zurücksetzen der Datenbank
    public static void resetDatabase(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(DROP_USER_TABLE_SQL);
            setupDatabase(connection);
        }
    }
}
