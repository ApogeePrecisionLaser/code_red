/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.codeRed.shift.controller;

import com.codeRed.dbCon.DBConnection;
import com.codeRed.shift.model.DutyTypeModel;
import com.codeRed.shift.model.LeaveTypeModel;
import com.codeRed.shift.tableClasses.DutyType;
import com.codeRed.shift.tableClasses.LeaveType;
import com.codeRed.util.UniqueIDGenerator;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Navitus1
 */
public class LeaveTypeController extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, SQLException
    {
        try
        {
        int itemNameId;
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        ServletContext ctx = getServletContext();
        LeaveType itemNameBean=new LeaveType();
        LeaveTypeModel itemNameModel=new LeaveTypeModel();
        itemNameModel.setDriverClass(ctx.getInitParameter("driverClass"));
        itemNameModel.setConnectionString(ctx.getInitParameter("connectionString"));
        itemNameModel.setDb_username(ctx.getInitParameter("db_username"));
        itemNameModel.setDb_password(ctx.getInitParameter("db_password"));

        itemNameModel.setConnection(DBConnection.getConnection(ctx));
        String leave_type=request.getParameter("leave_type");
//        String item_code=request.getParameter("item_code");
          String remark=request.getParameter("remark");
          String searchleave_type=request.getParameter("searchleave_type");  if(searchleave_type==null)searchleave_type="";
          String leave_type_id=request.getParameter("leave_type_id");
          if(leave_type==null)leave_type="";
          if(remark==null)remark="";
          if(leave_type_id==null || leave_type_id.isEmpty())itemNameId=0;
          else itemNameId=Integer.parseInt(leave_type_id);
          itemNameBean.setLeave_type(leave_type);
          itemNameBean.setRemark(remark);
          itemNameBean.setLeave_type_id(itemNameId);

          String JQuery=request.getParameter("action1");
            String q=request.getParameter("q");
               if(JQuery!=null)
                {
                   List list=null;
                    if(JQuery.equals("getLeave"))
                    {
                        list=itemNameModel.getLeave_typeName(q);
                    }
                    Iterator<String> iter = list.iterator();
                    while (iter.hasNext())
                    {
                        String data = iter.next();
                        out.println(data);
                    }
                    return;
                }

         int lowerLimit=0, noOfRowsTraversed=0, noOfRowsToDisplay = 6, noOfRowsInTable;
         String task=request.getParameter("task");
         if(task==null)
                task="";
         if(task.equals("Save"))
         {
            itemNameModel.insertRecord(itemNameBean);
         }
         if(task.equals("Save AS New"))
           {
             if(itemNameModel.insertRecord(itemNameBean)){
                 System.out.println("record saved successfully");
             }else{
               System.out.print("record not saved");
             }
           }  
        if(task.equals("Delete"))
           {
               if(itemNameModel.deleteRecord(itemNameBean)){
                   System.out.print("record deleted");
                    } else{
                    System.out.print("record not deleted");
                    }
           }
         if(task.equals("Update"))
           {
               if(itemNameModel.UpdateRecord(itemNameBean)){
                   System.out.print("record deleted");
                    } else{
                    System.out.print("record not deleted");
                    }
           }
         if(task.equals("Show All Records"))
           {
            searchleave_type="";
           }
            String buttonAction = request.getParameter("buttonAction");
         if(buttonAction == null)
                 buttonAction = "none";
              try {
        lowerLimit = Integer.parseInt(request.getParameter("lowerLimit"));
        noOfRowsTraversed = Integer.parseInt(request.getParameter("noOfRowsTraversed"));
            } catch (Exception e)
            {
                lowerLimit = noOfRowsTraversed = 0;
            }
        noOfRowsInTable =itemNameModel.getNoOfRows(searchleave_type);
        if (buttonAction.equals("Next"));
        else if (buttonAction.equals("Previous"))
            {
                int temp = lowerLimit - noOfRowsToDisplay - noOfRowsTraversed;
                if (temp < 0)
                {
                    noOfRowsToDisplay = lowerLimit - noOfRowsTraversed;
                    lowerLimit = 0;
                } else
                {
                    lowerLimit = temp;
                }
            } else if (buttonAction.equals("First"))
            {
                lowerLimit = 0;
            }
            else if (buttonAction.equals("Last"))
            {
                lowerLimit = noOfRowsInTable - noOfRowsToDisplay;
                if (lowerLimit < 0) {
                    lowerLimit = 0;
                }
            }
            List<LeaveType> list=itemNameModel.ShowData(lowerLimit,noOfRowsToDisplay,searchleave_type);
            lowerLimit = lowerLimit + list.size();
            noOfRowsTraversed = list.size();
            if ((lowerLimit - noOfRowsTraversed) == 0) {     // if this is the only data in the table or when viewing the data 1st time.
                request.setAttribute("showFirst", "false");
                request.setAttribute("showPrevious", "false");
            }
            if (lowerLimit == noOfRowsInTable)
            {
                request.setAttribute("showNext", "false");
                request.setAttribute("showLast", "false");
            }
        request.setAttribute("list", list);
        request.setAttribute("message", itemNameModel.getMessage());
            request.setAttribute("msgBgColor", itemNameModel.getMsgBgColor());
        request.setAttribute("searchleave_type", searchleave_type);
         request.setAttribute("lowerLimit",lowerLimit);
        request.setAttribute("noOfRowsTraversed",noOfRowsTraversed);
        request.setAttribute("IDGenerator", new UniqueIDGenerator());
        // request.setAttribute("searchcity", searchcity);
            RequestDispatcher rd=request.getRequestDispatcher("leave_type");
            rd.forward(request, response);
        }catch(Exception e){}
    } 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(LeaveTypeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(LeaveTypeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
