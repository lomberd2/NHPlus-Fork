package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao extends DaoImp<User> {

    public UserDao(Connection connection) {
        super(connection);
    }

    @Override
    protected PreparedStatement getCreateStatement(User user) {
        PreparedStatement preparedStatement = null;
        
        // SQL-Statement zum Einfügen eines neuen Benutzers
        try {
            final String SQL = "INSERT INTO user (name, description, hashed_pw, hashed_master_pw_key) VALUES (?, ?, ?, ?)";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getDescription());
            preparedStatement.setString(3, user.getHashedPw());
            preparedStatement.setString(4, user.getHashedMasterPwKey());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    // Implement other required methods like getReadByIDStatement, getUpdateStatement, etc.
}
