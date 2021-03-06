/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeRed.webServices.model;

import com.lowagie.text.Row;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author jpss
 */
public class UserAppWebServiceModel {

    private Connection connection;
    private String message;
    private String msgBgColor;
    private final String COLOR_OK = "yellow";
    private final String COLOR_ERROR = "red";
    private String location_code = "";

    // Notification part code -- starts here
    public int validateTokenId(String key_person_id, String token_id) {
        int count = -1;
        String query = "SELECT count(key_person_id) as count FROM token_data WHERE key_person_id=" + key_person_id + " AND token_id='" + token_id + "'";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (Exception ex) {
            System.out.println("ERROR: in validateTokenId in GeneralModel : " + ex);
        }
        return count;
    }

    public int insertTokenId(String key_person_id, String token_id) {
        int rowAffected = 0;
        String query = "INSERT INTO token_data (key_person_id, token_id) VALUES('" + key_person_id + "', '" + token_id + "')";
        try {
            rowAffected = connection.prepareStatement(query).executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR: in insertTokenId in GeneralModel : " + ex);
        }
        return rowAffected;
    }

    public int updateTokenId(String key_person_id, String token_id) {
        int rowAffected = 0;
        String query = "UPDATE token_data set token_id='" + token_id + "' WHERE key_person_id=" + key_person_id; // WHERE key_person_id='" + key_person_id + "'"
        try {
            rowAffected = connection.prepareStatement(query).executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR: in updateTokenId in GeneralModel : " + ex);
        }
        return rowAffected;
    }

    // Notification part code -- ends here
    public int registerUser(JSONObject json) {
        int rowAffected = 0;
        String query = "INSERT INTO user (name, mobile_no, email_id, password, gender, blood_group) VALUES(?, ?, ?, ?, ?, ?)";
        String selectQuery = "SELECT user_id, name, mobile_no, email_id, gender, mobile_no_verified "
                + "FROM user WHERE mobile_no='" + json.get("mobile_no").toString() + "'";
        String contactQuery = "INSERT INTO user_emergency_contact (user_id, name, mobile_no) VALUES(?, ?, ?)";
        try {
            ResultSet rs = connection.prepareStatement(selectQuery).executeQuery();
            String is_verified = "";
            if (rs.next()) {
                rowAffected = -1;
            } else {
                PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, json.get("name").toString());
                pst.setString(2, json.get("mobile_no").toString());
                pst.setString(3, json.get("email_id").toString());
                pst.setString(4, json.get("password").toString());
                pst.setString(5, json.get("gender") == null ? "" : json.get("gender").toString());
                pst.setString(6, json.get("blood_group") == null ? "" : json.get("blood_group").toString());
                rowAffected = pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                int user_id = 0;
                if (rs.next()) {
                    user_id = rs.getInt(1);
                }
                org.json.JSONArray array = new org.json.JSONArray(json.get("emergency_contact").toString());
                int arrayLength = array.length();
                for (int i = 0; i < arrayLength; i++) {
                    pst = connection.prepareStatement(contactQuery);
                    org.json.JSONObject jsonObj = array.getJSONObject(i);
                    pst.setInt(1, user_id);
                    pst.setString(2, jsonObj.get("name").toString());
                    pst.setString(3, jsonObj.get("contact").toString());
                    pst.executeUpdate();
                }
            }
        } catch (Exception ex) {
            System.out.println("ERROR: in registerUser in UserAppWebServiceModel : " + ex);
        }
        return rowAffected;
    }

    public int editUserProfile(JSONObject json) {
        int rowAffected = 0;
        int user_id = Integer.parseInt(json.get("user_id").toString());
        String selectQuery = "SELECT user_id, name, mobile_no, email_id, gender, mobile_no_verified "
                + "FROM user WHERE mobile_no='" + json.get("mobile_no").toString() + "'";
        String contactQuery = "INSERT INTO user_emergency_contact (user_id, name, mobile_no) VALUES(?, ?, ?)";
        String query = "UPDATE user SET mobile_no='" + json.get("mobile_no").toString() + "', password='" + json.get("password").toString() + "' WHERE user_id=" + user_id;
        String queryEmergencyContact = "UPDATE user_emergency_contact SET active='N' WHERE user_id=" + user_id;
        try {
            ResultSet rs = connection.prepareStatement(selectQuery).executeQuery();
            String is_verified = "";
            if (rs.next()) {
                int id = rs.getInt("user_id");
                if (id == user_id) {
                    rowAffected = 1;
                } else {
                    rowAffected = -1;
                }
            } else {
                rowAffected = 1;
            }
            if (rowAffected > 0) {
                connection.setAutoCommit(false);
                rowAffected = connection.prepareStatement(query).executeUpdate();
                if (rowAffected > 0) {
                    rowAffected = connection.prepareStatement(queryEmergencyContact).executeUpdate();
                    org.json.JSONArray array = new org.json.JSONArray(json.get("emergency_contact").toString());
                    int arrayLength = array.length();
                    for (int i = 0; i < arrayLength; i++) {
                        PreparedStatement pst = connection.prepareStatement(contactQuery);
                        org.json.JSONObject jsonObj = array.getJSONObject(i);
                        pst.setInt(1, user_id);
                        pst.setString(2, jsonObj.get("name").toString());
                        pst.setString(3, jsonObj.get("contact").toString());
                        rowAffected = pst.executeUpdate();
                    }
                }
                if (rowAffected > 0) {
                    connection.commit();
                } else {
                    connection.rollback();
                }
            }
        } catch (Exception ex) {
            System.out.println("ERROR: in editUserProfile in UserAppWebServiceModel : " + ex);
            try {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                }
            } catch (Exception e) {
                System.out.println("ERROR: in commiting connection in insertRecord() in PrepaidDocumentWebServiceModel : " + e);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (Exception ex) {
                System.out.println("ERROR: in commiting connection in insertRecord() in PrepaidDocumentWebServiceModel : " + ex);
            }
        }
        return rowAffected;
    }

    public int editDriverProfile(JSONObject json) {
        int rowAffected = 0;
        int driver_id = Integer.parseInt(json.get("driver_id").toString());
        String contactQuery = "INSERT INTO key_person_emergency_contact (key_person_id, name, mobile_no) VALUES(?, ?, ?)";
        String queryEmergencyContact = "UPDATE key_person_emergency_contact SET active='N' WHERE key_person_id=" + driver_id;
        try {
            rowAffected = connection.prepareStatement(queryEmergencyContact).executeUpdate();
            org.json.JSONArray array = new org.json.JSONArray(json.get("emergency_contact").toString());
            int arrayLength = array.length();
            for (int i = 0; i < arrayLength; i++) {
                PreparedStatement pst = connection.prepareStatement(contactQuery);
                org.json.JSONObject jsonObj = array.getJSONObject(i);
                pst.setInt(1, driver_id);
                pst.setString(2, jsonObj.get("name").toString());
                pst.setString(3, jsonObj.get("contact").toString());
                rowAffected = pst.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println("ERROR: in editUserProfile in UserAppWebServiceModel : " + ex);
        }
        return rowAffected;
    }

    public int editUserEmergencyContact() {
        int rowAffected = 0;
        String query = "UPDATE user SET mobile_no=?, password=? WHERE user_id=?";
        try {
            rowAffected = connection.prepareStatement(query).executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR: in editUserProfile in UserAppWebServiceModel : " + ex);
        }
        return rowAffected;
    }

    public int mobileNoVerified(String mobile_no) {
        int rowAffected = 0;
        String query = "UPDATE user SET mobile_no_verified='Y' WHERE mobile_no='" + mobile_no + "'";
        try {
            rowAffected = connection.prepareStatement(query).executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR: in mobileNoVerified in UserAppWebServiceModel : " + ex);
        }
        return rowAffected;
    }

    public int loginUser(JSONObject json) {
        int rowAffected = 0;
        String query = "INSERT INTO login_detail (user_id, login_time)  "
                + " SELECT user_id, concat(current_date,' ',current_time) "
                + " FROM user WHERE mobile_no = ? AND password=? ";

        String userQuery = "SELECT user_id, name, mobile_no, email_id, gender, mobile_no_verified, blood_group "
                + "FROM user WHERE mobile_no='" + json.get("mobile_no").toString() + "'";
        String contactQuery = "SELECT user_id, name, mobile_no FROM user_emergency_contact WHERE active='Y' AND user_id=";

        try {
            ResultSet rs = connection.prepareStatement(userQuery,Statement.RETURN_GENERATED_KEYS).executeQuery();
            String is_verified = "";
            String user_id = "";
            if (rs.next()) {
                user_id = rs.getString("user_id");
                json.put("user_id", user_id);
                json.put("name", rs.getString("name"));
                json.put("email_id", rs.getString("email_id"));
                is_verified = rs.getString("mobile_no_verified");
                json.put("mobile_no_verified", is_verified);
                json.put("blood_group", rs.getString("blood_group"));
            } else {
                json.put("result", "fail");
            }
            if (is_verified.equals("Y")) {
                PreparedStatement pst = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, json.get("mobile_no").toString());
                pst.setString(2, json.get("password").toString());
                rowAffected = pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    json.put("login_id", rs.getInt(1));
                    rs = connection.prepareStatement(contactQuery + user_id).executeQuery();
                    JSONArray jsonArray = new JSONArray();
                    while (rs.next()) {
                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put("name", rs.getString("name"));
                        jsonObj.put("mobile_no", rs.getString("mobile_no"));
                        jsonArray.add(jsonObj);
                    }
                    json.put("emergency_contact", jsonArray);
                }
                if (rowAffected == 0) {
                    json.put("result", "fail");
                }
            }
        } catch (Exception ex) {
            System.out.println("ERROR: in loginUser in UserAppWebServiceModel : " + ex);
        }
        return rowAffected;
    }

    public int loginUser(JSONObject json, String mobile_no) {
        int rowAffected = 0;
        String query = "INSERT INTO login_detail (user_id, login_time)  "
                + " SELECT user_id, concat(current_date,' ',current_time) "
                + " FROM user WHERE mobile_no ='" + mobile_no + "'";
        String selectQuery = "SELECT user_id FROM login_detail WHERE login_detail_id=?";
        String contactQuery = "SELECT user_id, name, mobile_no FROM user_emergency_contact WHERE active='Y' AND user_id=";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            rowAffected = pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                int login_id = rs.getInt(1);
                json.put("login_id", login_id);
                pst = connection.prepareStatement(selectQuery);
                pst.setInt(1, login_id);
                rs = pst.executeQuery();
                if (rs.next()) {
                    int user_id = rs.getInt("user_id");
                    json.put("user_id", user_id);
                    rs = connection.prepareStatement(contactQuery + user_id).executeQuery();
                    JSONArray jsonArray = new JSONArray();
                    while (rs.next()) {
                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put("name", rs.getString("name"));
                        jsonObj.put("mobile_no", rs.getString("mobile_no"));
                        jsonArray.add(jsonObj);
                    }
                    json.put("emergency_contact", jsonArray);
                }
            }
        } catch (Exception ex) {
            System.out.println("ERROR: in loginUser after verifyOTP in UserAppWebServiceModel : " + ex);
        }
        return rowAffected;
    }

    public int logoutUser(String login_id) {
        int rowAffected = 0;
        String query = "UPDATE login_detail SET logout_time=concat(current_date,' ',current_time)"
                + " WHERE login_detail_id = " + login_id;
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            rowAffected = pst.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR: in logoutUser in UserAppWebServiceModel : " + ex);
        }
        return rowAffected;
    }

    public int verifyMobileNo(String mobile_no) {
        int user_id = 0;
        String query = " SELECT user_id FROM user WHERE mobile_no = '" + mobile_no + "' ";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            if (rs.next()) {
                user_id = rs.getInt("user_id");
            }
        } catch (Exception ex) {
            System.out.println("ERROR: in verifyMobileNo in UserAppWebServiceModel : " + ex);
        }
        return user_id;
    }

    public int resetPassword(JSONObject json) {
        int affected = 0;
        String query = " UPDATE user SET password='" + json.get("password") + "' WHERE mobile_no = '" + json.get("mobile_no") + "' ";
        try {
            affected = connection.prepareStatement(query).executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR: in resetPassword() in UserAppWebServiceModel : " + ex);
        }
        return affected;
    }

    public String sendSmsToAssignedFor(String numberStr1, String messageStr1) {
        String result = "";
        try {
            String host_url = "http://login.smsgatewayhub.com/api/mt/SendSMS?";//"http://api.mVaayoo.com/mvaayooapi/MessageCompose?";
//           String tempMessage = messageStr1;
//           String sender_id = java.net.URLEncoder.encode("TEST SMS", "UTF-8");         // e.g. "TEST+SMS"
            System.out.println("messageStr1 is = " + messageStr1);
            messageStr1 = java.net.URLEncoder.encode(messageStr1, "UTF-8");
            String queryString = "APIKey=WIOg7OdIzkmYTrqTsw262w&senderid=JPSOFT&channel=2&DCS=8&flashsms=0&number=" + numberStr1 + "&text=" + messageStr1 + "&route=";
            String url = host_url + queryString;
            result = callURL(url);
            System.out.println("SMS URL: " + url);
        } catch (Exception e) {
            result = e.toString();
            System.out.println("SMSModel sendSMS() Error: " + e);
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

    public static String random(int size) {
        StringBuilder generatedToken = new StringBuilder();
        try {
            SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
            // Generate 20 integers 0..20
            for (int i = 0; i
                    < size; i++) {
                generatedToken.append(number.nextInt(9));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedToken.toString();
    }

    public JSONArray getSourceList() {
        JSONArray jsonArray = new JSONArray();
        int count = 0;
        String query = "SELECT location, location_code, source_id, s.latitude, s.longitude, s.english_location FROM source_destination sd, city_location s "
                + " WHERE s.city_location_id = sd.source_id "
                + " GROUP BY location ORDER BY location";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("source_id", rs.getString("source_id"));
                jsonObj.put("source", rs.getString("location"));
                jsonObj.put("source_code", rs.getString("location_code"));
                jsonObj.put("latitude", rs.getString("latitude"));
                jsonObj.put("longitude", rs.getString("longitude"));
                jsonObj.put("english_location", rs.getString("english_location"));
                jsonArray.add(jsonObj);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: in getSourceList in UserAppWebServiceModel : " + ex);
        }
        return jsonArray;
    }

    public JSONArray getDestinationList(String source_id) {
        JSONArray jsonArray = new JSONArray();
        String query = "SELECT sd.destination_id, d.location, d.location_code, source_id, distance, amount, d.latitude, d.longitude, d.english_location "
                + " FROM source_destination sd, city_location s, city_location d "
                + " WHERE d.city_location_id = sd.destination_id AND s.city_location_id = sd.source_id "
                + " AND sd.source_id=" + source_id
                + " GROUP BY location ORDER BY location";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("destination_id", rs.getString("destination_id"));
                jsonObj.put("destination", rs.getString("location"));
                jsonObj.put("destination_code", rs.getString("location_code"));
                jsonObj.put("distance", rs.getString("distance"));
                jsonObj.put("amount", rs.getString("amount"));
                jsonObj.put("latitude", rs.getString("latitude"));
                jsonObj.put("longitude", rs.getString("longitude"));
                jsonObj.put("english_location", rs.getString("english_location"));
                jsonArray.add(jsonObj);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: in getDestinationList in UserAppWebServiceModel : " + ex);
        }
        return jsonArray;
    }

    public JSONArray getTariffDetail() {
        JSONArray jsonArray = new JSONArray();
        String query = "SELECT tarrif_id, tarrif_code, minimum_charge, night_charge_percent, baggage_charge, rate, service_charge "
                + " FROM tarrif WHERE active = 'Y'";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
                JSONObject json = new JSONObject();
                json.put("tarrif_id", rs.getString("tarrif_id"));
                json.put("tarrif_code", rs.getString("tarrif_code"));
                json.put("minimum_charge", rs.getString("minimum_charge"));
                json.put("night_charge_percent", rs.getString("night_charge_percent"));
                json.put("baggage_charge", rs.getString("baggage_charge"));
                json.put("rate", rs.getString("rate"));
                json.put("service_charge", rs.getString("service_charge"));
                jsonArray.add(json);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: in getTariffDetail in UserAppWebServiceModel : " + ex);
        }
        return jsonArray;
    }

    public String getServerKey(String type) {
        String key = "";
        String query = "SELECT server_key FROM gcm_server_data WHERE type='" + type + "' ";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            if (rs.next()) {
                key = rs.getString("server_key");
            }
        } catch (Exception ex) {
        }
        return key;
    }

    public JSONArray getDriverTokenId(String vehicle_id) {
        JSONArray jsonArray = new JSONArray();
        String query = "SELECT token_id FROM driver_token_data d WHERE IF('" + vehicle_id + "'='', d.vehicle_id LIKE '%%', d.vehicle_id='" + vehicle_id + "')";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
                jsonArray.add(rs.getString("token_id"));
            }
        } catch (Exception ex) {
        }
        return jsonArray;
    }

    public String getSourceName(String source_id) {
        String location = "";
        String query = "SELECT location FROM city_location WHERE city_location_id= '" + source_id + "'";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            if (rs.next()) {
                location = rs.getString("location");
            }
        } catch (Exception ex) {
            System.out.println("ERROR: in getSourceName() in UserAppWebServiceModel : " + ex);
        }
        return location;
    }

    public String getUserData(String user_id, JSONObject json) {
        String msg = "";
        int rowAffected = 0;
        String userQuery = "SELECT user_id, name, mobile_no, email_id, gender, mobile_no_verified "
                + "FROM user WHERE user_id='" + user_id + "'";
        try {
            ResultSet rs = connection.prepareStatement(userQuery).executeQuery();
            if (rs.next()) {
                String mobile_no = rs.getString("mobile_no");
                json.put("passenger_mobile_no", mobile_no);
                msg = "Please contact Passenger " + rs.getString("name") + " Mobile No " + mobile_no;
            }
        } catch (Exception ex) {
            System.out.println("ERROR: in getUserData in UserAppWebServiceModel : " + ex);
        }
        return msg;
    }

    public String getVehicleDetail(String vehicleId, JSONObject json) {
        String msg = "";
//        String query = "SELECT vehicle_code FROM vehicle v WHERE v.active = 'Y' GROUP BY vehicle_no ORDER BY vehicle_no"
//                + " AND IF('"+vehicleNo+"'='', vehicle_no LIKE '%%', vehicle_no='"+vehicleNo+"') ";
//        String query = "SELECT v.vehicle_no, v.vehicle_code FROM vehicle_order odr, vehicle v "
//                + " WHERE v.active = 'Y' AND v.vehicle_id = odr.vehicle_id AND available = 'Y' "
//                + " AND IF('"+vehicleNo+"'='', vehicle_no LIKE '%%', vehicle_no='"+vehicleNo+"') "
//                + " HAVING MIN(odr.order_no) > 0 ";
        String query = "SELECT v.vehicle_no, v.vehicle_code, t.minimum_charge, t.night_charge_percent, "
                + " t.baggage_charge, t.rate, t.service_charge, kp.key_person_name, kp.mobile_no1, vdm.vehicle_driver_map_id "
                + " FROM vehicle v, vehicle_model_tarrif_map vmtm, tarrif t, key_person kp, vehicle_driver_map vdm "//vehicle_order odr,
                + " WHERE v.active = 'Y' "//AND v.vehicle_id = odr.vehicle_id AND available = 'Y' "
                + " AND vmtm.vehicle_model_tarrif_map_id = v.vehicle_model_tarrif_map_id "
                + " AND t.tarrif_id = vmtm.tarrif_id AND t.active = 'Y' AND vdm.active='Y' AND vdm.vehicle_id = v.vehicle_id AND kp.key_person_id = vdm.key_person_id "
                + " AND IF('" + vehicleId + "'='', v.vehicle_id LIKE '%%', v.vehicle_id='" + vehicleId + "') "
                + " LiMit 0, 1";//HAVING MIN(odr.order_no) > 0 "; ORDER BY odr.order_no
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
                String vehicle_code = rs.getString("vehicle_code");
                String mobile_no = rs.getString("mobile_no1");
                String driver_name = rs.getString("key_person_name");
                json.put("driver_mobile_no", mobile_no);
                json.put("driver_name", driver_name);
                json.put("vehicle_driver_map_id", rs.getString("vehicle_driver_map_id"));
                json.put("vehicle_no", rs.getString("vehicle_no"));
                json.put("vehicle_code", rs.getString("vehicle_code"));
                json.put("rate", rs.getDouble("rate"));
                json.put("night_charge_percent", rs.getInt("night_charge_percent"));
                json.put("service_charge", rs.getDouble("service_charge"));
                if (!vehicleId.isEmpty()) {
//                       msg = vehicle_code + "_" + rs.getDouble("minimum_charge") + "_"
//                                + rs.getDouble("night_charge_percent") + "_" + rs.getDouble("baggage_charge") + "_"
//                                + rs.getDouble("rate") + "_" + rs.getDouble("service_charge") + "&#$"
//                                + rs.getString("key_person_name") + "&#$" + rs.getString("mobile_no1");
                    msg = "Please Contact " + driver_name + " Mobile no " + mobile_no
                            + " Vehicle No " + rs.getString("vehicle_no") + " and Vehicle Code " + vehicle_code;
                }
            }
//            if(count == 0)
//                list.add("vkWVks miyC/k ugha gSA");
        } catch (Exception ex) {
            System.out.println("ERROR: in getVehicleDetail in UserAppWebServiceModel : " + ex);
        }
        return msg;
    }

    public String getBookingMsg(String booking_id, JSONObject json) {
        String msg = "";
        String userQuery = "SELECT u.user_id, name, mobile_no, email_id, gender, mobile_no_verified, "
                + " shared_mobile_no, r.is_app_to_app, r.source_id, r.destination_id, destination_name, "
                + " sd.distance, sd.amount, sd.source_destination_id, r.ride_date, r.ride_time, "
                + " r.destination_latitude, r.destination_longitude, is_coolie, pnr_no, d.latitude, d.longitude, r.status_id "
                + " FROM user u, ride_from_app r LEFT JOIN (source_destination sd, city_location d) "
                + " ON sd.source_id = r.source_id AND sd.destination_id = r.destination_id AND d.city_location_id = sd.destination_id "
                + " WHERE u.user_id=r.user_id AND r.active='Y' AND ride_from_app_id='" + booking_id + "'";
        try {
            ResultSet rs = connection.prepareStatement(userQuery).executeQuery();
            if (rs.next()) {
                String mobile_no = rs.getString("mobile_no");
                String name = rs.getString("name");
                json.put("passenger_mobile_no", mobile_no);
                json.put("passenger_name", name);
                json.put("shared_mobile_no", rs.getString("shared_mobile_no"));
                json.put("is_app_to_app", rs.getString("is_app_to_app"));
                json.put("gender", rs.getString("gender"));
                json.put("source_id", rs.getInt("source_id"));
                json.put("destination_id", rs.getInt("destination_id"));
                json.put("destination_name", rs.getString("destination_name"));
                json.put("source_destination_id", rs.getInt("source_destination_id"));
                json.put("distance", rs.getFloat("distance"));
                json.put("amount", rs.getDouble("amount"));
                json.put("ride_date", rs.getString("ride_date"));
                json.put("ride_time", rs.getString("ride_time"));
                json.put("destination_latitude", rs.getDouble("destination_latitude"));
                json.put("destination_longitude", rs.getDouble("destination_longitude"));
                json.put("is_coolie", rs.getString("is_coolie"));
                json.put("pnr_no", rs.getString("pnr_no"));
                json.put("latitude", rs.getDouble("latitude"));
                json.put("longitude", rs.getDouble("longitude"));
                json.put("status_id", rs.getString("status_id"));
                msg = "Please contact Passenger " + name + " Mobile No " + mobile_no;
            }
        } catch (Exception ex) {
            System.out.println("ERROR: in getUserData in UserAppWebServiceModel : " + ex);
        }
        return msg;
    }

    public void getRideStatus(JSONObject json) {
        String Status = "";
        String query = "SELECT vehicle_id FROM vehicle_rideapp_map WHERE booking_no=" + json.get("booking_no").toString();
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            if (rs.next()) {
                String vehicle_id = rs.getString("vehicle_id");
                getVehicleDetail(
                        vehicle_id, json);
                json.put("vehicle_id", vehicle_id);
                json.put("status", "Process");
            } else {
                json.put("status", "Pending");
            }
        } catch (Exception ex) {
            System.out.println("ERROR: in getRideStatus in UserAppWebServiceModel : " + ex);
        }
    }

    public int updateRideOnOff(String rideStatus, String ride_id, String date, String latitude, String longitude) {
        int rowAffected = 0;
        String query = " UPDATE ride_from_app SET start_latitude=?, start_longitude=?, start_date_time=?, status_id=2 WHERE active='Y' AND ride_from_app_id=? ";
        if (rideStatus.equals("stop")) {
            query = " UPDATE ride_from_app SET stop_latitude=?, stop_longitude=?, stop_date_time=?, status_id=3 WHERE active='Y' AND ride_from_app_id=? ";
        } else if (rideStatus.equals("cancel")) {
            query = "UPDATE ride_from_app SET cancel_date_time=?, status_id=4 WHERE active='Y' AND ride_from_app_id=? ";
        }
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            //pstmt.setString(1, date);
            if (rideStatus.equals("start") || rideStatus.equals("stop")) {
                pstmt.setString(1, latitude);
                pstmt.setString(2, longitude);
                pstmt.setString(3, date);
                pstmt.setString(4, ride_id);
            } else if (rideStatus.equals("cancel")) {
                pstmt.setString(1, date);
                pstmt.setString(2, ride_id);
            }
            rowAffected = pstmt.executeUpdate();
//            if(ride_id != null && !ride_id.isEmpty())
//                connection.prepareStatement(updateRideApp).executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR : in updateRideOnOff in UserAppWebServiceModel : " + ex);
        }
        return rowAffected;
    }

    public int getStatusId(String status) {
        int id = 0;
        String query = "SELECT status_id FROM status WHERE status='" + status + "'";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            if (rs.next()) {
                id = rs.getInt("status_id");
            }
        } catch (Exception ex) {
            System.out.println("ERROR : in getStatusId in UserAppWebServiceModel : " + ex);
        }
        return id;
    }

    public JSONArray getLocationName() {
        JSONArray jsonArray = new JSONArray();
        String query = "select city_location_id, location, location_code from city_location "
                + " GROUP BY location ORDER BY location";
        try {
            ResultSet rset = connection.prepareStatement(query).executeQuery();
            while (rset.next()) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("location_id", rset.getInt("city_location_id"));
                jsonObj.put("location_code", rset.getString("location_code"));
                jsonObj.put("location", rset.getString("location"));
                jsonArray.add(jsonObj);
            }
        } catch (Exception e) {
            System.out.println("getZoneName ERROR inside CityLocationModel - " + e);
        }
        return jsonArray;
    }

    public JSONArray getLiveBus() {
        JSONArray jsonArray = new JSONArray();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -5);
        int Hr = cal.get(Calendar.HOUR_OF_DAY);
        int Min = cal.get(Calendar.MINUTE);
        int Date = cal.get(Calendar.DATE);
        int Month = cal.get(Calendar.MONTH) + 1;
        int Year = cal.get(Calendar.YEAR);
        String date = Year + "-" + Month + "-" + Date + " " + Hr + ":" + Min + ":00";
        //PreparedStatement pstmt = null;

//        String query = " SELECT survey_cordinates_id, latitude, longitude"
//                 + " FROM survey_cordinates GROUP BY imei_no ORDER BY survey_cordinates_id DESC";
//        String query = "SELECT * FROM (SELECT auto_cordinates_id, latitude, longitude, imei_no, vehicle_id "
//                + " FROM auto_cordinates WHERE created_date >= '"+ date +"' "
//                + " ORDER BY auto_cordinates_id DESC) AS sc GROUP BY vehicle_id";//imei_no";
        String query = "SELECT * FROM (SELECT latitude, longitude, vehicle_code, vehicle_no, v.vehicle_id FROM auto_cordinates a "
                + " INNER JOIN (vehicle v, vehicle_model_tarrif_map vmt, vehicle_model_map vmp, vehicle_type vt)"
                + " ON v.vehicle_id=a.vehicle_id AND v.active='Y' AND vmt.vehicle_model_tarrif_map_id=v.vehicle_model_tarrif_map_id"
                + " AND vmp.vehicle_model_map_id=vmt.vehicle_model_map_id AND vt.vehicle_type_id=vmp.vehicle_type_id AND vehicle_type='Bus'"
                + " WHERE a.created_date >= '" + date + "' "
                + " ORDER BY auto_cordinates_id DESC) AS sc GROUP BY vehicle_id";//imei_no";
        //+ " LIMIT " + lowerLimit + "," + noOfRowsToDisplay;
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("vehicle_id", rset.getInt("vehicle_id"));
                jsonObj.put("vehicle_no", rset.getString("vehicle_no"));
                jsonObj.put("vehicle_code", rset.getString("vehicle_code"));
                jsonObj.put("latitude", rset.getString("latitude"));
                jsonObj.put("longitude", rset.getString("longitude"));
                jsonArray.add(jsonObj);
            }
        } catch (Exception e) {
            System.out.println("Error in ShowData() :AutoCoordinatesModel" + e);
        }
        return jsonArray;
    }

    public JSONArray getLiveAutos(String vehicle_type) {
        JSONArray jsonArray = new JSONArray();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -50);
        int Hr = cal.get(Calendar.HOUR_OF_DAY);
        int Min = cal.get(Calendar.MINUTE);
        int Date = cal.get(Calendar.DATE);
        int Month = cal.get(Calendar.MONTH) + 1;
        int Year = cal.get(Calendar.YEAR);
        String v_type = "Auto";
        if (vehicle_type != null && vehicle_type.equalsIgnoreCase("cab")) {
            v_type = "Cab";
        } else if (vehicle_type == null && vehicle_type.isEmpty()) {
            v_type = "Auto";
        }
        String date = Year + "-" + Month + "-" + Date + " " + Hr + ":" + Min + ":00";
        //PreparedStatement pstmt = null;

//        String query = " SELECT survey_cordinates_id, latitude, longitude"
//                 + " FROM survey_cordinates GROUP BY imei_no ORDER BY survey_cordinates_id DESC";
//        String query = "SELECT * FROM (SELECT auto_cordinates_id, latitude, longitude, imei_no, vehicle_id "
//                + " FROM auto_cordinates WHERE created_date >= '"+ date +"' "
//                + " ORDER BY auto_cordinates_id DESC) AS sc GROUP BY vehicle_id";//imei_no";
        String query = "SELECT * FROM (SELECT latitude, longitude, vehicle_code, vehicle_no, v.vehicle_id, a.created_date FROM auto_cordinates a "
                + " INNER JOIN (vehicle v, vehicle_model_tarrif_map vmt, vehicle_model_map vmp, vehicle_type vt)"
                + " ON v.vehicle_id=a.vehicle_id AND v.active='Y' AND vmt.vehicle_model_tarrif_map_id=v.vehicle_model_tarrif_map_id"
                + " AND vmp.vehicle_model_map_id=vmt.vehicle_model_map_id AND vt.vehicle_type_id=vmp.vehicle_type_id AND vehicle_type='" + v_type + "'"
                + " WHERE a.created_date >= '" + date + "' "
                + " ORDER BY auto_cordinates_id DESC) AS sc GROUP BY vehicle_id ";//imei_no";
        //+ " LIMIT " + lowerLimit + "," + noOfRowsToDisplay;
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("vehicle_id", rset.getInt("vehicle_id"));
                jsonObj.put("vehicle_no", rset.getString("vehicle_no"));
                jsonObj.put("vehicle_code", rset.getString("vehicle_code"));
                jsonObj.put("latitude", rset.getString("latitude"));
                jsonObj.put("longitude", rset.getString("longitude"));
                jsonObj.put("date_time", rset.getString("created_date"));
                jsonArray.add(jsonObj);
            }
        } catch (Exception e) {
            System.out.println("Error in ShowData() :AutoCoordinatesModel" + e);
        }
        return jsonArray;
    }

    public JSONArray getBusDetail(JSONObject json) {
        JSONArray jsonArray = new JSONArray();
        String source_code = json.get("source_code").toString();
        String destination_code = json.get("destination_code").toString();
        String query = "SELECT v.vehicle_id, vehicle_code, vehicle_no, s.stopage_id, location, no_of_seats, time"
                + " FROM vehicle v, vehicle_stopage_map vsm LEFT JOIN bus_ride br ON br.vehicle_stopage_map_id=vsm.vehicle_stopage_map_id , "
                + " stopage s, city_location cl WHERE v.vehicle_id = vsm.vehicle_id"
                + " AND v.active = 'Y' AND s.stopage_id=vsm.stopage_id AND cl.city_location_id = s.city_location_id "
                + " AND (cl.location_code='" + source_code + "' OR cl.location_code = '" + destination_code + "') GROUP BY vehicle_code;";
        //+ " AND (s.city_location_id=19 OR s.city_location_id = 26) GROUP BY vehicle_code;";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("vehicle_id", rset.getInt("vehicle_id"));
                jsonObj.put("vehicle_no", rset.getString("vehicle_no"));
                jsonObj.put("vehicle_code", rset.getString("vehicle_code"));
                jsonObj.put("no_of_seats", rset.getString("no_of_seats"));
                jsonObj.put("time", rset.getString("time"));
                jsonArray.add(jsonObj);
            }
        } catch (Exception ex) {
            System.out.println("Error in getBusDetail() :UserAppWebServiceModel : " + ex);
        }
        return jsonArray;
    }

    public JSONArray getUserHistory(String user_id) {
        JSONArray jsonArray = new JSONArray();
//        String query = "SELECT ride_from_app_id, user_id, status, is_app_to_app, source_id, source_name, destination_id, "
//                + " destination_name, distance, amount, ride_date, ride_time FROM user_history u  WHERE user_id = " + user_id;
        String query = "SELECT uh.ride_id, ride_from_app_id, user_id, status, is_app_to_app, uh.source_id, uh.source_name, uh.destination_id, "
                + " uh.destination_name, distance, amount, ride_date, ride_time, r.vehicle_id AS ride_vehicle_id, "
                + " rv.vehicle_no AS ride_vehicle_no, v.vehicle_id AS app_vehicle_id, v.vehicle_no AS app_vehicle_no, "
                + " uh.vehicle_type, uh.app_distance, uh.app_amount "
                + " FROM user_history uh left join (ride r, vehicle_driver_map vdm, vehicle rv, key_person kp) on r.ride_id=uh.ride_id "
                + " AND vdm.vehicle_driver_map_id=r.vehicle_driver_map_id AND kp.key_person_id=vdm.key_person_id "
                + " AND vdm.active='Y' AND rv.vehicle_id=vdm.vehicle_id AND rv.active='Y' "
                + " left join vehicle v on v.vehicle_id=uh.vehicle_id AND v.active='Y' WHERE uh.user_id = " + user_id + " ORDER BY ride_from_app_id desc ";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
                JSONObject json = new JSONObject();
                json.put("ride_from_app_id", rs.getString("ride_from_app_id"));
                json.put("user_id", rs.getString("user_id"));
                json.put("status", rs.getString("status"));
                String is_app_to_app = rs.getString("is_app_to_app");
                json.put("is_app_to_app", is_app_to_app);
                json.put("source_id", rs.getString("source_id"));
                json.put("source_name", rs.getString("source_name"));
                json.put("destination_id", rs.getString("destination_id"));
                json.put("destination_name", rs.getString("destination_name"));
                if (is_app_to_app.equals("Y")) {
                    String amt = rs.getString("app_amount") == null ? "" : rs.getString("app_amount");
                    String dst = rs.getString("app_distance") == null ? "" : rs.getString("app_distance");
                    if (dst.equals("0.0") || dst.equals("0.00")) {
                        amt = "";
                        dst = "";
                    }
                    json.put("amount", amt);
                    json.put("distance", dst);
                } else {
                    json.put("amount", rs.getString("amount") == null ? "" : rs.getString("amount"));
                    json.put("distance", rs.getString("distance") == null ? "" : rs.getString("distance"));
                }
                json.put("ride_date", rs.getString("ride_date"));
                json.put("ride_time", rs.getString("ride_time"));
                json.put("ride_id", rs.getString("ride_id"));
                json.put("ride_vehicle_id", rs.getString("ride_vehicle_id"));
                json.put("ride_vehicle_no", rs.getString("ride_vehicle_no"));
                json.put("app_vehicle_id", rs.getString("app_vehicle_id"));
                json.put("app_vehicle_no", rs.getString("app_vehicle_no"));
                json.put("vehicle_type", rs.getString("vehicle_type"));
                jsonArray.add(json);
            }
        } catch (Exception ex) {
            System.out.println("Error in getBusDetail() :UserAppWebServiceModel : " + ex);
        }
        return jsonArray;
    }

    public int rideFeedback(JSONObject json) {
        int rowAffacted = 0;
        String query = "INSERT INTO ride_feedback (rating, feedback, ride_from_app_id) "
                + " VALUES(?, ?, ?) ";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, json.get("rating").toString());
            pst.setString(2, json.get("feedback").toString());
            pst.setString(3, json.get("booking_no").toString());
            rowAffacted = pst.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error in getBusDetail() :UserAppWebServiceModel : " + ex);
        }
        return rowAffacted;
    }

    public JSONArray getNodeData() {
        JSONArray jarray = new JSONArray();
        String query = "SELECT node_id, node_name, generation_no, active, revision_no FROM code_red.node WHERE active='Y'";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
                JSONObject json = new JSONObject();
                json.put("node_id", rs.getString("node_id"));
                json.put("node_name", rs.getString("node_name"));
                json.put("generation_no", rs.getString("generation_no"));
                json.put("active", rs.getString("active"));
                json.put("revision_no", rs.getString("revision_no"));
                jarray.add(json);
            }
        } catch (Exception ex) {
            System.out.println("Error in getNodeData() UserAppWebServiceModel : " + ex);
        }
        return jarray;
    }

    public JSONArray getTreeData() {
        JSONArray jarray = new JSONArray();
        String query = "SELECT tree_id, node_id, node_parent_id, node_parent_rev_no, tree_parent_id, revision, active, "
                + " index_no, isSuperChild, node_rev FROM code_red.tree WHERE active='Y'";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
                JSONObject json = new JSONObject();
                json.put("tree_id", rs.getString("tree_id"));
                json.put("node_id", rs.getString("node_id"));
                json.put("node_parent_id", rs.getString("node_parent_id"));
                json.put("node_parent_rev_no", rs.getString("node_parent_rev_no"));
                json.put("tree_parent_id", rs.getString("tree_parent_id"));
                json.put("active", rs.getString("active"));
                json.put("revision", rs.getString("revision"));
                json.put("index_no", rs.getString("index_no"));
                json.put("isSuperChild", rs.getString("isSuperChild"));
                json.put("node_rev", rs.getString("node_rev"));
                jarray.add(json);
            }
        } catch (Exception ex) {
            System.out.println("Error in getTreeData() UserAppWebServiceModel : " + ex);
        }
        return jarray;
    }

    public JSONArray getContactList() {
        JSONArray jsonArray = new JSONArray();
        String query = "SELECT of.org_office_id, of.org_office_name, of.email_id1, of.mobile_no1, "
                + " kp.key_person_id, kp.key_person_name, kp.mobile_no1, kp.email_id1, d.designation "
                + " FROM org_office of LEFT JOIN (key_person kp, designation d) ON d.designation_id=kp.designation_id "
                + " AND kp.org_office_id=of.org_office_id;";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
                JSONObject json = new JSONObject();
                String key_person_id = rs.getString("kp.key_person_id");
                String temp_org_office_id = rs.getString("of.org_office_id");
                json.put("org_office_id", temp_org_office_id);
                json.put("org_office_name", rs.getString("of.org_office_name"));
                json.put("office_mobile_no", rs.getString("of.mobile_no1"));
                json.put("office_email_id", rs.getString("of.email_id1"));

                json.put("key_person_id", key_person_id);
                json.put("key_person_name", rs.getString("kp.key_person_name"));
                json.put("mobile_no", rs.getString("kp.mobile_no1"));
                json.put("email_id", rs.getString("kp.email_id1"));
                json.put("designation", rs.getString("d.designation"));
                jsonArray.add(json);
            }
        } catch (Exception ex) {
            System.out.println("Error in getContactList() UserAppWebServiceModel : " + ex);
        }
        return jsonArray;
    }

    public JSONObject getContactJSONList() {
        JSONObject jsonObj = new JSONObject();
        String query = "SELECT of.org_office_id, of.org_office_name, of.email_id1, of.mobile_no1, "
                + " kp.key_person_id, kp.key_person_name, kp.mobile_no1, kp.email_id1, d.designation "
                + " FROM org_office of LEFT JOIN (key_person kp, designation d) ON d.designation_id=kp.designation_id "
                + " AND kp.org_office_id=of.org_office_id;";


        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            JSONObject json = new JSONObject();
            JSONArray array = new JSONArray();
            JSONObject jsonTeam = new JSONObject();
            JSONArray teamArray = new JSONArray();
            String org_office_id = "";


            while (rs.next()) {
                String key_person_id = rs.getString("kp.key_person_id");
                String temp_org_office_id = rs.getString("of.org_office_id");


                if (key_person_id == null && org_office_id.isEmpty()) {
                    json.put("org_office_id", temp_org_office_id);
                    json.put("org_office_name", rs.getString("of.org_office_name"));
                    json.put("mobile_no", rs.getString("of.mobile_no1"));
                    json.put("email_id", rs.getString("of.email_id1"));


                }
                if (org_office_id.isEmpty()) {
                    org_office_id = temp_org_office_id;
                    jsonTeam.put("org_office_id", temp_org_office_id);
                    jsonTeam.put("org_office_name", rs.getString("of.org_office_name"));
                    jsonTeam.put("mobile_no", rs.getString("of.mobile_no1"));
                    jsonTeam.put("email_id", rs.getString("of.email_id1"));

                    JSONObject teamJson = new JSONObject();
                    teamJson.put("key_person_id", key_person_id);
                    teamJson.put("key_person_name", rs.getString("kp.key_person_name"));
                    teamJson.put("mobile_no", rs.getString("kp.mobile_no1"));
                    teamJson.put("email_id", rs.getString("kp.email_id1"));
                    teamJson.put("mobile_no", rs.getString("d.designation"));
                    teamArray.add(teamJson);


                } else if (org_office_id.equals(temp_org_office_id)) {
                    JSONObject teamJson = new JSONObject();
                    teamJson.put("key_person_id", key_person_id);
                    teamJson.put("key_person_name", rs.getString("kp.key_person_name"));
                    teamJson.put("mobile_no", rs.getString("kp.mobile_no1"));
                    teamJson.put("email_id", rs.getString("kp.email_id1"));
                    teamJson.put("mobile_no", rs.getString("d.designation"));
                    teamArray.add(teamJson);


                } else if (!org_office_id.equals(temp_org_office_id)) {
                    jsonTeam.put("personData", teamArray);
                    array.add(jsonTeam);
                    jsonTeam = new JSONObject();
                    teamArray = new JSONArray();
                    org_office_id = temp_org_office_id;
                    jsonTeam.put("org_office_id", temp_org_office_id);
                    jsonTeam.put("org_office_name", rs.getString("of.org_office_name"));
                    jsonTeam.put("mobile_no", rs.getString("of.mobile_no1"));
                    jsonTeam.put("email_id", rs.getString("of.email_id1"));

                    JSONObject teamJson = new JSONObject();
                    teamJson.put("key_person_id", key_person_id);
                    teamJson.put("key_person_name", rs.getString("kp.key_person_name"));
                    teamJson.put("mobile_no", rs.getString("kp.mobile_no1"));
                    teamJson.put("email_id", rs.getString("kp.email_id1"));
                    teamJson.put("mobile_no", rs.getString("d.designation"));
                    teamArray.add(teamJson);


                }
            }
            jsonObj.put("Team", array);


        } catch (Exception ex) {
            System.out.println("Error in getContactList() UserAppWebServiceModel : " + ex);


        }
        return jsonObj;


    }

    public void setConnection() {
        try {
            System.out.println("hii");
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/code_red?useUnicode=true&characterEncoding=UTF-8&character_set_results=utf8", "jpss_2", "jpss_1277");


        } catch (Exception e) {
            System.out.println("ReadMailModel setConnection() Error: " + e);


        }
    }

    public static void main(String... args) {
        UserAppWebServiceModel obj = new UserAppWebServiceModel();
        obj.setConnection();


        try {
            obj.getContactList();


        } catch (Exception ex) {
            System.out.println("ERROR : in response " + ex);


        }
    }

    public int sendEmergencySMS(JSONObject json) {
        int rowAffacted = 0;
        String user_id = json.get("user_id").toString();
        String query = "SELECT user_id, name, mobile_no FROM user_emergency_contact WHERE active='Y' AND user_id=" + user_id;
        String userQuery = "SELECT user_id, name, mobile_no, email_id, gender, mobile_no_verified, blood_group "
                + " FROM user WHERE user_id=" + user_id;
//        String insertQuery = "INSERT INTO online_complaint (user_id, datetime, latitude, longitude, complaint_status_id) "
//                + " VALUES(?, concat(concat(current_date, ' '), current_time),?,?,1) ";
        String insertComplainQuery = "INSERT INTO code_red.complain (victim_name, mobile_no, latitude, longitude, date_time, "
                + " node_id, text, user_id, is_emergency) "
                + " VALUES(?,?,?,?, concat(concat(current_date, ' '), current_time),?,?,?,?) ";


        try {
            ResultSet rs = connection.prepareStatement(userQuery).executeQuery();


            if (rs.next()) {
                String user_name = rs.getString("name");
                String mobile_no = rs.getString("mobile_no");
                String blood_group = rs.getString("blood_group");
                String message = user_name + " need your help mobile no. " + mobile_no;


                if (blood_group != null && !blood_group.isEmpty()) {
                    message = message + " and Blood group is " + blood_group;


                }
                String latitude = json.get("latitude") == null && json.get("latitude").toString().isEmpty() ? "0.0" : json.get("latitude").toString();
                String longitude = json.get("longitude") == null && json.get("longitude").toString().isEmpty() ? "0.0" : json.get("longitude").toString();


                double latitude1 = Double.parseDouble(latitude);


                double longitude1 = Double.parseDouble(longitude);


                if (latitude1 != 0 && longitude1 != 0) {
                    message += " and location is " + " http://www.google.com/maps/place/" + latitude1 + "," + longitude1;


                }
                rs = connection.prepareStatement(query).executeQuery();


                while (rs.next()) {
                    String mobile = rs.getString("mobile_no");
                    sendSmsToAssignedFor(
                            mobile, message);
                    rowAffacted = 1;


                } //                PreparedStatement pst = connection.prepareStatement(insertQuery);
                //                pst.setString(1, user_id);
                //                pst.setDouble(2, latitude1);
                //                pst.setDouble(3, longitude1);
                //                pst.executeUpdate();
                String code_red = json.get("codered").toString();


                if (code_red.equals("Y")) {
                    PreparedStatement pst = connection.prepareStatement(insertComplainQuery,Statement.RETURN_GENERATED_KEYS);
                    pst.setString(1, user_name);
                    pst.setString(2, mobile_no.trim());
                    pst.setDouble(3, latitude1);
                    pst.setDouble(4, longitude1);
                    String node_id = json.get("node_id") == null ? "" : json.get("node_id").toString();
                    String text = json.get("text") == null ? "" : json.get("text").toString();
                    String emergency = json.get("emergency") == null ? "" : json.get("emergency").toString();


                    if (!node_id.isEmpty()) {
                        pst.setString(5, node_id);//node_id


                    } else {
                        pst.setNull(5, java.sql.Types.NULL);//node_id


                    }
                    pst.setString(6, text);//text
                    pst.setString(7, user_id);
                    pst.setString(8, emergency);//is emergency Y/N
                    String data=json.get("msgSendtoPolice")==null?"0":json.get("msgSendtoPolice").toString();
                    //pst.setString(9, data);
                    rowAffacted = pst.executeUpdate();
                    int complaint_id = 0;
                    ResultSet rset = pst.getGeneratedKeys();
                    if (rset.next()) {
                        complaint_id = rset.getInt(1);
                    }
                    if (emergency.equals("N")) {
                        json.put("complaint_id", complaint_id);
                    }
                    try {
                        String isMsgSendtoPolice = json.get("msgSendtoPolice")==null?"0":json.get("msgSendtoPolice").toString();
                        if (isMsgSendtoPolice.equalsIgnoreCase("0")) {
                            sendSmsToAssignedFor(getCodeRedContactNo(), message);
                        } else {
                            System.out.println("msgSendtoPolice...." + isMsgSendtoPolice);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    try {
//                        sendSmsToAssignedFor(needHelp(latitude, longitude), message);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    try {
                        String number = needHelp(latitude, longitude);
                        sendSmsToAssignedFor(number, message);
                        int temp = connection.prepareStatement("update complain set officer_id=(select key_person_id from key_person where mobile_no1='" + number + "') where complain_id='" + complaint_id + "'").executeUpdate();
                        if (temp > 0) {
                            System.out.println("Officer Assign for new Complain");
                        } else {
                            System.out.println("Officer Not Assign for new Complain");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (Exception ex) {
            System.out.println("Error in sendEmergencySMS() :UserAppWebServiceModel : " + ex);


        }
        return rowAffacted;


    }

    public int saveComplainFromSms(JSONObject json) {//created by rajneesh to sace complain from message received by sms reader app
        int rowAffacted = 0;
        String insertComplainQuery = "INSERT INTO code_red.complain (victim_name, mobile_no, latitude, longitude, date_time, node_id) "
                + " VALUES(?,?,?,?, concat(concat(current_date, ' '), current_time),?) ";
        try {
            PreparedStatement pst = connection.prepareStatement(insertComplainQuery);

            String user_name = json.get("user_name").toString();
            String mobile_no = json.get("mobile_no").toString();
            String latitude = json.get("latitude") == null && json.get("latitude").toString().isEmpty() ? "0.0" : json.get("latitude").toString();
            String longitude = json.get("longitude") == null && json.get("longitude").toString().isEmpty() ? "0.0" : json.get("longitude").toString();
            double latitude1 = Double.parseDouble(latitude);
            double longitude1 = Double.parseDouble(longitude);

            pst.setString(1, user_name);
            pst.setString(2, mobile_no);
            pst.setDouble(3, latitude1);
            pst.setDouble(4, longitude1);
            pst.setNull(5, java.sql.Types.NULL);

            rowAffacted = pst.executeUpdate();


        } catch (Exception e) {
            System.out.print("error in saveComplainFromSms" + e);
        }
        return rowAffacted;
    }

    public int insertComplain(JSONObject json) {

        int rowAffacted = 0;
        String insertComplainQuery = "insert into complain_report_status (complain_id, victim_name, status, latitude, longitude, general_image_details_id,date_time,mobile_no) "
                + " values (?,?,?,?,?,?,?,?) ";
        try {
            connection.setAutoCommit(false);
            PreparedStatement pst = connection.prepareStatement(insertComplainQuery);

            String complain_id = json.get("complain_id").toString();
            String victim_name = json.get("victim_name").toString();
            String message = json.get("message").toString();
            String mobile_no = json.get("mobile_no").toString();
            String date_time = json.get("date_time").toString();
            String latitude = json.get("latitude") == null && json.get("latitude").toString().isEmpty() ? "0.0" : json.get("latitude").toString();
            String longitude = json.get("longitude") == null && json.get("longitude").toString().isEmpty() ? "0.0" : json.get("longitude").toString();
            double latitude1 = Double.parseDouble(latitude);
            double longitude1 = Double.parseDouble(longitude);
//",complain_id,mobile_no,latitude,longitude
            pst.setString(1, complain_id);
            pst.setString(2, victim_name);
            pst.setString(3, message);
            pst.setDouble(4, latitude1);
            pst.setDouble(5, longitude1);
            if (true) {
                pst.setNull(6, java.sql.Types.NULL);
            } else {
                pst.setNull(6, java.sql.Types.NULL);
            }
            pst.setString(7, date_time);
            pst.setString(8, mobile_no);
            rowAffacted = pst.executeUpdate();
            ResultSet st = pst.getGeneratedKeys();
            if (st.next()) {
                rowAffacted = st.getInt(1);
            }
            try {
                insertComplainQuery = "update complain SET status_id='" + json.get("status_id").toString() + "' where complain_id=" + complain_id;
                int rowAffacted1 = connection.prepareStatement(insertComplainQuery).executeUpdate();
                if (rowAffacted1 > 0) {
                    connection.commit();
                }
                connection.setAutoCommit(true);
            } catch (Exception e) {
                connection.rollback();
                System.out.print("error in insertComplain" + e);
            }
        } catch (Exception e) {
            System.out.print("error in insertComplain" + e);
        }
        return rowAffacted;
    }

    public int insertImageRecord(String image_name, int id, int image_upload_for) {
        int rowsAffected = 0;
        DateFormat dateFormat = new SimpleDateFormat("dd.MMMMM.yyyy/ hh:mm:ss aaa");
        Date date = new Date();
        String current_date = dateFormat.format(date);
        String imageQuery = "INSERT INTO code_red.general_image_details (image_name, image_destination_id, date_time, description,key_person_id) "
                + " VALUES(?, ?, ?, ?,?)";


        try {
            PreparedStatement pstmt = connection.prepareStatement(imageQuery);
            pstmt.setString(1, image_name);
            pstmt.setInt(2, image_upload_for);
            pstmt.setString(3, current_date);
            pstmt.setString(4, "this file is for complaint");
            pstmt.setInt(5, id);
            rowsAffected = pstmt.executeUpdate();
            pstmt.close();


        } catch (Exception e) {
            System.out.println("Error:keypersonModel--insertRecord-- " + e);


        }
        return rowsAffected;


    }

    public String getCodeRedContactNo() {
        String mobile_no = "";
        String query = "SELECT mobile_no FROM code_red.sms_info";


        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();


            if (rs.next()) {
                mobile_no = rs.getString("mobile_no");


            }
        } catch (Exception ex) {
            System.out.println("Error in getCodeRedContactNo() :UserAppWebServiceModel : " + ex);


        }
        return mobile_no;


    }

    public String needHelp(String latitude, String longitude) {
        String mobile_no = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -15);
        int Hr = cal.get(Calendar.HOUR_OF_DAY);
        int Min = cal.get(Calendar.MINUTE);
        int Date = cal.get(Calendar.DATE);
        int Month = cal.get(Calendar.MONTH) + 1;
        int Year = cal.get(Calendar.YEAR);
        String date = Year + "-" + Month + "-" + Date + " " + Hr + ":" + Min + ":00";
        String query = "SELECT cordinate_id, latitude, longitude, mobile_no FROM cordinate where created_date>='" + date + "'";
        try {
            double min = 0.0;

            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
                double d = distance(Double.parseDouble(latitude), Double.parseDouble(longitude), rs.getDouble("latitude"), rs.getDouble("longitude"), "K");
                if (min == 0.0) {
                    min = d;
                }
                if (min >= d) {
                    min = d;
                    mobile_no = rs.getString("mobile_no");
                }
            }
        } catch (Exception ex) {
            System.out.println("Error in needHelp() :UserAppWebServiceModel : " + ex);
        }
        return mobile_no;
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }

        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts decimal degrees to radians						 :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts radians to decimal degrees						 :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public String getComplainData(String mobile_no, JSONObject json, int officer_id) {
        String data = "fail";
//        String query = "SELECT complain_id,officer_id, victim_name, latitude, longitude, date_time ,text FROM complain where mobile_no='" + mobile_no + "' AND officer_id IS NOT NULL AND status_id !=4 order by date_time DESC";
        String query = "SELECT complain_id,officer_id, victim_name, latitude, longitude, date_time ,text FROM complain where mobile_no='" + mobile_no + "' AND officer_id='" + officer_id + "' AND status_id !=4 and active='Y' order by date_time DESC";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            if (rs.next()) {
                try {
                    if (rs.getString("officer_id") != null) {
                        if (Integer.parseInt(rs.getString("officer_id").trim()) == officer_id) {
                            json.put("complain_id", rs.getString("complain_id"));
                            json.put("victim_name", rs.getString("victim_name"));
                            json.put("latitude", rs.getString("latitude"));
                            json.put("longitude", rs.getString("longitude"));
                            json.put("date_time", rs.getString("date_time"));
                            json.put("text", rs.getString("text"));
                            data = "success";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            System.out.println("Error in getComplainData() :UserAppWebServiceModel : " + ex);
        }
        return data;
    }

    public String loginPolice(String mobile_no, JSONObject json) {
        String data = "fail";
        String query = "SELECT key_person_id, key_person_name FROM key_person where mobile_no1='" + mobile_no + "'";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            if (rs.next()) {
                JSONArray jsonArray = new JSONArray();
                json.put("officer_id", rs.getString("key_person_id"));
                json.put("officer_name", rs.getString("key_person_name"));
                query = "SELECT status_id, status FROM status ";
                ResultSet rs1 = connection.prepareStatement(query).executeQuery();
                while (rs1.next()) {
                    JSONObject json1 = new JSONObject();
                    json1.put("status_id", rs1.getInt("status_id"));
                    json1.put("status", rs1.getString("status"));
                    jsonArray.add(json1);
                }
                json.put("status_data", jsonArray);
                data = "success";
            }
        } catch (Exception ex) {
            System.out.println("Error in loginPolice() :UserAppWebServiceModel : " + ex);
        }
        return data;
    }

    public int sendEmergencyDriverSMS(JSONObject json) {
        int rowAffacted = 0;
        String driver_id = json.get("driver_id").toString();
        String query = "SELECT key_person_id, name, mobile_no FROM key_person_emergency_contact WHERE active='Y' AND key_person_id=" + driver_id;
        String userQuery = "SELECT key_person_id, key_person_name, mobile_no1 "
                + " FROM key_person WHERE key_person_id=" + driver_id;


        try {
            ResultSet rs = connection.prepareStatement(userQuery).executeQuery();


            if (rs.next()) {
                String user_name = rs.getString("key_person_name");
                String mobile_no = rs.getString("mobile_no1");
                String msg = user_name + " need your help mobile no. " + mobile_no;
                String latitude = json.get("latitude") == null && json.get("latitude").toString().isEmpty() ? "0.0" : json.get("latitude").toString();
                String longitude = json.get("longitude") == null && json.get("longitude").toString().isEmpty() ? "0.0" : json.get("longitude").toString();


                double latitude1 = Double.parseDouble(latitude);


                double longitude1 = Double.parseDouble(longitude);


                if (latitude1 != 0 && longitude1 != 0) {
                    msg += " and location is " + " http://www.google.com/maps/place/" + latitude1 + "," + longitude1;


                }
                rs = connection.prepareStatement(query).executeQuery();


                while (rs.next()) {
                    String mobile = rs.getString("mobile_no");
                    sendSmsToAssignedFor(
                            mobile, msg);
                    rowAffacted = 1;


                }
            }
        } catch (Exception ex) {
            System.out.println("Error in getBusDetail() :UserAppWebServiceModel : " + ex);


        }
        return rowAffacted;


    }

    public JSONObject registerDriver(JSONObject json) {
        int rowAffected = 0;
        String query = "INSERT INTO vehicle_registration (vehicle_no, driver_name, mobile_no) VALUES(?, ?, ?)";
        String personQuery = "INSERT INTO key_person (key_person_name, org_office_id, city_id, mobile_no1, designation_id, emp_code) VALUES(?,1,1,?,3,?)";
        String vehicleQuery = "INSERT INTO vehicle (vehicle_no, owner_id, remark, vehicle_code) VALUES(?,?,?,?)";
        String vehicleDriverQuery = "INSERT INTO vehicle_driver_map (vehicle_id, key_person_id, use_app) VALUES(?,?,'Y')";
        String vehicleOrderQuery = "INSERT INTO vehicle_order (vehicle_id) VALUES(?)";
        String vehicle_no = json.get("vehicle_no").toString().trim().toUpperCase().replace(" ", "").replace(".", "");


        int length = vehicle_no.length();
        String code = vehicle_no.substring(length - 4, length);
        String name = json.get("name").toString();
        String mobile_no = json.get("mobile_no").toString();


        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, vehicle_no);
            pst.setString(2, name);
            pst.setString(3, mobile_no);
            rowAffected = pst.executeUpdate();


            if (rowAffected > 0) {
                json.put("result", 1);


            } else {
                json.put("result", 0);


            }
            if (rowAffected > 0) {
                pst = connection.prepareStatement(personQuery);
                pst.setString(1, name);
                pst.setString(2, mobile_no);
                pst.setString(3, code);
                rowAffected = pst.executeUpdate();


                int person_id = 0;
                ResultSet rs = pst.getGeneratedKeys();


                if (rs.next()) {
                    person_id = rs.getInt(1);


                }
                if (rowAffected > 0) {
                    pst = connection.prepareStatement(vehicleQuery);
                    pst.setString(1, vehicle_no);
                    pst.setInt(2, person_id);
                    pst.setString(3, mobile_no);
                    pst.setString(4, code);
                    rowAffected = pst.executeUpdate();


                    int vehicle_id = 0;
                    rs = pst.getGeneratedKeys();


                    if (rs.next()) {
                        vehicle_id = rs.getInt(1);


                    }
                    if (rowAffected > 0) {
                        pst = connection.prepareStatement(vehicleDriverQuery);
                        pst.setInt(1, vehicle_id);
                        pst.setInt(2, person_id);
                        rowAffected = pst.executeUpdate();


                    }
                    if (rowAffected > 0) {
                        pst = connection.prepareStatement(vehicleOrderQuery);
                        pst.setInt(1, vehicle_id);
                        rowAffected = pst.executeUpdate();


                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Error in registerDriver() :UserAppWebServiceModel : " + ex + " IN " + json);


        }
        return json;


    }

    public int driverMobileNo(String mobile_no) {
        int rowAffected = 0;
        String query = "UPDATE vehicle_registration SET mobile_verify='Y' WHERE mobile_no='" + mobile_no + "'";


        try {
            rowAffected = connection.prepareStatement(query).executeUpdate();


        } catch (Exception ex) {
            System.out.println("Error in registerDriver() :UserAppWebServiceModel : " + ex);


        }
        return rowAffected;


    }

    public JSONArray getPopularDestination() {
        JSONArray jsonArray = new JSONArray();
        String query = "SELECT count(sd.source_destination_id) destination, sd.destination_id "
                + " FROM ride r, source_destination sd WHERE sd.source_destination_id=r.source_destination_id "
                + " GROUP BY sd.source_destination_id ORDER BY destination DESC LIMIT 0, 10 ";


        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();


            while (rs.next()) {
                jsonArray.add(rs.getString("destination_id"));


            }
        } catch (Exception ex) {
            System.out.println("Error in registerDriver() :UserAppWebServiceModel : " + ex);


        }
        return jsonArray;


    }

    public int setFavoriteDriver(JSONObject json) {
        int rowAffected = 0;
        String user_id = json.get("user_id").toString();
        String vehicle_id = json.get("vehicle_id").toString();
        String driver_id = json.get("driver_id").toString();
        String active = json.get("status").toString();
        String selectQuery = "SELECT user_id, vehicle_id, driver_id FROM user_favorite_driver WHERE user_id = " + user_id + " AND vehicle_id = " + vehicle_id + " AND driver_id = " + driver_id;
        String query = "INSERT INTO user_favorite_driver (user_id, vehicle_id, driver_id) "
                + " VALUES(" + user_id + ", " + vehicle_id + ", " + driver_id + ") ";


        if (active.equals("N")) {
            query = "UPDATE user_favorite_driver SET active='" + active + "' WHERE user_id = " + user_id + " AND vehicle_id = " + vehicle_id + " AND driver_id = " + driver_id;


        }
        try {
            if (active.equals("Y")) {
                ResultSet rs = connection.prepareStatement(selectQuery).executeQuery();


                if (rs.next()) {
                    query = "UPDATE user_favorite_driver SET active='" + active + "' WHERE user_id = " + user_id + " AND vehicle_id = " + vehicle_id + " AND driver_id = " + driver_id;


                }
            }
            rowAffected = connection.prepareStatement(query).executeUpdate();


        } catch (Exception ex) {
            System.out.println("Error in registerDriver() :UserAppWebServiceModel : " + ex);


        }
        return rowAffected;


    }

    public int setFavoriteLocation(JSONObject json) {
        int rowAffected = 0;
        String user_id = json.get("user_id").toString();
        String location_id = json.get("location_id").toString();
        String active = json.get("status").toString();
        String selectQuery = "SELECT user_id, city_location_id FROM user_favorite_location WHERE user_id = " + user_id + " AND city_location_id = " + location_id;
        String query = "INSERT INTO user_favorite_location (user_id, city_location_id) "
                + " VALUES(" + user_id + ", " + location_id + ") ";


        if (active.equals("N")) {
            query = "UPDATE user_favorite_location SET active='" + active + "' WHERE user_id = " + user_id + " AND city_location_id = " + location_id;


        }
        try {
            if (active.equals("Y")) {
                ResultSet rs = connection.prepareStatement(selectQuery).executeQuery();


                if (rs.next()) {
                    query = "UPDATE user_favorite_location SET active='" + active + "' WHERE user_id = " + user_id + " AND city_location_id = " + location_id;


                }
            }
            rowAffected = connection.prepareStatement(query).executeUpdate();


        } catch (Exception ex) {
            System.out.println("Error in registerDriver() :UserAppWebServiceModel : " + ex);


        }
        return rowAffected;


    }

    public JSONArray getFavoriteLocation(String user_id) {
        JSONArray jsonArray = new JSONArray();
        String selectQuery = "SELECT user_id, city_location_id FROM user_favorite_location "
                + " WHERE active='Y' AND user_id = " + user_id;


        try {
            ResultSet rs = connection.prepareStatement(selectQuery).executeQuery();


            while (rs.next()) {
                jsonArray.add(rs.getInt("city_location_id"));


            }
        } catch (Exception ex) {
            System.out.println("Error in registerDriver() :UserAppWebServiceModel : " + ex);


        }
        return jsonArray;


    }

    public JSONObject getLiveAutoTarrif() {
        JSONObject json = new JSONObject();
        String query = "SELECT tarrif_id, tarrif_code, minimum_charge, night_charge_percent, baggage_charge, rate, service_charge "
                + " FROM tarrif WHERE active = 'Y' AND tarrif_code='Live Auto 2'";


        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();


            if (rs.next()) {
                json.put("tarrif_id", rs.getString("tarrif_id"));
                json.put("tarrif_code", rs.getString("tarrif_code"));
                json.put("minimum_charge", rs.getString("minimum_charge"));
                json.put("night_charge_percent", rs.getString("night_charge_percent"));
                json.put("baggage_charge", rs.getString("baggage_charge"));
                json.put("rate", rs.getString("rate"));
                json.put("service_charge", rs.getString("service_charge"));


            }
        } catch (Exception ex) {
            System.out.println("ERROR: in getTariffDetail in UserAppWebServiceModel : " + ex);


        }
        return json;


    }

    public Connection getConnection() {
        return connection;


    }

    public void setConnection(Connection connection) {
        this.connection = connection;


    }

    public String getMessage() {
        return message;


    }

    public void setMessage(String message) {
        this.message = message;


    }

    public String getMsgBgColor() {
        return msgBgColor;


    }

    public void setMsgBgColor(String msgBgColor) {
        this.msgBgColor = msgBgColor;


    }

    public void closeConnection() {
        try {
            connection.close();


        } catch (Exception e) {
            System.out.println("CorrespondencePriorityModel closeConnection() Error: " + e);

        }
    }
}
