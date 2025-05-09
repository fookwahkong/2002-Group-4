package bto.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import com.opencsv.exceptions.CsvValidationException;
import bto.entity.user.*;
import bto.controller.user.UserManager;
import bto.entity.Enquiry;
import bto.entity.Registration;
import bto.entity.project.Project;
import bto.entity.project.ProjectBuilder;
import bto.enums.MaritalStatus;
import bto.enums.ProjectStatus;
import bto.enums.RegistrationStatus;
import bto.enums.UserRole;
import bto.interfaces.BookingCapable;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for file input/output operations.
 */
public class FileIOUtil {

    /**
     * The data directory
     */
    private static final String DATA_DIR = "src/main/resources/data/";

    public static final String APPLICANTS_FILE = DATA_DIR + "applicants.csv";
    public static final String MANAGERS_FILE = DATA_DIR + "managers.csv";
    public static final String OFFICERS_FILE = DATA_DIR + "officers.csv";
    public static final String ENQUIRIES_FILE = DATA_DIR + "enquiries.csv";
    public static final String PROJECTS_FILE = DATA_DIR + "projects.csv";
    public static final String BOOKING_FILE = DATA_DIR + "bookings.csv";
    public static final String REGISTRATIONS_FILE = DATA_DIR + "registrations.csv";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final UserFactory userFactory = new UserFactory();

    /**
     * Loads all users from the file system.
     *
     * @return a list of all users
     */
    public static List<User> loadUsers() {
        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(loadUsersFromFile(APPLICANTS_FILE, UserRole.APPLICANT));
        allUsers.addAll(loadUsersFromFile(MANAGERS_FILE, UserRole.HDB_MANAGER));
        allUsers.addAll(loadUsersFromFile(OFFICERS_FILE, UserRole.HDB_OFFICER));
        return allUsers;
    }

    /**
     * Loads all projects from the file system.
     *
     * @return a list of all projects
     */
    public static List<Project> loadProjects() {
        List<Project> allProjects = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(PROJECTS_FILE))) {

            reader.readNext();

            String[] line;
            while ((line = reader.readNext()) != null) {
                try {
                    if (line.length < 13) {
                        System.err.println("Skipping invalid project data: insufficient columns");
                        continue;
                    }


                    String projectName = line[0].trim();
                    String neighborhood = line[1].trim();
                    boolean visibility = Boolean.parseBoolean(line[2].trim());

                    String openingDateStr = line[3].trim();
                    String closingDateStr = line[4].trim();

                    int unitsTypeOne = line[6].trim().isEmpty() ? 0 : Integer.parseInt(line[6].trim());
                    float priceTypeOne = line[7].trim().isEmpty() ? 0 : Float.parseFloat(line[7].trim());

                    int unitsTypeTwo = line[9].trim().isEmpty() ? 0 : Integer.parseInt(line[9].trim());
                    float priceTypeTwo = line[10].trim().isEmpty() ? 0 : Float.parseFloat(line[10].trim());

                    String managerName = line[12].trim();


                    HDBManager manager = (HDBManager) UserManager.getInstance()
                            .findUserByName(managerName);

                    if (manager == null) {
                        System.err.println("Could not find manager: " + managerName + " for project: " + projectName);
                        continue;
                    }


                    int officerSlots = Integer.parseInt(line[13].trim());


                    ProjectBuilder builder = new ProjectBuilder()
                            .withName(projectName)
                            .withNeighborhood(neighborhood)
                            .withVisibility(visibility)
                            .withApplicationPeriod(openingDateStr, closingDateStr)
                            .withManager(manager)
                            .withOfficerSlots(officerSlots);


                    builder.addHousingType("2-Room", priceTypeOne, unitsTypeOne);
                    builder.addHousingType("3-Room", priceTypeTwo, unitsTypeTwo);

                    Project project = builder.build();


                    String applicantString = line[11].trim();
                    if (!applicantString.isEmpty()) {
                        Map<Applicant, ProjectStatus> applicantProjectStatusMap = parseApplicantProjects(
                                applicantString);
                        for (Map.Entry<Applicant, ProjectStatus> entry : applicantProjectStatusMap.entrySet()) {
                            project.addApplicant(entry.getKey(), entry.getValue());
                        }
                    }


                    String officersString = line[14].trim();
                    if (!officersString.isEmpty()) {
                        String[] officers = officersString.split(",");
                        for (String officerName : officers) {
                            HDBOfficer officer = (HDBOfficer) UserManager.getInstance()
                                    .findUserByName(officerName.trim());

                            if (officer != null) {
                                project.addOfficersIncharge(officer);
                            } else {
                                System.err.println(
                                        "Could not find officer: " + officerName + " for project: " + projectName);
                            }
                        }
                    }

                    allProjects.add(project);
                } catch (Exception e) {
                    System.err.println("Error parsing project data: " + String.join(",", line));
                    System.err.println("Exception: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading projects file: " + e.getMessage());
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return allProjects;
    }

    /**
     * Loads all enquiries from the file system.
     *
     * @param projects the list of projects
     */
    public static void loadEnquiries(List<Project> projects) {
        try (CSVReader reader = new CSVReader(new FileReader(ENQUIRIES_FILE))) {

            reader.readNext();

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length >= 5) {
                    String applicantID = line[0].trim();
                    String projectName = line[1].trim();
                    String message = line[2].trim();
                    String reply = line[3].trim();
                    boolean replied = Boolean.parseBoolean(line[4].trim());


                    User applicantUser = UserManager.getInstance().findUserByID(applicantID);
                    if (!(applicantUser instanceof Applicant applicant)) {
                        System.out.println("Warning: Applicant not found for ID: " + applicantID);
                        continue;
                    }


                    Project project = null;
                    for (Project p : projects) {
                        if (p.getName().equals(projectName)) {
                            project = p;
                            break;
                        }
                    }

                    if (project == null) {
                        System.out.println("Warning: Project not found: " + projectName);
                        continue;
                    }


                    Enquiry enquiry = new Enquiry(applicant, project, message);
                    if (!reply.equals("-")) {
                        enquiry.setReply(reply);
                    }
                    enquiry.setReplied(replied);
                    project.addEnquiry(enquiry);
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Error loading enquiries: " + e.getMessage());
        }
    }

    /**
     * Loads all users from a file.
     *
     * @param filepath the path to the file
     * @param userRole the role of the user
     * @return a list of users
     */
    public static List<User> loadUsersFromFile(String filepath, UserRole userRole) {
        List<User> users = new ArrayList<>();
        
        File file = new File(filepath);

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            reader.readNext(); // Skip header

            String[] line;
            while ((line = reader.readNext()) != null) {
                String name = line[0];
                String userID = line[1];
                int age = Integer.parseInt(line[2]);

                MaritalStatus maritalStatus = null;
                if (line[3].equals("Single")) {
                    maritalStatus = MaritalStatus.SINGLE;
                } else if (line[3].equals("Married")) {
                    maritalStatus = MaritalStatus.MARRIED;
                }

                String password = line[4];

                User user = userFactory.createUser(userID, password, name, age, maritalStatus, userRole);
                users.add(user);

            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Error loading users from file: " + e.getMessage());
        }

        return users;
    }


    /**
     * Loads all registrations from the file system.
     *
     * @param projects the list of projects
     */ 
    public static void loadRegistration(List<Project> projects) {
        File file = new File(REGISTRATIONS_FILE);
        
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            reader.readNext();

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length < 3) {
                    System.err.println("Skipping invalid registration data: insufficient columns");
                    continue;
                }

                String officerName = line[0].trim();
                String projectName = line[1].trim();
                String registrationStatusStr = line[2].trim();

                User officerUser = UserManager.getInstance().findUserByName(officerName);
                if (!(officerUser instanceof HDBOfficer officer)) {
                    System.err.println("Officer not found or invalid: " + officerName);
                    continue;
                }

                Project project = projects.stream()
                        .filter(p -> p.getName().equalsIgnoreCase(projectName))
                        .findFirst()
                        .orElse(null);
                if (project == null) {
                    System.err.println("Project not found: " + projectName);
                    continue;
                }

                try {
                    RegistrationStatus registrationStatus = RegistrationStatus
                            .valueOf(registrationStatusStr.toUpperCase());

                    Registration registration = new Registration(officer, project, registrationStatus);
                    project.addRegistration(registration);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid registration status: " + registrationStatusStr);
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Error loading registrations: " + e.getMessage());
        }
    }

    /**
     * Loads all booking details from the file system.
     *
     * @param projects the list of projects
     */
    public static void loadBookingDetails(List<Project> projects) {
        File file = new File(BOOKING_FILE);

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            reader.readNext();

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length < 3) {
                    System.err.println("Skipping invalid booking data: insufficient columns");
                    continue;
                }

                String applicantID = line[0].trim();
                String projectName = line[1].trim();
                String housingType = line[2].trim();

                User applicantUser = UserManager.getInstance().findUserByID(applicantID);

                // Check if user is BookingCapable (interface-based approach)
                if (!(applicantUser instanceof BookingCapable bookingCapable)) {
                    System.err.println("User is not booking capable: " + applicantID);
                    continue;
                }

                Project project = projects.stream()
                        .filter(p -> p.getName().equalsIgnoreCase(projectName))
                        .findFirst()
                        .orElse(null);
                if (project == null) {
                    System.err.println("Project not found: " + projectName);
                    continue;
                }

                // Use the interface method instead of direct casting
                bookingCapable.setBookingDetails(project, housingType);
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error reading booking details file: " + e.getMessage());
        }
    }

    /**
     * Saves all booking details to the file system.
     *
     * @param users the list of users
     */
    public static void saveBookingDetails(List<User> users) {
        List<BookingCapable> bookingCapableUsers = new ArrayList<>();
        for (User user : users) {
            // Use interface instead of concrete class
            if (user instanceof BookingCapable) {
                bookingCapableUsers.add((BookingCapable) user);
            }
        }

        // Ensure directory exists
        File file = new File(BOOKING_FILE);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            String[] header = {"ApplicantID", "ProjectName", "HousingType"};
            writer.writeNext(header);

            for (BookingCapable bookingCapable : bookingCapableUsers) {
                Map<Project, String> bookingDetails = bookingCapable.getBookingDetails();
                
                // Make sure the user is also a User type to get the ID
                if (bookingCapable instanceof User user && bookingDetails != null && !bookingDetails.isEmpty()) {
                    for (Map.Entry<Project, String> entry : bookingDetails.entrySet()) {
                        Project project = entry.getKey();
                        String housingType = entry.getValue();

                        // Make sure the user have not withdraw booking
                        if (project.getApplicantswithStatus().get(user) != ProjectStatus.UNSUCCESSFUL) {
                            String[] line = {
                                    user.getUserID(),
                                    project.getName(),
                                    housingType
                            };

                            writer.writeNext(line);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving booking details to file: " + e.getMessage());
        }
    }

    /**
     * Saves all users to the file system.
     *
     * @param userList the list of users
     * @param filepath the path to the file
     */
    public static void saveUsersToFile(List<User> userList, String filepath) {
        // Ensure directory exists
        File file = new File(filepath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            String[] header = {"Name", "UserID", "Age", "MaritalStatus", "Password"};
            writer.writeNext(header);

            for (User user : userList) {
                String maritalStatusStr = (user.getMaritalStatus() == MaritalStatus.SINGLE) ? "Single" : "Married";

                String[] line = {
                        user.getName(),
                        user.getUserID(),
                        String.valueOf(user.getAge()),
                        maritalStatusStr,
                        user.getPassword()
                };

                writer.writeNext(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving users to file: " + e.getMessage());
        }
    }

    /**
     * Saves all projects to the file system.
     *
     * @param projects the list of projects
     * @param filepath the path to the file
     */
    public static void saveProjectToFile(List<Project> projects, String filepath) {
        // Ensure directory exists
        File file = new File(filepath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            String[] header = {
                    "Project Name", "Neighborhood", "Visibility",
                    "Application opening date", "Application closing date",
                    "Type 1", "Number of units for Type 1", "Selling price for Type 1",
                    "Type 2", "Number of units for Type 2", "Selling price for Type 2",
                    "Applicants", "Manager", "Officer Slot", "Pending Officer", "Officer"
            };
            writer.writeNext(header);

            for (Project project : projects) {
                String applicants = (project.getApplicantswithStatus() != null)
                        ? project.getApplicantswithStatus().entrySet().stream()
                        .map(entry -> entry.getKey().getName() + ":" + entry.getValue().name())
                        .reduce((a, b) -> a + "," + b)
                        .orElse("")
                        : "";

                String assignedOfficers = (project.getAssignedOfficers() != null)
                        ? String.join(",", project.getAssignedOfficers().stream()
                        .map(User::getName)
                        .toArray(String[]::new))
                        : "";

                String[] line = {
                        project.getName(),
                        project.getNeighbourhood(),
                        String.valueOf(project.getVisibility()),
                        project.getOpeningDate().format(DATE_FORMATTER),
                        project.getClosingDate().format(DATE_FORMATTER),
                        "2-Room",
                        String.valueOf(project.getHousingType("2-Room") != null ?
                                project.getHousingType("2-Room").getNumberOfUnits() : 0),
                        String.valueOf(project.getHousingType("2-Room") != null ?
                                project.getHousingType("2-Room").getSellingPrice() : 0.0f),
                        "3-Room",
                        String.valueOf(project.getHousingType("3-Room") != null ?
                                project.getHousingType("3-Room").getNumberOfUnits() : 0),
                        String.valueOf(project.getHousingType("3-Room") != null ?
                                project.getHousingType("3-Room").getSellingPrice() : 0.0f),
                        applicants,
                        project.getManager().getName(),
                        String.valueOf(project.getSlots()),
                        assignedOfficers,
                        ""
                };

                writer.writeNext(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving project to file: " + e.getMessage());
        }
    }

    /**
     * Saves all enquiries to the file system.
     *
     * @param projects the list of projects
     */
    public static void saveEnquiriesToFile(List<Project> projects) {
        // Ensure directory exists
        File file = new File(ENQUIRIES_FILE);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            String[] header = {"ApplicantID", "ProjectName", "Message", "Reply", "Replied"};
            writer.writeNext(header);
    
            for (Project project : projects) {
                List<Enquiry> enquiries = project.getEnquiries();
                if (enquiries != null && !enquiries.isEmpty()) {
                    for (Enquiry enquiry : enquiries) {
                        String[] line = {
                                enquiry.getApplicant().getUserID(),
                                project.getName(),
                                enquiry.getContent(),
                                enquiry.getReply(),
                                String.valueOf(enquiry.isReplied())
                        };
                        writer.writeNext(line);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving enquiries to file: " + e.getMessage());
        }
    }

    /**
     * Saves all registrations to the file system.
     *
     * @param projects the list of projects
     */
    public static void saveRegistrationsToFile(List<Project> projects) {
        // Ensure directory exists
        File file = new File(REGISTRATIONS_FILE);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            String[] header = {"OfficerName", "ProjectName", "RegistrationStatus"};
            writer.writeNext(header);
    
            for (Project project : projects) {
                List<Registration> registrations = project.getRegistrationList();
                if (registrations != null && !registrations.isEmpty()) {
                    for (Registration registration : registrations) {
                        String[] line = {
                                registration.getOfficer().getName(),
                                project.getName(),
                                registration.getRegistrationStatus().toString()
                        };
                        writer.writeNext(line);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving registrations to file: " + e.getMessage());
        }
    }

    /**
     * Parses the applicant projects from a string.
     *
     * @param applicantProjects the string of applicant projects
     * @return a map of applicant and project status
     */ 
    private static Map<Applicant, ProjectStatus> parseApplicantProjects(String applicantProjects) {
        Map<Applicant, ProjectStatus> applicantProjectStatusMap = new HashMap<>();
        if (applicantProjects != null && !applicantProjects.isEmpty()) {
            String[] projectEntries = applicantProjects.split(",");
            for (String entry : projectEntries) {
                String[] parts = entry.split(":");
                if (parts.length == 2) {
                    String applicantName = parts[0].trim();
                    String status = parts[1].trim();

                    User user = UserManager.getInstance().findUserByName(applicantName);
                    if (user != null) {
                        try {
                            Applicant applicant = (Applicant) user;
                            ProjectStatus projectStatus = ProjectStatus.valueOf(status.toUpperCase());
                            applicantProjectStatusMap.put(applicant, projectStatus);
                        } catch (IllegalArgumentException e) {
                            System.err.println("Invalid project status: " + status);
                        }
                    } else {
                        System.err.println("Applicant not found: " + applicantName);
                    }
                }
            }
        }
        return applicantProjectStatusMap;
    }
}