<%-- 
    Document   : messageview
    Created on : Mar 9, 2017, 11:01:31 AM
    Author     : Nishu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="style/style.css" type="text/css" rel="stylesheet" media="Screen"/>
        <link href="style/Table_content.css" type="text/css" rel="stylesheet" media="Screen"/>
        <script type="text/javascript" language="javascript">

            function setStatus(id) { // alert(id);
                if(id == 'save') {
                    document.getElementById("clickedButton").value = "Save Message";
                    // alert(document.getElementById("clickedButton").value);
                }
            }

    function setDefaultColor(noOfRowsTraversed, noOfColumns) {
        for(var i = 0; i < noOfRowsTraversed; i++) {
            for(var j = 1; j <= noOfColumns; j++) {
                document.getElementById("t1c" + (i * noOfColumns + j)).bgColor = "";     // set the default color.
            }
        }
}

          
function fillColumns(id) {
    var noOfRowsTraversed = document.getElementById("noOfRowsTraversed").value;
    var noOfColumns = 5;
    var columnId = id;                              <%-- holds the id of the column being clicked, excluding the prefix t1c e.g. t1c3 (column 3 of table 1). --%>
    columnId = columnId.substring(3, id.length);    <%-- for e.g. suppose id is t1c3 we want characters after t1c i.e beginIndex = 3. --%>
    var lowerLimit, higherLimit;
    for(var i = 0; i < noOfRowsTraversed; i++) {
        lowerLimit = i * noOfColumns + 1;       // e.g. 11 = (1 * 10 + 1)
        higherLimit = (i + 1) * noOfColumns;    // e.g. 20 = ((1 + 1) * 10)
        if((columnId >= lowerLimit) && (columnId <= higherLimit)) break;
    }
    setDefaultColor(noOfRowsTraversed, noOfColumns);        // set default color of rows (i.e. of multiple coloumns).
    var t1id = "t1c";       // particular column id of table 1 e.g. t1c3.
    for(var i = 0; i < noOfColumns; i++) {
        // set the background color of clicked/selected row to yellow.
        document.getElementById(t1id + (lowerLimit + i)).bgColor = "#d0dafd";
    }

    getMsgFullView(document.getElementById(t1id + (lowerLimit + 0)).innerHTML);
}

function getMsgFullView(idVal){ //alert("hiiiii");
    //var parentHeaderName = $("#parentHeaderName").val();
    var name = "showFullMsg";// alert(name);
    // var idVal = $('#'+id).val();
    //  alert(idVal);
    var url = "messageViewCont.do?task="+name+"&idVal="+idVal;
    popupwin = openPopUp(url, "Msg Full View", 600, 725);
}

function openPopUp(url, window_name, popup_height, popup_width) {
    var popup_top_pos = (screen.availHeight / 2) - (popup_height / 2);
    var popup_left_pos = (screen.availWidth / 2) - (popup_width / 2);
    var window_features = "left=" + popup_left_pos + ", top=" + popup_top_pos + ", width=" + popup_width + ", height=" + popup_height + ", resizable=no, scrollbars=yes, status=no, dialog=yes, dependent=yes";
    return window.open(url, window_name, window_features);
    //                window.open('/pageaddress.html','winname','directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=400,height=350');
}

        </script>
    </head>
    <body>
        <table align="center"  class="main" cellpadding="0" cellspacing="0" style="height: 50%" >
            <tr>
                <td><%@include file="/layout/header.jsp" %></td>
            </tr>
            <tr>
                <td><%@include file="/layout/menu.jsp" %></td>
            </tr>

            <tr><td><div></div><br> </td></tr><br>

            <tr><td>
                    <div id="wrapper" align="center" style="margin-bottom: 0px;padding-bottom: 0px;background-color: green">
                        <div class="block1" style="width: 450px">
                            <div><h1><font color="white"> Display Messages View </font></h1></div>
                        </div>
                    </div>
                </td></tr>

            <!--            <tr>
                            <td>
                                <div class="maindiv" id="body">
                                    <table width="100%" >-->
            <tr>

            </tr>
            <!--                           </table>
                                    </div>
                                 </td>
                              </tr>-->

            <tr align="center">
                <td>
                    <table width="100%">
                        <tr>

                            <td align="left" style="vertical-align: top;" >
                                <form name="form1" method="POST" action="messageViewCont.do">
                                    <DIV style="height: 100%">
                                        <table border="2" id="table1" align="center" class="content" width="580px" style="height: 100%">
                                            <%-- <tr>
                                                 <th class="heading" style="display: none"><!-- Message ID --></th>
                                                 <th class="heading" style="width: 10px; display: none" align="left">S.No.</th>
                                                 <th class="heading" align="left" style="width: 20px" >Message From</th>
                                                 <th class="heading" align="left">Subject</th>
                                                 <th class="heading" align="left">Date & Time</th>
                                             </tr> --%>
                                            <c:forEach var="HomeData" items="${requestScope['HomeList']}" varStatus="loopCounter">
                                                <tr class="${loopCounter.index % 2 == 0 ? 'even': 'odd'}">
                                                    <%--   <td id="t1c${IDGenerator.uniqueID}" style="display: none" onclick="fillColumns(id)" >${HomeData.message_id}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}" style="display: none" onclick="fillColumns(id)" align="left" >${lowerLimit - noOfRowsTraversed + loopCounter.count}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}" style="width: 20px" onclick="fillColumns(id)"  align="center" >${HomeData.keyperson_name}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}" onclick="fillColumns(id)"  align="center" >${HomeData.subject}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}" onclick="fillColumns(id)"  align="center" >${HomeData.msg_date_time}</td>  --%>

                                                <tr onclick="getMsgFullView('${HomeData.message_id}')"><td>
                                                        <span class="heading1">
                                                            <c:if test="${HomeData.attachment_file == 'Yes' && HomeData.attachment_image == 'Yes'}">
                                                                <a href="#" > <img  src="./images/m-attach.png"></a>
                                                                Message From : 
                                                            </c:if>
                                                            <c:if test="${HomeData.attachment_file == 'Yes' && HomeData.attachment_image == 'No'}">
                                                                <a href="#" > <img  src="./images/m-attach.png"></a>
                                                                Message From :
                                                            </c:if>
                                                            <c:if test="${HomeData.attachment_file == 'No' && HomeData.attachment_image == 'Yes'}">
                                                                <a href="#" > <img  src="./images/gallery-icon1.png"></a>
                                                                Message From :
                                                            </c:if>
                                                            <c:if test="${HomeData.attachment_file == 'No' && HomeData.attachment_image == 'No'}">
                                                                <a href="#" > <img  src="./images/m-message-v.png"></a>
                                                                Message From :
                                                            </c:if>
                                                        </span>
                                                        <%-- <span class="heading1"># Message From : </span> --%>
                                                        <b>   ${HomeData.keyperson_name} </b>
                                                    </td></tr>
                                                <tr onclick="getMsgFullView('${HomeData.message_id}')"><td>
                                                        <span> &nbsp;  Date : </span>
                                                        ${HomeData.msg_date_time}
                                                        &nbsp;&nbsp;
                                                        <span> Subject : </span>
                                                        ${HomeData.subject}
                                                    </td></tr>

                                                </tr>
                                            </c:forEach>
                                            <tr>
                                                <td align='center' colspan="1">
                                                    <c:choose>
                                                        <c:when test="${showFirst eq 'false'}">
                                                            <input class="button" type='submit' name='buttonAction' value='First' disabled>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input class="button" type='submit' name='buttonAction' value='First'>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when test="${showPrevious == 'false'}">
                                                            <input class="button" type='submit' name='buttonAction' value='Previous' disabled>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input class="button" type='submit' name='buttonAction' value='Previous'>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when test="${showNext eq 'false'}">
                                                            <input class="button" type='submit' name='buttonAction' value='Next' disabled>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input class="button" type='submit' name='buttonAction' value='Next'>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when test="${showLast == 'false'}">
                                                            <input class="button" type='submit' name='buttonAction' value='Last' disabled>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input class="button" type='submit' name='buttonAction' value='Last'>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                            <input type="hidden" name="lowerLimit" value="${lowerLimit}">
                                            <input type="hidden" id="noOfRowsTraversed" name="noOfRowsTraversed" value="${noOfRowsTraversed}">
                                            <input  type="hidden" id="message_id" name="message_id" value="" disabled>
                                        </table>

                                    </DIV>
                                </form>
                            </td>

                            <td align="right" style="vertical-align: bottom">
                                <form name="form2" action="messageViewCont.do" method="post" accept-charset=="UTF-8"  enctype="multipart/form-data" >     <%-- onsubmit="return verify()" --%>
                                    <div>
                                        <table id="table" align="right" border="2" class="content" style="vertical-align: bottom">   <%--  ;margin-bottom: 10% width="250px" --%>
                                            <tr>
                                                <td align="center">
                                                    <input class="text" type="subject" name="subject" id="subject" value="" placeholder="Subject" size="45">
                                                </td>
                                            </tr>

                                            <tr>
                                                <td>
                                                    <textarea id="message" name="message" value="" style="overflow:auto;resize:none" placeholder="Enter Message" rows="5" cols="35"></textarea>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="">
                                                    <b class="heading1">Link</b>
                                                    <input class="link"  type="link" id="link" name="link" value=""  multiple="multiple" size="45"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="">
                                                    <b class="heading1">Select Files</b>
                                                    <input class="file"  type="file" id="file_name" name="file_name" value=""  multiple="multiple" accept=".xls,.xlsx,.pdf,.doc,.docx" />
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="">
                                                    <b class="heading1">Select Images</b>
                                                    <input class="file"  type="file" id="img_name" name="img_name" value=""   multiple="multiple" accept=".jpeg,.jpg,.png" />
                                                </td>
                                            </tr>
                                            <tr>
                                                <td align="center">
                                                    <input class="button" type="submit" name="task" id="save" value="Save Message" onclick="setStatus(id)">
                                                </td>
                                            </tr>
                                            <input type="hidden" id="clickedButton" value="">
                                        </table>
                                    </div>
                                </form>
                            </td>

                        </tr>
                    </table>
                </td>

            <tr style="background-color: green">
                <td>
                    <div align="center" style="background: green;color: white">Bar Association</div>
                </td>
            </tr>
        </table>
    </body>
</html>
