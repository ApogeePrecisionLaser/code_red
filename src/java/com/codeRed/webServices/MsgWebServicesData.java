/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.codeRed.webServices;

import com.codeRed.dbCon.DBConnection;
import com.codeRed.home.messageViewModel;
import com.codeRed.webServices.model.UserAppWebServiceModel;
import com.mysql.jdbc.Connection;
import java.io.File;
import javax.servlet.ServletContext;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sf.json.JSONObject;

/**
 *
 * @author Ritesh
 */
@Path("/msgdata")
public class MsgWebServicesData {

     @Context
    ServletContext serveletContext;
    Connection connection = null;
    // web service for notification
    @POST
    @Path("/GCMData")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String GCMData(JSONObject json) {
        System.out.println("GCMData WebServices");
          UserAppWebServiceModel userAppWebServiceModel = new UserAppWebServiceModel();
        String key_person_id = json.get("key_person_id").toString();
        String token_id = json.get("token_id").toString();
        String gcm_server_key = json.get("gcm_server_key").toString();
        try {
            userAppWebServiceModel.setConnection((Connection) DBConnection.getConnection(serveletContext));
        } catch (Exception ex) {
            System.out.println("ERROR : in GCMData() in webServiceData : " + ex);
        }
        int affected = 0;
        int count = userAppWebServiceModel.validateTokenId(key_person_id, token_id);
        if (count == 0) {
            affected = userAppWebServiceModel.insertTokenId(key_person_id, token_id);
        } else if (count>0) {
            affected = userAppWebServiceModel.updateTokenId(key_person_id, token_id);
        } else{
            System.out.println("GCMData can't insert or update tokenId as count= "+ count);
        }
        if (affected > 0) {
            return "success";
        } else {
            return "fail";
        }
    }
     // web service for viewing broadcast multipart messages
    @POST
    @Path("/viewMultiPartMsg")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONObject getMultiPartMsgView() {
        JSONObject jsonObject = new JSONObject();
        messageViewModel msgModel = new messageViewModel();

        try {
            msgModel.setConnection((Connection) DBConnection.getConnection(serveletContext));
            jsonObject.put("data", msgModel.getBroadcastMsgs());

        } catch (Exception ex) {
            System.out.println("ERROR : in getMultiPartMsgView() in webServiceData : " + ex);
        }

        msgModel.closeConnection();
        return jsonObject;
    }
 // web service for viewing broadcast multipart full message
    @POST
    @Path("/viewFullMsg")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONObject getFullMsg(int msg_id) {
        JSONObject jsonObject = new JSONObject();
        messageViewModel msgModel = new messageViewModel();

        try {
            msgModel.setConnection((Connection) DBConnection.getConnection(serveletContext));
            jsonObject.put("data", msgModel.getBroadcastFullMsg(msg_id));

        } catch (Exception ex) {
            System.out.println("ERROR : in getFullMsg() in webServiceData : " + ex);
        }

        msgModel.closeConnection();
        return jsonObject;
    }
     @POST
    @Path("/sendAttachment")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendMsgAttachment(JSONObject json) {
        messageViewModel msgModel = new messageViewModel();
        Response.ResponseBuilder responseBuilder = null;
        try {
            msgModel.setConnection((Connection) DBConnection.getConnection(serveletContext));
            int msg_id = Integer.parseInt(json.get("msg_id").toString());
            String attachment = json.get("attachment").toString();
            String image_path = msgModel.getAttachmentPath(msg_id, attachment);
            File file = new File(image_path);
            responseBuilder = Response.ok((Object) file);
            responseBuilder.header("Content-Disposition", "attachment; filename=\" " + attachment + "\"");

        } catch (Exception ex) {
            System.out.println("ERROR : in sendMsgAttachment() in webServiceData : " + ex);
        }

        msgModel.closeConnection();
        return responseBuilder.build();
    }


}
