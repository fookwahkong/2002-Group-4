package main.controller;

import main.entity.user.User;
import main.utils.FileIOUtil;
import java.util.ArrayList;
import java.util.List;

public class LoginController {
    private List<User> users;
    private User currentUser;

    public LoginController() {
        users = new ArrayList<>();
        users = FileIOUtil.loadUsers();
    }

    public User login(String userID, String password) {
        // implement NRIC validation

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