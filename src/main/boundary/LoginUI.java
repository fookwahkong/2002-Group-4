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
        boolean validLogin = false;

        while (!validLogin) {
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
            int choice;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } else {
                System.out.println("Invalid input! Please enter a number.");
                scanner.next(); // Clear invalid input
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("Logging in with your SingPass account...");

            boolean validNric = false;
            String nric = "";
            while (!validNric) {
                System.out.print("Enter userID: ");
                nric = scanner.nextLine().trim();
                if (loginController.verifyNric(nric)) {
                    validNric = true;
                } else {
                    System.out.println("Invalid userID. Please try again.");
                }
            }

                    System.out.print("Enter password: ");
                    String password = scanner.nextLine().trim();

                    User user = loginController.login(nric, password);

                    if (user != null) {
                        validLogin = true;
                        System.out.println("Login successful!\n");
                        return user;
                    } else {
                        System.out.println("Invalid NRIC or password. Please try again.\n");
                    }
                    break;

                case 2:
                    System.out.println("Redirecting to Change Password...");
                    // Call Change Password method here
                    changePasswordUI.showChangePasswordMenu();
                    break;

                case 3:
                    System.out.println("Exiting BTO Management System...");
                    scanner.close();                       // Exit the program


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
