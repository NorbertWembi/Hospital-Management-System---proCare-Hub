package dao;

import database.DBConnection;
import models.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Patient entity.
 * Handles all database operations related to patients.
 */
public class PatientDAO {

    // Add a new patient
    public boolean addPatient(Patient patient) {
        String sql = "INSERT INTO patients (name, age, gender, contact, address) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patient.getName());
            stmt.setInt(2, patient.getAge());
            stmt.setString(3, patient.getGender());
            stmt.setString(4, patient.getContact());
            stmt.setString(5, patient.getAddress());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all patients
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Patient patient = new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("contact"),
                        rs.getString("address")
                );
                patients.add(patient);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    // Get patient by ID
    public Patient getPatientById(int id) {
        String sql = "SELECT * FROM patients WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("contact"),
                        rs.getString("address")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
