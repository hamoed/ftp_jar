package com.production.kalagemet.ftpjar;

public class User_Class {
    private static String Username;
    private static String Password;

    public void setUsername(String username) {
        Username = username;
    }

    public  void setPassword(String password) {
        Password = password;
    }

    public  String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }
}
