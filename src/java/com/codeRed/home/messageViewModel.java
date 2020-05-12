/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeRed.home;

import com.codeRed.general.model.GCMNotificationlClass;
import com.codeRed.util.GetDate;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.fileupload.FileItem;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Manpreet
 */
public class messageViewModel {

    private Connection connection;
    private String message;
    private String msgBgColor;
    private final String COLOR_OK = "lightyellow";
    private final String COLOR_ERROR = "red";

    public void setConnection(Connection con) {
        try {
            connection = con;
        } catch (Exception e) {
            System.out.println("messageViewModel setConnection() Error: " + e);
        }
    }

    public int getNoOfRows() {
        int noOfRows = 0;
        try {
            ResultSet rset = connection.prepareStatement("SELECT COUNT(msg_id) as count FROM displaymsg ").executeQuery();
            if (rset.next()) {
                noOfRows = rset.getInt("count");
            }
        } catch (Exception e) {
            System.out.println("messageViewModel getNoOfRows() Error: " + e);
        }
        return noOfRows;
    }

    public List<individualHomeBean> showData(int lowerLimit, int noOfRowsToDisplay) {
        List<individualHomeBean> list = new ArrayList<individualHomeBean>();
        String addQuery = " LIMIT " + lowerLimit + ", " + noOfRowsToDisplay;
        if (lowerLimit == -1) {
            addQuery = "";
        }

        String query = "SELECT m.msg_id, m.body, m.subject,m.msg_date_time, m.link, "
                + " k.key_person_id, k.key_person_name "
                + " FROM displaymsg AS m, key_person AS k "
                + " WHERE m.key_person_id = k.key_person_id "
                + " ORDER BY m.msg_date_time Desc " // m.message_id Desc
                + addQuery;
        try {
            ResultSet rset = connection.prepareStatement(query).executeQuery();
            while (rset.next()) {
                individualHomeBean homeBean = new individualHomeBean();
                homeBean.setMessage_id(rset.getInt("msg_id"));
                homeBean.setMessage(rset.getString("body"));
                homeBean.setSubject(rset.getString("subject"));
                homeBean.setLink(rset.getString("link"));
                homeBean.setKeyperson_id(rset.getInt("key_person_id"));
                homeBean.setKeyperson_name(rset.getString("key_person_name"));
                String msg_date_time = rset.getString("msg_date_time");
                msg_date_time = msg_date_time.split(" ")[0];
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                Date d = sdf1.parse(msg_date_time);
                DateFormat df = new SimpleDateFormat("EEEE");
                String day = df.format(d);
                msg_date_time = msg_date_time + "(" + day + ")";
                homeBean.setMsg_date_time(msg_date_time);

                String subQuery1 = "SELECT count(general_image_details_id) as count_file "
                        + " FROM general_image_details AS g WHERE image_destination_id=3 AND key_person_id = " + rset.getInt("msg_id");
                ResultSet rs1 = connection.prepareStatement(subQuery1).executeQuery();
                while (rs1.next()) {
                    int count_file = rs1.getInt("count_file");
                    if (count_file > 0) {
                        homeBean.setAttachment_file("Yes");
                    } else {
                        homeBean.setAttachment_file("No");
                    }
                }

                String subQuery2 = "SELECT count(general_image_details_id) as count_image "
                        + " FROM general_image_details AS g WHERE image_destination_id=4 AND key_person_id = " + rset.getInt("msg_id");
                ResultSet rs2 = connection.prepareStatement(subQuery2).executeQuery();
                while (rs2.next()) {
                    int count_image = rs2.getInt("count_image");
                    if (count_image > 0) {
                        homeBean.setAttachment_image("Yes");
                    } else {
                        homeBean.setAttachment_image("No");
                    }
                }

                list.add(homeBean);
            }
        } catch (Exception e) {
            System.out.println("messageViewModel showData() Error: " + e);
        }
        return list;
    }

    public List<individualHomeBean> showData1(int idVal) {
        List<individualHomeBean> list = new ArrayList<individualHomeBean>();

//        String query = "SELECT m.msg_id, m.body, m.subject,m.msg_date_time, m.link, "
//                + " k.key_person_id, k.key_person_name, g.general_image_details_id, g.image_name "
//                + " FROM displaymsg AS m, key_person AS k, general_image_details AS g "
//                + " WHERE m.key_person_id = k.key_person_id and k.active='Y' and g.key_person_id = m.msg_id "   // g.key_person_id = k.key_person_id
//                + " and m.msg_id = " + idVal;

        String query = "SELECT m.msg_id, m.body, m.subject,m.msg_date_time, m.link, "
                + " k.key_person_id, k.key_person_name "
                + " FROM displaymsg AS m, key_person AS k "
                + " WHERE m.key_person_id = k.key_person_id  "
                + " and m.msg_id = " + idVal;

        try {
            ResultSet rset = connection.prepareStatement(query).executeQuery();
            while (rset.next()) {
                individualHomeBean homeBean = new individualHomeBean();
                homeBean.setMessage_id(rset.getInt("msg_id"));
                homeBean.setMessage(rset.getString("body"));
                homeBean.setSubject(rset.getString("subject"));
                homeBean.setLink(rset.getString("link"));
                homeBean.setKeyperson_id(rset.getInt("key_person_id"));
                homeBean.setKeyperson_name(rset.getString("key_person_name"));
                String msg_date_time = rset.getString("msg_date_time");
                msg_date_time = msg_date_time.split(" ")[0];
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                Date d = sdf1.parse(msg_date_time);
                DateFormat df = new SimpleDateFormat("EEEE");
                String day = df.format(d);
                msg_date_time = msg_date_time + "(" + day + ")";
                homeBean.setMsg_date_time(msg_date_time);
//                homeBean.setGeneral_image_details_id(rset.getInt("general_image_details_id"));
//                homeBean.setImage_name(rset.getString("image_name"));
                list.add(homeBean);
            }
        } catch (Exception e) {
            System.out.println("messageViewModel showData1() Error: " + e);
        }
        return list;
    }

    public List<individualHomeBean> getImageNameList(int idVal) {
        List<individualHomeBean> list = new ArrayList<individualHomeBean>();
        String query = "SELECT g.general_image_details_id, g.image_name "
                + " FROM general_image_details AS g "
                + " WHERE g.image_destination_id = 8 and g.key_person_id = " + idVal;

        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
                individualHomeBean homeBean = new individualHomeBean();
                homeBean.setGeneral_image_details_id(rs.getInt("general_image_details_id"));
                homeBean.setImage_name(rs.getString("image_name"));
                list.add(homeBean);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: in messageViewModel in getImageNameList : " + ex);
        }
        return list;
    }

    public List<individualHomeBean> getFileNameList(int idVal) {
        List<individualHomeBean> list = new ArrayList<individualHomeBean>();
        String query = "SELECT g.general_image_details_id, g.image_name "
                + " FROM general_image_details AS g "
                + " WHERE g.image_destination_id = 7 and g.key_person_id = " + idVal;

        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
                individualHomeBean homeBean = new individualHomeBean();
                homeBean.setGeneral_image_details_id(rs.getInt("general_image_details_id"));
                homeBean.setImage_name(rs.getString("image_name"));
                list.add(homeBean);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: in messageViewModel in getFileNameList : " + ex);
        }
        return list;
    }

    public String getAttachment_destination(int g_id, String image_name) {
        String destination_path = "";
        String query = " SELECT destination_path "
                + " FROM general_image_details AS g, image_destination As i "
                + " WHERE g.image_destination_id = i.image_destination_id "
                + " and g.general_image_details_id = " + g_id + " And g.image_name = '" + image_name + "'";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            if (rs.next()) {
                destination_path = rs.getString("destination_path");    // C:\association_repository\displaymsg\
            }
        } catch (Exception ex) {
            System.out.println("ERROR: in messageViewModel in getAttachment_destination : " + ex);
        }
        return destination_path;
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
            System.out.println("error in getServerKey() in messageViewModel : " + ex);
        }
        return key;
    }

    public JSONArray getDriverTokenId(int key_person_id) { // String key_person_id
        JSONArray jsonArray = new JSONArray();
//        String query = "SELECT token_id FROM token_data t WHERE IF('" + key_person_id + "'='', t.key_person_id LIKE '%%', t.key_person_id='" + key_person_id + "')";
        String query = "SELECT token_id FROM token_data t";   // WHERE key_person_id!= " + key_person_id;
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
                jsonArray.add(rs.getString("token_id"));
            }
        } catch (Exception ex) {
            System.out.println("error in getDriverTokenId() in messageViewModel : " + ex);
        }
        return jsonArray;
    }

    public int insertRecord(individualHomeBean homeBean, Iterator itr, String file_destination, String image_destination) {
        int id = 0;
//        DateFormat dateFormat1 = new SimpleDateFormat("dd.MMMMM.yyyy");
//        DateFormat dateFormat = new SimpleDateFormat("dd.MMMMM.yyyy/ hh:mm:ss aaa");
//        Date date = new Date();
//        String current_date = dateFormat.format(date);
        String query = "INSERT INTO displaymsg(body,key_person_id,msg_date_time,subject,link) VALUES(?,?,concat(current_date, concat(' ', current_time)),?,?)";//message_id,
        String imageQuery = "INSERT INTO general_image_details (image_name, image_destination_id, date_time, description,key_person_id) "
                + " VALUES(?, ?, ?, ?,?)";
        int rowsAffected = 0;
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            // pstmt.setInt(1, homeBean.getMessage_id());
            pstmt.setString(1, homeBean.getMessage());
            pstmt.setInt(2, homeBean.getKeyperson_id());
//            pstmt.setString(3, current_date);
            pstmt.setString(3, homeBean.getSubject());
            pstmt.setString(4, homeBean.getLink());
            rowsAffected = pstmt.executeUpdate();

//            rowsAffected = insertImageRecord(imageName, image_uploaded_for, current_date, homeBean.getKeyperson_id());
            if (rowsAffected > 0) {
                try {
                    individualHomeModel model = new individualHomeModel();
                    model.setConnection(connection);
                    String keypersonData = model.getkeypersonName(homeBean.getKeyperson_id());
                    String keypersonName = keypersonData.split("#")[0];
                    String message = "";
                    if(homeBean.getLink().isEmpty() || homeBean.getLink()==null){
                        message = "Subject: " + homeBean.getSubject() + " \n\n "
                            + " Message: " + homeBean.getMessage() + " \n\n "
                            + " Message From: " + keypersonName;
                    } else{
                        message = "Subject: " + homeBean.getSubject() + " \n\n "
                            + " Message: " + homeBean.getMessage() + " \n\n "
                            + " Link: " + homeBean.getLink() + " \n\n "
                            + " Message From: " + keypersonName;
                    }

//                    model.sendSmsToAllKeypersons(homeBean.getKeyperson_id(), keypersonName, message);
                } catch (Exception e) {
                    System.out.println("messageViewModel insertRecord() sendMsg block error - " + e);
                }

                GCMNotificationlClass.sendGCMData(getServerKey("CodeRedTeam"), getDriverTokenId(homeBean.getKeyperson_id()).toString(), homeBean.getSubject());

                ResultSet rst = pstmt.getGeneratedKeys();

                if (rst.next()) {
                    id = rst.getInt(1);
                }
                int r_no = 0;
                String image_uploaded_for = "message";
//                DateFormat dateFormat = new SimpleDateFormat("dd.MMMMM.yyyy/ hh:mm:ss aaa");
//                Date date = new Date();
//                String current_date = dateFormat.format(date);
                String[] file_name = {homeBean.getFile_path(), homeBean.getImage_path()};

                for (int i = 0; i < file_name.length; i++) {
//
                    String tempExt = file_name[i];
                    String p_destination = "";
                    p_destination = file_destination;
//
//                    if (!tempExt.isEmpty()) {
//                        String middleName = "";
//                        String p_destination = "";
//                        String fieldName = "";
//                        if (tempExt.equals(file_name[i])) {
//                            middleName = "pic";
//                            p_destination = file_destination;
//                            fieldName = "img_name";
//
//                        }


                    //                 else if(tempExt.equals(image_name[1])){
                    //                     middleName = "proof";
                    //                      p_destination = iD_destination;
//                        fieldName = "id_proof";
//                        image_uploaded_for = "key_person_ID";
                    //                  }
//                    String img_name = imageExist(image_uploaded_for, key_person_id);
//                    String rev = "";
//                    if(!img_name.isEmpty()){
//                        img_name = img_name.replace(".", "#");
//                        String imgArray = img_name.split("#")[0];
//                        String[] id = imgArray.split(key_person_id+"");
//                        if(id.length < 2)
//                            rev = "_1";
//                        else{
//                            id[1] = id[1].replace("_", "");
//                            rev = "_" + (Integer.parseInt(id[1]) + 1);
//                        }
//
//                   }
//
                    int index = tempExt.lastIndexOf(".");
                    int index1 = tempExt.length();
                    String Extention = tempExt.substring(index + 1, index1);
                    tempExt = "." + Extention;
//                        String imageName = homeBean.getImage_path() + "_" + id;
//                         rowsAffected = insertImageRecord(imageName, image_uploaded_for, current_date, id);
                    if (rowsAffected > 0) {
                        WirteImage(itr, p_destination, id);
                    }


                }
            }
        } catch (Exception e) {
            System.out.println("messageViewModel insertRecord() Error: " + e);
        }
        if (rowsAffected > 0) {
            message = "Message Saved Successfully";
            msgBgColor = COLOR_OK;
            System.out.println("Record inserted successfully in message");
        } else {
            message = "Cannot Save Message, Some Error";
            msgBgColor = COLOR_ERROR;
            System.out.println("Record not saved in message");
        }
        return rowsAffected;
    }

    public void WirteImage(Iterator itr, String destination, int id) {
        int count = 1;
        String tempExt = "";
        String image_uploaded_for = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MMMMM-dd/ hh:mm:ss aaa");
        Date date = new Date();
        String current_date = dateFormat.format(date);
        String save_date = current_date.split("/")[0];
        int year = GetDate.getCurrentYear();
        String mon = GetDate.getCurrentMonth();
        String month = GetDate.getFullMonthName(mon);
        try {
            destination = destination + year + "/" + month + "/" + save_date + "/" + id;
            makeDirectory(destination);
            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();
                try {
                    if (!item.isFormField()) {
                        String image = item.getName();
                        if (image.isEmpty()) {
                            continue;
                        }
                        String field_name = item.getFieldName();
                        if (field_name.equals("file_name")) {
                            image_uploaded_for = "file";
                        } else {
                            image_uploaded_for = "image";
                        }
                        tempExt = image;
                        int index = tempExt.lastIndexOf(".");
                        int index1 = tempExt.length();
                        String Extention = tempExt.substring(index + 1, index1);
                        tempExt = "." + Extention;
                        String imageName1 = save_date + "_" + id + "_" + count + tempExt;
                        insertImageRecord(imageName1, image_uploaded_for, current_date, id);
                        //if (item.getFieldName().equals(fieldName)) {
                        File file = new File(destination + "\\" + imageName1);//imageName
//                            File file = new File(imageName1);
                        if (image.isEmpty() || image.equals(destination)) {
                        } else {
                            item.write(file);
                            message = "Image Uploaded Successfully.";
                            //  updateImageName(imageName,id);
                            count++;
                        }
                        //break;
                        //}
                    }
                } catch (Exception e) {
                    System.out.println("Image upload error: " + e);
                }
            }
        } catch (Exception ex) {
        }
    }

    public boolean makeDirectory(String dirPathName) {
        boolean result = false;
        System.out.println("dirPathName---" + dirPathName);
        //dirPathName = "C:/ssadvt/sor/organisation" ;
        File directory = new File(dirPathName);
        if (!directory.exists()) {
            try {
                result = directory.mkdirs();
            } catch (Exception e) {
                System.out.println("messageViewModel Error - " + e);
            }
        }
        return result;
    }

    public int insertImageRecord(String image_name, String image_uploaded_for, String current_date, int kp_id) {
        int rowsAffected = 0;
        String imageQuery = "INSERT INTO general_image_details (image_name, image_destination_id, date_time, description,key_person_id) "
                + " VALUES(?, ?, ?, ?,?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(imageQuery);
            pstmt.setString(1, image_name);
            pstmt.setInt(2, getimage_destination_id(image_uploaded_for));
            pstmt.setString(3, current_date);
            pstmt.setString(4, "this image is for site");
            pstmt.setInt(5, kp_id);
            rowsAffected = pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            System.out.println("Error:messageViewModel--insertImageRecord-- " + e);
        }
        return rowsAffected;
    }

    public String getDestination_Path(String image_uploaded_for) {
        String destination_path = "";
        String query = " SELECT destination_path FROM image_destination id, image_uploaded_for  iuf "
                + " WHERE id.image_uploaded_for_id = iuf.image_uploaded_for_id "
                + " AND iuf.image_uploaded_for = '" + image_uploaded_for + "' ";
        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            if (rs.next()) {
                destination_path = rs.getString("destination_path");
            }
        } catch (Exception ex) {
            System.out.println("ERROR: in messageViewModel in getDestination_Path2 : " + ex);
        }
        return destination_path;
    }

    public int getimage_destination_id(String image_uploaded_for) {
        String query;
        int image_destination_id = 0;
        query = " SELECT image_destination_id, destination_path from image_destination AS id , image_uploaded_for As i "
                + " WHERE id.image_uploaded_for_id=i.image_uploaded_for_id AND i.image_uploaded_for= ? ";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, image_uploaded_for);
            ResultSet rset = pstmt.executeQuery();
            if (rset.next()) {
                String destination_path = rset.getString("destination_path");
                System.out.println(destination_path);
                rset.getInt("image_destination_id");
                System.out.println("id = " + rset.getInt("image_destination_id"));
                image_destination_id = rset.getInt("image_destination_id");
                System.out.println(image_destination_id);
            }

        } catch (Exception ex) {
            System.out.println("Error: messageViewModel-getimage_destination_id-" + ex);
        }
        return image_destination_id;
    }

// Web service methods -- starts here
    public JSONArray getBroadcastMsgs() {
        JSONArray jsonArray = new JSONArray();

        String query = "SELECT m.msg_id, m.body, m.subject,m.msg_date_time, m.link, "
                + " k.key_person_id, k.key_person_name "
                + " FROM displaymsg AS m, key_person AS k "
                + " WHERE m.key_person_id = k.key_person_id  "
                + " ORDER BY m.msg_date_time Desc ";
        try {
            ResultSet rset = connection.prepareStatement(query).executeQuery();
            while (rset.next()) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("msg_id", rset.getInt("msg_id"));
                jSONObject.put("body", rset.getString("body"));
                jSONObject.put("subject", rset.getString("subject"));
                String msg_date_time = rset.getString("msg_date_time");
                msg_date_time = msg_date_time.split(" ")[0];
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                Date d = sdf1.parse(msg_date_time);
                DateFormat df = new SimpleDateFormat("EEEE");
                String day = df.format(d);
                msg_date_time = msg_date_time + "(" + day + ")";
                jSONObject.put("msg_date_time", msg_date_time);
                jSONObject.put("link", rset.getString("link"));
                jSONObject.put("key_person_id", rset.getInt("key_person_id"));
                jSONObject.put("key_person_name", rset.getString("key_person_name"));

                String subQuery1 = "SELECT count(general_image_details_id) as count_file "
                        + " FROM general_image_details AS g WHERE image_destination_id=7 AND key_person_id = " + rset.getInt("msg_id");
                ResultSet rs1 = connection.prepareStatement(subQuery1).executeQuery();
                while (rs1.next()) {
                    int count_file = rs1.getInt("count_file");
                    if (count_file > 0) {
                        jSONObject.put("attachment_file", "Yes");
                    } else {
                        jSONObject.put("attachment_file", "No");
                    }
                }

                String subQuery2 = "SELECT count(general_image_details_id) as count_image "
                        + " FROM general_image_details AS g WHERE image_destination_id=8 AND key_person_id = " + rset.getInt("msg_id");
                ResultSet rs2 = connection.prepareStatement(subQuery2).executeQuery();
                while (rs2.next()) {
                    int count_image = rs2.getInt("count_image");
                    if (count_image > 0) {
                        jSONObject.put("attachment_image", "Yes");
                    } else {
                        jSONObject.put("attachment_image", "No");
                    }
                }

                jsonArray.add(jSONObject);
            }
        } catch (Exception e) {
            System.out.println("messageViewModel getBroadcastMsgs() Error: " + e);
        }
        return jsonArray;
    }

    public JSONArray getBroadcastFullMsg(int msg_id) {
        JSONArray jsonArray = new JSONArray();

        String query = "SELECT m.body, m.subject,m.msg_date_time, m.link, "
                + " k.key_person_id, k.key_person_name "
                + " FROM displaymsg AS m, key_person AS k "
                + " WHERE m.key_person_id = k.key_person_id  "
                + " And m.msg_id = " + msg_id;
        try {
            ResultSet rset = connection.prepareStatement(query).executeQuery();
            while (rset.next()) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("msg_id", msg_id);
                jSONObject.put("body", rset.getString("body"));
                jSONObject.put("subject", rset.getString("subject"));
                String msg_date_time = rset.getString("msg_date_time");
                msg_date_time = msg_date_time.split(" ")[0];
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                Date d = sdf1.parse(msg_date_time);
                DateFormat df = new SimpleDateFormat("EEEE");
                String day = df.format(d);
                msg_date_time = msg_date_time + "(" + day + ")";
                jSONObject.put("msg_date_time", msg_date_time);
                jSONObject.put("link", rset.getString("link"));
                jSONObject.put("key_person_id", rset.getInt("key_person_id"));
                jSONObject.put("key_person_name", rset.getString("key_person_name"));

                String subQuery1 = "SELECT g.image_name, (SELECT count(general_image_details_id) "
                        + " FROM general_image_details AS g WHERE image_destination_id=7 AND key_person_id = " + msg_id + ") as count_file "
                        + " FROM general_image_details AS g WHERE image_destination_id=7 AND key_person_id = " + msg_id;
                ResultSet rs1 = connection.prepareStatement(subQuery1).executeQuery();
                int count_file = 0, j = 0;
                JSONArray jsona = new JSONArray();
                while (rs1.next()) {
                    if (j == 0) {
                        count_file = rs1.getInt("count_file");
                        j++;
                    }
                    if (count_file > 0) {
                        jsona.add(rs1.getString("image_name"));
                    } else {
                        jsona.add("");
                    }
                }
                jSONObject.put("count_file", count_file);
                jSONObject.put("file_name", jsona);

                String subQuery2 = "SELECT g.image_name, (SELECT count(general_image_details_id) "
                        + " FROM general_image_details AS g WHERE image_destination_id=8 AND key_person_id = " + msg_id + ") as count_image "
                        + " FROM general_image_details AS g WHERE image_destination_id=8 AND key_person_id = " + msg_id;
                ResultSet rs2 = connection.prepareStatement(subQuery2).executeQuery();
                int count_image = 0, i = 0;
                JSONArray jsona1 = new JSONArray();
                while (rs2.next()) {
                    if (i == 0) {
                        count_image = rs2.getInt("count_image");
                        i++;
                    }
                    if (count_image > 0) {
                        jsona1.add(rs2.getString("image_name"));
                    } else {
                        jsona1.add("");
                    }
                }
                jSONObject.put("count_image", count_image);
                jSONObject.put("image_name", jsona1);

                jsonArray.add(jSONObject);
            }
        } catch (Exception e) {
            System.out.println("messageViewModel getBroadcastFullMsg() Error: " + e);
        }
        System.out.println(jsonArray);
        return jsonArray;
    }

    public String getAttachmentPath(int msg_id, String image_name) {
        String attachment_destination = "";
        String query = "SELECT i.destination_path "
                + " FROM general_image_details AS g, image_destination As i "
                + " WHERE g.image_destination_id = i.image_destination_id "
                + " AND g.image_name = '" + image_name + "' and g.key_person_id = " + msg_id;

        try {
            ResultSet rset = connection.prepareStatement(query).executeQuery();
            while (rset.next()) {
                attachment_destination = rset.getString("destination_path");
            }

            String[] img = image_name.split("_");
            String date = img[0];
            String[] dt = date.split("-");
            String year = dt[0];
            String month = dt[1];
            String id = img[1];
            attachment_destination = attachment_destination.concat(year).concat("\\").concat(month).concat("\\").concat(date).concat("\\").concat(id).concat("\\").concat(image_name);

        } catch (Exception e) {
            System.out.println("messageViewModel getAttachmentPath() Error: " + e);
        }

        return attachment_destination;
    }

    public JSONArray getAttachmentFiles(int msg_id) {
        JSONArray jsonArray = new JSONArray();

        String query = "SELECT g.general_image_details_id, g.image_name, i.destination_path "
                + " FROM general_image_details AS g, image_destination As i "
                + " WHERE g.image_destination_id = i.image_destination_id "
                + " AND g.image_destination_id = 7 and g.key_person_id = " + msg_id;

        try {
            ResultSet rset = connection.prepareStatement(query).executeQuery();
            while (rset.next()) {
                JSONObject jSONObject = new JSONObject();
                String image_name = rset.getString("image_name");
                String attachment_destination = rset.getString("destination_path");
                String[] img = image_name.split("_");
                String date = img[0];
                String[] dt = date.split("-");
                String year = dt[0];
                String month = dt[1];
                String id = img[1];
                attachment_destination = attachment_destination.concat(year).concat("\\").concat(month).concat("\\").concat(date).concat("\\").concat(id).concat("\\").concat(image_name);

                jSONObject.put("general_image_details_id", rset.getInt("general_image_details_id"));
                jSONObject.put("image_name", image_name);
                jSONObject.put("attachment_destination", attachment_destination);
                jsonArray.add(jSONObject);
            }

        } catch (Exception e) {
            System.out.println("messageViewModel getAttachmentFiles() Error: " + e);
        }

        return jsonArray;
    }

    public int insertFileRecord(String image_name, int id, int image_upload_for) {
        int rowsAffected = 0;
        DateFormat dateFormat = new SimpleDateFormat("dd.MMMMM.yyyy/ hh:mm:ss aaa");
        Date date = new Date();
        String current_date = dateFormat.format(date);
        String imageQuery = "INSERT INTO general_image_details (image_name, image_destination_id, date_time, description,key_person_id) "
                + " VALUES(?, ?, ?, ?,?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(imageQuery);
            pstmt.setString(1, image_name);
            pstmt.setInt(2, image_upload_for);
            pstmt.setString(3, current_date);
            pstmt.setString(4, "audio which advocate dictates");
            pstmt.setInt(5, id);
            rowsAffected = pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            System.out.println("Error:messageViewModel--insertRecord-- " + e);
        }

        return rowsAffected;
    }

// Web service methods -- ends here
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            } else {
                System.out.println("messageViewModel closeConnection() : Connection already closed ");
            }
        } catch (Exception e) {
            System.out.println("messageViewModel closeConnection() Error: " + e);
        }
    }

    public String getMessage() {
        return message;
    }

    public String getMsgBgColor() {
        return msgBgColor;
    }
}
