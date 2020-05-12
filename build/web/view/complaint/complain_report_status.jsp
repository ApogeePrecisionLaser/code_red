<%-- 
    Document   : sourceDestinationView
    Created on : Jan 22, 2016, 3:49:51 PM
    Author     : jpss
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<link href="style/style1.css" type="text/css" rel="stylesheet" media="Screen"/>
<link href="style/style.css" type="text/css" rel="stylesheet" media="Screen"/>
<link href="style/Table_content.css" type="text/css" rel="stylesheet" media="Screen"/>
<script type="text/javascript" src="JS/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="JS/jquery.autocomplete.js"></script>
<link rel="stylesheet" href="datePicker/jquery.ui.all.css">
<script type="text/javascript"  src="datePicker/jquery.ui.core.js"></script>
<script type="text/javascript" src="datePicker/jquery.ui.widget.js"></script>
<script type="text/javascript" src="datePicker/jquery.ui.datepicker.js"></script>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" language="javascript">
            var doc = document;
            jQuery(function(){
                $("#date").datepicker({
                    minDate: -100,
                    showOn: "both",
                    buttonImage: "images/calender.png",
                    dateFormat: 'yy-mm-dd',
                    buttonImageOnly: true,
                    changeMonth: true,
                    changeYear: true
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
                $("#searchMobileNo").autocomplete("complainstatusCont.do", {
                    extraParams: {
                        action1: function() { return "getMobileNo"}
                    }
                });
                $("#searchOfficerCode").autocomplete("personCount.do", {
                    extraParams: {
                        action1: function() { return "getSearchEmpCode"}
                    }
                });
                $("#searchOfficerName").autocomplete("personCount.do", {
                    extraParams: {
                        action1: function() { return "getSearchKeyPerson"}
                    }
                });
                $("#searchOfficerName").result(function(event, data, formatted){
                    var id = this.id;
                    $.ajax({url: "personCount.do?action1=getEmpCode", data: "personName="+ data +"&q=", success: function(response_data) {
                            $("#searchOfficerCode").val(response_data.trim());
                        }
                    });
                });
                $("#searchOfficerCode").result(function(event, data, formatted){
                    var id = this.id;
                    $.ajax({url: "personCount.do?action1=getKeyPerson", data: "empCode="+ data +"&q=", success: function(response_data) {
                            $("#searchOfficerName").val(response_data.trim());
                        }
                    });
                });
                $("#officer_code").autocomplete("personCount.do", {
                    extraParams: {
                        action1: function() { return "getSearchEmpCode"}
                    }
                });
                $("#officer_name").autocomplete("personCount.do", {
                    extraParams: {
                        action1: function() { return "getSearchKeyPerson"}
                    }
                });               
                $("#officer_name").result(function(event, data, formatted){
                    var id = this.id;
                    $.ajax({url: "personCount.do?action1=getEmpCode", data: "personName="+ data +"&q=", success: function(response_data) {
                            $("#officer_code").val(response_data.trim());
                        }
                    });
                });
                $("#officer_code").result(function(event, data, formatted){
                    var id = this.id;
                    $.ajax({url: "personCount.do?action1=getKeyPerson", data: "empCode="+ data +"&q=", success: function(response_data) {
                            $("#officer_name").val(response_data.trim());
                        }
                    });
                });
            });


            function makeEditable(id) {
                doc.getElementById("victim_name").disabled = false;
                doc.getElementById("mobile_no").disabled = false;
                doc.getElementById("date").disabled = false;
                doc.getElementById("time_h").disabled = false;
                doc.getElementById("time_m").disabled = false;
                
                doc.getElementById("text").disabled = false;
                
                doc.getElementById("latitude").disabled = false;
                doc.getElementById("longitude").disabled = false;
                
//                doc.getElementById("status").disabled = false;
                doc.getElementById("get_cordinate").disabled = false;
                doc.getElementById("save").disabled = false;

                if(id == 'new') {
                    //doc.getElementById("message").innerHTML = "";      // Remove message
                    $("#message").html("");
                    doc.getElementById("edit").disabled = true;
                    doc.getElementById("cancel").disabled = true;
                    doc.getElementById("save_As").disabled = true;
                    doc.getElementById("revise").disabled = true;
                    doc.getElementById("complain_id").value = 0;
                    doc.getElementById("complain_revision").value = 0;
                    setDefaultColor(doc.getElementById("noOfRowsTraversed").value, 10);
                    doc.getElementById("victim_name").focus();
                }
                if(id == 'edit'){
                    doc.getElementById("save_As").disabled = false;
                    doc.getElementById("cancel").disabled = false;
                    doc.getElementById("revise").disabled = false;
                    doc.getElementById("save").disabled = true;
                }
            }

            function setStatus(val) {
                doc.getElementById("clickedButton").value = val;
            }

            function verify()
            {
                var result;
                var clickedButton = doc.getElementById("clickedButton").value;
                if(clickedButton == 'Save' || clickedButton == 'Save AS New' || clickedButton == 'Revise') {
                    var distance = doc.getElementById("distance").value;
                    var amount = doc.getElementById("amount").value;
                    var amt = parseFloat(distance);
                    amt = amt * 10 + 3;

                    //            if(amount != amt) {
                    //                doc.getElementById("message").innerHTML = "<td colspan='5' bgcolor='coral'><b>Please currect Amount...</b></td>";
                    //                $("#message").html("<td colspan='5' bgcolor='coral'><b>Please currect Amount...</b></td>");
                    //                doc.getElementById("amount").focus();
                    //                return false; // code to stop from submitting the form2.
                    //            }
                    if(result == false)    // if result has value false do nothing, so result will remain contain value false.
                    {

                    }
                    else{ result = true;
                    }

                    if(clickedButton == 'Save AS New'){
                        result = confirm("Are you sure you want to save it as New record?")
                        return result;
                    }else if(clickedButton == 'Revise'){
                        result = confirm("Are you sure you want to Revise this record?")
                        return result;
                    }
                } else result = confirm("Are you sure you want to Cancel this record?")
                return result;
            }

            function setDefaultColor(noOfRowsTraversed, noOfColumns) {
                for(var i = 0; i < noOfRowsTraversed; i++) {
                    for(var j = 1; j <= noOfColumns; j++) {
                        doc.getElementById("t1c" + (i * noOfColumns + j)).bgColor = "";     // set the default color.
                    }
                }
            }

            function fillColumns(id) {
                var noOfRowsTraversed = doc.getElementById("noOfRowsTraversed").value;
                var noOfColumns = 8;

                var columnId = id;                              <%-- holds the id of the column being clicked, excluding the prefix t1c e.g. t1c3 (column 3 of table 1). --%>
                columnId = columnId.substring(3, id.length);    <%-- for e.g. suppose id is t1c3 we want characters after t1c i.e beginIndex = 3. --%>
                var lowerLimit, higherLimit,rowNum = 0;;

                for(var i = 0; i < noOfRowsTraversed; i++) {
                    lowerLimit = i * noOfColumns + 1;       // e.g. 11 = (1 * 10 + 1)
                    higherLimit = (i + 1) * noOfColumns;    // e.g. 20 = ((1 + 1) * 10)
                    rowNum++;
                    if((columnId >= lowerLimit) && (columnId <= higherLimit)) break;
                }

                setDefaultColor(noOfRowsTraversed, noOfColumns);        // set default color of rows (i.e. of multiple coloumns).
                var t1id = "t1c";       // particular column id of table 1 e.g. t1c3.

                doc.getElementById("complain_id").value = doc.getElementById("complain_id" + rowNum).value;
                
//                doc.getElementById("status").value = doc.getElementById("status_id" + rowNum).value;
//                doc.getElementById("node_id").value = doc.getElementById("node_id" + rowNum).value;
                doc.getElementById("victim_name").value = doc.getElementById(t1id +(lowerLimit+2)).innerHTML;
                doc.getElementById("mobile_no").value = doc.getElementById(t1id +(lowerLimit+3)).innerHTML;
                var date_time = doc.getElementById(t1id +(lowerLimit+4)).innerHTML;
                doc.getElementById("date").value = date_time.split(" ")[0];
                if (date_time != ""){
                    var time  = date_time.split(" ")[1];
                    doc.getElementById("time_h").value = time.split(":")[0];
                    doc.getElementById("time_m").value = time.split(":")[1];
                }
                doc.getElementById("text").value = doc.getElementById(t1id +(lowerLimit+5)).innerHTML;
//                doc.getElementById("parentHeaderName").value = doc.getElementById("node_name" + rowNum).value;
//                doc.getElementById("is_emergency").value = doc.getElementById(t1id +(lowerLimit+7)).innerHTML;
//                doc.getElementById("officer_name").value = doc.getElementById(t1id +(lowerLimit+8)).innerHTML;
//                doc.getElementById("remark").value = doc.getElementById(t1id +(lowerLimit+9)).innerHTML;
                //doc.getElementById("status").value = doc.getElementById(t1id +(lowerLimit+9)).innerHTML;
                doc.getElementById("latitude").value = doc.getElementById(t1id +(lowerLimit+6)).innerHTML;
                doc.getElementById("longitude").value = doc.getElementById(t1id +(lowerLimit+7)).innerHTML;
                for(var i = 0; i < noOfColumns; i++)
                {
                    doc.getElementById(t1id + (lowerLimit + i)).bgColor = "#d0dafd";        // set the background color of clicked row to yellow.
                }
                doc.getElementById("edit").disabled = false;
                if(!doc.getElementById("save").disabled)   // if save button is already enabled, then make edit, and delete button enabled too.
                {
                    doc.getElementById("save_As").disabled = true;
                    doc.getElementById("delete").disabled = false;
                }
                //doc.getElementById("message").innerHTML = "";      // Remove message
                $("#message").html("");
            }

            function myLeftTrim(str) {
                var beginIndex = 0;
                for(var i = 0; i < str.length; i++) {
                    if(str.charAt(i) == ' ')
                        beginIndex++;
                    else break;
                }
                return str.substring(beginIndex, str.length);
            }

            var popupwin = null;

            function displayMapList()
            {
                var report_for="status_type";
                // var searchLocation = $("#searchLocation").val();
                //var queryString = "task=generateMapReport&searchLocation="+searchLocation ;
                var searchsourcename=$("#searchsourcename").val();
                var searchdestinationname=$("#searchdestinationname").val();
                var queryString = "task=generateMapReport&searchsourcename="+searchsourcename+"&searchdestinationname="+searchdestinationname ;
                var url = "complainstatusCont.do?"+queryString;
                popupwin = openPopUp(url, "State Type Map Details", 500, 1000);

            }

            function openPopUp(url, window_name, popup_height, popup_width) {
                var popup_top_pos = (screen.availHeight / 2) - (popup_height / 2);
                var popup_left_pos = (screen.availWidth / 2) - (popup_width / 2);
                var window_features = "left=" + popup_left_pos + ", top=" + popup_top_pos + ", width=" + popup_width + ", height=" + popup_height + ", resizable=yes, scrollbars=yes, location=0, menubar=no, status=no, dependent=yes";

                return window.open(url, window_name, window_features);
            }

            function IsNumeric(id) {
                var strString = doc.getElementById(id).value;
                var strValidChars = "0123456789.";
                var strChar;
                var blnResult = true;
                if (strString.length == 0) return false;
                for (i = 0; i < strString.length && blnResult == true; i++)
                {
                    strChar = strString.charAt(i);
                    if (strValidChars.indexOf(strChar) == -1)
                    {
                        doc.getElementById(id).value="";
                        $("#message").html("<td colspan='2' bgcolor='coral'><b>Value should be Numeric...</b></td>");
                        doc.getElementById(id).value = "";
                        doc.getElementById(id).focus();
                        blnResult = false;
                    }
                }
                return blnResult;
            }           

            function viewMap(longitude, lattitude) {
                var x = lattitude;//$.trim(doc.getElementById(lattitude).value);
                var y = longitude;//$.trim(doc.getElementById(logitude).value);
                var url="generalCont.do?task=showMapWindow&logitude="+y+"&lattitude="+x;
                popupwin = openPopUp(url, "",  580, 620);
            }
            
            function openMapForCord() {
                var url="generalCont.do?task=GetCordinates";//"getCordinate";
                popupwin = openPopUp(url, "",  600, 630);
            }

            function getNode(name){
                //var parentHeaderName = $("#parentHeaderName").val();
                var url = "getParentHeaderCont.do?task="+name+"&parentHeaderName=";//+parentHeaderName;
                popupwin = openPopUp(url, "Header List", 600, 725);
            }

            function getAudio(id){
                var queryString = "task=getAudioFile&complaintId="+id;
                var url = "complainstatusCont.do?"+queryString;
                popupwin = openPopUp(url, "State Type Map Details", 500, 1000);
            }

            function view(id){
                var queryString = "task=viewImage&complaintId=" +id;
                var url = "complainstatusCont.do?" + queryString;
                popupwin = openPopUp(url, "Show Image", 600, 900);
            }

        </script>
        <title>Code Red</title>
    </head>
    <body>
        <table align="center" cellpadding="0" cellspacing="0" class="main">            <!--DWLayoutDefaultTable-->
            <tr><td><%@include file="/layout/header.jsp" %></td></tr>
            <tr>
                <td><%@include file="/layout/menu.jsp" %> </td>
            </tr>
            <%-- <tr>
                    <td width="50" height="600" valign="top"><%@include file="/view/layout/Leftmenu.jsp" %></td></tr> --%>
            <td>
                <DIV id="body" class="maindiv" align="center" >
                    <table width="100%" align="center">
                        <tr>
                            <td>
                                <table align="center">
                                    <tr>
                                        <td align="center" class="header_table" width="90%"><b>Complaint View</b></td>

                                        <%-- <td><td><%@include file="/layout/menu.jsp" %> </td>
                                             <%@include file="/view/layout/org_menu.jsp" %>
                                         </td> --%>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div align="center">
                                    <form name="form0" method="get" action="complainstatusCont.do">
                                        <%-- <table align="center" class="heading1" width="750">
                                            <tr>                                                
                                                <td>
                                                    <b>Mobile No.</b>
                                                    <input class="input" type="text" id="searchMobileNo" name="searchMobileNo" value="${searchMobileNo}" size="10" >
                                                </td>                                                
                                                <td>
                                                    <b>Date</b>
                                                    <input class="input" type="text" id="searchDate" name="searchDate" value="${searchDate}" size="12" >
                                                </td>
                                                <td>
                                                    <b>Officer Name</b>
                                                    <input class="input new_input" type="text" id="searchOfficerName" name="searchOfficerName" value="${searchOfficerName}" size="15" >
                                                    <b>Officer Code</b>
                                                    <input class="input" type="text" id="searchOfficerCode" name="searchOfficerCode" value="${searchOfficerCode}" size="5" >
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="3" align="center">
                                                    <b>Emergency</b>
                                                    <select class="input" type="text" id="searchEmergency" name="searchEmergency">
                                                        <option value="" ${searchEmergency eq ""?'selected':''}>Select</option>
                                                        <option value="Y" ${searchEmergency eq "Y"?'selected':''}>Yes</option>
                                                        <option value="N" ${searchEmergency eq "N"?'selected':''}>No</option>
                                                    </select>
                                                    <b>Status</b>
                                                    <select class="input" type="text" id="searchStatus" name="searchStatus">
                                                        <option value="" ${searchStatus eq ""?'selected':''}>Select</option>
                                                        <c:forEach var="list" items="${statusList}">
                                                            <option value="${list.key}" ${searchStatus eq list.key?'selected':''}>${list.value}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>                                                
                                                <td colspan="3" align="center">                                                    
                                                    <input class="button" type="submit" name="task" id="searchIn" value="Search">
                                                    <input class="button" type="submit" name="task" id="showAllRecords" value="Show All Records">
                                                    <!--                                                    <input type="button" style="" class="pdf_button" id="viewPdf" name="viewPdf" value="" onclick="displayMapList()">-->
                                                </td>
                                            </tr>
                                        </table> --%>
                                    </form>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <form name="form1" method="get" action="complainstatusCont.do">
                                    <DIV class="content_div">

                                        <table id="table1" width="600"  border="1"  align="center" class="content">
                                            <tr>
                                                <th class="heading">S.No.</th>
                                                <th class="heading">Complain Number</th>
                                                <th  class="heading">Victim Name</th>
                                                <th class="heading">Officer Name</th>
                                                <th  class="heading">Mobile No</th>
                                                <th  class="heading">Data & Time</th>
                                                <th  class="heading">Text</th>
<!--                                                <th  class="heading">Status</th>-->
                                                <th  class="heading">Latitude</th>
                                                <th  class="heading">Longitude</th>
                                                <th  class="heading">Map</th>
                                                <th  class="heading">Audio</th>
                                                <th  class="heading">Images</th>
                                            </tr>
                                            <c:forEach var="complaintBean" items="${requestScope['complaintList']}" varStatus="loopCounter">
                                                <tr class="${loopCounter.index % 2 == 0 ? 'even': 'odd'}" >
                                                    <td id="t1c${IDGenerator.uniqueID}" style="display:none" onclick="fillColumns(id)">
                                                        <input type="hidden" id="complain_id${loopCounter.count}" value = "${complaintBean.complain_id}">
                                                    </td>
                                                    <td id="t1c${IDGenerator.uniqueID}" onclick="fillColumns(id)" align="center">${lowerLimit - noOfRowsTraversed + loopCounter.count}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}" class="" onclick="fillColumns(id)" >${complaintBean.complain_id}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}" class="" onclick="fillColumns(id)" >${complaintBean.victim_name}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}" class="" onclick="fillColumns(id)" >${complaintBean.officer_name}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}" class="" onclick="fillColumns(id)" >${complaintBean.mobile_no}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}" class="" onclick="fillColumns(id)" >${complaintBean.date_time}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}" class="" onclick="fillColumns(id)">${complaintBean.text}</td>
                                                    <%--<td id="t1c${IDGenerator.uniqueID}" class="" onclick="fillColumns(id)" >${complaintBean.status}</td>--%>
                                                    <td id="t1c${IDGenerator.uniqueID}" class="" onclick="fillColumns(id)" >${complaintBean.latitude}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}" class="" onclick="fillColumns(id)" >${complaintBean.longitude}</td>
                                                    <td><input type="button" value="View Map" onclick="viewMap(${complaintBean.longitude}, ${complaintBean.latitude})"></td>
                                                    <td>
                                                        <%--<c:if test="${complaintBean.is_emergency eq 'N'}">--%>
                                                            <input type="button" value="Audio" onclick="getAudio(${complaintBean.complain_id})">
                                                        <%--</c:if>--%>
                                                    </td>
                                                    <td>
                                                       <%-- <c:if test="${complaintBean.is_emergency eq 'N'}">--%>
                                                            <input type="button" value="View" onclick="view(${complaintBean.complain_id})">
                                                        <%--</c:if>--%>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            <tr>
                                                <td align='center' colspan="14">
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
                                            <!--- These hidden fields "lowerLimit", and "noOfRowsTraversed" belong to form1 of table1. -->
                                            <input type="hidden" name="lowerLimit" value="${lowerLimit}">
                                            <input type="hidden" id="noOfRowsTraversed" name="noOfRowsTraversed" value="${noOfRowsTraversed}">
                                            <input type="hidden" name="searchStatusType" value="${searchStatus}">
                                            <input type="hidden" name="searchEmergency" value="${searchEmergency}">
                                            <input type="hidden" name="searchMobileNo" value="${searchMobileNo}">
                                            <input type="hidden" name="searchDate" value="${searchDate}">
                                            <input type="hidden" name="searchOfficerName" value="${searchOfficerName}" >
                                            <input type="hidden" name="searchOfficerCode" value="${searchOfficerCode}" >
                                        </table>
                                    </DIV>
                                </form>
                            </td>
                        </tr>
 <%--                       <tr>
                            <td align="center">
                                <div>
                                    <form name="form2" method="post" action="complainstatusCont.do" onsubmit="return verify()">
                                        <table id="table2"  class="content" border="0"  align="center" width="600">
                                            <tr id="message">
                                                <c:if test="${not empty message}">
                                                    <td colspan="2" bgcolor="${msgBgColor}"><b>Result: ${message}</b></td>
                                                </c:if>
                                            </tr>
                                            <tr>
                                                <th class="heading1">Victim Name</th>
                                                <td>
                                                    <input class="input" type="hidden" id="complain_id" name="complain_id" value="">
                                                    <input class="input" type="hidden" id="complain_revision" name="complain_revision" value="">
                                                    <input class="input" type="text" id="victim_name" name="victim_name" value="" size="20" disabled required>
                                                </td>
                                                <th class="heading1">Mobile No.</th>
                                                <td>
                                                    <input class="input" type="text" id="mobile_no" name="mobile_no" value="" size="20" disabled required>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="4" class="heading1" style="text-align:center">
                                                    Date
                                                    <input class="input " type="text" id="date" name="date" value="" size="12" onkeyup="" disabled required>
                                                    Time
                                                    <input class="input " type="numeric" pattern="([0-1]{1}[0-9]{1}|20|21|22|23)" id="time_h" name="time_h" value="" maxlength="2" size="2" onkeyup="" disabled required>
                                                    <input class="input " type="numeric" pattern="[0-5]{1}[0-9]{1}" id="time_m" name="time_m" value="" maxlength="2" size="2" onkeyup="" disabled required>
                                                    Emergency
                                                    <select class="input" type="text" id="is_emergency" name="is_emergency" disabled>
                                                        <option value="Y">Yes</option>
                                                        <option value="N">No</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="heading1" colspan="4" style="text-align:center">
                                                    <b>Complaint Type</b>
                                                    <input type="text" id="parentHeaderName" name="node_name" value="" disabled>
                                                    <input type="button" id="getParentHeader" name="getParentHeader" value="Get Complaint Type" onclick="getNode('parent');" disabled>
                                                    <input type="hidden" id="node_id" name="node_id" value="">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="heading1">Text</th><td colspan="3"><textarea name="text" id="text" style="width: 476px; height: 57px;"></textarea></td>
                                            </tr>
                                            <tr>
                                                <td class="heading1" colspan="4" style="text-align:center">
                                                    Officer Name
                                                    <input class="input new_input" type="text" id="officer_name" name="officer_name" value="" size="30" disabled>
                                                    Code
                                                    <input class="input" type="text" id="officer_code" name="officer_code" value="" size="5" disabled>
                                                </td>
                                            </tr>
                                            <tr style="">
                                                <th class="heading1">Latitude</th>
                                                <td><input class="input " type="text" id="latitude" name="latitude" value="" size="20" disabled></td>
                                                <th class="heading1">Longitude</th>
                                                <td>
                                                    <input class="input " type="text" id="longitude" name="longitude" value="" size="20" disabled>
                                                    <input type="button" id="get_cordinate" value="Get Cordinate" onclick="openMapForCord()" disabled>
                                                    <input type="hidden" id="cordinate_change" value="">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="heading1">Description</th>
                                                <td> 
                                                    <input class="input" type="text" id="remark" name="remark" value="" size="30" disabled>
                                                </td>
                                                <th class="heading1">Status</th>
                                                <td>
                                                    <select class="input" type="text" id="status" name="status" disabled>
                                                        <option value="">Select</option>
                                                        <c:forEach var="list" items="${statusList}">
                                                            <option value="${list.key}">${list.value}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td align='center' colspan="4">
                                                    <input class="button" type="button" name="edit" id="edit" value="Edit" onclick="makeEditable(id)" disabled>
                                                    <input class="button" type="submit" name="task" id="save" value="Save" onclick="setStatus(value)" disabled>
                                                    <input class="button" type="submit" name="task" id="save_As" value="Save AS New" onclick="setStatus(value)" disabled>
                                                    <input class="button" type="reset" name="new" id="new" value="New" onclick="makeEditable(id)">
                                                    <input class="button" type="submit" name="task" id="cancel" value="Delete" onclick="setStatus(value)" disabled>
                                                    <input class="button" type="submit" name="task" id="revise" style="" value="Revise" onclick="setStatus(value)" disabled>
                                                </td>
                                            </tr>
                                            <input type="hidden" name="lowerLimit" value="${lowerLimit}">
                                            <input type="hidden" name="noOfRowsTraversed" value="${noOfRowsTraversed}">
                                            <input type="hidden" id="clickedButton" value="">
                                            <input type="hidden"  name="searchStatusType" value="${searchStatusType}" >
                                        </table>
                                    </form>
                                </div>
                            </td>
                        </tr>--%>
                    </table>

                </DIV>
            </td>
            <tr><td><%@include file="/layout/footer.jsp" %></td> </tr>
        </table>
    </body>
</html>
