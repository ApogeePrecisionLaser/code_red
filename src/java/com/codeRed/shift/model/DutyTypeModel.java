/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.codeRed.shift.model;

import com.codeRed.shift.tableClasses.DutyType;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Navitus1
 */
public class DutyTypeModel
{
    private Connection connection;
    private String driverClass;
    private String connectionString;
    private String db_username;
    private String db_password;
    private String message;
    private String msgBgColor;
    private final String COLOR_OK = "yellow";
    private final String COLOR_ERROR = "red";
    public List getduty_typeName(String q)
    {
        List<String> list = new ArrayList<String>();
        String query = "select duty_type from duty_type order by duty_type ";
        try
        {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            int count = 0;
            q = q.trim();
            while (rs.next())
            {
                list.add(rs.getString("duty_type"));
                count++;
            }
            if (count == 0)
            {
                list.add("No such duty_type exists.");
            }
        }
        catch (Exception e)
        {
            System.out.println("getduty_typeName ERROR inside Model - " + e);
        }
        return list;
    }
    public List<DutyType> ShowData(int lowerLimit, int noOfRowsToDisplay,String searchitem_name)
    {
        List<DutyType> list = new ArrayList<DutyType>();
        String query = "SELECT * FROM duty_type where if('"+searchitem_name+"'='', duty_type LIKE '%%', duty_type='"+searchitem_name+"')"
                + " ORDER BY duty_type LIMIT "+lowerLimit+","+noOfRowsToDisplay;
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next())
            {
                DutyType itemNameBean=new DutyType();
                itemNameBean.setDuty_type(rs.getString("duty_type"));
                itemNameBean.setRemark(rs.getString("remark"));
                itemNameBean.setDuty_type_id(rs.getInt("duty_type_id"));
                list.add(itemNameBean);
            }
        } catch (Exception e)
        {
            System.out.println("getFuseType ERROR inside SurveyModel - " + e);
        }
        return list;
    }
    public int getNoOfRows(String searchitem_name)
    {
        int count=0;
       String query = "SELECT count(*) as count FROM duty_type where if('"+searchitem_name+"'='', duty_type LIKE '%%', duty_type='"+searchitem_name+"')";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next())
            {
                count=rs.getInt("count");
            }
        } catch (Exception e) {
            System.out.println("getNoOfRows ERROR inside Model - " + e);
        }
        return count;
    }
    public boolean insertRecord(DutyType itemNameBean) {
    boolean b=false;
        String query = "insert into duty_type (duty_type,remark) values (?,?)";
        try {
             PreparedStatement ps=(PreparedStatement) connection.prepareStatement(query);
             ps.setString(1,itemNameBean.getDuty_type());
             ps.setString(2,itemNameBean.getRemark());
             int rowsAffected=ps.executeUpdate();
             if (rowsAffected > 0) {
            message = "Record saved successfully.";
            msgBgColor = COLOR_OK;
        } else {
            message = "Cannot save the record, some error.";
            msgBgColor = COLOR_ERROR;
        }
        } catch (Exception e) {
             message = "Cannot save the record, some error.";
            msgBgColor = COLOR_ERROR;
            System.out.println("getFuseType ERROR inside SurveyModel - " + e);
        }
        return b;
    }
    public boolean UpdateRecord(DutyType itemNameBean)
    {
        String query = "update duty_type set duty_type='"+itemNameBean.getDuty_type()+"',remark='"+itemNameBean.getRemark()+"' where duty_type_id="+itemNameBean.getDuty_type_id()+"";
        try {
             PreparedStatement ps=(PreparedStatement) connection.prepareStatement(query);
             int rowsAffected=ps.executeUpdate();
             if (rowsAffected > 0) {
            message = "Record Update successfully.";
            msgBgColor = COLOR_OK;
        } else {
            message = "Cannot update the record, some error.";
            msgBgColor = COLOR_ERROR;
        }
        } catch (Exception e) {
             message = "Cannot update the record, some error.";
            msgBgColor = COLOR_ERROR;
            System.out.println("UpdateRecord ERROR inside Model - " + e);
        }
        boolean b=false;
        return b;
    }
    public boolean deleteRecord(DutyType itemNameBean) {
        boolean b=false;
        String query = "DELETE FROM duty_type where duty_type_id=?";
        try {
             PreparedStatement ps=(PreparedStatement) connection.prepareStatement(query);
             ps.setInt(1,itemNameBean.getDuty_type_id());
             int rowsAffected=ps.executeUpdate();
        if (rowsAffected > 0) {
            message = "Record deleted successfully.";
            msgBgColor = COLOR_OK;
        } else {
            message = "Cannot delete the record, some error.";
            msgBgColor = COLOR_ERROR;
        }

        } catch (Exception e) {
             message = "Cannot delete the record, some error.";
            msgBgColor = COLOR_ERROR;
            System.out.println("deleteRecord ERROR inside Model - " + e);
        }
         return b;
    }
        public String getMessage() {
        return message;
    }

    public String getMsgBgColor() {
        return msgBgColor;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public String getDb_password() {
        return db_password;
    }


    public void setDb_password(String db_password) {
        this.db_password = db_password;
    }

    public String getDb_username() {
        return db_username;
    }

    public void setDb_username(String db_username) {
        this.db_username = db_username;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }
}
