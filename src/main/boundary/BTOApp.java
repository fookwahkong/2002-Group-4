package main.boundary;
import main.controller.user.UserManager;

public class BTOApp {
    public static void start() {

        UserManager.load();

        LoginUI loginUI = new LoginUI();
        loginUI.startLogin();
    }
}