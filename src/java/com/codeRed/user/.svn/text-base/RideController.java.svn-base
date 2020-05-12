package com.codeRed.user;

import com.codeRed.dbCon.DBConnection;
import com.codeRed.general.model.GeneralModel;
import com.codeRed.userTable.RideBean;
import com.codeRed.userModel.RideModel;
import com.codeRed.util.UniqueIDGenerator;
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
 * @author Shubham
 */
public class RideController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int lowerLimit, noOfRowsTraversed, noOfRowsToDisplay = 20, noOfRowsInTable;
        ServletContext ctx = getServletContext();
        HttpSession sess = request.getSession(false);
            if (sess == null)
            {
                request.getRequestDispatcher("/beforeLoginPage").forward(request, response);
                return;
            }
        RideModel rideModel = new RideModel();
        response.setContentType("text/html");
        try {
            rideModel.setConnection(DBConnection.getConnection(ctx));
        } catch (Exception e) {
            System.out.println("error in TarrifController setConnection() calling try block" + e);
        }
        try {

            String JQString = request.getParameter("action1");
            String name = request.getParameter("action2");
            String mobileNo = request.getParameter("action3");
            String email = request.getParameter("action4");
            if (name == null) {
                name = "";
            }
            if (mobileNo == null) {
                mobileNo = "";
            }
            if (email == null) {
                email = "";
            }
            String q = request.getParameter("q");
            if (JQString != null && !JQString.isEmpty()) {
                PrintWriter out = response.getWriter();
                List<String> list = null;
                if (JQString.equals("getName")) {
                    list = rideModel.getName(q, mobileNo, email);
                }
                if (JQString.equals("getMobileNo")) {
                    list = rideModel.getMobileNo(q, name, email);
                }
                if (JQString.equals("getBookingNo")) {
                    list = rideModel.getBookingNo(q, name, mobileNo);
                }
                if (list != null) {
                    Iterator itr = list.iterator();
                    while (itr.hasNext()) {
                        String str = (String) itr.next();
                        out.println(str);
                    }
                }
                return;
            }
        } catch (Exception ex) {
            System.out.println("JQString autocompleter in TarrifController");
        }

        try {
            String task = request.getParameter("task");
            if (task == null) {
                task = "";
            }
            String searchName = request.getParameter("searchName");
            if (searchName == null) {
                searchName = "";
            }
            String searchMobileNo = request.getParameter("searchMobileNo");
            if (searchMobileNo == null) {
                searchMobileNo = "";
            }
            String SearchBookingNo = request.getParameter("SearchBookingNo");
            if (SearchBookingNo == null) {
                SearchBookingNo = "";
            }
            String searchDate = request.getParameter("searchDate");
            if (searchDate == null) {
                searchDate = "";
            }

            if (task.equals("generateReport"))
            {
                String jrxmlFilePath;
                List list = null;
                GeneralModel generalModel = new GeneralModel();
                response.setContentType("application/pdf");
                ServletOutputStream servletOutputStream = response.getOutputStream();
                jrxmlFilePath = ctx.getRealPath("/report/Ride.jrxml");
                list = rideModel.showData(-1, -1, searchName, searchMobileNo, SearchBookingNo, searchDate);
                byte[] reportInbytes = generalModel.generateRecordList(jrxmlFilePath, list);
                response.setContentLength(reportInbytes.length);
                servletOutputStream.write(reportInbytes, 0, reportInbytes.length);
                servletOutputStream.flush();
                servletOutputStream.close();
                rideModel.closeConnection();
                return;
            }
            if (task.equals("generateXlxReport")) {
                String jrxmlFilePath;
                List list = null;
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment; filename=user.xls");
                ServletOutputStream servletOutputStream = response.getOutputStream();
                jrxmlFilePath = ctx.getRealPath("/report/Ride.jrxml");
                list = rideModel.showData(-1, -1, searchName, searchMobileNo, SearchBookingNo, searchDate);
                ByteArrayOutputStream reportInbytes = GeneralModel.generateExcelList(jrxmlFilePath, list);
                response.setContentLength(reportInbytes.toByteArray().length);
                servletOutputStream.write(reportInbytes.toByteArray(), 0, reportInbytes.toByteArray().length);
                servletOutputStream.flush();
                servletOutputStream.close();
                return;
            }
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
                searchName = "";
                searchMobileNo = "";
                SearchBookingNo = "";
                searchDate = "";
            }
            noOfRowsInTable = rideModel.getNoOfRows(searchName, searchMobileNo, SearchBookingNo, searchDate);
            if (buttonAction.equals("Next")); // lowerLimit already has value such that it shows forward records, so do nothing here.
            else if (buttonAction.equals("Previous"))
            {
                int temp = lowerLimit - noOfRowsToDisplay - noOfRowsTraversed;
                if (temp < 0)
                {
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
            List<RideBean> list = rideModel.showData(lowerLimit, noOfRowsToDisplay, searchName, searchMobileNo, SearchBookingNo, searchDate);
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

            request.setAttribute("lowerLimit", lowerLimit);
            request.setAttribute("noOfRowsTraversed", noOfRowsTraversed);
            request.setAttribute("List", list);
            request.setAttribute("searchName", searchName);
            request.setAttribute("searchMobileNo", searchMobileNo);
            request.setAttribute("searchEmailId", SearchBookingNo);
            request.setAttribute("searchDate", searchDate);
            request.setAttribute("IDGenerator", new UniqueIDGenerator());
            request.setAttribute("message", rideModel.getMessage());
            request.setAttribute("msgBgColor", rideModel.getMsgBgColor());
            rideModel.closeConnection();
            request.getRequestDispatcher("rideType").forward(request, response);
            return;
        } catch (Exception e)
        {
            System.out.println("Error in TarrifController" + e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        this.doPost(request, response);
    }
}
