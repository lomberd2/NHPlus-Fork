package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.CryptoModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CryptoDao {

    Connection connection;

    public CryptoDao(Connection connection) {
        this.connection = connection;
    }

    public CryptoModel getCryptoModel() {
        CryptoModel cryptoModel = null;
        try {
            final String SQL = "SELECT * FROM crypto WHERE id = 1";
            PreparedStatement preparedStatement = this.connection.prepareStatement(SQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                cryptoModel = new CryptoModel(resultSet.getBoolean("isDBEncrypted"), resultSet.getString("testEncrypted"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return cryptoModel;
    }

    public void updateCryptoModel(CryptoModel cryptoModel) {
        try {
            final String SQL = "UPDATE crypto SET isDBEncrypted = ?, testEncrypted = ? WHERE id = 1";
            PreparedStatement preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setBoolean(1, cryptoModel.getIsDBEncrypted());
            preparedStatement.setString(2, cryptoModel.getTestEncrypted());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}