package main.controller.project;

import main.controller.user.UserManager;
import main.entity.Enquiry;
import main.entity.Housing;
import main.entity.Registration;
import main.entity.project.Project;
import main.entity.project.ProjectBuilder;
import main.entity.user.Applicant;
import main.entity.user.HDBManager;
import main.entity.user.HDBOfficer;
import main.enums.MaritalStatus;
import main.enums.ProjectStatus;
import main.utils.FileIOUtil;

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

    public static void loadRawData() {
        projects = FileIOUtil.loadProjects();   //load projects without resolving references 
    }

    public static void resolveReferences() {
        for (Project project: projects) {
            //resolve manager
            if (project.getManager() == null) {
                HDBManager manager = (HDBManager) UserManager.getInstance().findUserByName(project.getManager().getName()); 

                if (manager != null) {
                    project.setManagerInCharge(manager);
                } else {
                    System.err.println("Warning: Manager not found for project: " + project.getName());
                }
            }

            List<Applicant> resolvedApplicants = new ArrayList<>();
            for (Applicant applicant: project.getApplicants()) {
                Applicant resolvedApplicant = (Applicant) UserManager.getInstance().findUserByName(applicant.getName());

                if (resolvedApplicant != null) {
                    resolvedApplicants.add(resolvedApplicant);
                } else {
                    System.err.println("Warning: Applicant not found for project:" + project.getName());
                } 
            }
            project.getApplicants().clear(); //to clear the previously allocated applicants when loading Raw Data
            project.getApplicants().addAll(resolvedApplicants);
        }
    }

    // get ALL the projects
    public static List<Project> getProjectList() {
        return projects;
    }

    // get projects managed by manager
    public static List<Project> getManagerProjects(HDBManager user) {
        return ProjectController.getProjectList().stream()
                .filter(project -> {
                    System.out.println("Comparing managers: " + project.getManager() + " and " + user);
                    return project.getManager().equals(user);
                })
                .toList();
    }

    // get projects managed by officer
    public static List<Project> getOfficerProjects(HDBOfficer officer) {
        return ProjectController.getProjectList().stream()
                .filter(project -> project.getAssignedOfficers().contains(officer))
                .toList();
    }

    //get all the projects that applicant from each group can see
    public static List<Project> getApplicantProjects(Applicant applicant) {
        if (applicant.getMaritalStatus() == MaritalStatus.SINGLE) {
            return ProjectController.getProjectList().stream()
            .filter(project -> project.getAllHousingTypes().get("2-Room") != null)
            .filter(project -> project.getVisibility() == true)
            .toList();
        } else if (applicant.getMaritalStatus() == MaritalStatus.MARRIED) {
            return getOpenProjects(applicant);
        } 
        return new ArrayList<>();  //return empty list if the applicant is neither conditions are met
        //(possible bug if reached here)
    }

    //get open projects
    public static List<Project> getOpenProjects(Applicant applicant) {
        return ProjectController.getProjectList().stream()
                .filter(project -> project.getVisibility() == true)
                .toList();
    }

    //get projects applied by applicant
    public static Map<Applicant,ProjectStatus> getApplicantProjectMap() {
        Map<Applicant, ProjectStatus> result = new HashMap<>();
        for (Project project : projects) {
            for (Applicant applicant : project.getApplicants()) {
                if (!result.containsKey(applicant)) {
                    result.put(applicant, project.getApplicantStatus(applicant));
                }
            }
        }
        return result;
    }

    public static Project findProjectByName(String projectName) {
        List<Project> projectList = getProjectList();
        for (Project p: projectList) {
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
        List<HDBOfficer> pendingOfficers = project.getPendingOfficers();
        if (pendingOfficers != null && !pendingOfficers.isEmpty()) {
            details.append("Pending Officer Requests:\n");
            int count = 1;
            for (HDBOfficer officer : pendingOfficers) {
                details.append(String.format("  %d. %s\n", count++, officer.getName()));
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
    
    public static void createProject(String projectName, String neighbourhood, float priceOne,
            int numberOfUnitsOne, float priceTwo, int numberOfUnitsTwo,
            String openingDate, String closingDate, HDBManager manager,
            int officerSlots) {
            
            ProjectBuilder projectBuilder = new ProjectBuilder();
            Project project = projectBuilder
                .withName(projectName)
                .withNeighborhood(neighbourhood)
                .withVisibility(true)  //default set to true 
                .addHousingType("2-Room", priceOne, numberOfUnitsOne)
                .addHousingType("3-Room", priceTwo, numberOfUnitsTwo)
                .withApplicationPeriod(openingDate, closingDate)
                .withManager(manager)
                .withOfficerSlots(officerSlots)
                .build();
    
        projects.add(project);
        FileIOUtil.saveProjectToFile(projects, FileIOUtil.PROJECTS_FILE);
    }

    public static void deleteProject(Project project) {
        projects.remove(project);
    }

    public static boolean isProjectVisible(Project project) {
        return project.getVisibility();
    }

    public static void toggleProjectVisibility(Project project) {
        project.setVisiblity(!project.getVisibility());
    }

    public static void updateProjectName(Project project, String name) {
        project.setName(name);
    }

    public static void updateProjectNeighbourhood(Project project, String neighbourhood) {
        project.setNeighbourhood(neighbourhood);
    }

    public static void updateProjectOpeningDate(Project project, String openingDate) {
        project.setOpeningDate(LocalDate.parse(openingDate.trim(), formatter));
    }

    public static void updateProjectClosingDate(Project project, String closingDate) {
        project.setClosingDate(LocalDate.parse(closingDate.trim(), formatter));
    }

    public static void updateProjectSlots(Project project, int slots) {
        project.setOfficerSlot(slots);
    }
    
    public static void updateHousingType(Project project, String typeName, float sellingPrice, int numberOfUnits) {
        project.setHousingType(typeName, sellingPrice, numberOfUnits);
    }

    public static void addApplicants(Project project, Applicant applicant) {
        project.addApplicant(applicant, ProjectStatus.PENDING); //default pending, unless changed my manager
        FileIOUtil.saveProjectToFile(projects, FileIOUtil.PROJECTS_FILE);
    } 
}
