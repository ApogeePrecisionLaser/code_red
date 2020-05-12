/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeRed.general.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author jpss
 */
public class GCMNotificationlClass {

    public static String sendGCMData(String key, String jsonArray, String message) {
        String status = "OK";
        try {
            //System.out.println(" distance calculate start ");
            URL url = new URL("https://gcm-http.googleapis.com/gcm/send");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "key="+key);//Authorization: key=AIzaSyD2qygEvzwwGo0g1wmGKIFmFBtK69TrdQ0
            connection.setRequestMethod("POST");

            JSONObject jsonObject = new JSONObject();
            JSONObject msgJson = new JSONObject();
            jsonObject.put("registration_ids", new JSONArray(jsonArray));
            msgJson.put("message", URLEncoder.encode(message, "UTF-8"));
            jsonObject.put("data", msgJson);

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(jsonObject.toString());
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String mapData = "";
            String tempData = "";
            tempData = in.readLine();
            while (tempData != null) {
                mapData = mapData + tempData;
                tempData = in.readLine();
            }

            JSONTokener tokener = new JSONTokener(mapData);
            JSONObject top = new JSONObject(tokener);
            System.out.println(" sendGCMData Response data " + top);

            //System.out.println("\nCrunchify REST Service Invoked Successfully.. : " + mapData);
            in.close();
        } catch (Exception e) {
            System.out.println("\nError in GCMNotificationlClass");
            System.out.println(e);
        }
        System.out.println(" GCMNotificationlClass status : " + status);
        return status;
    }
}
