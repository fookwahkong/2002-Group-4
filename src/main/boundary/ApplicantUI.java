package main.boundary;

import main.controller.project.ProjectController;
import main.controller.user.ApplicantController;
import main.controller.user.UserManager;
import main.entity.Enquiry;
import main.entity.project.Project;
import main.entity.user.Applicant;
import main.entity.user.User;
import main.enums.ProjectStatus;
import main.enums.UserRole;

import java.util.List;
import java.util.Map;

public class ApplicantUI extends UI {
    protected final User currentUser;
    private ChangePasswordUI changePasswordUI = new ChangePasswordUI();

    private static final int VIEW_OPEN_PROJECTS = 1;
    private static final int APPLY_PROJECT = 2;
    private static final int VIEW_APPLIED_PROJECTS = 3;
    private static final int FLAT_BOOKING = 4;
    private static final int WITHDRAW_BOOKING = 5;
    private static final int SUBMIT_ENQUIRY = 6;
    private static final int VIEW_ENQUIRY = 7;
    private static final int EDIT_ENQUIRY = 8;
    private static final int DELETE_ENQUIRY = 9;
    private static final int CHANGE_PASSWORD = 10;
    private static final int LOGOUT = 0;

    public ApplicantUI() {
        User user = UserManager.getInstance().getCurrentUser();

        if (isValidUser(user)) {
            this.currentUser = user;
        } else {
            throw new IllegalStateException("Current user is not an Applicant");
        }
    }

    // method to validate the user, can be overriden by subclass (OfficerUI)
    protected boolean isValidUser(User user) {
        return user != null && user.getUserRole() == UserRole.APPLICANT;
    }

    protected Applicant getApplicantUser() {
        if (currentUser instanceof Applicant) {
            return (Applicant) currentUser;
        }
        throw new IllegalStateException("Current user is not an Applicant");
    }

    public void showMenu() {
        boolean running = true;
        while (running) {
            displayMenuOptions();

            try {
                int choice = getValidIntInput(0, 10);

                switch (choice) {
                    case VIEW_OPEN_PROJECTS -> viewOpenProjects();
                    case APPLY_PROJECT -> applyProject();
                    case VIEW_APPLIED_PROJECTS -> viewAppliedProjects();
                    case FLAT_BOOKING -> flatBooking(); 
                    case WITHDRAW_BOOKING -> withdrawBooking();
                    case SUBMIT_ENQUIRY -> submitEnquiry();
                    case VIEW_ENQUIRY -> viewEnquiry();
                    case EDIT_ENQUIRY -> editEnquiry();
                    case DELETE_ENQUIRY -> deleteEnquiry();
                    case CHANGE_PASSWORD -> changePasswordUI.showChangePasswordMenu();
                    case LOGOUT -> {
                        UserManager.getInstance().logout();
                        running = false;
                        new LoginUI().startLogin();
                    }
                    default -> System.out.println("Invalid choice! Please enter a number between 0 and 10");
                }

            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private void displayMenuOptions() {
        String[] menuOptions = {
                "APPLICANT UI",
                "==================================",
                "1. View Open Projects",
                "2. Apply Project",
                "3. View Applied Projects",
                "4. Book flat through HDB Officer",
                "5. Flat Booking Withdrawal",
                "6. Submit Enquiry",
                "7. View Submitted Enquiry",
                "8. Edit Enquiry",
                "9. Delete Enquiry",
                "10. Change Password",
                "0. Logout",
                "==================================",
                "Enter your choice: "
        };

        for (String option : menuOptions) {
            System.out.println(option);
        }
    }

    // option 1
    protected void viewOpenProjects() {
        System.out.println("List of Open Projects: ");
        List<Project> projectList = ProjectController.getApplicantProjects(getApplicantUser());

        if (projectList.isEmpty()) {
            System.out.println("No visible project to view");
            return;
        }
        displayProjectList(projectList);
        try {
            int projIndex = getIntInput("Select the project to view details for: ") - 1;
            Project project = projectList.get(projIndex);

            if (project != null) {
                ProjectController.displayProjectDetails(project);
            }
        } catch (Exception e) {
            System.out.println("Error viewing project: " + e.getMessage());
        }
    }

    // option 2
    protected void applyProject() {
        try {
            Applicant applicant = getApplicantUser();
            boolean validForApply = ApplicantController.checkValidity(applicant);
            if (!validForApply) {
                System.out.println("You are not eligible to apply for a project at this time.");
                return;
            }

            List<Project> projectList = ProjectController.getApplicantProjects(applicant);

            if (projectList == null || projectList.isEmpty()) {
                System.out.println("Sorry. Currently no projects available for you to apply for.");
                return;
            }

            System.out.println("List of Projects you can apply for: ");
            System.out.println("=====================================");
            displayProjectList(projectList);

            int projIndex = getIntInput("Select the project you want to apply for (0 to cancel): ") - 1;

            if (projIndex == -1) {
                System.out.println("Application cancelled.");
                return;
            }

            if (projIndex < 0 || projIndex >= projectList.size()) {
                System.out.println("Invalid project selection.");
                return;
            }

            Project proj = projectList.get(projIndex);
            if (proj == null) {
                System.out.println("Error: Selected project is null.");
                return;
            }

            ProjectController.addApplicants(proj, applicant);
            System.out.println("Application successful!");
        } catch (Exception e) {
            System.out.println("Error applying for project: " + e.getMessage());
        }
    }

    //option 3
    protected void viewAppliedProjects() {
        List<Project> projectList = ApplicantController.getAppliedProject();

        for (Project p : projectList) {
            try {
                ProjectStatus status = p.getApplicantStatus(getApplicantUser());
                System.out.println("Project: " + p.getName() + " - Status: " + status);
            } catch (Exception e) {
                System.out.println("Error retrieving status for project: " + p.getName() + ". " + e.getMessage());
            }
        }
    }

    //option 4
    protected void flatBooking() {

        Map<Project, ProjectStatus> activeProject = ProjectController.getApplicantActiveProject(getApplicantUser());
        if (activeProject == null) {
            System.out.println("No project was applied for!");
            return;
        }

        for (Map.Entry<Project, ProjectStatus> entry : activeProject.entrySet()) {
            Project p = entry.getKey();
            ProjectStatus status = entry.getValue();

            switch (status) {
                case SUCCESSFUL -> {
                    System.out.println("Request to book flat in " + p.getName() + "? (1. Yes 2. No)");
                    int choice = getValidIntInput(1, 2);
                    if (choice == 1) {
                        System.out.println("Booking request forwarded.");
                        ProjectController.updateApplicantStatus(p, getApplicantUser(), ProjectStatus.REQUEST_BOOK);
                    } else {
                        System.out.println("Booking not confirmed.");
                    }
                }
                case BOOKED -> System.out.println("Flat already booked for " + p.getName());
                case PENDING -> System.out.println("Current application is still pending approval.");
                case REQUEST_BOOK -> System.out.println("Current booking for " + p.getName() + " already requested for.");
                case REQUEST_WITHDRAW -> System.out.println("Current withdrawal request for " + p.getName() + " is in progress.");
            }
        }
    }

    //option 5
    protected void withdrawBooking() {
        Map<Project, ProjectStatus> activeProject = ProjectController.getApplicantActiveProject(getApplicantUser());
        if (activeProject == null) {
            System.out.println("No booking was applied for!");
            return;
        }

        for (Map.Entry<Project, ProjectStatus> entry : activeProject.entrySet()) {
            Project p = entry.getKey();
            ProjectStatus status = entry.getValue();

            switch (status) {
                case BOOKED,REQUEST_BOOK -> {
                    System.out.println("Withdrawal from flat in " + p.getName() + "? (1. Yes 2. No)");
                    int choice = getValidIntInput(1, 2);
                    if (choice == 1) {
                        System.out.println("Withdrawal request forwarded.");
                        ProjectController.updateApplicantStatus(p, getApplicantUser(), ProjectStatus.UNSUCCESSFUL);
                    } else {
                        System.out.println("Withdrawal not confirmed.");
                    }
                }
                case SUCCESSFUL -> System.out.println("You have yet to book a flat in Project " + p.getName());
                case PENDING -> System.out.println("Current BTO application is still pending approval. No withdrawal allowed.");
                case REQUEST_WITHDRAW -> System.out.println("Current withdrawal request " + p.getName() + " is already in progress.");
            }
        }
    }

    // option 6
    protected void submitEnquiry() {
        try {
            System.out.println("List of visible projects:");
            List<Project> projectList = ProjectController.getProjectList();

            if (projectList == null || projectList.isEmpty()) {
                System.out.println("No projects available to submit enquiries for.");
                return;
            }

            displayProjectList(projectList);

            int projIndex = getIntInput("Select the project to submit enquiry for (0 to cancel): ") - 1;

            if (projIndex == -1) {
                System.out.println("Enquiry submission cancelled.");
                return;
            }

            if (projIndex < 0 || projIndex >= projectList.size()) {
                System.out.println("Invalid project selection.");
                return;
            }

            Project proj = projectList.get(projIndex);

            System.out.print("Enter enquiry message: ");
            String message = scanner.nextLine();

            if (message == null || message.trim().isEmpty()) {
                System.out.println("Enquiry message cannot be empty.");
                return;
            }

            ApplicantController.submitEnquiry(message, proj);
        } catch (Exception e) {
            System.out.println("Error submitting enquiry: " + e.getMessage());
        }
    }

    // option 7
    protected void viewEnquiry() {
        try {
            List<Enquiry> enquiryList = ApplicantController.getEnquiries();

            if (enquiryList == null || enquiryList.isEmpty()) {
                System.out.println("You haven't submitted any enquiries yet.");
                return;
            }

            System.out.println("Your Enquiries:");
            int cnt = 0;
            for (Enquiry e : enquiryList) {
                if (e != null) {
                    cnt += 1;
                    System.out.print(cnt + ". ");
                    e.viewEnquiry(currentUser.getUserRole());
                }
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error viewing enquiries: " + e.getMessage());
        }
    }

    // option 8
    protected void editEnquiry() {
        try {
            List<Enquiry> enquiryList = ApplicantController.getEnquiries();

            if (enquiryList == null || enquiryList.isEmpty()) {
                System.out.println("You don't have any enquiries to edit.");
                return;
            }

            System.out.println("Your Enquiries:");
            int cnt = 0;
            for (Enquiry e : enquiryList) {
                if (e != null) {
                    cnt += 1;
                    System.out.print(cnt + ". ");
                    e.viewEnquiry(currentUser.getUserRole());
                }
            }
            System.out.println();

            int enquiryIndex = getIntInput("Select the enquiry to edit (0 to cancel): ") - 1;

            if (enquiryIndex == -1) {
                System.out.println("Edit operation cancelled.");
                return;
            }

            if (enquiryIndex < 0 || enquiryIndex >= enquiryList.size()) {
                System.out.println("Invalid enquiry selection.");
                return;
            }

            Enquiry enquiry = enquiryList.get(enquiryIndex);

            System.out.print("Enter new enquiry message: ");
            String newMessage = scanner.nextLine();

            if (newMessage == null || newMessage.trim().isEmpty()) {
                System.out.println("New message cannot be empty.");
                return;
            }

            ApplicantController.modifyEnquiry(enquiry, newMessage);
        } catch (Exception e) {
            System.out.println("Error editing enquiry: " + e.getMessage());
        }
    }

    // option 9
    protected void deleteEnquiry() {
        try {
            List<Enquiry> enquiryList = ApplicantController.getEnquiries();

            if (enquiryList == null || enquiryList.isEmpty()) {
                System.out.println("You don't have any enquiries to delete.");
                return;
            }

            System.out.println("Your Enquiries:");
            int cnt = 0;
            for (Enquiry e : enquiryList) {
                if (e != null) {
                    cnt += 1;
                    System.out.print(cnt + ". ");
                    e.viewEnquiry(currentUser.getUserRole());
                }
            }
            System.out.println();

            int enquiryIndex = getIntInput("Select the enquiry to delete (0 to cancel): ") - 1;

            if (enquiryIndex == -1) {
                System.out.println("Delete operation cancelled.");
                return;
            }

            if (enquiryIndex < 0 || enquiryIndex >= enquiryList.size()) {
                System.out.println("Invalid enquiry selection.");
                return;
            }

            Enquiry enquiry = enquiryList.get(enquiryIndex);

            ApplicantController.deleteEnquiry(enquiry);
        } catch (Exception e) {
            System.out.println("Error deleting enquiry: " + e.getMessage());
        }
    }

    // Helper method to display a list of projects
    protected void displayProjectList(List<Project> projectList) {
        if (projectList == null || projectList.isEmpty()) {
            System.out.println("No projects to display.");
            return;
        }

        int cnt = 1;
        for (Project p : projectList) {
            if (p != null) {
                System.out.print(cnt + ". ");
                System.out.println(p.getName());
                cnt += 1;
            }
        }
    }
}