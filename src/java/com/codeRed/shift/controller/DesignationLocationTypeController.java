/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.codeRed.shift.controller;

import com.codeRed.dbCon.DBConnection;
import com.codeRed.shift.model.DesignationLocationTypeModel;
import java.sql.Connection;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Administrator
 */
public class DesignationLocationTypeController extends HttpServlet {
   

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
         response.setContentType("text/html;charset=UTF-8");
          response.setCharacterEncoding("UTF-8");
          request.setCharacterEncoding("UTF-8");
        String task=request.getParameter("task");
        String search_location = "";
        String search_designation = "";
        DesignationLocationTypeModel dltm=new DesignationLocationTypeModel();
        ServletContext ctx = getServletContext();
        int lowerLimit=0, noOfRowsTraversed=0, noOfRowsToDisplay = 9, noOfRowsInTable = 0;
          try
        {

            dltm.setConnection((Connection) DBConnection.getConnectionForUtf(ctx));
        }
        catch(Exception e)
        {
         System.out.print(e);
        }
                  try {
            String JQstring = request.getParameter("action1");
            String q = request.getParameter("q");
            if (JQstring != null) {
                 PrintWriter out = response.getWriter();
                List<String> list = null;
                if (JQstring.equals("getLocationType"))
                {
                    list = DesignationLocationTypeModel.getLocationType(q);
                }
                else if(JQstring.equals("getDesignation"))
                {

               //    list = DesignationLocationTypeModel.getDesignation(q);
                }
                else if(JQstring.equals("getShiftType"))
                {

                  // list = DesignationLocationTypeModel.getShiftType(q);
                }

                Iterator<String> iter = list.iterator();
                while (iter.hasNext()) {
                    String data = iter.next();
                        out.println(data);
                }

                return;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
         if(task==null||task.isEmpty())
               {
                task="";
               }
        try {




           RequestDispatcher rd=request.getRequestDispatcher("designationlocationtype");
           rd.forward(request, response);
        }
        catch(Exception e)
        {
       System.out.print(e);
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
