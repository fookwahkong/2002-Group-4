package main.controller;

import main.entity.user.User;
import main.utils.FileIOUtil;
import java.util.ArrayList;
import java.util.List;
import main.controller.NRICChecker;

public class LoginController {
    private List<User> users;

    public LoginController() {
        users = new ArrayList<>();
        users = FileIOUtil.loadUsers();
    }

    public int login(String userID, String password) {
        // NRICChecker checker = new NRICChecker();
    
        // if (!checker.isNRICCorrect(userID)) {
        //     System.out.println("Invalid NRIC format. Please try again.");
        //     return -1;
        // }
    
        for (User user : users) {
            if (user.getUserID().trim().equalsIgnoreCase(userID.trim()) && 
                user.getPassword().trim().equals(password.trim())) {
                return 1; // Successful login
            }
        }
    
        System.out.println("Invalid userID or password.");
        return -1;
    }
    
}