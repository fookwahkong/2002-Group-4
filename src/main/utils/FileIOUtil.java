package main.utils;

import main.controller.user.UserManager;
import main.entity.Enquiry;
import main.entity.project.Project;
import main.entity.project.ProjectBuilder;
import main.entity.user.*;
import main.enums.MaritalStatus;
import main.enums.UserRole;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.HandlerBase;

public class FileIOUtil {
    static final String CLASSPATH = System.getProperty("java.class.path");

    public static final String APPLICANTS_FILE = CLASSPATH + "/main/data/applicants.csv";
    public static final String MANAGERS_FILE = CLASSPATH + "/main/data/managers.csv";
    public static final String OFFICERS_FILE = CLASSPATH + "/main/data/officers.csv";
    public static final String ENQUIRIES_FILE = CLASSPATH + "/main/data/enquiries.csv";
    public static final String PROJECTS_FILE = "C:/Users/fwkon/Documents/Uni stuff/Capstone Project/2002-Group-4/src/main/data/projects.csv";

    public static List<User> loadUsers() {
        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(loadUsersFromFile(APPLICANTS_FILE, UserRole.APPLICANT));
        allUsers.addAll(loadUsersFromFile(MANAGERS_FILE, UserRole.HDB_MANAGER));
        allUsers.addAll(loadUsersFromFile(OFFICERS_FILE, UserRole.HDB_OFFICER));
        return allUsers;
    }

    public static List<Project> loadProjects() {
        List<Project> allProjects = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(PROJECTS_FILE))) {
            String line = reader.readLine(); // consume header line

            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                    if (parts.length < 13) {
                        System.err.println("Skipping invalid project data: " + line);
                        continue;
                    }

                    // Clean and parse data
                    String projectName = parts[0].trim();
                    String neighborhood = parts[1].trim();
                    Boolean visibility = Boolean.parseBoolean(parts[2].trim());

                    String openingDateStr = parts[3].trim();
                    String closingDateStr = parts[4].trim();

                    int unitsTypeOne = parts[6].trim().isEmpty() ? 0 : parseInteger(parts[6]);
                    float priceTypeOne = parts[7].trim().isEmpty() ? 0 : parseFloat(parts[7]);
                    
                    int unitsTypeTwo = parts[9].trim().isEmpty() ? 0 : parseInteger(parts[9]);
                    float priceTypeTwo = parts[10].trim().isEmpty() ? 0 : parseFloat(parts[10]);
                    
                    String managerName = parts[12].trim();

                    // Get manager
                    HDBManager manager = (HDBManager) UserManager.getInstance()
                            .findUserByName(managerName);

                    if (manager == null) {
                        System.err.println("Could not find manager: " + managerName + " for project: " + projectName);
                        continue;
                    }

                    // Get officers slots
                    int officerSlots = parseInteger(parts[13]);

                    // Create project using builder
                    ProjectBuilder builder = new ProjectBuilder()
                            .withName(projectName)
                            .withNeighborhood(neighborhood)
                            .withVisibility(visibility)
                            .withApplicationPeriod(openingDateStr, closingDateStr)
                            .withManager(manager)
                            .withOfficerSlots(officerSlots);

                    // Only add 2-Room if data exists
                    if (unitsTypeOne > 0 || priceTypeOne > 0) {
                        builder.addHousingType("2-Room", priceTypeOne, unitsTypeOne);
                    }

                    // Only add 3-Room if data exists
                    if (unitsTypeTwo > 0 || priceTypeTwo > 0) {
                        builder.addHousingType("3-Room", priceTypeTwo, unitsTypeTwo);
                    }

                    Project project = builder.build();

                    // Add applicants
                    String applicantString = parts[11].trim().replace("\"", "");
                    if (!applicantString.isEmpty()) {
                        String[] applicants = applicantString.split(",");
                        for (String applicantName : applicants) {
                            Applicant applicant = (Applicant) UserManager.getInstance()
                                    .findUserByName(applicantName.trim());

                            if (applicant != null) {
                                project.addApplicants(applicant);
                            } else {
                                System.err.println(
                                        "Could not find applicant: " + applicantName + " for project: " + projectName);
                            }
                        }
                    }
                    // Add officers
                    String officersString = parts[14].trim().replace("\"", "");
                    if (!officersString.isEmpty()) {
                        String[] officers = officersString.split(",");
                        for (String officerName : officers) {
                            HDBOfficer officer = (HDBOfficer) UserManager.getInstance()
                                    .findUserByName(officerName.trim());

                            if (officer != null) {
                                project.addOfficersIncharge(officer);
                                officer.assignProject(project);
                            } else {
                                System.err.println(
                                        "Could not find officer: " + officerName + " for project: " + projectName);
                            }
                        }
                    }

                    allProjects.add(project);

                } catch (Exception e) {
                    System.err.println("Error parsing project data: " + line);
                    System.err.println("Exception: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading projects file: " + e.getMessage());
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
                } else if (parts[3].equals("Married")) {
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

    public static void saveProjectToFile(List<Project> projects, String filepath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            writer.write(String.join(",",
                    "Project Name", "Neighborhood", "Visibility",
                    "Application opening date", "Application closing date",
                    "Type 1", "Number of units for Type 1", "Selling price for Type 1",
                    "Type 2", "Number of units for Type 2", "Selling price for Type 2",
                    "Applicants", "Manager", "Officer Slot", "Officer"));

            writer.newLine();

            for (Project project : projects) {
                String applicants = (project.getApplicants() != null)
                        ? String.join(",", project.getApplicants().stream()
                                .map(User::getName)
                                .toArray(String[]::new))
                        : "";
                String assignedOfficers = (project.getAssignedOfficers() != null)
                        ? String.join(",", project.getAssignedOfficers().stream()
                                .map(User::getName)
                                .toArray(String[]::new))
                        : "";

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String line = String.format("%s,%s,%s,%s,%s,%s,%d,%.2f,%s,%d,%.2f,\"%s\",%s,%d,\"%s\"",
                        project.getName(),
                        project.getNeighbourhood(),
                        project.getVisibility(),
                        project.getOpeningDate().format(formatter),
                        project.getClosingDate().format(formatter),
                        project.getHousingType("2-Room").getTypeName(),
                        project.getHousingType("2-Room").getNumberOfUnits(),
                        project.getHousingType("2-Room").getSellingPrice(),
                        project.getHousingType("3-Room").getTypeName(),
                        project.getHousingType("3-Room").getNumberOfUnits(),
                        project.getHousingType("3-Room").getSellingPrice(),
                        applicants,
                        project.getManager().getName(),
                        project.getSlots(),
                        assignedOfficers);

                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException("Error saving project to file: " + e.getMessage());
        }
    }

    // Helper methods for parsing values
    private static int parseInteger(String value) {
        return Integer.parseInt(value.trim()
                .replace("\"", "")
                .replace(",", ""));
    }

    private static float parseFloat(String value) {
        return Float.parseFloat(value.trim()
                .replace("\"", "")
                .replace(",", ""));
    }
}
