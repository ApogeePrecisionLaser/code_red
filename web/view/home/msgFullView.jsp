<%-- 
    Document   : msgFullView
    Created on : Mar 10, 2017, 12:50:25 PM
    Author     : Manpreet
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Message Full View Page</title>
        <script src="js4LightBox/jquery-1.10.2.min.js"></script>
        <script type="text/javascript" src="js4LightBox/lightbox-2.6.min.js"></script>
        <link href="css4LightBox/lightbox.css" rel="stylesheet" />
        <link rel="stylesheet" type="text/css" href="style/style.css" />
        <link rel="stylesheet" type="text/css" href="style/Table_content.css" />
        <link href="style/style.css" type="text/css" rel="stylesheet" media="Screen"/>
        <link href="style/Table_content.css" type="text/css" rel="stylesheet" media="Screen"/>
        <script type="text/javascript" src="JS/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="JS/jquery.autocomplete.js"></script>
        <script type="text/javascript" language="javascript">
            
            function setPrevView(){
                window.close();
            }

            function navigateLink() {
                //  alert("hi link");
                var link = $("#link").val(); // alert(link);
                if(link.match("https://") || link.match("http://"))
                    link = link;
                else
                    link = "http://" + link;
                window.open(link);
                //                             var a = document.createElement('a');
                //                              a.href= link;
                //                              document.body.replaceWith(a);
                //                                a.click();
                // popupwin = openPopUp(link, "Message Link", 600, 725);
            }

            function openPopUp(url, window_name, popup_height, popup_width) {
                var popup_top_pos = (screen.availHeight / 2) - (popup_height / 2);
                var popup_left_pos = (screen.availWidth / 2) - (popup_width / 2);
                var window_features = "left=" + popup_left_pos + ", top=" + popup_top_pos + ", width=" + popup_width + ", height=" + popup_height + ", resizable=no, scrollbars=yes, status=no, dependent=yes, dialog=yes";
                return window.open(url, window_name, window_features);
                //                window.open('/pageaddress.html','winname','directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=400,height=350'); 
            }

            function openAttachment(image_name,general_image_details_id){
                  // alert("hi attachment");
                var name = "getImage";
                var url = "messageViewCont.do?task="+name+"&general_image_details_id="+general_image_details_id+"&image_name="+image_name;
                //                popupwin = openPopUp(url, "Message Attachment", 600, 725);
                window.open(url);

                //                var queryString = "task="+name+"&general_image_details_id="+general_image_details_id+"&image_name="+image_name;
                //                $.ajax({url: "messageViewCont.do",
                //                    data: queryString,
                //                    success: function(response_data) {
                //                        //  alert(response_data);
                //                    }
                //                });
            }
            
        </script>
    </head>
    <body>
        <table align="center"  class="main" cellpadding="0" cellspacing="0" style="height: 50%" >
            <%--  <tr>
                  <td align="left"> --%>
            <form name="form1" method="POST" action="messageViewCont.do">
                <%--  <DIV class="content_div" style="width: 990px;height: 50%"> --%>
                <table border="2" id="table1" align="center"  class="content" width="990px" style="height: 100%">

                    <tr><td>
                            <div id="wrapper" align="center" style="margin-bottom: 0px;padding-bottom: 0px;background-color: green">
                                <div class="block1" style="width: 550px">
                                    <div><h1><font color="white"> Message </font></h1></div>
                                </div>
                            </div>
                        </td></tr>

                    <%--   <tr><td><br><br></td></tr> --%>

                    <c:forEach var="HomeData" items="${requestScope['HomeList']}" varStatus="loopCounter">
                        <tr class="${loopCounter.index % 2 == 0 ? 'even': 'odd'}">
                        <tr style="display: none"><td>
                                <span class="heading1">Message id : </span>
                                ${HomeData.message_id}
                            </td></tr>

                        <tr><td>
                                <span class="heading1">Message From : </span>
                                ${HomeData.keyperson_name}
                            </td></tr>
                        <tr><td>
                                <span class="heading1">Date : </span>
                                ${HomeData.msg_date_time}
                            </td></tr>
                        <tr><td>
                                <span class="heading1">Subject : </span>
                                ${HomeData.subject}
                            </td></tr>
                        <tr><td>
                                <span class="heading1">Message : </span>
                                ${HomeData.message}
                            </td></tr>
                        <tr onclick="navigateLink()"><td>
                                <span class="heading1">Link : </span>
                                <%--  <a href="${HomeData.link}">${HomeData.link}</a> --%>
                                <font color="blue"><u>${HomeData.link}</u></font>
                            </td></tr>

                        <tr><td>
                                <span class="heading1">Attachment : </span>
                            </td></tr>
                        <tr><td>
                                <c:forEach var="imgList" items="${imageList}" varStatus="loopCounter">
                                    <a  href="messageViewCont.do?task=getImage&image_name=${imgList.image_name}&general_image_details_id=${imgList.general_image_details_id}" rel="lightbox[group1]">
                                        <img  src="messageViewCont.do?task=getImageThumb&image_name=${imgList.image_name}&general_image_details_id=${imgList.general_image_details_id}"  width="50px" height="50px"/></a>
                                    </c:forEach>
                            </td></tr>

                        <c:forEach var="fList" items="${fileList}" varStatus="loopCounter">
                            <tr onclick="openAttachment('${fList.image_name}' , '${fList.general_image_details_id}')"><td>
                                    <font color="blue"><u>${fList.image_name}</u></font>
                                </td></tr>
                            </c:forEach>

                        <%--    <tr onclick="openAttachment()"><td>
                                   <span class="heading1">Attachment : </span>
                                   ${HomeData.image_name}
                               </td></tr>

                                    <input  type="hidden" id="image_name" name="image_name" value="${HomeData.image_name}" disabled>
                                    <input  type="hidden" id="general_image_details_id" name="general_image_details_id" value="${HomeData.general_image_details_id}" disabled> --%>

                        <input  type="hidden" id="message_id" name="message_id" value="${HomeData.message_id}" disabled>
                        <input  type="hidden" id="link" name="link" value="${HomeData.link}" disabled>

                        </tr>
                    </c:forEach>

                    <tr align="center" style="background-color: green"><td align="center">
                            <input class="button" type="submit" name="add" id="back" value="Go Back" align="center" onclick="setPrevView()" >
                        </td></tr>
                    <input type="hidden" name="lowerLimit" value="${lowerLimit}">
                    <input type="hidden" id="noOfRowsTraversed" name="noOfRowsTraversed" value="${noOfRowsTraversed}">
                </table>
                <%--    </DIV> --%>
            </form>
            <%--    </td>
           </tr> --%>
        </table>
    </body>
</html>
