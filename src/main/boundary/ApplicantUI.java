package main.boundary;

import main.controller.user.ApplicantController;
import main.controller.user.UserManager;
import main.controller.project.ProjectController;
import main.entity.Enquiry;
import main.entity.project.Project;
import main.entity.user.Applicant;
import main.entity.user.User;
import main.enums.UserRole;

import java.util.List;

public class ApplicantUI extends UI{
    private final Applicant currentUser;
    private ChangePasswordUI changePasswordUI = new ChangePasswordUI();

    public ApplicantUI() {
        User user = UserManager.getInstance().getCurrentUser();

        // downcasting user to applicant
        if (user != null && user.getUserRole() == UserRole.APPLICANT) {
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
                int choice = getValidIntInput(5, 10);

                switch (choice) {
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
        System.out.println("APPLICANT UI");
        System.out.println("==================================");
        System.out.println("5. Submit Enquiry");
        System.out.println("6. View Submitted Enquiry");
        System.out.println("7. Edit Submitted Enquiry");
        System.out.println("8. Delete Enquiry");
        System.out.println("9. Change Password");
        System.out.println("10. Logout");
        System.out.println("==================================");
        System.out.println("Enter your choice: ");
    }

    public void submitEnquiry() {
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

    private List<Enquiry> viewEnquiry() {
        List<Enquiry> enquiryList = currentUser.getEnquiryList();
        System.out.println("Enquiries:");
        int cnt = 0;
        for (Enquiry e : enquiryList) {
            cnt += 1;
            System.out.print(cnt + ". ");
            e.viewEnquiry("applicant");
        }
        System.out.println();
        return enquiryList;
    }

    private void editEnquiry() {
        List<Enquiry> enquiryList = this.viewEnquiry(); // get enquiry list

        // get the enquiry to edit
        int enquiryIndex = getIntInput("Select the enquiry to edit: ") - 1;
        Enquiry enquiry = enquiryList.get(enquiryIndex);

        // take in input;
        System.out.print("Enter new enquiry message: ");
        String newMessage = scanner.nextLine();
        enquiry.setMessage(newMessage);
        System.out.println("Message edited.");
    }

    private void deleteEnquiry() {
        List<Enquiry> enquiryList = this.viewEnquiry();

        // get the qnquiry to delete
        int enquiryIndex = getIntInput("Select the enquiry to delete: ") - 1;
        Enquiry enquiry = enquiryList.get(enquiryIndex);

        ApplicantController.deleteEnquiry(enquiry);
        System.out.println("Enquiry deleted.");
    }

}
