package main.boundary;

import main.controller.enquiry.EnquiryController;
import main.controller.project.ProjectController;
import main.controller.user.UserManager;
import main.entity.Enquiry;
import main.entity.project.Project;
import main.entity.user.HDBManager;
import main.entity.user.User;
import main.enums.UserRole;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

public class ManagerUI extends UI {
    private final HDBManager currentUser;
    private final ChangePasswordUI changePasswordUI = new ChangePasswordUI();

    // Menu options as constants to improve readability
    private static final int VIEW_PROJECTS = 1;
    private static final int CREATE_PROJECT = 2;
    private static final int EDIT_PROJECT = 3;
    private static final int DELETE_PROJECT = 4;
    private static final int VIEW_ENQUIRIES = 5;
    private static final int REPLY_ENQUIRIES = 6;
    private static final int CHANGE_PASSWORD = 7;
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
                int choice = getValidIntInput(0, 7);
                running = handleMenuChoice(choice);
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private boolean handleMenuChoice(int choice) {
        switch (choice) {
            case VIEW_PROJECTS -> viewProjects(); // option 1
            case CREATE_PROJECT -> createHDBProject(); // option 2
            case EDIT_PROJECT -> editHDBProject(); // option 3
            case DELETE_PROJECT -> deleteHDBProject(); // option 4
            case VIEW_ENQUIRIES -> viewAllEnquiries(); // option 5
            case REPLY_ENQUIRIES -> viewAndReplyToEnquiries(); // option 6
            case CHANGE_PASSWORD -> changePasswordUI.showChangePasswordMenu(); // option 7
            case LOGOUT -> { // option 0
                UserManager.getInstance().logout();
                new LoginUI().startLogin();
                return false;
            }
        }
        return true;
    }

    private void displayMenuOptions() {
        System.out.print("""
                \n========== MANAGER UI ==========
                Please select an option:
                ---------------------------------
                1. View Projects
                2. Create HDB Project
                3. Edit HDB Project
                4. Delete HDB Project
                5. View All Enquiries
                6. View and Reply to Enquiries
                7. Change Password
                0. Logout
                ---------------------------------
                Your choice: """);
    }

    // option 1
    private void viewProjects() {
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
    private void createHDBProject() {
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

    // Helper class to store project input data (option 2)
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
    private void editHDBProject() {
        List<Project> projectList = ProjectController.getProjectList();

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

    /**
     * Helper method to get the current price of a housing type
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
     * Helper method to get the current units of a housing type
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

    private void updateProjectFloatField(String prompt, Consumer<Float> updateAction) {
        float input = getFloatInput(prompt);
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
    private void deleteHDBProject() {
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

    // option 5
    private void viewAllEnquiries() {
        List<Enquiry> enquiryList = EnquiryController.getEnquiriesList(currentUser);

        if (enquiryList.isEmpty()) {
            System.out.println("No enquiries found.");
            return;
        }

        displayEnquiryList(enquiryList);
    }

    // helper method for option 5
    private void displayEnquiryList(List<Enquiry> enquiries) {
        System.out.println("\nEnquiries:");
        for (int i = 0; i < enquiries.size(); i++) {
            System.out.print((i + 1) + ". ");
            enquiries.get(i).viewEnquiry(currentUser.getUserRole());
            System.out.println(); // Add a newline for better formatting
        }
    }

    // option 6
    private void viewAndReplyToEnquiries() {
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