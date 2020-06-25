$(document).ready(function () {

    var apiKey = "AIzaSyCYf-bigRT20CHeW79bBbnBjtp0RPeTfes"; //restricted api key
    checkuser();
    $("#createpatient").click(function () {
        $("#welcomesection").hide();
        $("#patientinfo").show();
        $("#exit").show();
    });

    function getRow (){
        var row = "<tr style=\"text-align:center\">" +
            "<td><input type='text' name='name'></td>" +
            "<td><input type='date' name='date'></td>" +
            "<td><input type='text' name='address' class='address'></td>" +
            "<td><input type='number' class='longitude'></td>" +
            "<td><input type='number' class='latitude'></td>" +
            "</tr>";
        return row;
    }

    $("#addevent").click(function () {
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
                if (index%5 == 0){ //Event name
                    event.name = value;
                } else if (index%5 == 1){//Event date
                    event.date = value;
                } else if (index%5 == 2){//Event Address
                    place.address = value;
                }else if (index%5 == 3){//longitude
                    place.longitude = value;
                } else if (index%5 == 4){//latitude
                    place.latitude = value;
                    event.place = place;
                }
            });
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
                alert ("server error, please check your input");
            });
        } else {
            var row = getRow();
            $("#eventtable tbody").append(row);
        }
    });

    $("#submitpatient").click(function () {
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
                if (index%5 == 0){ //Event name
                    event.name = value;
                } else if (index%5 == 1){//Event date
                    event.date = value;
                } else if (index%5 == 2){//Event Address
                    place.address = value;
                }else if (index%5 == 3){//longitude
                    place.longitude = value;
                } else if (index%5 == 4){//latitude
                    place.latitude = value;
                    event.place = place;
                }
            });
            $.ajax({
                url: "/eventinfo",
                method: "POST",
                data: JSON.stringify(event),
                contentType: "application/json"
            }).done(function () {
                var td = $('<td />').append(span);
                $("#eventtable tr").eq(info.length).append(td);
            }).fail(function () {
                alert ("server error, please check your input");
            });
            $.ajax({
                url: "/submitpatient" ,
                method: "PUT"
            }).done(function () {
                alert("Data posted");
                $("#exit").hide();
                $("#welcomesection").show();
                $("#patientinfo").hide();
                $("#eventtable tbody > tr").empty();
            }).fail(function () {
                alert("Server error.");
            });
        } else {
            alert("Please input data first");
        }
    });

    $("#exit").click(function () {
        $("#exit").hide();
    });

    function showEvents(currentPageNum){
        $("#eventlist tbody").empty();
        $.ajax({
            url: "/allEvents?page="+currentPageNum,
            type: "GET"
        }).done(function (data, textStatus, request) {
            var content = data.content;
            $.each(content, function (index, value) {
                var eventname = value.name;
                var eventdate = value.date;
                if (eventdate!=null && eventdate.indexOf("T")!=-1){
                    eventdate = (eventdate.split("T"))[0];//only show the yyyy-mm-dd.
                }
                var place = value.place;
                var address = place.address;
                var longitude = place.longitude;
                var latitude = place.latitude;
                var num = (value.patientEventAssociations).length;
                var row = "<tr style=\"text-align:center\">" +
                    "<td>"+ eventname +"</td>" +
                    "<td>"+ eventdate +"</td>" +
                    "<td>"+ address +"</td>" +
                    "<td>"+ longitude +"</td>" +
                    "<td>"+ latitude +"</td>" +
                    "<td>"+ num +"</td>" +
                    "</tr>";
                $("#eventlist tbody").append(row);
            });
            $("#nextpagebutton").hide();
            $("#lastpagebutton").hide();

            if (data.last==false){
                $("#nextpagebutton").show();
                $("#nextpagebutton").attr("pagenumber", parseInt(currentPageNum, 10)+1);
            }
            if (data.first==false){
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
       showEvents(1);
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


    var placeSearch, autocomplete;


    function initAutocomplete(element) {

        autocomplete = new google.maps.places.Autocomplete(element, {types: ['geocode']});

        autocomplete.setFields(['address_component']);

        autocomplete.addListener('place_changed', function() {fillInAddress(element)});
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

    function fillInAddress(element) {
        var address = element.value;
        var addrcomponent = address.split(" ");
        var addressValue="";
        if (addrcomponent.length > 2){
            for (var i=0; i<addrcomponent.length; i++){
                if (i==addressValue.length-1){
                    addressValue = addressValue + addrcomponent[i];
                } else{
                    addressValue = addressValue + addrcomponent[i] + "+";
                }
            }
            $.ajax({
                url: "https://maps.googleapis.com/maps/api/geocode/json?address="+addressValue+",&key="+apiKey,
                method: "GET"
            }).done(function (data) {
                var lat = data.results[0].geometry.location.lat;
                var lon = data.results[0].geometry.location.lng;
                var tr = $(element).parent().parent();
                $(tr[0]).find('.longitude').val(lon);
                $(tr[0]).find('.latitude').val(lat);
            });
        }

    }

});
