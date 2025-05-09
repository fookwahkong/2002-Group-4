package bto.boundary;

import bto.controller.enquiry.EnquiryController;
import bto.controller.password.IUserController;
import bto.controller.password.PasswordController;
import bto.controller.project.ProjectController;
import bto.controller.registration.RegistrationController;
import bto.entity.Enquiry;
import bto.entity.Registration;
import bto.entity.project.Project;
import bto.entity.user.Applicant;
import bto.entity.user.HDBManager;
import bto.entity.user.User;
import bto.enums.MaritalStatus;
import bto.enums.ProjectStatus;
import bto.enums.RegistrationStatus;
import bto.enums.UserRole;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A class representing a manager UI.
 */
public class ManagerUI extends UserUI {
    private final HDBManager currentUser;
    IUserController controller = new PasswordController();
    private final ChangePasswordUI changePasswordUI = new ChangePasswordUI(controller);

    // Menu options as constants to improve readability
    private static final int VIEW_PROJECTS = 1;
    private static final int CREATE_PROJECT = 2;
    private static final int EDIT_PROJECT = 3;
    private static final int DELETE_PROJECT = 4;
    private static final int VIEW_OFFICER_REGISTRATION_LIST = 5;
    private static final int APPROVE_REJECT_OFFICER = 6;
    private static final int APPROVE_REJECT_APPLICATION = 7;
    private static final int APPROVE_REJECT_WITHDRAWAL = 8;
    private static final int GENERATE_REPORTS = 9;
    private static final int VIEW_ENQUIRIES = 10;
    private static final int REPLY_ENQUIRIES = 11;
    private static final int CHANGE_PASSWORD = 12;

    // Housing type names
    private static final String TYPE_ONE = "2-Room";
    private static final String TYPE_TWO = "3-Room";

    /**
     * Constructor for the ManagerUI class.
     * 
     * @param user the user to be displayed in the UI
     */
    public ManagerUI(User user) {
        super(user);

        // downcasting from user to manager
        if (user != null && user.getUserRole() == UserRole.HDB_MANAGER) {
            this.currentUser = (HDBManager) user;
        } else {
            throw new IllegalStateException("Current user is not an HDB Manager");
        }
    }

    /**
     * Get the maximum menu option.
     * 
     * @return the maximum menu option
     */
    @Override
    protected int getMaxMenuOption() {
        return 12;
    }

    /**
     * Process the input.
     * 
     * @param choice the choice to be processed
     */
    @Override
    public void processInput(int choice) {
        switch (choice) {
            case VIEW_PROJECTS -> viewAllProjects(); // option 1
            case CREATE_PROJECT -> createHDBProject(); // option 2
            case EDIT_PROJECT -> editHDBProject(); // option 3
            case DELETE_PROJECT -> deleteHDBProject(); // option 4
            case VIEW_OFFICER_REGISTRATION_LIST -> viewOfficerRegistration(); //option 5
            case APPROVE_REJECT_OFFICER -> approveRejectOfficer(); //option 6
            case APPROVE_REJECT_APPLICATION -> approveRejectApplication(); //option 7
            case APPROVE_REJECT_WITHDRAWAL -> approveRejectWithdrawal(); //option 8
            case GENERATE_REPORTS -> generateReport(); //option 9
            case VIEW_ENQUIRIES -> viewAllEnquiries(); // option 10
            case REPLY_ENQUIRIES -> viewAndReplyToEnquiries(); // option 11
            case CHANGE_PASSWORD -> changePasswordUI.displayChangePasswordMenu(); // option 12
        }
    }

    /**
     * Display the menu options.
     */
    @Override
    public void displayMenuOptions() {
        String[] menuOptions = {
            "MANAGER UI",
            "===============================",
            "1. View Projects",
            "2. Create HDB Project",
            "3. Edit HDB Project",
            "4. Delete HDB Project",
            "5. View Pending and Approved HDB Officer Registration",
            "6. Approve / Reject HDB Officer Registration",
            "7. Approve / Reject BTO Application",
            "8. Approve / Reject Application Withdrawal",
            "9. Generate Reports",
            "10. View All Enquiries",
            "11. View and Reply to Enquiries",
            "12. Change Password",
            "0. Logout",
            "===============================",
            "Enter your choice: "
        };
        
        try {
            Thread.sleep(1000);
            for (String option : menuOptions) {
                System.out.println(option);
            }
        } catch (InterruptedException e) {
            System.out.println("Error displaying menu options: " + e.getMessage());
        }
    }

    /**
     * View all projects.
     */
    protected void viewAllProjects() {
        List<Project> projectsToShow = selectProjectList();

        if (projectsToShow == null || projectsToShow.isEmpty()) {
            System.out.println("No projects to display");
            return;
        }

        displayProjectList(projectsToShow);

        int projectChoice = getIntInput("\nSelect a project to view details (0 to return): ");
        if (projectChoice < 1 || projectChoice > projectsToShow.size()) {
            if (projectChoice != 0) {
                System.out.println("Invalid project selection.");
            }
            return;
        }

        Project selectedProject = projectsToShow.get(projectChoice - 1);
        ProjectController.displayProjectDetails(selectedProject);
    }

    /**
     * Select the project list.
     * 
     * @return the project list
     */
    private List<Project> selectProjectList() {
        int filterChoice = getIntInput("Select projects to view (1. All 2. Self): ");

        return switch (filterChoice) {
            case 1 -> ProjectController.getProjectList();
            case 2 -> ProjectController.getManagerProjects(currentUser);
            default -> {
                System.out.println("Invalid choice. Returning to menu.");
                yield null;
            }
        };
    }

    /**
     * Display the project list.
     * 
     * @param projects the project list
     */
    private void displayProjectList(List<Project> projects) {
        System.out.println("Available Projects:");
        for (int i = 0; i < projects.size(); i++) {
            System.out.println((i + 1) + ". " + projects.get(i).getName());
        }
        System.out.println("0. Return to main menu");
    }

    /**
     * Create a HDB project.
     */
    protected void createHDBProject() {
        try {
            ProjectData data = collectProjectData();


            ProjectController.createProject(
                    data.name,
                    data.neighborhood,
                    data.priceOne,
                    data.unitsOne,
                    data.priceTwo,
                    data.unitsTwo,
                    data.openingDate.format(DATE_FORMATTER),
                    data.closingDate.format(DATE_FORMATTER),
                    currentUser,
                    data.officerSlots);

            System.out.println("\nHDB Project created successfully!");
        } catch (Exception e) {
            System.out.println("Failed to create HDB Project: " + e.getMessage());
        }
    }

    /**
     * Collect the project data.
     * 
     * @return the project data
     */
    private ProjectData collectProjectData() {
        ProjectData data = new ProjectData();

        data.name = getStringInput("Enter project name: ");
        data.neighborhood = getStringInput("Enter neighbourhood: ");

        data.unitsOne = getIntInput("Enter number of units for 2-Room (-1 for no units): ");
        if (data.unitsOne == -1) {
            data.priceOne = -1;
        } else {
            data.priceOne = getFloatInput("Enter type 1 (2-Room) price: ");
        }

        data.unitsTwo = getIntInput("Enter number of units for 3-Room (-1 for no units): ");
        if (data.unitsTwo == -1) {
            data.priceTwo = -1;
        } else {
            data.priceTwo = getFloatInput("Enter type 2 (3-Room) price: ");
        }

        data.openingDate = getDateInput("Enter opening date (yyyy-MM-dd): ");
        data.closingDate = getDateInput("Enter closing date (yyyy-MM-dd): ");

        data.officerSlots = getIntInput("Enter number of officer slots: ");

        return data;
    }

    // Anonymous class to store project input data (option 2)
    private static class ProjectData {
        String name;
        String neighborhood;
        float priceOne;
        int unitsOne;
        float priceTwo;
        int unitsTwo;
        LocalDate openingDate;
        LocalDate closingDate;
        int officerSlots;
    }

    /**
     * Edit a HDB project.
     */
    protected void editHDBProject() {
        List<Project> projectList = ProjectController.getManagerProjects(currentUser);

        if (projectList.isEmpty()) {
            System.out.println("You have no projects to edit.");
            return;
        }

        displayProjectListWithStatus(projectList);

        int projectIndex = getIntInput("\nSelect a project to edit (0 to cancel): ") - 1;
        if (projectIndex < 0 || projectIndex >= projectList.size()) {
            System.out.println("Returning to main menu.");
            return;
        }

        Project selectedProject = projectList.get(projectIndex);
        editProjectMenu(selectedProject);
    }

    /**
     * Display the project list with status.
     * 
     * @param projects the project list
     */
    private void displayProjectListWithStatus(List<Project> projects) {
        System.out.println("\nYour HDB Projects:");
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            String visibilityStatus = ProjectController.isProjectVisible(project) ? "Visible" : "Hidden";
            System.out.println((i + 1) + ". " + project.getName() + " (" + visibilityStatus + ")");
        }
    }

    /**
     * Display the edit project menu.
     * 
     * @param project the project
     */
    private void editProjectMenu(Project project) {
        boolean editing = true;

        while (editing) {
            displayEditOptions(project);
            int choice = getValidIntInput(0, 10);
            editing = handleEditChoice(choice, project);
        }
    }

    /**
     * Display the edit options.
     * 
     * @param project the project
     */
    private void displayEditOptions(Project project) {
        System.out.println("\nEditing Project: " + project.getName());
        System.out.println("1. Edit project name");
        System.out.println("2. Edit neighbourhood");
        System.out.println("3. Edit 2-Room price");
        System.out.println("4. Edit number of 2-Room units");
        System.out.println("5. Edit 3-Room price");
        System.out.println("6. Edit number of 3-Room units");
        System.out.println("7. Edit opening date");
        System.out.println("8. Edit closing date");
        System.out.println("9. Edit number of officer slots");
        System.out.println("10. Toggle visibility (" +
                (ProjectController.isProjectVisible(project) ? "Currently Visible" : "Currently Hidden") + ")");
        System.out.println("0. Save and return to main menu");
    }

    /**
     * Handle the edit choice.
     * 
     * @param choice the choice
     * @param project the project
     * @return true if the edit is successful, false otherwise
     */
    private boolean handleEditChoice(int choice, Project project) {
        try {
            switch (choice) {
                case 1 -> updateProjectField(
                        "Enter new project name: ",
                        newName -> ProjectController.updateProjectName(project, newName));
                case 2 -> updateProjectField(
                        "Enter new neighbourhood: ",
                        newNeighborhood -> ProjectController.updateProjectNeighbourhood(project, newNeighborhood));
                case 3 -> updateHousingTypePrice(project, TYPE_ONE);
                case 4 -> updateHousingTypeUnits(project, TYPE_ONE);
                case 5 -> updateHousingTypePrice(project, TYPE_TWO);
                case 6 -> updateHousingTypeUnits(project, TYPE_TWO);
                case 7 -> updateProjectDateField(
                        "Enter new opening date (mm/dd/yyyy): ",
                        newDate -> ProjectController.updateProjectOpeningDate(project, newDate));
                case 8 -> updateProjectDateField(
                        "Enter new closing date (mm/dd/yyyy): ",
                        newDate -> ProjectController.updateProjectClosingDate(project, newDate));
                case 9 -> updateProjectIntField(
                        "Enter new number of officer slots: ",
                        newSlots -> ProjectController.updateProjectSlots(project, newSlots));
                case 10 -> toggleProjectVisibility(project);
                case 0 -> {
                    System.out.println("Project updated successfully!");
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("Error updating project: " + e.getMessage());
        }
        return true;
    }

    /**
     * Get the current price of a housing type.
     * 
     * @param project the project
     * @param typeName the type name
     * @return the current price of the housing type
     */
    private float getHousingTypePrice(Project project, String typeName) {
        try {
            if (project.getHousingType(typeName) != null) {
                return project.getHousingType(typeName).getSellingPrice();
            } else {
                System.out.println("Housing type not found.");
                return 0.0f;
            }
        } catch (Exception e) {
            System.out.println("Error retrieving housing price: " + e.getMessage());
            return 0.0f;
        }
    }

    /**
     * Get the current units of a housing type.
     * 
     * @param project the project
     * @param typeName the type name
     * @return the current units of the housing type
     */
    private int getHousingTypeUnits(Project project, String typeName) {
        try {
            if (project.getHousingType(typeName) != null) {
                return project.getHousingType(typeName).getNumberOfUnits();
            } else {
                System.out.println("Housing type not found.");
                return 0;
            }
        } catch (Exception e) {
            System.out.println("Error retrieving housing units: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Update the price of a specific housing type.
     * 
     * @param project the project
     * @param typeName the type name
     */
    private void updateHousingTypePrice(Project project, String typeName) {
        // Get the current housing details to preserve units
        float currentPrice = getHousingTypePrice(project, typeName);
        int currentUnits = getHousingTypeUnits(project, typeName);

        // Display the current price for reference
        System.out.println("Current " + typeName + " price: " + currentPrice);
        float newPrice = getFloatInput("Enter new " + typeName + " price: ");

        ProjectController.updateHousingType(project, typeName, newPrice, currentUnits);
        System.out.println(typeName + " price updated successfully.");
    }

    /**
     * Update the units of a specific housing type.
     * 
     * @param project the project
     * @param typeName the type name
     */
    private void updateHousingTypeUnits(Project project, String typeName) {
        // Get the current housing details to preserve price
        float currentPrice = getHousingTypePrice(project, typeName);
        int currentUnits = getHousingTypeUnits(project, typeName);

        // Display the current units for reference
        System.out.println("Current number of " + typeName + " units: " + currentUnits);
        int newUnits = getIntInput("Enter new number of " + typeName + " units: ");

        ProjectController.updateHousingType(project, typeName, currentPrice, newUnits);
        System.out.println(typeName + " units updated successfully.");
    }

    /**
     * Update the project field.
     * 
     * @param prompt the prompt
     * @param updateAction the update action
     */
    private void updateProjectField(String prompt, Consumer<String> updateAction) {
        String input = getStringInput(prompt);
        updateAction.accept(input);
    }

    /**
     * Update the project integer field.
     * 
     * @param prompt the prompt
     * @param updateAction the update action
     */
    private void updateProjectIntField(String prompt, Consumer<Integer> updateAction) {
        int input = getIntInput(prompt);
        updateAction.accept(input);
    }


    /**
     * Update the project date field.
     * 
     * @param prompt the prompt
     * @param updateAction the update action
     */
    private void updateProjectDateField(String prompt, Consumer<String> updateAction) {
        LocalDate date = getDateInput(prompt);
        updateAction.accept(date.format(DATE_FORMATTER));
    }

    /**
     * Toggle the project visibility.
     * 
     * @param project the project
     */
    private void toggleProjectVisibility(Project project) {
        boolean currentVisibility = ProjectController.isProjectVisible(project);
        ProjectController.toggleProjectVisibility(project);
        System.out.println(
                "Project visibility toggled to: " + (!currentVisibility ? "Visible" : "Hidden"));
    }

    /**
     * Delete a HDB project.
     */
    protected void deleteHDBProject() {
        List<Project> projectList = ProjectController.getProjectList();

        if (projectList.isEmpty()) {
            System.out.println("No projects available to delete.");
            return;
        }

        displayProjectList(projectList);

        int projIndex = getIntInput("Select the project to delete (0 to cancel): ") - 1;
        if (projIndex < 0 || projIndex >= projectList.size()) {
            System.out.println("Invalid selection or cancelled. Returning to menu.");
            return;
        }

        Project proj = projectList.get(projIndex);
        ProjectController.deleteProject(proj);
        System.out.println("Project \"" + proj.getName() + "\" deleted successfully.");
    }

    /**
     * Get the registration list.
     * 
     * @return the registration list
     */
    private List<Registration> getRegistrationList() {
        List<Registration> registrationList = new ArrayList<>();
        List<Project> projectList = ProjectController.getManagerProjects(currentUser);
        for (Project p: projectList) {
            registrationList.addAll(p.getRegistrationList());
        }
        return registrationList;
    }

    /**
     * View the officer registration.
     */
    protected void viewOfficerRegistration() {
        List<Registration> registrationList = getRegistrationList();

        int i = 0;
        System.out.println("Registration List:");
        System.out.println("=========================");
        for (Registration r: registrationList) {
            i += 1;
            System.out.print(i + ". ");
            r.viewRegistration();
            System.out.println();
        }
    }

    /**
     * Approve or reject an officer.
     */
    protected void approveRejectOfficer() {
        viewOfficerRegistration(); // print registration list
        List<Registration> registrationList = getRegistrationList();

        if (registrationList.isEmpty()) {
            System.out.println("There is no registration for your project currently");
            return;
        }

        System.out.print("Which Registration do you want to Approve/Reject?  ");
        int index = getValidIntInput(1, registrationList.size()) - 1;

        // print registration selected
        Registration r = registrationList.get(index);

        // Prevent changes to Approved / Rejected Registration
        if (r.getRegistrationStatus() != RegistrationStatus.PENDING) {
            System.out.println("You are not allowed to change the approval state of a Registration.");
            return;
        }

        r.viewRegistration();
        System.out.println("1. Approve");
        System.out.println("2. Reject");
        System.out.println("3. Cancel");
        System.out.print("Enter your choice: ");
        int choice = getValidIntInput(1, 3);

        switch (choice) {
            case 1: {
                if (RegistrationController.approveRegistration(r)) {
                    System.out.println("Registration Approved.");
                } else {
                    System.out.println("Cannot approve registration: No more officer slots available.");
                }
                return;
            }
            case 2: {
                RegistrationController.rejectRegistration(r);
                return;
            }
            default: {
            }
        }
    }

    /**
     * Approve or reject an application.
     */
    protected void approveRejectApplication() {
        List<Project> projectList = ProjectController.getManagerProjects(currentUser);

        System.out.println("Available Projects:");
        for (int i = 0; i < projectList.size(); i++) {
            System.out.println((i + 1) + ". " + projectList.get(i).getName() + 
            " (No. of 2-Room units: " + projectList.get(i).getHousingType(TYPE_ONE).getNumberOfUnits() +
            ", No. of 3-Room units: " + projectList.get(i).getHousingType(TYPE_TWO).getNumberOfUnits() +")");
        }
        System.out.println("0. Return to main menu");

        int projIndex = getIntInput("Select the project to view (0 to cancel): ") - 1;
        if (projIndex < 0 || projIndex >= projectList.size()) {
            System.out.println("Invalid selection or cancelled. Returning to menu.");
            return;
        }

        Project selectedProject = projectList.get(projIndex);

        // Get only applicants with PENDING status
        Map<Applicant, ProjectStatus> pendingApplicantsMap =
                ProjectController.getApplicationsByStatus(selectedProject, ProjectStatus.PENDING);

        if (pendingApplicantsMap.isEmpty()) {
            System.out.println("No pending applications for this project.");
            return;
        }

        // Convert keys to a list to support index selection
        List<Applicant> pendingApplicants = new ArrayList<>(pendingApplicantsMap.keySet());

        displayApplicantListWithStatus(pendingApplicantsMap);

        int appIndex = getIntInput("Select the applicant to approve/reject (0 to cancel): ") - 1;
        if (appIndex < 0 || appIndex >= pendingApplicants.size()) {
            System.out.println("Invalid selection or cancelled. Returning to menu.");
            return;
        }

        Applicant selectedApplicant = pendingApplicants.get(appIndex);
        ProjectStatus currentStatus = selectedProject.getApplicantStatus(selectedApplicant);
        System.out.println("Current status for " + selectedApplicant.getName() + ": " + currentStatus);

        String newStatusStr = getStringInput("Enter new status for the application (approve/reject): ").toLowerCase();
        ProjectStatus newStatus;

        if ("approve".equals(newStatusStr)) {
            newStatus = ProjectStatus.SUCCESSFUL;
        } else if ("reject".equals(newStatusStr)) {
            newStatus = ProjectStatus.UNSUCCESSFUL;
        } else {
            System.out.println("Invalid status. Returning to menu.");
            return;
        }

        ProjectController.updateApplicantStatus(selectedProject, selectedApplicant, newStatus);
        System.out.println("Status updated to " + newStatus + " for " + selectedApplicant.getName());
    }

    /**
     * Display the applicant list with status.
     * 
     * @param applicantsWithStatus the applicants with status
     */
    private void displayApplicantListWithStatus(Map<Applicant, ProjectStatus> applicantsWithStatus) {
        System.out.println("Applications:");

        int index = 1;
        for (Map.Entry<Applicant, ProjectStatus> entry : applicantsWithStatus.entrySet()) {
            Applicant applicant = entry.getKey();
            ProjectStatus status = entry.getValue();

            System.out.println(index + ". " + applicant.getName() + " (" + status + ")");
            index++;
        }

        if (index == 1) {
            System.out.println("No applicants found with the selected status.");
        }

        System.out.println("0. Return to main menu");
    }

    /**
     * Approve or reject a withdrawal.
     */
    protected void approveRejectWithdrawal() {
        List<Project> projectList = ProjectController.getManagerProjects(currentUser);
        displayProjectList(projectList);

        int projIndex = getIntInput("Select the project to view (0 to cancel): ") - 1;
        if (projIndex < 0 || projIndex >= projectList.size()) {
            System.out.println("Invalid selection or cancelled. Returning to menu.");
            return;
        }

        Project selectedProject = projectList.get(projIndex);

        Map<Applicant, ProjectStatus> withdrawingBookedMap =
                ProjectController.getApplicationsByStatus(selectedProject, ProjectStatus.WITHDRAW_FROM_BOOKED);
        Map<Applicant, ProjectStatus> withdrawingSuccessfulMap =
                ProjectController.getApplicationsByStatus(selectedProject, ProjectStatus.WITHDRAW_FROM_SUCCESSFUL);
        Map<Applicant, ProjectStatus> withdrawingApplicantsMap = new HashMap<>();
        withdrawingApplicantsMap.putAll(withdrawingBookedMap);
        withdrawingApplicantsMap.putAll(withdrawingSuccessfulMap);

        if (withdrawingApplicantsMap.isEmpty()) {
            System.out.println("No pending withdrawals for this project.");
            return;
        }

        List<Applicant> pendingApplicants = new ArrayList<>(withdrawingApplicantsMap.keySet());

        displayApplicantListWithStatus(withdrawingApplicantsMap);

        int appIndex = getIntInput("Select the withdrawal to approve / reject (0 to cancel): ") - 1;
        if (appIndex < 0 || appIndex >= pendingApplicants.size()) {
            System.out.println("Invalid selection or cancelled. Returning to menu.");
            return;
        }

        Applicant selectedApplicant = pendingApplicants.get(appIndex);
        ProjectStatus currentStatus = selectedProject.getApplicantStatus(selectedApplicant);
        System.out.println("Current status for " + selectedApplicant.getName() + ": " + currentStatus);

        String newStatusStr = getStringInput("Enter new status for the application (approve/reject): ").toLowerCase();
        ProjectStatus newStatus;

        if ("approve".equals(newStatusStr)) {
            newStatus = ProjectStatus.UNSUCCESSFUL;
            if (currentStatus == ProjectStatus.WITHDRAW_FROM_BOOKED) {
                selectedApplicant.removeBooking(selectedProject);
            }
        } else if ("reject".equals(newStatusStr)) {
            if (currentStatus == ProjectStatus.WITHDRAW_FROM_BOOKED) {
                newStatus = ProjectStatus.BOOKED;
            } else {
                newStatus = ProjectStatus.SUCCESSFUL;
            }
        } else {
            System.out.println("Invalid status. Returning to menu.");
            return;
        }
        
        ProjectController.updateApplicantStatus(selectedProject, selectedApplicant, newStatus);
        System.out.println("Status updated to " + newStatus + " for " + selectedApplicant.getName());

    }

    /**
     * Generate a report.
     */
    protected void generateReport() {

        List<Project> projects = ProjectController.getProjectList();

        System.out.println("\n===== REPORT GENERATION =====");
        System.out.println("1. All applicants");
        System.out.println("2. Filter by marital status");
        System.out.println("3. Filter by flat type");
        System.out.println("4. Filter by age range");
        System.out.println("0. Return to main menu");

        int filterChoice = getValidIntInput(0, 4);
        if (filterChoice == 0) return;

        MaritalStatus maritalStatus = null;
        String flatTypeFilter = null;
        int minAge = 0;
        int maxAge = Integer.MAX_VALUE;

        switch (filterChoice) {
            case 2 -> {
                try {
                    String statusInput = getStringInput("Enter marital status to filter by (SINGLE, MARRIED): ");
                    maritalStatus = MaritalStatus.valueOf(statusInput.toUpperCase());
                    System.out.println(maritalStatus);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid marital status. Using no filter.");
                }
            }
            case 3 -> {
                flatTypeFilter = getStringInput("Enter flat type to filter by (2-Room, 3-Room): ");
                if (!"2-Room".equals(flatTypeFilter) && !"3-Room".equals(flatTypeFilter)) {
                    System.out.println("Invalid flat type. Using no filter.");
                    flatTypeFilter = null;
                }
            }
            case 4 -> {
                try {
                    minAge = getIntInput("Enter minimum age: ");
                    maxAge = getIntInput("Enter maximum age: ");
                    if (minAge > maxAge) {
                        System.out.println("Minimum age cannot be greater than maximum age. Using no age filter.");
                        minAge = 0;
                        maxAge = Integer.MAX_VALUE;
                    }
                } catch (Exception e) {
                    System.out.println("Invalid age input. Using no age filter.");
                    minAge = 0;
                    maxAge = Integer.MAX_VALUE;
                }
            }
        }

        System.out.println("\n======= REPORT ========");
        System.out.println("Name | Age | Marital Status | Flat Type | Project Name");
        System.out.println("--------------------------------------------------");

        int recordCount = 0;
        for (Project project: projects) {
            for (Applicant applicant: project.getApplicants()) {
                Map<Project, String> bookingDetails = applicant.getBookingDetails();
                String flatType = bookingDetails.get(project);

                if (flatType == null) continue;

                if (maritalStatus != null && !applicant.getMaritalStatus().equals(maritalStatus)) continue;
                if (flatTypeFilter != null && !flatType.equals(flatTypeFilter)) continue;
                if (applicant.getAge() < minAge || applicant.getAge() > maxAge) continue;

                System.out.printf("%s | %d | %s | %s | %s\n",
                        applicant.getName(),
                        applicant.getAge(),
                        applicant.getMaritalStatus(),
                        flatType,
                        project.getName());
                recordCount++;
            }
        }

        if (recordCount == 0) {
            System.out.println("No records match the selected filters.");
        } else {
            System.out.println("\nTotal records: " + recordCount);
        }

        System.out.println("======= END REPORT ========");
    }
    

    /**
     * View all enquiries.
     */
    protected void viewAllEnquiries() {
        List<Enquiry> enquiryList = EnquiryController.getAllEnquiries();

        if (enquiryList.isEmpty()) {
            System.out.println("No enquiries found.");
            return;
        }

        displayEnquiryList(enquiryList);
    }

    /**
     * Display the enquiry list.
     * 
     * @param enquiries the enquiry list
     */
    private void displayEnquiryList(List<Enquiry> enquiries) {
        System.out.println("\nEnquiries:");
        for (int i = 0; i < enquiries.size(); i++) {
            System.out.print((i + 1) + ". ");
            enquiries.get(i).viewEnquiry(currentUser.getUserRole());
            System.out.println(); // Add a newline for better formatting
        }
    }

    /**
     * View and reply to enquiries.
     */
    protected void viewAndReplyToEnquiries() {
        List<Enquiry> enquiries = EnquiryController.getEnquiriesByManager(currentUser);
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries assigned to you.");
            return;
        }

        System.out.println("Enquiries you are handling:");
        displayEnquiryList(enquiries);

        int enquiryIndex = getIntInput("Select an enquiry to reply (0 to cancel): ") - 1;
        if (enquiryIndex < 0 || enquiryIndex >= enquiries.size()) {
            System.out.println("Returning to menu.");
            return;
        }

        Enquiry selectedEnquiry = enquiries.get(enquiryIndex);
        String reply = getStringInput("Enter your reply: ");
        EnquiryController.replyToEnquiry(selectedEnquiry, reply);
        System.out.println("Reply sent successfully!");
    }
}