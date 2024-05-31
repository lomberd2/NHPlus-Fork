package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.Treatment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArchivedTreatmentDao {

    private final Connection connection;

    public ArchivedTreatmentDao(Connection connection) {
        this.connection = connection;
    }

    public void insert(Treatment treatment) throws SQLException {

        String sql = "INSERT INTO ArchivedTreatment VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, treatment.getTid());
            statement.setLong(2, treatment.getPid());
            statement.setString(3, treatment.getDate());
            statement.setString(4, treatment.getBegin());
            statement.setString(5, treatment.getEnd());
            statement.setString(6, treatment.getDescription());
            statement.setString(7, treatment.getRemarks());
            statement.executeUpdate();
        }
    }
}