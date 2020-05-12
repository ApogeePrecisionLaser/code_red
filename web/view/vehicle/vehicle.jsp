<%--
    Document   : vehicle_type
    Created on : Aug 11, 2016, 3:58:44 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
           <script type="text/javascript">
    jQuery(function(){
        $("#vehicle_sub_type").autocomplete("VehicleController", {
            extraParams: {
                action1: function() { return "getVehicleSubType"}
            }
        });
        $("#vehicleSubType").autocomplete("VehicleController", {
            extraParams: {
                action1: function() { return "getVehicleSubType"}
            }
        });
       $("#vehicleNo").autocomplete("VehicleController", {
            extraParams: {
                action1: function() { return "getVehicleNo"}
            }
        });
       $("#mobileNo").autocomplete("VehicleController", {
            extraParams: {
                action1: function() { return "getMobileNo"}
            }
        });
         $("#permit_validity").datepicker({
                        minDate: -100,
                        showOn: "both",
                        buttonImage: "images/calender.png",
                        dateFormat: 'dd-mm-yy',
                        buttonImageOnly: true,
                        changeMonth: true,
                        changeYear: true
        });
        $("#fitness_validity").datepicker({
                        minDate: -100,
                        showOn: "both",
                        buttonImage: "images/calender.png",
                        dateFormat: 'dd-mm-yy',
                        buttonImageOnly: true,
                        changeMonth: true,
                        changeYear: true
        });
        $("#puc_validity").datepicker({
                        minDate: -100,
                        showOn: "both",
                        buttonImage: "images/calender.png",
                        dateFormat: 'dd-mm-yy',
                        buttonImageOnly: true,
                        changeMonth: true,
                        changeYear: true
        });


    });

    function fillColumns(id)
     {
        var noOfRowsTraversed = document.getElementById("noOfRowsTraversed").value;
        var noOfColumns = 11;
        var columnId = id;                              <%-- holds the id of the column being clicked, excluding the prefix t1c e.g. t1c3 (column 3 of table 1). --%>
        columnId = columnId.substring(3, id.length);    <%-- for e.g. suppose id is t1c3 we want characters after t1c i.e beginIndex = 3. --%>
        var lowerLimit, higherLimit;
        for(var i = 0; i < noOfRowsTraversed; i++)
        {
            lowerLimit = i * noOfColumns + 1;       // e.g. 11 = (1 * 10 + 1)
            higherLimit = (i + 1) * noOfColumns;    // e.g. 20 = ((1 + 1) * 10)

            if((columnId>= lowerLimit) && (columnId <= higherLimit)) break;
        }

        setDefaultColor(noOfRowsTraversed, noOfColumns);        // set default color of rows (i.e. of multiple coloumns).
        var t1id = "t1c";       // particular column id of table 1 e.g. t1c3.

     document.getElementById("vehicle_id").value=document.getElementById(t1id + (lowerLimit + 0)).innerHTML;
     document.getElementById("vehicle_sub_type").value=document.getElementById(t1id + (lowerLimit + 2)).innerHTML;
     document.getElementById("vehicle_no").value=document.getElementById(t1id + (lowerLimit + 3)).innerHTML;
     document.getElementById("vehicle_code").value=document.getElementById(t1id + (lowerLimit + 4)).innerHTML;
     document.getElementById("permit_validity").value=document.getElementById(t1id + (lowerLimit + 5)).innerHTML;
     document.getElementById("fitness_validity").value=document.getElementById(t1id + (lowerLimit + 6)).innerHTML;
     document.getElementById("puc_validity").value=document.getElementById(t1id + (lowerLimit + 7)).innerHTML;
     document.getElementById("mobile_no").value=document.getElementById(t1id + (lowerLimit + 8)).innerHTML;
     document.getElementById("imei_no").value=document.getElementById(t1id + (lowerLimit + 9)).innerHTML;
     document.getElementById("remark").value=document.getElementById(t1id + (lowerLimit + 10)).innerHTML;


        for(var i = 0; i < noOfColumns; i++) {
            document.getElementById(t1id + (lowerLimit + i)).bgColor = "#d0dafd";
        }


            document.getElementById("edit").disabled = false;

        if(!document.getElementById("save").disabled)
        {
            document.getElementById("save_as_new").disabled = true;
            document.getElementById("delete").disabled = false;
           // document.getElementById("revised").disabled = false;
            document.getElementById("save").disabled=true;

        }

        $("#message").html("");
    }
          function setDefaultColor(noOfRowsTraversed, noOfColumns) {

        for(var i = 0; i < noOfRowsTraversed; i++) {

            for(var j = 1; j <= noOfColumns; j++) {

                document.getElementById("t1c" + (i * noOfColumns + j)).bgColor = "";
        }
        }}
  function myLeftTrim(str) {
        var beginIndex = 0;
        for(var i = 0; i < str.length; i++) {
            if(str.charAt(i) == ' ')
                beginIndex++;
            else break;
        }
        return str.substring(beginIndex, str.length);
    }


     function makeEditable(id) {
        document.getElementById("vehicle_sub_type").disabled = false;
        document.getElementById("vehicle_code").disabled = false;
        document.getElementById("vehicle_no").disabled = false;
        document.getElementById("permit_validity").disabled = false;
        document.getElementById("fitness_validity").disabled = false;
        document.getElementById("puc_validity").disabled = false;
        document.getElementById("imei_no").disabled = false;
        document.getElementById("mobile_no").disabled = false;
        document.getElementById("remark").disabled = false;



        document.getElementById("save").disabled = true;

        if(id == 'new') {
           $("#message").html("");
            document.getElementById("edit").disabled = true;
            document.getElementById("delete").disabled = true;
            document.getElementById("save_as").disabled =true;
             document.getElementById("save").disabled =false;
             document.getElementById("vehicle_id").value=0;

        }
        if(id == 'edit'){
               $("#message").html("");
                document.getElementById("save_as").disabled = false;
                document.getElementById("delete").disabled = false;
                 document.getElementById("save").disabled = false;

            }

        }



function setStatus(id) {

        if(id == 'save'){

            document.getElementById("clickedButton").value = "save";
        }
        else if(id == 'save_as'){
            document.getElementById("clickedButton").value = "Save AS New";
        }
        else if(id == 'delete'){
            document.getElementById("clickedButton").value = "Delete";
        }

        else
            {}
    }
</script>
    </head>
    <body>

         <table align="center" cellpadding="0" cellspacing="0" class="main">
            <tr><td><%@include file="/layout/header.jsp" %></td></tr>
            <tr>
                <td><%@include file="/layout/menu.jsp" %> </td>
            </tr>
             <td>
                <DIV id="body" class="maindiv" align="center" >
                    <table width="100%" align="center">
                        <tr><td>
                                <table align="center">
                                    <tr>
                                        <td align="center" class="header_table" width="90%"><b>Vehicle</b></td>
                                    </tr>
                                </table>
                            </td></tr>

    <form name="form0" action="VehicleController">
          <table align="center" class="heading1" width="700">
                   <tr> <th>Vehicle Sub Type</th>
                   <td><input class="new_input" type="text" id="vehicleSubType" name="vehicleSubType" value="${vehicleSubType}" size="20" ></td>
                    <th>Vehicle No</th>
                   <td><input class="input" type="text" id="vehicleNo" name="vehicleNo" value="${vehicleNo}" size="20" ></td>
                  <th>Mobile No</th>
                   <td><input class="input" type="text" id="mobileNo" name="mobileNo" value="${mobileNo}" size="20" ></td>
                </tr>
                <tr>
                    <td colspan="6" align="center">
                   <input class="button" type="submit" name="task" id="searchIn" value="Search">
                   <input class="button" type="submit" name="task" id="showAllRecords" value="Show All Records">
<!--                   <input type="button" class="pdf_button" id="viewPdf" name="viewPdf" value="" onclick="getlist()">
                   <input type="button" class="button"  id="viewXls" name="viewXls" value="Excel" onclick="getCity()">-->
                   </td>
                 </tr>
          </table>
     </form>
   <form name="form1" action="VehicleController">


        <TABLE BORDER="1" align="center" cellpadding="5%" width="52%" class="content">

                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <TH class="heading">S.no&nbsp;</TH>
                <TH class="heading">Vehicle Sub Type&nbsp;</TH>
                <TH class="heading">Vehicle No&nbsp;</TH>
                <TH class="heading">Vehicle Code&nbsp;</TH>
                <th  class="heading">Permit Validity</th>
                <th  class="heading">Fitness Validity</th>
                <th  class="heading">PUC Validity</th>
                <th  class="heading">Mobile No</th>
                <th  class="heading">IMEI No</th>
                <TH class="heading">Remark&nbsp;</TH>


            <c:forEach var="VehicleType" items="${requestScope['list']}" varStatus="loopCounter">
            <TR class="${loopCounter.index % 2 == 0 ? 'even': 'odd'}" class="bcolor">
            <tr>
            <td id="t1c${IDGenerator.uniqueID}" style="display:none" onclick="fillColumns(id)"> ${VehicleType.vehicle_id}</td>
            <td id="t1c${IDGenerator.uniqueID}"  onclick="fillColumns(id)" align="center"> ${lowerLimit + loopCounter.count- noOfRowsTraversed }</td>
            <td id="t1c${IDGenerator.uniqueID}" class="new_input" onclick="fillColumns(id)">${VehicleType.vehicle_sub_type}</td>
            <td id="t1c${IDGenerator.uniqueID}"  onclick="fillColumns(id)">${VehicleType.vehicle_no}</td>
            <td id="t1c${IDGenerator.uniqueID}"  onclick="fillColumns(id)">${VehicleType.vehicle_code}</td>
            <td id="t1c${IDGenerator.uniqueID}"  onclick="fillColumns(id)">${VehicleType.permit_validity}</td>
            <td id="t1c${IDGenerator.uniqueID}"  onclick="fillColumns(id)">${VehicleType.fitness_validity}</td>
            <td id="t1c${IDGenerator.uniqueID}"  onclick="fillColumns(id)">${VehicleType.puc_validity}</td>
             <td id="t1c${IDGenerator.uniqueID}"  onclick="fillColumns(id)">${VehicleType.mobile_no}</td>
             <td id="t1c${IDGenerator.uniqueID}"  onclick="fillColumns(id)">${VehicleType.imei_no}</td>
            <td id="t1c${IDGenerator.uniqueID}"  class="new_input"  onclick="fillColumns(id)">${VehicleType.remark}</td>

            </tr>
            </TR>
            </c:forEach>

            <tr> <td align='center' colspan="10">
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
                                                            <input class="button" type='submit' name='buttonAction' value='Last' >
                                                        </c:otherwise>
                                                    </c:choose>
                                            </td>
                                            </tr>
        </TABLE>
     <input type="hidden"  name="lowerLimit" value="${lowerLimit}">
     <input type="hidden"  name="noOfRowsTraversed" value="${noOfRowsTraversed}">
     <input  type="hidden" name="vehicleSubType" value="${vehicleSubType}">
     <input  type="hidden" name="vehicleNo" value="${vehicleNo}">
     <input  type="hidden" name="mobileNo" value="${mobileNo}">
        </form>
        <br>
        <br>

            <form  action="VehicleController" method="post">

                           <table  align="center"  class="content" cellpadding="2%" border="1">
                                            <tr id="message">
                                                <c:if test="${not empty message}">
                                                    <td colspan="2" bgcolor="${msgBgColor}"><b>Result: ${message}</b></td>
                                                </c:if>
                                            </tr>
                              <tr><input class="input" type="hidden" id="vehicle_id" name="vehicle_id" value="" ></tr>
                   
                              <tr>

                                 <th class="heading1"> Vehicle Sub Type </th>
                                 <td><input type="text" class="new_input" id="vehicle_sub_type" size="19" name="vehicle_sub_type" value="" disabled></td>

                              </tr>
                              <tr>
                                 <th class="heading1">Vehicle No</th>
                                 <td><input type="text"  class="input " id="vehicle_no" size="19" name="vehicle_no" value="" disabled></td>

                                     <th class="heading1">Vehicle Code</th>
                                 <td><input type="text"  class="input "  id="vehicle_code" size="19" name="vehicle_code" value="" disabled></td>
                              </tr>
                                   <tr>
                                  <th class="heading1">Permit Validity</th>
                                <td>
                                          <input class="input " type="text" id="permit_validity" name="permit_validity" value="" size="12" disabled>
                                 </td>
                               <th class="heading1">Fitness Validity</th>
                                      <td>
                                       <input class="input " type="text" id="fitness_validity" name="fitness_validity" value="" size="12" disabled>
                                 </td>
                              </tr>
                                <tr>
                                 <th class="heading1" title="Pollution Under Control">PUC Validity</th>
                              <td>
                                 <input class="input " type="text" id="puc_validity" name="puc_validity" value="" size="12" disabled>
                              </td>
                                 <th class="heading1" >Mobile No</th>
                              <td>
                                 <input class="input " type="text" id="mobile_no" name="mobile_no" value="" size="12" disabled>
                              </td>
                                </tr>
                              <tr>
                               <th class="heading1" >IMEI No</th>
                              <td>
                                 <input class="input " type="text" id="imei_no" name="imei_no" value="" size="12" disabled>
                              </td>
                                 <th class="heading1">Remark</th>
                                 <td><input type="text" class="new_input" id="remark" size="19" name="remark" value="" disabled></td>
                              </tr>



             <tr>
                 <td colspan="4" align="center">
           <input  class="button" type="button" name="edit" id="edit" value="Edit" onclick="makeEditable(id)" disabled >
           <input class="button" type="submit" name="task" id="save" value="save" onclick="setStatus(id)" disabled >
           <input  class="button" type="submit" name="task" id="save_as" value="Save AS New" onclick="setStatus(id)" disabled>
           <input  class="button" type="submit" name="task" id="delete" value="Delete" onclick="setStatus(id)" disabled>
            <input  class="button" type="reset" name="new" id="new" value="New" onclick="makeEditable(id)" >
            </td>
            </tr>

                          </table>

               <input type="hidden" id="clickedButton" value="">
               <input type="hidden" name="lowerLimit" value="${lowerLimit}">
               <input type="hidden" id="noOfRowsTraversed" name="noOfRowsTraversed" value="${noOfRowsTraversed}">
               <input  type="hidden" name="vehicleSubType" value="${vehicleSubType}">
               <input  type="hidden" name="vehicleNo" value="${vehicleNo}">
               <input  type="hidden" name="mobileNo" value="${mobileNo}">
                </form>
               <tr><td><%@include file="/layout/footer.jsp" %></td> </tr>
                   </table>

            </DIV>
         </td>

      </table>
    </body>
</html>
