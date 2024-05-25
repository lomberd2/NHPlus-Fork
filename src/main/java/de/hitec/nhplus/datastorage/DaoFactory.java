package de.hitec.nhplus.datastorage;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DaoFactory {

    private static DaoFactory instance;

    private DaoFactory() {
    }

    public static DaoFactory getDaoFactory() {
        if (DaoFactory.instance == null) {
            DaoFactory.instance = new DaoFactory();
        }
        return DaoFactory.instance;
    }

    public static String[] getAllTablesForEncryption() {
        var tables = new ArrayList<String>();

        Collections.addAll(tables, Arrays.stream(getDaoFactory().getAllTables()).filter(s -> !s.equals("crypto") ).toArray(String[]::new));

        return tables.toArray(new String[0]);
    }

    public String[] getAllTables() {
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


    public TreatmentDao createTreatmentDao() {
        return new TreatmentDao(ConnectionBuilder.getConnection());
    }

    public PatientDao createPatientDAO() {
        return new PatientDao(ConnectionBuilder.getConnection());
    }

    public CryptoDao createCryptoDAO() {
        return new CryptoDao(ConnectionBuilder.getConnection());
    }
}
