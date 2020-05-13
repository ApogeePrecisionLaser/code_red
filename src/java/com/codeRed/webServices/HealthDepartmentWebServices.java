/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeRed.webServices;

import com.codeRed.dbCon.DBConnection;
import com.codeRed.general.model.MapDetailClass;
import com.codeRed.shift.model.ShiftLoginModel;
import com.codeRed.shift.model.ShiftTimeModel;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author Administrator
 */
@Path("/shift")
public class HealthDepartmentWebServices {

    @Context
    ServletContext serveletContext;
    Connection connection = null;
    //private  String zone;
    // private  String ward;
    //private  String area;
    static Map<String, String> otpMap = new HashMap<String, String>();

    @POST
    @Path("/detail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    //@Produces(MediaType.TEXT_PLAIN)
    public JSONObject personDetail(String emp_code) throws Exception {
        JSONObject obj = new JSONObject();
        JSONArray arrayObj = new JSONArray();
        Response res = null;
        System.out.println("ShiftWebServices");
        ShiftLoginModel slm = new ShiftLoginModel();
        try {
            slm.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in personDetail() in RideWebservices : " + ex);
        }
        JSONObject jsonObj = slm.getAreaDetails(emp_code);
        String zone = jsonObj.getString("zone_name");
        String ward = jsonObj.getString("ward_name");
        String area = jsonObj.getString("area_name");
        System.out.println("Data Retrived : " + jsonObj);
        arrayObj.add(jsonObj);
        obj.put("Data", arrayObj);
        arrayObj = slm.getBeneficiaryDetails(zone, ward, area);
        System.out.println("Data Retrived : " + arrayObj);
        obj.put("BeneficiaryData", arrayObj);
        arrayObj = slm.getCityDetails(zone, ward, area);
        System.out.println("Data Retrived : " + arrayObj);
        obj.put("city_location", arrayObj);
        arrayObj = slm.getReasonDetails();
        System.out.println("Data Retrived : " + arrayObj);
        obj.put("reason", arrayObj);
        arrayObj = slm.getOccupationTypeDetails();
        System.out.println("Data Retrived : " + arrayObj);
        obj.put("occupation_type", arrayObj);
        res = Response.ok(obj, MediaType.APPLICATION_JSON).build();
        slm.closeConnection();
        return obj;
    }

    @POST
    @Path("/insert")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String InsertRecord(JSONObject jsonObj) throws Exception {
        JSONObject obj = new JSONObject();
        Response res = null;
        String reply = "";
        String beneficiary_id, reason_id, emp_code, status, date_time;
        beneficiary_id = jsonObj.get("beneficiary_id").toString();
        reason_id = jsonObj.get("reason").toString();
        emp_code = jsonObj.get("emp_code").toString();
        status = jsonObj.get("status").toString();
        date_time = jsonObj.get("date").toString();
        //mobile_no = jsonObj.get("mobile_no").toString();
        System.out.println("insertRecord");
        ShiftLoginModel slm = new ShiftLoginModel();
        try {
            slm.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in insertRecord() in ShiftWebservices : " + ex);
        }
        int result = slm.insertShiftRecord(beneficiary_id, reason_id, date_time, emp_code, status);
        String id_attendence = slm.getskpmId(emp_code);
        String skpm_id = id_attendence.split("_")[0];
        String attendence = id_attendence.split("_")[1];
        if (attendence.equals("N")) {
            result = slm.updateAttendence(skpm_id);
            JSONObject jsonObj1 = slm.getAreaDetails(emp_code);
            String zone = jsonObj1.getString("zone_name");
            String ward = jsonObj1.getString("ward_name");
            String area = jsonObj1.getString("area_name");
            slm.getBeneficiaryDetails1(zone, ward, area);
        }
        if (result > 0) {
            System.out.println("Data Retrived : " + jsonObj + " : Saved...");
            reply = "Successfully";
        } else {
            System.out.println("Data Retrived : " + jsonObj + " : Not Saved Some Error...");
            reply = " Not Successfully";
        }
        slm.closeConnection();
        return reply;
    }

    @POST
    @Path("/shiftFinish")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String shiftFinish(String emp_id) throws Exception {
        ShiftLoginModel slm = new ShiftLoginModel();
        try {
            slm.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in insertRecord() in ShiftWebservices : " + ex);
        }
        JSONObject jsonObj = slm.getAreaDetails(emp_id);
        String zone = jsonObj.getString("zone_name");
        String ward = jsonObj.getString("ward_name");
        String area = jsonObj.getString("area_name");
        System.out.println("Data Retrived : " + jsonObj);
        slm.insertAllRemainingBeneficary(emp_id, zone, ward, area);
        slm.closeConnection();
        return "Success";
    }

    @POST
    @Path("/insertKeyPerson")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String InsertKeyPerson(JSONObject jsonObj) throws Exception {
        JSONObject obj = new JSONObject();
        Response res = null;
        String reply = "";
        String salutation, person_name, father_name, age, address_line1, address_line2, mobile_no1, email_id1, no_of_person, city_location, is_residencial, occupation_type, occupation_name, key_person_id;
        String latitude = "";
        String longitude = "";
        salutation = jsonObj.get("salutation").toString();
        person_name = jsonObj.get("person_name").toString();
        father_name = jsonObj.get("father_name").toString();
        age = jsonObj.get("age").toString();
        address_line1 = jsonObj.get("address_line1").toString();
        address_line2 = jsonObj.get("address_line2").toString();
        mobile_no1 = jsonObj.get("mobile_no").toString();
        email_id1 = jsonObj.get("email").toString();
        no_of_person = jsonObj.get("no_of_person").toString();
        city_location = jsonObj.get("location").toString();
        is_residencial = jsonObj.get("is_residencial").toString();
        occupation_type = jsonObj.get("occupationTypeId").toString();
        occupation_name = jsonObj.get("occupation_name").toString();
        key_person_id = jsonObj.get("key_person_id").toString();
        if (Integer.parseInt(key_person_id) == 0) {
            latitude = jsonObj.get("latitude").toString();
            longitude = jsonObj.get("longitude").toString();
        }
        System.out.println("insertRecord");
        ShiftLoginModel slm = new ShiftLoginModel();
        try {
            slm.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in insertRecord() in ShiftWebservices : " + ex);
        }
        int result = slm.insertKeyPerson(salutation, person_name, father_name, age, address_line1, address_line2, mobile_no1, email_id1, latitude, longitude, key_person_id, occupation_type, occupation_name, no_of_person, city_location, is_residencial);
        if (result > 0) {
            System.out.println("Data Retrived : " + jsonObj + " : Saved...");
            reply = "Successfully";
        } else {
            System.out.println("Data Retrived : " + jsonObj + " : Not Saved Some Error...");
            reply = " Not Successfully";
        }
        slm.closeConnection();
        return reply;
    }

    @POST
    @Path("/insertCordinate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String InsertCordinate(JSONObject jsonObj) throws Exception {
        JSONObject obj = new JSONObject();
        Response res = null;
        String reply = "";
        int result = 0;
        String latitude, longitude, imei_no, emp_code, mobile_no;
        imei_no = jsonObj.get("deviceid") == null?"":jsonObj.get("deviceid").toString();
        emp_code = jsonObj.get("empcode").toString();
        latitude = jsonObj.get("latitude").toString();
        longitude = jsonObj.get("longitude").toString();
        if (jsonObj.get("phoneno") == null) {
            mobile_no = "";
        } else {
            mobile_no = jsonObj.get("phoneno").toString();
        }
        System.out.println("insertRecord");
        ShiftLoginModel slm = new ShiftLoginModel();
        try {
            slm.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in insertRecord() in ShiftWebservices : " + ex);
        }
        slm.insertCordinate(latitude, longitude, emp_code, imei_no, mobile_no);
        System.out.println("record insert in cordinate table");
        String id_attendence = slm.getskpmId(emp_code);
        String skpm_id = id_attendence.split("_")[0];
        String attendence = id_attendence.split("_")[1];

        if (attendence.equals("N")) {
            JSONObject Obj = slm.getAreaDetails(emp_code);
            String zone = Obj.getString("zone_name");
            String ward = Obj.getString("ward_name");
            String area = Obj.getString("area_name");
            List destination = slm.getLocationCordinates(zone, ward, area);
            Iterator itr = destination.iterator();
            while (itr.hasNext()) {
                String data = (String) itr.next();
                int distance = MapDetailClass.getDistance(latitude + "," + longitude, data);
                if (distance < 30) {
                    result = slm.updateAttendence(skpm_id);
                    break;
                }
            }
        }
        if (result > 0) {
            System.out.println("Data Retrived : " + jsonObj + " : Saved...");
            res = Response.ok(jsonObj, MediaType.APPLICATION_JSON).build();
        } else {
        }
        slm.closeConnection();

        return reply;
    }

    @POST
    @Path("/verify_mobileno")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String VerifyMoileNo(String mobile_no) throws Exception {
        ShiftLoginModel slm = new ShiftLoginModel();
        try {
            slm.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in Login() in AppointmentWebservices : " + ex);
        }
        int affected = slm.getKeyPersonId(mobile_no);
        if (affected > 0) {
            sendOTP(mobile_no);
        }
        slm.closeConnection();
        if (affected > 0) {
            return "success";
        } else {
            return "error";
        }
    }

    @POST
    @Path("/sendOTP")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void sendOTP(String mobile_no) throws Exception {
        String otp = "";
        System.out.println("UserAppWebServices...");
        ShiftTimeModel stm = new ShiftTimeModel();
        try {
            stm.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in Login() in AppointmentWebservices : " + ex);
        }
        otp = stm.random(6);
        otpMap.put(mobile_no, otp);
        stm.sendSmsToAssignedFor(mobile_no, "OTP:- " + otp);
        System.out.println("Data Retrived : " + mobile_no);
        stm.closeConnection();
    }

    @POST
    @Path("/reSendOTP")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String reSendOTP(String mobile_no) throws Exception {
        String otp = "";
        System.out.println("PocServiceConnectionServices...");
        ShiftTimeModel stm = new ShiftTimeModel();
        try {
            stm.setConnection((java.sql.Connection) DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in vehicleDetail() in RideWebservices : " + ex);
        }
        otp = otpMap.get(mobile_no);
        if (otp != null) {
            stm.sendSmsToAssignedFor(mobile_no, "OTP:- " + otp);
        } else {
            otp = "fail";
        }
        System.out.println("Data Retrived : " + mobile_no);
        stm.closeConnection();
        if (!otp.equals("fail")) {
            return "success";
        } else {
            return "error";
        }
    }

    @POST
    @Path("/verifyOTP")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONObject verifyOTP(String mobile_no_otp) throws Exception {
        String otp = "";
        JSONObject jsonObj = new JSONObject();
        JSONArray arrayObj = new JSONArray();
        System.out.println("PocServiceConnectionServices...");
        String mobile_no = mobile_no_otp.split("_")[0];
        otp = mobile_no_otp.split("_")[1];
        ShiftLoginModel slm = new ShiftLoginModel();
        try {
            slm.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in vehicleDetail() in RideWebservices : " + ex);
        }
        if (otp.equals(otpMap.get(mobile_no))) {
            otpMap.remove(mobile_no);
            int emp_code = slm.getKeyPersonId(mobile_no);
            jsonObj = slm.getAreaDetails("" + emp_code);
            arrayObj.add(jsonObj);
            jsonObj.put("Data", arrayObj);
            slm.closeConnection();
        }
        return jsonObj;
    }

    @POST
    @Path("/verifyOTPWithDetail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONObject verifyOTPWithDetail(String mobile_no_otp) throws Exception {
        String otp = "";
        JSONObject jsonObj = new JSONObject();
        JSONArray arrayObj = new JSONArray();
        System.out.println("HealthDepartmentWebServices...");
        String mobile_no = mobile_no_otp.split("_")[0];
        otp = mobile_no_otp.split("_")[1];
        ShiftLoginModel slm = new ShiftLoginModel();
        try {
            slm.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in vehicleDetail() in RideWebservices : " + ex);
        }
        if (otp.equals(otpMap.get(mobile_no))) {
            otpMap.remove(mobile_no);
            jsonObj.put("Data", slm.getKeyPersonDetail(mobile_no));
            jsonObj.put("zone", slm.getZoneNames());
            jsonObj.put("ward", slm.getWardNames());
            jsonObj.put("area", slm.getAreaNames());
            jsonObj.put("city_location", slm.getCityLocationNames());
            jsonObj.put("org_office", slm.getOrgOfficeNames());
            jsonObj.put("designations", slm.getDesignation());
            slm.closeConnection();
        }
        return jsonObj;
    }

    @POST
    @Path("/getPersonDetail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONObject getPersonDetail(JSONObject json) throws Exception {
        String otp = "";
        System.out.println("PocServiceConnectionServices...");
        ShiftLoginModel slm = new ShiftLoginModel();
        try {
            slm.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in vehicleDetail() in RideWebservices : " + ex);
        }
        json.put("personDetail", slm.getPersonDetail(json));
        slm.closeConnection();
        return json;
    }

    @POST
    @Path("/currentShiftDetail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONObject currentShiftDetail(int city_location_id) throws Exception {

        JSONObject jsonObj = new JSONObject();
        JSONArray arrayObj = new JSONArray();

        ShiftLoginModel slm = new ShiftLoginModel();
        String temp = "";
        try {
            slm.setConnection(DBConnection.getConnectionForUtf(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in vehicleDetail() in RideWebservices : " + ex);
        }
        arrayObj = slm.getShiftDetail(city_location_id);
        jsonObj.put("Data", arrayObj);
        arrayObj = slm.getShiftType(city_location_id);
        jsonObj.put("shift_type", arrayObj);
        int size = arrayObj.size();
        if (size > 0) {
            jsonObj.put("result", "success");
        } else {
            jsonObj.put("result", "error");
        }
        slm.closeConnection();
        return jsonObj;
    }
}
