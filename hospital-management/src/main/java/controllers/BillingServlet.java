package controllers;

import database.DBConnection;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class BillingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String patientId = request.getParameter("patient_id");
        String doctorId = request.getParameter("doctor_id");
        String amount = request.getParameter("amount");
        String date = request.getParameter("date");
        String status = request.getParameter("status");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO billing (patient_id, doctor_id, amount, date, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(patientId));
            stmt.setInt(2, Integer.parseInt(doctorId));
            stmt.setDouble(3, Double.parseDouble(amount));
            stmt.setString(4, date);
            stmt.setString(5, status);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("billing");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><link rel='stylesheet' href='css/style.css'></head><body>");
        out.println("<div class='container'><h1>Billing Records</h1>");
        out.println("<table><tr><th>ID</th><th>Patient ID</th><th>Doctor ID</th><th>Amount</th><th>Date</th><th>Status</th></tr>");

        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM billing");

            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("id") + "</td><td>" +
                        rs.getInt("patient_id") + "</td><td>" +
                        rs.getInt("doctor_id") + "</td><td>$" +
                        rs.getDouble("amount") + "</td><td>" +
                        rs.getString("date") + "</td><td>" +
                        rs.getString("status") + "</td></tr>");
            }
        } catch (Exception e) {
            out.println("<p>Error loading billing records.</p>");
        }
        out.println("</table><a href='dashboard.html' class='back-link'>← Back to Dashboard</a></div></body></html>");
    }
}
