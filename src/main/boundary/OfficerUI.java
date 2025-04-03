package main.boundary;

import main.controller.project.ProjectController;
import main.controller.enquiry.EnquiryController;
import main.controller.user.UserManager;
import main.entity.Enquiry;
import main.entity.project.Project;
import main.entity.user.HDBOfficer;
import main.entity.user.User;
import main.enums.UserRole;

import java.util.List;
import java.util.Scanner;

public class OfficerUI {
    private static final Scanner scanner = new Scanner(System.in);
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
        while (true) {
            displayMenuOptions();
            try {
                int choice = getValidIntInput(0, 3);
                switch (choice) {
                    case 1 -> viewProjects();
                    case 2 -> viewAndReplyToEnquiries();
                    case 3 -> changePasswordUI.showChangePasswordMenu();
                    case 0 -> new LoginUI().navigateToLoginMenu();
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
        System.out.println("3. Change Password");
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

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private int getValidIntInput(int min, int max) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.printf("Please enter a number between %d and %d%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }
}
