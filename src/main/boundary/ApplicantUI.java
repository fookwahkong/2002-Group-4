package main.boundary;

import main.controller.user.ApplicantController;
import main.controller.user.UserManager;
import main.controller.project.ProjectController;
import main.entity.project.Enquiry;
import main.entity.project.Project;
import main.entity.user.Applicant;
import main.entity.user.User;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class ApplicantUI {
    private static final Scanner scanner = new Scanner(System.in);
    private final Applicant currentUser = (Applicant) UserManager.getInstance().getCurrentUser();

    public void showMenu() {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("APPLICANT UI");
            System.out.println("==================================");
            System.out.println("5. Submit Enquiry");
            System.out.println("6. View Submitted Enquiry");
            System.out.println("7. Edit Submitted Enquiry");
            System.out.println("8. Delete Enquiry");
            System.out.println("9. Logout");
            System.out.println("==================================");
            System.out.println("Enter your choice: ");

            int choice = getValidIntInput();

            switch (choice) {
                case 5 -> submitEnquiry();
                case 6 -> viewEnquiry();
                case 7 -> editEnquiry();
                case 8 -> deleteEnquiry();
                case 9 -> {
                    System.out.println("Logging out.");
                    loggedIn = false;
                    break;
                }
                default -> System.out.println("Invalid choice! Please enter a number between 1 and 5");
            }
        }
    }

    public void submitEnquiry() {
        // go to ProjectController to get list of projects
        System.out.println("List of visible projects:");
        List<Project> projectList = ProjectController.getProjectList(currentUser);
        int cnt = 1;
        for (Project p: projectList) {
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

    private void viewEnquiry() {
        List<Enquiry> enquiryList = currentUser.getEnquiryList();
        for (Enquiry e: enquiryList) {
            e.viewEnquiry("applicant");
        }
        System.out.println();
    }

    private void editEnquiry() {
        List<Enquiry> enquiryList = currentUser.getEnquiryList();
        for (Enquiry e: enquiryList) {
            e.viewEnquiry("applicant");
        }
        System.out.println();
    }

    private void deleteEnquiry() {}
    
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
