package controllers;

import controllers.DBConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
import javax.servlet.http.*;


import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class MedicalRecordsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String patientId = request.getParameter("patient_id");
        String doctorId = request.getParameter("doctor_id");
        String diagnosis = request.getParameter("diagnosis");
        String treatment = request.getParameter("treatment");
        String prescription = request.getParameter("prescription");
        String date = request.getParameter("date");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO medical_records (patient_id, doctor_id, diagnosis, treatment, prescription, date) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(patientId));
            stmt.setInt(2, Integer.parseInt(doctorId));
            stmt.setString(3, diagnosis);
            stmt.setString(4, treatment);
            stmt.setString(5, prescription);
            stmt.setString(6, date);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("medical_records");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><link rel='stylesheet' href='css/style.css'></head><body>");
        out.println("<div class='container'><h1>Medical Records</h1>");
        out.println("<form action='medical_records' method='post'>");
        out.println("<label>Patient ID:</label><input type='number' name='patient_id' required>");
        out.println("<label>Doctor ID:</label><input type='number' name='doctor_id' required>");
        out.println("<label>Diagnosis:</label><input type='text' name='diagnosis' required>");
        out.println("<label>Treatment:</label><input type='text' name='treatment'>");
        out.println("<label>Prescription:</label><input type='text' name='prescription'>");
        out.println("<label>Date:</label><input type='date' name='date' required>");
        out.println("<button type='submit'>Add Record</button></form>");

        out.println("<h2>Existing Records</h2>");
        out.println("<table><tr><th>ID</th><th>Patient ID</th><th>Doctor ID</th><th>Diagnosis</th><th>Treatment</th><th>Prescription</th><th>Date</th></tr>");

        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM medical_records");

            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("id") + "</td><td>" +
                        rs.getInt("patient_id") + "</td><td>" +
                        rs.getInt("doctor_id") + "</td><td>" +
                        rs.getString("diagnosis") + "</td><td>" +
                        rs.getString("treatment") + "</td><td>" +
                        rs.getString("prescription") + "</td><td>" +
                        rs.getString("date") + "</td></tr>");
            }
        } catch (Exception e) {
            out.println("<p>Error loading records.</p>");
        }

        out.println("</table><a href='dashboard.html' class='back-link'>← Back to Dashboard</a></div></body></html>");
    }
}
