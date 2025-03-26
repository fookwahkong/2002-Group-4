package main.boundary;

import main.controller.user.UserManager;
import main.entity.user.User;
import main.enums.UserRole;

import java.util.Scanner;

public class LoginUI {
    private static final Scanner scanner = new Scanner(System.in);
    private final ChangePasswordUI changePasswordUI;

    public LoginUI() {
        this.changePasswordUI = new ChangePasswordUI();
    }

    public void startLogin() {
        User loggedInUser = showLoginMenu();

        if (loggedInUser != null) {
            navigateToMainMenu(loggedInUser);
        }
    }

    public User showLoginMenu() {
        boolean exit = false;
        User loggedInUser = null;

        while (!exit) {
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

            int choice = getValidIntInput();

            switch (choice) {
                case 1 -> loggedInUser = handleLogin();
                case 2 -> changePasswordUI.showChangePasswordMenu();
                case 3 -> {
                    System.out.println("Exiting BTO Management System...");
                    exit = true;
                }
                default -> System.out.println("Invalid choice! Please enter a number between 1 and 3.");
            }

            if (loggedInUser != null) {
                return loggedInUser;
            }

            System.out.println();
        }
        return null;
    }

    private User handleLogin() {
        System.out.println("Logging in with your SingPass account...");

        String nric;
        do {
            System.out.print("Enter userID: ");
            nric = scanner.nextLine().trim();
            if (!UserManager.verifyNRIC(nric)) {
                System.out.println("Invalid userID format. Try again.");
            }
        } while (!UserManager.verifyNRIC(nric));

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        User user = UserManager.getInstance().login(nric, password);

        if (user != null) {
            System.out.println("Login successful!\n");
            return user;
        } else {
            System.out.println("Invalid NRIC or password. Please try again.\n");
            return null;
        }
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

    public void navigateToMainMenu(User user) {
        if (user == null) return;

        switch (user.getUserRole()) {
            case APPLICANT -> new ApplicantUI().showMenu();
            case HDB_OFFICER -> new OfficerUI().showMenu();
            case HDB_MANAGER -> new ManagerUI().showMenu();
            default -> System.out.println("Unknown role. Cannot proceed.");
        }
    }
}
