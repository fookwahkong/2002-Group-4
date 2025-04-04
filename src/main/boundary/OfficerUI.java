package main.boundary;

import main.controller.enquiry.EnquiryController;
import main.controller.project.ProjectController;
import main.controller.user.OfficerController;
import main.controller.user.UserManager;
import main.entity.Enquiry;
import main.entity.Registration;
import main.entity.project.Project;
import main.entity.user.HDBOfficer;
import main.entity.user.User;
import main.enums.UserRole;

import java.util.List;

public class OfficerUI extends ApplicantUI {
    private final HDBOfficer currentUser;
    private ChangePasswordUI changePasswordUI = new ChangePasswordUI();

    public OfficerUI() {
        User user = UserManager.getInstance().getCurrentUser();

        //downcasting from user to officer
        if (user != null && user.getUserRole() == UserRole.HDB_OFFICER) {
            this.currentUser = (HDBOfficer) user;
        } else {
            throw new IllegalStateException("Current user is not an HDB Officer");
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
                    case 2 -> viewAndReplyToEnquiries();
                    case 3 -> registerJoinProject();
                    case 4 -> viewReigstrationStatus();
                    case 5 -> super.submitEnquiry();
                    case 6 -> super.viewEnquiry();
                    case 7 -> super.editEnquiry();
                    case 8 -> super.deleteEnquiry();
                    case 9 -> changePasswordUI.showChangePasswordMenu();
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
        System.out.println("\nOFFICER UI");
        System.out.println("==================================");
        System.out.println("1. View projects I'm handling");
        System.out.println("2. View and reply to enquiries");
        System.out.println("3. Register to join a Project");
        System.out.println("4. View Registration Status");
        System.out.println("5. Submit Enquiry");
        System.out.println("6. View Submitted Enquiry");
        System.out.println("7. Edit Submitted Enquiry");
        System.out.println("8. Delete Enquiry");
        System.out.println("9. Change Password");
        System.out.println("0. Logout");
        System.out.print("Enter your choice: ");
    }


    // View all projects that the officer is handling.

    private void viewProjects() {
        List<Project> projects = ProjectController.getOfficerProjects(currentUser);
        if (projects.isEmpty()) {
            System.out.println("You are not assigned to any projects.");
            return;
        }

        System.out.println("Projects you are handling:");
        for (Project project : projects) {
            System.out.println("- " + project.getName());
        }
    }

    //View and reply to enquiries that the officer is handling.

    private void viewAndReplyToEnquiries() {
        List<Enquiry> enquiries = EnquiryController.getEnquiriesList(currentUser);
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

    // Register to join project
    private void registerJoinProject() {
        List<Project> projectList = ProjectController.getProjectList();
        
        boolean exit = false;
        while (!exit) {
            // print all projects
            System.out.println("List of Projects:");
            for (int i=0; i<projectList.size(); i++) {
                System.out.println((i+1) + ": " + projectList.get(i).getName());
            }
            System.out.println("0: Exit\n");
            int projectIndex = getIntInput("Select the project to join: ");
            System.out.println();

            // check if exit
            if (projectIndex == 0) {
                exit = true;
                return;
            }

            // print detail of selected project
            Project selectedProject = projectList.get(projectIndex-1);
            System.out.println("Information for project: " + selectedProject.getName());
            System.out.println("Visiblity: " + selectedProject.getVisibility());
            System.out.println("Neighborhood: " + selectedProject.getNeighbourhood());
            System.out.println("Opening Date: " + selectedProject.getOpeningDate());
            System.out.println("Closing Date: " + selectedProject.getClosingDate());
            System.out.println("Manager: " + selectedProject.getManager().getName());
            System.out.println("Officer Slot Remaining: " + (selectedProject.getRemainingSlots()));

            System.out.println("Do you want to join this Project?");
            System.out.println("1. Yes");
            System.out.println("2. No");

            int decisionJoinProject = getValidIntInput(1,2);
            if (decisionJoinProject == 1) {
                OfficerController.submitRegistration(selectedProject);
            }
            System.out.println();
        }
    }

    // View Registration Status
    private void viewReigstrationStatus() {
        List<Registration> registrationList = currentUser.getRegistrationList();
        System.out.println("Registrations:");
        for (Registration r: registrationList) {
            System.out.println("Project: " + r.getProject().getName());
            System.out.println("Status: " + r.getreRegistrationStatus() + "\n");
        }
    }
}
