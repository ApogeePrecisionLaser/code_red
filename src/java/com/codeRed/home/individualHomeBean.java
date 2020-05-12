/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.codeRed.home;

/**
 *
 * @author Manpreet
 */
public class individualHomeBean {
   private int message_id;
   private String message;
   private int keyperson_id;
   private String keyperson_name;
   private String subject;

   //  fields from displayMsg
  private String link;
  private String msg_date_time;
  private int general_image_details_id;
  private String image_name;
  private  String image_path;
  private  String file_path;
  private  String attachment_file;
  private  String attachment_image;

    public String getAttachment_file() {
        return attachment_file;
    }

    public void setAttachment_file(String attachment_file) {
        this.attachment_file = attachment_file;
    }

    public String getAttachment_image() {
        return attachment_image;
    }

    public void setAttachment_image(String attachment_image) {
        this.attachment_image = attachment_image;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

  public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

  public String getMsg_date_time() {
        return msg_date_time;
    }

    public void setMsg_date_time(String msg_date_time) {
        this.msg_date_time = msg_date_time;
    }

    public int getGeneral_image_details_id() {
        return general_image_details_id;
    }

    public void setGeneral_image_details_id(int general_image_details_id) {
        this.general_image_details_id = general_image_details_id;
    }

  public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
   
    public String getKeyperson_name() {
        return keyperson_name;
    }

    public void setKeyperson_name(String keyperson_name) {
        this.keyperson_name = keyperson_name;
    }

    public int getKeyperson_id() {
        return keyperson_id;
    }

    public void setKeyperson_id(int keyperson_id) {
        this.keyperson_id = keyperson_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }


}
