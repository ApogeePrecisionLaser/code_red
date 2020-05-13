/*
 ShiftWorkBench-2
 */

package com.codeRed.shift.controller;

import java.sql.Connection;
import com.codeRed.dbCon.DBConnection;
import com.codeRed.shift.model.ShiftModel;
import com.codeRed.shift.tableClasses.ShiftBean;
import com.codeRed.util.UniqueIDGenerator;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Tushar Singh
 */
public class ShiftController extends HttpServlet {
   
  
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
   {
        response.setContentType("text/html;charset=UTF-8");
          response.setCharacterEncoding("UTF-8");
          request.setCharacterEncoding("UTF-8");
        ServletContext ctx = getServletContext();
        ShiftModel sm=new ShiftModel();
        String task=request.getParameter("task");
         int lowerLimit=0, noOfRowsTraversed=0, noOfRowsToDisplay = 10, noOfRowsInTable = 0;
        try
        {
            
            sm.setConnection((Connection) DBConnection.getConnectionForUtf(ctx));
        }
        catch(Exception e)
        {
         System.out.print(e);
        }

           if(task==null||task.isEmpty())
            {
                task="";
            }

        try {
             if(task.equals("Delete"))
                 sm.deleteRecord(request.getParameter("shift_type_id"));
             else if(task.equals("save") || task.equals("Save AS New"))
             {
                 int shift_type_id=0;
                 try{
                     shift_type_id = Integer.parseInt(request.getParameter("shift_type_id").trim());
                 }catch(Exception ex){
                     shift_type_id = 0;
                 }
                 
                 if(task.equals("Save AS New"))
                     shift_type_id = 0;
             String shift_type=request.getParameter("shift_type");
             String starting_time_hour=request.getParameter("starting_time_hour").trim();
             String starting_time_min=request.getParameter("starting_time_min").trim();
             String ending_time_hour=request.getParameter("ending_time_hour").trim();
             String ending_time_min=request.getParameter("ending_time_min").trim();
             ShiftBean sb=new ShiftBean();
             sb.setShift_type_id(shift_type_id);
             sb.setShift_type(shift_type);
            String starting_time =starting_time_hour + ":" + starting_time_min;
            if(starting_time.equals(":"))
                starting_time="";
            String ending_time=ending_time_hour + ":" + ending_time_min;
            if(ending_time.equals(":"))
                  ending_time="";
             sb.setStarting_time(starting_time);
             sb.setEnding_time(ending_time);
          
            sm.insertRecord(sb);            
         }
                 String buttonAction = request.getParameter("buttonAction");
             if(buttonAction == null)
                 buttonAction = "none";

              try {
        lowerLimit = Integer.parseInt(request.getParameter("lowerLimit"));
        noOfRowsTraversed = Integer.parseInt(request.getParameter("noOfRowsTraversed"));
            } catch (Exception e) {
                lowerLimit = noOfRowsTraversed = 0;
            }

     noOfRowsInTable = ShiftModel.getNoOfRows();

    if (buttonAction.equals("Next"));
      else if (buttonAction.equals("Previous")) {
                int temp = lowerLimit - noOfRowsToDisplay - noOfRowsTraversed;
                if (temp < 0) {
                    noOfRowsToDisplay = lowerLimit - noOfRowsTraversed;
                    lowerLimit = 0;
                } else {
                    lowerLimit = temp;
                }
            } else if (buttonAction.equals("First")) {
                lowerLimit = 0;
            } else if (buttonAction.equals("Last")) {
                lowerLimit = noOfRowsInTable - noOfRowsToDisplay;
                if (lowerLimit < 0) {
                    lowerLimit = 0;
                }
            }
     if (task.equals("save") || task.equals("Save AS New") ||  task.equals("Delete")) {
            lowerLimit = lowerLimit - noOfRowsTraversed;
        }

         List<ShiftBean>  list=  ShiftModel.showData(lowerLimit, noOfRowsToDisplay);

          lowerLimit = lowerLimit + list.size();
            noOfRowsTraversed = list.size();
            if ((lowerLimit - noOfRowsTraversed) == 0) {
                request.setAttribute("showFirst", "false");
                request.setAttribute("showPrevious", "false");
            }
            if (lowerLimit == noOfRowsInTable) {
                request.setAttribute("showNext", "false");
                request.setAttribute("showLast", "false");
            }
         request.setAttribute("list", list);
         request.setAttribute("lowerLimit",lowerLimit);
         request.setAttribute("noOfRowsTraversed",noOfRowsTraversed);
        request.setAttribute("IDGenerator", new UniqueIDGenerator());
        request.setAttribute("message", sm.getMessage());
            RequestDispatcher rd=request.getRequestDispatcher("shift");
            rd.forward(request, response);
            }
       catch(Exception e)
        {
           System.out.print(e);
       }
    } 

   
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
