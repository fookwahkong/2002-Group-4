package bto.controller.user;

import bto.entity.user.User;
import bto.utils.FileIOUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A class representing a user manager.
 */
public class UserManager {
    private static final Pattern NRIC_PATTERN = Pattern.compile("^[ST]\\d{7}[A-Z]$");
    private static UserManager instance;
    private static List<User> users;
    private User currentUser;

    private UserManager() {
    }

    /**
     * Returns the singleton instance of UserManager.
     * 
     * @return the singleton instance
     */
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    /**
     * Loads user data from persistent storage.
     */
    public static void load() {
        users = FileIOUtil.loadUsers();  
    }

    /**
     * Saves user data to persistent storage.
     */
    public static void save() {
        List<User> applicants = new ArrayList<>();
        List<User> officers = new ArrayList<>();
        List<User> managers = new ArrayList<>();

        for (User user : users) {
            switch (user.getUserRole()) {
                case APPLICANT -> applicants.add(user);
                case HDB_OFFICER -> officers.add(user);
                case HDB_MANAGER -> managers.add(user);
            }
        }

        FileIOUtil.saveUsersToFile(applicants, FileIOUtil.APPLICANTS_FILE);
        FileIOUtil.saveUsersToFile(officers, FileIOUtil.OFFICERS_FILE);
        FileIOUtil.saveUsersToFile(managers, FileIOUtil.MANAGERS_FILE);
    }

    /**
     * Verifies if a given NRIC is valid.
     * 
     * @param nric the NRIC to verify
     * @return true if the NRIC is valid, false otherwise
     */
    public static boolean verifyNRIC(String nric) {
        return NRIC_PATTERN.matcher(nric).matches();
    }

    /**
     * Finds a user by their name.
     * 
     * @param targetName the name of the user to find
     * @return the user if found, null otherwise
     */
    public User findUserByName(String targetName) {
        for (User user : users) {
            if (user.getName().equals(targetName)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Finds a user by their ID.
     * 
     * @param userId the ID of the user to find
     * @return the user if found, null otherwise
     */
    public User findUserByID(String userId) {
        for (User user : users) {
            if (user.getUserID().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Logs in a user with the given ID and password.
     * 
     * @param userID the ID of the user to login
     * @param password the password of the user
     * @return the user if login is successful, null otherwise
     */
    public User login(String userID, String password) {
        for (User user : users) {
            if (user.getUserID().equals(userID) && user.getPassword().equals(password)) {
                currentUser = user;
                return user;
            }
        }
        return null; // login failed
    }

    /**
     * Returns the current user.
     * 
     * @return the current user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Returns the list of all users.
     * 
     * @return the list of all users
     */
    public static List<User> getUsers() {
        return users;
    }

    /**
     * Logs out the current user.
     */
    public void logout() {
        // save project
        currentUser = null;
    }

}