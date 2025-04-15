package entity.user;

import enums.MaritalStatus;
import enums.UserRole;
import interfaces.Authenticatable;
import interfaces.PersonalProfile;

public abstract class User implements Authenticatable, PersonalProfile{
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

    @Override
    public void changePassword(String password) {
        this.password = password;
    }

    @Override
    public String getUserID() {
        return this.userID;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public UserRole getUserRole() {
        return this.userRole;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public MaritalStatus getMaritalStatus() {
        return this.maritalStatus;
    }

    @Override
    public int getAge() {
        return this.age;
    }
}