$(document).ready(function () {
    $("#verifysubmit").click(function () {
        var pathname = window.location.href;
        if (pathname.indexOf("token=")>-1) {
            var token = pathname.split("token=")[1];
            $.ajax({
                url: "/posttoken",
                method: "POST",
                contentType:"text/plain",
                data: token,
            }).done(function () {
                alert("Email verified.");
                window.location="/login";
            }).fail(function () {
                alert ("Server error, please try again");
            });
        }
    });

});