/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeRed.userModel;

import com.codeRed.userTable.UserBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Shubham
 */
public class UserModel {

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

    public int getNoOfRows(String searchName, String searchMobileNo, String searchEmailId, String searchDate) {
        int noOfRows = 0;
        String query = "Select count(*) "
                + " FROM user as u where"
                + " IF('" + searchName + "'='', name LIKE '%%',name = '" + searchName + "') "
                + " AND IF('" + searchMobileNo + "'='', mobile_no LIKE '%%',mobile_no = '" + searchMobileNo + "') "
                + " AND IF('" + searchEmailId + "'='', email_id LIKE '%%',email_id = '" + searchEmailId + "') "
                + " AND IF('" + searchDate + "'='', created_date LIKE '%%',created_date Like '" + searchDate + "%') ";
        try {
            ResultSet rset = connection.prepareStatement(query).executeQuery();
            rset.next();
            noOfRows = Integer.parseInt(rset.getString(1));
        } catch (Exception e) {
            System.out.println("error in getNoOfRows TarrifModel");
        }
        return noOfRows;
    }

    public List<UserBean> showData(int lowerLimit, int noOfRowsToDisplay, String searchName, String searchMobileNo, String searchEmailId, String searchDate) {
        List<UserBean> list = new ArrayList<UserBean>();
        String add_limit = "  LIMIT " + lowerLimit + ", " + noOfRowsToDisplay;
        if (lowerLimit == -1) {
            add_limit = "";
        }

        String query = "Select name, mobile_no, email_id, mobile_no_verified,remark, gender, blood_group,created_date "
                + " FROM user as u where"
                + " IF('" + searchName + "'='', name LIKE '%%',name = '" + searchName + "') "
                + " AND IF('" + searchMobileNo + "'='', mobile_no LIKE '%%',mobile_no = '" + searchMobileNo + "') "
                + " AND IF('" + searchEmailId + "'='', email_id LIKE '%%',email_id = '" + searchEmailId + "') "
                + " AND IF('" + searchDate + "'='', created_date LIKE '%%',created_date Like '" + searchDate + "%') "
                + add_limit;
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                UserBean ub = new UserBean();
                ub.setName(rset.getString("name"));
                ub.setMobile_no(rset.getString("mobile_no"));
                ub.setEmail_id(rset.getString("email_id"));
                ub.setMobile_no_verified(rset.getString("mobile_no_verified"));
                ub.setRemark(rset.getString("remark"));
                ub.setGender(rset.getString("gender"));
                ub.setBlood_group(rset.getString("blood_group"));
                ub.setCreated_date(rset.getString("created_date"));
                list.add(ub);
            }
        } catch (Exception e) {
            System.out.println("error in showData() TarrifModel");
        }
        return list;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (Exception e) {
            System.out.println("TarrifModel closeConnection() Error: " + e);
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

    public List<String> getName(String q, String mobileNo, String email) {
        List<String> list = new ArrayList<String>();
        int count = 0;
        String query = "SELECT name FROM user as u "
                + " where if('" + mobileNo + "'='' , mobile_no like '%%', mobile_no='" + mobileNo + "') "
                + " AND if('" + email + "'='' , email_id like '%%', email_id='" + email + "')";
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
                list.add("Not Found!");
            }
        } catch (Exception e) {
            System.out.println("Error in TarrifModel getTariffCode" + e);
        }
        return list;
    }

    public List<String> getMobileNo(String q, String name, String email) {
        List<String> list = new ArrayList<String>();
        int count = 0;
        String query = "SELECT mobile_no FROM user as u "
                + " where if('" + name + "'='' , name like '%%', name='" + name + "') "
                + " AND if('" + email + "'='' , email_id like '%%', email_id='" + email + "')";
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
                list.add("Not Found!");
            }
        } catch (Exception e) {
            System.out.println("Error in TarrifModel getTariffCode" + e);
        }
        return list;
    }

    public List<String> getEmailId(String q, String name, String mobileNo) {
        List<String> list = new ArrayList<String>();
        int count = 0;
        String query = "SELECT email_id FROM user as u "
                + " where if('" + name + "'='' , name like '%%', name='" + name + "') "
                + " AND if('" + mobileNo + "'='' , mobile_no like '%%', mobile_no='" + mobileNo + "')";
        PreparedStatement pstmt;
        try {
            pstmt = connection.prepareStatement(query);
            ResultSet rset = pstmt.executeQuery();
            q = q.trim();
            while (rset.next()) {
                String tarrif_code = rset.getString("email_id");
                if (tarrif_code.toUpperCase().startsWith(q.toUpperCase())) {
                    list.add(tarrif_code);
                    count++;
                }
            }
            if (count == 0) {
                list.add("Not Found!");
            }
        } catch (Exception e) {
            System.out.println("Error in TarrifModel getTariffCode" + e);
        }
        return list;
    }
}
