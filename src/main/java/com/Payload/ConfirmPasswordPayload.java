package com.Payload;

public class ConfirmPasswordPayload {

    private String oldPassword;
    private String newPassword;
    private String confirmnewPassword;


    public ConfirmPasswordPayload(String oldPassword, String newPassword, String confirmnewPassword){
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmnewPassword = confirmnewPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getConfirmnewPassword() {
        return confirmnewPassword;
    }
}
