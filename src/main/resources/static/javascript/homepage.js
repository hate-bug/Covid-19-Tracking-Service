$(document).ready(function () {

    setUserAccountSection();

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
            alert(data);
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
            $.each(data, function (index, value) {
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

            if (request.getResponseHeader("has-next-page")=="true"){
                $("#nextpagebutton").show();
                $("#nextpagebutton").attr("pagenumber", parseInt(currentPageNum, 10)+1);
            }
            if (currentPageNum>1){
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
        $("#welcomesection").hide();
        $("#loginsection").show();
        $("#exit").show();
    });

    $("#registerbutton").click(function () {
        $("#loginsection").hide();
        $("#registersection").show();
    });

    $("#loginsubmit").click(function () {
        var user = new Object();
        user.emailAddress = $("#emaillogin").val();
        user.password = $("#passwordlogin").val();
        var data = JSON.stringify(user);
        $.ajax({
            url: "/userlogin",
            type: "POST",
            data: data,
            dataType: "text",
            contentType: "application/json"
        }).done(function (data) {
            sessionStorage.setItem("useremailaddress", user.emailAddress);
            alert(data);
            setUserAccountSection();
            $("#loginsection").hide();
            $("#welcomesection").show();
            $("#exit").hide();
        }).fail(function (data) {
            alert(data.responseText);
        });
    });

    $("#registersubmit").click(function () {
        if ($("#passwordregister").val()!= $("#passwordconfirm").val()){
            alert("password not the same, please check");
            return;
        }
        var user = new Object();
        user.emailAddress = $("#emailregister").val();
        user.password = $("#passwordregister").val();
        var postdata = JSON.stringify(user);
        $.ajax({
            url: "/userregister",
            type: "POST",
            data: postdata,
            dataType: "text",
            contentType: "application/json"
        }).done(function (data) {
            alert(data);
            $("#registersection").hide();
            $("#loginsection").show();
        }).fail(function () {
            alert("Server error;")
        });
    });

    function setUserAccountSection() {
        if (sessionStorage.getItem("useremailaddress")!=null){//user already logged in
            var emailAddress = sessionStorage.getItem("useremailaddress");
            $("#useremail").text(emailAddress);
            $("#useraccountsection").show();
            $("#loginbutton").hide();
        } else {
            $("#loginbutton").show();
            $("#useraccountsection").hide();
        }
    }

    $("#logoutbutton").click(function () {
        sessionStorage.removeItem("useremailaddress");
        setUserAccountSection();
    });
});
