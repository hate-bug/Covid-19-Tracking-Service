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
            dataType: "text",
            contentType: "application/json"
        }).ajaxSuccess(function () {
            localStorage.setItem("useremail", user.emailAddress);
            window.location=("/");
        }).fail(function (data) {
            alert(data.responseText);
        });
    });

    $("#registersubmit").click(function (e) {
        if ($("#password").val()!= $("#confirmpassword").val()){
            alert("password not the same, please check");
            return;
        }
        var user = new Object();
        user.emailAddress = $("#emailAddress").val();
        user.password = $("#password").val();
        var postdata = JSON.stringify(user);
        $.ajax({
            url: "/userregister",
            type: "POST",
            data: postdata,
            dataType: "text",
            contentType: "application/json"
        }).done(function (data) {
            alert(data);
            window.location="/";
        }).fail(function () {
            alert("Server error;")
        });
    });
});