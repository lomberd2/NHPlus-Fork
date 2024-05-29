package de.hitec.nhplus.utils;

import de.hitec.nhplus.datastorage.ConnectionBuilder;

import java.sql.PreparedStatement;
import java.util.ArrayList;

public class DbUtils {
    /**
     * This method retrieves all the table names from the database.
     * It executes a SQL query to fetch all table names except 'sqlite_sequence'.
     *
     * @return An array of all table names in the database.
     * @throws Exception If there is an error while executing the SQL query or processing the result.
     */
    public static String[] getAllTables() {
        PreparedStatement preparedStatement = null;
        ArrayList<String> tables = null;
        try {
            preparedStatement = ConnectionBuilder.getConnection().prepareStatement("SELECT tbl_name FROM sqlite_master WHERE tbl_name <> 'sqlite_sequence'");
            var resultSet = preparedStatement.executeQuery();
            tables = new ArrayList<String>();
            while (resultSet.next()) {
                tables.add(resultSet.getString("tbl_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tables.toArray(new String[0]);
    }
}
