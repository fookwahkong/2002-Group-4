package main.controller;

import main.entity.user.User;
import main.utils.FileIOUtil;

import java.util.List;
import java.util.regex.*;

public class LoginController {
    private static LoginController instance;
    private List<User> users;
    private User currentUser;
    private static final Pattern NRIC_PATTERN = Pattern.compile("^[ST]\\d{7}[A-Z]$");

    public LoginController() {
        users = FileIOUtil.loadAll();
    }

    // singleton
    // instance will persist throughout lifetime
    public static LoginController getInstance() {
        if (instance == null) {
            instance = new LoginController();
        }
        return instance;
    }
    
    public boolean verifyNric(String nric) {
        // starts with S or T, followed by 7-digit number and ends with another letter
        return NRIC_PATTERN.matcher(nric).matches();
    }

    public User login(String userID, String password) {
        // polymorphic: to implement subclasses of User
        for (User user: users) {
            if (user.getUserID().equals(userID) && user.getPassword().equals(password)) {
                currentUser = user;
                return user;
            }
        }
        return null; // login failed
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }
}