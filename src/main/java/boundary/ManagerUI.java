package boundary;

import controller.enquiry.EnquiryController;
import controller.password.IUserController;
import controller.password.PasswordController;
import controller.project.ProjectController;
import controller.user.UserManager;
import entity.Enquiry;
import entity.Registration;
import entity.project.Project;
import entity.user.Applicant;
import entity.user.HDBManager;
import entity.user.User;
import enums.MaritalStatus;
import enums.ProjectStatus;
import enums.UserRole;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ManagerUI extends UI {
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
    private static final int LOGOUT = 0;

    // Housing type names
    private static final String TYPE_ONE = "2-Room";
    private static final String TYPE_TWO = "3-Room";

    public ManagerUI() {
        User user = UserManager.getInstance().getCurrentUser();

        // downcasting from user to manager
        if (user != null && user.getUserRole() == UserRole.HDB_MANAGER) {
            this.currentUser = (HDBManager) user;
        } else {
            throw new IllegalStateException("Current user is not an HDB Manager");
        }
    }

    public void showMenu() {
        boolean running = true;
        while (running) {
            displayMenuOptions();

            try {
                int choice = getValidIntInput(0, 12);
                running = handleMenuChoice(choice);
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private boolean handleMenuChoice(int choice) {
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
            case LOGOUT -> { // option 0
                UserManager.getInstance().logout();
                new LoginUI().startLogin();
                return false;
            }
        }
        return true;
    }

    private void displayMenuOptions() {
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
        
        for (String option : menuOptions) {
            System.out.println(option);
        }
    }

    // option 1
    protected void viewAllProjects() {
        List<Project> projectsToShow = selectProjectList();

        if (projectsToShow == null || projectsToShow.isEmpty()) {
            System.out.println("No projects to display");
            return;
        }

        displayProjectList(projectsToShow);

        int projectChoice = getIntInput("\nSelect a project to view details (0 to return): ");
        if (projectChoice == 0 || projectChoice < 1 || projectChoice > projectsToShow.size()) {
            if (projectChoice != 0) {
                System.out.println("Invalid project selection.");
            }
            return;
        }

        Project selectedProject = projectsToShow.get(projectChoice - 1);
        ProjectController.displayProjectDetails(selectedProject);
    }

    // helper method for option 1
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

    // helper method
    private void displayProjectList(List<Project> projects) {
        System.out.println("Available Projects:");
        for (int i = 0; i < projects.size(); i++) {
            System.out.println((i + 1) + ". " + projects.get(i).getName());
        }
        System.out.println("0. Return to main menu");
    }

    // option 2
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

    // helper method for option 2
    private ProjectData collectProjectData() {
        ProjectData data = new ProjectData();

        data.name = getStringInput("Enter project name: ");
        data.neighborhood = getStringInput("Enter neighbourhood: ");

        data.priceOne = getFloatInput("Enter type 1 (2-Room) price: ");
        data.unitsOne = getIntInput("Enter number of units for 2-Room: ");

        data.priceTwo = getFloatInput("Enter type 2 (3-Room) price: ");
        data.unitsTwo = getIntInput("Enter number of units for 3-Room: ");

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

    // option 3
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

    // helper method for option 3
    private void displayProjectListWithStatus(List<Project> projects) {
        System.out.println("\nYour HDB Projects:");
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            String visibilityStatus = ProjectController.isProjectVisible(project) ? "Visible" : "Hidden";
            System.out.println((i + 1) + ". " + project.getName() + " (" + visibilityStatus + ")");
        }
    }

    // helper method for option 3
    private void editProjectMenu(Project project) {
        boolean editing = true;

        while (editing) {
            displayEditOptions(project);
            int choice = getValidIntInput(0, 10);
            editing = handleEditChoice(choice, project);
        }
    }

    // helper method for option 3
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

    // helper method for option 3
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

    //Helper method to get the current price of a housing type
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

    //Helper method to get the current units of a housing type
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
     * Helper method for option 3
     * Updates the price of a specific housing type
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
     * Helper method for option 3
     * Updates the units of a specific housing type
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

    // to update project details based on the different input types
    private void updateProjectField(String prompt, Consumer<String> updateAction) {
        String input = getStringInput(prompt);
        updateAction.accept(input);
    }

    private void updateProjectIntField(String prompt, Consumer<Integer> updateAction) {
        int input = getIntInput(prompt);
        updateAction.accept(input);
    }


    private void updateProjectDateField(String prompt, Consumer<String> updateAction) {
        LocalDate date = getDateInput(prompt);
        updateAction.accept(date.format(DATE_FORMATTER));
    }

    private void toggleProjectVisibility(Project project) {
        boolean currentVisibility = ProjectController.isProjectVisible(project);
        ProjectController.toggleProjectVisibility(project);
        System.out.println(
                "Project visibility toggled to: " + (!currentVisibility ? "Visible" : "Hidden"));
    }

    // option 4
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

    // helper function for 5,6
    private List<Registration> getRegistrationList() {
        List<Registration> registrationList = new ArrayList<>();
        List<Project> projectList = ProjectController.getManagerProjects(currentUser);
        for (Project p: projectList) {
            registrationList.addAll(p.getRegistrationList());
        }
        return registrationList;
    }

    // option 5
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

    // option 6
    protected void approveRejectOfficer() {
        viewOfficerRegistration(); // print registration list
        List<Registration> registrationList = getRegistrationList();

        System.out.print("Which Registration do you want to Approve/Reject?  ");
        int index = getValidIntInput(1, registrationList.size()) - 1;

        // print registration selected
        Registration r = registrationList.get(index);
        r.viewRegistration();
        System.out.println("1. Approve");
        System.out.println("2. Reject");
        System.out.println("3. Cancel");
        System.out.print("Enter your choice: ");
        int choice = getValidIntInput(1, 3);

        switch (choice) {
            case 1: {
                r.approveRegistration();
                System.out.println("Registration Approved.");
                return;
            }
            case 2: {
                r.rejectRegistration();
                System.out.println("Registration Rejected.");
                return;
            }
            default: {
                return;
            }
        }
    }

    // option 7
    protected void approveRejectApplication() {
        List<Project> projectList = ProjectController.getManagerProjects(currentUser);
        displayProjectList(projectList);

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


    // helper method
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

    //option 8
    protected void approveRejectWithdrawal() {
        List<Project> projectList = ProjectController.getManagerProjects(currentUser);
        displayProjectList(projectList);

        int projIndex = getIntInput("Select the project to view (0 to cancel): ") - 1;
        if (projIndex < 0 || projIndex >= projectList.size()) {
            System.out.println("Invalid selection or cancelled. Returning to menu.");
            return;
        }

        Project selectedProject = projectList.get(projIndex);

        Map<Applicant, ProjectStatus> withdrawingApplicantsMap =
                ProjectController.getApplicationsByStatus(selectedProject, ProjectStatus.REQUEST_WITHDRAW);

        if (withdrawingApplicantsMap.isEmpty()) {
            System.out.println("No pending withdrawals for this project.");
            return;
        }

        List<Applicant> pendingApplicants = new ArrayList<>(withdrawingApplicantsMap.keySet());

        displayApplicantListWithStatus(withdrawingApplicantsMap);

        int appIndex = getIntInput("Select the withdrawal to approve (0 to cancel): ") - 1;
        if (appIndex < 0 || appIndex >= pendingApplicants.size()) {
            System.out.println("Invalid selection or cancelled. Returning to menu.");
            return;
        }

        Applicant selectedApplicant = pendingApplicants.get(appIndex);
        ProjectStatus currentStatus = selectedProject.getApplicantStatus(selectedApplicant);
        System.out.println("Current status for " + selectedApplicant.getName() + ": " + currentStatus);

        String newStatusStr = getStringInput("Confirm withdrawal (y/n): ").toLowerCase();
        ProjectStatus newStatus;

        if ("y".equals(newStatusStr)) {
            newStatus = ProjectStatus.UNSUCCESSFUL;
        } else if ("n".equals(newStatusStr)) {
            System.out.println("No changes made. Returning to menu.");
            return;
        } else {
            System.out.println("Invalid choice. Returning to menu.");
            return;
        }

        ProjectController.updateApplicantStatus(selectedProject, selectedApplicant, newStatus);
        System.out.println("Status updated to " + newStatus + " for " + selectedApplicant.getName());

    }

    //option 9
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

                if (maritalStatus != null && !applicant.getMaritalStatus().equals(maritalStatus.toString())) continue;
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
    

    // option 10
    protected void viewAllEnquiries() {
        List<Enquiry> enquiryList = EnquiryController.getEnquiriesList(currentUser);

        if (enquiryList.isEmpty()) {
            System.out.println("No enquiries found.");
            return;
        }

        displayEnquiryList(enquiryList);
    }

    // helper method for option 10
    private void displayEnquiryList(List<Enquiry> enquiries) {
        System.out.println("\nEnquiries:");
        for (int i = 0; i < enquiries.size(); i++) {
            System.out.print((i + 1) + ". ");
            enquiries.get(i).viewEnquiry(currentUser.getUserRole());
            System.out.println(); // Add a newline for better formatting
        }
    }

    // option 11
    protected void viewAndReplyToEnquiries() {
        List<Enquiry> enquiries = EnquiryController.getEnquiriesByManager(currentUser);
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries assigned to you.");
            return;
        }

        System.out.println("Enquiries you are handling:");
        for (int i = 0; i < enquiries.size(); i++) {
            System.out.println((i + 1) + ". " + enquiries.get(i).getContent());
        }

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