package com.Payload;

public class ConfirmPasswordEntity {

    private String oldPassword;
    private String newPassword;
    private String confirmnewPassword;


    public ConfirmPasswordEntity (String oldPassword, String newPassword, String confirmnewPassword){
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmnewPassword = confirmnewPassword;
    }

    /*public boolean verifyAndSavePassword (Principal principal){
        if (!this.newPassword.equals(this.confirmnewPassword)){ //check if passwords the same
            return false;
        }
        User user = this.userRepository.findUserByEmailAddressIgnoreCase(principal.getName());
        if (!user.getPassword().equals(new BCryptPasswordEncoder().encode(this.oldPassword))){ //check old password
            return false;
        }
        user.setPassword(this.newPassword);
        this.userRepository.save(user);
        return true;
    }*/

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
