/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.camunda.bpm.engine.impl.util.json.JSONObject;
import org.camunda.connect.Connectors;
import org.camunda.connect.httpclient.HttpConnector;
import org.camunda.connect.httpclient.HttpResponse;

/**
 *
 * @author kell
 */
public class order_delivered extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String orderId = request.getParameter("orderId");
        String messageName = request.getParameter("messageName");
        //https://docs.camunda.org/manual/7.5/reference/rest/message/post-message/#example
        String url = "http://localhost:8080//engine-rest/message";
        String payload = new JSONObject()
                .put("messageName",messageName)
                .put("correlationKeys",new JSONObject()
                .put("orderId", new JSONObject().put("value", orderId).put("type", "integer")))
                .toString();
        System.out.println("Servlet order_delivered: payload: " + payload);
        
        //make http call /execution/{id}/messageSubscriptions/{messageName}/trigger
        HttpConnector http = Connectors.getConnector(HttpConnector.ID);
        HttpResponse resp = http.createRequest()
            .post()
            .url(url)
            .contentType("application/json")
            .payload(payload)
            .execute();
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet order_delivered</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet order_delivered at " + request.getContextPath() + "</h1>");
            out.println("<p>called following url: " + url + "</p>");
            out.println("<p>got following status code from process engine: " + resp.getStatusCode() + "</p>");
            out.println("</body>");
            out.println("</html>");
        }
        resp.close();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
