package main.boundary;

import main.controller.enquiry.EnquiryController;
import main.controller.project.ProjectController;
import main.controller.user.OfficerController;
import main.controller.user.UserManager;
import main.entity.Enquiry;
import main.entity.Registration;
import main.entity.project.Project;
import main.entity.user.Applicant;
import main.entity.user.HDBOfficer;
import main.entity.user.User;
import main.enums.ProjectStatus;
import main.enums.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OfficerUI extends ApplicantUI {
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

    @Override
    protected boolean isValidUser(User user) {
        return user != null && user.getUserRole() == UserRole.HDB_OFFICER;
    }

    protected HDBOfficer getOfficerUser() {
        if (currentUser instanceof HDBOfficer) {
            return (HDBOfficer) currentUser;
        }
        throw new IllegalStateException("Current user is not an HDB Officer");
    }

    @Override
    public void showMenu() {
        boolean running = true;
        while (running) {
            displayMenuOptions();
            try {
                int choice = getValidIntInput(0, 12);
                switch (choice) {
                    case VIEW_PROJECTS -> viewProjects(); //option 1
                    case VIEW_AND_REPLY_ENQUIRIES -> viewAndReplyToEnquiries(); //option 2
                    case REGISTER_JOIN_PROJECT -> registerJoinProject(); //option 3
                    case VIEW_REGISTRATION_STATUS -> viewReigstrationStatus(); //option 4
                    case APPROVE_BOOKING -> approveOrRejectBooking(); //option 5
                    case GENERATE_RECEIPTS -> {System.out.println("Option 6 not implemented yet");} // option 6
                    case APPLY_PROJECT -> {System.out.println("Option 7 not implemented yet");} //option 7
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
        HDBOfficer officer = getOfficerUser();
        List<Project> projectList = ProjectController.getOfficerProjects(officer);
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
        HDBOfficer officer = getOfficerUser();
        List<Enquiry> enquiries = EnquiryController.getEnquiriesList(officer);
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
        HDBOfficer officer = getOfficerUser();
        List<Project> projectList = ProjectController.getProjectList();
        
        boolean exit = false;
        while (!exit) {
            // print all projects
            System.out.println("List of Projects:");
            displayProjectList(projectList);
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
                selectedProject.getPendingOfficers().add(officer);
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
    protected void approveOrRejectBooking() {
        /*
        the logic here is wrong and should be corrected. i dont think
        PENDING_BOOK is needed at all, since officer might just
        need to enter the NRIC to change the status accordingly.
        please remove as needed.
         */
        List<Project> projectList = ProjectController.getOfficerProjects(getOfficerUser());
        displayProjectList(projectList);

        int projIndex = getIntInput("Select the project to view (0 to cancel): ") - 1;
        if (projIndex < 0 || projIndex >= projectList.size()) {
            System.out.println("Invalid selection or cancelled. Returning to menu.");
            return;
        }

        Project selectedProject = projectList.get(projIndex);

        // Filter applicants with PENDING_BOOK status
        List<Applicant> pendingApplicants = new ArrayList<>();
        for (Applicant applicant : selectedProject.getApplicants()) {
            if (selectedProject.getApplicantStatus(applicant) == ProjectStatus.REQUEST_BOOK) {
                pendingApplicants.add(applicant);
            }
        }

        if (pendingApplicants.isEmpty()) {
            System.out.println("No applicants with PENDING_BOOK status for this project.");
            return;
        }

        // Display only pending applicants
        for (int i = 0; i < pendingApplicants.size(); i++) {
            Applicant applicant = pendingApplicants.get(i);
            System.out.println((i + 1) + ". " + applicant.getName() + " - " + selectedProject.getApplicantStatus(applicant));
        }

        int appIndex = getIntInput("Select the booking to approve/reject (0 to cancel): ") - 1;
        if (appIndex < 0 || appIndex >= pendingApplicants.size()) {
            System.out.println("Invalid selection or cancelled. Returning to menu.");
            return;
        }

        Applicant selectedApplicant = pendingApplicants.get(appIndex);
        ProjectStatus currentStatus = selectedProject.getApplicantStatus(selectedApplicant);
        System.out.println("Current status for " + selectedApplicant.getName() + ": " + currentStatus);

        String newStatusStr = getStringInput("Enter new status for the applicant (approve/reject): ").toLowerCase();
        ProjectStatus newStatus = null;

        if ("approve".equals(newStatusStr)) {
            newStatus = ProjectStatus.BOOKED;
        } else if ("reject".equals(newStatusStr)) {
            return;
        } else {
            System.out.println("Invalid status. Returning to menu.");
            return;
        }

        ProjectController.updateApplicantStatus(selectedProject, selectedApplicant, newStatus);
    }


    private void displayApplicantListWithStatus(Project project) {
        System.out.println("Applications:");

        int index = 1;
        for (Map.Entry<Applicant, ProjectStatus> entry : project.getApplicantswithStatus().entrySet()) {
            Applicant applicant = entry.getKey();
            ProjectStatus status = entry.getValue();

            System.out.println(index + ". " + applicant.getName() + " (" + status + ")");
            index++;
        }

        System.out.println("0. Return to main menu");
    }

    // Override parent methods that use getApplicantUser to prevent ClassCastException
    @Override
    protected void viewOpenProjects() {
        System.out.println("This functionality is not available for HDB Officers in that capacity.");
        System.out.println("Please use Option 1 to view projects you're handling.");
    }
}