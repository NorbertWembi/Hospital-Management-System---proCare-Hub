package controllers;

import database.DBConnection;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM users WHERE username=? AND password=? AND role=?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Redirect based on role
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setAttribute("role", role);
                response.sendRedirect("dashboard.html");
            } else {
                response.sendRedirect("login.html?error=1");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("login.html?error=db");
        }
    }
}
