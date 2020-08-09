$(document).ready(function () {

    var apiKey = "AIzaSyCYf-bigRT20CHeW79bBbnBjtp0RPeTfes"; //restricted api key
    checkuser();
    $("#exit").hide();
    $("#createpatient").click(function () {
        $("#eventtable > tbody").html("");
        $("#welcomesection").hide();
        $("#patientinfo").show();
        $("#exit").show();
    });

    function getRow (){
        var row = "<tr style=\"text-align:center\">" +
            "<td><input type='text' class='eventname' name='eventname'></td>" +
            "<td><input type='date' max='2030-12-31' name='date'></td>" +
            "<td><input type='text' name='address' class='address'></td>" +
            "</tr>";
        return row;
    }

    $("#addevent").click(function () {
        var existFunction = false;
        var info = $("#eventtable tr").get().map(function (tr) {
            return $(tr).find("input").get().map(function (input) {
                return input.value;
            })
        });
        info.shift();
        if (info.length > 0) {
            var event = new Object();
            var place = new Object();
            $.each(info[info.length - 1], function (index, value) {
                if (value == "" || value == 'undefined') {
                    alert("Please complete patient information.");
                    existFunction = true;
                    return false;
                }
                if (index % 3 == 0) { //Event name
                    event.name = value.trim();
                } else if (index % 3 == 1) {//Event date
                    event.date = value;
                } else if (index % 3 == 2) {//Event Address
                    place.address = value;
                    event.place = place;
                    getLngLat(event, info);
                }
            });
            if (existFunction == true){
                return;
            }
        } else {
            var row = getRow();
            $("#eventtable tbody").append(row);
        }
    });

    function getLngLat(event, info) {
        var coordinates = new Object();
        var geocoder = new google.maps.Geocoder();
        geocoder.geocode( { 'address': event.place.address}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                var lat = results[0].geometry.location.lat();
                var lon = results[0].geometry.location.lng();
                event.place.latitude = lat;
                event.place.longitude = lon;
                $.ajax({
                    url: "/eventinfo",
                    method: "POST",
                    data: JSON.stringify(event),
                    contentType: "application/json"
                }).done(function () {
                    var span = $('<span />').attr('class', 'greencheck').html("&#10003; Saved");
                    var td = $('<td />').append(span);
                    $("#eventtable tr").eq(info.length).append(td);
                    var row = getRow();
                    $("#eventtable tbody").append(row);
                }).fail(function () {
                    alert("server error, please check your input");
                });
            } else {
                alert("Geocode was not successful for the following reason: " + status);
            }
        });

    }

    $("#submitpatient").click(function () {
        var existFunction = false;
        var info = $("#eventtable tr").get().map(function (tr) {
            return $(tr).find("input").get().map(function (input) {
                return input.value;
            })
        });
        info.shift();
        if (info.length>0){
            var event = new Object();
            var place = new Object();
            $.each(info[info.length-1], function (index, value) {
                if (value == "" || value == 'undefined') {
                    alert("Please complete patient information.");
                    existFunction = true;
                    return false;
                }
                if (index%3 == 0){ //Event name
                    event.name = value.trim();
                } else if (index%3 == 1){//Event date
                    event.date = value;
                } else if (index%3 == 2){//Event Address
                    place.address = value;
                    event.place = place;
                    getLngLat(event, info);
                }
            });
            if (existFunction == true){
                return;
            }
            $.ajax({
                url: "/submitpatient" ,
                method: "PUT"
            }).done(function () {
                alert("Data posted");
                $("#exit").hide();
                $("#welcomesection").show();
                $("#patientinfo").hide();
                $("#eventtable > tbody").html("");
            }).fail(function () {
                alert("Server error.");
            });
        } else {
            alert("Please input data first");
        }
    });

    function showEvents(currentPageNum){
        let eventset = new Set();
        $("#eventlist > tbody").html("");
        var heatmapData = [];
        var requestUrl = "";
        var eventsize;
        createMap(heatmapData); //clean heatmap
        if ($("#verifycheck").is(":checked")){ //show only verified user
            requestUrl = "/patientEventAssociations/search/findAllByisValid?isValid=true&";
        } else {
            requestUrl = "/patientEventAssociations";
        }
        $.ajax({
            url: requestUrl+"?page="+currentPageNum,
            type: "GET"
        }).done(function (data, textStatus, request) {
            var content = data._embedded.patientEventAssociations;
            eventsize = content.length;
            $.each(content, function (index, value) {
                var eventUrl = value._links.event.href;
                $.ajax({
                   url: eventUrl,
                   type: "GET"
                }).done (function (value) {
                    var eventname = value.name;
                    var eventdate = value.date;
                    if (eventdate!=null && eventdate.indexOf("T")!=-1){
                        eventdate = (eventdate.split("T"))[0];//only show the yyyy-mm-dd.
                    }
                    var placeurl = value._links.place.href;
                    var associationurl = value._links.patientEventAssociations.href;
                    $.ajax({
                        url: placeurl,
                        type: "GET"
                    }).done(function (data) {
                        var address = data.address;
                        var longitude = data.longitude;
                        var latitude = data.latitude;
                        heatmapData.push(new google.maps.LatLng(latitude, longitude));
                        if (heatmapData.length == eventsize) {
                            createMap(heatmapData);
                        }
                        $.ajax({
                            url: associationurl,
                            type: "GET"
                        }).done(function (data) {
                            var num = 0;
                            if ($("#verifycheck").is(":checked")){
                                $.each (data._embedded.patientEventAssociations, function (index, value) {
                                    if (value.verified){
                                        num++;
                                    }
                                });
                            } else {
                                num = data._embedded.patientEventAssociations.length;
                            }
                            var eventEntity = new Object();
                            var place = new Object();
                            eventEntity.eventname = eventname;
                            eventEntity.eventdate = eventdate;
                            eventEntity.address = address;
                            place.longitude = longitude;
                            place.latitude = latitude;
                            place.address = address;
                            eventEntity.place = place;
                            var eventstring = JSON.stringify(eventEntity);
                            if (!eventset.has(eventstring)){
                                eventset.add(eventstring);
                                var row = "<tr style=\"txt-align:center\">" +
                                    "<td>"+ eventname +"</td>" +
                                    "<td>"+ eventdate +"</td>" +
                                    "<td>"+ address +"</td>" +
                                    "<td>"+ longitude +"</td>" +
                                    "<td>"+ latitude +"</td>" +
                                    "<td>"+ num +"</td>" +
                                    "</tr>";
                                $("#eventlist tbody").append(row);
                            }
                        });
                    });
                });
            });
            $("#nextpagebutton").hide();
            $("#lastpagebutton").hide();

            if (data._links.hasOwnProperty("next")){
                $("#nextpagebutton").show();
                $("#nextpagebutton").attr("pagenumber", parseInt(currentPageNum, 10)+1);
            }
            if (data._links.hasOwnProperty("prev")){
                $("#lastpagebutton").show();
                $("#lastpagebutton").attr("pagenumber", parseInt(currentPageNum, 10)-1);
            }
            $("#welcomesection").hide();
            $("#eventsection").show();
            $("#exit").show();
        });
    }

    $("#nextpagebutton").click(function () {
        var pagenum = $("#nextpagebutton").attr("pagenumber");
        showEvents(pagenum);
    });

    $("#lastpagebutton").click(function () {
        var pagenum = $("#lastpagebutton").attr("pagenumber");
        showEvents(pagenum);
    });

    $("#showevents").click(function () {
       showEvents(0);
    });

    $("#registerbutton").click(function () {
        $("#loginsection").hide();
        $("#registersection").show();
    });


    function checkuser() {
        $.ajax({
            url:"/isloggedin",
            method: "GET"
        }).done(function (data) {
            $("#loginsection").hide();
            $("#userinfosection").show();
            $("#useremail").text(data);
        }).fail(function () {
            $("#loginsection").show();
            $("#userinfosection").hide();
        });
        $.ajax({
            url:"/admin",
            method: "GET",
            success: function () {
                $("#adminpage").show();
            }
        });
    }

    $("#logoutbutton").click(function () {
        $.ajax({
            method:"GET",
            url: "/logout"
        }).done(function () {
            window.location="/";
        });
    });

    $("#loginbutton").click(function () {
        window.location = "/login";
    });

    $("#showform").click(function () {
        $("#welcomesection").hide();
        $("#formsection").show();
        $("#exit").show();
    });

    $("#adminpage").click(function () {
        window.location="/admin"
    });

    $("#applicationform").submit(function (e) {
        e.preventDefault();
        var applicant = new Object();
        applicant.applicantEmail = $("#formemail").val();
        applicant.description = $("#formdescription").val();
        $.ajax({
            url: "/postapplication",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(applicant)
        }).done(function () {
            alert("Form posted.")
            window.location="/";
        }).fail(function () {
            alert("Server error, please submit again");
        });
    });

    $("#changepasswordbutton").click(function () {
        window.location = "/changepassword";
    });

    $("#eventtable tbody").on("click", 'input.address', function () {
        geolocate(this);
    });



    $("#eventtable tbody").on("input click", 'input.eventname', function () {
        var inputElement = this;
        var eventinput = this.value;
        var recommendlist = [];
        let eventSet = new Set();
        $.ajax({
            type: "GET",
            url: "/events/search/findAllByNameContainingIgnoreCase?name="+eventinput
        }).done(function (result) {
            $.each(result._embedded.events, function (index, value) {
                eventSet.add(value.name);
            });
            recommendlist = Array.from(eventSet);
            $(inputElement).autocomplete({
                name: "eventrecommend",
                source: recommendlist,
                open: function() {
                    var position = $(inputElement).position(),
                        left = position.left, top = position.top;

                    $("#results > ul").css({left: left + 20 + "px",
                        top: top + 4 + "px" });

                }
            });
        });

    });

    var placeSearch, autocomplete;


    function initAutocomplete(element) {

        autocomplete = new google.maps.places.Autocomplete(element, {types: ['geocode','establishment']});

        autocomplete.setFields(['address_component']);

        //autocomplete.addListener('place_changed', function() {fillInAddress(element)});
    }

    // Bias the autocomplete object to the user's geographical location,
    // as supplied by the browser's 'navigator.geolocation' object.
    function geolocate(element) {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                var geolocation = {
                    lat: position.coords.latitude,
                    lng: position.coords.longitude
                };
                var circle = new google.maps.Circle(
                    {center: geolocation, radius: position.coords.accuracy});
                autocomplete.setBounds(circle.getBounds());
            });
        }
        initAutocomplete(element);
    }

    $("#verifycheck").change(function () {
        showEvents(0);
    });

    function createMap(heatmapData) {
        //var center = new google.maps.LatLng(54.170193, -96.857464);
        var map = new google.maps.Map(document.getElementById("googlemap"), {
            //center: center,
            zoom: 4,
            mapTypeId: 'roadmap'
        });
        var bounds = new google.maps.LatLngBounds();
        $.each(heatmapData, function (index, value) {
            bounds.extend(value);
        });
        map.fitBounds(bounds);
        heatmap = new google.maps.visualization.HeatmapLayer({
            data: heatmapData,
            map: map
        });
        heatmap.set("radius,30");
    }

});
