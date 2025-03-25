package main.boundary;
import main.entity.user.User;

public class BTOApp {
    public static void start() {
        System.out.println("BTO Management System");
        LoginUI loginUI = new LoginUI();

        User loggedInUser = loginUI.showLoginMenu();

        if (loggedInUser != null) {
            loginUI.navigateToMainMenu(loggedInUser);
        }
    }
}