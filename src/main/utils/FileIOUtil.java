package main.utils;

import main.controller.user.UserManager;
import main.entity.user.Applicant;
import main.entity.user.HDBManager;
import main.entity.user.HDBOfficer;
import main.entity.user.User;
import main.entity.user.UserFactory;
import main.entity.Enquiry;
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

    public static final String APPLICANTS_FILE =  CLASSPATH + "main/data/applicants.csv";
    public static final String MANAGERS_FILE = CLASSPATH + "main/data/managers.csv";
    public static final String OFFICERS_FILE =  CLASSPATH + "main/data/officers.csv";
    private static final String PROJECTS_FILE = CLASSPATH + "main/data/projects.csv";
    public static final String ENQUIRIES_FILE = CLASSPATH + "main/data/enquiries.csv";


    public static List<User> loadUsers() {
        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(loadUsersFromFile(APPLICANTS_FILE, UserRole.APPLICANT));
        allUsers.addAll(loadUsersFromFile(MANAGERS_FILE, UserRole.HDB_MANAGER));
        allUsers.addAll(loadUsersFromFile(OFFICERS_FILE, UserRole.HDB_OFFICER));
        return allUsers;
    }

    public static List<Project> loadProjects() {
        List<Project> allProjects = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

        try (BufferedReader reader = new BufferedReader(new FileReader(PROJECTS_FILE))) {
            String line = reader.readLine(); // consume first line

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (parts.length >= 13) {
                    Project project = new Project(
                            parts[0].trim(),  // Project Name
                            parts[1].trim(),  // Neighborhood
                            true  // Assuming this is always true for now
                    );

                    // Set housing type one details
                    project.setHousingTypeOne(
                            Float.parseFloat(parts[4].trim().replace("\"", "").replace(",", "")),  // Selling price
                            Integer.parseInt(parts[3].trim().replace("\"", "").replace(",", ""))
                    );


                    // Set housing type two details
                    project.setHousingTypeTwo(
                            Float.parseFloat(parts[7].trim().replace("\"", "").replace(",", "")), // Selling price
                            Integer.parseInt(parts[6].trim().replace("\"", "").replace(",", ""))
                            );

                    // Set application dates
                    project.setDate(
                            LocalDate.parse(parts[8].trim(), formatter),
                            LocalDate.parse(parts[9].trim(), formatter)
                    );

                    // Set manager
                    HDBManager manager = (HDBManager) UserManager.getInstance()
                            .findUserByName(parts[10].trim());
                    project.setManagerInCharge(manager);

                    // Set officer slot
                    project.setOfficerSlot(Integer.parseInt(parts[11].trim()));

                    // Add officers
                    String[] officers = parts[12].trim().replace("\"", "").split(",");
                    for (String officerName : officers) {
                        HDBOfficer officer = (HDBOfficer) UserManager.getInstance()
                                .findUserByName(officerName.trim());
                        project.addOfficersIncharge(officer);
                    }

                    allProjects.add(project);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return allProjects;
    }


    public static void loadEnquiries(List<Project> projects) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ENQUIRIES_FILE))) {
            String line = reader.readLine(); // consume header

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 5) {
                    String applicantID = parts[0].trim();
                    String projectName = parts[1].trim();
                    String message = parts[2].trim();
                    String reply = parts[3].trim();
                    boolean replied = Boolean.parseBoolean(parts[4].trim());

                    // Find the applicant
                    User applicantUser = UserManager.getInstance().findUserByID(applicantID);
                    if (applicantUser == null || !(applicantUser instanceof Applicant)) {
                        System.out.println("Warning: Applicant not found for ID: " + applicantID);
                        continue; // Skip if applicant not found or not an Applicant
                    }
                    Applicant applicant = (Applicant) applicantUser;

                    // Find the project
                    Project project = null;
                    for (Project p : projects) {
                        if (p.getName().equals(projectName)) {
                            project = p;
                            break;
                        }
                    }

                    if (project == null) {
                        System.out.println("Warning: Project not found: " + projectName);
                        continue; // Skip if project not found
                    }

                    // Create and add the enquiry
                    Enquiry enquiry = new Enquiry(applicant, project, message);
                    if (!reply.equals("-")) {
                        enquiry.setReply(reply);
                    }
                    enquiry.setReplied(replied);
                    project.addEnquiry(enquiry);
                    System.out.println("Added enquiry to project " + projectName);

                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading enquiries: " + e.getMessage());
        }
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

    public static void saveUsersToFile(List<User> userList, String filepath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            // Write header
            writer.write("Name,UserID,Age,MaritalStatus,Password");
            writer.newLine();
            
            // Write each user
            for (User user : userList) {
                String maritalStatusStr = (user.getMaritalStatus() == MaritalStatus.SINGLE) ? "Single" : "Married";
                
                String line = String.format("%s,%s,%d,%s,%s",
                        user.getName(),
                        user.getUserID(),
                        user.getAge(),
                        maritalStatusStr,
                        user.getPassword());
                
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving users to file: " + e.getMessage());
        }
    } 
}
