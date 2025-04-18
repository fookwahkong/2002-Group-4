package bto.controller.project;

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

public class ProjectController {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static List<Project> projects = new ArrayList<>();

    public ProjectController() {
    }

    public static void load() {
        projects = FileIOUtil.loadProjects(); 
    }

    public static void save() {
        FileIOUtil.saveProjectToFile(projects, FileIOUtil.PROJECTS_FILE);
    }

    // get ALL the projects
    public static List<Project> getProjectList() {
        return projects;
    }

    // get projects managed by manager
    public static List<Project> getManagerProjects(HDBManager manager) {
        return ProjectController.getProjectList().stream()
                .filter(project -> {
                    return project.getManager().equals(manager);
                })
                .toList();
    }

    // get projects managed by officer
    public static List<Project> getOfficerProjects(HDBOfficer officer) {
        return ProjectController.getProjectList().stream()
                .filter(project -> project.getAssignedOfficers().contains(officer))
                .toList();
    }

    // get projects yet to be managed/applied by officer
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
    

    // get all the projects that applicant from each group can see and apply
    public static List<Project> getApplicantProjects(Applicant applicant) {
        if (applicant.getMaritalStatus() == MaritalStatus.SINGLE) {
            return ProjectController.getProjectList().stream()
                    .filter(project -> project.getAllHousingTypes().get("2-Room").getNumberOfUnits() != -1)
                    .filter(project -> project.getVisibility() == true)
                    .toList();
        } else if (applicant.getMaritalStatus() == MaritalStatus.MARRIED) {
            return ProjectController.getProjectList().stream()
                .filter(project -> project.getVisibility() == true)
                .toList();
        }
        return new ArrayList<>(); // return empty list if the applicant is neither conditions are met
        // (possible bug if reached here)
    }

    // get the active project applied by the applicant
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

    public static Project findProjectByName(String projectName) {
        List<Project> projectList = getProjectList();
        for (Project p : projectList) {
            if (p.getName().equals(projectName)) {
                return p;
            }
        }
        return null; // Return null if no project is found (bug is here if not found)
    }

    /**
     * Displays all details of a project in a formatted manner.
     * Can be used in a console or for generating reports.
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

        System.out.println(details.toString());
    }

    private static boolean singleProjectCheck(Project project, HDBManager manager) {
        List<Project> projectList = getManagerProjects(manager);
        for (Project p: projectList) {
            if (!(p.getClosingDate().isBefore(project.getOpeningDate()) || p.getOpeningDate().isAfter(project.getClosingDate()))) { // if closing < opening or opening > closing
                return false;
            }  
        }
        return true;
    }

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

    public static void deleteProject(Project project) {
        projects.remove(project);
        save();
    }

    public static boolean isProjectVisible(Project project) {
        return project.getVisibility();
    }

    public static void toggleProjectVisibility(Project project) {
        project.setVisiblity(!project.getVisibility());
        save();
    }

    public static void updateProjectName(Project project, String name) {
        project.setName(name);
        save();
    }

    public static void updateProjectNeighbourhood(Project project, String neighbourhood) {
        project.setNeighbourhood(neighbourhood);
        save();
    }

    public static void updateProjectOpeningDate(Project project, String openingDate) {
        project.setOpeningDate(LocalDate.parse(openingDate.trim(), formatter));
        save();
    }

    public static void updateProjectClosingDate(Project project, String closingDate) {
        project.setClosingDate(LocalDate.parse(closingDate.trim(), formatter));
        save();
    }

    public static void updateProjectSlots(Project project, int slots) {
        project.setOfficerSlot(slots);
        save();
    }

    public static void updateHousingType(Project project, String typeName, float sellingPrice, int numberOfUnits) {
        project.setHousingType(typeName, sellingPrice, numberOfUnits);
        save();
    }

    public static void addApplicant(Project project, Applicant applicant) {
        project.addApplicant(applicant, ProjectStatus.PENDING); // default pending, unless changed my manager
        save();
    }

    public static void updateApplicantStatus(Project project, Applicant applicant, ProjectStatus status) {
        project.getApplicantswithStatus().put(applicant, status);
        save();
    }

    public static void updateOfficer(Project project, HDBOfficer officer) {
        project.addOfficer(officer);
        save();
    }
    
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
