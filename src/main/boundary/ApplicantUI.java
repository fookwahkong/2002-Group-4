package main.boundary;

import main.controller.project.ProjectController;
import main.controller.user.ApplicantController;
import main.controller.user.UserManager;
import main.entity.Enquiry;
import main.entity.project.Project;
import main.entity.user.Applicant;
import main.entity.user.User;
import main.enums.UserRole;

import java.util.ArrayList;
import java.util.List;

public class ApplicantUI extends UI {
    private final Applicant currentUser;
    private ChangePasswordUI changePasswordUI = new ChangePasswordUI();

    public ApplicantUI() {
        User user = UserManager.getInstance().getCurrentUser();

        // downcasting user to applicant
        if (user != null && (user.getUserRole() == UserRole.APPLICANT || user.getUserRole() == UserRole.HDB_OFFICER)) {
            this.currentUser = (Applicant) user;
        } else {
            throw new IllegalStateException("Current user is not an Applicant");
        }
    }

    public void showMenu() {
        boolean running = true;
        while (running) {
            displayMenuOptions();

            try {
                int choice = getValidIntInput(1, 10);

                switch (choice) {
                    case 1 -> viewOpenProjects();
                    case 3 -> viewAppliedProjects();
                    case 5 -> submitEnquiry();
                    case 6 -> viewEnquiry();
                    case 7 -> editEnquiry();
                    case 8 -> deleteEnquiry();
                    case 9 -> changePasswordUI.showChangePasswordMenu();
                    case 10 -> {
                        UserManager.getInstance().logout();
                        running = false;
                        new LoginUI().startLogin();
                    }
                    default -> System.out.println("Invalid choice! Please enter a number between 1 and 5");
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
            "1. View the list of open Projects",
            "3. View the projects you applied for",
            "5. Submit Enquiry",
            "6. View Submitted Enquiry",
            "7. Edit Submitted Enquiry",
            "8. Delete Enquiry",
            "9. Change Password",
            "10. Logout",
            "==================================",
            "Enter your choice: "
        };

        for (String option : menuOptions) {
            System.out.println(option);
        }
    }

    //option 1
    protected void viewOpenProjects() {
        System.out.println("List of Open Projects: ");
        List<Project> projectList = ProjectController.getApplicantProjects(currentUser);

        if (!projectList.isEmpty()) {
            int cnt = 1;
            for (Project p : projectList) {
                System.out.print(cnt + ". ");
                System.out.println(p.getName());
                cnt += 1;
            }
    
            int projIndex = getIntInput("Select the project to view details for: ") - 1;
            Project proj = projectList.get(projIndex);
            System.out.print(proj.toString());
        } else {
            System.out.println("No visible projects to view!");
        }
    }

    //option 3
    protected void viewAppliedProjects() {

    }

    //option 5
    protected void submitEnquiry() {
        // go to ProjectController to get list of projects
        System.out.println("List of visible projects:");
        List<Project> projectList = ProjectController.getProjectList();
        int cnt = 1;
        for (Project p : projectList) {
            System.out.print(cnt + ". ");
            System.out.println(p.getName());
            cnt += 1;
        }

        int projIndex = getIntInput("Select the project to submit enquiry for: ") - 1;
        Project proj = projectList.get(projIndex);

        // take in input;
        System.out.print("Enter enquiry message: ");
        String message = scanner.nextLine();

        // selected project will be pass to ApplicantController
        ApplicantController.submitEnquiry(message, proj);
    }

    //option 6
    // protected List<Enquiry> viewEnquiry() {
    //     List<Enquiry> enquiryList = currentUser.getEnquiryList();
    //     System.out.println("Enquiries:");
    //     int cnt = 0;
    //     for (Enquiry e : enquiryList) {
    //         cnt += 1;
    //         System.out.print(cnt + ". ");
    //         e.viewEnquiry(currentUser.getUserRole());
    //     }
    //     System.out.println();
    //     return enquiryList;
    // }
    protected void viewEnquiry() {
        List<Enquiry> enquiryList = ApplicantController.getEnquiries();

        int cnt = 0;
        for (Enquiry e: enquiryList) {
            cnt += 1;
            System.out.print(cnt + ". ");
            e.viewEnquiry(currentUser.getUserRole());
        }
        System.out.println();
    }

    //option 7
    protected void editEnquiry() {
        List<Enquiry> enquiryList = ApplicantController.getEnquiries(); // get enquiry list
        this.viewEnquiry(); // print the enquiry list

        // get the enquiry to edit
        int enquiryIndex = getIntInput("Select the enquiry to edit: ") - 1;
        Enquiry enquiry = enquiryList.get(enquiryIndex);

        // take in input;
        System.out.print("Enter new enquiry message: ");
        String newMessage = scanner.nextLine();
        enquiry.setMessage(newMessage);
        System.out.println("Message edited.");
    }

    //option 8
    protected void deleteEnquiry() {
        List<Enquiry> enquiryList = ApplicantController.getEnquiries();
        this.viewEnquiry(); // print the enquiry list

        // get the qnquiry to delete
        int enquiryIndex = getIntInput("Select the enquiry to delete: ") - 1;
        Enquiry enquiry = enquiryList.get(enquiryIndex);

        ApplicantController.deleteEnquiry(enquiry);
        System.out.println("Enquiry deleted.");
    }

}
