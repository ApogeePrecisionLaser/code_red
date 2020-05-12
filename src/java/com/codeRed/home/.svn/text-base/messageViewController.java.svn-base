/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeRed.home;

//import com.association.home.individualHomeModel;

import com.codeRed.dbCon.DBConnection;
import com.codeRed.util.UniqueIDGenerator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Manpreet
 */
public class messageViewController extends HttpServlet {

    private File tmpDir;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext ctx = getServletContext();
        messageViewModel homeModel = new messageViewModel();
        Map<String, String> map = new HashMap<String, String>();
        int lowerLimit = 0, noOfRowsTraversed = 0, noOfRowsToDisplay = 10, noOfRowsInTable;
        HttpSession sess = request.getSession(false);
        int keyperson_id=0;
            if (sess == null)
            {
                request.getRequestDispatcher("/beforeLoginPage").forward(request, response);
                return;
            }

        if (sess != null) {
            keyperson_id = (Integer) sess.getAttribute("user_id");
        }

        try {
            homeModel.setConnection(DBConnection.getConnection(ctx));
        } catch (Exception e) {
            System.out.println("error in individualHomeController setConnection() calling try block" + e);
        }

        try {            
            List items = null;
            Iterator itr = null;
            DiskFileItemFactory fileItemFactory = new DiskFileItemFactory(); //Set the size threshold, above which content will be stored on disk.
            fileItemFactory.setSizeThreshold(1 * 1024 * 1024); //1 MB Set the temporary directory to store the uploaded files of size above threshold.
            fileItemFactory.setRepository(tmpDir);
            ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
            try {
                items = uploadHandler.parseRequest(request);         
                itr = items.iterator();
                while (itr.hasNext()) {
                    FileItem item = (FileItem) itr.next();
                    if (item.isFormField()) {
                        System.out.println("File Name = " + item.getFieldName() + ", Value = " + item.getString() + "\n");//(getString())its for form field
                        map.put(item.getFieldName(), item.getString("UTF-8"));
                    } else {
                        System.out.println("File Name = " + item.getFieldName() + ", Value = " + item.getName());//it is (getName()) for file related things
                        if (item.getName() == null || item.getName().isEmpty()) {
                            map.put(item.getFieldName(), "");
                        } else {
                            String image_name = item.getName();
                            image_name = image_name.substring(0, image_name.length());
                            System.out.println(image_name);
                            map.put(item.getFieldName(), item.getName());
                        }
                    }
                }
                itr = null;
                itr = items.iterator();
                
            } catch (Exception ex) {
                System.out.println("KeypersonController Error encountered while uploading file" + ex);
            }

            String task = request.getParameter("task");
            if (task == null) {
                task = map.get("task");
                if (task == null) {
                    task = "";
                }
            }
            if (task.equals("Save Message")) {
                individualHomeBean homeBean = new individualHomeBean();
                // homeBean.setMessage_id(Integer.parseInt(request.getParameter("message_id")));map.get("address_line1").trim()accept=".xls,.xlsx,.pdf,.doc,.docx"
                homeBean.setMessage(map.get("message").trim());
                homeBean.setSubject(map.get("subject").trim());
                homeBean.setLink(map.get("link").trim());
                homeBean.setKeyperson_id(keyperson_id);
                homeBean.setFile_path(map.get("file_name"));
                homeBean.setImage_path(map.get("img_name"));
                String file_destination = homeModel.getDestination_Path("file"); //C:\association_repository\KeyPerson\PHOTO
                String image_destination = homeModel.getDestination_Path("image");
//                File f = new File(file_destination);
//                File f2 = new File(image_destination);
//                if (f.exists()) {
//                    String ext = file_destination.split("\\.")[1];
//                    if (ext.equals("xlsx") || ext.equals("xls")) {
//                        response.setContentType("application/vnd.ms-excel");
//                        response.setHeader("Content-Disposition", "attachment;filename=sheet.xls");
//                    } else {
//                        if (ext.equals("pdf")) {
//                            response.setContentType("application/" + ext);
//                        }
//                    }
//                }
//                if (f2.exists()) {
//                    String ext = image_destination.split("\\.")[1];
//                    response.setContentType("image/" + ext);
//                }
                homeModel.insertRecord(homeBean, itr, file_destination, image_destination);
            } else if (task.equals("showFullMsg")) {
                int idVal = Integer.parseInt(request.getParameter("idVal").trim());
                List<individualHomeBean> HomeList = homeModel.showData1(idVal);
                List<individualHomeBean> imageList = homeModel.getImageNameList(idVal);
                List<individualHomeBean> fileList = homeModel.getFileNameList(idVal);

                // Now set request scoped attributes, and then forward the request to view.
                request.setAttribute("HomeList", HomeList);
                request.setAttribute("imageList", imageList);
                request.setAttribute("fileList", fileList);
                homeModel.closeConnection();
                request.getRequestDispatcher("msgFullView").forward(request, response);
                return;

            } else if(task.equals("getImage") || task.equals("getImageThumb")) {                  // if (task.equals("openAttachment")) {
                String repository_path = "C:\\association_repository\\general\\no_image.png";
                int g_id = Integer.parseInt(request.getParameter("general_image_details_id").trim());
                String image_name = request.getParameter("image_name").trim();
                String attachment_destination = homeModel.getAttachment_destination(g_id, image_name);   // C:\association_repository\displaymsg\

                String[] img = image_name.split("_");
                String date = img[0];
                String[] dt = date.split("-");
                String year = dt[0];
                String month = dt[1];
                String id = img[1];
                attachment_destination = attachment_destination.concat(year).concat("\\").concat(month).concat("\\").concat(date).concat("\\").concat(id).concat("\\").concat(image_name);

                if (attachment_destination == null || attachment_destination.isEmpty()) {
                    attachment_destination = repository_path;
                } else {
                    File f = new File(attachment_destination);
                    if (f.exists()) { 
                        String ext = attachment_destination.split("\\.")[1];
                        if (ext.equals("xlsx") || ext.equals("xls")) {
                            response.setContentType("application/vnd.ms-excel");
                            response.setHeader("Content-Disposition", "attachment;filename=sheet.xls");
                        } else {
                            if (ext.equals("pdf")) {
                                response.setContentType("application/" + ext);
                            } else {
                                response.setContentType("image/" + ext);
                            }
                        }
                    } else {
                        attachment_destination = repository_path;
                    }
                }
                ServletOutputStream os = response.getOutputStream();
                FileInputStream is = new FileInputStream(new File(attachment_destination));
                byte[] buf = new byte[32 * 1024];
                int nRead = 0;
                while ((nRead = is.read(buf)) != -1) {
                    os.write(buf, 0, nRead);
                }
                os.flush();
                os.close();
                is.close();
                homeModel.closeConnection();
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
            noOfRowsInTable = homeModel.getNoOfRows();                  // get the number of records (rows) in the table.

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

            if (task.equals("Save Message")) {
                lowerLimit = lowerLimit - noOfRowsTraversed;    // Here objective is to display the same view again, i.e. reset lowerLimit to its previous value.
            }

            List<individualHomeBean> HomeList = homeModel.showData(lowerLimit, noOfRowsToDisplay);

            lowerLimit = lowerLimit + HomeList.size();
            noOfRowsTraversed = HomeList.size();

            // Now set request scoped attributes, and then forward the request to view.
            request.setAttribute("lowerLimit", lowerLimit);
            request.setAttribute("noOfRowsTraversed", noOfRowsTraversed);
            request.setAttribute("HomeList", HomeList);

            if ((lowerLimit - noOfRowsTraversed) == 0) {     // if this is the only data in the table or when viewing the data 1st time.
                request.setAttribute("showFirst", "false");
                request.setAttribute("showPrevious", "false");
            }
            if (lowerLimit == noOfRowsInTable) {             // if No further data (rows) in the table.
                request.setAttribute("showNext", "false");
                request.setAttribute("showLast", "false");
            }

            request.setAttribute("IDGenerator", new UniqueIDGenerator());
            request.setAttribute("message", homeModel.getMessage());
            request.setAttribute("msgBgColor", homeModel.getMsgBgColor());

            homeModel.closeConnection();
            request.getRequestDispatcher("messageview").forward(request, response);
        } catch (Exception e) {
            System.out.println("individualHomeController main thread " + e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
