package main.controller.user;

import main.controller.project.ProjectController;
import main.entity.project.Project;
import main.entity.user.Applicant;
import main.entity.user.User;
import main.enums.ProjectStatus;
import main.utils.FileIOUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class UserManager {
    private static final Pattern NRIC_PATTERN = Pattern.compile("^[ST]\\d{7}[A-Z]$");
    private static UserManager instance;
    private static List<User> users;
    private User currentUser;

    private UserManager() {
    }

    // Singleton
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public static void loadRawUsers() {
        users = FileIOUtil.loadUsers();  //load users without resolving references (AppliedProjects Ref)
    }

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

    public static boolean verifyNRIC(String nric) {
        return NRIC_PATTERN.matcher(nric).matches();
    }

    public User findUserByName(String targetName) {
        for (User user : users) {
            if (user.getName().equals(targetName)) {
                return user;
            }
        }
        return null;
    }

    public User findUserByID(String userId) {
        for (User user : users) {
            if (user.getUserID().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public User login(String userID, String password) {
        for (User user : users) {
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