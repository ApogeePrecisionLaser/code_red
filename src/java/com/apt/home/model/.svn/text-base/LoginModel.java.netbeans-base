/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apt.home.model;

import com.apt.tableClasses.log.LoginBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JPSS
 */
public class LoginModel {

    private static Connection connection;
    private String message;
    private String msgBgColor;
    private final String COLOR_OK = "yellow";
    private final String COLOR_ERROR = "red";
    public static String role = "";
      private int user_id;  

    public boolean setUserFullDetail(String myUserName, String myUserPass) {
        int id=0;
        String query = " SELECT user_login_id,role FROM user_login as u where u.mobile_no=? AND u.password=? ";
        try
        {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, myUserName);
            pst.setString(2, myUserPass);
            ResultSet rst = pst.executeQuery();
            while (rst.next())
            {
                id=rst.getInt("user_login_id");
                role=rst.getString("role");
            }
        } catch (Exception e)
        {
            System.out.println("LoginModel getUseName() Error: " + e);
        }
        return id==0?false:true;
    }
     public int iSUserExist(String user_name, String password) {
        int count = 0;
        try {
            String query = "Select count(*) AS count, key_person_id  from user_login as u where u.mobile_no =? and u.password =? ";
            PreparedStatement psmt = connection.prepareStatement(query);
            psmt.setString(1, user_name);
            psmt.setString(2, password);
            ResultSet rset = psmt.executeQuery();
            if (rset.next()) {
                count = rset.getInt("count");
                user_id = rset.getInt("key_person_id");
            }
        } catch (Exception e) {
            System.out.println("Error LoginModel  iSUserExist :" + e);
        }
        if (count == 0) {
            message = "Invalid User name or password";
            msgBgColor = COLOR_ERROR;
        }
        return user_id;
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
   public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    public void connectionClose() throws SQLException {
        connection.close();
    }
}
