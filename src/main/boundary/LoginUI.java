package main.boundary;

import main.controller.LoginController;
import main.entity.user.User;
import main.enums.UserRole;

import java.util.Scanner;

public class LoginUI {
    private static Scanner scanner = new Scanner(System.in);
    private LoginController loginController;
    private ChangePasswordUI changePasswordUI;

    public LoginUI() {
        loginController = LoginController.getInstance();
        changePasswordUI = new ChangePasswordUI();
    }

    public User showLoginMenu() {
        boolean exit = false;
        User loggedInUser = null;

        while (!exit) {
            // Display menu
            System.out.println("""
            ==================================
               BTO MANAGEMENT SYSTEM LOGIN   
            ==================================
            1. Login with your Singpass account
            2. Change Password
            3. Exit
            ==================================
            Enter your choice:  
            """);

            // Ensure valid integer input
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.next(); // Clear invalid input
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Logging in with your SingPass account...");

                    String nric;
                    do {
                        System.out.print("Enter userID: ");
                        nric = scanner.nextLine().trim();
                        if (!loginController.verifyNric(nric)) {
                            System.out.println("Invalid userID. Please try again.");
                        }
                    } while (!loginController.verifyNric(nric));

                    System.out.print("Enter password: ");
                    String password = scanner.nextLine().trim();

                    loggedInUser = loginController.login(nric, password);

                    if (loggedInUser != null) {
                        System.out.println("Login successful!\n");
                        return loggedInUser;
                    } else {
                        System.out.println("Invalid NRIC or password. Please try again.\n");
                    }
                    break;

                case 2:
                    System.out.println("Redirecting to Change Password...");
                    changePasswordUI.showChangePasswordMenu();
                    break;

                case 3:
                    System.out.println("Exiting BTO Management System...");
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid choice! Please enter a number between 1 and 3.");
            }

            System.out.println();
        }
        return null;
    }

    public void navigateToMainMenu(User user) {
        if (user == null) {
            return;
        }

        UserRole role = user.getUserRole();
        switch (role) {
            case APPLICANT:
                new ApplicantUI().showMenu();
                break;
            case HDB_OFFICER:
                new OfficerUI().showMenu();
                break;
            case HDB_MANAGER:
                new ManagerUI().showMenu();
                break;
            default:
                break;
        }
    }
}
