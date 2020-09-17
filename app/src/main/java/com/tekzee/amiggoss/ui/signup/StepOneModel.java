package com.tekzee.amiggoss.ui.signup;

import java.io.Serializable;

public class StepOneModel implements Serializable {
    private String email;
    private String password;
    private String username;
    private String dateofbirth;

    public StepOneModel() {

    }

    public StepOneModel(String email, String password, String username, String dateofbirth) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.dateofbirth = dateofbirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }
}
