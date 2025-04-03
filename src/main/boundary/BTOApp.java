package main.boundary;
import main.controller.user.UserManager;
import main.controller.enquiry.EnquiryController;
import main.controller.project.ProjectController;

public class BTOApp {
    public static void start() {

        UserManager.load();
        ProjectController.load();

        //load Enquiries to the respective projects
        EnquiryController.load(ProjectController.getProjectList());

        LoginUI loginUI = new LoginUI();
        loginUI.startLogin();
    }
}