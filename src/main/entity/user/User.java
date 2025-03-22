package main.entity.user;

import main.enums.MaritalStatus;
import main.enums.UserRole;

public class User {
    private String userID;
    private String password;
    private String name;
    private int age;
    private MaritalStatus maritalStatus;
    private UserRole userRole;

    public User(String userID, String password, int age, MaritalStatus maritalStatus, UserRole userRole) {
        this.userID = userID;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.userRole = userRole;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return this.userID;
    }

    public String getPassword() {
        return this.password;
    }

}