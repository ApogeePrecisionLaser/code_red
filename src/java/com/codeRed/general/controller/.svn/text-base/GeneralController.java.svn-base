/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.codeRed.general.controller;

import com.codeRed.general.model.MapDetailClass;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jpss
 */
public class GeneralController extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            String task = request.getParameter("task");
            if(task == null)
                task = "";
            if (task.equals("showMapWindow")) {
                String longi = request.getParameter("logitude");
                String latti = request.getParameter("lattitude");
                request.setAttribute("longi", longi);
                request.setAttribute("latti", latti);
                System.out.println(latti + "," + longi);
                request.getRequestDispatcher("openMapWindowView").forward(request, response);
                return;
            }else if (task.equals("GetCordinates")) {
                String longi = request.getParameter("longitude");
                String latti = request.getParameter("latitude");
                if(longi == null || longi.equals("undefined"))
                    longi = "0";
                if(latti == null || latti.equals("undefined"))
                    latti = "0";
                request.setAttribute("longi", longi);
                request.setAttribute("latti", latti);
                System.out.println(latti + "," + longi);
                request.getRequestDispatcher("getCordinate").forward(request, response);
                return;
            }else if(task.equals("GetDistance")){
                int disance = MapDetailClass.getDistance(request.getParameter("source"), request.getParameter("destination"));
                PrintWriter out = response.getWriter();
                out.print(disance);
                out.close();
                return;
            }
        } finally {

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
