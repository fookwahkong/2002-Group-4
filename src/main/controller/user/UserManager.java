package main.controller.user;

import main.entity.user.User;
import main.utils.FileIOUtil;

import java.util.List;
import java.util.regex.*;

public class UserManager {
    private static UserManager instance;
    private static List<User> users;
    private User currentUser;
    private static final Pattern NRIC_PATTERN = Pattern.compile("^[ST]\\d{7}[A-Z]$");

    private UserManager() {}

    // Singleton
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public static void load() {
        users = FileIOUtil.loadUsers();
    }

    public static boolean verifyNRIC(String nric) {
        return NRIC_PATTERN.matcher(nric).matches();
    }

    public User login(String userID, String password) {
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