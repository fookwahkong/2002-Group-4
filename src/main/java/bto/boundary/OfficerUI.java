package bto.boundary;

import bto.controller.enquiry.EnquiryController;
import bto.controller.password.IUserController;
import bto.controller.password.PasswordController;
import bto.controller.project.ProjectController;
import bto.controller.user.OfficerController;
import bto.controller.user.UserManager;
import bto.entity.Enquiry;
import bto.entity.Registration;
import bto.entity.project.Project;
import bto.entity.user.Applicant;
import bto.entity.user.HDBOfficer;
import bto.entity.user.User;
import bto.enums.ProjectStatus;
import bto.enums.RegistrationStatus;
import bto.enums.UserRole;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A class representing an officer UI.
 */
public class OfficerUI extends UserUI {

    IUserController controller;
    private ChangePasswordUI changePasswordUI;
    private final HDBOfficer currentUser;
    private final ApplicantUI applicantUI;

    private static final int VIEW_PROJECTS = 1;
    private static final int VIEW_AND_REPLY_ENQUIRIES = 2;
    private static final int REGISTER_JOIN_PROJECT = 3;
    private static final int VIEW_REGISTRATION_STATUS = 4;
    private static final int APPROVE_BOOKING = 5;
    private static final int GENERATE_RECEIPTS = 6;

    // Applicant Functions
    private static final int VIEW_OPEN_PROJECTS = 7;
    private static final int APPLY_PROJECT = 8;
    private static final int VIEW_APPLIED_PROJECTS = 9;
    private static final int FLAT_BOOKING = 10;
    private static final int WITHDRAW_BOOKING = 11;
    private static final int SUBMIT_ENQUIRY = 12;
    private static final int VIEW_ENQUIRY = 13;
    private static final int EDIT_ENQUIRY = 14;
    private static final int DELETE_ENQUIRY = 15;

    private static final int CHANGE_PASSWORD = 16;

    /**
     * Constructor for the OfficerUI class.
     * 
     * @param user the user to be displayed in the UI
     */ 
    public OfficerUI(User user) {
        super(user);

        if (user != null && user.getUserRole() == UserRole.HDB_OFFICER) {
            this.currentUser = (HDBOfficer) user;
        } else {
            throw new IllegalStateException("Current user is not an officer");
        }

        this.applicantUI = new ApplicantUI((Applicant) user) {
            
            // override projectList for view and apply as Applicant
            @Override
            protected List<Project> getApplyProjectList() {
                return ProjectController.getProjectList().stream()
                        .filter(project ->
                // check if officer is not in assigned officers list
                !project.getAssignedOfficers().contains(getOfficerUser())
                        &&
                // check if officer registered for the project
                        project.getRegistrationList().stream()
                                .noneMatch(registration -> registration.getOfficer().equals(getOfficerUser())))
                        .toList();
            }
        };

        controller = new PasswordController();
        this.changePasswordUI = new ChangePasswordUI(controller);
    }

    /**
     * Get the officer user.
     * 
     * @return the officer user
     */
    protected HDBOfficer getOfficerUser() {
        if (currentUser instanceof HDBOfficer) {
            return (HDBOfficer) currentUser;
        }
        throw new IllegalStateException("Current user is not an HDB Officer");
    }

    /**
     * Process the input.
     * 
     * @param choice the choice to be processed
     */
    @Override
    public void processInput(int choice) {

        switch (choice) {
            case VIEW_PROJECTS -> viewProjects(); // option 1
            case VIEW_AND_REPLY_ENQUIRIES -> viewAndReplyToEnquiries(); // option 2
            case REGISTER_JOIN_PROJECT -> registerJoinProject(); // option 3
            case VIEW_REGISTRATION_STATUS -> viewReigstrationStatus(); // option 4
            case APPROVE_BOOKING -> approveOrRejectBooking(); // option 5
            case GENERATE_RECEIPTS -> generateReceipts(); // option 6

            // Applicant functions
            case VIEW_OPEN_PROJECTS -> viewOpenProjects(); // option 7 (projectList override)
            // case VIEW_OPEN_PROJECTS -> super.viewOpenProjects(); // option 7 use
            // applicant one directly (remove the two additional print lines)
            case APPLY_PROJECT -> applicantUI.applyProject(); // option 8 (projectList override)
            case VIEW_APPLIED_PROJECTS -> applicantUI.viewAppliedProjects(); // option 9
            case FLAT_BOOKING -> applicantUI.flatBooking(); // option 10
            case WITHDRAW_BOOKING -> applicantUI.withdrawBooking(); // option 11
            case SUBMIT_ENQUIRY -> applicantUI.submitEnquiry(); // option 12
            case VIEW_ENQUIRY -> applicantUI.viewEnquiry(); // option 13
            case EDIT_ENQUIRY -> applicantUI.editEnquiry(); // option 14
            case DELETE_ENQUIRY -> applicantUI.deleteEnquiry(); // option 15
            case CHANGE_PASSWORD -> changePasswordUI.displayChangePasswordMenu(); // option 16
        }
    }

    /**
     * Get the maximum menu option.
     * 
     * @return the maximum menu option
     */ 
    @Override
    public int getMaxMenuOption() {
        return 16;
    }

    /**
     * Display the menu options.
     */
    @Override
    public void displayMenuOptions() {
        String[] menuOptions = {
                "OFFICER UI",
                "==================================",
                "1. View projects I'm handling",
                "2. View and reply to enquiries",
                "3. Register to join a Project",
                "4. View Registration Status",
                "5. Approve / Reject Applicants' flat booking",
                "6. Generate receipts",
                "=================================",
                "OFFICER UI (APPLICANT)",
                "=================================",
                "7. View Open Projects",
                "8. Apply Project",
                "9. View Applied Projects",
                "10. Book flat through HDB Officer",
                "11. Flat Booking Withdrawal",
                "12. Submit Enquiry",
                "13. View Submitted Enquiry",
                "14. Edit Enquiry",
                "15. Delete Enquiry",
                "==================================",
                "16. Change Password",
                "0. Logout",
                "=================================",
                "Enter your choice: " };
                
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
     * View the projects.
     */
    protected void viewProjects() {
        HDBOfficer officer = getOfficerUser();
        List<Project> projectList = ProjectController.getOfficerProjects(officer);
        if (projectList.isEmpty()) {
            System.out.println("You are not assigned to any projects.");
            return;
        }

        System.out.println("Projects you are handling:");
        int cnt = 1;
        for (Project project : projectList) {
            System.out.println(cnt + ". " + project.getName());
            cnt += 1;
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

    /**
     * View and reply to enquiries.
     */
    protected void viewAndReplyToEnquiries() {
        HDBOfficer officer = getOfficerUser();
        List<Enquiry> enquiries = EnquiryController.getEnquiriesByOfficer(officer);
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

    /**
     * Single project checker (Officer cannot registere for project within same opening and closing of projects registered / registereing)
     * 
     * @param project the project to check
     * @return true if the project is valid, false otherwise
     */
    private boolean singleProjectCheck(Project project) {
        List<Project> registeringProject = OfficerController.getRegistrationList().stream()
            // only include non-rejected registrations
            .filter(registration -> registration.getRegistrationStatus() != RegistrationStatus.REJECTED)
            .map(Registration::getProject)
            .toList();

        List<Project> projectList = ProjectController.getOfficerProjects(getOfficerUser());
        List<Project> combined = Stream.concat(registeringProject.stream(), projectList.stream())
            .toList();

        for (Project p: combined) {
            if (!(p.getClosingDate().isBefore(project.getOpeningDate()) || p.getOpeningDate().isAfter(project.getClosingDate()))) { // if closing < opening or opening > closing
                return false;
            }  
        }
        return true;
    }

    /**
     * Register to join a project.
     */
    private void registerJoinProject() {
        HDBOfficer officer = getOfficerUser();
        List<Project> projectList = ProjectController.canRegisterProjects(officer);

        if (projectList == null || projectList.isEmpty()) {
            System.out.println("There is no available project to join now.");
            return;
        }

        System.out.println("Projects that you can register for");
        System.out.println("========================================");
        int cnt = 1;

        for (Project p : projectList) {
            System.out.println(cnt + ". " + p.getName());
            cnt++;
        }

        int projIndex = getIntInput("Select the project that you want to be part of (0 to cancel): ") - 1;

        if (projIndex == -1) {
            System.out.println("Enquiry submission cancelled.");
            return;
        }

        if (projIndex < 0 || projIndex >= projectList.size()) {
            System.out.println("Invalid project selection.");
            return;
        }

        Project selectedProject = projectList.get(projIndex);
        ProjectController.displayProjectDetails(selectedProject);

        System.out.println("Do you want to join this Project?");
        System.out.println("1. Yes");
        System.out.println("2. No");

        int decisionJoinProject = getValidIntInput(1, 2);

        if (decisionJoinProject == 1) {
            if (selectedProject.getRemainingSlots() > 0) {

                if (singleProjectCheck(selectedProject)) {
                    OfficerController.submitRegistration(selectedProject);
                    System.out.println("Registration Submitted.");
                } else {
                    System.out.println("You are only allow to register for one Project within the same Opening and Closing Date.");
                }

            } else {
                System.out.println("Unable to register due to insufficent Officer Slots remaining.");
            }
            
        }
        System.out.println();
    }

    /**
     * View the registration status.
     */
    private void viewReigstrationStatus() {
        // get registration List
        List<Registration> registrationList = OfficerController.getRegistrationList();

        System.out.println("Registrations:");
        for (Registration r : registrationList) {
            System.out.println("Project: " + r.getProject().getName());
            System.out.println("Status: " + r.getRegistrationStatus() + "\n");
        }
    }

    /**
     * Approve or reject a booking.
     */
    protected void approveOrRejectBooking() {
        List<Project> projectList = ProjectController.getOfficerProjects(currentUser);
        if (projectList.isEmpty()) {
            System.out.println("You are not handling any Projects.");
            return;
        }
        Map<Applicant, ProjectStatus> applicationMap = new HashMap<>();
        for (Project p: projectList) {
            applicationMap.putAll(p.getApplicantswithStatus());
        }
        for (Map.Entry<Applicant, ProjectStatus> entry : applicationMap.entrySet()) {
            System.out.println("Applicant: " + entry.getKey().getName() + " (" + entry.getKey().getUserID() + ")" + ", Status: " + entry.getValue());
        }

        String userId = getStringInput("Type the NRIC of the applicant that you want to approve booking for: ");
        User user = UserManager.getInstance().findUserByID(userId);

        // Check if user exists and is an Applicant
        if (user == null) {
            System.out.println("User not found with the given NRIC.");
            return;
        }

        if (!(user instanceof Applicant)) {
            System.out.println(
                    "The user with ID " + userId + " is not an Applicant. Found " + user.getUserRole() + " instead.");
            return;
        }

        Applicant applicant = (Applicant) user;

        Map<Project, ProjectStatus> applicantActiveProject = ProjectController.getApplicantActiveProject(applicant);
        List<Project> officerProjects = ProjectController.getOfficerProjects(getOfficerUser());

        // Check if the applicant has any active project
        if (applicantActiveProject == null || applicantActiveProject.isEmpty()) {
            System.out.println("This applicant does not have an active booking or application for any project.");
            return;
        }

        // Get the project and status from the map (there should be only one entry)
        Project applicantProject = applicantActiveProject.keySet().iterator().next();
        ProjectStatus currentStatus = applicantActiveProject.get(applicantProject);

        // Check if the officer is assigned to the applicant's project
        if (!officerProjects.contains(applicantProject)) {
            System.out.println("You are not authorized to approve/reject bookings for this project. " +
                    "The project is managed by another officer.");
            return;
        }

        // Check if the applicant's status is REQUEST_BOOK
        if (currentStatus != ProjectStatus.REQUEST_BOOK) {
            System.out.println("This applicant's status is '" + currentStatus +
                    "' and not pending for booking approval.");
            System.out.println(
                    "Only applicants with 'REQUEST_BOOK' status can have their bookings approved or rejected.");
            return;
        }

        // If all checks pass, proceed with approval/rejection
        String action = getStringInput("Do you want to approve or reject the booking? Enter approve or reject: ");

        if ("approve".equalsIgnoreCase(action)) {
            OfficerController.updateBookingStatus(applicantProject, applicant, ProjectStatus.BOOKED);
            System.out.println("Booking has been approved for " + applicant.getName() + ".");

        } else if ("reject".equalsIgnoreCase(action)) {
            ProjectController.updateApplicantStatus(applicantProject, applicant, ProjectStatus.SUCCESSFUL);
            System.out.println("Booking has been rejected for " + applicant.getName() +
                    ". Applicant status reverted to SUCCESSFUL.");

        } else {
            System.out.println("Invalid action. Please enter 'approve' or 'reject'.");
        }
    }

    /**
     * Generate receipts.
     */
    protected void generateReceipts() {
        // Get all users from the UserManager
        List<User> allUsers = UserManager.getUsers();
        List<Applicant> applicantsWithBookings = new ArrayList<>();

        // Filter for applicants with booking details
        for (User user : allUsers) {
            if (user instanceof Applicant) {
                Applicant applicant = (Applicant) user;
                Map<Project, String> bookingDetails = applicant.getBookingDetails();

                // Only include applicants with bookings
                if (bookingDetails != null && !bookingDetails.isEmpty()) {
                    applicantsWithBookings.add(applicant);
                }
            }
        }

        if (applicantsWithBookings.isEmpty()) {
            System.out.println("No applicants with bookings found.");
            return;
        }

        // Display list of applicants with bookings
        System.out.println("\n=== APPLICANTS WITH BOOKINGS ===");
        for (int i = 0; i < applicantsWithBookings.size(); i++) {
            Applicant applicant = applicantsWithBookings.get(i);
            System.out.printf("%d. %s (ID: %s)%n",
                    i + 1,
                    applicant.getName(),
                    applicant.getUserID());
        }
        System.out.println("0. Return to previous menu");

        int selection = getIntInput("Select an applicant to generate receipt (0 to cancel): ") - 1;

        // Return to previous menu if user selects 0
        if (selection == -1) {
            return;
        }

        // Get selected applicant
        Applicant selectedApplicant = applicantsWithBookings.get(selection);
        Map<Project, String> bookingDetails = selectedApplicant.getBookingDetails();

        Project project = bookingDetails.keySet().iterator().next();
        ;

        // Generate receipts
        if (project != null) {
            // Generate receipt for specific project
            displayReceiptForApplicantAndProject(selectedApplicant, project,
                    bookingDetails.get(project));
        }
    }

    /**
     * Display a receipt for a specific applicant.
     * 
     * @param applicant the applicant
     * @param project the project
     * @param housingType the housing type
     */
    private void displayReceiptForApplicantAndProject(
            Applicant applicant, Project project, String housingType) {

        System.out.println("\n====================================================");
        System.out.println("                BOOKING RECEIPT                     ");
        System.out.println("====================================================");
        System.out.println();

        // Applicant details
        System.out.println("APPLICANT DETAILS:");
        System.out.println("------------------");
        System.out.printf("Name: %s%n", applicant.getName());
        System.out.printf("NRIC/ID: %s%n", applicant.getUserID());
        System.out.printf("Age: %d%n", applicant.getAge());
        System.out.printf("Marital Status: %s%n", applicant.getMaritalStatus().toString());
        System.out.println();

        // Project details
        System.out.println("PROJECT DETAILS:");
        System.out.println("---------------");
        System.out.printf("Project Name: %s%n", project.getName());
        System.out.printf("Neighborhood: %s%n", project.getNeighbourhood());
        System.out.printf("Application Period: %s to %s%n",
                project.getOpeningDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                project.getClosingDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        System.out.println();

        // Booking details
        System.out.println("BOOKING DETAILS:");
        System.out.println("---------------");
        System.out.printf("Housing Type: %s%n", housingType);

        // Get housing type specific details
        if (project.getHousingType(housingType) != null) {
            System.out.printf("Price: $%.2f%n", project.getHousingType(housingType).getSellingPrice());
        }

        // Add timestamp
        System.out.println();
        System.out.println("----------------------------------------------------");
        System.out.printf("Receipt generated on: %s%n",
                java.time.LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss")));
        System.out.println("====================================================");
    }


    // Override parent methods that use getApplicantUser to prevent
    // ClassCastException
    /**
     * View the open projects.
     */
    protected void viewOpenProjects() {
        System.out.println(
                "This functionality is only for viewing Projects open for Application as Applicant only..");
        System.out.println("Please use Option 1 to view projects you're handling.");

        applicantUI.viewOpenProjects();
    }
}