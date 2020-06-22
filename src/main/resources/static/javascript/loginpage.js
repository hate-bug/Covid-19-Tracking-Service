$(document).ready(function () {
    $("#loginform").submit(function (e) {
        e.preventDefault();
        $.ajax({
            url: "/userlogin",
            type: "POST",
            data: $("#loginform").serialize()
        }).done(function () {
            window.location=("/");
        }).fail(function (data) {
            alert("Wrong credential, please login again");
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
                    window.location = "/login"
                });
            } else {
                alert("Server error.");
                window.location = "/";
            }
        });
    });

});