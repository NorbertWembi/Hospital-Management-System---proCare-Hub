package controllers;

import controllers.DBConnection;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AppointmentsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int patientId = Integer.parseInt(request.getParameter("patient_id"));
        int doctorId = Integer.parseInt(request.getParameter("doctor_id"));
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        String reason = request.getParameter("reason");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO appointments (patient_id, doctor_id, date, time, reason) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, patientId);
            ps.setInt(2, doctorId);
            ps.setString(3, date);
            ps.setString(4, time);
            ps.setString(5, reason);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("appointments");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {

            // Fetch patients
            Statement stmt = conn.createStatement();
            ResultSet patients = stmt.executeQuery("SELECT id, name FROM patients");

            // Fetch doctors
            Statement stmt2 = conn.createStatement();
            ResultSet doctors = stmt2.executeQuery("SELECT id, name FROM doctors");

            out.println("<html><head><link rel='stylesheet' href='css/style.css'></head><body>");
            out.println("<div class='container'><h2>Appointments Management</h2>");
            out.println("<form action='appointments' method='post' class='appointment-form'>");

            // Patient dropdown
            out.println("<select name='patient_id' required><option value=''>Select Patient</option>");
            while (patients.next()) {
                out.println("<option value='" + patients.getInt("id") + "'>" + patients.getString("name") + "</option>");
            }
            out.println("</select>");

            // Doctor dropdown
            out.println("<select name='doctor_id' required><option value=''>Select Doctor</option>");
            while (doctors.next()) {
                out.println("<option value='" + doctors.getInt("id") + "'>" + doctors.getString("name") + "</option>");
            }
            out.println("</select>");

            out.println("<input type='date' name='date' required>");
            out.println("<input type='time' name='time' required>");
            out.println("<input type='text' name='reason' placeholder='Reason for visit' required>");
            out.println("<button type='submit'>Book Appointment</button></form>");

            // Display existing appointments
            Statement stmt3 = conn.createStatement();
            ResultSet rs = stmt3.executeQuery(
                    "SELECT a.id, p.name AS patient, d.name AS doctor, a.date, a.time, a.reason " +
                            "FROM appointments a " +
                            "JOIN patients p ON a.patient_id = p.id " +
                            "JOIN doctors d ON a.doctor_id = d.id"
            );

            out.println("<h3>All Appointments</h3>");
            out.println("<table class='data-table'><tr><th>ID</th><th>Patient</th><th>Doctor</th><th>Date</th><th>Time</th><th>Reason</th></tr>");
            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("id") + "</td><td>" + rs.getString("patient") +
                        "</td><td>" + rs.getString("doctor") + "</td><td>" + rs.getString("date") +
                        "</td><td>" + rs.getString("time") + "</td><td>" + rs.getString("reason") + "</td></tr>");
            }
            out.println("</table><a href='dashboard.html' class='back-link'>← Back to Dashboard</a></div></body></html>");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
