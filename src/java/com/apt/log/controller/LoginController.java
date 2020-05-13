/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apt.log.controller;

import com.apt.home.model.LoginModel;
import com.apt.tableClasses.log.LoginBean;
import com.codeRed.dbCon.DBConnection;
import java.sql.Connection;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Shubham
 */
public class LoginController extends HttpServlet {

    List<LoginBean> list = null;
    HttpSession sess = null;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try
        {
            LoginModel loginModel = new LoginModel();
            String connString = null;
            String db_name = null;
            sess = request.getSession(false);
            ServletContext ctx = getServletContext();
            String MyUserName = "", myUserPass = "";
            loginModel.setConnection((Connection) DBConnection.getConnectionForUtf(ctx));
            String task = request.getParameter("task");
            if (task == null) {
                task = "";
            } else if (task.equals("login"))
            {
                MyUserName = request.getParameter("user_name").trim();
                myUserPass = (String) request.getParameter("password");
                if(loginModel.setUserFullDetail(MyUserName, myUserPass)){
                    sess = request.getSession(true);
                    request.setAttribute("role", LoginModel.role);
                     int user_id = loginModel.iSUserExist(MyUserName, myUserPass);
                    if (user_id > 0) {

                       sess.setAttribute("user_id", user_id);
                         request.getRequestDispatcher("/afterLoginHomeView").forward(request, response);
                    }else{}
                  
                }
                else
                {
                    request.setAttribute("message", "Login Failed !! \n Please Check Login Credential");
                    request.setAttribute("msgColor", "RED");
                    request.getRequestDispatcher("/beforeLoginPage").forward(request, response);
                }
                loginModel.connectionClose();
                return;
            }
            if (sess != null && task.equals("logout"))
            {
                sess.invalidate();
                request.getRequestDispatcher("/beforeLoginPage").forward(request, response);
            } 

        } catch (Exception e) {
            System.out.println("Error LoginController" + e);
            request.getRequestDispatcher("/beforeLoginPage").forward(request, response);
            return;
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }
}
