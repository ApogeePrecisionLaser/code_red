/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeRed.complaint.model;

import com.codeRed.complaint.tableClasses.ComplaintBean;
import com.codeRed.shift.model.ShiftTimeModel;
import com.codeRed.util.KrutiDevToUnicodeConverter;
import com.codeRed.util.UnicodeToKrutiDevConverter;
import com.codeRed.webServices.model.UserAppWebServiceModel;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import javazoom.jl.decoder.JavaLayerException;
//import javazoom.jl.player.Player;

/**
 *
 * @author jpss
 */
public class ComplaintModel {

    private Connection connection;
    private String message;
    private String msgBgColor;
    private final String COLOR_OK = "lightyellow";
    private final String COLOR_ERROR = "red";
    private KrutiDevToUnicodeConverter krutiToUnicode = new KrutiDevToUnicodeConverter();
    private UnicodeToKrutiDevConverter unicodeToKruti = new UnicodeToKrutiDevConverter();

    public int getNoOfRows(String searchMobileNo, String searchDate, String searchStatus, String searchEmergency, String searchOfficerCode) {
        int noOfRows = 0;
        String query = " SELECT count(c.complain_id) "
                + " FROM complain AS c INNER JOIN status AS s ON s.status_id=c.status_id "
                + " LEFT JOIN key_person AS kp ON kp.key_person_id=c.officer_id "
                + " LEFT JOIN node AS n ON n.node_id=c.node_id"
                + " WHERE c.active='Y' "
                + " AND IF('" + searchMobileNo + "'='', c.mobile_no LIKE '%%', c.mobile_no='" + searchMobileNo + "' )"
                + " AND IF('" + searchDate + "'='', c.date_time LIKE '%%' OR c.date_time IS NULL, c.date_time LIKE '" + searchDate + "%' )"
                + " AND IF('" + searchStatus + "'='', s.status_id LIKE '%%', s.status_id='" + searchStatus + "' )"
                + " AND IF('" + searchEmergency + "'='', c.is_emergency LIKE '%%' OR c.is_emergency IS NULL, c.is_emergency='" + searchEmergency + "' )"
                + " AND IF('" + searchOfficerCode + "'='', kp.emp_code LIKE '%%' OR kp.emp_code IS NULL, kp.emp_code='" + searchOfficerCode + "' )";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            if (rs.next()) {
                noOfRows = rs.getInt(1);
            }
        } catch (Exception ex) {
        }
        return noOfRows;
    }

    public List<ComplaintBean> showData(int lowerLimit, int noOfRowsToDisplay, String searchMobileNo, String searchDate, String searchStatus, String searchEmergency, String searchOfficerCode) {
        List<ComplaintBean> list = new ArrayList<ComplaintBean>();
        String add_limit = " LIMIT " + lowerLimit + "," + noOfRowsToDisplay;
        if (lowerLimit == -1)
        {
            add_limit = "";
        }
        String query = "SELECT c.police_involve,c.complain_id, c.victim_name, c.mobile_no, c.latitude, c.longitude, c.officer_id, c.status_id,"
                + " c.revision_no, c.active, c.remark, c.date_time, c.node_id, c.text, c.user_id, c.is_emergency, s.status, "
                + " kp.key_person_name AS officer_name, n.node_name "
                + " FROM complain AS c INNER JOIN status AS s ON s.status_id=c.status_id "
                + " LEFT JOIN key_person AS kp ON kp.key_person_id=c.officer_id "
                + " LEFT JOIN node AS n ON n.node_id=c.node_id "
                + " WHERE c.active='Y' "
                + " AND IF('" + searchMobileNo + "'='', c.mobile_no LIKE '%%', c.mobile_no='" + searchMobileNo + "' )"
                + " AND IF('" + searchDate + "'='', c.date_time LIKE '%%' OR c.date_time IS NULL, c.date_time LIKE '" + searchDate + "%' )"
                + " AND IF('" + searchStatus + "'='', s.status_id LIKE '%%', s.status_id='" + searchStatus + "' )"
                + " AND IF('" + searchEmergency + "'='', c.is_emergency LIKE '%%' OR c.is_emergency IS NULL, c.is_emergency='" + searchEmergency + "' )"
                + " AND IF('" + searchOfficerCode + "'='', kp.emp_code LIKE '%%' OR kp.emp_code IS NULL, kp.emp_code='" + searchOfficerCode + "' )"
                + " ORDER BY c.complain_id DESC "
                + add_limit;
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
                ComplaintBean complaintBean = new ComplaintBean();
                complaintBean.setComplain_id(rs.getInt("complain_id"));
                complaintBean.setRevision_no(rs.getInt("revision_no"));
                complaintBean.setVictim_name(rs.getString("victim_name"));
                complaintBean.setMobile_no(rs.getString("mobile_no"));
                complaintBean.setLatitude(rs.getDouble("latitude"));
                complaintBean.setLongitude(rs.getDouble("longitude"));
                complaintBean.setOfficer_id(rs.getInt("officer_id"));
                complaintBean.setOfficer_name(rs.getString("officer_name"));
                complaintBean.setStatus_id(rs.getInt("status_id"));
                complaintBean.setStatus(rs.getString("status"));
                complaintBean.setActive(rs.getString("active"));
                complaintBean.setRemark(rs.getString("remark"));
                complaintBean.setDate_time(rs.getString("date_time"));
                complaintBean.setPolice_involve(rs.getString("police_involve").equals("1")?"N":"Y");
                int node_id = rs.getInt("node_id");
                String fullNodeName = "";
                if (node_id > 0) {
                    ResultSet rSet = connection.prepareStatement("call getNodeRevData(" + node_id + ")").executeQuery();
                    while (rSet.next()) {
                        if (fullNodeName.isEmpty()) {
                            fullNodeName = rSet.getString("node_name");
                        } else {
                            fullNodeName = fullNodeName + ", " + rSet.getString("node_name");
                        }
                    }
                    complaintBean.setFullNodeName(fullNodeName);
                }
                complaintBean.setNode_id(node_id);
                complaintBean.setNode_name(rs.getString("node_name"));
                complaintBean.setText(rs.getString("text"));
                complaintBean.setUser_id(rs.getInt("user_id"));
                complaintBean.setIs_emergency(rs.getString("is_emergency"));
                list.add(complaintBean);
            }
        } catch (Exception ex) {
            System.out.println("ERROR : in showData() in ComplaintModel : " + ex);
        }
        return list;
    }

    public String checkTotalRows(int total_rows) {
        String response = "";
        int noOfRows = 0;
        String query = " SELECT count(c.complain_id) "
                + " FROM complain AS c INNER JOIN status AS s ON s.status_id=c.status_id "
                + " LEFT JOIN key_person AS kp ON kp.key_person_id=c.officer_id "
                + " LEFT JOIN node AS n ON n.node_id=c.node_id"
                + " WHERE c.active='Y' ";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            if (rs.next()) {
                noOfRows = rs.getInt(1);
            }

            if(noOfRows > total_rows){
               //response = playComplaintAlert();
                response = "play_audio";
            }
        } catch (Exception ex) {
            System.out.println("ERROR : in checkTotalRows() in ComplaintModel : " + ex);
        }
        return response;
    }

//    public String playComplaintAlert() {
//        String response = "";
//        try {
////            File file = new File("E:\\Manpreet.Workspace\\CodeRed\\web\\audios\\complainAlert.wav");
//            File file = new File("E:\\NetBeansProjects\\new_repository\\CodeRed\\web\\audios\\audio.mp3");
//            FileInputStream fis = new FileInputStream(file);
//            BufferedInputStream bis = new BufferedInputStream(fis);
//            try {
//                Player player = new Player(bis);
//                player.play();
//                response = "playing";
//               // boolean boll=player.play(1);
//               // Thread.sleep(10000);
//               // respone = "playing";
//            } catch (JavaLayerException e) {
//                System.out.println("playComplaintAlert() Error - " + e);
//            }
//        } catch (Exception e) {
//            System.out.println("Error in playComplaintAlert() - " + e);
//        }
//        return response;
//    }

//    public String stopAudio() {
//        String response = "";
//        try {
////            File file = new File("E:\\Manpreet.Workspace\\CodeRed\\web\\audios\\complainAlert.wav");
//            File file = new File("E:\\NetBeansProjects\\new_repository\\CodeRed\\web\\audios\\audio.mp3");
//            FileInputStream fis = new FileInputStream(file);
//            BufferedInputStream bis = new BufferedInputStream(fis);
//            try {
//                Player player = new Player(bis);
//                player.close();
//                response = "stopping";
//            } catch (JavaLayerException e) {
//                System.out.println("stopAudio() Error - " + e);
//            }
//        } catch (Exception e) {
//            System.out.println("Error in stopAudio() - " + e);
//        }
//        return response;
//    }

    public int insertRecord(ComplaintBean complaintBean, String task) {
        int rowAffected = 0;
        String query = "INSERT INTO complain (victim_name, mobile_no, latitude, longitude, officer_id, "
                + " status_id, remark, date_time, node_id, text, user_id, is_emergency) "
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String reviseQuery = "INSERT INTO complain (victim_name, mobile_no, latitude, longitude, officer_id, "
                + " status_id, remark, date_time, node_id, text, user_id, is_emergency, complain_id, revision_no) "
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int complain_id = complaintBean.getComplain_id();
        int revision_no = complaintBean.getRevision_no();
        String updateQuery = "UPDATE complain SET active='N' WHERE complain_id=" + complain_id + " AND revision_no=" + revision_no;
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            if (task.equals("Revise")) {
                pst = connection.prepareStatement(reviseQuery);
            }
            pst.setString(1, complaintBean.getVictim_name());
            pst.setString(2, complaintBean.getMobile_no());
            pst.setDouble(3, complaintBean.getLatitude());
            pst.setDouble(4, complaintBean.getLongitude());
            int officer_id = complaintBean.getOfficer_id();
            if (officer_id > 0) {
                pst.setInt(5, officer_id);
            } else {
                pst.setNull(5, java.sql.Types.NULL);
            }
            pst.setString(6, complaintBean.getStatus());
            pst.setString(7, complaintBean.getRemark());
            pst.setString(8, complaintBean.getDate_time());
            int node_id = complaintBean.getNode_id();
            if (node_id > 0) {
                pst.setInt(9, node_id);
            } else {
                pst.setNull(9, java.sql.Types.NULL);
            }
            pst.setString(10, complaintBean.getText());
            int user_id = complaintBean.getUser_id();
            if (user_id > 0) {
                pst.setInt(11, user_id);
            } else {
                pst.setNull(11, java.sql.Types.NULL);
            }
            pst.setString(12, complaintBean.getIs_emergency());
            if (task.equals("Revise")) {
                pst.setInt(13, complain_id);
                pst.setInt(14, revision_no + 1);
            }
            rowAffected = pst.executeUpdate();
            if (rowAffected > 0 && task.equals("Revise")) {
                rowAffected = connection.prepareStatement(updateQuery).executeUpdate();
            }
            //modifed by rajneesh to send msg to the assigned officer 3/4/2017
            if(task.equalsIgnoreCase("Revise") && officer_id>0){
                sendMsgToOfficer(complaintBean);
            }
        } catch (Exception ex) {
            System.out.println("ERROR : in insertRecord() in ComplaintModel : " + ex);
        }
        if (rowAffected > 0) {
            this.message = "Record is Updated Successfully.";
            this.msgBgColor = COLOR_OK;
        } else {
            this.message = "problem in updating record Some Error!";
            this.msgBgColor = COLOR_OK;
        }
        return rowAffected;
    }

    public int cancelRecord(int complain_id) {
        int rowAffected = 0;
        String query = "UPDATE complain SET active='N' WHERE complain_id=" + complain_id;
        try {
            rowAffected = connection.prepareStatement(query).executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR : in cancelRecord() in ComplaintModel : " + ex);
        }
        if (rowAffected > 0) {
            this.message = "Record is Updated Successfully.";
            this.msgBgColor = COLOR_OK;
        } else {
            this.message = "problem in Updating record Some Error!";
            this.msgBgColor = COLOR_OK;
        }
        return rowAffected;
    }

    public Map<String, String> getStatus() {
        Map<String, String> map = new HashMap<String, String>();
        String query = " SELECT status_id, status FROM status ";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
                map.put(rs.getString("status_id"), rs.getString("status"));
            }
        } catch (Exception ex) {
            System.out.println("ERROR : in getStatus() in ComplaintModel : " + ex);
        }
        return map;
    }

    public int getOfficerId(String officer_code) {
        int officer_id = 0;
        Map<String, String> map = new HashMap<String, String>();
        String query = " SELECT key_person_id FROM key_person WHERE emp_code='" + officer_code + "' ";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
                officer_id = rs.getInt("key_person_id");
            }
        } catch (Exception ex) {
            System.out.println("ERROR : in getStatus() in ComplaintModel : " + ex);
        }
        return officer_id;
    }

    public List<String> getMobileNo(String q) {
        List<String> list = new ArrayList<String>();
        String query = " SELECT mobile_no FROM complain WHERE mobile_no Like '" + q.trim() + "%' GROUP BY mobile_no ";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
                list.add(rs.getString("mobile_no"));
            }
        } catch (Exception ex) {
            System.out.println("ERROR : in getMobileNo() in ComplaintModel : " + ex);
        }
        return list;
    }

    public String getDestination_Path(String image_uploaded_for) {
        String destination_path = "";
        String query = " SELECT destination_path FROM image_destination id, image_uploaded_for  iuf "
                + " WHERE id.image_uploaded_for_id = iuf.image_uploaded_for_id "
                + " AND iuf.image_uploaded_for = '" + image_uploaded_for + "' ";//traffic_police
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            if (rs.next()) {
                destination_path = rs.getString("destination_path");
            }
        } catch (Exception ex) {
            System.out.println("ERROR: in getTrafficPoliceId in TraffiPoliceSearchModel : " + ex);
        }
        return destination_path;
    }

    public List<String> getImageNameList(String id) {
        List<String> list = new ArrayList<String>();
        int count = 0;
        String query = "SELECT g.image_name FROM image_destination id,image_uploaded_for  iuf,general_image_details g "
                + "  WHERE id.image_uploaded_for_id = iuf.image_uploaded_for_id AND id.image_destination_id = g.image_destination_id "
                + "  AND iuf.image_uploaded_for = 'Complaint_image' and key_person_id=" + id + " ";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
                String image_name = rs.getString("image_name");
                list.add(image_name);
                count++;

            }
        } catch (Exception ex) {
        }
        return list;
    }

    public void sendMsgToOfficer(ComplaintBean complaintBean ){
        String officer_ph = "",officer_name;
        UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        String query="SELECT key_person_name,mobile_no1 FROM key_person WHERE key_person_id="+complaintBean.getOfficer_id();
       try{
        ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
                 officer_name = rs.getString("key_person_name");
                 officer_ph=rs.getString("mobile_no1");
            }
        String message=complaintBean.getVictim_name()+" need your help mobile no. " + complaintBean.getMobile_no()+
                " location is " + " http://www.google.com/maps/place/" + complaintBean.getLatitude() + "," + complaintBean.getLongitude();
        userAppWebServiceModel.sendSmsToAssignedFor(officer_ph, message);

        }catch(Exception e){
            System.out.println("error in sendMsgToOfficer"+e);
        }

    }

    public Connection getConnection() {
        return connection;
    }

    public String getMessage() {
        return message;
    }

    public String getMsgBgColor() {
        return msgBgColor;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMsgBgColor(String msgBgColor) {
        this.msgBgColor = msgBgColor;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (Exception ex) {
        }
    }
}
