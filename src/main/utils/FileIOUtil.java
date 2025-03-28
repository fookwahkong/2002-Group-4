package main.utils;

import main.controller.user.UserManager;
import main.entity.user.HDBManager;
import main.entity.user.HDBOfficer;
import main.entity.user.User;
import main.entity.user.UserFactory;
import main.entity.project.Project;
import main.enums.MaritalStatus;
import main.enums.UserRole;

import java.io.*;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FileIOUtil {
    // To extend functionality once proper user classes are added
    static final String CLASSPATH = System.getProperty("java.class.path");

    private static final String APPLICANTS_FILE = CLASSPATH + "/main/data/applicants.csv";
    private static final String MANAGERS_FILE = CLASSPATH + "/main/data/managers.csv";
    private static final String OFFICERS_FILE = CLASSPATH + "/main/data/officers.csv";
    private static final String PROJECTS_FILE = CLASSPATH + "/main/data/projects.csv";

    public static List<User> loadUsers() {
        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(loadUsersFromFile(APPLICANTS_FILE, UserRole.APPLICANT));
        allUsers.addAll(loadUsersFromFile(MANAGERS_FILE, UserRole.HDB_MANAGER));
        allUsers.addAll(loadUsersFromFile(OFFICERS_FILE, UserRole.HDB_OFFICER));
        return allUsers;
    }

    public static List<Project> loadProjects() {
        List<Project> allProjects = new ArrayList<>();

        // temp loading. to use csv later

        Project temp = new Project("Acacia Breeze", "Yishun", true);
        temp.setHousingTypeOne(350000);
        temp.setHousingTypeTwo(450000);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        temp.setDate(LocalDate.parse("2/15/2025", formatter), LocalDate.parse("3/20/2025", formatter));
        temp.setManagerInCharge((HDBManager) UserManager.getInstance().findUserByName("Jessica"));
        temp.setOfficerSlot(3);
        temp.addOfficersIncharge((HDBOfficer) UserManager.getInstance().findUserByName("Daniel"));
        temp.addOfficersIncharge((HDBOfficer) UserManager.getInstance().findUserByName("Emily"));

        allProjects.add(temp);
        return allProjects;
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
