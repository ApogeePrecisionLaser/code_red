<%--
    Document   : mapWindow
    Created on : Sep 29, 2014, 11:36:35 AM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <script src="http://maps.googleapis.com/maps/api/js?key=AIzaSyDOT5yBi-LAmh9P2X0jQmm4y7zOUaWRXI0"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!--        <META http-equiv="REFRESH" content="60">-->
        <script type="text/javascript" src="JS/jquery-1.4.2.min.js"></script>
        <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <title>Map View</title>
        <script type="text/javascript" language="javascript">
            var markers = [];
            var waypoints = [];
            var map;
            var start;
            var end;
            var length = 0;

            //            var directionsService = new google.maps.DirectionsService();
            function bool()
            {
                var mapOptions = {
                    zoom: 12,
                    // 29.940822 78.1281141
                    center: new google.maps.LatLng(29.91, 78.1),
                    travelMode: google.maps.TravelMode.DRIVING
                };

                map = new google.maps.Map(document.getElementById('googleMap'),
                        mapOptions);
                initialize();
            }

//            var ini=0;
            var coordinateCount = 0;

            function initialize()
            {
//                alert(++ini);
                var bool = remove();
//                alert(bool);
                coordinateCount;
                //                var array = new Array();
                //                for(var i = 1; i <= coordinateCount; i++)
                //                {
                //                    array[i - 1] = new google.maps.LatLng($("#lati"+i).val(),$("#longi"+i).val());
                //                }
                //                var locations = array;
                //locations = [new google.maps.LatLng($("#longi").val(), $("#latti").val())];//(23.153982100000000,79.907076500000000)];//,
                //                    new google.maps.LatLng(23.164302900000000,79.893905500000000),
                //                    new google.maps.LatLng(23.155434900000000,79.915218900000000)];

                //                for (var i=0; i < locations.length; i++)
                //                {
                //                    markers[i] = new google.maps.Marker({
                //                        position: locations[i],
                //                        map: map,
                //                        url: 'http://www.google.com/',
                //                        title: locations[i].lat()+", "+locations[i].lng()+" "+"<a href='www.google.com'>Google Link</a>"
                //                    });
                //                    google.maps.event.addListener(markers[i], 'click', function() {
                //                        window.open(this.url);  //changed from markers[i] to this
                //                    });
                //
                //                }

                directionsDisplay = new google.maps.DirectionsRenderer();
                directionsDisplay.setMap(map);
                $.ajax({url: "ShiftShowController?task=showMapWindow1",
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json',
                    context: document.body,
                    success: function (response_data)
                    {
                        var data = response_data.data;
//                        alert("data: "+data);
//                        if(data.length==0)
//                        {
//                            setTimeout(function(){
//                                window.location.reload(1);
//                            }, 15000);
//                        }
                        var mark;
                        mark = "img/marker.png";
                        //alert("mmm -"+mark);
                        for (var i = 0; i < data.length; i++)
                        {//alert("lat -"+data[i].latitude);
                            //alert("long -"+data[i].longitude);
                            //alert("mark -"+mark);
                            markers[i] = new google.maps.Marker({
                                position: new google.maps.LatLng(data[i].latitude, data[i].longitude),
                                map: map,
                                icon: mark
                                        //url: 'http://www.google.com/',
                                        //title: data[i].key_person_name + ": - " + data[i].mobile_no
                            });
                            //alert("marker -"+markers[i]);
                            google.maps.event.addListener(markers[i], 'click', function () {
                                window.open(this.url);  //changed from markers[i] to this
                            });
//                            $("#lati"+(i+1)).val(data[i].latitude);
//                            $("#longi"+(i+1)).val(data[i].longitude);
                        }
                        coordinateCount = data.length;
//                        alert("recursion");
                        setTimeout(initialize, 1000);
                    }
                });
            }

            function remove() {
                if (markers != null)
                {
                    for (var ii = 0; ii < coordinateCount; ii++)
                    {
                        markers[ii].setMap(null);
                    }
                    return true;
                }
            }
            google.maps.event.addDomListener(window, 'load', bool);
        </script>
    </head>
    <body>
        <div id="googleMap" style="width:1450px;height:650px;text-align: center"></div><!--width:1500px;height:650px;-->
        <input type="hidden" id="longi" value="${longi}" >
        <input type="hidden" id="latti" value="${latti}" >
        <c:forEach var="Coordinates" items="${requestScope['CoordinatesList']}" varStatus="loopCounter">
            <c:set var="cordinateLength"  value="${loopCounter.count}"></c:set>
            <input type="hidden" id="lati${loopCounter.count}" value="${Coordinates.latitude}">
            <input type="hidden" id="longi${loopCounter.count}" value="${Coordinates.longitude}">
        </c:forEach>
        <input type="hidden" id="count" value="${cordinateLength}">
    </body>
</html>
