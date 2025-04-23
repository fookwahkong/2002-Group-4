package bto.boundary;

import bto.controller.user.UserManager;
import bto.entity.user.User;

/**
 * A class representing a login UI.
 */
public class LoginUI {

    /**
     * Start the login process.
     */
    public void startLogin() {
        User loggedInUser = displayLoginMenu();
        if (loggedInUser != null) {
            navigateToMainMenu(loggedInUser);
        }
    }

    /**
     * Display the login menu.
     * 
     * @return the logged in user
     */
    public User displayLoginMenu() {
        try{
            boolean exit = false;
            User loggedInUser = null;

            while (!exit) {
                System.out.println("Loading BTO Management System...");

                Thread.sleep(1000); // Simulate loading time
                String[] menuOptions = {
                        "==================================",
                        "BTO MANAGEMENT SYSTEM LOGIN",
                        "==================================",
                        "1. Login with your Singpass account",
                        "2. Exit",
                        "==================================",
                        "Enter your choice: " };

                UIUtils.displayMenuOptions(menuOptions);

                int choice = UIUtils.getValidIntInput(1, 2);

                switch (choice) {
                    case 1 -> loggedInUser = handleLogin();
                    case 2 -> {
                        System.out.println("Exiting BTO Management System...");
                        return null;
                    }
                    default -> System.out.println("Invalid choice! Please enter a number between 1 and 3.");
                }

                if (loggedInUser != null) {
                    return loggedInUser;
                }

                System.out.println();
            }
            return null;
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return null;
        }
    }

    /**
     * Handle the login process.
     * 
     * @return the logged in user
     */
    private User handleLogin() {
        System.out.println("Logging in with your SingPass account...");

        String nric;
        do {
            nric = UIUtils.getStringInput("Enter userID: ");
            if (!UserManager.verifyNRIC(nric)) {
                System.out.println("Invalid userID format. Try again.");
            }
        } while (!UserManager.verifyNRIC(nric));

        String password = UIUtils.getStringInput("Enter password: ");

        User user = UserManager.getInstance().login(nric, password);

        if (user != null) {
            System.out.println("Login successful!\n");

            return user;
        } else {
            System.out.println("Invalid NRIC or password. Please try again.\n");
            return null;
        }
    }

    /**
     * Navigate to the main menu.
     * 
     * @param user the logged in user
     */ 
    protected void navigateToMainMenu(User user) {
        if (user == null) {
            return;
        }

        try {
            switch (user.getUserRole()) {
                case APPLICANT:
                    System.out.println("Opening ApplicantUI...");
                    new ApplicantUI(user).showMenu();
                    break;
                case HDB_OFFICER:
                    System.out.println("Opening OfficerUI...");
                    new OfficerUI(user).showMenu();
                    break;
                case HDB_MANAGER:
                    System.out.println("Opening ManagerUI...");
                    new ManagerUI(user).showMenu();
                    break;
                default:
                    System.out.println("Unknown role. Cannot proceed.");
            }
        } catch (Exception e) {
            System.out.println("ERROR when navigating to UI: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
