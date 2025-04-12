package entity.user;

import enums.MaritalStatus;
import enums.UserRole;

public abstract class User {
    private String userID;
    private String password;
    private String name;
    private int age;
    private MaritalStatus maritalStatus;
    private UserRole userRole;

    public User(String userID, String password, String name, int age, MaritalStatus maritalStatus, UserRole userRole) {
        this.userID = userID;
        this.password = password;
        this.name = name;
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

    public UserRole getUserRole() {
        return this.userRole;
    }

    public String getName() {
        return this.name;
    }

    public MaritalStatus getMaritalStatus() {
        return this.maritalStatus;
    }

    public int getAge() {
        return this.age;
    }
}