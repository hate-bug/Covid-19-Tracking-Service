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

    $("#changepasswordform").submit(function (e) {
        e.preventDefault();
        if ($("#newpassword").val() != $("#confirmnewpassword").val()){
            alert ("Password not the same.")
            return;
        }
        var passwordEntity = new Object();
        passwordEntity.oldPassword = $("#oldpassword").val();
        passwordEntity.newPassword = $("#newpassword").val();
        passwordEntity.confirmnewPassword = $("#confirmnewpassword").val();
        var data = JSON.stringify(passwordEntity);
        $.ajax({
            url: "/changepassword",
            method: "POST",
            data: data,
            contentType: "application/json"
        }).done(function (data) {
            if (data == true){
                alert("Password changed, please log in again.");
                $.ajax({
                    url: "/logout",
                    method: "GET"
                }).done(function () {
                    window.location = "/login.html"
                });
            } else {
                alert("Server error.");
                window.location = "/";
            }
        });
    });

});