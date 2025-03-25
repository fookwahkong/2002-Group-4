package main.boundary;

import main.controller.LoginController;
import main.entity.user.User;
import main.enums.UserRole;

import java.util.Scanner;

public class LoginUI {
    private static Scanner scanner = new Scanner(System.in);
    private LoginController loginController;

    public LoginUI() {
        loginController = new LoginController();
    }

    public User showLoginMenu() {
        User loggedInUser = null;
        boolean validLogin = false;

        while (!validLogin) {
            System.out.println("==================================");
            System.out.println("   BTO MANAGEMENT SYSTEM LOGIN   ");
            System.out.println("==================================");
            System.out.println("Please login with your Singpass account");

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

            loggedInUser = loginController.login(nric, password);

            if (loggedInUser != null) {
                validLogin = true;
                System.out.println("Login successful!");
            } else {
                System.out.println("Invalid userID or password. Please try again.\n");
            }
        }

        return loggedInUser;
    }
}