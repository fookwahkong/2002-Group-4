package main.boundary;
import main.controller.user.UserManager;
import main.controller.enquiry.EnquiryController;
import main.controller.project.ProjectController;

public class BTOApp {
    public static void start() {

        UserManager.load();
        ProjectController.load();
        EnquiryController.load();

        LoginUI loginUI = new LoginUI();
        loginUI.startLogin();
    }
}