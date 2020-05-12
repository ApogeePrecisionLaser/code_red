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
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
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
@Path("/app")
public class UserAppWebServices {

    public static Map<String, String> otpMap = new HashMap<String, String>();
    @Context
    ServletContext serveletContext;//= getServletContext() ;
    Connection connection = null;

    @GET
    @Path("/g")
    public String showInfo() throws Exception {
        System.out.println("InfoWebServices.java");
        return "<html><body><input type='text' value='InfoWebServices'></body></html>";
    }

    @POST
    @Path("/registerUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String registerUser(JSONObject jsonObj) throws Exception {
        System.out.println("UserAppWebServices...");
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        try {
            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in vehicleDetail() in RideWebservices : " + ex);
        }
        int a = userAppWebServiceModel.registerUser(jsonObj);
        System.out.println("Data Retrived : " + jsonObj);
        userAppWebServiceModel.closeConnection();
        if (a > 0) {
            sendOTP(jsonObj.get("mobile_no").toString());
            //return "success";
        } //else
        //return "fail";
        return a + "";
    }

    @POST
    @Path("/sendsmsfamily")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String sendSmsToFamily(String no) {
        String familyno = no.split("##")[0];
        String trackno = "";
        try {
            trackno = no.split("##")[1].substring(3, 13);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = "";

//        String keypersonData = getkeypersonName(user_id);
//        String keypersonName = keypersonData.split("#")[0];
//        String keypersonNo = keypersonData.split("#")[1];

        try {
            int count = 1;
            int state;
            state = 4;

            String host_url = "http://login.smsgatewayhub.com/api/mt/SendSMS?";//"http://api.mVaayoo.com/mvaayooapi/MessageCompose?";
            String from_user_name = "jpss1277@gmail.com";        // e.g. "jpss1277@gmail.com"
            String from_user_password = "8826887606"; // e.g. "8826887606"
            String sender_id = java.net.URLEncoder.encode("TEST SMS", "UTF-8");         // e.g. "TEST+SMS"

            String strMsg = " My location is ";
//            if (latitude1 != 0 && longitude1 != 0) {
//                strMsg += " http://www.google.com/maps/place/" ;
//            strMsg += " http://122.176.75.92:8077/CodeRed/";
            String otp = UserAppWebServiceModel.random(10);
            otpMap.put(trackno, otp);
//            strMsg += " http://45.114.142.35:8080/CodeRed/ShiftShowController?task=showMapWindow&logitude=&lattitude=&otp=ABC" + otp;
            strMsg += " http://45.114.142.35:8080/CodeRed/ShiftShowController?task=showMapWindow&logitude=&lattitude=&otp=ABC" + otp;
//            }

            String strMsg1 = java.net.URLEncoder.encode(strMsg, "UTF-8");

//            for (int i = 0; i < list.size(); i++) {
            String mobile_no = familyno;
//                String queryString = "user=" + from_user_name + ":" + from_user_password + "&senderID="
//                        + sender_id + "&receipientno=" + mobile_no + "&msgtxt=" + strMsg1;
//                        //+ "&state=" + state;

            String queryString = "APIKey=WIOg7OdIzkmYTrqTsw262w&senderid=JPSOFT&channel=2&DCS=8&flashsms=0&number=" + mobile_no + "&text=" + strMsg1 + "&route=";
            String url = host_url + queryString;
            result = callURL(url);
            System.out.println("result :" + result + "SMS URL: " + url);
//            }

        } catch (Exception e) {
            result = e.toString();
            System.out.println("RegistrationModel - sendSmsForHelp() Error: " + e);
        }
        return result;
    }

    private String callURL(String strURL) {
        String status = "";
        try {
            java.net.URL obj = new java.net.URL(strURL);
            HttpURLConnection httpReq = (HttpURLConnection) obj.openConnection();
            httpReq.setDoOutput(true);
            httpReq.setInstanceFollowRedirects(true);
            httpReq.setRequestMethod("GET");
            status = httpReq.getResponseMessage();
        } catch (MalformedURLException me) {
            status = me.toString();
        } catch (IOException ioe) {
            status = ioe.toString();
        } catch (Exception e) {
            status = e.toString();
        }
        return status;
    }

    @POST
    @Path("/editUserProfile")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editUserProfile(JSONObject jsonObj) throws Exception {
        System.out.println("UserAppWebServices...");
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        try {
            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in vehicleDetail() in RideWebservices : " + ex);
        }
        int a = userAppWebServiceModel.editUserProfile(jsonObj);
        System.out.println("Data Retrived : " + jsonObj);
        userAppWebServiceModel.closeConnection();
        return a + "";
    }

    @POST
    @Path("/loginUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONObject loginUser(JSONObject jsonObj) throws Exception {
        System.out.println("UserAppWebServices...");
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        try {
            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in loginUser() in RideWebservices : " + ex);
        }
        userAppWebServiceModel.loginUser(jsonObj);
        String result = "";

        if (jsonObj.get("mobile_no_verified").toString().equals("N")) {
            sendOTP(jsonObj.get("mobile_no").toString());
            result = "OTP";
        } else if (jsonObj.get("login_id") != null && !jsonObj.get("login_id").toString().isEmpty()) {
            jsonObj.put("sms_info", userAppWebServiceModel.getCodeRedContactNo());
            result = "success";
        }
        if (!result.isEmpty()) {
            jsonObj.put("result", result);
        }
        userAppWebServiceModel.closeConnection();
        System.out.println("Data Retrived : " + jsonObj);
        return jsonObj;
    }

    @POST
    @Path("/logoutUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String logoutUser(String login_id) throws Exception {
        System.out.println("UserAppWebServices...");
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        try {
            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in logoutUser() in RideWebservices : " + ex);
        }
        int a = userAppWebServiceModel.logoutUser(login_id);
        System.out.println("Data Retrived : " + login_id);
        userAppWebServiceModel.closeConnection();
        if (a > 0) {
            return "success";
        } else {
            return "fail";
        }
    }

    @POST
    @Path("/forgetPassword")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String forgetPassword(String mobile_no) throws Exception {
        System.out.println("forgetPassword in UserAppWebServices...");
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        try {
            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in forgetPassword() in UserAppWebServices : " + ex);
        }
        int a = userAppWebServiceModel.verifyMobileNo(mobile_no);
        System.out.println("Data Retrived : " + mobile_no);
        userAppWebServiceModel.closeConnection();
        if (a > 0) {
            sendOTP(mobile_no);
            return "success";
        } else {
            return "fail";
        }
    }

    @POST
    @Path("/resetPassword")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String resetPassword(JSONObject json) throws Exception {
        System.out.println("forgetPassword in UserAppWebServices...");
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        try {
            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in forgetPassword() in UserAppWebServices : " + ex);
        }
        int a = userAppWebServiceModel.resetPassword(json);
        System.out.println("Data Retrived : " + json);
        userAppWebServiceModel.closeConnection();
        if (a > 0) {
            return "success";
        } else {
            return "fail";
        }
    }

    @POST
    @Path("/sendOTP")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String sendOTP(String mobile_no) throws Exception {
        String otp = "";
        System.out.println("UserAppWebServices...");
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        otp = UserAppWebServiceModel.random(6);
        otpMap.put(mobile_no, otp);
        userAppWebServiceModel.sendSmsToAssignedFor(mobile_no, "OTP:- " + otp);
        System.out.println("Data Retrived : " + mobile_no);
        return otp;
    }

    @POST
    @Path("/reSendOTP")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String reSendOTP(String mobile_no) throws Exception {
        String otp = "";
        System.out.println("UserAppWebServices...");
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        otp = otpMap.get(mobile_no);
        if (otp != null) {
            userAppWebServiceModel.sendSmsToAssignedFor(mobile_no, "OTP:- " + otp);
        } else {
            otp = "fail";
        }
        System.out.println("Data Retrived : " + mobile_no);
        return otp;
    }

    @POST
    @Path("/verifyOTP")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONObject verifyOTP(String mobile_no_otp) throws Exception {
        String otp = "";
        JSONObject jsonObj = new JSONObject();
        System.out.println("UserAppWebServices...");
        String mobile_no = "";
        String[] array = mobile_no_otp.split("_");
        if (array.length == 2) {
            mobile_no = array[0];
            otp = array[1];
        } else {
            System.out.println("UserAppWebServices...verifyOTP..." + mobile_no_otp);
        }
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        try {
            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in vehicleDetail() in RideWebservices : " + ex);
        }
        if (otp.equals(otpMap.get(mobile_no))) {
            otpMap.remove(mobile_no);
            userAppWebServiceModel.mobileNoVerified(mobile_no);
            jsonObj.put("result", "success");
            jsonObj.put("sms_info", userAppWebServiceModel.getCodeRedContactNo());
            userAppWebServiceModel.loginUser(jsonObj, mobile_no);
        } else {
            jsonObj.put("result", "fail");
        }
        userAppWebServiceModel.closeConnection();
        return jsonObj;
    }

    @POST
    @Path("/getUserHistory")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    //@Produces(MediaType.TEXT_PLAIN)
    public JSONObject getUserHistory(String user_id) throws Exception {
        JSONObject obj = new JSONObject();
        System.out.println("getUserHistory");
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        try {
            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in getUserHistory() in RideWebservices : " + ex);
        }
        obj.put("userHistory", userAppWebServiceModel.getUserHistory(user_id));
        userAppWebServiceModel.closeConnection();
        return obj;
    }

    @POST
    @Path("/rideFeedback")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    //@Produces(MediaType.TEXT_PLAIN)
    public String rideFeedback(JSONObject json) throws Exception {
        JSONObject obj = new JSONObject();
        System.out.println("getRideStatus");
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        try {
            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in rideFeedback() in RideWebservices : " + ex);
        }
        int affected = userAppWebServiceModel.rideFeedback(json);
        userAppWebServiceModel.closeConnection();
        if (affected > 0) {
            return "success";
        } else {
            return "fail";
        }
    }

    @POST
    @Path("/sendEmergencySMS")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String sendEmergencySMS(JSONObject json) throws Exception {
        JSONObject obj = new JSONObject();
        System.out.println("sendEmergencySMS");
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        try {
            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in sendEmergencySMS() in UserAppWebservices : " + ex);
        }
        int affected = userAppWebServiceModel.sendEmergencySMS(json);
        userAppWebServiceModel.closeConnection();
        if (affected > 0) {
            String complaint_id = json.get("complaint_id") == null ? "" : json.get("complaint_id").toString();
            if (!complaint_id.isEmpty()) {
                return "success_" + complaint_id;
            } else {
                return "success";
            }
        } else {
            return "fail";
        }
    }

    @POST
    @Path("/saveComplainFromSms")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String saveComplainFromSms(JSONObject json) throws Exception {
        JSONObject obj = new JSONObject();
        System.out.println("saveComplainFromSms");
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        try {
            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in saveComplainFromSms() in UserAppWebservices : " + ex);
        }
        int affected = userAppWebServiceModel.saveComplainFromSms(json);
        userAppWebServiceModel.closeConnection();
        if (affected > 0) {
            return "success";
        } else {
            return "fail";
        }
    }

    @POST
    @Path("/saveCoderedteamCordinate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String saveCoderedteamCordinate(JSONObject jsonObj) throws Exception {
        JSONObject obj = new JSONObject();
        String latitude, longitude, imei_no, emp_code, mobile_no;
        // imei_no = jsonObj.get("deviceid") == null?"":jsonObj.get("deviceid").toString();
        //  emp_code = jsonObj.get("empcode").toString();
        latitude = jsonObj.get("latitude").toString();
        longitude = jsonObj.get("longitude").toString();
        if (jsonObj.get("mobile_no") == null) {
            mobile_no = "";
        } else {
            mobile_no = jsonObj.get("mobile_no").toString();
            int length = mobile_no.length();
            mobile_no = mobile_no.substring(length - 10, length);
        }

        System.out.println("saveCoderedteamCordinate" + mobile_no);
        ShiftLoginModel slm = new ShiftLoginModel();
        try {
            slm.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in insertRecord() in ShiftWebservices : " + ex);
        }
        int rowsaffected = slm.insertCordinate(latitude, longitude, "0", "", mobile_no);
        slm.closeConnection();
        if (rowsaffected > 0) {
            return "success";
        } else {
            return "fail";
        }
    }

    @POST
    @Path("/saveComplaintFiles")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String saveComplaintFiles(JSONObject json) throws Exception {
        int affected = 0;
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        try {
            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in saveComplaintFiles() in UserAppWebservices : " + ex);
        }
        System.out.println("Receiving data...");
        String path = "C:/ssadvt_repository/CodeRed/complaint/";
        int complaint_id = Integer.parseInt(json.get("complaint_id").toString());
        path = path + complaint_id + "/";
        makeDirectory(path);
        FileOutputStream outputStream = null;
        String audioFileName = json.get("fileName") == null ? "" : json.get("fileName").toString();
        byte[] fileAsBytes = null;
        if (audioFileName != null && !audioFileName.isEmpty()) {
            fileAsBytes = new BASE64Decoder().decodeBuffer(json.get("audio").toString());
            outputStream = new FileOutputStream(new File(path + audioFileName));
            outputStream.write(fileAsBytes);
            outputStream.close();
            affected = userAppWebServiceModel.insertImageRecord(audioFileName, complaint_id, 3);
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
            jsonObject = jsonArray.getJSONObject(i);
            fileAsBytes = new BASE64Decoder().decodeBuffer(jsonObject.getString("byte_arr"));
            String fileName = jsonObject.getString("imgname");
            String file = path + fileName;
            outputStream = new FileOutputStream(file);
            outputStream.write(fileAsBytes);
            outputStream.close();
            affected = userAppWebServiceModel.insertImageRecord(fileName, complaint_id, 4);
        }
        if (affected > 0) {
            return "success";
        } else {
            return "error";
        }
    }

    @POST
    @Path("/getNodeTreeData")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONObject getNodeTreeData() throws Exception {
        JSONObject obj = new JSONObject();
        System.out.println("getNodeTreeData");
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        try {
            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in getNodeTreeData() in UserAppWebservices : " + ex);
        }
        obj.put("nodeData", userAppWebServiceModel.getNodeData());
        obj.put("treeData", userAppWebServiceModel.getTreeData());
        userAppWebServiceModel.closeConnection();
        return obj;
    }

    @POST
    @Path("/getContactList")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONObject getContactList() throws Exception {
        JSONObject obj = new JSONObject();
        System.out.println("getContactList");
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        try {
            userAppWebServiceModel.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in getContactList() in UserAppWebservices : " + ex);
        }
        obj.put("ContactList", userAppWebServiceModel.getContactList());
        userAppWebServiceModel.closeConnection();
        return obj;
    }

    public boolean makeDirectory(String dirPathName) {
        boolean result = false;
        File directory = new File(dirPathName);
        if (!directory.exists()) {
            result = directory.mkdir();
        }
        return result;
    }
}
