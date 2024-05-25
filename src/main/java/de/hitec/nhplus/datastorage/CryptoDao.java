package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.Crypto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CryptoDao extends DaoImp<Crypto> {

    public CryptoDao(Connection connection) {
        super(connection);
    }

    @Override
    protected Crypto getInstanceFromResultSet(ResultSet set) throws SQLException {
        return new Crypto(set.getBoolean("isDBEncrypted"), set.getString("testEncrypted"));
    }

    @Override
    protected ArrayList<Crypto> getListFromResultSet(ResultSet set) throws SQLException {
        ArrayList<Crypto> cryptos = new ArrayList<>();
        while (set.next()) {
            cryptos.add(getInstanceFromResultSet(set));
        }
        return cryptos;
    }

    @Override
    protected PreparedStatement getCreateStatement(Crypto crypto) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "INSERT INTO crypto (isDBEncrypted, testEncrypted) " +
                    "VALUES (?, ?)";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setBoolean(1, crypto.getIsDBEncrypted());
            preparedStatement.setString(2, crypto.getTestEncrypted());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    @Override
    protected PreparedStatement getReadByIDStatement(long id) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "SELECT * FROM crypto WHERE id = 1";
            preparedStatement = this.connection.prepareStatement(SQL);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    @Override
    protected PreparedStatement getReadAllStatement() {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "SELECT * FROM crypto";
            preparedStatement = this.connection.prepareStatement(SQL);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    @Override
    protected PreparedStatement getUpdateStatement(Crypto crypto) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "UPDATE crypto SET isDBEncrypted = ?, testEncrypted = ? WHERE id = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setBoolean(1, crypto.getIsDBEncrypted());
            preparedStatement.setString(2, crypto.getTestEncrypted());
            preparedStatement.setInt(3, crypto.getId());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    @Override
    protected PreparedStatement getDeleteStatement(long id) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "DELETE FROM crypto WHERE id = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, id);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }
}