package controllers;

import controllers.DBConnection;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import dao.PatientDAO;
import models.Patient;


public class PatientsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String age = request.getParameter("age");
        String gender = request.getParameter("gender");
        String contact = request.getParameter("contact");
        String address = request.getParameter("address");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO patients (name, age, gender, contact, address) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, Integer.parseInt(age));
            stmt.setString(3, gender);
            stmt.setString(4, contact);
            stmt.setString(5, address);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("patients.html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM patients")) {

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getString("name") + "</td>");

                out.println("<td>" + rs.getInt("age") + "</td>");

                out.println("<td>" + rs.getString("gender") + "</td>");
                out.println("<td>" + rs.getString("contact") + "</td>");
                out.println("<td>" + rs.getString("address") + "</td>");
                out.println("</tr>");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}






