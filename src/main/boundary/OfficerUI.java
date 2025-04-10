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

    private static final int VIEW_PROJECTS = 1;
    private static final int VIEW_AND_REPLY_ENQUIRIES = 2;
    private static final int REGISTER_JOIN_PROJECT = 3;
    private static final int VIEW_REGISTRATION_STATUS = 4;
    private static final int APPROVE_BOOKING = 5;
    private static final int GENERATE_RECEIPTS = 6;
    private static final int APPLY_PROJECT = 7;
    private static final int SUBMIT_ENQUIRY = 8;
    private static final int VIEW_ENQUIRY = 9;
    private static final int EDIT_ENQUIRY = 10;
    private static final int DELETE_ENQUIRY = 11;
    private static final int CHANGE_PASSWORD = 12;
    private static final int LOGOUT = 0;

    public OfficerUI() {
        User user = UserManager.getInstance().getCurrentUser();

        //downcasting from user to officer
        if (isValidUser(user)) {
            this.currentUser = (HDBOfficer) user;
        } else {
            throw new IllegalStateException("Current user is not an HDB Officer");
        }
    }

    @Override
    protected boolean isValidUser(User user) {
        return user != null && user.getUserRole() == UserRole.HDB_OFFICER;
    }

    @Override
    public void showMenu() {
        boolean running = true;
        while (running) {
            displayMenuOptions();
            try {
                int choice = getValidIntInput(0, 9);
                switch (choice) {
                    case VIEW_PROJECTS -> viewProjects(); //option 1
                    case VIEW_AND_REPLY_ENQUIRIES -> viewAndReplyToEnquiries(); //option 2
                    case REGISTER_JOIN_PROJECT -> registerJoinProject(); //option 3
                    case VIEW_REGISTRATION_STATUS -> viewReigstrationStatus(); //option 4
                    case APPROVE_BOOKING -> {System.out.println("Option 5 not implemented yet.");} //option 5
                    case GENERATE_RECEIPTS -> {System.out.println("Option 6 not implemented yet");} // option 6
                    case APPLY_PROJECT -> {System.out.println("Option 6 not implemented yet");} //option 7
                    case SUBMIT_ENQUIRY -> super.submitEnquiry();  //option 8
                    case VIEW_ENQUIRY -> super.viewEnquiry(); //option 9
                    case EDIT_ENQUIRY -> super.editEnquiry(); //option 10
                    case DELETE_ENQUIRY -> super.deleteEnquiry(); //option 11
                    case CHANGE_PASSWORD -> changePasswordUI.showChangePasswordMenu(); //option 12
                    case LOGOUT -> {  //option 0
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
        String[] menuOptions = {
            "OFFICER UI",
            "==================================",
            "1. View projects I'm handling",
            "2. View and reply to enquiries",
            "3. Register to join a Project",
            "4. View Registration Status",
            "5. (Reserved) Approve / Reject Applicants' flat booking",
            "6. (Reserved) Generate receipts",
            "=================================",
            "OFFICER AS AN APPLICANT",
            "7. (Reserved) Apply for Project",
            "8. Submit Enquiry",
            "9. View Submitted Enquiry",
            "10. Edit Enquiry",
            "11. Delete Enquiry",
            "==================================",
            "12. Change Password",
            "0. Logout",
            "=================================",
            "Enter your choice: "};
        for (String option : menuOptions) {
            System.out.println(option);
        }
    }


    // Option 1
    private void viewProjects() {
        List<Project> projectList = ProjectController.getOfficerProjects(currentUser);
        if (projectList.isEmpty()) {
            System.out.println("You are not assigned to any projects.");
            return;
        }

        System.out.println("Projects you are handling:");
        for (Project project : projectList) {
            System.out.println("- " + project.getName());
        }
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

    //Option 2
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

    // Option 3
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
                selectedProject.getPendingOfficers().add(currentUser);
                System.out.println();
                return;
            }
            System.out.println();
        }
    }

    // option 4
    private void viewReigstrationStatus() {
        // get registration List
        List<Registration> registrationList = OfficerController.getRegistrationList();

        System.out.println("Registrations:");
        for (Registration r: registrationList) {
            System.out.println("Project: " + r.getProject().getName());
            System.out.println("Status: " + r.getreRegistrationStatus() + "\n");
        }
    }

    //option 5
    @Override
    protected void applyProject() {

    }
}
