package main.controller;

import main.entity.user.User;
import main.utils.FileIOUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class LoginController {
    private List<User> users;
    private User currentUser;

    public LoginController() {
        users = new ArrayList<>();
        users = FileIOUtil.loadUsers();
    }
    
    public boolean verifyNric(String nric) {
        // starts with S or T, followed by 7-digit number and ends with another letter
        String regex = "^[ST]\\d{7}[A-Z]$";
        
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nric);

        return matcher.matches();
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
}