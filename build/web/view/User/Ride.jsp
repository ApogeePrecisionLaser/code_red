<%--
    Document   : stateView
    Created on : May 30, 2014, 12:13:45 PM
    Author     : JPSS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="style/style2.css" type="text/css" rel="stylesheet" media="Screen"/>
<link href="style/Table_content.css" type="text/css" rel="stylesheet" media="Screen"/>
<script type="text/javascript" src="JS/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="JS/jquery.autocomplete.js"></script>
<script language="javascript" type="text/javascript" src="JS/calendar.js"></script>
<link rel="stylesheet" href="datePicker/jquery.ui.all.css">
<link rel="stylesheet" type="text/css" href="css/calendar.css" >
<script type="text/javascript"  src="datePicker/jquery.ui.core.js"></script>
<script type="text/javascript" src="datePicker/jquery.ui.widget.js"></script>
<script type="text/javascript" src="datePicker/jquery.ui.datepicker.js"></script>
<script type="text/javascript" language="javascript">
    jQuery(function(){
        $("#searchName").autocomplete("rideTypeCont",
        {
            extraParams: {
                action1: function() { return "getName"},
                action3: function() { return document.getElementById("searchMobileNo").value},
                action4: function() { return document.getElementById("searchBookindNo").value}
            }
        });
        $("#searchMobileNo").autocomplete("rideTypeCont",
        {
            extraParams: {
                action1: function() { return "getMobileNo"},
                action2: function() { return document.getElementById("searchName").value;},
                action4: function() { return document.getElementById("searchBookindNo").value;}
            }
        });
        $("#searchBookindNo").autocomplete("rideTypeCont", {
            extraParams: {
                action1: function() { return "getBookingNo"},
                action2: function() { return document.getElementById("searchName").value;},
                action3: function() { return document.getElementById("searchMobileNo").value;}
            }
        });
        $("#searchDate").datepicker({

            minDate: -100,
            showOn: "both",
            buttonImage: "images/calender.png",
            dateFormat: 'yy-mm-dd',
            buttonImageOnly: true,
            changeMonth: true,
            changeYear: true
        });
    });

    function fillColumns(id) {
        //        var noOfRowsTraversed = document.getElementById("noOfRowsTraversed").value;
        //        var noOfColumns = 4;
        //
        //        var columnId = id;                              <%-- holds the id of the column being clicked, excluding the prefix t1c e.g. t1c3 (column 3 of table 1). --%>
        //        columnId = columnId.substring(3, id.length);    <%-- for e.g. suppose id is t1c3 we want characters after t1c i.e beginIndex = 3. --%>
        //        var lowerLimit, higherLimit;
        //
        //        for(var i = 0; i < noOfRowsTraversed; i++) {
        //            lowerLimit = i * noOfColumns + 1;       // e.g. 11 = (1 * 10 + 1)
        //            higherLimit = (i + 1) * noOfColumns;    // e.g. 20 = ((1 + 1) * 10)
        //
        //            if((columnId >= lowerLimit) && (columnId <= higherLimit)) break;
        //        }
        //
        //        setDefaultColor(noOfRowsTraversed, noOfColumns);        // set default color of rows (i.e. of multiple coloumns).
        //        var t1id = "t1c";       // particular column id of table 1 e.g. t1c3.
        //
        //        document.getElementById("vehicle_type_id").value= document.getElementById(t1id + (lowerLimit + 0)).innerHTML;
        //        document.getElementById("vehicle_type").value = document.getElementById(t1id +(lowerLimit+2)).innerHTML;
        //      //   document.getElementById("commercial_type").value = document.getElementById(t1id +(lowerLimit+3)).innerHTML;
        //        document.getElementById("remark").value = document.getElementById(t1id +(lowerLimit+3)).innerHTML;
        //        for(var i = 0; i < noOfColumns; i++) {
        //            document.getElementById(t1id + (lowerLimit + i)).bgColor = "#d0dafd";        // set the background color of clicked row to yellow.
        //        }
        //        document.getElementById("edit").disabled = false;
        //        if(!document.getElementById("save").disabled)   // if save button is already enabled, then make edit, and delete button enabled too.
        //        {
        //            document.getElementById("save_As").disabled = true;
        //            document.getElementById("delete").disabled = false;
        //        }
        //        //  document.getElementById("message").innerHTML = "";      // Remove message
        //        $("#message").html("");
    }
    //
    //
    //
    //    function myLeftTrim(str) {
    //        var beginIndex = 0;
    //        for(var i = 0; i < str.length; i++) {
    //            if(str.charAt(i) == ' ')
    //                beginIndex++;
    //            else break;
    //        }
    //        return str.substring(beginIndex, str.length);
    //    }
    var popupwin = null;
    function displayMapList(id)
    {
        var queryString;
        var searchName=document.getElementById("searchName").value;
        var searchMobile_no=document.getElementById("searchMobileNo").value;
        var searchBookindNo=document.getElementById("searchBookindNo").value;
        var searchDate=document.getElementById("searchDate").value;
        if(id == "viewPdf")
            queryString = "task=generateReport&searchName="+searchName+"&searchMobileNo="+searchMobile_no+"&searchBookindNo="+searchBookindNo+"&searchDate="+searchDate;
        else
            queryString = "task=generateXlxReport&searchName="+searchName+"&searchMobileNo="+searchMobile_no+"&searchBookindNo="+searchBookindNo+"&searchDate="+searchDate;
        var url = "rideTypeCont?"+queryString;
        popupwin = openPopUp(url, "State Type Map Details", 500, 1000);

    }

    function openPopUp(url, window_name, popup_height, popup_width) {
        var popup_top_pos = (screen.availHeight / 2) - (popup_height / 2);
        var popup_left_pos = (screen.availWidth / 2) - (popup_width / 2);
        var window_features = "left=" + popup_left_pos + ", top=" + popup_top_pos + ", width=" + popup_width + ", height=" + popup_height + ", resizable=yes, scrollbars=yes, location=0, menubar=no, status=no, dependent=yes";
        return window.open(url, window_name, window_features);
    }
</script>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ride</title>
    </head>
    <body>
        <table align="center" cellpadding="0" cellspacing="0" class="main">
            <tr><td>    <%@include file="/layout/header.jsp" %> </td></tr>
            <tr>
                <td>    <%@include file="/layout/menu.jsp" %> </td>
            </tr>
            <td>
                <DIV id="body" class="maindiv" align="center" >
                    <table width="100%" align="center">
                        <tr><td>
                                <table align="center">
                                    <tr>
                                        <td align="center" class="header_table" width="90%"><b>RIDE FEEDBACK</b></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td> <div align="center">
                                    <form name="form0" method="post" action="rideTypeCont">
                                        <table align="center" class="heading1" width="600">
                                            <tr>
                                                <th>Name</th>
                                                <td><input class="input" type="text" id="searchName" name="searchName" value="${searchName}" size="30" ></td>
                                                <th>Mobile No</th>
                                                <td><input class="input" type="text" id="searchMobileNo" name="searchMobileNo" value="${searchMobileNo}" size="30" ></td>
                                                <th>Booking No</th>
                                                <td><input class="input" type="text" id="searchBookindNo" name="SearchBookingNo" value="${searchBookindNo}" size="30" ></td>
                                            </tr>
                                            <tr>
                                                <th>Date</th>
                                                <td><input class="input" type="text" id="searchDate" name="searchDate" value="${searchDate}" size="30" ></td>
                                                <td><input class="button" type="submit" name="task" id="searchIn" value="Search"></td>
                                                <td><input class="button" type="submit" name="task" id="showAllRecords" value="Show All Records"></td>
                                                <td><input type="button" class="pdf_button" id="viewPdf" name="viewPdf" value="" onclick="displayMapList(id)"></td>
                                                <td><input type="button" class="button" id="viewExcel" name="viewExcel" value="Excel" onclick="displayMapList(id)"></td>
                                            </tr>
                                        </table>
                                    </form></div>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <form name="form1" method="post" action="rideTypeCont">
                                    <DIV class="content_div">
                                        <table id="table1" width="600"  border="1"  align="center" class="content">
                                            <tr>
                                                <th class="heading">S.No.</th>
                                                <th class="heading">Name</th>
                                                <th class="heading">Mobile No</th>
                                                <th class="heading">Booking No</th>
                                                <th class="heading">Vehicle No</th>
                                                <th class="heading">Driver Name</th>
                                                <th class="heading">Ride Date</th>
                                                <th class="heading">Ride Time</th>
                                                <th class="heading">Feedback</th>
                                                <th class="heading">Ratings</th>
                                                <th  class="heading">Remark</th>
                                            </tr>
                                            <c:forEach var="list" items="${requestScope['List']}"  varStatus="loopCounter">
                                                <tr  class="${loopCounter.index % 2 == 0 ? 'even': 'odd'}" >
                                                    <td id="t1c${IDGenerator.uniqueID}" onclick="fillColumns(id)" align="center">${lowerLimit - noOfRowsTraversed + loopCounter.count}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}" onclick="fillColumns(id)" onclick="fillColumns(id)">${list.name}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}"  onclick="fillColumns(id)" >${list.mobile_no}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}"  onclick="fillColumns(id)" >${list.booking_no}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}"  onclick="fillColumns(id)">${list.vehicle_no}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}"  onclick="fillColumns(id)">${list.driver_name}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}"  onclick="fillColumns(id)">${list.ride_date}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}"  onclick="fillColumns(id)">${list.ride_time}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}"  onclick="fillColumns(id)">${list.feedback}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}"  onclick="fillColumns(id)">${list.rating}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}"  onclick="fillColumns(id)">${list.remark}</td>
                                                </tr>
                                            </c:forEach>
                                            <tr>
                                                <td align='center' colspan="11">
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
                                            <input  type="hidden" id="searchName" name="searchName" value="${searchName}" >
                                            <input  type="hidden" id="searchEmailId" name="searchEmailId" value="${searchEmailId}" >
                                            <input  type="hidden" id="searchMobileNo" name="searchMobileNo" value="${searchMobileNo}" >
                                            <input  type="hidden" id="searchDate" name="searchDate" value="${searchDate}" >
                                        </table>
                                    </DIV>
                                </form>
                            </td>
                        </tr>
                    </table>
                </DIV>
            </td>
            <tr><td><%@include file="/layout/footer.jsp" %></td> </tr>
        </table>
    </body>
</html>

