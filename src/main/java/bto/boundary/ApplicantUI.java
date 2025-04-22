package bto.boundary;

import bto.controller.password.IUserController;
import bto.controller.password.PasswordController;
import bto.controller.project.ProjectController;
import bto.controller.user.ApplicantController;
import bto.entity.Enquiry;
import bto.entity.project.Project;
import bto.entity.user.Applicant;
import bto.entity.user.User;
import bto.enums.ProjectStatus;
import bto.enums.UserRole; 

import java.util.List;
import java.util.Map;

/**
 * A class representing an applicant UI.
 */
public class ApplicantUI extends UserUI {

    IUserController controller = new PasswordController();
    private final ChangePasswordUI changePasswordUI = new ChangePasswordUI(controller);
    private final Applicant currentUser;

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


    /**
     * Constructor for the ApplicantUI class.
     * Depending on UserRole, we cast it to Applicant
     * @param user the user to be displayed in the UI
     */
    public ApplicantUI(User user) {
        super(user);

        if (user.getUserRole() == UserRole.HDB_OFFICER) {
            this.currentUser = (Applicant) user;
        } else if(user.getUserRole() == UserRole.APPLICANT) {
            this.currentUser = (Applicant) user;
        } else {
            throw new IllegalStateException("Current user is not an Applicant");
        }
    }


    /**
     * Get the applicant user.
     * 
     * @return the applicant user
     */
    protected Applicant getApplicantUser() {
        if (currentUser != null) {
            return (Applicant) currentUser;
        }
        throw new IllegalStateException("Current user is not an Applicant");
    }

    /**
     * Get the maximum menu option.
     * 
     * @return the maximum menu option
     */ 
    @Override
    protected int getMaxMenuOption() {
        return 10;
    }

    /**
     * Process the input.
     * 
     * @param choice the choice to be processed
     */
    public void processInput(int choice) {

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
            case CHANGE_PASSWORD -> changePasswordUI.displayChangePasswordMenu();
        }
    }

    /**
     * Display the menu options.
     */     
    @Override
    public void displayMenuOptions() {
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
                "==================================",
                "10. Change Password",
                "0. Logout",
                "==================================",
                "Enter your choice: "
        };

        for (String option : menuOptions) {
            System.out.println(option);
        }
    }

    /**
     * Get the list of projects that the applicant can apply for.
     * 
     * @return the list of projects that the applicant can apply for
     */
    protected List<Project> getApplyProjectList() {
        return ProjectController.getApplicantProjects(getApplicantUser());
    }

    /**
     * View the open projects.
     */
    protected void viewOpenProjects() {
        System.out.println("List of Open Projects: ");
        List<Project> projectList = getApplyProjectList();

        if (projectList.isEmpty()) {
            System.out.println("No visible project to view based on your User Group.");
            return;
        }

        displayProjectList(projectList);

        try {
            int projIndex = getIntInput("Select the project to view details for: ") - 1;
            if (projIndex >= 0 && projIndex < projectList.size()) {
                Project project = projectList.get(projIndex);
                ProjectController.displayProjectDetails(project);
            } else {
                System.out.println("Invalid project selection.");
            }
        } catch (Exception e) {
            System.out.println("Error viewing project: " + e.getMessage());
        }
    }

    /**
     * Apply for a project.
     */
    protected void applyProject() {
        try {
            Applicant applicant = getApplicantUser();
            if (!ApplicantController.checkEligibility()) {
                System.out.println("You are not eligible to apply for a project at this time.");
                return;
            }

            List<Project> projectList = getApplyProjectList();
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
            ProjectController.addApplicant(proj, applicant);
            ProjectController.save();
            System.out.println("Application successful!");

        } catch (Exception e) {
            System.out.println("Error applying for project: " + e.getMessage());
        }
    }

    /**
     * View the applied projects.
     */
    protected void viewAppliedProjects() {
        List<Project> projectList = ApplicantController.getAppliedProjects();
        if (projectList.isEmpty()) {
            System.out.println("You haven't applied for any projects yet.");
            return;
        }

        for (Project p : projectList) {
            try {
                ProjectStatus status = p.getApplicantStatus(getApplicantUser());
                System.out.println("Project: " + p.getName() + " - Status: " + status);
            } catch (Exception e) {
                System.out.println("Error retrieving status for project: " + p.getName() + ". " + e.getMessage());
            }
        }
    }

    /**
     * Book a flat through an HDB Officer.
     */
    protected void flatBooking() {
        Map<Project, ProjectStatus> activeProject = ApplicantController.getActiveProjectStatus(getApplicantUser());
        if (activeProject == null || activeProject.isEmpty()) {
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
                        if (ApplicantController.requestFlatBooking(p, getApplicantUser())) {
                            System.out.println("Booking request forwarded.");
                        } else {
                            System.out.println("Booking request failed.");
                        }
                    } else {
                        System.out.println("Booking not confirmed.");
                    }
                }
                case BOOKED -> System.out.println("Flat already booked for " + p.getName());
                case PENDING -> System.out.println("Current application is still pending approval.");
                case REQUEST_BOOK ->
                    System.out.println("Current booking for " + p.getName() + " already requested for.");
                case REQUEST_WITHDRAW ->
                    System.out.println("Current withdrawal request for " + p.getName() + " is in progress.");
                case UNSUCCESSFUL ->
                    System.out.println("Currenty application has been successfully withdraw.\nPlease submit a new application before booking.");
            }
        }
    }

    /**
     * Withdraw a booking.
     */
    protected void withdrawBooking() {
        Map<Project, ProjectStatus> activeProject = ApplicantController.getActiveProjectStatus(getApplicantUser());
        if (activeProject == null || activeProject.isEmpty()) {
            System.out.println("No booking was applied for!");
            return;
        }

        for (Map.Entry<Project, ProjectStatus> entry : activeProject.entrySet()) {
            Project p = entry.getKey();
            ProjectStatus status = entry.getValue();

            switch (status) {
                case BOOKED, SUCCESSFUL -> {
                    System.out.println("Withdrawal from flat in " + p.getName() + "? (1. Yes 2. No)");
                    int choice = getValidIntInput(1, 2);
                    if (choice == 1) {
                        if (ApplicantController.requestBookingWithdrawal(p, getApplicantUser())) {
                            System.out.println("Withdrawal request forwarded.");
                        } else {
                            System.out.println("Withdrawal request failed.");
                        }
                    } else {
                        System.out.println("Withdrawal not confirmed.");
                    }
                }
                case REQUEST_BOOK -> System.out.println("Current Book application is still pending approval. No withdrawl allowed.");
                case PENDING ->
                    System.out.println("Current BTO application is still pending approval. No withdrawal allowed.");
                case REQUEST_WITHDRAW ->
                    System.out.println("Current withdrawal request " + p.getName() + " is already in progress.");
                case UNSUCCESSFUL ->
                    System.out.println("Booking has already been successfully withdrawn.");
            }
        }
    }

    /**
     * Submit an enquiry.
     */
    protected void submitEnquiry() {
        try {
            System.out.println("List of all projects:");
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
            System.out.println("Enquiry submitted successfully.");
        } catch (Exception e) {
            System.out.println("Error submitting enquiry: " + e.getMessage());
        }
    }

    /**
     * View an enquiry.
     */
    protected void viewEnquiry() {
        try {
            List<Enquiry> enquiryList = ApplicantController.getEnquiries();

            if (enquiryList.isEmpty()) {
                System.out.println("You haven't submitted any enquiries yet.");
                return;
            }

            System.out.println("Your Enquiries:");
            displayEnquiryList(enquiryList);
        } catch (Exception e) {
            System.out.println("Error viewing enquiries: " + e.getMessage());
        }
    }

    /**
     * Edit an enquiry.
     */
    protected void editEnquiry() {
        try {
            List<Enquiry> enquiryList = ApplicantController.getEnquiries();

            if (enquiryList.isEmpty()) {
                System.out.println("You don't have any enquiries to edit.");
                return;
            }

            System.out.println("Your Enquiries:");
            displayEnquiryList(enquiryList);

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
            if (enquiry.isReplied()) {
                System.out.println("Cannot edit a replied enquiry.");
                return;
            }

            System.out.print("Enter new enquiry message: ");
            String newMessage = scanner.nextLine();

            if (newMessage == null || newMessage.trim().isEmpty()) {
                System.out.println("New message cannot be empty.");
                return;
            }

            ApplicantController.modifyEnquiry(enquiry, newMessage);
            System.out.println("Enquiry updated successfully.");
        } catch (Exception e) {
            System.out.println("Error editing enquiry: " + e.getMessage());
        }
    }

    /**
     * Delete an enquiry.
     */
    protected void deleteEnquiry() {
        try {
            List<Enquiry> enquiryList = ApplicantController.getEnquiries();

            if (enquiryList.isEmpty()) {
                System.out.println("You don't have any enquiries to delete.");
                return;
            }

            System.out.println("Your Enquiries:");
            displayEnquiryList(enquiryList);

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
            System.out.println("Enquiry deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting enquiry: " + e.getMessage());
        }
    }

    /**
     * Display a list of projects.
     * 
     * @param projectList the list of projects to display
     */
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

    /**
     * Display a list of enquiries.
     * 
     * @param enquiryList the list of enquiries to display
     */
    private void displayEnquiryList(List<Enquiry> enquiryList) {
        int cnt = 0;
        for (Enquiry e : enquiryList) {
            if (e != null) {
                cnt += 1;
                System.out.print(cnt + ". ");
                e.viewEnquiry(currentUser.getUserRole());
            }
        }
        System.out.println();
    }
}