package main.boundary;

import main.controller.user.ApplicantController;
import main.controller.user.UserManager;
import main.controller.project.ProjectController;
import main.entity.Enquiry;
import main.entity.project.Project;
import main.entity.user.Applicant;
import main.entity.user.User;
import main.enums.UserRole;

import java.util.Scanner;
import java.util.List;

public class ApplicantUI {
    private static final Scanner scanner = new Scanner(System.in);
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
        while (true) {
            displayMenuOptions();
            
            try {
                int choice = getValidIntInput();

                switch (choice) {
                    case 5 -> submitEnquiry();
                    case 6 -> viewEnquiry();
                    case 7 -> editEnquiry();
                    case 8 -> deleteEnquiry();
                    case 9 -> changePasswordUI.showChangePasswordMenu();
                    case 10 -> new LoginUI().navigateToLoginMenu();
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
        List<Project> projectList = ProjectController.getProjectList(currentUser);
        int cnt = 1;
        for (Project p : projectList) {
            System.out.print(cnt + ". ");
            System.out.println(p.getName());
            cnt += 1;
        }
        System.out.println("Select the project to submit enquiry for: ");

        int projIndex = getValidIntInput() - 1;
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
        System.out.println("Select the enquiry to edit: ");
        int enquiryIndex = getValidIntInput() - 1;
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
        System.out.println("Select the enquiry to delete: ");
        int enquiryIndex = getValidIntInput() - 1;
        Enquiry enquiry = enquiryList.get(enquiryIndex);

        ApplicantController.deleteEnquiry(enquiry);
        System.out.println("Enquiry deleted.");
    }

    private int getValidIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.next(); // Clear invalid input
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return input;
    }
}
