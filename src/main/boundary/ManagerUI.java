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

public class ManagerUI extends UI {
    private final HDBManager currentUser;
    private ChangePasswordUI changePasswordUI = new ChangePasswordUI();

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
                int choice = getValidIntInput(0, 9);

                switch (choice) {
                    case 1 -> viewProjects();
                    case 2 -> createHDBProject();
                    case 3 -> editHDBProject();
                    case 4 -> deleteHDBProject();
                    case 5 -> viewAllEnquiries();
                    case 6 -> viewAndReplyToEnquiries();
                    case 7 -> changePasswordUI.showChangePasswordMenu();
                    case 8 -> viewAndApprovePendingOfficers();
                    case 9 -> viewApprovedOfficers();
                    case 0 -> {
                        UserManager.getInstance().logout();
                        running = false;
                        new LoginUI().startLogin();
                    }
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private void displayMenuOptions() {
        System.out.println("\nMANAGER UI");
        System.out.println("==================================");
        System.out.println("1. View projects");
        System.out.println("2. Create HDB Project");
        System.out.println("3. Edit HDB Project");
        System.out.println("4. Delete HDB Project");
        System.out.println("5. View all enquiries");
        System.out.println("6. View and reply enquiries on projects you handle");
        System.out.println("7. Change Password");
        System.out.println("8. View and approve pending officer approvals");
        System.out.println("9. View approved officers");
        System.out.println("0. Logout");
        System.out.print("Enter your choice: ");
    }

    private void viewProjects() {
        int filterChoice = getIntInput("Select projects to view (1. All 2. Self): ");
        List<Project> projectsToShow;

        switch (filterChoice) {
            case 1 -> projectsToShow = ProjectController.getProjectList();
            case 2 -> projectsToShow = ProjectController.getManagerProjects(currentUser);
            default -> {
                System.out.println("Invalid choice. Returning to menu.");
                return;
            }
        }

        if (projectsToShow.isEmpty()) {
            System.out.println("No projects to display");
            return;
        }

        System.out.println("Available Projects:");
        for (int i = 0; i < projectsToShow.size(); i++) {
            System.out.println((i + 1) + ". " + projectsToShow.get(i).getName());
        }
        System.out.println("0. Return to main menu");

        int projectChoice = getIntInput("\nSelect a project to view details (0 to return): ");
        if (projectChoice == 0) {
            return;
        }

        if (projectChoice < 1 || projectChoice > projectsToShow.size()) {
            System.out.println("Invalid project selection.");
            return;
        }

        // Display detailed information about the selected project
        Project selectedProject = projectsToShow.get(projectChoice - 1);
        displayProjectDetails(selectedProject);
    }

    private void displayProjectDetails(Project project) {
        System.out.println("\nProject Details:");
        System.out.println("==================================");
        System.out.println("Name: " + project.getName());
        System.out.println("Neighbourhood: " + project.getNeighbourhood());
        System.out.println("Manager: " + project.getManager().getName());

        System.out.println("\nHousing Types:");
        System.out.println(
                "- 2-Room: $" + project.getPriceTypeOne() + " (" + project.getNoOfUnitsTypeOne() +
                        " units)");
        System.out.println(
                "- 3-Room: $" + project.getPriceTypeTwo() + " (" + project.getNoOfUnitsTypeTwo() +
                        " units)");

        System.out.println("\nApplication Period:");
        System.out.println("Opening Date: " + project.getOpeningDate());
        System.out.println("Closing Date: " + project.getClosingDate());

        System.out.println("\nOfficer Slots: " + project.getSlots());

        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void createHDBProject() {
        try {
            String projectName = getStringInput("Enter project name: ");
            String neighbourHood = getStringInput("Enter neighbourhood: ");

            float priceOne = getFloatInput("Enter type 1 (2-Room) price: ");
            int noOfUnitsOne = getIntInput("Enter number of units for 2-Room: ");

            float priceTwo = getFloatInput("Enter type 2 (3-Room) price: ");
            int noOfUnitsTwo = getIntInput("Enter number of units for 3-Room: ");

            LocalDate openingDate = getDateInput("Enter opening date (mm/dd/yyyy): ");
            LocalDate closingDate = getDateInput("Enter closing date (mm/dd/yyyy): ");

            int slots = getIntInput("Enter number of officer slots: ");

            ProjectController.createProject(
                    projectName,
                    neighbourHood,
                    priceOne,
                    noOfUnitsOne,
                    priceTwo,
                    noOfUnitsTwo,
                    openingDate.format(DATE_FORMATTER),
                    closingDate.format(DATE_FORMATTER),
                    currentUser,
                    slots);

            System.out.println("HDB Project created successfully!");
        } catch (Exception e) {
            System.out.println("Failed to create HDB Project: " + e.getMessage());
        }
    }

    private void editHDBProject() {
        List<Project> projectList = ProjectController.getProjectList();

        if (projectList.isEmpty()) {
            System.out.println("You have no projects to edit.");
            return;
        }

        System.out.println("\nYour HDB Projects:");
        for (int i = 0; i < projectList.size(); i++) {
            Project project = projectList.get(i);
            String visibilityStatus = ProjectController.isProjectVisible(project) ? "Visible" : "Hidden";
            System.out.println((i + 1) + ". " + project.getName() + " (" + visibilityStatus + ")");
        }

        int projectIndex = getIntInput("\nSelect a project to edit (0 to cancel): ") - 1;
        if (projectIndex < 0 || projectIndex >= projectList.size()) {
            System.out.println("Returning to main menu.");
            return;
        }

        Project selectedProject = projectList.get(projectIndex);
        boolean editing = true;

        while (editing) {
            System.out.println("\nEditing Project: " + selectedProject.getName());
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
                    (ProjectController.isProjectVisible(selectedProject) ? "Currently Visible" :
                            "Currently Hidden") + ")");
            System.out.println("0. Save and return to main menu");

            int choice = getValidIntInput(0, 14);

            try {
                switch (choice) {
                    case 1 -> ProjectController.updateProjectName(selectedProject,
                            getStringInput("Enter new project name: "));
                    case 2 -> ProjectController.updateProjectNeighbourhood(selectedProject,
                            getStringInput("Enter new neighbourhood: "));
                    case 3 -> ProjectController.updateProjectPriceTypeOne(selectedProject,
                            getFloatInput("Enter new 2-Room price: "));
                    case 4 -> ProjectController.updateProjectNoOfUnitsTypeOne(selectedProject,
                            getIntInput("Enter new number of 2-Room units: "));
                    case 5 -> ProjectController.updateProjectPriceTypeTwo(selectedProject,
                            getFloatInput("Enter new 3-Room price: "));
                    case 6 -> ProjectController.updateProjectNoOfUnitsTypeTwo(selectedProject,
                            getIntInput("Enter new number of 3-Room units: "));
                    case 7 -> {
                        LocalDate newOpeningDate = getDateInput("Enter new opening date (mm/dd/yyyy): ");
                        ProjectController.updateProjectOpeningDate(selectedProject,
                                newOpeningDate.format(DATE_FORMATTER));
                    }
                    case 8 -> {
                        LocalDate newClosingDate = getDateInput("Enter new closing date (mm/dd/yyyy): ");
                        ProjectController.updateProjectClosingDate(selectedProject,
                                newClosingDate.format(DATE_FORMATTER));
                    }
                    case 9 -> ProjectController.updateProjectSlots(selectedProject,
                            getIntInput("Enter new number of officer slots: "));
                    case 10 -> {
                        boolean currentVisibility = ProjectController.isProjectVisible(selectedProject);
                        ProjectController.toggleProjectVisibility(selectedProject);
                        System.out.println(
                                "Project visibility toggled to: " + (!currentVisibility ? "Visible" : "Hidden"));
                    }
                 
                    case 0 -> {
                        System.out.println("Project updated successfully!");
                        editing = false;
                    }
                }
            } catch (Exception e) {
                System.out.println("Error updating project: " + e.getMessage());
            }
        }
    }


    private void deleteHDBProject() {
        List<Project> projectList = ProjectController.getProjectList();
        int cnt = 1;
        for (Project p : projectList) {
            System.out.print(cnt + ". ");
            System.out.println(p.getName());
            cnt += 1;
        }

        int projIndex = getIntInput("Select the project to delete: ") - 1;
        Project proj = projectList.get(projIndex);
        ProjectController.deleteProject(proj);
    }

    private void viewAllEnquiries() {
        List<Enquiry> enquiryList = EnquiryController.getEnquiriesList(currentUser);

        if (enquiryList.isEmpty()) {
            System.out.println("No enquiries found.");
            return;
        }

        int cnt = 1;
        for (Enquiry e : enquiryList) {
            System.out.print(cnt + ". ");
            e.viewEnquiry(currentUser.getUserRole());
            cnt++;  // Increment the counter
            System.out.println(); // Add a newline for better formatting
        }
    }

    private void viewAndReplyToEnquiries() {
        List<Enquiry> enquiries = EnquiryController.getEnquiriesByManager(currentUser);
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries assigned to you.");
            return;
        }

        System.out.println("Enquiries you are handling:");
        int index = 1;
        for (Enquiry enquiry : enquiries) {
            System.out.println(index++ + ". " + enquiry.getContent());
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

    private void viewAndApprovePendingOfficers() {
        List<Project> managedProject = currentUser.getManagerProjects();
        List<HDBOfficer> pendingOfficers = managedProject.getPendingOfficers();
        
        if (managedProject == null) {
            System.out.println("You are not assigned to any project.");
            return;
        }
        

        if (pendingOfficers.isEmpty()) {
            System.out.println("No pending officer applications for project: " + managedProject.getName());
            return;
        }

        System.out.println("\nPending officer applications for project: " + managedProject.getName());
        for (int i = 0; i < pendingOfficers.size(); i++) {
            HDBOfficer officer = pendingOfficers.get(i);
            System.out.println((i + 1) + ". " + officer.getName());
        }

        System.out.println("\nSelect officer to approve/reject (0 to cancel): ");
        int index = getValidIntInput() - 1;

        if (index == -1) return;
        if (index < 0 || index >= pendingOfficers.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        HDBOfficer selectedOfficer = pendingOfficers.get(index);

        System.out.print("Approve (A) or Reject (R)? ");
        String action = scanner.nextLine().trim().toUpperCase();

        if (action.equals("A")) {
            currentUser.approveOfficer(selectedOfficer);
        } else if (action.equals("R")) {
            currentUser.rejectOfficer(selectedOfficer);
        } else {
            System.out.println("Invalid action. Please enter A or R.");
        }
    }

}
