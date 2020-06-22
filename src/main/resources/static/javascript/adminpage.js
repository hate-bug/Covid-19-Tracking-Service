$(document).ready(function () {

    getApplicantList(1);
    function getApplicantList (pagenum) {
        $("#applicantlist tbody").empty();
        $.ajax({
            url: "/admin/getapplicants?page="+pagenum,
            method: "GET"
        }).done(function (data) {
            var content = data.content;
            $.each(content, function (index, value) {
                var applicantID = value.id;
                var applicantEmail = value.applicantEmail;
                var applicantDescription = value.description;
                var row = "<tr style=\"text-align:center\">" +
                    "<td>" + applicantEmail + "</td>" +
                    "<td><html>" + applicantDescription + "</html></td>" +
                    "<td><button class='approve' num='"+ applicantID +"'>Approve</button>" +
                    "<button class='decline margin-left' num='"+ applicantID +"'>Decline</button></td>"+
                    "</tr>";
                $("#applicantlist tbody").append(row);
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

    $("#applicantlist tbody ").on('click', 'button.approve', function () {
        var applicantId = $(this).attr("num");
        var cRow = $(this).parents('tr');
        console.log(JSON.stringify(applicantId));
        cRow.fadeOut("slow", function () {
            $.ajax({
                url: "/admin/approveapplicant",
                method: "POST",
                data: JSON.stringify(applicantId),
                contentType:"application/json",
            }).done(function (data) {
                alert("Message sent to applicant.");
            }).fail(function (data) {
                alert("Server error.");
            });
        });
    });

    $('#applicantlist tbody').on('click', 'button.decline' ,function () {
        var applicantId = $(this).attr("num");
        var cRow = $(this).parents('tr');
        cRow.fadeOut("slow", function () {
            $.ajax({
                url: "/admin/deleteapplicant?id="+applicantId,
                method: "DELETE"
            });
        });
    });
});