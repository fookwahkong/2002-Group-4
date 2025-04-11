package main.boundary;

import main.controller.enquiry.EnquiryController;
import main.controller.project.ProjectController;
import main.controller.user.UserManager;

public class BTOApp {
    public static void start() {

        UserManager.load();
        ProjectController.load();

        
        EnquiryController.load();

        LoginUI loginUI = new LoginUI();
        loginUI.startLogin();
    }
}