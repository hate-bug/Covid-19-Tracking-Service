$(document).ready(function () {
    $("#loginform").submit(function (e) {
        e.preventDefault();
        var user = new Object();
        user.emailAddress = $("#emailAddress").val();
        user.password = $("#password").val();
        var data = JSON.stringify(user);
        $.ajax({
            url: "/userlogin",
            type: "POST",
            data: data,
            contentType: "application/json"
        }).ajaxSuccess(function () {
            window.location=("/");
        }).fail(function (data) {
            alert(data.responseText);
        });
    });
});