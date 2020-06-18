$(document).ready(function () {

    checkuser();

    $("#createpatient").click(function () {
        $("#welcomesection").hide();
        $("#patientinfo").show();
        $("#exit").show();
    });

    $("#addevent").click(function () {
        var row = "<tr style=\"text-align:center\">" +
            "<td><input type='text' name='name'></td>" +
            "<td><input type='date' name='date'></td>" +
            "<td><input type='text' name='address'></td>" +
            "<td><input type='number' name='longitude'></td>" +
            "<td><input type='number' name='latitude'></td>" +
            "</tr>";
        $("#eventtable tbody").append(row);
    });

    $("#submitpatient").click(function () {
        var info = $("#eventtable tr").get().map(function (tr) {
            return $(tr).find("input").get().map(function (input) {
                return input.value;
            })
        });
        info.shift();
        var events = [];
        for (var i=0; i<info.length; i++){
            var event = new Object();
            var place = new Object();
            $.each(info[i], function (index, value) {
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
                    events.push(event);
                }
            });
        }
        var patientdata = JSON.stringify(events);

        $.ajax({
            url: "/patientinfo",
            data: patientdata,
            type: "POST",
            dataType: "text",
            contentType: "application/json"
        }).done(function (data) {
            alert("Data posted");
            $("#exit").hide();
            $("#welcomesection").show();
            $("#patientinfo").hide();
            $("#eventtable tbody > tr").empty();
        }).fail(function (data) {
            alert("Server Error, please confirm data try again.");
        });

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

    $("#loginbutton").click(function () {
        /*$("#welcomesection").hide();
        $("#loginsection").show();
        $("#exit").show();*/
        window.location.href="/login";
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

    }

    $("#logoutbutton").click(function () {
        $.ajax({
            method:"GET",
            url: "/logout"
        }).done(function () {
            localStorage.removeItem("useremail");
            window.location="/";
        });
    });

    $("#profilepagebutton").click(function () {
        window.location = "/profilepage";
    });
});
