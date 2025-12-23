package controllers;

import database.DBConnection;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class ReportsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        int totalPatients = 0;
        int totalDoctors = 0;
        int totalAppointments = 0;
        double totalRevenue = 0;
        int pendingBills = 0;

        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();

            // Count patients
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM patients");
            if (rs.next()) totalPatients = rs.getInt("count");

            // Count doctors
            rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM doctors");
            if (rs.next()) totalDoctors = rs.getInt("count");

            // Count appointments
            rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM appointments");
            if (rs.next()) totalAppointments = rs.getInt("count");

            // Sum billing (total revenue)
            rs = stmt.executeQuery("SELECT SUM(amount) AS total FROM billing WHERE status='Paid'");
            if (rs.next()) totalRevenue = rs.getDouble("total");

            // Count pending bills
            rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM billing WHERE status='Pending'");
            if (rs.next()) pendingBills = rs.getInt("count");

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Send as JSON
        out.print("{");
        out.print("\"patients\":" + totalPatients + ",");
        out.print("\"doctors\":" + totalDoctors + ",");
        out.print("\"appointments\":" + totalAppointments + ",");
        out.print("\"revenue\":" + totalRevenue + ",");
        out.print("\"pending\":" + pendingBills);
        out.print("}");
        out.flush();
    }
}
