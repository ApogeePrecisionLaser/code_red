/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.codeRed.organization.controller;

import com.codeRed.dbCon.DBConnection;
import com.codeRed.organization.model.BeneficiaryModel;
import com.codeRed.organization.tableClasses.Beneficiary;
import com.codeRed.util.UniqueIDGenerator;
import java.sql.Connection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Administrator
 */
public class BeneficiaryController extends HttpServlet {
   
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
          HttpSession sess = request.getSession(false);
            if (sess == null)
            {
                request.getRequestDispatcher("/beforeLoginPage").forward(request, response);
                return;
            }
          response.setCharacterEncoding("UTF-8");
          request.setCharacterEncoding("UTF-8");
          ServletContext ctx = getServletContext();
          BeneficiaryModel bm=new BeneficiaryModel();
          String task=request.getParameter("task");
          String searchOccupationName=request.getParameter("searchOccupationName");
          String searchCityName= request.getParameter("searchCityName");
          String searchZoneName= request.getParameter("searchZone");
          String searchZoneNo= request.getParameter("searchZone_no");
          String searchWardType= request.getParameter("searchWardType");
          String searchCityNo= request.getParameter("searchCityNo");
          String searchWardNo= request.getParameter("searchWardNo");
          String searchArea= request.getParameter("searchArea");
          String searchAreaNo= request.getParameter("searchAreaNo");
          String searchPersonName= request.getParameter("searchPersonName");
           String person_code= request.getParameter("person_code");
           if(searchCityName==null)
            searchCityName="";
           if(searchCityNo==null)
            searchCityNo="";
           if(searchZoneNo==null)
             searchZoneNo="";
          if(searchZoneName==null)
          searchZoneName="";
          if(searchCityNo==null)
            searchCityNo="";
          if(searchWardNo==null)
            searchWardNo="";
          if(searchAreaNo==null)
            searchAreaNo="";
          if(searchArea==null)
              searchArea="";
         if(searchWardType==null)
          searchWardType="";
          if(searchOccupationName== null)
            searchOccupationName="";
         if(searchPersonName== null)
          searchPersonName="";
           if(person_code== null)
          person_code="";
           String emp_code="";
           String person_name="";
           if(task==null||task.isEmpty())
           task="";
           int lowerLimit=0, noOfRowsTraversed=0, noOfRowsToDisplay =5, noOfRowsInTable = 0;
            try{
              bm.setConnection((Connection) DBConnection.getConnectionForUtf(ctx));}
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
                if (JQstring.equals("getTypeOfOccupation"))             
                {
                    list = bm.getTypeOfOccupation(q);
                }
                else if(JQstring.equals("getPersonName"))
                {
                    if (request.getParameter("action2") != null && !request.getParameter("action2").isEmpty())
              emp_code = request.getParameter("action2");
                  list = bm.getPersonName(q,emp_code);
                }
                  else if(JQstring.equals("getSearchPersonName"))
                {
                  list = bm.getSearchPersonName(q);
                }
                 else if(JQstring.equals("getSearchOccupationName"))
                {
                  list = bm.getSearchOccupationName(q);
                }
                else if(JQstring.equals("getperson_code"))
                {
                  list = bm.getperson_code(q);
                }
                  else if(JQstring.equals("getEmp_code"))
                {
                  if (request.getParameter("action2") != null && !request.getParameter("action2").isEmpty())
                    person_name = request.getParameter("action2");
                  list = bm.getEmp_code(q,person_name);
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

           if (task.equals("generateHSReport")) {
                        String jrxmlFilePath;
                        List list=null;
                        response.setContentType("application/pdf");
                        response.setCharacterEncoding("UTF-8");
                        ServletOutputStream servletOutputStream = response.getOutputStream();
                        jrxmlFilePath = ctx.getRealPath("/Report/organization/person.jrxml");
                        list=bm.showAll(-1,noOfRowsToDisplay,searchOccupationName,searchCityName,searchZoneName,searchWardType,searchArea,searchPersonName,person_code);
                        byte[] reportInbytes =bm.generateRecordList(jrxmlFilePath,list);
                        response.setContentLength(reportInbytes.length);
                        servletOutputStream.write(reportInbytes, 0, reportInbytes.length);
                        servletOutputStream.flush();
                        servletOutputStream.close();
                        return;
                     }
           if (task.equals("generateReport"))
{
                     String jrxmlFilePath;
                        List list=null;
                        response.setContentType("application/vnd.ms-excel");
                        response.setHeader("Content-Disposition", "attachment; filename=Beneficiary.xls");
                        ServletOutputStream servletOutputStream = response.getOutputStream();
                        jrxmlFilePath = ctx.getRealPath("/Report/organization/person.jrxml");
                         list=bm.showAll(-1,noOfRowsToDisplay,searchOccupationName,searchCityName,searchZoneName,searchWardType,searchArea,searchPersonName,person_code);
                        ByteArrayOutputStream reportInbytes =bm.generateXlsRecordList(jrxmlFilePath,list);
                        response.setContentLength(reportInbytes.toByteArray().length);
                       servletOutputStream.write(reportInbytes.toByteArray() , 0, reportInbytes.toByteArray().length);
                       servletOutputStream.flush();
                       servletOutputStream.close();

                        return;
}

        try {
                if(task.equals("Delete"))
                bm.deleteRecord(request.getParameter("beneficiary_id"));
                else if(task.equals("save") || task.equals("Save AS New"))
               {
                 int beneficiary_id;
                 try{
                     beneficiary_id = Integer.parseInt(request.getParameter("beneficiary_id").trim());
                 }catch(Exception ex){
                     beneficiary_id = 0;
                 }
                 if(task.equals("Save AS New"))
                     beneficiary_id = 0;
               String occupation_name=request.getParameter("occupation_name");
               String type_of_beneficiary=request.getParameter("type_of_beneficiary");
               String zone=request.getParameter("zone");
               String ward=request.getParameter("ward");
               String area=request.getParameter("area");
               String location=request.getParameter("location");
               person_name=request.getParameter("person_name");
               int code=Integer.parseInt(request.getParameter("emp_code"));
               String no_of_person=request.getParameter("no_of_person");
               String description=request.getParameter("description");
               String is_residencial=request.getParameter("is");
               String rfid=request.getParameter("rfid");
               Beneficiary b=new Beneficiary();
               b.setBeneficiary_id(beneficiary_id);
               b.setType_of_beneficiary(type_of_beneficiary);
               b.setOccupation_name(occupation_name);
               b.setPerson_name(person_name);
               b.setPerson_code(code);
               b.setNo_of_person(Integer.parseInt(no_of_person));
               b.setDescription(description);
               b.setIs_residencial(is_residencial);
               b.setZone(zone);
               b.setWard(ward);
               b.setArea(area);
               b.setLocation(location);
               b.setRfid(rfid);
               bm.insertRecord(b);
         }
            else if (task.equals("Show All Records")) {
               searchOccupationName="";
            }
           String buttonAction = request.getParameter("buttonAction");
             if(buttonAction == null)
                 buttonAction = "none";
              try {
        lowerLimit = Integer.parseInt(request.getParameter("lowerLimit"));
        noOfRowsTraversed = Integer.parseInt(request.getParameter("noOfRowsTraversed"));
            }catch (Exception e) {
                lowerLimit = noOfRowsTraversed = 0;
            }

     noOfRowsInTable = bm.getNoOfRows(searchOccupationName,searchCityName,searchZoneName,searchWardType,searchArea,searchPersonName,person_code);

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
     else if (task.equals("Show All Records")) {
           searchWardType = "";
           searchWardNo="";
           searchCityName = "";
           searchCityNo  = "";
           searchArea = "";
           searchAreaNo="";
           searchZoneName = "";
           searchZoneNo="";
           searchOccupationName="";
            searchPersonName="";
            person_code="";
        }
         List<Beneficiary>  list=  bm.showData(lowerLimit, noOfRowsToDisplay,searchOccupationName,searchCityName,searchZoneName,searchWardType,searchArea,searchPersonName,person_code);
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
        request.setAttribute("searchOccupationName",searchOccupationName);
        request.setAttribute("searchCityName", searchCityName);
         request.setAttribute("searchCityNo", searchCityNo);
         request.setAttribute("searchZone", searchZoneName);
         request.setAttribute("searchZone_no", searchZoneNo);
         request.setAttribute("searchWardNo", searchWardNo);
         request.setAttribute("searchAreaNo", searchAreaNo);
        request.setAttribute("searchWardType", searchWardType);
        request.setAttribute("searchArea", searchArea);
         request.setAttribute("person_code", person_code);
        request.setAttribute("searchPersonName", searchPersonName);
               request.setAttribute("searchArea", searchArea);
        request.setAttribute("noOfRowsTraversed",noOfRowsTraversed);
        request.setAttribute("IDGenerator", new UniqueIDGenerator());
        request.setAttribute("message", bm.getMessage());

     request.getRequestDispatcher("beneficiary").forward(request, response);
        }
        catch(Exception e)
        {
            System.out.println(e);
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
