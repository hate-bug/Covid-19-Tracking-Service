$(document).ready(function () {

    $("#createpatient").click(function () {
        $("#welcomesection").hide();
        $("#patientinfo").show();
    });

    $("#addevent").click(function () {
        var row = "<tr style=\"text-align:center\">" +
            "<td><input type='text' name='name'></td>" +
            "<td><input type='date' name='date'></td>" +
            "<td><input type='text' name='address'></td>" +
            "<td><input type='text' name='longitude'></td>" +
            "<td><input type='text' name='latitude'></td>" +
            "</tr>";
        $("#eventtable").append(row);
    });

    $("#submitpatient").click(function () {
        var info = $("#eventtable tr").get().map(function (tr) {
            return $(tr).find("input").get().map(function (input) {
                return input.value;
            })
        });
        info.shift();
        var events = [];
        console.log(info);
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
        var data = JSON.stringify(events);

        $.ajax({
            url: "/patientinfo",
            data: data,
            type: "POST",
            dataType: "json",
            contentType: "application/json"
        }).done(function (data) {
            alert(data);
        });
    });

    $("#showevents").click(function () {
        $.ajax({
           url: "/allEvents",
           type: "GET"
        }).done(function (data) {
            $.each(data, function (index, value) {
                var eventname = value.name;
                var eventdate = value.date;
                var place = value.place;
                var address = place.address;
                var longitude = place.longitude;
                var latitude = place.latitude;
                var num = (value.attendedPatients).length;
                var row = "<tr style=\"text-align:center\">" +
                    "<td>"+ eventname +"</td>" +
                    "<td>"+ eventdate +"</td>" +
                    "<td>"+ address +"</td>" +
                    "<td>"+ longitude +"</td>" +
                    "<td>"+ latitude +"</td>" +
                    "<td>"+ num +"</td>" +
                    "</tr>"
                $("#eventlist").append(row);
                $("#welcomesection").hide();
                $("#eventsection").show();
            });
        });
    });

});
