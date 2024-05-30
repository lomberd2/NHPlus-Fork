package de.hitec.nhplus.utils;

import de.hitec.nhplus.datastorage.ConnectionBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class createArchivedTreatmentTable{


    public static void main(String[] args) {
        Connection connection = ConnectionBuilder.getConnection();


        try (Statement statement = connection.createStatement()) {

                statement.execute("DROP TABLE IF EXISTS " + "ArchivedTreatment");

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        String sql = "CREATE TABLE IF NOT EXISTS ArchivedTreatment (\n"
                + " Tid INTEGER PRIMARY KEY,\n"
                + " Pid INTEGER,\n"
                + " Date TEXT,\n"
                + " Begin TEXT,\n"
                + " End TEXT,\n"
                + " Description TEXT,\n"
                + " Remarks TEXT\n"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    }

