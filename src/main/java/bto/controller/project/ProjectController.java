package bto.controller.project;

import bto.controller.user.UserManager;
import bto.entity.Enquiry;
import bto.entity.Housing;
import bto.entity.Registration;
import bto.entity.project.Project;
import bto.entity.project.ProjectBuilder;
import bto.entity.user.Applicant;
import bto.entity.user.HDBManager;
import bto.entity.user.HDBOfficer;
import bto.enums.MaritalStatus;
import bto.enums.ProjectStatus;
import bto.enums.RegistrationStatus;
import bto.utils.FileIOUtil;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A controller class for project operations.
 */
public class ProjectController {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static List<Project> projects = new ArrayList<>();

    public ProjectController() {
    }

    /**
     * Loads the list of projects from persistent storage.
     */
    public static void load() {
        projects = FileIOUtil.loadProjects(); 
    }

    /**
     * Saves the current list of projects to persistent storage.
     */
    public static void save() {
        FileIOUtil.saveProjectToFile(projects, FileIOUtil.PROJECTS_FILE);
    }

    /**
     * Returns the list of all projects.
     *
     * @return a list containing all projects
     */
    public static List<Project> getProjectList() {
        return projects;
    }

    /**
     * Returns the list of projects managed by the specified manager.
     *
     * @param manager the HDB manager
     * @return a list of projects managed by the given manager
     */
    public static List<Project> getManagerProjects(HDBManager manager) {
        return ProjectController.getProjectList().stream()
                .filter(project -> project.getManager().equals(manager))
                .toList();
    }

    /**
     * Returns the list of projects assigned to the specified officer.
     *
     * @param officer the HDB officer
     * @return a list of projects assigned to the given officer
     */
    public static List<Project> getOfficerProjects(HDBOfficer officer) {
        return ProjectController.getProjectList().stream()
                .filter(project -> project.getAssignedOfficers().contains(officer))
                .toList();
    }

    /**
     * Returns the list of projects that the officer can register for (i.e., not already assigned, not registered, and not an applicant).
     *
     * @param officer the HDB officer
     * @return a list of projects the officer can register for
     */
    public static List<Project> canRegisterProjects(HDBOfficer officer) {
        return ProjectController.getProjectList().stream()
                .filter(project ->
                        // Officer is not already assigned to this project
                        !project.getAssignedOfficers().contains(officer)
    
                        // Officer has no active (non-rejected) registration for this project
                        && project.getRegistrationList().stream()
                            .noneMatch(registration ->
                                    registration.getOfficer().equals(officer)
                                    && registration.getRegistrationStatus() != RegistrationStatus.REJECTED)
    
                        // Officer is not an applicant for this project
                        && project.getApplicants().stream()
                            .noneMatch(applicant -> applicant.equals(officer)))
                .toList();
    }
    

    /**
     * Returns the list of projects visible and available for the applicant to apply, based on their marital status.
     *
     * @param applicant the applicant
     * @return a list of projects the applicant can see and apply for
     */
    public static List<Project> getApplicantProjects(Applicant applicant) {
        if (applicant.getMaritalStatus() == MaritalStatus.SINGLE && applicant.getAge() >= 35) {
            return ProjectController.getProjectList().stream()
                .filter(project -> project.getAllHousingTypes().get("2-Room").getNumberOfUnits() != -1)
                .filter(Project::getVisibility)
                .toList();
            
        } else if (applicant.getMaritalStatus() == MaritalStatus.MARRIED && applicant.getAge() >= 21) {
            return ProjectController.getProjectList().stream()
                .filter(Project::getVisibility)
                .toList();
        }
        return new ArrayList<>(); // return empty list if the applicant is neither conditions are met
        // not eligible for any project
    }

    /**
     * Returns the active project (if any) that the applicant has applied for, along with its status.
     *
     * @param applicant the applicant
     * @return a map containing the project and its status, or null if none found
     */
    public static Map<Project, ProjectStatus> getApplicantActiveProject(Applicant applicant) {
        for (Project p : projects) {
            Map<Applicant, ProjectStatus> applicantStatusMap = p.getApplicantswithStatus();
            for (Applicant a : applicantStatusMap.keySet()) {
                ProjectStatus status = applicantStatusMap.get(a);
                if (a == applicant && (status == ProjectStatus.PENDING ||
                        status == ProjectStatus.SUCCESSFUL ||
                        status == ProjectStatus.BOOKED ||
                        status == ProjectStatus.REQUEST_BOOK ||
                        status == ProjectStatus.REQUEST_WITHDRAW)) {
                    Map<Project, ProjectStatus> result = new HashMap<>();
                    result.put(p, status);
                    return result;
                }
            }
        }
        return null; // Return null if no applied project is found
    }

    /**
     * Displays all details of the specified project in a formatted manner.
     *
     * @param project the project to display details for
     */
    public static void displayProjectDetails(Project project) {
        if (project == null) {
            System.out.println("Project not found.");
        }

        StringBuilder details = new StringBuilder();

        // Format dates nicely
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String openingDate = project.getOpeningDate() != null ? project.getOpeningDate().format(displayFormat)
                : "Not set";
        String closingDate = project.getClosingDate() != null ? project.getClosingDate().format(displayFormat)
                : "Not set";

        // Format currency with commas and 2 decimal places
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        // Build the details string
        details.append("=".repeat(60)).append("\n");
        details.append(String.format("PROJECT: %s\n", project.getName().toUpperCase())).append("\n");
        details.append("=".repeat(60)).append("\n\n");

        // Basic information
        details.append("BASIC INFORMATION\n");
        details.append("-".repeat(30)).append("\n");
        details.append(String.format("Neighborhood: %s\n", project.getNeighbourhood()));
        details.append(String.format("Visibility: %s\n",
                project.getVisibility() ? "Visible to applicants" : "Hidden from applicants"));
        details.append(String.format("Application Period: %s to %s\n", openingDate, closingDate));
        details.append("\n");

        // Housing information
        details.append("HOUSING TYPES\n");
        details.append("-".repeat(30)).append("\n");

        Map<String, Housing> housingTypes = project.getAllHousingTypes();
        if (housingTypes != null && !housingTypes.isEmpty()) {
            // Display using the new housing types map
            for (Map.Entry<String, Housing> entry : housingTypes.entrySet()) {
                Housing housing = entry.getValue();
                details.append(String.format("Type: %s\n", entry.getKey()));
                details.append(String.format("  Price: %s\n", currencyFormat.format(housing.getSellingPrice())));
                details.append(String.format("  Available Units: %d\n", housing.getNumberOfUnits()));
                details.append("\n");
            }
        }

        // Management information
        details.append("MANAGEMENT\n");
        details.append("-".repeat(30)).append("\n");
        details.append(String.format("Manager: %s\n",
                project.getManager() != null ? project.getManager().getName() : "Unassigned"));
        details.append(String.format("Officer Slots: %d/%d (Assigned/Total)\n",
                project.getAssignedOfficers().size(), project.getSlots()));

        // Display assigned officers
        List<HDBOfficer> officers = project.getAssignedOfficers();
        if (officers != null && !officers.isEmpty()) {
            details.append("Assigned Officers:\n");
            int count = 1;
            for (HDBOfficer officer : officers) {
                details.append(String.format("  %d. %s\n", count++, officer.getName()));
            }
        } else {
            details.append("No officers assigned to this project.\n");
        }
        details.append("\n");

        // Pending officer requests
        List<Registration> pendingRegistrations = project.getRegistrationList();
        if (pendingRegistrations != null && !pendingRegistrations.isEmpty()) {
            details.append("Pending Registrations:\n");
            int count = 1;
            for (Registration registration : pendingRegistrations) {
                if (registration.getRegistrationStatus() == RegistrationStatus.PENDING) {
                    details.append(String.format("  %d. %s\n", count++, registration.getOfficer().getName()));
                }
            }
            details.append("\n");
        }

        // Registration information
        List<Registration> registrations = project.getRegistrationList();
        details.append("REGISTRATIONS\n");
        details.append("-".repeat(30)).append("\n");
        if (registrations != null && !registrations.isEmpty()) {
            details.append(String.format("Total Registrations: %d\n", registrations.size()));
            // You could add more detailed registration statistics here
        } else {
            details.append("No registrations for this project.\n");
        }
        details.append("\n");

        // Enquiry information
        List<Enquiry> enquiries = project.getEnquiries();
        details.append("ENQUIRIES\n");
        details.append("-".repeat(30)).append("\n");
        if (enquiries != null && !enquiries.isEmpty()) {
            details.append(String.format("Total Enquiries: %d\n", enquiries.size()));
            // You could add more detailed enquiry statistics here
        } else {
            details.append("No enquiries for this project.\n");
        }

        details.append("\n");
        details.append("=".repeat(60)).append("\n");

        System.out.println(details);
    }

    /**
     * Checks if the manager can handle only one project within each application period.
     *
     * @param project the project to check
     * @param manager the manager
     * @return true if the manager can handle only one project within each application period, false otherwise
     */
    private static boolean singleProjectCheck(Project project, HDBManager manager) {
        List<Project> projectList = getManagerProjects(manager);
        for (Project p: projectList) {
            if (!(p.getClosingDate().isBefore(project.getOpeningDate()) || p.getOpeningDate().isAfter(project.getClosingDate()))) { // if closing < opening or opening > closing
                return false;
            }  
        }
        return true;
    }
    
    /**
     * Creates a new project and adds it to the list if the manager is eligible.
     *
     * @param projectName the name of the project
     * @param neighbourhood the neighbourhood of the project
     * @param priceOne the price of the 2-room housing type
     * @param numberOfUnitsOne the number of units for the 2-room housing type
     * @param priceTwo the price of the 3-room housing type
     * @param numberOfUnitsTwo the number of units for the 3-room housing type
     * @param openingDate the opening date (format: yyyy-MM-dd)
     * @param closingDate the closing date (format: yyyy-MM-dd)
     * @param manager the manager for the project
     * @param officerSlots the number of officer slots
     * @throws Exception if the manager is not eligible to handle the project
     */
    public static void createProject(String projectName, String neighbourhood, float priceOne,
            int numberOfUnitsOne, float priceTwo, int numberOfUnitsTwo,
            String openingDate, String closingDate, HDBManager manager,
            int officerSlots) throws Exception {

        ProjectBuilder projectBuilder = new ProjectBuilder();
        Project project = projectBuilder
                .withName(projectName)
                .withNeighborhood(neighbourhood)
                .withVisibility(true) // default set to true
                .addHousingType("2-Room", priceOne, numberOfUnitsOne)
                .addHousingType("3-Room", priceTwo, numberOfUnitsTwo)
                .withApplicationPeriod(openingDate, closingDate)
                .withManager(manager)
                .withOfficerSlots(officerSlots)
                .build();

        if (singleProjectCheck(project, manager)) {
            projects.add(project);
            save();
        } else {
            throw new Exception("Manager can only handle one Project within each application period.");
        }
    }

    /**
     * Deletes the specified project from the list.
     *
     * @param project the project to delete
     */
    public static void deleteProject(Project project) {
        projects.remove(project);
        save();
    }

    /**
     * Checks if the specified project is visible to applicants.
     *
     * @param project the project
     * @return true if the project is visible, false otherwise
     */
    public static boolean isProjectVisible(Project project) {
        return project.getVisibility();
    }

    /**
     * Toggles the visibility of the specified project.
     *
     * @param project the project
     */
    public static void toggleProjectVisibility(Project project) {
        project.setVisiblity(!project.getVisibility());
        save();
    }

    /**
     * Updates the name of the specified project.
     *
     * @param project the project
     * @param name the new name
     */
    public static void updateProjectName(Project project, String name) {
        project.setName(name);
        save();
    }

    /**
     * Updates the neighbourhood of the specified project.
     *
     * @param project the project
     * @param neighbourhood the new neighbourhood
     */
    public static void updateProjectNeighbourhood(Project project, String neighbourhood) {
        project.setNeighbourhood(neighbourhood);
        save();
    }

    /**
     * Updates the opening date of the specified project.
     *
     * @param project the project
     * @param openingDate the new opening date (format: yyyy-MM-dd)
     */
    public static void updateProjectOpeningDate(Project project, String openingDate) {
        project.setOpeningDate(LocalDate.parse(openingDate.trim(), formatter));
        save();
    }

    /**
     * Updates the closing date of the specified project.
     *
     * @param project the project
     * @param closingDate the new closing date (format: yyyy-MM-dd)
     */
    public static void updateProjectClosingDate(Project project, String closingDate) {
        project.setClosingDate(LocalDate.parse(closingDate.trim(), formatter));
        save();
    }

    /**
     * Updates the number of officer slots for the specified project.
     *
     * @param project the project
     * @param slots the new number of officer slots
     */
    public static void updateProjectSlots(Project project, int slots) {
        project.setOfficerSlot(slots);
        save();
    }

    /**
     * Updates the specified housing type for the project.
     *
     * @param project the project
     * @param typeName the housing type name
     * @param sellingPrice the new selling price
     * @param numberOfUnits the new number of units
     */
    public static void updateHousingType(Project project, String typeName, float sellingPrice, int numberOfUnits) {
        project.setHousingType(typeName, sellingPrice, numberOfUnits);
        save();
    }

    /**
     * Adds an applicant to the specified project with a default status of PENDING.
     *
     * @param project the project
     * @param applicant the applicant to add
     */
    public static void addApplicant(Project project, Applicant applicant) {
        project.addApplicant(applicant, ProjectStatus.PENDING); // default pending, unless changed my manager
        save();
    }

    /**
     * Updates the status of the specified applicant in the project.
     *
     * @param project the project
     * @param applicant the applicant
     * @param status the new status
     */
    public static void updateApplicantStatus(Project project, Applicant applicant, ProjectStatus status) {
        project.getApplicantswithStatus().put(applicant, status);
        FileIOUtil.saveBookingDetails(UserManager.getUsers());
        save();
    }

    /**
     * Adds an officer to the specified project.
     *
     * @param project the project
     * @param officer the officer to add
     */
    public static void updateOfficer(Project project, HDBOfficer officer) {
        project.addOfficer(officer);
        save();
    }
    
    /**
     * Returns a map of applicants with the specified status for the given project.
     *
     * @param project the project
     * @param status the status to filter by
     * @return a map of applicants and their status matching the given status
     */
    public static Map<Applicant, ProjectStatus> getApplicationsByStatus(Project project, ProjectStatus status) {
        Map<Applicant, ProjectStatus> applications = new HashMap<>();

        for (Map.Entry<Applicant, ProjectStatus> entry : project.getApplicantswithStatus().entrySet()) {
            if (entry.getValue() == status) {
                applications.put(entry.getKey(), entry.getValue());
            }
        }
        return applications;
    }


}
