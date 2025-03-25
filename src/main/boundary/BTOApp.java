package main.boundary;


public class BTOApp {
    public static void start() {
        System.out.println("BTO Management System");
        LoginUI loginUI = new LoginUI();

        int loggedIn= loginUI.showLoginMenu();

        if (loggedIn != -1) {
            // change UI
            System.out.println("redirect user to their role specific UI");
        }
    }
}