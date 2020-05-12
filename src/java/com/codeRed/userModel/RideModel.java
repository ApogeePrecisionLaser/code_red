/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeRed.userModel;

import com.codeRed.userTable.RideBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Shubham
 */
public class RideModel {

    private Connection connection;
    private String message;
    private String msgBgColor;
    private final String COLOR_OK = "lightyellow";
    private final String COLOR_ERROR = "red";

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public int getNoOfRows(String searchName, String searchMobileNo, String SearchBookingNo, String searchDate) {
        int noOfRows = 0;
        String query = "SELECT count(rf.ride_feedback_id)"
                + " FROM ride_feedback as rf"
                + " LEFT JOIN ride_from_app as rfa ON rf.ride_from_app_id=rfa.ride_from_app_id"
                + " LEFT JOIN user as u ON rfa.user_id=u.user_id"
                + " LEFT JOIN (SELECT v.vehicle_id, v.vehicle_no, kp.key_person_name, kp.key_person_id "
                + " FROM vehicle AS v, vehicle_driver_map AS vdm, key_person AS kp WHERE v.active='Y' AND vdm.vehicle_id=v.vehicle_id "
                + " AND vdm.key_person_id=kp.key_person_id AND vdm.active='Y' GROUP BY v.vehicle_id) AS v ON v.vehicle_id = rfa.vehicle_id "
                + " where IF('" + searchName + "'='', name LIKE '%%',name = '" + searchName + "') "
                + " AND IF('" + searchMobileNo + "'='', mobile_no LIKE '%%',mobile_no = '" + searchMobileNo + "') "
                + " AND IF('" + SearchBookingNo + "'='', rf.ride_from_app_id LIKE '%%',rf.ride_from_app_id = '" + SearchBookingNo + "') "
                + " AND IF('" + searchDate + "'='', ride_date LIKE '%%',ride_date Like '" + searchDate + "%') ";
        try {
            ResultSet rset = connection.prepareStatement(query).executeQuery();
            rset.next();
            noOfRows = Integer.parseInt(rset.getString(1));
        } catch (Exception e) {
            System.out.println("error in getNoOfRows RideModel");
        }
        return noOfRows;
    }

    public List<RideBean> showData(int lowerLimit, int noOfRowsToDisplay, String searchName, String searchMobileNo, String SearchBookingNo, String searchDate) {
        List<RideBean> list = new ArrayList<RideBean>();
        String add_limit = "  LIMIT " + lowerLimit + ", " + noOfRowsToDisplay;
        if (lowerLimit == -1) {
            add_limit = "";
        }
        String query = "SELECT name,mobile_no,rating,rf.ride_from_app_id as booking_no , v.vehicle_no,"
                + " v.key_person_name as driver_name,ride_time,ride_date, rf.remark, rf.feedback"
                + " FROM ride_feedback as rf"
                + " LEFT JOIN ride_from_app as rfa ON rf.ride_from_app_id=rfa.ride_from_app_id"
                + " LEFT JOIN user as u ON rfa.user_id=u.user_id"
                + " LEFT JOIN (SELECT v.vehicle_id, v.vehicle_no, kp.key_person_name, kp.key_person_id "
                + " FROM vehicle AS v, vehicle_driver_map AS vdm, key_person AS kp WHERE v.active='Y' AND vdm.vehicle_id=v.vehicle_id "
                + " AND vdm.key_person_id=kp.key_person_id AND vdm.active='Y' GROUP BY v.vehicle_id) AS v ON v.vehicle_id = rfa.vehicle_id "
                + " where IF('" + searchName + "'='', name LIKE '%%',name = '" + searchName + "') "
                + " AND IF('" + searchMobileNo + "'='', mobile_no LIKE '%%',mobile_no = '" + searchMobileNo + "') "
                + " AND IF('" + SearchBookingNo + "'='', rf.ride_from_app_id LIKE '%%',rf.ride_from_app_id = '" + SearchBookingNo + "') "
                + " AND IF('" + searchDate + "'='', ride_date LIKE '%%',ride_date Like '" + searchDate + "%') "
                + add_limit;
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                RideBean rb = new RideBean();
                rb.setName(rset.getString("name"));
                rb.setMobile_no(rset.getString("mobile_no"));
                rb.setRating(rset.getString("rating"));
                rb.setBooking_no(rset.getString("booking_no"));
                rb.setVehicle_no(rset.getString("vehicle_no"));
                rb.setDriver_name(rset.getString("driver_name"));
                rb.setRide_date(rset.getString("ride_date"));
                rb.setRide_time(rset.getString("ride_time"));
                rb.setRemark(rset.getString("remark"));
                rb.setFeedback(rset.getString("feedback"));
                list.add(rb);
            }
        } catch (Exception e) {
            System.out.println("error in showData() RideModel");
        }
        return list;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (Exception e) {
            System.out.println("RideModel closeConnection() Error: " + e);
        }
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

    public List<String> getName(String q, String mobileNo, String booking) {
        List<String> list = new ArrayList<String>();
        int count = 0;
        String query = "SELECT name FROM user as u "
                + " GROUP BY name";
        PreparedStatement pstmt;
        try {
            pstmt = connection.prepareStatement(query);
            ResultSet rset = pstmt.executeQuery();
            q = q.trim();
            while (rset.next()) {
                String tarrif_code = rset.getString("name");
                if (tarrif_code.toUpperCase().startsWith(q.toUpperCase())) {
                    list.add(tarrif_code);
                    count++;
                }
            }
            if (count == 0) {
                list.add("No  Name Found!");
            }
        } catch (Exception e) {
            System.out.println("Error in RideModel getName " + e);
        }
        return list;
    }

    public List<String> getMobileNo(String q, String name, String booking) {
        List<String> list = new ArrayList<String>();
        int count = 0;
        String query = "SELECT mobile_no FROM user as u "
                + " GROUP BY mobile_no";
        PreparedStatement pstmt;
        try {
            pstmt = connection.prepareStatement(query);
            ResultSet rset = pstmt.executeQuery();
            q = q.trim();
            while (rset.next()) {
                String tarrif_code = rset.getString("mobile_no");
                if (tarrif_code.toUpperCase().startsWith(q.toUpperCase())) {
                    list.add(tarrif_code);
                    count++;
                }
            }
            if (count == 0) {
                list.add("No  Mobile Number Found!");
            }
        } catch (Exception e) {
            System.out.println("Error in RideModel getMobileNo " + e);
        }
        return list;
    }

    public List<String> getBookingNo(String q, String name, String mobileNo) {
        List<String> list = new ArrayList<String>();
        int count = 0;
        String query = "SELECT rfa.ride_from_app_id as booking_no"
                + " FROM ride_from_app as rfa WHERE rfa.active='Y' GROUP BY booking_no";
        PreparedStatement pstmt;
        try {
            pstmt = connection.prepareStatement(query);
            ResultSet rset = pstmt.executeQuery();
            q = q.trim();
            while (rset.next()) {
                String booking_no = rset.getString("booking_no");
                if (booking_no.toUpperCase().startsWith(q.toUpperCase())) {
                    list.add(booking_no);
                    count++;
                }
            }
            if (count == 0) {
                list.add("NO booking no Found!");
            }
        } catch (Exception e) {
            System.out.println("Error in RideModel getBookingNo " + e);
        }
        return list;
    }
}
