/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeRed.webServices;

import com.codeRed.dbCon.DBConnection;
import com.codeRed.shift.model.ShiftLoginModel;
import com.codeRed.webServices.model.UserAppWebServiceModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import sun.misc.BASE64Decoder;

/**
 *
 * @author Navitus1
 */
@Path("/complainApp")
public class ComplainAppWebServices {

    static Map<String, String> otpMap = new HashMap<String, String>();
    @Context
    ServletContext serveletContext;//= getServletContext() ;
    Connection connection = null;

//    @GET
//    @Path("/g")
//    public String showInfo() throws Exception {
//        System.out.println("InfoWebServices.java");
//        return "<html><body><input type='text' value='InfoWebServices'></body></html>";
//    }
//    @POST
//    @Path("/registerUser")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public String registerUser(JSONObject jsonObj) throws Exception {
//        System.out.println("UserAppWebServices...");
//        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
//        try {
//            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
//        } catch (Exception ex) {
//            System.out.println("ERROR : in vehicleDetail() in ComplainAppWebService : " + ex);
//        }
//        int a = userAppWebServiceModel.registerUser(jsonObj);
//        System.out.println("Data Retrived : " + jsonObj);
//        userAppWebServiceModel.closeConnection();
//        if (a > 0) {
//            sendOTP(jsonObj.get("mobile_no").toStrjpss
//    ing());
//            //return "success";
//        } //else
//        //return "fail";
//        return a + "";
//    }
//
    @POST
    @Path("/insertComplain")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String insertComplain(JSONObject jsonObj) throws Exception {
        System.out.println("UserAppWebServices...");
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        try {
            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in insertComplain() in ComplainAppWebService : " + ex);
        }
        int a = userAppWebServiceModel.insertComplain(jsonObj);
        System.out.println("Data Retrived : " + jsonObj);
        userAppWebServiceModel.closeConnection();
        return "" + a;
    }

    @POST
    @Path("/saveComplainReportFiles")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String saveComplainReportFiles(JSONObject json) throws Exception {
//        System.out.println("UserAppWebServices...");
//        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
//        try {
//            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
//        } catch (Exception ex) {
//            System.out.println("ERROR : in vehicleDetail() in ComplainAppWebService : " + ex);
//        }
//        int a = userAppWebServiceModel.editUserProfile(jsonObj);
//        System.out.println("Data Retrived : " + jsonObj);
//        userAppWebServiceModel.closeConnection();
        int affected = 0;
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        try {
            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Receiving data...");
        String path = "C:\\ssadvt_repository\\CodeRed\\complaint\\report_status";
        int complaint_id = Integer.parseInt(json.get("report_status_id").toString());
        path = path + complaint_id + "\\";
        makeDirectory(path);
        FileOutputStream outputStream = null;
        String audioFileName = json.get("fileName") == null ? "" : json.get("fileName").toString();
        byte[] fileAsBytes = null;
        if (audioFileName != null && !audioFileName.isEmpty()) {
            try {
                System.out.println(" Audio Uploading.... ");
                fileAsBytes = new BASE64Decoder().decodeBuffer(json.get("audio").toString());
                outputStream = new FileOutputStream(new File(path + audioFileName));
                outputStream.write(fileAsBytes);
                outputStream.close();
                affected = userAppWebServiceModel.insertImageRecord(audioFileName, complaint_id, 5);
                if (affected > 0) {
                    System.out.println(" Audio Uploaded Successfully ");
                } else {
                    System.out.println(" Audio Uploading Failed");
                }
            } catch (Exception ex) {
                System.out.println("ERROR : in saveComplaintFiles() in ComplainAppWebservices : " + ex);
            }
        }
        JSONArray jsonArray = null;
        int size = 0;
        org.json.JSONObject jsn = new org.json.JSONObject(json.toString());
        if (json.get("images") != null) {
            jsonArray = jsn.getJSONArray("images");//new JSONArray(json.get("images") == null ? "" : json.get("images").toString());
            size = jsonArray.length();
        }
        org.json.JSONObject jsonObject = null;
        for (int i = 0; i < size; i++) {
            try {
                System.out.println(" Image Uploading.....");
                jsonObject = jsonArray.getJSONObject(i);
                fileAsBytes = new BASE64Decoder().decodeBuffer(jsonObject.getString("byte_arr"));
                String fileName = jsonObject.getString("imgname");
                String file = path + fileName;
                outputStream = new FileOutputStream(file);
                outputStream.write(fileAsBytes);
                outputStream.close();
                affected = userAppWebServiceModel.insertImageRecord(fileName, complaint_id, 6);
                if (affected > 0) {
                    System.out.println(" Image Uploaded Successfully ");
                } else {
                    System.out.println(" Image Uploading Failed");
                }
            } catch (Exception ex) {
                System.out.println("ERROR : in saveComplainReportFiles() in ComplainAppWebservices : " + ex);
            }
        }
        if (affected > 0) {
            return "success";
        } else {
            return "error";
        }
    }
// ,audio,fileName

    @POST
    @Path("/searchComplain")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONObject searchComplain(String data) throws Exception {
        JSONObject json = new JSONObject();
        String mobile_no = data.split("##")[1];
        int officer_id = Integer.parseInt(data.split("##")[0]);
        System.out.println("UserAppWebServices...");
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        try {
            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String result = userAppWebServiceModel.getComplainData(mobile_no, json, officer_id);
        if (result.equals("success")) {
            json.put("result", "success");
        } else {
            json.put("result", "fail");
        }

        userAppWebServiceModel.closeConnection();
        return json;
    }

    @POST
    @Path("/loginPolice")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONObject loginPolice(String mobile_no) throws Exception {
        JSONObject json = new JSONObject();
        System.out.println("UserAppWebServices...");
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        try {
            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String result = userAppWebServiceModel.loginPolice(mobile_no, json);
        if (result.equals("success")) {
            json.put("result", "success");
        } else {
            json.put("result", "fail");
        }
        userAppWebServiceModel.closeConnection();
        return json;
    }

    public boolean makeDirectory(String dirPathName) {
        boolean result = false;
        File directory = new File(dirPathName);
        if (!directory.exists()) {
            result = directory.mkdirs();
        }
        return result;
    }
}
