package main.boundary;
import main.controller.user.UserManager;
import main.controller.project.ProjectController;

public class BTOApp {
    public static void start() {

        UserManager.load();
        ProjectController.load();

        LoginUI loginUI = new LoginUI();
        loginUI.startLogin();
    }
}