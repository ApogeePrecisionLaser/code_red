<%--
    Document   : before_login_home
    Created on : Nov 3, 2012, 1:12:23 PM
    Author     : Neha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Code Red</title>
        <link href="style/style.css" type="text/css" rel="stylesheet" media="Screen"/>
        <link href="style/Table_content.css" type="text/css" rel="stylesheet" media="Screen"/>
        <link href="style/login.css" type="text/css" rel="stylesheet" media="Screen"/>
    </head>
    <body>
        <table align="center"  class="main" cellpadding="0" cellspacing="0" >
            <tr>
                <td><%@include file="/layout/header.jsp" %></td>
            </tr>
     
            <tr>
                <td>
                    <div class="maindiv" id="body" >
                        <div id="login">
                            <h2>Login </h2>
                            <div id="loginborder"></div>
                            <form  action="loginCont.do" method="post">
                                <fieldset>
                                    <p>
                                        <span>Mobile Number</span>
                                        <input class="text" id="name" name="user_name" type="text" class="text" placeholder="Enter Mobile Number.." required/>
                                    </p>
                                    <p>
                                        <span >Password</span>
                                        <input id="password" name="password" class="text" type="password" placeholder="Enter Password.." required/>
                                    </p>
                                    <p>
                                        <span style="background-color:${msgColor}">${message}</span>
                                    </p>
                                    <p>
                                        <input  type="submit" class="button1" name="task" id="login12" value="login"/>
                                    </p>
                                </fieldset>
                            </form>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td><%@include file="/layout/footer.jsp" %></td>
            </tr>
        </table>
    </body>
</html>



