package main.utils;

import main.entity.user.User;
import main.entity.user.UserFactory;
import main.enums.MaritalStatus;
import main.enums.UserRole;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileIOUtil {
    // To extend functionality once proper user classes are added
    static final String CLASSPATH = System.getProperty("java.class.path");

    private static final String APPLICANTS_FILE = CLASSPATH + "/main/data/applicants.csv";
    private static final String MANAGERS_FILE = CLASSPATH + "/main/data/managers.csv";
    private static final String OFFICERS_FILE = CLASSPATH + "/main/data/officers.csv";

    public static List<User> loadUsers() {
        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(loadUsersFromFile(APPLICANTS_FILE, UserRole.APPLICANT));
        allUsers.addAll(loadUsersFromFile(MANAGERS_FILE, UserRole.HDB_MANAGER));
        allUsers.addAll(loadUsersFromFile(OFFICERS_FILE, UserRole.HDB_OFFICER));
        return allUsers;
    }

    public static List<User> loadUsersFromFile(String filepath, UserRole userRole) {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;

            // consume header
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                String userID = parts[1];
                int age = Integer.parseInt(parts[2]);

                MaritalStatus maritalStatus = null;
                if (parts[3].equals("Single")) {
                    maritalStatus = MaritalStatus.SINGLE;
                } else if (parts[3].equals("Married")){
                    maritalStatus = MaritalStatus.MARRIED;
                }

                String password = parts[4];
                users.add(UserFactory.createUser(userID, password, name, age, maritalStatus, userRole));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}
