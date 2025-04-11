package main.boundary;

import main.controller.enquiry.EnquiryController;
import main.controller.project.ProjectController;
import main.controller.user.UserManager;
import main.utils.FileIOUtil;
import main.controller.registration.RegistrationController;

public class BTOApp {
    public static void start() {

        UserManager.load();
        ProjectController.load();
        EnquiryController.load();
        RegistrationController.load();
        FileIOUtil.loadBookingDetails(ProjectController.getProjectList());
        
        LoginUI loginUI = new LoginUI();
        loginUI.startLogin();
    }
}