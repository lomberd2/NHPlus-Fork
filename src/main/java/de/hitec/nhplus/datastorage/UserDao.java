package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDao extends DaoImp<User> {

    public UserDao(Connection connection) {
        super(connection);
    }

    /**
     * @param set
     * @return
     * @throws SQLException
     */
    @Override
    protected User getInstanceFromResultSet(ResultSet set) throws SQLException {
        return null;
    }

    /**
     * Not implemented yet
     *
     * @param set
     * @return
     * @throws SQLException
     */
    @Override
    protected ArrayList<User> getListFromResultSet(ResultSet set) throws SQLException {
        return null;
    }

    /**
     * @param user
     * @return
     */
    @Override
    protected PreparedStatement getCreateStatement(User user) {
        PreparedStatement preparedStatement = null;
        
        // SQL-Statement zum Einfügen eines neuen Benutzers
        try {
            final String SQL = "INSERT INTO user (username, firstname, surname, hashedMasterPwKey) VALUES (?, ?, ?, ?)";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getFirstname());
            preparedStatement.setString(3, user.getSurname());
            preparedStatement.setString(4, user.getHashedMasterPwKey());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * @param key
     * @return
     */
    @Override
    protected PreparedStatement getReadByIDStatement(long key) {
        return null;
    }

    /**
     * @return
     */
    @Override
    protected PreparedStatement getReadAllStatement() {
        return null;
    }

    /**
     * @param user
     * @return
     */
    @Override
    protected PreparedStatement getUpdateStatement(User user) {
        return null;
    }

    /**
     * @param key
     * @return
     */
    @Override
    protected PreparedStatement getDeleteStatement(long key) {
        return null;
    }

    // Implement other required methods like getReadByIDStatement, getUpdateStatement, etc.
}
