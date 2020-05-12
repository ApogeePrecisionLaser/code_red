/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.codeRed.shift.controller;

import com.codeRed.dbCon.DBConnection;
import com.codeRed.shift.model.DutyTypeModel;
import com.codeRed.shift.tableClasses.DutyType;
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
import javax.servlet.http.HttpSession;

/**
 *
 * @author Navitus1
 */
public class DutyTypeController extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, SQLException
    {
        try
        {
            HttpSession sess = request.getSession(false);
            if (sess == null)
            {
                request.getRequestDispatcher("/beforeLoginPage").forward(request, response);
                return;
            }
        int itemNameId;
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        ServletContext ctx = getServletContext();
        DutyType itemNameBean=new DutyType();
        DutyTypeModel itemNameModel=new DutyTypeModel();
        itemNameModel.setDriverClass(ctx.getInitParameter("driverClass"));
        itemNameModel.setConnectionString(ctx.getInitParameter("connectionString"));
        itemNameModel.setDb_username(ctx.getInitParameter("db_username"));
        itemNameModel.setDb_password(ctx.getInitParameter("db_password"));

        itemNameModel.setConnection(DBConnection.getConnection(ctx));
        String duty_type=request.getParameter("duty_type");
//        String item_code=request.getParameter("item_code");
          String remark=request.getParameter("remark");
          String searchduty_type=request.getParameter("searchduty_type");  if(searchduty_type==null)searchduty_type="";
          String duty_type_id=request.getParameter("duty_type_id");
          if(duty_type==null)duty_type="";
          if(remark==null)remark="";
          if(duty_type_id==null || duty_type_id.isEmpty())itemNameId=0;
          else itemNameId=Integer.parseInt(duty_type_id);
          itemNameBean.setDuty_type(duty_type);
          itemNameBean.setRemark(remark);
          itemNameBean.setDuty_type_id(itemNameId);

          String JQuery=request.getParameter("action1");
            String q=request.getParameter("q");
               if(JQuery!=null)
                {
                   List list=null;
                    if(JQuery.equals("getDuty"))
                    {
                        list=itemNameModel.getduty_typeName(q);
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
            searchduty_type="";
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
        noOfRowsInTable =itemNameModel.getNoOfRows(searchduty_type);
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
            List<DutyType> list=itemNameModel.ShowData(lowerLimit,noOfRowsToDisplay,searchduty_type);
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
        request.setAttribute("searchduty_type", searchduty_type);
         request.setAttribute("lowerLimit",lowerLimit);
        request.setAttribute("noOfRowsTraversed",noOfRowsTraversed);
        request.setAttribute("IDGenerator", new UniqueIDGenerator());
        // request.setAttribute("searchcity", searchcity);
            RequestDispatcher rd=request.getRequestDispatcher("duty_type");
            rd.forward(request, response);
        }catch(Exception e){}
    } 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(DutyTypeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(DutyTypeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
