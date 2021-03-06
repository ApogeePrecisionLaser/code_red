/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeRed.complaint.controller;

import com.codeRed.complaint.model.ComplaintReportStatusModel;
import com.codeRed.complaint.tableClasses.ComplaintBean;
import com.codeRed.dbCon.DBConnection;
import com.codeRed.general.model.GeneralModel;
import com.codeRed.util.UniqueIDGenerator;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
 * @author jpss
 */
public class Complain_report_statusController extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            HttpSession sess = request.getSession(false);
            if (sess == null)
            {
                request.getRequestDispatcher("/beforeLoginPage").forward(request, response);
                return;
            }
        int lowerLimit = 0, noOfRowsTraversed = 0, noOfRowsToDisplay = 10, noOfRowsInTable;
        System.out.println("this is ComplaintController....");
        ServletContext ctx = getServletContext();
        ComplaintReportStatusModel complaintModel = new ComplaintReportStatusModel();
        try {
            complaintModel.setConnection(DBConnection.getConnectionForUtf(ctx));
        } catch (Exception ex) {
        }
        request.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");

        String task = request.getParameter("task");
        String searchStatus = request.getParameter("searchStatus");
        String searchEmergency = request.getParameter("searchEmergency");
        String searchOfficerName = request.getParameter("searchOfficerName");
        String searchOfficerCode = request.getParameter("searchOfficerCode");
        String searchMobileNo = request.getParameter("searchMobileNo");
        String searchDate = request.getParameter("searchDate");
        try
        {
            //----- This is only for Vendor key Person JQuery
            String JQstring = request.getParameter("action1");
            String q = request.getParameter("q");   // field own input
            if (JQstring != null) {
                PrintWriter out = response.getWriter();
                List<String> list = null;
                if (JQstring.equals("getMobileNo")) {
                    list = complaintModel.getMobileNo(q);
                }
                if (list != null) {
                    Iterator<String> iter = list.iterator();
                    while (iter.hasNext()) {
                        String data = iter.next();
                        out.println(data);
                    }
                }
                complaintModel.closeConnection();
                out.close();
                return;
            }
        } catch (Exception e) {
            System.out.println("\n Error --ClientPersonMapController get JQuery Parameters Part-" + e);
        }

        if (task == null) {
            task = "";
        }
        if (task.equals("getAudioFile"))
        {
            String complaintId = request.getParameter("complaintId");
            String path = complaintModel.getDestination_Path("Complaint_report_status_audio") + complaintId + "/" + complaintId + ".mp3";
            File file = new File(path);
            response.setContentType("audio/x-wav");
            FileInputStream fis = new FileInputStream(file);
            response.addHeader("Content-Disposition", "attachment; filename=\"audio.mp3\"");
            BufferedInputStream bis = new BufferedInputStream(fis);
            response.setContentLength(fis.available());
            ServletOutputStream os = response.getOutputStream();
            BufferedOutputStream out = new BufferedOutputStream(os);
            int ch = 0;
            while ((ch = bis.read()) != -1)
            {
                out.write(ch);
            }
            bis.close();
            fis.close();
            out.close();
            os.close();
            response.flushBuffer();
            return;
        }
        String image_destination = "";
        image_destination = request.getParameter("img_destination");
        if (task.equals("viewImage"))
        {
            String complaintId = request.getParameter("complaintId");
            String path = complaintModel.getDestination_Path("Complaint_report_status_image") + complaintId;
            List<String> imageList = complaintModel.getImageNameList(complaintId);
            List<String> list1 = new ArrayList<String>();
            if (!imageList.isEmpty())
            {
                for (int i = 0; i < imageList.size(); i++)
                {
                    list1.add(path + "/" + imageList.get(i));
                }
                request.setAttribute("imageList", list1);
                request.getRequestDispatcher("ImagesView").forward(request, response);
                return;
            } else
            {
                task = "getImage";
            }
        }
        if (task.equals("getImage") || task.equals("getImageThumb"))
        {
            String path = "C:/ssadvt_repository/CodeRed/";
            if (image_destination == null || image_destination.isEmpty()) {
                image_destination = path + "general/no_image.png";
                String ext = image_destination.split("\\.")[1];
                response.setContentType("image/" + ext);
            } else {
                File f = new File(image_destination);
                if (f.exists()) {
                    String ext = image_destination.split("\\.")[1];
                    response.setContentType("image/" + ext);
                } else {
                    image_destination = path + "general/no_image.png";
                }
            }
            ServletOutputStream os = response.getOutputStream();
            FileInputStream is = new FileInputStream(new File(image_destination));
            byte[] buf = new byte[1024 * 1024];
            int nRead = 0;
            while ((nRead = is.read(buf)) != -1) {
                os.write(buf, 0, nRead);
            }
            os.flush();
            os.close();
            is.close();
            return;
        }
        
        if (task.equals("generateMapReport"))
        {
            String jrxmlFilePath;
            List list = null;
            GeneralModel generalModel = new GeneralModel();
            response.setContentType("application/pdf");
            ServletOutputStream servletOutputStream = response.getOutputStream();
            jrxmlFilePath = ctx.getRealPath("/report/sourceDestinationReport.jrxml");
            //list=complaintModel.showData(-1, -1,searchsourcename,searchdestinationname,searchcity);
            byte[] reportInbytes = generalModel.generateRecordList(jrxmlFilePath, list);
            //generalModel.generateExcelList(jrxmlFilePath,list);
            response.setContentLength(reportInbytes.length);
            servletOutputStream.write(reportInbytes, 0, reportInbytes.length);
            servletOutputStream.flush();
            servletOutputStream.close();
            complaintModel.closeConnection();
            return;
        }
        if (task.equals("Delete")) {
            complaintModel.cancelRecord(Integer.parseInt(request.getParameter("complain_id")));  // Pretty sure that organisation_type_id will be available.
        } else if (task.equals("Save") || task.equals("Save AS New") || task.equals("Revise")) {
            int complain_id, complain_revision;
            try {
                complain_id = Integer.parseInt(request.getParameter("complain_id"));
                complain_revision = Integer.parseInt(request.getParameter("complain_revision"));
            } catch (Exception e) {
                complain_id = 0;
                complain_revision = 0;
            }
            if (task.equals("Save AS New")) {
                complain_id = 0;
                complain_revision = 0;
            }
            ComplaintBean complaintBean = new ComplaintBean();
            complaintBean.setComplain_id(complain_id);
            complaintBean.setRevision_no(complain_revision);
            complaintBean.setVictim_name(request.getParameter("victim_name"));
            complaintBean.setMobile_no(request.getParameter("mobile_no"));
            complaintBean.setDate_time(request.getParameter("date") + " " + request.getParameter("time_h") + ":" + request.getParameter("time_m"));
            complaintBean.setText(request.getParameter("text"));
            complaintBean.setNode_name(request.getParameter("node_name"));
            String node_id = request.getParameter("node_id");
            if (node_id != null && !node_id.isEmpty()) {
                complaintBean.setNode_id(Integer.parseInt(node_id));
            }
            complaintBean.setIs_emergency(request.getParameter("is_emergency"));
            String officer_name = request.getParameter("officer_name");
            String officer_code = request.getParameter("officer_code");
            complaintBean.setOfficer_name(officer_name);
            complaintBean.setOfficer_id(complaintModel.getOfficerId(officer_code));
            double latitude, longitude;
            try {
                latitude = Double.parseDouble(request.getParameter("latitude"));
            } catch (Exception ex) {
                latitude = 0;
            }
            try {
                longitude = Double.parseDouble(request.getParameter("longitude"));
            } catch (Exception ex) {
                longitude = 0;
            }
            complaintBean.setLatitude(latitude);
            complaintBean.setLongitude(longitude);
            complaintBean.setRemark(request.getParameter("remark"));
            complaintBean.setStatus(request.getParameter("status"));
            //if (destination_id == 0) {
            System.out.println("Inserting values by complaintModel......");
            complaintModel.insertRecord(complaintBean, task);
        }

        try {
            if (searchStatus == null) {
                searchStatus = "";
            }
            if (searchEmergency == null) {
                searchEmergency = "";
            }
            if (searchMobileNo == null) {
                searchMobileNo = "";
            }
            if (searchDate == null) {
                searchDate = "";
            }
            if (searchOfficerName == null) {
                searchOfficerName = "";
            }
            if (searchOfficerCode == null) {
                searchOfficerCode = "";
            }

        } catch (Exception e) {
        }
        // Start of Search Table
        try {
            lowerLimit = Integer.parseInt(request.getParameter("lowerLimit"));
            noOfRowsTraversed = Integer.parseInt(request.getParameter("noOfRowsTraversed"));
        } catch (Exception e) {
            lowerLimit = noOfRowsTraversed = 0;
        }
        String buttonAction = request.getParameter("buttonAction"); // Holds the name of any of the four buttons: First, Previous, Next, Delete.
        if (buttonAction == null) {
            buttonAction = "none";
        }
        if (task.equals("Show All Records")) {
            searchMobileNo = "";
            searchDate = "";
            searchStatus = "";
            searchEmergency = "";
            searchOfficerName = "";
            searchOfficerCode = "";
        }
        System.out.println("searching.......... ");
        noOfRowsInTable = complaintModel.getNoOfRows(searchMobileNo, searchDate, searchStatus, searchEmergency, searchOfficerCode);                  // get the number of records (rows) in the table.

        if (buttonAction.equals("Next")); // lowerLimit already has value such that it shows forward records, so do nothing here.
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

        if (task.equals("Save") || task.equals("Delete") || task.equals("Save AS New") || task.equals("Revise")) {
            lowerLimit = lowerLimit - noOfRowsTraversed;    // Here objective is to display the same view again, i.e. reset lowerLimit to its previous value.
        }

        List<ComplaintBean> complaintList = complaintModel.showData(lowerLimit, noOfRowsToDisplay, searchMobileNo, searchDate, searchStatus, searchEmergency, searchOfficerCode);
        lowerLimit = lowerLimit + complaintList.size();
        noOfRowsTraversed = complaintList.size();

        if ((lowerLimit - noOfRowsTraversed) == 0) {     // if this is the only data in the table or when viewing the data 1st time.
            request.setAttribute("showFirst", "false");
            request.setAttribute("showPrevious", "false");
        }
        if (lowerLimit == noOfRowsInTable) {             // if No further data (rows) in the table.
            request.setAttribute("showNext", "false");
            request.setAttribute("showLast", "false");
        }
        request.setAttribute("complaintList", complaintList);
        request.setAttribute("statusList", complaintModel.getStatus());
        request.setAttribute("lowerLimit", lowerLimit);
        request.setAttribute("noOfRowsTraversed", noOfRowsTraversed);
        request.setAttribute("searchMobileNo", searchMobileNo);
        request.setAttribute("searchDate", searchDate);
        request.setAttribute("searchOfficerName", searchOfficerName);
        request.setAttribute("searchOfficerCode", searchOfficerCode);
        request.setAttribute("searchStatus", searchStatus);
        request.setAttribute("searchEmergency", searchEmergency);
        request.setAttribute("IDGenerator", new UniqueIDGenerator());
        request.setAttribute("message", complaintModel.getMessage());
        complaintModel.closeConnection();
        request.getRequestDispatcher("/complaint_report_status").forward(request, response);
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
