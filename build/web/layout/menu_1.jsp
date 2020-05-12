

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<!DOCTYPE html>

<html>
    <head>
        <title>Menu</title>
        <script type="text/javascript">
            $(function() {
                if ($.browser.msie && $.browser.version.substr(0,1)<7)
                {
                    $('li').has('ul').mouseover(function(){
                        $(this).children('ul').show();
                    }).mouseout(function(){
                        $(this).children('ul').hide();
                    })
                }
            });

            $(document).ready(function(){
                var mouseX;
                var mouseY;
                $(document).mousemove( function(e) {
                    mouseX = e.pageX;
                    mouseY = e.pageY;
                });

                $("#close_div").click(function(){
                    //this.title = "fffff</div>";
                    $("#popup").hide();
                    $('#ride_no_from').val("");
                    $('#ride_no_to').val("");

                });

                $("#logout").click(function(){
                    $('#popup').css( {
                        'position': 'absolute',
                        'left': mouseX,
                        'top': mouseY
                    });
                    $("#make_work_order_btn").val("quickWorkOrder");
                    $("#popup").show();

                });

            });





            //        function logout(){debugger;
            //         var ride_no_from = $("#ride_no_from").val();
            //         var ride_no_to = $("#ride_no_to").val();
            //
            //         $.ajax({url: "shiftLoginCont.do?task=ride_detail",
            //             data: "ride_no_from="+ ride_no_from +"&ride_no_to="+ ride_no_to +"",
            //             success: function(response_data) {
            //                 var affacted = response_data.trim().split("#&");
            //
            //                 if(parseInt(affacted[0]) > 0){
            //                     doc.getElementById("message").innerHTML = "<td colspan='5' bgcolor='yellow'><b>Ride Detail is Saved...</b></td>";
            //                     $("#ride_no_from").val(ride_no_from);
            //                     $("#ride_no_to").val(affacted[1]);
            //                     $("#driver").val(quick_driver_name);
            //                 }else
            //                     doc.getElementById("message").innerHTML = "<td colspan='5' bgcolor='coral'><b>Ride Detail is not saved some error!</b></td>";
            //                 $("#order_detail_div").hide();
            //                //$("#message").html("<td colspan='5' bgcolor='coral'><b>Location Name is required...</b></td>");
            //             }
            //         });
            //        }
        </script>
        <link type="text/css" href="style/menu.css" rel="stylesheet"/>
    </head>
    <body>
        <div>
            <ul id="menu">
                <li><a id="mpeb" href="#">Data Entry</a>
                    <ul>
                        <li><a id="" href="PurposeHeaderCont.do">Complain Header</a></li>
                        <li><a id="" href="complaintCont.do">Complaint</a></li>
                    </ul>
                </li>
                
                <li id="loginCont.do?task=logout"><a id="logout" href="loginCont.do?task=logout">LogOut</a>
            </ul>
        </div>
        <form name="log" action="shiftLoginCont.do" method="post">
            <input  type="hidden" name="task" id="logout" value="Logout" >
        </form>
        <!--
         <div id="popup" align="center" style="display: none; background-color: white; border: black; border-width: 2px;border-style: solid">
             <form action="shiftLoginCont.do" method="post">
                    <a id="close_div">Close</a><br>
                    <Span><b><u>Provide Ride Details</u></b></Span><br>
                    <Span id="show_message" style="display: none; background: red"></Span><br>
                    <b>Ride No From</b><br>
                    <input type="text" id="ride_no_from" name="ride_no_from" value=""><br>
                    <b>Ride No To</b><br>
                    <input class="new_input" type="text" id="ride_no_to" name="ride_no_to" value=""><br>
                    <input class="button" type="submit" name="task" id="save" value="save" onclick="setStatus(id)">
                    <input class="" type="hidden" id="make_work_order_btn" name="" value="">
             </form>
        </div>
        -->

    </body>
</html>
