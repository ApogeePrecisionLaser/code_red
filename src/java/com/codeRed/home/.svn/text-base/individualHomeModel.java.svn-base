/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeRed.home;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Manpreet
 */
public class individualHomeModel {

    private Connection connection;
    private String message;
    private String msgBgColor;
    private final String COLOR_OK = "lightyellow";
    private final String COLOR_ERROR = "red";

    public void setConnection(Connection con) {
        try {
            connection = con;
        } catch (Exception e) {
            System.out.println("individualHomeModel setConnection() Error: " + e);
        }
    }

    public int getNoOfRows() {
        int noOfRows = 0;
        try {
            ResultSet rset = connection.prepareStatement("SELECT COUNT(*) FROM message ").executeQuery();
            rset.next();
            noOfRows = Integer.parseInt(rset.getString(1));
        } catch (Exception e) {
            System.out.println("individualHomeModel getNoOfRows() Error: " + e);
        }
        return noOfRows;
    }

    public List<individualHomeBean> showData(int lowerLimit, int noOfRowsToDisplay) {
        List<individualHomeBean> list = new ArrayList<individualHomeBean>();
        // Use DESC or ASC for descending or ascending order respectively of fetched data.
        String query = "SELECT m.message_id, m.message, m.keyperson_id, k.key_person_name "
                + "FROM message m, key_person k "
                + "WHERE m.keyperson_id = k.key_person_id "
                + "ORDER BY m.message_id Desc "
                + "LIMIT " + lowerLimit + ", " + noOfRowsToDisplay;
        try {
            ResultSet rset = connection.prepareStatement(query).executeQuery();
            while (rset.next()) {
                individualHomeBean homeBean = new individualHomeBean();
                homeBean.setMessage_id(rset.getInt("message_id"));
                homeBean.setMessage(rset.getString("message"));
                homeBean.setKeyperson_name(rset.getString("key_person_name"));
                list.add(homeBean);
            }
        } catch (Exception e) {
            System.out.println("individualHomeModel showData() Error: " + e);
        }
        return list;
    }

//    public int insertRecord(individualHomeBean homeBean) {
//
//        String query = "INSERT INTO displaymsg(body,subject,msg_date_time,keyperson_id,link) VALUES(?,?,?,?,?) "; //message_id,
//        int rowsAffected = 0;
//        try {
//            PreparedStatement pstmt = connection.prepareStatement(query);
//            // pstmt.setInt(1, homeBean.getMessage_id());
//            pstmt.setString(1, homeBean.getMessage());
//            pstmt.setInt(2, homeBean.getKeyperson_id());
//            rowsAffected = pstmt.executeUpdate();
//        } catch (Exception e) {
//            System.out.println("individualHomeModel insertRecord() Error: " + e);
//        }
//        if (rowsAffected > 0) {
//            message = "Message Saved Successfully";
//            msgBgColor = COLOR_OK;
//            System.out.println("Record inserted successfully in message");
//        } else {
//            message = "Cannot Save Message, Some Error";
//            msgBgColor = COLOR_ERROR;
//            System.out.println("Record not saved in message");
//        }
//        return rowsAffected;
//    }

    public int insertRecord(individualHomeBean homeBean) {
        String query = "INSERT INTO message(message, keyperson_id) VALUES(?,?) ";
        int rowsAffected = 0;
        int user_id = homeBean.getKeyperson_id();
        String message = homeBean.getMessage();
        String keypersonData = getkeypersonName(user_id);
        String keypersonName = keypersonData.split("#")[0];
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, message);
            pstmt.setInt(2, user_id);
            rowsAffected = pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("individualHomeModel insertMessageRecord() Error: " + e);
        }
        if (rowsAffected > 0) {
            message = message + " \n\n "
                    + "Message From:- " + keypersonName;
            sendSmsToAllKeypersons(user_id, keypersonName, message);
            message = "Message Saved Successfully";
            msgBgColor = COLOR_OK;
            System.out.println("individualHomeModel - Record inserted successfully in message");
        } else {
            message = "Cannot Save Message, Some Error";
            msgBgColor = COLOR_ERROR;
            System.out.println("individualHomeModel - Record not saved in message");
        }
        return rowsAffected;
    }

    public String sendSmsToAllKeypersons(int user_id, String keypersonName, String message) {
        ArrayList<String> list = new ArrayList<String>();
        String result = "";

        try {
//            String query = "Select mobile_no1 from key_person Where  "
//                    + " designation_id=4 and mobile_no1 is not null And mobile_no1 != '1234567890' ";
//                   // + " And key_person_id!= " + user_id;
            String query = "Select mobile_no from user  ";
            ResultSet rset = connection.prepareStatement(query).executeQuery();
            while (rset.next()) {
                String mobNo = rset.getString("mobile_no");
                list.add(mobNo);
            }

        } catch (Exception e) {
            System.out.println("individualHomeModel - sendSmsToAllKeypersons() query block Error: " + e);
        }

        try {
            int count = 1;
            int state;
            state = 4;

            String host_url = "http://login.smsgatewayhub.com/api/mt/SendSMS?";//"http://api.mVaayoo.com/mvaayooapi/MessageCompose?";
            String from_user_name = "jpss1277@gmail.com";        // e.g. "jpss1277@gmail.com"
            String from_user_password = "8826887606"; // e.g. "8826887606"
            String sender_id = java.net.URLEncoder.encode("TEST SMS", "UTF-8");         // e.g. "TEST+SMS"

//                String strMsg = message + " \n\n "
//                               + "Message From:- " + keypersonName;
            String strMsg = message;

            String strMsg1 = java.net.URLEncoder.encode(strMsg, "UTF-8");

            for (int i = 0; i < list.size(); i++) {
                String mobile_no = list.get(i);
//                String queryString = "user=" + from_user_name + ":" + from_user_password + "&senderID="
//                        + sender_id + "&receipientno=" + mobile_no + "&msgtxt=" + strMsg1;
//                        //+ "&state=" + state;

                String queryString = "APIKey=WIOg7OdIzkmYTrqTsw262w&senderid=JPSOFT&channel=2&DCS=8&flashsms=0&number=" + mobile_no + "&text=" + strMsg1 + "&route=";
                String url = host_url + queryString;
                result = callURL(url);
                System.out.println("result :" + result + "SMS URL: " + url);
            }

        } catch (Exception e) {
            result = e.toString();
            System.out.println("individualHomeModel - sendSmsToAllKeypersons() Error: " + e);
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

    public String getkeypersonName(int user_id) {
        String keypersonData = "";
        String query = "SELECT key_person_name , mobile_no1 FROM key_person k where  key_person_id = " + user_id;
        try {
            ResultSet rset = connection.prepareStatement(query).executeQuery();
            if (rset.next()) {
                keypersonData = rset.getString("key_person_name") + "#" + rset.getString("mobile_no1");
            }
        } catch (Exception e) {
            System.out.println("individualHomeModel getkeypersonName() Error: " + e);
        }

        return keypersonData;
    }

// Web service methods -- starts here

    public JSONArray getBroadcastMsgs() {
        JSONArray jsonArray = new JSONArray();

//        DateFormat dv=new SimpleDateFormat("yyyy-MM-dd");
//        Date d = new Date();
//        String searchCaseDate = dv.format(d);

        String query = "SELECT m.message_id, m.message, m.keyperson_id, k.key_person_name "
                + "FROM message m, key_person k "
                + "WHERE m.keyperson_id = k.key_person_id "
                + "ORDER BY m.message_id Desc ";
        try {
            ResultSet rset = connection.prepareStatement(query).executeQuery();
            while (rset.next()) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("message_id", rset.getInt("message_id"));
                jSONObject.put("message", rset.getString("message"));
                jSONObject.put("keyperson_id", rset.getInt("keyperson_id"));
                jSONObject.put("key_person_name", rset.getString("key_person_name"));
                jsonArray.add(jSONObject);
            }
        } catch (Exception e) {
            System.out.println("individualHomeModel getBroadcastMsgs() Error: " + e);
        }
        return jsonArray;
    }

// Web service methods -- ends here
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            } else {
                System.out.println("individualHomeModel closeConnection() : Connection already closed ");
            }
        } catch (Exception e) {
            System.out.println("individualHomeModel closeConnection() Error: " + e);
        }
    }

    public String getMessage() {
        return message;
    }

    public String getMsgBgColor() {
        return msgBgColor;
    }
}
