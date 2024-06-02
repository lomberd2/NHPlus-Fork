package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArchivedPatientDao {

    private final Connection connection;

    public ArchivedPatientDao() {
        this.connection = ConnectionBuilder.getConnection();
    }

    public void insert(Patient patient) throws SQLException {
        String sql = "INSERT INTO ArchivedPatient(Pid, Name, Surname, CareLevel, Birthdate, Room) VALUES(?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, patient.getPid());
            pstmt.setString(2, patient.getFirstName());
            pstmt.setString(3, patient.getSurname());
            pstmt.setString(4, patient.getCareLevel());
            pstmt.setString(5, patient.getDateOfBirth());
            pstmt.setString(6, patient.getRoomNumber());
            pstmt.executeUpdate();
        }
    }
}