<%--
    Document   : leave_type
    Created on : Jan 1, 2002, 12:34:10 AM
    Author     : Navitus1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <script type="text/javascript" src="JS/jquery-1.4.2.min.js"></script>
 <script type="text/javascript" src="JS/jquery.autocomplete.js"></script>
<link href="style/style.css" type="text/css" rel="stylesheet" media="Screen"/>
<link href="style/Table_content.css" type="text/css" rel="stylesheet" media="Screen"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <script type="text/javascript">
         jQuery(function(){
           $("#searchleave_type").autocomplete("leaveTypeCont.do", {
            extraParams: {
                action1: function() { return "getLeave" }
            }
              });
          });
     function fillColumns(id)
     {

        var noOfRowsTraversed = document.getElementById("noOfRowsTraversed").value;
        var noOfColumns = 4;
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
      document.getElementById("leave_type_id").value=document.getElementById(t1id + (lowerLimit + 0)).innerHTML;
      document.getElementById("leave_type").value=document.getElementById(t1id + (lowerLimit + 2)).innerHTML;
      document.getElementById("remark").value=document.getElementById(t1id +(lowerLimit+3)).innerHTML;

        for(var i = 0; i < noOfColumns; i++)
        {
            document.getElementById(t1id + (lowerLimit + i)).bgColor = "#d0dafd";
        }

        $("#message").html("");
        makeEditable('');
    }
    function setDefaultColor(noOfRowsTraversed, noOfColumns) {
        for(var i = 0; i < noOfRowsTraversed; i++) {
            for(var j = 1; j <= noOfColumns; j++) {
                document.getElementById("t1c" + (i * noOfColumns + j)).bgColor = "";
            }
        }
    }
      function makeEditable(id)
      {
        document.getElementById("leave_type").disabled = false;
        document.getElementById("remark").disabled = false;
        document.getElementById("save").disabled = true;
        document.getElementById("update").disabled = false;
        document.getElementById("delete").disabled = false;
        document.getElementById("save_As").disabled = false;
        if(id == 'new')
        {
        $("#message").html("");
        document.getElementById("update").disabled = true;
        document.getElementById("delete").disabled = true;
        document.getElementById("save_As").disabled = true;
        document.getElementById("save").disabled = false;
        document.getElementById("leave_type").disabled=false;
        document.getElementById("remark").disabled=false;
        document.getElementById("leave_type").focus();
        }
      }
      function setStatus(id)
      {
        if(id == 'save')
        {
            document.getElementById("clickedButton").value = "Save";
        }else if(id == 'update'){
                    document.getElementById("clickedButton").value = "Update";
                } else if(id == 'save_As'){
            document.getElementById("clickedButton").value = "Save AS New";
        }

        else document.getElementById("clickedButton").value = "Delete";
    }
    function verify()
    {
                var result;

                if(document.getElementById("clickedButton").value == 'Save' || document.getElementById("clickedButton").value == 'Save AS New' || document.getElementById("clickedButton").value == 'Update') {
                    if((document.getElementById("leave_type").value).trim().length == 0) {
                        //document.getElementById("message").innerHTML = "<td colspan='6' bgcolor='coral'><b>Key Person Name is required...</b></td>";
                        $("#message").html("<td colspan='6' bgcolor='coral'><b>Item Name is required...</b></td>");
                        document.getElementById("leave_type").focus();
                        return false; // code to stop from submitting the form2.
                    }
                    if(result == false)
                    {
                        // if result has value false do nothing, so result will remain contain value false.
                    }
                    else{

                        result = true;
                    }
                    if(document.getElementById("clickedButton").value == 'Save AS New'){
                        result = confirm("Are you sure you want to save it as New record?")
                        return result;
                    }
                } else result = confirm("Are you sure you want to delete this record?")
                return result;
            }
    </script>
    <body>
        <table align="center" class="main" cellpadding="0" cellspacing="0" border="1" width="1000px">

            <tr>
                <td><%@include file="/layout/header.jsp" %></td>
            </tr>
            <tr>
                <td><%@include file="/layout/menu.jsp" %></td>
            </tr>

            <tr>
                 <td>
                     <div class="maindiv" id="body">
                       <form name="form" action="leaveTypeCont.do">

                           <table width="100%" align="center" >
                                   <tr align="center">
                                        <td>

                                                   <span class="heading1">Leave Type</span>
                                                   <input class="input" type="text" id="searchleave_type" name="searchleave_type" value="${searchleave_type}" size="30">
                                                   <input class="button" type="submit" name="task" id="searchIn" value="Search">
                                                   <input class="button" type="submit" name="task" id="showAllRecords" value="Show All Records">

                                          </td>
                                    </tr>
                           </table>
                      </form>
                        <table>
                            <tr>
                                <td align="center">
                                    <div  class="content_div" style="width:990px" >
                                   
                                       

                                        <form name="form1" action="leaveTypeCont.do">
                                           <TABLE BORDER="1" align="center" cellpadding="5" class="content" >
                                                <TR>
                                                     <th class="heading" >S.no</th>
                                                     <th class="heading" >Leave Type</th>
                                                     <th class="heading" >remark</th>
                                                </TR>
                                                <c:forEach var="leave_type" items="${requestScope['list']}" varStatus="loopCounter">
                                                  <TR class="${loopCounter.index % 2 == 0 ? 'even': 'odd'}" class="bcolor">
                                                    <td id="t1c${IDGenerator.uniqueID}" style="display:none" onclick="fillColumns(id)">${leave_type.leave_type_id}</td>
                                                    <td id="t1c${IDGenerator.uniqueID}" onclick="fillColumns(id)" align="center">${lowerLimit + loopCounter.count- noOfRowsTraversed}</td>
                                                    <TD id="t1c${IDGenerator.uniqueID}" onclick="fillColumns(id)">${leave_type.leave_type}</TD>
                                                    <TD id="t1c${IDGenerator.uniqueID}"  onclick="fillColumns(id)">${leave_type.remark}</TD>
                                                  </TR>
                                                </c:forEach>
                                            <tr> <td align='center' colspan="5">
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

                                                                             <input type="hidden"  id="lowerLimit" name="lowerLimit" value="${lowerLimit}">
                                                                             <input type="hidden"  id="searchleave_type" name="searchleave_type" value="${searchleave_type}">

                                                                                <input type="hidden" id="noOfRowsTraversed" name="noOfRowsTraversed" value="${noOfRowsTraversed}">
                                                                            </form>
                                        </div>
                                        <div  style="width:990px" >
                                            <form name="form2" action="leaveTypeCont.do" onsubmit="return verify()">
                                                                                <TABLE BORDER="1" class="content" align="center" cellpadding="5%" width="52%">
                                                                                <tr id="message">
                                                                                        <c:if test="${not empty message}">
                                                                                            <td colspan="6" bgcolor="${msgBgColor}"><b>Result: ${message}</b></td>
                                                                                        </c:if>
                                                                                </tr>

                                                                                <tr>
                                                                                   <td>Leave Type</td>
                                                                                   <td><input class="input" type="hidden" id="leave_type_id" name="leave_type_id" value="" >
                                                                                   <input class="input" type="text" id="leave_type" name="leave_type" value="" disabled>
                                                                                </td>
                                                                               </tr>
                                                                               <tr>
                                                                                     <td>remark</td>
                                                                                     <td><input type="text" id="remark" name="remark" value="" disabled></td>
                                                                               </tr>
                                                                               <tr><td colspan="2" >
                                                                                    <input class="button"  type="submit" name="task" id="update" value="Update" onclick="setStatus(id)"  disabled>
                                                                                    <input class="button"  type="submit" name="task" id="save" value="Save" onclick="setStatus(id)"  disabled>
                                                                                    <input class="button" type="submit" name="task" id="save_As" value="Save AS New" onclick="setStatus(id)"  disabled>
                                                                                    <input class="button" type="reset" name="new" id="new" value="New" onclick="makeEditable(id)" >
                                                                                    <input class="button" type="submit" name="task" id="delete" value="Delete" onclick="setStatus(id)" disabled>
                                                                                 <input type="hidden"  name="lowerLimit" value="${lowerLimit}">
                                                                                <input type="hidden" id="noOfRowsTraversed" name="noOfRowsTraversed" value="${noOfRowsTraversed}">
                                                                                <input type="hidden"  id="searchleave_type" name="searchleave_type" value="${searchleave_type}">
                                                                                <input type="hidden" id="clickedButton" name="clickedButton" value="">

                                                                  </td></tr>
                                                                  </TABLE>
                                            </form>
                                         </div>



                                </td>
                            </tr>
                        </table>

                     </div>
                </td>
            </tr>
            <tr>
                <td><%@include file="/layout/footer.jsp" %></td>
            </tr>
        </table>
    </body>
</html>