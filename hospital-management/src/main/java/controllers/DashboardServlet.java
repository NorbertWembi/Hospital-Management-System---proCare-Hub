package controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class DashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("dashboard.html");
        dispatcher.forward(request, response);
    }
}
