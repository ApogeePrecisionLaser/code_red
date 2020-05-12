<%-- 
    Document   : after_login_home
    Created on : Dec 5, 2012, 5:20:41 PM
    Author     : Neha
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Meters Table</title>
        <link href="style/style.css" type="text/css" rel="stylesheet" media="Screen"/>
        <link href="style/Table_content.css" type="text/css" rel="stylesheet" media="Screen"/>
        <link href="style/login.css" type="text/css" rel="stylesheet" media="Screen"/>
        <style>
            .button {font-size: 35px;
                     display: inline-block;}
            </style>
        </head>
        <table align="center"  class="main" cellpadding="0" cellspacing="0" >            <%--DWLayoutDefaultTable--%>
        <tr>
            <td><%@include file="/layout/header.jsp" %></td>
        </tr>
        <tr>
            <c:if test="${role eq 'admin'}">
                <td><%@include file="/layout/menu.jsp" %></td>
            </c:if>
            <c:if test="${role eq 'clerk'}">
                <td><%@include file="/layout/menu_1.jsp" %></td>
            </c:if>
        </tr>
        <tr>

        </tr>
        <tr>
            <td>
                <br/>
                <br/>
                <div class="maindiv" id="body">
                    <div id="" align="center">
                        <TABLE  align="center">
                            <c:forEach var="list1" items="${requestScope['list']}" varStatus="loopCounter">
                                <TR>
                                    <td>
                                        <a href="loginCont.do?task=showProject&office_type=${list1.office_type}&key_person_id=${list1.key_person_id}">
                                            <input type="button" name="" class="button" value="${list1.office_type}"/>
                                        </a>
                                        <br/>
                                        <br/>
                                    </td>
                                </TR>
                            </c:forEach>
                        </TABLE>
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
